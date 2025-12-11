package guitest.admintool;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;

import java.lang.reflect.Field;
import java.util.ArrayList;

import kundenverwaltung.controller.admintool.VerteilstellenController;
import kundenverwaltung.model.Verteilstelle;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class VerteilstellenControllerGUITest
{

    private VerteilstellenController controller;

    @Start
    private void start(Stage stage) throws Exception
    {
        controller = new VerteilstellenController();

        ArrayList<Verteilstelle> testListe = new ArrayList<>();
        testListe.add(new Verteilstelle(1, "TestVerteilstelle", "Musterstraße 1", 1));

        ObservableList<Verteilstelle> obsList = FXCollections.observableArrayList(testListe);

        setPrivateField(controller, "verteilstellenliste", testListe);

        ComboBox<Verteilstelle> cbVerteil = new ComboBox<>(obsList);
        TableView<Verteilstelle> tabelle = new TableView<>(obsList);
        TableColumn<Verteilstelle, Integer> colReihenfolge = new TableColumn<>("Listennummer");
        TableColumn<Verteilstelle, String> colBezeichnung = new TableColumn<>("Bezeichnung");
        TableColumn<Verteilstelle, String> colAdresse = new TableColumn<>("Adresse");
        tabelle.getColumns().addAll(colReihenfolge, colBezeichnung, colAdresse);

        setPrivateField(controller, "cbVerteil", cbVerteil);
        setPrivateField(controller, "tabelleVerteilstellen", tabelle);
        setPrivateField(controller, "colReihenfolge", colReihenfolge);
        setPrivateField(controller, "colBezeichnung", colBezeichnung);
        setPrivateField(controller, "colAdresse", colAdresse);

        CheckBox chkCustomer = new CheckBox();
        chkCustomer.setSelected(true);
        setPrivateField(controller, "chkCustomer", chkCustomer);

        Button btnChange = new Button("Verteilstellenname ändern");
        Button btnNew = new Button("neue Verteilstelle erstellen");
        Button btnSave = new Button("speichern");
        setPrivateField(controller, "btnChange", btnChange);
        setPrivateField(controller, "btnNew", btnNew);
        setPrivateField(controller, "btnSave", btnSave);

        Parent root = new javafx.scene.layout.VBox(cbVerteil, tabelle, chkCustomer, btnChange, btnNew, btnSave);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void setPrivateField(Object obj, String fieldName, Object value) throws Exception
    {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    @Test
    void testComponentsExist(FxRobot robot)
    {
        ComboBox<?> cb = robot.lookup(".combo-box").queryAs(ComboBox.class);
        TableView<?> table = robot.lookup(".table-view").queryAs(TableView.class);
        CheckBox chk = robot.lookup(".check-box").queryAs(CheckBox.class);
        Button btnChange = robot.lookup("Verteilstellenname ändern").queryAs(Button.class);
        Button btnNew = robot.lookup("neue Verteilstelle erstellen").queryAs(Button.class);
        Button btnSave = robot.lookup("speichern").queryAs(Button.class);

        assertNotNull(cb);
        assertNotNull(table);
        assertNotNull(chk);
        assertNotNull(btnChange);
        assertNotNull(btnNew);
        assertNotNull(btnSave);
    }

    @Test
    void testCheckBoxToggle(FxRobot robot)
    {
        CheckBox chk = robot.lookup(".check-box").queryAs(CheckBox.class);
        boolean initial = chk.isSelected();
        robot.clickOn(chk);
        assertNotEquals(initial, chk.isSelected());
    }

    @Test
    void testButtonClick(FxRobot robot)
    {
        Button btnChange = robot.lookup("Verteilstellenname ändern").queryAs(Button.class);
        Button btnNew = robot.lookup("neue Verteilstelle erstellen").queryAs(Button.class);
        Button btnSave = robot.lookup("speichern").queryAs(Button.class);

        assertDoesNotThrow(() -> robot.clickOn(btnChange));
        assertDoesNotThrow(() -> robot.clickOn(btnNew));
        assertDoesNotThrow(() -> robot.clickOn(btnSave));
    }
}
