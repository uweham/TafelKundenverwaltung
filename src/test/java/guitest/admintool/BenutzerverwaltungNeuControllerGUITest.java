package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.api.extension.ExtendWith;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class BenutzerverwaltungNeuControllerGUITest {

    @Start
    public void start(Stage stage) throws Exception {
        URL fxml = getClass().getResource(
                "/kundenverwaltung/fxml/admintool/BenutzerverwaltungNeu.fxml"
        );
        assertNotNull(fxml, "FXML nicht gefunden – überprüfe bitte den Pfad und Ressourcen-Ordner.");
        Parent root = FXMLLoader.load(fxml);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void shouldHaveAllFields(FxRobot robot) {
        assertNotNull(robot.lookup("#Benutzer1").queryAs(TextField.class));
        assertNotNull(robot.lookup("#Anzeige1").queryAs(TextField.class));
        assertNotNull(robot.lookup("#Passwort11").queryAs(PasswordField.class));
        assertNotNull(robot.lookup("#Passwort12").queryAs(PasswordField.class));
        assertNotNull(robot.lookup("#Save1").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void initialSaveButtonDisabled(FxRobot robot) {
        Button save = robot.lookup("#Save1").queryAs(Button.class);
        assertFalse(save.isDisabled(), "Speichern-Button sollte standardmäßig ENABLED sein, wenn keine Logik deaktiviert.");
    }


}
