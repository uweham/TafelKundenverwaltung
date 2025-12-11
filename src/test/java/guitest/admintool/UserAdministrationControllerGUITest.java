package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.net.URL;

@ExtendWith(ApplicationExtension.class)
public class UserAdministrationControllerGUITest {

    private static final String ID_BUTTON_ADD_USER = "#buttonAddUser";
    private static final String ID_BUTTON_CHANGE_USER = "#buttonChangeUser";
    private static final String ID_BUTTON_DELETE_USER = "#buttonDeleteUser";
    private static final String ID_TABLEVIEW_ALL_USERS = "#tableViewAllUsers";
    private static final String ID_LABEL_OVERVIEW = "Übersicht aller Benutzer";

    @Start
    private void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/admintool/UserAdministration.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei 'UserAdministration.fxml' nicht gefunden (prüfe Ressourcenpfad!)");

        Parent root = FXMLLoader.load(fxmlUrl);
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup(ID_BUTTON_ADD_USER).queryAs(Button.class), "Button 'buttonAddUser' fehlt");
        assertNotNull(robot.lookup(ID_BUTTON_CHANGE_USER).queryAs(Button.class), "Button 'buttonChangeUser' fehlt");
        assertNotNull(robot.lookup(ID_BUTTON_DELETE_USER).queryAs(Button.class), "Button 'buttonDeleteUser' fehlt");
        assertNotNull(robot.lookup(ID_TABLEVIEW_ALL_USERS).queryAs(TableView.class), "TableView 'tableViewAllUsers' fehlt");

        assertNotNull(robot.lookup(ID_LABEL_OVERVIEW).queryAs(Label.class), "Label 'Übersicht aller Benutzer' fehlt");
    }

    @Test
    @Tag("gui")
    void testButtonsTextAndInitialState(FxRobot robot) {
        Button btnAddUser = robot.lookup(ID_BUTTON_ADD_USER).queryAs(Button.class);
        Button btnChangeUser = robot.lookup(ID_BUTTON_CHANGE_USER).queryAs(Button.class);
        Button btnDeleteUser = robot.lookup(ID_BUTTON_DELETE_USER).queryAs(Button.class);

        assertEquals("Neuen Benutzer anlegen", btnAddUser.getText());
        assertEquals("Benutzerdaten ändern", btnChangeUser.getText());
        assertEquals("Benuzer löschen", btnDeleteUser.getText()); // Note: Typo in original text "Benuzer"

        assertFalse(btnAddUser.isDisabled(), "Add User button should be enabled initially");
        assertTrue(btnChangeUser.isDisabled(), "Change User button should be disabled initially");
        assertTrue(btnDeleteUser.isDisabled(), "Delete User button should be disabled initially");
    }

    @Test
    void testTableColumnsExistAndHaveCorrectText(FxRobot robot) {
        TableView<?> tableView = robot.lookup(ID_TABLEVIEW_ALL_USERS).queryAs(TableView.class);
        assertNotNull(tableView, "TableView not found");

        assertEquals(5, tableView.getColumns().size(), "TableView should have 5 columns");

        assertEquals("#", tableView.getColumns().get(0).getText());
        assertEquals("Benutzer", tableView.getColumns().get(1).getText());
        assertEquals("Name", tableView.getColumns().get(2).getText());
        assertEquals("Geburtsdatum", tableView.getColumns().get(3).getText());
        assertEquals("Benutzerrechte", tableView.getColumns().get(4).getText());
    }

    @Test
    @Tag("gui")
    void testMainLabelProperties(FxRobot robot) {
        Label mainLabel = robot.lookup("Benutzerverwaltung").queryAs(Label.class);
        assertNotNull(mainLabel, "Main label 'Benutzerverwaltung' not found");

        Font font = mainLabel.getFont();
        assertEquals("System Bold", font.getName());
        assertEquals(18.0, font.getSize(), 0.1);
    }

    @Test
    @Tag("gui")
    void testTableViewProperties(FxRobot robot) {
        TableView<?> tableView = robot.lookup(ID_TABLEVIEW_ALL_USERS).queryAs(TableView.class);

        assertNotNull(tableView, "TableView not found");
        assertEquals(5, tableView.getColumns().size(), "TableView should have 5 columns");

        assertNotNull(tableView.getItems(), "Table items should not be null");
    }

    @Test
    @Tag("gui")
    void testButtonClicks(FxRobot robot) {
        Button btnAddUser = robot.lookup(ID_BUTTON_ADD_USER).queryAs(Button.class);
        Button btnChangeUser = robot.lookup(ID_BUTTON_CHANGE_USER).queryAs(Button.class);
        Button btnDeleteUser = robot.lookup(ID_BUTTON_DELETE_USER).queryAs(Button.class);

        assertDoesNotThrow(() -> robot.clickOn(btnAddUser));
        assertDoesNotThrow(() -> robot.clickOn(btnChangeUser));
        assertDoesNotThrow(() -> robot.clickOn(btnDeleteUser));
    }

    @Test
    @Tag("gui")
    void testOverviewLabelProperties(FxRobot robot) {
        Label overviewLabel = robot.lookup(ID_LABEL_OVERVIEW).queryAs(Label.class);
        assertNotNull(overviewLabel, "Overview label not found");
        Font font = overviewLabel.getFont();
        assertEquals(14.0, font.getSize(), 0.1);
    }


    @Test
    @Tag("gui")
    void testScrollPaneExists(FxRobot robot) {
        TableView<?> tableView = robot.lookup(ID_TABLEVIEW_ALL_USERS).queryAs(TableView.class);
        assertNotNull(tableView.getParent(), "TableView should be inside a container");
    }

    @Test
    @Tag("gui")
    void testColumnIds(FxRobot robot) {
        TableView<?> tableView = robot.lookup(ID_TABLEVIEW_ALL_USERS).queryAs(TableView.class);

        assertEquals("userId", tableView.getColumns().get(0).getId());
        assertEquals("userName", tableView.getColumns().get(1).getId());
        assertEquals("fullName", tableView.getColumns().get(2).getId());
        assertEquals("birthdayString", tableView.getColumns().get(3).getId());
        assertEquals("userRights", tableView.getColumns().get(4).getId());
    }
}