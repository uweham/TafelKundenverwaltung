package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.net.URL;

@ExtendWith(ApplicationExtension.class)
public class AusgabegruppeControllerGUITest
{

    @Start
    private void start(Stage stage) throws Exception
    {
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/admintool/Ausgabegruppen.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden im Ressourcenordner 'kundenverwaltung/fxml/admintool'");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot)
    {
        assertNotNull(robot.lookup("#chGruppe").queryAs(ChoiceBox.class));
        assertNotNull(robot.lookup("#btnUmbenennen").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnNeu").queryAs(Button.class));
        assertNotNull(robot.lookup("#chkAktiv").queryAs(CheckBox.class));
        assertNotNull(robot.lookup("#colPick").queryAs(ColorPicker.class));
        assertNotNull(robot.lookup("#tabelleAusgabetage").queryAs(TableView.class));
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot)
    {
        Button btnRename = robot.lookup("#btnUmbenennen").queryAs(Button.class);
        Button btnNew = robot.lookup("#btnNeu").queryAs(Button.class);

        assertEquals("gewählte Ausgabegruppe umbennenen", btnRename.getText());
        assertEquals("neue Ausgabegruppe erstellen", btnNew.getText());
    }

    @Test
    @Tag("gui")
    void testTableViewInitialized(FxRobot robot)
    {
        TableView<?> table = robot.lookup("#tabelleAusgabetage").queryAs(TableView.class);
        assertNotNull(table, "Tabelle sollte initialisiert sein");
        assertNotNull(table.getItems(), "TableView Items dürfen nicht null sein");
    }

    @Test
    @Tag("gui")
    void testColorPickerDefault(FxRobot robot)
    {
        ColorPicker cp = robot.lookup("#colPick").queryAs(ColorPicker.class);
        assertNotNull(cp.getValue(), "ColorPicker sollte einen Standardwert haben");
    }
}
