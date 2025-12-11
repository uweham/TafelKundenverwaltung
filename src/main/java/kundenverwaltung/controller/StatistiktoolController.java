package kundenverwaltung.controller;
import kundenverwaltung.controller.admintool.StatistiktoolSQLController;
import kundenverwaltung.controller.statistiktool.ArchivierteKundenStatistikController;
import kundenverwaltung.controller.statistiktool.HerkunftStatistikController;
import kundenverwaltung.dao.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kundenverwaltung.logger.event.GlobalEventLogger;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert.AlertType;
import kundenverwaltung.model.statistiktool.Statistiktool;
import kundenverwaltung.model.Verteilstelle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

/**
 * Controller-Klasse für das Statistik-Tool.
 * Diese Klasse steuert die Benutzeroberfläche und die Interaktionen für die Anzeige und Verwaltung der Altersstatistik.
 */

public class StatistiktoolController
{

    @FXML
    private ComboBox<String> verteilstelleComboBox;
    @FXML
    private Button openSQLQueryToolButton;
    @FXML
    private TextField startAgeTextField;
    @FXML
    private TextField endAgeTextField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField yearResultField;
    @FXML
    private VBox dynamicGroupContainer;
    @FXML
    private TextField gesamtsummeField;
    @FXML
    private Button addGroupButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button printButton;

    @FXML
    private MenuItem handleExit;
    @FXML
    private MenuItem handleHelp;
    @FXML
    private MenuItem handleSettings;
    @FXML
    private MenuItem handleViewStatistics;
    @FXML
    private ComboBox<String> sqlDropdown;

    @FXML
    private Button bescheidButton; // Hinzufügen des Bescheid-Buttons

    @SuppressWarnings("unused")
    private HaushaltDAO haushaltDAO;
    private StatistiktoolDAO statistikDAO;
    private VerteilstelleDAO verteilstelleDAO;
    private List<TextField> dynamicGroupStartFields = new ArrayList<>();
    private List<TextField> dynamicGroupEndFields = new ArrayList<>();
    private List<TextField> dynamicResultFields = new ArrayList<>();
    /**
     *
     */

    @FXML
    public void initialize()
    {
        verteilstelleComboBox.getItems().clear(); // ComboBox leeren

        // Initialisiere DAOs
        statistikDAO = new StatistiktoolDAOimpl();
        verteilstelleDAO = new VerteilstelleDAOimpl();

        // Lade Verteilstellen in die ComboBox
        List<Verteilstelle> verteilstelleList = verteilstelleDAO.readAll();
        if (verteilstelleList != null && !verteilstelleList.isEmpty())
        {
            for (Verteilstelle verteilstelle : verteilstelleList)
            {
                String bezeichnung = verteilstelle.getBezeichnung();
                if (!verteilstelleComboBox.getItems().contains(bezeichnung))
                {
                    verteilstelleComboBox.getItems().add(bezeichnung);
                }
            }
        } else
        {
            System.out.println("Keine Verteilstellen gefunden oder Fehler beim Abrufen.");
        }

        // Füge Listener zum Jahrfeld hinzu
        yearField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d{0,4}"))
            {
                yearField.setText(oldValue);
            }
            if (newValue.length() == 4)
            {
                loadAltersstatistik();
            }
        });

        // Initialisiere die Listen für die dynamischen Felder
        initializeDynamicFields();

        // Setze Action-Events für Buttons
        openSQLQueryToolButton.setOnAction(this::openSQLQueryTool);
        addGroupButton.setOnAction(event -> handleAddGroup());
        //generateSQLQueryButton.setOnAction(this::handleGenerateSQLQuery);
        bescheidButton.setOnAction(this::openBescheidStatistik);

        handleLoadData(null);
    }

    private void initializeDynamicFields()
    {
        dynamicGroupStartFields.clear();
        dynamicGroupEndFields.clear();

        for (Node node : dynamicGroupContainer.getChildren())
        {
            if (node instanceof TextField)
            {
                TextField textField = (TextField) node;
                if (textField.getId() != null)
                {
                    if (textField.getId().startsWith("lowerAgeLimit"))
                    {
                        dynamicGroupStartFields.add(textField);
                    } else if (textField.getId().startsWith("upperAgeLimit"))
                    {
                        dynamicGroupEndFields.add(textField);
                    }
                }
            }
        }
    }

    @FXML
    private void openSQLQueryTool(ActionEvent actionEvent)
    {
        String sqlQuery;
        try
        {
            sqlQuery = getCurrentSQLQuery();
        } catch (IllegalArgumentException e)
        {
            // Zeige eine Fehlermeldung an und beende die Methode
            showAlert(Alert.AlertType.ERROR, "Fehler", e.getMessage());
            return;
        }

        try
        {
            // Lade die FXML-Datei und initialisiere den Controller
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/StatistiktoolSQL.fxml"));
            Parent root = fxmlLoader.load();

            // Hol den Controller und setze die SQL-Abfrage
            StatistiktoolSQLController sqlController = fxmlLoader.getController();
            sqlController.setSqlQuery(sqlQuery);

            // Erstelle eine neue Stage für das SQL-Abfrage-Tool
            Stage stage = new Stage();
            stage.setTitle("SQL-Abfrage Tool");

            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("StatistikToolSQL", scene);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e)
        {
            // Zeige eine Fehlermeldung an, wenn die FXML-Datei nicht geladen werden kann
            showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler beim Laden der FXML-Datei: " + e.getMessage());
        }
    }

    private String getCurrentSQLQuery()
    {
        String year = yearField.getText().trim();

        // Validierung der Eingaben für das Jahr
        if (year.isEmpty())
        {
            throw new IllegalArgumentException("Jahr muss angegeben werden.");
        }

        // SQL-Abfrage für das Jahr erstellen
        StringBuilder query = new StringBuilder("SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = " + year);

        // Überprüfen der Altersgruppen-Eingaben
        boolean hasAgeGroup = false;

        // Begrenzung auf maximal 16 Gruppen
        int maxGroups = Math.min(dynamicGroupStartFields.size(), 16);

        for (int i = 0; i < maxGroups; i++)
        {
            TextField startField = dynamicGroupStartFields.get(i);
            TextField endField = dynamicGroupEndFields.get(i);

            String startAgeText = startField.getText().trim();
            String endAgeText = endField.getText().trim();

            if (!startAgeText.isEmpty() && !endAgeText.isEmpty())
            {
                int startAge = Integer.parseInt(startAgeText);
                int endAge = Integer.parseInt(endAgeText);

                if (startAge > endAge)
                {
                    throw new IllegalArgumentException("Das Startalter darf nicht größer als das Endalter sein.");
                }

                // Altersgrenzen zur Abfrage hinzufügen
                if (hasAgeGroup)
                {
                    query.append(" OR ");
                } else
                {
                    query.append(" OR (");
                    hasAgeGroup = true; // Altersgruppe wurde hinzugefügt
                }

                // Altersgrenzen korrekt in Klammern setzen
                query.append("(TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN ")
                        .append(startAge).append(" AND ").append(endAge).append(")");
            }
        }

        // Schließe die Klammer für die Altersgruppenbedingung, falls eine Altersgruppe hinzugefügt wurde
        if (hasAgeGroup)
        {
            query.append(")");
        }

        // Schließe die Klammer für die WHERE-Bedingung
        query.append(");");

        return query.toString();
    }

    /**
     * @FXML private void handleGenerateSQLQuery(ActionEvent event) {
     * // Diese Methode öffnet das SQL-Tool und übergibt die SQL-Abfrage
     * openSQLQueryTool(event);
     * }
     **/

    @FXML
    private void handleAddGroup()
    {
        if (dynamicGroupStartFields.size() >= 16)
        {
            System.out.println("Maximale Anzahl von Gruppen erreicht.");
            return;
        }

        HBox groupBox = new HBox(10);
        TextField startField = new TextField();
        startField.setPromptText("Start");
        TextField endField = new TextField();
        endField.setPromptText("Ende");
        TextField resultField = new TextField();
        resultField.setEditable(false);

        dynamicGroupStartFields.add(startField);
        dynamicGroupEndFields.add(endField);
        dynamicResultFields.add(resultField);

        groupBox.getChildren().addAll(new Label("Gruppe:"), startField, new Label("bis"), endField, new Label("Ergebnis:"), resultField);
        dynamicGroupContainer.getChildren().add(groupBox);

        startField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d{0,2}"))
            {  // Erlaubt nur 0 bis 2 Stellen
                startField.setText(oldValue);
            } else
            {
                loadAltersstatistik();
            }
        });

        endField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d{0,2}"))
            {  // Erlaubt nur 0 bis 2 Stellen
                endField.setText(oldValue);
            } else
            {
                loadAltersstatistik();
            }
        });
    }

    /*
     public void executeStatistikQuery(String query) {
     try (Connection connection = statistikDAO.getConnection(); // Verbindung zur Datenbank holen
     Statement statement = connection.createStatement();
     ResultSet resultSet = statement.executeQuery(query)) {

     // Zähler für die Ergebnisse
     int index = 0;

     // Durch die Ergebnismenge iterieren
     while (resultSet.next()) {
     if (index >= dynamicResultFields.size()) {
     // Wenn mehr Ergebnisse als TextFields vorhanden sind, breche ab
     System.out.println("Mehr Ergebnisse als verfügbare TextFields.");
     break;
     }

     // Lese die Daten aus der Ergebnismenge
     // Ersetze 'someColumnName' durch den tatsächlichen Spaltennamen aus deiner SQL-Abfrage
     String result = resultSet.getString("someColumnName");

     // Setze das Ergebnis in das entsprechende TextField
     dynamicResultFields.get(index).setText(result);

     // Weiter zum nächsten TextField
     index++;
     }

     // Wenn es noch verbleibende TextFields gibt, setze diese auf leer
     while (index < dynamicResultFields.size()) {
     dynamicResultFields.get(index).setText("");
     index++;
     }

     } catch (SQLException e) {
     // Fehlerbehandlung: Fehlermeldung anzeigen
     showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler beim Ausführen der SQL-Abfrage: " + e.getMessage());
     }
     }
     **/

    /**
     * Methode zum Laden der Altersstatistik.
     * Diese Methode lädt die Statistikdaten basierend auf den eingegebenen Jahr, Altersgruppen und Verteilstelle
    */
    @FXML
    private void loadAltersstatistik()
    {
        if (!validateInput())
        {
            return;
        }
        List<int[]> altersgruppen = getAltersgruppen();
        int selectedYear = getSelectedYear();
        Optional<Statistiktool> statistikOpt = statistikDAO.loadAltersstatistik(selectedYear, altersgruppen);
        if (statistikOpt.isPresent())
        {
            Statistiktool statistik = statistikOpt.get();
            int yearResult = statistik.getJahresergebnis();
            int gesamtsumme = statistik.getGesamtsumme();
            int[] gruppenErgebnisse = statistik.getGruppen();
            setResults(yearResult, gesamtsumme, gruppenErgebnisse);
        }
    }

    /**
     * Setzt die Ergebnisse in den entsprechenden Ausgabefeldern.
     *
     * @param yearResult        Ergebnis für das Jahr
     * @param gruppenErgebnisse Ergebnisse für die Altersgruppen
     */
    public void setResults(int yearResult, int gesamtsumme, int[] gruppenErgebnisse)
    {
        yearResultField.setText(String.valueOf(yearResult));
        gesamtsummeField.setText(String.valueOf(gesamtsumme));
        for (int i = 0; i < gruppenErgebnisse.length; i++)
        {
            if (i < dynamicResultFields.size())
            {
                dynamicResultFields.get(i).setText(String.valueOf(gruppenErgebnisse[i]));
            }
        }
    }

    /**
     * Validiert die Benutzereingaben.
     * Überprüft, ob das Jahr, die Altersgruppen und die Verteilstelle korrekt eingegeben wurden.
     *
     * @return true, wenn alle Eingaben gültig sind, sonst false
     */
    private boolean validateInput()
    {
        String yearInput = yearField.getText().trim();
        boolean yearValid = yearInput.matches("\\d{4}");

        int year = yearValid ? Integer.parseInt(yearInput) : -1;
        if (yearValid && (year < 1900 || year > 2100))
        {
            yearValid = false;
        }

        List<int[]> altersgruppen = getAltersgruppen();

        boolean hasError = false;
        StringBuilder errorMessage = new StringBuilder();

        if (yearInput.isEmpty() && altersgruppen.isEmpty())
        {
            hasError = true;
            errorMessage.append("Bitte geben Sie entweder ein Jahr oder mindestens eine Altersgruppe ein.\n");
        }

        if (!yearInput.isEmpty() && !yearValid)
        {
            hasError = true;
            errorMessage.append("Das Jahr muss eine vierstellige Zahl sein und zwischen 1900 und 2100 liegen.\n");
        }

        if (!altersgruppen.isEmpty())
        {
            for (int[] gruppe : altersgruppen)
            {
                int startAge = gruppe[0];
                int endAge = gruppe[1];

                // Überprüfe, ob die Alterswerte gültige zwei-stellige Zahlen sind
                if (startAge < 0 || startAge > 99 || endAge < 0 || endAge > 99)
                {
                    hasError = true;
                    errorMessage.append("Alterswerte müssen zwei-stellig sein (0-99).\n");
                    break;
                }

                if (startAge > endAge)
                {
                    hasError = true;
                    errorMessage.append("Das Startalter darf nicht größer als das Endalter sein.\n");
                }
            }
        }

        if (hasError)
        {
            showAlert(Alert.AlertType.ERROR, "Fehler", errorMessage.toString());
            return false;
        }

        return true;
    }

    /**
     * Liest die Altersgruppen aus den Eingabefeldern aus und gibt diese als Liste von Integer-Arrays zurück.
     *
     * @return Liste von Altersgruppen als Integer-Arrays
     */
    // Methode zum Abrufen der Start- und Endalter
    public List<int[]> getAltersgruppen()
    {
        List<int[]> altersgruppen = new ArrayList<>();
        boolean hasError = false;
        StringBuilder errorMessage = new StringBuilder();

        // Überprüfen, ob die Listen korrekt synchronisiert sind
        if (dynamicGroupStartFields.size() != dynamicGroupEndFields.size())
        {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Die Anzahl der Start- und Endfelder stimmt nicht überein.");
            return altersgruppen;
        }

        for (int i = 0; i < dynamicGroupStartFields.size(); i++)
        {
            TextField startField = dynamicGroupStartFields.get(i);
            TextField endField = dynamicGroupEndFields.get(i);
            String startText = startField.getText().trim();
            String endText = endField.getText().trim();

            // Debug-Ausgaben hinzufügen
            System.out.println("Verarbeite Altersgruppe " + (i + 1) + ": Start = '" + startText + "', End = '" + endText + "'");

            // Wenn beide Felder leer sind, überspringen
            if (startText.isEmpty() && endText.isEmpty())
            {
                continue;
            }

            try
            {
                // Wenn sowohl Start- als auch Endwert vorhanden sind
                if (!startText.isEmpty() && !endText.isEmpty())
                {
                    int start = Integer.parseInt(startText);
                    int end = Integer.parseInt(endText);

                    // Validierung: Endwert muss mindestens zwei Ziffern haben
                    if (endText.length() < 2)
                    {
                        continue; // Endwert hat weniger als zwei Ziffern, keine Überprüfung nötig
                    }

                    // Validierung: Startwert darf nicht größer als Endwert sein
                    if (start > end)
                    {
                        hasError = true;
                        errorMessage.append("Der Startwert in Gruppe ").append(i + 1).append(" darf nicht größer als der Endwert sein.\n");
                    } else
                    {
                        altersgruppen.add(new int[]{start, end});
                    }
                } else if (!startText.isEmpty() || !endText.isEmpty())
                {
                    // Hier keine Fehlermeldung, wenn nur ein Wert vorhanden ist
                    continue;
                }
            } catch (NumberFormatException e)
            {
                // Die Fehlermeldung wird nur hinzugefügt, wenn beide Werte vorhanden sind
                if (!startText.isEmpty() && !endText.isEmpty())
                {
                    hasError = true;
                    errorMessage.append("Bitte geben Sie gültige Zahlen für die Altersgruppe ").append(i + 1).append(" ein.\n");
                }
            }
        }

        if (hasError)
        {
            showAlert(Alert.AlertType.ERROR, "Fehler", errorMessage.toString());
        }

        return altersgruppen;
    }

    /**
     * Liest das ausgewählte Jahr aus dem Eingabefeld.
     *
     * @return Das eingegebene Jahr als int.
     */
    public int getSelectedYear()
    {
        try
        {
            String input = yearField.getText().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e)
        {
            showAlert(AlertType.ERROR, "Fehler", "Ungültiges Jahresformat: " + yearField.getText() + ". Bitte verwenden Sie das Format JJJJ.");
            return -1;
        }
    }

    /**
     * Speichert die Ergebnisse in einer CSV-Datei.
     */
    @FXML
    private void handleSaveResults()
    {
        List<int[]> altersgruppen = getAltersgruppen();
        int selectedYear = getSelectedYear();
        int jahresergebnis = Integer.parseInt(yearResultField.getText());

        List<Integer> ergebnisse = new ArrayList<>();
        int gesamtsumme = 0; // Initialisierung der Gesamtsumme

        for (TextField resultField : dynamicResultFields)
        {
            int ergebnis = Integer.parseInt(resultField.getText());
            ergebnisse.add(ergebnis);
            gesamtsumme += ergebnis; // Addiere das Ergebnis zur Gesamtsumme
        }

        boolean success = statistikDAO.saveAltersgruppen(selectedYear, altersgruppen, jahresergebnis, ergebnisse);

        if (success)
        {
            gesamtsummeField.setText(String.valueOf(gesamtsumme)); // Setze die berechnete Gesamtsumme
            showAlert(AlertType.ERROR, "Erfolg", "Ergebnisse erfolgreich gespeichert.");
        } else
        {
            showAlert(AlertType.ERROR, "Fehler", "Fehler beim Speichern der Ergebnisse.");
        }
    }

    /*
     private void saveJahresergebnis(int year, int jahresergebnis) {
     String insertQuery = "INSERT INTO jahresergebnisse (year, jahresergebnis) VALUES (?, ?) ON DUPLICATE KEY UPDATE jahresergebnis=VALUES(jahresergebnis)";
     try (Connection conn = SQLConnection.getCon();
     PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
     pstmt.setInt(1, year);
     pstmt.setInt(2, jahresergebnis);
     pstmt.executeUpdate();
     } catch (SQLException e) {
     showAlert(AlertType.ERROR, "Fehler", "Fehler beim Speichern des Jahresergebnisses: " + e.getMessage());
     e.printStackTrace();
     }
     }
     **/

    /**
     * Beendet das Programm.
    */
    @FXML
    private void handleExit()
    {
        Stage stage = (Stage) addGroupButton.getScene().getWindow(); /// Stage stage = (Stage) handleExit.getParentPopup().getOwnerWindow();
        stage.close();
    }

    /**
     * Zeigt die Hilfe an.
     */
    @FXML
    private void handleHelp()
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Hilfe");
        alert.setHeaderText("Anleitung zur Altersstruktur");
        alert.setContentText(
                "Anleitung zur Verwendung des Altersstruktur-Statistiktools:\n\n"
        +
                        "1. Jahr eingeben: Geben Sie das Jahr im Format JJJJ ein. Das Jahr wird verwendet, um die Altersstruktur für ein bestimmtes Jahr zu berechnen.\n"
        +
                        "2. Verteilstelle auswählen: Wählen Sie die gewünschte Verteilstelle aus der Dropdown-Liste aus.\n"
                        +
                        "3. Gruppen hinzufügen: Klicken Sie auf 'Gruppe hinzufügen', um neue Altersgruppen hinzuzufügen. Sie können bis zu 16 Gruppen erstellen. Geben Sie für jede Gruppe ein Start- und ein Endalter ein.\n"
                        +
                        "4. Altersgruppen eingeben: Geben Sie das Start- und Endalter für jede Altersgruppe ein. Zum Beispiel '20' und '30' für die Altersgruppe von 20 bis 30 Jahren.\n"
                        +
                        "5. Ergebnisse anzeigen: Die Ergebnisse der Altersstruktur werden im Ergebnisbereich angezeigt. Die Gesamtsumme zeigt die Gesamtzahl der Personen, die auf die angegebenen Kriterien zutreffen.\n"
                        +
                        "6. Ergebnisse speichern: Klicken Sie auf 'Ergebnisse speichern', um die Ergebnisse in einer CSV-Datei zu speichern.\n\n"
                        +
                        "Hinweis: Wenn Sie nur das Jahr und die Verteilstelle eingeben, wird die Gesamtsumme für diese Kriterien angezeigt. Wenn Sie nur Altersgruppen eingeben, wird die Gesamtsumme für die angegebenen Altersgruppen angezeigt. Beides gleichzeitig zu verwenden, ist möglich."
        );
        alert.showAndWait();
    }

    /**
     * Öffnet das Admin-Tool.
     */
    @FXML
    private void handleSettings()
    {
        System.out.println("Öffne Admin-Tool...");
    }

    /**
     * Zeigt die gespeicherte Statistik an.
     */
    @FXML
    private void handleViewStatistics()
    {
        List<String[]> statistikData = statistikDAO.loadStatistik("altersstatistik.csv");
        if (statistikData.isEmpty())
        {
            showAlert(AlertType.ERROR, "Fehler", "Fehler beim Lesen der CSV-Datei.");
            return;
        }

        StringBuilder content = new StringBuilder();
        for (String[] row : statistikData)
        {
            content.append(String.join(",", row)).append("\n");
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Gespeicherte Statistik");
        alert.setHeaderText("Inhalt der CSV-Datei");
        TextArea textArea = new TextArea(content.toString());
        textArea.setEditable(false);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    /**
     * Druckt die Ergebnisse als PDF.
     */
    @FXML
    private void handlePrint()
    {
        try
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("PDF speichern");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Dateien", "*.pdf"));
            File file = fileChooser.showSaveDialog(printButton.getScene().getWindow());
            if (file != null)
            {
                saveAsPdf(file);
            }
        } catch (Exception e)
        {
            showAlert(AlertType.ERROR, "Fehler", "Fehler beim Drucken der Statistik: " + e.getMessage());
        }
    }

    /**
     * Speichert die Ergebnisse als PDF-Datei.
     *
     * @param file Die Datei, in die die Ergebnisse gespeichert werden.
     */
    private void saveAsPdf(File file)
    {
        try (PDDocument document = new PDDocument())
        {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page))
            {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(25, 725);

                contentStream.showText("Altersstatistik");
                contentStream.newLine();
                contentStream.newLine();

                contentStream.showText("Gesamtsumme: " + gesamtsummeField.getText());
                contentStream.newLine();

                for (int i = 0; i < dynamicGroupStartFields.size(); i++)
                {
                    String start = dynamicGroupStartFields.get(i).getText();
                    String end = dynamicGroupEndFields.get(i).getText();
                    String result = dynamicResultFields.get(i).getText();
                    contentStream.showText("Gruppe " + start + " - " + end + ": " + result);
                    contentStream.newLine();
                }

                contentStream.endText();
            }

            document.save(file);
            showAlert(AlertType.ERROR, "Erfolg", "PDF erfolgreich gespeichert.");
        } catch (IOException e)
        {
            showAlert(AlertType.ERROR, "Fehler", "Fehler beim Speichern des PDFs: " + e.getMessage());
        }
    }
    /**
    */
    // Methode zum Abrufen des Jahres
    public String getYear()
    {
        return yearField.getText().trim();
    }

    @SuppressWarnings("unused")
    private String generateAltersgruppenCondition(List<int[]> altersgruppen)
    {
        StringBuilder condition = new StringBuilder();
        if (altersgruppen != null && !altersgruppen.isEmpty())
        {
            for (int i = 0; i < altersgruppen.size(); i++)
            {
                int startAlter = altersgruppen.get(i)[0];
                int endAlter = altersgruppen.get(i)[1];
                condition.append(String.format("(YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN %d AND %d", startAlter, endAlter));
                if (i < altersgruppen.size() - 1)
                {
                    condition.append(" OR ");
                }
            }
        }
        return condition.toString();
    }

    /**
     * Zeigt eine Benachrichtigung an.
     *
     * @param title   Der Titel der Benachrichtigung.
     * @param message Die Nachricht der Benachrichtigung.
     */
    private void showAlert(AlertType error, String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Initialisiert das Statistik-Tool.
     * Aktuell keine Implementierung.
     */
    public void initStatistikTool()
    {
    }

    /**
     */
    @FXML
    public void handleLoadData(ActionEvent actionEvent)
    {
        try
        {
            // Daten aus der Datenbank laden
            List<Verteilstelle> verteilstellen = verteilstelleDAO.readAll();
            Set<String> verteilstelleNames = new HashSet<>(); // Verwende Set für eindeutige Werte

            // Überprüfen, ob Daten geladen wurden
            if (verteilstellen != null && !verteilstellen.isEmpty())
            {
                // Namen der Verteilstellen in eine Set umwandeln
                for (Verteilstelle v : verteilstellen)
                {
                    verteilstelleNames.add(v.getBezeichnung());
                }

                // Die ComboBox mit den geladenen Daten aktualisieren
                verteilstelleComboBox.getItems().clear(); // Vorherige Einträge löschen
                verteilstelleComboBox.getItems().addAll(verteilstelleNames); // Neue Einträge hinzufügen
            } else
            {
                showAlert(AlertType.ERROR, "Fehler", "Keine Verteilstellen gefunden oder Fehler beim Abrufen.");
            }
        } catch (Exception e)
        {
            // Fehlerbehandlung
            showAlert(AlertType.ERROR, "Fehler", "Fehler beim Laden der Daten: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenGuthabenWindow(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/statistiktool/GuthabenStatistik.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Guthaben und offene Beträge");

            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("GuthabenStatistik", scene);
            stage.setScene(scene);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Button) event.getSource()).getScene().getWindow());
            stage.show();
        } catch (Exception e)
        {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Fehler", "Fehler beim Öffnen des Guthaben-Fensters: " + e.getMessage());
        }
    }
    /**
     */
    public void handleOpenNationalitaetStatistik()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/statistiktool/NationalitaetStatistik.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Nationalitäten Statistik");

            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("NationalitaetStatistik", scene);
            stage.setScene(scene);

            stage.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void openHerkunft(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/statistiktool/HerkunftStatistik.fxml"));
            Parent root = loader.load();

            // Den Controller abrufen
            HerkunftStatistikController controller = loader.getController();

            @SuppressWarnings("unused")
            Connection con = SQLConnection.getCon();

            HaushaltDAO haushaltDAO = new HaushaltDAOimpl();
            controller.setHaushaltDAO(haushaltDAO);

            // Erstelle und zeige das neue Fenster
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Herkunft-Statistik");
            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("HerkunftStatistik.fxml", scene);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler beim Öffnen der Herkunft-Statistik Fenster: " + e.getMessage());
        } catch (SQLException e)
        {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Fehler", "Datenbankfehler: " + e.getMessage());
        }
    }

    // Beispiel im übergeordneten Controller, der das FXML lädt
    @FXML
    private void openArchivierteKunden()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/statistiktool/ArchivierteKundenStatistik.fxml"));
            Parent root = loader.load();

            // Den Controller abrufen und das DAO setzen
            ArchivierteKundenStatistikController controller = loader.getController();
            controller.setHaushaltDAO(new HaushaltDAOimpl());
            System.out.println("setHaushaltDAO() aufgerufen");

            Stage stage = new Stage();
            stage.setTitle("Archivierte Kunden");
            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("ArchivierteKundenStatistik.fxml", scene);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Das FXML konnte nicht geladen werden: " + e.getMessage());
        }
    }
    /**
     */
    // Methode, um das Jahresübersicht-Fenster zu öffnen
    public void openJahresuebersicht()
    {
        try
        {
            // FXML-Datei laden
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/statistiktool/JahresuebersichtStatistik.fxml"));
            Parent root = loader.load();

            // Neue Stage (Fenster) erstellen
            Stage stage = new Stage();
            stage.setTitle("Jahresübersicht");

            // Szene setzen und anzeigen
            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("JahresuebersichtStatistik.fxml", scene);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void openBescheidStatistik(ActionEvent event)
    {
        try
        {
            // 1) Test: Ressource abfragen und in der Konsole ausgeben
            URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/statistiktool/BescheidartStatistik.fxml");
            System.out.println("Pfad: " + fxmlUrl);

            // 2) Falls fxmlUrl == null, wurde die Datei nicht gefunden
            if (fxmlUrl == null)
            {
                showAlert(Alert.AlertType.ERROR, "Fehler",
                        "Die FXML-Ressource wurde nicht gefunden! "
                      + "Pfad fehlerhaft oder Datei existiert nicht.");
                return; // Methode beenden, da wir nicht laden können
            }

            // 3) Ansonsten normal laden
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // 4) Fenster aufbauen und anzeigen
            Stage stage = new Stage();
            stage.setTitle("Bescheidarten Statistik");
            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("BescheidartStatistik.fxml", scene);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            // 5) Fehler beim Laden/Parsen der FXML
            showAlert(Alert.AlertType.ERROR, "Fehler",
                    "Fehler beim Öffnen der Bescheidarten Statistik: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
