package kundenverwaltung.server.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.logger.file.LogReader;
import kundenverwaltung.logger.model.LogEntry;
import kundenverwaltung.server.api.ServerClient;
import kundenverwaltung.server.base.BaseServiceImpl;
import kundenverwaltung.server.dto.ErrorReportDTO;
import kundenverwaltung.server.dto.ErrorReportLogDTO;
import kundenverwaltung.server.dto.ScreenshotDTO;
import kundenverwaltung.server.exception.EntityCreationError;
import kundenverwaltung.server.exception.EntityDeleteError;
import kundenverwaltung.server.exception.EntityUpdateError;
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

public class ErrorReportService extends BaseServiceImpl<ErrorReportDTO>
{
    private final ScreenshotService SCREENSHOT_SERVICE = new ScreenshotService();
    private final Logger LOGGER = LoggerFactory.getLogger(ErrorReportService.class);
    private final JSONConverter<ErrorReportDTO> JSON_CONVERTER = new JSONConverter<ErrorReportDTO>();
    private final String USER_HOME = System.getProperty("user.home");

    /**
     * Sends an error report, including logs and an optional screenshot, to the server.
     *
     * @param errorReportDTO   The error report data transfer object.
     * @param screenshotFile   The screenshot file to be uploaded. Can be null.
     * @throws EntityCreationError if the server fails to create the error report.
     */
    public void sendErrorReportToServer(ErrorReportDTO errorReportDTO, File screenshotFile) throws EntityCreationError
    {
        ServerClient client = new ServerClient();
        String path = "/api/" + getAPIKeyWord() + "/create";

        UUID userAnonmyizedUUID = UserEntityService.getInstance().getUserGeneratedAnonymizedUUID();
        errorReportDTO.setAnonymizedUserUUID(userAnonmyizedUUID);

        try
        {
            addLogsToErrorReportDTO(errorReportDTO);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
        }

        ScreenshotDTO screenshotDTO = null;

        if (screenshotFile != null)
        {
            try
            {
                screenshotDTO = SCREENSHOT_SERVICE.uploadScreenshotToServer(screenshotFile);
            }
            catch (Exception exception)
            {
                LOGGER.error("There was an error while uploading the screenshot-file for the error-report! We will continue sending the error-report anyway!");
            }
        }

        if (screenshotDTO != null)
        {
            errorReportDTO.setScreenshot(screenshotDTO);
        }

        String jsonPayload = JSONConverter.toJson(errorReportDTO);

        int httpStatus = client.sendRequest("POST", path, jsonPayload);

        if (httpStatus != 200)
        {
            LOGGER.error("httpStatus: {}", httpStatus);
            throw new EntityCreationError("httpStatus: " + httpStatus);
        }
    }

    /**
     * Retrieves all error reports for a specific anonymized user UUID from the server.
     *
     * @param uuid The anonymized UUID of the user.
     * @return A list of ErrorReportDTO objects, or an empty list if none are found or an error occurs.
     */
    public List<ErrorReportDTO> getAllErrorReportsForGivenUserUUID(UUID uuid)
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

            JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, ErrorReportDTO.class);

            return objectMapper.readValue(jsonResponse, listType);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes an error report on the server, identified by the report's content and the user's anonymized UUID.
     *
     * @param errorReportDTO The error report to be deleted.
     * @throws EntityDeleteError if the server fails to delete the report.
     */
    public void deleteErrorReportForAnonymizedUser(ErrorReportDTO errorReportDTO) throws EntityDeleteError
    {
        UUID userAnonmyizedUUID = UserEntityService.getInstance().getUserGeneratedAnonymizedUUID();
        ServerClient client = new ServerClient();

        String path = "/api/" + getAPIKeyWord() + "/deleteByAnonymizedUserUUID/" + userAnonmyizedUUID.toString();
        String jsonPayload = JSONConverter.toJson(errorReportDTO);

        int httpStatus = client.sendRequest("DELETE", path, jsonPayload);

        if (httpStatus != 200)
        {
            throw new EntityDeleteError("Server HTTP-Status: " + httpStatus);
        }
    }

    /**
     * Submits an error report. It first tries to send it to the server directly.
     * If the server is unreachable, it saves the report locally as an offline report.
     *
     * @param errorReportDTO   The error report data.
     * @param screenshotFile   An optional screenshot file.
     */
    public void sendErrorReportToServerViaSubmit(ErrorReportDTO errorReportDTO, File screenshotFile)
    {
        try
        {
            sendErrorReportToServer(errorReportDTO, screenshotFile);

            Benachrichtigung.infoBenachrichtigung(
                    "Tafel-Server",
                    "Der Fehlerbericht wurde erfolgreich übermittelt."
            );
            return;
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
        }

        try
        {
            createOfflineErrorReport(errorReportDTO, screenshotFile);

            boolean showAndWait = true;
            WindowService.openWindow("/kundenverwaltung/fxml/server/ServerConnectionError4Member.fxml", "Fehlerbericht Speicherung", new Stage(), StageStyle.DECORATED, Modality.APPLICATION_MODAL, showAndWait);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
        }
    }

    /**
     * Creates an encrypted ZIP file containing the error report and an optional screenshot for offline storage.
     *
     * @param reportDTO      The error report data.
     * @param screenshotFile The screenshot file.
     * @throws Exception if an error occurs during file creation or encryption.
     */
    public void createOfflineErrorReport(ErrorReportDTO reportDTO, File screenshotFile) throws Exception
    {
        File errorReportFile = JSON_CONVERTER.saveToJSON("error_report", reportDTO);
        ZipUtils.createEncryptedZip(getZipDirectory(), "error_report", new File[] {errorReportFile, screenshotFile});

        try
        {
            errorReportFile.delete();

            if (screenshotFile.getName().endsWith("_screenshot_capturing.jpg"))
            {
                screenshotFile.delete();
            }
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
        }
    }

    /**
     * Scans the offline reports directory, and attempts to upload any found reports to the server.
     *
     * @return The number of successfully uploaded reports.
     */
    public int uploadOfflineReports()
    {
        File dir = new File(getZipDirectory());

        int uploadedReports = 0;

        if (!dir.exists())
        {
            return uploadedReports;
        }

        File[] zips = dir.listFiles((d, name) -> name.endsWith(".zip"));

        if (zips == null || zips.length == 0)
        {
            LOGGER.info("No local zipped error-reports found!");
            return uploadedReports;
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
            LOGGER.info("Trying to unpack (error-report) zip: {}", zip.getName());

            try
            {
                File extractedDir = ZipUtils.extractEncryptedZip(zip);
                File[] files = extractedDir.listFiles((f, name) -> name.endsWith(".json"));

                if (files != null && files.length > 0)
                {
                    ErrorReportDTO errorReport = JSON_CONVERTER.loadFromJSON(files[0], ErrorReportDTO.class);

                    File[] screenshotFiles = extractedDir.listFiles((f, name) -> (name.endsWith(".jpg") || name.endsWith(".png")));
                    File screenshotFile = null;

                    if (screenshotFiles != null && screenshotFiles.length == 1)
                    {
                        screenshotFile = screenshotFiles[0];
                    }

                    try
                    {
                        sendErrorReportToServer(errorReport, screenshotFile);
                    }
                    catch (Exception exception)
                    {
                        LOGGER.error(exception.getMessage());

                        deleteExtractedFiles(zip, extractedDir);
                        return uploadedReports;
                    }

                    deleteExtractedFiles(zip, extractedDir);

                    LOGGER.info("{} error-report unzipped & successfully sent to server!", zip.getName());
                    uploadedReports++;
                }
            }
            catch (Exception exception)
            {
                LOGGER.error(exception.getMessage());
            }
        }

        return uploadedReports;
    }

    /**
     * Adds the last 10 log entries from the latest log file to the error report DTO.
     *
     * @param errorReportDTO The error report DTO to which the logs will be added.
     * @throws IOException if an error occurs reading the log file.
     */
    private void addLogsToErrorReportDTO(ErrorReportDTO errorReportDTO) throws IOException
    {
        String latestLog = kundenverwaltung.logger.file.LogFileService.getAvailableLogFiles().stream().findFirst().orElse(null);

        if (latestLog == null)
        {
            return;
        }

        String path = kundenverwaltung.logger.file.LogFileService.getLogDirectoryPath() + File.separator + latestLog;
        List<LogEntry> logEntries = LogReader.readLogsFromFile(path);

        logEntries.sort(Comparator.comparing(LogEntry::getTimestamp).reversed());
        List<LogEntry> last10Entries = logEntries.stream().limit(10).toList();

        List<ErrorReportLogDTO> logDTOs = new ArrayList<>();

        for (LogEntry logEntry : last10Entries)
        {
            ErrorReportLogDTO currentLogDTO = new ErrorReportLogDTO();

            currentLogDTO.setMessage(logEntry.getMessage());
            currentLogDTO.setFullMessage(logEntry.getFullMessage());
            currentLogDTO.setSource(logEntry.getSource());
            currentLogDTO.setLevel(logEntry.getLevel());
            currentLogDTO.setTimestamp(logEntry.getTimestamp());

            logDTOs.add(currentLogDTO);
        }

        errorReportDTO.setLogs(logDTOs);
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
     * Gets the directory path for storing offline reports.
     *
     * @return The path to the offline reports directory.
     */
    private String getZipDirectory()
    {
        return USER_HOME + File.separator + "Tafel Kundenverwaltung" + File.separator + "offline_reports";
    }

    /**
     * Returns the API keyword for the error report entity.
     *
     * @return The API keyword as a String.
     */
    @Override
    public String getAPIKeyWord()
    {
        return "reports";
    }
}