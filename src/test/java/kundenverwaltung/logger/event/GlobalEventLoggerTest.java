package kundenverwaltung.logger.event;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import kundenverwaltung.logger.core.DefaultInteractionLogger;
import org.junit.jupiter.api.*;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GlobalEventLoggerTest {

    private static final String SCENE_NAME = "TestScene";
    private DefaultInteractionLogger mockLogger;

    private static PrintStream originalErr;

    @BeforeAll
    public void initJavaFX() throws Exception {
        originalErr = System.err;
        System.setErr(new PrintStream(java.io.OutputStream.nullOutputStream()));
        new JFXPanel();
        Thread.sleep(500);
        System.setErr(originalErr);
    }

    @BeforeEach
    public void setUp() throws Exception {
        mockLogger = mock(DefaultInteractionLogger.class);
        setLoggerMock(mockLogger);
    }

    @Test
    @Tag("unit")
    public void testButtonClickLogsInteraction() throws Exception {
        Button button = new Button("Klick mich");
        button.setId("btn1");
        Scene scene = createSceneWith(button);
        GlobalEventLogger.attachTo(SCENE_NAME, scene);
        simulateMouseClick(button);
        verify(mockLogger).logInteraction(
                eq("Benutzer interagierte mit einem Button auf Fenster {}"),
                eq(SCENE_NAME),
                eq("btn1"),
                eq("Klick mich")
        );
    }

    @Test
    @Tag("unit")
    public void testTextFieldLogsInput() throws Exception {
        TextField field = new TextField("Hallo");
        field.setId("field1");
        Scene scene = createSceneWith(field);
        GlobalEventLogger.attachTo(SCENE_NAME, scene);
        simulateKeyTyped(field);
        verify(mockLogger).logInteraction(
                eq("Benutzer eingabe in ein Textfield auf Fenster {}"),
                eq(SCENE_NAME),
                eq("field1"),
                eq("Hallo")
        );
    }

    @Test
    @Tag("unit")
    public void testPasswordFieldIsIgnored() throws Exception {
        TextField pwField = new TextField("Passwort");
        pwField.setId("passwordInput");
        Scene scene = createSceneWith(pwField);
        GlobalEventLogger.attachTo(SCENE_NAME, scene);
        simulateKeyTyped(pwField);
        verify(mockLogger, never()).logInteraction(anyString(), any(), any(), any());
    }

    @Test
    @Tag("unit")
    public void testCheckBoxClickLogs() throws Exception {
        CheckBox cb = new CheckBox("Zustimmen");
        cb.setId("cb1");
        cb.setSelected(true);
        Scene scene = createSceneWith(cb);
        GlobalEventLogger.attachTo(SCENE_NAME, scene);
        simulateMouseClick(cb);
        verify(mockLogger).logInteraction(
                eq("Benutzer klickte eine Checkbox auf Fenster {}"),
                eq(SCENE_NAME),
                eq("cb1"),
                eq("true")
        );
    }

    private Scene createSceneWith(Control control) throws Exception {
        StackPane root = new StackPane(control);
        Scene scene = new Scene(root, 300, 200);
        runOnFxThread(() -> {});
        return scene;
    }

    private void simulateMouseClick(Control control) throws Exception {
        runOnFxThread(() -> {
            MouseEvent click = new MouseEvent(MouseEvent.MOUSE_CLICKED,
                    0, 0, 0, 0,
                    MouseButton.PRIMARY, 1,
                    false, false, false, false,
                    true, false, false, true,
                    false, false, null);
            control.fireEvent(click);
        });
        Thread.sleep(100);
    }

    private void simulateKeyTyped(Control control) throws Exception {
        runOnFxThread(() -> {
            KeyEvent key = new KeyEvent(KeyEvent.KEY_TYPED,
                    "a", "a", null, false, false, false, false);
            control.fireEvent(key);
        });
        Thread.sleep(100);
    }

    private void setLoggerMock(DefaultInteractionLogger mockLogger) throws Exception {
        Field field = GlobalEventLogger.class.getDeclaredField("interactionLogger");
        field.setAccessible(true);
        field.set(null, mockLogger);
    }

    private void runOnFxThread(Runnable runnable) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                runnable.run();
            } finally {
                latch.countDown();
            }
        });
        latch.await(2, TimeUnit.SECONDS);
    }
}
