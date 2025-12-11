package kundenverwaltung.server.util;

import kundenverwaltung.server.service.UserEntityService;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ZipUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);

    /**
     * Creates a password-protected ZIP file using AES-256 encryption.
     * The password is dynamically generated based on the current user and date.
     *
     * @param zipDirectory The directory where the ZIP file will be saved.
     * @param fileName     The base name for the ZIP file.
     * @param files        An array of files to be added to the ZIP archive.
     * @throws Exception if the ZIP creation fails or if no files are provided.
     */
    public static void createEncryptedZip(String zipDirectory, String fileName, File[] files) throws Exception
    {
        if (files == null || files.length == 0)
        {
            throw new IllegalArgumentException("Files are missing or does not exist.");
        }

        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String userName = UserEntityService.getInstance().getUser().getUserName();
        String password = "Tafel" + userName + date;

        new File(zipDirectory).mkdirs();

        // UUID für den Dateinamen
        String uuid = UUID.randomUUID().toString();
        String zipName = fileName + date + "_" + uuid + ".zip";

        // ZIP-Datei erstellen und verschlüsseln
        File zipFilePath = new File(zipDirectory, zipName);
        ZipFile zipFile = new ZipFile(zipFilePath, password.toCharArray());

        ZipParameters parameters = new ZipParameters();
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(EncryptionMethod.AES);
        parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
        parameters.setCompressionLevel(CompressionLevel.MAXIMUM);

        for (File currentFile : files)
        {
            if (currentFile != null)
            {
                zipFile.addFile(currentFile, parameters);
            }
        }

        LOGGER.debug("ZIP saved & encrypted: {}", zipFilePath.getAbsolutePath());
    }

    /**
     * Extracts a password-protected ZIP file.
     * The password is dynamically reconstructed based on the user and the file's creation date.
     *
     * @param zipFile The encrypted ZIP file to extract.
     * @return The directory where the files were extracted.
     * @throws Exception if extraction fails (e.g., incorrect password, corrupt file).
     */
    public static File extractEncryptedZip(File zipFile) throws Exception
    {
        Path path = zipFile.toPath();
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

        LocalDate creationDate = attr.creationTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        String date = creationDate.format(DateTimeFormatter.ISO_DATE);

        String userName = UserEntityService.getInstance().getUser().getUserName();
        String password = "Tafel" + userName + date;

        String outputDir = zipFile.getParent() + "/unzipped_" + zipFile.getName().replace(".zip", "");
        ZipFile zf = new ZipFile(zipFile);

        if (zf.isEncrypted())
        {
            zf.setPassword(password.toCharArray());
        }

        zf.extractAll(outputDir);

        LOGGER.debug("Extracted to: {}", outputDir);
        return new File(outputDir);
    }
}