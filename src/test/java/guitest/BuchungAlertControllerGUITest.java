package guitest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import kundenverwaltung.controller.BuchungController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class BuchungAlertControllerGUITest {

    private Stage stage;
    private BuchungController controller;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/BuchungAlert.fxml"));
        DialogPane root = loader.load();

        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testDialogNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#dialogPane").queryAs(DialogPane.class));
        assertNotNull(robot.lookup("#lwWarnungen").queryAs(ListView.class));
        assertNotNull(robot.lookup("#textContinue").queryAs(Text.class));
    }

    @Test
    @Tag("gui")
    void testDialogButtonsExist(FxRobot robot) {
        DialogPane dialogPane = robot.lookup("#dialogPane").queryAs(DialogPane.class);
        assertNotNull(dialogPane);

        assertTrue(dialogPane.getButtonTypes().contains(ButtonType.YES));
        assertTrue(dialogPane.getButtonTypes().contains(ButtonType.NO));
        assertTrue(dialogPane.getButtonTypes().contains(ButtonType.CANCEL));
    }

    @Test
    @Tag("gui")
    void testListViewContent(FxRobot robot) {
        ListView<String> listView = robot.lookup("#lwWarnungen").queryAs(ListView.class);
        assertNotNull(listView);
        assertTrue(listView.isVisible());
    }

    @Test
    @Tag("gui")
    void testTextContent(FxRobot robot) {
        Text text = robot.lookup("#textContinue").queryAs(Text.class);
        assertNotNull(text);
        assertTrue(text.getText().contains("Möchten Sie trotzdem weitermachen?"));
    }

    @Test
    @Tag("gui")
    void testDialogVisibility(FxRobot robot) {
        DialogPane dialogPane = robot.lookup("#dialogPane").queryAs(DialogPane.class);
        assertTrue(dialogPane.isVisible());
    }
}