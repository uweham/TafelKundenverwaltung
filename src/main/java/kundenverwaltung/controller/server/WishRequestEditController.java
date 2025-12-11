package kundenverwaltung.controller.server;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kundenverwaltung.Benachrichtigung;

import kundenverwaltung.server.dto.ErrorReportDTO;
import kundenverwaltung.server.dto.ScreenshotDTO;
import kundenverwaltung.server.dto.WishRequestDTO;
import kundenverwaltung.server.dto.UserEntityDTO;
import kundenverwaltung.server.service.WishRequestService;
import kundenverwaltung.server.service.UserEntityService;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.toolsandworkarounds.FileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class WishRequestEditController
{
    private final Logger LOGGER = LoggerFactory.getLogger(WishRequestEditController.class);
    private final WishRequestService WISH_REQUEST_SERVICE = new WishRequestService();

    @FXML
    private TextField titleField;

    @FXML
    private ComboBox<WishRequestDTO.Status> statusComboBox;

    @FXML
    private ComboBox<String> userNameComboBox;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextArea internNotesArea;

    @FXML
    private Button btnOpenFile;

    private WishRequestDTO currentWishRequest;

    private boolean savedState = false;

    /**
     * Initializes the wish request edit controller.
     */
    @FXML
    public void initialize()
    {
        // Fill combo boxes with data
        List<UserEntityDTO> userEntityList = UserEntityService.getInstance().getCachedUserEntityList();
        String[] userNames = userEntityList.stream()
                .map(UserEntityDTO::getUsername)
                .toArray(String[]::new);

        statusComboBox.getItems().addAll(WishRequestDTO.Status.values());
        userNameComboBox.getItems().addAll(userNames);
    }

    /**
     * Sets the wish request data to be edited.
     * @param request the wish request DTO to edit
     */
    public void setWishRequest(WishRequestDTO request)
    {
        currentWishRequest = WishRequestDTO.builder()
                .setUuid(request.getUuid())
                .setTitle(request.getTitle())
                .setStatus(request.getStatus())
                .setDescription(request.getDescription())
                .setInternNotes(request.getInternNotes())
                .setUser(request.getUser())
                .setScreenshot(request.getScreenshot())
                .build();

        titleField.setText(currentWishRequest.getTitle());
        statusComboBox.setValue(currentWishRequest.getStatus());

        String userName = ( currentWishRequest.getUser() != null && currentWishRequest.getUser().getUsername() != null ) ? currentWishRequest.getUser().getUsername() : "";
        userNameComboBox.setValue(userName);

        descriptionArea.setText(currentWishRequest.getDescription());
        internNotesArea.setText(currentWishRequest.getInternNotes());

        //  File entry...
        if(currentWishRequest.getScreenshot() == null || currentWishRequest.getScreenshot().getScreenshot() == null)
        {
            btnOpenFile.setVisible(false);
            currentWishRequest.setScreenshot(null);
        }
    }

    /**
     * Handles the save action for the wish request.
     */
    @FXML
    private void onSave()
    {
        UserEntityDTO userEntityDTO = currentWishRequest.getUser();
        Optional<UserEntityDTO> optionalUser = UserEntityService.getInstance()
                .getCachedUserEntityList()
                .stream()
                .filter(entry -> entry.getUsername().equals(userNameComboBox.getValue()))
                .findFirst();

        if (optionalUser.isPresent())
        {
            userEntityDTO = optionalUser.get();
        }

        currentWishRequest.setUser(userEntityDTO);
        currentWishRequest.setInternNotes(internNotesArea.getText());
        currentWishRequest.setStatus(statusComboBox.getValue());

        try
        {
            WISH_REQUEST_SERVICE.update(currentWishRequest, WishRequestDTO.class);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            Benachrichtigung.errorDialog("Tafel-Server", "Fehler beim Speichern!", "Es ist ein Fehler beim Speichern der Wunschanfrage aufgetreten!");
            return;
        }

        savedState = true;
        ((Stage) titleField.getScene().getWindow()).close();
    }

    /**
     * Handles the cancel action for the wish request.
     */
    @FXML
    private void onCancel()
    {
        savedState = false;
        ((Stage) titleField.getScene().getWindow()).close();
    }

    /**
     * Returns whether the wish request has been saved.
     * @return true if the wish request has been saved, false otherwise
     */
    public boolean hasBeenSaved()
    {
        return savedState;
    }

    /**
     * Returns the current wish request being edited.
     * @return the current wish request DTO
     */
    public WishRequestDTO getCurrentWishRequest()
    {
        return currentWishRequest;
    }

    /**
     * Handles opening the attached file for the wish request.
     * @param actionEvent the action event
     */
    @FXML
    public void onOpenFile(ActionEvent actionEvent)
    {
        WishRequestDTO wishRequestDTO = getCurrentWishRequest();

        if(wishRequestDTO.getScreenshot() == null)
        {
            return;
        }

        ScreenshotDTO screenshotDTO = wishRequestDTO.getScreenshot();

        byte[] data = screenshotDTO.getScreenshot();
        ScreenshotDTO.FileType fileType = screenshotDTO.getFileType();

        String extension = fileType.name().toLowerCase();

        //  Opens given file...
        FileHandler.openFile(data, "screenshot_", extension);
    }
}
