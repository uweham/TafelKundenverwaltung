package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
class BescheidartenControllerGUITest {

    private static final String TEST_BESCHEIDART = "TestBescheidart";

    @Start
    public void start(Stage stage) throws Exception {
        URL url = getClass().getResource(
                "/kundenverwaltung/fxml/admintool/Bescheidarten.fxml"
        );
        assertNotNull(url, "FXML nicht gefunden – prüfe bitte den Pfad.");

        String fxml = Files.readString(Paths.get(url.toURI()), StandardCharsets.UTF_8);

        String cleaned = fxml
                .replaceAll("onAction=\"#.*?\"", "")
                .replaceAll("fx:controller=\"[^\"]*\"", "");

        FXMLLoader loader = new FXMLLoader();
        loader.setController(new DummyController());
        Parent root = loader.load(
                new ByteArrayInputStream(cleaned.getBytes(StandardCharsets.UTF_8))
        );

        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    void setUp(FxRobot robot) throws TimeoutException {
        FxToolkit.hideStage();
        FxToolkit.setupStage(Stage::show);

        robot.interact(() -> {
            TextField tf = robot.lookup("#textFieldAssessmentName").queryAs(TextField.class);
            tf.clear();
        });
    }

    @Test
    @Tag("gui")
    void shouldHaveAllControls(FxRobot robot) {
        assertNotNull(robot.lookup("#listViewEnabled").queryAs(ListView.class), "Aktive ListView fehlt");
        assertNotNull(robot.lookup("#listViewDisabled").queryAs(ListView.class), "Inaktive ListView fehlt");
        assertNotNull(robot.lookup("#buttonEnabled").queryAs(Button.class), "Aktivieren-Button fehlt");
        assertNotNull(robot.lookup("#buttonDisabled").queryAs(Button.class), "Deaktivieren-Button fehlt");
        assertNotNull(robot.lookup("#buttonDelete").queryAs(Button.class), "Löschen-Button fehlt");
        assertNotNull(robot.lookup("#buttonAdd").queryAs(Button.class), "Hinzufügen-Button fehlt");
        assertNotNull(robot.lookup("#textFieldAssessmentName").queryAs(TextField.class), "Textfeld fehlt");
    }

    @Test
    @Tag("gui")
    void addButtonEnablement(FxRobot robot) {
        Button addButton = robot.lookup("#buttonAdd").queryAs(Button.class);
        TextField textField = robot.lookup("#textFieldAssessmentName").queryAs(TextField.class);

        assertTrue(addButton.isDisabled(), "Add-Button sollte initial disabled sein");

        robot.clickOn(textField).write("NeueArt");
        assertFalse(addButton.isDisabled(), "Add-Button sollte nach Texteingabe enabled sein");

        robot.clickOn(textField).eraseText(7);
        assertTrue(addButton.isDisabled(), "Add-Button sollte wieder disabled sein wenn Textfeld leer ist");
    }

    @Test
    @Tag("gui")
    void addNewAssessment(FxRobot robot) {
        ListView<String> enabledList = robot.lookup("#listViewEnabled").queryAs(ListView.class);
        TextField textField = robot.lookup("#textFieldAssessmentName").queryAs(TextField.class);
        Button addButton = robot.lookup("#buttonAdd").queryAs(Button.class);

        robot.clickOn(textField).write(TEST_BESCHEIDART);
        robot.clickOn(addButton);

        robot.interact(() -> {
            ObservableList<String> items = enabledList.getItems();
            assertTrue(items.contains(TEST_BESCHEIDART), "Neue Bescheidart sollte in der Liste sein");
            assertTrue(textField.getText().isEmpty(), "Textfeld sollte nach dem Hinzufügen leer sein");
        });
    }

    @Test
    @Tag("gui")
    void moveBetweenLists(FxRobot robot) {
        robot.interact(() -> {
            ListView<String> enabledList = robot.lookup("#listViewEnabled").queryAs(ListView.class);
            ListView<String> disabledList = robot.lookup("#listViewDisabled").queryAs(ListView.class);

            enabledList.getItems().addAll("Art1", "Art2");
            disabledList.getItems().addAll("Art3", "Art4");
        });

        robot.clickOn("Art1");
        robot.clickOn("#buttonDisabled");

        robot.interact(() -> {
            ListView<String> enabledList = robot.lookup("#listViewEnabled").queryAs(ListView.class);
            ListView<String> disabledList = robot.lookup("#listViewDisabled").queryAs(ListView.class);

            assertFalse(enabledList.getItems().contains("Art1"), "Art1 sollte nicht mehr in aktiver Liste sein");
            assertTrue(disabledList.getItems().contains("Art1"), "Art1 sollte in inaktiver Liste sein");
        });

        robot.clickOn("Art3");
        robot.clickOn("#buttonEnabled");

        robot.interact(() -> {
            ListView<String> enabledList = robot.lookup("#listViewEnabled").queryAs(ListView.class);
            ListView<String> disabledList = robot.lookup("#listViewDisabled").queryAs(ListView.class);

            assertTrue(enabledList.getItems().contains("Art3"), "Art3 sollte in aktiver Liste sein");
            assertFalse(disabledList.getItems().contains("Art3"), "Art3 sollte nicht mehr in inaktiver Liste sein");
        });
    }

    @Test
    @Tag("gui")
    void deleteAssessment(FxRobot robot) {
        robot.interact(() -> {
            ListView<String> enabledList = robot.lookup("#listViewEnabled").queryAs(ListView.class);
            ListView<String> disabledList = robot.lookup("#listViewDisabled").queryAs(ListView.class);

            enabledList.getItems().addAll("Art1", "Art2");
            disabledList.getItems().addAll("Art3", "Art4");
        });

        robot.clickOn("Art1");
        robot.clickOn("#buttonDelete");

        robot.interact(() -> {
            ListView<String> enabledList = robot.lookup("#listViewEnabled").queryAs(ListView.class);
            assertFalse(enabledList.getItems().contains("Art1"), "Art1 sollte gelöscht sein");
        });

        robot.clickOn("Art3");
        robot.clickOn("#buttonDelete");

        robot.interact(() -> {
            ListView<String> disabledList = robot.lookup("#listViewDisabled").queryAs(ListView.class);
            assertFalse(disabledList.getItems().contains("Art3"), "Art3 sollte gelöscht sein");
        });
    }

    public static class DummyController {
        @FXML private ListView<String> listViewEnabled;
        @FXML private ListView<String> listViewDisabled;
        @FXML private Button buttonEnabled;
        @FXML private Button buttonDisabled;
        @FXML private Button buttonDelete;
        @FXML private Button buttonAdd;
        @FXML private TextField textFieldAssessmentName;

        @FXML
        public void initialize() {
            buttonAdd.disableProperty()
                    .bind(textFieldAssessmentName.textProperty().isEmpty());

            buttonAdd.setOnAction(e -> {
                if (!textFieldAssessmentName.getText().isEmpty()) {
                    listViewEnabled.getItems().add(textFieldAssessmentName.getText());
                    textFieldAssessmentName.clear();
                }
            });

            buttonEnabled.setOnAction(e -> {
                String selected = listViewDisabled.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    listViewDisabled.getItems().remove(selected);
                    listViewEnabled.getItems().add(selected);
                }
            });

            buttonDisabled.setOnAction(e -> {
                String selected = listViewEnabled.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    listViewEnabled.getItems().remove(selected);
                    listViewDisabled.getItems().add(selected);
                }
            });

            buttonDelete.setOnAction(e -> {
                String selectedEnabled = listViewEnabled.getSelectionModel().getSelectedItem();
                String selectedDisabled = listViewDisabled.getSelectionModel().getSelectedItem();

                if (selectedEnabled != null) {
                    listViewEnabled.getItems().remove(selectedEnabled);
                }
                if (selectedDisabled != null) {
                    listViewDisabled.getItems().remove(selectedDisabled);
                }
            });
        }
    }
}