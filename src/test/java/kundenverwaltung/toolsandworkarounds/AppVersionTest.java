package kundenverwaltung.toolsandworkarounds;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AppVersionTest
{

    /** Test with valid pom.xml */
    @Test
    void getPomVersion_WithValidPomFile_ShouldReturnVersion() throws IOException
    {
        String pomContent = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                         http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>test-project</artifactId>
                    <version>1.2.3</version>
                </project>
                """;

        File originalPom = new File("pom.xml");
        File backupPom = null;
        boolean hadOriginalPom = originalPom.exists();

        if (hadOriginalPom)
        {
            backupPom = new File("pom.xml.backup");
            originalPom.renameTo(backupPom);
        }

        try
        {
            try (FileWriter writer = new FileWriter("pom.xml"))
            {
                writer.write(pomContent);
            }

            String version = AppVersion.getPomVersion();

            assertEquals("1.2.3", version, "Should return the version from pom.xml");

        } finally
        {
            File testPom = new File("pom.xml");
            if (testPom.exists())
            {
                testPom.delete();
            }

            if (hadOriginalPom && backupPom != null)
            {
                backupPom.renameTo(originalPom);
            }
        }
    }

    /** Test with missing pom.xml */
    @Test
    void getPomVersion_WithMissingPomFile_ShouldReturnErrorMessage()
    {
        File originalPom = new File("pom.xml");
        File backupPom = null;
        boolean hadOriginalPom = originalPom.exists();

        if (hadOriginalPom)
        {
            backupPom = new File("pom.xml.temp.backup");
            originalPom.renameTo(backupPom);
        }

        try
        {
            String result = AppVersion.getPomVersion();

            assertEquals("Version nicht gefunden", result,
                    "Should return error message when pom.xml is missing");

        } finally
        {
            if (hadOriginalPom && backupPom != null)
            {
                backupPom.renameTo(originalPom);
            }
        }
    }

    /** Test with invalid pom.xml */
    @Test
    void getPomVersion_WithInvalidPomFile_ShouldReturnErrorMessage() throws IOException
    {
        String invalidPomContent = """
                <?xml version="1.0" encoding="UTF-8"?>
                <invalid>
                    <not-a-pom>true</not-a-pom>
                </invalid>
                """;

        File originalPom = new File("pom.xml");
        File backupPom = null;
        boolean hadOriginalPom = originalPom.exists();

        if (hadOriginalPom)
        {
            backupPom = new File("pom.xml.backup");
            originalPom.renameTo(backupPom);
        }

        try
        {
            try (FileWriter writer = new FileWriter("pom.xml"))
            {
                writer.write(invalidPomContent);
            }

            String result = AppVersion.getPomVersion();

            assertEquals("Version nicht gefunden", result,
                    "Should return error message for invalid pom.xml");

        } finally
        {
            File testPom = new File("pom.xml");
            if (testPom.exists())
            {
                testPom.delete();
            }

            if (hadOriginalPom && backupPom != null)
            {
                backupPom.renameTo(originalPom);
            }
        }
    }

    /** Test with pom.xml without version */
    @Test
    void getPomVersion_WithPomFileWithoutVersion_ShouldReturnErrorMessage() throws IOException
    {
        String pomWithoutVersion = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>test-project</artifactId>
                    <!-- No version element -->
                </project>
                """;

        File originalPom = new File("pom.xml");
        File backupPom = null;
        boolean hadOriginalPom = originalPom.exists();

        if (hadOriginalPom)
        {
            backupPom = new File("pom.xml.backup");
            originalPom.renameTo(backupPom);
        }

        try
        {
            try (FileWriter writer = new FileWriter("pom.xml"))
            {
                writer.write(pomWithoutVersion);
            }

            String result = AppVersion.getPomVersion();

            assertEquals("Version nicht gefunden", result,
                    "Should return error message when version element is missing");

        } finally
        {
            File testPom = new File("pom.xml");
            if (testPom.exists())
            {
                testPom.delete();
            }

            if (hadOriginalPom && backupPom != null)
            {
                backupPom.renameTo(originalPom);
            }
        }
    }

    /** Test with empty version */
    @Test
    void getPomVersion_WithEmptyVersion_ShouldReturnErrorMessage() throws IOException
    {
        String pomWithEmptyVersion = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>test-project</artifactId>
                    <version></version>
                </project>
                """;

        File originalPom = new File("pom.xml");
        File backupPom = null;
        boolean hadOriginalPom = originalPom.exists();

        if (hadOriginalPom)
        {
            backupPom = new File("pom.xml.backup");
            originalPom.renameTo(backupPom);
        }

        try
        {
            try (FileWriter writer = new FileWriter("pom.xml"))
            {
                writer.write(pomWithEmptyVersion);
            }

            String result = AppVersion.getPomVersion();

            assertEquals("Version nicht gefunden", result,
                    "Should return error message when version is empty");

        } finally
        {
            File testPom = new File("pom.xml");
            if (testPom.exists())
            {
                testPom.delete();
            }

            if (hadOriginalPom && backupPom != null)
            {
                backupPom.renameTo(originalPom);
            }
        }
    }

    /** Test method is static */
    @Test
    void getPomVersion_MethodShouldBeStatic() throws NoSuchMethodException
    {
        var method = AppVersion.class.getDeclaredMethod("getPomVersion");

        assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()),
                "getPomVersion method should be static");

        assertEquals(String.class, method.getReturnType(),
                "getPomVersion should return String");

        assertEquals(0, method.getParameterCount(),
                "getPomVersion should take no parameters");
    }

    /** Test class is public */
    @Test
    void appVersion_ClassShouldBePublic()
    {
        assertTrue(java.lang.reflect.Modifier.isPublic(AppVersion.class.getModifiers()),
                "AppVersion class should be public");

        assertEquals("kundenverwaltung.toolsandworkarounds", AppVersion.class.getPackageName(),
                "AppVersion should be in correct package");
    }
}