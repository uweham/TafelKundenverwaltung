package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
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
import java.util.Optional;

@ExtendWith(ApplicationExtension.class)
public class OrtsteileControllerGUITest {

    private static final String ID_INP_PLZ = "#inpPlz";
    private static final String ID_INP_ORT = "#inpOrt";
    private static final String ID_BTN_ORT_NEU = "#btnOrtNeu";
    private static final String ID_BTN_ORT_AENDERN = "#btnOrtAendern";
    private static final String ID_COMBO_ORTSTEIL = "#comboOrtsteil";
    private static final String ID_LIST_ORTSTEILE = "#listOrtsteile";
    private static final String ID_BTN_ORTSTEIL_NEU = "#btnOrtsteilNeu";
    private static final String ID_BTN_ORTSTEIL_AENDERN = "#btnOrtsteilAendern";

    private static final String ID_BTN_ORTSTEIL_LOESCHEN_UMLAUT = "#btnOrtsteilL\u00F6schen";
    private static final String ID_BTN_ORTSTEIL_LOESCHEN_ASCII  = "#btnOrtsteilLoeschen";

    private static final String ID_TXT_ORT = "#txtOrt";

    @Start
    @Tag("gui")
    private void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/admintool/Ortsteile.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei 'Ortsteile.fxml' nicht gefunden (prüfe Ressourcenpfad!)");

        Parent root = FXMLLoader.load(fxmlUrl);
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }

    private Button lookupDeleteButton(FxRobot robot) {
        Optional<Button> byUmlaut = robot.lookup(ID_BTN_ORTSTEIL_LOESCHEN_UMLAUT).tryQueryAs(Button.class);
        if (byUmlaut.isPresent()) return byUmlaut.get();

        Optional<Button> byAscii = robot.lookup(ID_BTN_ORTSTEIL_LOESCHEN_ASCII).tryQueryAs(Button.class);
        if (byAscii.isPresent()) return byAscii.get();

        Optional<Button> byText = robot.lookup(n ->
                n instanceof Button && "Ortsteil löschen".equals(((Button) n).getText())
        ).tryQueryAs(Button.class);

        return byText.orElse(null);
    }

    @Test
    @Tag("gui")

    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup(ID_INP_PLZ).queryAs(TextField.class), "TextField 'inpPlz' fehlt");
        assertNotNull(robot.lookup(ID_INP_ORT).queryAs(TextField.class), "TextField 'inpOrt' fehlt");
        assertNotNull(robot.lookup(ID_BTN_ORT_NEU).queryAs(Button.class), "Button 'btnOrtNeu' fehlt");
        assertNotNull(robot.lookup(ID_BTN_ORT_AENDERN).queryAs(Button.class), "Button 'btnOrtAendern' fehlt");
        assertNotNull(robot.lookup(ID_COMBO_ORTSTEIL).queryAs(ComboBox.class), "ComboBox 'comboOrtsteil' fehlt");
        assertNotNull(robot.lookup(ID_LIST_ORTSTEILE).queryAs(ListView.class), "ListView 'listOrtsteile' fehlt");
        assertNotNull(robot.lookup(ID_BTN_ORTSTEIL_NEU).queryAs(Button.class), "Button 'btnOrtsteilNeu' fehlt");
        assertNotNull(robot.lookup(ID_BTN_ORTSTEIL_AENDERN).queryAs(Button.class), "Button 'btnOrtsteilAendern' fehlt");

        Button loeschen = lookupDeleteButton(robot);
        assertNotNull(loeschen, "Button 'Ortsteil löschen' (fx:id mit Umlaut oder ASCII) konnte nicht gefunden werden");
        assertNotNull(robot.lookup(ID_TXT_ORT).queryAs(Text.class), "Text 'txtOrt' fehlt");
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot) {
        Button btnOrtNeu = robot.lookup(ID_BTN_ORT_NEU).queryAs(Button.class);
        Button btnOrtAendern = robot.lookup(ID_BTN_ORT_AENDERN).queryAs(Button.class);
        Button btnOrtsteilNeu = robot.lookup(ID_BTN_ORTSTEIL_NEU).queryAs(Button.class);
        Button btnOrtsteilAendern = robot.lookup(ID_BTN_ORTSTEIL_AENDERN).queryAs(Button.class);
        Button btnOrtsteilLoeschen = lookupDeleteButton(robot);
        assertNotNull(btnOrtsteilLoeschen, "Löschen-Button nicht gefunden");

        assertEquals("neuen Ort hinzufügen", btnOrtNeu.getText());
        assertEquals("Ort ändern", btnOrtAendern.getText());
        assertEquals("Ortsteil hinzufügen", btnOrtsteilNeu.getText());
        assertEquals("Ortsteil ändern", btnOrtsteilAendern.getText());
        assertEquals("Ortsteil löschen", btnOrtsteilLoeschen.getText());
    }

    @Test
    @Tag("gui")
    void testTextFieldsEmptyOnStart(FxRobot robot) {
        TextField plzField = robot.lookup(ID_INP_PLZ).queryAs(TextField.class);
        TextField ortField = robot.lookup(ID_INP_ORT).queryAs(TextField.class);
        assertEquals("", plzField.getText(), "PLZ-Feld sollte anfangs leer sein");
        assertEquals("", ortField.getText(), "Ort-Feld sollte anfangs leer sein");
    }

    @Test
    @Tag("gui")
    void testOrtFieldEditableState(FxRobot robot) {
        TextField ortField = robot.lookup(ID_INP_ORT).queryAs(TextField.class);
        assertFalse(ortField.isEditable(), "Ort-Feld sollte nicht editierbar sein");
    }

    @Test
    @Tag("gui")
    void testTextFieldsInput(FxRobot robot) {
        TextField plzField = robot.lookup(ID_INP_PLZ).queryAs(TextField.class);
        robot.clickOn(ID_INP_PLZ).write("12345");
        assertEquals("12345", plzField.getText(), "PLZ-Feld sollte den eingegebenen Text enthalten");
    }

    @Test
    @Tag("gui")
    void testComboBoxAndListViewEmptyOnStart(FxRobot robot) {
        ComboBox<?> comboOrtsteil = robot.lookup(ID_COMBO_ORTSTEIL).queryAs(ComboBox.class);
        ListView<?> listOrtsteile = robot.lookup(ID_LIST_ORTSTEILE).queryAs(ListView.class);
        assertTrue(comboOrtsteil.getItems().isEmpty(), "ComboBox sollte anfangs leer sein");
        assertTrue(listOrtsteile.getItems().isEmpty(), "ListView sollte anfangs leer sein");
    }

    @Test
    @Tag("gui")
    void testButtonClicks(FxRobot robot) {
        Button btnOrtNeu = robot.lookup(ID_BTN_ORT_NEU).queryAs(Button.class);
        Button btnOrtAendern = robot.lookup(ID_BTN_ORT_AENDERN).queryAs(Button.class);
        Button btnOrtsteilNeu = robot.lookup(ID_BTN_ORTSTEIL_NEU).queryAs(Button.class);
        Button btnOrtsteilAendern = robot.lookup(ID_BTN_ORTSTEIL_AENDERN).queryAs(Button.class);
        Button btnOrtsteilLoeschen = lookupDeleteButton(robot);
        assertNotNull(btnOrtsteilLoeschen, "Löschen-Button nicht gefunden");

        assertDoesNotThrow(() -> robot.clickOn(btnOrtNeu));
        assertDoesNotThrow(() -> robot.clickOn(btnOrtAendern));
        assertDoesNotThrow(() -> robot.clickOn(btnOrtsteilNeu));
        assertDoesNotThrow(() -> robot.clickOn(btnOrtsteilAendern));
        assertDoesNotThrow(() -> robot.clickOn(btnOrtsteilLoeschen));
    }

    @Test
    @Tag("gui")
    void testLayoutPositions(FxRobot robot) {
        TextField plzField = robot.lookup(ID_INP_PLZ).queryAs(TextField.class);
        TextField ortField = robot.lookup(ID_INP_ORT).queryAs(TextField.class);
        Button btnOrtNeu = robot.lookup(ID_BTN_ORT_NEU).queryAs(Button.class);

        assertTrue(plzField.getLayoutY() < ortField.getLayoutY(), "PLZ-Feld sollte über dem Ort-Feld sein");
        assertTrue(plzField.getLayoutX() + plzField.getWidth() < btnOrtNeu.getLayoutX(),
                "Buttons sollten rechts von den TextFields sein");
    }

    @Test
    void testTextContent(FxRobot robot) {
        Text txtOrt = robot.lookup(ID_TXT_ORT).queryAs(Text.class);
        assertNotNull(txtOrt.getText(), "Text sollte initial gesetzt sein");
    }
}
