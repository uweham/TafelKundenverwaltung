package guitest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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

import kundenverwaltung.controller.PrintConsentForm;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.model.Vorlage;
import kundenverwaltung.model.Warentyp;

import java.net.URL;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class PrintConsentFormGUITest {

    private Stage stage;
    private PrintConsentForm controller;

    @Mock
    private Familienmitglied mockFamilienmitglied;

    @Mock
    private Haushalt mockHaushalt;

    @Mock
    private Verteilstelle mockVerteilstelle;

    @Mock
    private Vorlage mockVorlage;

    @Mock
    private Warentyp mockWarentyp;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/PrintConsentForm.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden im Ressourcenordner 'kundenverwaltung/fxml'");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        controller = loader.getController();

        mockHaushalt = mock(Haushalt.class);
        mockFamilienmitglied = mock(Familienmitglied.class);
        mockVerteilstelle = mock(Verteilstelle.class);
        mockVorlage = mock(Vorlage.class);
        mockWarentyp = mock(Warentyp.class);

        when(mockHaushalt.getKundennummer()).thenReturn(12345);
        when(mockFamilienmitglied.getHaushalt()).thenReturn(mockHaushalt);
        when(mockHaushalt.getVerteilstelle()).thenReturn(mockVerteilstelle);
        when(mockVerteilstelle.getVerteilstellenId()).thenReturn(1);
        when(mockVorlage.getName()).thenReturn("Test Vorlage");
        when(mockWarentyp.getName()).thenReturn("Test Warentyp");

        controller.setFamilymember(mockFamilienmitglied);

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeader").queryAs(Label.class));
        assertNotNull(robot.lookup("#cbVorlage").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#rbAktuellerKunde").queryAs(RadioButton.class));
        assertNotNull(robot.lookup("#rbAlleKunden").queryAs(RadioButton.class));
        assertNotNull(robot.lookup("#cbVerteilstelle").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#lbVerteilstelle").queryAs(Label.class));
        assertNotNull(robot.lookup("#cbxAutomatischEinstellen").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxArchivierteKunden").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxGesperrteKunden").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxEinstellungenAlsStandard").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#buttonRun").queryAs(Button.class));
        assertNotNull(robot.lookup("#buttonCancel").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testLabelsText(FxRobot robot) {
        Label header = robot.lookup("#labelHeader").queryAs(Label.class);
        Label template = robot.lookup("#labelTemplate").queryAs(Label.class);
        Label verteilstelle = robot.lookup("#lbVerteilstelle").queryAs(Label.class);

        assertEquals("Ausgabe von Benutzerdefinierten Daten", header.getText());
        assertEquals("Vorlage:", template.getText());
        assertEquals("Verteilstelle:", verteilstelle.getText());
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot) {
        Button buttonRun = robot.lookup("#buttonRun").queryAs(Button.class);
        Button buttonCancel = robot.lookup("#buttonCancel").queryAs(Button.class);

        assertEquals("Ausführen", buttonRun.getText());
        assertEquals("Abbrechen", buttonCancel.getText());

        assertFalse(buttonRun.isDisabled());
        assertFalse(buttonCancel.isDisabled());
    }




    @Test
    @Tag("gui")
    void testRadioButtonSelectionChangesState(FxRobot robot) {
        RadioButton rbAlleKunden = robot.lookup("#rbAlleKunden").queryAs(RadioButton.class);
        ComboBox<?> cbVerteilstelle = robot.lookup("#cbVerteilstelle").queryAs(ComboBox.class);
        CheckBox cbxAutomatischEinstellen = robot.lookup("#cbxAutomatischEinstellen").queryAs(CheckBox.class);
        CheckBox cbxArchivierteKunden = robot.lookup("#cbxArchivierteKunden").queryAs(CheckBox.class);
        CheckBox cbxGesperrteKunden = robot.lookup("#cbxGesperrteKunden").queryAs(CheckBox.class);
        Label lbVerteilstelle = robot.lookup("#lbVerteilstelle").queryAs(Label.class);

        robot.clickOn(rbAlleKunden);

        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(cbVerteilstelle.isDisable());
        assertFalse(cbxAutomatischEinstellen.isDisable());
        assertFalse(lbVerteilstelle.isDisable());
        assertFalse(cbxArchivierteKunden.isDisable());
        assertFalse(cbxGesperrteKunden.isDisable());
    }

    @Test
    @Tag("gui")
    void testCancelButtonClosesWindow(FxRobot robot) {
        Button buttonCancel = robot.lookup("#buttonCancel").queryAs(Button.class);

        assertNotNull(buttonCancel);
        assertTrue(buttonCancel.isVisible());

        boolean initiallyShowing = stage.isShowing();

        robot.clickOn(buttonCancel);

        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(stage.isShowing());
    }

    @Test
    @Tag("gui")
    void testUIElementsVisible(FxRobot robot) {
        assertTrue(robot.lookup("#labelHeader").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#cbVorlage").queryAs(ComboBox.class).isVisible());
        assertTrue(robot.lookup("#rbAktuellerKunde").queryAs(RadioButton.class).isVisible());
        assertTrue(robot.lookup("#rbAlleKunden").queryAs(RadioButton.class).isVisible());
        assertTrue(robot.lookup("#buttonRun").queryAs(Button.class).isVisible());
        assertTrue(robot.lookup("#buttonCancel").queryAs(Button.class).isVisible());
    }

    @Test
    @Tag("gui")
    void testComboBoxesPopulated(FxRobot robot) {
        ComboBox<?> cbVorlage = robot.lookup("#cbVorlage").queryAs(ComboBox.class);
        ComboBox<?> cbVerteilstelle = robot.lookup("#cbVerteilstelle").queryAs(ComboBox.class);
        assertTrue(cbVorlage.getItems().size() > 0);
        assertTrue(cbVerteilstelle.getItems().size() > 0);
    }
}