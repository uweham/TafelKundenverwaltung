package guitest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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

import kundenverwaltung.controller.ShowDeletedPersonsController;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.DeletedMemberOfTheFamily;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class ShowDeletedPersonsControllerGUITest {

    private Stage stage;
    private ShowDeletedPersonsController controller;

    @Mock
    private Haushalt mockHaushalt;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/ShowDeletedPersons.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden im Ressourcenordner 'kundenverwaltung/fxml'");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        controller = loader.getController();

        mockHaushalt = mock(Haushalt.class);
        when(mockHaushalt.getKundennummer()).thenReturn(123);

        List<DeletedMemberOfTheFamily> deletedMembers = new ArrayList<>();

        DeletedMemberOfTheFamily member1 = new DeletedMemberOfTheFamily(
                1, // autoincrementId
                123, // householdId
                "Max", // firstName
                "Mustermann", // lastName
                Date.valueOf("1980-01-01"), // birthday
                Date.valueOf("2023-12-01"), // deletedOn
                "Kunde hat um Löschung gebeten" // reasonDelete
        );
        deletedMembers.add(member1);

        DeletedMemberOfTheFamily member2 = new DeletedMemberOfTheFamily(
                2, // autoincrementId
                123, // householdId
                "Maria", // firstName
                "Musterfrau", // lastName
                Date.valueOf("1985-05-15"), // birthday
                Date.valueOf("2023-12-05"), // deletedOn
                "Datenbereinigung" // reasonDelete
        );
        deletedMembers.add(member2);

        controller.showDeletedMemberOfTheFamilyInTable(mockHaushalt);

        controller.setDeletedMemberOfTheFamilyArrayList(new ArrayList<>(deletedMembers));
        controller.setDeletedMemberOfTheFamilyObservableList(javafx.collections.FXCollections.observableArrayList(deletedMembers));

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeading").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelListDeletedPersons").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelReasonDelete").queryAs(Label.class));
        assertNotNull(robot.lookup("#tableViewDeletedPersons").queryAs(TableView.class));
        assertNotNull(robot.lookup("#textAreaReasonDeletePerson").queryAs(TextArea.class));
        assertNotNull(robot.lookup("#btnCancel").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testLabelsText(FxRobot robot) {
        Label heading = robot.lookup("#labelHeading").queryAs(Label.class);
        Label listLabel = robot.lookup("#labelListDeletedPersons").queryAs(Label.class);
        Label reasonLabel = robot.lookup("#labelReasonDelete").queryAs(Label.class);

        assertEquals("Gelöschte Personen", heading.getText());
        assertEquals("Liste gelöschter Familienmitglieder", listLabel.getText());
        assertEquals("Grund der Löschung:", reasonLabel.getText());
    }




    @Test
    @Tag("gui")
    void testCancelButtonClosesWindow(FxRobot robot) {
        Button btnCancel = robot.lookup("#btnCancel").queryAs(Button.class);

        assertNotNull(btnCancel);
        assertEquals("Abbrechen", btnCancel.getText());
        assertTrue(btnCancel.isVisible());

        boolean initiallyShowing = stage.isShowing();

        robot.clickOn(btnCancel);

        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(stage.isShowing());
    }

    @Test
    @Tag("gui")
    void testUIElementsVisible(FxRobot robot) {
        Label heading = robot.lookup("#labelHeading").queryAs(Label.class);
        Label listLabel = robot.lookup("#labelListDeletedPersons").queryAs(Label.class);
        Label reasonLabel = robot.lookup("#labelReasonDelete").queryAs(Label.class);
        TableView<?> tableView = robot.lookup("#tableViewDeletedPersons").queryAs(TableView.class);
        TextArea textArea = robot.lookup("#textAreaReasonDeletePerson").queryAs(TextArea.class);
        Button btnCancel = robot.lookup("#btnCancel").queryAs(Button.class);

        assertTrue(heading.isVisible());
        assertTrue(listLabel.isVisible());
        assertTrue(reasonLabel.isVisible());
        assertTrue(tableView.isVisible());
        assertTrue(textArea.isVisible());
        assertTrue(btnCancel.isVisible());
    }

    @Test
    @Tag("gui")
    void testTextAreaIsReadOnly(FxRobot robot) {
        TextArea textArea = robot.lookup("#textAreaReasonDeletePerson").queryAs(TextArea.class);

        assertFalse(textArea.isEditable(), "TextArea should be read-only");
    }


    @Test
    @Tag("gui")
    void testEmptyTableView(FxRobot robot) {
        TableView<DeletedMemberOfTheFamily> tableView = robot.lookup("#tableViewDeletedPersons").queryAs(TableView.class);

        controller.setDeletedMemberOfTheFamilyObservableList(javafx.collections.FXCollections.observableArrayList());

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(0, tableView.getItems().size());

        TextArea textArea = robot.lookup("#textAreaReasonDeletePerson").queryAs(TextArea.class);
        assertTrue(textArea.getText().isEmpty() || textArea.getText().equals(""));
    }
}