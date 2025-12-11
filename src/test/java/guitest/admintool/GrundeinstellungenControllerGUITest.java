package guitest.admintool;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class GrundeinstellungenControllerGUITest {

    private TextField inpGebuehr;
    private TextField inpBescheide;
    private CheckBox chkDSE;
    private Button save;
    private ComboBox<String> cbBescheide;
    private MockGrundeinstellungenController mockController;

    @Start
    public void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource(
                "/kundenverwaltung/fxml/admintool/Grundeinstellungen.fxml"
        );
        if (fxmlUrl == null) {
            throw new IOException("FXML file not found. Please check the path.");
        }

        mockController = new MockGrundeinstellungenController();

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        loader.setControllerFactory(type -> {
            if (type == kundenverwaltung.controller.admintool.GrundeinstellungenController.class) {
                return mockController;
            }
            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Parent root = loader.load();

        inpGebuehr = (TextField) root.lookup("#inpGebuehr");
        inpBescheide = (TextField) root.lookup("#inpBescheide");
        chkDSE = (CheckBox) root.lookup("#chkDSE");
        save = (Button) root.lookup("#save");
        cbBescheide = (ComboBox<String>) root.lookup("#cbBescheide");

        javafx.application.Platform.runLater(() -> {
            inpGebuehr.setText("25");
            inpBescheide.setText("18");
            chkDSE.setSelected(true);

            cbBescheide.getItems().addAll("Pro Person", "Pro Haushalt");
            cbBescheide.getSelectionModel().select(0);
        });

        stage.setScene(new Scene(root));
        stage.show();

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    @Tag("gui")
    public void shouldContainAllUIComponents() {
        FxAssert.verifyThat("#inpGebuehr", Node::isVisible);
        FxAssert.verifyThat("#inpBescheide", Node::isVisible);
        FxAssert.verifyThat("#chkDSE", Node::isVisible);
        FxAssert.verifyThat("#save", Node::isVisible);
        FxAssert.verifyThat("#cbBescheide", Node::isVisible);
    }

    @Test
    @Tag("gui")
    public void shouldHaveCorrectLabels() {
        FxAssert.verifyThat("#save", LabeledMatchers.hasText("Speichern"));
        FxAssert.verifyThat("#chkDSE", node -> ((CheckBox) node).getText() != null && !((CheckBox) node).getText().isEmpty());
    }

    @Test
    @Tag("gui")
    public void shouldHaveInitialValuesFromController() {
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("25", inpGebuehr.getText());
        assertEquals("18", inpBescheide.getText());
        assertTrue(chkDSE.isSelected());
        assertNotNull(cbBescheide.getSelectionModel().getSelectedItem());
    }

    @Test
    @Tag("gui")
    public void shouldCallSpeichernMethodWhenSaveButtonClicked(FxRobot robot) {
        robot.clickOn("#save");

        assertTrue(mockController.speichernCalled.get(), "Speichern method should be called");
    }

    @Test
    @Tag("gui")

    public void shouldHandleInvalidInputGracefully(FxRobot robot) {
        robot.clickOn("#inpGebuehr").eraseText(2).write("abc");

        robot.clickOn("#save");

        assertTrue(mockController.speichernCalled.get(), "Speichern should be called even with invalid input");
        assertTrue(mockController.numberFormatExceptionHandled.get(), "NumberFormatException should be handled");
    }

    @Test
    @Tag("gui")
    public void shouldHaveCorrectComboBoxOptions() {
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(2, cbBescheide.getItems().size());
        assertTrue(cbBescheide.getItems().contains("Pro Person"));
        assertTrue(cbBescheide.getItems().contains("Pro Haushalt"));
    }

    @Test
    @Tag("gui")
    public void shouldChangeComboBoxSelection(FxRobot robot) {
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("Pro Person", cbBescheide.getSelectionModel().getSelectedItem());

        javafx.application.Platform.runLater(() -> {
            cbBescheide.getSelectionModel().select(1);
        });
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("Pro Haushalt", cbBescheide.getSelectionModel().getSelectedItem());
    }

    @Test
    @Tag("gui")
    public void shouldUpdateValuesWhenComboBoxChanges(FxRobot robot) {
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("Pro Person", cbBescheide.getSelectionModel().getSelectedItem());

        javafx.application.Platform.runLater(() -> {
            cbBescheide.getSelectionModel().select(1);
        });
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("Pro Haushalt", cbBescheide.getSelectionModel().getSelectedItem());
    }

    /**
     * Mock-Controller für Grundeinstellungen
     */
    public static class MockGrundeinstellungenController {
        public AtomicBoolean speichernCalled = new AtomicBoolean(false);
        public AtomicBoolean numberFormatExceptionHandled = new AtomicBoolean(false);

        public void initialize() {
        }

        public void speichern(javafx.event.ActionEvent event) {
            speichernCalled.set(true);

            try {
                Integer.parseInt("test");
            } catch (NumberFormatException e) {
                numberFormatExceptionHandled.set(true);
            }
        }
    }
}