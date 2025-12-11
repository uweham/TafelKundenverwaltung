package kundenverwaltung.toolsandworkarounds;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomPropertiesStoreTest {

    @Test
    @Tag("unit")
    void store_shouldWritePropertiesWithoutEscapingSpecialCharacters() throws IOException {
        // Arrange
        CustomPropertiesStore props = new CustomPropertiesStore();
        props.setProperty("db:url", "jdbc:mysql://localhost:3306/database");
        props.setProperty("file/path", "C:/users/test");
        String comments = "Test Properties File";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        props.store(baos, comments);

        String output = baos.toString("8859_1");

        assertTrue(output.startsWith("#" + comments), "Output should start with the provided comments.");

        assertTrue(
                output.contains("db:url=jdbc:mysql://localhost:3306/database"),
                "Should contain the database URL without escaping colons or slashes."
        );
        assertTrue(
                output.contains("file/path=C:/users/test"),
                "Should contain the file path without escaping slashes."
        );
    }

    @Test
    @Tag("unit")
    void store_shouldHandleEmptyProperties() throws IOException {
        CustomPropertiesStore props = new CustomPropertiesStore();
        String comments = "Empty Properties";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        props.store(baos, comments);
        String output = baos.toString("8859_1");
        String[] lines = output.split("\\R");

        assertEquals(2, lines.length, "Should only contain comment lines when properties are empty.");
        assertTrue(lines[0].startsWith("#" + comments));
        assertTrue(lines[1].startsWith("#"));
    }


    @Test
    @Tag("unit")
    void loadProperties_shouldSucceedForValidFile(@TempDir Path tempDir) throws Exception {
        Path propertiesFile = tempDir.resolve("test-info.properties");
        List<String> lines = Arrays.asList(
                "app.name=Test App",
                "app.version=1.0.0",
                "db.url=jdbc:mysql://localhost/testdb"
        );
        Files.write(propertiesFile, lines, StandardCharsets.UTF_8);


        CustomPropertiesStore props = loadFromFile(propertiesFile);

        assertEquals("Test App", props.getProperty("app.name"));
        assertEquals("1.0.0", props.getProperty("app.version"));
        assertEquals("jdbc:mysql://localhost/testdb", props.getProperty("db.url"));
        assertNull(props.getProperty("non.existent.key"));
    }

    private CustomPropertiesStore loadFromFile(Path path) throws IOException {
        CustomPropertiesStore prop = new CustomPropertiesStore();
        try (var in = Files.newInputStream(path)) {
            prop.load(in);
        }
        return prop;
    }
}