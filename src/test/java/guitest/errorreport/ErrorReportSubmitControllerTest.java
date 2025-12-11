package guitest.errorreport;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import kundenverwaltung.controller.errorreport.ErrorReportSubmitController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;


import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class ErrorReportSubmitControllerTest extends ApplicationTest {

    private ErrorReportSubmitController controller;
    private TextArea userDescriptionTextArea;
    private Label selectedFileLabel;

    @Override
    public void start(Stage stage) throws Exception {
        // Mock the LogFileService to avoid the NPE during initialization
        mockLogFileService();

        // Load the FXML and set up the controller
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/kundenverwaltung/fxml/errorreport/ErrorReportSubmit.fxml")
        );
        AnchorPane root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();

        userDescriptionTextArea = lookup("#userDescriptionTextArea").query();
        selectedFileLabel = lookup("#selectedFileLabel").query();
    }

    private void mockLogFileService() {

        System.setProperty("log.path", "test-logs");
        System.setProperty("log.user_name", "test-user");

        // Create test log directory if it doesn't exist
        java.nio.file.Path testLogDir = java.nio.file.Paths.get("test-logs");
        if (!java.nio.file.Files.exists(testLogDir)) {
            try {
                java.nio.file.Files.createDirectories(testLogDir);
            } catch (Exception e) {
                // Ignore, test will handle the absence of logs
            }
        }
    }

    @BeforeEach
    public void setUp() {
        WaitForAsyncUtils.waitForFxEvents();
    }

    @AfterEach
    public void tearDown() {

        try {
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("test-logs"));
        } catch (Exception e) {

        }
    }

    @Test
    @Tag("gui")
    public void testInitialState() {

        verifyThat("#userDescriptionTextArea", hasText(""));
        verifyThat("#selectedFileLabel", (Label label) ->
                label.getText().equals("Keine Datei ausgewählt"));


        TableView<?> tableViewLogs = lookup("#tableViewLogs").query();
        assertNotNull(tableViewLogs);
    }

    @Test
    @Tag("gui")
    public void testUserDescriptionInput() {

        clickOn("#userDescriptionTextArea").write("Test error description");


        verifyThat("#userDescriptionTextArea", hasText("Test error description"));
    }

    @Test
    @Tag("gui")
    public void testLogTableViewStructure() {

        TableView<?> tableViewLogs = lookup("#tableViewLogs").query();


        assertNotNull(tableViewLogs);
        assertEquals(4, tableViewLogs.getColumns().size());


        TableColumn<?, ?> timestampCol = tableViewLogs.getColumns().get(0);
        TableColumn<?, ?> levelCol = tableViewLogs.getColumns().get(1);
        TableColumn<?, ?> sourceCol = tableViewLogs.getColumns().get(2);
        TableColumn<?, ?> messageCol = tableViewLogs.getColumns().get(3);

        assertEquals("Zeitstempel", timestampCol.getText());
        assertEquals("Level", levelCol.getText());
        assertEquals("Quelle", sourceCol.getText());
        assertEquals("Nachricht", messageCol.getText());
    }

    @Test
    @Tag("gui")
    public void testSendReportButtonWithEmptyDescription() {
        // ACT & ASSERT - Should not crash
        assertDoesNotThrow(() -> clickOn("#sendReportToServerButton"));
    }

    @Test
    @Tag("gui")
    public void testUIComponentsAreAccessible() {
        // Test that all required components are present
        assertNotNull(lookup("#userDescriptionTextArea").query());
        assertNotNull(lookup("#screenshotButton").query());
        assertNotNull(lookup("#selectedFileLabel").query());
        assertNotNull(lookup("#sendReportToServerButton").query());
        assertNotNull(lookup("#cancelButton").query());
        assertNotNull(lookup("#screenshotCaptureButton").query());
        assertNotNull(lookup("#tableViewLogs").query());
    }


    @Test
    @Tag("gui")
    public void testScreenshotButtonExists() {
        Button screenshotButton = lookup("#screenshotButton").query();
        assertNotNull(screenshotButton);
        assertEquals("Datei auswählen", screenshotButton.getText());
    }

    @Test
    @Tag("gui")
    public void testScreenshotCaptureButtonExists() {
        Button screenshotCaptureButton = lookup("#screenshotCaptureButton").query();
        assertNotNull(screenshotCaptureButton);
        assertEquals("Screenshot", screenshotCaptureButton.getText());
    }
}