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
import javafx.stage.Stage;

import kundenverwaltung.controller.KundenlisteErstellenController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class KundenlisteErstellenControllerGUITest {

    private Stage stage;
    private KundenlisteErstellenController controller;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/KundenlisteErstellen.fxml"));
        Parent root = loader.load();

        controller = loader.getController();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testMainNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeader").queryAs(Label.class));

        assertNotNull(robot.lookup("#labelTemplate").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelDistributionPoint").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelProductType").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelOrderBy").queryAs(Label.class));

        assertNotNull(robot.lookup("#cbVorlage").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbVerteilstelle").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbWarentyp").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbSortierenNach").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbSortierReihenfolge").queryAs(ComboBox.class));

        assertNotNull(robot.lookup("#cbxAutomatischEinstellen").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxArchivierteKunden").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxEinstellungenAlsStandard").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxBescheidarten").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#cbxGesperrteKunden").queryAs(CheckBox.class));

        assertNotNull(robot.lookup("#buttonRun").queryAs(Button.class));
        assertNotNull(robot.lookup("#buttenCancel").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testLabelContents(FxRobot robot) {
        Label headerLabel = robot.lookup("#labelHeader").queryAs(Label.class);
        assertTrue(headerLabel.getText().contains("Geben Sie Ihre gesamten Kunden als Liste aus"));
        assertTrue(headerLabel.getText().contains("speichern oder drucken"));

        assertEquals("Vorlage:", robot.lookup("#labelTemplate").queryAs(Label.class).getText());
        assertEquals("Verteilstelle:", robot.lookup("#labelDistributionPoint").queryAs(Label.class).getText());
        assertEquals("Warentyp:", robot.lookup("#labelProductType").queryAs(Label.class).getText());
        assertEquals("Sortieren nach:", robot.lookup("#labelOrderBy").queryAs(Label.class).getText());
    }

    @Test
    @Tag("gui")
    void testCheckBoxContents(FxRobot robot) {
        CheckBox autoAdjust = robot.lookup("#cbxAutomatischEinstellen").queryAs(CheckBox.class);
        CheckBox archivedCustomers = robot.lookup("#cbxArchivierteKunden").queryAs(CheckBox.class);
        CheckBox saveAsDefault = robot.lookup("#cbxEinstellungenAlsStandard").queryAs(CheckBox.class);
        CheckBox noDecisionTypes = robot.lookup("#cbxBescheidarten").queryAs(CheckBox.class);
        CheckBox blockedCustomers = robot.lookup("#cbxGesperrteKunden").queryAs(CheckBox.class);

        assertEquals("automatisch \neinstellen", autoAdjust.getText());
        assertEquals("Auch archivierte Kunden ausgeben", archivedCustomers.getText());
        assertEquals("Diese Einstellungen als Standard speichern", saveAsDefault.getText());
        assertEquals("Bescheidarten nicht ausgeben", noDecisionTypes.getText());
        assertEquals("Auch gesperrte Kunden ausgeben", blockedCustomers.getText());
    }

    @Test
    @Tag("gui")
    void testButtonContents(FxRobot robot) {
        Button runButton = robot.lookup("#buttonRun").queryAs(Button.class);
        Button cancelButton = robot.lookup("#buttenCancel").queryAs(Button.class);

        assertEquals("Ausführen", runButton.getText());
        assertEquals("Abbrechen", cancelButton.getText());
    }

    @Test
    @Tag("gui")
    void testComboBoxDefaults(FxRobot robot) {
        ComboBox<?> templateCombo = robot.lookup("#cbVorlage").queryAs(ComboBox.class);
        ComboBox<?> distributionCombo = robot.lookup("#cbVerteilstelle").queryAs(ComboBox.class);
        ComboBox<?> productTypeCombo = robot.lookup("#cbWarentyp").queryAs(ComboBox.class);
        ComboBox<?> sortByCombo = robot.lookup("#cbSortierenNach").queryAs(ComboBox.class);
        ComboBox<?> sortOrderCombo = robot.lookup("#cbSortierReihenfolge").queryAs(ComboBox.class);

        assertNotNull(templateCombo.getValue());
        assertNotNull(distributionCombo.getValue());
        assertNotNull(productTypeCombo.getValue());
        assertNotNull(sortByCombo.getValue());
        assertNotNull(sortOrderCombo.getValue());
    }

    @Test
    @Tag("gui")
    void testCheckBoxDefaults(FxRobot robot) {
        CheckBox autoAdjust = robot.lookup("#cbxAutomatischEinstellen").queryAs(CheckBox.class);
        CheckBox archivedCustomers = robot.lookup("#cbxArchivierteKunden").queryAs(CheckBox.class);
        CheckBox saveAsDefault = robot.lookup("#cbxEinstellungenAlsStandard").queryAs(CheckBox.class);
        CheckBox noDecisionTypes = robot.lookup("#cbxBescheidarten").queryAs(CheckBox.class);
        CheckBox blockedCustomers = robot.lookup("#cbxGesperrteKunden").queryAs(CheckBox.class);

        assertFalse(autoAdjust.isSelected());
        assertFalse(archivedCustomers.isSelected());
        assertFalse(saveAsDefault.isSelected());
        assertFalse(noDecisionTypes.isSelected());
        assertFalse(blockedCustomers.isSelected());
    }

    @Test
    @Tag("gui")
    void testButtonVisibility(FxRobot robot) {
        Button runButton = robot.lookup("#buttonRun").queryAs(Button.class);
        Button cancelButton = robot.lookup("#buttenCancel").queryAs(Button.class);

        assertTrue(runButton.isVisible());
        assertTrue(cancelButton.isVisible());
    }

    @Test
    @Tag("gui")
    void testButtonDisableState(FxRobot robot) {
        Button runButton = robot.lookup("#buttonRun").queryAs(Button.class);
        Button cancelButton = robot.lookup("#buttenCancel").queryAs(Button.class);

        assertFalse(runButton.isDisabled());
        assertFalse(cancelButton.isDisabled());
    }

    @Test
    @Tag("gui")
    void testHeaderStyle(FxRobot robot) {
        Label headerLabel = robot.lookup("#labelHeader").queryAs(Label.class);

        assertTrue(headerLabel.getTextFill().toString().contains("ee6f00"),
                "Header should have orange text color");
        assertEquals(javafx.geometry.Pos.CENTER, headerLabel.getAlignment());
        assertEquals(javafx.scene.text.TextAlignment.CENTER, headerLabel.getTextAlignment());
    }

    @Test
    @Tag("gui")
    void testCheckBoxToggle(FxRobot robot) {
        CheckBox archivedCustomers = robot.lookup("#cbxArchivierteKunden").queryAs(CheckBox.class);
        CheckBox blockedCustomers = robot.lookup("#cbxGesperrteKunden").queryAs(CheckBox.class);

        assertFalse(archivedCustomers.isSelected());
        assertFalse(blockedCustomers.isSelected());

        robot.clickOn(archivedCustomers);
        assertTrue(archivedCustomers.isSelected());

        robot.clickOn(blockedCustomers);
        assertTrue(blockedCustomers.isSelected());

        robot.clickOn(archivedCustomers);
        assertFalse(archivedCustomers.isSelected());

        robot.clickOn(blockedCustomers);
        assertFalse(blockedCustomers.isSelected());
    }

    @Test
    @Tag("gui")
    void testComboBoxInteraction(FxRobot robot) {
        ComboBox<?> sortOrderCombo = robot.lookup("#cbSortierReihenfolge").queryAs(ComboBox.class);

        assertEquals("aufsteigend", sortOrderCombo.getValue().toString());

        robot.clickOn(sortOrderCombo);

        assertTrue(sortOrderCombo.getItems().contains("aufsteigend"));
        assertTrue(sortOrderCombo.getItems().contains("absteigend"));
    }

    @Test
    @Tag("gui")
    void testLayoutStructure(FxRobot robot) {
        assertTrue(robot.lookup("#labelTemplate").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#cbVorlage").queryAs(ComboBox.class).isVisible());
        assertTrue(robot.lookup("#labelDistributionPoint").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#cbVerteilstelle").queryAs(ComboBox.class).isVisible());
        assertTrue(robot.lookup("#labelProductType").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#cbWarentyp").queryAs(ComboBox.class).isVisible());
    }

    @Test
    @Tag("gui")
    void testButtonActions(FxRobot robot) {
        Button runButton = robot.lookup("#buttonRun").queryAs(Button.class);
        Button cancelButton = robot.lookup("#buttenCancel").queryAs(Button.class);

        assertNotNull(runButton.getOnAction());
        assertNotNull(cancelButton.getOnAction());
    }

    @Test
    @Tag("gui")
    void testFontSizes(FxRobot robot) {
        CheckBox saveAsDefault = robot.lookup("#cbxEinstellungenAlsStandard").queryAs(CheckBox.class);
        Button runButton = robot.lookup("#buttonRun").queryAs(Button.class);
        Button cancelButton = robot.lookup("#buttenCancel").queryAs(Button.class);

        assertTrue(saveAsDefault.getFont().getSize() == 11.0);

        assertTrue(runButton.getFont().getSize() == 13.0);
        assertTrue(cancelButton.getFont().getSize() == 13.0);
    }

    @Test
    @Tag("gui")
    void testWindowProperties(FxRobot robot) {
        assertNotNull(stage.getTitle());
        assertTrue(stage.isShowing());
        assertTrue(stage.getWidth() > 0);
        assertTrue(stage.getHeight() > 0);

        assertEquals(479.0, stage.getWidth(), 1.0);
        assertEquals(499.0, stage.getHeight(), 1.0);
    }

    @Test
    @Tag("gui")
    void testSeparatorExistence(FxRobot robot) {
        assertTrue(robot.lookup(".separator").tryQuery().isPresent());
    }
}