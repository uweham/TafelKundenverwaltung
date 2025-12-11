package setupprogram;

import static kundenverwaltung.toolsandworkarounds.PropertiesFileController.PROPERTIES_LOCATION_DATABASE_INFO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Properties;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kundenverwaltung.toolsandworkarounds.BlowfishEncryption;
import kundenverwaltung.toolsandworkarounds.CustomPropertiesStore;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

/**
 * Contains methods to set up a new database and to create the required database structure.
 *
 * @author Robin Becker
 * @version 1.0
 * @Date 16.08.2018
 */
public class NewDatabaseInstallationController
{

  @FXML
  private TextField txtDbAddress;

  @FXML
  private TextField txtDbPort;

  @FXML
  private TextField txtDbUser;

  @FXML
  private TextField txtDbName;

  @FXML
  private Button btnCheckConnect;

  @FXML
  private Button btnImportDatabase;

  @FXML
  private Button btnFinish;

  @FXML
  private Button btnBack;

    @FXML
    private CheckBox ckbxConnectionOkay;

    @FXML
    private PasswordField txtDbPassword;

    /**
     * Gets the database information from the GUI TextFields and checks the SQL connection with JDBC.<br>
     *     If the connection is successful the database credentials are saved in the corresponding .properties file (database.properties).
     *
     * @throws FileNotFoundException
     * @throws SQLException
     */
    // SQL-Skript Importmethode aus DatabaseMigrationController
    private void runSqlScript(Connection con, File sqlFile, String dbName) throws SQLException, IOException
    {
      try (Statement stmt = con.createStatement())
      {
          // Wählen Sie die Datenbank aus, bevor Sie das SQL-Skript ausführen
          stmt.execute("USE " + dbName + ";");

          String sql = new String(Files.readAllBytes(sqlFile.toPath()), StandardCharsets.UTF_8);
          String[] sqlStatements = sql.split(";");
          for (String statement : sqlStatements)
          {
              if (!statement.trim().isEmpty())
              {
                  stmt.execute(statement);
              }
          }
      }
  }

    /**
     */
    @FXML
    void btnConnectDatabase(ActionEvent event)
    {
        try
        {
            String dbAddress = txtDbAddress.getText();
            String dbPort = txtDbPort.getText();
            String dbUsername = txtDbUser.getText();
            String dbPassword = txtDbPassword.getText();
            Connection checkCon = DriverManager
                    .getConnection("jdbc:mariadb://" + dbAddress + ":" + dbPort, dbUsername, dbPassword);

            if (checkCon != null)
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Datenbank-Verbindung");
                alert.setHeaderText("Die Datenbank Einstellungen wurden geprüft.");
                alert.setContentText(
                        "SQL-Verbindung erfolgreich getestet. Bestätigen Sie mit \"OK\", wenn Sie"
                                + " die Einstellungen "
                                + "übernehmen wollen.");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK)
                {
                    try
                    {
                        CustomPropertiesStore prop = CustomPropertiesStore.loadDatabasePropertiesFileCustomStore();
                        prop.setProperty("jdbc.hostname", dbAddress);
                        prop.setProperty("jdbc.port", dbPort);
                        prop.setProperty("jdbc.username", dbUsername);
                        prop.setProperty("jdbc.password", BlowfishEncryption.encrypt(dbPassword));

                        FileOutputStream output =
                                new FileOutputStream(PROPERTIES_LOCATION_DATABASE_INFO);
                        prop.store(output, "MySQL database information");
                        output.close();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    ckbxConnectionOkay.setVisible(true);
                    ckbxConnectionOkay.setSelected(true);
                    ckbxConnectionOkay.setStyle("-fx-color: green");
                    ckbxConnectionOkay.setText("Datenbank-Server verbunden");
                    txtDbAddress.setEditable(false);
                    txtDbPort.setEditable(false);
                    txtDbUser.setEditable(false);
                    txtDbPassword.setEditable(false);
                    btnImportDatabase.setDisable(false);
                }
            }
            checkCon.close();
        } catch (SQLException SQLex)
        {
            SQLex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fehler bei der Verbindung!");
            alert.setHeaderText(
                    "Achtung, die Datenbank-Einstellungen sind möglicherweise fehlerhaft!");
            alert.setContentText(
                    "Es kann keine Verbindung zum SQL-Server hergestellt werden. Bitte prüfen Sie"
                            + " die Einstellungen oder Ihren Datenbank-Server.");
            alert.showAndWait();
            ckbxConnectionOkay.setVisible(true);
            ckbxConnectionOkay.setSelected(false);
            ckbxConnectionOkay.setStyle("-fx-color: red");
            ckbxConnectionOkay.setText("Datenbank-Server nicht verbunden");
        }
    }
    /**
     */
    @FXML
    void btnImportDatabase(ActionEvent event)
    {
        try
        {
            Properties prop = PropertiesFileController.loadDbInfoPropertiesFile();
            String dbPort = prop.getProperty("jdbc.port");
            String dbAddress = prop.getProperty("jdbc.hostname");
            String dbUsername = prop.getProperty("jdbc.username");
            String dbPassword = txtDbPassword.getText();
            String dbName = txtDbName.getText();

            // Erstellen Sie die Datenbank, wenn sie nicht existiert
            try (Connection con = DriverManager
                    .getConnection("jdbc:mariadb://" + dbAddress + ":" + dbPort, dbUsername, dbPassword))
            {
                try (Statement stmt = con.createStatement())
                {
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + dbName + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                }
            }

            // Verbinden Sie sich mit der neu erstellten Datenbank
            try (Connection con = DriverManager
                    .getConnection("jdbc:mariadb://" + dbAddress + ":" + dbPort + "/" + dbName, dbUsername, dbPassword))
            {

                File sqlFile = chooseSqlScript();
                if (sqlFile != null)
                {
                    runSqlScript(con, sqlFile, dbName);

                    Alert alertPositiv = new Alert(Alert.AlertType.INFORMATION);
                    alertPositiv.setTitle("Datenbank-Wiederherstellung");
                    alertPositiv.setHeaderText("Die Datenbank \"" + dbName + "\" wurde erfolgreich auf den Server \"" + dbAddress + "\" importiert.");
                    alertPositiv.setContentText("Die Datenbank-Struktur wurde erstellt. Sie können nun das Kundenverwaltungsprogramm starten.");
                    alertPositiv.showAndWait();
                    btnFinish.setDisable(false);
                } else
                {
                    Alert alertNoFile = new Alert(Alert.AlertType.WARNING);
                    alertNoFile.setTitle("Datei auswählen");
                    alertNoFile.setHeaderText("Sie haben keine .sql Datei ausgewählt!");
                    alertNoFile.setContentText("Bitte wählen Sie eine SQL-Sicherungsdatei (*.sql) aus und versuchen Sie es erneut.");
                    alertNoFile.showAndWait();
                }
            }
        } catch (SQLException | IOException ex)
        {
            Alert alertNegativ = new Alert(Alert.AlertType.WARNING);
            alertNegativ.setTitle("Fehler beim Importieren");
            alertNegativ.setHeaderText("Fehler beim Importieren der SQL-Datei");
            alertNegativ.setContentText(ex.getMessage());
            alertNegativ.showAndWait();
        }
    }


    private File chooseSqlScript()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wählen Sie die SQL-Datei");
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("SQL-Skript", "*.sql"));
        return fileChooser.showOpenDialog(new Stage());
    }
    /**
     */
    @FXML
    void returnToOptionMenu(ActionEvent event)
    {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        currentStage.close();
        WelcomeScreenController.getChooseDatabaseOptionStg().show();
    }
    /**
     */
    @FXML
    void finishSetup(ActionEvent event)
    {
        Platform.exit();
    }
}

