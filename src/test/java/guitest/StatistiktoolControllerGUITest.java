package guitest;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import kundenverwaltung.controller.StatistiktoolController;
import kundenverwaltung.dao.*;

import java.net.URL;


@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class StatistiktoolControllerGUITest {

    private Stage stage;
    private StatistiktoolController controller;

    @Mock
    private StatistiktoolDAO statistikDAO;

    @Mock
    private VerteilstelleDAO verteilstelleDAO;

    @Mock
    private HaushaltDAO haushaltDAO;

    @BeforeEach
    void setUp() {
    }

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;

        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/Statistiktool.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        controller = loader.getController();


        stage.setScene(new Scene(root));
        stage.show();
    }


    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#verteilstelleComboBox").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#openSQLQueryToolButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#yearField").queryAs(TextField.class));
        assertNotNull(robot.lookup("#yearResultField").queryAs(TextField.class));
        assertNotNull(robot.lookup("#dynamicGroupContainer").queryAs(VBox.class));
        assertNotNull(robot.lookup("#gesamtsummeField").queryAs(TextField.class));
        assertNotNull(robot.lookup("#addGroupButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#saveButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#printButton").queryAs(Button.class));

        assertNotNull(robot.lookup("#guthabenButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#nationalitätButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#archivierteKundenButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#bescheidButton").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot) {
        Button openSQLButton = robot.lookup("#openSQLQueryToolButton").queryAs(Button.class);
        Button addGroupButton = robot.lookup("#addGroupButton").queryAs(Button.class);
        Button saveButton = robot.lookup("#saveButton").queryAs(Button.class);
        Button printButton = robot.lookup("#printButton").queryAs(Button.class);

        assertEquals("SQL-Abfrage Tool öffnen", openSQLButton.getText());
        assertEquals("Gruppe hinzufügen", addGroupButton.getText());
        assertEquals("Speichern", saveButton.getText());
        assertEquals("Drucken", printButton.getText());

        assertFalse(openSQLButton.isDisabled());
        assertFalse(addGroupButton.isDisabled());
        assertFalse(saveButton.isDisabled());
        assertFalse(printButton.isDisabled());
    }

    @Test
    @Tag("gui")
    void testNavigationButtonsText(FxRobot robot) {
        Button guthabenButton = robot.lookup("#guthabenButton").queryAs(Button.class);
        Button nationalitaetButton = robot.lookup("#nationalitätButton").queryAs(Button.class);
        Button archivierteKundenButton = robot.lookup("#archivierteKundenButton").queryAs(Button.class);
        Button bescheidButton = robot.lookup("#bescheidButton").queryAs(Button.class);

        assertEquals("Guthaben und offene Beträge", guthabenButton.getText());
        assertEquals("Nationalität", nationalitaetButton.getText());
        assertEquals("Kundeninformationen", archivierteKundenButton.getText());
        assertEquals("Bescheidarten Statistik", bescheidButton.getText());
    }


    @Test
    @Tag("gui")
    void testAddGroupButton(FxRobot robot) {
        VBox container = robot.lookup("#dynamicGroupContainer").queryAs(VBox.class);
        int initialChildCount = container.getChildren().size();

        robot.clickOn("#addGroupButton");
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(initialChildCount + 1, container.getChildren().size());
    }

    @Test
    @Tag("gui")
    void testYearFieldInputValidation(FxRobot robot) {
        TextField yearField = robot.lookup("#yearField").queryAs(TextField.class);

        robot.clickOn(yearField).write("2023");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("2023", yearField.getText());

        robot.clickOn(yearField).eraseText(4).write("abcd");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(yearField.getText().isEmpty() || yearField.getText().matches("\\d*"));

        robot.clickOn(yearField).eraseText(yearField.getText().length()).write("20234");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("2023", yearField.getText());
    }

    @Test
    @Tag("gui")
    void testResultFieldsNotEditable(FxRobot robot) {
        TextField yearResultField = robot.lookup("#yearResultField").queryAs(TextField.class);
        TextField gesamtsummeField = robot.lookup("#gesamtsummeField").queryAs(TextField.class);

        assertFalse(yearResultField.isEditable());
        assertFalse(gesamtsummeField.isEditable());
    }

    @Test
    @Tag("gui")
    void testUIElementsVisible(FxRobot robot) {
        ComboBox<String> verteilstelleCombo = robot.lookup("#verteilstelleComboBox").queryAs(ComboBox.class);
        TextField yearField = robot.lookup("#yearField").queryAs(TextField.class);
        Button addGroupButton = robot.lookup("#addGroupButton").queryAs(Button.class);
        Button saveButton = robot.lookup("#saveButton").queryAs(Button.class);
        Button printButton = robot.lookup("#printButton").queryAs(Button.class);

        assertTrue(verteilstelleCombo.isVisible());
        assertTrue(yearField.isVisible());
        assertTrue(addGroupButton.isVisible());
        assertTrue(saveButton.isVisible());
        assertTrue(printButton.isVisible());
    }

    @Test
    @Tag("gui")
    void testMenuItemsExist(FxRobot robot) {
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        assertNotNull(menuBar);

        assertEquals(2, menuBar.getMenus().size());
        assertEquals("Datei", menuBar.getMenus().get(0).getText());
        assertEquals("Hilfe", menuBar.getMenus().get(1).getText());
    }

    @AfterEach
    void tearDown() {
    }
}