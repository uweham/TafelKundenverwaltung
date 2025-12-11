package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.net.URL;

@ExtendWith(ApplicationExtension.class)
public class NeueVerteilstelleControllerGUITest {

    @Start
    private void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource(
                "/kundenverwaltung/fxml/admintool/NeueVerteilstelle.fxml"
        );
        assertNotNull(fxmlUrl, "FXML-Datei 'NeueVerteilstelle.fxml' nicht gefunden");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(
                robot.lookup("#inpBezeichnung").queryAs(TextField.class),
                "TextField 'inpBezeichnung' sollte vorhanden sein"
        );
        assertNotNull(
                robot.lookup("#inpAdresse").queryAs(TextField.class),
                "TextField 'inpAdresse' sollte vorhanden sein"
        );
        assertNotNull(
                robot.lookup("#txtBezeichnung").queryAs(Text.class),
                "Text 'txtBezeichnung' sollte vorhanden sein"
        );
        assertNotNull(
                robot.lookup("#txtAdresse").queryAs(Text.class),
                "Text 'txtAdresse' sollte vorhanden sein"
        );
        assertNotNull(
                robot.lookup("#btnSpeichern").queryAs(Button.class),
                "Button 'btnSpeichern' sollte vorhanden sein"
        );
        assertNotNull(
                robot.lookup("#btnAbbrechen").queryAs(Button.class),
                "Button 'btnAbbrechen' sollte vorhanden sein"
        );
    }

    @Test
    @Tag("gui")
    void testLabelsText(FxRobot robot) {
        Text txtBezeichnung = robot.lookup("#txtBezeichnung").queryAs(Text.class);
        Text txtAdresse = robot.lookup("#txtAdresse").queryAs(Text.class);
        Button btnSave = robot.lookup("#btnSpeichern").queryAs(Button.class);
        Button btnCancel = robot.lookup("#btnAbbrechen").queryAs(Button.class);

        assertEquals(
                "Bezeichnung",
                txtBezeichnung.getText(),
                "Beschriftung für Bezeichnung stimmt nicht"
        );
        assertEquals(
                "Adresse",
                txtAdresse.getText(),
                "Beschriftung für Adresse stimmt nicht"
        );
        assertEquals(
                "Speichern",
                btnSave.getText(),
                "Beschriftung des Speichern-Buttons stimmt nicht"
        );
        assertEquals(
                "Abbrechen",
                btnCancel.getText(),
                "Beschriftung des Abbrechen-Buttons stimmt nicht"
        );
    }

    @Test
    @Tag("gui")
    void testTextFieldsEmptyOnStart(FxRobot robot) {
        TextField bezeichnungField = robot.lookup("#inpBezeichnung").queryAs(TextField.class);
        TextField adresseField = robot.lookup("#inpAdresse").queryAs(TextField.class);

        assertEquals(
                "",
                bezeichnungField.getText(),
                "Bezeichnungsfeld sollte anfangs leer sein"
        );
        assertEquals(
                "",
                adresseField.getText(),
                "Adressfeld sollte anfangs leer sein"
        );
    }

    @Test
    @Tag("gui")
    void testTextFieldsInput(FxRobot robot) {
        TextField bezeichnungField = robot.lookup("#inpBezeichnung").queryAs(TextField.class);
        TextField adresseField = robot.lookup("#inpAdresse").queryAs(TextField.class);

        robot.clickOn("#inpBezeichnung").write("Test Verteilstelle");
        robot.clickOn("#inpAdresse").write("Teststraße 123");

        assertEquals(
                "Test Verteilstelle",
                bezeichnungField.getText(),
                "Bezeichnungsfeld sollte den eingegebenen Text enthalten"
        );
        assertEquals(
                "Teststraße 123",
                adresseField.getText(),
                "Adressfeld sollte den eingegebenen Text enthalten"
        );
    }



    @Test
    @Tag("gui")
    void testLayoutPositions(FxRobot robot) {
        TextField bezeichnungField = robot.lookup("#inpBezeichnung").queryAs(TextField.class);
        TextField adresseField = robot.lookup("#inpAdresse").queryAs(TextField.class);

        assertTrue(
                bezeichnungField.getLayoutX() < adresseField.getLayoutX(),
                "Bezeichnungsfeld sollte links vom Adressfeld sein"
        );
    }


    @Test
    @Tag("gui")
    void testButtonClicks(FxRobot robot) {
        Button btnSave = robot.lookup("#btnSpeichern").queryAs(Button.class);
        Button btnCancel = robot.lookup("#btnAbbrechen").queryAs(Button.class);

        assertDoesNotThrow(() -> robot.clickOn(btnSave));
        assertDoesNotThrow(() -> robot.clickOn(btnCancel));
    }
}