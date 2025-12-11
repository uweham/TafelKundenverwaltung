package guitest.setupprogram;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class DatabaseMigrationControllerGUITest {

    private static final String DB_ADDRESS_OLD = "#txtDbAddressOld";
    private static final String DB_PORT_OLD = "#txtDbPortOld";
    private static final String DB_USER_OLD = "#txtDbUserOld";
    private static final String DB_PASSWORD_OLD = "#txtDbPasswordOld";
    private static final String DB_NAME_OLD = "#txtDbNameOld";
    private static final String DB_ADDRESS_NEW = "#txtDbAddressNew";
    private static final String DB_PORT_NEW = "#txtDbPortNew";
    private static final String DB_USER_NEW = "#txtDbUserNew";
    private static final String DB_PASSWORD_NEW = "#txtDbPasswordNew";
    private static final String DB_NAME_NEW = "#txtDbNameNew";
    private static final String CHECK_CONNECT_BUTTON = "#btnCheckConnect";
    private static final String CREATE_DB_BUTTON = "#btnCreateDatabase";
    private static final String START_MIGRATION_BUTTON = "#btnStartMigration";
    private static final String FINISH_BUTTON = "#btnFinish";
    private static final String BACK_BUTTON = "#btnBack";
    private static final String OLD_DB_CHECKBOX = "#ckbxOldDb";
    private static final String NEW_DB_CHECKBOX = "#ckbxNewDb";
    private static final String MIGRATE_CHECKBOX = "#ckbxMigrate";
    private static final String DB_CREATE_CHECKBOX = "#ckbxDbCreate";

    @Start
    private void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource("/setupprogram/fxml/DatabaseMigration.fxml");
        assertNotNull(fxmlUrl, "FXML 'DatabaseMigration.fxml' nicht gefunden. Ressourcenpfad prüfen!");

        Parent root = FXMLLoader.load(fxmlUrl);
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
        stage.toFront();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup(DB_ADDRESS_OLD).queryAs(TextField.class), "DB Address Old TextField fehlt");
        assertNotNull(robot.lookup(DB_PORT_OLD).queryAs(TextField.class), "DB Port Old TextField fehlt");
        assertNotNull(robot.lookup(DB_USER_OLD).queryAs(TextField.class), "DB User Old TextField fehlt");
        assertNotNull(robot.lookup(DB_PASSWORD_OLD).queryAs(PasswordField.class), "DB Password Old PasswordField fehlt");
        assertNotNull(robot.lookup(DB_NAME_OLD).queryAs(TextField.class), "DB Name Old TextField fehlt");
        assertNotNull(robot.lookup(DB_ADDRESS_NEW).queryAs(TextField.class), "DB Address New TextField fehlt");
        assertNotNull(robot.lookup(DB_PORT_NEW).queryAs(TextField.class), "DB Port New TextField fehlt");
        assertNotNull(robot.lookup(DB_USER_NEW).queryAs(TextField.class), "DB User New TextField fehlt");
        assertNotNull(robot.lookup(DB_PASSWORD_NEW).queryAs(PasswordField.class), "DB Password New PasswordField fehlt");
        assertNotNull(robot.lookup(DB_NAME_NEW).queryAs(TextField.class), "DB Name New TextField fehlt");

        assertNotNull(robot.lookup(CHECK_CONNECT_BUTTON).queryAs(Button.class), "Check Connect Button fehlt");
        assertNotNull(robot.lookup(CREATE_DB_BUTTON).queryAs(Button.class), "Create DB Button fehlt");
        assertNotNull(robot.lookup(START_MIGRATION_BUTTON).queryAs(Button.class), "Start Migration Button fehlt");
        assertNotNull(robot.lookup(FINISH_BUTTON).queryAs(Button.class), "Finish Button fehlt");
        assertNotNull(robot.lookup(BACK_BUTTON).queryAs(Button.class), "Back Button fehlt");

        assertNotNull(robot.lookup("Datenmigration").queryAs(Text.class), "Title Text fehlt");
        assertNotNull(robot.lookup("Hiermit werden die Datenbank-Daten aus dem alten Tafel-Kundenverwaltungsprogramm in die neue benötigte Datenbank übernommen.").queryAs(Text.class), "Description Text fehlt");
        assertNotNull(robot.lookup("Verbindungsdaten").queryAs(Text.class), "Connection Data Text fehlt");
        assertNotNull(robot.lookup("alte Datenbank").queryAs(Text.class), "Old Database Text fehlt");
        assertNotNull(robot.lookup("neue Datenbank").queryAs(Text.class), "New Database Text fehlt");
    }


    @Test
    @Tag("gui")
    void testCheckboxInitialStates(FxRobot robot) {
        CheckBox oldDbCheckbox = robot.lookup(OLD_DB_CHECKBOX).queryAs(CheckBox.class);
        CheckBox newDbCheckbox = robot.lookup(NEW_DB_CHECKBOX).queryAs(CheckBox.class);
        CheckBox migrateCheckbox = robot.lookup(MIGRATE_CHECKBOX).queryAs(CheckBox.class);
        CheckBox dbCreateCheckbox = robot.lookup(DB_CREATE_CHECKBOX).queryAs(CheckBox.class);

        assertTrue(oldDbCheckbox.isDisabled(), "Old DB Checkbox sollte initial deaktiviert sein");
        assertTrue(newDbCheckbox.isDisabled(), "New DB Checkbox sollte initial deaktiviert sein");
        assertTrue(migrateCheckbox.isDisabled(), "Migrate Checkbox sollte initial deaktiviert sein");
        assertTrue(dbCreateCheckbox.isDisabled(), "DB Create Checkbox sollte initial deaktiviert sein");

        assertFalse(oldDbCheckbox.isVisible(), "Old DB Checkbox sollte initial unsichtbar sein");
        assertFalse(newDbCheckbox.isVisible(), "New DB Checkbox sollte initial unsichtbar sein");
        assertFalse(migrateCheckbox.isVisible(), "Migrate Checkbox sollte initial unsichtbar sein");
        assertFalse(dbCreateCheckbox.isVisible(), "DB Create Checkbox sollte initial unsichtbar sein");
    }

    @Test
    @Tag("gui")
    void testTextFieldPrompts(FxRobot robot) {
        TextField dbAddressOld = robot.lookup(DB_ADDRESS_OLD).queryAs(TextField.class);
        TextField dbPortOld = robot.lookup(DB_PORT_OLD).queryAs(TextField.class);
        TextField dbAddressNew = robot.lookup(DB_ADDRESS_NEW).queryAs(TextField.class);
        TextField dbPortNew = robot.lookup(DB_PORT_NEW).queryAs(TextField.class);

        assertEquals("z.B. 127.0.0.1", dbAddressOld.getPromptText());
        assertEquals("z.B. 3306", dbPortOld.getPromptText());
        assertEquals("z.B. 127.0.0.1", dbAddressNew.getPromptText());
        assertEquals("z.B. 3306", dbPortNew.getPromptText());
    }

    @Test
    @Tag("gui")
    void testButtonTexts(FxRobot robot) {
        Button checkConnectButton = robot.lookup(CHECK_CONNECT_BUTTON).queryAs(Button.class);
        Button createDbButton = robot.lookup(CREATE_DB_BUTTON).queryAs(Button.class);
        Button startMigrationButton = robot.lookup(START_MIGRATION_BUTTON).queryAs(Button.class);
        Button finishButton = robot.lookup(FINISH_BUTTON).queryAs(Button.class);
        Button backButton = robot.lookup(BACK_BUTTON).queryAs(Button.class);

        assertEquals("Verbindungen prüfen", checkConnectButton.getText());
        assertEquals("Datenbank erstellen", createDbButton.getText());
        assertEquals("Migration starten", startMigrationButton.getText());
        assertEquals("Fertigstellen", finishButton.getText());
        assertEquals("Zurück", backButton.getText());
    }


    @Test
    @Tag("gui")
    void testTextFieldEditableStates(FxRobot robot) {
        TextField dbAddressOld = robot.lookup(DB_ADDRESS_OLD).queryAs(TextField.class);
        TextField dbPortOld = robot.lookup(DB_PORT_OLD).queryAs(TextField.class);
        TextField dbUserOld = robot.lookup(DB_USER_OLD).queryAs(TextField.class);
        PasswordField dbPasswordOld = robot.lookup(DB_PASSWORD_OLD).queryAs(PasswordField.class);

        assertTrue(dbAddressOld.isEditable(), "DB Address Old sollte initial editierbar sein");
        assertTrue(dbPortOld.isEditable(), "DB Port Old sollte initial editierbar sein");
        assertTrue(dbUserOld.isEditable(), "DB User Old sollte initial editierbar sein");
        assertTrue(dbPasswordOld.isEditable(), "DB Password Old sollte initial editierbar sein");
    }

    @Test
    @Tag("gui")
    void testStepTextsExist(FxRobot robot) {
        assertNotNull(robot.lookup("1. Datenbank-Verbindungen prüfen").queryAs(Text.class), "Step 1 Text fehlt");
        assertNotNull(robot.lookup("2. Name der neuen Datenbank:").queryAs(Text.class), "Step 2 Text fehlt");
        assertNotNull(robot.lookup("3. leere Datenbank-Struktur erstellen").queryAs(Text.class), "Step 3 Text fehlt");
        assertNotNull(robot.lookup("4. Daten-Migration starten").queryAs(Text.class), "Step 4 Text fehlt");
    }

    @Test
    @Tag("gui")
    void testDatabaseLabelTexts(FxRobot robot) {
        assertNotNull(robot.lookup("Server-Adresse:").queryAs(Text.class), "Server Address Label fehlt");
        assertNotNull(robot.lookup("Server Port:").queryAs(Text.class), "Server Port Label fehlt");
        assertNotNull(robot.lookup("Datebank Benutzername:").queryAs(Text.class), "DB Username Label fehlt");
        assertNotNull(robot.lookup("Datebank Password:").queryAs(Text.class), "DB Password Label fehlt");
        assertNotNull(robot.lookup("Datebank Name:").queryAs(Text.class), "DB Name Label fehlt");
    }
}