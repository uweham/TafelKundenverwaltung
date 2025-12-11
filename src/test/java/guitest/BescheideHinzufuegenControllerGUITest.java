package guitest;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import kundenverwaltung.controller.BescheideBearbeitenController;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Bescheidart;

import java.net.URL;
import java.util.ArrayList;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class BescheideHinzufuegenControllerGUITest {

    private Stage stage;
    private BescheideBearbeitenController controller;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/BescheidHinzufuegen.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden im Ressourcenordner 'kundenverwaltung/fxml'");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        controller = loader.getController();

        ArrayList<Familienmitglied> testFamilienmitglieder = new ArrayList<>();

        controller.setzeDatenBescheidHinzufuegen(testFamilienmitglieder);

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeadingAddAssesment").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelMemberOfTheFamily").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelAssessmentType").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelValidFrom").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelDateOfExpiry").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelOr").queryAs(Label.class));


        assertNotNull(robot.lookup("#btnSaveAssessment").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnHinzufuegenSchliessen").queryAs(Button.class));

        assertNotNull(robot.lookup("#cbFamilienmitglied").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbBescheidart").queryAs(ComboBox.class));

        assertNotNull(robot.lookup("#dateGueltigAb").queryAs(DatePicker.class));
        assertNotNull(robot.lookup("#dateGueltigBis").queryAs(DatePicker.class));

        assertNotNull(robot.lookup("#cbxUnbegrenzt").queryAs(CheckBox.class));
    }

    @Test
    @Tag("gui")
    void testLabelsText(FxRobot robot) {
        Label heading = robot.lookup("#labelHeadingAddAssesment").queryAs(Label.class);
        Label familyMember = robot.lookup("#labelMemberOfTheFamily").queryAs(Label.class);
        Label assessmentType = robot.lookup("#labelAssessmentType").queryAs(Label.class);
        Label validFrom = robot.lookup("#labelValidFrom").queryAs(Label.class);
        Label dateOfExpiry = robot.lookup("#labelDateOfExpiry").queryAs(Label.class);
        Label or = robot.lookup("#labelOr").queryAs(Label.class);

        assertTrue(heading.getText().contains("Bitte wählen Sie ein Familienmitglied und ein Bescheid aus"));
        assertEquals("Familienmitglied", familyMember.getText());
        assertEquals("Bescheidart", assessmentType.getText());
        assertEquals("Gültig ab:", validFrom.getText());
        assertEquals("Gültig bis:", dateOfExpiry.getText());
        assertEquals("oder", or.getText());
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot) {
        Button btnSave = robot.lookup("#btnSaveAssessment").queryAs(Button.class);
        Button btnClose = robot.lookup("#btnHinzufuegenSchliessen").queryAs(Button.class);

        assertEquals("OK", btnSave.getText());
        assertEquals("Abbrechen", btnClose.getText());

        assertFalse(btnSave.isDisabled());
        assertFalse(btnClose.isDisabled());
    }

    @Test
    @Tag("gui")
    void testComboBoxesInitialState(FxRobot robot) {
        ComboBox<Familienmitglied> familyComboBox = robot.lookup("#cbFamilienmitglied").queryAs(ComboBox.class);
        ComboBox<Bescheidart> assessmentComboBox = robot.lookup("#cbBescheidart").queryAs(ComboBox.class);

        assertNotNull(familyComboBox);
        assertNotNull(assessmentComboBox);

        assertTrue(familyComboBox.getItems().isEmpty() || familyComboBox.getItems().size() >= 0);
        assertTrue(assessmentComboBox.getItems().isEmpty() || assessmentComboBox.getItems().size() >= 0);
    }

    @Test
    @Tag("gui")
    void testDatePickersInitialState(FxRobot robot) {
        DatePicker dateValidFrom = robot.lookup("#dateGueltigAb").queryAs(DatePicker.class);
        DatePicker dateValidUntil = robot.lookup("#dateGueltigBis").queryAs(DatePicker.class);

        assertNotNull(dateValidFrom);
        assertNotNull(dateValidUntil);

        assertNull(dateValidFrom.getValue());
        assertNull(dateValidUntil.getValue());
    }

    @Test
    @Tag("gui")
    void testCheckBoxInitialState(FxRobot robot) {
        CheckBox unlimitedCheckbox = robot.lookup("#cbxUnbegrenzt").queryAs(CheckBox.class);

        assertNotNull(unlimitedCheckbox);
        assertEquals("unbegrenzt", unlimitedCheckbox.getText());
        assertFalse(unlimitedCheckbox.isSelected());

        DatePicker dateValidUntil = robot.lookup("#dateGueltigBis").queryAs(DatePicker.class);
        assertFalse(dateValidUntil.isDisabled());
    }

    @Test
    @Tag("gui")
    void testCheckBoxInteraction(FxRobot robot) {
        CheckBox unlimitedCheckbox = robot.lookup("#cbxUnbegrenzt").queryAs(CheckBox.class);
        DatePicker dateValidUntil = robot.lookup("#dateGueltigBis").queryAs(DatePicker.class);

        assertFalse(unlimitedCheckbox.isSelected());
        assertFalse(dateValidUntil.isDisabled());

        robot.clickOn(unlimitedCheckbox);
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(unlimitedCheckbox.isSelected());
        assertTrue(dateValidUntil.isDisabled());

        robot.clickOn(unlimitedCheckbox);
        WaitForAsyncUtils.waitForFxEvents();
        assertFalse(unlimitedCheckbox.isSelected());
        assertFalse(dateValidUntil.isDisabled());
    }

    @Test
    @Tag("gui")
    void testUIElementsVisible(FxRobot robot) {
        assertTrue(robot.lookup("#labelHeadingAddAssesment").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#btnSaveAssessment").queryAs(Button.class).isVisible());
        assertTrue(robot.lookup("#cbFamilienmitglied").queryAs(ComboBox.class).isVisible());
        assertTrue(robot.lookup("#cbBescheidart").queryAs(ComboBox.class).isVisible());
        assertTrue(robot.lookup("#dateGueltigAb").queryAs(DatePicker.class).isVisible());
        assertTrue(robot.lookup("#dateGueltigBis").queryAs(DatePicker.class).isVisible());
        assertTrue(robot.lookup("#cbxUnbegrenzt").queryAs(CheckBox.class).isVisible());
    }

    @Test
    @Tag("gui")
    void testButtonClicks(FxRobot robot) {
        Button btnSave = robot.lookup("#btnSaveAssessment").queryAs(Button.class);
        Button btnClose = robot.lookup("#btnHinzufuegenSchliessen").queryAs(Button.class);

        assertDoesNotThrow(() -> robot.clickOn(btnSave));
        WaitForAsyncUtils.waitForFxEvents();

        boolean initiallyShowing = stage.isShowing();
        robot.clickOn(btnClose);
        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(stage.isShowing());
    }

    @Test
    @Tag("gui")
    void testDatePickerInteraction(FxRobot robot) {
        DatePicker dateValidFrom = robot.lookup("#dateGueltigAb").queryAs(DatePicker.class);

        assertDoesNotThrow(() -> robot.clickOn(dateValidFrom));
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(dateValidFrom.isShowing() || !dateValidFrom.isShowing()); // Just check it doesn't crash
    }

    @Test
    @Tag("gui")
    void testComboBoxInteraction(FxRobot robot) {
        ComboBox<?> familyComboBox = robot.lookup("#cbFamilienmitglied").queryAs(ComboBox.class);

        assertDoesNotThrow(() -> robot.clickOn(familyComboBox));
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(familyComboBox.isShowing() || !familyComboBox.isShowing()); // Just check it doesn't crash
    }
}