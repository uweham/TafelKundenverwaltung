package kundenverwaltung.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kundenverwaltung.server.api.ServerClient;
import kundenverwaltung.server.base.BaseServiceImpl;
import kundenverwaltung.server.dto.ErrorReportDTO;
import kundenverwaltung.server.dto.ScreenshotDTO;
import kundenverwaltung.server.exception.EntityCreationError;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ScreenshotService extends BaseServiceImpl<ScreenshotDTO>
{
    private final Logger LOGGER = LoggerFactory.getLogger(ScreenshotService.class);

    /**
     * Uploads a screenshot file to the server.
     *
     * @param screenshotFile The screenshot file to upload. Must not be null.
     * @return The ScreenshotDTO object as returned by the server, or null if the upload fails or the file type is unsupported.
     * @throws EntityCreationError if the server returns a non-200 status code.
     * @throws RuntimeException if a JSON parsing error occurs.
     */
    public ScreenshotDTO uploadScreenshotToServer(@NotNull File screenshotFile) throws EntityCreationError
    {
        //  Prepare screenshotDTO for server...
        String fileName = screenshotFile.getName();
        String extension = getFileExtension(fileName);

        ScreenshotDTO.FileType fileType;

        try
        {
            fileType = ScreenshotDTO.FileType.valueOf(extension.toUpperCase());
        }
        catch (IllegalArgumentException | NullPointerException e)
        {
            LOGGER.error("Unsupported or missing file type for file: {}", fileName);
            return null;
        }

        ScreenshotDTO screenshotDTO = new ScreenshotDTO();
        screenshotDTO.setFileType(fileType);

        //  Upload to server...
        ServerClient client = new ServerClient();
        String path = "/api/" + getAPIKeyWord() + "/upload";

        int httpStatus = client.sendMultipartRequest(path, screenshotDTO, screenshotFile);

        if (httpStatus != 200)
        {
            LOGGER.error("Screenshot could not be uploaded to the server! HttpStatus: {}", httpStatus);
            throw new EntityCreationError("httpStatus: " + httpStatus);
        }

        //  Handle server response...
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try
        {
            String jsonResponse = client.getResponse();

            if (jsonResponse == null || jsonResponse.trim().isEmpty())
            {
                return null;
            }

            return objectMapper.readValue(jsonResponse, ScreenshotDTO.class);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extracts the file extension from a file name.
     *
     * @param fileName The name of the file.
     * @return The file extension in lowercase, or null if no extension is found.
     */
    private String getFileExtension(String fileName)
    {
        if (fileName == null || !fileName.contains("."))
        {
            return null;
        }

        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    /**
     * Returns the API keyword for the screenshot entity.
     *
     * @return The API keyword as a String.
     */
    @Override
    public String getAPIKeyWord()
    {
        return "screenshots";
    }
}