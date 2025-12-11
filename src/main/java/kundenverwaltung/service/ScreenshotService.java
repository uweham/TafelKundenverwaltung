package kundenverwaltung.service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ScreenshotService
{
    /**
     * Captures a screenshot of the entire screen and saves it to a temporary file.
     *
     * @return A File object pointing to the created screenshot image.
     * @throws RuntimeException if an AWT or I/O error occurs during the screenshot process.
     */
    public static File createScreenshot()
    {
        try
        {
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = new Robot().createScreenCapture(screenRect);
            File file = new File(UUID.randomUUID() + "_screenshot_capturing.jpg");
            ImageIO.write(capture, "jpg", file);
            return file;
        }
        catch (IOException | AWTException e)
        {
            throw new RuntimeException(e);
        }
    }
}
