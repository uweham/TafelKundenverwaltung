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
public class ExistingDatabaseConnectionControllerGUITest {

    private static final String ID_DB_ADDRESS = "#txtDbAddress";
    private static final String ID_DB_PORT = "#txtDbPort";
    private static final String ID_DB_USER = "#txtDbUser";
    private static final String ID_DB_PASSWORD = "#txtDbPassword";
    private static final String ID_DB_NAME = "#txtDbName";
    private static final String ID_CHECK_CONNECT_BUTTON = "#btnCheckConnect";
    private static final String ID_FINISH_BUTTON = "#btnFinish";
    private static final String ID_BACK_BUTTON = "#btnBack";
    private static final String ID_CONNECTION_CHECKBOX = "#ckbxConnectionOkay";

    private static final String TITLE_TEXT = "Datenbank-Verbindung";
    private static final String DESCRIPTION_TEXT = "Wenn Sie bereits das aktuelle Tafel-Kundenverwaltungsprogramm genutzt haben, so geben Sie bitte hier die Verbindungsdaten der verwendeten Datenbank ein. Dadurch wird diese Datenbank wieder mit dem Kundenverwaltungsprogramm verbunden.";
    private static final String CONNECTION_DATA_TEXT = "Verbindungsdaten der bestehenden Datenbank";
    private static final String SERVER_ADDRESS_TEXT = "Server-Adresse:";
    private static final String SERVER_PORT_TEXT = "Server-Port:";
    private static final String DB_USERNAME_TEXT = "Datenbank Benutzername:";
    private static final String DB_PASSWORD_TEXT = "Datenbank Password:";
    private static final String DB_NAME_TEXT = "Datenbank Name:";
    private static final String CHECK_CONNECT_TEXT = "Prüfen und verbinden";
    private static final String FINISH_TEXT = "Fertigstellen";
    private static final String BACK_TEXT = "Zurück";

    @Start
    private void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource("/setupprogram/fxml/ExistingDatabaseConnection.fxml");
        assertNotNull(fxmlUrl, "FXML 'ExistingDatabaseConnection.fxml' nicht gefunden. Ressourcenpfad prüfen!");

        Parent root = FXMLLoader.load(fxmlUrl);
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
        stage.toFront();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup(ID_DB_ADDRESS).queryAs(TextField.class), "Database Address Field fehlt");
        assertNotNull(robot.lookup(ID_DB_PORT).queryAs(TextField.class), "Database Port Field fehlt");
        assertNotNull(robot.lookup(ID_DB_USER).queryAs(TextField.class), "Database User Field fehlt");
        assertNotNull(robot.lookup(ID_DB_PASSWORD).queryAs(PasswordField.class), "Database Password Field fehlt");
        assertNotNull(robot.lookup(ID_DB_NAME).queryAs(TextField.class), "Database Name Field fehlt");

        assertNotNull(robot.lookup(ID_CHECK_CONNECT_BUTTON).queryAs(Button.class), "Check Connect Button fehlt");
        assertNotNull(robot.lookup(ID_FINISH_BUTTON).queryAs(Button.class), "Finish Button fehlt");
        assertNotNull(robot.lookup(ID_BACK_BUTTON).queryAs(Button.class), "Back Button fehlt");

        assertNotNull(robot.lookup(ID_CONNECTION_CHECKBOX).queryAs(CheckBox.class), "Connection Checkbox fehlt");
    }

    @Test
    @Tag("gui")
    void testTextContentAndLabels(FxRobot robot) {
        assertNotNull(robot.lookup(TITLE_TEXT).queryAs(Text.class), "Title Text fehlt");
        assertNotNull(robot.lookup(DESCRIPTION_TEXT).queryAs(Text.class), "Description Text fehlt");
        assertNotNull(robot.lookup(CONNECTION_DATA_TEXT).queryAs(Text.class), "Connection Data Text fehlt");
        assertNotNull(robot.lookup(SERVER_ADDRESS_TEXT).queryAs(Text.class), "Server Address Text fehlt");
        assertNotNull(robot.lookup(SERVER_PORT_TEXT).queryAs(Text.class), "Server Port Text fehlt");
        assertNotNull(robot.lookup(DB_USERNAME_TEXT).queryAs(Text.class), "Database Username Text fehlt");
        assertNotNull(robot.lookup(DB_PASSWORD_TEXT).queryAs(Text.class), "Database Password Text fehlt");
        assertNotNull(robot.lookup(DB_NAME_TEXT).queryAs(Text.class), "Database Name Text fehlt");

        Button checkConnectButton = robot.lookup(ID_CHECK_CONNECT_BUTTON).queryAs(Button.class);
        Button finishButton = robot.lookup(ID_FINISH_BUTTON).queryAs(Button.class);
        Button backButton = robot.lookup(ID_BACK_BUTTON).queryAs(Button.class);

        assertEquals(CHECK_CONNECT_TEXT, checkConnectButton.getText());
        assertEquals(FINISH_TEXT, finishButton.getText());
        assertEquals(BACK_TEXT, backButton.getText());
    }

    @Test
    @Tag("gui")
    void testTextFieldPrompts(FxRobot robot) {
        TextField dbAddressField = robot.lookup(ID_DB_ADDRESS).queryAs(TextField.class);
        TextField dbPortField = robot.lookup(ID_DB_PORT).queryAs(TextField.class);

        assertEquals("z.B. 127.0.0.1", dbAddressField.getPromptText());
        assertEquals("z.B. 3306", dbPortField.getPromptText());
    }

    @Test
    @Tag("gui")
    void testInitialButtonStates(FxRobot robot) {
        Button finishButton = robot.lookup(ID_FINISH_BUTTON).queryAs(Button.class);
        CheckBox connectionCheckbox = robot.lookup(ID_CONNECTION_CHECKBOX).queryAs(CheckBox.class);

        assertTrue(finishButton.isDisabled(), "Finish Button sollte initial deaktiviert sein");

        assertFalse(connectionCheckbox.isVisible(), "Connection Checkbox sollte initial unsichtbar sein");
    }



    @Test
    @Tag("gui")

    void testFormLayout(FxRobot robot) {
        TextField dbAddressField = robot.lookup(ID_DB_ADDRESS).queryAs(TextField.class);
        TextField dbPortField = robot.lookup(ID_DB_PORT).queryAs(TextField.class);
        TextField dbUserField = robot.lookup(ID_DB_USER).queryAs(TextField.class);
        PasswordField dbPasswordField = robot.lookup(ID_DB_PASSWORD).queryAs(PasswordField.class);
        TextField dbNameField = robot.lookup(ID_DB_NAME).queryAs(TextField.class);

        assertTrue(dbAddressField.getLayoutY() < dbPortField.getLayoutY(),
                "Address field should be above Port field");
        assertTrue(dbPortField.getLayoutY() < dbUserField.getLayoutY(),
                "Port field should be above User field");
        assertTrue(dbUserField.getLayoutY() < dbPasswordField.getLayoutY(),
                "User field should be above Password field");
        assertTrue(dbPasswordField.getLayoutY() < dbNameField.getLayoutY(),
                "Password field should be above Name field");
    }

    @Test
    @Tag("gui")
    void testInputValidation(FxRobot robot) {
        TextField dbAddressField = robot.lookup(ID_DB_ADDRESS).queryAs(TextField.class);
        TextField dbPortField = robot.lookup(ID_DB_PORT).queryAs(TextField.class);

        robot.clickOn(dbPortField).write("abc123");
        robot.clickOn(dbAddressField).write("localhost");
        assertEquals("localhost", dbAddressField.getText());
    }
}