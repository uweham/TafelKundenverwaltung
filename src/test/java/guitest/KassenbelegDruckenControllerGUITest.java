package guitest;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import kundenverwaltung.logger.event.GlobalEventLogger;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class KassenbelegDruckenControllerGUITest extends ApplicationTest
{

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/Kundenverwaltung/FXML/KassenbelegDrucken.fxml"));

        Scene scene = new Scene(root);
        GlobalEventLogger.attachTo("KassenbelegDrucken", scene);
        stage.setScene(scene);

        stage.show();
        primaryStage = stage;
    }

    @Test
    @Tag("gui")
    public void testUIComponentsExist()
    {
        assertNotNull(lookup("#labelPeriod").query());
        assertNotNull(lookup("#labelProductType").query());
        assertNotNull(lookup("#labelDate").query());
        assertNotNull(lookup("#lbVerteilstelle").query());
        assertNotNull(lookup("#labelOrderBy").query());
        assertNotNull(lookup("#labelTamplate").query());
        assertNotNull(lookup("#labelHeader").query());

        assertNotNull(lookup("#rbAktuellerKunde").query());
        assertNotNull(lookup("#rbAlleKunden").query());

        assertNotNull(lookup("#cbWarentyp").queryComboBox());
        assertNotNull(lookup("#cbZeitraum").queryComboBox());
        assertNotNull(lookup("#cbVorlage").queryComboBox());
        assertNotNull(lookup("#cbSortierreihnfolge").queryComboBox());
        assertNotNull(lookup("#cbSortierenNach").queryComboBox());
        assertNotNull(lookup("#cbVerteilstelle").queryComboBox());

        assertNotNull(lookup("#cbxZeitraumAutomatisch").query());
        assertNotNull(lookup("#cbxEinstellungenAlsStandard").query());
        assertNotNull(lookup("#cbxLiefernummer").query());
        assertNotNull(lookup("#cbxVerteilstelleAutomatisch").query());

        assertNotNull(lookup("#buttonCancel").query());
        assertNotNull(lookup("#buttonCreate").query());

        assertNotNull(lookup("#datum").query());
    }

    @Test
    @Tag("gui")
    public void testButtonCreateActionWithoutSelectingCustomer() throws Exception
    {
        clickOn("#buttonCreate");

        Thread.sleep(500);
        WaitForAsyncUtils.waitForFxEvents();

        boolean progressBarVisible = isProgressBarVisible();
        System.out.println("Progressbar sichtbar? " + progressBarVisible);

        assertTrue(primaryStage.isShowing(), "Main window should remain open after clicking 'Erstellen'");
    }

    @Test
    @Tag("gui")
    public void testButtonCreateActionWithAllCustomers() throws Exception
    {
        clickOn("#rbAlleKunden");
        clickOn("#buttonCreate");

        Thread.sleep(500);
        WaitForAsyncUtils.waitForFxEvents();

        boolean progressBarVisible = isProgressBarVisible();
        System.out.println("Progressbar sichtbar? " + progressBarVisible);

        assertTrue(primaryStage.isShowing(), "Main window should remain open after clicking 'Erstellen'");
    }

    private boolean isProgressBarVisible()
    {
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] isVisible = {false};

        Platform.runLater(() ->
        {
            try {
                Stage progressBarStage = findProgressBarStage();
                isVisible[0] = progressBarStage != null && progressBarStage.isShowing();
            } finally {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return isVisible[0];
    }

    private Stage findProgressBarStage()
    {
        for (Window window : Window.getWindows())
        {
            if (window instanceof Stage)
            {
                Stage stage = (Stage) window;
                if ("Prozess läuft..".equals(stage.getTitle()))
                {
                    return stage;
                }
            }
        }
        return null;
    }

    @Test
    @Tag("gui")
    public void testButtonCancelAction()
    {
        clickOn("#buttonCancel");

        WaitForAsyncUtils.waitForFxEvents();
        assertFalse(primaryStage.isShowing(), "Das Fenster sollte nach dem Klicken auf 'Abbrechen' geschlossen sein.");
    }
}
