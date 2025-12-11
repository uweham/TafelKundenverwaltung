package kundenverwaltung;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kundenverwaltung.controller.errorreport.ErrorReportSubmitController;
import kundenverwaltung.logger.event.GlobalEventLogger;
import kundenverwaltung.logger.file.LogFileService;
import kundenverwaltung.service.WindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;


public final class NewMain
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NewMain.class);

    static
    {
        try
        {
            //  Needs to get hooked right before logback!
            LogFileService.loadLogDirectoryPath();
            LogFileService.reloadLogbackConfiguration();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private NewMain()
    {

    }

    public static void main(String[] args)
    {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
        {
            LOGGER.error("Unbehandelter Fehler im Thread '{}'", thread.getName(), throwable);

            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(WindowService.class.getResource("/kundenverwaltung/fxml/errorreport/ErrorReportSubmit.fxml"));
                AnchorPane pane = fxmlLoader.load();

                Scene scene = new Scene(pane);
                Stage stage = new Stage();

                GlobalEventLogger.attachTo("ErrorReport", scene);

                stage.setTitle("Fehler");
                stage.setScene(scene);
                stage.initStyle(StageStyle.DECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.centerOnScreen();

                ErrorReportSubmitController controller = fxmlLoader.getController();
                controller.setErrorMessage(throwable.getMessage());

                stage.show();
            }
            catch (IOException exception)
            {
                LOGGER.error("Fehler beim öffnen des Fehlerbericht dialogs!", exception);
            }
        });

        try
        {
            Main.main(args);
        }
        catch (Throwable throwable)
        {
            LOGGER.error("Fehler in der Anwendung!", throwable);
        }
    }
}
