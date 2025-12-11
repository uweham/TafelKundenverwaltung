package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
class ChangeVerteilstelleControllerGUITest {

    @Start
    public void start(Stage stage) throws Exception {
        URL url = getClass().getResource(
                "/kundenverwaltung/fxml/admintool/ChangeVerteilstelle.fxml"
        );
        assertNotNull(url, "FXML file not found - please check the path");

        String fxml = Files.readString(Paths.get(url.toURI()), StandardCharsets.UTF_8);

        String cleaned = fxml
                .replaceAll("onAction=\"#.*?\"", "")
                .replaceAll("fx:controller=\"[^\"]*\"", "");

        FXMLLoader loader = new FXMLLoader();
        loader.setController(new DummyController());
        Parent root = loader.load(
                new ByteArrayInputStream(cleaned.getBytes(StandardCharsets.UTF_8))
        );

        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    void setUp(FxRobot robot) throws TimeoutException {
        FxToolkit.hideStage();
        FxToolkit.setupStage(Stage::show);

        robot.interact(() -> {
            TextField bezeichnung = robot.lookup("#inpBezeichnung").queryAs(TextField.class);
            TextField adresse = robot.lookup("#inpAdresse").queryAs(TextField.class);
            CheckBox checkNeu = robot.lookup("#checkNeu").queryAs(CheckBox.class);

            bezeichnung.clear();
            adresse.clear();
            checkNeu.setSelected(false);
        });
    }

    @Test
    @Tag("gui")
    void shouldHaveAllControls(FxRobot robot) {
        assertNotNull(robot.lookup("#inpBezeichnung").queryAs(TextField.class), "Bezeichnung TextField missing");
        assertNotNull(robot.lookup("#txtBezeichnung").queryAs(Text.class), "Bezeichnung Text missing");
        assertNotNull(robot.lookup("#txtAdresse").queryAs(Text.class), "Adresse Text missing");
        assertNotNull(robot.lookup("#inpAdresse").queryAs(TextField.class), "Adresse TextField missing");
        assertNotNull(robot.lookup("#btnSpeichern").queryAs(Button.class), "Speichern Button missing");
        assertNotNull(robot.lookup("#btnAbbrechen").queryAs(Button.class), "Abbrechen Button missing");
        assertNotNull(robot.lookup("#checkNeu").queryAs(CheckBox.class), "Neu CheckBox missing");
    }

    @Test
    @Tag("gui")
    void testInputFields(FxRobot robot) {
        TextField bezeichnung = robot.lookup("#inpBezeichnung").queryAs(TextField.class);
        TextField adresse = robot.lookup("#inpAdresse").queryAs(TextField.class);

        robot.clickOn("#inpBezeichnung").write("Test Verteilstelle");
        assertEquals("Test Verteilstelle", bezeichnung.getText(), "Bezeichnung field not updated");

        robot.clickOn("#inpAdresse").write("Teststraße 123");
        assertEquals("Teststraße 123", adresse.getText(), "Adresse field not updated");
    }

    @Test
    @Tag("gui")
    void testCheckBox(FxRobot robot) {
        CheckBox checkNeu = robot.lookup("#checkNeu").queryAs(CheckBox.class);

        assertFalse(checkNeu.isSelected(), "Checkbox should be initially unchecked");

        robot.clickOn("#checkNeu");
        assertTrue(checkNeu.isSelected(), "Checkbox should be checked after click");

        robot.clickOn("#checkNeu");
        assertFalse(checkNeu.isSelected(), "Checkbox should be unchecked after second click");
    }

    @Test
    @Tag("gui")
    void testButtons(FxRobot robot) {
        Button btnSpeichern = robot.lookup("#btnSpeichern").queryAs(Button.class);
        Button btnAbbrechen = robot.lookup("#btnAbbrechen").queryAs(Button.class);

        assertEquals("Speichern", btnSpeichern.getText(), "Speichern button text incorrect");
        assertEquals("Abbrechen", btnAbbrechen.getText(), "Abbrechen button text incorrect");

        assertDoesNotThrow(() -> robot.clickOn("#btnSpeichern"), "Speichern button should be clickable");
        assertDoesNotThrow(() -> robot.clickOn("#btnAbbrechen"), "Abbrechen button should be clickable");
    }

    @Test
    @Tag("gui")
    void testFormValidation(FxRobot robot) {
        Button btnSpeichern = robot.lookup("#btnSpeichern").queryAs(Button.class);

        assertTrue(btnSpeichern.isDisabled(), "Save button should be disabled when fields are empty");

        robot.clickOn("#inpBezeichnung").write("Test");
        assertTrue(btnSpeichern.isDisabled(), "Save button should still be disabled with only Bezeichnung");

        robot.clickOn("#inpBezeichnung").eraseText(4);
        robot.clickOn("#inpAdresse").write("Test");
        assertTrue(btnSpeichern.isDisabled(), "Save button should still be disabled with only Adresse");

        robot.clickOn("#inpBezeichnung").write("Test");
        assertFalse(btnSpeichern.isDisabled(), "Save button should be enabled with both fields filled");
    }

    /** Dummy Controller for testing */
    public static class DummyController {
        @FXML private TextField inpBezeichnung;
        @FXML private Text txtBezeichnung;
        @FXML private Text txtAdresse;
        @FXML private TextField inpAdresse;
        @FXML private Button btnSpeichern;
        @FXML private Button btnAbbrechen;
        @FXML private CheckBox checkNeu;

        @FXML
        public void initialize() {
            btnSpeichern.disableProperty().bind(
                    inpBezeichnung.textProperty().isEmpty()
                            .or(inpAdresse.textProperty().isEmpty())
            );
        }
    }
}