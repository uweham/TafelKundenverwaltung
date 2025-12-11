package kundenverwaltung.controller.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ServerConnectionErrorController
{

    @FXML
    private Button buttonExit;

    /**
     * Handles the exit program action.
     * @param event the action event
     */
    @FXML
    private void exitProgram(ActionEvent event)
    {
        Stage stage = (Stage) buttonExit.getScene().getWindow();
        stage.close();
    }
}
