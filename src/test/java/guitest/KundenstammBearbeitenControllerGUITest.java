package guitest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import kundenverwaltung.controller.KundenstammBearbeitenController;
import kundenverwaltung.model.Ausgabegruppe;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Verteilstelle;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.time.LocalDate;

@ExtendWith(ApplicationExtension.class)
public class KundenstammBearbeitenControllerGUITest {

    private Stage stage;
    private KundenstammBearbeitenController controller;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/KundenstammBearbeiten.fxml"));
        Parent root = loader.load();

        controller = loader.getController();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testMainNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeading").queryAs(Label.class));

        assertNotNull(robot.lookup("#labelCustomerSince").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelVerteilstelle").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelOutputGroup").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelGroupColor").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelOutputTimes").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelDataProtection").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelHousholdIsArchivedInfo").queryAs(Label.class));
        assertNotNull(robot.lookup("#lbAusgabezeit").queryAs(Label.class));

        assertNotNull(robot.lookup("#cbVerteilstelle").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbAusgabegruppe").queryAs(ComboBox.class));

        assertNotNull(robot.lookup("#cbxDatenschutz").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxHaushaltGesperrt").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxHaushaltArchiviert").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxHaushaltBeliefern").queryAs(CheckBox.class));

        assertNotNull(robot.lookup("#dateKundeSeit").queryAs(DatePicker.class));

        assertNotNull(robot.lookup("#gruppenfarbe").queryAs(Rectangle.class));

        assertNotNull(robot.lookup("#btnOk").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnAbbrechen").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testLabelContents(FxRobot robot) {
        Label headingLabel = robot.lookup("#labelHeading").queryAs(Label.class);
        assertTrue(headingLabel.getText().contains("Legen Sie hier die Ausgabe- und Verteilstelleninformationen fest."));

        assertEquals("Kunde seit", robot.lookup("#labelCustomerSince").queryAs(Label.class).getText());
        assertEquals("Verteilstelle", robot.lookup("#labelVerteilstelle").queryAs(Label.class).getText());
        assertEquals("Ausgabegruppe:", robot.lookup("#labelOutputGroup").queryAs(Label.class).getText());
        assertEquals("Gruppenfarbe:", robot.lookup("#labelGroupColor").queryAs(Label.class).getText());
        assertEquals("Ausgabezeiten", robot.lookup("#labelOutputTimes").queryAs(Label.class).getText());
        assertEquals("Datenschutz", robot.lookup("#labelDataProtection").queryAs(Label.class).getText());
        assertTrue(robot.lookup("#labelHousholdIsArchivedInfo").queryAs(Label.class).getText()
                .contains("Der Haushalt wird beim nächsten Einkauf automatisch aus dem Archiv entfernt."));
    }

    @Test
    @Tag("gui")
    void testCheckBoxContents(FxRobot robot) {
        CheckBox datenschutz = robot.lookup("#cbxDatenschutz").queryAs(CheckBox.class);
        CheckBox gesperrt = robot.lookup("#cbxHaushaltGesperrt").queryAs(CheckBox.class);
        CheckBox archiviert = robot.lookup("#cbxHaushaltArchiviert").queryAs(CheckBox.class);
        CheckBox beliefern = robot.lookup("#cbxHaushaltBeliefern").queryAs(CheckBox.class);

        assertTrue(datenschutz.getText().contains("Der Kunde wurde über die Erfassung und Verwendung seiner Daten informiert"));
        assertEquals("Haushalt ist gesperrt", gesperrt.getText());
        assertEquals("Haushalt ist archiviert", archiviert.getText());
        assertEquals("Haushalt wird beliefert", beliefern.getText());
    }

    @Test
    @Tag("gui")
    void testButtonContents(FxRobot robot) {
        Button okButton = robot.lookup("#btnOk").queryAs(Button.class);
        Button cancelButton = robot.lookup("#btnAbbrechen").queryAs(Button.class);

        assertEquals("OK", okButton.getText());
        assertEquals("Abbrechen", cancelButton.getText());
    }

    @Test
    @Tag("gui")
    void testCheckBoxDefaults(FxRobot robot) {
        CheckBox datenschutz = robot.lookup("#cbxDatenschutz").queryAs(CheckBox.class);
        CheckBox gesperrt = robot.lookup("#cbxHaushaltGesperrt").queryAs(CheckBox.class);
        CheckBox archiviert = robot.lookup("#cbxHaushaltArchiviert").queryAs(CheckBox.class);
        CheckBox beliefern = robot.lookup("#cbxHaushaltBeliefern").queryAs(CheckBox.class);

        assertFalse(datenschutz.isSelected());
        assertFalse(gesperrt.isSelected());
        assertFalse(archiviert.isSelected());
        assertFalse(beliefern.isSelected());
    }

    @Test
    @Tag("gui")
    void testButtonVisibility(FxRobot robot) {
        Button okButton = robot.lookup("#btnOk").queryAs(Button.class);
        Button cancelButton = robot.lookup("#btnAbbrechen").queryAs(Button.class);

        assertTrue(okButton.isVisible());
        assertTrue(cancelButton.isVisible());
    }

    @Test
    @Tag("gui")
    void testButtonDisableState(FxRobot robot) {
        Button okButton = robot.lookup("#btnOk").queryAs(Button.class);
        Button cancelButton = robot.lookup("#btnAbbrechen").queryAs(Button.class);

        assertFalse(okButton.isDisabled());
        assertFalse(cancelButton.isDisabled());
    }

    @Test
    @Tag("gui")
    void testHeaderStyle(FxRobot robot) {
        Label headerLabel = robot.lookup("#labelHeading").queryAs(Label.class);

        assertTrue(headerLabel.getTextFill().toString().contains("ee6f00"),
                "Header should have orange text color");
    }

    @Test
    @Tag("gui")
    void testCheckBoxToggle(FxRobot robot) {
        CheckBox datenschutz = robot.lookup("#cbxDatenschutz").queryAs(CheckBox.class);
        CheckBox beliefern = robot.lookup("#cbxHaushaltBeliefern").queryAs(CheckBox.class);

        assertFalse(datenschutz.isSelected());
        assertFalse(beliefern.isSelected());

        robot.clickOn(datenschutz);
        assertTrue(datenschutz.isSelected());

        robot.clickOn(beliefern);
        assertTrue(beliefern.isSelected());

        robot.clickOn(datenschutz);
        assertFalse(datenschutz.isSelected());

        robot.clickOn(beliefern);
        assertFalse(beliefern.isSelected());
    }

    @Test
    @Tag("gui")
    void testDatePickerInteraction(FxRobot robot) {
        DatePicker datePicker = robot.lookup("#dateKundeSeit").queryAs(DatePicker.class);

        assertNotNull(datePicker);
        assertFalse(datePicker.isEditable()); // Sollte nicht editierbar sein laut FXML

        LocalDate testDate = LocalDate.now();
        robot.interact(() -> datePicker.setValue(testDate));
        assertEquals(testDate, datePicker.getValue());
    }

    @Test
    @Tag("gui")
    void testRectangleProperties(FxRobot robot) {
        Rectangle gruppenfarbe = robot.lookup("#gruppenfarbe").queryAs(Rectangle.class);

        assertNotNull(gruppenfarbe);
        assertEquals(197.0, gruppenfarbe.getWidth(), 0.1);
        assertEquals(23.0, gruppenfarbe.getHeight(), 0.1);
        assertTrue(gruppenfarbe.getFill().toString().contains("0xffffff00"));
    }

    @Test
    @Tag("gui")
    void testComboBoxInteraction(FxRobot robot) {
        ComboBox<?> ausgabegruppeCombo = robot.lookup("#cbAusgabegruppe").queryAs(ComboBox.class);
        ComboBox<?> verteilstelleCombo = robot.lookup("#cbVerteilstelle").queryAs(ComboBox.class);

        assertNotNull(ausgabegruppeCombo);
        assertNotNull(verteilstelleCombo);

        assertTrue(ausgabegruppeCombo.getItems().size() > 0);
        assertTrue(verteilstelleCombo.getItems().size() > 0);
    }

    @Test
    @Tag("gui")
    void testButtonActions(FxRobot robot) {
        Button okButton = robot.lookup("#btnOk").queryAs(Button.class);
        Button cancelButton = robot.lookup("#btnAbbrechen").queryAs(Button.class);

        assertNotNull(okButton.getOnAction());
        assertNotNull(cancelButton.getOnAction());
    }

    @Test
    @Tag("gui")
    void testWindowProperties(FxRobot robot) {
        assertNotNull(stage.getTitle());
        assertTrue(stage.isShowing());
        assertTrue(stage.getWidth() > 0);
        assertTrue(stage.getHeight() > 0);

        assertEquals(687.0, stage.getWidth(), 1.0);
        assertEquals(347.0, stage.getHeight(), 1.0);
    }

    @Test
    @Tag("gui")
    void testLayoutStructure(FxRobot robot) {
        assertTrue(robot.lookup("#labelCustomerSince").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#dateKundeSeit").queryAs(DatePicker.class).isVisible());
        assertTrue(robot.lookup("#labelVerteilstelle").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#cbVerteilstelle").queryAs(ComboBox.class).isVisible());
        assertTrue(robot.lookup("#labelOutputGroup").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#cbAusgabegruppe").queryAs(ComboBox.class).isVisible());
    }



    @Test
    @Tag("gui")
    void testInitialAusgabezeitLabel(FxRobot robot) {
        Label ausgabezeitLabel = robot.lookup("#lbAusgabezeit").queryAs(Label.class);

        assertNotNull(ausgabezeitLabel.getText());
    }


}