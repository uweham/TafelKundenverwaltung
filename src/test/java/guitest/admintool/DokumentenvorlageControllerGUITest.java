package guitest.admintool;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class DokumentenvorlageControllerGUITest
{
    private TableView<?> filesTableView;
    private Label lbPathTest;
    private MockDokumentenvorlageController mockController;

    @Start
    public void start(Stage stage) throws Exception
    {
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/admintool/Dokumentenvorlage.fxml");
        if (fxmlUrl == null)
        {
            throw new IOException("FXML file not found. Please check the path.");
        }

        mockController = new MockDokumentenvorlageController();

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        loader.setControllerFactory(type ->
        {
            if (type == kundenverwaltung.controller.admintool.DokumentenvorlageController.class)
            {
                return mockController;
            }
            try
            {
                return type.getDeclaredConstructor().newInstance();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        Parent root = loader.load();
        filesTableView = (TableView<?>) root.lookup("#filesTableView");
        lbPathTest      = (Label)      root.lookup("#lbPathTest");

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    public void shouldContainAllUIComponents()
    {
        FxAssert.verifyThat("#filesTableView", Node::isVisible);
        FxAssert.verifyThat("#btnDelete",      Node::isVisible);
        FxAssert.verifyThat("#btnActivate",    Node::isVisible);
        FxAssert.verifyThat("#btnExport",      Node::isVisible);
        FxAssert.verifyThat("#btnImport",      Node::isVisible);
        FxAssert.verifyThat("#lbPathTest",     Node::isVisible);
    }

    @Test
    @Tag("gui")
    public void shouldHaveCorrectLabels()
    {
        FxAssert.verifyThat("#btnDelete",   LabeledMatchers.hasText("löschen"));
        FxAssert.verifyThat("#btnActivate", LabeledMatchers.hasText("de-/aktivieren"));
        FxAssert.verifyThat("#btnExport",   LabeledMatchers.hasText("exportieren"));
        FxAssert.verifyThat("#btnImport",   LabeledMatchers.hasText("importieren"));
        FxAssert.verifyThat(".label",       LabeledMatchers.hasText("Dokumentenvorlage"));
    }

    @Test
    @Tag("gui")
    public void shouldHaveCorrectTableColumns()
    {
        FxAssert.verifyThat("#filesTableView", (Predicate<Node>) node ->
        {
            TableView<?> table = (TableView<?>) node;
            List<String> columnTexts = table.getColumns().stream()
                    .map(TableColumn::getText)
                    .toList();
            return columnTexts.equals(List.of("Vorlage", "Autor", "Dateiversion", "Status"));
        });
    }

    @Test
    @Tag("gui")
    public void shouldCallControllerMethodsWhenButtonsClicked(FxRobot robot)
    {
        robot.clickOn("#btnDelete");
        assertTrue(mockController.deleteCalled.get(),   "Delete method should be called");

        robot.clickOn("#btnActivate");
        assertTrue(mockController.disabledCalled.get(), "Disable method should be called");

        robot.clickOn("#btnExport");
        assertTrue(mockController.exportCalled.get(),   "Export method should be called");

        robot.clickOn("#btnImport");
        assertTrue(mockController.importCalled.get(),   "Import method should be called");
    }

    @Test
    @Tag("gui")
    public void shouldButtonsExistAndBeEnabled(FxRobot robot)
    {
        Button btnDelete   = robot.lookup("#btnDelete").queryAs(Button.class);
        Button btnActivate = robot.lookup("#btnActivate").queryAs(Button.class);
        Button btnExport   = robot.lookup("#btnExport").queryAs(Button.class);
        Button btnImport   = robot.lookup("#btnImport").queryAs(Button.class);

        assertNotNull(btnDelete);
        assertNotNull(btnActivate);
        assertNotNull(btnExport);
        assertNotNull(btnImport);

        assertFalse(btnImport.isDisabled(),  "Importieren-Button muss aktiv sein");
    }

    public static class MockDokumentenvorlageController
    {
        public AtomicBoolean deleteCalled   = new AtomicBoolean(false);
        public AtomicBoolean disabledCalled = new AtomicBoolean(false);
        public AtomicBoolean exportCalled   = new AtomicBoolean(false);
        public AtomicBoolean importCalled   = new AtomicBoolean(false);

        public void initialize() { }

        public void deleteTemplate() {
            deleteCalled.set(true);
        }

        public void disabledTemplate() {
            disabledCalled.set(true);
        }

        public void exportTemplate() {
            exportCalled.set(true);
        }

        public void importButtonAction() {
            importCalled.set(true);
        }
    }
}
