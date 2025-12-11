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
import javafx.scene.control.*;
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
import kundenverwaltung.plugins.BarcodeGenerator;
import kundenverwaltung.toolsandworkarounds.BlobToTemplate;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;
import kundenverwaltung.toolsandworkarounds.IndeterminateProgressBar;
import kundenverwaltung.toolsandworkarounds.ReplaceGermanCharacters;

public class AusweisDruckenController extends Thread
{

    private static final ObservableList<String> SORT_ASC_DESC_OBSERVABLELIST =
            FXCollections.observableArrayList("aufsteigend", "absteigend");
    private static final String[] ORDER_BY_HOUSEHOLD_ID = {"Kundennummer", "kundennummer"};
    private static final String[] ORDER_BY_SURNAME = {"Nachname", "nName"};
    private static final String[] ORDER_BY_FIRST_NAME = {"Vorname", "vName"};
    private static final String[] ORDER_BY_STREET = {"Straße", "strasse"};
    private static final String[] ORDER_BY_POSTCODE = {"PLZ", "plzTemp"};
    private static final String[] ORDER_BY_LOCATION = {"Wohnort", "ort"};
    private static final String[] ORDER_BY_DISTRIBUTION_POINT = {"Verteilstelle", "bezeichnung"};
    private static final String[] ORDER_BY_OUTPUT_GROUP = {"Ausgabegruppe", "name"};

    @SuppressWarnings("unused")
    private static final String CUSTOMER_ID = "%Kundennummer%";
    @SuppressWarnings("unused")
    private static final String FIRST_NAME = "%Vorname%";
    @SuppressWarnings("unused")
    private static final String SURNAME = "%Name%";
    @SuppressWarnings("unused")
    private static final String BIRTHDAY = "%Geburtsdatum%";
    @SuppressWarnings("unused")
    private static final String ADDRESS = "%Strasse%";
    @SuppressWarnings("unused")
    private static final String POSTCODE = "%Postleitzahl%";
    @SuppressWarnings("unused")
    private static final String LOCATION = "%Ort%";
    @SuppressWarnings("unused")
    private static final String NUMBER_OF_ADULTS = "%Anzahl_Erwachsene%";
    @SuppressWarnings("unused")
    private static final String NUMBER_OF_CHILDREN = "%Anzahl_Kinder%";
    @SuppressWarnings("unused")
    private static final String ID_CARD_START = "%Ausweis_Start%";
    private static final String ID_CARD_STOP = "%Ausweis_Stop%";
    @SuppressWarnings("unused")
    private static final String BARCODE_PATH = "%Barcode%";
    private static final String NOTIFICATION_TITEL_DEFAULT = "Achtung!";
    private static final String NOTIFICATION_TEXT_ILLEGAL_USER_INPUT =
            "Bitte wählen Sie zwischen \"Aktuellen Kunden\"\n und \"Alle Kunden folgender Verteilstelle\"";
    private static final int FIRST_POSITION = 0;
    private static final int SECOND_POSITION = 1;

    private static final String FUNCTION_CREATE_TABLE = "tableCreate(";
    private static final String FUNCTION_END = ");";
    private static final String SEPERATOR = ",";
    private static final String HIGH_COMMA = "\"";
    private static final String ESCAPE = "\\\\";
    private static final String ESCAPED = "\\\\\\\\";
    private static final String SCRIPT_TAG_OPEN = "<script>";
    private static final String SCRIPT_TAG_CLOSE = "</script>";
    @SuppressWarnings("unused")
    private static final String HTML_TAG_CLOSE = "</html>";
    private static final String PLACEHOLDER_FOR_FUNCTIONS = "%PLACEHOLDER_FOR_FUNCTIONS%";
    private static final String PLACEHOLDER_END_OF_FILE = "%END_OF_FILE%";
    private static final String NEW_ID_CARD = "%Ausweis_Neu%";
    private static final String NULL = "null";


    private Familienmitglied familymember;

    @FXML private CheckBox cbxArchivierteKunden;
    @FXML private CheckBox cbxGesperrteKunden;
    @FXML private CheckBox cbxAutomatischEinstellen;
    @FXML private CheckBox cbxEinkaufsberechtigte;
    @FXML private RadioButton rbAktuellerKunde;
    @FXML private RadioButton rbAlleKunden;
    @FXML private ComboBox<Verteilstelle> cbVerteilstelle;
    @FXML private ComboBox<Vorlage> cbVorlage;
    @FXML private ComboBox<Warentyp> cbWarentyp;
    @FXML private ComboBox<String> cbSortierReihenfolge;
    @FXML private Label labelHeader;
    @FXML private Label lbVerteilstelle;
    @FXML private Label labelTemplate;
    @FXML private Button buttonCreate;
    @FXML private Button buttonCancel;
    @FXML private ComboBox<OrderBy> cbSortierenNach;


    @FXML private ToggleGroup waehleKunde;

    private ChangeFontSize changeFontSize = new ChangeFontSize();
    private ChangeDateFormat changeDateFormat = new ChangeDateFormat();
    private ReplaceGermanCharacters replaceGermanCharacters = new ReplaceGermanCharacters();

    private VorlageDAOimpl vorlageDAOimpl = new VorlageDAOimpl();
    private VerteilstelleDAOimpl verteilstelleDAOimpl = new VerteilstelleDAOimpl();
    private WarentypDAOimpl warentypDAOimpl = new WarentypDAOimpl();
    private HaushaltDAOimpl haushaltDAOimpl = new HaushaltDAOimpl();
    private FamilienmitgliedDAOimpl familienmitgliedDAOimpl = new FamilienmitgliedDAOimpl();

    private ArrayList<Vorlage> templatesArrayList =
            vorlageDAOimpl.getTemplates(Vorlagearten.Ausweis);
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
    @SuppressWarnings("unused")
    private Boolean showDecision;
    private Boolean showArchivedCustomer;
    private Boolean showBlockedCustomer;

    private IndeterminateProgressBar indeterminateProgressBar = new IndeterminateProgressBar();
    private BlobToTemplate blobToTemplate = new BlobToTemplate();
    private BarcodeGenerator barCodeUtil = new BarcodeGenerator();

    /**
     * Initialisiert Elemente für die Ausweiserstellung.
     */
    public void initialize()
    {
        cbVorlage.setItems(templateObserverList);
        cbVorlage.setConverter(new StringConverter<Vorlage>()
        {
            @Override
            public String toString(Vorlage object)
            { return object.getName();
            }
            @Override
            public Vorlage fromString(String string)
            { return null;
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
            @Override
            public String toString(OrderBy object)
            { return object.getTerm();
            }
            @Override
            public OrderBy fromString(String string)
            { return null;
            }
        });

        cbSortierReihenfolge.setItems(SORT_ASC_DESC_OBSERVABLELIST);
        cbSortierReihenfolge.getSelectionModel().selectFirst();

        // --- NEU: ToggleGroup + Listener wie im Kassenbeleg-Controller ---
        waehleKunde = new ToggleGroup();
        rbAktuellerKunde.setToggleGroup(waehleKunde);
        rbAlleKunden.setToggleGroup(waehleKunde);

        // Erstellen-Button initial sperren, bis eine gültige Auswahl vorliegt
        buttonCreate.setDisable(true);

        waehleKunde.selectedToggleProperty().addListener((obs, oldT, newT) ->
        {
            if (newT == rbAktuellerKunde)
            {
                if (familymember == null)
                {
                    Benachrichtigung.warnungBenachrichtigung(
                            "Kein Kunde ausgewählt",
                            "Bitte wählen Sie zuerst einen Kunden im Hauptfenster aus.");
                    buttonCreate.setDisable(true);
                } else
                {
                    buttonCreate.setDisable(false);
                }
            } else if (newT == rbAlleKunden)
            {
                buttonCreate.setDisable(false);
            } else
            {
                buttonCreate.setDisable(true);
            }
            // Visuals für Verteilstelle etc. aktualisieren
            checkClientsFromDistributionPoints();
        });

        // UI gemäß aktuellem Radiostatus initial justieren
        checkClientsFromDistributionPoints();
    }

    private Boolean readUserInput()
    {
        template = cbVorlage.getValue();
        orderBy = cbSortierenNach.getValue();

        // auf/absteigend aus ComboBox übernehmen
        int idx = cbSortierReihenfolge.getSelectionModel().getSelectedIndex();
        ascending = (idx == 0);

        showDecision = false;
        showArchivedCustomer = cbxArchivierteKunden.isSelected();
        showBlockedCustomer = cbxGesperrteKunden.isSelected();

        if (rbAktuellerKunde.isSelected())
        {
            // Safety: familymember muss gesetzt sein
            if (familymember == null)
            {
                Benachrichtigung.warnungBenachrichtigung("Kein Kunde ausgewählt",
                        "Bitte wählen Sie zuerst einen Kunden aus.");
                return false;
            }
            distributionPoint = verteilstelleDAOimpl
                    .read(familymember.getHaushalt().getVerteilstelle().getVerteilstellenId());
        } else
        {
            distributionPoint = cbVerteilstelle.getValue();
            // explizit für „Alle Kunden“ keine einzelne Person
            familymember = null;
        }

        productType = cbWarentyp.getValue();
        return checkUserInput();
    }

    /**
     * Erstellt den Ausweis basierend auf der Auswahl (aktueller Kunde oder alle Kunden).
     */
    public void chooseIdType() throws IOException
    {
        // Zusätzliche Guard: aktueller Kunde gewählt, aber keiner gesetzt
        if (rbAktuellerKunde.isSelected() && familymember == null)
        {
            Benachrichtigung.warnungBenachrichtigung("Kein Kunde ausgewählt",
                    "Bitte wählen Sie zuerst einen Kunden im Hauptfenster aus.");
            return;
        }

        if (rbAktuellerKunde.isSelected())
        {
            createIDCard();
        } else if (rbAlleKunden.isSelected())
        {
            createAllIDCards();
        }
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
        return FXCollections.observableArrayList(orderByArrayList);
    }

    /**
     * Einzel-Ausweis erstellen.
     */
    @SuppressWarnings("unchecked")
    @FXML
    public void createIDCard() throws IOException
    {
        if (readUserInput())
        {
            Stage taskUpdateStage = new Stage();
            indeterminateProgressBar.start(taskUpdateStage);

            @SuppressWarnings("rawtypes")
            Task creatingCashSettlement = new Task<Void>()
            {
                @Override
                protected Void call() throws Exception
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

                        int tempCountRow = 1;
                        int findPlaceholder;
                        String row = null;
                        while ((row = bufferedReader.readLine()) != null)
                        {
                            findPlaceholder = row.indexOf(PLACEHOLDER_FOR_FUNCTIONS);
                            if (findPlaceholder > -1)
                            {
                                row = row.replace(PLACEHOLDER_END_OF_FILE, "");
                                row = row.replace(PLACEHOLDER_FOR_FUNCTIONS, createSingleIdCard());
                                row = row.replace(NEW_ID_CARD, "");
                                row = row.replace(NULL, "");
                                row = row.replace(ID_CARD_STOP, "");
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
                @Override
                public void handle(WorkerStateEvent t)
                {
                    taskUpdateStage.hide();
                }
            });

            taskUpdateStage.show();
            new Thread(creatingCashSettlement).start();
        }
    }

    /**
     * Alle Ausweise erstellen.
     */
    @SuppressWarnings("unchecked")
    public void createAllIDCards()
    {
        if (readUserInput())
        {
            Stage taskUpdateStage = new Stage();
            indeterminateProgressBar.start(taskUpdateStage);

            @SuppressWarnings("rawtypes")
            Task creatingCashSettlement = new Task<Void>()
            {
                @Override
                protected Void call() throws Exception
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

                        int tempCountRow = 1;
                        int findPlaceholder;
                        String row = null;
                        while ((row = bufferedReader.readLine()) != null)
                        {
                            findPlaceholder = row.indexOf(PLACEHOLDER_FOR_FUNCTIONS);
                            if (findPlaceholder > -1)
                            {
                                row = row.replace(PLACEHOLDER_END_OF_FILE, "");
                                row = row.replace(PLACEHOLDER_FOR_FUNCTIONS, createTableIdCard());
                                row = row.replace(NEW_ID_CARD, "");
                                row = row.replace(NULL, "");
                                row = row.replace(ID_CARD_STOP, "");
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
                @Override
                public void handle(WorkerStateEvent t)
                {
                    taskUpdateStage.hide();
                }
            });

            taskUpdateStage.show();
            new Thread(creatingCashSettlement).start();
        }
    }

    /**
     * Baut JS-Aufrufe für alle Kunden.
     */
    private String createTableIdCard()
    {
        String resultString = null;

        customerArrayList = haushaltDAOimpl
                .createCustomerList(distributionPoint, productType, orderBy, ascending,
                        showArchivedCustomer, showBlockedCustomer);

        for (Haushalt element : customerArrayList)
        {
            String city = (element.getHaushaltsvorstand(
                    familienmitgliedDAOimpl.getAllFamilienmitglieder(
                            element.getKundennummer())).getPlz()) + " "
                    + replaceGermanCharacters.replaceGermanUmlauts(element
                    .getHaushaltsvorstand(familienmitgliedDAOimpl.getAllFamilienmitglieder(
                            element.getKundennummer())).getWohnort());

            String fullName = (replaceGermanCharacters.replaceGermanUmlauts(
                    element.getHaushaltsvorstand(familienmitgliedDAOimpl
                            .getAllFamilienmitglieder(
                                    element.getKundennummer())).getvName())
                    + " " + replaceGermanCharacters.replaceGermanUmlauts(element
                    .getHaushaltsvorstand(familienmitgliedDAOimpl.getAllFamilienmitglieder(
                            element.getKundennummer())).getNachname()));

            // BUGFIX 4: Nutzung der Kundennummer (Haushalt) statt der PersonID für Barcode
            barCodeUtil.createBarCode128(String.valueOf(element.getKundennummer()));
            String barcodePath = barCodeUtil.getPathToTemp().replaceAll(ESCAPE, ESCAPED);

            resultString += FUNCTION_CREATE_TABLE
                    // BUGFIX 4: Nutzung der Kundennummer (Haushalt) statt der PersonID für Anzeige
                    + getFinishedValueForJavaScriptFunction(
                    String.valueOf(element.getKundennummer()), false)
                    + getFinishedValueForJavaScriptFunction(fullName, false)
                    + getFinishedValueForJavaScriptFunction(changeDateFormat
                    .changeDateToDefaultString(element.getHaushaltsvorstand(
                            familienmitgliedDAOimpl.getAllFamilienmitglieder(
                                    element.getKundennummer())).getGeburtsdatum()), false)
                    + getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                    .replaceGermanUmlauts(element.getHaushaltsvorstand(
                            familienmitgliedDAOimpl.getAllFamilienmitglieder(
                                    element.getKundennummer())).getAdresse()), false)
                    + getFinishedValueForJavaScriptFunction(city, false)
                    + getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                    .replaceGermanUmlauts(String.valueOf(element.getanzahlErwachsene())), false)
                    + getFinishedValueForJavaScriptFunction(replaceGermanCharacters
                    .replaceGermanUmlauts(String.valueOf(element.getanzahlKinder())), false)
                    + getFinishedValueForJavaScriptFunction(barcodePath, true)
                    + FUNCTION_END;
        }
        return (SCRIPT_TAG_OPEN + resultString + SCRIPT_TAG_CLOSE);
    }

    /**
     * Baut JS-Aufruf für einen Kunden.
     */
    private String createSingleIdCard()
    {
        String resultString = null;

        String city = (familymember.getPlz()) + " "
                + replaceGermanCharacters.replaceGermanUmlauts(familymember.getWohnort());
        String fullName =
                (replaceGermanCharacters.replaceGermanUmlauts(familymember.getvName())) + " "
                        + (replaceGermanCharacters.replaceGermanUmlauts(familymember.getnName()));

        // BUGFIX 4: Nutzung der Kundennummer (Haushalt) statt der PersonID für Barcode
        barCodeUtil.createBarCode128(String.valueOf(familymember.getHaushalt().getKundennummer()));
        String barcodePath = barCodeUtil.getPathToTemp().replaceAll(ESCAPE, ESCAPED);

        resultString += FUNCTION_CREATE_TABLE
                // BUGFIX 4: Nutzung der Kundennummer (Haushalt) statt der PersonID für Anzeige
                + getFinishedValueForJavaScriptFunction(String.valueOf(familymember.getHaushalt().getKundennummer()), false)
                + getFinishedValueForJavaScriptFunction(fullName, false)
                + getFinishedValueForJavaScriptFunction(
                changeDateFormat.changeDateToDefaultString(familymember.getGeburtsdatum()), false)
                + getFinishedValueForJavaScriptFunction(
                replaceGermanCharacters.replaceGermanUmlauts(familymember.getAdresse()), false)
                + getFinishedValueForJavaScriptFunction(city, false)
                + getFinishedValueForJavaScriptFunction(replaceGermanCharacters.replaceGermanUmlauts(
                String.valueOf(familymember.getHaushalt().getanzahlErwachsene())), false)
                + getFinishedValueForJavaScriptFunction(replaceGermanCharacters.replaceGermanUmlauts(
                String.valueOf(familymember.getHaushalt().getanzahlKinder())), false)
                + getFinishedValueForJavaScriptFunction(barcodePath, true)
                + FUNCTION_END;

        return (SCRIPT_TAG_OPEN + resultString + SCRIPT_TAG_CLOSE);
    }

    /**
     * Wert für JS-Funktion quotieren.
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
     * Aktiviert/Deaktiviert Verteilstelle-UI abhängig von Auswahl.
     */
    @FXML
    public void checkClientsFromDistributionPoints()
    {
        if (rbAlleKunden.isSelected())
        {
            cbVerteilstelle.setDisable(false);
            cbxAutomatischEinstellen.setDisable(false);
            lbVerteilstelle.setDisable(false);
            cbxArchivierteKunden.setDisable(false);
            cbxEinkaufsberechtigte.setDisable(false);
            cbxGesperrteKunden.setDisable(false);
        } else
        {
            cbVerteilstelle.setDisable(true);
            cbxAutomatischEinstellen.setDisable(true);
            lbVerteilstelle.setDisable(true);
            cbxArchivierteKunden.setDisable(true);
            cbxEinkaufsberechtigte.setDisable(true);
            cbxGesperrteKunden.setDisable(true);
        }
    }
    /**
    *
    */
    @FXML
    public void terminateIDCreation()
    {
        Stage stage = (Stage) lbVerteilstelle.getScene().getWindow();
        stage.close();
    }

    /**
     * Schriftgröße setzen.
     */
    public void setFontSize(Double fontSize)
    {
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);

        ArrayList<Label> labelArrayList =
                new ArrayList<>(Arrays.asList(labelHeader, labelTemplate, lbVerteilstelle));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        @SuppressWarnings("rawtypes")
        ArrayList<ComboBox> comboBoxArrayList =
                new ArrayList<>(Arrays.asList(cbVerteilstelle, cbVorlage));
        changeFontSize.changeFontSizeFromComboBoxArrayList(comboBoxArrayList, newFontSize);

        ArrayList<RadioButton> radioButtonArrayList =
                new ArrayList<>(Arrays.asList(rbAktuellerKunde, rbAlleKunden));
        changeFontSize.changeFontSizeFromRadioButtonArrayList(radioButtonArrayList, newFontSize);

        ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>(
                Arrays.asList(cbxArchivierteKunden, cbxEinkaufsberechtigte, cbxGesperrteKunden,
                        cbxAutomatischEinstellen));
        changeFontSize.changeFontSizeFromCheckBoxArrayList(checkBoxArrayList, newFontSize);
    }

    // ---------------------------
    // Setter / Getter Ergänzungen
    // ---------------------------

    /**
     * Setzt das aktuell selektierte Familienmitglied (ermöglicht „Aktueller Kunde“).
     */
    public void setFamilymember(Familienmitglied familymember)
    {
        this.familymember = familymember;
        if (this.familymember != null)
        {
            // „Aktueller Kunde“-Option freischalten und Erstellen erlauben
            rbAktuellerKunde.setDisable(false);
            // Button ggf. aktivieren, wenn bereits „Aktueller Kunde“ ausgewählt wurde
            if (rbAktuellerKunde.isSelected())
            {
                buttonCreate.setDisable(false);
            }
        }
    }

    /**
     * Erlaubt extern das (De-)Aktivieren des „Aktueller Kunde“-Radiobuttons.
     */
    public void setRbAktuellerKundeDisabled(boolean disabled)
    {
        rbAktuellerKunde.setDisable(disabled);
        // Wenn deaktiviert und gerade ausgewählt, auf „Alle Kunden“ umschalten
        if (disabled && rbAktuellerKunde.isSelected())
        {
            rbAlleKunden.setSelected(true);
            checkClientsFromDistributionPoints();
            buttonCreate.setDisable(false);
        }
    }
    /**
    *
    */
    public void setRbAktuellerKunde(RadioButton radioButton)
    { this.rbAktuellerKunde = radioButton;
    }
    /**
    *
    */
    public void setCbVerteilstelle(ComboBox<Verteilstelle> comboBox)
    { this.cbVerteilstelle = comboBox;
    }
    /**
    *
    */
    public void setCbxAutomatischEinstellen(CheckBox checkBox)
    { this.cbxAutomatischEinstellen = checkBox;
    }
    /**
    *
    */
    public void setLbVerteilstelle(Label label)
    { this.lbVerteilstelle = label;
    }
    /**
    *
    */
    public void setCbxArchivierteKunden(CheckBox checkBox)
    { this.cbxArchivierteKunden = checkBox;
    }
    /**
    *
    */
    public void setCbxEinkaufsberechtigte(CheckBox checkBox)
    { this.cbxEinkaufsberechtigte = checkBox;
    }
    /**
    *
    */
    public void setCbxGesperrteKunden(CheckBox checkBox)
    { this.cbxGesperrteKunden = checkBox;
    }
    /**
    *
    */
    public RadioButton getRbAktuellerKunde()
    { return this.rbAktuellerKunde;
    }
    /**
    *
    */
    public ComboBox<Verteilstelle> getCbVerteilstelle()
    { return this.cbVerteilstelle;
    }
    /**
    *
    */
    public CheckBox getCbxAutomatischEinstellen()
    { return this.cbxAutomatischEinstellen;
    }
    /**
    *
    */
    public Label getLbVerteilstelle()
    { return this.lbVerteilstelle;
    }
    /**
    *
    */
    public CheckBox getCbxArchivierteKunden()
    { return this.cbxArchivierteKunden;
    }
    /**
    *
    */
    public CheckBox getCbxEinkaufsberechtigte()
    { return this.cbxEinkaufsberechtigte;
    }
    /**
    *
    */
    public CheckBox getCbxGesperrteKunden()
    { return this.cbxGesperrteKunden;
    }

    /**
     * Gibt den Abbrechen-Button zurück.
     */
    public final Button getButtonCancel()
    {
        return this.buttonCancel;
    }
}