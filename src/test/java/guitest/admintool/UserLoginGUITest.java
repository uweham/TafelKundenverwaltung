package guitest.admintool;

import kundenverwaltung.Main;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.api.FxAssert.verifyThat;

public class UserLoginGUITest extends ApplicationTest
{

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception
    {
        new Main().start(stage);
        primaryStage = stage;
    }


    private void waitForWindow(String title)
    {
        long endTime = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < endTime)
        {
            for (Window window : Stage.getWindows())
            {
                if (window instanceof Stage && window.isShowing() && ((Stage) window).getTitle().equals(title))
                {
                    return;
                }
            }
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Test
    @Tag("gui")
    public void testVisibleComponents()
    {
        verifyThat("#textfieldUserName", NodeMatchers.isVisible());
        verifyThat("#passwordField", NodeMatchers.isVisible());
        verifyThat("#buttonSignIn", NodeMatchers.isVisible());
        verifyThat("#buttonExitProgram", NodeMatchers.isVisible());
    }

    @Test
    @Tag("gui")
    public void testFailedLoginEmpty()
    {
        waitForFxEvents();
        
        clickOn("#buttonSignIn");
        
        verifyThat("Anmeldung fehlgeschlagen", NodeMatchers.isVisible());
    }
    
    private void waitForFxEvents()
    {
        try
        {
            Thread.sleep(500);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
