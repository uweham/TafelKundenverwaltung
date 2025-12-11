package guitest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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

import kundenverwaltung.controller.BemerkungenController;
import kundenverwaltung.model.Haushalt;

import java.net.URL;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class BemerkungenControllerGUITest {

    private Stage stage;
    private BemerkungenController controller;

    @Mock
    private Haushalt mockHaushalt;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/Bemerkungen.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden im Ressourcenordner 'kundenverwaltung/fxml'");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        controller = loader.getController();

        mockHaushalt = mock(Haushalt.class);

        when(mockHaushalt.getBemerkungen()).thenReturn("Initiale Bemerkung");

        controller.setzeBemerkung(mockHaushalt);

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeading").queryAs(Label.class));
        assertNotNull(robot.lookup("#txtBMBemerkungen").queryAs(TextArea.class));
        assertNotNull(robot.lookup("#btnSaveComment").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnCancelComment").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testLabelsText(FxRobot robot) {
        Label heading = robot.lookup("#labelHeading").queryAs(Label.class);
        assertEquals("Ändern Sie hier die Bemerkungen zum Kunden", heading.getText());
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot) {
        Button btnSave = robot.lookup("#btnSaveComment").queryAs(Button.class);
        Button btnCancel = robot.lookup("#btnCancelComment").queryAs(Button.class);

        assertEquals("OK", btnSave.getText());
        assertEquals("Abbrechen", btnCancel.getText());

        assertFalse(btnSave.isDisabled());
        assertFalse(btnCancel.isDisabled());
    }

    @Test
    @Tag("gui")
    void testTextAreaInitialContent(FxRobot robot) {
        TextArea textArea = robot.lookup("#txtBMBemerkungen").queryAs(TextArea.class);
        assertEquals("Initiale Bemerkung", textArea.getText());
    }

    @Test
    @Tag("gui")
    void testTextAreaInput(FxRobot robot) {
        TextArea textArea = robot.lookup("#txtBMBemerkungen").queryAs(TextArea.class);

        robot.clickOn(textArea);
        robot.eraseText(textArea.getText().length());
        robot.write("Test-Bemerkung für den Kunden");

        assertEquals("Test-Bemerkung für den Kunden", textArea.getText());
    }

    @Test
    @Tag("gui")
    void testCancelButtonClosesWindow(FxRobot robot) {
        Button btnCancel = robot.lookup("#btnCancelComment").queryAs(Button.class);

        assertNotNull(btnCancel);
        assertTrue(btnCancel.isVisible());

        boolean initiallyShowing = stage.isShowing();

        robot.clickOn(btnCancel);

        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(stage.isShowing());
    }


    @Test
    @Tag("gui")
    void testUIElementsVisible(FxRobot robot) {
        Label heading = robot.lookup("#labelHeading").queryAs(Label.class);
        TextArea textArea = robot.lookup("#txtBMBemerkungen").queryAs(TextArea.class);
        Button btnSave = robot.lookup("#btnSaveComment").queryAs(Button.class);
        Button btnCancel = robot.lookup("#btnCancelComment").queryAs(Button.class);

        assertTrue(heading.isVisible());
        assertTrue(textArea.isVisible());
        assertTrue(btnSave.isVisible());
        assertTrue(btnCancel.isVisible());
    }
}