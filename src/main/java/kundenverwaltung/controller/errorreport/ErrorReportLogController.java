package kundenverwaltung.controller.errorreport;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kundenverwaltung.logger.model.LogEntry;

public class ErrorReportLogController
{
    @FXML
    private TextField timestampField;

    @FXML
    private TextField levelField;

    @FXML
    private TextField sourceField;

    @FXML
    private TextArea messageArea;

    /**
     * Sets the log entry data to be displayed in the log detail window.
     *
     * @param logEntry the log entry to display
     */
    public void setLogEntry(LogEntry logEntry)
    {
        timestampField.setText(logEntry.getTimestamp());
        levelField.setText(logEntry.getLevel());
        sourceField.setText(logEntry.getSource());
        messageArea.setText(logEntry.getMessage());
    }

    /**
     * Closes the log detail window.
     */
    @FXML
    private void onClose()
    {
        Stage stage = (Stage) timestampField.getScene().getWindow();
        stage.close();
    }
}
