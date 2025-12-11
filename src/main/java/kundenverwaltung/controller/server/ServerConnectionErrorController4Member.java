package kundenverwaltung.controller.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerConnectionErrorController4Member
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnectionErrorController4Member.class);

    @FXML
    private Button buttonExit;

    /**
     * Handles the exit program action for member users.
     * @param event the action event
     */
    @FXML
    private void exitProgram(ActionEvent event)
    {
        Stage stage = (Stage) buttonExit.getScene().getWindow();

        if (stage != null)
        {
            LOGGER.info("Schließe das 'Server nicht erreichbar'-Fenster.");
            stage.close();
        }
        else
        {
            LOGGER.warn("Konnte das Fenster nicht schließen, da die Stage nicht gefunden wurde.");
        }
    }
}
