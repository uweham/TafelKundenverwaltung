package kundenverwaltung.controller;

//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.FileSystems;
//import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kundenverwaltung.toolsandworkarounds.BlowfishEncryption;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;
import kundenverwaltung.toolsandworkarounds.CustomPropertiesStore;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

/**
 * @author Robin Becker
 * @version 1.0
 * @Date 20.07.2018
 */
public class DatabaseConnectionController
{
  @FXML
  private Label labelHeader;
  @FXML
  private Label labelServerName;
  @FXML
  private Label labelPort;
  @FXML
  private Label labelLogin;
  @FXML
  private Label labelPassword;
  @FXML
  private Label labelDatabase;
  @FXML
  private TextField txtServername;
  @FXML
  private TextField txtPort;
  @FXML
  private TextField txtUser;
  @FXML
  private TextField txtPasswort;
  @FXML
  private TextField txtDatenbank;
  @FXML
  private Button buttonCheckAndSetConnection;
  @FXML
  private Button buttonCancel;

    private ChangeFontSize changeFontSize = new ChangeFontSize();

    /**
     * Initializes the controller and loads the database settings from properties file.
     *
     * @throws Exception if an error occurs during initialization
     */
    @FXML
    public void initialize() throws Exception
    {
        Properties prop = PropertiesFileController.loadDbInfoPropertiesFile();
        String dbName = prop.getProperty("jdbc.dbName");
        String dbPort = prop.getProperty("jdbc.port");
        String dbHostname = prop.getProperty("jdbc.hostname");
        String dbUsername = prop.getProperty("jdbc.username");

        txtServername.setText(dbHostname);
        txtDatenbank.setText(dbName);
        txtPort.setText(dbPort);
        txtUser.setText(dbUsername);
    }

    /**
     * Takes the database information and checks the SQL connection with JDBC.<br>
     *     If the connection is successful the database credentials are saved in the corresponding .properties file (database.properties) and the program is closed.
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void checkAndSetConnection()
    {
        try
        {
            String fileSeparator = FileSystems.getDefault().getSeparator();
            String dbHostname = txtServername.getText();
            String dbName = txtDatenbank.getText();
            String dbPort = txtPort.getText();
            String dbUsername = txtUser.getText();
            String dbPassword = txtPasswort.getText();

            // Überprüfe die Verbindung und fange alle Fehler ab
            Connection checkCon = DriverManager.getConnection("jdbc:mariadb://"
            + dbHostname + ":" + dbPort + "/" + dbName,
                    dbUsername, dbPassword);

            if (checkCon != null && !checkCon.isClosed())
            {
                checkCon.close();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Datenbank-Einstellungen");
                alert.setHeaderText("Achtung: Sie versuchen die Datenbank-Einstellungen zu ändern");
                alert.setContentText(
                        "SQL Verbindung erfolgreich getestet. Bestätigen Sie mit \"OK\", wenn Sie"
                                + " die Einstellungen übernehmen wollen.");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK)
                {
                    try
                    {
                        CustomPropertiesStore prop = CustomPropertiesStore.loadDatabasePropertiesFileCustomStore();

                        prop.setProperty("jdbc.hostname", dbHostname);
                        prop.setProperty("jdbc.dbName", dbName);
                        prop.setProperty("jdbc.port", dbPort);
                        prop.setProperty("jdbc.username", dbUsername);
                        prop.setProperty("jdbc.password", BlowfishEncryption.encrypt(dbPassword));

                        String homeDir = System.getProperty("user.home");
                        try (FileOutputStream output = new FileOutputStream(homeDir + fileSeparator+"Tafel Kundenverwaltung"+fileSeparator+"DatabaseInfo.properties"))
                        {
                            prop.store(output, "MySQL database information");
                        }

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    Alert alertDone = new Alert(Alert.AlertType.INFORMATION);
                    alertDone.setTitle("Datenbank verbunden");
                    alertDone.setHeaderText("Die Datenbank-Einstellungen wurden erfolgreich übernommen.");
                    alertDone.setContentText("Bitte starten Sie nun das Kundenverwaltungsprogramm neu.");
                    alertDone.showAndWait();

                    Stage stageDbSettings = (Stage) buttonCheckAndSetConnection.getScene().getWindow();
                    stageDbSettings.close();
                    Platform.exit();

                } else
                {
                    Stage stageDbSettings = (Stage) buttonCheckAndSetConnection.getScene().getWindow();
                    stageDbSettings.close();
                }
            } else
            {
                throw new SQLException("Verbindung konnte nicht hergestellt werden.");
            }
        } catch (SQLException e)
        {
            Alert alertError = new Alert(Alert.AlertType.WARNING);
            alertError.setTitle("Fehler bei der Verbindung!");
            alertError.setHeaderText("Achtung, die Datenbank-Einstellungen sind möglicherweise fehlerhaft!");
            alertError.setContentText("Es kann keine Verbindung zum SQL-Server hergestellt werden. Bitte prüfen Sie die Einstellungen oder Ihren Datenbank-Server.");
            alertError.showAndWait();
        }
    }

    /**
     * Checks the database connection using the stored properties.
     *
     * <p>
     * This method retrieves the database connection details from a properties file, decrypts the stored password, and attempts
     * to establish a connection via JDBC. If the password decryption fails or the connection cannot be established,
     * the method returns <code>false</code>.
     * </p>
     *
     * @return <code>true</code> if the database connection is valid and open; <code>false</code> otherwise.
     */
    public boolean checkDatabaseConnection()
    {
        Properties prop = PropertiesFileController.loadDbInfoPropertiesFile();

        String url = "jdbc:mariadb://" + prop.getProperty("jdbc.hostname") + ":" + prop.getProperty("jdbc.port") + "/" + prop.getProperty("jdbc.dbName");
        String user = prop.getProperty("jdbc.username");

        String encryptedPassword = prop.getProperty("jdbc.password");
        String password = BlowfishEncryption.decrypt(encryptedPassword);

        // Wenn die Entschlüsselung fehlschlägt, gib false zurück
        if (password == null)
        {
            System.err.println("Fehler bei der Entschlüsselung des Passworts. Verbindungsprüfung fehlgeschlagen.");
            return false;
        }

        try (Connection connection = DriverManager.getConnection(url, user, password))
        {
            return connection != null && !connection.isClosed();
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * Closes the database connection settings window.
     */
    @FXML
    public void einstellungenAnwendenAbbrechen()
    {
        Stage stageDbSettings = (Stage) buttonCheckAndSetConnection.getScene().getWindow();
        stageDbSettings.close();
    }



    /**
     * Set the correct font size.
     *
     * @param fontSize
     */
    public void setFontSize(Double fontSize)
    {
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);

        ArrayList<Label> labelArrayList = new ArrayList<>(
                Arrays.asList(labelHeader, labelServerName, labelPort, labelLogin, labelPassword,
                        labelDatabase));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        ArrayList<TextField> textFieldArrayList = new ArrayList<>(
                Arrays.asList(txtServername, txtPort, txtUser, txtPasswort, txtDatenbank));
        changeFontSize.changeFontSizeFromTextFieldArrayList(textFieldArrayList, newFontSize);

        ArrayList<Button> buttonArrayList =
                new ArrayList<>(Arrays.asList(buttonCancel, buttonCheckAndSetConnection));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);
    }
}
