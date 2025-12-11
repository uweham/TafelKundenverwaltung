package kundenverwaltung.controller.admintool;

import static kundenverwaltung.toolsandworkarounds.PropertiesFileController.HOME_DIR;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import kundenverwaltung.toolsandworkarounds.BlowfishEncryption;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

/**
 * Controller für die Datenbanksicherung.
 */
public class DatabaseBackupController
{
    @FXML
    private TextField txtDbServerEx;

    @FXML
    private TextField txtDbPortEx;

    @FXML
    private TextField txtDbNameEx;

    @FXML
    private TextField txtDbUserEx;

    @FXML
    private TextField txtBackupPath;

    @FXML
    private Button btnChooseBackupFolder;
    @FXML
    private TextField txtMysqldumpPath;

    @FXML
    private Button btnChooseMysqldump;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private LocalDate localDate = LocalDate.now();

    /**
     * Wird aufgerufen, wenn die View initialisiert wird.
     * Lädt DB-Einstellungen und versucht, vorhandene Pfade einzustellen.
     *
     * @throws Exception falls ein Fehler auftritt.
     */
    @FXML
    public void initialize() throws Exception
    {
        Properties prop = PropertiesFileController.loadDbInfoPropertiesFile();
        String dbAddress = prop.getProperty("jdbc.hostname");
        String dbPort = prop.getProperty("jdbc.port");
        String dbUsername = prop.getProperty("jdbc.username");
        String dbName = prop.getProperty("jdbc.dbName");

        txtDbServerEx.setText(dbAddress);
        txtDbPortEx.setText(dbPort);
        txtDbNameEx.setText(dbName);
        txtDbUserEx.setText(dbUsername);

        // Prüfen, ob in den Properties schon ein mysqldump-Pfad gespeichert ist
        String configuredPath = prop.getProperty("mysqldump.path");
        if (configuredPath != null && !configuredPath.trim().isEmpty())
        {
            File f = new File(configuredPath);
            if (f.exists())
            {
                txtMysqldumpPath.setText(configuredPath);
            }
            else
            {
                // Datei existiert nicht mehr => automatisch suchen
                tryAutoFindMysqldump();
            }
        }
        else
        {
            // Nichts konfiguriert => automatisch suchen
            tryAutoFindMysqldump();
        }
    }

    /**
     * Öffnet einen DirectoryChooser, um den Backup-Ordner auszuwählen.
     */
    @FXML
    public void chooseBackupFolder()
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Wählen Sie den Sicherungsordner");
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null)
        {
            txtBackupPath.setText(selectedDirectory.getAbsolutePath());
        }
    }

    /**
     * Öffnet einen FileChooser, um die mysqldump.exe auszuwählen.
     */
    @FXML
    public void chooseMysqldump()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wählen Sie die mysqldump.exe");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Executable Files", "*.exe")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null)
        {
            txtMysqldumpPath.setText(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Versucht, mysqldump.exe in den bekannten Standardverzeichnissen zu finden.
     * Gibt den Pfad zurück oder null, wenn nichts gefunden wurde.
     */
    private static String findMysqldumpAutomatically()
    {
        String[] baseDirs = {
                "C:\\Program Files\\",
                "C:\\Program Files (x86)\\"
        };

        for (String baseDir : baseDirs)
        {
            File base = new File(baseDir);
            if (!base.exists() || !base.isDirectory())
            {
                continue;
            }

            // Alle Unterordner in "C:\Program Files" (bzw. x86) durchsuchen, die mit "MariaDB" anfangen
            File[] mariaDbDirs = base.listFiles((current, name) ->
                    name.startsWith("MariaDB") && new File(current, name).isDirectory()
            );
            if (mariaDbDirs == null)
            {
                continue;
            }

            // Ohne spezielle Sortierung wird die erste gefundene MariaDB-Version genommen
            for (File mariaDbDir : mariaDbDirs)
            {
                File binDir = new File(mariaDbDir, "bin");
                if (!binDir.exists())
                {
                    continue;
                }
                File mysqldumpFile = new File(binDir, "mysqldump.exe");
                if (mysqldumpFile.exists())
                {
                    return mysqldumpFile.getAbsolutePath();
                }
            }
        }

        return null;
    }

    /**
     * Versucht, mysqldump.exe automatisch zu finden und setzt das Textfeld.
     * Falls nichts gefunden wurde, wird eine Meldung angezeigt.
     */
    private void tryAutoFindMysqldump()
    {
        String autoFound = findMysqldumpAutomatically();
        if (autoFound != null)
        {
            txtMysqldumpPath.setText(autoFound);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("mysqldump.exe nicht gefunden");
            alert.setHeaderText("Keine MariaDB-Installation gefunden");
            alert.setContentText("Bitte wählen Sie manuell die mysqldump.exe aus.");
            alert.showAndWait();
        }
    }

    /**
     * Führt das Backup durch.
     *
     * @throws Exception falls ein Fehler auftritt.
     */
    @FXML
    public void backupDatabase() throws Exception
    {
        try
        {
            Properties prop = PropertiesFileController.loadDbInfoPropertiesFile();
            String dbName = prop.getProperty("jdbc.dbName");
            String dbPort = prop.getProperty("jdbc.port");
            String dbHostname = prop.getProperty("jdbc.hostname");
            String dbUsername = prop.getProperty("jdbc.username");
            String encryptedPassword = prop.getProperty("jdbc.password");
            String dbPassword = BlowfishEncryption.decrypt(encryptedPassword);

            String url = "jdbc:mariadb://" + dbHostname + ":" + dbPort + "/" + dbName;

            // Ermittlung des Sicherungsordners: entweder vom Benutzer ausgewählt oder Standard
            String folderPath;
            if (txtBackupPath.getText() != null && !txtBackupPath.getText().trim().isEmpty())
            {
                folderPath = txtBackupPath.getText() + File.separator + "MySQL Backup";
            }
            else
            {
                File propertiesFile = new File(HOME_DIR);
                File propertiesDirectory = new File(propertiesFile.getParentFile().getAbsolutePath());
                propertiesDirectory.mkdirs();
                folderPath = HOME_DIR + File.separator + "Tafel Kundenverwaltung"
                        + File.separator + "MySQL Backup";
            }
            File backupDir = new File(folderPath);
            backupDir.mkdirs();

            String savePath = folderPath + File.separator + "backup("
                    + dtf.format(localDate) + ").sql";

            // Falls kein Pfad zur mysqldump.exe im Textfeld steht, verwenden wir einen Standard
            String mysqldumpPath;
            if (txtMysqldumpPath.getText() != null && !txtMysqldumpPath.getText().trim().isEmpty())
            {
                mysqldumpPath = txtMysqldumpPath.getText();
            }
            else
            {
                mysqldumpPath = "C:\\Program Files\\MariaDB 11.5\\bin\\mysqldump.exe";
            }

            // Kommando für den Export
            String executeCmdExp = String.format(
                    "\"%s\" --host=%s --port=%s --user=%s --password=%s %s --result-file=\"%s\"",
                    mysqldumpPath, dbHostname, dbPort, dbUsername, dbPassword, dbName, savePath
            );
            System.out.println("Executing command: " + executeCmdExp);

            // Verbindung testen
            Connection checkCon = null;
            try
            {
                checkCon = DriverManager.getConnection(url, dbUsername, dbPassword);
                System.out.println("Database connection successful");
            }
            catch (SQLException e)
            {
                System.out.println("Database connection failed: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }

            if (checkCon != null)
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Datenbank-Export/Backup");
                alert.setHeaderText("Sie versuchen die Datenbank zu sichern.");
                alert.setContentText(
                        "Die MySQL Verbindung wurde erfolgreich getestet. "
                                + "Bestätigen Sie mit \"OK\", wenn Sie die Datenbank nun sichern wollen. "
                                + "Der Sicherungsprozess kann einen Moment dauern."
                );
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK)
                {
                    try
                    {
                        Process runtimeProcess = Runtime.getRuntime().exec(executeCmdExp);
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(runtimeProcess.getErrorStream())
                        );
                        String line;
                        while ((line = reader.readLine()) != null)
                        {
                            System.out.println("Backup process output: " + line);
                        }
                        int processComplete = runtimeProcess.waitFor();

                        if (processComplete == 0)
                        {
                            System.out.println("Backup Complete");
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setTitle("Datenbank Sicherung");
                            alert1.setHeaderText(
                                    "Die Datenbank " + dbName + " wurde erfolgreich gesichert."
                            );
                            alert1.setContentText(
                                    "Sie finden die .sql Datei unter: " + savePath + "."
                            );
                            alert1.showAndWait();

                            // Letztes Backup-Datum speichern
                            prop.setProperty("db.lastbackup", dtf.format(localDate));

                            // mysqldump-Pfad in Properties speichern
                            prop.setProperty("mysqldump.path", mysqldumpPath);

                            String homeDir = System.getProperty("user.home");
                            FileOutputStream output = new FileOutputStream(
                                    homeDir + File.separator + "Tafel Kundenverwaltung"
                                            + File.separator + "DatabaseInfo.properties"
                            );
                            prop.store(output, "MySQL database information");
                            output.close();
                        }
                        else
                        {
                            System.out.println("Backup Error");
                            Alert alert2 = new Alert(Alert.AlertType.WARNING);
                            alert2.setTitle("FEHLER: Datenbank Sicherung");
                            alert2.setHeaderText(
                                    "Die ausgewählte Datenbank konnte nicht gesichert werden."
                            );
                            alert2.setContentText(
                                    "Bitte prüfen Sie Ihre Datenbank-Einstellungen."
                            );
                            alert2.showAndWait();
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error during backup process: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                checkCon.close();
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fehler bei der Verbindung!");
            alert.setHeaderText(
                    "Achtung, die Datenbank-Einstellungen sind möglicherweise fehlerhaft!"
            );
            alert.setContentText(
                    "Es kann keine Verbindung zum MySQL-Server hergestellt werden. "
                            + "Bitte prüfen Sie die Einstellungen oder Ihren Datenbank-Server."
            );
            alert.showAndWait();
        }
    }
}
