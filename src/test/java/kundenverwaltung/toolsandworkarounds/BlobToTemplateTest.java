package kundenverwaltung.toolsandworkarounds;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlobToTemplateTest
{

    @Mock
    private Blob mockBlob;

    private BlobToTemplate blobToTemplate;
    private final List<File> tempFiles = new ArrayList<>();

    private java.io.PrintStream originalErr;

    @BeforeEach
    void setUp()
    {
        blobToTemplate = new BlobToTemplate();

        originalErr = System.err;
        System.setErr(new java.io.PrintStream(java.io.OutputStream.nullOutputStream()));
    }

    @AfterEach
    void tearDown()
    {
        for (File file : tempFiles)
        {
            if (file != null && file.exists())
            {
                file.delete();
            }
        }
        tempFiles.clear();

        System.setErr(originalErr);
    }

    @Test
    @Tag("unit")
    void convertBlobToTemplate_withValidContent_createsCorrectTempFile() throws SQLException, IOException
    {
        String htmlContent = "<html><body><h1>Test</h1><p>Das ist ein Test.</p></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        when(mockBlob.getBinaryStream()).thenReturn(inputStream);

        File resultFile = blobToTemplate.convertBlobToTemplate(mockBlob);
        if (resultFile != null)
        {
            tempFiles.add(resultFile);
        }

        assertNotNull(resultFile, "Die zurückgegebene Datei sollte nicht null sein.");
        assertTrue(resultFile.exists(), "Die temporäre Datei sollte existieren.");
        assertTrue(resultFile.getName().startsWith("tempTemplate"), "Der Dateiname sollte mit 'tempTemplate' beginnen.");
        assertTrue(resultFile.getName().endsWith(".html"), "Die Dateiendung sollte '.html' sein.");

        String fileContent = Files.readString(resultFile.toPath(), StandardCharsets.UTF_8);
        assertEquals(htmlContent, fileContent, "Der Inhalt der Datei sollte dem ursprünglichen String entsprechen.");
    }

    @Test
    @Tag("unit")
    void convertBlobToTemplate_withEmptyBlob_createsEmptyFile() throws SQLException, IOException
    {
        String emptyContent = "";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(emptyContent.getBytes(StandardCharsets.UTF_8));
        when(mockBlob.getBinaryStream()).thenReturn(inputStream);

        File resultFile = blobToTemplate.convertBlobToTemplate(mockBlob);
        if (resultFile != null)
        {
            tempFiles.add(resultFile);
        }

        assertNotNull(resultFile);
        assertTrue(resultFile.exists());
        assertEquals(0, resultFile.length(), "Die Datei sollte leer sein.");
    }

    @Test
    @Tag("unit")
    void convertBlobToTemplate_withMultiLineContent_concatenatesLines() throws SQLException, IOException
    {
        String multiLineContent = "Zeile 1\nZeile 2";
        String expectedFileContent = "Zeile 1Zeile 2";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(multiLineContent.getBytes(StandardCharsets.UTF_8));
        when(mockBlob.getBinaryStream()).thenReturn(inputStream);

        File resultFile = blobToTemplate.convertBlobToTemplate(mockBlob);
        if (resultFile != null)
        {
            tempFiles.add(resultFile);
        }

        assertNotNull(resultFile);
        String fileContent = Files.readString(resultFile.toPath(), StandardCharsets.UTF_8);
        assertEquals(expectedFileContent, fileContent, "Die Zeilen sollten ohne Umbruch aneinandergereiht werden.");
    }

    @Test
    @Tag("unit")
    void convertBlobToTemplate_whenBlobThrowsSqlException_throwsNullPointerException() throws SQLException
    {
        when(mockBlob.getBinaryStream()).thenThrow(new SQLException("Datenbankverbindung verloren"));

        assertThrows(NullPointerException.class,
                () -> blobToTemplate.convertBlobToTemplate(mockBlob),
                "Aufgrund des Bugs in der Implementierung sollte eine NullPointerException geworfen werden.");
    }

    @Test
    @Tag("unit")
    void getterAndSetter_forBlobTempFileOut_workCorrectly()
    {
        File testFile = new File("test.tmp");
        tempFiles.add(testFile);

        blobToTemplate.setBlobTempFileOut(testFile);
        File retrievedFile = blobToTemplate.getBlobTempFileOut();

        assertSame(testFile, retrievedFile, "Der Getter sollte exakt das gesetzte File-Objekt zurückgeben.");
    }
}
