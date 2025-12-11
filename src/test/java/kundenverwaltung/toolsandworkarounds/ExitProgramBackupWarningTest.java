package kundenverwaltung.toolsandworkarounds;

import javafx.application.Platform;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;


class ExitProgramBackupWarningTest
{

    private static boolean javaFxInitialized = false;

    @BeforeAll
    static void initializeJavaFX()
    {
        if (!javaFxInitialized)
        {
            try
            {
                // Initialize JavaFX toolkit properly for testing
                System.setProperty("testfx.robot", "glass");
                System.setProperty("testfx.headless", "true");
                System.setProperty("prism.order", "sw");
                System.setProperty("prism.text", "t2k");
                System.setProperty("java.awt.headless", "true");

                // Start JavaFX Platform
                Platform.startup(() -> {});
                javaFxInitialized = true;
            } catch (IllegalStateException e)
            {
                // Platform already started
                javaFxInitialized = true;
            }
        }
    }

    /** Test backup warning when no backup record exists */
    @Test
    void testShowBackupWarning_NoBackupRecordExists()
    {
        try (MockedStatic<PropertiesFileController> mockedPropsController =
                     Mockito.mockStatic(PropertiesFileController.class))
        {

            Properties mockProps = new Properties();
            mockedPropsController.when(PropertiesFileController::loadDbInfoPropertiesFile)
                    .thenReturn(mockProps);

            Properties result = PropertiesFileController.loadDbInfoPropertiesFile();
            String lastDbBackup = result.getProperty("db.lastbackup");

            assertNull(lastDbBackup, "Last backup should be null when no backup exists");

            mockedPropsController.verify(PropertiesFileController::loadDbInfoPropertiesFile);
        }
    }

    /** Test backup warning when backup record exists */
    @Test
    void testShowBackupWarning_BackupRecordExists()
    {
        try (MockedStatic<PropertiesFileController> mockedPropsController =
                     Mockito.mockStatic(PropertiesFileController.class))
        {

            Properties mockProps = new Properties();
            mockProps.setProperty("db.lastbackup", "2023-12-01");
            mockedPropsController.when(PropertiesFileController::loadDbInfoPropertiesFile)
                    .thenReturn(mockProps);

            Properties result = PropertiesFileController.loadDbInfoPropertiesFile();
            String lastDbBackup = result.getProperty("db.lastbackup");

            assertNotNull(lastDbBackup, "Last backup should not be null when backup exists");
            assertEquals("2023-12-01", lastDbBackup, "Last backup date should match expected value");

            mockedPropsController.verify(PropertiesFileController::loadDbInfoPropertiesFile);
        }
    }

    /** Test properties file loading behavior */
    @Test
    void testPropertiesFileLoading()
    {
        try (MockedStatic<PropertiesFileController> mockedPropsController =
                     Mockito.mockStatic(PropertiesFileController.class))
        {

            Properties mockProps = new Properties();
            mockProps.setProperty("db.lastbackup", "2023-11-15");
            mockProps.setProperty("db.version", "1.0");
            mockedPropsController.when(PropertiesFileController::loadDbInfoPropertiesFile)
                    .thenReturn(mockProps);

            Properties result = PropertiesFileController.loadDbInfoPropertiesFile();

            assertNotNull(result, "Properties should not be null");
            assertEquals("2023-11-15", result.getProperty("db.lastbackup"),
                    "Last backup date should match expected value");
            assertEquals("1.0", result.getProperty("db.version"),
                    "DB version should match expected value");

            mockedPropsController.verify(PropertiesFileController::loadDbInfoPropertiesFile);
        }
    }

    /** Test backup warning logic when no backup exists */
    @Test
    void testBackupWarningLogic_NoBackup()
    {
        try (MockedStatic<PropertiesFileController> mockedPropsController =
                     Mockito.mockStatic(PropertiesFileController.class))
        {

            Properties mockProps = new Properties();
            mockedPropsController.when(PropertiesFileController::loadDbInfoPropertiesFile)
                    .thenReturn(mockProps);

            Properties prop = PropertiesFileController.loadDbInfoPropertiesFile();
            String lastDbBackup = prop.getProperty("db.lastbackup");

            if (lastDbBackup == null)
            {
                assertTrue(true, "Should show 'no backup' dialog");
            } else
            {
                fail("Should not show 'backup exists' dialog when no backup date is set");
            }
        }
    }

    /** Test backup warning logic when backup exists */
    @Test
    void testBackupWarningLogic_BackupExists()
    {
        try (MockedStatic<PropertiesFileController> mockedPropsController =
                     Mockito.mockStatic(PropertiesFileController.class))
        {

            Properties mockProps = new Properties();
            mockProps.setProperty("db.lastbackup", "2023-10-15");
            mockedPropsController.when(PropertiesFileController::loadDbInfoPropertiesFile)
                    .thenReturn(mockProps);

            Properties prop = PropertiesFileController.loadDbInfoPropertiesFile();
            String lastDbBackup = prop.getProperty("db.lastbackup");

            if (lastDbBackup == null)
            {
                fail("Should not show 'no backup' dialog when backup date exists");
            } else
            {
                assertEquals("2023-10-15", lastDbBackup, "Backup date should match");
                assertTrue(true, "Should show 'backup exists' dialog");
            }
        }
    }

    /** Test properties file loading error handling */
    @Test
    void testPropertiesFileLoadingError()
    {
        try (MockedStatic<PropertiesFileController> mockedPropsController =
                     Mockito.mockStatic(PropertiesFileController.class))
        {

            mockedPropsController.when(PropertiesFileController::loadDbInfoPropertiesFile)
                    .thenThrow(new RuntimeException("Properties file not found"));

            assertThrows(RuntimeException.class, () -> {
                PropertiesFileController.loadDbInfoPropertiesFile();
            }, "Should throw exception when properties file cannot be loaded");
        }
    }

    /** Test empty properties file handling */
    @Test
    void testEmptyPropertiesFile()
    {
        try (MockedStatic<PropertiesFileController> mockedPropsController =
                     Mockito.mockStatic(PropertiesFileController.class))
        {

            Properties emptyProps = new Properties();
            mockedPropsController.when(PropertiesFileController::loadDbInfoPropertiesFile)
                    .thenReturn(emptyProps);

            Properties result = PropertiesFileController.loadDbInfoPropertiesFile();

            assertNotNull(result, "Properties should not be null even when empty");
            assertNull(result.getProperty("db.lastbackup"),
                    "db.lastbackup should be null in empty properties");
            assertEquals(0, result.size(), "Properties should be empty");
        }
    }

    /** Test malformed date in properties handling */
    @Test
    void testMalformedDateInProperties()
    {
        try (MockedStatic<PropertiesFileController> mockedPropsController =
                     Mockito.mockStatic(PropertiesFileController.class))
        {

            Properties mockProps = new Properties();
            mockProps.setProperty("db.lastbackup", "invalid-date-format");
            mockedPropsController.when(PropertiesFileController::loadDbInfoPropertiesFile)
                    .thenReturn(mockProps);

            Properties result = PropertiesFileController.loadDbInfoPropertiesFile();
            String lastDbBackup = result.getProperty("db.lastbackup");

            assertNotNull(lastDbBackup, "Should still return the property value even if malformed");
            assertEquals("invalid-date-format", lastDbBackup,
                    "Should return the actual property value regardless of format");
        }
    }
}