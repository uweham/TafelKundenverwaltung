package kundenverwaltung.toolsandworkarounds;

import kundenverwaltung.Benachrichtigung;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileHandler.class);

    public static void openFile(byte[] data, String filePrefix, String extension)
    {
        try
        {
            File tempFile = File.createTempFile(filePrefix, "." + extension);
            tempFile.deleteOnExit();

            try (FileOutputStream fos = new FileOutputStream(tempFile))
            {
                fos.write(data);
            }

            boolean isImage = extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png");

            if (isImage)
            {
                Desktop.getDesktop().open(tempFile);
            }
            else
            {
                Desktop.getDesktop().open(tempFile.getParentFile());
            }
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage());
            Benachrichtigung.errorDialog("Datei öffnen", "Die Datei konnte nicht geöffnet werden!", e.getMessage());
        }
    }
}
