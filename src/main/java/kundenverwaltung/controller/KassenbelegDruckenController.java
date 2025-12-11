package kundenverwaltung.controller;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Timestamp;
import java.time.LocalTime;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.EinkaufDAOimpl;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.dao.VorlageDAOimpl;
import kundenverwaltung.dao.WarentypDAOimpl;
import kundenverwaltung.model.Einkauf;
import kundenverwaltung.model.Familienmitglied;
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

public class KassenbelegDruckenController extends Thread
{

    private static final ObservableList<String> PERIODE_OBSERVABLELIST = FXCollections.observableArrayList("Gesamt", "Vormittag", "Nachmittag");
    private static final ObservableList<String> SORT_ASC_DESC_OBSERVABLELIST = FXCollections.observableArrayList("aufsteigend", "absteigend");
    private static final String[] ORDER_BY_TIME_STAMP = {"Erfassungszeit", "erfassungsZeit"};
    private static final String[] ORDER_BY_DISTRIBUTION_POINT = {"Verteilstelle", "beiVerteilstelle"};
    private static final String[] ORDER_BY_HOUSEHOLD_ID = {"Kundennummer (HaushaltsID)", "kunde"};
    private static final String[] ORDER_BY_CUSTOMER_ID = {"Personennummer", "person"};
    private static final String[] ORDER_BY_SURNAME = {"Nachname", "nName"};
    private static final String[] ORDER_BY_FIRST_NAME = {"Vorname", "vName"};

    private static final String NOTIFICATION_TITEL_DEFAULT = "Achtung!";
    private static final String NOTIFICATION_TEXT_ILLEGAL_USER_INPUT = "Bitte wählen Sie zwischen \"Aktuellen Kunden\""
            + "\n und \"Alle Kunden folgender Verteilstelle\"";

    private static final int FIRST_POSITION = 0;
    private static final int SECOND_POSITION = 1;
    private static final int ITEM_POSITION_SORT_ASCENDING = 0;
    private static final int ITEM_POSITION_MORNING = 1;
    private static final int ITEM_POSITION_AFTERNOON = 2;
    private static final int INT_TRUE = 1;
    private static final int INT_FALSE = 0;

    private static final String PLACEHOLDER_FOR_FUNCTIONS = "%PLACEHOLDER_FOR_FUNCTIONS%";
    private static final String PLACEHOLDER_END_OF_FILE = "%END_OF_FILE%";

    private static final String FUNCTION_SET_PARAMETER = "setParameter(";
    private static final String FUNCTION_ADD_ROW = "addRow(";
    private static final String FUNCTION_SET_TOTAL_SHOPPING_AND_PAYMENT_IN_TABLE_SALES_TODAY = "setTotalShoppingPaymentSalesToday(";
    private static final String FUNCTION_SET_TOTAL_SHOPPING_AND_PAYMENT_IN_TABLE_SALES_AND_CANCELED_TODAY = "setTotalShoppingPaymentSalesCanceledToday(";
    private static final String FUNCTION_SET_TOTAL_SHOPPING_AND_PAYMENT_IN_TABLE_SALES_EARLIER_AND_CANCELED_TODAY = "setTotalShoppingPaymentSalesEarlierCanceledToday(";
    @SuppressWarnings("unused")
    private static final String FUNCTION_SET_CASHIER_VALUE = "setCashierValue(";
    private static final String FUNCTION_END = ");";
    private static final String SEPERATOR = ",";
    private static final String HIGH_COMMA = "\"";
    private static final String SCRIPT_TAG_OPEN = "<script>";
    private static final String SCRIPT_TAG_CLOSE = "</script>";
    private static final String HTML_TAG_CLOSE = "</html>";

    private static final String TABLE_ID_SALES_TODAY = "salesToday";
    private static final String TABLE_ID_SALES_AND_CANCELED_TODAY = "salesCanceledToday";
    private static final String TABLE_ID_SALES_EARLIER_AND_CANCELED_TODAY = "salesEarlierCanceledToday";

    @FXML
    private Label labelHeader;
    @FXML
    private Label labelTamplate;
    @FXML
    private Label labelDate;
    @FXML
    private Label labelPeriod;
    @FXML
    private Label lbVerteilstelle;
    @FXML
    private Label labelOrderBy;
    @FXML
    private Label labelProductType;
    @FXML
    private ComboBox<String> cbZeitraum;
    @FXML
    private ComboBox<String> cbSortierreihnfolge;
    @FXML
    private ComboBox<Warentyp> cbWarentyp;
    @FXML
    private ComboBox<Vorlage> cbVorlage;
    @FXML
    private ComboBox<OrderBy> cbSortierenNach;
    @FXML
    private ComboBox<Verteilstelle> cbVerteilstelle;
    @FXML
    private CheckBox cbxLiefernummer;
    @FXML
    private CheckBox cbxVerteilstelleAutomatisch;
    @FXML
    private CheckBox cbxZeitraumAutomatisch;
    @FXML
    private CheckBox cbxEinstellungenAlsStandard;
    @FXML
    private DatePicker datum;
    @FXML
    private RadioButton rbAktuellerKunde;
    @FXML
    private RadioButton rbAlleKunden;
    @FXML
    private Button buttonCreate;
    @FXML
    private Button buttonCancel;

    @FXML
    private ToggleGroup waehleKunde;

    private ChangeDateFormat changeDateFormat = new ChangeDateFormat();
    private ChangeFontSize changeFontSize = new ChangeFontSize();
    private ReplaceGermanCharacters replaceGermanCharacters = new ReplaceGermanCharacters();
    private ParseToEuroFormat parseToEuroFormat = new ParseToEuroFormat();
    private VorlageDAOimpl vorlageDAOimpl = new VorlageDAOimpl();
    private VerteilstelleDAOimpl verteilstelleDAOimpl = new VerteilstelleDAOimpl();
    private EinkaufDAOimpl einkaufDAOimpl = new EinkaufDAOimpl();
    private FamilienmitgliedDAOimpl familienmitgliedDAOimpl = new FamilienmitgliedDAOimpl();

    private ArrayList<Vorlage> templatesArrayList = vorlageDAOimpl.getTemplates(Vorlagearten.Kassenabrechnung);
    private ObservableList<Vorlage> templateObserverList = FXCollections.observableArrayList(templatesArrayList);
    private ArrayList<Verteilstelle> distributionsPointsArrayList = new VerteilstelleDAOimpl().readAll();
    private ObservableList<Verteilstelle> distributionsPointsObservableList = FXCollections.observableArrayList(distributionsPointsArrayList);
    private ArrayList<Warentyp> productTypsArrayList = new WarentypDAOimpl().readAll();
    private Warentyp invalidProductType = new Warentyp(-1, "Alle", 0, 0, false);
    private ObservableList<Warentyp> productTypsObservableList;

    private Familienmitglied familyMember;
    private Timestamp periodeStart;
    private Timestamp periodeEnd;
    private Verteilstelle distributionPoint;
    private OrderBy orderBy;
    private Boolean ascending;
    private Warentyp productType;
    private Familienmitglied selectedCustomer;

    private IndeterminateProgressBar indeterminateProgressBar = new IndeterminateProgressBar();
    private BlobToTemplate blobToTemplate = new BlobToTemplate();
    /**
     * initialize of GUI elements.
     */

    @FXML
    public void initialize()
    {
        // Set up the toggle group for the customer selection options
        waehleKunde = new ToggleGroup();
        rbAktuellerKunde.setToggleGroup(waehleKunde);
        rbAlleKunden.setToggleGroup(waehleKunde);
        buttonCreate.setDisable(true);  // Initially disable the "Erstellen" button

        // Add the toggle listener for customer selection
        waehleKunde.selectedToggleProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue == rbAktuellerKunde)
            {
                if (selectedCustomer == null)
                {
                    showNoCustomerSelectedWarning();
                    buttonCreate.setDisable(true);  // Disable button if no customer is selected
                } else
                {
                    // Ensure that customer data is correctly reset or reloaded
                    reloadCustomerData(selectedCustomer);  // Call a method to handle customer reload (implement if necessary)
                    buttonCreate.setDisable(false);  // Enable button
                }
            } else if (newValue == rbAlleKunden)
            {
                disabledDistributionPoint(false);  // Enable distribution point selection
                buttonCreate.setDisable(false);  // Enable button
            } else
            {
                buttonCreate.setDisable(true);  // Disable button if no valid selection
            }
        });

        // Initialize combo boxes and other fields
        cbVorlage.setItems(templateObserverList);
        cbVorlage.getSelectionModel().selectFirst();
        cbVorlage.setConverter(new StringConverter<Vorlage>()
        {
            @Override
            public String toString(Vorlage object)
            {
                return object.getName();
            }

            @Override
            public Vorlage fromString(String string)
            {
                return null;
            }
        });

        datum.setConverter(changeDateFormat.convertDatePickerFormat());
        datum.setValue(java.time.LocalDate.now());

        cbZeitraum.setItems(PERIODE_OBSERVABLELIST);
        cbZeitraum.getSelectionModel().selectFirst();

        cbVerteilstelle.setItems(distributionsPointsObservableList);
        cbVerteilstelle.getSelectionModel().selectFirst();

        cbSortierenNach.setItems(orderBy());
        cbSortierenNach.getSelectionModel().selectFirst();
        cbSortierenNach.setConverter(new StringConverter<OrderBy>()
        {
            @Override
            public String toString(OrderBy object)
            {
                return object.getTerm();
            }

            @Override
            public OrderBy fromString(String string)
            {
                return null;
            }
        });

        cbSortierreihnfolge.setItems(SORT_ASC_DESC_OBSERVABLELIST);
        cbSortierreihnfolge.getSelectionModel().selectFirst();

        productTypsArrayList.add(FIRST_POSITION, invalidProductType);
        productTypsObservableList = FXCollections.observableArrayList(productTypsArrayList);
        cbWarentyp.setItems(productTypsObservableList);
        cbWarentyp.getSelectionModel().selectFirst();
    }


    /**
     */
    public void setSelectedCustomer(Familienmitglied customer)
    {
        this.selectedCustomer = customer;
        if (customer != null)
        {
            buttonCreate.setDisable(false);  // Enable the button if a customer is selected
        }
    }

  private void showNoCustomerSelectedWarning()
  {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Kein Kunde ausgewählt");
      alert.setHeaderText(null);
      alert.setContentText("Bitte wählen Sie zuerst einen Kunden aus dem Hauptfenster aus.");
      alert.showAndWait();
  }

    /**
     * this function creates an CashSettlement Document
     * functions as Follows:
     * 1. gets template from Database (converts blob to html)
     * 2. generates the Barcode
     * 3. writes new html and rewrites the placeholder strings into correct data
     * 4. opens the new file (the file has an print pdf function built in)
     * 5. deletes all temporary files
     * It also uses additional thread to run the progress bar
     *
     */
    @SuppressWarnings("unchecked")
    @FXML
    public void createCashSettlement()
    {
        // Check if a customer has been selected if "Aktueller Kunde" is selected
        if (rbAktuellerKunde.isSelected() && selectedCustomer == null)
        {
            showNoCustomerSelectedWarning();  // Show warning if no customer is selected
            return;
        }

        // Start the progress bar in a new window
        Stage taskUpdateStage = new Stage();
        indeterminateProgressBar.start(taskUpdateStage);

        // Create a new task for generating the cash settlement
        Task<Void> creatingCashSettlement = new Task<Void>()
        {
            @Override
            protected Void call() throws Exception
            {

                // Validate user input
                if (readUserInput())
                {
                    try
                    {
                        // Get the selected template from the combobox
                        Blob blobFile = cbVorlage.getSelectionModel().getSelectedItem().getDaten();

                        // Convert the template blob to an HTML file
                        blobToTemplate.convertBlobToTemplate(blobFile);

                        // Read the template file and prepare for writing a new HTML file
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(blobToTemplate.getBlobTempFileOut()));
                        File tempReadyHtml = File.createTempFile("readyHtml", ".html");
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempReadyHtml));

                        int tempCountRow = 1;
                        int findPlaceholder;
                        String row;

                        // Iterate over each line in the template file
                        while ((row = bufferedReader.readLine()) != null)
                        {
                            findPlaceholder = row.indexOf(PLACEHOLDER_FOR_FUNCTIONS);
                            if (findPlaceholder > -1)
                            {
                                // Replace placeholders with actual data and JavaScript functions
                                row = row.replace(PLACEHOLDER_END_OF_FILE, "");
                                row = row.replace(PLACEHOLDER_FOR_FUNCTIONS,
                                    setParameterCashSettlement() + "\n"
                                +
                                    addRow(TABLE_ID_SALES_TODAY) + "\n"
                                        +
                                    addRow(TABLE_ID_SALES_AND_CANCELED_TODAY) + "\n"
                                    +
                                    addRow(TABLE_ID_SALES_EARLIER_AND_CANCELED_TODAY)
                                );
                                bufferedWriter.write(row + HTML_TAG_CLOSE);
                                break;
                            }
                            bufferedWriter.write(row);
                            System.out.println(tempCountRow + ": " + row);
                            tempCountRow++;
                        }

                        // Close streams and open the newly created HTML file
                        bufferedWriter.close();
                        bufferedReader.close();
                        Desktop.getDesktop().open(tempReadyHtml);
                        tempReadyHtml.deleteOnExit();  // Ensure the file is deleted after use

                    } catch (IOException ioException)
                    {
                        ioException.printStackTrace();
                    }
                }
                return null;
            }
        };

        // After the task is completed, close the progress bar window
        creatingCashSettlement.setOnSucceeded(new EventHandler<WorkerStateEvent>()
        {
            @Override
            public void handle(WorkerStateEvent t)
            {
                taskUpdateStage.hide();  // Close the progress bar window
            }
        });

        // Show the progress bar and start the task in a new thread
        taskUpdateStage.show();
        new Thread(creatingCashSettlement).start();
    }


    /**
     * This function close this window.
     */
    @FXML
    public void erstellenAbbrechen()
    {
    	Stage stage = (Stage) lbVerteilstelle.getScene().getWindow();
        stage.close();
    }

    /**
     * This function set different variables after the user input.
     * @return
     */
    private Boolean readUserInput()
    {
        int selectedPeriodIndex = cbZeitraum.getSelectionModel().getSelectedIndex();
        switch (selectedPeriodIndex)
        {
            case ITEM_POSITION_MORNING:
                periodeStart = Timestamp.valueOf(datum.getValue().atTime(LocalTime.MIN));
				periodeEnd = Timestamp.valueOf(datum.getValue().atTime(LocalTime.NOON));
                break;
            case ITEM_POSITION_AFTERNOON:
				periodeStart = Timestamp.valueOf(datum.getValue().atTime(LocalTime.NOON));
				periodeEnd = Timestamp.valueOf(datum.getValue().atTime(LocalTime.MAX));
                break;
            default:
				periodeStart = Timestamp.valueOf(datum.getValue().atTime(LocalTime.MIN));
				periodeEnd = Timestamp.valueOf(datum.getValue().atTime(LocalTime.MAX));
                break;
        }

        if (rbAktuellerKunde.isSelected())
        {
            distributionPoint = verteilstelleDAOimpl.read(familyMember.getHaushalt().getVerteilstelle().getVerteilstellenId());
        } else
            {
                distributionPoint = cbVerteilstelle.getValue();
                familyMember = null;
            }

        orderBy = cbSortierenNach.getValue();
        if (cbSortierreihnfolge.getSelectionModel().getSelectedIndex() == ITEM_POSITION_SORT_ASCENDING)
        {
            ascending = true;
        } else
            {
                ascending = false;
            }

        productType = cbWarentyp.getValue();
        return checkUserInput();
    }

    /**
     * This function check the complete user input.
     * @return
     */
    private Boolean checkUserInput()
    {
        if (rbAktuellerKunde.isSelected() || rbAlleKunden.isSelected())
        {
            return true;
        } else
            {
                Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_TEXT_ILLEGAL_USER_INPUT);
                 return false;
            }
    }

    /**
     * This function create a observable list of the type "order by".
     * @return observable list for a combobox
     */
    private ObservableList<OrderBy> orderBy()
    {
        ArrayList<OrderBy> orderByArrayList = new ArrayList<>();
        orderByArrayList.add(new OrderBy(ORDER_BY_TIME_STAMP[FIRST_POSITION], ORDER_BY_TIME_STAMP[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_DISTRIBUTION_POINT[FIRST_POSITION], ORDER_BY_DISTRIBUTION_POINT[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_HOUSEHOLD_ID[FIRST_POSITION], ORDER_BY_HOUSEHOLD_ID[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_CUSTOMER_ID[FIRST_POSITION], ORDER_BY_CUSTOMER_ID[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_FIRST_NAME[FIRST_POSITION], ORDER_BY_FIRST_NAME[SECOND_POSITION]));
        orderByArrayList.add(new OrderBy(ORDER_BY_SURNAME[FIRST_POSITION], ORDER_BY_SURNAME[SECOND_POSITION]));

        ObservableList<OrderBy> orderByObservableList = FXCollections.observableArrayList(orderByArrayList);
        return orderByObservableList;
    }

    /**
     * This function return a javascript-function (setParameter) as string. This string past in the template file.
     * @return
     */
    private String setParameterCashSettlement()
    {
        String date = changeDateFormat.changeDateToDefaultString(datum.getValue());
        String employee = "Administrator";
        String distributionPoint = "Hauptstelle";
        String listNumber = "K.A. was hier stehen soll.";
        String periode = cbZeitraum.getSelectionModel().getSelectedItem();

        String result = SCRIPT_TAG_OPEN + FUNCTION_SET_PARAMETER
                    + getFinishedValueForJavaScriptFunction(date, false)
                    + getFinishedValueForJavaScriptFunction(employee, false)
                    + getFinishedValueForJavaScriptFunction(distributionPoint, false)
                    + getFinishedValueForJavaScriptFunction(listNumber, false)
                    + getFinishedValueForJavaScriptFunction(periode, true)
                + FUNCTION_END + SCRIPT_TAG_CLOSE;

        return result;
    }

    private void reloadCustomerData(Familienmitglied customer)
    {
      if (customer != null)
      {
          // Reset or reload any relevant customer data
          // Example: update UI components with current customer details
          // Ensure that no unnecessary loading processes are triggered
          System.out.println("Reloading customer data: " + customer.getName());
      } else
      {
          showNoCustomerSelectedWarning();
      }
  }


    /**
     * This function return a javascript-function (addRow) as string. This string past in the template file.
     * @param tableId
     * @return
     */
    private String addRow(String tableId)
    {
        String functionStart = FUNCTION_ADD_ROW;
        String resultString = "";
        String functionSetTotalShoppingPayment = "";
        Double totalShopping = 0.0;
        Double totalPayment = 0.0;
        ArrayList<Einkauf> shoppingArrayList;
        switch (tableId)
        {
            case TABLE_ID_SALES_TODAY:
                shoppingArrayList = einkaufDAOimpl.getAllSalesToday(periodeStart, periodeEnd, familyMember, distributionPoint, orderBy, ascending, productType, false, false);
                functionStart += getFinishedValueForJavaScriptFunction(TABLE_ID_SALES_TODAY, false);
                functionSetTotalShoppingPayment += FUNCTION_SET_TOTAL_SHOPPING_AND_PAYMENT_IN_TABLE_SALES_TODAY;
                break;
            case TABLE_ID_SALES_AND_CANCELED_TODAY:
                shoppingArrayList = einkaufDAOimpl.getAllSalesToday(periodeStart, periodeEnd, familyMember, distributionPoint, orderBy, ascending, productType, true, false);
                functionStart += getFinishedValueForJavaScriptFunction(TABLE_ID_SALES_AND_CANCELED_TODAY, false);
                functionSetTotalShoppingPayment += FUNCTION_SET_TOTAL_SHOPPING_AND_PAYMENT_IN_TABLE_SALES_AND_CANCELED_TODAY;
                break;
            case TABLE_ID_SALES_EARLIER_AND_CANCELED_TODAY:
                shoppingArrayList = einkaufDAOimpl.getAllSalesToday(periodeStart, periodeEnd, familyMember, distributionPoint, orderBy, ascending, productType, false, true);
                functionStart += getFinishedValueForJavaScriptFunction(TABLE_ID_SALES_EARLIER_AND_CANCELED_TODAY, false);
                functionSetTotalShoppingPayment += FUNCTION_SET_TOTAL_SHOPPING_AND_PAYMENT_IN_TABLE_SALES_EARLIER_AND_CANCELED_TODAY;
                break;
            default:
                return null;
        }

        for (int runVar = 0; runVar < shoppingArrayList.size(); runVar++)
        {
            resultString += functionStart
                    + getFinishedValueForJavaScriptFunction(String.valueOf(runVar + 1), false)
                    + getFinishedValueForJavaScriptFunction(changeDateFormat.changeDateToDefaultString(shoppingArrayList.get(runVar).getErfassungsZeit().toLocalDate()), false)
                    + getFinishedValueForJavaScriptFunction(String.valueOf(shoppingArrayList.get(runVar).getKunde().getKundennummer()), false)
                    + getFinishedValueForJavaScriptFunction(replaceGermanCharacters.replaceGermanUmlauts(shoppingArrayList.get(runVar).getPerson().getName()), false)
                    + getFinishedValueForJavaScriptFunction(shoppingArrayList.get(runVar).getWarentyp().getName(), false)
                    + getFinishedValueForJavaScriptFunction(replaceGermanCharacters.replaceGermanUmlauts(parseToEuroFormat.parseFloatToEuroString(shoppingArrayList.get(runVar).getSummeEinkauf(), true)), false)
                    + getFinishedValueForJavaScriptFunction(replaceGermanCharacters.replaceGermanUmlauts(parseToEuroFormat.parseFloatToEuroString(shoppingArrayList.get(runVar).getSummeZahlung(), true)), false)
                    + getFinishedValueForJavaScriptFunction(replaceGermanCharacters.replaceGermanUmlauts(parseToEuroFormat.parseFloatToEuroString(shoppingArrayList.get(runVar).getKunde().getSaldo(), true)), false)
                    + getFinishedValueForJavaScriptFunction(replaceGermanCharacters.replaceGermanUmlauts(shoppingArrayList.get(runVar).getBeiVerteilstelle().getBezeichnung()), false)
                    + getFinishedValueForJavaScriptFunction(replaceGermanCharacters.replaceGermanUmlauts(shoppingArrayList.get(runVar).getKunde().
                    getHaushaltsvorstand(familienmitgliedDAOimpl.getAllFamilienmitglieder(shoppingArrayList.get(runVar).getKunde().getKundennummer())).getName()), false)
                    + getFinishedValueForJavaScriptFunction(replaceGermanCharacters.replaceGermanUmlauts(shoppingArrayList.get(runVar).getBuchungstext()), false)
                    + getFinishedValueForJavaScriptFunction(String.valueOf((shoppingArrayList.get(runVar).getPerson().isAdult()) ? INT_TRUE : INT_FALSE), true)
                    + FUNCTION_END;

            totalShopping += shoppingArrayList.get(runVar).getSummeEinkauf();
            totalPayment += shoppingArrayList.get(runVar).getSummeZahlung();
        }

        String buildFunctionSetTotal = functionSetTotalShoppingPayment
                + getFinishedValueForJavaScriptFunction(parseToEuroFormat.parseFloatToEuroString(totalShopping, false), false)
                + getFinishedValueForJavaScriptFunction(parseToEuroFormat.parseFloatToEuroString(totalPayment, false), true)
                + FUNCTION_END;

        return (SCRIPT_TAG_OPEN + resultString + buildFunctionSetTotal +  SCRIPT_TAG_CLOSE);
    }


    /**
     * This function adjust a string to a string with high comma.
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

    /**
     * This function disabled, enabled a combobox and a checkbox.
     * @param disabled
     */
    private void disabledDistributionPoint(Boolean disabled)
    {
        lbVerteilstelle.setDisable(disabled);
        cbVerteilstelle.setDisable(disabled);
        cbxVerteilstelleAutomatisch.setDisable(disabled);
    }

    /**
     * Set the correct font size.
     * @param fontSize
     */
    public void setFontSize(Double fontSize)
    {
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);

        ArrayList<Label> labelArrayList = new ArrayList<>(Arrays.asList(
                    labelHeader, labelTamplate, labelDate, labelPeriod, lbVerteilstelle, labelOrderBy, labelProductType
                ));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(buttonCancel, buttonCreate));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);

        @SuppressWarnings("rawtypes")
		ArrayList<ComboBox> comboBoxArrayList = new ArrayList<>(Arrays.asList(
                        cbVorlage, cbSortierenNach, cbVerteilstelle, cbWarentyp, cbZeitraum
                ));
        changeFontSize.changeFontSizeFromComboBoxArrayList(comboBoxArrayList, newFontSize);

        ArrayList<DatePicker> datePickerArrayList = new ArrayList<>(Arrays.asList(datum));
        changeFontSize.changeFontSizeFromDatePickerArrayList(datePickerArrayList, newFontSize);

        ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>(Arrays.asList(
                        cbxLiefernummer, cbxVerteilstelleAutomatisch, cbxZeitraumAutomatisch
                ));
        changeFontSize.changeFontSizeFromCheckBoxArrayList(checkBoxArrayList, newFontSize);

        ArrayList<RadioButton> radioButtonArrayList = new ArrayList<>(Arrays.asList(rbAktuellerKunde, rbAlleKunden));
        changeFontSize.changeFontSizeFromRadioButtonArrayList(radioButtonArrayList, newFontSize);

    }


    /**
     * Gets the family member.
     *
     * @return The family member.
     */
    public Familienmitglied getFamilyMember()
    {
        return familyMember;
    }

    /**
     * Sets the family member.
     *
     * @param familyMember The family member to set.
     */
    public void setFamilyMember(Familienmitglied familyMember)
    {
        this.familyMember = familyMember;
    }

    /**
     * Disables or enables the radio button for the current customer.
     *
     * @param disabled Boolean indicating whether to disable or enable the radio button.
     */
    public void disabledRadioButtonCurrentCustomer(Boolean disabled)
    {
        rbAktuellerKunde.setDisable(disabled);
    }

}
