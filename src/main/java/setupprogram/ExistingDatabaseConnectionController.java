package setupprogram;

import static kundenverwaltung.toolsandworkarounds.PropertiesFileController.PROPERTIES_LOCATION_DATABASE_INFO;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kundenverwaltung.toolsandworkarounds.BlowfishEncryption;
import kundenverwaltung.toolsandworkarounds.CustomPropertiesStore;

/**
 * Contains a Method to connect an existing database from the current version of the "Tafel" customer administration software.
 *
 * @author Robin Becker
 * @version 1.0
 * @Date 16.08.2018
 */
public class ExistingDatabaseConnectionController
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
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void btnConnectDatabase()
    {

        try
        {
            String dbAddress = txtDbAddress.getText();
            String dbPort = txtDbPort.getText();
            String dbUsername = txtDbUser.getText();
            String dbPassword = txtDbPassword.getText();
            String dbName = txtDbName.getText();
            Connection checkCon = DriverManager.getConnection(
                "jdbc:mariadb://" + dbAddress + ":" + dbPort + "/"
            +
                            dbName, dbUsername, dbPassword);

            if (checkCon != null)
            {
                Alert alertPositiv = new Alert(Alert.AlertType.CONFIRMATION);
                alertPositiv.setTitle("Datenbank-Verbindung");
                alertPositiv.setHeaderText("Die Datenbank Einstellungen wurden geprüft.");
                alertPositiv.setContentText(
                        "Die Verbindung zur Datenbank \"" + dbName + "\" wurde erfolgreich getestet. Bestätigen Sie mit \"OK\", wenn Sie"
                +
                                " die Einstellungen "
                            +
                                "übernehmen wollen.");
                Optional<ButtonType> result = alertPositiv.showAndWait();

                if (result.get() == ButtonType.OK)
                {

                    try
                    {

                        CustomPropertiesStore prop = CustomPropertiesStore.loadDatabasePropertiesFileCustomStore();

                        prop.setProperty("jdbc.hostname", dbAddress);
                        prop.setProperty("jdbc.port", dbPort);
                        prop.setProperty("jdbc.username", dbUsername);
                        prop.setProperty("jdbc.password", BlowfishEncryption.encrypt(dbPassword));
                        prop.setProperty("jdbc.dbName", dbName);

                        FileOutputStream output =
                                new FileOutputStream(PROPERTIES_LOCATION_DATABASE_INFO);
                        prop.store(output, "MySQL database information");
                        output.close();
                        ckbxConnectionOkay.setVisible(true);
                        ckbxConnectionOkay.setSelected(true);
                        ckbxConnectionOkay.setStyle("-fx-color: green");
                        ckbxConnectionOkay.setText("Datenbank-Server verbunden");
                        btnFinish.setDisable(false);

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }
            checkCon.close();

        } catch (SQLException e)
        {
            Alert alertNegativ = new Alert(Alert.AlertType.WARNING);
            alertNegativ.setTitle("Fehler bei der Verbindung!");
            alertNegativ.setHeaderText(
                    "Achtung, die Datenbank-Einstellungen sind möglicherweise fehlerhaft!");
            alertNegativ.setContentText(
                    "Es kann keine Verbindung zum SQL-Server hergestellt werden. Bitte prüfen Sie"
            +
                            " die Einstellungen oder Ihren Datenbank-Server.");

            alertNegativ.showAndWait();
            ckbxConnectionOkay.setVisible(true);
            ckbxConnectionOkay.setSelected(false);
            ckbxConnectionOkay.setStyle("-fx-color: red");
            ckbxConnectionOkay.setText("Datenbank-Server nicht verbunden");
            e.printStackTrace();
        }

    }

    /**
     *.
     */
    @FXML
    void returnToOptionMenu(ActionEvent event)
    {

        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        currentStage.close();
        WelcomeScreenController.getChooseDatabaseOptionStg().show();
    }
    /**
     *.
     */
    @FXML
    void finishSetup(ActionEvent event)
    {
        Platform.exit();
    }
}


