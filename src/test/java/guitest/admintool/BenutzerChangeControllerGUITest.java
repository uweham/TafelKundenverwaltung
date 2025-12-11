package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
public class BenutzerChangeControllerGUITest {

    @Start
    public void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource(
                "/kundenverwaltung/fxml/admintool/BenutzerverwaltungChange.fxml"
        );
        assertNotNull(fxmlUrl,
                "FXML nicht gefunden – überprüfe bitte den Pfad unter src/main/resources");

        Parent root = new FXMLLoader(fxmlUrl).load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testAllFieldsPresent(FxRobot robot) {
        assertNotNull(robot.lookup("#Benutzer2").queryAs(TextField.class));
        assertNotNull(robot.lookup("#Anzeige2").queryAs(TextField.class));
        assertNotNull(robot.lookup("#Passwort21").queryAs(PasswordField.class));
        assertNotNull(robot.lookup("#Passwort22").queryAs(PasswordField.class));
        assertNotNull(robot.lookup("#Save2").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testBenutzerFieldNonEditable(FxRobot robot) {
        TextField benutzerField = robot.lookup("#Benutzer2").queryAs(TextField.class);
        assertFalse(benutzerField.isEditable(), "Benutzer-Feld muss nicht editierbar sein");
    }

    @Test
    @Tag("gui")
    void testPasswordFieldsEmptyOnStart(FxRobot robot) {
        PasswordField pw1 = robot.lookup("#Passwort21").queryAs(PasswordField.class);
        PasswordField pw2 = robot.lookup("#Passwort22").queryAs(PasswordField.class);
        assertEquals("", pw1.getText());
        assertEquals("", pw2.getText());
    }

    @Test
    @Tag("gui")
    void testSaveButtonText(FxRobot robot) {
        Button saveButton = robot.lookup("#Save2").queryAs(Button.class);
        assertEquals("Speichern", saveButton.getText());
    }

    @Test
    @Tag("gui")
    void testCanEnterAnzeigeNameAndPasswords(FxRobot robot) {
        // Wait for the UI to be ready
        WaitForAsyncUtils.waitForFxEvents();
        
        TextField anzeigeField = robot.lookup("#Anzeige2").queryAs(TextField.class);
        assertNotNull(anzeigeField);
        
        robot.clickOn("#Anzeige2").write("Max Mustermann");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Max Mustermann", anzeigeField.getText());

        robot.clickOn("#Passwort21").write("secret123");
        robot.clickOn("#Passwort22").write("secret123");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("secret123", robot.lookup("#Passwort21").queryAs(PasswordField.class).getText());
        assertEquals("secret123", robot.lookup("#Passwort22").queryAs(PasswordField.class).getText());
    }
}
