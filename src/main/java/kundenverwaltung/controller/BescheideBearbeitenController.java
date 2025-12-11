package kundenverwaltung.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.BescheidDAOimpl;
import kundenverwaltung.dao.BescheidartDAOimpl;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.model.Bescheid;
import kundenverwaltung.model.Bescheidart;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;

/**
 * The class allows to edit assessments, to add, delete, enable, and disable them.
 *
 * @Author Gruppe 1
 * Last Change: @Author: Philipp Wilm
 */
public class BescheideBearbeitenController
{
    private static final String NOTIFICATION_TITEL_DEFAULT = "Achtung!";
    private static final String NOTIFICATION_TEXT_ASSESSMENT = "Bitte wählen Sie eine Bescheidart aus.";
    private static final String PENSION = "Rente";
    private static final String OLD_AGE_PENSION = "Altersrente";
    private static final String EU_PENSION = "EU-Rente";
    private static final String WIDOWS_PENSION = "Witwenrente";
    private static final String ORPHANS_PENSION = "Waisenrente";
    private static final String ASYLUM_MODEST = "AsylbLg";
    private static final int LENGTH_ASYLUM_MODEST_IN_YEAR = 1;
    private static final Familienmitglied ILLEGAL_FAMILY_MEMBER = new Familienmitglied(-1, null,
            null, null, "Alle", "", null, "", false,
            false, false, null, null, false,
            false, null, null);

    private ChangeFontSize changeFontSize = new ChangeFontSize();
    private ChangeDateFormat changeDateFormat = new ChangeDateFormat();
    @SuppressWarnings("static-access")
    private Double currentFontSize = changeFontSize.getDefaultFontSize();


    @FXML
    private Label labelHeading;
    @FXML
    private Label labelShowAssessments;
    @FXML
    private Label labelExistingAssessments;
    @FXML
    private Button btnAddAssessments;
    @FXML
    private Button btnEditAssessments;
    @FXML
    private Button btnDeleteAssessments;
    @FXML
    private Button btnBescheideSchliessen;
    @FXML
    private ComboBox<String> cbAuswahlBescheide;
    @FXML
    private TableView<Bescheid> tvBescheide;
    @FXML
    private TableColumn<Bescheid, String> columnBSName;
    @FXML
    private TableColumn<Bescheid, String> columnBSBescheid;
    @FXML
    private TableColumn<Bescheid, LocalDate> columnBSgueltigAb;
    @FXML
    private TableColumn<Bescheid, LocalDate> columnBSgueltigBis;

    @FXML
    private Label labelHeadingAddAssesment;
    @FXML
    private Label labelOr;
    @FXML
    private Label labelMemberOfTheFamily;
    @FXML
    private Label labelAssessmentType;
    @FXML
    private Label labelValidFrom;
    @FXML
    private Label labelDateOfExpiry;
    @FXML
    private Button btnSaveAssessment;
    @FXML
    private Button btnHinzufuegenSchliessen;
    @FXML
    private ComboBox<Familienmitglied> cbFamilienmitglied;
    @FXML
    private ComboBox<Bescheidart> cbBescheidart;
    @FXML
    private DatePicker dateGueltigAb;
    @FXML
    private DatePicker dateGueltigBis;
    @FXML
    private CheckBox cbxUnbegrenzt;


    private Boolean neuHinzufuegen;
    private Bescheid bescheid;
    private Familienmitglied familienmitglied;
    private ArrayList<Familienmitglied> familienmitglieder;
    @SuppressWarnings("unused")
    private Haushalt haushalt;

    private ArrayList<Bescheid> bescheideListe = new ArrayList<>();
    private ObservableList<Bescheid> alleBescheide = FXCollections.observableArrayList();
    private ObservableList<Bescheid> gueltigeBescheide = FXCollections.observableArrayList();
    private ObservableList<Bescheid> ungueltigeBescheide = FXCollections.observableArrayList();


    /**
     * Creates a table with existing assessments.
     */
    @SuppressWarnings("unchecked")
    public void erstelleTabelleBestehendeBescheide()
    {
        columnBSName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnBSName.setId("Name");

        columnBSBescheid.setCellValueFactory(new PropertyValueFactory<>("BescheidName"));
        columnBSBescheid.setId("BescheidName");

        columnBSgueltigAb.setCellValueFactory(new PropertyValueFactory<>("ValidFrom"));
        columnBSgueltigAb.setId("ValidFrom");

        columnBSgueltigBis.setCellValueFactory(new PropertyValueFactory<>("DateOfExpiry"));
        columnBSgueltigBis.setId("DateOfExpiry");

        for (Familienmitglied element : familienmitglieder)
        {
            bescheideListe = new BescheidDAOimpl().readAll(element);
            alleBescheide.addAll(bescheideListe);
        }

        tvBescheide.setItems(alleBescheide);

        TablePreferenceServiceImpl.getInstance().setupPersistence(tvBescheide, "tvBescheideBearbeiten");
    }


    /**
     * Searches for assessments based on the selected criteria.
     */
    @FXML
    public void bescheideSuchen()
    {

        switch (cbAuswahlBescheide.getSelectionModel().getSelectedItem())
        {

            case "Alle":
                tvBescheide.setItems(alleBescheide);
                break;
            case "Nur gültige":
                gueltigeBescheide.clear();
                gueltigeBescheide.addAll(alleBescheide.stream().filter(r -> r.getGueltigAb().isBefore((LocalDate.now().plusDays(1)))).filter(r -> r.getGueltigBis().isAfter((LocalDate.now().minusDays(1)))).collect(Collectors.toList()));
                tvBescheide.setItems(gueltigeBescheide);
                break;
            case "Nur ungültige":
                ungueltigeBescheide.clear();
                ungueltigeBescheide.addAll(alleBescheide.stream().filter(r -> (r.getGueltigAb().isAfter(LocalDate.now())) || (r.getGueltigBis().isBefore(LocalDate.now()))).collect(Collectors.toList()));
                tvBescheide.setItems(ungueltigeBescheide);
                break;
            default:
                break;
        }

    }


    /**
     * Adds a new assessment.
     */
    @FXML
    public void btnBescheidHinzufuegen()
    {

        Bescheid neuerBescheid;
        neuerBescheid = MainController.getInstance().oeffneBescheideHinzufuegen(familienmitglieder, null, currentFontSize);

        if (neuerBescheid != null)
        {
            bescheideListe.clear();
            alleBescheide.clear();
            for (Familienmitglied element : familienmitglieder)
            {
                bescheideListe = new BescheidDAOimpl().readAll(element);
                alleBescheide.addAll(bescheideListe);
            }

            tvBescheide.setItems(alleBescheide);
        }
    }

    /**
     * Edits the selected assessment.
     */
    @FXML
    public void btnBescheidBearbeiten()
    {

        bescheid = null;
        bescheid = tvBescheide.getSelectionModel().getSelectedItem();

        if (bescheid != null)
        {
            bescheid = MainController.getInstance().oeffneBescheideHinzufuegen(familienmitglieder, bescheid, currentFontSize);

            tvBescheide.refresh();

        } else
        {
            Benachrichtigung.infoBenachrichtigung("Kein Bescheid ausgewählt.", "Bitte wählen Sie einen Bescheid, der bearbeitet werden soll.");
        }

    }

    /**
     * Deletes the selected assessment.
     */
    @FXML
    public void btnBescheidLoeschen()
    {

        bescheid = null;
        bescheid = tvBescheide.getSelectionModel().getSelectedItem();

        if (bescheid != null)
        {

            Boolean checkDelete = new BescheidDAOimpl().delete(bescheid);

            if (checkDelete)
            {
                Benachrichtigung.infoBenachrichtigung("Löschen erfolgreich.", "Der ausgewählte Bescheid wurde erfolgreich gelöscht.");
                alleBescheide.clear();

                for (Familienmitglied element : familienmitglieder)
                {
                    bescheideListe = new BescheidDAOimpl().readAll(element);

                    alleBescheide.addAll(bescheideListe);
                }

                tvBescheide.setItems(alleBescheide);
            } else
            {
                Benachrichtigung.warnungBenachrichtigung("Der Bescheid konnte nicht bearbeitet werden.", "Bitte überprüfen Sie Ihre Verbindung zur Datenbank und probieren Sie es erneut.");
            }

        } else
        {
            Benachrichtigung.infoBenachrichtigung("Kein Bescheid ausgewählt.", "Bitte wählen Sie einen Bescheid, der gelöscht werden soll.");
        }

    }

    /**
     * Closes the assessment editing window.
     */
    @FXML
    public void btnBescheideSchliessen()
    {
        Stage stage = (Stage) btnBescheideSchliessen.getScene().getWindow();
        stage.close();
    }


    /**
     * Confirms the creation of a new assessment.
     */
    @FXML
    public void btnBescheidAnlegenOk()
    {

        LocalDate gueltigAb = dateGueltigAb.getValue();
        LocalDate gueltigBis;

        if (cbxUnbegrenzt.isSelected())
        {
            gueltigBis = LocalDate.of(9999, 12, 31);
        } else
        {
            gueltigBis = dateGueltigBis.getValue();
        }

        familienmitglied = cbFamilienmitglied.getValue();
        Bescheidart bescheidArt = cbBescheidart.getValue();

        Boolean checkUpdate = false;

        if (pruefeFelder())
        {
            if (cbxUnbegrenzt.isSelected() || (!gueltigAb.isAfter(gueltigBis)))
            {
                if (neuHinzufuegen)
                {
                    if (familienmitglied.equals(ILLEGAL_FAMILY_MEMBER))
                    {
                        ObservableList<Familienmitglied> familyMemberObservableList = cbFamilienmitglied.getItems();
                        for (int i = 1; i < familyMemberObservableList.size(); i++)  // i=1, because 0 = illegal family member!
                        {
                            bescheid = new Bescheid(familyMemberObservableList.get(i), bescheidArt, gueltigAb, gueltigBis);
                            checkUpdate = new BescheidDAOimpl().create(bescheid);
                            bescheideListe = new BescheidDAOimpl().readAll(familyMemberObservableList.get(i));
                            alleBescheide.addAll(bescheideListe);
                        }
                    } else
                        {
                            bescheid = new Bescheid(familienmitglied, bescheidArt, gueltigAb, gueltigBis);
                            checkUpdate = new BescheidDAOimpl().create(bescheid);
                        }
                } else
                {

                    bescheid.setBescheidart(bescheidArt);
                    bescheid.setGueltigAb(gueltigAb);
                    bescheid.setGueltigBis(gueltigBis);

                    checkUpdate = new BescheidDAOimpl().update(bescheid);
                }

                if (checkUpdate)
                {
                    Benachrichtigung.infoBenachrichtigung("Bearbeiten erfolgreich.", "Der Bescheid wurde erfolgreich bearbeitet.");
                    Stage stage = (Stage) btnHinzufuegenSchliessen.getScene().getWindow();
                    stage.close();
                } else
                {
                    Benachrichtigung.warnungBenachrichtigung("Becheid konnte nicht bearbeitet werden.", "Bitte überprüfen Sie Ihre Verbindung zur Datenbank und probieren Sie es erneut.");
                }
            } else
            {
                Benachrichtigung.infoBenachrichtigung("Achtung!", "Das Anfangsdatum darf nicht nach dem Enddatum liegen.");
            }
        } else
        {
            Benachrichtigung.infoBenachrichtigung("Achtung!", "Bitte füllen Sie alle Pflichtfelder aus.");
        }
    }

    /**
     * Cancels the creation of a new assessment.
     */
    @FXML
    public void btnBescheidAnlegenAbbrechen()
    {
        Stage stage = (Stage) btnHinzufuegenSchliessen.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the end date of the assessment to unlimited if selected.
     */
    @FXML
    public void setzeBescheidUnbegrenzt()
    {

        if (cbxUnbegrenzt.isSelected())
        {
            dateGueltigBis.setDisable(true);
        } else
        {
            dateGueltigBis.setDisable(false);
        }

    }
    /**
     * Sets the assessment data.
     *
     * @param haushalt The household whose assessment data is to be set.
     */
    public void setzeBescheiddaten(Haushalt haushalt)
    {

        this.haushalt = haushalt;

        familienmitglieder = new FamilienmitgliedDAOimpl().getAllFamilienmitglieder(haushalt.getKundennummer());

    }
    /**
     * Sets the data for adding a new assessment.
     *
     * @param familienmitglieder List of family members.
     */
    public void setzeDatenBescheidHinzufuegen(ArrayList<Familienmitglied> familienmitglieder)
    {
        // KORREKTUR: Nur aktive Bescheidarten laden, statt alle!
        ArrayList<Bescheidart> alleBescheidarten = new BescheidartDAOimpl().readAssessments(true);
        
        ObservableList<Bescheidart> alleBescheidartenOL = FXCollections.observableArrayList(alleBescheidarten);


        ObservableList<Familienmitglied> familienmitgliederOL = FXCollections.observableArrayList(familienmitglieder);

        familienmitgliederOL.add(0, ILLEGAL_FAMILY_MEMBER);

        cbFamilienmitglied.setItems(familienmitgliederOL);
        cbBescheidart.setItems(alleBescheidartenOL);

    }
    /**
     * Gets the selected family member.
     *
     * @return The selected family member.
     */
    public Familienmitglied getFamilienmitglied()
    {
        return familienmitglied;
    }
    /**
     * Sets the data for adding a new assessment.
     *
     * @param bescheid The assessment to be added.
     * @param neuHinzufuegen Flag indicating if a new assessment is to be added.
     */
    public void setzeDatenBescheidHinzufuegen(Bescheid bescheid, Boolean neuHinzufuegen)
    {

        this.neuHinzufuegen = neuHinzufuegen;


        if (bescheid != null)
        {
            this.bescheid = bescheid;

            cbFamilienmitglied.setValue(bescheid.getPerson());
            cbFamilienmitglied.setDisable(true);
            cbBescheidart.setValue(bescheid.getBescheidart());
            dateGueltigAb.setConverter(changeDateFormat.convertDatePickerFormat());
            dateGueltigBis.setConverter(changeDateFormat.convertDatePickerFormat());
            dateGueltigAb.setValue(bescheid.getGueltigAb());
            dateGueltigBis.setValue(bescheid.getGueltigBis());
        }

    }
    /**
     * Gets the assessment.
     *
     * @return The assessment.
     */
    public Bescheid getBescheid()
    {
        return bescheid;
    }

    /**
     * Checks if all required fields are filled.
     *
     * @return true if all required fields are filled, false otherwise.
     */
    public Boolean pruefeFelder()
    {
        return !((dateGueltigAb.getValue() == null || dateGueltigAb.getValue().toString().isEmpty()) || ((dateGueltigBis.getValue() == null || dateGueltigBis.getValue().toString().isEmpty()) && !cbxUnbegrenzt.isSelected()) || (cbBescheidart.getSelectionModel().getSelectedItem() == null) || (cbFamilienmitglied.getSelectionModel().getSelectedItem() == null));
    }


    /**
     * Sets validity of pension unlimited and of asylum to one year.
     *
     *
     */
    public void setDateOfExpiry()
    {
        String assessment = "";

        try
        {
            assessment = cbBescheidart.getSelectionModel().getSelectedItem().getName();
        } catch (NullPointerException exception)
            {
                Benachrichtigung.infoBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_TEXT_ASSESSMENT);
            }

        if (PENSION.equals(assessment) || OLD_AGE_PENSION.equals(assessment) || EU_PENSION.equals(assessment) || WIDOWS_PENSION.equals(assessment) || ORPHANS_PENSION.equals(assessment))
        {
            cbxUnbegrenzt.setSelected(true);
            setzeBescheidUnbegrenzt();
        } else
            {
                cbxUnbegrenzt.setSelected(false);
                setzeBescheidUnbegrenzt();
            }


        LocalDate dateValidFrom = dateGueltigAb.getValue();
        if (dateValidFrom != null && ASYLUM_MODEST.equals(assessment))
        {
            dateGueltigBis.setValue(dateValidFrom.plusYears(LENGTH_ASYLUM_MODEST_IN_YEAR));
        }
    }


    /**
     * Datepicker changes in German format.
     *
     * @param actionEvent The action event.
     */
    public void changeDatePickerFormat(ActionEvent actionEvent)
    {
        DatePicker datePickerValidFrom = dateGueltigAb;
        DatePicker datePickerDateOfExpiry = dateGueltigBis;
        DatePicker datePicker = null;

        if (datePickerValidFrom.equals(actionEvent.getSource()))
        {
            datePicker = datePickerValidFrom;
        } else
        {
            datePicker = datePickerDateOfExpiry;
        }

        datePicker.setConverter(changeDateFormat.convertDatePickerFormat());

        if (datePickerValidFrom.equals(actionEvent.getSource()))
        {
            setDateOfExpiry();
        }
    }


    /**
     * Sets the correct font size in BescheidBearbeiten.fxml.
     *
     * @param fontSize
     */
    public void setFontSizeShowAssessments(Double fontSize)
    {
        setCurrentFontSize(fontSize);
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);

        ArrayList<Label> labelArrayList = new ArrayList<>(Arrays.asList(
                        labelHeading, labelShowAssessments, labelExistingAssessments
                ));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(
                        btnAddAssessments, btnEditAssessments, btnDeleteAssessments, btnBescheideSchliessen
                ));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);

        @SuppressWarnings("rawtypes")
        ArrayList<TableColumn> tableColumnArrayList = new ArrayList<>(Arrays.asList(
                        columnBSName, columnBSBescheid, columnBSgueltigAb, columnBSgueltigBis
                ));
        changeFontSize.changeFontSizeFromTableColumnArrayList(tableColumnArrayList, newFontSize);

        @SuppressWarnings("rawtypes")
        ArrayList<ComboBox> comboBoxArrayList = new ArrayList<>(Arrays.asList(cbAuswahlBescheide));
        changeFontSize.changeFontSizeFromComboBoxArrayList(comboBoxArrayList, newFontSize);
    }

    /**
     * Sets the correct font size in BescheidHinzufuegen.fxml.
     *
     * @param fontSize
     */
    public void setFontSizeAddAssessments(Double fontSize)
    {
        DoubleProperty primaryFontSize = new SimpleDoubleProperty(fontSize);
        DoubleProperty secondaryFontSize = new SimpleDoubleProperty(fontSize - ChangeFontSize.getDifferenceBetweenPrimarySecondaryFontsize());

        ArrayList<Label> primarylabelArrayList = new ArrayList<>(Arrays.asList(labelHeadingAddAssesment, labelOr));
        changeFontSize.changeFontSizeFromLabelArrayList(primarylabelArrayList, primaryFontSize);

        ArrayList<Label> secondarylabelArrayList = new ArrayList<>(Arrays.asList(
                       labelMemberOfTheFamily, labelAssessmentType, labelValidFrom, labelDateOfExpiry
                ));
        changeFontSize.changeFontSizeFromLabelArrayList(secondarylabelArrayList, secondaryFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(
                        btnSaveAssessment, btnHinzufuegenSchliessen
                ));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, primaryFontSize);

        ArrayList<DatePicker> datePickerArrayList = new ArrayList<>(Arrays.asList(dateGueltigAb, dateGueltigBis));
        changeFontSize.changeFontSizeFromDatePickerArrayList(datePickerArrayList, primaryFontSize);

        ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>(Arrays.asList(cbxUnbegrenzt));
        changeFontSize.changeFontSizeFromCheckBoxArrayList(checkBoxArrayList, primaryFontSize);

        @SuppressWarnings("rawtypes")
        ArrayList<ComboBox> comboBoxArrayList = new ArrayList<>(Arrays.asList(cbBescheidart, cbFamilienmitglied));
        changeFontSize.changeFontSizeFromComboBoxArrayList(comboBoxArrayList, primaryFontSize);
    }


    /**
     * Gets the current font size.
     *
     * @return The current font size.
     */
    public Double getCurrentFontSize()
    {
        return currentFontSize;
    }
    /**
     * Sets the current font size.
     *
     * @param currentFontSize The new current font size.
     */
    public void setCurrentFontSize(Double currentFontSize)
    {
        this.currentFontSize = currentFontSize;
    }
}