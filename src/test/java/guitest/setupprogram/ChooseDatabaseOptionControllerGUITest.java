package guitest.setupprogram;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class ChooseDatabaseOptionControllerGUITest {

    private static final String ID_EXIT_BUTTON = "#btnExit";
    private static final String NEW_DB_BUTTON_TEXT = "Eine neue leere Datenbank und benötigte Tabellen erstellen";
    private static final String MIGRATE_DB_BUTTON_TEXT = "Daten aus dem alten Kundenverwaltungsprogramm migrieren";
    private static final String EXISTING_DB_BUTTON_TEXT = "Eine bereits vorhandene aktuelle Datenbank verbinden";
    private static final String EXIT_BUTTON_TEXT = "beenden";

    @Start
    private void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource("/setupprogram/fxml/ChooseDatabaseOption.fxml");
        assertNotNull(fxmlUrl, "FXML 'ChooseDatabaseOption.fxml' nicht gefunden. Ressourcenpfad prüfen!");

        Parent root = FXMLLoader.load(fxmlUrl);
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
        stage.toFront();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup(ID_EXIT_BUTTON).queryAs(Button.class), "Exit Button fehlt");
        assertNotNull(robot.lookup(NEW_DB_BUTTON_TEXT).queryAs(Button.class), "New Database Button fehlt");
        assertNotNull(robot.lookup(MIGRATE_DB_BUTTON_TEXT).queryAs(Button.class), "Migrate Database Button fehlt");
        assertNotNull(robot.lookup(EXISTING_DB_BUTTON_TEXT).queryAs(Button.class), "Existing Database Button fehlt");

        assertNotNull(robot.lookup("Herzlich Willkommen").queryAs(Text.class), "Welcome Text fehlt");
        assertNotNull(robot.lookup("Tafel Kundenverwaltungsprogramm der Hochschule Osnabrück").queryAs(Text.class), "Subtitle Text fehlt");
        assertNotNull(robot.lookup("Bitte wählen Sie:").queryAs(Text.class), "Selection Prompt Text fehlt");

        assertNotNull(robot.lookup(".image-view").queryAs(ImageView.class), "Image fehlt");
    }

    @Test
    @Tag("gui")
    void testButtonTexts(FxRobot robot) {
        Button newDbButton = robot.lookup(NEW_DB_BUTTON_TEXT).queryAs(Button.class);
        Button migrateDbButton = robot.lookup(MIGRATE_DB_BUTTON_TEXT).queryAs(Button.class);
        Button existingDbButton = robot.lookup(EXISTING_DB_BUTTON_TEXT).queryAs(Button.class);
        Button exitButton = robot.lookup(ID_EXIT_BUTTON).queryAs(Button.class);

        assertEquals(NEW_DB_BUTTON_TEXT, newDbButton.getText());
        assertEquals(MIGRATE_DB_BUTTON_TEXT, migrateDbButton.getText());
        assertEquals(EXISTING_DB_BUTTON_TEXT, existingDbButton.getText());
        assertEquals(EXIT_BUTTON_TEXT, exitButton.getText());
    }


    @Test
    @Tag("gui")
    void testWelcomeTextContent(FxRobot robot) {
        Text welcomeText = robot.lookup("Herzlich Willkommen").queryAs(Text.class);
        Text subtitleText = robot.lookup("Tafel Kundenverwaltungsprogramm der Hochschule Osnabrück").queryAs(Text.class);
        Text selectionText = robot.lookup("Bitte wählen Sie:").queryAs(Text.class);

        assertEquals("Herzlich Willkommen", welcomeText.getText());
        assertEquals("Tafel Kundenverwaltungsprogramm der Hochschule Osnabrück", subtitleText.getText());
        assertEquals("Bitte wählen Sie:", selectionText.getText());

        assertTrue(welcomeText.getFont().getName().contains("Bold") ||
                        welcomeText.getFont().getStyle().contains("Bold"),
                "Welcome text should be bold");
        assertEquals(36.0, welcomeText.getFont().getSize(), 0.1, "Welcome text size should be 36");
    }

    @Test
    @Tag("gui")
    void testHelpTextContent(FxRobot robot) {
        Text helpText = robot.lookup("Sollten Sie Fragen haben oder Hilfe bei der Einrichtung und der Übernahme bestehender Daten benötigen, kontaktieren Sie uns gern. Bitte besuchen Sie für weitere Informationen und Kontakt-Möglichkeiten unsere Internetseite unter http://tafel.hs-osnabrueck.de.").queryAs(Text.class);

        assertNotNull(helpText, "Help text should be present");
        assertTrue(helpText.getText().contains("http://tafel.hs-osnabrueck.de"),
                "Help text should contain website URL");
    }

    @Test
    @Tag("gui")
    void testButtonStyles(FxRobot robot) {
        Button newDbButton = robot.lookup(NEW_DB_BUTTON_TEXT).queryAs(Button.class);

        assertEquals(14.5, newDbButton.getFont().getSize(), 0.1, "Button font size should be 14.5");

        assertNotNull(newDbButton.getPadding(), "Button should have padding");
    }
}

