package guitest;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import kundenverwaltung.controller.StatistikAnsehenController;

import java.io.*;
import java.net.URL;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class StatistikAnsehenControllerGUITest {

    private Stage stage;
    private StatistikAnsehenController controller;
    private File testCsvFile;

    @BeforeEach
    void setUp() throws IOException {
        testCsvFile = new File("src/test/resources/altersstatistik.csv");
        testCsvFile.getParentFile().mkdirs(); // Erstelle Verzeichnis falls nicht vorhanden

        try (PrintWriter writer = new PrintWriter(new FileWriter(testCsvFile))) {
            writer.println("Altersgruppe,Anzahl");
            writer.println("0-18,25");
            writer.println("19-35,42");
            writer.println("36-60,38");
            writer.println("60+,22");
        }

        System.setProperty("csv.file.path", testCsvFile.getAbsolutePath());
    }

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/StatistikAnsehen.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        controller = loader.getController();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#csvContentArea").queryAs(TextArea.class));
        assertNotNull(robot.lookup("#buttonPrint").queryAs(Button.class));
        assertNotNull(robot.lookup("#buttonDownload").queryAs(Button.class));
        assertNotNull(robot.lookup("#buttonClose").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot) {
        Button btnPrint = robot.lookup("#buttonPrint").queryAs(Button.class);
        Button btnDownload = robot.lookup("#buttonDownload").queryAs(Button.class);
        Button btnClose = robot.lookup("#buttonClose").queryAs(Button.class);

        assertEquals("Drucken", btnPrint.getText());
        assertEquals("Als PDF herunterladen", btnDownload.getText());
        assertEquals("Schließen", btnClose.getText());

        assertFalse(btnPrint.isDisabled());
        assertFalse(btnDownload.isDisabled());
        assertFalse(btnClose.isDisabled());
    }


    @Test
    @Tag("gui")
    void testCloseButtonClosesWindow(FxRobot robot) {
        Button btnClose = robot.lookup("#buttonClose").queryAs(Button.class);

        assertNotNull(btnClose);
        assertTrue(btnClose.isVisible());

        robot.clickOn(btnClose);

        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(stage.isShowing());
    }

    @Test
    @Tag("gui")
    void testUIElementsVisible(FxRobot robot) {
        TextArea textArea = robot.lookup("#csvContentArea").queryAs(TextArea.class);
        Button btnPrint = robot.lookup("#buttonPrint").queryAs(Button.class);
        Button btnDownload = robot.lookup("#buttonDownload").queryAs(Button.class);
        Button btnClose = robot.lookup("#buttonClose").queryAs(Button.class);

        assertTrue(textArea.isVisible());
        assertTrue(btnPrint.isVisible());
        assertTrue(btnDownload.isVisible());
        assertTrue(btnClose.isVisible());
    }

    @AfterEach
    void tearDown() {
        // Lösche die Test-CSV-Datei
        if (testCsvFile != null && testCsvFile.exists()) {
            testCsvFile.delete();
        }
    }
}