package kundenverwaltung.controller.admintool;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kundenverwaltung.controller.MainController;
import kundenverwaltung.model.Benutzer;

public class BenutzerverwaltungNeuController implements Initializable
{
    @FXML
    private TextField benutzer1;

    @FXML
    private TextField anzeige1;

    @FXML
    private PasswordField passwort11;

    @FXML
    private PasswordField passwort12;

    @FXML
    private Button save1;

    private Benutzer updatebenutzer;
    /**
     * Saves the user.
     *
     * @param event The ActionEvent triggered when saving.
     */
    @FXML
    void benutzerSpeichern(ActionEvent event)
    {
        if (!benutzer1.getText().equals("") && !anzeige1.getText().equals("") && !passwort11.getText().equals("") && !passwort12.getText().equals(""))
        {
            if (updatebenutzer == null)
            {




                    Benutzer neuerBenutzer = new Benutzer(benutzer1.getText(), passwort11.getText(), anzeige1.getText(), false, new ArrayList<>());

                    BenutzerverwaltungController.setBenutzerliste(neuerBenutzer.getBenutzerDAO().readAll());

                    System.out.println("Neuer Benutzer erstellt");

            }
            else
            {
                updatebenutzer.setName(benutzer1.getText());
                updatebenutzer.getBenutzerDAO().update(updatebenutzer);
            }
        }

        updatebenutzer = null;
        try
        {
            MainController.getInstance().getAdmintoolAnlegenController().openBenutzerverwaltung(event);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Stage stage = (Stage) anzeige1.getScene().getWindow();
        // do what you have to do
        stage.close();

    }
    /**
     * Updates the user with the specified data.
     *
     * @param benutzer The user to update.
     */
    public void updateBenutzer(Benutzer benutzer)
    {
        updatebenutzer = benutzer;
        benutzer1.setText(updatebenutzer.getName());
    }
    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if (updatebenutzer != null)
        {
            benutzer1.setText(updatebenutzer.getName());
            anzeige1.setText(updatebenutzer.getAnzeigename());
        }
    }
}
