package kundenverwaltung.controller;

//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kundenverwaltung.toolsandworkarounds.BlowfishEncryption;
import kundenverwaltung.toolsandworkarounds.CustomPropertiesStore;

/**
 * @author Robin Becker
 * @version 1.0
 * @Date 22.07.2018
 */
public class DatabaseConnectionErrorController
{
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
    private void initialize() throws Exception
    {
    CustomPropertiesStore prop = CustomPropertiesStore.loadDatabasePropertiesFileCustomStore();
    String dbName = prop.getProperty("jdbc.dbName");
    String dbPort = prop.getProperty("jdbc.port");
    String dbHostname = prop.getProperty("jdbc.hostname");
    String dbUsername = prop.getProperty("jdbc.username");

    txtDatenbank.setText(dbName);
    txtPort.setText(dbPort);
    txtServername.setText(dbHostname);
    txtUser.setText(dbUsername);

    }

    /**
     * This method is called if there is no connection to the database at program start.
     * It takes the database information and checks the SQL connection with JDBC.<br>
     *     If the connection is successful the database credentials are saved in the corresponding .properties file (database.properties).
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    @FXML private void checkAndSetConnection()
    {

        try
        {
            String dbHostname = txtServername.getText();
            String dbName = txtDatenbank.getText();
            String dbPort = txtPort.getText();
            String dbUsername = txtUser.getText();
            String dbPassword = txtPasswort.getText();
            Connection checkCon = DriverManager.getConnection(
                "jdbc:mariadb://" + dbHostname + ":" + dbPort + "/"
            +
                            dbName, dbUsername, dbPassword);
            checkCon.close();
            if (checkCon != null)
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Datenbank-Einstellungen");
                alert.setHeaderText("Sie versuchen die Datenbank-Einstellungen zu ändern");
                alert.setContentText(
                        "SQL-Verbindung erfolgreich getestet. Bestätigen Sie mit \"OK\", wenn Sie die Einstellungen "
                +
                                "übernehmen wollen.");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK)
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
                        FileOutputStream output =
                                new FileOutputStream(homeDir + "\\Tafel Kundenverwaltung\\DatabaseInfo.properties");
                        prop.store(output, "MySQL database information");
                        output.close();

                        Stage stage = (Stage) txtServername.getScene().getWindow();
                        stage.close();

                        //MainController.getInstance().oeffneAnmeldung();

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                } else
                {
                    Stage stage = (Stage) txtServername.getScene().getWindow();
                    stage.close();
                }
            }

        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fehler bei der Verbindung!");
            alert.setHeaderText(
                    "Achtung, die Datenbank-Einstellungen sind möglicherweise fehlerhaft!");
            alert.setContentText(
                    "Es kann keine Verbindung zum SQL-Server hergestellt werden. Bitte prüfen Sie"
            +
                            " die Einstellungen oder Ihren Datenbank-Server.");

            alert.showAndWait();
        }
    }



    @FXML private void einstellungenAnwendenAbbrechen()
    {
        Stage stage = (Stage) txtServername.getScene().getWindow();
        stage.close();
    }

}
