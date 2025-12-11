package kundenverwaltung.controller;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.*;
import kundenverwaltung.model.Einkauf;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Haushaltsinformationen;
import kundenverwaltung.model.Informationstypen;
import kundenverwaltung.model.User;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.model.Warentyp;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.service.WindowService;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import kundenverwaltung.toolsandworkarounds.ExitProgramBackupWarning;
import javafx.scene.layout.AnchorPane;


/**
 * @Author Gruppe_1
 * <p>
 * Last Change:
 * @Author Adam Starobrzanski Date: 27.07.2018
 * @Author Philipp Wilm
 */

public class MainWindowController
{
    private static final ChangeDateFormat CHANGE_DATE_FORMAT = new ChangeDateFormat();
 //   private static final ChangeFontSize CHANGE_FONT_SIZE = new ChangeFontSize();
    private static final double  MAX_FONT_SIZE = 14.5;
    private static final double MIN_FONT_SIZE = 11.5;
    private static final double ZOOM_FACTOR = 0.5;
    private static final double PRIMARY_FONT_SIZE = 13.0;
    private static final double SECONDARY_FONT_SIZE = 11.0;
    @SuppressWarnings("unused")
    private static final double DIFFERENCE_PRIMARY_SECONDARY_FONT_SIZE = PRIMARY_FONT_SIZE - SECONDARY_FONT_SIZE;

    private static final int FIND_SUBSTRING_FROM_FIRST_POSITION = 0;
    private static final int SEARCH_ALL_INDEX = 0;
    private static final int SEARCH_CUSTOMER_ID_INDEX = 1;
    private static final int SEARCH_SURNAME_INDEX = 2;
    private static final int SEARCH_FIRST_NAME_INDEX = 3;
    private static final int SEARCH_STREET_INDEX = 4;
    private static final int SEARCH_POSTCODE_OR_LOCATION_INDEX = 5;
    private static final int SEARCH_DISTRIBUTION_POINT_INDEX = 6;
    private static final int SEARCH_OUTPUT_GROUP_INDEX = 7;

    @SuppressWarnings("unused")
    private static final String ADMIN_USER_RIGHT = "Administrator";
    private static final String DISTRIBUTION_POINT_LEADER_USER_RIGHT = "Verteilstellenleiter";
    private static final String CASH_PERSONAL_USER_RIGHT = "Kassenpersonal";


    private double currentFontSize = PRIMARY_FONT_SIZE;

    @FXML
    private Menu menuFile;
    @FXML
    private Menu menuEdit;
    @FXML
    private Menu menuShow;
    @FXML
    private MenuItem menuItemUserAccount;
    @FXML
    private MenuItem menuItemUserChange;
    @FXML
    private MenuItem menuItemCreateIdCard;
    @FXML
    private MenuItem menuItemCashSettlement;
    @FXML
    private MenuItem menuItemCreateCustomerList;
    @FXML
    private MenuItem menuItemOthers;
    @FXML
    private MenuItem menuItemDbSettings;
    @FXML
    private MenuItem menuItemAdminTool;
    @FXML
    private MenuItem menuItemStatistikTool;
    @FXML
    private MenuItem menuItemExit;
    @FXML
    private MenuItem menuItemAddHousehold;
    @FXML
    private MenuItem menuItemManageHousehold;
    @FXML
    private MenuItem menuItemCommentEdit;
    @FXML
    private MenuItem menuItemDecisionEdit;
    @FXML
    private MenuItem menuItemBookingEdit;
    @FXML
    private MenuItem menuItemPowerOfAttorneyEdit;
    @FXML
    private MenuItem menuItemFontSmall;
    @FXML
    private MenuItem menuItemFontBigger;
    @FXML
    private MenuItem menuItemErrorProtocol;
    @FXML
    private MenuItem menuItemErrorReportList4Members;
    @FXML
    private MenuItem menuItemWishRequestList4Members;
    @FXML
    private MenuItem menuItemDeleteAllTableLayouts;
    @FXML
    private TextField txtSucheInput;
    @FXML
    private TextField txtHaushaltsvorstand;
    @FXML
    private TextField txtVerteilstelle;
    @FXML
    private TextField txtKundennummer;
    @FXML
    private TextField txtAnzahlFamilienmitglieder;
    @FXML
    private TextField txtAusgabegruppe;
    @FXML
    private TextField txtOertlKundennummer;
    @FXML
    private TextField txtKassiert;
    @FXML
    private TextField txtKontosaldo;
    @FXML
    private ComboBox<String> cbSucheFilter;
    @FXML
    private ComboBox<String> cbSucheSortieren;
    @FXML
    private CheckBox ckbxDSE;
    @FXML
    private CheckBox ckbxNation;
    @FXML
    private ComboBox<Warentyp> cbWarentyp;
    @FXML
    private ComboBox<Float> cbZuZahlen;
    @FXML
    private ComboBox<Verteilstelle> cbErfassungsVerteilstelle;
    @FXML
    private TextArea txtBemerkungen;
    @FXML
    private ListView<Haushaltsinformationen> listWeitereInformationen;
    @FXML
    private TableView<Familienmitglied> kundensucheOutput;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnKundennummer;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnNachname;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnVorname;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnStrasse;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnNation;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnPLZ;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnWohnort;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnGeburtsdatum;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnKundeSeit;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnVerteilstelle;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnAusgabegruppe;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnBelieferung;
    @FXML
    private DatePicker dateLetzterEinkauf;
    @FXML
    private Rectangle gruppenfarbe;
    @FXML
    private HBox haushaltHintergrund;
    @FXML
    private Button btnWeitereInformationen;
    @FXML
    private Button btnSucheKunden;
    @FXML
    private Button btnHaushaltVerwalten;
    @FXML
    private Button btnBermerkungAendern;
    @FXML
    private Button btnHaushaltHinzufuegen;
    @FXML
    private Button btnDiesesBezahlt;
    @FXML
    private Button btnInfosAendern;
    @FXML
    private Label labelKundenSuchen;
    @FXML
    private Label labelFolgendesDurchsuchen;
    @FXML
    private Label labelHaushaltsvorstand;
    @FXML
    private Label labelVerteilstelle;
    @FXML
    private Label labelKundennummer;
    @FXML
    private Label labelAnzahlFamilienmitglieder;
    @FXML
    private Label labelGruppenfarbe;
    @FXML
    private Label labelLetzterEinkauf;
    @FXML
    private Label labelWeitereInformationen;
    @FXML
    private Label labelErfassungsVerteilstelle;
    @FXML
    private Label labelBemerkungen;
    @FXML
    private Label labelWarentyp;
    @FXML
    private Label labelZuZahlen;
    @FXML
    private Label labelHeuteKassiert;
    @FXML
    private Label labelKontosaldo;
    @FXML private AnchorPane root;


    private ChangeDateFormat changeDateFormat = new ChangeDateFormat();
    private ArrayList<Warentyp> warentypListe = new WarentypDAOimpl().readAllAktiv();
    private ObservableList<Warentyp> warentypen = FXCollections.observableArrayList(warentypListe);
    @SuppressWarnings("unused")
    private ObservableList<Float> zuZahlenWerte;

    @SuppressWarnings("unused")
    private Einkauf einkauf;
    private Float saldo = 0.0F;

    //  User:
    private User user;

    //  Testobjekte
    private Haushalt haushalt;
    private Familienmitglied familienmitglied;


    private ArrayList<Familienmitglied> familienmitglieder =
            new FamilienmitgliedDAOimpl().getAllFamilienmitglieder();
    private ObservableList<Familienmitglied> familienmitgliederOL =
            FXCollections.observableArrayList(familienmitglieder);
    private ObservableList<Familienmitglied> changedFamiliyMemberObservableList
            = FXCollections.observableArrayList(familienmitglieder);


    private ArrayList<Familienmitglied> familienmitliederAkt = new ArrayList<>();
    private ArrayList<Verteilstelle> verteilstellen = new VerteilstelleDAOimpl().readAll();
    private ObservableList<Verteilstelle> verteilstellenOL =
            FXCollections.observableArrayList(verteilstellen);

    private String suche = "";
    private int filter = 1;
    private boolean genaueSuche;

    private TablePreferenceServiceImpl tablePreferenceService = new TablePreferenceServiceImpl();

    /**
     *.
     */
    @SuppressWarnings("unchecked")
    public void initialize()
    {
        columnKundennummer.setCellValueFactory(new PropertyValueFactory<>("Kundennummer"));
        columnKundennummer.setId("Kundennummer");

        columnNachname.setCellValueFactory(new PropertyValueFactory<>("Nachname"));
        columnNachname.setId("Nachname");

        columnVorname.setCellValueFactory(new PropertyValueFactory<>("Vorname"));
        columnVorname.setId("Vorname");

        columnStrasse.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
        columnStrasse.setId("Adresse");

        columnPLZ.setCellValueFactory(new PropertyValueFactory<>("Plz"));
        columnPLZ.setId("Plz");

        columnWohnort.setCellValueFactory(new PropertyValueFactory<>("Wohnort"));
        columnWohnort.setId("Wohnort");

        columnGeburtsdatum.setCellValueFactory(new PropertyValueFactory<>("BirthdayString"));
        columnGeburtsdatum.setId("BirthdayString");

        columnKundeSeit.setCellValueFactory(new PropertyValueFactory<>("CustomerSince"));
        columnKundeSeit.setId("CustomerSince");

        columnAusgabegruppe.setCellValueFactory(new PropertyValueFactory<>("Ausgabegruppe"));
        columnAusgabegruppe.setId("Ausgabegruppe");

        columnVerteilstelle.setCellValueFactory(new PropertyValueFactory<>("Verteilstelle"));
        columnVerteilstelle.setId("Verteilstelle");

        columnBelieferung.setCellValueFactory(new PropertyValueFactory<>("BelieferungString"));
        columnBelieferung.setId("BelieferungString");

        columnNation.setCellValueFactory(new PropertyValueFactory<>("NationString"));
        columnNation.setId("NationString");

        kundensucheOutput.setItems(familienmitgliederOL);


        cbErfassungsVerteilstelle.setItems(verteilstellenOL);
        cbErfassungsVerteilstelle.getSelectionModel().selectFirst();
        fuelleKassenFelder();
        cbSucheFilter.getSelectionModel().selectFirst();

        dateLetzterEinkauf.setConverter(CHANGE_DATE_FORMAT.convertDatePickerFormat());


        txtSucheInput.setOnKeyReleased(event -> searchCustomer());
        cbSucheFilter.setOnHiding(event ->
        {
            txtSucheInput.setText("");
            kundensucheOutput.setItems(familienmitgliederOL);
            txtSucheInput.requestFocus();
        });
     // Schriftgröße auf dem Root setzen (vererbt sich an fast alle Controls)
        root.setStyle(String.format("-fx-font-size: %.1fpx;", currentFontSize));

        // Zoom-Menüeinträge initial korrekt (de)aktivieren
        menuItemFontBigger.setDisable(currentFontSize >= MAX_FONT_SIZE);
        menuItemFontSmall.setDisable(currentFontSize <= MIN_FONT_SIZE);


        TablePreferenceServiceImpl.getInstance().setupPersistence(kundensucheOutput, "KundenSucheOutput");
    }


    /**
     *.
     */
    @FXML public void waehleKunde()
    {
        if (kundensucheOutput.getSelectionModel().getSelectedItem() != null)
        {
            familienmitglied = kundensucheOutput.getSelectionModel().getSelectedItem();
            if ((haushalt == null
                    || haushalt.getKundennummer() != familienmitglied.getHaushalt().getKundennummer()))
            {
                haushalt = familienmitglied.getHaushalt();
                familienmitliederAkt = new FamilienmitgliedDAOimpl().getAllFamilienmitglieder(haushalt.getKundennummer());
                fuelleKundendaten();
                fuelleKassenFelder();
            }
        }
    }


    /**
     *.
     */
    @FXML public void oeffneBenutzerprofil()
    {
        MainController.getInstance().oeffneBenutzerprofil(user, currentFontSize);
    }


    /**
     *.
     */
    @FXML public void oeffneBenutzerWechseln()
    {
        MainController.getInstance().oeffneAnmeldung();
        Stage stage = (Stage) txtSucheInput.getScene().getWindow();
        stage.close();
    }

    /**
     *.
     */
    @FXML public void openErrorProtocolWindow()
    {
        MainController.getInstance().openErrorProtocolWindow();
    }

    @FXML
    public void openErrorReportList4Members() throws IOException
    {
        WindowService.openWindow("/kundenverwaltung/fxml/errorreport/ErrorReportList4Member.fxml", "Fehlerberichte");
    }

    /**
     *.
     */

    @FXML public void oeffneAusweiseErstellen()
    {

        MainController.getInstance().oeffneAusweisDrucken(familienmitglied, currentFontSize);
    }
    /**
     *.
     */
    @FXML public void oeffneKassenabrechnung()
    {
      // Überprüfen, ob ein Kunde ausgewählt wurde
      if (familienmitglied != null)
      {
          // Controller für das Kassenbeleg-Drucken-Fenster öffnen und den ausgewählten Kunden übergeben
          KassenbelegDruckenController kassenbelegController = MainController.getInstance().oeffneKassenbelegDrucken(familienmitglied, currentFontSize);
          kassenbelegController.setSelectedCustomer(familienmitglied);  // Kunde an den neuen Controller übergeben
      } else
      {
          // Warnmeldung anzeigen, wenn kein Kunde ausgewählt wurde
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Kein Kunde ausgewählt");
          alert.setHeaderText(null);
          alert.setContentText("Bitte wählen Sie zuerst einen Kunden aus.");
          alert.showAndWait();
      }
  }

    /**
     *.
     */
    @FXML public void oeffneKundenlisteErstellen()
    {
        MainController.getInstance().oeffneKundenlisteErstellen(currentFontSize);
    }
    /**
     *.
     */
    @FXML public void oeffneSonstigesAusgeben()
    {
        MainController.getInstance().oeffneSonstigesDrucken(familienmitglied, currentFontSize);
    }
    /**
     *.
     */
    @FXML public void oeffneAdmintool()
    {
      // 1) erst die Maske blockierend öffnen
      MainController.getInstance().oeffneAdmintool();

      // 2) frisch aus der DB holen
      List<Warentyp> alle = new WarentypDAOimpl().readAllAktiv();

      // 3) die ObservableList aktualisieren
      warentypen.setAll(alle);

      // 4) und die Felder / Combos neu befüllen
      fuelleKassenFelder();
    }
    /**
     *.
     */
    @FXML public void startIexplore() throws URISyntaxException
    {
        MainController.getInstance().startIexplore();
    }

    @FXML public void oeffneDBConnection()
    {

    }
    /**
     *.
     */
    @FXML public void oeffneDBEinstellungen()
    {
        MainController.getInstance().oeffneDBVerbindung(currentFontSize);
    }
    /**
     *.
     */
    @FXML public void beendeProgramm() throws Exception
    {
        ExitProgramBackupWarning.showBackupWarning();
    }
    /**
     *.
     */
    @FXML public void oeffneHaushaltHinzufuegen()
    {
        MainController.getInstance().oeffneHaushaltHinzufuegen(currentFontSize);
        refreshCustomerTableView();
    }
    /**
     *.
     */
    @FXML public void oeffneHaushaltVerwalten()
    {
        try
        {

            haushalt = MainController.getInstance().oeffneHaushaltVerwalten(haushalt, currentFontSize);
            refreshCustomerTableView();

        } catch (NullPointerException e)
        {
            System.out.println("kein Objekt gewählt");
        }
    }

    @FXML
    private void oeffneStatistikTool()
    {
        MainController.getInstance().oeffneStatistikTool();
    }
    /**
     *.
     */
    @FXML public void oeffneBemerkungenBearbeiten()
    {

        if (haushalt != null)
        {
            haushalt = MainController.getInstance().oeffneBemerkungenBearbeiten(haushalt, currentFontSize);
            txtBemerkungen.setText(haushalt.getBemerkungen());
            familienmitgliederOL.clear();
            familienmitgliederOL.addAll(new FamilienmitgliedDAOimpl()
                    .getAllFamilienmitglieder(suche, filter, genaueSuche));
        } else
        {
            System.out.println("Kein Objekt gewählt");
        }
    }


    /**
     *.
     */
    @FXML public void oeffneBescheideBearbeiten()
    {

        if (haushalt != null && familienmitglied != null)
        {
            MainController.getInstance().oeffneBescheideBearbeiten(haushalt, currentFontSize);

            familienmitliederAkt = new FamilienmitgliedDAOimpl()
                    .getAllFamilienmitglieder(haushalt.getKundennummer());
            ObservableList<Haushaltsinformationen> informationenHaushaltOL = FXCollections
                    .observableArrayList(haushalt.getHaushaltsinformationen(familienmitliederAkt));
            listWeitereInformationen.setItems(informationenHaushaltOL);

            Boolean einkaufsberechtigt = haushalt.getEinkaufsberechtigt();

            if (einkaufsberechtigt)
            {
                haushaltHintergrund.setStyle("-fx-background-color: greenyellow");
            } else
            {
                haushaltHintergrund.setStyle("-fx-background-color: red");
            }

        } else
        {
            System.out.println("Kein Objekt gewählt");
        }
    }


    /**
     *.
     */
    @FXML public void oeffneBuchungenBearbeiten()
    {

        MainController.getInstance().oeffneBuchungenBearbeiten(haushalt, currentFontSize);
    }


    /**
     *.
     */
    @FXML public void oeffneVollmachtenBearbeiten()
    {

        if (haushalt != null)
        {
            MainController.getInstance().oeffneVollmachtenAnzeigen(haushalt, currentFontSize);
            familienmitliederAkt = new FamilienmitgliedDAOimpl()
                    .getAllFamilienmitglieder(haushalt.getKundennummer());
            ObservableList<Haushaltsinformationen> informationenHaushaltOL = FXCollections
                    .observableArrayList(haushalt.getHaushaltsinformationen(familienmitliederAkt));
            listWeitereInformationen.setItems(informationenHaushaltOL);

            Boolean einkaufsberechtigt = haushalt.getEinkaufsberechtigt();

            if (einkaufsberechtigt)
            {
                haushaltHintergrund.setStyle("-fx-background-color: greenyellow");
            } else
            {
                haushaltHintergrund.setStyle("-fx-background-color: red");
            }

        } else
        {
            System.out.println("Kein Objekt ausgewählt");
        }
    }



    @FXML public void oeffneLogdatei()
    {

    }

    @FXML public void oeffneInfos()
    {

    }



    /**
     * This function searches a customer in the tableview.
     *
     */
    @FXML
    public void searchCustomer()
    {
        changedFamiliyMemberObservableList.clear();
        changedFamiliyMemberObservableList = FXCollections.observableArrayList(familienmitglieder);
        int selectedSearchIndex = cbSucheFilter.getSelectionModel().getSelectedIndex();
        String userInput = txtSucheInput.getText().toUpperCase();

        for (int i = 0; i < changedFamiliyMemberObservableList.size(); i++)
        {
            switch (selectedSearchIndex)
            {
                case SEARCH_ALL_INDEX:
                    if ((String.valueOf(changedFamiliyMemberObservableList.get(i).getKundennummer()).indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                            && (changedFamiliyMemberObservableList.get(i).getnName().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                            && (changedFamiliyMemberObservableList.get(i).getvName().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                            && (changedFamiliyMemberObservableList.get(i).getAdresse().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                            && (changedFamiliyMemberObservableList.get(i).getPlz().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                            && (changedFamiliyMemberObservableList.get(i).getWohnort().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                            && (changedFamiliyMemberObservableList.get(i).getNationString().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                            && (changeDateFormat.changeDateToDefaultString(changedFamiliyMemberObservableList.get(i).getgDatum()).indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                            && (changedFamiliyMemberObservableList.get(i).getVerteilstelle().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                            && (changedFamiliyMemberObservableList.get(i).getAusgabegruppe().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION))
                    {
                        changedFamiliyMemberObservableList.remove(i);
                        i--;
                    }
                    break;
                case SEARCH_CUSTOMER_ID_INDEX:
                    if (String.valueOf(changedFamiliyMemberObservableList.get(i).getKundennummer()).indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                    {
                        changedFamiliyMemberObservableList.remove(i);
                        i--;
                    }
                    break;
                case SEARCH_SURNAME_INDEX:
                    if (changedFamiliyMemberObservableList.get(i).getnName().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                    {
                        changedFamiliyMemberObservableList.remove(i);
                        i--;
                    }
                    break;
                case SEARCH_FIRST_NAME_INDEX:
                    if (changedFamiliyMemberObservableList.get(i).getvName().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                    {
                        changedFamiliyMemberObservableList.remove(i);
                        i--;
                    }
                    break;
                case SEARCH_STREET_INDEX:
                    if (changedFamiliyMemberObservableList.get(i).getAdresse().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                    {
                        changedFamiliyMemberObservableList.remove(i);
                        i--;
                    }
                    break;
                case SEARCH_POSTCODE_OR_LOCATION_INDEX:
                    if (changedFamiliyMemberObservableList.get(i).getPlz().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION
                            && changedFamiliyMemberObservableList.get(i).getWohnort().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                    {
                        changedFamiliyMemberObservableList.remove(i);
                        i--;
                    }
                    break;
                case SEARCH_DISTRIBUTION_POINT_INDEX:
                    if (changedFamiliyMemberObservableList.get(i).getVerteilstelle().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                    {
                        changedFamiliyMemberObservableList.remove(i);
                        i--;
                    }
                    break;
                case SEARCH_OUTPUT_GROUP_INDEX:
                    if (changedFamiliyMemberObservableList.get(i).getAusgabegruppe().toUpperCase().indexOf(userInput) != FIND_SUBSTRING_FROM_FIRST_POSITION)
                    {
                        changedFamiliyMemberObservableList.remove(i);
                        i--;
                    }
                    break;
                default:
                    break;
            }
        }
        kundensucheOutput.setItems(changedFamiliyMemberObservableList);
    }



    /**
     * @Author Adam Starobrzanski
     * <p>
     * fixed the "saldo Berechnung"
     *
     * @Author Richard Kromm
     * fixed "save in DB"
     */

    @FXML public void umsatzBezahlt()
    {
        Warentyp warentyp = cbWarentyp.getSelectionModel().getSelectedItem();
        LocalDateTime erfassungszeit = LocalDateTime.now();
        int anzahlKinder = haushalt.getanzahlKinder();
        int anzahlErwachsene = haushalt.getanzahlErwachsene();
        Boolean warnung = true;

        try
        {
            Float summEinkauf = Float.parseFloat(txtKassiert.getText());

            if (haushalt.getBuchungswarnungen(warentyp).size() != 0)
            {
                warnung = MainController.getInstance()
                        .zeigeBuchungswarnungen(haushalt.getBuchungswarnungen(warentyp));
            }

            if (warnung)
            {
                try
                {
                    Einkauf result = null;       //Fuer Buchungstext und Familienmitglied auswahl
                    if (warentyp.getZuordnungPerson() != 0 || warentyp.getZuordnungBuchungstext() != 0)
                    {                     //0 = nicht erforderlich
                        result = MainController.getInstance()
                                .oeffneBuchungErstellen(haushalt, warentyp.getZuordnungBuchungstext(),
                                        warentyp.getZuordnungPerson(), currentFontSize);
                    }

                    saldo = saldo + (summEinkauf - cbZuZahlen.getValue());
                    haushalt.setSaldo(saldo);


                    @SuppressWarnings("unused")
                    Einkauf einkauf =
                            new Einkauf(warentyp, null, null, result.getBuchungstext(), haushalt,
                                    familienmitglied, erfassungszeit, summEinkauf,
                                    cbZuZahlen.getValue(),
                                    cbErfassungsVerteilstelle.getSelectionModel().getSelectedItem(),
                                    anzahlKinder, anzahlErwachsene);

                    Boolean updateCheck = new HaushaltDAOimpl().update(haushalt);

                    if (!updateCheck)
                    {
                        Benachrichtigung.warnungBenachrichtigung("",
                                "Der Einkauf konnte nicht gebucht werden. Bitte prüfen sie die "
                        +
                                        "Datenbank verbindung.");
                    }

                    fuelleKassenFelder();
                } catch (NullPointerException e)
                {
                    e.printStackTrace();
                    System.out.println("kein Objekt gewählt (Zeile 658)");
                }
            }
        } catch (NumberFormatException e)
            {
                Benachrichtigung.warnungBenachrichtigung("Falsche Eingabe",
                        "Bitte geben sie den Betrag im richtigen Format an.");
            }

            txtSucheInput.requestFocus();
    }



    /**
     * @Author Adam Starobrzanski
     * <p>
     * fixed the "saldo Berechnung"
     *
     * @Author Richard Kromm
     * fixed "save in DB"
     */

    @FXML public void diesesBezahlt()
    {
        Warentyp warentyp = cbWarentyp.getSelectionModel().getSelectedItem();
        LocalDateTime erfassungszeit = LocalDateTime.now();
        Float summEinkauf = haushalt.getZuZahlen(warentyp);
        Float summeZahlung = 0.0F;
        try
        {
            summeZahlung = Float.valueOf(txtKassiert.getText());
        } catch (Exception e)
        {
            Benachrichtigung.warnungBenachrichtigung("Falsche Eingabe",
                    "Bitte geben sie den Betrag im richtigen Format an.");
            return;
        }
        int anzahlKinder = haushalt.getanzahlKinder();
        int anzahlErwachsene = haushalt.getanzahlErwachsene();
        Boolean warnung = true;

        if (haushalt.getBuchungswarnungen(warentyp).size() != 0)
        {
            warnung = MainController.getInstance()
                    .zeigeBuchungswarnungen(haushalt.getBuchungswarnungen(warentyp));
        }

        if (warnung)
        {
            try
            {
                Einkauf result = null;       //Fuer Buchungstext und Familienmitglied auswahl
                if (warentyp.getZuordnungPerson() != 0 || warentyp.getZuordnungBuchungstext()
                    !=
                        0)
                {                     //0 = nicht erforderlich
                    result = MainController.getInstance()
                            .oeffneBuchungErstellen(haushalt, warentyp.getZuordnungBuchungstext(),
                                    warentyp.getZuordnungPerson(), currentFontSize);
                }

                saldo = saldo + (summeZahlung - summEinkauf);

                haushalt.setSaldo(saldo);

                @SuppressWarnings("unused")
                Einkauf einkauf =
                        new Einkauf(warentyp, null, null, result.getBuchungstext(), haushalt,
                                familienmitglied, erfassungszeit, summEinkauf, summeZahlung,
                                cbErfassungsVerteilstelle.getSelectionModel().getSelectedItem(),
                                anzahlKinder, anzahlErwachsene);
                Boolean updateCheck = new HaushaltDAOimpl().update(haushalt);

                if (!updateCheck)
                {
                    Benachrichtigung.warnungBenachrichtigung("",
                            "Der Einkauf konnte nicht gebucht werden. Bitt prüfen sie die "
                    +
                                    "Datenbank verbindung.");
                }

                fuelleKassenFelder();
            } catch (NullPointerException e)
            {
                System.out.println("kein Objekt gewählt (Zeile 722)");
            }
        }

        txtSucheInput.requestFocus();
    }


    /**
     *.
     */
    @FXML public void infosAendern()
    {
        MainController.getInstance().oeffneBuchungenBearbeiten(haushalt, currentFontSize);
    }

    /**
     *.
     */

    public void fuelleKassenFelder()
    {
        cbWarentyp.setItems(warentypen);
        cbWarentyp.getSelectionModel().selectFirst();

        cbWarentyp.setCellFactory(new Callback<ListView<Warentyp>, ListCell<Warentyp>>()
        {
            @Override public ListCell<Warentyp> call(ListView<Warentyp> l)
            {
                return new ListCell<Warentyp>()
                {
                    @Override protected void updateItem(Warentyp item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (item == null || empty)
                        {
                            setGraphic(null);
                        } else
                        {
                            setText(item.getName());
                        }
                    }
                };
            }
        });

        cbWarentyp.setConverter(new StringConverter<Warentyp>()
        {
            @Override public String toString(Warentyp warentyp)
            {
                if (warentyp == null)
                {
                    return null;
                } else
                {
                    return warentyp.getName();
                }
            }



            @Override public Warentyp fromString(String warentyp)
            {
                return null;
            }
        });

        if (haushalt != null)
        {
          Warentyp wt = cbWarentyp.getValue();

          if (!wt.isManuelleBerechnung())
          {
              // Berechne einmal zentral
              float zuZahlen = haushalt.getZuZahlen(wt);

              // Nur diesen einen Wert anbieten (kein 0.0 mehr)
              ObservableList<Float> zuZahlenWerte =
                  FXCollections.observableArrayList(zuZahlen);
              cbZuZahlen.setItems(zuZahlenWerte);
              cbZuZahlen.getSelectionModel().selectFirst();

              // Und im Textfeld vorfüllen
              txtKassiert.setText(Float.toString(zuZahlen));
          }

          // Kontosaldo aktualisieren
          saldo = haushalt.getSaldo();
          txtKontosaldo.setText(String.valueOf(saldo));
      }
    }


    /**
     *.
     */
    public void fuelleKundendaten()
    {

        //familienmitliederAkt = new FamilienmitgliedDAOimpl().getAllFamilienmitglieder(haushalt
        // .getKundennummer());
        Integer anzahlFamilienmitglieder = familienmitliederAkt.size();
        Integer anzahlKinder = 0;
        Integer anzahlErwachsene = 0;

        for (int i = 0; i < anzahlFamilienmitglieder; i++)
        {
            //Ab wann sind Familienmitglieder "Erwachsen"?
            if ((familienmitliederAkt.get(i).getGeburtsdatum().toLocalDate()
                    .isBefore(LocalDate.now().minusYears(18))))
            {
                anzahlErwachsene++;
            } else
            {
                anzahlKinder++;
            }
        }

        Familienmitglied haushaltsvorstand = null;

        haushaltsvorstand = haushalt.getHaushaltsvorstand(familienmitliederAkt);

        ObservableList<Haushaltsinformationen> informationenHaushaltOL = FXCollections
                .observableArrayList(haushalt.getHaushaltsinformationen(familienmitliederAkt));
        listWeitereInformationen.setItems(informationenHaushaltOL);

        Boolean einkaufsberechtigt = haushalt.getEinkaufsberechtigt();

        if (einkaufsberechtigt)
        {
            haushaltHintergrund.setStyle("-fx-background-color: greenyellow");
        } else
        {
            haushaltHintergrund.setStyle("-fx-background-color: red");
        }

        if (familienmitglied.dseSubmitted())
        {
            ckbxDSE.setSelected(true);
        } else
        {
            ckbxDSE.setSelected(false);
        }

        if (familienmitglied.getNationString().equals("Angabe verweigert"))
        {
            ckbxNation.setSelected(false);
        } else
        {
            ckbxNation.setSelected(true);
        }

        if (haushaltsvorstand != null)
        {
            txtHaushaltsvorstand.setText(haushaltsvorstand.getName());
        } else
        {
            txtHaushaltsvorstand.setText("Bitte wählen Sie einen Vorstand");
        }

        txtAnzahlFamilienmitglieder.setText(String.valueOf(anzahlErwachsene) + " Erwachsene(r), "
        +
                String.valueOf(anzahlKinder) + " Kinder");
        txtVerteilstelle.setText(haushalt.getVerteilstelle().getBezeichnung());
        txtKundennummer.setText(String.valueOf(haushalt.getKundennummer()));
        if (haushalt.getAusgabegruppe() != null)
        {
            txtAusgabegruppe.setText(haushalt.getAusgabegruppe().getName());
            gruppenfarbe.setFill(haushalt.getAusgabegruppe().getAnzeigeFarbe());
        }
        txtBemerkungen.setText(haushalt.getBemerkungen());

    }


    /**
     *.
     */
    @FXML public void aendereLabelbezeichnung()
    {
        Haushaltsinformationen haushaltsinformationen =
                listWeitereInformationen.getSelectionModel().getSelectedItem();

        if (haushaltsinformationen != null)
        {
            if (haushaltsinformationen.getTyp() == Informationstypen.Archiv
                ||
                    haushaltsinformationen.getTyp() == Informationstypen.Belieferung
                    ||
                    haushaltsinformationen.getTyp() == Informationstypen.Gesperrt)
            {
                btnWeitereInformationen.setText("Einstellungen ändern");
            } else if (haushaltsinformationen.getTyp() == Informationstypen.Bemerkung
                ||
                    haushaltsinformationen.getTyp() == Informationstypen.Berechtigung
                    ||
                    haushaltsinformationen.getTyp() == Informationstypen.Gebuehrenbefreiung)
            {
                btnWeitereInformationen.setText("Personendaten ändern");
            } else if (haushaltsinformationen.getTyp() == Informationstypen.BescheideGueltig
                ||
                    haushaltsinformationen.getTyp() == Informationstypen.BescheideUngueltig)
            {
                btnWeitereInformationen.setText("Bescheide bearbeiten");
            } else if (haushaltsinformationen.getTyp() == Informationstypen.Kontensaldo)
            {
                btnWeitereInformationen.setText("Einkäufe anzeigen");
            } else if (haushaltsinformationen.getTyp() == Informationstypen.Vollmacht)
            {
                btnWeitereInformationen.setText("Vollmachten bearbeiten");
            }
        }
    }


    /**
     *.
     */
    @FXML public void btnWeitereInformationen()
    {
        Boolean einkaufsberechtigt;
        ObservableList<Haushaltsinformationen> informationenHaushaltOL =
                FXCollections.observableArrayList();

        switch (btnWeitereInformationen.getText())
        {
            case "Einstellungen ändern":
                haushalt = MainController.getInstance().oeffneKundenstammBearbeiten(haushalt, currentFontSize);
                familienmitgliederOL.clear();
                familienmitgliederOL.addAll(new FamilienmitgliedDAOimpl()
                        .getAllFamilienmitglieder(suche, filter, genaueSuche));
                fuelleKundendaten();
                break;
            case "Personendaten ändern":
                MainController.getInstance().oeffnePersonAendern(familienmitglied, haushalt, currentFontSize);
                familienmitgliederOL.clear();
                familienmitgliederOL.addAll(new FamilienmitgliedDAOimpl()
                        .getAllFamilienmitglieder(suche, filter, genaueSuche));
                fuelleKundendaten();

                familienmitliederAkt = new FamilienmitgliedDAOimpl()
                        .getAllFamilienmitglieder(haushalt.getKundennummer());
                informationenHaushaltOL.clear();
                informationenHaushaltOL
                        .addAll(haushalt.getHaushaltsinformationen(familienmitliederAkt));
                listWeitereInformationen.setItems(informationenHaushaltOL);
                einkaufsberechtigt = haushalt.getEinkaufsberechtigt();

                if (einkaufsberechtigt)
                {
                    haushaltHintergrund.setStyle("-fx-background-color: greenyellow");
                } else
                {
                    haushaltHintergrund.setStyle("-fx-background-color: red");
                }
                break;
            case "Bescheide bearbeiten":
                MainController.getInstance().oeffneBescheideBearbeiten(haushalt, currentFontSize);

                familienmitliederAkt = new FamilienmitgliedDAOimpl()
                        .getAllFamilienmitglieder(haushalt.getKundennummer());
                informationenHaushaltOL.clear();
                informationenHaushaltOL
                        .addAll(haushalt.getHaushaltsinformationen(familienmitliederAkt));
                einkaufsberechtigt = haushalt.getEinkaufsberechtigt();

                if (einkaufsberechtigt)
                {
                    haushaltHintergrund.setStyle("-fx-background-color: greenyellow");
                } else
                {
                    haushaltHintergrund.setStyle("-fx-background-color: red");
                }
                break;
            case "Einkäufe anzeigen":
                MainController.getInstance().oeffneBuchungenBearbeiten(haushalt, currentFontSize);
                break;
            case "Vollmachten bearbeiten":
                MainController.getInstance().oeffneVollmachtenAnzeigen(haushalt, currentFontSize);

                familienmitliederAkt = new FamilienmitgliedDAOimpl()
                        .getAllFamilienmitglieder(haushalt.getKundennummer());
                informationenHaushaltOL.clear();
                informationenHaushaltOL
                        .addAll(haushalt.getHaushaltsinformationen(familienmitliederAkt));
                break;
            case "Bemerkung bearbeiten":
                haushalt = MainController.getInstance().oeffneBemerkungenBearbeiten(haushalt, currentFontSize);
                txtBemerkungen.setText(haushalt.getBemerkungen());
                familienmitgliederOL.clear();
                familienmitgliederOL.addAll(new FamilienmitgliedDAOimpl()
                        .getAllFamilienmitglieder(suche, filter, genaueSuche));
                break;
          default:
            break;
        }
    }


    /**
     *
     * @Author Richard Kromm
     * @Date 13.09.2018
     */

    public void setUserRights(User user)
    {
        this.user = user;

        if (user.getUserRights().equals(DISTRIBUTION_POINT_LEADER_USER_RIGHT)
             || user.getUserRights().equals(CASH_PERSONAL_USER_RIGHT))
        {
            menuItemDbSettings.setDisable(true);
            menuItemAdminTool.setDisable(true);


            if (user.getUserRights().equals(CASH_PERSONAL_USER_RIGHT))
            {
                menuItemStatistikTool.setDisable(true);
                menuItemAddHousehold.setDisable(true);
                btnHaushaltHinzufuegen.setDisable(true);
                menuItemManageHousehold.setDisable(true);
                btnHaushaltVerwalten.setDisable(true);
                menuItemCommentEdit.setDisable(true);
                btnBermerkungAendern.setDisable(true);
                menuItemDecisionEdit.setDisable(true);
                btnWeitereInformationen.setDisable(true);
                menuItemPowerOfAttorneyEdit.setDisable(true);
            }
        }
    }

    /**
     * This function refresh householdArraylist, householdObservablelist and householdtableview.
     *
     * @Author Richard Kromm
     * @Date 19.09.2018
     */
    public void refreshCustomerTableView()
    {
        familienmitglieder = new FamilienmitgliedDAOimpl().getAllFamilienmitglieder();
        familienmitgliederOL = FXCollections.observableArrayList(familienmitglieder);
        kundensucheOutput.setItems(familienmitgliederOL);
    }

    @FXML
    public void changeFontSize(javafx.event.ActionEvent e)
    {
        // Referenzvergleich: welches Menü wurde geklickt?
        MenuItem src = (MenuItem) e.getSource();
        boolean bigger = (src == menuItemFontBigger);

        // neue Größe clampen
        currentFontSize = Math.max(MIN_FONT_SIZE,
                           Math.min(MAX_FONT_SIZE,
                                    currentFontSize + (bigger ? ZOOM_FACTOR : -ZOOM_FACTOR)));

        // Wirkung erzeugen: Root-Stil setzen -> vererbt sich
        root.setStyle(String.format("-fx-font-size: %.1fpx;", currentFontSize));

        // Menüeinträge passend (de)aktivieren
        menuItemFontBigger.setDisable(currentFontSize >= MAX_FONT_SIZE);
        menuItemFontSmall.setDisable(currentFontSize <= MIN_FONT_SIZE);
    }

    /**
     * Is used to delete all table layouts for the given user-id.
     * @throws SQLException Throws SQL-Exception if an error occurs.
     */
    @FXML
    public void deleteAllTableLayouts() throws SQLException
    {
        User user = MainController.getInstance().getUser();

        if (user == null)
        {
            return;
        }

        ButtonType buttonOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonCancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Bestätigung erforderlich");
        confirmDialog.setHeaderText("Alle Tabellen-Layouts zurücksetzen?");
        confirmDialog.setContentText("Möchten Sie wirklich alle gespeicherten\r\nTabellen-Einstellungen löschen?");
        confirmDialog.getButtonTypes().setAll(buttonOk, buttonCancel);

        Platform.runLater(() ->
        {
            Button cancelBtn = (Button) confirmDialog.getDialogPane().lookupButton(buttonCancel);
            if (cancelBtn != null)
            {
                cancelBtn.setDefaultButton(true);
            }

            Button okBtn = (Button) confirmDialog.getDialogPane().lookupButton(buttonOk);
            if (okBtn != null)
            {
                okBtn.setDefaultButton(false);
            }
        });

        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == buttonOk)
        {
            TablePreferenceServiceImpl.getInstance().deleteAllTableLayouts(user.getUserId());

            kundensucheOutput.getColumns().clear();

            initialize();

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Erfolg");
            info.setHeaderText(null);
            info.setContentText("Alle Tabellen-Einstellungen wurden gelöscht.\r\nBitte einmal Anwendung neustarten.");
            info.showAndWait();
        }
    }

    public User getUser()
    {
        return user;
    }
}
