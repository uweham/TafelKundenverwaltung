package kundenverwaltung.controller.server;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.server.dto.WishRequestDTO;
import kundenverwaltung.server.service.AuthService;
import kundenverwaltung.server.service.WishRequestService;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.service.WindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WishRequestListController
{
    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<WishRequestDTO> wishRequestTable;

    @FXML
    private TableColumn<WishRequestDTO, String> uuidColumn;

    @FXML
    private TableColumn<WishRequestDTO, String> titleColumn;

    @FXML
    private TableColumn<WishRequestDTO, String> descriptionColumn;

    @FXML
    private TableColumn<WishRequestDTO, String> statusColumn;

    @FXML
    public TableColumn<WishRequestDTO, LocalDateTime> createdAtColumn;

    @FXML
    private TableColumn<WishRequestDTO, String> usernameColumn;

    @FXML
    private Button deleteButton;

    private final ObservableList<WishRequestDTO> wishRequestList = FXCollections.observableArrayList();

    private final WishRequestService WISH_REQUEST_SERVICE = new WishRequestService();

    private final Logger LOGGER = LoggerFactory.getLogger(WishRequestListController.class);

    /**
     * Initializes the wish request list controller and sets up the table view.
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void initialize() throws IOException
    {
        uuidColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getUuid() != null
                        ? cellData.getValue().getUuid().toString()
                        : ""));
        uuidColumn.setId("uuid");

        titleColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        titleColumn.setId("title");

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setId("description");

        usernameColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getUser() != null
                        ? cellData.getValue().getUser().getUsername()
                        : ""));
        usernameColumn.setId("username");

        statusColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getStatus().toString()));
        statusColumn.setId("status");

        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        createdAtColumn.setId("createdAt");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        // Format local-date-time
        createdAtColumn.setCellFactory(column -> new TableCell<WishRequestDTO, LocalDateTime>()
        {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty || item == null)
                {
                    setText(null);
                }
                else
                {
                    setText(item.format(formatter));
                }
            }
        });

        FilteredList<WishRequestDTO> filteredList = new FilteredList<>(wishRequestList, p -> true);

        searchTextField.textProperty().addListener((obs, oldText, newText) ->
        {
            filteredList.setPredicate(wish ->
            {
                if (newText == null || newText.isBlank())
                {
                    return true;
                }

                String lower = newText.toLowerCase();

                return wish.getTitle().toLowerCase().contains(lower)
                        || wish.getUuid().toString().toLowerCase().contains(lower);
            });
        });

        SortedList<WishRequestDTO> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(wishRequestTable.comparatorProperty());
        wishRequestTable.setItems(sortedList);

        // Setup table-preferences...
        TablePreferenceServiceImpl.getInstance().setupPersistence(wishRequestTable, "WishRequestListTable", true);

        // Loading requests from server...
        loadWishRequestsFromServer();

        if (!AuthService.getInstance().isAdmin())
        {
            deleteButton.setVisible(false);
        }
    }

    /**
     * Handles editing a selected wish request.
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void handleEdit() throws IOException
    {
        WishRequestDTO selected = wishRequestTable.getSelectionModel().getSelectedItem();

        if (selected != null)
        {
            WishRequestDTO edited = openWishRequestEditDialog(selected);

            if (edited != null)
            {
                int index = wishRequestList.indexOf(selected);

                if (index != -1)
                {
                    wishRequestList.set(index, edited);
                }
            }

            loadWishRequestsFromServer();
        }
        else
        {
            Benachrichtigung.warnungBenachrichtigung("Keine Auswahl", "Bitte wähle eine zu bearbeitende Wunschanfrage aus!");
        }
    }

    /**
     * Handles deleting a selected wish request.
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void handleDeleteWishRequest() throws IOException
    {
        WishRequestDTO selectedRequest = wishRequestTable.getSelectionModel().getSelectedItem();

        if (selectedRequest != null)
        {
            String wishDisplayInfo = "Ausgewählte Wunschanfrage mit der UUID: '" + selectedRequest.getUuid() + "' löschen?";

            if(!Benachrichtigung.deleteConfirmationDialog("Die Wunschanfrage wirklich löschen?", wishDisplayInfo))
            {
                return;
            }

            try
            {
                WISH_REQUEST_SERVICE.delete(selectedRequest.getUuid());
            }
            catch (Exception exception)
            {
                LOGGER.error(exception.getMessage());
                Benachrichtigung.errorDialog("Tafel-Server", "Löschen der Wunschanfrage", "Beim Löschen der Wunschanfrage ist ein Fehler aufgetreten!");
                return;
            }

            Benachrichtigung.infoBenachrichtigung("Tafel-Server", "Die Wunschanfrage wurde erfolgreich gelöscht!");
            loadWishRequestsFromServer();
        }
        else
        {
            Benachrichtigung.warnungBenachrichtigung("Keine Auswahl", "Bitte wähle eine zu löschende Wunschanfrage aus!");
        }
    }

    /**
     * Handles closing the wish request list window.
     */
    @FXML
    private void handleClose()
    {
        Stage stage = (Stage) wishRequestTable.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles double-click editing of wish requests.
     * @param event the mouse event
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void handleWishRequestEdit(MouseEvent event) throws IOException
    {
        if (event.getClickCount() == 2 && wishRequestTable.getSelectionModel().getSelectedItem() != null)
        {
            handleEdit();
        }
    }

    /**
     * Opens the wish request edit dialog for the specified wish request.
     * @param wish the wish request to edit
     * @return the edited wish request or null if cancelled
     */
    private WishRequestDTO openWishRequestEditDialog(WishRequestDTO wish)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(WishRequestListController.class.getResource("/kundenverwaltung/fxml/server/WishRequestEdit.fxml"));
            Parent parent = fxmlLoader.load();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();

            stage.setTitle("Wunschanfrage bearbeiten");
            stage.setScene(scene);
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            WishRequestEditController controller = fxmlLoader.getController();
            controller.setWishRequest(wish);

            stage.showAndWait();

            if (controller.hasBeenSaved())
            {
                Benachrichtigung.infoBenachrichtigung("Tafel-Server", "Änderungen wurden erfolgreich gespeichert!");
                return controller.getCurrentWishRequest();
            }

            return null;
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage());
            Benachrichtigung.errorDialog("Fehler", "Es ist ein Fehler beim Öffnen aufgetreten!", e.getMessage());
            return null;
        }
    }

    /**
     * Loads wish requests from the server.
     * @throws IOException if an I/O error occurs
     */
    private void loadWishRequestsFromServer() throws IOException
    {
        wishRequestList.clear();

        try
        {
            // Loading requests from server...
            List<WishRequestDTO> wishRequests = WISH_REQUEST_SERVICE.getAll(WishRequestDTO.class);
            wishRequestList.setAll(wishRequests);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());

            boolean showAndWait = true;
            WindowService.openWindow("/kundenverwaltung/fxml/server/ServerConnectionError.fxml", "Tafel-Server", new Stage(), StageStyle.DECORATED, Modality.APPLICATION_MODAL, showAndWait);

            Platform.runLater(() ->
            {
                Stage currentStage = (Stage) wishRequestTable.getScene().getWindow();
                currentStage.close();
            });
        }
    }
}
