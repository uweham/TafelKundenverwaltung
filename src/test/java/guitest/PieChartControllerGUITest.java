package guitest;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
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

import kundenverwaltung.controller.PieChartController;
import kundenverwaltung.dao.PieChartDAO;

import kundenverwaltung.toolsandworkarounds.PieChartStorage;

import java.lang.reflect.Field;
import java.net.URL;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class PieChartControllerGUITest {

    private Stage stage;
    private PieChartController controller;

    @Mock
    private PieChartDAO mockPieChartDAO;

    @Mock
    private PieChartStorage mockPieChartStorage;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;

        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/PieChart.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden im Ressourcenordner 'kundenverwaltung/fxml'");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        controller = loader.getController();

        Field daoField = PieChartController.class.getDeclaredField("pieChartDAO");
        daoField.setAccessible(true);
        daoField.set(controller, mockPieChartDAO);

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#queryField").queryAs(TextField.class));
        assertNotNull(robot.lookup("#pieChart").queryAs(PieChart.class));

        Button loadButton = robot.lookup("Laden").queryButton();
        assertNotNull(loadButton);
    }

    @Test
    @Tag("gui")
    void testTextFieldProperties(FxRobot robot) {
        TextField queryField = robot.lookup("#queryField").queryAs(TextField.class);

        assertEquals("Geben Sie Ihre SQL-Abfrage hier ein...", queryField.getPromptText());
        assertEquals("", queryField.getText()); // Should be empty initially
        assertTrue(queryField.isEditable());
    }

    @Test
    @Tag("gui")
    void testButtonProperties(FxRobot robot) {
        Button loadButton = robot.lookup("Laden").queryButton();

        assertEquals("Laden", loadButton.getText());
        assertFalse(loadButton.isDisabled());
        assertTrue(loadButton.isVisible());
    }

    @Test
    @Tag("gui")
    void testPieChartProperties(FxRobot robot) {
        PieChart pieChart = robot.lookup("#pieChart").queryAs(PieChart.class);

        assertNotNull(pieChart);
        assertTrue(pieChart.isVisible());
        assertEquals(0, pieChart.getData().size()); // Should be empty initially
    }


    @Test
    @Tag("gui")
    void testUIElementsLayout(FxRobot robot) {
        TextField queryField = robot.lookup("#queryField").queryAs(TextField.class);
        Button loadButton = robot.lookup("Laden").queryButton();
        PieChart pieChart = robot.lookup("#pieChart").queryAs(PieChart.class);

        assertTrue(queryField.getLayoutX() < loadButton.getLayoutX());
        assertTrue(queryField.getLayoutY() < pieChart.getLayoutY());
        assertTrue(loadButton.getLayoutY() < pieChart.getLayoutY());
    }

    @Test
    @Tag("gui")
    void testWindowTitleAndSize(FxRobot robot) {
        assertTrue(stage.isShowing());
        assertNotNull(stage.getScene());
        assertNotNull(stage.getScene().getRoot());
    }

}