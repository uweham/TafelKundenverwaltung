package kundenverwaltung.controller.wishrequest;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kundenverwaltung.server.dto.WishRequestDTO;
import kundenverwaltung.server.service.WishRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WishRequestSubmitController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WishRequestSubmitController.class);
    private final WishRequestService WISH_REQUEST_SERVICE = new WishRequestService();

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private Button screenshotButton;
    @FXML private Label selectedFileLabel;

    private File selectedScreenshot;
    private WishRequestDTO currentWishRequest;

    /**
     * Initializes the controller. This method is called automatically after the FXML file has been loaded.
     * It sets up the action for the screenshot selection button.
     */
    @FXML
    public void initialize()
    {
        currentWishRequest = new WishRequestDTO();

        screenshotButton.setOnAction(event ->
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Screenshot auswählen");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Bilddateien", "*.png", "*.jpg", "*.jpeg")
            );

            File file = fileChooser.showOpenDialog(null);

            if (file != null)
            {
                selectedScreenshot = file;
                selectedFileLabel.setText(file.getName());
            }
        });
    }

    /**
     * Handles the action of the "Send" button. It validates the input,
     * creates a wish request DTO, and sends it to the server.
     */
    @FXML
    private void onSendWishRequestToServerButton()
    {
        if (!isValid())
        {
            return;
        }

        String title = titleField.getText().isBlank()
                ? LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                : titleField.getText();

        currentWishRequest.setTitle(title);
        currentWishRequest.setDescription(descriptionArea.getText());
        currentWishRequest.setStatus(WishRequestDTO.Status.Offen);

        ((Stage) titleField.getScene().getWindow()).close();

        WISH_REQUEST_SERVICE.sendWishRequestToServerViaSubmit(currentWishRequest, selectedScreenshot);
    }

    /**
     * Handles the action of the "Cancel" button, closing the window.
     */
    @FXML
    private void handleCancel()
    {
        ((Stage) titleField.getScene().getWindow()).close();
    }

    /**
     * Validates the user input for the wish request.
     *
     * @return true if the input is valid, false otherwise.
     */
    private boolean isValid()
    {
        StringBuilder errorBuilder = new StringBuilder();

        if (titleField.getText().isBlank())
            errorBuilder.append("Titel darf nicht leer sein.\n");

        if (descriptionArea.getText().isBlank())
            errorBuilder.append("Beschreibung darf nicht leer sein.\n");

        if (!errorBuilder.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eingabefehler");
            alert.setHeaderText("Bitte Eingaben prüfen:");
            alert.setContentText(errorBuilder.toString());

            Platform.runLater(alert::showAndWait);
            return false;
        }

        return true;
    }
}