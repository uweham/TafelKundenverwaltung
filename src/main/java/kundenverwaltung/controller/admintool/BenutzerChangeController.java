package kundenverwaltung.controller.admintool;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kundenverwaltung.model.Benutzer;

public class BenutzerChangeController implements Initializable
{


    @FXML
    private TextField benutzer2;

    @FXML
    private TextField anzeige2;

    @FXML
    private PasswordField passwort21;

    @FXML
    private PasswordField passwort22;

    @FXML
    private Button save2;

    @FXML
    private Benutzer benutzer;
    /**
     * Updates the user information when the save button is pressed.
     *
     * @param event the ActionEvent triggered by pressing the save button.
     */
    @FXML
    void benutzerUpdate(ActionEvent event)
    {
        if (!benutzer2.getText().equals("") && !anzeige2.getText().equals("") && !passwort21.getText().equals("") && !passwort22.getText().equals(""))
        {
            System.out.println("Update");
            benutzer.setAnzeigename(anzeige2.getText());
            benutzer.setPasswort(passwort21.getText());
            benutzer.getBenutzerDAO().update(benutzer);

            BenutzerverwaltungController.setBenutzerliste(benutzer.getBenutzerDAO().readAll());

            Stage stage = (Stage) benutzer2.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
       // MainController.getInstance().getBenutzerprofilController().
    }

    /**
     * Gets the Benutzer object.
     *
     * @return the Benutzer object.
     */
    public Benutzer getBenutzer()
    {
        return benutzer;
    }

    /**
     * Sets the Benutzer object.
     *
     * @param benutzer the Benutzer object to set.
     */
    public void setBenutzer(Benutzer benutzer)
    {
        this.benutzer = benutzer;
    }
}
