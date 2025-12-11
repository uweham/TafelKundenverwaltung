package kundenverwaltung.controller.admintool;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import kundenverwaltung.logger.event.GlobalEventLogger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import kundenverwaltung.service.WindowService;

public class AdmintoolController
{

    @FXML private Pane mainPane;
    @FXML private Button exit;

    // Merker für die zuletzt gewählte Ausgabegruppe (zum Erhalten der Selektion)
    private static Integer lastSelectedAusgabegruppeId = null;

    public static Integer getLastSelectedAusgabegruppeId()
    { return lastSelectedAusgabegruppeId;
    }
    public static void setLastSelectedAusgabegruppeId(Integer id)
    { lastSelectedAusgabegruppeId = id;
    }
    /**
    *
    */
    @FXML
    public void initialize() throws IOException
    {
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Uebersicht.fxml")));
    }

    private static AdmintoolController instance;

    public static AdmintoolController getInstance()
    {
        if (instance == null)
        {
            instance = new AdmintoolController();
        }
        return instance;
    }

    private static BenutzerverwaltungController benutzerverwaltungController;

    public static BenutzerverwaltungController getBenutzerverwaltungController()
    { return benutzerverwaltungController;
    }
    public static void setBenutzerverwaltungController(BenutzerverwaltungController c)
    { benutzerverwaltungController = c;
    }

    /**
    *
    */
    @FXML
    public void openUebersicht(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Uebersicht.fxml")));
    }

    /**
    *
    */
    @FXML
    public void openBenutzerverwaltung(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Benutzerverwaltung.fxml")));
    }

    /**
    *
    */
    @FXML
    public void openUserAdministration(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/UserAdministration.fxml")));
    }

    /**
    *
    */
    @FXML
    public void openGrundeinstellungen(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Grundeinstellungen.fxml")));
    }

    /**
    *
    */
    @FXML
    public void openVerteilstellen(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Verteilstelle.fxml")));
    }

    /**
    *
    */
    @FXML
    public void openAusgabegruppen(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Ausgabegruppen.fxml")));
    }

    /**
    *
    */
    @FXML
    public void openWarentypen(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Warentypen.fxml")));
    }

    /**
    *
    */
    @FXML
    public void openBescheidarten(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Bescheidarten.fxml")));
    }

    /**
    *
    */
    public void openBescheidarten() throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Bescheidarten.fxml")));
    }

    /**
    *
    */
    @FXML
    public void openNationen(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Nationen.fxml")));
    }

    /**
    *
    */
    public void openNationen() throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Nationen.fxml")));
    }

    /**
    *
    */
    @FXML
    public void openOrtsteile(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Ortsteile.fxml")));
    }

    @FXML
    public void openWishRequestList4Members() throws IOException
    {
        WindowService.openWindow("/kundenverwaltung/fxml/wishrequest/WishRequestList4Member.fxml", "Wunschanfragen-Liste");
    }

    /**
    *
    */
    @FXML
    public void openDokumentenvorlage(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Dokumentenvorlage.fxml")));
    }

    /**
     * Opens the dialog for the tafel-server-login.
     *
     * @param event the action event
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void openTafelServerLoginDialog(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();

        WindowService.openWindow("/kundenverwaltung/fxml/server/ServerLogin.fxml", "Tafel-Server Anmeldung");
    }

    /**
    *
    */
    @FXML
    public void openEinstellungen(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Einstellungen.fxml")));
    }

    /**
    *
    */
    @FXML
    public void openPflegeOptimierung(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/PflegeOptimierung.fxml")));
    }

    /**
    *
    */
    @FXML
    public void openDatenbanksicherung(ActionEvent event) throws IOException
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(FXMLLoader
                .load(getClass().getResource("/kundenverwaltung/fxml/admintool/Datenbanksicherung.fxml")));
    }

    /**
     * Opens the SQL Query Tool screen.
     *
     * @param event the action event
     */
    @FXML
    public void openSQLQueryTool(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/StatistiktoolSQL.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("SQL-Abfrage Tool");

            Scene scene = new Scene(root);
            GlobalEventLogger.attachTo("StatistikToolSQL", scene);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler beim Laden der FXML-Datei.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
    *
    */
    public void exit(ActionEvent event)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Admintool");
        alert.setHeaderText("Achtung!");
        alert.setContentText("Sind Sie sicher, dass Sie das Admintool schließen möchten?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.close();
        }
    }
}
