package kundenverwaltung.controller.server;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.server.dto.UserEntityDTO;
import kundenverwaltung.server.service.UserEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class UserNewAndEditController
{
    @FXML
    private Label labelTitle;

    @FXML
    private TextField usernameField;

    @FXML
    private Label txtRepeatPassword;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordField2;

    @FXML
    private ComboBox<UserEntityDTO.Role> roleComboBox;

    private UserEntityDTO currentUser;

    private final UserEntityService USER_ENTITY_SERVICE = new UserEntityService();

    private final Logger LOGGER = LoggerFactory.getLogger(UserNewAndEditController.class);

    private boolean saved = false;

    private boolean userCreation = false;

    /**
     * Initializes the user new and edit controller.
     */
    @FXML
    public void initialize()
    {
        roleComboBox.setItems(FXCollections.observableArrayList(UserEntityDTO.Role.values()));
    }

    /**
     * Sets the user to edit or creates a new user.
     * @param user the user to edit, or null for creating a new user
     */
    public void setUser(UserEntityDTO user)
    {
        this.userCreation = (user == null);

        if (userCreation)
        {
            this.currentUser = new UserEntityDTO();

            labelTitle.setText("Neuen Benutzer");
            usernameField.clear();
            roleComboBox.getSelectionModel().clearSelection();
            passwordField.clear();
            passwordField.setPromptText("Erforderlich für neuen Benutzer");

            passwordField2.clear();
            passwordField2.setPromptText("Passwort wiederholen");
        }
        else
        {
            this.currentUser = user;

            labelTitle.setText("Benutzer bearbeiten");
            usernameField.setText(user.getUsername());
            roleComboBox.setValue(user.getRole());
            passwordField.setPromptText("Leer lassen für keine Änderung");
            passwordField2.clear();
        }
    }

    /**
     * Handles saving the user data.
     */
    @FXML
    private void handleSave()
    {
        if (!isValid())
        {
            return;
        }

        currentUser.setUsername(usernameField.getText().trim());
        currentUser.setRole(roleComboBox.getValue());

        String password = passwordField.getText();

        if (password != null && !password.isBlank())
        {
            currentUser.setPasswordHash(password);
        }

        try
        {
            if(isUserCreation())
            {
                USER_ENTITY_SERVICE.create(currentUser, UserEntityDTO.class);
            }
            else
            {
                USER_ENTITY_SERVICE.update(currentUser, UserEntityDTO.class);

            }

            this.saved = true;
            closeStage();
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            Benachrichtigung.errorDialog("Tafel-Server", "Fehler beim Speichern!", "Es ist ein Fehler beim Speichern des Benutzers aufgetreten!");
            return;
        }
    }

    /**
     * Handles canceling the user edit operation.
     */
    @FXML
    private void handleCancel()
    {
        closeStage();
    }

    /**
     * Validates the user input data.
     * @return true if the input is valid, false otherwise
     */
    private boolean isValid()
    {
        StringBuilder errorMessage = new StringBuilder();

        if (usernameField.getText() == null || usernameField.getText().trim().isEmpty())
        {
            errorMessage.append("Username cannot be empty.\n");
        }

        if (roleComboBox.getValue() == null)
        {
            errorMessage.append("A role must be selected.\n");
        }

        if (isUserCreation() && (currentUser.getPasswordHash() == null && (passwordField.getText() == null || passwordField.getText().isBlank())))
        {
            errorMessage.append("A password must be set for a new user.\n");
        }

        if(isUserCreation())
        {
            if(!passwordField.getText().equals(passwordField2.getText()))
            {
                errorMessage.append("Die Passwörter müssen gleich sein!");
            }
        }
        else
        {
            if(passwordField.getText() != null && !passwordField.getText().isEmpty())
            {
                if(!passwordField.getText().equals(passwordField2.getText()))
                {
                    errorMessage.append("Die Passwörter müssen gleich sein!");
                }
            }
        }

        if (errorMessage.isEmpty())
        {
            return true;
        }
        else
        {
            showInvalidMessage(errorMessage.toString());
            return false;
        }
    }

    /**
     * Shows an error message for invalid input.
     * @param message the error message to display
     */
    private void showInvalidMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Falsche Eingabe!");
        alert.setHeaderText("Bitte korrigiere folgende Eingaben:");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Returns the current user being edited.
     * @return the current user DTO
     */
    public UserEntityDTO getCurrentUser()
    {
        return currentUser;
    }

    /**
     * Returns whether the user has been saved.
     * @return true if the user has been saved, false otherwise
     */
    public boolean isSaved()
    {
        return saved;
    }

    /**
     * Returns whether this is a user creation operation.
     * @return true if creating a new user, false if editing an existing user
     */
    public boolean isUserCreation()
    {
        return userCreation;
    }

    /**
     * Closes the current stage.
     */
    private void closeStage()
    {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
