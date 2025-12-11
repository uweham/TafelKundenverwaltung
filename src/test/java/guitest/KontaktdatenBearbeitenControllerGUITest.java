package guitest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import kundenverwaltung.controller.KontaktdatenBearbeitenController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class KontaktdatenBearbeitenControllerGUITest {

    private Stage stage;
    private KontaktdatenBearbeitenController controller;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/KontaktdatenBearbeiten.fxml"));
        Parent root = loader.load();

        controller = loader.getController();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testMainNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeading").queryAs(Label.class));

        assertNotNull(robot.lookup("#labelStreet").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelHouseNumber").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelPostcode").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelLocation").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelPhoneNumber").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelMobilNumber").queryAs(Label.class));

        assertNotNull(robot.lookup("#txtKDStrasse").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtKDHausnummer").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtKDPostleitzahl").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtKDWohnort").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtKDTelefonnummer").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtKDMobiltelefon").queryAs(TextField.class));

        assertNotNull(robot.lookup("#btnKontaktdatenOK").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnKontaktdatenAbbrechen").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testLabelContents(FxRobot robot) {
        assertEquals("Geben Sie hier die Kontaktdaten des Kunden ein.",
                robot.lookup("#labelHeading").queryAs(Label.class).getText());
        assertEquals("Straße", robot.lookup("#labelStreet").queryAs(Label.class).getText());
        assertEquals("Hausnummer", robot.lookup("#labelHouseNumber").queryAs(Label.class).getText());
        assertEquals("PLZ", robot.lookup("#labelPostcode").queryAs(Label.class).getText());
        assertEquals("Wohnort", robot.lookup("#labelLocation").queryAs(Label.class).getText());
        assertEquals("Telefonnummer", robot.lookup("#labelPhoneNumber").queryAs(Label.class).getText());
        assertEquals("Mobiltelefon-Nr.", robot.lookup("#labelMobilNumber").queryAs(Label.class).getText());
    }

    @Test
    @Tag("gui")
    void testButtonContents(FxRobot robot) {
        Button okButton = robot.lookup("#btnKontaktdatenOK").queryAs(Button.class);
        Button cancelButton = robot.lookup("#btnKontaktdatenAbbrechen").queryAs(Button.class);

        assertEquals("OK", okButton.getText());
        assertEquals("Abbrechen", cancelButton.getText());
    }

    @Test
    @Tag("gui")
    void testTextFieldDefaults(FxRobot robot) {
        TextField[] editableFields = {
                robot.lookup("#txtKDStrasse").queryAs(TextField.class),
                robot.lookup("#txtKDHausnummer").queryAs(TextField.class),
                robot.lookup("#txtKDPostleitzahl").queryAs(TextField.class),
                robot.lookup("#txtKDWohnort").queryAs(TextField.class),
                robot.lookup("#txtKDTelefonnummer").queryAs(TextField.class),
                robot.lookup("#txtKDMobiltelefon").queryAs(TextField.class)
        };

        for (TextField field : editableFields) {
            assertTrue(field.isEditable(), "Field " + field.getId() + " should be editable");
        }
    }

    @Test
    @Tag("gui")
    void testTextFieldPlaceholders(FxRobot robot) {
        TextField[] textFields = {
                robot.lookup("#txtKDStrasse").queryAs(TextField.class),
                robot.lookup("#txtKDHausnummer").queryAs(TextField.class),
                robot.lookup("#txtKDPostleitzahl").queryAs(TextField.class),
                robot.lookup("#txtKDWohnort").queryAs(TextField.class),
                robot.lookup("#txtKDTelefonnummer").queryAs(TextField.class),
                robot.lookup("#txtKDMobiltelefon").queryAs(TextField.class)
        };

        for (TextField field : textFields) {
            assertEquals("", field.getText(), "Field " + field.getId() + " should be empty initially");
        }
    }

    @Test
    @Tag("gui")
    void testButtonVisibility(FxRobot robot) {
        Button okButton = robot.lookup("#btnKontaktdatenOK").queryAs(Button.class);
        Button cancelButton = robot.lookup("#btnKontaktdatenAbbrechen").queryAs(Button.class);

        assertTrue(okButton.isVisible());
        assertTrue(cancelButton.isVisible());
    }

    @Test
    @Tag("gui")
    void testButtonDisableState(FxRobot robot) {
        Button okButton = robot.lookup("#btnKontaktdatenOK").queryAs(Button.class);
        Button cancelButton = robot.lookup("#btnKontaktdatenAbbrechen").queryAs(Button.class);

        assertFalse(okButton.isDisabled());
        assertFalse(cancelButton.isDisabled());
    }

    @Test
    @Tag("gui")
    void testHeadingStyle(FxRobot robot) {
        Label headingLabel = robot.lookup("#labelHeading").queryAs(Label.class);

        assertTrue(headingLabel.getTextFill().toString().contains("ee6f00"),
                "Heading should have orange text color");
    }

    @Test
    @Tag("gui")
    void testTextFieldInput(FxRobot robot) {
        TextField strasseField = robot.lookup("#txtKDStrasse").queryAs(TextField.class);
        TextField hausnummerField = robot.lookup("#txtKDHausnummer").queryAs(TextField.class);

        robot.clickOn(strasseField).write("Teststraße");
        robot.clickOn(hausnummerField).write("123");

        assertEquals("Teststraße", strasseField.getText());
        assertEquals("123", hausnummerField.getText());
    }

    @Test
    @Tag("gui")
    void testLayoutStructure(FxRobot robot) {
        Label streetLabel = robot.lookup("#labelStreet").queryAs(Label.class);
        Label houseNumberLabel = robot.lookup("#labelHouseNumber").queryAs(Label.class);

        assertTrue(streetLabel.isVisible());
        assertTrue(houseNumberLabel.isVisible());
        assertEquals("Straße", streetLabel.getText());
        assertEquals("Hausnummer", houseNumberLabel.getText());
    }

    @Test
    @Tag("gui")
    void testRequiredFieldsIndication(FxRobot robot) {
        Label streetLabel = robot.lookup("#labelStreet").queryAs(Label.class);
        Label plzLabel = robot.lookup("#labelPostcode").queryAs(Label.class);
        Label hausnummerLabel = robot.lookup("#labelHouseNumber").queryAs(Label.class);
        Label wohnortLabel = robot.lookup("#labelLocation").queryAs(Label.class);

        assertTrue(streetLabel.isVisible());
        assertTrue(plzLabel.isVisible());
        assertTrue(hausnummerLabel.isVisible());
        assertTrue(wohnortLabel.isVisible());
    }

    @Test
    @Tag("gui")
    void testButtonActions(FxRobot robot) {
        Button okButton = robot.lookup("#btnKontaktdatenOK").queryAs(Button.class);
        Button cancelButton = robot.lookup("#btnKontaktdatenAbbrechen").queryAs(Button.class);

        assertNotNull(okButton.getOnAction());
        assertNotNull(cancelButton.getOnAction());
    }

    @Test
    @Tag("gui")
    void testWindowTitleAndSize(FxRobot robot) {
        assertNotNull(stage.getTitle());
        assertTrue(stage.isShowing());
        assertTrue(stage.getWidth() > 0);
        assertTrue(stage.getHeight() > 0);
    }
}