package kundenverwaltung.controller.server;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kundenverwaltung.logger.event.GlobalEventLogger;
import kundenverwaltung.server.service.AuthService;

import java.io.IOException;
import java.util.Objects;


public class ServerDashboardController
{
    @FXML
    private VBox boxList;

    @FXML
    private Button btnBenutzerverwaltung;

    @FXML
    private Button btnErrorReports;

    @FXML
    private Button btnWishRequests;

    @FXML
    private Button btnTracking;


    /**
     * Initializes the server dashboard controller.
     */
    public final void initialize()
    {
        if (!AuthService.getInstance().isAdmin())
        {
            boxList.getChildren().remove(btnBenutzerverwaltung);
            boxList.getChildren().remove(btnTracking);
        }
    }

    /**
     * Loads a window with the specified FXML path and title.
     * @param fxmlPath the path to the FXML file
     * @param title the title of the window
     */
    private void loadWindow(String fxmlPath, String title)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            GlobalEventLogger.attachTo("ServerDashboard", scene);
            stage.setScene(scene);

            stage.setTitle(title);
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            stage.show();
        }
        catch (IOException e)
        {
            showErrorDialog("Fehler beim Laden des Fensters",
                    "Die Ansicht '" + fxmlPath + "' konnte nicht geladen werden.");
        }
        catch (NullPointerException e)
        {
            showErrorDialog("Datei nicht gefunden",
                    "Die FXML-Datei unter dem Pfad '" + fxmlPath + "' wurde nicht gefunden.");
        }
    }

    /**
     * Handles the help action to show information about the dashboard.
     * @param event the action event
     */
    @FXML
    private void handleHelp(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Tafel Server Verwaltungs-Dashboard");

        alert.setContentText("Dies ist die zentrale Steuerung für die Server-Anwendung.\n"
                +
                "Nutzen Sie das Navigationsmenü auf der rechten Seite, um die einzelnen Module zu öffnen.");

        alert.showAndWait();
    }

    /**
     * Shows an error dialog with the specified header and content.
     * @param header the error dialog header
     * @param content the error dialog content
     */
    private void showErrorDialog(String header, String content)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * Handles opening the user management window.
     * @param event the action event
     */
    @FXML
    private void handleOpenBenutzerverwaltung(ActionEvent event)
    {
        loadWindow("/kundenverwaltung/fxml/server/UserList.fxml", "Benutzerverwaltung");
    }

    /**
     * Handles opening the wish requests window.
     * @param event the action event
     */
    @FXML
    private void handleOpenWishRequests(ActionEvent event)
    {
        loadWindow("/kundenverwaltung/fxml/server/WishRequestList.fxml", "Wunschanfragen");
    }

    /**
     * Handles opening the error reports window.
     * @param event the action event
     */
    @FXML
    private void handleOpenErrorReports(ActionEvent event)
    {
        loadWindow("/kundenverwaltung/fxml/server/ErrorReportList.fxml", "Fehlerberichte");
    }

    /**
     * Handles opening the tracking window.
     * @param event the action event
     */
    @FXML
    private void handleOpenTracking(ActionEvent event)
    {
        loadWindow("/kundenverwaltung/fxml/server/TrackingList.fxml", "Tracking");
    }

    /**
     * Handles the exit application action.
     * @param event the action event
     */
    @FXML
    private void handleBeenden(ActionEvent event)
    {
        Platform.exit();
    }
}
