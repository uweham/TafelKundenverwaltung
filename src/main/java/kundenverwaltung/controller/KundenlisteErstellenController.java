package kundenverwaltung.controller;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import kundenverwaltung.dao.BescheidDAOimpl;
import kundenverwaltung.dao.EinkaufDAOimpl;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.dao.HaushaltDAOimpl;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.dao.VorlageDAOimpl;
import kundenverwaltung.dao.WarentypDAOimpl;
import kundenverwaltung.model.Bescheid;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.OrderBy;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.model.Vorlage;
import kundenverwaltung.model.Vorlagearten;
import kundenverwaltung.model.Warentyp;
import kundenverwaltung.toolsandworkarounds.BlobToTemplate;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;
import kundenverwaltung.toolsandworkarounds.IndeterminateProgressBar;
import kundenverwaltung.toolsandworkarounds.ParseToEuroFormat;
import kundenverwaltung.toolsandworkarounds.ReplaceGermanCharacters;

/**
 *
 * function addRow(customerId, surName, firstName, birthday, street, housenumber, postcode,.
 * location,
 *                     numberAdults, numberChildren, outputGroup, saldo, positivSaldo,
 *                     lastShoppingDate,
 *                     customerIsBlocked, decision, comment){...}
 *  function setParameter(distributionPoint, totalHousholds, totalAdults, totalChildren,
 *  totalPersons){...}
 *
 *
 *
 */
public class KundenlisteErstellenController extends Thread
{
    private static final ObservableList<String> SORT_ASC_DESC_OBSERVABLELIST =
            FXCollections.observableArrayList("aufsteigend", "absteigend");
    private static final String[] ORDER_BY_HOUSEHOLD_ID = {"Kundennummer", "kundennummer"};
            //first position show in combobox, second position is the database columnname
    private static final String[] ORDER_BY_SURNAME = {"Nachname", "nName"};
    private static final String[] ORDER_BY_FIRST_NANEM = {"Vorname", "vName"};
    private static final String[] ORDER_BY_STREET = {"Straße", "strasse"};
    private static final String[] ORDER_BY_POSTCODE = {"PLZ", "plzTemp"};
    private static final String[] ORDER_BY_LOCATION = {"Wohnort", "ort"};
    private static final String[] ORDER_BY_DISTRIBUTION_POINT = {"Verteilstelle", "bezeichnung"};
    private static final String[] ORDER_BY_OUTPUT_GROUP = {"Ausgabegruppe", "name"};

    private static final String FUNCTION_SET_PARAMETER = "setParameter(";
    private static final String FUNCTION_ADD_ROW = "addRow(";
    private static final String FUNCTION_END = ");";
    private static final String SEPERATOR = ",";
    private static final String HIGH_COMMA = "\"";
    private static final String SCRIPT_TAG_OPEN = "<script>";
    private static final String SCRIPT_TAG_CLOSE = "</script>";
    private static final String HTML_TAG_CLOSE = "</html>";
    private static final String PLACEHOLDER_FOR_FUNCTIONS = "%PLACEHOLDER_FOR_FUNCTIONS%";
    private static final String PLACEHOLDER_END_OF_FILE = "%END_OF_FILE%";

    private static final String CUMSTEMER_IS_BLOCKED_STRING = "Kunden gesperrt";
    private static final int FIRST_POSITION = 0;
    private static final int SECOND_POSITION = 1;
    private static final int ITEM_POSITION_SORT_ASCENDING = 0;
    @SuppressWarnings("unused")
	private static final int ITEM_POSITION_MORNING = 1;
    @SuppressWarnings("unused")
	private static final int ITEM_POSITION_AFTERNOON = 2;
    private static final int INT_TRUE = 1;
    private static final int INT_FALSE = 0;

    @FXML private Label labelHeader;
    @FXML private Label labelTemplate;
    @FXML private Label labelDistributionPoint;
    @FXML private Label labelProductType;
    @FXML private Label labelOrderBy;
    @FXML private CheckBox cbxAutomatischEinstellen;
    @FXML private CheckBox cbxArchivierteKunden;
    @FXML private CheckBox cbxEinstellungenAlsStandard;
    @FXML private CheckBox cbxBescheidarten;
    @FXML private CheckBox cbxGesperrteKunden;
    @FXML private ComboBox<String> cbSortierReihenfolge;
    @FXML private ComboBox<Vorlage> cbVorlage;
    @FXML private ComboBox<Warentyp> cbWarentyp;
    @FXML private ComboBox<Verteilstelle> cbVerteilstelle;
    @FXML private ComboBox<OrderBy> cbSortierenNach;
    @FXML private Button buttonRun;
    @FXML private Button buttenCancel;


    private ChangeFontSize changeFontSize = new ChangeFontSize();
    private ChangeDateFormat changeDateFormat = new ChangeDateFormat();
    private ReplaceGermanCharacters replaceGermanCharacters = new ReplaceGermanCharacters();
    private ParseToEuroFormat parseToEuroFormat = new ParseToEuroFormat();

    private VorlageDAOimpl vorlageDAOimpl = new VorlageDAOimpl();
    private VerteilstelleDAOimpl verteilstelleDAOimpl = new VerteilstelleDAOimpl();
    private WarentypDAOimpl warentypDAOimpl = new WarentypDAOimpl();
    private HaushaltDAOimpl haushaltDAOimpl = new HaushaltDAOimpl();
    private FamilienmitgliedDAOimpl familienmitgliedDAOimpl = new FamilienmitgliedDAOimpl();
    private BescheidDAOimpl bescheidDAOimpl = new BescheidDAOimpl();
    private EinkaufDAOimpl einkaufDAOimpl = new EinkaufDAOimpl();

    private ArrayList<Vorlage> templatesArrayList = vorlageDAOimpl.getTemplates(Vorlagearten.Liste);
    private ObservableList<Vorlage> templateObserverList =
            FXCollections.observableArrayList(templatesArrayList);
    private ArrayList<Verteilstelle> distributionPointsArrayList = verteilstelleDAOimpl.readAll();
    private ObservableList<Verteilstelle> distributionPointsObservableList =
            FXCollections.observableArrayList(distributionPointsArrayList);
    private ArrayList<Warentyp> productTypeArrayList = warentypDAOimpl.readAll();
    private ObservableList<Warentyp> productTypeObservableList;
    private ObservableList<OrderBy> orderByObservableList = orderBy();

    private Warentyp invalidProductType = new Warentyp(-1, "Alle", 0, 0, false);
    private ArrayList<Haushalt> customerArrayList;
    @SuppressWarnings("unused")
	private Vorlage template;
    private Verteilstelle distributionPoint;
    private Warentyp productType;
    private OrderBy orderBy;
    private Boolean ascending;
    private Boolean showDecision;
    private Boolean showArchivedCustomer;
    private Boolean showBlockedCustomer;

    private IndeterminateProgressBar indeterminateProgressBar = new IndeterminateProgressBar();
    private BlobToTemplate blobToTemplate = new BlobToTemplate();

    /**
     * Gets the IndeterminateProgressBar instance.
     *
     * @return the current IndeterminateProgressBar
     */
    public IndeterminateProgressBar getIndeterminateProgressBar()
    {
        return indeterminateProgressBar;
    }
    /**
     * Sets the IndeterminateProgressBar instance.
     *
     * @param indeterminateProgressBar the IndeterminateProgressBar to set
     */
    public void setIndeterminateProgressBar(IndeterminateProgressBar indeterminateProgressBar)
    {
        this.indeterminateProgressBar = indeterminateProgressBar;
    }
    /**
     * Gets the BlobToTemplate instance.
     *
     * @return the current BlobToTemplate
     */
    public BlobToTemplate getBlobToTemplate()
    {
        return blobToTemplate;
    }
    /**
     * Sets the BlobToTemplate instance.
     *
     * @param blobToTemplate the BlobToTemplate to set
     */
    public void setBlobToTemplate(BlobToTemplate blobToTemplate)
    {
        this.blobToTemplate = blobToTemplate;
    }


    /**
     * This function initializes the controller.
     */
    @FXML public void initialize()
    {
        cbVorlage.setItems(templateObserverList);
        cbVorlage.getSelectionModel().selectFirst();
        cbVorlage.setConverter(new StringConverter<Vorlage>()
        {
            @Override public String toString(Vorlage object)
            {
                return object.getName();
            }



            @Override public Vorlage fromString(String string)
            {
                return null;
            }
        });

        cbVerteilstelle.setItems(distributionPointsObservableList);
        cbVerteilstelle.getSelectionModel().selectFirst();

        productTypeArrayList.add(FIRST_POSITION, invalidProductType);
        productTypeObservableList = FXCollections.observableArrayList(productTypeArrayList);
        cbWarentyp.setItems(productTypeObservableList);
        cbWarentyp.getSelectionModel().selectFirst();

        cbSortierenNach.setItems(orderByObservableList);
        cbSortierenNach.getSelectionModel().selectFirst();
        cbSortierenNach.setConverter(new StringConverter<OrderBy>()
        {
            @Override public String toString(OrderBy object)
            {
                return object.getTerm();
            }



            @Override public OrderBy fromString(String string)
            {
                return null;
            }
        });
        cbSortierReihenfolge.setItems(SORT_ASC_DESC_OBSERVABLELIST);
        cbSortierReihenfolge.getSelectionModel().selectFirst();
    }


    /**
     * This function creates a customer list.
     *
     * @Author Richard Kromm
     * Last Change:
     * @Author Adam Starobrzanski
     * @Date 14.09.2018
     */
    @SuppressWarnings("unchecked")
	@FXML public void createCustomerList()
    {
        Stage taskUpdateStage = new Stage();
        indeterminateProgressBar.start(taskUpdateStage);

        @SuppressWarnings("rawtypes")
		Task creatingCashSettlement = new Task<Void>()
        {
            @Override protected Void call() throws Exception
            {

                readUserInput();
                customerArrayList = haushaltDAOimpl
                        .createCustomerList(distributionPoint, productType, orderBy, ascending,
                                showArchivedCustomer, showBlockedCustomer);

                System.out.println("Länge ArrayListe: " + customerArrayList.size());

                try
                {

                    Blob blobFile = cbVorlage.getSelectionModel().getSelectedItem().getDaten();

                    blobToTemplate.convertBlobToTemplate(blobFile);

                    BufferedReader bufferedReader =
                            new BufferedReader(new FileReader(blobToTemplate.getBlobTempFileOut()));
                    File tempReadyHtml = File.createTempFile("readyHtml", ".html");
                    BufferedWriter bufferedWriter =
                            new BufferedWriter(new FileWriter(tempReadyHtml));

                    int tempCountRow = 1;
                    int findPlaceholder;
                    String row = null;
                    while ((row = bufferedReader.readLine()) != null)
                    {
                        findPlaceholder = row.indexOf(PLACEHOLDER_FOR_FUNCTIONS);
                        if (findPlaceholder > -1)
                        {
                            row = row.replace(PLACEHOLDER_END_OF_FILE, "");
                            row = row.replace(PLACEHOLDER_FOR_FUNCTIONS,
                                    createJavaScriptFunction());
                            bufferedWriter.write(row + HTML_TAG_CLOSE);
                            break;
                        }
                        bufferedWriter.write(row);

                        System.out.println(tempCountRow + ": " + row);
                        tempCountRow++;
                    }
                    /*       indeterminateProgressBar.closeProgressBar();*/
                    Desktop.getDesktop().open(tempReadyHtml);
                    bufferedWriter.close();
                    bufferedReader.close();
                    tempReadyHtml.deleteOnExit();

                } catch (IOException ioException)
                {
                    ioException.printStackTrace();
                }

                return null;
            }
        };

        creatingCashSettlement.setOnSucceeded(new EventHandler<WorkerStateEvent>()
        {
            @Override public void handle(WorkerStateEvent t)
            {
                taskUpdateStage.hide();
            }
        });

        taskUpdateStage.show();
        new Thread(creatingCashSettlement).start();
    }



    /**
     * This function close this window.
     */
    @FXML public void erstellenAbbrechen()
    {
        Stage stage = (Stage) cbxAutomatischEinstellen.getScene().getWindow();
        stage.close();
    }

    /**
     * This function set different variables after the user input.
     */
    private void readUserInput()
    {
      template = cbVorlage.getValue();
      distributionPoint = cbVerteilstelle.getValue();
      productType = cbWarentyp.getValue();
      orderBy = cbSortierenNach.getValue();
      ascending = cbSortierReihenfolge.getSelectionModel().getSelectedIndex() == ITEM_POSITION_SORT_ASCENDING;
      showDecision = !cbxBescheidarten.isSelected();
      showArchivedCustomer = cbxArchivierteKunden.isSelected();
      showBlockedCustomer = cbxGesperrteKunden.isSelected();
  }


    /**
     * This function build a javascript String, this string past in the template.
     *
     * @Author Richard Kromm
     */
    private String createJavaScriptFunction()
    {
        int totalHouseholds = customerArrayList.size();
        int totalAdults = 0;
        int totalChildren = 0;
        String payString;
        String resultAddRow = "";
        for (Haushalt element : customerArrayList)
        {
            totalAdults += element.getanzahlErwachsene();
            totalChildren += element.getanzahlKinder();

            payString = replaceGermanCharacters.replaceGermanUmlauts(parseToEuroFormat
                    .parseFloatToEuroString(element.getanzahlErwachsene()
                        *
                            productType.getPreisErwachsene()
                            +
                            element.getanzahlKinder()
                            *
                                    productType.getPreisKinder(), true));

            ArrayList<Bescheid> decision = bescheidDAOimpl.readAllGueltige(element
                    .getHaushaltsvorstand(familienmitgliedDAOimpl
                            .getAllFamilienmitglieder(element.getKundennummer())));
            String decisionString = "";
            if (showDecision)
            {
                for (Bescheid element2 : decision)
                {
                    decisionString += element2.getBescheidName() + " ";
                }
            }

            String lastShoppingString = "";
            LocalDateTime lastShopping = einkaufDAOimpl.getLetzerEinkauf(element);
            if (lastShopping != null)
            {
                lastShoppingString = changeDateFormat.changeDateTimeToDefaultString(lastShopping);
            }

            resultAddRow += FUNCTION_ADD_ROW + getFinishedValueForJavaScriptFunction(
                    String.valueOf(element.getKundennummer()), false)
            +
                    getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                            .replaceGermanUmlauts(element.getHaushaltsvorstand(
                                    familienmitgliedDAOimpl.getAllFamilienmitglieder(
                                            element.getKundennummer()))
                                    .getnName()), false) + getFinishedValueForJavaScriptFunction(
                    replaceGermanCharacters.replaceGermanUmlauts(element
                            .getHaushaltsvorstand(familienmitgliedDAOimpl.getAllFamilienmitglieder(
                                    element.getKundennummer())).getvName()),
                    false) + getFinishedValueForJavaScriptFunction(changeDateFormat
                            .changeDateToDefaultString(element.getHaushaltsvorstand(
                                    familienmitgliedDAOimpl.getAllFamilienmitglieder(
                                            element.getKundennummer())).getgDatum()),
                    false) + getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                    .replaceGermanUmlauts(element.getStrasse()), false)
                    +
                    getFinishedValueForJavaScriptFunction(element.getHausnummer(),
                            false) + getFinishedValueForJavaScriptFunction(
                    element.getPlz().getPlz(), false)
                            +
                    getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                                    .replaceGermanUmlauts(element.getPlz().getOrt()),
                            false) + getFinishedValueForJavaScriptFunction(
                    String.valueOf(element.getanzahlErwachsene()), false)
                            +
                    getFinishedValueForJavaScriptFunction(
                            String.valueOf(element.getanzahlKinder()), false)
                            +
                    getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                            .replaceGermanUmlauts(
                                    element.getAusgabegruppe().getName()), false)
                    // hier steht noch das falsche in der Tabelle, Ausgabegruppe oder Zeit??
                    + getFinishedValueForJavaScriptFunction(payString, false)
                    +
                    getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                            .replaceGermanUmlauts(parseToEuroFormat
                                    .parseFloatToEuroString(element.getSaldo(),
                                            true)), false) + getFinishedValueForJavaScriptFunction(
                    String.valueOf(
                            element.getSaldo() >= 0.0 ? INT_TRUE : INT_FALSE),
                    false) + getFinishedValueForJavaScriptFunction(lastShoppingString, false)
                    +
                    getFinishedValueForJavaScriptFunction(
                            (element.getIstGesperrt()
                                ?
                                    CUMSTEMER_IS_BLOCKED_STRING : ""), false)
                    +
                   getFinishedValueForJavaScriptFunction(
                            replaceGermanCharacters.replaceGermanUmlauts(decisionString), false)
                    +
                    getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                                    .replaceGermanUmlauts(element.getBemerkungen()),
                            true) + FUNCTION_END;

        }

        int totalPersons = totalAdults + totalChildren;
        String resultSetParameter = FUNCTION_SET_PARAMETER + getFinishedValueForJavaScriptFunction(
                replaceGermanCharacters.replaceGermanUmlauts(distributionPoint.getBezeichnung()),
                false) + getFinishedValueForJavaScriptFunction(
                replaceGermanCharacters.replaceGermanUmlauts(productType.getName()), false)
                +
                getFinishedValueForJavaScriptFunction(String.valueOf(totalHouseholds), false)
                +
                getFinishedValueForJavaScriptFunction(String.valueOf(totalAdults), false)
                +
                getFinishedValueForJavaScriptFunction(String.valueOf(totalChildren), false)
                +
                getFinishedValueForJavaScriptFunction(String.valueOf(totalPersons), true)
                +
                FUNCTION_END;

        return SCRIPT_TAG_OPEN + resultAddRow + resultSetParameter + SCRIPT_TAG_CLOSE;
    }

    /**
     * This function create a observable list of the type "order by".
     * @return observable list for a combobox
     */
    private ObservableList<OrderBy> orderBy()
    {
        ArrayList<OrderBy> orderByArrayList = new ArrayList<>();
        orderByArrayList.add(new OrderBy(ORDER_BY_HOUSEHOLD_ID[FIRST_POSITION],
                ORDER_BY_HOUSEHOLD_ID[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_SURNAME[FIRST_POSITION],
                ORDER_BY_SURNAME[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_FIRST_NANEM[FIRST_POSITION],
                ORDER_BY_FIRST_NANEM[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_STREET[FIRST_POSITION],
                ORDER_BY_STREET[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_POSTCODE[FIRST_POSITION],
                ORDER_BY_POSTCODE[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_LOCATION[FIRST_POSITION],
                ORDER_BY_LOCATION[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_DISTRIBUTION_POINT[FIRST_POSITION],
                ORDER_BY_DISTRIBUTION_POINT[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_OUTPUT_GROUP[FIRST_POSITION],
                ORDER_BY_OUTPUT_GROUP[SECOND_POSITION]));

        ObservableList<OrderBy> orderByObservableList =
                FXCollections.observableArrayList(orderByArrayList);
        return orderByObservableList;
    }

    /**
     * This function adjust a string to a string with high comma.
     * @param value
     * @param lastParameter
     */
    private String getFinishedValueForJavaScriptFunction(String value, Boolean lastParameter)
    {
        if (!lastParameter)
        {
            return (HIGH_COMMA + value + HIGH_COMMA + SEPERATOR);
        } else
        {
            return (HIGH_COMMA + value + HIGH_COMMA);
        }
    }

    /**
     * Set the correct font size.
     * @param fontSize
     */
    public void setFontSize(Double fontSize)
    {
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);

        ArrayList<Label> labelArrayList = new ArrayList<>(
                Arrays.asList(labelHeader, labelTemplate, labelDistributionPoint, labelProductType,
                        labelOrderBy));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>(
                Arrays.asList(cbxAutomatischEinstellen, cbxArchivierteKunden, cbxBescheidarten,
                        cbxGesperrteKunden));
        changeFontSize.changeFontSizeFromCheckBoxArrayList(checkBoxArrayList, newFontSize);

        @SuppressWarnings("rawtypes")
		ArrayList<ComboBox> comboBoxArrayList = new ArrayList<>(
                Arrays.asList(cbWarentyp, cbVerteilstelle, cbSortierenNach, cbVorlage));
        changeFontSize.changeFontSizeFromComboBoxArrayList(comboBoxArrayList, newFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(buttenCancel, buttonRun));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);
    }

}
