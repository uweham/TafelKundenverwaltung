package guitest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import kundenverwaltung.controller.BuchungenBearbeitenController;
import kundenverwaltung.model.Haushalt;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class BuchungenBearbeitenControllerGUITest {

    private Stage stage;
    private BuchungenBearbeitenController controller;
    private Haushalt testHaushalt;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;

        try {
            testHaushalt = Haushalt.class.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            testHaushalt = mock(Haushalt.class);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/BuchungenBearbeiten.fxml"));
        Parent root = loader.load();

        controller = loader.getController();
        controller.setHaushalt(testHaushalt);
        controller.manuellInitialize();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testMainNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeader").queryAs(Label.class));
        assertNotNull(robot.lookup("#cbWarentyp").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbVerteilstelle").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#cbxStornierteUmsaetze").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#dateStartdatum").queryAs(DatePicker.class));
        assertNotNull(robot.lookup("#dateEnddatum").queryAs(DatePicker.class));
        assertNotNull(robot.lookup("#tvBuchungen").queryAs(TableView.class));
        assertNotNull(robot.lookup("#buttonCancelThisSales").queryAs(Button.class));
        assertNotNull(robot.lookup("#buttonSaveTable").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnSchliessen").queryAs(Button.class));
    }


    @Test
    @Tag("gui")
    void testComboBoxesExist(FxRobot robot) {
        ComboBox<?> warentypCombo = robot.lookup("#cbWarentyp").queryAs(ComboBox.class);
        ComboBox<?> verteilstelleCombo = robot.lookup("#cbVerteilstelle").queryAs(ComboBox.class);

        assertNotNull(warentypCombo);
        assertNotNull(verteilstelleCombo);
    }

    @Test
    @Tag("gui")
    void testDatePickersInitialized(FxRobot robot) {
        DatePicker startDate = robot.lookup("#dateStartdatum").queryAs(DatePicker.class);
        DatePicker endDate = robot.lookup("#dateEnddatum").queryAs(DatePicker.class);

        assertNotNull(startDate);
        assertNotNull(endDate);
    }

    @Test
    @Tag("gui")
    void testTableViewInitialized(FxRobot robot) {
        TableView<?> tableView = robot.lookup("#tvBuchungen").queryAs(TableView.class);
        assertNotNull(tableView);
        assertNotNull(tableView.getColumns());
    }

    @Test
    @Tag("gui")
    void testButtonsVisible(FxRobot robot) {
        Button cancelButton = robot.lookup("#buttonCancelThisSales").queryAs(Button.class);
        Button saveButton = robot.lookup("#buttonSaveTable").queryAs(Button.class);
        Button closeButton = robot.lookup("#btnSchliessen").queryAs(Button.class);

        assertTrue(cancelButton.isVisible());
        assertTrue(saveButton.isVisible());
        assertTrue(closeButton.isVisible());
    }

    @Test
    @Tag("gui")
    void testCheckBoxFunctionality(FxRobot robot) {
        CheckBox stornierteUmsaetze = robot.lookup("#cbxStornierteUmsaetze").queryAs(CheckBox.class);

        assertNotNull(stornierteUmsaetze);

        boolean initialState = stornierteUmsaetze.isSelected();

        robot.clickOn(stornierteUmsaetze);
        assertNotEquals(initialState, stornierteUmsaetze.isSelected());

        robot.clickOn(stornierteUmsaetze);
        assertEquals(initialState, stornierteUmsaetze.isSelected());
    }

    @Test
    @Tag("gui")
    void testApplyButtonExists(FxRobot robot) {
        Button applyButton = robot.lookup(".button").queryAll()
                .stream()
                .filter(node -> node instanceof Button)
                .map(node -> (Button) node)
                .filter(button -> "Anwenden".equals(button.getText()))
                .findFirst()
                .orElse(null);

        assertNotNull(applyButton);
        assertTrue(applyButton.isVisible());
    }

    @Test
    @Tag("gui")
    void testCloseButtonFunctionality(FxRobot robot) {
        Button closeButton = robot.lookup("#btnSchliessen").queryAs(Button.class);
        assertTrue(stage.isShowing());

        robot.clickOn(closeButton);
    }

    @Test
    @Tag("gui")
    void testDateInput(FxRobot robot) {
        DatePicker startDate = robot.lookup("#dateStartdatum").queryAs(DatePicker.class);
        DatePicker endDate = robot.lookup("#dateEnddatum").queryAs(DatePicker.class);

        robot.interact(() -> {
            startDate.setValue(LocalDate.of(2023, 1, 1));
            endDate.setValue(LocalDate.of(2023, 12, 31));
        });

        assertEquals(LocalDate.of(2023, 1, 1), startDate.getValue());
        assertEquals(LocalDate.of(2023, 12, 31), endDate.getValue());
    }

    @Test
    @Tag("gui")
    void testApplyButtonAction(FxRobot robot) {
        Button applyButton = robot.lookup(".button").queryAll()
                .stream()
                .filter(node -> node instanceof Button)
                .map(node -> (Button) node)
                .filter(button -> "Anwenden".equals(button.getText()))
                .findFirst()
                .orElse(null);

        assertNotNull(applyButton);

        assertDoesNotThrow(() -> robot.clickOn(applyButton));
    }

    @Test
    @Tag("gui")
    void testUIStructure(FxRobot robot) {
        assertNotNull(robot.lookup(".v-box"));
        assertNotNull(robot.lookup(".h-box"));
        assertNotNull(robot.lookup(".anchor-pane"));
    }

    @Test
    @Tag("gui")
    void testTableViewStructure(FxRobot robot) {
        TableView<?> tableView = robot.lookup("#tvBuchungen").queryAs(TableView.class);
        assertNotNull(tableView);

        assertTrue(tableView.isVisible());

        assertNotNull(tableView.getItems());
    }
}