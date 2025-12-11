package setupprogram;

import static kundenverwaltung.toolsandworkarounds.PropertiesFileController.PROPERTIES_LOCATION_DATABASE_INFO;
import static kundenverwaltung.toolsandworkarounds.PropertiesFileController.PROPERTIES_LOCATION_TAFEL_INFO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kundenverwaltung.logger.event.GlobalEventLogger;
import kundenverwaltung.toolsandworkarounds.CustomPropertiesStore;

/**
 * WelcomeScreenController.
 *
 * @author Robin Becker
 * @version 1.0
 * @Date 15.08.2018
 */
public class WelcomeScreenController
{

    public TextField txtVerteilstelle;
    @FXML private TextField txtTafelName;

    @FXML private Button btnStartSetup;

    @FXML private Button btnExit;

    private static Stage chooseDatabaseOptionStg;
    /**
     * Getter for chooseDatabaseOptionStg.
     *
     * @return the current stage for choosing the database option
     */
    public static Stage getChooseDatabaseOptionStg()
    {
        return chooseDatabaseOptionStg;
    }

    /**
     * Setter for chooseDatabaseOptionStg.
     *
     * @param stage the stage to set
     */
    public static void setChooseDatabaseOptionStg(Stage stage)
    {
        chooseDatabaseOptionStg = stage;
    }

    /**
     * Creates the directory "Tafel Kundenverwaltung" with the .properties file (TafelInfo.properties) in the home directory of the current user.<br>
     *     Then sets the entered String from the textfield as the Tafel name in the TafelInfo.properties and starts the next Stage ChooseDatabaseOption.
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    @FXML void setNameAndStartSetup(ActionEvent event)
    {
        File tafelInfoPropertiesFile = new File(PROPERTIES_LOCATION_TAFEL_INFO);
        File databaseInfoPropertiesFile = new File(PROPERTIES_LOCATION_DATABASE_INFO);
        File propertiesDirectory = new File(tafelInfoPropertiesFile.getParentFile().getAbsolutePath());
        propertiesDirectory.mkdirs();

        try
        {
            tafelInfoPropertiesFile.createNewFile();
            databaseInfoPropertiesFile.createNewFile();
            String tafelName = txtTafelName.getText();
            CustomPropertiesStore prop =
                    CustomPropertiesStore.loadTafelInfoPropertiesFileCustomStore();

            prop.setProperty("tafel.name", tafelName);
            prop.setProperty("tafel.location", String.valueOf(txtVerteilstelle.getText()));

            FileOutputStream output = new FileOutputStream(PROPERTIES_LOCATION_TAFEL_INFO);
            prop.store(output, "Tafel Informationen");
            output.close();

            Parent root = FXMLLoader.load(getClass().getResource("/setupprogram/fxml/ChooseDatabaseOption.fxml"));
            chooseDatabaseOptionStg = new Stage();
            chooseDatabaseOptionStg.setTitle("Datenbank-Einrichtung");

            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("ChooseDatabaseOption", scene);
            chooseDatabaseOptionStg.setScene(scene);

            chooseDatabaseOptionStg.setMinWidth(800);
            chooseDatabaseOptionStg.setMinHeight(600);
            chooseDatabaseOptionStg.show();
            StartSetup.getWelcomeScreenStg().close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    /**
     *.
     */
    @FXML void exitSetup(ActionEvent event)
    {
        Platform.exit();
    }

}
