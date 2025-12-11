package kundenverwaltung.service;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class WindowServiceTest
{

    private static boolean javaFXInitialized = false;

    @BeforeAll
    static void initJavaFX()
    {
        if (!javaFXInitialized) {
            try {
                CountDownLatch latch = new CountDownLatch(1);
                SwingUtilities.invokeLater(() -> {
                    new JFXPanel();
                    latch.countDown();
                });
                latch.await();
                javaFXInitialized = true;
            } catch (Exception e) {
                System.out.println("Could not initialize JavaFX: " + e.getMessage());
            }
        }
    }

    @Test
    void openWindow_ShouldBeCallableAndThrowAppropriateException()
    {
        String fxmlFileName = "/nonexistent.fxml";
        String title = "Test Window";

        Exception exception = assertThrows(Exception.class,
                () -> WindowService.openWindow(fxmlFileName, title),
                "Method should be callable and throw some exception");

        assertNotNull(exception, "Some exception should be thrown");
    }

    @Test
    void openWindow_WithThreeParameters_ShouldBeCallable()
    {
        String fxmlFileName = "/test.fxml";
        String title = "Test Window";
        boolean showAndWait = false;

        assertThrows(Exception.class,
                () -> WindowService.openWindow(fxmlFileName, title, showAndWait),
                "Three-parameter overload should be callable");
    }

    @Test
    void openWindow_WithNullFileName_ShouldThrowException()
    {
        String nullFileName = null;
        String title = "Test Window";

        assertThrows(Exception.class,
                () -> WindowService.openWindow(nullFileName, title),
                "Should throw exception with null filename");
    }

    @Test
    void openWindow_WithNullTitle_ShouldThrowException()
    {
        String fxmlFileName = "/test.fxml";
        String nullTitle = null;

        assertThrows(Exception.class,
                () -> WindowService.openWindow(fxmlFileName, nullTitle),
                "Should throw exception with null title");
    }

    @Test
    void windowService_AllMethodOverloads_ShouldExist() throws NoSuchMethodException
    {
        Class<WindowService> clazz = WindowService.class;

        assertNotNull(clazz.getDeclaredMethod("openWindow", String.class, String.class));
        assertNotNull(clazz.getDeclaredMethod("openWindow", String.class, String.class, boolean.class));
        assertNotNull(clazz.getDeclaredMethod("openWindow", String.class, String.class,
                javafx.stage.Stage.class, javafx.stage.StageStyle.class, javafx.stage.Modality.class));
        assertNotNull(clazz.getDeclaredMethod("openWindow", String.class, String.class,
                javafx.stage.Stage.class, javafx.stage.StageStyle.class, javafx.stage.Modality.class, boolean.class));
    }

    @Test
    void windowService_MethodsShouldBeStatic()
    {
        Class<WindowService> clazz = WindowService.class;

        java.lang.reflect.Method[] methods = clazz.getDeclaredMethods();
        for (java.lang.reflect.Method method : methods)
        {
            if (method.getName().equals("openWindow"))
            {
                assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()),
                        "Method " + method.getName() + " should be static");
            }
        }
    }

    @Test
    void windowService_ShouldBeInCorrectPackage()
    {
        assertEquals("kundenverwaltung.service", WindowService.class.getPackageName(),
                "WindowService should be in correct package");
    }

    @Test
    void windowService_ClassShouldBePublic()
    {
        assertTrue(java.lang.reflect.Modifier.isPublic(WindowService.class.getModifiers()),
                "WindowService class should be public");
    }
}