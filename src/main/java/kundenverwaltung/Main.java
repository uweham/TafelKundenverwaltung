package kundenverwaltung;

import javafx.application.Application;
import javafx.stage.Stage;
import kundenverwaltung.controller.MainController;
import kundenverwaltung.controller.DatabaseConnectionController;
import kundenverwaltung.service.WindowService;
import setupprogram.StartSetup;
import java.io.IOException;

/**
 * @Author Gruppe_1
 * @Author Adam Starobrzanski
 * Date: 24.07.2018
 */

public class Main extends Application
{
    @Override
    public final void start(Stage primaryStage) throws IOException
    {
        DatabaseConnectionController dbController = new DatabaseConnectionController();

        boolean connectionSuccessful = dbController.checkDatabaseConnection();

        if (connectionSuccessful)
        {
            MainController.getInstance().oeffneAnmeldung();
            MainController.getInstance().setPrimaryStage(primaryStage);
        }
        else
        {

            StartSetup setup = new StartSetup();

            try
            {
                setup.start(new Stage());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        primaryStage.setOnCloseRequest(we ->
        {
            we.consume();

            try
            {
                kundenverwaltung.toolsandworkarounds.ExitProgramBackupWarning.showBackupWarning();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
