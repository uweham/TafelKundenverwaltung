package guitest;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import kundenverwaltung.controller.HaushaltVerwaltenController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class HaushaltVerwaltenControllerGUITest {

    private Stage stage;
    private HaushaltVerwaltenController controller;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/HaushaltVerwalten.fxml"));
        Parent root = loader.load();

        controller = loader.getController();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testMainNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeading").queryAs(Label.class));

        assertNotNull(robot.lookup("#txtKundennummer").queryAs(TextField.class));
        assertNotNull(robot.lookup("#dateKundeSeit").queryAs(DatePicker.class));
        assertNotNull(robot.lookup("#txtVerteilstelle").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtAusgabegruppe").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtAusgabezeiten").queryAs(TextArea.class));
        assertNotNull(robot.lookup("#lbVollmachten").queryAs(Label.class));

        assertNotNull(robot.lookup("#txtStrasse").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtHausnummer").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtTelefonnummer").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtPostleitzahl").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtWohnort").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtMobiltelefon").queryAs(TextField.class));

        assertNotNull(robot.lookup("#txtBemerkungen").queryAs(TextArea.class));

        assertNotNull(robot.lookup("#tvFamilienmitglieder").queryAs(TableView.class));

        assertNotNull(robot.lookup("#buttonChangeOutputTimes").queryAs(Button.class));
        assertNotNull(robot.lookup("#buttonEditPowerOfAttorney").queryAs(Button.class));
        assertNotNull(robot.lookup("#buttonChangeContactDetails").queryAs(Button.class));
        assertNotNull(robot.lookup("#buttonChangeComment").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnShowDeletedPerson").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnEditDecision").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnChangePerson").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnAddPerson").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnDeletePerson").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnDeleteHousehold").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnExitWindow").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testLabelContents(FxRobot robot) {
        assertEquals("Kundennummer", robot.lookup("#labelCustomerId").queryAs(Label.class).getText());
        assertEquals("Kunde seit", robot.lookup("#labelCustomerSince").queryAs(Label.class).getText());
        assertEquals("Verteilstelle", robot.lookup("#labelVerteilstelle").queryAs(Label.class).getText());
        assertEquals("Ausgabegruppe", robot.lookup("#labelAusgabegruppe").queryAs(Label.class).getText());
        assertEquals("Ausgabezeiten", robot.lookup("#labelOutputTimes").queryAs(Label.class).getText());
        assertEquals("Straße", robot.lookup("#labelStreet").queryAs(Label.class).getText());
        assertEquals("Nr.", robot.lookup("#labelHouseNumber").queryAs(Label.class).getText());
        assertEquals("Telefonnummer", robot.lookup("#labelPhoneNumber").queryAs(Label.class).getText());
        assertEquals("PLZ", robot.lookup("#labelPostcode").queryAs(Label.class).getText());
        assertEquals("Wohnort", robot.lookup("#labelPlace").queryAs(Label.class).getText());
        assertEquals("Mobiltelefon-Nr.", robot.lookup("#labelMobilPhoneNumber").queryAs(Label.class).getText());
    }

    @Test
    @Tag("gui")
    void testHeaderLabels(FxRobot robot) {
        Label headingLabel = robot.lookup("#labelHeading").queryAs(Label.class);
        Label generalLabel = robot.lookup("#labelGeneral").queryAs(Label.class);
        Label contactLabel = robot.lookup("#labelContactDetails").queryAs(Label.class);
        Label memberLabel = robot.lookup("#labelMemberOfTheFamily").queryAs(Label.class);
        Label commentLabel = robot.lookup("#labelComment").queryAs(Label.class);
        Label powerLabel = robot.lookup("#labelPowerOfAttorney").queryAs(Label.class);

        assertEquals("Ändern Sie hier die Daten des Haushalts.", headingLabel.getText());
        assertEquals("Allgemeines", generalLabel.getText());
        assertEquals("Kontaktdaten", contactLabel.getText());
        assertEquals("Familienmitglieder", memberLabel.getText());
        assertEquals("Bemerkungen", commentLabel.getText());
        assertEquals("Vollmachten", powerLabel.getText());


    }

    @Test
    @Tag("gui")
    void testButtonContents(FxRobot robot) {
        Button changeOutputButton = robot.lookup("#buttonChangeOutputTimes").queryAs(Button.class);
        Button editPowerButton = robot.lookup("#buttonEditPowerOfAttorney").queryAs(Button.class);
        Button changeContactButton = robot.lookup("#buttonChangeContactDetails").queryAs(Button.class);
        Button changeCommentButton = robot.lookup("#buttonChangeComment").queryAs(Button.class);
        Button showDeletedButton = robot.lookup("#btnShowDeletedPerson").queryAs(Button.class);
        Button editDecisionButton = robot.lookup("#btnEditDecision").queryAs(Button.class);
        Button changePersonButton = robot.lookup("#btnChangePerson").queryAs(Button.class);
        Button addPersonButton = robot.lookup("#btnAddPerson").queryAs(Button.class);
        Button deletePersonButton = robot.lookup("#btnDeletePerson").queryAs(Button.class);
        Button deleteHouseholdButton = robot.lookup("#btnDeleteHousehold").queryAs(Button.class);
        Button exitButton = robot.lookup("#btnExitWindow").queryAs(Button.class);

        assertEquals("Ändern", changeOutputButton.getText());
        assertEquals("Bearbeiten", editPowerButton.getText());
        assertEquals("Ändern", changeContactButton.getText());
        assertEquals("Ändern", changeCommentButton.getText());
        assertEquals("Gelöschte Personen anzeigen", showDeletedButton.getText());
        assertEquals("Bescheide bearbeiten", editDecisionButton.getText());
        assertEquals("Person ändern", changePersonButton.getText());
        assertEquals("Person hinzufügen", addPersonButton.getText());
        assertEquals("Person löschen", deletePersonButton.getText());
        assertEquals("Diesen Haushalt löschen", deleteHouseholdButton.getText());
        assertEquals("Schließen", exitButton.getText());
    }

    @Test
    @Tag("gui")
    void testTextFieldDefaults(FxRobot robot) {
        TextField kundennummerField = robot.lookup("#txtKundennummer").queryAs(TextField.class);
        TextField verteilstelleField = robot.lookup("#txtVerteilstelle").queryAs(TextField.class);
        TextField ausgabegruppeField = robot.lookup("#txtAusgabegruppe").queryAs(TextField.class);

        assertFalse(kundennummerField.isEditable());
        assertFalse(verteilstelleField.isEditable());
        assertFalse(ausgabegruppeField.isEditable());
    }

    @Test
    @Tag("gui")
    void testTextAreaDefaults(FxRobot robot) {
        TextArea ausgabezeitenArea = robot.lookup("#txtAusgabezeiten").queryAs(TextArea.class);
        TextArea bemerkungenArea = robot.lookup("#txtBemerkungen").queryAs(TextArea.class);

        assertFalse(ausgabezeitenArea.isEditable());
        assertFalse(bemerkungenArea.isEditable());
    }

    @Test
    @Tag("gui")
    void testDatePickerDefaults(FxRobot robot) {
        DatePicker kundeSeitPicker = robot.lookup("#dateKundeSeit").queryAs(DatePicker.class);
        assertFalse(kundeSeitPicker.isEditable());
    }

    @Test
    @Tag("gui")
    void testTableStructure(FxRobot robot) {
        TableView<?> table = robot.lookup("#tvFamilienmitglieder").queryAs(TableView.class);
        assertNotNull(table);
        assertEquals(10, table.getColumns().size());
    }

    @Test
    @Tag("gui")
    void testPowerOfAttorneyLabel(FxRobot robot) {
        Label powerLabel = robot.lookup("#lbVollmachten").queryAs(Label.class);
        assertTrue(powerLabel.getText().contains("Vollmachten"));
        assertTrue(powerLabel.isWrapText());
    }


    @Test
    @Tag("gui")
    void testButtonVisibility(FxRobot robot) {
        Button changeOutputButton = robot.lookup("#buttonChangeOutputTimes").queryAs(Button.class);
        Button editPowerButton = robot.lookup("#buttonEditPowerOfAttorney").queryAs(Button.class);
        Button changeContactButton = robot.lookup("#buttonChangeContactDetails").queryAs(Button.class);
        Button changeCommentButton = robot.lookup("#buttonChangeComment").queryAs(Button.class);
        Button exitButton = robot.lookup("#btnExitWindow").queryAs(Button.class);

        assertTrue(changeOutputButton.isVisible());
        assertTrue(editPowerButton.isVisible());
        assertTrue(changeContactButton.isVisible());
        assertTrue(changeCommentButton.isVisible());
        assertTrue(exitButton.isVisible());
    }

    @Test
    @Tag("gui")
    void testFontSizes(FxRobot robot) {
        Label headingLabel = robot.lookup("#labelHeading").queryAs(Label.class);
        assertEquals(13.0, headingLabel.getFont().getSize(), 0.1);

        Label generalLabel = robot.lookup("#labelGeneral").queryAs(Label.class);
        assertEquals(12.0, generalLabel.getFont().getSize(), 0.1);

        Label customerIdLabel = robot.lookup("#labelCustomerId").queryAs(Label.class);
        assertTrue(customerIdLabel.getFont().getSize() > 0);
    }

    @Test
    @Tag("gui")
    void testInitialReadOnlyState(FxRobot robot) {
        TextField[] readOnlyFields = {
                robot.lookup("#txtKundennummer").queryAs(TextField.class),
                robot.lookup("#txtVerteilstelle").queryAs(TextField.class),
                robot.lookup("#txtAusgabegruppe").queryAs(TextField.class),
                robot.lookup("#txtStrasse").queryAs(TextField.class),
                robot.lookup("#txtHausnummer").queryAs(TextField.class),
                robot.lookup("#txtTelefonnummer").queryAs(TextField.class),
                robot.lookup("#txtPostleitzahl").queryAs(TextField.class),
                robot.lookup("#txtWohnort").queryAs(TextField.class),
                robot.lookup("#txtMobiltelefon").queryAs(TextField.class)
        };

        for (TextField field : readOnlyFields) {
            assertFalse(field.isEditable(), "Field " + field.getId() + " should be read-only");
        }

        TextArea[] readOnlyAreas = {
                robot.lookup("#txtAusgabezeiten").queryAs(TextArea.class),
                robot.lookup("#txtBemerkungen").queryAs(TextArea.class)
        };

        for (TextArea area : readOnlyAreas) {
            assertFalse(area.isEditable(), "TextArea " + area.getId() + " should be read-only");
        }

        DatePicker datePicker = robot.lookup("#dateKundeSeit").queryAs(DatePicker.class);
        assertFalse(datePicker.isEditable(), "DatePicker should be read-only");
    }

    @Test
    @Tag("gui")
    void testTableColumnExistence(FxRobot robot) {
        TableView<?> table = robot.lookup("#tvFamilienmitglieder").queryAs(TableView.class);
        assertNotNull(table);

        boolean hasAnredeColumn = table.getColumns().stream()
                .anyMatch(col -> "Anrede".equals(col.getText()));
        boolean hasNameColumn = table.getColumns().stream()
                .anyMatch(col -> "Name".equals(col.getText()));
        boolean hasGeburtsdatumColumn = table.getColumns().stream()
                .anyMatch(col -> "Geburtsdatum".equals(col.getText()));

        assertTrue(hasAnredeColumn, "Table should have Anrede column");
        assertTrue(hasNameColumn, "Table should have Name column");
        assertTrue(hasGeburtsdatumColumn, "Table should have Geburtsdatum column");
    }
}