package kundenverwaltung.controller;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.AusgabegruppeDAOimpl;
import kundenverwaltung.dao.HaushaltDAOimpl;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.model.AusgabeTagZeit;
import kundenverwaltung.model.Ausgabegruppe;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;
import javafx.scene.paint.Color;

public class KundenstammBearbeitenController
{
    private static final ChangeDateFormat CHANGE_DATE_FORMAT = new ChangeDateFormat();
    private ChangeFontSize changeFontSize = new ChangeFontSize();

    //Kundenstamm Bearbeiten

    @FXML
    private Label labelHeading;
    @FXML
    private Label labelCustomerSince;
    @FXML
    private Label labelVerteilstelle;
    @FXML
    private Label labelOutputGroup;
    @FXML
    private Label labelGroupColor;
    @FXML
    private Label labelOutputTimes;
    @FXML
    private Label labelDataProtection;
    @FXML
    private Label labelHousholdIsArchivedInfo;
    @FXML
    private ComboBox<Verteilstelle> cbVerteilstelle;
    @FXML
    private ComboBox<Ausgabegruppe> cbAusgabegruppe;
    @FXML
    private CheckBox cbxOertlicheKdnr;
    @FXML
    private CheckBox cbxDatenschutz;
    @FXML
    private CheckBox cbxHaushaltArchiviert;
    @FXML
    private CheckBox cbxHaushaltGesperrt;
    @FXML
    private CheckBox cbxHaushaltBeliefern;
    @FXML
    private TextField txtKSOertlicheKdnr;
    @FXML
    private Rectangle gruppenfarbe;
    @FXML
    private Label lbAusgabetage;
    @FXML
    private Label lbAusgabezeit;
    @FXML
    private DatePicker dateKundeSeit;
    @FXML
    private Button btnAbbrechen;
    @FXML
    private Button btnOk;
    private Haushalt haushalt;


    /**
     * Initializes the controller class.
     * This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    public void initialize()
    {
        ArrayList<Verteilstelle> verteilstellen = new VerteilstelleDAOimpl().readAll();
        ObservableList<Verteilstelle> vliste = FXCollections.observableArrayList(verteilstellen);
        cbVerteilstelle.setItems(vliste);

        ArrayList<Ausgabegruppe> ausgabegruppe = new AusgabegruppeDAOimpl().readAll();
        ObservableList<Ausgabegruppe> aliste = FXCollections.observableArrayList(ausgabegruppe);
        cbAusgabegruppe.setItems(aliste);

        dateKundeSeit.setConverter(CHANGE_DATE_FORMAT.convertDatePickerFormat());
    }
    /**
     * Sets the font size for the UI components.
     *
     * @param fontSize the font size to set
     */
    public void setFontSize(Double fontSize)
    {
        DoubleProperty primaryFontSize = new SimpleDoubleProperty(fontSize);
        DoubleProperty secondaryFontSize = new SimpleDoubleProperty(fontSize - ChangeFontSize.getDifferenceBetweenPrimarySecondaryFontsize());

        ArrayList<Label> primaryLabelArrayList =  new ArrayList<>(Arrays.asList(
                    labelHeading, labelCustomerSince, labelOutputGroup, labelGroupColor, labelOutputTimes,
                    labelDataProtection, labelVerteilstelle, lbAusgabezeit
                ));
        ArrayList<Label> secondaryLabelArrayList = new ArrayList<>(Arrays.asList(labelHousholdIsArchivedInfo));
        changeFontSize.changeFontSizeFromLabelArrayList(primaryLabelArrayList, primaryFontSize);
        changeFontSize.changeFontSizeFromLabelArrayList(secondaryLabelArrayList, secondaryFontSize);

        ArrayList<DatePicker> datePickerArrayList = new ArrayList<>(Arrays.asList(dateKundeSeit));
        changeFontSize.changeFontSizeFromDatePickerArrayList(datePickerArrayList, primaryFontSize);

        @SuppressWarnings("rawtypes")
		ArrayList<ComboBox> comboBoxArrayList = new ArrayList<>(Arrays.asList(cbAusgabegruppe, cbVerteilstelle));
        changeFontSize.changeFontSizeFromComboBoxArrayList(comboBoxArrayList, primaryFontSize);

        ArrayList<CheckBox> primaryCheckBoxArrayList = new ArrayList<>(Arrays.asList(
                    cbxHaushaltGesperrt, cbxHaushaltArchiviert, cbxHaushaltBeliefern
                ));
        ArrayList<CheckBox> secondaryCheckBoxArrayList  = new ArrayList<>(Arrays.asList(cbxDatenschutz));
        changeFontSize.changeFontSizeFromCheckBoxArrayList(primaryCheckBoxArrayList, primaryFontSize);
        changeFontSize.changeFontSizeFromCheckBoxArrayList(secondaryCheckBoxArrayList, secondaryFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(btnAbbrechen, btnOk));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, primaryFontSize);
    }


    /**
     * Updates the customer data.
     */

    @FXML
    public void btnKundenstammdatenAendern()
    {

        haushalt.setKundeSeit(dateKundeSeit.getValue());

        haushalt.setVerteilstelle(cbVerteilstelle.getValue());
        haushalt.setAusgabegruppe(cbAusgabegruppe.getValue());

        if (cbxHaushaltGesperrt.isSelected())
        {
            haushalt.setIstGesperrt(true);
        } else
        {
            haushalt.setIstGesperrt(false);
        }

        if (cbxHaushaltArchiviert.isSelected())
        {
            haushalt.setIstArchiviert(true);
        } else
        {
            haushalt.setIstArchiviert(false);
        }

        if (cbxHaushaltBeliefern.isSelected())
        {
            haushalt.setBelieferung(true);
        } else
        {
            haushalt.setBelieferung(false);
        }

        if (cbxDatenschutz.isSelected())
        {
            haushalt.setDatenschutzerklaerung(true);
        } else
        {
            haushalt.setDatenschutzerklaerung(false);
        }


        if (!(dateKundeSeit.getValue() == null || dateKundeSeit.getValue().toString().isEmpty()))
        {
            if (cbxDatenschutz.isSelected())
            {
                Boolean checkUpdate = new HaushaltDAOimpl().update(haushalt);

                if (checkUpdate)
                {
                    Benachrichtigung.infoBenachrichtigung("Bearbeiten erfolgreich.", "Die Kundenstammdaten wurde erfolgreich bearbeitet.");
                    Stage stage = (Stage) btnAbbrechen.getScene().getWindow();
                    stage.close();
                } else
                {
                    Benachrichtigung.warnungBenachrichtigung("Kundenstammdaten konnten nicht bearbeitet werden.", "Bitte überprüfen Sie Ihre Verbindung zur Datenbank und probieren Sie es erneut.");
                }
            } else
            {
                Benachrichtigung.infoBenachrichtigung("Achtung!", "Der Kunde muss die Datenschutzvereinbarung akzeptiert haben.");
            }
        } else
        {
            Benachrichtigung.infoBenachrichtigung("Achtung!", "Bitte füllen Sie alle Pflichtfelder aus.");
        }
    }

    /**
     * Closes the window without saving changes.
     */
    public void btnKundenstammAbbrechen()
    {
        Stage stage = (Stage) btnAbbrechen.getScene().getWindow();
        stage.close();
    }


    /**
     * Sets the output group and updates the group color and times.
     */
    public void setzeAusgabegruppe()
    {

        gruppenfarbe.setFill(cbAusgabegruppe.getValue().getAnzeigeFarbe());
        StringBuilder ausgabezeiten = new StringBuilder();

        for (AusgabeTagZeit element : cbAusgabegruppe.getValue().getAusgabeTagZeiten())
        {
            ausgabezeiten.append(element.getAusgabetag().toString()).append("  ").append(element.getStartzeit()).append("-").append(element.getEndzeit()).append("\n");
        }

        if (ausgabezeiten.toString().length() == 0)
        {
            lbAusgabezeit.setText("Für diese Gruppe wurden keine \n Ausgabezeiten hinterlegt");
        } else
        {
            lbAusgabezeit.setText(ausgabezeiten.toString());
        }



        //setzen der Farbe und der Zeiten
    }
    /**
     * Sets the customer data in the UI components.
     *
     * @param haushalt the customer data to set
     */
    public void setzeKundenstammdaten(Haushalt haushalt)
    {
        this.haushalt = haushalt;
        dateKundeSeit.setValue(haushalt.getKundeSeit());

        // Verteilstelle / Ausgabegruppe in die Comboboxen setzen (null-sicher)
        cbVerteilstelle.setValue(haushalt.getVerteilstelle());
        cbAusgabegruppe.setValue(haushalt.getAusgabegruppe());

        // Farbe & Ausgabezeiten null-sicher befüllen
        if (haushalt.getAusgabegruppe() != null)
        {
            if (haushalt.getAusgabegruppe().getAnzeigeFarbe() != null)
            {
                gruppenfarbe.setFill(haushalt.getAusgabegruppe().getAnzeigeFarbe());
            } else
            {
                gruppenfarbe.setFill(Color.TRANSPARENT);
            }

            StringBuilder ausgabezeiten = new StringBuilder();
            for (AusgabeTagZeit element : haushalt.getAusgabegruppe().getAusgabeTagZeiten())
            {
                ausgabezeiten.append(element.getAusgabetag())
                             .append("  ")
                             .append(element.getStartzeit())
                             .append("-")
                             .append(element.getEndzeit())
                             .append("\n");
            }
            lbAusgabezeit.setText(ausgabezeiten.toString());
        } else
        {
            // Gruppe fehlt -> neutrale Anzeige
            gruppenfarbe.setFill(Color.TRANSPARENT);
            lbAusgabezeit.setText("Für diese Gruppe wurden keine Ausgabezeiten hinterlegt");
        }

        // Checkboxen / Flags
        cbxHaushaltBeliefern.setSelected(haushalt.isBelieferung());
        cbxDatenschutz.setSelected(haushalt.isDatenschutzerklaerung());
        cbxHaushaltGesperrt.setSelected(haushalt.getIstGesperrt());
        cbxHaushaltArchiviert.setSelected(haushalt.getIstArchiviert());
    }

    /**
     * Gets the customer data.
     *
     * @return the customer data
     */
    public Haushalt getHaushalt()
    {
        return haushalt;
    }

}
