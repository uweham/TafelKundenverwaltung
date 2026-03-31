package kundenverwaltung.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.dao.VollmachtDAOimpl;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Vollmacht;
import kundenverwaltung.service.Constants;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;

public class VollmachtenController
{

    private  ChangeDateFormat changeDateFormat = new ChangeDateFormat();
    private ChangeFontSize changeFontSize = new ChangeFontSize();
    @SuppressWarnings("static-access")
	private Double currentFontSize = changeFontSize.getDefaultFontSize();


    @FXML
    private Label labelHeadingShow;
    @FXML
    private Label labelCurrentPowerOfAttorney;
    @FXML
    private TableView<Vollmacht> tvBestehendeVollmachten;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnBestehendEmpfaenger;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnBestehendKdnr;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnBestehendAusstelldatum;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnBestehendAblaufdatum;
    @FXML
    private TableView<Familienmitglied> tvVollmachtenPersonenSuche;
    @FXML
    private TextField txtVollmachtNachname;
    @FXML
    private Button btnAddPowerOfAttorneyShow;
    @FXML
    private Button btnEditPowerOfAttorneyShow;
    @FXML
    private Button btnDeletePowerOfAttorneyShow;
    @FXML
    private Button btnAnzeigenSchliessen;


    //edit power of attorney
    @FXML
    private Label labelHeadingAdd;
    @FXML
    private Label labelSearchPerson;
    @FXML
    private Label labelPeriodOfValidity;
    @FXML
    private Label labelOr;
    @FXML
    private Label labelSurname;
    @FXML
    private Label labelDateOfExpiry;
    @FXML
    private Label labelValidFrom;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnVollmachtName;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnVollmachtGeburtsdatum;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnVollmachtAnschrift;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnVollmachtWohnort;
    @FXML
    private Button btnSearchPerson;
    @FXML
    private Button btnSavaChange;
    @FXML
    private Button btnHinzufuegenSchliessen;
    @FXML
    private DatePicker dateVollmachtGueltigAb;
    @FXML
    private DatePicker dateVollmachtGueltigBis;
    @FXML
    private CheckBox cbxVollmachtUnbegrenzt;




    private Boolean neuHinzufuegen;
    private Integer anzahlVollmachten;

    private Vollmacht vollmacht;

    private ObservableList<Familienmitglied> empfaenger = FXCollections.observableArrayList();
    private Haushalt haushalt;
    private Familienmitglied familienmitglied;
    //private Familienmitglied familienmitglied = new Familienmitglied(1,haushalt,new Anrede(31), "Max", "Mustermann", LocalDate.of(2000,1,1), "Bemerkung", true, true, false, new Nation(1,"Deutschland", "deutsch"), new Berechtigung(1,"Darf vor ohne zu warten"), true, LocalDateTime.now(), LocalDateTime.now());

    private ArrayList<Vollmacht> vollmachtenliste = new ArrayList<>();
    private ObservableList<Vollmacht> bestehendeVollmachten = FXCollections.observableArrayList();

    private ArrayList<Familienmitglied> familienmitglieder = new ArrayList<>();
    private ObservableList<Familienmitglied> familienmitgliederListe = FXCollections.observableArrayList();


    /**
     * Initializes the table for existing powers of attorney.
     */
    @SuppressWarnings("unchecked")
    public void erstelleTabelleBestehendeVollmachten()
    {
        columnBestehendEmpfaenger.setCellValueFactory(new PropertyValueFactory<>("Empfaenger"));
        columnBestehendEmpfaenger.setId("Empfaenger");

        columnBestehendKdnr.setCellValueFactory(new PropertyValueFactory<>("EmpfaengerNr"));
        columnBestehendKdnr.setId("EmpfaengerNr");

        columnBestehendAusstelldatum.setCellValueFactory(new PropertyValueFactory<>("DateOfIssue"));
        columnBestehendAusstelldatum.setId("DateOfIssue");

        columnBestehendAblaufdatum.setCellValueFactory(new PropertyValueFactory<>("DateOfExpiry"));
        columnBestehendAblaufdatum.setId("DateOfExpiry");

        vollmachtenliste = new VollmachtDAOimpl().getAllVollmachtenById(haushalt.getKundennummer());
        bestehendeVollmachten.clear();
        bestehendeVollmachten.addAll(vollmachtenliste);

        anzahlVollmachten = bestehendeVollmachten.size();
        tvBestehendeVollmachten.setItems(bestehendeVollmachten);

        TablePreferenceServiceImpl.getInstance().setupPersistence(tvBestehendeVollmachten, "BestehendeVollmachten");
    }

    /**
     * Initializes the table for adding a power of attorney.
     */
    @SuppressWarnings("unchecked")
    public void erstelleTabelleVollmachthinzufuegen()
    {
        columnVollmachtName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnVollmachtName.setId("Name");

        columnVollmachtAnschrift.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
        columnVollmachtAnschrift.setId("Adresse");

        columnVollmachtWohnort.setCellValueFactory(new PropertyValueFactory<>("Wohnort"));
        columnVollmachtWohnort.setId("Wohnort");

        columnVollmachtGeburtsdatum.setCellValueFactory(new PropertyValueFactory<>("BirthdayString"));
        columnVollmachtGeburtsdatum.setId("BirthdayString");

        dateVollmachtGueltigAb.setConverter(changeDateFormat.convertDatePickerFormat());
        dateVollmachtGueltigBis.setConverter(changeDateFormat.convertDatePickerFormat());
        dateVollmachtGueltigAb.setValue(LocalDate.now());
        dateVollmachtGueltigBis.setValue(LocalDate.now());

        empfaenger.add(familienmitglied);
        tvVollmachtenPersonenSuche.setItems(empfaenger);

        TablePreferenceServiceImpl.getInstance().setupPersistence(tvVollmachtenPersonenSuche, "VollmachtenPersonenSuche");
    }


    /**
     * Opens the window to add a power of attorney.
     */
    @FXML
    public void btnVollmachtenAnzeigenHinzufuegen()
    {

        Vollmacht neueVollmacht = MainController.getInstance().oeffneVollmachtenHinzufuegen(haushalt, null, currentFontSize);

        if (neueVollmacht != null)
        {
            bestehendeVollmachten.add(neueVollmacht);
            tvBestehendeVollmachten.setItems(bestehendeVollmachten);
            anzahlVollmachten++;
        }
       // System.out.println(haushalt.getBemerkungen());
    }
    /**
     * Opens the window to edit a selected power of attorney.
     */
    @FXML
    public void btnVollmachtenAnzeigenBearbeiten()
    {

        vollmacht = null;
        vollmacht = tvBestehendeVollmachten.getSelectionModel().getSelectedItem();

        if (vollmacht != null)
        {
            vollmacht = MainController.getInstance().oeffneVollmachtenHinzufuegen(haushalt, vollmacht, currentFontSize);

            tvBestehendeVollmachten.refresh();

        } else
        {
            Benachrichtigung.infoBenachrichtigung("Keine Vollmacht ausgewählt.", "Bitte wählen Sie eine bestehende Vollmacht aus, die bearbeitet werden soll.");
        }
    }
    /**
     * Deletes a selected power of attorney.
     */
    @FXML
    public void btnVollmachtenAnzeigenLoeschen()
    {

        Vollmacht loeschendeVollmacht = tvBestehendeVollmachten.getSelectionModel().getSelectedItem();
       // Integer index = tvBestehendeVollmachten.getSelectionModel().getSelectedIndex();

        if (loeschendeVollmacht != null)
        {

            Boolean checkDelete = new VollmachtDAOimpl().delete(loeschendeVollmacht);

            if (checkDelete)
            {
                Benachrichtigung.infoBenachrichtigung("Löschen erfolgreich.", "Die Vollmacht wurde erfolgreich gelöscht..");

                vollmachtenliste = new VollmachtDAOimpl().getAllVollmachtenById(haushalt.getKundennummer());
                bestehendeVollmachten.clear();
                bestehendeVollmachten.addAll(vollmachtenliste);
                tvBestehendeVollmachten.refresh();
                anzahlVollmachten--;
            } else
            {
                Benachrichtigung.warnungBenachrichtigung("Vollmacht konnte nicht gelöscht werden.", "Bitte überprüfen Sie Ihre Verbindung zur Datenbank und probieren Sie es erneut.");
            }
        } else
        {
            Benachrichtigung.infoBenachrichtigung("Keine Vollmacht ausgewählt.", "Bitte wählen Sie eine bestehende Vollmacht aus, die gelöscht werden soll.");

        }
    }
    /**
     * Closes the current window.
     */
    @FXML
    public void btnVollmachtenAnzeigenSchliessen()
    {
        Stage stage = (Stage) btnAnzeigenSchliessen.getScene().getWindow();
        stage.close();
    }
    /**
     * Adds or edits a power of attorney based on the user input.
     */
    @FXML
    public void btnVollmachtAnlegenOK()
    {

        familienmitglied = null;
        familienmitglied = tvVollmachtenPersonenSuche.getSelectionModel().getSelectedItem();

        Boolean checkUpdate;

        if (familienmitglied == null && familienmitgliederListe.size() == 1)
        {
            familienmitglied = familienmitgliederListe.get(0);
        }

        if (familienmitglied != null)
        {
            if (pruefeFelder())
            {

                LocalDate vollmachtAb = dateVollmachtGueltigAb.getValue();
                LocalDate vollmachtBis;


                if (cbxVollmachtUnbegrenzt.isSelected())
                {
                    vollmachtBis = LocalDate.of(9999, 12, 31);
                } else
                {
                    vollmachtBis = dateVollmachtGueltigBis.getValue();
                }

                if ((cbxVollmachtUnbegrenzt.isSelected()) || (!vollmachtAb.isAfter(vollmachtBis)))
                {

                    if (neuHinzufuegen)
                    {
                        vollmacht = new Vollmacht(haushalt, familienmitglied, vollmachtAb, vollmachtBis);
                        checkUpdate = new VollmachtDAOimpl().create(vollmacht);
                    } else
                    {
                        vollmacht.setAblaufDatum(vollmachtBis);
                        vollmacht.setAusgestelltAm(vollmachtAb);

                        checkUpdate = new VollmachtDAOimpl().update(vollmacht);
                    }

                    if (checkUpdate)
                    {
                        Benachrichtigung.infoBenachrichtigung("Bearbeiten erfolgreich.", "Die Vollmacht wurde erfolgreich bearbeitet..");
                        Stage stage = (Stage) btnHinzufuegenSchliessen.getScene().getWindow();
                        stage.close();
                    } else
                    {
                        Benachrichtigung.warnungBenachrichtigung("Vollmacht konnte nicht bearbeitet werden.", "Bitte überprüfen Sie Ihre Verbindung zur Datenbank und probieren Sie es erneut.");
                    }


                } else
                {
                    Benachrichtigung.infoBenachrichtigung("Achtung!", "Das Anfangsdatum darf nicht nach dem Enddatum liegen.");

                }
            } else
            {
                Benachrichtigung.infoBenachrichtigung("Achtung!", "Bitte füllen Sie alle Pflichtfelder aus.");
                }
            } else
            {
            Benachrichtigung.infoBenachrichtigung("Kein Bevollmächtigter ausgewählt", "Bitte wählen Sie eine Person aus, die bevollmächtigt werden soll.");
            }
        }


    /**
     * Closes the current window.
     */
    @FXML
    public void btnVollmachtAnlegenAbbrechen()
    {
        Stage stage = (Stage) btnHinzufuegenSchliessen.getScene().getWindow();
        stage.close();
    }
    /**
     * Searches for persons to be granted a power of attorney based on their surname.
     */
    @FXML
    public void btnSuchePersonFuerVollmacht()
    {

        String nachname = txtVollmachtNachname.getText();
        familienmitglieder.clear();
        familienmitgliederListe.clear();
        familienmitglieder = new FamilienmitgliedDAOimpl().getAllFamilienmitglieder(nachname, Constants.SEARCH_SURNAME_INDEX, false);
        familienmitgliederListe.addAll(familienmitglieder);

        tvVollmachtenPersonenSuche.setItems(familienmitgliederListe);
    }
    /**
     * Sets the state of the unlimited power of attorney checkbox.
     */
    @FXML
    public void setzeVollmachtUnbegrenzt()
    {

        if (cbxVollmachtUnbegrenzt.isSelected())
        {

            dateVollmachtGueltigBis.setDisable(true);
        } else
        {
            dateVollmachtGueltigBis.setDisable(false);
        }
    }


    /**
     * Sets the data for editing or adding a power of attorney.
     *
     * @param vollmacht the power of attorney to be edited, or null if adding a new one.
     * @param neuHinzufuegen true if a new power of attorney is being added, false if editing an existing one.
     */
    public void setzeDaten(Vollmacht vollmacht, Boolean neuHinzufuegen)
    {

        this.neuHinzufuegen = neuHinzufuegen;


        if (vollmacht != null)
        {
            this.vollmacht = vollmacht;
            familienmitgliederListe.clear();
            familienmitgliederListe.add(vollmacht.getBevollmaechtigtePerson());
            tvVollmachtenPersonenSuche.setItems(familienmitgliederListe);

            dateVollmachtGueltigBis.setConverter(changeDateFormat.convertDatePickerFormat());
            dateVollmachtGueltigAb.setConverter(changeDateFormat.convertDatePickerFormat());
            dateVollmachtGueltigAb.setValue(vollmacht.getAusgestelltAm());
            dateVollmachtGueltigBis.setValue(vollmacht.getAblaufDatum());
        }

    }

    public void aktualisiereListe()
    {

    }
    /**
     * Checks if the required fields for a power of attorney are filled.
     *
     * @return true if all required fields are filled, false otherwise.
     */
    public Boolean pruefeFelder()
    {
        return !((dateVollmachtGueltigAb.getValue() == null || dateVollmachtGueltigAb.getValue().toString().isEmpty())
                ||
                ((dateVollmachtGueltigBis.getValue() == null || dateVollmachtGueltigBis.getValue().toString().isEmpty()) && !cbxVollmachtUnbegrenzt.isSelected()));
    }


    /**
     * Set the correct font size in VollmachtenAnzeigen.fxml.
     * @param fontSize
     */
    public void setFontSizeShowPowerOfAttorney(Double fontSize)
    {
        setCurrentFontSize(fontSize);
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);

        ArrayList<Label> labelArrayList = new ArrayList<>(Arrays.asList(labelHeadingShow, labelCurrentPowerOfAttorney));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        @SuppressWarnings("rawtypes")
		ArrayList<TableColumn> tableColumnArrayList = new ArrayList<>(Arrays.asList(columnBestehendEmpfaenger, columnBestehendKdnr, columnBestehendAusstelldatum, columnBestehendAblaufdatum));
        changeFontSize.changeFontSizeFromTableColumnArrayList(tableColumnArrayList, newFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(btnAddPowerOfAttorneyShow, btnEditPowerOfAttorneyShow, btnDeletePowerOfAttorneyShow, btnAnzeigenSchliessen));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);
    }

    /**
     * Set the correct font size in VollmachtenErstellen.fxml.
     * @param fontSize
     */
    public void setFontSizeEditPowerOfAttorney(Double fontSize)
    {
        DoubleProperty primarayFontSize = new SimpleDoubleProperty(fontSize);
        DoubleProperty secondaryFontSize = new SimpleDoubleProperty(fontSize - ChangeFontSize.getDifferenceBetweenPrimarySecondaryFontsize());

        ArrayList<Label> primaryLabelArrayList = new ArrayList<>(Arrays.asList(
                        labelHeadingAdd, labelSearchPerson, labelPeriodOfValidity, labelOr
                ));
        changeFontSize.changeFontSizeFromLabelArrayList(primaryLabelArrayList, primarayFontSize);

        ArrayList<Label> secondaryLabelArrayList = new ArrayList<>(Arrays.asList(
                        labelSurname, labelDateOfExpiry, labelValidFrom
                ));
        changeFontSize.changeFontSizeFromLabelArrayList(secondaryLabelArrayList, secondaryFontSize);

        @SuppressWarnings("rawtypes")
		ArrayList<TableColumn> tableColumnArrayList = new ArrayList<>(Arrays.asList(
                        columnVollmachtName, columnVollmachtGeburtsdatum, columnVollmachtAnschrift, columnVollmachtWohnort
                ));
        changeFontSize.changeFontSizeFromTableColumnArrayList(tableColumnArrayList, primarayFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(btnSearchPerson, btnSavaChange, btnHinzufuegenSchliessen));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, primarayFontSize);

        ArrayList<DatePicker> datePickerArrayList = new ArrayList<>(Arrays.asList(dateVollmachtGueltigAb, dateVollmachtGueltigBis));
        changeFontSize.changeFontSizeFromDatePickerArrayList(datePickerArrayList, primarayFontSize);

        ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>(Arrays.asList(cbxVollmachtUnbegrenzt));
        changeFontSize.changeFontSizeFromCheckBoxArrayList(checkBoxArrayList, primarayFontSize);
    }



    /**
     * Gets the current font size.
     *
     * @return the current font size.
     */
    public Double getCurrentFontSize()
    {
        return currentFontSize;
    }
    /**
     * Sets the current font size.
     *
     * @param currentFontSize the new font size to be set.
     */
    public void setCurrentFontSize(Double currentFontSize)
    {
        this.currentFontSize = currentFontSize;
    }
    /**
     * Sets the household for the controller.
     *
     * @param haushalt the household to be set.
     */
    public void setHaushalt(Haushalt haushalt)
    {
        this.haushalt = haushalt;
    }
    /**
     * Gets the number of powers of attorney.
     *
     * @return the number of powers of attorney.
     */
    public Integer getAnzahlVollmachten()
    {
        return anzahlVollmachten;
    }
    /**
     * Gets the power of attorney.
     *
     * @return the power of attorney.
     */
    public Vollmacht getVollmacht()
    {
        return vollmacht;
    }
}
