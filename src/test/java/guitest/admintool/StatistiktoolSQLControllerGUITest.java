package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;

import java.util.ArrayList;

@ExtendWith(ApplicationExtension.class)
public class StatistiktoolSQLControllerGUITest
{

    private TextArea sqlArea;
    private TableView<ObservableList<String>> table;
    private PieChart pie;
    private ComboBox<String> xAxisDropdown;
    private ComboBox<String> yAxisDropdown;
    private ComboBox<String> sqlDropdown;
    private Button executeQueryButton;
    private Button loadRelationsButton;
    private Button loadTablesColsButton;
    private Button addCustomSQLButton;
    private Button deleteQueryButton;
    private Button generatePieButton;
    private Button savePieButton;
    private Button resetButton;

    @Start
    private void start(Stage stage)
    {
        sqlArea = new TextArea();
        sqlArea.setId("sqlQueryArea");

        table = new TableView<>();
        table.setId("queryResultTable");

        pie = new PieChart();
        pie.setId("pieChart");

        xAxisDropdown = new ComboBox<>();
        xAxisDropdown.setId("xAxisDropdown");

        yAxisDropdown = new ComboBox<>();
        yAxisDropdown.setId("yAxisDropdown");

        sqlDropdown = new ComboBox<>();
        sqlDropdown.setId("sqlDropdown");

        executeQueryButton = new Button("Abfrage ausführen");
        executeQueryButton.setId("executeQueryButton");

        loadRelationsButton = new Button("Relationen laden");
        loadRelationsButton.setId("loadRelationsButton");

        loadTablesColsButton = new Button("Tabellen und Spalten laden");
        loadTablesColsButton.setId("loadTablesAndColumnsButton");

        addCustomSQLButton = new Button("SQL-Abfrage hinzufügen");
        addCustomSQLButton.setId("addCustomSQLQueryButton");

        deleteQueryButton = new Button("SQL-Abfrage löschen");
        deleteQueryButton.setId("deleteQueryButton");

        generatePieButton = new Button("PieChart erstellen");
        generatePieButton.setId("generatePieChartButton");

        savePieButton = new Button("PieChart speichern");
        savePieButton.setId("savePieChartButton");

        resetButton = new Button("Abfrage zurücksetzen");
        resetButton.setId("resetButton");

        VBox root = new VBox(10,
                sqlArea, table, pie,
                sqlDropdown, xAxisDropdown, yAxisDropdown,
                executeQueryButton, loadRelationsButton, loadTablesColsButton,
                addCustomSQLButton, deleteQueryButton, generatePieButton, savePieButton, resetButton
        );

        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot)
    {
        assertNotNull(robot.lookup("#sqlQueryArea").queryAs(TextArea.class));
        assertNotNull(robot.lookup("#queryResultTable").queryAs(TableView.class));
        assertNotNull(robot.lookup("#pieChart").queryAs(PieChart.class));
        assertNotNull(robot.lookup("#executeQueryButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#loadRelationsButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#loadTablesAndColumnsButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#addCustomSQLQueryButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#deleteQueryButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#generatePieChartButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#savePieChartButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#resetButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#xAxisDropdown").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#yAxisDropdown").queryAs(ComboBox.class));
        assertNotNull(robot.lookup("#sqlDropdown").queryAs(ComboBox.class));
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot)
    {
        assertEquals("Abfrage ausführen", executeQueryButton.getText());
        assertEquals("Relationen laden", loadRelationsButton.getText());
        assertEquals("Tabellen und Spalten laden", loadTablesColsButton.getText());
        assertEquals("SQL-Abfrage hinzufügen", addCustomSQLButton.getText());
        assertEquals("SQL-Abfrage löschen", deleteQueryButton.getText());
        assertEquals("PieChart erstellen", generatePieButton.getText());
        assertEquals("PieChart speichern", savePieButton.getText());
        assertEquals("Abfrage zurücksetzen", resetButton.getText());
    }

    @Test
    @Tag("gui")
    void testInitialState(FxRobot robot)
    {
        robot.interact(() ->
        {
            assertTrue(sqlArea.getText().isEmpty());
            assertTrue(table.getItems().isEmpty());
            assertEquals(0, table.getColumns().size());
            assertTrue(pie.getData().isEmpty());
            assertTrue(sqlDropdown.getItems().isEmpty());
            assertTrue(xAxisDropdown.getItems().isEmpty());
            assertTrue(yAxisDropdown.getItems().isEmpty());
        });
    }

    @Test
    @Tag("gui")
    void testNonBlockingButtonsClickable(FxRobot robot)
    {
        assertDoesNotThrow(() -> executeQueryButton.fire());
        assertDoesNotThrow(() -> loadRelationsButton.fire());
        assertDoesNotThrow(() -> loadTablesColsButton.fire());
        assertDoesNotThrow(() -> addCustomSQLButton.fire());
        assertDoesNotThrow(() -> deleteQueryButton.fire());
        assertDoesNotThrow(() -> generatePieButton.fire());
        assertDoesNotThrow(() -> savePieButton.fire());
        assertDoesNotThrow(() -> resetButton.fire());
    }

}
