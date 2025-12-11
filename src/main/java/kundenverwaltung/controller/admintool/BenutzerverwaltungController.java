package kundenverwaltung.controller.admintool;

import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.controller.MainController;
import kundenverwaltung.dao.BenutzerDAO;
import kundenverwaltung.dao.BenutzerDAOimpl;
import kundenverwaltung.dao.RechteDAO;
import kundenverwaltung.dao.RechteDAOimpl;
import kundenverwaltung.model.Benutzer;
import kundenverwaltung.model.Recht;

public class BenutzerverwaltungController implements Initializable
{


    @FXML
    private Button btnNew;

    @FXML
    private Button btnDel;

    @FXML
    private Button btncopy;

    @FXML
    private Button btnSave;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private CheckBox c1;
    @FXML
    private CheckBox c2;

    @FXML
    private CheckBox c3;

    @FXML
    private CheckBox c4;

    @FXML
    private CheckBox c5;

    @FXML
    private CheckBox c6;

    @FXML
    private CheckBox c7;

    @FXML
    private CheckBox c8;

    @FXML
    private CheckBox c9;

    @FXML
    private CheckBox c10;

    @FXML
    private CheckBox c11;

    @FXML
    private CheckBox c12;

    @FXML
    private CheckBox c13;

    @FXML
    private CheckBox c14;

    @FXML
    private CheckBox c15;

    @FXML
    private CheckBox c16;

    @FXML
    private CheckBox c17;

    @FXML
    private CheckBox c18;

    @FXML
    private CheckBox c19;

    @FXML
    private CheckBox c20;

    @FXML
    private CheckBox c21;

    @FXML
    private CheckBox c22;

    @FXML
    private CheckBox c23;

    @FXML
    private CheckBox c24;

    @FXML
    private CheckBox c25;

    @FXML
    private CheckBox c26;

    @FXML
    private CheckBox c27;

    @FXML
    private CheckBox c28;

    @FXML
    private ComboBox<Benutzer> comboBenutzer;

    @FXML
    private Button btnChange;

    @FXML
    private AnchorPane anchorpane;

     //RECHTE kopieren fehlt + Alle Berechtigungen prüfen (aktuell nur bis 12 / 16 möglich)


    private Recht r1 = new Recht(1, "volle Systemrechte. Dieses Benutzerrecht überschreibt alle anderen.", "volle Systemrechte. Dieses Benutzerrecht überschreibt alle anderen.");
    private Recht r2 = new Recht(2, "darf Kunden im Kundenbestand suchen", "darf Kunden im Kundenbestand suchen");
    private Recht r3 = new Recht(3, "darf neue Kunden anlegen", "darf neue Kunden anlegen");
    private Recht r4 = new Recht(4, ",darf Kunden bearbeiten", "darf Kunden bearbeiten");
    private Recht r5 = new Recht(5, "darf Kunden anderer Verteilstellen bearbeiten", "darf Kunden anderer Verteilstellen bearbeiten");
    private Recht r6 = new Recht(6, "darf Kunden löschen", "darf Kunden löschen");
    private Recht r7 = new Recht(7, "darf Personen zum Kunden hinzufügen", "darf Personen zum Kunden hinzufügen");
    private Recht r8 = new Recht(8, "darf Personen vom Kunden löschen", "darf Personen vom Kunden löschen");
    private Recht r9 = new Recht(9, "darf Umsatz \"Umsatz bezahlt\" buchen", "darf Umsatz \"Umsatz bezahlt\" buchen");
    private Recht r10 = new Recht(10, "darf Umsatz \"Dieses bezahlt\" buchen", "darf Umsatz \"Dieses bezahlt\" buchen");
    private Recht r11 = new Recht(11, "darf Umsätze von Kunden anderer Verteilstellen buchen", "darf Umsätze von Kunden anderer Verteilstellen buchen");
    private Recht r12 = new Recht(12, "darf Buchungen stornieren", "darf Buchungen stornieren");
    private Recht r13 = new Recht(13, "darf Buchungen anderer Verteilstellen stornieren", "darf Buchungen anderer Verteilstellen stornieren");
    private Recht r14 = new Recht(14, "darf Kundenlisten ausdrucken", "darf Kundenlisten ausdrucken");
    private Recht r15 = new Recht(15, "darf die Kasse abrechnen", "darf die Kasse abrechnen");
    private Recht r16 = new Recht(16, "darf die Erfassungs-Verteilstelle über die Auswahl wechseln", "darf die Erfassungs-Verteilstelle über die Auswahl wechseln");
    private Recht r17 = new Recht(17, "darf das Erfassungs-Datum über die Auswahl wechseln", "darf das Erfassungs-Datum über die Auswahl wechseln");
    private Recht r18 = new Recht(18, "darf die Statistiken einsehen", "darf die Statistiken einsehen");
    private Recht r19 = new Recht(19, "darf Bescheide ändern", "darf Bescheide ändern");
    private Recht r20 = new Recht(20, "darf Bemerkungen ändern", "darf Bemerkungen ändern");
    private Recht r21 = new Recht(21, "darf Benutzerpasswörter ändern", "darf Benutzerpasswörter ändern");
    private Recht r22 = new Recht(22, "darf den Betreuer eines Kunden ändern", "darf den Betreuer eines Kunden ändern");
    private Recht r23 = new Recht(23, "darf Verteilstellenzugehörigkeit, örtliche Kundennumer und Ausgabetag ändern", "darf Verteilstellenzugehörigkeit, örtliche Kundennumer und Ausgabetag ändern");
    private Recht r24 = new Recht(24, "darf die Kundenausweise drucken", "darf die Kundenausweise drucken");
    private Recht r25 = new Recht(25, "darf die Informationen unter \"Infos/Ändern...\" einsehen", "darf die Informationen unter \"Infos/Ändern...\" einsehen");
    private Recht r26 = new Recht(26, "darf Vollmachten ausstellen", "darf Vollmachten ausstellen");
    private Recht r27 = new Recht(27, "darf Funktionen -sonstiges Drucken- verwenden", "darf Funktionen -sonstiges Drucken- verwenden");
    private Recht r28 = new Recht(28, "darf mit dem Tafel Admintool arbeiten", "darf mit dem Tafel Admintool arbeiten");

    private ArrayList<Recht> kopierteRechte;
    @SuppressWarnings("rawtypes")
    private static ObservableList obsbenutzerliste = FXCollections.observableArrayList();

   @SuppressWarnings({ "unchecked", "rawtypes" })
   private static ArrayList<Benutzer> benutzerliste = new ArrayList();

    @SuppressWarnings("unchecked")
	public static void setBenutzerliste(ArrayList<Benutzer> benutzerliste)
    {
        BenutzerverwaltungController.benutzerliste = benutzerliste;
        obsbenutzerliste.clear();
        obsbenutzerliste.addAll(benutzerliste);

    }

    public static ArrayList<Benutzer> getBenutzerliste()
    {
        return benutzerliste;
    }


    /**
     * Deletes a selected user.
     *
     * @param event The action event.
     */
    @FXML
    void benutzerLoeschen(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Benutzer löschen");
        alert.setContentText("Möchten sie den Benutzer " + comboBenutzer.getSelectionModel().getSelectedItem().getAnzeigename() + " wirklich löschen?");
        ButtonType okButton = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Nein", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type ->
        {
            if (type.getText().equals("Ja"))
            {
                Benutzer benutzer = comboBenutzer.getSelectionModel().getSelectedItem();
                obsbenutzerliste.remove(benutzer);
                benutzer.getBenutzerDAO().delete(benutzer);
            }
        });
    }
    /**
     * Opens the change user dialog.
     *
     * @param event The action event.
     */

    @FXML
    void benutzerdatenAendern(ActionEvent event)
    {
       Benutzer changebenutzer = comboBenutzer.getSelectionModel().getSelectedItem();
        MainController.getInstance().oeffneChangeBenutzer(changebenutzer);
    }


    /**
     * Copies or pastes user rights.
     *
     * @param event The action event.
     */
    @FXML
    void berechtigungenKopieren(ActionEvent event)
    {

        if (btncopy.getText().equals("Berechtigungen einfügen"))
        {
            btncopy.setText("Berechtigungen kopieren");

            kopierteRechteEinfuegen();
        }
        else
        {
            Benutzer aktuellerBenutzer = comboBenutzer.getSelectionModel().getSelectedItem();

            kopierteRechte = aktuellerBenutzer.getRechte();

            btncopy.setText("Berechtigungen einfügen");


        }

    }

    private void kopierteRechteEinfuegen()
    {
        Benutzer aktuellerBenutzer = comboBenutzer.getSelectionModel().getSelectedItem();
        Dictionary<Integer, Boolean> benutzerdict = new Hashtable<>();

        for (Recht element : kopierteRechte)
        {

            benutzerdict.put(element.getRechtId(), true);
        }

            if (benutzerdict.get(1) == null)
            {
                aktuellerBenutzer.entferneRecht(r1);
            }

            if (benutzerdict.get(1) != null)
            {
                aktuellerBenutzer.addRecht(r1);
            }

            if (benutzerdict.get(2) == null)
            {
                aktuellerBenutzer.entferneRecht(r2);
            }

            if (benutzerdict.get(2) != null)
            {
                aktuellerBenutzer.addRecht(r2);
            }


            if (benutzerdict.get(3) == null)
            {
                aktuellerBenutzer.entferneRecht(r3);
            }

            if (benutzerdict.get(3) != null)
            {
                aktuellerBenutzer.addRecht(r3);
            }


            if (benutzerdict.get(4) == null)
            {
                aktuellerBenutzer.entferneRecht(r4);
            }

            if (benutzerdict.get(4) != null)
            {
                aktuellerBenutzer.addRecht(r4);
            }


            if (benutzerdict.get(5) == null)
            {
                aktuellerBenutzer.entferneRecht(r5);
            }

            if (benutzerdict.get(5) != null)
            {
                aktuellerBenutzer.addRecht(r5);
            }

            if (benutzerdict.get(6) == null)
            {
                aktuellerBenutzer.entferneRecht(r6);
            }

            if (benutzerdict.get(6) != null)
            {
                aktuellerBenutzer.addRecht(r6);
            }


            if (benutzerdict.get(7) == null)
            {
                aktuellerBenutzer.entferneRecht(r7);
            }


            if (benutzerdict.get(7) != null)
            {
                aktuellerBenutzer.addRecht(r7);
            }


            if (benutzerdict.get(8) == null)
            {
                aktuellerBenutzer.entferneRecht(r8);
            }


            if (benutzerdict.get(8) != null)
            {
                aktuellerBenutzer.addRecht(r8);
            }


            if (benutzerdict.get(9) == null)
            {
                aktuellerBenutzer.entferneRecht(r9);
            }

            if (benutzerdict.get(9) != null)
            {
                aktuellerBenutzer.addRecht(r9);
            }



            if (benutzerdict.get(10) == null)
            {
                aktuellerBenutzer.entferneRecht(r10);
            }

            if (benutzerdict.get(10) != null)
            {
                aktuellerBenutzer.addRecht(r10);
            }

            if (benutzerdict.get(11) == null)
            {
                aktuellerBenutzer.entferneRecht(r11);
            }

            if (benutzerdict.get(11) != null)
            {
                aktuellerBenutzer.addRecht(r11);
            }

            if (benutzerdict.get(12) == null)
            {
                aktuellerBenutzer.entferneRecht(r12);
            }


            if (benutzerdict.get(12) != null)
            {
                aktuellerBenutzer.addRecht(r12);
            }



            if (benutzerdict.get(13) == null)
            {
                aktuellerBenutzer.entferneRecht(r13);
            }

            if (benutzerdict.get(13) != null)
            {
                aktuellerBenutzer.addRecht(r13);
            }

            if (benutzerdict.get(14) == null)
            {
                aktuellerBenutzer.entferneRecht(r14);
            }

            if (benutzerdict.get(14) != null)
            {
                aktuellerBenutzer.addRecht(r14);
            }

            if (benutzerdict.get(15) == null)
            {
                aktuellerBenutzer.entferneRecht(r15);
            }

            if (benutzerdict.get(15) != null)
            {
                aktuellerBenutzer.addRecht(r15);
            }

            if (benutzerdict.get(16) == null)
            {
                aktuellerBenutzer.entferneRecht(r16);
            }

            if (benutzerdict.get(16) != null)
            {
                aktuellerBenutzer.addRecht(r16);
            }



            if (benutzerdict.get(17) == null)
            {
                aktuellerBenutzer.entferneRecht(r17);
            }

            if (benutzerdict.get(17) != null)
            {
                aktuellerBenutzer.addRecht(r17);
            }

            if (benutzerdict.get(18) == null)
            {
                aktuellerBenutzer.entferneRecht(r18);
            }

            if (benutzerdict.get(18) != null)
            {
                aktuellerBenutzer.addRecht(r18);
            }

             if (benutzerdict.get(19) == null)
            {
                aktuellerBenutzer.entferneRecht(r19);
            }

            if (benutzerdict.get(19) != null)
            {
                aktuellerBenutzer.addRecht(r19);
            }


            if (benutzerdict.get(20) == null)
            {
                aktuellerBenutzer.entferneRecht(r20);
            }

            if (benutzerdict.get(20) != null)
            {
                aktuellerBenutzer.addRecht(r20);
            }


            if (benutzerdict.get(21) == null)
            {
                aktuellerBenutzer.entferneRecht(r21);
            }

            if (benutzerdict.get(21) != null)
            {
                aktuellerBenutzer.addRecht(r21);
            }


            if (benutzerdict.get(22) == null)
            {
                aktuellerBenutzer.entferneRecht(r22);
            }


            if (benutzerdict.get(22) != null)
            {
                aktuellerBenutzer.addRecht(r22);
            }



            if (benutzerdict.get(23) == null)
            {
                aktuellerBenutzer.entferneRecht(r23);
            }

            if (benutzerdict.get(23) != null)
            {
                aktuellerBenutzer.addRecht(r23);
            }


            if (benutzerdict.get(24) == null)
            {
                aktuellerBenutzer.entferneRecht(r24);
            }

            if (benutzerdict.get(24) != null)
            {
                aktuellerBenutzer.addRecht(r24);
            }


            if (benutzerdict.get(25) == null)
            {
                aktuellerBenutzer.entferneRecht(r25);
            }

            if (benutzerdict.get(25) != null)
            {
                aktuellerBenutzer.addRecht(r25);
            }


            if (benutzerdict.get(26) == null)
            {
                aktuellerBenutzer.entferneRecht(r26);
            }

            if (benutzerdict.get(26) != null)
            {
                aktuellerBenutzer.addRecht(r26);
            }


            if (benutzerdict.get(27) == null)
            {
                aktuellerBenutzer.entferneRecht(r27);
            }


            if (benutzerdict.get(27) != null)
            {
                aktuellerBenutzer.addRecht(r27);
            }


            if (benutzerdict.get(28) == null)
            {
                aktuellerBenutzer.entferneRecht(r28);
            }



            if (benutzerdict.get(28) != null)
            {
                aktuellerBenutzer.addRecht(r28);
            }



        kopierteRechte = null;
        Benachrichtigung.infoBenachrichtigung("Kopieren erfolgreich", "Die Rechte wurden erfolgreich eingefügt");
        rechteLaden(aktuellerBenutzer);
    }
    /**
     * Saves the current settings.
     *
     * @param event the action event
     */
    @FXML
    void einstellungenSpeichern(ActionEvent event)
    {
        Benutzer aktuellerBenutzer = comboBenutzer.getSelectionModel().getSelectedItem();
        ArrayList<Recht> benutzerrechte = null;

           benutzerrechte = aktuellerBenutzer.getRechte();


        Dictionary<Integer, Boolean> benutzerdict = new Hashtable<>();

        for (Recht element : benutzerrechte)
        {

            benutzerdict.put(element.getRechtId(), true);
        }

        if (c1.isSelected())
        {
            if (benutzerdict.get(1) == null)
            {
                aktuellerBenutzer.addRecht(r1);
            }

        }
        if (!c1.isSelected())
        {
            if (benutzerdict.get(1) != null)
            {
                aktuellerBenutzer.entferneRecht(r1);
            }

        }
        //Neue Checkbox
        if (c2.isSelected())
        {
            if (benutzerdict.get(2) == null)
            {
                aktuellerBenutzer.addRecht(r2);
            }

        }
        if (!c2.isSelected())
        {
            if (benutzerdict.get(2) != null)
            {
                aktuellerBenutzer.entferneRecht(r2);
            }

        }
        //Neue Checkbox
        if (c3.isSelected())
        {
            if (benutzerdict.get(3) == null)
            {
                aktuellerBenutzer.addRecht(r3);
            }

        }
        if (!c3.isSelected())
        {
            if (benutzerdict.get(3) != null)
            {
                aktuellerBenutzer.entferneRecht(r3);
            }

        }
        //Neue Checkbox
        if (c4.isSelected())
        {
            if (benutzerdict.get(4) == null)
            {
                aktuellerBenutzer.addRecht(r4);
            }

        }
        if (!c4.isSelected())
        {
            if (benutzerdict.get(4) != null)
            {
                aktuellerBenutzer.entferneRecht(r4);
            }

        }
        //Neue Checkbox
        if (c5.isSelected())
        {
            if (benutzerdict.get(5) == null)
            {
                aktuellerBenutzer.addRecht(r5);
            }

        }
        if (!c5.isSelected())
        {
            if (benutzerdict.get(5) != null)
            {
                aktuellerBenutzer.entferneRecht(r5);
            }

        }
        //Neue Checkbox
        if (c6.isSelected())
        {
            if (benutzerdict.get(6) == null)
            {
                aktuellerBenutzer.addRecht(r6);
            }

        }
        if (!c6.isSelected())
        {
            if (benutzerdict.get(6) != null)
            {
                aktuellerBenutzer.entferneRecht(r6);
            }

        }
        //Neue Checkbox
        if (c7.isSelected())
        {
            if (benutzerdict.get(7) == null)
            {
                aktuellerBenutzer.addRecht(r7);
            }

        }
        if (!c7.isSelected())
        {
            if (benutzerdict.get(7) != null)
            {
                aktuellerBenutzer.entferneRecht(r7);
            }

        }
        //Neue Checkbox
        if (c8.isSelected())
        {
            if (benutzerdict.get(8) == null)
            {
                aktuellerBenutzer.addRecht(r8);
            }

        }
        if (!c8.isSelected())
        {
            if (benutzerdict.get(8) != null)
            {
                aktuellerBenutzer.entferneRecht(r8);
            }

        }
        //Neue Checkbox
        if (c9.isSelected())
        {
            if (benutzerdict.get(9) == null)
            {
                aktuellerBenutzer.addRecht(r9);
            }

        }
        if (!c9.isSelected())
        {
            if (benutzerdict.get(9) != null)
            {
                aktuellerBenutzer.entferneRecht(r9);
            }

        }
        //Neue Checkbox
        if (c10.isSelected())
        {
            if (benutzerdict.get(10) == null)
            {
                aktuellerBenutzer.addRecht(r10);
            }

        }
        if (!c10.isSelected())
        {
            if (benutzerdict.get(10) != null)
            {
                aktuellerBenutzer.entferneRecht(r10);
            }

        }
        //Neue Checkbox
        if (c11.isSelected())
        {
            if (benutzerdict.get(11) == null)
            {
                aktuellerBenutzer.addRecht(r11);
            }

        }
        if (!c11.isSelected())
        {
            if (benutzerdict.get(11) != null)
            {
                aktuellerBenutzer.entferneRecht(r11);
            }

        }
        //Neue Checkbox
        if (c12.isSelected())
        {
            if (benutzerdict.get(12) == null)
            {
                aktuellerBenutzer.addRecht(r12);
            }

        }
        if (!c12.isSelected())
        {
            if (benutzerdict.get(12) != null)
            {
                aktuellerBenutzer.entferneRecht(r12);
            }

        }
        //Neue Checkbox
        if (c13.isSelected())
        {
            if (benutzerdict.get(13) == null)
            {
                aktuellerBenutzer.addRecht(r13);
            }

        }
        if (!c13.isSelected())
        {
            if (benutzerdict.get(13) != null)
            {
                aktuellerBenutzer.entferneRecht(r13);
            }

        }

        //Neue Checkbox
        if (c14.isSelected())
        {
            if (benutzerdict.get(14) == null)
            {
                aktuellerBenutzer.addRecht(r14);
            }

        }
        if (!c14.isSelected())
        {
            if (benutzerdict.get(14) != null)
            {
                aktuellerBenutzer.entferneRecht(r14);
            }

        }

        //Neue Checkbox
        if (c15.isSelected())
        {
            if (benutzerdict.get(15) == null)
            {
                aktuellerBenutzer.addRecht(r15);
            }

        }
        if (!c15.isSelected())
        {
            if (benutzerdict.get(15) != null)
            {
                aktuellerBenutzer.entferneRecht(r15);
            }

        }

        //Neue Checkbox
        if (c16.isSelected())
        {
            if (benutzerdict.get(16) == null)
            {
                aktuellerBenutzer.addRecht(r16);
            }

        }
        if (!c16.isSelected())
        {
            if (benutzerdict.get(16) != null)
            {
                aktuellerBenutzer.entferneRecht(r16);
            }

        }

        //Neue Checkbox
        if (c17.isSelected())
        {
            if (benutzerdict.get(17) == null)
            {
                aktuellerBenutzer.addRecht(r17);
            }

        }
        if (!c17.isSelected())
        {
            if (benutzerdict.get(17) != null)
            {
                aktuellerBenutzer.entferneRecht(r17);
            }

        }

        //Neue Checkbox
        if (c18.isSelected())
        {
            if (benutzerdict.get(18) == null)
            {
                aktuellerBenutzer.addRecht(r18);
            }

        }
        if (!c18.isSelected())
        {
            if (benutzerdict.get(18) != null)
            {
                aktuellerBenutzer.entferneRecht(r18);
            }

        }

        //Neue Checkbox
        if (c19.isSelected())
        {
            if (benutzerdict.get(19) == null)
            {
                aktuellerBenutzer.addRecht(r19);
            }

        }
        if (!c19.isSelected())
        {
            if (benutzerdict.get(19) != null)
            {
                aktuellerBenutzer.entferneRecht(r19);
            }

        }

        //Neue Checkbox
        if (c20.isSelected())
        {
            if (benutzerdict.get(20) == null)
            {
                aktuellerBenutzer.addRecht(r20);
            }

        }
        if (!c20.isSelected())
        {
            if (benutzerdict.get(20) != null)
            {
                aktuellerBenutzer.entferneRecht(r20);
            }

        }

        //Neue Checkbox
        if (c21.isSelected())
        {
            if (benutzerdict.get(21) == null)
            {
                aktuellerBenutzer.addRecht(r21);
            }

        }
        if (!c21.isSelected())
        {
            if (benutzerdict.get(21) != null)
            {
                aktuellerBenutzer.entferneRecht(r21);
            }

        }

        //Neue Checkbox
        if (c22.isSelected())
        {
            if (benutzerdict.get(22) == null)
            {
                aktuellerBenutzer.addRecht(r22);
            }

        }
        if (!c22.isSelected())
        {
            if (benutzerdict.get(22) != null)
            {
                aktuellerBenutzer.entferneRecht(r22);
            }

        }

        //Neue Checkbox
        if (c23.isSelected())
        {
            if (benutzerdict.get(23) == null)
            {
                aktuellerBenutzer.addRecht(r23);
            }

        }
        if (!c23.isSelected())
        {
            if (benutzerdict.get(23) != null)
            {
                aktuellerBenutzer.entferneRecht(r23);
            }

        }

        //Neue Checkbox
        if (c24.isSelected())
        {
            if (benutzerdict.get(24) == null)
            {
                aktuellerBenutzer.addRecht(r24);
            }

        }
        if (!c24.isSelected())
        {
            if (benutzerdict.get(24) != null)
            {
                aktuellerBenutzer.entferneRecht(r24);
            }

        }

        //Neue Checkbox
        if (c25.isSelected())
        {
            if (benutzerdict.get(25) == null)
            {
                aktuellerBenutzer.addRecht(r25);
            }

        }
        if (!c25.isSelected())
        {
            if (benutzerdict.get(25) != null)
            {
                aktuellerBenutzer.entferneRecht(r25);
            }

        }

        //Neue Checkbox
        if (c26.isSelected())
        {
            if (benutzerdict.get(26) == null)
            {
                aktuellerBenutzer.addRecht(r26);
            }

        }
        if (!c26.isSelected())
        {
            if (benutzerdict.get(26) != null)
            {
                aktuellerBenutzer.entferneRecht(r26);
            }

        }

        //Neue Checkbox
        if (c27.isSelected())
        {
            if (benutzerdict.get(27) == null)
            {
                aktuellerBenutzer.addRecht(r27);
            }

        }
        if (!c27.isSelected())
        {
            if (benutzerdict.get(27) != null)
            {
                aktuellerBenutzer.entferneRecht(r27);
            }

        }

        //Neue Checkbox
        if (c28.isSelected())
        {
            if (benutzerdict.get(28) == null)
            {
                aktuellerBenutzer.addRecht(r28);
            }

        }
        if (!c28.isSelected())
        {
            if (benutzerdict.get(28) != null)
            {
                aktuellerBenutzer.entferneRecht(r28);
            }

        }
        Benachrichtigung.infoBenachrichtigung("Speichern erfolgreich", "Die Rechte von " + aktuellerBenutzer + " wurden erfolgreich aktualsiert");

    }
    /**
     * Opens the view for creating a new user.
     *
     * @param event the action event
     */
    @FXML
    void neuenBenutzerAnlegen(ActionEvent event)
    {
        MainController.getInstance().oeffneNeuerBenutzer();
    }
    /**
     * Loads the data for the user administration.
     */
    @SuppressWarnings("unchecked")
	public void datenLaden()
    {
        obsbenutzerliste.clear();

        AdmintoolController.setBenutzerverwaltungController(this);
        BenutzerDAO benutzerDAO = new BenutzerDAOimpl();
        @SuppressWarnings("unused")
		RechteDAO rechteDAO = new RechteDAOimpl();
        benutzerliste = benutzerDAO.readAll();
        //auskommentieren!!!
        if (benutzerliste.size() < 1)
        {
            rechteErstellen();
        }
        //rechteliste=rechteDAO.read(1);

        obsbenutzerliste.addAll(benutzerliste);
        comboBenutzer.setItems(obsbenutzerliste);
        comboBenutzer.getSelectionModel().select(0);
    }
    private void rechteLaden(Benutzer benutzer)
    {

        //System.out.println(benutzer.getRechte());

        @SuppressWarnings("unused")
		ArrayList<Recht> rechtArrayList = benutzer.getRechte();

/*        C1.setSelected( benutzer.getBenutzerDAO().readRecht(benutzer,1));
        C2.setSelected( benutzer.getBenutzerDAO().readRecht(benutzer,2));
        C3.setSelected( benutzer.getBenutzerDAO().readRecht(benutzer,3));
        C4.setSelected( benutzer.getBenutzerDAO().readRecht(benutzer,4));*/


        ArrayList<Recht> benutzerrechte = benutzer.getRechte();

        Dictionary<Integer, Boolean> benutzerdict = new Hashtable<>();

        for (Recht element : benutzerrechte)
        {
            benutzerdict.put(element.getRechtId(), true);
        }


        if (benutzerdict.get(1) != null)
        {
            c1.setSelected(true);
        }
        if (benutzerdict.get(1) == null)
        {
            c1.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(2) != null)
        {
            c2.setSelected(true);
        }
        if (benutzerdict.get(2) == null)
        {
            c2.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(3) != null)
        {
            c3.setSelected(true);
        }
        if (benutzerdict.get(3) == null)
        {
            c3.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(4) != null)
        {
            c4.setSelected(true);
        }
        if (benutzerdict.get(4) == null)
        {
            c4.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(5) != null)
        {
            c5.setSelected(true);
        }
        if (benutzerdict.get(5) == null)
        {
            c5.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(6) != null)
        {
            c6.setSelected(true);
        }
        if (benutzerdict.get(6) == null)
        {
            c6.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(7) != null)
        {
            c7.setSelected(true);
        }
        if (benutzerdict.get(7) == null)
        {
            c7.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(8) != null)
        {
            c8.setSelected(true);
        }
        if (benutzerdict.get(8) == null)
        {
            c8.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(9) != null)
        {
            c9.setSelected(true);
        }
        if (benutzerdict.get(9) == null)
        {
            c9.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(10) != null)
        {
            c10.setSelected(true);
        }
        if (benutzerdict.get(10) == null)
        {
            c10.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(11) != null)
        {
            c11.setSelected(true);
        }
        if (benutzerdict.get(11) == null)
        {
            c11.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(12) != null)
        {
            c12.setSelected(true);
        }
        if (benutzerdict.get(12) == null)
        {
            c12.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(2) != null)
        {
            c13.setSelected(true);
        }
        if (benutzerdict.get(13) == null)
        {
            c13.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(14) != null)
        {
            c14.setSelected(true);
        }
        if (benutzerdict.get(14) == null)
        {
            c14.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(15) != null)
        {
            c15.setSelected(true);
        }
        if (benutzerdict.get(15) == null)
        {
            c15.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(16) != null)
        {
            c16.setSelected(true);
        }
        if (benutzerdict.get(16) == null)
        {
            c16.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(17) != null)
        {
            c17.setSelected(true);
        }
        if (benutzerdict.get(17) == null)
        {
            c17.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(18) != null)
        {
            c18.setSelected(true);
        }
        if (benutzerdict.get(18) == null)
        {
            c18.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(19) != null)
        {
            c19.setSelected(true);
        }
        if (benutzerdict.get(19) == null)
        {
            c19.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(20) != null)
        {
            c20.setSelected(true);
        }
        if (benutzerdict.get(20) == null)
        {
            c20.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(21) != null)
        {
            c21.setSelected(true);
        }
        if (benutzerdict.get(21) == null)
        {
            c21.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(22) != null)
        {
            c22.setSelected(true);
        }
        if (benutzerdict.get(22) == null)
        {
            c22.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(23) != null)
        {
            c23.setSelected(true);
        }
        if (benutzerdict.get(23) == null)
        {
            c23.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(24) != null)
        {
            c24.setSelected(true);
        }
        if (benutzerdict.get(24) == null)
        {
            c24.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(25) != null)
        {
            c25.setSelected(true);
        }
        if (benutzerdict.get(25) == null)
        {
            c25.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(26) != null)
        {
            c26.setSelected(true);
        }
        if (benutzerdict.get(26) == null)
        {
            c26.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(27) != null)
        {
            c27.setSelected(true);
        }
        if (benutzerdict.get(27) == null)
        {
            c27.setSelected(false);
        }
        //Neue Checkbox
        if (benutzerdict.get(28) != null)
        {
            c28.setSelected(true);
        }
        if (benutzerdict.get(28) == null)
        {
            c28.setSelected(false);
        }




    }
    /**
     * Initializes the controller class.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        // Nur bei der ersten Initialisierung --> rechteErstellen();
datenLaden();
        comboBenutzer.getSelectionModel().selectFirst();
        Benutzer benutzer = comboBenutzer.getSelectionModel().getSelectedItem();
        if (benutzer != null)
        {
            rechteLaden(benutzer);
        }
        comboBenutzer.valueProperty().addListener(new ChangeListener<Benutzer>()
        {
            @Override
            public void changed(ObservableValue<? extends Benutzer> observable, Benutzer oldValue, Benutzer newValue)
            {

                if (newValue != null)
                {
                    if (newValue.getRechte() != null)
                    {
                        rechteLaden(newValue);
                    }
                }


            }
        });
/*
        Benutzer_RechteDAO benutzer_rechteDAO = new Benutzer_RechteDAOimpl();
        benutzer_rechteDAO.rechtHinzufuegen(benutzerliste.get(0),rechteDAO.read(1));



        Recht r = new Recht(5,"VerteilstellenBearbeiten","darf Kunden anderer Verteilstellen bearbeiten");
        benutzerliste.get(0).getRechte().add(r);
        RechteDAO rechteDao = new RechteDAOimpl();
        rechteDao.create(r);
        benutzerDAO.update(benutzerliste.get(0));

         ArrayList<Recht> a = new ArrayList<>();

        Benutzer jenstest= new Benutzer(1,"jens","jens","jens",true,a);
        benutzerDAO.create(jenstest);
        benutzerliste.add(jenstest);
        System.out.println(benutzerliste);*/

    }

    @SuppressWarnings("unused")
	private void rechteErstellen()
    {
		Recht r1 = new Recht("volle Systemrechte. Dieses Benutzerrecht überschreibt alle anderen.", "volle Systemrechte. Dieses Benutzerrecht überschreibt alle anderen.");
        Recht r2 = new Recht("darf Kunden im Kundenbestand suchen", "darf Kunden im Kundenbestand suchen");
        Recht r3 = new Recht("darf neue Kunden anlegen", "darf neue Kunden anlegen");
        Recht r4 = new Recht("darf Kunden bearbeiten", "darf Kunden bearbeiten");
        Recht r5 = new Recht("darf Kunden anderer Verteilstellen bearbeiten", "darf Kunden anderer Verteilstellen bearbeiten");
        Recht r6 = new Recht("darf Kunden löschen", "darf Kunden löschen");
        Recht r7 = new Recht("darf Personen zum Kunden hinzufügen", "darf Personen zum Kunden hinzufügen");
        Recht r8 = new Recht("darf Personen vom Kunden löschen", "darf Personen vom Kunden löschen");
        Recht r9 = new Recht("darf Umsatz \"Umsatz bezahlt\" buchen", "darf Umsatz \"Umsatz bezahlt\" buchen");
        Recht r10 = new Recht("darf Umsatz \"Dieses bezahlt\" buchen", "darf Umsatz \"Dieses bezahlt\" buchen");
        Recht r11 = new Recht("darf Umsätze von Kunden anderer Verteilstellen buchen", "darf Umsätze von Kunden anderer Verteilstellen buchen");
        Recht r12 = new Recht("darf Buchungen stornieren", "darf Buchungen stornieren");
        Recht r13 = new Recht("darf Buchungen anderer Verteilstellen stornieren", "darf Buchungen anderer Verteilstellen stornieren");
        Recht r14 = new Recht("darf Kundenlisten ausdrucken", "darf Kundenlisten ausdrucken");
        Recht r15 = new Recht("darf die Kasse abrechnen", "darf die Kasse abrechnen");
        Recht r16 = new Recht("darf die Erfassungs-Verteilstelle über die Auswahl wechseln", "darf die Erfassungs-Verteilstelle über die Auswahl wechseln");
        Recht r17 = new Recht("darf das Erfassungs-Datum über die Auswahl wechseln", "darf das Erfassungs-Datum über die Auswahl wechseln");
        Recht r18 = new Recht("darf die Statistiken einsehen", "darf die Statistiken einsehen");
        Recht r19 = new Recht("darf Bescheide ändern", "darf Bescheide ändern");
        Recht r20 = new Recht("darf Bemerkungen ändern", "darf Bemerkungen ändern");
        Recht r21 = new Recht("darf Benutzerpasswörter ändern", "darf Benutzerpasswörter ändern");
        Recht r22 = new Recht("darf den Betreuer eines Kunden ändern", "darf den Betreuer eines Kunden ändern");
        Recht r23 = new Recht("darf Verteilstellenzugehörigkeit, örtliche Kundennumer und Ausgabetag ändern", "darf Verteilstellenzugehörigkeit, örtliche Kundennumer und Ausgabetag ändern");
        Recht r24 = new Recht("darf die Kundenausweise drucken", "darf die Kundenausweise drucken");
        Recht r25 = new Recht("darf die Informationen unter \"Infos/Ändern...\" einsehen", "darf die Informationen unter \"Infos/Ändern...\" einsehen");
        Recht r26 = new Recht("darf Vollmachten ausstellen", "darf Vollmachten ausstellen");
        Recht r27 = new Recht("darf Funktionen -sonstiges Drucken- verwenden", "darf Funktionen -sonstiges Drucken- verwenden");
        Recht r28 = new Recht("darf mit dem Tafel Admintool arbeiten", "darf mit dem Tafel Admintool arbeiten");



    }


}
