package kundenverwaltung.controller.errorreport;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.server.dto.ErrorReportDTO;
import kundenverwaltung.server.service.ErrorReportService;
import kundenverwaltung.service.WindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ErrorReportNewController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorReportNewController.class);
    private final ErrorReportService ERROR_REPORT_SERVICE = new ErrorReportService();

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private Button screenshotBtn;
    @FXML private Label selectedFileLabel;

    private File selectedScreenshot;
    private ErrorReportDTO currentReport;

    /**
     * Initializes the new error report controller and sets up the screenshot selection functionality.
     */
    @FXML
    public void initialize()
    {
        currentReport = new ErrorReportDTO();

        screenshotBtn.setOnAction(event ->
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
    }

    /**
     * Handles sending the error report to the server after validation.
     */
    @FXML
    private void onSendReportToServerButton()
    {
        if (!isValid())
        {
            return;
        }
        String title = titleField.getText().isBlank()
                ? LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                : titleField.getText();

        currentReport.setTitle(title);
        currentReport.setDescription("Vom Nutzer erstellter Fehlerbericht");
        currentReport.setUserDescription(descriptionArea.getText());

        ((Stage) titleField.getScene().getWindow()).close();

        ERROR_REPORT_SERVICE.sendErrorReportToServerViaSubmit(currentReport, selectedScreenshot);
    }

    /**
     * Handles canceling the error report creation and closes the window.
     */
    @FXML
    private void handleCanceling()
    {
        ((Stage) titleField.getScene().getWindow()).close();
    }

    /**
     * Returns the current error report DTO.
     *
     * @return the current error report DTO
     */
    public ErrorReportDTO getCurrentReport()
    {
        return currentReport;
    }

    /**
     * Validates the input fields for the error report.
     *
     * @return true if all required fields are valid, false otherwise
     */
    private boolean isValid()
    {
        StringBuilder errorBuilder = new StringBuilder();

        if (titleField.getText().isBlank())
            errorBuilder.append("Der Titel darf nicht leer sein.\n");

        if (descriptionArea.getText().isBlank())
            errorBuilder.append("Die Beschreibung darf nicht leer sein.\n");

        if (!errorBuilder.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eingabefehler");
            alert.setHeaderText("Bitte Eingaben prüfen:");
            alert.setContentText(errorBuilder.toString());

            Platform.runLater(alert::showAndWait);
            return false;
        }

        return true;
    }
}
