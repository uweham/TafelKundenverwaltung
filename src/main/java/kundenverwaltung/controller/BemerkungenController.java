package kundenverwaltung.controller;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.HaushaltDAOimpl;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;

public class BemerkungenController
{

    private ChangeFontSize changeFontSize = new ChangeFontSize();

    //Bemerkungen bearbeiten
    @FXML
    private Label labelHeading;
    @FXML
    private TextArea txtBMBemerkungen;
    @FXML
    private Button btnSaveComment;
    @FXML
    private Button btnCancelComment;



    private Haushalt haushalt;

    /**
     * Methode, die ausgeführt wird, wenn der OK-Button für Bemerkungen gedrückt wird.
     */
    @FXML
    public void btnBemerkungenOK()
    {
        haushalt.setBemerkungen(txtBMBemerkungen.getText());

        Boolean checkUpdate = new HaushaltDAOimpl().update(haushalt);

        if (checkUpdate)
        {
            Benachrichtigung.infoBenachrichtigung("Bearbeiten erfolgreich.", "Die Bemerkung wurde erfolgreich bearbeitet.");
            Stage stage = (Stage) txtBMBemerkungen.getScene().getWindow();
            stage.close();
        } else
        {
            System.out.println("fehlschlag");
        }


    }

    /**
     * Methode, die ausgeführt wird, wenn der Abbrechen-Button für Bemerkungen gedrückt wird.
     */
    public void btnBemerkungenAbbrechen()
    {
        Stage stage = (Stage) txtBMBemerkungen.getScene().getWindow();
        stage.close();
    }

    /**
     * Setzt die Schriftgröße der UI-Elemente.
     * @param fontSize Die neue Schriftgröße.
     */
    public void setFontSize(Double fontSize)
    {
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);

        ArrayList<Label> labelArrayList = new ArrayList<>(Arrays.asList(labelHeading));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(btnCancelComment, btnSaveComment));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);

        ArrayList<TextArea> textAreaArrayList = new ArrayList<>(Arrays.asList(txtBMBemerkungen));
        changeFontSize.changeFontSizeFromTextAreaArrayList(textAreaArrayList, newFontSize);
    }



    /**
     * Setzt die Bemerkung des angegebenen Haushalts.
     * @param haushalt Der Haushalt, dessen Bemerkung gesetzt werden soll.
     */
    public void setzeBemerkung(Haushalt haushalt)
    {

        txtBMBemerkungen.setText(haushalt.getBemerkungen());
        this.haushalt = haushalt;
    }
    /**
     * Gibt den Haushalt zurück.
     * @return Der Haushalt.
     */
    public Haushalt getHaushalt()
    {
        return haushalt;
    }


}
