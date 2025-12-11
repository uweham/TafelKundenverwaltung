package guitest.admintool;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
public class NationenControllerGUITest {

    private ListView<Object> listDeaktiviert;
    private ListView<Object> listAktiviert;
    private Button btnAuswaehlen;
    private Button btnAbwaehlen;
    private MockNationenController mockController;

    @Start
    public void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/admintool/Nationen.fxml");
        if (fxmlUrl == null) {
            throw new IOException("FXML file not found. Please check the path.");
        }

        mockController = new MockNationenController();

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        loader.setControllerFactory(type -> {
            if (type == kundenverwaltung.controller.admintool.NationenController.class) {
                return mockController;
            }
            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Parent root = loader.load();

        listDeaktiviert = (ListView<Object>) root.lookup("#listDeaktiviert");
        listAktiviert = (ListView<Object>) root.lookup("#listAktiviert");
        btnAuswaehlen = (Button) root.lookup("#btnAuswaehlen");
        btnAbwaehlen = (Button) root.lookup("#btnAbwaehlen");

        stage.setScene(new Scene(root));
        stage.show();

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    @Tag("gui")
    public void shouldContainAllUIComponents() {
        FxAssert.verifyThat("#listDeaktiviert", Node::isVisible);
        FxAssert.verifyThat("#listAktiviert", Node::isVisible);
        FxAssert.verifyThat("#btnAuswaehlen", Node::isVisible);
        FxAssert.verifyThat("#btnAbwaehlen", Node::isVisible);
    }

    @Test
    @Tag("gui")
    public void shouldHaveCorrectLabels() {
        FxAssert.verifyThat("#btnAuswaehlen", LabeledMatchers.hasText("Auswählen"));
        FxAssert.verifyThat("#btnAbwaehlen", LabeledMatchers.hasText("Abwählen"));
    }

    @Test
    @Tag("gui")
    public void shouldHaveListsInitialized() {
        WaitForAsyncUtils.waitForFxEvents();
        assertNotNull(listDeaktiviert.getItems());
        assertNotNull(listAktiviert.getItems());
    }

    @Test
    @Tag("gui")
    public void shouldButtonsBeEnabledByDefault() {
        WaitForAsyncUtils.waitForFxEvents();
        assertFalse(btnAuswaehlen.isDisabled(), "Auswählen-Button sollte standardmäßig aktiv sein");
        assertFalse(btnAbwaehlen.isDisabled(), "Abwählen-Button sollte standardmäßig aktiv sein");
    }

    @Test
    @Tag("gui")
    public void shouldCallNationAuswaehlenWhenButtonClicked(FxRobot robot) {
        WaitForAsyncUtils.waitForFxEvents();

        robot.interact(() -> {
            if (listDeaktiviert.getItems().isEmpty()) {
                Object mockNation = new Object() {
                    @Override public String toString() { return "Test Nation"; }
                };
                listDeaktiviert.getItems().add(mockNation);
            }
            listDeaktiviert.getSelectionModel().select(0);
        });
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn("#btnAuswaehlen");

        assertTrue(mockController.nationAuswaehlenCalled.get(), "nationAuswaehlen method should be called");
    }

    @Test
    @Tag("gui")
    public void shouldCallNationAbwaehlenWhenButtonClicked(FxRobot robot) {
        WaitForAsyncUtils.waitForFxEvents();

        robot.interact(() -> {
            if (listAktiviert.getItems().isEmpty()) {
                Object mockNation = new Object() {
                    @Override public String toString() { return "Test Nation"; }
                };
                listAktiviert.getItems().add(mockNation);
            }
            listAktiviert.getSelectionModel().select(0);
        });
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn("#btnAbwaehlen");

        assertTrue(mockController.nationAbwaehlenCalled.get(), "nationAbwaehlen method should be called");
    }

    @Test
    @Tag("gui")
    public void shouldHandleEmptySelectionGracefully(FxRobot robot) {
        WaitForAsyncUtils.waitForFxEvents();

        robot.interact(() -> {
            listAktiviert.getSelectionModel().clearSelection();
            listDeaktiviert.getSelectionModel().clearSelection();
        });
        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(btnAuswaehlen.isDisabled(), "Auswählen-Button sollte aktiv bleiben");
        assertFalse(btnAbwaehlen.isDisabled(), "Abwählen-Button sollte aktiv bleiben");

        robot.clickOn("#btnAuswaehlen");
        assertTrue(mockController.nationAuswaehlenCalled.get(), "Methode sollte trotzdem aufgerufen werden");
    }

    @Test
    @Tag("gui")
    public void shouldHandleEmptyListsGracefully(FxRobot robot) {
        WaitForAsyncUtils.waitForFxEvents();

        robot.interact(() -> {
            listDeaktiviert.getItems().clear();
            listAktiviert.getItems().clear();
        });
        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(btnAuswaehlen.isDisabled(), "Auswählen-Button sollte aktiv bleiben");
        assertFalse(btnAbwaehlen.isDisabled(), "Abwählen-Button sollte aktiv bleiben");
    }

    /**
     * Mock-Controller für Nationen
     */
    public static class MockNationenController {
        public AtomicBoolean nationAuswaehlenCalled = new AtomicBoolean(false);
        public AtomicBoolean nationAbwaehlenCalled = new AtomicBoolean(false);

        public void initialize() {
        }

        public void nationAuswaehlen(javafx.event.ActionEvent event) {
            nationAuswaehlenCalled.set(true);
        }

        public void nationAbwaehlen(javafx.event.ActionEvent event) {
            nationAbwaehlenCalled.set(true);
        }
    }
}