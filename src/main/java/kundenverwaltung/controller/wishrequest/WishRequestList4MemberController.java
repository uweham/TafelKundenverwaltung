package kundenverwaltung.controller.wishrequest;

import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.server.dto.WishRequestDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import kundenverwaltung.server.service.WishRequestService;
import kundenverwaltung.server.service.UserEntityService;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.service.WindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class WishRequestList4MemberController
{
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<WishRequestDTO> wishRequestTable;
    @FXML
    private TableColumn<WishRequestDTO, String> titleColumn;
    @FXML
    private TableColumn<WishRequestDTO, String> descriptionColumn;
    @FXML
    private TableColumn<WishRequestDTO, String> statusColumn;
    @FXML
    public TableColumn<WishRequestDTO, LocalDateTime> createdAtColumn;
    @FXML
    private Button newButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button deleteButton;

    private ObservableList<WishRequestDTO> masterData = FXCollections.observableArrayList();

    private final WishRequestService WISH_REQUEST_SERVICE = new WishRequestService();
    private final Logger LOGGER = LoggerFactory.getLogger(WishRequestList4MemberController.class);

    /**
     * Initializes the controller. This method is called automatically after the FXML file has been loaded.
     * It sets up the table columns, search functionality, and loads the initial data.
     *
     * @throws IOException if an error occurs during loading of server data.
     */
    @FXML
    public void initialize() throws IOException
    {
        // Setup table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setId("title");

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setId("description");

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setId("status");

        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        createdAtColumn.setId("createdAt");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        //  Format local-date-time
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

        // Setup filtering/search feature
        FilteredList<WishRequestDTO> filteredData = new FilteredList<>(masterData, p -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            filteredData.setPredicate(wishRequest ->
            {
                if (newValue == null || newValue.isEmpty())
                {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (wishRequest.getTitle().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                else if (wishRequest.getDescription().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                return false;
            });
        });

        SortedList<WishRequestDTO> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(wishRequestTable.comparatorProperty());
        wishRequestTable.setItems(sortedData);

        // Setup table preferences (if needed)
        TablePreferenceServiceImpl.getInstance().setupPersistence(wishRequestTable, "WishRequestList4MemberController", true);

        // Load wish requests from the server
        loadWishRequestsFromServer();
    }

    /**
     * Loads the wish requests for the current user from the server and populates the table.
     * Handles server connection errors by showing an error dialog.
     *
     * @throws IOException if an I/O error occurs.
     */
    private void loadWishRequestsFromServer() throws IOException
    {
        masterData.clear();
        try
        {
            UUID uuid = UserEntityService.getInstance().getUserGeneratedAnonymizedUUID();
            List<WishRequestDTO> wishRequestDTOs = WISH_REQUEST_SERVICE.getAllWishRequestsForGivenUserUUID(uuid);
            masterData.setAll(wishRequestDTOs);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());

            boolean showAndWait = true;
            WindowService.openWindow("/kundenverwaltung/fxml/server/ServerConnectionError.fxml", "Server Error", new Stage(), StageStyle.DECORATED, Modality.APPLICATION_MODAL, showAndWait);

            Platform.runLater(() ->
            {
                Stage currentStage = (Stage) wishRequestTable.getScene().getWindow();
                currentStage.close();
            });
        }
    }


    /**
     * Handles the click event for the delete button. Deletes the selected wish request after confirmation.
     *
     * @throws Exception if an error occurs during deletion.
     */
    @FXML
    private void onBtnClickDeleteWishRequest() throws Exception
    {
        WishRequestDTO selectedWishRequestDTO = wishRequestTable.getSelectionModel().getSelectedItem();

        if (selectedWishRequestDTO != null)
        {
            String wishRequestTitle = selectedWishRequestDTO.getTitle();
            if(wishRequestTitle == null)
            {
                wishRequestTitle = "(Kein Titel)";
            }

            String wishRequestTitleShorten = ( wishRequestTitle.length() > 20 ) ? wishRequestTitle.substring(0, 20) + "..." : wishRequestTitle;
            String wishRequestDisplayInfo = "Ausgewählte Wunschanfrage mit dem Titel: '" + wishRequestTitleShorten + "' löschen?";

            if(!Benachrichtigung.deleteConfirmationDialog("Die gemeldete Wunschanfrage wirklich löschen?", wishRequestDisplayInfo))
            {
                return;
            }

            try
            {
                WISH_REQUEST_SERVICE.deleteWishRequestForAnonymizedUser(selectedWishRequestDTO);
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
     * Handles the click event for the "New" button. Opens a new window to submit a wish request.
     *
     * @throws Exception if an error occurs opening the new window.
     */
    @FXML
    private void onBtnClickNewWishRequest() throws Exception
    {
        boolean showAndWaitForWindow = true;
        WindowService.openWindow("/kundenverwaltung/fxml/wishrequest/WishRequestSubmit.fxml", "New Wish Request", showAndWaitForWindow);
        loadWishRequestsFromServer();
    }

    /**
     * Handles the click event for the "Refresh" button. Reloads the list of wish requests from the server.
     *
     * @throws IOException if an I/O error occurs.
     */
    @FXML
    private void onBtnClickRefreshList() throws IOException
    {
        loadWishRequestsFromServer();
    }
}