package kundenverwaltung.toolsandworkarounds;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import kundenverwaltung.logger.event.GlobalEventLogger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import kundenverwaltung.Main;

/**
 * @author Robin Becker
 * @version 1.0
 * @Date 22.07.2018
 */
public class ExitProgramBackupWarning
{

    /**
     * Reads the date string (property key: "db.lastbackup") of the last database backup from
     * DatabaseInfo.properties and creates a confirmation dialog box with the appropriate date hint. <br>
     * The user can choose between: "close program" (buttonExit), "open backup settings"
     * (buttonOpenDB) and "cancel" (buttonCancel)<br>
     * buttonExit: uses Platform.exit() to quit<br>
     * buttonOpenDB: starts the method {@link ExitProgramBackupWarning#openDbSettings()}<br>
     * buttonCancel: closes the confirmation dialog box and the main program continues
     */
    public static void showBackupWarning()
    {
        Properties prop = PropertiesFileController.loadDbInfoPropertiesFile();
        String lastDbBackup = prop.getProperty("db.lastbackup");
        if (lastDbBackup == null)
        {
            Alert alertNoBackup = new Alert(Alert.AlertType.CONFIRMATION);
            alertNoBackup.setTitle("Datenbank Sicherung");
            alertNoBackup.setHeaderText(
                    "Sie versuchen das Programm zu beenden.\nEs wurde noch nie eine Datenbank-Sicherung durchgeführt.\nMöchten Sie jetzt eine Sicherung der Datenbank durchführen?");

            alertNoBackup.setContentText("Bitte wählen Sie:");

            ButtonType buttonExit = new ButtonType("Programm beenden");
            ButtonType buttonOpenDB = new ButtonType("Sicherungseinstellungen öffnen");
            ButtonType buttonCancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

            alertNoBackup.getButtonTypes().setAll(buttonExit, buttonOpenDB, buttonCancel);

            Optional<ButtonType> result = alertNoBackup.showAndWait();
            if (result.get() == buttonExit)
            {
                Platform.exit();
            } else if (result.get() == buttonOpenDB)
            {
                openDbSettings();
            }
        }
        else
        {
            Alert alertBackup = new Alert(Alert.AlertType.CONFIRMATION);
            alertBackup.setTitle("Datenbank Sicherung");
            alertBackup.setHeaderText(
                    "Sie versuchen das Programm zu beenden.\nDie letzte Sicherung wurde am "
            +
                            lastDbBackup
                            +
                            " durchgeführt.\nMöchten Sie jetzt eine Sicherung der Datenbank durchführen?");

            alertBackup.setContentText("Bitte wählen Sie:");

            ButtonType buttonExit = new ButtonType("Programm beenden");
            ButtonType buttonOpenDB = new ButtonType("Sicherungseinstellungen öffnen");
            ButtonType buttonCancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

            alertBackup.getButtonTypes().setAll(buttonExit, buttonOpenDB, buttonCancel);

            Optional<ButtonType> result = alertBackup.showAndWait();
            if (result.get() == buttonExit)
            {
                Platform.exit();
            } else if (result.get() == buttonOpenDB)
            {
                openDbSettings();
            }
        }

    }


    /**
     * Opens the database backup settings menu<br>
     *     The method loads the .fxml file and creates a stage (with correct minimum width and height) with a scene and a pane.
     * @throws IOException
     */
    private static void openDbSettings()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(Main.class
                    .getResource("/Kundenverwaltung/FXML/Admintool/Datenbanksicherung.fxml"));
            AnchorPane pane = loader.load();
            Stage dbSettingsStage = new Stage();

            Scene dbSettingsScene = new Scene(pane);
            GlobalEventLogger.attachTo("Datenbanksicherung", dbSettingsScene);
            dbSettingsStage.setScene(dbSettingsScene);

            dbSettingsStage.setTitle("Datenbank-Wiederherstellung");
            dbSettingsStage.setMinWidth(700);
            dbSettingsStage.setMinHeight(450);
            dbSettingsStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
