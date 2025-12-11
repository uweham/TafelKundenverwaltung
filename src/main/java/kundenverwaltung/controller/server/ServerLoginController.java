package kundenverwaltung.controller.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kundenverwaltung.server.service.AuthService;
import kundenverwaltung.server.service.UserEntityService;
import kundenverwaltung.service.WindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ServerLoginController
{
    private final Logger LOGGER = LoggerFactory.getLogger(ServerLoginController.class);

    @FXML
    private TextField textfieldUserName;

    @FXML
    private PasswordField passwordField;

    /**
     * Handles the sign-in action for user authentication.
     * @param event the action event
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void signIn(ActionEvent event) throws IOException
    {
        String username = textfieldUserName.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank() || password == null || password.isBlank())
        {
            showAlert("Fehlerhafte Eingabe", "Benutzername und Passwort dürfen nicht leer sein.");
            return;
        }

        try
        {
            AuthService.getInstance().signIn(username, password);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            showAlert("Anmeldung fehlgeschlagen", "Falscher Benutzername oder Passwort!");
            return;
        }

        Stage stage = (Stage) textfieldUserName.getScene().getWindow();
        stage.close();

        UserEntityService.getInstance().resetCachedUserEntityList();
        UserEntityService.getInstance().getCachedUserEntityList();

        WindowService.openWindow("/kundenverwaltung/fxml/server/ServerDashboard.fxml", "Server Dashboard");
     }

    /**
     * Handles the exit program action.
     * @param event the action event
     */
    @FXML
    private void exitProgram(ActionEvent event)
    {
        Stage stage = (Stage) textfieldUserName.getScene().getWindow();
        stage.close();
    }

    /**
     * Shows an alert dialog with the specified title and message.
     * @param title the alert title
     * @param message the alert message
     */
    private void showAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hashes a password using a simple hash function.
     * @param password the password to hash
     * @return the hashed password as a hex string
     */
    private String hashPassword(String password)
    {
        return Integer.toHexString(password.hashCode());
    }
}
