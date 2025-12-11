package guitest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Properties;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import kundenverwaltung.controller.DeletePersonController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class DeletePersonControllerGUITest {

    private Stage stage;
    private DeletePersonController controller;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/DeletePerson.fxml"));
        Parent root = loader.load();

        controller = loader.getController();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testMainNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeading").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelName").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelBirthday").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelNameInput").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelBirthdayInput").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelDeletedReason").queryAs(Label.class));
        assertNotNull(robot.lookup("#textAreaReasonDeletePerson").queryAs(TextArea.class));
        assertNotNull(robot.lookup("#btnDeletePerson").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnCancelDeletePerson").queryAs(Button.class));
    }


    @Test
    @Tag("gui")
    void testLabelContents(FxRobot robot) {
        assertEquals("Name:", robot.lookup("#labelName").queryAs(Label.class).getText());
        assertEquals("Geburtsdatum:", robot.lookup("#labelBirthday").queryAs(Label.class).getText());
        assertEquals("Grund der Löschung:", robot.lookup("#labelDeletedReason").queryAs(Label.class).getText());
    }

    @Test
    @Tag("gui")
    void testTextAreaExists(FxRobot robot) {
        TextArea reasonTextArea = robot.lookup("#textAreaReasonDeletePerson").queryAs(TextArea.class);
        assertNotNull(reasonTextArea);
    }

    @Test
    @Tag("gui")
    void testButtonContents(FxRobot robot) {
        Button deleteButton = robot.lookup("#btnDeletePerson").queryAs(Button.class);
        Button cancelButton = robot.lookup("#btnCancelDeletePerson").queryAs(Button.class);

        assertEquals("OK", deleteButton.getText());
        assertEquals("Abbrechen", cancelButton.getText());
    }

    @Test
    @Tag("gui")
    void testButtonsVisible(FxRobot robot) {
        Button deleteButton = robot.lookup("#btnDeletePerson").queryAs(Button.class);
        Button cancelButton = robot.lookup("#btnCancelDeletePerson").queryAs(Button.class);

        assertTrue(deleteButton.isVisible());
        assertTrue(cancelButton.isVisible());
    }

    @Test
    @Tag("gui")
    void testTextInputFunctionality(FxRobot robot) {
        TextArea reasonTextArea = robot.lookup("#textAreaReasonDeletePerson").queryAs(TextArea.class);

        robot.clickOn(reasonTextArea).write("Testgrund für das Löschen der Person");

        assertEquals("Testgrund für das Löschen der Person", reasonTextArea.getText());
    }

    @Test
    @Tag("gui")
    void testCancelButtonFunctionality(FxRobot robot) {
        Button cancelButton = robot.lookup("#btnCancelDeletePerson").queryAs(Button.class);
        assertTrue(stage.isShowing());

        robot.clickOn(cancelButton);
    }

    @Test
    @Tag("gui")
    void testUIStructure(FxRobot robot) {
        assertNotNull(robot.lookup(".anchor-pane"));
        assertNotNull(robot.lookup(".border-pane"));

        assertEquals(6, robot.lookup(".label").queryAll().size());
        assertEquals(1, robot.lookup(".text-area").queryAll().size());
    }

    @Test
    @Tag("gui")
    void testFontSizes(FxRobot robot) {
        Label nameLabel = robot.lookup("#labelName").queryAs(Label.class);
        assertTrue(nameLabel.getFont().getSize() > 0);

        Label headerLabel = robot.lookup("#labelHeading").queryAs(Label.class);
        assertEquals(12.0, headerLabel.getFont().getSize(), 0.1);
    }
}