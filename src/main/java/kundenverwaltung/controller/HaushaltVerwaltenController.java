package kundenverwaltung.controller;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.dao.HaushaltDAOimpl;
import kundenverwaltung.dao.VollmachtDAOimpl;
import kundenverwaltung.model.AusgabeTagZeit;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Vollmacht;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;

/**
 * * HaushaltVerwaltenController.java.
 *  *
 *  * @Author Gruppe_1
 *  * @Author Adam Starobrzanski
 *  * @Author Richard Kromm
 *  * Date: 21.07.2018
 *  * @Version: 2.2
 *  *
 *  * last Change:
 *         Author: Richard Kromm
 *         Date: 30.07.2018
 *
 */
public class HaushaltVerwaltenController
{
    private static final ChangeDateFormat CHANGE_DATE_FORMAT = new ChangeDateFormat();
    private static final ChangeFontSize CHANGE_FONT_SIZE = new ChangeFontSize();
    private static final String NOTIFICATION_TITEL_DEFAULT = "Achtung!";
    private static final String NOTIFICATION_TEXT_ILLEGAL_DELETE = "Löschen nicht möglich, da Haushaltsvorstand!"
            + "\nWeise Sie erste einem anderen Familienmitglied den Haushaltsvorstand zu.";
    private static final String[] LABEL_NUMBER_OF_POWER_OF_ATTORNEY =  {"Dieser Haushalt hat ", " Vollmachten"};
    private static final int FIRST_ARRAY_POSITION = 0;
    private static final int SECOND_ARRAY_POSITION = 1;

    @SuppressWarnings("static-access")
    private double currentFontSize = CHANGE_FONT_SIZE.getDefaultFontSize();


    //testdaten

    private Haushalt haushalt;
    private Familienmitglied familienmitglied;
    @SuppressWarnings("unused")
    private Haushalt h;

    @FXML
    private TextField txtKundennummer;
    @FXML
    private TextField txtVerteilstelle;
    @FXML
    private TextField txtAusgabegruppe;
    @FXML
    private TextField txtStrasse;
    @FXML
    private TextField txtHausnummer;
    @FXML
    private TextField txtTelefonnummer;
    @FXML
    private TextField txtPostleitzahl;
    @FXML
    private TextField txtWohnort;
    @FXML
    private TextField txtMobiltelefon;
    @FXML
    private TextArea txtAusgabezeiten;
    @FXML
    private TextArea txtBemerkungen;
    @FXML
    private DatePicker dateKundeSeit;
    @FXML
    private TableView<Familienmitglied> tvFamilienmitglieder;
    @FXML
    private TableColumn<Familienmitglied, String> columnAnrede;
    @FXML
    private TableColumn<Familienmitglied, String> columnGender;
    @FXML
    private TableColumn<Familienmitglied, String> columnName;
    @FXML
    private TableColumn<Familienmitglied, String> columnGeburtsdatum;
    @FXML
    private TableColumn<Familienmitglied, String> columnBemerkung;
    @FXML
    private TableColumn<Familienmitglied, String> columnNationalitaet;
    @FXML
    private TableColumn<Familienmitglied, String> columnBerechtigung;
    @FXML
    private TableColumn<Familienmitglied, String> columnTyp;
    @FXML
    private TableColumn<Familienmitglied, String> columnGebuehren;
    @FXML
    private TableColumn<Familienmitglied, String> columnKundenausweis;
    @FXML
    private Label labelHeading;
    @FXML
    private Label labelCustomerId;
    @FXML
    private Label labelCustomerSince;
    @FXML
    private Label labelStreet;
    @FXML
    private Label labelHouseNumber;
    @FXML
    private Label labelPhoneNumber;
    @FXML
    private Label labelPostcode;
    @FXML
    private Label labelPlace;
    @FXML
    private Label labelMobilPhoneNumber;
    @FXML
    private Label labelOutputTimes;
    @FXML
    private Label labelVerteilstelle;
    @FXML
    private Label labelAusgabegruppe;
    @FXML
    private Label lbVollmachten;
    @FXML
    private Label labelGeneral;
    @FXML
    private Label labelContactDetails;
    @FXML
    private Label labelMemberOfTheFamily;
    @FXML
    private Label labelComment;
    @FXML
    private Label labelPowerOfAttorney;
    @FXML
    private Button buttonChangeOutputTimes;
    @FXML
    private Button buttonChangeContactDetails;
    @FXML
    private Button buttonChangeComment;
    @FXML
    private Button buttonEditPowerOfAttorney;
    @FXML
    private Button btnShowDeletedPerson;
    @FXML
    private Button btnEditDecision;
    @FXML
    private Button btnChangePerson;
    @FXML
    private Button btnAddPerson;
    @FXML
    private Button btnDeletePerson;
    @FXML
    private Button btnDeleteHousehold;
    @FXML
    private Button btnExitWindow;

    private ArrayList<Familienmitglied> familienmitgliedListe;
    private ObservableList<Familienmitglied> familienmitglieder = FXCollections.observableArrayList();

    private Boolean haushaltsvorstandVorhanden = true;
    private ArrayList<Vollmacht> vollmachtenliste;
    private Integer anzahlVollmachten;

    /**
     *.
     */
    @SuppressWarnings("unchecked")
    public void manuellInitialize()
    {
        familienmitgliedListe = new FamilienmitgliedDAOimpl().getAllFamilienmitglieder(haushalt.getKundennummer());
        familienmitglieder.addAll(familienmitgliedListe);

        vollmachtenliste = new VollmachtDAOimpl().getAllVollmachtenById(haushalt.getKundennummer());

        columnAnrede.setCellValueFactory(new PropertyValueFactory<>("AnredeString"));
        columnAnrede.setId("AnredeString");

        columnGender.setCellValueFactory(new PropertyValueFactory<>("GenderString"));
        columnGender.setId("GenderString");

        columnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnName.setId("Name");

        columnGeburtsdatum.setCellValueFactory(new PropertyValueFactory<>("BirthdayString"));
        columnGeburtsdatum.setId("BirthdayString");

        columnBemerkung.setCellValueFactory(new PropertyValueFactory<>("Bemerkung"));
        columnBemerkung.setId("Bemerkung");

        columnNationalitaet.setCellValueFactory(new PropertyValueFactory<>("NationString"));
        columnNationalitaet.setId("NationString");

        columnBerechtigung.setCellValueFactory(new PropertyValueFactory<>("BerechtigungString"));
        columnBerechtigung.setId("BerechtigungString");

        columnTyp.setCellValueFactory(new PropertyValueFactory<>("TypString"));
        columnTyp.setId("TypString");

        columnGebuehren.setCellValueFactory(new PropertyValueFactory<>("Gebuehren"));
        columnGebuehren.setId("Gebuehren");

        columnKundenausweis.setCellValueFactory(new PropertyValueFactory<>("Ausweis"));
        columnKundenausweis.setId("Ausweis");

        tvFamilienmitglieder.setItems(familienmitglieder);

        anzahlVollmachten = vollmachtenliste.size();
        lbVollmachten.setText("Dieser Haushalt hat " + anzahlVollmachten + " Vollmachten");

        dateKundeSeit.setConverter(CHANGE_DATE_FORMAT.convertDatePickerFormat());

        TablePreferenceServiceImpl.getInstance().setupPersistence(tvFamilienmitglieder, "tvFamilienMitgleider");
    }

    /**
     *.
     */
    @FXML public void oeffneKundenstammBearbeiten()
    {
        haushalt = MainController.getInstance().oeffneKundenstammBearbeiten(haushalt, currentFontSize);
        fuelleFelder(haushalt);
    }

    /**
     *.
     */

    @FXML public void oeffneVollmachtenBearbeiten()
    {

        anzahlVollmachten = MainController.getInstance().oeffneVollmachtenAnzeigen(haushalt, currentFontSize);
        lbVollmachten.setText("Dieser Haushalt hat " + anzahlVollmachten + " Vollmachten");
    }


    /**
     *.
     */
    @FXML public void oeffneKontaktdatenBearbeiten()
    {

        haushalt = MainController.getInstance().oeffneKontaktdatenBearbeiten(haushalt, currentFontSize);
        fuelleFelder(haushalt);
    }


    /**
     *.
     */
    @FXML public void oeffnePersonenAendern()
    {
        familienmitglied = null;
        @SuppressWarnings("unused")
        Integer index = tvFamilienmitglieder.getSelectionModel().getSelectedIndex();
        familienmitglied = tvFamilienmitglieder.getSelectionModel().getSelectedItem();

        if (familienmitglied != null)
        {

            Boolean haushaltsvorstandVorher = familienmitglied.isHaushaltsVorstand();

            //familienmitglieder.remove(index + 1);
            familienmitglied =
                    MainController.getInstance().oeffnePersonAendern(familienmitglied, haushalt, currentFontSize);

            Boolean haushaltsvorstandNachher = familienmitglied.isHaushaltsVorstand();

            if (haushaltsvorstandVorher && !haushaltsvorstandNachher)
            {
                haushaltsvorstandVorhanden = false;
            } else if (!haushaltsvorstandVorher && haushaltsvorstandNachher)
            {
                haushaltsvorstandVorhanden = true;
            }

            //personhinzufuegen(familienmitglied);
            personenAktualisieren();
        } else
        {
            Benachrichtigung.infoBenachrichtigung("Keine Person ausgewählt.",
                    "Bitte wählen Sie eine Person aus, die bearbeitet werden soll.");
        }

    }

    /**
     *.
     */

    @FXML public void oeffnePersonHinzufuegen()
    {

        familienmitglied = MainController.getInstance().oeffnePersonAendern(null, haushalt, currentFontSize);
        //familienmitglied.setBerechtigung(new Berechtigung(2,"test"));
        // familienmitglied.setHaushalt(haushalt);
        personhinzufuegen(familienmitglied);
    }

    /**
     *.
     */

    @FXML public void oeffneBemerkungenBearbeiten()
    {
        haushalt = MainController.getInstance().oeffneBemerkungenBearbeiten(haushalt, currentFontSize);
        txtBemerkungen.setText(haushalt.getBemerkungen());
    }

    /**
     *.
     */
    @FXML public void btnHaushaltLoeschen()
    {

        Boolean checkLoeschenOk = Benachrichtigung.deleteConfirmationDialog(
                "Wenn Sie diesen Haushalt endgültig löschen möchten, dann geben Sie in dieses Textfeld bitte das Wort 'LÖSCHEN' ein.",
                "Haushalt löschen", "Abbrechen", "diesen Haushalt");

        if (checkLoeschenOk)
        {
            Boolean checkDelete = new HaushaltDAOimpl().delete(haushalt);

            if (checkDelete)
            {
                System.out.println("Haushalt gelöscht");
                Stage stage = (Stage) txtBemerkungen.getScene().getWindow();
                stage.close();
            } else
            {
                System.out.println("Löschen fehlgeschlagen");
            }
        }

    }


    /**
     *.
     */
    @FXML public void btnSchliessen()
    {

        if (haushaltsvorstandVorhanden)
        {
            Stage stage = (Stage) txtKundennummer.getScene().getWindow();
            stage.close();
        } else
        {
            Benachrichtigung.warnungBenachrichtigung("Kein Haushaltsvorstand vorhanden.",
                    "Bitte wählen Sie einen Haushaltsvorstand für diesen Haushalt (Person ändern "
            +
                            "-> 'Haushaltsvorstand' anchecken).");
        }

    }


    /**
     *.
     */
    @FXML public void oeffneBescheideBearbeiten()
    {

        MainController.getInstance().oeffneBescheideBearbeiten(haushalt, currentFontSize);

    }

    /**
     *.
     */

    public void setHaushalt(Haushalt haushalt)
    {
        this.haushalt = haushalt;
    }

    /**
     *.
     */

    public void setFamilienmitglied(Familienmitglied familienmitglied)
    {
        this.familienmitglied = familienmitglied;
    }

    /**
     *.
     */

    public void fuelleFelder(Haushalt haushalt)
    {

        // Allgemeines
        this.haushalt = haushalt;

        txtKundennummer.setText(String.valueOf(haushalt.getKundennummer()));
        dateKundeSeit.setValue(haushalt.getKundeSeit());

        if (haushalt.getVerteilstelle() != null)
        {
            txtVerteilstelle.setText(haushalt.getVerteilstelle().getBezeichnung());
        } else
        {
            txtVerteilstelle.setText("");
        }

        if (haushalt.getAusgabegruppe() != null)
        {
            txtAusgabegruppe.setText(haushalt.getAusgabegruppe().getName());
            StringBuilder ausgabezeiten = new StringBuilder();

            for (AusgabeTagZeit element : haushalt.getAusgabegruppe().getAusgabeTagZeiten())
            {
                ausgabezeiten.append(element
                        .getAusgabetag()
                        .getAbkuerzung()).append(" , ")
                        .append(element.getStartzeit())
                        .append("-").append(element.getEndzeit())
                        .append("\n");
            }

            txtAusgabezeiten.setText(ausgabezeiten.toString());
        } else
        {
            // Gruppe fehlt -> neutral anzeigen
            txtAusgabegruppe.setText("—");
            txtAusgabezeiten.setText("");
        }

        // Kontaktdaten
        txtStrasse.setText(haushalt.getStrasse());
        txtHausnummer.setText(haushalt.getHausnummer());
        txtTelefonnummer.setText(haushalt.getTelefonnummer());
        txtPostleitzahl.setText(haushalt.getPlz().getPlz());
        txtWohnort.setText(haushalt.getPlz().getOrt());
        txtMobiltelefon.setText(haushalt.getMobilnummer());

        // Bemerkungen
        txtBemerkungen.setText(haushalt.getBemerkungen());
    }



    /**
     *.
     */
    public void personenAktualisieren()
    {
        familienmitgliedListe = new FamilienmitgliedDAOimpl().getAllFamilienmitglieder(haushalt.getKundennummer());
        familienmitglieder.clear();
        familienmitglieder = FXCollections.observableArrayList(familienmitgliedListe);
        tvFamilienmitglieder.setItems(familienmitglieder);
    }

    /**
     *.
     */

    public void personhinzufuegen(Familienmitglied familienmitglied)
    {
        familienmitgliedListe.add(familienmitglied);
        familienmitglieder.add(familienmitglied);
        tvFamilienmitglieder.setItems(familienmitglieder);
    }
    /**
     *.
     */
    public Haushalt getHaushalt()
    {
        return haushalt;
    }
    /**
     *.
     */
    public ArrayList<Familienmitglied> getFamilienmitgliedListe()
    {
        return familienmitgliedListe;
    }





    /**
     * This function open the "delete famaliymember window".
     */
    @FXML
    public void openDeletePerson()
    {
        familienmitglied = null;
        familienmitglied = tvFamilienmitglieder.getSelectionModel().getSelectedItem();

        if (familienmitglied != null && !familienmitglied.isHaushaltsVorstand())
        {
                familienmitglied = MainController.getInstance().openDeletePerson(familienmitglied, currentFontSize);
                refreshlblVollmachten();
        } else
            {
                Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_TEXT_ILLEGAL_DELETE);
            }
        personenAktualisieren();
    }

    /**
     * This function refresh the label with number of power of attorney.
     */
    public void refreshlblVollmachten()
    {
        lbVollmachten.setText(LABEL_NUMBER_OF_POWER_OF_ATTORNEY[FIRST_ARRAY_POSITION]
                + vollmachtenliste.size()
                + LABEL_NUMBER_OF_POWER_OF_ATTORNEY[SECOND_ARRAY_POSITION]);
    }

    /**
     * This function open "show deleted persons window".
     */
    @FXML
    public void showDeletedPersons()
    {
        if (haushalt != null)
        {
            MainController.getInstance().showDeletedPersons(haushalt, currentFontSize);
        }
    }

    /**
     * set the correct font size.
     *
     * @param fontSize
     */
    public void setFontSize(Double fontSize)
    {
        currentFontSize = fontSize;
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);
        DoubleProperty secondaryFontSize = new SimpleDoubleProperty(fontSize
                - ChangeFontSize.getDifferenceBetweenPrimarySecondaryFontsize());

        ArrayList<Label> labelArrayListPrimaryFontSize = new ArrayList<>(Arrays.asList(
                        labelHeading, labelCustomerId, labelCustomerSince, labelStreet, labelHouseNumber,
                        labelPhoneNumber, labelPostcode, labelPlace, labelMobilPhoneNumber, labelOutputTimes,
                        labelVerteilstelle, labelAusgabegruppe, lbVollmachten
                ));
        ArrayList<Label> labelArrayListSecondaryFontSize = new ArrayList<>(Arrays.asList(
                        labelGeneral, labelContactDetails, labelMemberOfTheFamily, labelComment, labelPowerOfAttorney
                ));
        CHANGE_FONT_SIZE.changeFontSizeFromLabelArrayList(labelArrayListPrimaryFontSize, newFontSize);
        CHANGE_FONT_SIZE.changeFontSizeFromLabelArrayList(labelArrayListSecondaryFontSize, secondaryFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(
                        buttonChangeOutputTimes, buttonChangeContactDetails, buttonChangeComment,
                        buttonEditPowerOfAttorney, btnShowDeletedPerson, btnEditDecision, btnChangePerson, btnAddPerson,
                        btnDeletePerson, btnDeleteHousehold, btnExitWindow
                ));
        CHANGE_FONT_SIZE.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);

        ArrayList<TextField> textFieldArrayList = new ArrayList<>(Arrays.asList(
                        txtKundennummer, txtVerteilstelle, txtAusgabegruppe, txtStrasse,
                        txtHausnummer, txtTelefonnummer, txtPostleitzahl, txtWohnort, txtMobiltelefon
                ));
        CHANGE_FONT_SIZE.changeFontSizeFromTextFieldArrayList(textFieldArrayList, newFontSize);

        ArrayList<TextArea> textAreaArrayList = new ArrayList<>(Arrays.asList(txtAusgabezeiten, txtBemerkungen));
        CHANGE_FONT_SIZE.changeFontSizeFromTextAreaArrayList(textAreaArrayList, newFontSize);

        @SuppressWarnings("rawtypes")
        ArrayList<TableColumn> tableColumnArrayList = new ArrayList<>(Arrays.asList(
                        columnAnrede, columnGender, columnName, columnGeburtsdatum,
                        columnBemerkung, columnNationalitaet, columnBerechtigung, columnTyp, columnGebuehren,
                        columnKundenausweis
                ));
        CHANGE_FONT_SIZE.changeFontSizeFromTableColumnArrayList(tableColumnArrayList, newFontSize);
    }

}
