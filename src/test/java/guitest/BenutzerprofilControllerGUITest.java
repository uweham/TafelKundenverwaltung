package guitest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import kundenverwaltung.controller.BenutzerprofilController;
import kundenverwaltung.model.User;
import kundenverwaltung.toolsandworkarounds.PasswordEncoding;

import java.net.URL;
import java.time.LocalDate;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class BenutzerprofilControllerGUITest {

    private Stage stage;
    private BenutzerprofilController controller;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/Benutzerprofil.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden im Ressourcenordner 'kundenverwaltung/fxml'");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        controller = loader.getController();

        User testUser = new User(1, "testuser", "Max", "Mustermann",
                LocalDate.of(1990, 1, 1), "hashedPassword", "admin");

        controller.setUser(testUser);
        controller.setUserData(testUser);

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeader").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelUserHeader").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelPasswordHeader").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelSettingsHeader").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelAuthorizationHeader").queryAs(Label.class));

        assertNotNull(robot.lookup("#txtBenutzername").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtVorname").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtNachname").queryAs(TextField.class));

        assertNotNull(robot.lookup("#passwordFieldConfirm").queryAs(PasswordField.class));
        assertNotNull(robot.lookup("#passwordFieldOldPassword").queryAs(PasswordField.class));
        assertNotNull(robot.lookup("#passwordFieldNewPassword").queryAs(PasswordField.class));
        assertNotNull(robot.lookup("#passwordFieldRepeatNewPassword").queryAs(PasswordField.class));

        assertNotNull(robot.lookup("#buttonSaveUserName").queryAs(Button.class));
        assertNotNull(robot.lookup("#buttonSavePassword").queryAs(Button.class));
        assertNotNull(robot.lookup("#buttonSaveSettings").queryAs(Button.class));
        assertNotNull(robot.lookup("#buttonResetSettings").queryAs(Button.class));

        assertNotNull(robot.lookup("#cbxEinstellungenSpeichern").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxMehrerePersonen").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxAmpeldarstellung").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxUmsatzspeicherung").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxPositionierungSpeichern").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxSuchvorgaengeAutomatisch").queryAs(CheckBox.class));

        TabPane tabPane = robot.lookup(".tab-pane").queryAs(TabPane.class);
        assertNotNull(tabPane);
        assertEquals(3, tabPane.getTabs().size());

        assertNotNull(robot.lookup("#lwBerechtigungen").queryAs(ListView.class));
    }

    @Test
    @Tag("gui")
    void testInitialUserDataDisplay(FxRobot robot) {
        TextField usernameField = robot.lookup("#txtBenutzername").queryAs(TextField.class);
        TextField vornameField = robot.lookup("#txtVorname").queryAs(TextField.class);
        TextField nachnameField = robot.lookup("#txtNachname").queryAs(TextField.class);
        Label userRightLabel = robot.lookup("#labelWriteUserRight").queryAs(Label.class);

        assertEquals("testuser", usernameField.getText());
        assertEquals("Max", vornameField.getText());
        assertEquals("Mustermann", nachnameField.getText());
        assertEquals("admin", userRightLabel.getText());
    }

    @Test
    @Tag("gui")
    void testLabelsText(FxRobot robot) {
        Label header = robot.lookup("#labelHeader").queryAs(Label.class);
        Label userHeader = robot.lookup("#labelUserHeader").queryAs(Label.class);
        Label passwordHeader = robot.lookup("#labelPasswordHeader").queryAs(Label.class);
        Label userName = robot.lookup("#labelUserName").queryAs(Label.class);
        Label showFirstName = robot.lookup("#labelShowFirstName").queryAs(Label.class);

        assertEquals("Hier können Sie ihr Benutzerprofil ändern", header.getText());
        assertEquals("Benutzer", userHeader.getText());
        assertEquals("Passwort", passwordHeader.getText());
        assertEquals("Benutzername:", userName.getText());
        assertEquals("Vorname:", showFirstName.getText());
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot) {
        Button saveUserButton = robot.lookup("#buttonSaveUserName").queryAs(Button.class);
        Button savePasswordButton = robot.lookup("#buttonSavePassword").queryAs(Button.class);
        Button saveSettingsButton = robot.lookup("#buttonSaveSettings").queryAs(Button.class);
        Button resetSettingsButton = robot.lookup("#buttonResetSettings").queryAs(Button.class);

        assertEquals("Speichern", saveUserButton.getText());
        assertEquals("Speichern", savePasswordButton.getText());
        assertEquals("Speichern", saveSettingsButton.getText());
        assertEquals("Einstellung zurücksetzen", resetSettingsButton.getText());

        assertFalse(saveUserButton.isDisabled());
        assertFalse(savePasswordButton.isDisabled());
        assertFalse(saveSettingsButton.isDisabled());
        assertFalse(resetSettingsButton.isDisabled());
    }

    @Test
    @Tag("gui")
    void testTextFieldsEditable(FxRobot robot) {
        TextField usernameField = robot.lookup("#txtBenutzername").queryAs(TextField.class);
        TextField vornameField = robot.lookup("#txtVorname").queryAs(TextField.class);
        TextField nachnameField = robot.lookup("#txtNachname").queryAs(TextField.class);

        robot.clickOn(usernameField);
        robot.eraseText(usernameField.getText().length());
        robot.write("newusername");
        assertEquals("newusername", usernameField.getText());

        robot.clickOn(vornameField);
        robot.eraseText(vornameField.getText().length());
        robot.write("NeuerVorname");
        assertEquals("NeuerVorname", vornameField.getText());

        robot.clickOn(nachnameField);
        robot.eraseText(nachnameField.getText().length());
        robot.write("NeuerNachname");
        assertEquals("NeuerNachname", nachnameField.getText());
    }

    @Test
    @Tag("gui")
    void testPasswordFieldsEmptyInitially(FxRobot robot) {
        PasswordField confirmField = robot.lookup("#passwordFieldConfirm").queryAs(PasswordField.class);
        PasswordField oldPasswordField = robot.lookup("#passwordFieldOldPassword").queryAs(PasswordField.class);
        PasswordField newPasswordField = robot.lookup("#passwordFieldNewPassword").queryAs(PasswordField.class);
        PasswordField repeatPasswordField = robot.lookup("#passwordFieldRepeatNewPassword").queryAs(PasswordField.class);

        assertTrue(confirmField.getText().isEmpty());
        assertTrue(oldPasswordField.getText().isEmpty());
        assertTrue(newPasswordField.getText().isEmpty());
        assertTrue(repeatPasswordField.getText().isEmpty());
    }




    @Test
    @Tag("gui")
    void testListViewExists(FxRobot robot) {
        ListView<String> berechtigungen = robot.lookup("#lwBerechtigungen").queryAs(ListView.class);
        assertNotNull(berechtigungen);
    }

    @Test
    @Tag("gui")
    void testUIElementsVisible(FxRobot robot) {
        assertTrue(robot.lookup("#labelHeader").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#txtBenutzername").queryAs(TextField.class).isVisible());
        assertTrue(robot.lookup("#buttonSaveUserName").queryAs(Button.class).isVisible());

        TabPane tabPane = robot.lookup(".tab-pane").queryAs(TabPane.class);
        assertTrue(tabPane.isVisible());
    }

    @Test
    @Tag("gui")
    void testPasswordFieldInput(FxRobot robot) {
        PasswordField newPasswordField = robot.lookup("#passwordFieldNewPassword").queryAs(PasswordField.class);

        robot.clickOn(newPasswordField);
        robot.write("newPassword123!");

        assertEquals("newPassword123!", newPasswordField.getText());
    }

    @Test
    @Tag("gui")
    void testPasswordConfirmationMismatch(FxRobot robot) {
        PasswordField newPasswordField = robot.lookup("#passwordFieldNewPassword").queryAs(PasswordField.class);
        PasswordField repeatPasswordField = robot.lookup("#passwordFieldRepeatNewPassword").queryAs(PasswordField.class);

        robot.clickOn(newPasswordField);
        robot.write("password1");

        robot.clickOn(repeatPasswordField);
        robot.write("password2");

        assertNotEquals(newPasswordField.getText(), repeatPasswordField.getText());
    }
}