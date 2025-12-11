package kundenverwaltung.server.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.server.api.ServerClient;
import kundenverwaltung.server.base.BaseServiceImpl;
import kundenverwaltung.server.dto.ScreenshotDTO;
import kundenverwaltung.server.dto.WishRequestDTO;
import kundenverwaltung.server.exception.EntityCreationError;
import kundenverwaltung.server.exception.EntityDeleteError;
import kundenverwaltung.server.util.JSONConverter;
import kundenverwaltung.server.util.ZipUtils;
import kundenverwaltung.service.WindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class WishRequestService extends BaseServiceImpl<WishRequestDTO>
{
    private final ScreenshotService SCREENSHOT_SERVICE = new ScreenshotService();
    private final Logger LOGGER = LoggerFactory.getLogger(WishRequestDTO.class);
    private final JSONConverter<WishRequestDTO> JSON_CONVERTER = new JSONConverter<WishRequestDTO>();
    private final String USER_HOME = System.getProperty("user.home");

    /**
     * Sends a wish request, including an optional screenshot, to the server.
     *
     * @param wishRequestDTO The wish request data transfer object.
     * @param file           The optional screenshot file to be uploaded.
     * @throws EntityCreationError if the server fails to create the wish request.
     */
    public void sendWishRequestToServer(WishRequestDTO wishRequestDTO, File file) throws EntityCreationError
    {
        ServerClient client = new ServerClient();
        String path = "/api/" + getAPIKeyWord() + "/create";

        UUID userAnonmyizedUUID = UserEntityService.getInstance().getUserGeneratedAnonymizedUUID();
        wishRequestDTO.setAnonymizedUserUUID(userAnonmyizedUUID);

        ScreenshotDTO screenshotDTO = null;

        if (file != null)
        {
            try
            {
                screenshotDTO = SCREENSHOT_SERVICE.uploadScreenshotToServer(file);
            }
            catch (Exception exception)
            {
                LOGGER.error("There was an error while uploading the screenshot-file for the wish-request! We will continue sending the wish-request anyway!");
            }
        }

        if (screenshotDTO != null)
        {
            wishRequestDTO.setScreenshot(screenshotDTO);
        }

        String jsonPayload = JSONConverter.toJson(wishRequestDTO);

        int httpStatus = client.sendRequest("POST", path, jsonPayload);

        if (httpStatus != 200)
        {
            LOGGER.error("httpStatus: {}", httpStatus);
            throw new EntityCreationError("httpStatus: " + httpStatus);
        }
    }

    /**
     * Retrieves all wish requests for a specific anonymized user UUID from the server.
     *
     * @param uuid The anonymized UUID of the user.
     * @return A list of WishRequestDTO objects, or an empty list if none are found or an error occurs.
     */
    public List<WishRequestDTO> getAllWishRequestsForGivenUserUUID(UUID uuid)
    {
        ServerClient client = new ServerClient();
        String path = "/api/" + getAPIKeyWord() + "/getAllByAnonymizedUserUUID/" + uuid.toString();
        int httpStatus = client.sendRequest("GET", path, null);

        if (httpStatus != 200)
        {
            return Collections.emptyList();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try
        {
            String jsonResponse = client.getResponse();

            if (jsonResponse == null || jsonResponse.trim().isEmpty())
            {
                return Collections.emptyList();
            }

            JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, WishRequestDTO.class);

            return objectMapper.readValue(jsonResponse, listType);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a wish request on the server, identified by the request's content and the user's anonymized UUID.
     *
     * @param wishRequestDTO The wish request to be deleted.
     * @throws EntityDeleteError if the server fails to delete the request.
     */
    public void deleteWishRequestForAnonymizedUser(WishRequestDTO wishRequestDTO) throws EntityDeleteError
    {
        UUID userAnonmyizedUUID = UserEntityService.getInstance().getUserGeneratedAnonymizedUUID();
        ServerClient client = new ServerClient();

        String path = "/api/" + getAPIKeyWord() + "/deleteByAnonymizedUserUUID/" + userAnonmyizedUUID.toString();
        String jsonPayload = JSONConverter.toJson(wishRequestDTO);

        int httpStatus = client.sendRequest("DELETE", path, jsonPayload);

        if (httpStatus != 200)
        {
            throw new EntityDeleteError("Server HTTP-Status: " + httpStatus);
        }
    }

    /**
     * Submits a wish request. It first tries to send it to the server directly.
     * If the server is unreachable, it saves the request locally as an offline wish.
     *
     * @param wishRequestDTO   The wish request data.
     * @param screenshotFile   An optional screenshot file.
     */
    public void sendWishRequestToServerViaSubmit(WishRequestDTO wishRequestDTO, File screenshotFile)
    {
        try
        {
            sendWishRequestToServer(wishRequestDTO, screenshotFile);

            Benachrichtigung.infoBenachrichtigung(
                    "Tafel-Server",
                    "Die Wunschanfrage wurde erfolgreich übermittelt."
            );
            return;
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
        }

        try
        {
            createOfflineWishes(wishRequestDTO, screenshotFile);

            boolean showAndWait = true;
            WindowService.openWindow("/kundenverwaltung/fxml/server/ServerConnectionError4Member.fxml", "Wunschanfrage Speicherung", new Stage(), StageStyle.DECORATED, Modality.APPLICATION_MODAL, showAndWait);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
        }
    }

    /**
     * Creates an encrypted ZIP file containing the wish request and an optional screenshot for offline storage.
     *
     * @param wishRequestDTO The wish request data.
     * @param file           The screenshot file.
     * @throws Exception if an error occurs during file creation or encryption.
     */
    public void createOfflineWishes(WishRequestDTO wishRequestDTO, File file) throws Exception
    {
        File wishRequestFile = JSON_CONVERTER.saveToJSON("wish_request", wishRequestDTO);
        ZipUtils.createEncryptedZip(getZipDirectory(), "wish_request", new File[] {wishRequestFile, file});

        try
        {
            wishRequestFile.delete();
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
        }
    }

    /**
     * Scans the offline wishes directory, and attempts to upload any found requests to the server.
     *
     * @return The number of successfully uploaded wishes.
     */
    public int uploadOfflineWishes()
    {
        File dir = new File(getZipDirectory());

        int uploadedWishes = 0;

        if (!dir.exists())
        {
            return uploadedWishes;
        }

        File[] zips = dir.listFiles((d, name) -> name.endsWith(".zip"));

        if (zips == null || zips.length == 0)
        {
            LOGGER.info("No local zipped error-reports found!");
            return uploadedWishes;
        }

        Arrays.sort(zips, Comparator.comparing(file ->
        {
            try
            {
                return Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime();
            }
            catch (Exception e)
            {
                return java.nio.file.attribute.FileTime.fromMillis(0);
            }
        }));

        for (File zip : zips)
        {
            LOGGER.info("Trying to unpack (wish-requests) zip: {}", zip.getName());

            try
            {
                File extractedDir = ZipUtils.extractEncryptedZip(zip);
                File[] files = extractedDir.listFiles((f, name) -> name.endsWith(".json"));

                if (files != null && files.length > 0)
                {
                    WishRequestDTO wishRequest = JSON_CONVERTER.loadFromJSON(files[0], WishRequestDTO.class);

                    File[] screenshotFiles = extractedDir.listFiles((f, name) -> (name.endsWith(".jpg") || name.endsWith(".png")));
                    File screenshotFile = null;

                    if (screenshotFiles != null && screenshotFiles.length == 1)
                    {
                        screenshotFile = screenshotFiles[0];
                    }

                    try
                    {
                        sendWishRequestToServer(wishRequest, screenshotFile);
                    }
                    catch (Exception exception)
                    {
                        LOGGER.error(exception.getMessage());

                        deleteExtractedFiles(zip, extractedDir);
                        return uploadedWishes;
                    }

                    deleteExtractedFiles(zip, extractedDir);

                    LOGGER.info("{} wish-request unzipped & successfully sent to server!", zip.getName());
                    uploadedWishes++;
                }

            }
            catch (Exception exception)
            {
                LOGGER.error(exception.getMessage());
            }
        }

        return uploadedWishes;
    }

    /**
     * Deletes the extracted files and the original zip file after processing.
     *
     * @param zip          The original zip file.
     * @param extractedDir The directory where files were extracted.
     */
    private void deleteExtractedFiles(File zip, File extractedDir)
    {
        for (File file : Objects.requireNonNull(extractedDir.listFiles()))
        {
            file.delete();
        }

        extractedDir.delete();
        zip.delete();
    }

    /**
     * Gets the directory path for storing offline wishes.
     *
     * @return The path to the offline wishes directory.
     */
    private String getZipDirectory()
    {
        return USER_HOME + File.separator + "Tafel Kundenverwaltung" + File.separator + "offline_wishes";
    }

    /**
     * Returns the API keyword for the wish request entity.
     *
     * @return The API keyword as a String.
     */
    @Override
    public String getAPIKeyWord()
    {
        return "wishes";
    }
}