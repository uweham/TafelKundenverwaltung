package kundenverwaltung.controller.admintool;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.EinstellungenDAO;
import kundenverwaltung.dao.EinstellungenDAOimpl;
import kundenverwaltung.model.Einstellungen;

public class GrundeinstellungenController implements Initializable
{

    @FXML
    private TextField inpGebuehr;

    @FXML
    private TextField inpBescheide;

    @FXML
    private CheckBox chkDSE;

    @FXML
    private Button save;

    @FXML
    private ComboBox<?> cbOertliche;

    @FXML
    private ComboBox<String> cbBescheide; // Typisierung <String> hinzugefügt für Typsicherheit
    
    private Einstellungen einstellungen;

    /**
     * Saves the settings when the save button is pressed.
     *
     * @param event The action event.
     */
    @FXML
    void speichern(ActionEvent event)
    {
        try
        {
            // Werte aus den Textfeldern in das Objekt schreiben
            einstellungen.setAlterErwachsener(Integer.parseInt(inpGebuehr.getText()));
            einstellungen.setAlterBescheid(Integer.parseInt(inpBescheide.getText()));

            // ComboBox Logik
            if (cbBescheide.getSelectionModel().getSelectedIndex() == 0)
            {
                einstellungen.setBescheidBenoetigt(true);
            }
            else if (cbBescheide.getSelectionModel().getSelectedIndex() == 1)
            {
                einstellungen.setBescheidBenoetigt(false);
            }

            // Checkbox Logik
            einstellungen.setDatenschutzerklaerung(chkDSE.isSelected());

            // --- WICHTIGE ÄNDERUNG HIER ---
            // Wir speichern das Ergebnis (true/false) in einer Variable
            boolean updateErfolgreich = einstellungen.getEinstellungenDAO().update(einstellungen);

            // Wir prüfen, ob das Update geklappt hat
            if (updateErfolgreich)
            {
                Benachrichtigung.infoBenachrichtigung("Speichern erfolgreich", "Die Einstellungen wurden erfolgreich aktualisiert.");
            }
            else
            {
                // Falls false zurückkommt (z.B. wegen SQL Fehler in der DAO), zeigen wir einen Fehler an!
                Benachrichtigung.warnungBenachrichtigung("Speichern fehlgeschlagen", "Fehler beim Schreiben in die Datenbank. Bitte Logs prüfen.");
            }
            // ------------------------------

        }
        catch (NumberFormatException e)
        {
            Benachrichtigung.warnungBenachrichtigung("Speichern fehlgeschlagen", "Bitte prüfen Sie die Eingaben (nur Zahlen erlaubt).");
            e.printStackTrace();
        }
        catch (Exception e)
        {
            Benachrichtigung.warnungBenachrichtigung("Unbekannter Fehler", "Es ist ein Fehler aufgetreten: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller by loading settings and setting the input fields and checkboxes accordingly.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        EinstellungenDAO einstellungenDAO = new EinstellungenDAOimpl();

        // Laden der aktuellen Werte aus der DB
        einstellungen = einstellungenDAO.read();

        // Debug Ausgabe
        System.out.println(einstellungen);

        // Setzen der Werte in die GUI Elemente
        if (einstellungen != null) {
            inpGebuehr.setText(String.valueOf(einstellungen.getAlterErwachsener()));
            inpBescheide.setText(String.valueOf(einstellungen.getAlterBescheid()));
            chkDSE.setSelected(einstellungen.isDatenschutzerklaerung());

            boolean gueltigerbescheid = einstellungen.isBescheidBenoetigt();
            
            @SuppressWarnings("rawtypes")
            ObservableList obsbescheide = FXCollections.observableArrayList();
            obsbescheide.add("Pro Person");
            obsbescheide.add("Pro Haushalt");
            
            cbBescheide.setItems(obsbescheide);

            // true = Pro Person
            if (gueltigerbescheid)
            {
                cbBescheide.getSelectionModel().select(0);
            }
            // False = Pro Haushalt
            else
            {
                cbBescheide.getSelectionModel().select(1);
            }
        } else {
             Benachrichtigung.warnungBenachrichtigung("Fehler", "Einstellungen konnten nicht geladen werden.");
        }
    }
}