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
import kundenverwaltung.logger.event.GlobalEventLogger;
import kundenverwaltung.server.dto.ErrorReportDTO;
import kundenverwaltung.server.service.AuthService;
import kundenverwaltung.server.service.ErrorReportService;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.service.WindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ErrorReportListController
{
    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<ErrorReportDTO> errorReportTable;

    @FXML
    private TableColumn<ErrorReportDTO, String> uuidColumn;

    @FXML
    private TableColumn<ErrorReportDTO, String> titleColumn;

    @FXML
    private TableColumn<ErrorReportDTO, String> descriptionColumn;

    @FXML
    private TableColumn<ErrorReportDTO, String> statusColumn;

    @FXML
    public TableColumn<ErrorReportDTO, LocalDateTime> createdAtColumn;

    @FXML
    private TableColumn<ErrorReportDTO, String> usernameColumn;

    @FXML
    public Button deleteButton;

    private final ObservableList<ErrorReportDTO> errorReportList = FXCollections.observableArrayList();

    private final ErrorReportService ERROR_REPORT_SERVICE = new ErrorReportService();

    private final Logger LOGGER = LoggerFactory.getLogger(ErrorReportListController.class);

    /**
     * Initializes the controller and sets up the table view for error reports.
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

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("userDescription"));
        descriptionColumn.setId("userDescription");

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


        FilteredList<ErrorReportDTO> filteredList = new FilteredList<>(errorReportList, p -> true);

        searchTextField.textProperty().addListener((obs, oldText, newText) ->
        {
            filteredList.setPredicate(report ->
            {
                if (newText == null || newText.isBlank())
                {
                    return true;
                }

                String lower = newText.toLowerCase();

                return report.getTitle().toLowerCase().contains(lower)
                        || report.getUuid().toString().toLowerCase().contains(lower);
            });
        });

        SortedList<ErrorReportDTO> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(errorReportTable.comparatorProperty());
        errorReportTable.setItems(sortedList);

        //  Setup table-preferences...
        TablePreferenceServiceImpl.getInstance().setupPersistence(errorReportTable, "ErrorReportListTable", true);

        // Loading reports from server...
        loadErrorReportsFromServer();

        if (!AuthService.getInstance().isAdmin())
        {
            deleteButton.setVisible(false);
        }
    }

    /**
     * Handles editing a selected error report.
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void handleEdit() throws IOException
    {
        ErrorReportDTO selected = errorReportTable.getSelectionModel().getSelectedItem();

        if (selected != null)
        {
            ErrorReportDTO edited = openErrorReportEditDialog(selected);

            if (edited != null)
            {
                int index = errorReportList.indexOf(selected);

                if (index != -1)
                {
                    errorReportList.set(index, edited);
                }
            }

            loadErrorReportsFromServer();
        }
        else
        {
            Benachrichtigung.warnungBenachrichtigung("Keine Auswahl", "Bitte wähle einen zu bearbeitenden Fehlerbericht aus!");
        }
    }

    /**
     * Handles deleting a selected error report.
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void handleDeleteError() throws IOException
    {
        ErrorReportDTO selectedErrorReport = errorReportTable.getSelectionModel().getSelectedItem();

        if (selectedErrorReport != null)
        {
            String errorDisplayInfo = "Ausgewählten Fehlerbericht mit der UUID: '" + selectedErrorReport.getUuid() + "' löschen?";

            if(!Benachrichtigung.deleteConfirmationDialog("Den gemeldeten Fehlerbericht wirklich löschen?", errorDisplayInfo))
            {
                return;
            }

            try
            {
                ERROR_REPORT_SERVICE.delete(selectedErrorReport.getUuid());
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
     * Handles closing the error report list window.
     */
    @FXML
    private void handleClose()
    {
        Stage stage = (Stage) errorReportTable.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles double-click editing of error reports.
     * @param event the mouse event
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void handleErrorEdit(MouseEvent event) throws IOException
    {
        if (event.getClickCount() == 2 && errorReportTable.getSelectionModel().getSelectedItem() != null)
        {
            handleEdit();
        }
    }

    /**
     * Opens the error report edit dialog for the specified report.
     * @param report the error report to edit
     * @return the edited error report or null if cancelled
     */
    private ErrorReportDTO openErrorReportEditDialog(ErrorReportDTO report)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(ErrorReportListController.class.getResource("/kundenverwaltung/fxml/server/ErrorReportEdit.fxml"));
            Parent parent = fxmlLoader.load();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();

            GlobalEventLogger.attachTo("ErrorReportEdit", scene);

            stage.setTitle("Fehlerbericht bearbeiten");
            stage.setScene(scene);
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            ErrorReportEditController controller = fxmlLoader.getController();
            controller.setErrorReport(report);

            stage.showAndWait();

            if (controller.hasBeenSaved())
            {
                Benachrichtigung.infoBenachrichtigung("Tafel-Server", "Änderungen wurden erfolgreich gespeichert!");
                return controller.getCurrentReport();
            }

            return null;
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage());
            Benachrichtigung.errorDialog("Fehler", "Es ist ein Fehler beim öffnen aufgetreten!", e.getMessage());
            return null;
        }
    }

    /**
     * Loads error reports from the server.
     * @throws IOException if an I/O error occurs
     */
    private void loadErrorReportsFromServer() throws IOException
    {
        errorReportList.clear();

        try
        {
            // Loading reports from server...
            List<ErrorReportDTO> errorReports = ERROR_REPORT_SERVICE.getAll(ErrorReportDTO.class);
            errorReportList.setAll(errorReports);
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
}