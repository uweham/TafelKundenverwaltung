package kundenverwaltung.controller.errorreport;

import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.server.dto.ErrorReportDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import kundenverwaltung.server.service.ErrorReportService;
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

public class ErrorReportList4MemberController
{
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<ErrorReportDTO> errorReportTable;
    @FXML
    private TableColumn<ErrorReportDTO, String> titleColumn;
    @FXML
    private TableColumn<ErrorReportDTO, String> descriptionColumn;
    @FXML
    private TableColumn<ErrorReportDTO, String> statusColumn;
    @FXML
    public TableColumn<ErrorReportDTO, LocalDateTime> createdAtColumn;
    @FXML
    private Button newButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button deleteButton;

    private ObservableList<ErrorReportDTO> masterData = FXCollections.observableArrayList();

    private final ErrorReportService ERROR_REPORT_SERVICE = new ErrorReportService();
    private final Logger LOGGER = LoggerFactory.getLogger(ErrorReportList4MemberController.class);

    /**
     * Initializes the error report list controller and sets up the table columns,
     * filtering functionality, and loads error reports from the server.
     *
     * @throws IOException if there is an error loading data from the server
     */
    @FXML
    public void initialize() throws IOException
    {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setId("title");

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("userDescription"));
        descriptionColumn.setId("userDescription");

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setId("status");

        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        createdAtColumn.setId("createdAt");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        //  Format local-date-time
        createdAtColumn.setCellFactory(column -> new TableCell<ErrorReportDTO, LocalDateTime>()
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

        //  Setup filtering/search-feature
        FilteredList<ErrorReportDTO> filteredData = new FilteredList<>(masterData, p -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            filteredData.setPredicate(errorReport ->
            {
                if (newValue == null || newValue.isEmpty())
                {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (errorReport.getTitle().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                else if (errorReport.getUserDescription().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                else return (errorReport.getDescription().toLowerCase().contains(lowerCaseFilter));
            });
        });

        SortedList<ErrorReportDTO> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(errorReportTable.comparatorProperty());
        errorReportTable.setItems(sortedData);

        //  Setup table-preferences...
        TablePreferenceServiceImpl.getInstance().setupPersistence(errorReportTable, "ErrorReportList4MemberController", true);

        loadErrorReportsFromServer();
    }

    /**
     * Loads error reports from the server for the current user.
     *
     * @throws IOException if there is an error connecting to the server
     */
    private void loadErrorReportsFromServer() throws IOException
    {
        masterData.clear();

        try
        {
            UUID uuid = UserEntityService.getInstance().getUserGeneratedAnonymizedUUID();
            List<ErrorReportDTO> errorReportDTOs = ERROR_REPORT_SERVICE.getAllErrorReportsForGivenUserUUID(uuid);
            masterData.setAll(errorReportDTOs);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());

            boolean showAndWait = true;
            WindowService.openWindow("/kundenverwaltung/fxml/server/ServerConnectionError.fxml", "Tafel-Server", new Stage(), StageStyle.DECORATED, Modality.APPLICATION_MODAL, showAndWait);

            Platform.runLater(() ->
            {
                Stage currentStage = (Stage) errorReportTable.getScene().getWindow();
                currentStage.close();
            });
        }
    }

    /**
     * Handles the deletion of a selected error report after user confirmation.
     *
     * @throws Exception if there is an error during the deletion process
     */
    @FXML
    private void onBtnClickDeleteErrorReport() throws Exception
    {
        ErrorReportDTO selectedErrorReport = errorReportTable.getSelectionModel().getSelectedItem();

        if (selectedErrorReport != null)
        {
            String errorReportTitle = selectedErrorReport.getTitle();
            if(errorReportTitle == null)
            {
                errorReportTitle = "(Kein Titel)";
            }

            String errorReportTitleShorten = ( errorReportTitle.length() > 20 ) ? errorReportTitle.substring(0, 20) + "..." : errorReportTitle;
            String errorDisplayInfo = "Ausgewählten Fehlerbericht mit dem Titel: '" + errorReportTitleShorten + "' löschen?";

            if(!Benachrichtigung.deleteConfirmationDialog("Den gemeldeten Fehlerbericht wirklich löschen?", errorDisplayInfo))
            {
                return;
            }

            try
            {
                ERROR_REPORT_SERVICE.deleteErrorReportForAnonymizedUser(selectedErrorReport);
            }
            catch (Exception exception)
            {
                LOGGER.error(exception.getMessage());
                Benachrichtigung.errorDialog("Tafel-Server", "Löschen des Fehlerberichts", "Beim Löschen des Fehlerberichts ist ein Fehler aufgetreten!");
                return;
            }

            Benachrichtigung.infoBenachrichtigung("Tafel-Server", "Der Fehlerbericht wurde erfolgreich gelöscht!");
            loadErrorReportsFromServer();
        }
        else
        {
            Benachrichtigung.warnungBenachrichtigung("Keine Auswahl", "Bitte wähle einen zu löschenden Fehlerbericht aus!");
        }
    }

    /**
     * Opens a new error report creation window.
     *
     * @throws IOException if there is an error opening the new error report window
     */
    @FXML
    private void onBtnClickNewErrorReport() throws IOException
    {
        boolean showAndWaitForWindow = true;
        WindowService.openWindow("/kundenverwaltung/fxml/errorreport/ErrorReportNew.fxml", "Neuer Fehlerbericht", showAndWaitForWindow);
        loadErrorReportsFromServer();
    }

    /**
     * Refreshes the error report list by reloading data from the server.
     *
     * @throws IOException if there is an error loading data from the server
     */
    @FXML
    private void onBtnClickRefreshList() throws IOException
    {
        loadErrorReportsFromServer();
    }

    /**
     * Closes the current window.
     */
    private void closeWindow()
    {

    }
}
