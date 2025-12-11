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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import kundenverwaltung.controller.PersonAendernController;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Nation;
import kundenverwaltung.model.Berechtigung;
import kundenverwaltung.model.Anrede;
import kundenverwaltung.model.Gender;

import java.net.URL;
import java.time.LocalDate;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class PersonAendernControllerGUITest {

    private Stage stage;
    private PersonAendernController controller;

    @Mock
    private Familienmitglied mockFamilienmitglied;

    @Mock
    private Haushalt mockHaushalt;

    @Mock
    private Nation mockNation;

    @Mock
    private Berechtigung mockBerechtigung;

    @Mock
    private Anrede mockAnrede;

    @Mock
    private Gender mockGender;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/PersonAendern.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden im Ressourcenordner 'kundenverwaltung/fxml'");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        controller = loader.getController();

        mockHaushalt = mock(Haushalt.class);
        mockFamilienmitglied = mock(Familienmitglied.class);
        mockNation = mock(Nation.class);
        mockBerechtigung = mock(Berechtigung.class);
        mockAnrede = mock(Anrede.class);
        mockGender = mock(Gender.class);

        when(mockHaushalt.getKundennummer()).thenReturn(12345);
        when(mockFamilienmitglied.getvName()).thenReturn("Max");
        when(mockFamilienmitglied.getnName()).thenReturn("Mustermann");
        when(mockFamilienmitglied.getBemerkung()).thenReturn("Test-Bemerkung");
        when(mockFamilienmitglied.getNation()).thenReturn(mockNation);
        when(mockFamilienmitglied.getBerechtigung()).thenReturn(mockBerechtigung);
        when(mockFamilienmitglied.getAnrede()).thenReturn(mockAnrede);
        when(mockFamilienmitglied.getGender()).thenReturn(mockGender);
        when(mockFamilienmitglied.getgDatum()).thenReturn(LocalDate.of(1990, 1, 1));
        when(mockFamilienmitglied.isEinkaufsBerechtigt()).thenReturn(true);
        when(mockFamilienmitglied.isAufAusweis()).thenReturn(true);
        when(mockFamilienmitglied.dseSubmitted()).thenReturn(true);
        when(mockFamilienmitglied.isHaushaltsVorstand()).thenReturn(false);
        when(mockFamilienmitglied.isGebuehrenBefreiung()).thenReturn(false);

        when(mockAnrede.getAnredeId()).thenReturn(32);
        when(mockGender.getGenderId()).thenReturn(71);

        controller.setHaushalt(mockHaushalt);
        controller.setzePersonendaten(mockFamilienmitglied);

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeading").queryAs(Label.class));
        assertNotNull(robot.lookup("#txtPAVorname").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtPANachname").queryAs(TextField.class));
        assertNotNull(robot.lookup("#txtPABemerkungen").queryAs(TextField.class));
        assertNotNull(robot.lookup("#cbPAAnrede").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbPAGender").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbPANationalitaet").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbPABesBerechtigungen").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#datePAGeburtsdatum").queryAs(DatePicker.class));
        assertNotNull(robot.lookup("#cbxEinkaufsberechtigt").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxHaushaltsvorstand").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxKundenausweis").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxGebuehren").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxDseSubmitted").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#btnSavePerson").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnCancel").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testLabelsText(FxRobot robot) {
        Label heading = robot.lookup("#labelHeading").queryAs(Label.class);
        Label firstName = robot.lookup("#labelFirstName").queryAs(Label.class);
        Label surname = robot.lookup("#labelSurname").queryAs(Label.class);
        Label anrede = robot.lookup("#labelAnrede").queryAs(Label.class);
        Label birthday = robot.lookup("#labelBirthday").queryAs(Label.class);
        Label gender = robot.lookup("#labelGender").queryAs(Label.class);
        Label nation = robot.lookup("#labelNation").queryAs(Label.class);
        Label authorization = robot.lookup("#labelAuthorization").queryAs(Label.class);
        Label comment = robot.lookup("#labelComment").queryAs(Label.class);

        assertEquals("Fügen Sie Personen zum aktuellen Haushalt hinzu. Dadurch ändert sich u.a. die Berechtigung der zu zahlenden Beiträge.", heading.getText());
        assertEquals("Vorname", firstName.getText());
        assertEquals("Nachname", surname.getText());
        assertEquals("Anrede", anrede.getText());
        assertEquals("Geburtsdatum", birthday.getText());
        assertEquals("Geschlecht", gender.getText());
        assertEquals("Nationalität", nation.getText());
        assertEquals("Besondere Berechtigungen", authorization.getText());
        assertEquals("Bemerkungen zur Person", comment.getText());
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot) {
        Button btnSave = robot.lookup("#btnSavePerson").queryAs(Button.class);
        Button btnCancel = robot.lookup("#btnCancel").queryAs(Button.class);

        assertEquals("OK", btnSave.getText());
        assertEquals("Abbrechen", btnCancel.getText());

        assertFalse(btnSave.isDisabled());
        assertFalse(btnCancel.isDisabled());
    }

    @Test
    @Tag("gui")
    void testTextFieldsInitialContent(FxRobot robot) {
        TextField vorname = robot.lookup("#txtPAVorname").queryAs(TextField.class);
        TextField nachname = robot.lookup("#txtPANachname").queryAs(TextField.class);
        TextField bemerkungen = robot.lookup("#txtPABemerkungen").queryAs(TextField.class);

        assertEquals("Max", vorname.getText());
        assertEquals("Mustermann", nachname.getText());
        assertEquals("Test-Bemerkung", bemerkungen.getText());
    }

    @Test
    @Tag("gui")
    void testCheckBoxesInitialState(FxRobot robot) {
        CheckBox einkaufsberechtigt = robot.lookup("#cbxEinkaufsberechtigt").queryAs(CheckBox.class);
        CheckBox kundenausweis = robot.lookup("#cbxKundenausweis").queryAs(CheckBox.class);
        CheckBox dseSubmitted = robot.lookup("#cbxDseSubmitted").queryAs(CheckBox.class);
        CheckBox haushaltsvorstand = robot.lookup("#cbxHaushaltsvorstand").queryAs(CheckBox.class);
        CheckBox gebuehren = robot.lookup("#cbxGebuehren").queryAs(CheckBox.class);

        assertTrue(einkaufsberechtigt.isSelected());
        assertTrue(kundenausweis.isSelected());
        assertTrue(dseSubmitted.isSelected());
        assertFalse(haushaltsvorstand.isSelected());
        assertFalse(gebuehren.isSelected());
    }

    @Test
    @Tag("gui")
    void testTextFieldsInput(FxRobot robot) {
        TextField vorname = robot.lookup("#txtPAVorname").queryAs(TextField.class);
        TextField nachname = robot.lookup("#txtPANachname").queryAs(TextField.class);
        TextField bemerkungen = robot.lookup("#txtPABemerkungen").queryAs(TextField.class);

        robot.clickOn(vorname);
        robot.eraseText(vorname.getText().length());
        robot.write("Anna");

        robot.clickOn(nachname);
        robot.eraseText(nachname.getText().length());
        robot.write("Schmidt");

        robot.clickOn(bemerkungen);
        robot.eraseText(bemerkungen.getText().length());
        robot.write("Neue Test-Bemerkung");

        assertEquals("Anna", vorname.getText());
        assertEquals("Schmidt", nachname.getText());
        assertEquals("Neue Test-Bemerkung", bemerkungen.getText());
    }

    @Test
    @Tag("gui")
    void testCancelButtonClosesWindow(FxRobot robot) {
        Button btnCancel = robot.lookup("#btnCancel").queryAs(Button.class);

        assertNotNull(btnCancel);
        assertTrue(btnCancel.isVisible());

        boolean initiallyShowing = stage.isShowing();

        robot.clickOn(btnCancel);

        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(stage.isShowing());
    }

    @Test
    @Tag("gui")
    void testUIElementsVisible(FxRobot robot) {
        assertTrue(robot.lookup("#labelHeading").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#txtPAVorname").queryAs(TextField.class).isVisible());
        assertTrue(robot.lookup("#txtPANachname").queryAs(TextField.class).isVisible());
        assertTrue(robot.lookup("#btnSavePerson").queryAs(Button.class).isVisible());
        assertTrue(robot.lookup("#btnCancel").queryAs(Button.class).isVisible());
    }

    @Test
    @Tag("gui")
    void testComboBoxesPopulated(FxRobot robot) {
        ComboBox<?> anredeCombo = robot.lookup("#cbPAAnrede").queryAs(ComboBox.class);
        ComboBox<?> genderCombo = robot.lookup("#cbPAGender").queryAs(ComboBox.class);
        ComboBox<?> berechtigungCombo = robot.lookup("#cbPABesBerechtigungen").queryAs(ComboBox.class);

        assertTrue(anredeCombo.getItems().size() > 0);
        assertTrue(genderCombo.getItems().size() > 0);
        assertTrue(berechtigungCombo.getItems().size() > 0);
    }

    @Test
    @Tag("gui")
    void testDatePickerFunctionality(FxRobot robot) {
        DatePicker datePicker = robot.lookup("#datePAGeburtsdatum").queryAs(DatePicker.class);

        assertEquals(LocalDate.of(1990, 1, 1), datePicker.getValue());

        assertTrue(datePicker.isEditable());
    }
}