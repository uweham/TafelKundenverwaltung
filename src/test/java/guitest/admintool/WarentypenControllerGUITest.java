package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import java.net.URL;

@ExtendWith(ApplicationExtension.class)
public class WarentypenControllerGUITest {

    private static final String[] NODE_IDS = {
            "#btnChange", "#btnNew", "#btnSave",
            "#chkActive", "#chkCompute", "#chkLimitierung",
            "#inpErwachsener", "#inpKind", "#inpPauschal", "#inpDeckel",
            "#inpMinimum", "#inpAnzahl", "#inpAbstand",
            "#cbWTyp", "#cbJumpTo", "#cbBuchung", "#cbZuordnung", "#cbAnzahl2"
    };

    private static final String[] TEXTS = {
            "Warentypen & Gebühren",
            "Wählen Sie einen Warentyp zur Bearbeitung aus oder erstellen Sie einen neuen.",
            "Einstellungen für Lebensmittel",
            "Nach der Buchung auf den folgenden Warentyp springen:",
            "Die Eingabe eines Buchungstextes ist",
            "Die Zuordnung einer Person ist",
            "Kunden dürfen diesen Warentyp",
            "mal pro",
            "einkaufen.",
            "Einkauf-Sperrtage (Tage zwischen den Einkäufen):",
            "Gebühr pro Erwachsener (in Euro)",
            "Gebühr pro Kind (in Euro)",
            "Haushaltspauschale",
            "Deckelbetrag",
            "Minimal zu zahlender Betrag",
            "Euro"
    };

    @Start
    private void start(Stage stage) throws Exception {
        String[] paths = {
                "/kundenverwaltung/fxml/admintool/Warentypen.fxml",
                "/fxml/admintool/Warentypen.fxml",
                "/admintool/Warentypen.fxml",
                "/Warentypen.fxml"
        };

        URL fxmlUrl = null;
        for (String path : paths) {
            fxmlUrl = getClass().getResource(path);
            if (fxmlUrl != null) break;
        }

        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden");
        Parent root = FXMLLoader.load(fxmlUrl);
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }

    @Test
    @Tag("gui")
    void testAllNodesExist(FxRobot robot) {
        for (String id : NODE_IDS) {
            assertNotNull(robot.lookup(id).tryQuery().orElse(null), "Element fehlt: " + id);
        }

        for (String text : TEXTS) {
            assertTrue(robot.lookup(text).tryQuery().isPresent(), "Text fehlt: " + text);
        }
    }

    @Test
    @Tag("gui")
    void testButtonProperties(FxRobot robot) {
        Button btnChange = robot.lookup("#btnChange").queryAs(Button.class);
        Button btnNew = robot.lookup("#btnNew").queryAs(Button.class);
        Button btnSave = robot.lookup("#btnSave").queryAs(Button.class);

        assertEquals("gewählten Warentyp umbennenen", btnChange.getText());
        assertEquals("Warentyp erstellen", btnNew.getText());
        assertEquals("speichern", btnSave.getText());
    }

    @Test
    @Tag("gui")
    void testCheckboxInteractions(FxRobot robot) {
        CheckBox chkActive = robot.lookup("#chkActive").queryAs(CheckBox.class);
        CheckBox chkLimitierung = robot.lookup("#chkLimitierung").queryAs(CheckBox.class);

        boolean initialActive = chkActive.isSelected();
        boolean initialLimit = chkLimitierung.isSelected();

        robot.clickOn(chkActive);
        assertNotEquals(initialActive, chkActive.isSelected());

        robot.clickOn(chkLimitierung);
        assertNotEquals(initialLimit, chkLimitierung.isSelected());
    }

    @Test
    @Tag("gui")
    void testTextFieldEditing(FxRobot robot) {
        TextField inpErwachsener = robot.lookup("#inpErwachsener").queryAs(TextField.class);

        robot.clickOn(inpErwachsener);
        robot.eraseText(inpErwachsener.getText().length());
        robot.write("10.50");
        assertEquals("10.50", inpErwachsener.getText());
    }

    @Test
    @Tag("gui")
    void testLimitierungVisibility(FxRobot robot) {
        CheckBox chkLimitierung = robot.lookup("#chkLimitierung").queryAs(CheckBox.class);
        TextField inpAnzahl = robot.lookup("#inpAnzahl").queryAs(TextField.class);

        boolean initialState = chkLimitierung.isSelected();
        assertEquals(initialState, inpAnzahl.isVisible());

        robot.clickOn(chkLimitierung);
        assertNotEquals(initialState, inpAnzahl.isVisible());
    }

    @Test
    @Tag("gui")
    void testComboBoxFunctionality(FxRobot robot) {
        ComboBox<?> cbBuchung = robot.lookup("#cbBuchung").queryAs(ComboBox.class);
        assertTrue(cbBuchung.getItems().size() > 0, "Combobox sollte Items enthalten");
    }
}