package guitest;

import static org.junit.jupiter.api.Assertions.*;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import kundenverwaltung.controller.DatabaseConnectionController;
import kundenverwaltung.controller.DatabaseConnectionErrorController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class DatabaseConnectionControllerGUITest {

    private Stage stage;
    private DatabaseConnectionController controller;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/DatabaseConnectionError.fxml"));
        Parent root = loader.load();

        Object controllerObj = loader.getController();

        if (controllerObj instanceof DatabaseConnectionErrorController) {
            DatabaseConnectionErrorController errorController = (DatabaseConnectionErrorController) controllerObj;
        } else if (controllerObj instanceof DatabaseConnectionController) {
            controller = (DatabaseConnectionController) controllerObj;
        }

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testMainNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeader").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelServerName").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelPort").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelLogin").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelPassword").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelDatabase").queryAs(Label.class));
        assertNotNull(robot.lookup("#txtServername").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtPort").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtUser").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtPasswort").queryAs(PasswordField.class));
        assertNotNull(robot.lookup("#txtDatenbank").queryAs(TextField.class));
        assertNotNull(robot.lookup("#buttonCheckAndSetConnection").queryAs(Button.class));
        assertNotNull(robot.lookup("#buttonCancel").queryAs(Button.class));
    }


    @Test
    @Tag("gui")
    void testLabelContents(FxRobot robot) {
        assertEquals("Server-Adresse:", robot.lookup("#labelServerName").queryAs(Label.class).getText());
        assertEquals("Port:", robot.lookup("#labelPort").queryAs(Label.class).getText());
        assertEquals("Benutzername:", robot.lookup("#labelLogin").queryAs(Label.class).getText());
        assertEquals("Passwort:", robot.lookup("#labelPassword").queryAs(Label.class).getText());
        assertEquals("Datenbank-Name:", robot.lookup("#labelDatabase").queryAs(Label.class).getText());
    }

    @Test
    @Tag("gui")
    void testTextFieldsExist(FxRobot robot) {
        TextField serverField = robot.lookup("#txtServername").queryAs(TextField.class);
        TextField portField = robot.lookup("#txtPort").queryAs(TextField.class);
        TextField userField = robot.lookup("#txtUser").queryAs(TextField.class);
        TextField databaseField = robot.lookup("#txtDatenbank").queryAs(TextField.class);

        assertNotNull(serverField);
        assertNotNull(portField);
        assertNotNull(userField);
        assertNotNull(databaseField);
    }

    @Test
    @Tag("gui")
    void testPasswordFieldExists(FxRobot robot) {
        PasswordField passwordField = robot.lookup("#txtPasswort").queryAs(PasswordField.class);
        assertNotNull(passwordField);
    }

    @Test
    @Tag("gui")
    void testButtonContents(FxRobot robot) {
        Button checkButton = robot.lookup("#buttonCheckAndSetConnection").queryAs(Button.class);
        Button cancelButton = robot.lookup("#buttonCancel").queryAs(Button.class);

        assertEquals("Prüfen und übernehmen", checkButton.getText());
        assertEquals("Abbrechen", cancelButton.getText());
    }

    @Test
    @Tag("gui")
    void testButtonsVisible(FxRobot robot) {
        Button checkButton = robot.lookup("#buttonCheckAndSetConnection").queryAs(Button.class);
        Button cancelButton = robot.lookup("#buttonCancel").queryAs(Button.class);

        assertTrue(checkButton.isVisible());
        assertTrue(cancelButton.isVisible());
    }


    @Test
    @Tag("gui")
    void testCancelButtonFunctionality(FxRobot robot) {
        Button cancelButton = robot.lookup("#buttonCancel").queryAs(Button.class);
        assertTrue(stage.isShowing());

        robot.clickOn(cancelButton);
    }

    @Test
    @Tag("gui")
    void testUIStructure(FxRobot robot) {
        assertNotNull(robot.lookup(".anchor-pane"));

        assertEquals(5, robot.lookup(".text-field").queryAll().size());
        assertEquals(1, robot.lookup(".password-field").queryAll().size());
    }

    @Test
    @Tag("gui")
    void testFieldPositions(FxRobot robot) {
        TextField serverField = robot.lookup("#txtServername").queryAs(TextField.class);
        TextField portField = robot.lookup("#txtPort").queryAs(TextField.class);

        assertTrue(serverField.getLayoutX() < portField.getLayoutX());
    }

    @Test
    @Tag("gui")
    void testFontSizes(FxRobot robot) {
        Label serverLabel = robot.lookup("#labelServerName").queryAs(Label.class);
        assertEquals(14.0, serverLabel.getFont().getSize(), 0.1);

        Label headerLabel = robot.lookup("#labelHeader").queryAs(Label.class);
        assertEquals(15.0, headerLabel.getFont().getSize(), 0.1);
    }

    @Test
    @Tag("gui")
    void testApplyButtonExists(FxRobot robot) {
        Button applyButton = robot.lookup(".button").queryAll()
                .stream()
                .filter(node -> node instanceof Button)
                .map(node -> (Button) node)
                .filter(button -> "Prüfen und übernehmen".equals(button.getText()))
                .findFirst()
                .orElse(null);

        assertNotNull(applyButton);
        assertTrue(applyButton.isVisible());
    }
}