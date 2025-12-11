package guitest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Properties;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import kundenverwaltung.controller.HaushaltAnlegenController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class HaushaltAnlegenControllerGUITest {

    private Stage stage;
    private HaushaltAnlegenController controller;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/HaushaltAnlegen.fxml"));
        Parent root = loader.load();

        controller = loader.getController();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testMainNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeader").queryAs(Label.class));
        assertNotNull(robot.lookup("#tabpane").queryAs(TabPane.class));

        assertNotNull(robot.lookup("#txtVorname").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtNachname").queryAs(TextField.class));
        assertNotNull(robot.lookup("#cbAnrede").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#dateGeb").queryAs(DatePicker.class));
        assertNotNull(robot.lookup("#cbGender").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbBesBerechtigung").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbNation").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#txtStrasse").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtHausnummer").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtPostleitzahl").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtWohnort").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtTelefon").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtMobilTelefon").queryAs(TextField.class));

        assertNotNull(robot.lookup("#dateKundeSeit").queryAs(DatePicker.class));
        assertNotNull(robot.lookup("#cbAusgabegruppe").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbVerteilstelle").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbxLieferung").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#gruppenfarbe").queryAs(Rectangle.class));
        assertNotNull(robot.lookup("#txtAusgabetag").queryAs(TextArea.class));

        assertNotNull(robot.lookup("#cbxDatenschutz").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#labelDatenschutz").queryAs(Label.class));

        assertNotNull(robot.lookup("#buttonAddHousehold").queryAs(Button.class));
        assertNotNull(robot.lookup("#buttonCancel").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testLabelContents(FxRobot robot) {
        assertEquals("Vorname", robot.lookup("#labelFirstName").queryAs(Label.class).getText());
        assertEquals("Name", robot.lookup("#labelSurname").queryAs(Label.class).getText());
        assertEquals("Anrede", robot.lookup("#labelAnrede").queryAs(Label.class).getText());
        assertEquals("Geburtsdatum", robot.lookup("#labelBirthday").queryAs(Label.class).getText());
        assertEquals("Geschlecht", robot.lookup("#labelGender").queryAs(Label.class).getText());
        assertEquals("Besondere Berechtigung", robot.lookup("#labelAuthorization").queryAs(Label.class).getText());
        assertEquals("Nationalität", robot.lookup("#labelNation").queryAs(Label.class).getText());
        assertEquals("Straße", robot.lookup("#labelStreet").queryAs(Label.class).getText());
        assertEquals("Hausnummer", robot.lookup("#labelHousenumber").queryAs(Label.class).getText());
        assertEquals("Postleitzahl", robot.lookup("#labelPostcode").queryAs(Label.class).getText());
        assertEquals("Wohnort", robot.lookup("#labelLocation").queryAs(Label.class).getText());
        assertEquals("Telefonnummer", robot.lookup("#labelPhoneNumber").queryAs(Label.class).getText());
        assertEquals("Mobiltelefon-Nr.", robot.lookup("#labelMobileNumber").queryAs(Label.class).getText());
        assertEquals("Kunde seit", robot.lookup("#labelCustomerSince").queryAs(Label.class).getText());
        assertEquals("Verteilstelle", robot.lookup("#labelDistributionPoint").queryAs(Label.class).getText());
        assertEquals("Ausgabegruppe", robot.lookup("#labelOutputGroup").queryAs(Label.class).getText());
        assertEquals("Gruppenfarbe", robot.lookup("#labelGroupColor").queryAs(Label.class).getText());
        assertEquals("Ausgabezeiten", robot.lookup("#labelOutputTimes").queryAs(Label.class).getText());
    }

    @Test
    @Tag("gui")
    void testHeaderLabels(FxRobot robot) {
        Label householdHeader = robot.lookup("#labelHouseholdDirectorHeader").queryAs(Label.class);
        Label personDataHeader = robot.lookup("#labelPersonDataHeader").queryAs(Label.class);

        assertEquals("Haushaltsvorstand", householdHeader.getText());
        assertEquals("Kontaktdaten", personDataHeader.getText());
    }



    @Test
    @Tag("gui")
    void testButtonContents(FxRobot robot) {
        Button addButton = robot.lookup("#buttonAddHousehold").queryAs(Button.class);
        Button cancelButton = robot.lookup("#buttonCancel").queryAs(Button.class);

        assertEquals("Hinzufügen", addButton.getText());
        assertEquals("Abbrechen", cancelButton.getText());
    }

    @Test
    @Tag("gui")
    void testComboBoxDefaults(FxRobot robot) {
        ComboBox<String> anredeCombo = robot.lookup("#cbAnrede").queryAs(ComboBox.class);
        ComboBox<String> genderCombo = robot.lookup("#cbGender").queryAs(ComboBox.class);

        assertEquals("Herr", anredeCombo.getValue());
        assertEquals("Keine Angabe", genderCombo.getValue());
    }

    @Test
    @Tag("gui")
    void testDatePickerDefaults(FxRobot robot) {
        DatePicker kundeSeitPicker = robot.lookup("#dateKundeSeit").queryAs(DatePicker.class);
        assertEquals(LocalDate.now(), kundeSeitPicker.getValue());
    }

    @Test
    @Tag("gui")
    void testCheckBoxDefaults(FxRobot robot) {
        CheckBox lieferungCheck = robot.lookup("#cbxLieferung").queryAs(CheckBox.class);
        CheckBox datenschutzCheck = robot.lookup("#cbxDatenschutz").queryAs(CheckBox.class);

        assertFalse(lieferungCheck.isSelected());
        assertFalse(datenschutzCheck.isSelected());
    }

    @Test
    @Tag("gui")
    void testTextInputFunctionality(FxRobot robot) {
        TextField vornameField = robot.lookup("#txtVorname").queryAs(TextField.class);
        TextField nachnameField = robot.lookup("#txtNachname").queryAs(TextField.class);

        robot.clickOn(vornameField).write("Max");
        robot.clickOn(nachnameField).write("Mustermann");

        assertEquals("Max", vornameField.getText());
        assertEquals("Mustermann", nachnameField.getText());
    }


    @Test
    @Tag("gui")
    void testCancelButtonFunctionality(FxRobot robot) {
        Button cancelButton = robot.lookup("#buttonCancel").queryAs(Button.class);
        assertTrue(stage.isShowing());

        robot.clickOn(cancelButton);
    }


    @Test
    @Tag("gui")
    void testUIStructure(FxRobot robot) {
        assertNotNull(robot.lookup(".anchor-pane"));
        assertNotNull(robot.lookup(".tab-pane"));

        assertTrue(robot.lookup(".text-field").queryAll().size() >= 8);
        assertTrue(robot.lookup(".combo-box").queryAll().size() >= 6);
        assertTrue(robot.lookup(".date-picker").queryAll().size() >= 2);
        assertTrue(robot.lookup(".check-box").queryAll().size() >= 2);
    }

    @Test
    @Tag("gui")
    void testFontSizes(FxRobot robot) {
        Label headerLabel = robot.lookup("#labelHeader").queryAs(Label.class);
        assertTrue(headerLabel.getFont().getSize() > 0);

        Label firstNameLabel = robot.lookup("#labelFirstName").queryAs(Label.class);
        assertTrue(firstNameLabel.getFont().getSize() > 0);
    }

    @Test
    @Tag("gui")
    void testInitialVisibility(FxRobot robot) {
        CheckBox datenschutzCheck = robot.lookup("#cbxDatenschutz").queryAs(CheckBox.class);
        assertTrue(datenschutzCheck.isVisible());

        Button addButton = robot.lookup("#buttonAddHousehold").queryAs(Button.class);
        assertTrue(addButton.isVisible());

        Button cancelButton = robot.lookup("#buttonCancel").queryAs(Button.class);
        assertTrue(cancelButton.isVisible());
    }
}