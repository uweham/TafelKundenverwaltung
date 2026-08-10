package kundenverwaltung.controller;
import kundenverwaltung.controller.admintool.StatistiktoolSQLController;
import kundenverwaltung.controller.statistiktool.ArchivierteKundenStatistikController;
import kundenverwaltung.controller.statistiktool.AusgabegruppenStatistikController;
import kundenverwaltung.controller.statistiktool.BescheidartStatistikController;
import kundenverwaltung.controller.statistiktool.GuthabenStatistikController;
import kundenverwaltung.controller.statistiktool.HerkunftStatistikController;
import kundenverwaltung.controller.statistiktool.JahresuebersichtStatistikController;
import kundenverwaltung.controller.statistiktool.NationalitaetStatistikController;
import kundenverwaltung.controller.statistiktool.StatistiktoolHeaderController;
import kundenverwaltung.controller.statistiktool.StatistiktoolMasterClassController;
import kundenverwaltung.controller.statistiktool.StatistiktoolResultViewController;
import kundenverwaltung.dao.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import kundenverwaltung.service.Constants;
import kundenverwaltung.service.SQLQuery_to_CSV;
import kundenverwaltung.service.TableView_to_PDF;
import kundenverwaltung.service.UtilErrorLog;
import kundenverwaltung.model.User;
import kundenverwaltung.model.Verteilstelle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.scene.Node;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import javafx.scene.layout.VBox;


/**
 * Controller-Klasse für das Statistik-Tool.
 * Diese Klasse steuert die Benutzeroberfläche und die Interaktionen für die Anzeige und Verwaltung der Altersstatistik.
 */

public class StatistiktoolController extends StatistiktoolMasterClassController<StatistiktoolController>
{

   // @FXML
    //private ComboBox<String> verteilstelleComboBox;
   // private ComboBox<Verteilstelle> verteilstelleComboBox;
    @FXML
    private TextField startAgeTextField;
    @FXML
    private TextField endAgeTextField;
    @FXML
    private TextField yearField;
    @FXML
    private VBox dynamicGroupContainer;
    @FXML
    private Button addGroupButton;
    @FXML
    private Button  clearGroupButton;
    
    @FXML
    private MenuItem handleHelp;
    @FXML
    private MenuItem handleSettings;
    @FXML
    private MenuItem handleViewStatistics;

    @SuppressWarnings("unused")
    
    private VerteilstelleDAO verteilstelleDAO;
    private List<TextField> dynamicGroupStartFields = new ArrayList<>();
    private List<TextField> dynamicGroupEndFields = new ArrayList<>();
    
    private UtilErrorLog utilerrorlog = new UtilErrorLog();
    
    /**
     *
     */

    @SuppressWarnings("unchecked")
    @FXML
    public void initialize()
    {
        initRange();
        initVerteilstelle();
        // Füge Listener zum Jahrfeld hinzu
        yearField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d{0,4}"))
            {
                yearField.setText(oldValue);
            }
            if (newValue.length() == 4)
            {
              //              loadAltersstatistik();
            }
        });

        // Initialisiere die Listen für die dynamischen Felder
        initializeDynamicFields();

        // Setze Action-Events für Buttons
        addGroupButton.setOnAction(event -> handleAddGroup());
        loadheader("Tafel Statistik - Angemeldet als :", "Statistik:Altersstruktur");
        loadresultview(this,childResultContainer.getPrefWidth(),childResultContainer.getPrefHeight());
        
         
    }
    
    @FXML
    private void handleClearGroup()
    {
      initializeDynamicFields();
      dynamicGroupContainer.getChildren().clear();
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


    public String getCurrentSQLQuery()
    {
        if (!validateInput())
        {
            return "";
        }
        List<int[]> altersgruppen = getAltersgruppen();
        int selectedYear = getSelectedYear(yearField.getText().trim());
        int verteilstellenId=getSelectedVerteilstelle();
        int rangeId=getSelectedRange();
        String query = statistikDAO.buildSqlQueryAlterstatistik(verteilstellenId,selectedYear, altersgruppen,rangeId);
        statistikDAO.addSqlPar(1,verteilstellenId);
        return query.toString();
    }

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

        dynamicGroupStartFields.add(startField);
        dynamicGroupEndFields.add(endField);
        
        groupBox.getChildren().addAll(new Label("Gruppe:"), startField, new Label("bis"), endField);
        dynamicGroupContainer.getChildren().add(groupBox);

        startField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d{0,2}"))
            {  // Erlaubt nur 0 bis 2 Stellen
                startField.setText(oldValue);
            }
        });

        endField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d{0,2}"))
            {  // Erlaubt nur 0 bis 2 Stellen
                endField.setText(oldValue);
            }
        });
    }
        

    /**
     * Validiert die Benutzereingaben.
     * Überprüft, ob das Jahr, die Altersgruppen und die Verteilstelle korrekt eingegeben wurden.
     *
     * @return true, wenn alle Eingaben gültig sind, sonst false
     */
    private boolean validateInput()
    {
      utilerrorlog.addError(Constants.ERROR_MSG_CLEAR, null );
      boolean hasError = false; 
      String yearInput = yearField.getText().trim();
      hasError = !(yearInput.matches("\\d{4}") ||  yearInput.isEmpty()) ;
      if (hasError)
      {
        utilerrorlog.addError(Constants.ERROR_MSG_ADD, "Ungültiges Jahresformat: " + yearInput + ". Bitte verwenden Sie das Format JJJJ.");
      }
      if (!hasError)
      {
        int year=getSelectedYear(yearInput) ; // -1 Error // 0 Empty
        if (year == Constants.NUMERIC_ERROR)
        {
          hasError=true;
          utilerrorlog.addError(Constants.ERROR_MSG_ADD, "Ungültiges Jahresformat: " + yearInput + ". Bitte verwenden Sie das Format JJJJ.");
        }
        else
        {
          if ((year < 1900 || year > 2100) && year != 0)
          {
            hasError=true;
            utilerrorlog.addError(Constants.ERROR_MSG_ADD, "Ungültiges Jahr: " + yearInput + ". Bitte Jahr zwischen 1900 und 2100.");
          }
        }
      }
      if (!hasError)
      { 
        List<int[]> altersgruppen = getAltersgruppen();
        if (yearInput.isEmpty() && altersgruppen.isEmpty())
        {
            hasError = true;
            utilerrorlog.addError(Constants.ERROR_MSG_ADD, "Bitte geben Sie entweder ein Jahr oder mindestens eine Altersgruppe ein.");
        }

        if (!altersgruppen.isEmpty())
        {
            for (int[] gruppe : altersgruppen)
            {
                int startAge = gruppe[0];
                int endAge = gruppe[1];
                if (startAge == Constants.INTERNAL_ERROR || startAge == Constants.NUMERIC_ERROR )
                {
                  hasError = true;
                  utilerrorlog.addError(Constants.ERROR_MSG_ADD,"Numerischer Formatfehler Gruppe "+endAge);
                  break;
                }
                // Überprüfe, ob die Alterswerte gültige zwei-stellige Zahlen sind
                if (startAge < 0 || startAge > 99 || endAge < 0 || endAge > 99)
                {
                    hasError = true;
                    utilerrorlog.addError(Constants.ERROR_MSG_ADD,"Alterswerte müssen zwei-stellig sein (0-99).");
                    break;
                }

                if (startAge > endAge)
                {
                    hasError = true;
                    utilerrorlog.addError(Constants.ERROR_MSG_ADD,"Das Startalter darf nicht größer als das Endalter sein.");
                }
            }
        }
      }
      if (hasError)
      {
          showalert.showAlert(Alert.AlertType.ERROR, "Fehler", utilerrorlog.getErrormessage());
      }
      return !hasError;
    
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
        // Überprüfen, ob die Listen korrekt synchronisiert sind
        if (dynamicGroupStartFields.size() != dynamicGroupEndFields.size())
        {
            altersgruppen.add(new int[]{Constants.INTERNAL_ERROR,0});
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
                System.out.println("Überspringe leere Altersgruppe " + (i + 1) );
                continue;
            }

            try
            {
                // Wenn sowohl Start- als auch Endwert vorhanden sind
                if (!startText.isEmpty() && !endText.isEmpty())
                {
                    int start = Integer.parseInt(startText);
                    int end = Integer.parseInt(endText);
                    altersgruppen.add(new int[]{start, end});

                } else if (!startText.isEmpty() || !endText.isEmpty())
                {
                  altersgruppen.clear();
                  altersgruppen.add(new int[]{Constants.NUMERIC_ERROR,i});
                  return altersgruppen;
              }
            } catch (NumberFormatException e)
            {
                    altersgruppen.clear();
                    altersgruppen.add(new int[]{Constants.NUMERIC_ERROR,i});
                    return altersgruppen;
            }
        }
        return altersgruppen;
    }

    /**
     * Liest das ausgewählte Jahr aus dem Eingabefeld.
     *
     * @return Das eingegebene Jahr als int.
     */
    public int getSelectedYear(String input)
    {   
        try
        {
            if (input.isBlank() || input.isEmpty())
            {
              return 0;
            }
            else
            {
              return Integer.parseInt(input);
            }
        } catch (NumberFormatException e)
        {
            return Constants.NUMERIC_ERROR;
        }
        
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
                        "5. Abfrage ausführen: Die Ergebnisse der Altersstruktur werden im Ergebnisbereich angezeigt.\n"
                        +
                        "6. CSV/PDF Export : Die Ergebnisse werden in einer CSV oder PDF Datei gespeichert.\n\n"
                        +
                        "Hinweis: Wenn Sie nur das Jahr und die Verteilstelle eingeben, wird die Gesamtsumme für diese Kriterien angezeigt. Wenn Sie nur Altersgruppen eingeben, wird die Gesamtsumme für die angegebenen Altersgruppen angezeigt. Beides gleichzeitig zu verwenden, ist nicht möglich."
        );
        alert.showAndWait();
    }

 
  
    /**
    */
    // Methode zum Abrufen des Jahres
    public String getYear()
    {
        return yearField.getText().trim();
    }

    /**
     * Initialisiert das Statistik-Tool.
     * Aktuell keine Implementierung.
     */
    public void initStatistikTool()
    {
    }


    
    // 
    @FXML
    private void handleOpenGuthabenWindow(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/statistiktool/GuthabenStatistik.fxml"));
            Parent root = loader.load();
            GuthabenStatistikController controller = loader.getController();
            controller.setUser(user);
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
            showalert.showAlert(AlertType.ERROR, "Fehler", "Fehler beim Öffnen des Guthaben-Fensters: " + e.getMessage());
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
            NationalitaetStatistikController controller = loader.getController();
            controller.setUser(user);
 
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
            showalert.showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler beim Öffnen der Herkunft-Statistik Fenster: " + e.getMessage());
        } catch (SQLException e)
        {
            e.printStackTrace();
            showalert.showAlert(Alert.AlertType.ERROR, "Fehler", "Datenbankfehler: " + e.getMessage());
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
            controller.setUser(user);
 
            Stage stage = new Stage();
            stage.setTitle("Statistik Kunden");
            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("ArchivierteKundenStatistik.fxml", scene);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
          showalert.showAlert(Alert.AlertType.ERROR, "Fehler", "Das FXML konnte nicht geladen werden: " + e.getMessage());
        }
    }
    /**
     */
    @FXML
    // Methode, um das Jahresübersicht-Fenster zu öffnen
    public void openJahresuebersicht()
    {
        try
        {
            // FXML-Datei laden
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/statistiktool/JahresuebersichtStatistik.fxml"));
            Parent root = loader.load();
            // Den Controller abrufen und das DAO setzen
            JahresuebersichtStatistikController controller = loader.getController();
            controller.setUser(user);
 
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
    // Methode, um das Jahresübersicht-Fenster zu öffnen
    public void openAusgabegruppen()
    {
        try
        {
            // FXML-Datei laden
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/statistiktool/AusgabegruppenStatistik.fxml"));
            Parent root = loader.load();
            // Den Controller abrufen und das DAO setzen
            AusgabegruppenStatistikController controller = loader.getController();
            controller.setUser(user);
 
            // Neue Stage (Fenster) erstellen
            Stage stage = new Stage();
            stage.setTitle("Ausgabegruppen");

            // Szene setzen und anzeigen
            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("AusgabegruppenStatistik.fxml", scene);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/statistiktool/BescheidartStatistik.fxml"));
            Parent root = loader.load();
            BescheidartStatistikController controller = loader.getController();
            controller.setUser(user);
 
            Stage stage = new Stage();
            stage.setTitle("Bescheidarten Statistik");
            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("BescheidartStatistik.fxml", scene);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            // 5) Fehler beim Laden/Parsen der FXML
          showalert.showAlert(Alert.AlertType.ERROR, "Fehler",
                    "Fehler beim Öffnen der Bescheidarten Statistik: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
