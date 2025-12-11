package guitest.errorreport;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kundenverwaltung.controller.errorreport.ErrorReportLogController;
import kundenverwaltung.logger.model.LogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class ErrorReportLogControllerTest extends ApplicationTest {

    private ErrorReportLogController controller;
    private TextField timestampField;
    private TextField levelField;
    private TextField sourceField;
    private TextArea messageArea;

    @Override
    public void start(Stage stage) throws Exception {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/kundenverwaltung/fxml/errorreport/ErrorReportLog.fxml")
        );
        javafx.scene.layout.VBox root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        WaitForAsyncUtils.waitForFxEvents();


        timestampField = lookup("#timestampField").query();
        levelField = lookup("#levelField").query();
        sourceField = lookup("#sourceField").query();
        messageArea = lookup("#messageArea").query();
    }

    @Test
    @Tag("gui")
    public void testInitialState() {
        verifyThat("#timestampField", hasText(""));
        verifyThat("#levelField", hasText(""));
        verifyThat("#sourceField", hasText(""));
        verifyThat("#messageArea", hasText(""));

        assertFalse(timestampField.isEditable());
        assertFalse(levelField.isEditable());
        assertFalse(sourceField.isEditable());
        assertFalse(messageArea.isEditable());
    }

    @Test
    @Tag("gui")
    public void testSetLogEntry() {
        LogEntry logEntry = new LogEntry("2024-08-31 10:30:45", "ERROR", "com.example.Application",
                "This is a test error message", "additionalInfo");

        // Use interact() to ensure UI operations happen on the JavaFX thread
        interact(() -> controller.setLogEntry(logEntry));

        verifyThat("#timestampField", hasText("2024-08-31 10:30:45"));
        verifyThat("#levelField", hasText("ERROR"));
        verifyThat("#sourceField", hasText("com.example.Application"));
        verifyThat("#messageArea", hasText("This is a test error message"));
    }

    @Test
    @Tag("gui")
    public void testSetLogEntryWithEmptyValues() {
        LogEntry logEntry = new LogEntry("", "", "", "", "");

        interact(() -> controller.setLogEntry(logEntry));

        verifyThat("#timestampField", hasText(""));
        verifyThat("#levelField", hasText(""));
        verifyThat("#sourceField", hasText(""));
        verifyThat("#messageArea", hasText(""));
    }


    @Test
    @Tag("gui")
    public void testSetLogEntryWithLongMessage() {
        String longMessage = "This is a very long error message that should wrap in the text area. " +
                "It contains multiple sentences to test the text wrapping functionality. " +
                "The message should be displayed properly in the text area with word wrap enabled.";

        LogEntry logEntry = new LogEntry("2024-08-31 10:30:45", "INFO", "com.example.Application",
                longMessage, "additionalInfo");

        interact(() -> controller.setLogEntry(logEntry));

        verifyThat("#timestampField", hasText("2024-08-31 10:30:45"));
        verifyThat("#levelField", hasText("INFO"));
        verifyThat("#sourceField", hasText("com.example.Application"));
        verifyThat("#messageArea", hasText(longMessage));

        assertTrue(messageArea.isWrapText());
    }

    @Test
    @Tag("gui")
    public void testUIComponentsAreAccessible() {
        assertNotNull(timestampField);
        assertNotNull(levelField);
        assertNotNull(sourceField);
        assertNotNull(messageArea);
        assertNotNull(lookup("Schließen").queryButton());
    }

    @Test
    @Tag("gui")
    public void testCloseButtonFunctionality() {
        assertDoesNotThrow(() -> interact(() -> clickOn("Schließen")));
    }

    @Test
    @Tag("gui")
    public void testMessageAreaProperties() {
        assertTrue(messageArea.isWrapText());
        assertFalse(messageArea.isEditable());
    }

    @Test
    @Tag("gui")
    public void testMultipleSetLogEntryCalls() {
        LogEntry logEntry1 = new LogEntry("2024-08-31 10:30:45", "ERROR", "com.example.App",
                "First message", "info1");
        LogEntry logEntry2 = new LogEntry("2024-08-31 11:45:22", "WARN", "com.example.Service",
                "Second message", "info2");

        interact(() -> controller.setLogEntry(logEntry1));
        verifyThat("#timestampField", hasText("2024-08-31 10:30:45"));
        verifyThat("#levelField", hasText("ERROR"));
        verifyThat("#sourceField", hasText("com.example.App"));
        verifyThat("#messageArea", hasText("First message"));

        interact(() -> controller.setLogEntry(logEntry2));
        verifyThat("#timestampField", hasText("2024-08-31 11:45:22"));
        verifyThat("#levelField", hasText("WARN"));
        verifyThat("#sourceField", hasText("com.example.Service"));
        verifyThat("#messageArea", hasText("Second message"));
    }

    @Test
    @Tag("gui")
    public void testTextFieldMaxWidth() {
        assertTrue(timestampField.getWidth() > 0 || timestampField.getPrefWidth() > 0);
        assertTrue(levelField.getWidth() > 0 || levelField.getPrefWidth() > 0);
        assertTrue(sourceField.getWidth() > 0 || sourceField.getPrefWidth() > 0);
    }

    @Test
    @Tag("gui")
    public void testLayoutStructure() {
        assertNotNull(lookup("Zeitstempel:").query());
        assertNotNull(lookup("Level:").query());
        assertNotNull(lookup("Quelle:").query());
        assertNotNull(lookup("Nachricht:").query());
    }
}