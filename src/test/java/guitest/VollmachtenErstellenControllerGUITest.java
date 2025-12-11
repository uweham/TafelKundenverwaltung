package guitest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.DatePicker;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import kundenverwaltung.controller.VollmachtenController;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Vollmacht;
import kundenverwaltung.model.Familienmitglied;

import java.net.URL;
import java.time.LocalDate;


@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class VollmachtenErstellenControllerGUITest {

    private Stage stage;
    private VollmachtenController controller;

    @Mock
    private Haushalt mockHaushalt;

    @Mock
    private Vollmacht mockVollmacht;

    @Mock
    private Familienmitglied mockFamilienmitglied;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/VollmachtenErstellen.fxml");
        assertNotNull(fxmlUrl, "FXML-Datei nicht gefunden im Ressourcenordner 'kundenverwaltung/fxml'");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        controller = loader.getController();

        mockHaushalt = mock(Haushalt.class);
        mockVollmacht = mock(Vollmacht.class);
        mockFamilienmitglied = mock(Familienmitglied.class);

        when(mockHaushalt.getKundennummer()).thenReturn(12345);
        when(mockVollmacht.getAusgestelltAm()).thenReturn(LocalDate.now());
        when(mockVollmacht.getAblaufDatum()).thenReturn(LocalDate.now().plusYears(1));
        when(mockVollmacht.getBevollmaechtigtePerson()).thenReturn(mockFamilienmitglied);
        when(mockFamilienmitglied.getName()).thenReturn("Max Mustermann");

        controller.setHaushalt(mockHaushalt);

        controller.erstelleTabelleVollmachthinzufuegen();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup("#labelHeadingAdd").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelSearchPerson").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelPeriodOfValidity").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelSurname").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelDateOfExpiry").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelValidFrom").queryAs(Label.class));
        assertNotNull(robot.lookup("#labelOr").queryAs(Label.class));


        assertNotNull(robot.lookup("#txtVollmachtNachname").queryAs(TextField.class));
        assertNotNull(robot.lookup("#dateVollmachtGueltigAb").queryAs(DatePicker.class));
        assertNotNull(robot.lookup("#dateVollmachtGueltigBis").queryAs(DatePicker.class));
        assertNotNull(robot.lookup("#cbxVollmachtUnbegrenzt").queryAs(CheckBox.class));

        assertNotNull(robot.lookup("#tvVollmachtenPersonenSuche").queryAs(TableView.class));

        assertNotNull(robot.lookup("#btnSearchPerson").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnSavaChange").queryAs(Button.class));
        assertNotNull(robot.lookup("#btnHinzufuegenSchliessen").queryAs(Button.class));
    }

    @Test
    @Tag("gui")
    void testLabelsText(FxRobot robot) {
        Label heading = robot.lookup("#labelHeadingAdd").queryAs(Label.class);
        Label searchPerson = robot.lookup("#labelSearchPerson").queryAs(Label.class);
        Label period = robot.lookup("#labelPeriodOfValidity").queryAs(Label.class);
        Label surname = robot.lookup("#labelSurname").queryAs(Label.class);
        Label dateExpiry = robot.lookup("#labelDateOfExpiry").queryAs(Label.class);
        Label validFrom = robot.lookup("#labelValidFrom").queryAs(Label.class);
        Label or = robot.lookup("#labelOr").queryAs(Label.class);

        assertTrue(heading.getText().contains("Verwenden Sie die Personensuche um eine Person auszuwählen"));
        assertEquals("Personensuche", searchPerson.getText());
        assertEquals("Gültigkeitsdauer", period.getText());
        assertEquals("Nachname", surname.getText());
        assertEquals("Gültig bis:", dateExpiry.getText());
        assertEquals("Gültig ab:", validFrom.getText());
        assertEquals("oder", or.getText());
    }

    @Test
    @Tag("gui")
    void testButtonsText(FxRobot robot) {
        Button btnSearch = robot.lookup("#btnSearchPerson").queryAs(Button.class);
        Button btnSave = robot.lookup("#btnSavaChange").queryAs(Button.class);
        Button btnCancel = robot.lookup("#btnHinzufuegenSchliessen").queryAs(Button.class);

        assertEquals("Suchen", btnSearch.getText());
        assertEquals("OK", btnSave.getText());
        assertEquals("Abbrechen", btnCancel.getText());

        assertFalse(btnSearch.isDisabled());
        assertFalse(btnSave.isDisabled());
        assertFalse(btnCancel.isDisabled());
    }

    @Test
    @Tag("gui")
    void testInputFieldsInitialState(FxRobot robot) {
        TextField surnameField = robot.lookup("#txtVollmachtNachname").queryAs(TextField.class);
        DatePicker dateFrom = robot.lookup("#dateVollmachtGueltigAb").queryAs(DatePicker.class);
        DatePicker dateTo = robot.lookup("#dateVollmachtGueltigBis").queryAs(DatePicker.class);
        CheckBox unlimited = robot.lookup("#cbxVollmachtUnbegrenzt").queryAs(CheckBox.class);

        assertEquals("", surnameField.getText());
        assertNotNull(dateFrom.getValue());
        assertNotNull(dateTo.getValue());
        assertFalse(unlimited.isSelected());
        assertFalse(dateTo.isDisabled());
    }

    @Test
    @Tag("gui")
    void testTableColumnsExist(FxRobot robot) {
        TableView<?> table = robot.lookup("#tvVollmachtenPersonenSuche").queryAs(TableView.class);

        assertNotNull(table);
        assertEquals(4, table.getColumns().size());
    }

    @Test
    @Tag("gui")
    void testTableColumnHeaders(FxRobot robot) {
        TableView<?> table = robot.lookup("#tvVollmachtenPersonenSuche").queryAs(TableView.class);

        assertEquals("Name", table.getColumns().get(0).getText());
        assertEquals("Anschrift", table.getColumns().get(1).getText());
        assertEquals("Wohnort", table.getColumns().get(2).getText());
        assertEquals("Geburtsdatum", table.getColumns().get(3).getText());
    }

    @Test
    @Tag("gui")
    void testUnlimitedCheckboxBehavior(FxRobot robot) {
        CheckBox unlimited = robot.lookup("#cbxVollmachtUnbegrenzt").queryAs(CheckBox.class);
        DatePicker dateTo = robot.lookup("#dateVollmachtGueltigBis").queryAs(DatePicker.class);

        assertFalse(dateTo.isDisabled());

        robot.clickOn(unlimited);
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(unlimited.isSelected());
        assertTrue(dateTo.isDisabled());

        robot.clickOn(unlimited);
        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(unlimited.isSelected());
        assertFalse(dateTo.isDisabled());
    }

    @Test
    @Tag("gui")
    void testCancelButtonClosesWindow(FxRobot robot) {
        Button btnCancel = robot.lookup("#btnHinzufuegenSchliessen").queryAs(Button.class);

        assertNotNull(btnCancel);
        assertTrue(btnCancel.isVisible());

        boolean initiallyShowing = stage.isShowing();

        robot.clickOn(btnCancel);

        WaitForAsyncUtils.waitForFxEvents();

        assertFalse(stage.isShowing());
    }

    @Test
    @Tag("gui")
    void testSearchButtonAction(FxRobot robot) {
        Button btnSearch = robot.lookup("#btnSearchPerson").queryAs(Button.class);
        TextField surnameField = robot.lookup("#txtVollmachtNachname").queryAs(TextField.class);

        robot.clickOn(surnameField);
        robot.write("Mustermann");
        robot.clickOn(btnSearch);

        assertNotNull(btnSearch.getOnAction());
    }

    @Test
    @Tag("gui")
    void testTextFieldAcceptsInput(FxRobot robot) {
        TextField surnameField = robot.lookup("#txtVollmachtNachname").queryAs(TextField.class);

        robot.clickOn(surnameField);
        robot.write("Test Name");
        assertEquals("Test Name", surnameField.getText());

        robot.eraseText(surnameField.getText().length());
        assertEquals("", surnameField.getText());
    }

    @Test
    @Tag("gui")
    void testDatePickersFunctionality(FxRobot robot) {
        DatePicker dateFrom = robot.lookup("#dateVollmachtGueltigAb").queryAs(DatePicker.class);
        DatePicker dateTo = robot.lookup("#dateVollmachtGueltigBis").queryAs(DatePicker.class);

        assertEquals(LocalDate.now(), dateFrom.getValue());
        assertEquals(LocalDate.now(), dateTo.getValue());

        assertTrue(dateFrom.isEditable());
        assertTrue(dateTo.isEditable());
    }

    @Test
    @Tag("gui")
    void testUIElementsVisible(FxRobot robot) {
        assertTrue(robot.lookup("#labelHeadingAdd").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#labelSearchPerson").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#labelPeriodOfValidity").queryAs(Label.class).isVisible());
        assertTrue(robot.lookup("#txtVollmachtNachname").queryAs(TextField.class).isVisible());
        assertTrue(robot.lookup("#btnSearchPerson").queryAs(Button.class).isVisible());
        assertTrue(robot.lookup("#tvVollmachtenPersonenSuche").queryAs(TableView.class).isVisible());
        assertTrue(robot.lookup("#dateVollmachtGueltigAb").queryAs(DatePicker.class).isVisible());
        assertTrue(robot.lookup("#dateVollmachtGueltigBis").queryAs(DatePicker.class).isVisible());
        assertTrue(robot.lookup("#cbxVollmachtUnbegrenzt").queryAs(CheckBox.class).isVisible());
        assertTrue(robot.lookup("#btnSavaChange").queryAs(Button.class).isVisible());
        assertTrue(robot.lookup("#btnHinzufuegenSchliessen").queryAs(Button.class).isVisible());
    }

    @Test
    @Tag("gui")
    void testButtonActionsExist(FxRobot robot) {
        Button btnSearch = robot.lookup("#btnSearchPerson").queryAs(Button.class);
        Button btnSave = robot.lookup("#btnSavaChange").queryAs(Button.class);
        Button btnCancel = robot.lookup("#btnHinzufuegenSchliessen").queryAs(Button.class);

        assertNotNull(btnSearch.getOnAction());
        assertNotNull(btnSave.getOnAction());
        assertNotNull(btnCancel.getOnAction());
    }

    @Test
    @Tag("gui")
    void testEditModeInitialization(FxRobot robot) {
    }
}