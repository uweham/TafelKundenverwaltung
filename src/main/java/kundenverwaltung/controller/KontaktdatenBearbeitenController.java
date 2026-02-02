package kundenverwaltung.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.HaushaltDAOimpl;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;
import kundenverwaltung.toolsandworkarounds.FirstLetterToUppercase;

/**
 * * HaushaltVerwaltenController.java.
 *
 * @Author Gruppe_1
 * @Author Adam Starobrzanski Date: 21.07.2018
 * @Version: 2.2
 * <p>
 * last Change:
 */

public class KontaktdatenBearbeitenController
{
    private ChangeFontSize changeFontSize = new ChangeFontSize();

    //Kontaktdaten bearbeiten

    @FXML
    private Label labelHeading;
    @FXML
    private Label labelStreet;
    @FXML
    private Label labelHouseNumber;
    @FXML
    private Label labelPostcode;
    @FXML
    private Label labelLocation;
    @FXML
    private Label labelPhoneNumber;
    @FXML
    private Label labelMobilNumber;
    @FXML
    private TextField txtKDStrasse;
    @FXML
    private TextField txtKDPostleitzahl;
    @FXML
    private TextField txtKDTelefonnummer;
    @FXML
    private TextField txtKDHausnummer;
    @FXML
    private TextField txtKDWohnort;
    @FXML
    private TextField txtKDMobiltelefon;
    @FXML
    private Button btnKontaktdatenOK;
    @FXML
    private Button btnKontaktdatenAbbrechen;


    private Haushalt haushalt;

    //Kontaktdaten bearbeiten


    /**
     * Handles the OK button action. Updates the household contact details if all fields are valid.
     */
    @FXML public void btnKontaktdatenOK()
    {

        if (pruefeFelderHaushalt())
        {
          if (!kundenverwaltung.toolsandworkarounds.CheckUserInput.checkPLZLength(txtKDPostleitzahl.getText())) 
          {
              Benachrichtigung.warnungBenachrichtigung(
                  "Ungültige Postleitzahl", 
                  "Die Postleitzahl muss genau 5 Stellen haben."
              );
              return;
          }
            haushalt.setStrasse(txtKDStrasse.getText().trim());     // inihibit first letter Uppercase
            haushalt.setHausnummer(txtKDHausnummer.getText().trim());
            haushalt.setMobilnummer(txtKDMobiltelefon.getText().trim());
            haushalt.setTelefonnummer(txtKDTelefonnummer.getText().trim());
            haushalt.getPlz().setPlz(txtKDPostleitzahl.getText().trim());
            haushalt.getPlz().setOrt(ersteBuchstabenGross(txtKDWohnort.getText()));

            Boolean checkUpdate = new HaushaltDAOimpl().update(haushalt);

            if (checkUpdate)
            {
                Benachrichtigung.infoBenachrichtigung("Bearbeiten erfolgreich.",
                        "Die Kontaktdaten wurden erfolgreich bearbeitet..");
                Stage stage = (Stage) btnKontaktdatenOK.getScene().getWindow();
                stage.close();
            } else
            {
                Benachrichtigung
                        .warnungBenachrichtigung("Kontaktdaten konnte nicht bearbeitet werden.",
                                "Bitte überprüfen Sie Ihre Verbindung zur Datenbank und probieren"
                        +
                                        " Sie es erneut.");
            }
        } else
        {
            Benachrichtigung
                    .infoBenachrichtigung("Achtung!", "Bitte füllen Sie alle Pflichtfelder aus.");
        }
    }

    /**
     * Handles the cancel button action. Closes the current window.
     */

    @FXML public void btnKontaktdatenAbbrechen()
    {
        Stage stage = (Stage) btnKontaktdatenAbbrechen.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the household object.
     *
     * @param haushalt The household object to be set.
     */

    public void setHaushalt(Haushalt haushalt)
    {
        this.haushalt = haushalt;
    }

    /**
     * Gets the household object.
     *
     * @return The household object.
     */

    public Haushalt getHaushalt()
    {
        return haushalt;
    }

    /**
     * Sets the contact details in the UI fields based on the given household object.
     *
     * @param haushalt The household object from which to set the contact details.
     */

    public void kontaktdatenSetzen(Haushalt haushalt)
    {
        txtKDStrasse.setText(haushalt.getStrasse());
        txtKDHausnummer.setText(haushalt.getHausnummer());
        txtKDMobiltelefon.setText(haushalt.getMobilnummer());
        txtKDTelefonnummer.setText(haushalt.getTelefonnummer());
        txtKDWohnort.setText(haushalt.getPlz().getOrt());
        txtKDPostleitzahl.setText(haushalt.getPlz().getPlz());
    }

    /**
     * Checks if all required fields for the household are filled.
     *
     * @return true if all required fields are filled, false otherwise.
     */

    public Boolean pruefeFelderHaushalt()
    {
        return !(txtKDWohnort.getText().trim().isEmpty()
            ||
                txtKDStrasse.getText().trim().isEmpty()
                ||
                txtKDPostleitzahl.getText().trim().isEmpty()
                ||
                txtKDHausnummer.getText().trim().isEmpty());
    }

    /**
     * Converts the first letters of each word in a string to uppercase.
     *
     * @param string The input string.
     * @return The string with the first letters of each word converted to uppercase.
     */

    /**
     * Wandelt den ersten Buchstaben jedes Wortes in Großbuchstaben um.
     * Berücksichtigt Leerzeichen, Bindestriche und Punkte als Trenner.
     */
    public String ersteBuchstabenGross(String string)
    {

        string = string.toLowerCase();
        String ergebnisstring = "";
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(string);
        while (scanner.hasNext())
        {
            String word = scanner.next();
            ergebnisstring += Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ";
        }
        return ergebnisstring.trim();
    }

    /**
     * @Author Adam Starobrzanski This function turns the first character of the Textfield to uppercase
     * UPPERCASE_POSITION defines the Character that should be changed to Uppercase
     */
    private FirstLetterToUppercase firstLetterToUppercase = new FirstLetterToUppercase();

    /**
     * Turns the first character of the TextField to uppercase.
     */
    @FXML
    protected void firstToUppercase()
    {
        firstLetterToUppercase.firstLetterUppercase(txtKDStrasse);
        firstLetterToUppercase.firstLetterUppercase(txtKDWohnort);
    }

    /**
     * Gets the FirstLetterToUppercase object.
     *
     * @return The FirstLetterToUppercase object.
     */
    public FirstLetterToUppercase getFirstLetterToUppercase()
    {
        return firstLetterToUppercase;
    }

    /**
     * Sets the FirstLetterToUppercase object.
     *
     * @param firstLetterToUppercase The FirstLetterToUppercase object to be set.
     */
    public void setFirstLetterToUppercase(FirstLetterToUppercase firstLetterToUppercase)
    {
        this.firstLetterToUppercase = firstLetterToUppercase;
    }




    /**
     * Set the correct font size.
     *
     * @param fontSize
     */
    public void setFontSize(Double fontSize)
    {
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);

        ArrayList<Label> labelArrayList = new ArrayList<>(
                Arrays.asList(labelHeading, labelStreet, labelHouseNumber, labelPostcode,
                        labelLocation, labelPhoneNumber, labelMobilNumber));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        ArrayList<TextField> textFieldArrayList = new ArrayList<>(
                Arrays.asList(txtKDStrasse, txtKDPostleitzahl, txtKDTelefonnummer, txtKDHausnummer,
                        txtKDWohnort, txtKDMobiltelefon));
        changeFontSize.changeFontSizeFromTextFieldArrayList(textFieldArrayList, newFontSize);

        ArrayList<Button> buttonArrayList =
                new ArrayList<>(Arrays.asList(btnKontaktdatenAbbrechen, btnKontaktdatenOK));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);

    }
}


