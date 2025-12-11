package guitest.statistiktool;

import javafx.scene.control.*;
import javafx.stage.Stage;
import kundenverwaltung.controller.statistiktool.JahresuebersichtStatistikController;
import kundenverwaltung.model.statistiktool.Jahresuebersicht;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class JahresuebersichtStatistikControllerTest extends ApplicationTest {

    private JahresuebersichtStatistikController controller;
    private ComboBox<Integer> yearDropdown;
    private TableView<Jahresuebersicht> jahresuebersichtTable;

    private kundenverwaltung.dao.JahresuebersichtDAO mockJahresuebersichtDAO;

    @Override
    public void start(Stage stage) throws Exception {
        mockJahresuebersichtDAO = mock(kundenverwaltung.dao.JahresuebersichtDAO.class);

        when(mockJahresuebersichtDAO.getJahresuebersichtByYear(anyInt()))
                .thenReturn(javafx.collections.FXCollections.observableArrayList(mock(Jahresuebersicht.class)));

        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/kundenverwaltung/fxml/statistiktool/JahresuebersichtStatistik.fxml")
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
            var daoField = controller.getClass().getDeclaredField("jahresuebersichtDAO");
            daoField.setAccessible(true);
            daoField.set(controller, mockJahresuebersichtDAO);
        } catch (Exception e) {
            System.out.println("Could not inject mock via reflection: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setUp() {
        WaitForAsyncUtils.waitForFxEvents();

        yearDropdown = lookup("#yearDropdown").query();
        jahresuebersichtTable = lookup("#jahresuebersichtTable").query();
    }

    @Test
    @Tag("gui")
    public void testInitialState() {
        assertAll(
                () -> assertNotNull(yearDropdown, "Year Dropdown"),
                () -> assertNotNull(jahresuebersichtTable, "Jahresübersicht Table")
        );

        assertFalse(yearDropdown.getItems().isEmpty());
    }

    @Test
    @Tag("gui")
    public void testYearDropdownContent() {
        assertEquals(5, yearDropdown.getItems().size());
        assertTrue(yearDropdown.getItems().contains(2020));
        assertTrue(yearDropdown.getItems().contains(2021));
        assertTrue(yearDropdown.getItems().contains(2022));
        assertTrue(yearDropdown.getItems().contains(2023));
        assertTrue(yearDropdown.getItems().contains(2024));
    }

    @Test
    @Tag("gui")
    public void testTableStructure() {
        assertEquals(5, jahresuebersichtTable.getColumns().size());

        String[] expectedColumns = {
                "Monat", "Neuzugänge", "Anzahl Personen",
                "Gesamtumsatz Haushalt", "Gesamtumsatz Einkauf"
        };

        for (int i = 0; i < expectedColumns.length; i++) {
            assertEquals(expectedColumns[i], jahresuebersichtTable.getColumns().get(i).getText());
        }
    }

    @Test
    @Tag("gui")
    public void testYearSelection() {
        interact(() -> {
            yearDropdown.setValue(2023);
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(2023, yearDropdown.getValue());

        verify(mockJahresuebersichtDAO, atLeastOnce()).getJahresuebersichtByYear(2023);
    }

    @Test
    @Tag("gui")
    public void testTableColumnIds() {
        String[] expectedIds = {
                "monat", "neuzugaenge", "anzahlPersonen",
                "gesamtUmsatzHaushalt", "gesamtUmsatzEinkauf"
        };

        for (int i = 0; i < expectedIds.length; i++) {
            assertEquals(expectedIds[i], jahresuebersichtTable.getColumns().get(i).getId());
        }
    }

    @Test
    @Tag("gui")
    public void testUIComponentsVisibility() {
        assertTrue(yearDropdown.isVisible());
        assertTrue(jahresuebersichtTable.isVisible());

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
    public void testTableDataLoading() {
        interact(() -> {
            yearDropdown.setValue(2024);
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertNotNull(jahresuebersichtTable.getItems());

        verify(mockJahresuebersichtDAO, atLeastOnce()).getJahresuebersichtByYear(2024);
    }

    @Test
    @Tag("gui")
    public void testExitViaStage() {
        Stage stage = (Stage) jahresuebersichtTable.getScene().getWindow();
        assertTrue(stage.isShowing(), "Fenster sollte anfangs geöffnet sein");

        interact(() -> stage.close());
        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(stage.isShowing(), "Fenster sollte geschlossen sein");
    }


    @Test
    @Tag("gui")
    public void testDataPersistence() {

        assertDoesNotThrow(() -> {

            interact(() -> {
                yearDropdown.setValue(2022);
                yearDropdown.setValue(2023);
            });
            WaitForAsyncUtils.waitForFxEvents();
        });
    }

    @Test
    @Tag("gui")
    public void testEmptyDataHandling() {
        when(mockJahresuebersichtDAO.getJahresuebersichtByYear(anyInt()))
                .thenReturn(javafx.collections.FXCollections.observableArrayList());

        interact(() -> {
            yearDropdown.setValue(1999);
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(jahresuebersichtTable.getItems().isEmpty());
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
}