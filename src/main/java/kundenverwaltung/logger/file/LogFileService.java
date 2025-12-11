package kundenverwaltung.logger.file;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kundenverwaltung.toolsandworkarounds.CustomPropertiesStore;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static kundenverwaltung.toolsandworkarounds.PropertiesFileController.PROPERTIES_LOCATION_TAFEL_INFO;
import static kundenverwaltung.toolsandworkarounds.PropertiesFileController.loadTafelInfoPropertiesFile;

public class LogFileService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LogFileService.class);
    private static final String HOME_DIR = System.getProperty("user.home");
    private static final int DEFAULT_DELETION_DAYS = 3;

    public static final Path DEFAULT_LOG_DIRECTORY_PATH = Paths.get(
            System.getProperty("user.home"),
            "Tafel Kundenverwaltung",
            "logs"
    );

    /**
     * Sets up the logging environment for a specific user.
     * This includes setting system properties for the user's name and ID,
     * configuring the encryption key for log messages, and cleaning up old log files.
     *
     * @param userName       The username of the person who is logged in.
     * @param userIdAsString The user's ID as a String.
     */
    public static void setupUserLogFileHandling(String userName, String userIdAsString)
    {
        userName = userName.toLowerCase();
        System.setProperty("log.user_name", userName);
        System.setProperty("log.user_id_as_string", userIdAsString);

        String userDefinedSecretKey = userIdAsString + "_" + userName;
        LogMessageCipher.setSecretKey(userDefinedSecretKey);

        //  Auto delete if necessary
        int deleteIntervalInDays = getDeleteIntervalInDays();
        LogFileService.deleteOldLogFiles(deleteIntervalInDays);

        reloadLogbackConfiguration();
    }

    /**
     * Loads the log directory path from the application's properties file.
     * If the path is not defined in the properties, it falls back to a default directory.
     * It also ensures the log directory exists.
     *
     * @throws IOException if an I/O error occurs when creating the directory.
     */
    public static void loadLogDirectoryPath() throws IOException
    {
        System.setProperty("log.path", DEFAULT_LOG_DIRECTORY_PATH.toAbsolutePath().toString());
        System.setProperty("log.user_name", "default");
        System.setProperty("log.user_id_as_string", "-1");

        Files.createDirectories(Paths.get(DEFAULT_LOG_DIRECTORY_PATH.toAbsolutePath().toString()).toAbsolutePath());

        if (!Files.exists(Paths.get(PROPERTIES_LOCATION_TAFEL_INFO)))
        {
            return;
        }

        Properties properties = PropertiesFileController.loadTafelInfoPropertiesFile();
        String directoryPath = properties.getProperty("tafel.protokoll.directoryPath");

        //  Set back to default if no property-entry found
        if (directoryPath == null)
        {
            return;
        }

        System.setProperty("log.path", directoryPath);

        reloadLogbackConfiguration();
    }

    /**
     * Updates the directory path where log files are stored.
     * This method updates the application's properties file with the new path
     * and reloads the logging configuration to apply the change.
     *
     * @param directoryPath The new absolute path for the log directory.
     * @throws Exception if there is an error creating the directory or updating the properties file.
     */
    public static void updateDirectoryLogPath(String directoryPath) throws Exception
    {
        Path path = Paths.get(directoryPath).toAbsolutePath();
        Files.createDirectories(path);

        CustomPropertiesStore properties = CustomPropertiesStore.loadTafelInfoPropertiesFileCustomStore();

        //  Escape backslashes needing to added manually...
        String unixStylePath = path.toString().replace("\\", "/");
        properties.setProperty("tafel.protokoll.directoryPath", unixStylePath);

        try (OutputStream output = Files.newOutputStream(Paths.get(PROPERTIES_LOCATION_TAFEL_INFO)))
        {
            properties.store(output, "Tafel information");
        }

        System.setProperty("log.path", path.toString());

        //  To make sure after directory change, it does not use the default.log-file!
        String userName = System.getProperty("log.user_name");
        String userIdAsString = System.getProperty("log.user_id_as_string");

        if (userName == null)
        {
            System.setProperty("log.user_name", "default");
            System.setProperty("log.user_id_as_string", "-1");
        }

        if (userName != null && userIdAsString != null)
        {
            //  Call again to make sure it sets the correct-file-pattern for the new directory-path!
            setupUserLogFileHandling(userName, userIdAsString);
            return;
        }

        reloadLogbackConfiguration();
    }

    /**
     * Reads a log file, decrypts its content, and returns the messages as an array of strings.
     *
     * @param exactLogFilePath The absolute path to the .log file.
     * @return An array of decrypted log messages. Returns a fallback message if the file cannot be read.
     */
    public static String[] getDecryptedMessagesByFile(String exactLogFilePath)
    {
        try
        {
            String[] fallbackMessage = new String[]{"[Keine Logdatei gefunden]"};

            if (exactLogFilePath == null || exactLogFilePath.isBlank())
            {
                return fallbackMessage;
            }

            Path logFile = Paths.get(exactLogFilePath);

            if (!exactLogFilePath.toLowerCase().endsWith(".log"))
            {
                return fallbackMessage;
            }

            if (!Files.exists(logFile))
            {
                return fallbackMessage;
            }

            List<String> lines = Files.readAllLines(logFile);
            List<String> decryptedMessages = new ArrayList<>();
            boolean useDefaultEncryptionKey = exactLogFilePath.toLowerCase().endsWith("default.log");

            for (String line : lines)
            {
                if (!line.trim().isEmpty())
                {
                    try
                    {
                        decryptedMessages.add(LogMessageCipher.decryptMessage(line, useDefaultEncryptionKey));
                    }
                    catch (Exception e)
                    {
                        decryptedMessages.add("[Fehler beim Entschlüsseln: " + e.getMessage() + "]");
                    }
                }
            }

            return decryptedMessages.isEmpty() ? fallbackMessage : decryptedMessages.toArray(new String[0]);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Fehler beim Lesen der Logdatei: " + exactLogFilePath, e);
        }
    }

    /**
     * Reloads the Logback logging configuration.
     * This is necessary to apply changes to system properties that affect logging,
     * such as the log file path or user-specific information.
     */
    public static void reloadLogbackConfiguration()
    {
        try
        {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            context.reset();

            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);

            // Load logback.xml config again...
            configurator.doConfigure(
                    Objects.requireNonNull(
                            LogFileService.class.getClassLoader().getResource("logback.xml")
                    )
            );
        }
        catch (Exception e)
        {
            System.err.println("Error while reloading the logback.xml file!\n" + e.getMessage());
        }
    }

    /**
     * Deletes log files that are older than a specified number of days.
     *
     * @param daysThreshold The maximum age of log files in days before they are deleted.
     */
    public static void deleteOldLogFiles(int daysThreshold)
    {
        final String logFilePattern = "_default.log";
        String userName = System.getProperty("log.user_name");
        String userFilePattern = (userName == null) ? logFilePattern : "_" + userName + ".log";

        String logDirectory = getLogDirectoryPath();

        try
        {
            Files.list(Paths.get(logDirectory))
                    .filter(path ->
                    {
                        String filename = path.getFileName().toString();
                        return filename.contains(logFilePattern) || filename.contains(userFilePattern);
                    })
                    .filter(path ->
                    {
                        try
                        {
                            FileTime lastModified = Files.getLastModifiedTime(path);
                            long daysOld = Duration.between(lastModified.toInstant(), Instant.now()).toDays();
                            return daysOld > daysThreshold;
                        }
                        catch (IOException e)
                        {
                            LOGGER.error("Error while checking file: {} - {}", path.getFileName(), e.getMessage());
                            return false;
                        }
                    })
                    .forEach(path ->
                    {
                        try
                        {
                            Files.delete(path);
                            LOGGER.info("Deleted log file: {}", path.getFileName());
                        }
                        catch (IOException e)
                        {
                            LOGGER.error("Error while deleting file: {} - {}", path.getFileName(), e.getMessage());
                        }
                    });
        }
        catch (IOException e)
        {
            LOGGER.error("Error while reading the log-directory files: {}", e.getMessage());
        }
    }

    /**
     * Retrieves a list of available log file names for the currently logged-in user.
     * The list is sorted by modification date, with the newest files first.
     *
     * @return An ObservableList of log file names.
     */
    public static ObservableList<String> getAvailableLogFiles()
    {
        final String logFilePattern = "_default.log";
        String userName = System.getProperty("log.user_name");
        String userFilePattern = (userName == null) ? logFilePattern : "_" + userName + ".log";

        String logDirectory = LogFileService.getLogDirectoryPath();
        ObservableList<String> logFiles = FXCollections.observableArrayList();

        try
        {
            Files.list(Paths.get(logDirectory))
                    .filter(path ->
                    {
                        String filename = path.getFileName().toString();
                        return filename.contains(logFilePattern) || filename.contains(userFilePattern);
                    })
                    .sorted((p1, p2) ->
                    {
                        try
                        {
                            return Files.getLastModifiedTime(p2).compareTo(Files.getLastModifiedTime(p1));
                        }
                        catch (IOException e)
                        {
                            return 0;
                        }
                    })
                    .forEach(path -> logFiles.add(path.getFileName().toString()));
        }
        catch (IOException e)
        {
            LOGGER.error("Error while reading the given log-directory!\n {}", e.getMessage());
        }

        return logFiles;
    }

    /**
     * Gets the configured interval in days for deleting old log files.
     * It reads the value from the application's properties file.
     * If the value is not present or invalid, it returns a default value.
     *
     * @return The log file deletion interval in days.
     */
    public static int getDeleteIntervalInDays()
    {
        if (!Files.exists(Paths.get(PROPERTIES_LOCATION_TAFEL_INFO)))
        {
            return DEFAULT_DELETION_DAYS;
        }

        try
        {
            Properties prop = loadTafelInfoPropertiesFile();
            return Integer.valueOf(prop.getProperty("tafel.protokoll.loeschinterval"));
        }
        catch (Exception ignored)
        {
            return DEFAULT_DELETION_DAYS;
        }
    }

    /**
     * Gets the current directory path for storing log files from the system properties.
     *
     * @return The log directory path as a String.
     */
    public static String getLogDirectoryPath()
    {
        return System.getProperty("log.path");
    }
}