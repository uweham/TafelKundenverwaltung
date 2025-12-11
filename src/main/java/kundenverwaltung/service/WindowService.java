package kundenverwaltung.service;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kundenverwaltung.logger.event.GlobalEventLogger;

import java.io.IOException;

public class WindowService
{
    /**
     * Opens a new window with the specified properties.
     *
     * @param fxmlFileName The name of the FXML file to load for the window's content.
     * @param title        The title of the window.
     * @param stage        The Stage to use for the new window.
     * @param stageStyle   The style of the stage (e.g., DECORATED, UNDECORATED).
     * @param modality     The modality for the new window (e.g., APPLICATION_MODAL).
     * @param showAndWait  If true, the method will block until the new window is closed.
     * @throws IOException if the FXML file cannot be loaded.
     */
    public static void openWindow(String fxmlFileName, String title, Stage stage, StageStyle stageStyle, Modality modality, boolean showAndWait) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(WindowService.class.getResource(fxmlFileName));
        Parent parent = fxmlLoader.load();

        Scene scene = new Scene(parent);

        GlobalEventLogger.attachTo("WindowService" + title, scene);

        stage.setTitle(title);
        stage.setScene(scene);
        stage.initStyle(stageStyle);
        stage.initModality(modality);
        stage.centerOnScreen();

        if (showAndWait)
        {
            stage.showAndWait();
        }
        else
        {
            stage.show();
        }
    }

    /**
     * Opens a new modal window with a decorated style.
     *
     * @param fxmlFileName The name of the FXML file to load.
     * @param title        The title of the window.
     * @param showAndWait  If true, blocks until the window is closed.
     * @throws IOException if the FXML file cannot be loaded.
     */
    public static void openWindow(String fxmlFileName, String title, boolean showAndWait) throws IOException
    {
        openWindow(fxmlFileName, title, new Stage(), StageStyle.DECORATED, Modality.APPLICATION_MODAL, showAndWait);
    }

    /**
     * Opens a new non-modal window with a decorated style.
     *
     * @param fxmlFileName The name of the FXML file to load.
     * @param title        The title of the window.
     * @throws IOException if the FXML file cannot be loaded.
     */
    public static void openWindow(String fxmlFileName, String title) throws IOException
    {
        openWindow(fxmlFileName, title, false);
    }

    /**
     * Opens a new non-modal window with the specified style and modality.
     *
     * @param fxmlFileName The name of the FXML file to load.
     * @param title        The title of the window.
     * @param stage        The Stage to use for the new window.
     * @param stageStyle   The style of the stage.
     * @param modality     The modality for the new window.
     * @throws IOException if the FXML file cannot be loaded.
     */
    public static void openWindow(String fxmlFileName, String title, Stage stage, StageStyle stageStyle, Modality modality) throws IOException
    {
        openWindow(fxmlFileName, title, stage, stageStyle, modality, false);
    }
}