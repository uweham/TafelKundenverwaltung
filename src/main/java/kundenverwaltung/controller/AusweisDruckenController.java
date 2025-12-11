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
    private static final String FUNCTION_SET_CONFIG = "setConfig(";
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

    // --- NEU: Die Hybrid-Konfigurator Checkboxen ---
    @FXML private CheckBox cbxShowMarker;
    @FXML private CheckBox cbxShowGroup;
    @FXML private CheckBox cbxShowBarcodeGroup;

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

    private ArrayList<Vorlage> templatesArrayList;
    private ObservableList<Vorlage> templateObserverList;
    private ArrayList<Verteilstelle> distributionPointsArrayList;
    private ObservableList<Verteilstelle> distributionPointsObservableList;
    private ArrayList<Warentyp> productTypeArrayList;
    private ObservableList<Warentyp> productTypeObservableList;
    private ObservableList<OrderBy> orderByObservableList;

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
        templatesArrayList = vorlageDAOimpl.getTemplates(Vorlagearten.Ausweis);
        templateObserverList = FXCollections.observableArrayList(templatesArrayList);

        distributionPointsArrayList = verteilstelleDAOimpl.readAll();
        distributionPointsObservableList = FXCollections.observableArrayList(distributionPointsArrayList);

        productTypeArrayList = warentypDAOimpl.readAll();
        productTypeArrayList.add(0, invalidProductType);
        productTypeObservableList = FXCollections.observableArrayList(productTypeArrayList);
        orderByObservableList = orderBy();

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

        waehleKunde = new ToggleGroup();
        rbAktuellerKunde.setToggleGroup(waehleKunde);
        rbAlleKunden.setToggleGroup(waehleKunde);

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
            checkClientsFromDistributionPoints();
        });

        checkClientsFromDistributionPoints();
    }

    private Boolean readUserInput()
    {
        template = cbVorlage.getValue();
        orderBy = cbSortierenNach.getValue();

        int idx = cbSortierReihenfolge.getSelectionModel().getSelectedIndex();
        ascending = (idx == 0);

        showDecision = false;
        showArchivedCustomer = cbxArchivierteKunden.isSelected();
        showBlockedCustomer = cbxGesperrteKunden.isSelected();

        if (rbAktuellerKunde.isSelected())
        {
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

                        String row = null;
                        while ((row = bufferedReader.readLine()) != null)
                        {
                            if (row.contains(PLACEHOLDER_FOR_FUNCTIONS))
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

                        String row = null;
                        while ((row = bufferedReader.readLine()) != null)
                        {
                            if (row.contains(PLACEHOLDER_FOR_FUNCTIONS))
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
        StringBuilder js = new StringBuilder();

        customerArrayList = haushaltDAOimpl
                .createCustomerList(distributionPoint, productType, orderBy, ascending,
                        showArchivedCustomer, showBlockedCustomer);

        // 1. Konfiguration setzen (anhand der Checkboxen)
        // JS Signatur: setConfig(showMarker, showGroup, showBarcode2)
        js.append(FUNCTION_SET_CONFIG)
          .append(cbxShowMarker.isSelected()).append(SEPERATOR)
          .append(cbxShowGroup.isSelected()).append(SEPERATOR)
          .append(cbxShowBarcodeGroup.isSelected())
          .append(FUNCTION_END);

        for (Haushalt element : customerArrayList)
        {
            Familienmitglied vorstand = element.getHaushaltsvorstand(
                    familienmitgliedDAOimpl.getAllFamilienmitglieder(element.getKundennummer()));
            
            if(vorstand == null) continue;

            String city = element.getPlz().getPlz() + " " + replaceGermanCharacters.replaceGermanUmlauts(element.getPlz().getOrt());
            String fullName = replaceGermanCharacters.replaceGermanUmlauts(vorstand.getvName()) + " " + replaceGermanCharacters.replaceGermanUmlauts(vorstand.getnName());
            String address = replaceGermanCharacters.replaceGermanUmlauts(element.getStrasse() + " " + element.getHausnummer());

            // Barcode 1: Haushalt
            barCodeUtil.createBarCode128(String.valueOf(element.getKundennummer()));
            String bc1 = barCodeUtil.getPathToTemp().replaceAll(ESCAPE, ESCAPED);

            // Barcode 2: Gruppe (falls gewünscht, sonst leer)
            String bc2 = "";
            String groupName = "";
            if (element.getAusgabegruppe() != null) {
                groupName = replaceGermanCharacters.replaceGermanUmlauts(element.getAusgabegruppe().getName());
                if(cbxShowBarcodeGroup.isSelected()) {
                    // Erstelle Barcode für Gruppe (Hier Beispiel: ID der Gruppe)
                    barCodeUtil.createBarCode128(String.valueOf(element.getAusgabegruppe().getAusgabegruppeId()));
                    bc2 = barCodeUtil.getPathToTemp().replaceAll(ESCAPE, ESCAPED);
                }
            }

            // JS Aufruf bauen
            js.append(FUNCTION_CREATE_TABLE)
              .append(getFinishedValueForJavaScriptFunction(String.valueOf(element.getKundennummer()), false))
              .append(getFinishedValueForJavaScriptFunction(fullName, false))
              .append(getFinishedValueForJavaScriptFunction(changeDateFormat.changeDateToDefaultString(vorstand.getGeburtsdatum()), false))
              .append(getFinishedValueForJavaScriptFunction(address, false))
              .append(getFinishedValueForJavaScriptFunction(city, false))
              .append(getFinishedValueForJavaScriptFunction(groupName, false))
              .append(getFinishedValueForJavaScriptFunction(String.valueOf(element.getanzahlErwachsene()), false))
              .append(getFinishedValueForJavaScriptFunction(String.valueOf(element.getanzahlKinder()), false))
              .append(getFinishedValueForJavaScriptFunction(bc1, false)) // Barcode 1
              .append(getFinishedValueForJavaScriptFunction(bc2, true))  // Barcode 2 (letzter)
              .append(FUNCTION_END);
        }
        
        return SCRIPT_TAG_OPEN + js.toString() + SCRIPT_TAG_CLOSE;
    }

    /**
     * Baut JS-Aufruf für einen Kunden.
     */
    private String createSingleIdCard()
    {
        StringBuilder js = new StringBuilder();

        // Config setzen
        js.append(FUNCTION_SET_CONFIG)
          .append(cbxShowMarker.isSelected()).append(SEPERATOR)
          .append(cbxShowGroup.isSelected()).append(SEPERATOR)
          .append(cbxShowBarcodeGroup.isSelected())
          .append(FUNCTION_END);

        Haushalt hh = familymember.getHaushalt();
        String city = hh.getPlz().getPlz() + " " + replaceGermanCharacters.replaceGermanUmlauts(hh.getPlz().getOrt());
        String fullName = replaceGermanCharacters.replaceGermanUmlauts(familymember.getvName()) + " " + replaceGermanCharacters.replaceGermanUmlauts(familymember.getnName());
        String address = replaceGermanCharacters.replaceGermanUmlauts(hh.getStrasse() + " " + hh.getHausnummer());

        barCodeUtil.createBarCode128(String.valueOf(hh.getKundennummer()));
        String bc1 = barCodeUtil.getPathToTemp().replaceAll(ESCAPE, ESCAPED);

        String bc2 = "";
        String groupName = "";
        if (hh.getAusgabegruppe() != null) {
            groupName = replaceGermanCharacters.replaceGermanUmlauts(hh.getAusgabegruppe().getName());
            if(cbxShowBarcodeGroup.isSelected()) {
                barCodeUtil.createBarCode128(String.valueOf(hh.getAusgabegruppe().getAusgabegruppeId()));
                bc2 = barCodeUtil.getPathToTemp().replaceAll(ESCAPE, ESCAPED);
            }
        }

        js.append(FUNCTION_CREATE_TABLE)
          .append(getFinishedValueForJavaScriptFunction(String.valueOf(hh.getKundennummer()), false))
          .append(getFinishedValueForJavaScriptFunction(fullName, false))
          .append(getFinishedValueForJavaScriptFunction(changeDateFormat.changeDateToDefaultString(familymember.getGeburtsdatum()), false))
          .append(getFinishedValueForJavaScriptFunction(address, false))
          .append(getFinishedValueForJavaScriptFunction(city, false))
          .append(getFinishedValueForJavaScriptFunction(groupName, false))
          .append(getFinishedValueForJavaScriptFunction(String.valueOf(hh.getanzahlErwachsene()), false))
          .append(getFinishedValueForJavaScriptFunction(String.valueOf(hh.getanzahlKinder()), false))
          .append(getFinishedValueForJavaScriptFunction(bc1, false))
          .append(getFinishedValueForJavaScriptFunction(bc2, true))
          .append(FUNCTION_END);

        return (SCRIPT_TAG_OPEN + js.toString() + SCRIPT_TAG_CLOSE);
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