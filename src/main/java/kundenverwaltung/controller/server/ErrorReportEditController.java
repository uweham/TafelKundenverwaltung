package kundenverwaltung.controller.server;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.controller.errorreport.ErrorReportLogController;
import kundenverwaltung.controller.errorreport.ErrorReportSubmitController;
import kundenverwaltung.logger.file.LogReader;
import kundenverwaltung.logger.model.LogEntry;
import kundenverwaltung.server.dto.ErrorReportDTO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kundenverwaltung.server.dto.ErrorReportLogDTO;
import kundenverwaltung.server.dto.ScreenshotDTO;
import kundenverwaltung.server.dto.UserEntityDTO;
import kundenverwaltung.server.service.ErrorReportService;
import kundenverwaltung.server.service.UserEntityService;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.toolsandworkarounds.FileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ErrorReportEditController
{
    private final ErrorReportService ERROR_REPORT_SERVICE = new ErrorReportService();

    private final Logger LOGGER = LoggerFactory.getLogger(ErrorReportEditController.class);

    @FXML
    private TextField titleField;

    @FXML
    private ComboBox<ErrorReportDTO.Status> statusComboBox;

    @FXML
    private ComboBox<String> userNameComboBox;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextArea userDescriptionArea;

    @FXML
    public TextArea internNotesArea;

    @FXML
    private TableView<ErrorReportLogDTO> tableViewLogs;

    @FXML
    private TableColumn<ErrorReportLogDTO, String> colTimestamp;

    @FXML
    private TableColumn<ErrorReportLogDTO, String> colLevel;

    @FXML
    private TableColumn<ErrorReportLogDTO, String> colSource;

    @FXML
    private TableColumn<ErrorReportLogDTO, String> colMessage;

    @FXML
    public Button btnOpenFile;

    private ErrorReportDTO currentReport;

    private boolean savedState = false;

    /**
     * Initializes the controller and sets up the UI components.
     */
    @FXML
    public void initialize()
    {
        List<UserEntityDTO> userEntityList = UserEntityService.getInstance().getCachedUserEntityList();

        String[] userNames = userEntityList.stream()
                .map(UserEntityDTO::getUsername)
                .toArray(String[]::new);

        statusComboBox.getItems().addAll(ErrorReportDTO.Status.values());
        userNameComboBox.getItems().addAll(userNames);

        colTimestamp.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTimestamp()));
        colTimestamp.setId("timestamp");

        colLevel.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLevel()));
        colLevel.setId("level");

        colSource.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSource()));
        colSource.setId("source");

        colMessage.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getMessage()));
        colMessage.setId("message");

        TablePreferenceServiceImpl.getInstance().setupPersistence(tableViewLogs, "ErrorReportEditLogTable");

        tableViewLogs.setOnMouseClicked(event ->
        {
            if (event.getClickCount() == 2)
            {
                ErrorReportLogDTO selectedErrorLog = tableViewLogs.getSelectionModel().getSelectedItem();

                if(selectedErrorLog != null)
                {
                    LogEntry logEntry = LogEntry.fromDTO(selectedErrorLog);
                    openLogDetailWindow(logEntry);
                }
            }
        });
    }

    /**
     * Sets the error report data to be edited.
     * @param report the error report DTO to edit
     */
    public void setErrorReport(ErrorReportDTO report)
    {
        currentReport = ErrorReportDTO.builder()
                .setUuid(report.getUuid())
                .setTitle(report.getTitle())
                .setStatus(report.getStatus())
                .setUserUUID(report.getUserUUID())
                .setDescription(report.getDescription())
                .setUserDescription(report.getUserDescription())
                .setInternNotes(report.getInternNotes())
                .setScreenshot(report.getScreenshot())
                .setUser(report.getUser())
                .build();

        titleField.setText(currentReport.getTitle());
        statusComboBox.setValue(currentReport.getStatus());

        String userName = ( currentReport.getUser() != null && currentReport.getUser().getUsername() != null ) ? currentReport.getUser().getUsername() : "";
        userNameComboBox.setValue(userName);

        descriptionArea.setText(currentReport.getDescription());
        userDescriptionArea.setText(currentReport.getUserDescription());
        internNotesArea.setText(currentReport.getInternNotes());

        //  Log entries...
        List<ErrorReportLogDTO> logEntries = report.getLogs();

        logEntries.sort(Comparator.comparing(ErrorReportLogDTO::getCreatedAt).reversed());

        List<ErrorReportLogDTO> last10Entries = logEntries.stream()
                .limit(10)
                .toList();

        ObservableList<ErrorReportLogDTO> observableLogs = FXCollections.observableArrayList(last10Entries);
        tableViewLogs.setItems(observableLogs);

        //  File entry...
        if(currentReport.getScreenshot() == null || currentReport.getScreenshot().getScreenshot() == null)
        {
            btnOpenFile.setVisible(false);
            currentReport.setScreenshot(null);
        }
    }

    /**
     * Handles the save action for the error report.
     */
    @FXML
    private void onSave()
    {
        UserEntityDTO userEntityDTO = currentReport.getUser();

        Optional<UserEntityDTO> optionalUser = UserEntityService.getInstance()
                .getCachedUserEntityList()
                .stream()
                .filter(entry -> entry.getUsername().equals(userNameComboBox.getValue()))
                .findFirst();

        if (optionalUser.isPresent())
        {
            userEntityDTO = optionalUser.get();
        }

        currentReport.setUser(userEntityDTO);
        currentReport.setInternNotes(internNotesArea.getText());
        currentReport.setStatus(statusComboBox.getValue());

        try
        {
            ERROR_REPORT_SERVICE.update(currentReport, ErrorReportDTO.class);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            Benachrichtigung.errorDialog("Tafel-Server", "Fehler beim Speichern!", "Es ist ein Fehler beim Speichern des Fehlerberichts aufgetreten!");
            return;
        }

        savedState = true;
        ((Stage) titleField.getScene().getWindow()).close();
    }

    /**
     * Handles the cancel action for the error report.
     */
    @FXML
    private void onCancel()
    {
        savedState = false;
        ((Stage) titleField.getScene().getWindow()).close();
    }

    /**
     * Returns whether the error report has been saved.
     * @return true if the error report has been saved, false otherwise
     */
    public boolean hasBeenSaved()
    {
        return savedState;
    }

    /**
     * Returns the current error report being edited.
     * @return the current error report DTO
     */
    public ErrorReportDTO getCurrentReport()
    {
        return currentReport;
    }

    /**
     * Opens a detailed log window for the specified log entry.
     * @param logEntry the log entry to display
     */
    private void openLogDetailWindow(LogEntry logEntry)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(ErrorReportSubmitController.class.getResource("/kundenverwaltung/fxml/errorreport/ErrorReportLog.fxml"));
            Parent root = loader.load();

            ErrorReportLogController controller = loader.getController();
            controller.setLogEntry(logEntry);

            Stage stage = new Stage();
            stage.setTitle("Fehlerbericht - Logeintrag");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Handles opening the attached file for the error report.
     * @param actionEvent the action event
     */
    @FXML
    public void onOpenFile(ActionEvent actionEvent)
    {
        ErrorReportDTO errorReportDTO = getCurrentReport();

        if(errorReportDTO.getScreenshot() == null)
        {
            return;
        }

        ScreenshotDTO screenshotDTO = errorReportDTO.getScreenshot();

        byte[] data = screenshotDTO.getScreenshot();
        ScreenshotDTO.FileType fileType = screenshotDTO.getFileType();

        String extension = fileType.name().toLowerCase();

        //  Opens given file...
        FileHandler.openFile(data, "screenshot_", extension);
    }
}
