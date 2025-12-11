package setupprogram;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kundenverwaltung.logger.event.GlobalEventLogger;

/**
 * Starts the setup process of the "Tafel-Kundenverwaltungsprogramm".
 *
 * @author Robin Becker
 * @version 1.0
 * @Date 15.08.2018
 */
public class StartSetup extends Application
{

    private static Stage welcomeScreenStg;
    /**
     *.
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        StartSetup.setWelcomeScreenStg(stage);
        Parent root = FXMLLoader.load(getClass().getResource("/setupprogram/fxml/WelcomeScreen.fxml"));
        stage.setTitle("Einrichtung");

        Scene scene = new Scene(root);
        GlobalEventLogger.attachTo("WelcomeScreen", scene);
        stage.setScene(scene);

        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }


    public static Stage getWelcomeScreenStg()
    {
      return welcomeScreenStg;
    }


    public static void setWelcomeScreenStg(Stage welcomeScreenStg)
    {
      StartSetup.welcomeScreenStg = welcomeScreenStg;
    }
}
