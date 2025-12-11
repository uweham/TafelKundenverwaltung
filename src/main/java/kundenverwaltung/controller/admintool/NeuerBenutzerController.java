package kundenverwaltung.controller.admintool;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import kundenverwaltung.dao.BenutzerDAO;
import kundenverwaltung.dao.BenutzerDAOimpl;
import kundenverwaltung.model.Benutzer;
//import kundenverwaltung.model.Recht;

@SuppressWarnings("unused")
public class NeuerBenutzerController
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

    /**
     * Handles the action of saving a new Benutzer.
     *
     * @param event The action event.
     */
    @FXML
    void benutzerSpeichern(ActionEvent event)
    {


                    BenutzerDAO benutzerDAO = new BenutzerDAOimpl();

			ArrayList<Benutzer> benutzerliste = benutzerDAO.readAll();



			Benutzer benutzer = new Benutzer(benutzer1.getText(), passwort11.getText(), anzeige1.getText(), false, new ArrayList<>());
        }




}
