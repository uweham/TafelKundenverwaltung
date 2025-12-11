package kundenverwaltung.controller.errorreport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.logger.file.LogReader;
import kundenverwaltung.logger.model.LogEntry;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kundenverwaltung.server.dto.ErrorReportDTO;
import kundenverwaltung.server.dto.ErrorReportLogDTO;
import kundenverwaltung.server.service.ErrorReportService;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.service.WindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Timer;

public class ErrorReportSubmitController
{
    private final Logger LOGGER = LoggerFactory.getLogger(ErrorReportSubmitController.class);
    private final ErrorReportService ERROR_REPORT_SERVICE = new ErrorReportService();

    @FXML
    private TextArea userDescriptionTextArea;

    @FXML
    private Button screenshotButton;

    @FXML
    private Label selectedFileLabel;

    @FXML
    private Label hintTitle;

    @FXML
    private Label hintText;

    @FXML
    private Button sendReportToServerButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button screenshotCaptureButton;

    @FXML private TableView<LogEntry> tableViewLogs;
    @FXML private TableColumn<LogEntry, String> colTimestamp;
    @FXML private TableColumn<LogEntry, String> colLevel;
    @FXML private TableColumn<LogEntry, String> colSource;
    @FXML private TableColumn<LogEntry, String> colMessage;


    private File selectedScreenshot;
    private String errorMessage;
    private boolean userCustomError = false;

    /**
     * Initializes the error report submit controller, loads log entries,
     * and sets up event handlers for buttons and table interactions.
     */
    @FXML
    public void initialize()
    {
        String latestLog = kundenverwaltung.logger.file.LogFileService.getAvailableLogFiles().stream().findFirst().orElse(null);

        if (latestLog != null)
        {
            String path = kundenverwaltung.logger.file.LogFileService.getLogDirectoryPath()
                    + File.separator + latestLog;

            try
            {
                List<LogEntry> logEntries = LogReader.readLogsFromFile(path);

                logEntries.sort(Comparator.comparing(LogEntry::getTimestamp).reversed());

                List<LogEntry> last10Entries = logEntries.stream()
                        .limit(10)
                        .toList();

                ObservableList<LogEntry> observableLogs = FXCollections.observableArrayList(last10Entries);

                tableViewLogs.setItems(observableLogs);

                colTimestamp.setCellValueFactory(cellData -> cellData.getValue().timestampProperty());
                colTimestamp.setId("timestamp");

                colLevel.setCellValueFactory(cellData -> cellData.getValue().levelProperty());
                colLevel.setId("level");

                colSource.setCellValueFactory(cellData -> cellData.getValue().sourceProperty());
                colSource.setId("source");

                colMessage.setCellValueFactory(cellData -> cellData.getValue().messageProperty());
                colMessage.setId("message");

                TablePreferenceServiceImpl.getInstance().setupPersistence(tableViewLogs, "TableViewLogsErrorReport");
            }
            catch (Exception exception)
            {
                LOGGER.error("There was an error while retrieving data from the .LOG-file!", exception);
            }
        }

        screenshotButton.setOnAction(event ->
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Screenshot auswählen");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Bilddateien", "*.png", "*.jpg", "*.jpeg")
            );

            File file = fileChooser.showOpenDialog(null);

            if (file != null)
            {
                selectedScreenshot = file;
                selectedFileLabel.setText(file.getName());
            }
        });

        sendReportToServerButton.setOnAction(event ->
        {
            try
            {
                onSendReportToServerButton();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        cancelButton.setOnAction(event -> onCancelButton());
        screenshotCaptureButton.setOnAction(event -> onCreateScreenshotButton());

        tableViewLogs.setOnMouseClicked(event ->
        {
            if (event.getClickCount() == 2)
            {
                LogEntry selectedLog = tableViewLogs.getSelectionModel().getSelectedItem();

                if (selectedLog != null)
                {
                    openLogDetailWindow(selectedLog);
                }
            }
        });
    }

    /**
     * Post-initialization method that configures the UI based on whether
     * this is a user custom error or system error.
     */
    public void postInitialize()
    {
        if (isUserCustomError())
        {
            hintTitle.setText("Fehler gefunden?");
            hintText.setText("Bitte beschreiben Sie hier einen gefunden Fehler so präzise wie möglich.");
        }
    }

    /**
     * Handles sending the error report to the server with the provided data.
     *
     * @throws Exception if there is an error sending the report to the server
     */
    private void onSendReportToServerButton() throws Exception
    {
        ErrorReportDTO reportDTO = new ErrorReportDTO();
        reportDTO.setDescription(getErrorMessage());

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedDateTime = currentTime.format(formatter);

        reportDTO.setTitle(formattedDateTime + " " + getErrorMessage());
        reportDTO.setUserDescription(userDescriptionTextArea.getText());

        Stage stage = (Stage) userDescriptionTextArea.getScene().getWindow();
        stage.close();

        ERROR_REPORT_SERVICE.sendErrorReportToServerViaSubmit(reportDTO, selectedScreenshot);
    }

    /**
     * Sets the error message for the error report.
     *
     * @param errorMessage the error message to set
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    /**
     * Sets whether this is a user custom error or system error.
     *
     * @param bool true if this is a user custom error, false if it's a system error
     */
    public void setUserCustomError(boolean bool)
    {
        this.userCustomError = bool;
    }

    /**
     * Returns whether this is a user custom error or system error.
     *
     * @return true if this is a user custom error, false if it's a system error
     */
    public boolean isUserCustomError()
    {
        return this.userCustomError;
    }

    /**
     * Returns the error message for the error report.
     *
     * @return the error message
     */
    public String getErrorMessage()
    {
        return this.errorMessage;
    }

    /**
     * Handles canceling the error report submission and closes the window.
     */
    private void onCancelButton()
    {
        userDescriptionTextArea.clear();
        selectedScreenshot = null;
        selectedFileLabel.setText("Keine Datei ausgewählt");

        Stage stage = (Stage) userDescriptionTextArea.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles creating a screenshot by hiding windows, taking a screenshot,
     * and then showing the windows again.
     */
    private void onCreateScreenshotButton()
    {
        Stage currentStage = (Stage) userDescriptionTextArea.getScene().getWindow();

        currentStage.hide();
        kundenverwaltung.service.AnonymizeService.getInstance().anonymizeAllOpenWindows(true);

        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                File file = kundenverwaltung.service.ScreenshotService.createScreenshot();

                Platform.runLater(() ->
                {
                    selectedScreenshot = file;
                    selectedFileLabel.setText(file.getName());
                    kundenverwaltung.service.AnonymizeService.getInstance().anonymizeAllOpenWindows(false);

                    // Show the ErrorReport window again
                    currentStage.show();
                });
            }
        }, 200);
    }

    /**
     * Opens a detailed log entry window for the selected log entry.
     *
     * @param logEntry the log entry to display in detail
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
}
