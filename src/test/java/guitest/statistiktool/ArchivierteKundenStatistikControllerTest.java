package guitest.statistiktool;

import javafx.scene.control.*;
import javafx.stage.Stage;
import kundenverwaltung.controller.statistiktool.ArchivierteKundenStatistikController;
import kundenverwaltung.model.statistiktool.ArchivierteKunden;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ArchivierteKundenStatistikControllerTest extends ApplicationTest {

    private ArchivierteKundenStatistikController controller;
    private ComboBox<String> customerTypeComboBox;
    private TableView<ArchivierteKunden> customerTable;
    private Button bearbeitenButton;
    private Button loeschenButton;
    private Button schliessenButton;

    @Override
    public void start(Stage stage) throws Exception {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/kundenverwaltung/fxml/statistiktool/ArchivierteKundenStatistik.fxml")
        );
        javafx.scene.layout.AnchorPane root = loader.load();
        controller = loader.getController();

        var mockDao = mock(kundenverwaltung.dao.HaushaltDAO.class);
        controller.setHaushaltDAO(mockDao);

        javafx.scene.Scene scene = new javafx.scene.Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        WaitForAsyncUtils.waitForFxEvents();

        customerTypeComboBox = lookup("#customerTypeComboBox").query();
        customerTable = lookup("#customerTable").query();
        bearbeitenButton = lookup(node ->
                node instanceof Button && "Kunde bearbeiten".equals(((Button) node).getText())
        ).query();
        loeschenButton = lookup(node ->
                node instanceof Button && "Löschen".equals(((Button) node).getText())
        ).query();
        schliessenButton = lookup(node ->
                node instanceof Button && "Schließen".equals(((Button) node).getText())
        ).query();
    }

    @Test
    @Tag("gui")
    public void testInitialState() {
        assertNotNull(customerTypeComboBox, "ComboBox nicht gefunden");
        assertNotNull(customerTable, "Tabelle nicht gefunden");
        assertNotNull(bearbeitenButton, "Bearbeiten-Button nicht gefunden");
        assertNotNull(loeschenButton, "Löschen-Button nicht gefunden");
        assertNotNull(schliessenButton, "Schließen-Button nicht gefunden");

        assertEquals(2, customerTypeComboBox.getItems().size());
        assertTrue(customerTypeComboBox.getItems().contains("Archivierte Kunden"));
        assertTrue(customerTypeComboBox.getItems().contains("Gesperrte Kunden"));

        assertEquals(5, customerTable.getColumns().size());
        assertEquals("Kundennummer", customerTable.getColumns().get(0).getText());
        assertEquals("Name", customerTable.getColumns().get(1).getText());
        assertEquals("Straße", customerTable.getColumns().get(2).getText());
        assertEquals("Plz", customerTable.getColumns().get(3).getText());
        assertEquals("Ort", customerTable.getColumns().get(4).getText());
    }

    @Test
    @Tag("gui")
    public void testComboBoxSelection() {
        assertEquals("Archivierte Kunden", customerTypeComboBox.getValue());

        interact(() -> {
            customerTypeComboBox.setValue("Gesperrte Kunden");
        });

        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Gesperrte Kunden", customerTypeComboBox.getValue());
    }

    @Test
    @Tag("gui")
    public void testButtonActions() {
        assertNotNull(bearbeitenButton.getOnAction(), "Bearbeiten-Button sollte Action-Handler haben");
        assertNotNull(loeschenButton.getOnAction(), "Löschen-Button sollte Action-Handler haben");
        assertNotNull(schliessenButton.getOnAction(), "Schließen-Button sollte Action-Handler haben");
    }



    @Test
    @Tag("gui")
    public void testEditWithoutSelection() {
        interact(() -> {
            customerTable.getSelectionModel().clearSelection();
            bearbeitenButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertNotNull(customerTable.getScene().getWindow(),
                "Fenster sollte nach Warnung noch offen sein");
    }

    @Test
    @Tag("gui")
    public void testCloseButton() {
        Stage stage = (Stage) schliessenButton.getScene().getWindow();
        assertTrue(stage.isShowing(), "Fenster sollte vorher geöffnet sein");

        interact(() -> schliessenButton.fire());
        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(stage.isShowing(), "Fenster sollte nach Schließen-Button geschlossen sein");
    }

    @Test
    @Tag("gui")
    public void testTableColumns() {
        assertEquals("kdNr", customerTable.getColumns().get(0).getId());
        assertEquals("name", customerTable.getColumns().get(1).getId());
        assertEquals("strasse", customerTable.getColumns().get(2).getId());
        assertEquals("plz", customerTable.getColumns().get(3).getId());
        assertEquals("ort", customerTable.getColumns().get(4).getId());
    }
}