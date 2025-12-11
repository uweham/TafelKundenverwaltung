package kundenverwaltung.controller;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Blob;
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
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.dao.HaushaltDAOimpl;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.dao.VorlageDAOimpl;
import kundenverwaltung.dao.WarentypDAOimpl;
import kundenverwaltung.model.Familienmitglied;
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
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;
import kundenverwaltung.toolsandworkarounds.ReplaceGermanCharacters;

public class PrintConsentForm extends Thread
{
    @SuppressWarnings("unused")
	private static final String CUSTOMER_ID = "%Kundennummer%";
    @SuppressWarnings("unused")
    private static final String TITLE = "%Anrede%";
    @SuppressWarnings("unused")
    private static final String FIRST_NAME = "%Vorname%";
    @SuppressWarnings("unused")
    private static final String SURNAME = "%Nachname%";
    @SuppressWarnings("unused")
    private static final String GENDER = "%Geschlecht%";
    @SuppressWarnings("unused")
    private static final String BIRTHDAY = "%Geburtsdatum%";
    @SuppressWarnings("unused")
    private static final String ADDRESS = "%Strasse%";
    @SuppressWarnings("unused")
    private static final String POSTCODE = "%Postleitzahl%";
    @SuppressWarnings("unused")
    private static final String LOCATION = "%Ort%";
    @SuppressWarnings("unused")
    private static final String NATIONALITY = "%Nationalitaet%";
    @SuppressWarnings("unused")
    private static final String PHONE_NUMBER = "%Telefonnummer%";
    @SuppressWarnings("unused")
    private static final String NUMBER_OF_ADULTS = "%Anzahl_Erwachsene%";
    @SuppressWarnings("unused")
    private static final String NUMBER_OF_CHILDREN = "%Anzahl_Kinder%";
    private static final String TEMPLATE_START = "%Vorlage_Start%";
    private static final String TEMPLATE_STOP = "%Vorlage_Stop%";
    private static final String PLACEHOLDER_FOR_FUNCTIONS = "%PLACEHOLDER_FOR_FUNCTIONS%";
    private static final String PLACEHOLDER_FOR_FUNCTIONS_2 = "%PLACEHOLDER_FOR_FUNCTIONS_2%";
    private static final String PLACEHOLDER_END_OF_FILE = "%END_OF_FILE%";
    @SuppressWarnings("unused")
    private static final String STATUS = "%Status%";
    @SuppressWarnings("unused")
    private static final String HTML_TAG_CLOSE = "</html>";
    @SuppressWarnings("unused")
    private static final String FUNCTION_SET_PARAMETER = "setParameter(";
    @SuppressWarnings("unused")
    private static final String FUNCTION_ADD_ROW = "addRow(";
    private static final String FUNCTION_END = ");";
    private static final String ARRAY_FUNCTION = "familymember = [";
    private static final String ARRAY_END = "];";
    private static final String ARRAY_OBJECT_START = "{";
    private static final String ARRAY_LAST_OBJECT = "}";
    private static final String ARRAY_OBJECT_END = "},";
    private static final String SEPERATOR = ",";
    private static final String HIGH_COMMA = "\"";
    private static final String SCRIPT_TAG_OPEN = "<script>";
    private static final String SCRIPT_TAG_CLOSE = "</script>";
    private static final String COMPANY_NAME = "%Firmenname%";
    private static final String NOTIFICATION_TITEL_DEFAULT = "Achtung!";
    private static final String NOTIFICATION_TEXT_ILLEGAL_USER_INPUT =
            "Bitte wählen Sie zwischen \"Aktuellen Kunden\""
    +
                    "\n und \"Alle Kunden folgender Verteilstelle\"";
    private static final String NULL = "null";
    private static final String FUNCTION_CREATE_TABLE = "tableCreate(";
    @SuppressWarnings("unused")
    private static final String FUNCTION_CREATE_TABLE_FM = "createTableFm(";
    private static final String FUNCTION_NEW_FM_TABLE =
            "createTableFm(fmArrayLabels, familymember);";
    private static final String FUNCTION_CREATE_FM_ARRAY = "createArrayFm(";
    private static final String CUSTOMER_ID_ARRAY_VALUE = " \"Kundennummer\":";
    private static final String SURNAME_ARRAY_VALUE = " \"Nachname\":";
    private static final String FIRST_NAME_ARRAY_VALUE = " \"Vorname\":";
    private static final String DATE_OF_BIRTH_ARRAY_VALUE = "\"Geburtsdatum\":";

    private static final int FIRST_POSITION = 0;
    private static final int SECOND_POSITION = 1;

    private static final ObservableList<String> SORT_ASC_DESC_OBSERVABLELIST =
            FXCollections.observableArrayList("aufsteigend", "absteigend");
    private static final String[] ORDER_BY_HOUSEHOLD_ID = {"Kundennummer", "kundennummer"};
    //first position show in combobox, second position is the database columnname
    private static final String[] ORDER_BY_SURNAME = {"Nachname", "nName"};
    private static final String[] ORDER_BY_FIRST_NAME = {"Vorname", "vName"};
    private static final String[] ORDER_BY_STREET = {"Straße", "strasse"};
    private static final String[] ORDER_BY_POSTCODE = {"PLZ", "plzTemp"};
    private static final String[] ORDER_BY_LOCATION = {"Wohnort", "ort"};
    private static final String[] ORDER_BY_DISTRIBUTION_POINT = {"Verteilstelle", "bezeichnung"};
    private static final String[] ORDER_BY_OUTPUT_GROUP = {"Ausgabegruppe", "name"};

    @FXML
    private Label labelHeader;
    @FXML
    private Label labelTemplate;
    @FXML
    private Label labelDistributionPoint;
    @FXML
    private Label labelProductType;
    @FXML
    private Label labelOrderBy;
    @FXML
    private Label lbVerteilstelle;
    @FXML
    private CheckBox cbxAutomatischEinstellen;
    @FXML
    private CheckBox cbxArchivierteKunden;
    @FXML
    private CheckBox cbxEinstellungenAlsStandard;
    @FXML
    private CheckBox cbxGesperrteKunden;
    @FXML private ComboBox<Vorlage> cbVorlage;
    @FXML private ComboBox<Warentyp> cbWarentyp;
    @FXML private ComboBox<Verteilstelle> cbVerteilstelle;
    @FXML private ComboBox<OrderBy> cbSortierenNach;
    @FXML private ComboBox<String> cbSortierReihenfolge;
    @FXML
    private Button buttonRun;
    @FXML
    private Button buttonCancel;
    @FXML
    private RadioButton rbAktuellerKunde;
    @FXML
    private RadioButton rbAlleKunden;

    private WarentypDAOimpl warentypDAOimpl = new WarentypDAOimpl();
    private Familienmitglied familymember;
    private HaushaltDAOimpl haushaltDAOimpl = new HaushaltDAOimpl();
    private VerteilstelleDAOimpl verteilstelleDAOimpl = new VerteilstelleDAOimpl();
    private ReplaceGermanCharacters replaceGermanCharacters = new ReplaceGermanCharacters();
    private ChangeDateFormat changeDateFormat = new ChangeDateFormat();
    private VorlageDAOimpl vorlageDAOimpl = new VorlageDAOimpl();
    private ArrayList<Familienmitglied> familymembersArrayList;
    private FamilienmitgliedDAOimpl familienmitgliedDAOimpl = new FamilienmitgliedDAOimpl();
    private ArrayList<Verteilstelle> distributionsPointsArrayList =
            new VerteilstelleDAOimpl().readAll();
    @SuppressWarnings("unused")
	private ObservableList<Verteilstelle> distributionsPointsObservableLIst =
            FXCollections.observableArrayList(distributionsPointsArrayList);
    private ArrayList<Vorlage> templatesArrayList = vorlageDAOimpl.getTemplates(Vorlagearten.DSGVO);
    private ObservableList<Vorlage> templateObserverList =
            FXCollections.observableArrayList(templatesArrayList);
    private ArrayList<Haushalt> customerArrayList;
    private ObservableList<OrderBy> orderByObservableList = orderBy();

    private ArrayList<Verteilstelle> distributionPointsArrayList = verteilstelleDAOimpl.readAll();
    private ObservableList<Verteilstelle> distributionPointsObservableList =
            FXCollections.observableArrayList(distributionPointsArrayList);
    private ArrayList<Warentyp> productTypeArrayList = warentypDAOimpl.readAll();
    private ObservableList<Warentyp> productTypeObservableList;

    private Warentyp invalidProductType = new Warentyp(-1, "Alle", 0, 0, false);
    @SuppressWarnings("unused")
	private Vorlage template;
    private Verteilstelle distributionPoint;
    private Warentyp productType;
    private OrderBy orderBy;
    private Boolean ascending;
    @SuppressWarnings("unused")
	private Boolean showDecision;
    private Boolean showArchivedCustomer;
    private Boolean showBlockedCustomer;
    private String readyTableFm;
    private ChangeFontSize changeFontSize = new ChangeFontSize();

    private IndeterminateProgressBar indeterminateProgressBar = new IndeterminateProgressBar();
    private BlobToTemplate blobToTemplate = new BlobToTemplate();



    /**
     * this Method initializes all items that we will need in other functions.
     */

    public void initialize()
    {

        cbVorlage.setItems(templateObserverList);
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

        cbVorlage.getSelectionModel().selectFirst();
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
     * this function creates an Customer ID Card functions as Follows: 1. gets template from
     * Database (converts blob to html) 2. generates the Barcode 3. writes new html and rewrites the
     * placeholder strings into correct data 4. opens the new file (the file has an print pdf
     * function built in) 5. deletes all temporary files
     *
     * @throws IOException
     */

    @SuppressWarnings("unchecked")
	@FXML public void createConsentForm()
    {
        if (readUserInput())
        {
            Stage taskUpdateStage = new Stage();
            indeterminateProgressBar.start(taskUpdateStage);

            @SuppressWarnings("rawtypes")
			Task creatingCashSettlement = new Task<Void>()
            {
                @Override protected Void call() throws Exception
                {

                    try
                    {
                        Blob blobFile = cbVorlage.getSelectionModel().getSelectedItem().getDaten();

                        blobToTemplate.convertBlobToTemplate(blobFile);

                        BufferedReader bufferedReader =
                                new BufferedReader(new FileReader(blobToTemplate.getBlobTempFileOut()));
                        File tempReadyHtml = File.createTempFile("readyHtml", ".html");
                        BufferedWriter bufferedWriter =
                                new BufferedWriter(new FileWriter(tempReadyHtml));
                        String companyName = replaceGermanCharacters
                                .replaceGermanUmlauts(PropertiesFileController.getTafelName());

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
                                        createSingleConsentForm());
                                row = row.replace(PLACEHOLDER_FOR_FUNCTIONS_2, " ");
                                row = row.replace(TEMPLATE_START, "");
                                row = row.replace(NULL, "");
                                row = row.replace(TEMPLATE_STOP, "");
                                row = row.replace(COMPANY_NAME, companyName);
                                bufferedWriter.write(row);

                                break;
                            }

                            bufferedWriter.write(row);

                            System.out.println(tempCountRow + ": " + row);
                            tempCountRow++;
                        }
                        bufferedWriter.close();
                        bufferedReader.close();
                        Desktop.getDesktop().open(tempReadyHtml);
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
    }
    /**
     * Cancels the printing process.
     */
    @FXML public void cancelPrintOthers()
    {
        Stage stage = (Stage) cbVorlage.getScene().getWindow();
        stage.close();
    }
    /**
     * This function creates Consent Forms for one choosen Customer.
     */
    @FXML String createSingleConsentForm()
    {
        String resultString = null;


            readyTableFm = createStringForJsArray(familymember.getKundennummer());

            String city = String.valueOf(familymember.getPlz()) + " "
            +
            replaceGermanCharacters.replaceGermanUmlauts(familymember.getWohnort());

            String gender = familymember.getGenderString();
            if (gender == null)
            {
                gender = "keine Angabe";
            }
            Boolean state = familymember.getHaushalt().getIstGesperrt();
            String stateString;

            if (state)
            {
                stateString = "Gesperrt";
            } else
            {
                stateString = "Nicht gesperrt";
            }

            resultString += FUNCTION_CREATE_TABLE + getFinishedValueForJavaScriptFunction(
                    String.valueOf(familymember.getPersonId()), false) + getFinishedValueForJavaScriptFunction(
                    replaceGermanCharacters.replaceGermanUmlauts(familymember.getAnredeString()), false) + getFinishedValueForJavaScriptFunction(
                    replaceGermanCharacters.replaceGermanUmlauts(familymember.getNachname()),
                    false) + getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                            .replaceGermanUmlauts(familymember.getVorname()),
                    false) + getFinishedValueForJavaScriptFunction(gender, false)
                    +
                    getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                            .replaceGermanUmlauts(familymember.getAdresse()), false)
                    +
                    getFinishedValueForJavaScriptFunction(city, false)
                    +
                    getFinishedValueForJavaScriptFunction(changeDateFormat
                                    .changeDateToDefaultString(familymember.getgDatum()),
                            false) + getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                    .replaceGermanUmlauts(familymember.getNationString()), false)
                            +
                    getFinishedValueForJavaScriptFunction(city, false)
                            +
                    getFinishedValueForJavaScriptFunction(stateString, true) + FUNCTION_END + " "
                            +
                    readyTableFm;

        return (SCRIPT_TAG_OPEN + resultString + " " + SCRIPT_TAG_CLOSE);
    }



    /**
     * This function creates Consent Forms for all Customers of a choosen distribution point.
     */

    @SuppressWarnings("unchecked")
	@FXML public void createAllConsentForms()
    {
        if (readUserInput())
        {
            Stage taskUpdateStage = new Stage();
            indeterminateProgressBar.start(taskUpdateStage);

            @SuppressWarnings("rawtypes")
			Task creatingCashSettlement = new Task<Void>()
            {
                @Override protected Void call() throws Exception
                {

                    try
                    {
                        Blob blobFile = cbVorlage.getSelectionModel().getSelectedItem().getDaten();

                        blobToTemplate.convertBlobToTemplate(blobFile);

                        BufferedReader bufferedReader =
                                new BufferedReader(new FileReader(blobToTemplate.getBlobTempFileOut()));
                        File tempReadyHtml = File.createTempFile("readyHtml", ".html");
                        BufferedWriter bufferedWriter =
                                new BufferedWriter(new FileWriter(tempReadyHtml));
                        String companyName = replaceGermanCharacters
                                .replaceGermanUmlauts(PropertiesFileController.getTafelName());

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
                                        createTableConsentForm());
                                row = row.replace(PLACEHOLDER_FOR_FUNCTIONS_2, " ");
                                row = row.replace(TEMPLATE_START, "");
                                row = row.replace(NULL, "");
                                row = row.replace(TEMPLATE_STOP, "");
                                row = row.replace(COMPANY_NAME, companyName);
                                bufferedWriter.write(row);

                                break;
                            }

                            bufferedWriter.write(row);

                            System.out.println(tempCountRow + ": " + row);
                            tempCountRow++;
                        }
                        bufferedWriter.close();
                        bufferedReader.close();
                        Desktop.getDesktop().open(tempReadyHtml);
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
    }



    /**
     * This function prepares all customer data that is required for the ID.
     *
     * @return Javascript string that initialize a function inside the Template
     */

    private String createTableConsentForm()
    {
        String resultString = null;

        customerArrayList = haushaltDAOimpl
                .createCustomerList(distributionPoint, productType, orderBy, ascending,
                        showArchivedCustomer, showBlockedCustomer);

        for (Haushalt element : customerArrayList)
        {
            readyTableFm = createStringForJsArray(element
                    .getHaushaltsvorstand(familienmitgliedDAOimpl.getAllFamilienmitglieder(
                            element.getKundennummer())).getHaushalt()
                    .getKundennummer());

            String city = String.valueOf(element.getHaushaltsvorstand(
                    familienmitgliedDAOimpl.getAllFamilienmitglieder(
                            element.getKundennummer())).getPlz()) + " "
                    +
                    replaceGermanCharacters.replaceGermanUmlauts(element
                            .getHaushaltsvorstand(familienmitgliedDAOimpl.getAllFamilienmitglieder(
                                    element.getKundennummer())).getWohnort());

            String gender = element.getHaushaltsvorstand(
                    familienmitgliedDAOimpl.getAllFamilienmitglieder(
                            element.getKundennummer())).getGenderString();
            if (gender == null)
            {
                gender = "keine Angabe";
            }
            Boolean state = element.getHaushaltsvorstand(
                    familienmitgliedDAOimpl.getAllFamilienmitglieder(
                            element.getKundennummer())).getHaushalt()
                    .getIstGesperrt();
            String stateString;

            if (state)
            {
                stateString = "Gesperrt";
            } else
            {
                stateString = "Nicht gesperrt";
            }

            resultString += FUNCTION_CREATE_TABLE + getFinishedValueForJavaScriptFunction(
                    String.valueOf(element.getHaushaltsvorstand(
                            familienmitgliedDAOimpl.getAllFamilienmitglieder(
                                    element.getKundennummer()))
                            .getPersonId()), false) + getFinishedValueForJavaScriptFunction(
                    replaceGermanCharacters.replaceGermanUmlauts(element
                            .getHaushaltsvorstand(familienmitgliedDAOimpl.getAllFamilienmitglieder(
                                    element.getKundennummer()))
                            .getAnredeString()), false) + getFinishedValueForJavaScriptFunction(
                    replaceGermanCharacters.replaceGermanUmlauts(element
                            .getHaushaltsvorstand(familienmitgliedDAOimpl.getAllFamilienmitglieder(
                                    element.getKundennummer())).getnName()),
                    false) + getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                            .replaceGermanUmlauts(element.getHaushaltsvorstand(
                                    familienmitgliedDAOimpl.getAllFamilienmitglieder(
                                            element.getKundennummer())).getvName()),
                    false) + getFinishedValueForJavaScriptFunction(gender, false)
                    +
                    getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                            .replaceGermanUmlauts(element
                                    .getHaushaltsvorstand(familienmitgliedDAOimpl
                                            .getAllFamilienmitglieder(element
                                                    .getKundennummer())).getAdresse()), false)
                    +
                    getFinishedValueForJavaScriptFunction(city, false)
                                                    +
                    getFinishedValueForJavaScriptFunction(changeDateFormat
                                    .changeDateToDefaultString(element
                                            .getHaushaltsvorstand(familienmitgliedDAOimpl
                                                    .getAllFamilienmitglieder(element
                                                            .getKundennummer())).getGeburtsdatum()),
                            false) + getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                    .replaceGermanUmlauts(element.getHaushaltsvorstand(
                            familienmitgliedDAOimpl.getAllFamilienmitglieder(
                                    element.getKundennummer()))
                            .getNationString()), false)
                            +
                    getFinishedValueForJavaScriptFunction(city, false)
                            +
                    getFinishedValueForJavaScriptFunction(stateString, true) + FUNCTION_END + " "
                            +
                    readyTableFm;


        }

        return (SCRIPT_TAG_OPEN + resultString + " " + SCRIPT_TAG_CLOSE);

    }



    /**
     * this function checks if a customer was selected.
     */

    @FXML public void checkClientsFromDistributionPoints()
    {

        if (rbAlleKunden.isSelected())
        {
            cbVerteilstelle.setDisable(false);
            cbxAutomatischEinstellen.setDisable(false);
            lbVerteilstelle.setDisable(false);
            cbxArchivierteKunden.setDisable(false);
            cbxGesperrteKunden.setDisable(false);
        } else
        {
            cbVerteilstelle.setDisable(true);
            cbxAutomatischEinstellen.setDisable(true);
            lbVerteilstelle.setDisable(true);
            cbxArchivierteKunden.setDisable(true);
            cbxGesperrteKunden.setDisable(true);
        }
    }



    private ObservableList<OrderBy> orderBy()
    {
        ArrayList<OrderBy> orderByArrayList = new ArrayList<>();
        orderByArrayList.add(new OrderBy(ORDER_BY_HOUSEHOLD_ID[FIRST_POSITION],
                ORDER_BY_HOUSEHOLD_ID[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_SURNAME[FIRST_POSITION],
                ORDER_BY_SURNAME[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_FIRST_NAME[FIRST_POSITION],
                ORDER_BY_FIRST_NAME[SECOND_POSITION]));
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
     * reads users choices int the menu.
     */

    private Boolean readUserInput()
    {
        template = cbVorlage.getValue();
        orderBy = cbSortierenNach.getValue();
        ascending = true;
        showDecision = false;
        showArchivedCustomer = cbxArchivierteKunden.isSelected();
        showBlockedCustomer = cbxGesperrteKunden.isSelected();

        if (rbAktuellerKunde.isSelected())
        {
            distributionPoint = verteilstelleDAOimpl
                    .read(familymember.getHaushalt().getVerteilstelle().getVerteilstellenId());
        } else
        {
            distributionPoint = cbVerteilstelle.getValue();
            familymember = null;
        }

        productType = cbWarentyp.getValue();
        return checkUserInput();
    }
    /**
     * This method determines which type of consent form to create based on the selected option.
     * It either creates a consent form for a single customer or for all customers.
     *
     * @throws IOException if an I/O error occurs
     */
    public void chooseConsentFormType() throws IOException
    {
        if (rbAktuellerKunde.isSelected())
        {
            createConsentForm();
        } else if (rbAlleKunden.isSelected())
        {
            createAllConsentForms();
        }
    }



    /**
     * returns special characters at the right positions.
     *
     * @param value
     * @param lastParameter
     * @return
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

       /* buttonCancel.setDisable(true);
        buttonRun.setDisable(true);
        */



    /**
     * This function build a javascript String, this string past in the template.
     *
     * @Author Richard Kromm Last Changes:
     * @Author Adam Starobrzanski .@Date 17.09.2018
     */

    @SuppressWarnings("unused")
	private String createJavaScriptFunction(int customerId)
    {

        familymembersArrayList = familienmitgliedDAOimpl.getAllFamilienmitglieder(customerId);

        int totalFamilyMembers = familymembersArrayList.size();
        String resultAddRow = "";

        if (totalFamilyMembers == 0)
        {
            return SCRIPT_TAG_OPEN + FUNCTION_NEW_FM_TABLE + " " + SCRIPT_TAG_CLOSE;
        } else
        {

            for (int i = 0; i < totalFamilyMembers; i++)
            {

                resultAddRow += FUNCTION_CREATE_FM_ARRAY + getFinishedValueForJavaScriptFunction(
                        String.valueOf(familymembersArrayList.get(i).getPersonId()), false)
                +
                        getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                                        .replaceGermanUmlauts(familymembersArrayList.get(i).getnName()),

                                false) + getFinishedValueForJavaScriptFunction(
                        replaceGermanCharacters
                                .replaceGermanUmlauts(familymembersArrayList.get(i).getvName()),

                        false) + getFinishedValueForJavaScriptFunction(changeDateFormat
                                .changeDateToDefaultString(familymembersArrayList.get(i).getGeburtsdatum()),
                        true)

                        + FUNCTION_END;

            }
            System.out.println("Row: " + SCRIPT_TAG_OPEN + resultAddRow + SCRIPT_TAG_CLOSE);
            return SCRIPT_TAG_OPEN + resultAddRow + " " + SCRIPT_TAG_CLOSE;
        }
    }



    private String createStringForJsArray(int customerId)
    {

        familymembersArrayList = familienmitgliedDAOimpl.getAllFamilienmitglieder(customerId);
        int totalFamilyMembers = familymembersArrayList.size();
        String resultArray = "";

        resultArray += ARRAY_FUNCTION;

        if (totalFamilyMembers == 0)
        {
            return FUNCTION_NEW_FM_TABLE;
        } else
        {

            for (int i = 0; i < totalFamilyMembers; i++)
            {
                resultArray += ARRAY_OBJECT_START + CUSTOMER_ID_ARRAY_VALUE
                    +
                        getFinishedValueForJavaScriptFunction(
                                String.valueOf(familymembersArrayList.get(i).getPersonId()),
                                false) + SURNAME_ARRAY_VALUE
                        +
                        getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                                        .replaceGermanUmlauts(familymembersArrayList.get(i).getnName()),
                                false) + FIRST_NAME_ARRAY_VALUE
                        +
                        getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                                        .replaceGermanUmlauts(familymembersArrayList.get(i).getvName()),
                                false) + DATE_OF_BIRTH_ARRAY_VALUE
                        +
                        getFinishedValueForJavaScriptFunction(changeDateFormat
                                .changeDateToDefaultString(
                                        familymembersArrayList.get(i).getGeburtsdatum()), true);
                if (0 == (i + 1) % totalFamilyMembers)
                {
                    resultArray += ARRAY_LAST_OBJECT;
                } else
                {
                    resultArray += ARRAY_OBJECT_END;
                }
            }
        }

        resultArray += ARRAY_END;

        System.out.println("Row: " + SCRIPT_TAG_OPEN + resultArray + SCRIPT_TAG_CLOSE);
        return resultArray + " " + FUNCTION_NEW_FM_TABLE;
    }
    /*private String createJavaScriptFunction()
    {

        familymembersArrayList = familienmitgliedDAOimpl
                .getAllFamilienmitglieder(familymember.getKundennummer());

        int totalFamilyMembers = familymembersArrayList.size();
        String resultAddRow = "";

        for (int i = 0; i < totalFamilyMembers; i++)
        {

            resultAddRow += FUNCTION_ADD_ROW + getFinishedValueForJavaScriptFunction(
                    String.valueOf(familymembersArrayList.get(i).getPersonId()), false) +
                    getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                                    .replaceGermanUmlauts(familymembersArrayList.get(i)
                                            .getnName()),
                            false) + getFinishedValueForJavaScriptFunction(
                    replaceGermanCharacters
                            .replaceGermanUmlauts(familymembersArrayList.get(i).getvName()),
                    false) + getFinishedValueForJavaScriptFunction(changeDateFormat
                            .changeDateToDefaultString(familymembersArrayList.get(i)
                                    .getGeburtsdatum()),
                    false)

                    + FUNCTION_END;

        }

        return SCRIPT_TAG_OPEN + resultAddRow + SCRIPT_TAG_CLOSE;
    }*/



    /**
     * @param fontSize
     * @Author Richard Kromm Set the correct font size. Last Change:
     * @Author Adam Starobrzanski
     * @Date 15.09.2018
     */
    public void setFontSize(Double fontSize)
    {
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);

        ArrayList<Label> labelArrayList =
                new ArrayList<>(Arrays.asList(labelHeader, labelTemplate, lbVerteilstelle));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        @SuppressWarnings("rawtypes")
		ArrayList<ComboBox> comboBoxArrayList =
                new ArrayList<>(Arrays.asList(cbVorlage, cbVerteilstelle));
        changeFontSize.changeFontSizeFromComboBoxArrayList(comboBoxArrayList, newFontSize);

        ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>(
                Arrays.asList(cbxAutomatischEinstellen, cbxArchivierteKunden, cbxGesperrteKunden));
        changeFontSize.changeFontSizeFromCheckBoxArrayList(checkBoxArrayList, newFontSize);

        ArrayList<RadioButton> radioButtonArrayList =
                new ArrayList<>(Arrays.asList(rbAktuellerKunde, rbAlleKunden));
        changeFontSize.changeFontSizeFromRadioButtonArrayList(radioButtonArrayList, newFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(buttonCancel, buttonRun));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);
    }



    private Boolean checkUserInput()
    {
        if (rbAktuellerKunde.isSelected() || rbAlleKunden.isSelected())
        {
            return true;
        } else
        {
            Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT,
                    NOTIFICATION_TEXT_ILLEGAL_USER_INPUT);
            return false;
        }
    }



    /**
     * Returns the radio button for selecting the current customer.
     *
     * @return the radio button for the current customer
     */
    public RadioButton getRbAktuellerKunde()
    {
        return rbAktuellerKunde;
    }


    /**
     * Sets the disabled state of the radio button for selecting the current customer.
     *
     * @param disabled the new disabled state
     */
    public void setRbAktuellerKundeDisabled(Boolean disabled)
    {
        rbAktuellerKunde.setDisable(disabled);
    }


    /**
     * Returns the currently selected family member.
     *
     * @return the currently selected family member
     */
    public Familienmitglied getFamilymember()
    {
        return familymember;
    }


    /**
     * Sets the currently selected family member.
     *
     * @param familymember the new family member
     */
    public void setFamilymember(Familienmitglied familymember)
    {
        this.familymember = familymember;
    }

}



