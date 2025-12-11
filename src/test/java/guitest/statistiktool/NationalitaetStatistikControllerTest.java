package guitest.statistiktool;

import javafx.scene.control.*;
import javafx.stage.Stage;
import kundenverwaltung.controller.statistiktool.NationalitaetStatistikController;
import kundenverwaltung.model.Nation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NationalitaetStatistikControllerTest extends ApplicationTest {

    private NationalitaetStatistikController controller;
    private ComboBox<String> verteilstelleComboBox;
    private TableView<Nation> nationalitaetTableView;
    private Button loadButton;

    private kundenverwaltung.dao.NationDAO mockNationDAO;

    @Override
    public void start(Stage stage) throws Exception {
        mockNationDAO = mock(kundenverwaltung.dao.NationDAO.class);

        when(mockNationDAO.getAllNationenMitAnzahl())
                .thenReturn((ArrayList<Nation>) new ArrayList<Nation>());
        doNothing().when(mockNationDAO).saveNationStatistics(any(ArrayList.class));

        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/kundenverwaltung/fxml/statistiktool/NationalitaetStatistik.fxml")
        );
        javafx.scene.layout.VBox root = loader.load();
        controller = loader.getController();

        setDAOviaReflection();

        javafx.scene.Scene scene = new javafx.scene.Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void setDAOviaReflection() {
        try {
            var daoField = controller.getClass().getDeclaredField("nationDAO");
            daoField.setAccessible(true);
            daoField.set(controller, mockNationDAO);
        } catch (Exception e) {
            System.out.println("Could not inject mock via reflection: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setUp() {
        WaitForAsyncUtils.waitForFxEvents();

        verteilstelleComboBox = lookup("#verteilstelleComboBox").query();
        nationalitaetTableView = lookup("#nationalitaetTableView").query();
        loadButton = lookup("#loadButton").query();
    }

    @Test
    @Tag("gui")
    public void testInitialState() {
        assertAll(
                () -> assertNotNull(verteilstelleComboBox, "Verteilstelle ComboBox"),
                () -> assertNotNull(nationalitaetTableView, "Nationalität TableView"),
                () -> assertNotNull(loadButton, "Load Button")
        );

        assertFalse(verteilstelleComboBox.getItems().isEmpty());
    }

    @Test
    @Tag("gui")
    public void testVerteilstelleDropdownContent() {
        String[] expectedVerteilstellen = {
                "Hauptstelle", "AWO", "Stroot", "Bringdienst",
                "Freren", "Haren", "Lathen", "Spelle", "Twist"
        };

        assertEquals(expectedVerteilstellen.length, verteilstelleComboBox.getItems().size());

        for (String verteilstelle : expectedVerteilstellen) {
            assertTrue(verteilstelleComboBox.getItems().contains(verteilstelle));
        }
    }

    @Test
    @Tag("gui")
    public void testTableStructure() {
        assertEquals(2, nationalitaetTableView.getColumns().size());

        String[] expectedColumns = {"Nationalität", "Anzahl der Personen"};
        String[] expectedIds = {"name", "anzahl"};

        for (int i = 0; i < expectedColumns.length; i++) {
            assertEquals(expectedColumns[i], nationalitaetTableView.getColumns().get(i).getText());
            assertEquals(expectedIds[i], nationalitaetTableView.getColumns().get(i).getId());
        }
    }

    @Test
    @Tag("gui")
    public void testVerteilstelleSelection() {
        interact(() -> {
            verteilstelleComboBox.setValue("Hauptstelle");
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("Hauptstelle", verteilstelleComboBox.getValue());
    }

    @Test
    @Tag("gui")
    public void testLoadButtonFunctionality() {
        interact(() -> {
            verteilstelleComboBox.setValue("Hauptstelle");
        });

        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            loadButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        verify(mockNationDAO, atLeastOnce()).getAllNationenMitAnzahl();
        verify(mockNationDAO, atLeastOnce()).saveNationStatistics(any(ArrayList.class));
    }

    @Test
    @Tag("gui")
    public void testLoadButtonWithoutSelection() {
        interact(() -> {
            verteilstelleComboBox.getSelectionModel().clearSelection();
        });

        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            loadButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        verify(mockNationDAO, never()).getAllNationenMitAnzahl();
        verify(mockNationDAO, never()).saveNationStatistics(any(ArrayList.class));
    }

    @Test
    @Tag("gui")
    public void testTableDataLoading() {
        ArrayList<Nation> mockNations = new ArrayList<>();

        try {
            Nation nation1 = createNation("Deutschland", 100);
            Nation nation2 = createNation("Türkei", 50);
            Nation nation3 = createNation("Polen", 30);

            mockNations.add(nation1);
            mockNations.add(nation2);
            mockNations.add(nation3);
        } catch (Exception e) {
            fail("Could not create Nation objects: " + e.getMessage());
        }

        when(mockNationDAO.getAllNationenMitAnzahl()).thenReturn(mockNations);

        interact(() -> {
            verteilstelleComboBox.setValue("Hauptstelle");
            loadButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertNotNull(nationalitaetTableView.getItems());
        assertEquals(3, nationalitaetTableView.getItems().size());

        verify(mockNationDAO, atLeastOnce()).getAllNationenMitAnzahl();
        verify(mockNationDAO, atLeastOnce()).saveNationStatistics(mockNations);
    }

    private Nation createNation(String name, int anzahl) {
        try {
            Nation nation = Nation.class.getDeclaredConstructor().newInstance();

            java.lang.reflect.Field nameField = Nation.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(nation, name);

            java.lang.reflect.Field anzahlField = Nation.class.getDeclaredField("anzahl");
            anzahlField.setAccessible(true);
            anzahlField.set(nation, anzahl);

            return nation;
        } catch (Exception e) {
            Nation mockNation = mock(Nation.class);
            when(mockNation.getName()).thenReturn(name);
            when(mockNation.getAnzahl()).thenReturn(anzahl);
            return mockNation;
        }
    }

    @Test
    @Tag("gui")
    public void testUIComponentsVisibility() {
        assertTrue(verteilstelleComboBox.isVisible());
        assertTrue(nationalitaetTableView.isVisible());
        assertTrue(loadButton.isVisible());

        MenuBar menuBar = lookup(".menu-bar").query();
        assertNotNull(menuBar);
        assertTrue(menuBar.isVisible());
    }

    @Test
    @Tag("gui")
    public void testMenuFunctionality() {
        MenuBar menuBar = lookup(".menu-bar").query();
        assertNotNull(menuBar);

        assertFalse(menuBar.getMenus().isEmpty());
        Menu menu = menuBar.getMenus().get(0);
        assertEquals("Datei", menu.getText());

        assertFalse(menu.getItems().isEmpty());
        MenuItem menuItem = menu.getItems().get(0);
        assertEquals("Programm beenden", menuItem.getText());
    }

    @Test
    @Tag("gui")
    public void testExitViaStage() {
        Stage stage = (Stage) nationalitaetTableView.getScene().getWindow();
        assertTrue(stage.isShowing(), "Fenster sollte anfangs geöffnet sein");

        interact(() -> stage.close());
        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(stage.isShowing(), "Fenster sollte geschlossen sein");
    }

    @Test
    @Tag("gui")
    public void testMenuItemAction() {
        MenuBar menuBar = lookup(".menu-bar").query();
        Menu menu = menuBar.getMenus().get(0);
        MenuItem exitMenuItem = menu.getItems().get(0);

        assertNotNull(exitMenuItem.getOnAction(), "MenuItem sollte Action-Handler haben");

        assertDoesNotThrow(() -> {
            interact(() -> exitMenuItem.getOnAction().handle(null));
            WaitForAsyncUtils.waitForFxEvents();
        });
    }

    @Test
    @Tag("gui")
    public void testEmptyDataHandling() {
        when(mockNationDAO.getAllNationenMitAnzahl()).thenReturn(new ArrayList<Nation>());

        interact(() -> {
            verteilstelleComboBox.setValue("Hauptstelle");
            loadButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(nationalitaetTableView.getItems().isEmpty());
    }
}