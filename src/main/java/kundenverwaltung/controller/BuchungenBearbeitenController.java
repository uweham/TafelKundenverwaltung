package kundenverwaltung.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.EinkaufDAOimpl;
import kundenverwaltung.dao.HaushaltDAOimpl;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.dao.WarentypDAOimpl;
import kundenverwaltung.model.Einkauf;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.model.Warentyp;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;
import kundenverwaltung.toolsandworkarounds.ParseToEuroFormat;

/**
 * This class edit a existing sale.
 *
 * @author Group_1
 * @author Richard Kromm
 * Last change:
 *      By: Richard Kromm
 *      On: 05.08.2018
 */
public class BuchungenBearbeitenController
{
    private static final String NOTIFICATION_TITEL_DEFAULT = "Achtung!";
    private static final String NOTIFICATION_SHOPPING_IS_CANCELED = "Der Einkauf ist bereits storniert";
    private static final String DIALOG_CANCELED_TITEL = "Buchung stornieren";
    private static final String DIALOG_CANCELED_CONTENT_TEXT = "Stornotext: ";
    private static final Double DIALOG_CANCELED_MIN_WIDTH = 600.0;
    private static final String REPAYMENT_CASH = "Barrückzahlung: ";
    private static final String REPAYMENT_MINUS_SALDO = "werden vom Kontostand abgezogen.";
    private static final String REPAYMENT_PLUS_SALDO = "werden dem Kontostand gutgeschrieben.";
    @FXML
    private Label labelHeader;
    @FXML
    private Label labelProductType;
    @FXML
    private Label labelDistributionPointOfEntry;
    @FXML
    private Label labelStartDate;
    @FXML
    private Label labelEndDate;

    @FXML
    private ComboBox<Warentyp> cbWarentyp;
    @FXML
    private ComboBox<Verteilstelle> cbVerteilstelle;

    @FXML
    private CheckBox cbxStornierteUmsaetze;

    @FXML
    private DatePicker dateStartdatum;
    @FXML
    private DatePicker dateEnddatum;

    @FXML
    private TableView<Einkauf> tvBuchungen;

    @FXML
    @SuppressWarnings("rawtypes")
    private TableColumn columnPerson;
    @FXML
    @SuppressWarnings("rawtypes")
    private TableColumn columnText;
    @FXML
    @SuppressWarnings("rawtypes")
    private TableColumn dateEinkauf;
    @FXML
    @SuppressWarnings("rawtypes")
    private TableColumn dateErfasst;
    @FXML
    @SuppressWarnings("rawtypes")
    private TableColumn columnWarentyp;
    @FXML
    @SuppressWarnings("rawtypes")
    private TableColumn columnUmsatz;
    @FXML
    @SuppressWarnings("rawtypes")
    private TableColumn columnZahlung;
    @FXML
    @SuppressWarnings("rawtypes")
    private TableColumn columnVerteilstelle;
    @FXML
    @SuppressWarnings("rawtypes")
    private TableColumn columnStoniert;

    @FXML
    private Button btnSchliessen;
    @FXML
    private Button buttonSaveTable;
    @FXML
    private Button buttonCancelThisSales;


    private ChangeDateFormat changeDateFormat = new ChangeDateFormat();
    private ChangeFontSize changeFontSize = new ChangeFontSize();
    private ParseToEuroFormat parseToEuroFormat = new ParseToEuroFormat();
    private HaushaltDAOimpl haushaltDAOimpl = new HaushaltDAOimpl();


    private Haushalt haushalt;
    private Einkauf einkauf;
    private ArrayList<Einkauf> einkaufListe;
    private ObservableList<Einkauf> einkauefe = FXCollections.observableArrayList();
    private ArrayList<Warentyp> warentypListe = new WarentypDAOimpl().readAll();
    private ObservableList<Warentyp> warentypen = FXCollections.observableArrayList(warentypListe);
    private ArrayList<Verteilstelle> verteilstelleListe = new VerteilstelleDAOimpl().readAll();
    private ObservableList<Verteilstelle> verteilstellen = FXCollections.observableArrayList(verteilstelleListe);
    /**
     * Initializes the controller.
     */
    public void initialize()
    {
        cbWarentyp.setItems(warentypen);
        cbVerteilstelle.setItems(verteilstellen);

        dateEnddatum.setConverter(changeDateFormat.convertDatePickerFormat());
        dateStartdatum.setConverter(changeDateFormat.convertDatePickerFormat());

    }

    /**
     * Manually initializes the controller.
     */
    public void manuellInitialize()
    {
        tvBuchungen.getItems().clear();
        einkaufListe = new EinkaufDAOimpl().getAllEinkauefe(haushalt);
        einkauefe.addAll(einkaufListe);
        setEinkaufTabelView();
    }

    /**
     * Sets the table view for the purchases.
     */
    @SuppressWarnings("unchecked")
	public void setEinkaufTabelView()
    {
        columnPerson.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnPerson.setId("Name");

        columnText.setCellValueFactory(new PropertyValueFactory<>("BuchungText"));
        columnText.setId("BuchungText");

        dateEinkauf.setCellValueFactory(new PropertyValueFactory<>("ShoppingOn"));
        dateEinkauf.setId("ShoppingOn");

        dateErfasst.setCellValueFactory(new PropertyValueFactory<>("ErfasstAm"));
        dateErfasst.setId("ErfasstAm");

        columnWarentyp.setCellValueFactory(new PropertyValueFactory<>("WarentypName"));
        columnWarentyp.setId("WarentypName");

        columnUmsatz.setCellValueFactory(new PropertyValueFactory<>("Umsatz"));
        columnUmsatz.setId("Umsatz");

        columnZahlung.setCellValueFactory(new PropertyValueFactory<>("Zahlung"));
        columnZahlung.setId("Zahlung");

        columnVerteilstelle.setCellValueFactory(new PropertyValueFactory<>("VerteilstelleName"));
        columnVerteilstelle.setId("VerteilstelleName");

        columnStoniert.setCellValueFactory(new PropertyValueFactory<>("CanceledOn"));
        columnStoniert.setId("CanceledOn");

        tvBuchungen.setItems(einkauefe);

        TablePreferenceServiceImpl.getInstance().setupPersistence(tvBuchungen, "tvBuchungenBearbeiten");
    }

    /**
     * Executes the search based on the filter criteria.
     */
    @FXML
    public void sucheDurchfuehren()
    {
        Haushalt household = haushalt;
        Warentyp warentyp = cbWarentyp.getSelectionModel().getSelectedItem();
        Verteilstelle verteilstelle = cbVerteilstelle.getSelectionModel().getSelectedItem();
        boolean stortnierteUmsaetze = cbxStornierteUmsaetze.isSelected();
        LocalDate startDatum = dateStartdatum.getValue();
        LocalDate endDatum = dateEnddatum.getValue();

        einkaufListe = new EinkaufDAOimpl().getAllEinkauefe(household, warentyp, verteilstelle, stortnierteUmsaetze, startDatum, endDatum);
        einkauefe = FXCollections.observableArrayList(einkaufListe);
        setEinkaufTabelView();
    }

    /**
     * Handles the action when the "Save Table" button is clicked.
     */
    @FXML
    public void btnTabelleSpeichern()
    {

    }

    /**
     * Handles the action when the "Close" button is clicked.
     */
    @FXML
    public void btnSchliessen()
    {
        Stage stage = (Stage) btnSchliessen.getScene().getWindow();
        stage.close();
    }


    /**
     * This function canceled a shopping, write the canceled date and the canceled text in database and change the customer saldo.
     */
    @FXML
    public void btnUmsatzStornieren()
    {
        einkauf = tvBuchungen.getSelectionModel().getSelectedItem();

        if
        (einkauf.getStorniertAm() != null)
        {
            Benachrichtigung.infoBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_SHOPPING_IS_CANCELED);
            return;
        }

        double repaymentCash = einkauf.getSummeZahlung();
        double repaymentSaldo = einkauf.getSummeEinkauf() - repaymentCash;
        String repaymentSaldoText = "";
        if (einkauf.getSummeEinkauf() != einkauf.getSummeZahlung())
        {
            repaymentSaldoText = parseToEuroFormat.parseFloatToEuroString(Math.abs(repaymentSaldo), true)
                    + " " + (repaymentSaldo < 0.0 ? REPAYMENT_MINUS_SALDO : REPAYMENT_PLUS_SALDO);
        }

        TextInputDialog dialog = new TextInputDialog("");
        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        dialog.getEditor().setOnKeyReleased(event ->
        {
            if (dialog.getEditor().getText().length() > 0)
            {
                dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
            } else
            {
                dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
            }
        });
        dialog.getDialogPane().setMinWidth(DIALOG_CANCELED_MIN_WIDTH);
        dialog.setTitle(DIALOG_CANCELED_TITEL);
        dialog.setHeaderText(REPAYMENT_CASH + parseToEuroFormat.parseFloatToEuroString(repaymentCash, true)
                + "\n \n" + repaymentSaldoText);
        dialog.setContentText(DIALOG_CANCELED_CONTENT_TEXT);
        Optional<String> canceledText = dialog.showAndWait();
        if (canceledText.isPresent())
        {
            einkauf.setStornoText(dialog.getEditor().getText());
            einkauf.setStorniertAm(LocalDateTime.now());
            @SuppressWarnings("unused")
			Boolean updateCheck = new EinkaufDAOimpl().update(einkauf);
            einkaufListe = new EinkaufDAOimpl().getAllEinkauefe(haushalt);
            einkauefe = FXCollections.observableArrayList(einkaufListe);
            setEinkaufTabelView();

            if (einkauf.getSummeEinkauf() != einkauf.getSummeZahlung())
            {
                haushalt.setSaldo((float) (haushalt.getSaldo() + repaymentSaldo));
                haushaltDAOimpl.update(haushalt);
            }
            
        }
    }

    /**
     * Set the correct font size.
     * @param fontSize
     */
    public void setFontSize(Double fontSize)
    {
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);

        ArrayList<Label> labelArrayList = new ArrayList<>(Arrays.asList(
                labelHeader, labelProductType, labelDistributionPointOfEntry, labelStartDate, labelEndDate
            ));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(buttonCancelThisSales, buttonSaveTable, btnSchliessen));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);

        @SuppressWarnings("rawtypes")
		ArrayList<ComboBox> comboBoxArrayList = new ArrayList<>(Arrays.asList(cbVerteilstelle, cbWarentyp));
        changeFontSize.changeFontSizeFromComboBoxArrayList(comboBoxArrayList, newFontSize);

        ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>(Arrays.asList(cbxStornierteUmsaetze));
        changeFontSize.changeFontSizeFromCheckBoxArrayList(checkBoxArrayList, newFontSize);

        ArrayList<DatePicker> datePickerArrayList = new ArrayList<>(Arrays.asList(dateEnddatum, dateStartdatum));
        changeFontSize.changeFontSizeFromDatePickerArrayList(datePickerArrayList, newFontSize);

        @SuppressWarnings("rawtypes")
		ArrayList<TableColumn> tableColumnArrayList = new ArrayList<>(Arrays.asList(
                        columnPerson, columnText, dateEinkauf, dateErfasst, columnWarentyp, columnUmsatz,
                        columnZahlung, columnVerteilstelle, columnStoniert
                ));
        changeFontSize.changeFontSizeFromTableColumnArrayList(tableColumnArrayList, newFontSize);

        changeFontSize.changeFontSizeFromTableColumn(columnPerson, fontSize);
    }


    /**
     * Sets the household for the controller.
     * @param haushalt the household to set
     */
    public void setHaushalt(Haushalt haushalt)
    {
        this.haushalt = haushalt;
    }
}
