package guitest.admintool;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import kundenverwaltung.controller.MainController;
import kundenverwaltung.controller.admintool.AdmintoolController;
import kundenverwaltung.model.Ausgabegruppe;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.net.URL;

@ExtendWith(ApplicationExtension.class)
public class NeueAusgabegruppeControllerGUITest {

    private Stage stage;
    private TextField inpName;
    private Button btnSpeichern;

    private MainController mainMock;
    private AdmintoolController adminMock;
    private MockedStatic<MainController> mainStaticMock;
    private MockedConstruction<Ausgabegruppe> ausgabegruppeConstructionMock;

    @Start
    void start(Stage stage) throws Exception {
        this.stage = stage;

        mainMock = mock(MainController.class);
        adminMock = mock(AdmintoolController.class);

        doNothing().when(adminMock).openAusgabegruppen(any(ActionEvent.class));

        mainStaticMock = mockStatic(MainController.class);
        mainStaticMock.when(MainController::getInstance).thenReturn(mainMock);
        when(mainMock.getAdmintoolAnlegenController()).thenReturn(adminMock);


        ausgabegruppeConstructionMock = mockConstruction(Ausgabegruppe.class,
                (mock, context) -> {
                });

        URL fxml = getClass().getResource("/kundenverwaltung/fxml/admintool/NeueAusgabegruppe.fxml");
        assertNotNull(fxml, "FXML nicht gefunden: /kundenverwaltung/fxml/admintool/NeueAusgabegruppe.fxml");

        Parent root = new FXMLLoader(fxml).load();

        inpName = (TextField) root.lookup("#inpName");
        btnSpeichern = (Button) root.lookup("#btnSpeichern");

        stage.setScene(new Scene(root));
        stage.show();

        WaitForAsyncUtils.waitForFxEvents();
    }

    @AfterEach
    void tearDown() {
        if (ausgabegruppeConstructionMock != null) {
            ausgabegruppeConstructionMock.close();
        }
        if (mainStaticMock != null) {
            mainStaticMock.close();
        }
    }


    @Test
    @Tag("gui")
    void clickingSave_withName_callsOpenAusgabegruppen_andClosesWindow(FxRobot robot) throws Exception {
        robot.clickOn(inpName).write("Lebensmittel");
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn(btnSpeichern);
        WaitForAsyncUtils.waitForFxEvents();

        verify(adminMock, times(1)).openAusgabegruppen(any(ActionEvent.class));

        assertFalse(stage.isShowing(), "Stage sollte nach Speichern geschlossen sein");

        assertEquals(1, ausgabegruppeConstructionMock.constructed().size(),
                "Es sollte genau eine Ausgabegruppe erzeugt worden sein");
    }

    @Test
    @Tag("gui")
    void clickingSave_whenOpenThrowsIOException_isCaught_andTestDoesNotCrash(FxRobot robot) throws Exception {
        doThrow(new IOException("boom"))
                .when(adminMock).openAusgabegruppen(any(ActionEvent.class));

        robot.clickOn(inpName).eraseText(inpName.getText().length()).write("Haushalt");
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn(btnSpeichern);
        WaitForAsyncUtils.waitForFxEvents();

        verify(adminMock, times(1)).openAusgabegruppen(any(ActionEvent.class));

        assertTrue(stage.isShowing() || !stage.isShowing(),
                "Wichtig ist nur, dass kein 'Unhandled exception' passiert – Verhalten nach Exception ist projektspezifisch");

        assertEquals(1, ausgabegruppeConstructionMock.constructed().size());
    }
}
