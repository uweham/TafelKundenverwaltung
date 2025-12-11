package kundenverwaltung.controller.server;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.server.dto.UserEntityDTO;
import kundenverwaltung.server.service.UserEntityService;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.service.WindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class UserListController
{
    @FXML
    private TextField suchTextFeld;

    @FXML
    private TableView<UserEntityDTO> benutzerTabelle;

    @FXML
    private TableColumn<UserEntityDTO, String> idSpalte;

    @FXML
    private TableColumn<UserEntityDTO, String> benutzernameSpalte;

    @FXML
    private TableColumn<UserEntityDTO, String> rollenSpalte;

    private final ObservableList<UserEntityDTO> benutzerListe = FXCollections.observableArrayList();

    private final UserEntityService USER_SERVICE = new UserEntityService();

    private final Logger LOGGER = LoggerFactory.getLogger(UserListController.class);

    /**
     * Initializes the user list controller and sets up the table view.
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void initialize() throws IOException
    {
        idSpalte.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUuid().toString()));
        idSpalte.setId("uuid");

        benutzernameSpalte.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));
        benutzernameSpalte.setId("username");

        rollenSpalte.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRole().toString()));
        rollenSpalte.setId("role");

        FilteredList<UserEntityDTO> gefilterteListe = new FilteredList<>(benutzerListe, p -> true);

        suchTextFeld.textProperty().addListener((obs, alt, neu) ->
        {
            gefilterteListe.setPredicate(user ->
            {
                if (neu == null || neu.isBlank())
                {
                    return true;
                }

                String lower = neu.toLowerCase();

                return user.getUsername().toLowerCase().contains(lower) || user.getUuid().toString().toLowerCase().contains(lower);
            });
        });

        SortedList<UserEntityDTO> sortierteListe = new SortedList<>(gefilterteListe);
        sortierteListe.comparatorProperty().bind(benutzerTabelle.comparatorProperty());
        benutzerTabelle.setItems(sortierteListe);

        //  Setup table-preferences...
        TablePreferenceServiceImpl.getInstance().setupPersistence(benutzerTabelle, "UserListEntriesTable", true);

        // Loading users from server...
        loadUserFromServer();
    }

    /**
     * Handles editing a selected user.
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void handleBearbeiten() throws IOException
    {
        UserEntityDTO ausgewaehlt = benutzerTabelle.getSelectionModel().getSelectedItem();

        if (ausgewaehlt != null)
        {
            UserEntityDTO bearbeitet = zeigeBenutzerDialog(ausgewaehlt);

            if (bearbeitet != null)
            {
                int index = benutzerListe.indexOf(ausgewaehlt);

                if (index != -1)
                {
                    benutzerListe.set(index, bearbeitet);
                }
            }

            loadUserFromServer();
        }
        else
        {
            Benachrichtigung.warnungBenachrichtigung("Keine Auswahl", "Bitte wähle einen zu bearbeitenden Benutzer aus!");
        }
    }

    /**
     * Handles creating a new user.
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void handleNeuerBenutzer() throws IOException
    {
        UserEntityDTO neuerBenutzer = zeigeBenutzerDialog(null);

        if (neuerBenutzer != null)
        {
            benutzerListe.add(neuerBenutzer);
            loadUserFromServer();
        }
    }

    /**
     * Handles deleting a selected user.
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void handleBenutzerLoeschen() throws IOException
    {
        UserEntityDTO ausgewaehlt = benutzerTabelle.getSelectionModel().getSelectedItem();

        if (ausgewaehlt != null)
        {
            String errorDisplayInfo = "Ausgewählten Benutzer mit der UUID: '" + ausgewaehlt.getUuid() + "' löschen?";

            if(!Benachrichtigung.deleteConfirmationDialog("Den Benutzer wirklich löschen?", errorDisplayInfo))
            {
                return;
            }

            try
            {
                USER_SERVICE.delete(ausgewaehlt.getUuid());
            }
            catch (Exception exception)
            {
                LOGGER.error(exception.getMessage());
                Benachrichtigung.errorDialog("Tafel-Server", "Löschen eines Benutzers", "Beim Löschen des Benutzers ist ein Fehler aufgetreten!");
                return;
            }

            Benachrichtigung.infoBenachrichtigung("Tafel-Server", "Der Benutzer wurde erfolgreich gelöscht!");
            loadUserFromServer();
        }
        else
        {
            Benachrichtigung.warnungBenachrichtigung("Keine Auswahl", "Bitte wähle einen zu löschenden Benutzer aus!");
        }
    }

    /**
     * Handles closing the user list window.
     */
    @FXML
    private void handleSchliessen()
    {
        Stage stage = (Stage) benutzerTabelle.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles double-click editing of users.
     * @param event the mouse event
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void handleBenutzerBearbeiten(MouseEvent event) throws IOException
    {
        if (event.getClickCount() == 2 && benutzerTabelle.getSelectionModel().getSelectedItem() != null)
        {
            handleBearbeiten();
        }
    }

    /**
     * Shows the user dialog for creating or editing a user.
     * @param benutzer the user to edit, or null for creating a new user
     * @return the edited or created user, or null if cancelled
     */
    private UserEntityDTO zeigeBenutzerDialog(UserEntityDTO benutzer)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/server/UserNewAndEdit.fxml"));
            Stage dialogStage = new Stage();

            dialogStage.setTitle(benutzer == null ? "Neuen Benutzer anlegen" : "Benutzer bearbeiten");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(benutzerTabelle.getScene().getWindow());
            dialogStage.setScene(new Scene(loader.load()));

            UserNewAndEditController controller = loader.getController();
            controller.setUser(benutzer);

            dialogStage.showAndWait();

            if (controller.isSaved())
            {
                return controller.getCurrentUser();
            }

            return null;
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage());
            Benachrichtigung.errorDialog("Fehler", "Es ist ein Fehler beim öffnen aufgetreten!", e.getMessage());
            return null;
        }
    }

    /**
     * Loads users from the server.
     * @throws IOException if an I/O error occurs
     */
    private void loadUserFromServer() throws IOException
    {
        benutzerListe.clear();

        try
        {
            // Loading users from server...
            List<UserEntityDTO> users = USER_SERVICE.getAll(UserEntityDTO.class);
            benutzerListe.setAll(users);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());

            boolean showAndWait = true;
            WindowService.openWindow("/kundenverwaltung/fxml/server/ServerConnectionError.fxml", "Tafel-Server", new Stage(), StageStyle.DECORATED, Modality.APPLICATION_MODAL, showAndWait);

            Platform.runLater(() ->
            {
                Stage currentStage = (Stage) benutzerTabelle.getScene().getWindow();
                currentStage.close();
            });
        }
    }
}
