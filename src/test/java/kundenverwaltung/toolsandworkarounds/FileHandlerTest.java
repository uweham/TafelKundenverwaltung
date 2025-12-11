package kundenverwaltung.toolsandworkarounds;

import kundenverwaltung.Benachrichtigung;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FileHandlerTest
{

    private byte[] testData;
    private Desktop mockDesktop;

    @BeforeEach
    void setUp()
    {
        testData = "Test file content for unit testing".getBytes();
        mockDesktop = mock(Desktop.class);
    }

    /** Test successful opening of an image file (jpg) */
    @Test
    void testOpenFile_ImageJpg_Success() throws IOException
    {
        try (MockedStatic<Desktop> mockedDesktop = Mockito.mockStatic(Desktop.class);
             MockedStatic<Benachrichtigung> mockedBenachrichtigung = Mockito.mockStatic(Benachrichtigung.class))
        {

            mockedDesktop.when(Desktop::getDesktop).thenReturn(mockDesktop);

            FileHandler.openFile(testData, "test", "jpg");

            verify(mockDesktop, times(1)).open(any(File.class));

            mockedBenachrichtigung.verifyNoInteractions();
        }
    }

    /** Test successful opening of an image file (jpeg) */
    @Test
    void testOpenFile_ImageJpeg_Success() throws IOException
    {
        try (MockedStatic<Desktop> mockedDesktop = Mockito.mockStatic(Desktop.class);
             MockedStatic<Benachrichtigung> mockedBenachrichtigung = Mockito.mockStatic(Benachrichtigung.class))
        {

            mockedDesktop.when(Desktop::getDesktop).thenReturn(mockDesktop);

            FileHandler.openFile(testData, "test", "jpeg");

            verify(mockDesktop, times(1)).open(any(File.class));
            mockedBenachrichtigung.verifyNoInteractions();
        }
    }

    /** Test successful opening of an image file (png) */
    @Test
    void testOpenFile_ImagePng_Success() throws IOException
    {
        try (MockedStatic<Desktop> mockedDesktop = Mockito.mockStatic(Desktop.class);
             MockedStatic<Benachrichtigung> mockedBenachrichtigung = Mockito.mockStatic(Benachrichtigung.class))
        {

            mockedDesktop.when(Desktop::getDesktop).thenReturn(mockDesktop);

            FileHandler.openFile(testData, "test", "png");

            verify(mockDesktop, times(1)).open(any(File.class));
            mockedBenachrichtigung.verifyNoInteractions();
        }
    }

    /** Test successful opening of a non-image file (should open parent directory) */
    @Test
    void testOpenFile_NonImage_Success() throws IOException
    {
        try (MockedStatic<Desktop> mockedDesktop = Mockito.mockStatic(Desktop.class);
             MockedStatic<Benachrichtigung> mockedBenachrichtigung = Mockito.mockStatic(Benachrichtigung.class))
        {

            mockedDesktop.when(Desktop::getDesktop).thenReturn(mockDesktop);

            FileHandler.openFile(testData, "test", "pdf");

            verify(mockDesktop, times(1)).open(any(File.class));
            mockedBenachrichtigung.verifyNoInteractions();
        }
    }

    /** Test file creation and content verification */
    @Test
    void testOpenFile_FileCreationAndContent() throws IOException
    {
        try (MockedStatic<Desktop> mockedDesktop = Mockito.mockStatic(Desktop.class);
             MockedStatic<Benachrichtigung> mockedBenachrichtigung = Mockito.mockStatic(Benachrichtigung.class))
        {

            mockedDesktop.when(Desktop::getDesktop).thenReturn(mockDesktop);
            String testContent = "Hello, World! This is test content.";
            byte[] contentData = testContent.getBytes();

            doAnswer(invocation -> {
                File fileOrDirectory = invocation.getArgument(0);
                assertTrue(fileOrDirectory.exists(), "File or directory should exist when Desktop.open() is called");

                if (fileOrDirectory.isDirectory())
                {
                    File[] tempFiles = fileOrDirectory.listFiles((dir, name) ->
                            name.contains("test") && name.endsWith(".txt"));

                    assertTrue(tempFiles != null && tempFiles.length > 0,
                            "Should find temp file with 'test' prefix in directory");

                    File tempFile = tempFiles[0];
                    String actualContent = Files.readString(tempFile.toPath());
                    assertEquals(testContent, actualContent, "File content should match input string");
                } else
                {
                    assertTrue(fileOrDirectory.getName().contains("test"),
                            "File name should contain the specified prefix");
                    assertTrue(fileOrDirectory.getName().endsWith(".txt"),
                            "File should end with specified extension");

                    String actualContent = Files.readString(fileOrDirectory.toPath());
                    assertEquals(testContent, actualContent, "File content should match input string");
                }

                return null;
            }).when(mockDesktop).open(any(File.class));

            FileHandler.openFile(contentData, "test", "txt");

            verify(mockDesktop, times(1)).open(any(File.class));
            mockedBenachrichtigung.verifyNoInteractions();
        }
    }

    /** Test IOException handling during file creation */
    @Test
    void testOpenFile_IOExceptionDuringFileCreation()
    {
        try (MockedStatic<File> mockedFile = Mockito.mockStatic(File.class);
             MockedStatic<Benachrichtigung> mockedBenachrichtigung = Mockito.mockStatic(Benachrichtigung.class))
        {

            IOException testException = new IOException("Cannot create temp file");
            mockedFile.when(() -> File.createTempFile(anyString(), anyString()))
                    .thenThrow(testException);

            FileHandler.openFile(testData, "test", "txt");

            mockedBenachrichtigung.verify(() ->
                    Benachrichtigung.errorDialog(eq("Datei öffnen"),
                            eq("Die Datei konnte nicht geöffnet werden!"),
                            eq("Cannot create temp file")));
        }
    }

    /** Test IOException handling during Desktop.open() */
    @Test
    void testOpenFile_IOExceptionDuringDesktopOpen() throws IOException
    {
        try (MockedStatic<Desktop> mockedDesktop = Mockito.mockStatic(Desktop.class);
             MockedStatic<Benachrichtigung> mockedBenachrichtigung = Mockito.mockStatic(Benachrichtigung.class))
        {

            IOException testException = new IOException("Cannot open file with default application");
            mockedDesktop.when(Desktop::getDesktop).thenReturn(mockDesktop);
            doThrow(testException).when(mockDesktop).open(any(File.class));

            FileHandler.openFile(testData, "test", "txt");

            mockedBenachrichtigung.verify(() ->
                    Benachrichtigung.errorDialog(eq("Datei öffnen"),
                            eq("Die Datei konnte nicht geöffnet werden!"),
                            eq("Cannot open file with default application")));
        }
    }

    /** Test with empty data */
    @Test
    void testOpenFile_EmptyData() throws IOException
    {
        try (MockedStatic<Desktop> mockedDesktop = Mockito.mockStatic(Desktop.class);
             MockedStatic<Benachrichtigung> mockedBenachrichtigung = Mockito.mockStatic(Benachrichtigung.class))
        {

            mockedDesktop.when(Desktop::getDesktop).thenReturn(mockDesktop);
            byte[] emptyData = new byte[0];

            FileHandler.openFile(emptyData, "empty", "txt");

            verify(mockDesktop, times(1)).open(any(File.class));
            mockedBenachrichtigung.verifyNoInteractions();
        }
    }

    /** Test with null data (edge case) */
    @Test
    void testOpenFile_NullData()
    {
        try (MockedStatic<Benachrichtigung> mockedBenachrichtigung = Mockito.mockStatic(Benachrichtigung.class))
        {

            assertThrows(NullPointerException.class, () -> {
                FileHandler.openFile(null, "test", "txt");
            }, "Should throw NPE for null data");
        }
    }

    /** Test with various file extensions to verify image detection logic */
    @Test
    void testOpenFile_VariousExtensions() throws IOException
    {
        try (MockedStatic<Desktop> mockedDesktop = Mockito.mockStatic(Desktop.class);
             MockedStatic<Benachrichtigung> mockedBenachrichtigung = Mockito.mockStatic(Benachrichtigung.class))
        {

            mockedDesktop.when(Desktop::getDesktop).thenReturn(mockDesktop);

            String[] imageExtensions = {"jpg", "jpeg", "png"};
            for (String ext : imageExtensions)
            {
                FileHandler.openFile(testData, "test", ext);
            }

            String[] nonImageExtensions = {"pdf", "txt", "doc", "docx", "gif", "bmp"};
            for (String ext : nonImageExtensions)
            {
                FileHandler.openFile(testData, "test", ext);
            }

            int totalCalls = imageExtensions.length + nonImageExtensions.length;
            verify(mockDesktop, times(totalCalls)).open(any(File.class));

            mockedBenachrichtigung.verifyNoInteractions();
        }
    }

    /** Test case sensitivity of image extension detection */
    @Test
    void testOpenFile_ExtensionCaseSensitivity() throws IOException
    {
        try (MockedStatic<Desktop> mockedDesktop = Mockito.mockStatic(Desktop.class);
             MockedStatic<Benachrichtigung> mockedBenachrichtigung = Mockito.mockStatic(Benachrichtigung.class))
        {

            mockedDesktop.when(Desktop::getDesktop).thenReturn(mockDesktop);

            String[] uppercaseExtensions = {"JPG", "JPEG", "PNG"};
            for (String ext : uppercaseExtensions)
            {
                FileHandler.openFile(testData, "test", ext);
            }

            verify(mockDesktop, times(uppercaseExtensions.length)).open(any(File.class));
            mockedBenachrichtigung.verifyNoInteractions();
        }
    }

    /** Test with special characters in file prefix */
    @Test
    void testOpenFile_SpecialCharactersInPrefix() throws IOException
    {
        try (MockedStatic<Desktop> mockedDesktop = Mockito.mockStatic(Desktop.class);
             MockedStatic<Benachrichtigung> mockedBenachrichtigung = Mockito.mockStatic(Benachrichtigung.class))
        {

            mockedDesktop.when(Desktop::getDesktop).thenReturn(mockDesktop);

            FileHandler.openFile(testData, "test_file-123", "txt");

            verify(mockDesktop, times(1)).open(any(File.class));
            mockedBenachrichtigung.verifyNoInteractions();
        }
    }

    /** Test that verifies image files open directly while non-image files open parent directory */
    @Test
    void testOpenFile_ImageVsNonImageBehavior() throws IOException
    {
        try (MockedStatic<Desktop> mockedDesktop = Mockito.mockStatic(Desktop.class);
             MockedStatic<Benachrichtigung> mockedBenachrichtigung = Mockito.mockStatic(Benachrichtigung.class))
        {

            mockedDesktop.when(Desktop::getDesktop).thenReturn(mockDesktop);

            FileHandler.openFile(testData, "image", "jpg");

            FileHandler.openFile(testData, "document", "pdf");

            verify(mockDesktop, times(2)).open(any(File.class));
            mockedBenachrichtigung.verifyNoInteractions();
        }
    }
}