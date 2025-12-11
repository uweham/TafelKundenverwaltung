package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.net.URL;

@ExtendWith(ApplicationExtension.class)
public class AusgabetagZeitControllerGUITest {

    @Start
    private void start(Stage stage) throws Exception {
        URL fxml = getClass().getResource(
                "/kundenverwaltung/fxml/admintool/AusgabetagZeit.fxml"
        );
        assertNotNull(fxml, "FXML nicht gefunden unter 'kundenverwaltung/fxml/admintool'");
        Parent root = FXMLLoader.load(fxml);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#comboWochentag").queryAs(ComboBox.class),
                "ComboBox für Wochentag fehlt");
        assertNotNull(robot.lookup("#inpStartzeit").queryAs(TextField.class),
                "TextField für Startzeit fehlt");
        assertNotNull(robot.lookup("#inpEndzeit").queryAs(TextField.class),
                "TextField für Endzeit fehlt");
        assertNotNull(robot.lookup("#btnSpeichern").queryAs(Button.class),
                "Speichern-Button fehlt");
        assertNotNull(robot.lookup("#btnAbbrechen").queryAs(Button.class),
                "Abbrechen-Button fehlt");
    }

    @Test
    @Tag("gui")
    void testDefaultTimeValues(FxRobot robot) {
        TextField start = robot.lookup("#inpStartzeit").queryAs(TextField.class);
        TextField end   = robot.lookup("#inpEndzeit").queryAs(TextField.class);

        assertEquals("08:00", start.getText(), "Startzeit sollte standardmäßig '08:00' sein");
        assertEquals("16:00", end.getText(),   "Endzeit sollte standardmäßig '16:00' sein");
    }

    @Test
    @Tag("gui")
    void testButtonTexts(FxRobot robot) {
        Button save   = robot.lookup("#btnSpeichern").queryAs(Button.class);
        Button cancel = robot.lookup("#btnAbbrechen").queryAs(Button.class);

        assertEquals("Speichern", save.getText());
        assertEquals("Abbrechen", cancel.getText());
    }

}
