package guitest.errorreport;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kundenverwaltung.service.AnonymizeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class AnonymizeServiceTest extends ApplicationTest {

    private TextField textField;
    private TextArea textArea;
    private AnonymizeService service;

    @Override
    public void start(Stage stage) {
        textField = new TextField("SensitiveInput123");
        textArea = new TextArea("VerySecretNotes");

        VBox root = new VBox(textField, textArea);
        Scene scene = new Scene(root, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        service = AnonymizeService.getInstance();
    }

    @AfterEach
    public void tearDown() {
        // restore state for other tests
        service.anonymizeAllOpenWindows(false);
    }

    @Test
    @Tag("gui")
    public void testAnonymizeAllOpenWindows_shouldMaskTextInputs() {
        // ACT
        service.anonymizeAllOpenWindows(true);

        // ASSERT
        assertEquals("*****************", textField.getText());
        assertEquals("***************", textArea.getText());
    }

    @Test
    @Tag("gui")
    public void testAnonymizeAllOpenWindows_restoreOriginalText() {
        // ACT
        service.anonymizeAllOpenWindows(true);
        service.anonymizeAllOpenWindows(false);

        // ASSERT
        assertEquals("SensitiveInput123", textField.getText());
        assertEquals("VerySecretNotes", textArea.getText());
    }

    @Test
    @Tag("gui")
    public void testGetInstance_returnsSingleton() {
        AnonymizeService another = AnonymizeService.getInstance();
        assertSame(service, another);
    }
}
