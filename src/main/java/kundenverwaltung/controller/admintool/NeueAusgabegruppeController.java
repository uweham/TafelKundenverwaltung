package kundenverwaltung.controller.admintool;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kundenverwaltung.controller.MainController;
import kundenverwaltung.model.Ausgabegruppe;

public class NeueAusgabegruppeController
{

    @FXML
    private TextField inpName;

    @FXML
    private Label lblname;

    @FXML
    private Button btnSpeichern;

    /**
     * Handles the action of saving a new Ausgabegruppe.
     *
     * @param event The action event.
     */
    @FXML
    void speichern(ActionEvent event)
    {
        if (!inpName.getText().equals(""))
        {

            @SuppressWarnings("unused")
			Ausgabegruppe ausgabegruppe = new Ausgabegruppe(inpName.getText());

            try
            {
                MainController.getInstance().getAdmintoolAnlegenController().openAusgabegruppen(event);
                Stage stage  = (Stage) inpName.getScene().getWindow();
                stage.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }
}
