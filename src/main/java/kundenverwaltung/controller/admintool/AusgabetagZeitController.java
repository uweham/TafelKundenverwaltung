package kundenverwaltung.controller.admintool;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import kundenverwaltung.controller.MainController;
import kundenverwaltung.dao.AusgabeTagZeitDAO;
import kundenverwaltung.dao.AusgabeTagZeitDAOimpl;
import kundenverwaltung.model.AusgabeTagZeit;
import kundenverwaltung.model.Ausgabetag;

public class AusgabetagZeitController implements Initializable
{

    @FXML private ComboBox<String> comboWochentag;
    @FXML private TextField inpStartzeit;
    @FXML private TextField inpEndzeit;
    @FXML private Button btnSpeichern;
    @FXML private Button btnAbbrechen;

    private final AusgabeTagZeitDAO ausgabeTagZeitDAO = new AusgabeTagZeitDAOimpl();
    private ObservableList<String> wochentage = FXCollections.observableArrayList();

    // Reentrancy-Guard, verhindert Doppelklick/Enter+Klick
    private volatile boolean saving = false;
    /**
    *
    */
    @FXML
    void abbrechen(ActionEvent event)
    {
        Stage stage = (Stage) inpEndzeit.getScene().getWindow();
        stage.close();
    }
    /**
    *
    */
    @FXML
    void speichern(ActionEvent event)
    {
        if (saving) return;     // schon am Speichern → zweites Event ignorieren
        saving = true;
        btnSpeichern.setDisable(true);
        try
        {
            Ausgabetag ausgabetag = new Ausgabetag(comboWochentag.getSelectionModel().getSelectedIndex() + 1);
            LocalTime startzeit = LocalTime.parse(inpStartzeit.getText());
            LocalTime endzeit   = LocalTime.parse(inpEndzeit.getText());

            AusgabeTagZeit ausz = new AusgabeTagZeit(ausgabetag, startzeit, endzeit);

            // genau EIN Datenbank-Write
            ausgabeTagZeitDAO.create(ausz);

            // zurück zur Seite „Ausgabegruppen“
            MainController.getInstance().getAdmintoolAnlegenController().openAusgabegruppen(event);

        } catch (Exception e)
        {
            System.out.println("Falsche Zeiteingabe: " + e.getMessage());
        } finally
        {
            Stage stage = (Stage) inpEndzeit.getScene().getWindow();
            stage.close();
        }
    }
    /**
    *
    */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        wochentage.addAll("Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag");
        comboWochentag.setItems(wochentage);
        comboWochentag.getSelectionModel().selectFirst();

        // keine setOnAction-Verdrahtung hier! (onAction kommt aus der FXML)
        // optional: Enter-Taste auf Speichern legen
        btnSpeichern.setDefaultButton(true);
        btnAbbrechen.setCancelButton(true);
    }
}
