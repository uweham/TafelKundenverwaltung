package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import kundenverwaltung.controller.admintool.BenutzerverwaltungController;

@ExtendWith(ApplicationExtension.class)
public class BenutzerverwaltungControllerGUITest {


    public static class TestController extends BenutzerverwaltungController {
        @Override
        public void datenLaden() {
        }
    }

    @Start
    private void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/admintool/Benutzerverwaltung.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden im Ressourcenordner 'kundenverwaltung/fxml/admintool'");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        loader.setControllerFactory(type -> {
            if (type == BenutzerverwaltungController.class) {
                return new TestController();
            }
            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#comboBenutzer").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#btnNew").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnDel").queryAs(Button.class));
        assertNotNull(robot.lookup("#btncopy").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnSave").queryAs(Button.class));
        assertNotNull(robot.lookup("#BtnChange").queryAs(Button.class));
        assertNotNull(robot.lookup("#scrollPane").queryAs(ScrollPane.class));
        assertNotNull(robot.lookup("#anchorpane").queryAs(AnchorPane.class));
    }

    @Test
    @Tag("gui")
    void testComboBoxEmptyOnStart(FxRobot robot) {
        ComboBox<?> combo = robot.lookup("#comboBenutzer").queryAs(ComboBox.class);
        assertEquals(0, combo.getItems().size(), "ComboBox sollte anfangs leer sein");
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot) {
        assertEquals("Neuen Benutzer anlegen",
                robot.lookup("#btnNew").queryAs(Button.class).getText());
        assertEquals("Benutzer löschen",
                robot.lookup("#btnDel").queryAs(Button.class).getText());
        assertEquals("Berechtigungen kopieren",
                robot.lookup("#btncopy").queryAs(Button.class).getText());
        assertEquals("Einstellungen speichern",
                robot.lookup("#btnSave").queryAs(Button.class).getText());
        assertEquals("Benutzerdaten ändern",
                robot.lookup("#BtnChange").queryAs(Button.class).getText());
    }

    @Test
    @Tag("gui")
    void testCheckBoxCount(FxRobot robot) {
        var boxes = robot.lookup(".check-box").queryAllAs(CheckBox.class);
        assertEquals(28, boxes.size(), "Es sollten 28 Berechtigungs-Checkboxen vorhanden sein");
    }

    @Test
    @Tag("gui")
    void testToggleCheckBox(FxRobot robot) {
        CheckBox cb = robot.lookup("#C1").queryAs(CheckBox.class);
        assertFalse(cb.isSelected(), "Checkbox C1 sollte initial nicht ausgewählt sein");
        robot.clickOn(cb);
        assertTrue(cb.isSelected(), "Checkbox C1 sollte nach Klick ausgewählt sein");
    }
}
