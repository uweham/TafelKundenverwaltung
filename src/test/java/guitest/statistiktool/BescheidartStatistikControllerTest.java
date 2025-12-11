package guitest.statistiktool;

import javafx.scene.control.*;
import javafx.stage.Stage;
import kundenverwaltung.controller.statistiktool.BescheidartStatistikController;
import kundenverwaltung.model.Bescheid;
import kundenverwaltung.model.Verteilstelle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BescheidartStatistikControllerTest extends ApplicationTest {

    private BescheidartStatistikController controller;
    private ComboBox<String> verteilstelleComboBox;
    private ComboBox<String> statusComboBox;
    private TableView<Bescheid> bescheidartenTable;
    private Button loadButton;

    private kundenverwaltung.dao.VerteilstelleDAO mockVerteilstelleDAO;
    private kundenverwaltung.dao.BescheidDAO mockBescheidDAO;

    @Override
    public void start(Stage stage) throws Exception {
        mockVerteilstelleDAO = mock(kundenverwaltung.dao.VerteilstelleDAO.class);
        mockBescheidDAO = mock(kundenverwaltung.dao.BescheidDAO.class);

        Verteilstelle mockVerteilstelle = mock(Verteilstelle.class);
        when(mockVerteilstelle.getName()).thenReturn("Test Verteilstelle");

        Bescheid mockBescheid = mock(Bescheid.class);
        when(mockBescheid.getAnzahlPersonen()).thenReturn(5);
        when(mockBescheid.getBescheidart()).thenReturn(null); // oder mock Bescheidart wenn nötig

        when(mockVerteilstelleDAO.readAll()).thenReturn(Arrays.asList(mockVerteilstelle));
        when(mockVerteilstelleDAO.readByName(anyString())).thenReturn(mockVerteilstelle);
        when(mockBescheidDAO.readBescheid(any(Verteilstelle.class), anyString()))
                .thenReturn(Arrays.asList(mockBescheid));

        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/kundenverwaltung/fxml/statistiktool/BescheidartStatistik.fxml")
        );
        javafx.scene.layout.AnchorPane root = loader.load();
        controller = loader.getController();

        setDAOsViaReflection();

        javafx.scene.Scene scene = new javafx.scene.Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void setDAOsViaReflection() {
        try {
            var verteilstelleField = controller.getClass().getDeclaredField("verteilstelleDAO");
            verteilstelleField.setAccessible(true);
            verteilstelleField.set(controller, mockVerteilstelleDAO);

            var bescheidField = controller.getClass().getDeclaredField("bescheidDAO");
            bescheidField.setAccessible(true);
            bescheidField.set(controller, mockBescheidDAO);

            try {
                var familienField = controller.getClass().getDeclaredField("familienmitgliedDAO");
                familienField.setAccessible(true);
                familienField.set(controller, mock(kundenverwaltung.dao.FamilienmitgliedDAO.class));
            } catch (NoSuchFieldException e) {
            }

        } catch (Exception e) {
            System.out.println("Could not inject mocks via reflection: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setUp() {
        WaitForAsyncUtils.waitForFxEvents();

        verteilstelleComboBox = lookup("#verteilstelleComboBox").query();
        statusComboBox = lookup("#statusComboBox").query();
        bescheidartenTable = lookup("#bescheidartenTable").query();
        loadButton = lookup("#loadButton").query();
    }

    @Test
    @Tag("gui")
    public void testInitialState() {
        assertAll(
                () -> assertNotNull(verteilstelleComboBox, "Verteilstelle ComboBox"),
                () -> assertNotNull(statusComboBox, "Status ComboBox"),
                () -> assertNotNull(bescheidartenTable, "Tabelle"),
                () -> assertNotNull(loadButton, "Laden-Button")
        );

        assertEquals("Laden", loadButton.getText());
        assertTrue(loadButton.getStyle().contains("#80c47c"));
    }

    @Test
    @Tag("gui")
    public void testComboBoxContent() {
        List<String> statusOptions = statusComboBox.getItems();
        assertEquals(2, statusOptions.size());
        assertTrue(statusOptions.contains("Gültig"));
        assertTrue(statusOptions.contains("Nicht gültig"));
    }

    @Test
    @Tag("gui")
    public void testLoadButtonWithValidSelection() {
        interact(() -> {
            verteilstelleComboBox.getItems().add("Test Verteilstelle");
            verteilstelleComboBox.getSelectionModel().selectFirst();
            statusComboBox.setValue("Gültig");
        });

        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> loadButton.fire());
        WaitForAsyncUtils.waitForFxEvents();
        assertNotNull(bescheidartenTable);
    }

    @Test
    @Tag("gui")
    public void testLoadButtonWithoutSelection() {
        interact(() -> {
            verteilstelleComboBox.getSelectionModel().clearSelection();
            statusComboBox.getSelectionModel().clearSelection();
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertDoesNotThrow(() -> {
            interact(() -> loadButton.fire());
            WaitForAsyncUtils.waitForFxEvents();
        });
    }

    @Test
    @Tag("gui")
    public void testTableStructure() {
        assertEquals(2, bescheidartenTable.getColumns().size());

        TableColumn<Bescheid, ?> bescheidartColumn = bescheidartenTable.getColumns().get(0);
        TableColumn<Bescheid, ?> anzahlColumn = bescheidartenTable.getColumns().get(1);

        assertEquals("Bescheidart", bescheidartColumn.getText());
        assertEquals("Anzahl Personen", anzahlColumn.getText());
    }


    @Test
    @Tag("gui")
    public void testCloseViaStage() {
        Stage stage = (Stage) loadButton.getScene().getWindow();
        assertTrue(stage.isShowing(), "Fenster sollte anfangs geöffnet sein");

        interact(() -> stage.close());
        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(stage.isShowing(), "Fenster sollte geschlossen sein");
    }


    @Test
    @Tag("gui")
    public void testComboBoxInteraction() {
        interact(() -> {
            statusComboBox.getItems().clear();
            statusComboBox.getItems().addAll("Gültig", "Nicht gültig");
            statusComboBox.setValue("Gültig");
        });

        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Gültig", statusComboBox.getValue());
    }
}