package kundenverwaltung.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class ScreenshotServiceTest
{

    private File createdScreenshotFile;

    @BeforeEach
    void setUp()
    {
        createdScreenshotFile = null;
    }

    @AfterEach
    void tearDown()
    {
        if (createdScreenshotFile != null && createdScreenshotFile.exists())
        {
            try
            {
                Files.delete(createdScreenshotFile.toPath());
            } catch (IOException e)
            {
                System.err.println("Failed to delete temporary screenshot file: " + e.getMessage());
            }
        }
    }

    @Test
    void createScreenshot()
    {
        assertDoesNotThrow(() ->
        {
            createdScreenshotFile = ScreenshotService.createScreenshot();
        }, "createScreenshot should not throw an exception on success");

        assertNotNull(createdScreenshotFile, "Returned file should not be null");
        assertTrue(createdScreenshotFile.exists(), "Screenshot file should exist");
        assertTrue(createdScreenshotFile.length() > 0, "Screenshot file should not be empty");
        assertTrue(createdScreenshotFile.getName().endsWith("_screenshot_capturing.jpg"), "Screenshot file name should contain expected suffix");
    }
}