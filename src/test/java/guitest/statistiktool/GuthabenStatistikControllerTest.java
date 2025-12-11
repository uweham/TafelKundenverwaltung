package guitest.statistiktool;

import javafx.scene.control.*;
import javafx.stage.Stage;
import kundenverwaltung.controller.statistiktool.GuthabenStatistikController;
import kundenverwaltung.model.Einkauf;
import kundenverwaltung.model.Verteilstelle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GuthabenStatistikControllerTest extends ApplicationTest {

    private GuthabenStatistikController controller;
    private ComboBox<Verteilstelle> verteilstelleComboBox;
    private ComboBox<String> typeComboBox;
    private TableView<Einkauf> resultTableView;
    private Button generateSQLButton;
    private Button loadDataButton;

    private kundenverwaltung.dao.EinkaufDAO mockEinkaufDAO;
    private kundenverwaltung.dao.VerteilstelleDAO mockVerteilstelleDAO;

    @Override
    public void start(Stage stage) throws Exception {
        mockEinkaufDAO = mock(kundenverwaltung.dao.EinkaufDAO.class);
        mockVerteilstelleDAO = mock(kundenverwaltung.dao.VerteilstelleDAO.class);

        Verteilstelle mockVerteilstelle = mock(Verteilstelle.class);
        when(mockVerteilstelle.getBezeichnung()).thenReturn("Test Verteilstelle");
        when(mockVerteilstelle.toString()).thenReturn("Test Verteilstelle");

        when(mockVerteilstelleDAO.readAll()).thenReturn(java.util.Arrays.asList(mockVerteilstelle));
        when(mockEinkaufDAO.generateSQLQuery(anyString(), anyString())).thenReturn("SELECT * FROM test");
        when(mockEinkaufDAO.getEinkaeufeByQuery(anyString(), anyString()))
                .thenReturn(java.util.Arrays.asList(mock(Einkauf.class)));

        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/kundenverwaltung/fxml/statistiktool/GuthabenStatistik.fxml")
        );
        javafx.scene.layout.VBox root = loader.load();
        controller = loader.getController();

        setDAOsViaReflection();

        javafx.scene.Scene scene = new javafx.scene.Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void setDAOsViaReflection() {
        try {
            var einkaufField = controller.getClass().getDeclaredField("einkaufDAO");
            einkaufField.setAccessible(true);
            einkaufField.set(controller, mockEinkaufDAO);

            var verteilstelleField = controller.getClass().getDeclaredField("verteilstelleDAO");
            verteilstelleField.setAccessible(true);
            verteilstelleField.set(controller, mockVerteilstelleDAO);

        } catch (Exception e) {
            System.out.println("Could not inject mocks via reflection: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setUp() {
        WaitForAsyncUtils.waitForFxEvents();

        verteilstelleComboBox = lookup("#verteilstelleComboBox").query();
        typeComboBox = lookup("#typeComboBox").query();
        resultTableView = lookup("#resultTableView").query();
        generateSQLButton = lookup("#generateSQLButton").query();
        loadDataButton = lookup("#loadDataButton").query();
    }

    @Test
    @Tag("gui")
    public void testInitialState() {
        assertAll(
                () -> assertNotNull(verteilstelleComboBox, "Verteilstelle ComboBox"),
                () -> assertNotNull(typeComboBox, "Type ComboBox"),
                () -> assertNotNull(resultTableView, "Result TableView"),
                () -> assertNotNull(generateSQLButton, "Generate SQL Button"),
                () -> assertNotNull(loadDataButton, "Load Data Button")
        );

        assertEquals("SQL-Abfrage generieren", generateSQLButton.getText());
        assertEquals("Daten laden", loadDataButton.getText());
        assertTrue(generateSQLButton.getStyle().contains("#9098d6"));
        assertTrue(loadDataButton.getStyle().contains("#80c47c"));
    }

    @Test
    @Tag("gui")
    public void testComboBoxContent() {
        assertEquals(2, typeComboBox.getItems().size());
        assertTrue(typeComboBox.getItems().contains("Offene Beträge"));
        assertTrue(typeComboBox.getItems().contains("Guthaben"));

        WaitForAsyncUtils.waitForFxEvents(100);
        assertFalse(verteilstelleComboBox.getItems().isEmpty());
    }

    @Test
    @Tag("gui")
    public void testTableStructure() {
        assertEquals(8, resultTableView.getColumns().size());

        String[] expectedColumns = {
                "Einkauf ID", "Warentyp", "Kunde", "Saldo",
                "Summe Einkauf", "Summe Zahlung", "Anzahl Kinder", "Anzahl Erwachsene"
        };

        for (int i = 0; i < expectedColumns.length; i++) {
            assertEquals(expectedColumns[i], resultTableView.getColumns().get(i).getText());
        }
    }

    @Test
    @Tag("gui")
    public void testGenerateSQLButtonWithSelection() {
        interact(() -> {
            if (!verteilstelleComboBox.getItems().isEmpty()) {
                verteilstelleComboBox.getSelectionModel().selectFirst();
            }
            typeComboBox.setValue("Offene Beträge");
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertDoesNotThrow(() -> {
            interact(() -> generateSQLButton.fire());
            WaitForAsyncUtils.waitForFxEvents();
        });

        verify(mockEinkaufDAO, atLeastOnce()).generateSQLQuery(anyString(), anyString());
    }

    @Test
    @Tag("gui")
    public void testLoadDataButtonWithSelection() {
        interact(() -> {
            if (!verteilstelleComboBox.getItems().isEmpty()) {
                verteilstelleComboBox.getSelectionModel().selectFirst();
            }
            typeComboBox.setValue("Guthaben");
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertDoesNotThrow(() -> {
            interact(() -> loadDataButton.fire());
            WaitForAsyncUtils.waitForFxEvents();
        });

        verify(mockEinkaufDAO, atLeastOnce()).getEinkaeufeByQuery(anyString(), anyString());
    }

    @Test
    @Tag("gui")
    public void testButtonsWithoutSelection() {
        interact(() -> {
            verteilstelleComboBox.getSelectionModel().clearSelection();
            typeComboBox.getSelectionModel().clearSelection();
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertDoesNotThrow(() -> {
            interact(() -> generateSQLButton.fire());
            WaitForAsyncUtils.waitForFxEvents();
        });

        assertDoesNotThrow(() -> {
            interact(() -> loadDataButton.fire());
            WaitForAsyncUtils.waitForFxEvents();
        });
    }

    @Test
    @Tag("gui")
    public void testTableColumnIds() {
        String[] expectedIds = {
                "einkaufId", "warentyp", "kundeName", "saldo",
                "summeEinkauf", "summeZahlung", "anzahlKinder", "anzahlErwachsene"
        };

        for (int i = 0; i < expectedIds.length; i++) {
            assertEquals(expectedIds[i], resultTableView.getColumns().get(i).getId());
        }
    }

    @Test
    @Tag("gui")
    public void testUIComponentsVisibility() {
        assertTrue(verteilstelleComboBox.isVisible());
        assertTrue(typeComboBox.isVisible());
        assertTrue(resultTableView.isVisible());
        assertTrue(generateSQLButton.isVisible());
        assertTrue(loadDataButton.isVisible());
    }

    @Test
    @Tag("gui")
    public void testComboBoxInteractions() {
        interact(() -> {
            typeComboBox.show();
            verteilstelleComboBox.show();
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(typeComboBox.isShowing());
        assertTrue(verteilstelleComboBox.isShowing());

        interact(() -> {
            typeComboBox.setValue("Offene Beträge");
            if (!verteilstelleComboBox.getItems().isEmpty()) {
                verteilstelleComboBox.getSelectionModel().selectFirst();
            }
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("Offene Beträge", typeComboBox.getValue());
        assertNotNull(verteilstelleComboBox.getValue());
    }


    @Test
    @Tag("gui")
    public void testExitFunctionality() {
        Stage stage = (Stage) resultTableView.getScene().getWindow();
        assertTrue(stage.isShowing(), "Fenster sollte anfangs geöffnet sein");

        interact(() -> stage.close());
        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(stage.isShowing(), "Fenster sollte geschlossen sein");
    }
}