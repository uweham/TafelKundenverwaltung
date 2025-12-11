package setupprogram;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import kundenverwaltung.logger.event.GlobalEventLogger;

/**
 * A menu in which the user can select and start a database setting process.
 *
 * @author Robin Becker
 * @version 1.0
 * @Date 15.08.2018
 */
public class ChooseDatabaseOptionController
{

    @FXML
    private Button btnExit;

    private static Stage databaseSettingsStg;

    public static Stage getDatabaseSettingsStg()
    {
        return databaseSettingsStg;
    }

    /**
     * Starts the configuration GUI to set up a new database and to create the required database structure.
     *
     * @throws IOException
     */
    @FXML
    void newDatabase(ActionEvent event)
    {
        try
        {
            databaseSettingsStg = new Stage();

            Parent root = FXMLLoader.load(getClass().getResource("/setupprogram/fxml/NewDatabaseInstallation.fxml"));
            databaseSettingsStg.setTitle("Neue Datenbank-Verbindung");

            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("NewDatabaseInstallation", scene);
            databaseSettingsStg.setScene(scene);

            databaseSettingsStg.setMinWidth(800);
            databaseSettingsStg.setMinHeight(600);
            databaseSettingsStg.show();
            WelcomeScreenController.getChooseDatabaseOptionStg().close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Starts the configuration GUI to migrate an old database from the previous version of the "Tafel" customer administration software.
     *
     * @throws IOException
     */
    @FXML
    void migrateOldDatabase(ActionEvent event)
    {
        try
        {
            databaseSettingsStg = new Stage();

            Parent root = FXMLLoader.load(getClass().getResource("/setupprogram/fxml/DatabaseMigration.fxml"));
            databaseSettingsStg.setTitle("Datenbank-Migration");

            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("DatabaseMigration", scene);
            databaseSettingsStg.setScene(scene);

            databaseSettingsStg.setMinWidth(800);
            databaseSettingsStg.setMinHeight(600);
            databaseSettingsStg.show();
            WelcomeScreenController.getChooseDatabaseOptionStg().close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Starts the configuration GUI to connect an existing database from the current version of the "Tafel" customer administration software.
     *
     * @throws IOException
     */
    @FXML
    void loadExitingDatabase(ActionEvent event)
    {
        try
        {
            databaseSettingsStg = new Stage();

            Parent root = FXMLLoader.load(getClass().getResource("/setupprogram/fxml/ExistingDatabaseConnection.fxml"));
            databaseSettingsStg.setTitle("Bestehende Datenbank verbinden");

            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("ExistingDatabaseConnection", scene);
            databaseSettingsStg.setScene(scene);

            databaseSettingsStg.setMinWidth(800);
            databaseSettingsStg.setMinHeight(600);
            databaseSettingsStg.show();
            WelcomeScreenController.getChooseDatabaseOptionStg().close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     *.
     */
    @FXML
    void exitSetup(ActionEvent event)
    {
        Platform.exit();
    }

}
