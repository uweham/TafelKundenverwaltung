package guitest;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.ComboBox;
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

import kundenverwaltung.controller.BescheideBearbeitenController;

import kundenverwaltung.dao.FamilienmitgliedDAOimpl;

import java.net.URL;


@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class BescheideBearbeitenControllerGUITest {

    private Stage stage;
    private BescheideBearbeitenController controller;

    @Mock
    private FamilienmitgliedDAOimpl mockFamilienmitgliedDAO;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/BescheideBearbeiten.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden im Ressourcenordner 'kundenverwaltung/fxml'");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        controller = loader.getController();


        stage.setScene(new Scene(root));
        stage.show();
    }



    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeading").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelShowAssessments").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelExistingAssessments").queryAs(Label.class));

        assertNotNull(robot.lookup("#btnAddAssessments").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnEditAssessments").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnDeleteAssessments").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnBescheideSchliessen").queryAs(Button.class));

        assertNotNull(robot.lookup("#cbAuswahlBescheide").queryAs(ComboBox.class));

        assertNotNull(robot.lookup("#tvBescheide").queryAs(TableView.class));
    }

    @Test
    @Tag("gui")
    void testLabelsText(FxRobot robot) {
        Label heading = robot.lookup("#labelHeading").queryAs(Label.class);
        Label showAssessments = robot.lookup("#labelShowAssessments").queryAs(Label.class);
        Label existingAssessments = robot.lookup("#labelExistingAssessments").queryAs(Label.class);

        assertTrue(heading.getText().contains("Bitte fügen Sie hier die gültigen Bescheide ein"));
        assertEquals("Bescheide anzeigen.", showAssessments.getText());
        assertEquals("Vorhandene Bescheide:", existingAssessments.getText());
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot) {
        Button btnAdd = robot.lookup("#btnAddAssessments").queryAs(Button.class);
        Button btnEdit = robot.lookup("#btnEditAssessments").queryAs(Button.class);
        Button btnDelete = robot.lookup("#btnDeleteAssessments").queryAs(Button.class);
        Button btnClose = robot.lookup("#btnBescheideSchliessen").queryAs(Button.class);

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
    void testComboBoxInitialState(FxRobot robot) {
        ComboBox<String> comboBox = robot.lookup("#cbAuswahlBescheide").queryAs(ComboBox.class);

        assertNotNull(comboBox);
        assertEquals(3, comboBox.getItems().size());
        assertTrue(comboBox.getItems().contains("Alle"));
        assertTrue(comboBox.getItems().contains("Nur gültige"));
        assertTrue(comboBox.getItems().contains("Nur ungültige"));

        assertEquals("Alle", comboBox.getValue());
    }

    @Test
    @Tag("gui")
    void testTableViewInitialState(FxRobot robot) {
        TableView<?> tableView = robot.lookup("#tvBescheide").queryAs(TableView.class);

        assertNotNull(tableView);
        assertTrue(tableView.isVisible());
        assertEquals(4, tableView.getColumns().size());
    }

    @Test
    @Tag("gui")
    void testUIElementsVisible(FxRobot robot) {
        assertTrue(robot.lookup("#labelHeading").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#btnAddAssessments").queryAs(Button.class).isVisible());
        assertTrue(robot.lookup("#tvBescheide").queryAs(TableView.class).isVisible());
        assertTrue(robot.lookup("#cbAuswahlBescheide").queryAs(ComboBox.class).isVisible());
    }

    @Test
    @Tag("gui")
    void testComboBoxInteraction(FxRobot robot) {
        ComboBox<String> comboBox = robot.lookup("#cbAuswahlBescheide").queryAs(ComboBox.class);

        robot.clickOn(comboBox);
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn("Nur gültige");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Nur gültige", comboBox.getValue());

        robot.clickOn(comboBox);
        robot.clickOn("Nur ungültige");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Nur ungültige", comboBox.getValue());

        robot.clickOn(comboBox);
        robot.clickOn("Alle");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Alle", comboBox.getValue());
    }



    @Test
    @Tag("gui")
    void testTableViewColumns(FxRobot robot) {
        TableView<?> tableView = robot.lookup("#tvBescheide").queryAs(TableView.class);

        assertEquals("Name", tableView.getColumns().get(0).getText());
        assertEquals("Bescheid", tableView.getColumns().get(1).getText());
        assertEquals("gültig ab", tableView.getColumns().get(2).getText());
        assertEquals("gültig bis", tableView.getColumns().get(3).getText());
    }

    @Test
    @Tag("gui")
    void testInitialEmptyTableView(FxRobot robot) {
        TableView<?> tableView = robot.lookup("#tvBescheide").queryAs(TableView.class);

        assertTrue(tableView.getItems().isEmpty());
    }
}