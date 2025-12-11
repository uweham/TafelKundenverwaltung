package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class NeuerWarentypControllerGUITest {

    @Start
    private void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource(
                "/kundenverwaltung/fxml/admintool/NeuerWarentyp.fxml"
        );
        assertNotNull(fxmlUrl, "FXML-Datei 'NeuerWarentyp.fxml' nicht gefunden");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(
                robot.lookup("#inpName").queryAs(TextField.class),
                "TextField 'inpName' sollte vorhanden sein"
        );
        assertNotNull(
                robot.lookup("#lblName").queryAs(Label.class),
                "Label 'lblName' sollte vorhanden sein"
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
        Label lblName = robot.lookup("#lblName").queryAs(Label.class);
        Button btnSave = robot.lookup("#btnSpeichern").queryAs(Button.class);
        Button btnCancel = robot.lookup("#btnAbbrechen").queryAs(Button.class);

        assertEquals(
                "Warentypname",
                lblName.getText(),
                "Beschriftung des Labels stimmt nicht"
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
    void testTextFieldEmptyOnStart(FxRobot robot) {
        TextField textField = robot.lookup("#inpName").queryAs(TextField.class);
        assertEquals(
                "",
                textField.getText(),
                "Textfeld sollte anfangs leer sein"
        );
    }

    @Test
    @Tag("gui")
    void testTextFieldInput(FxRobot robot) {
        TextField textField = robot.lookup("#inpName").queryAs(TextField.class);

        robot.clickOn("#inpName").write("Test Warentyp");

        assertEquals(
                "Test Warentyp",
                textField.getText(),
                "Textfeld sollte den eingegebenen Text enthalten"
        );
    }


    @Test
    @Tag("gui")
    void testWindowTitle(FxRobot robot) {
        Stage stage = (Stage) robot.lookup("#inpName").queryAs(TextField.class).getScene().getWindow();
        assertNotNull(stage.getTitle(), "Fenster sollte einen Titel haben");
    }
}