package guitest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import kundenverwaltung.controller.VollmachtenController;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Vollmacht;

import java.net.URL;


@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class VollmachtenControllerGUITest {

    private Stage stage;
    private VollmachtenController controller;

    @Mock
    private Haushalt mockHaushalt;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/VollmachtenAnzeigen.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden im Ressourcenordner 'kundenverwaltung/fxml'");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        controller = loader.getController();

        mockHaushalt = mock(Haushalt.class);

        when(mockHaushalt.getKundennummer()).thenReturn(12345);

        controller.setHaushalt(mockHaushalt);

        controller.erstelleTabelleBestehendeVollmachten();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeadingShow").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelCurrentPowerOfAttorney").queryAs(Label.class));

        assertNotNull(robot.lookup("#tvBestehendeVollmachten").queryAs(TableView.class));

        assertNotNull(robot.lookup("#btnAddPowerOfAttorneyShow").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnEditPowerOfAttorneyShow").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnDeletePowerOfAttorneyShow").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnAnzeigenSchliessen").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testLabelsText(FxRobot robot) {
        Label heading = robot.lookup("#labelHeadingShow").queryAs(Label.class);
        Label current = robot.lookup("#labelCurrentPowerOfAttorney").queryAs(Label.class);

        assertEquals("Sehen Sie hier die Liste der bestehenden Vollmachten", heading.getText());
        assertEquals("Aktuell bestehende Vollmachten", current.getText());
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot) {
        Button btnAdd = robot.lookup("#btnAddPowerOfAttorneyShow").queryAs(Button.class);
        Button btnEdit = robot.lookup("#btnEditPowerOfAttorneyShow").queryAs(Button.class);
        Button btnDelete = robot.lookup("#btnDeletePowerOfAttorneyShow").queryAs(Button.class);
        Button btnClose = robot.lookup("#btnAnzeigenSchliessen").queryAs(Button.class);

        assertEquals("Hinzufügen", btnAdd.getText());
        assertEquals("Bearbeiten", btnEdit.getText());
        assertEquals("Löschen", btnDelete.getText());
        assertEquals("Schließen", btnClose.getText());

        assertFalse(btnAdd.isDisabled());
        assertFalse(btnEdit.isDisabled());
        assertFalse(btnDelete.isDisabled());
        assertFalse(btnClose.isDisabled());
    }

    @Test
    @Tag("gui")
    void testTableColumnsExist(FxRobot robot) {
        TableView<Vollmacht> table = robot.lookup("#tvBestehendeVollmachten").queryAs(TableView.class);

        assertNotNull(table);
        assertEquals(4, table.getColumns().size());
    }

    @Test
    @Tag("gui")
    void testCloseButtonClosesWindow(FxRobot robot) {
        Button btnClose = robot.lookup("#btnAnzeigenSchliessen").queryAs(Button.class);

        assertNotNull(btnClose);
        assertTrue(btnClose.isVisible());

        boolean initiallyShowing = stage.isShowing();

        robot.clickOn(btnClose);

        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(stage.isShowing());
    }

    @Test
    @Tag("gui")
    void testUIElementsVisible(FxRobot robot) {
        Label heading = robot.lookup("#labelHeadingShow").queryAs(Label.class);
        Label current = robot.lookup("#labelCurrentPowerOfAttorney").queryAs(Label.class);
        TableView<?> table = robot.lookup("#tvBestehendeVollmachten").queryAs(TableView.class);
        Button btnAdd = robot.lookup("#btnAddPowerOfAttorneyShow").queryAs(Button.class);
        Button btnEdit = robot.lookup("#btnEditPowerOfAttorneyShow").queryAs(Button.class);
        Button btnDelete = robot.lookup("#btnDeletePowerOfAttorneyShow").queryAs(Button.class);
        Button btnClose = robot.lookup("#btnAnzeigenSchliessen").queryAs(Button.class);

        assertTrue(heading.isVisible());
        assertTrue(current.isVisible());
        assertTrue(table.isVisible());
        assertTrue(btnAdd.isVisible());
        assertTrue(btnEdit.isVisible());
        assertTrue(btnDelete.isVisible());
        assertTrue(btnClose.isVisible());
    }

    @Test
    @Tag("gui")
    void testTableInitialization(FxRobot robot) {
        TableView<Vollmacht> table = robot.lookup("#tvBestehendeVollmachten").queryAs(TableView.class);

        assertNotNull(table);
        assertTrue(table.getItems() != null);
    }

    @Test
    @Tag("gui")
    void testButtonActionsExist(FxRobot robot) {
        Button btnAdd = robot.lookup("#btnAddPowerOfAttorneyShow").queryAs(Button.class);
        Button btnEdit = robot.lookup("#btnEditPowerOfAttorneyShow").queryAs(Button.class);
        Button btnDelete = robot.lookup("#btnDeletePowerOfAttorneyShow").queryAs(Button.class);
        Button btnClose = robot.lookup("#btnAnzeigenSchliessen").queryAs(Button.class);

        assertNotNull(btnAdd.getOnAction());
        assertNotNull(btnEdit.getOnAction());
        assertNotNull(btnDelete.getOnAction());
        assertNotNull(btnClose.getOnAction());
    }

    @Test
    @Tag("gui")
    void testTableColumnHeaders(FxRobot robot) {
        TableView<Vollmacht> table = robot.lookup("#tvBestehendeVollmachten").queryAs(TableView.class);

        assertEquals("Empfänger", table.getColumns().get(0).getText());
        assertEquals("Kundennummer", table.getColumns().get(1).getText());
        assertEquals("Ausstelldatum", table.getColumns().get(2).getText());
        assertEquals("Ablaufdatum", table.getColumns().get(3).getText());
    }

    @Test
    @Tag("gui")
    void testWindowTitle(FxRobot robot) {
        assertNotNull(stage.getTitle());
    }
}