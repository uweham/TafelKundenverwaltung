package kundenverwaltung.controller;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import javafx.animation.PauseTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.AusgabegruppeDAOimpl;
import kundenverwaltung.dao.BerechtigungDAOimpl;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.dao.HaushaltDAOimpl;
import kundenverwaltung.dao.NationDAOimpl;
import kundenverwaltung.dao.PLZDaoImpl;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.logger.event.GlobalEventLogger;
import kundenverwaltung.model.Anrede;
import kundenverwaltung.model.AusgabeTagZeit;
import kundenverwaltung.model.Ausgabegruppe;
import kundenverwaltung.model.Berechtigung;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Gender;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Nation;
import kundenverwaltung.model.PLZ;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;
import kundenverwaltung.toolsandworkarounds.CheckOfAge;
import kundenverwaltung.toolsandworkarounds.DynamicNationDropDownMenu;
import kundenverwaltung.toolsandworkarounds.FirstLetterToUppercase;

public class HaushaltAnlegenController
{
    private static final int DEFAULT_SELECTED_NATION_INDEX = 1;

    private ChangeDateFormat changeDateFormat = new ChangeDateFormat();
    private DynamicNationDropDownMenu dynamicNationDropDownMenu = new DynamicNationDropDownMenu();
    private ChangeFontSize changeFontSize = new ChangeFontSize();
    private CheckOfAge checkOfAge = new CheckOfAge();

    private static final int GENDER_ID_SONSTIGES = 73;
    private static final int GENDER_ID_KEINE_ANGABE = 74;

    @FXML
    private Label labelHouseholdDirectorHeader;
    @FXML
    private Label labelPersonDataHeader;
    @FXML
    private Label labelHeader;
    @FXML
    private Label labelFirstName;
    @FXML
    private Label labelSurname;
    @FXML
    private Label labelAnrede;
    @FXML
    private Label labelBirthday;
    @FXML
    private Label labelGender;
    @FXML
    private Label labelAuthorization;
    @FXML
    private Label labelNation;
    @FXML
    private Label labelStreet;
    @FXML
    private Label labelHousenumber;
    @FXML
    private Label labelPostcode;
    @FXML
    private Label labelLocation;
    @FXML
    private Label labelLocationPart;
    @FXML
    private Label labelPhoneNumber;
    @FXML
    private Label labelMobileNumber;
    @FXML
    private Label labelCustomerSince;
    @FXML
    private Label labelDistributionPoint;
    @FXML
    private Label labelOutputGroup;
    @FXML
    private Label labelGroupColor;
    @FXML
    private Label labelOutputTimes;
    @FXML
    private TextField txtVorname;
    @FXML
    private TextField txtNachname;
    @FXML
    private TextField txtStrasse;
    @FXML
    private TextField txtHausnummer;
    @FXML
    private TextField txtPostleitzahl;
    @FXML
    private TextField txtWohnort;
    @FXML
    private TextField txtTelefon;
    @FXML
    private TextField txtMobilTelefon;
    @FXML
    private ComboBox<String> cbAnrede;
    @FXML
    private ComboBox<String> cbGender;
    @FXML
    private ComboBox<Verteilstelle> cbVerteilstelle;
    @FXML
    private ComboBox<Ausgabegruppe> cbAusgabegruppe;
    @FXML
    private ComboBox<Nation> cbNation;
    @FXML
    private ComboBox<Berechtigung> cbBesBerechtigung;
    @FXML
    private DatePicker dateGeb;
    @FXML
    private DatePicker dateKundeSeit;
    @FXML
    private CheckBox cbxLieferung;
    @FXML
    private CheckBox cbxDatenschutz;
    @FXML
    private TextArea txtAusgabetag;
    @FXML
    private Button buttonAddHousehold;
    @FXML
    private Button buttonCancel;
    @FXML
    private Tab tabdse;
    @FXML
    private Tab tabausgabe;
    @FXML
    private Tab tabkundendaten;
    @FXML
    private Rectangle gruppenfarbe;
    @FXML
    private Label labelDatenschutz;
    @FXML
    private ComboBox<PLZ> cbWohnort; // Where is this combobox??
    @FXML
    private CheckBox cbxOertlicheKdnr;
    @FXML
    private TextField txtOertlicheKdnr;
    @FXML
    private Button btnAendern;

    private ArrayList<PLZ> plzListe = new PLZDaoImpl().readAll();
    @SuppressWarnings("unused")
    private Boolean plzNeu = true;
    private PLZ plz;
    private String wohnortAlt = "";

    private Familienmitglied neuAngelegterVorstand;

    public Familienmitglied getNeuAngelegterVorstand() {
        return neuAngelegterVorstand;
    }
    // --- Autocomplete-Struktur ---
    private ContextMenu plzSuggestMenu;
    private TableView<PLZ> plzTable;
    private FilteredList<PLZ> filteredPlz;
    private PauseTransition plzDebounce = new PauseTransition(Duration.millis(220));
    private PLZ selectedPlz;
    /**
     *.
     */
    public void initialize()
    {
        ArrayList<Verteilstelle> verteilstellen = new VerteilstelleDAOimpl().readAll();
        ObservableList<Verteilstelle> vliste = FXCollections.observableArrayList(verteilstellen);

        ArrayList<Ausgabegruppe> ausgabegruppen = new AusgabegruppeDAOimpl().readAll();
        ObservableList<Ausgabegruppe> aliste = FXCollections.observableArrayList(ausgabegruppen);

        ArrayList<Nation> nationen = new NationDAOimpl().getAllEnabledNationen();
        ObservableList<Nation> nliste = FXCollections.observableArrayList(nationen);
        cbNation.setItems(nliste);
        cbNation.setOnShowing(event -> dynamicNationDropDownMenu.resetComboboxNation(cbNation, nliste));
        cbNation.setOnKeyReleased(event -> dynamicNationDropDownMenu.jumpToUserInput(cbNation, nationen, event));
        dynamicNationDropDownMenu.onFocused(cbNation, nationen);

        ArrayList<Berechtigung> berechtigungen = new BerechtigungDAOimpl().getAllBerechtigungen();
        ObservableList<Berechtigung> bliste = FXCollections.observableArrayList(berechtigungen);
        cbBesBerechtigung.setItems(bliste);

        cbVerteilstelle.setItems(vliste);
        cbAusgabegruppe.setItems(aliste);

        dateGeb.setConverter(changeDateFormat.convertDatePickerFormat());
        changeDateFormat.checkUserInputDate(dateGeb);
        dateKundeSeit.setConverter(changeDateFormat.convertDatePickerFormat());

        cbAnrede.getSelectionModel().select(0);
        cbGender.getSelectionModel().select(3);
        cbNation.getSelectionModel().select(DEFAULT_SELECTED_NATION_INDEX);
        cbBesBerechtigung.getSelectionModel().select(3);
        cbAusgabegruppe.getSelectionModel().selectFirst();
        setzeAusgabegruppe();
        cbVerteilstelle.getSelectionModel().selectFirst();
        dateKundeSeit.setValue(LocalDate.now());

        cbGender.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                evaluateShowingAnrede();
            }
        });

        evaluateShowingAnrede();

        String text = " ";

        try
        {
            InputStream is = getClass().getResourceAsStream("/Datenschutzerklaerung.txt");
            if (is != null)
            {
                Scanner read = new Scanner(is, "UTF8");
                read.useDelimiter("\\A");

                while (read.hasNext())
                {
                    text = text.concat(read.next());
                }
                read.close();
            } else
            {
                text = "Datenschutzerklärung nicht gefunden.";
            }

            labelDatenschutz.setText(text);
        }
        catch (Exception e)
        { e.printStackTrace();
        }

        // Autocomplete aktivieren
        setupPlzAutocomplete();
    }

 // --- Autocomplete-Setup ---
    @SuppressWarnings("unchecked")
    private void setupPlzAutocomplete()
    {
        if (plzListe == null) plzListe = new kundenverwaltung.dao.PLZDaoImpl().readAll();

        // 1. Definiere den roten "Fehler-Rahmen" als Effekt (Trick, um setStyle zu umgehen)
        javafx.scene.effect.InnerShadow errorShadow = new javafx.scene.effect.InnerShadow(javafx.scene.effect.BlurType.THREE_PASS_BOX, javafx.scene.paint.Color.RED, 10, 0, 0, 0);

        // 2. Initial prüfen: Ist das Feld leer/falsch? Dann roten Rahmen setzen.
        if (!kundenverwaltung.toolsandworkarounds.CheckUserInput.checkPLZLength(txtPostleitzahl.getText())) {
            txtPostleitzahl.setEffect(errorShadow);
        }

        // Nur Ziffern, max. 5 (Dein alter Code)
        txtPostleitzahl.setTextFormatter(new TextFormatter<>(change ->
        {
            String t = change.getControlNewText();
            if (t.length() > 5) return null;
            if (!t.matches("\\d*")) return null;
            return change;
        }));

        // --- Dein Tabellen-Setup (unverändert) ---
        plzTable = new TableView<>();
        plzTable.setPrefWidth(260);
        plzTable.setPrefHeight(200);
        plzTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<PLZ, String> colPlz = new TableColumn<>("PLZ");
        colPlz.setCellValueFactory(new PropertyValueFactory<>("plz"));
        colPlz.setMaxWidth(90);
        colPlz.setMinWidth(80);

        TableColumn<PLZ, String> colOrt = new TableColumn<>("Ort");
        colOrt.setCellValueFactory(new PropertyValueFactory<>("ort"));

        plzTable.getColumns().addAll(colPlz, colOrt);

        ObservableList<PLZ> base = FXCollections.observableArrayList(plzListe);
        filteredPlz = new FilteredList<>(base, p -> true);
        SortedList<PLZ> sorted = new SortedList<>(filteredPlz);
        plzTable.setItems(sorted);

        plzSuggestMenu = new ContextMenu();
        plzSuggestMenu.getItems().add(new CustomMenuItem(plzTable, false));

        plzTable.setOnMouseClicked(ev ->
        { if (ev.getClickCount() == 2) applySelectedPlz(); });
        plzTable.setOnKeyPressed(ev ->
        {
            switch (ev.getCode())
            {
                case ENTER -> applySelectedPlz();
                case ESCAPE -> plzSuggestMenu.hide();
                default -> {}
            }
        });

        plzDebounce.setOnFinished(e -> filterAndShowPlz());

        // --- NEU: Listener für visuellen Check (Nutzt .setEffect statt .setStyle) ---
        txtPostleitzahl.textProperty().addListener((observable, oldValue, newValue) -> {
            
            // Logik für Autocomplete (Dein alter Code)
            selectedPlz = null;
            if (newValue != null && newValue.length() >= 2) {
                plzDebounce.playFromStart();
            } else {
                plzSuggestMenu.hide();
            }

            // Logik für roten Rahmen (Neuer Code)
            if (!kundenverwaltung.toolsandworkarounds.CheckUserInput.checkPLZLength(newValue)) {
                txtPostleitzahl.setEffect(errorShadow); // Rot
            } else {
                txtPostleitzahl.setEffect(null); // Normal
            }
        });
        // -----------------------------------------------------------------

        txtPostleitzahl.addEventFilter(KeyEvent.KEY_PRESSED, ev ->
        {
            if (plzSuggestMenu.isShowing()) {
                switch (ev.getCode()) {
                    case DOWN -> {
                        plzTable.requestFocus();
                        plzTable.getSelectionModel().selectFirst();
                        ev.consume();
                    }
                    case ESCAPE -> plzSuggestMenu.hide();
                    default -> {}
                }
            }
        });

        txtPostleitzahl.focusedProperty().addListener((o, was, isNow) ->
        {
            if (!isNow) plzSuggestMenu.hide();
        });
    }

    private void filterAndShowPlz()
    {
        String q = txtPostleitzahl.getText();
        if (q == null || q.length() < 2)
        { plzSuggestMenu.hide(); return;
        }

        filteredPlz.setPredicate(p -> p.getPlz() != null && p.getPlz().startsWith(q));

        if (plzTable.getItems().isEmpty())
        {
          plzSuggestMenu.hide(); return;
        }

        if (!plzSuggestMenu.isShowing())
        {
            plzSuggestMenu.show(txtPostleitzahl, Side.BOTTOM, 0, 0);
        }
    }


    private boolean evaluateShowingAnrede()
    {
        Gender gender = getGenderByComboBoxIndex(cbGender.getSelectionModel().getSelectedIndex());

        if (gender != null)
        {
            boolean showAnrede = (gender.getGenderId() != GENDER_ID_SONSTIGES
                    && gender.getGenderId() != GENDER_ID_KEINE_ANGABE);

            cbAnrede.setVisible(showAnrede);
            labelAnrede.setVisible(showAnrede);
            return showAnrede;
        }

        return true;
    }


    private Gender getGenderByComboBoxIndex(int selectedIndex)
    {
        return switch (cbGender.getSelectionModel().getSelectedIndex())
        {
            case 0 -> new Gender(71);
            case 1 -> new Gender(72);
            case 2 -> new Gender(GENDER_ID_SONSTIGES);
            case 3 -> new Gender(GENDER_ID_KEINE_ANGABE);
            default -> null;
        };
    }


    // --- Auswahl übernehmen ---
    private void applySelectedPlz()
    {
        PLZ plzObj = plzTable.getSelectionModel().getSelectedItem();
        if (plzObj == null) return;

        selectedPlz = plzObj;
        this.plz = plzObj; // wichtig: auch Feld plz setzen

        txtPostleitzahl.setText(plzObj.getPlz());
        txtWohnort.setText(plzObj.getOrt());

        cbWohnort.setVisible(false);
        cbWohnort.setDisable(true);
        txtWohnort.setVisible(true);
        txtWohnort.setDisable(false);

        plzSuggestMenu.hide();
    }

    /**
     *.
     */
    @FXML
    public void plzGesetzt()
    {
        txtWohnort.clear();
        plz = null;
        setWohnortAlt("");

        String plzString = txtPostleitzahl.getText().trim();

        ObservableList<PLZ> orteAktuell = FXCollections.observableArrayList();
        orteAktuell.addAll(plzListe.stream().filter(r -> r.getPlz().equals(plzString)).collect(Collectors.toList()));

        if (orteAktuell != null && !orteAktuell.isEmpty())
        {
            cbWohnort.setItems(orteAktuell);
            cbWohnort.getSelectionModel().selectFirst();
            txtWohnort.setVisible(false);
            txtWohnort.setDisable(true);
            cbWohnort.setVisible(true);
            cbWohnort.setPrefWidth(200);
            cbWohnort.setDisable(false);
            btnAendern.setVisible(true);
            plzNeu = false;
        } else
        {
            txtWohnort.setDisable(false);
            txtWohnort.setText("");
            txtWohnort.setVisible(true);
            txtWohnort.setPrefWidth(244);
            btnAendern.setVisible(false);
            plzNeu = true;

            cbWohnort.setVisible(false);
            cbWohnort.setDisable(true);
        }
    }
    /**
     *.
     */
    @FXML
    public void setzeAusgabegruppe()
    {
        Ausgabegruppe ausgabegruppe = cbAusgabegruppe.getValue();
        if (ausgabegruppe != null)
        {
            gruppenfarbe.setFill(ausgabegruppe.getAnzeigeFarbe());
            StringBuilder ausgabezeiten = new StringBuilder();

            for (AusgabeTagZeit element : ausgabegruppe.getAusgabeTagZeiten())
            {
                ausgabezeiten.append(element.getAusgabetag().getAbkuerzung()).append(" , ").append(element.getStartzeit()).append("-").append(element.getEndzeit()).append("\n");
            }

            txtAusgabetag.setText(ausgabezeiten.toString());
        }
    }
    /**
     *.
     */
    @FXML
    public void pressButtonAendern()
    {
        createStage(txtPostleitzahl.getText());

        // PLZ-Liste frisch laden
        plzListe.clear();
        plzListe = new PLZDaoImpl().readAll();

        // frisch angelegte/angepasste PLZ übernehmen
        String code = txtPostleitzahl.getText().trim();
        selectedPlz = plzListe.stream()
                .filter(p -> p.getPlz().equals(code))
                .findFirst()
                .orElse(null);
        if (selectedPlz != null)
        {
            this.plz = selectedPlz;
            txtWohnort.setText(selectedPlz.getOrt());
            cbWohnort.setVisible(false);
            cbWohnort.setDisable(true);
            txtWohnort.setVisible(true);
            txtWohnort.setDisable(false);
        }

        plzGesetzt();
    }

    private void createStage(String plz)
    {
        Stage plzStage = new Stage();
        VBox box = new VBox();
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);
        Label label = new Label("Bitte geben Sie hier den Namen des Ortes zu der PLZ " + txtPostleitzahl.getText() + " ein.");
        label.setPadding(new Insets(0, 10, 20, 10));
        TextField plzTextfield = new TextField();

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10));

        Button btnNeuerOrtOk = new Button();
        btnNeuerOrtOk.setAlignment(Pos.TOP_LEFT);
        Button btnNeuerOrtAbrrechen = new Button();

        btnNeuerOrtOk.setText("Ort übernehmen");
        btnNeuerOrtAbrrechen.setText("Abbrechen");

        btnNeuerOrtOk.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                PLZ neuePLZ = new PLZ(plz, ersteBuchstabenGross(plzTextfield.getText()), false);
                Boolean checkInsert = new PLZDaoImpl().create(neuePLZ);

                if (checkInsert)
                {
                    System.out.println("erfolg");
                } else
                {
                    System.out.println("fehlschlag");
                }
                plzStage.close();
            }
        });

        btnNeuerOrtAbrrechen.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                plzStage.close();
            }
        });

        box.getChildren().add(label);
        box.getChildren().add(plzTextfield);
        box.getChildren().add(hbox);
        hbox.getChildren().add(btnNeuerOrtOk);
        hbox.getChildren().add(btnNeuerOrtAbrrechen);
        HBox.setMargin(btnNeuerOrtOk, new Insets(0, 10, 0, 0));
        btnNeuerOrtAbrrechen.setAlignment(Pos.TOP_RIGHT);

        Scene plzScene = new Scene(box, 400, 150);
        GlobalEventLogger.attachTo("HaushaltAnlegenController_PLZ_Eingabe", plzScene);
        plzStage.setScene(plzScene);
        plzStage.showAndWait();
    }
    /**
     *.
     */
    @FXML
    public void btnHaushaltHinzufuegen()
    {
        Haushalt haushalt = createHaushalt();

        if (haushalt != null)
        {
            Familienmitglied familienmitglied = createHaushaltsvorstand(haushalt);

            if (familienmitglied != null)
            {
                Integer haushaltID = new HaushaltDAOimpl().createHaushaltAndGetId(haushalt);

                if (haushaltID != -1)
                {
                    haushalt.setKundennummer(haushaltID);
                    Boolean checkInsert = new FamilienmitgliedDAOimpl().create(familienmitglied);

                    if (checkInsert)
                    {
                        Benachrichtigung.infoBenachrichtigung("Anlegen erfolgreich.", "Der Haushalt wurde erfolgreich hinzugefügt.");
                       
                        ArrayList<Familienmitglied> fms = new FamilienmitgliedDAOimpl().getAllFamilienmitglieder(haushaltID);
                        if(fms != null && !fms.isEmpty()) {
                            neuAngelegterVorstand = fms.get(0);
                        }
                        
                        Stage stage = (Stage) btnAendern.getScene().getWindow();
                        stage.close();
                    } else
                    {
                        Benachrichtigung.warnungBenachrichtigung("Haushalt konnte nicht angelegt werden.", "Bitte überprüfen Sie Ihre Verbindung zur Datenbank und probieren Sie es erneut.");
                    }
                } else
                {
                    Benachrichtigung.warnungBenachrichtigung("Haushalt konnte nicht angelegt werden.", "Bitte überprüfen Sie Ihre Verbindung zur Datenbank und probieren Sie es erneut.");
                }
            }
        }
    }
    /**
     *.
     */
    @FXML
    public void btnAbbrechen()
    {
        Stage stage = (Stage) labelDatenschutz.getScene().getWindow();
        stage.close();
    }
    /**
     *.
     */
    public Haushalt createHaushalt()
    {
        // Priorität: Auswahl aus Typeahead
        if (selectedPlz != null)
        {
            plz = selectedPlz;
        } else if (cbWohnort.isVisible())
        {
            plz = cbWohnort.getSelectionModel().getSelectedItem();
        } else
        {
            plz = null;
        }

        if (pruefeFelderHaushalt())
        {
          if (!kundenverwaltung.toolsandworkarounds.CheckUserInput.checkPLZLength(txtPostleitzahl.getText())) 
          {
              Benachrichtigung.warnungBenachrichtigung(
                  "Ungültige Postleitzahl", 
                  "Die Postleitzahl muss genau 5 Stellen haben."
              );
              return null; // Abbruch
          }
            Boolean plzEinfuegen = plzEinfuegen();

            if (plzEinfuegen)
            {
                String strasse =  txtStrasse.getText().trim();  // inhibit uppercase first letter
                String hausnummmer = txtHausnummer.getText().trim();

                String bemerkung = "";
                LocalDate kundeSeit = dateKundeSeit.getValue();
                Verteilstelle verteilstelle = cbVerteilstelle.getValue();
                @SuppressWarnings("unused") boolean istArchiviert = false;
                @SuppressWarnings("unused") boolean istGesperrt = false;
                Boolean wirdBeliefert = cbxLieferung.isSelected();
                Boolean datenschutz = cbxDatenschutz.isSelected();
                Ausgabegruppe ausgabegruppe = cbAusgabegruppe.getValue();

                String telefon = txtTelefon.getText().trim();
                String mobil = txtMobilTelefon.getText().trim();

                if (datenschutz)
                {
                    Haushalt haushalt = new Haushalt(strasse, hausnummmer, plz, telefon, mobil, bemerkung, kundeSeit, 0, verteilstelle, ausgabegruppe, wirdBeliefert, datenschutz);
                    return haushalt;
                } else
                {
                    Benachrichtigung.infoBenachrichtigung("Achtung!", "Der Kunde muss die Datenschutzvereinbarung akzeptiert haben.");
                    return null;
                }
            } else
            {
                Benachrichtigung.warnungBenachrichtigung("Beim Anlegen der neuen PLZ ist ein Fehler aufgetreten.", "Bitte überprüfen Sie Ihre Verbindung zur Datenbank und probieren Sie es erneut.");
                return null;
            }
        } else
        {
            Benachrichtigung.infoBenachrichtigung("Achtung!", "Bitte füllen Sie alle Pflichtfelder aus.");
        }
        return null;
    }
    /**
     *.
     */
    public Familienmitglied createHaushaltsvorstand(Haushalt haushalt)
    {
        if (pruefeFelderVorstand())
        {
            String vname = txtVorname.getText().trim(); // inhibit uppercase first letter
            String nname = txtNachname.getText().trim(); // inhibit uppercase first letter
            Anrede anrede = null;
            Gender gender = null;

            anrede = switch (cbAnrede.getSelectionModel().getSelectedIndex())
                {
              case 0 -> new Anrede(31);
              case 1 -> new Anrede(32);
              case 2 -> new Anrede(33);
              default -> null;
          };

            gender = getGenderByComboBoxIndex(cbGender.getSelectionModel().getSelectedIndex());

            boolean useAnrede = evaluateShowingAnrede();

            if (!useAnrede)
            {
                anrede = null;
            }

            LocalDate gdatum = dateGeb.getValue();
            if (!checkOfAge.isAdult(gdatum))
            {
                Benachrichtigung.infoBenachrichtigung("Achtung", "Der Haushaltsvorstand muss volljährig sein.");
                return null;
            }

            ArrayList<Nation> nationen = new NationDAOimpl().getAllEnabledNationen();
            Nation nation = dynamicNationDropDownMenu.getAndCheckNationValue(cbNation, nationen);
            Berechtigung berechtigung;
            if (cbBesBerechtigung.getSelectionModel().getSelectedItem() == null)
            {
                berechtigung = new Berechtigung(46, "keine");
            } else
            {
                berechtigung = cbBesBerechtigung.getValue();
            }
            Boolean gebuehren = false;
            Boolean ausweis = true;
            Boolean dse = cbxDatenschutz.isSelected();
            Boolean vorstand = true;
            Boolean einkaufsberechtigt = true;
            String bemerkung = "";

            return new Familienmitglied(haushalt, anrede, gender, vname, nname, gdatum, bemerkung, vorstand, einkaufsberechtigt, gebuehren, nation, berechtigung, ausweis, dse, LocalDateTime.now(), LocalDateTime.now());
        } else
        {
            Benachrichtigung.infoBenachrichtigung("Achtung!", "Bitte füllen Sie alle Pflichtfelder aus.");
            return null;
        }
    }
    /**
    *
    */
    public Boolean plzEinfuegen()
    {
        // Bereits gesetzt (Typeahead/Combo)? -> nichts tun
        if (plz != null) return true;

        String code = txtPostleitzahl.getText().trim();
        String ortName = ersteBuchstabenGross(txtWohnort.getText().trim());

        // existiert (plz, ort) schon im Cache?
        PLZ existing = plzListe.stream()
                .filter(p -> code.equals(p.getPlz())
                    &&
                             ortName.equalsIgnoreCase(p.getOrt()))
                .findFirst()
                .orElse(null);

        if (existing != null)
        {
            plz = existing;
            selectedPlz = existing;
            return true;
        }

        // neu anlegen
        plz = new PLZ(code, ortName, false);
        int plzid = new PLZDaoImpl().createAndGetId(plz);

        if (plzid != -1)
        {
            plz.setPlzId(plzid);
            // Cache aktualisieren, damit Folgeaktionen denselben Eintrag sehen
            plzListe.add(plz);
            selectedPlz = plz;
            return true;
        } else
        {
            return false;
        }
    }
    /**
     *.
     */
    public Boolean pruefeFelderHaushalt()
    {
        return !((txtWohnort.isVisible() && txtWohnort.getText().trim().isEmpty())
                || txtHausnummer.getText().trim().isEmpty()
                || txtPostleitzahl.getText().trim().isEmpty()
                || (dateKundeSeit.getValue() == null || dateKundeSeit.getValue().toString().isEmpty())
                || txtStrasse.getText().trim().isEmpty());
    }
    /**
     *.
     */
    public Boolean pruefeFelderVorstand()
    {
        return !(txtVorname.getText().trim().isEmpty()
                || (dateGeb.getValue() == null || dateGeb.getValue().toString().isEmpty())
                || txtNachname.getText().trim().isEmpty());
    }
    /**
     *.
     */
    /**
     * Wandelt den ersten Buchstaben jedes Wortes in Großbuchstaben um.
     * Berücksichtigt Leerzeichen, Bindestriche und Punkte als Trenner.
     */
    public String ersteBuchstabenGross(String string)
    {
        if (string == null || string.isEmpty()) {
            return "";
        }

        // 1. Alles klein machen
        char[] chars = string.toLowerCase().toCharArray();
        boolean capitalizeNext = true;

        for (int i = 0; i < chars.length; i++)
        {
            char c = chars[i];
            
            // Wenn es ein Buchstabe ist...
            if (Character.isLetter(c))
            {
                if (capitalizeNext)
                {
                    chars[i] = Character.toUpperCase(c);
                    capitalizeNext = false; // Nächster Buchstabe wieder klein, bis Trenner kommt
                }
            } 
            else
            {
                // Wenn das Zeichen ein Trenner ist (Leerzeichen, Bindestrich, Punkt)
                // dann muss das darauf folgende Zeichen groß geschrieben werden.
              if (c == ' ' || c == '-' || c == '.' || c == '_')
              {
                  capitalizeNext = true;
              }
            }
        }
        return new String(chars);
    }

    private FirstLetterToUppercase firstLetterToUppercase = new FirstLetterToUppercase();
    /**
     *.
     */
    public FirstLetterToUppercase getFirstLetterToUppercase()
    {
        return firstLetterToUppercase;
    }
    /**
     *.
     */
    public void setFirstLetterToUppercase(FirstLetterToUppercase firstLetterToUppercase)
    {
        this.firstLetterToUppercase = firstLetterToUppercase;
    }
    /**
     *.
     */
    @FXML protected void firstToUppercase()
    {
        //firstLetterToUppercase.firstLetterUppercase(txtVorname);
        //firstLetterToUppercase.firstLetterUppercase(txtNachname);
        //firstLetterToUppercase.firstLetterUppercase(txtWohnort);
    }
    /**
     *.
     */
    public void setFontSize(Double fontSize)
    {
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);
        DoubleProperty headerFontSize = new SimpleDoubleProperty(fontSize + ChangeFontSize.getDifferenceBetweenDefaultHeaderFontsize());

        ArrayList<Label> labelHeaderArrayList = new ArrayList<>(Arrays.asList(
            labelHouseholdDirectorHeader, labelPersonDataHeader
        ));
        changeFontSize.changeFontSizeFromLabelArrayList(labelHeaderArrayList, headerFontSize);

        ArrayList<Label> labelArrayList = new ArrayList<>(Arrays.asList(
            labelHeader, labelFirstName, labelSurname, labelAnrede, labelBirthday, labelGender,
            labelAuthorization, labelNation, labelStreet, labelHousenumber, labelPostcode, labelLocation,
            labelPhoneNumber, labelMobileNumber, labelCustomerSince, labelDistributionPoint,
            labelOutputGroup, labelGroupColor, labelOutputTimes
        ));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        @SuppressWarnings("rawtypes")
        ArrayList<ComboBox> comboBoxArrayList = new ArrayList<>(Arrays.asList(
            cbAnrede, cbGender, cbAusgabegruppe, cbBesBerechtigung, cbNation, cbVerteilstelle
        ));
        changeFontSize.changeFontSizeFromComboBoxArrayList(comboBoxArrayList, newFontSize);

        ArrayList<DatePicker> datePickerArrayList = new ArrayList<>(Arrays.asList(dateGeb, dateKundeSeit));
        changeFontSize.changeFontSizeFromDatePickerArrayList(datePickerArrayList, newFontSize);

        ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>(Arrays.asList(
            cbxDatenschutz, cbxLieferung
        ));
        changeFontSize.changeFontSizeFromCheckBoxArrayList(checkBoxArrayList, newFontSize);

        ArrayList<TextField> textFieldArrayList = new ArrayList<>(Arrays.asList(
            txtVorname, txtNachname, txtStrasse, txtHausnummer, txtPostleitzahl, txtWohnort, txtTelefon, txtMobilTelefon
        ));
        changeFontSize.changeFontSizeFromTextFieldArrayList(textFieldArrayList, newFontSize);

        ArrayList<TextArea> textAreaArrayList = new ArrayList<>(Arrays.asList(txtAusgabetag));
        changeFontSize.changeFontSizeFromTextAreaArrayList(textAreaArrayList, newFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(buttonAddHousehold, buttonCancel));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);

        ArrayList<Tab> tabArrayList = new ArrayList<>(Arrays.asList(tabdse, tabausgabe, tabkundendaten));
        changeFontSize.changeFontSizeFromTabArrayList(tabArrayList, newFontSize);
    }
    /**
     *.
     */
    public String getWohnortAlt()
    {
      return wohnortAlt;
    }
    /**
     *.
     */
    public void setWohnortAlt(String wohnortAlt)
    {
      this.wohnortAlt = wohnortAlt;
    }
}
