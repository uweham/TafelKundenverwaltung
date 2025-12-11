package kundenverwaltung.controller.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSynchronizationNotificationController
{
    @FXML
    private Text syncInfoText;

    @FXML
    private Button okButton;

    /**
     * Handles the OK button action to close the synchronization notification.
     * @param event the action event
     */
    @FXML
    private void handleOk(ActionEvent event)
    {
        Stage stage = (Stage) okButton.getScene().getWindow();

        if (stage != null)
        {
            stage.close();
        }
    }

    /**
     * Sets the synchronization information to display.
     * @param errorReports the number of error reports synchronized
     * @param wishRequests the number of wish requests synchronized
     */
    public void setSyncInfo(int errorReports, int wishRequests)
    {
        String info = String.format(
                "Es wurden %d Fehlerberichte und %d Wunschanfragen synchronisiert.",
                errorReports,
                wishRequests
        );

        syncInfoText.setText(info);
    }
}
