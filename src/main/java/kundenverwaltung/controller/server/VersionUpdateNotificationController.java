package kundenverwaltung.controller.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kundenverwaltung.toolsandworkarounds.CustomPropertiesStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static kundenverwaltung.toolsandworkarounds.PropertiesFileController.PROPERTIES_LOCATION_TAFEL_INFO;
import static kundenverwaltung.toolsandworkarounds.PropertiesFileController.loadTafelInfoPropertiesFile;

public class VersionUpdateNotificationController
{

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionUpdateNotificationController.class);

    @FXML
    private Text versionInfo;

    @FXML
    public CheckBox dontShowAgainCheckBox;

    @FXML
    private Button okButton;

    /**
     * Handles the OK button action to close the version update notification.
     * @param event the action event
     */
    @FXML
    private void handleOk(ActionEvent event)
    {
        if (isDontShowAgainSelected())
        {
            LOGGER.info("Benutzer hat gewählt, den Update-Hinweis zukünftig nicht mehr anzuzeigen.");

            try
            {
                showUpdateNotificationWindow(false);
            }
            catch (Exception e)
            {
                LOGGER.error("Fehler beim Speichern der Einstellung für 'nicht mehr anzeigen'", e);
            }
        }

        Stage stage = (Stage) okButton.getScene().getWindow();
        if (stage != null)
        {
            stage.close();
        }
    }

    /**
     * Sets the version information to display.
     * @param currentVersion the current version
     * @param newVersion the new version, or null if no new version is available
     */
    public void setVersionInfo(String currentVersion, String newVersion)
    {
        if (newVersion == null || newVersion.isEmpty())
        {
            versionInfo.setText("Aktuelle Version: " + currentVersion + " (keine neue Version gefunden)");
        }
        else
        {
            versionInfo.setText("Aktuelle Version: " + currentVersion + "   →   Neue Version: " + newVersion);
        }
    }

    /**
     * Returns whether the "don't show again" checkbox is selected.
     * @return true if the checkbox is selected, false otherwise
     */
    public boolean isDontShowAgainSelected()
    {
        return dontShowAgainCheckBox.isSelected();
    }

    public void showUpdateNotificationWindow(boolean bool) throws Exception
    {
        if (!Files.exists(Paths.get(PROPERTIES_LOCATION_TAFEL_INFO)))
        {
            return;
        }

        CustomPropertiesStore prop = CustomPropertiesStore.loadTafelInfoPropertiesFileCustomStore();
        prop.setProperty("tafel.update.notification", String.valueOf(bool));
        FileOutputStream output = new FileOutputStream(PROPERTIES_LOCATION_TAFEL_INFO);
        prop.store(output, "Tafel Informationen");
        output.close();
    }

    public static boolean showUpdateNotificationWindow()
    {
        if (!Files.exists(Paths.get(PROPERTIES_LOCATION_TAFEL_INFO)))
        {
            return true;
        }

        try
        {
            Properties prop = loadTafelInfoPropertiesFile();
            return Boolean.parseBoolean(prop.getProperty("tafel.update.notification"));
        }
        catch (Exception ignored)
        {
            return true;
        }
    }
}



