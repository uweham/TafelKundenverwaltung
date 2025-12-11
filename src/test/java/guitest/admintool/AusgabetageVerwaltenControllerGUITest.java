package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class AusgabetageVerwaltenControllerGUITest
{

    private TableView<?> table;
    private Button btnRemove;
    private Button btnCancel;

    @Start
    private void start(Stage stage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/kundenverwaltung/fxml/admintool/AusgabetageVerwalten.fxml"
        ));
        Parent root = loader.load();

        table = (TableView<?>) root.lookup("#tabelleAusgabetage");
        btnRemove = (Button) root.lookup("#btnAusgabetagentfernen");
        btnCancel = (Button) root.lookup("#btnAbbrechen");

        if (table != null)
        {
            table.getItems().clear();
        }

        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot)
    {
        assertNotNull(table, "Tabelle 'tabelleAusgabetage' sollte vorhanden sein");
        assertNotNull(btnRemove, "Button 'btnAusgabetagentfernen' sollte vorhanden sein");
        assertNotNull(btnCancel, "Button 'btnAbbrechen' sollte vorhanden sein");
    }

    @Test
    @Tag("gui")
    void testTableEmptyOnStart(FxRobot robot)
    {
        assertEquals(0, table.getItems().size(), "Tabelle sollte anfangs leer sein");
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot)
    {
        assertEquals("Ausgabetag entfernen", btnRemove.getText(), "Beschriftung des Entfernen-Buttons stimmt nicht");
        assertEquals("Abbrechen", btnCancel.getText(), "Beschriftung des Abbrechen-Buttons stimmt nicht");
    }
}
