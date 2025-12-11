package guitest.admintool;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import kundenverwaltung.dao.SQLConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class UserAddChangeControllerGUITest {

    private static final String ID_HEADER_LABEL = "#labelHeader";
    private static final String ID_USERNAME_FIELD = "#textFieldUserName";
    private static final String ID_FIRSTNAME_FIELD = "#textFieldFirstName";
    private static final String ID_SURNAME_FIELD = "#textFieldSurname";
    private static final String ID_PASSWORD_FIELD = "#passwordFieldPassword";
    private static final String ID_REPEAT_PASSWORD_FIELD = "#passwordFieldRepeatPassword";
    private static final String ID_BIRTHDAY_PICKER = "#datePickerBirthday";
    private static final String ID_USER_RIGHTS_COMBO = "#comboboxUserRights";
    private static final String ID_DEFAULT_PASSWORD_CHECKBOX = "#checkboxDefaultPassword";

    private static final String SAVE_BUTTON_TEXT = "Speichern"; // Anpassen an den tatsächlichen Text
    private static final String CANCEL_BUTTON_TEXT = "Abbrechen"; // Anpassen an den tatsächlichen Text

    private static MockedStatic<SQLConnection> mockedSqlConnection;
    private static Connection mockConn;
    private static Statement mockStmt;
    private static PreparedStatement mockPstmt;
    private static ResultSet mockRs;

    @BeforeAll
    static void setUpDbMocks() throws Exception {
        mockConn = mock(Connection.class);
        mockStmt = mock(Statement.class);
        mockPstmt = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);
        java.sql.ResultSetMetaData mockMetaData = mock(java.sql.ResultSetMetaData.class);

        mockedSqlConnection = mockStatic(SQLConnection.class);
        mockedSqlConnection.when(SQLConnection::getCon).thenReturn(mockConn);

        when(mockConn.createStatement()).thenReturn(mockStmt);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPstmt);
        when(mockStmt.executeQuery(anyString())).thenReturn(mockRs);
        when(mockPstmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.getMetaData()).thenReturn(mockMetaData);
        when(mockMetaData.getColumnCount()).thenReturn(2);
        when(mockMetaData.getColumnName(1)).thenReturn("column1");
        when(mockMetaData.getColumnName(2)).thenReturn("column2");
        when(mockMetaData.getColumnType(1)).thenReturn(Types.VARCHAR);
        when(mockMetaData.getColumnType(2)).thenReturn(Types.NUMERIC);

        when(mockRs.next()).thenReturn(false);
    }

    @AfterAll
    static void tearDownDbMocks() {
        if (mockedSqlConnection != null) {
            mockedSqlConnection.close();
        }
    }

    @Start
    private void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource("/kundenverwaltung/fxml/admintool/UserAddChange.fxml");
        assertNotNull(fxmlUrl, "FXML 'UserAddChange.fxml' nicht gefunden. Ressourcenpfad prüfen!");

        Parent root = FXMLLoader.load(fxmlUrl);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
        stage.toFront();
    }

    @Test
    @Tag("gui")
    void testInitialNodesExist(FxRobot robot) {
        assertNotNull(robot.lookup(ID_HEADER_LABEL).queryAs(Text.class), "Header Label fehlt");
        assertNotNull(robot.lookup(ID_USERNAME_FIELD).queryAs(TextField.class), "Username Field fehlt");
        assertNotNull(robot.lookup(ID_FIRSTNAME_FIELD).queryAs(TextField.class), "Firstname Field fehlt");
        assertNotNull(robot.lookup(ID_SURNAME_FIELD).queryAs(TextField.class), "Surname Field fehlt");
        assertNotNull(robot.lookup(ID_PASSWORD_FIELD).queryAs(PasswordField.class), "Password Field fehlt");
        assertNotNull(robot.lookup(ID_REPEAT_PASSWORD_FIELD).queryAs(PasswordField.class), "Repeat Password Field fehlt");
        assertNotNull(robot.lookup(ID_BIRTHDAY_PICKER).queryAs(DatePicker.class), "Birthday Picker fehlt");
        assertNotNull(robot.lookup(ID_USER_RIGHTS_COMBO).queryAs(ComboBox.class), "User Rights ComboBox fehlt");
        assertNotNull(robot.lookup(ID_DEFAULT_PASSWORD_CHECKBOX).queryAs(CheckBox.class), "Default Password Checkbox fehlt");

        assertNotNull(robot.lookup(SAVE_BUTTON_TEXT).queryAs(Button.class), "Save Button fehlt");
        assertNotNull(robot.lookup(CANCEL_BUTTON_TEXT).queryAs(Button.class), "Cancel Button fehlt");
    }

    @Test
    @Tag("gui")
    void testInitialState(FxRobot robot) {
        TextField usernameField = robot.lookup(ID_USERNAME_FIELD).queryAs(TextField.class);
        TextField firstnameField = robot.lookup(ID_FIRSTNAME_FIELD).queryAs(TextField.class);
        TextField surnameField = robot.lookup(ID_SURNAME_FIELD).queryAs(TextField.class);
        PasswordField passwordField = robot.lookup(ID_PASSWORD_FIELD).queryAs(PasswordField.class);
        PasswordField repeatPasswordField = robot.lookup(ID_REPEAT_PASSWORD_FIELD).queryAs(PasswordField.class);
        DatePicker birthdayPicker = robot.lookup(ID_BIRTHDAY_PICKER).queryAs(DatePicker.class);
        @SuppressWarnings("unchecked")
        ComboBox<String> userRightsCombo = robot.lookup(ID_USER_RIGHTS_COMBO).queryAs(ComboBox.class);
        CheckBox defaultPasswordCheckbox = robot.lookup(ID_DEFAULT_PASSWORD_CHECKBOX).queryAs(CheckBox.class);

        assertTrue(usernameField.getText().isEmpty(), "Username field sollte leer starten");
        assertTrue(firstnameField.getText().isEmpty(), "Firstname field sollte leer starten");
        assertTrue(surnameField.getText().isEmpty(), "Surname field sollte leer starten");
        assertTrue(passwordField.getText().isEmpty(), "Password field sollte leer starten");
        assertTrue(repeatPasswordField.getText().isEmpty(), "Repeat password field sollte leer starten");
        assertNull(birthdayPicker.getValue(), "Birthday picker sollte keinen Wert haben");
        assertFalse(defaultPasswordCheckbox.isSelected(), "Default password checkbox sollte nicht ausgewählt sein");
        assertFalse(passwordField.isDisabled(), "Password field sollte nicht disabled sein");
        assertFalse(repeatPasswordField.isDisabled(), "Repeat password field sollte nicht disabled sein");

        assertNotNull(userRightsCombo.getItems(), "User rights combo sollte Items haben");
        if (!userRightsCombo.getItems().isEmpty()) {
            assertEquals(userRightsCombo.getItems().get(0), userRightsCombo.getValue(),
                    "Erste Option sollte ausgewählt sein");
        }
    }

    @Test
    @Tag("gui")
    void testDefaultPasswordCheckboxBehavior(FxRobot robot) {
        CheckBox defaultPasswordCheckbox = robot.lookup(ID_DEFAULT_PASSWORD_CHECKBOX).queryAs(CheckBox.class);
        PasswordField passwordField = robot.lookup(ID_PASSWORD_FIELD).queryAs(PasswordField.class);
        PasswordField repeatPasswordField = robot.lookup(ID_REPEAT_PASSWORD_FIELD).queryAs(PasswordField.class);

        robot.clickOn(defaultPasswordCheckbox);

        assertTrue(passwordField.isDisabled(), "Password field sollte disabled sein wenn Checkbox ausgewählt");
        assertTrue(repeatPasswordField.isDisabled(), "Repeat password field sollte disabled sein wenn Checkbox ausgewählt");
        assertTrue(passwordField.getText().isEmpty(), "Password field sollte leer sein");
        assertTrue(repeatPasswordField.getText().isEmpty(), "Repeat password field sollte leer sein");

        robot.clickOn(defaultPasswordCheckbox);

        assertFalse(passwordField.isDisabled(), "Password field sollte nicht disabled sein");
        assertFalse(repeatPasswordField.isDisabled(), "Repeat password field sollte nicht disabled sein");
    }

    @Test
    @Tag("gui")
    void testSaveButtonWithEmptyFields(FxRobot robot) {
        Button saveButton = robot.lookup(SAVE_BUTTON_TEXT).queryAs(Button.class);

        assertDoesNotThrow(() -> robot.clickOn(saveButton));
    }

    @Test
    @Tag("gui")
    void testCancelButton(FxRobot robot) {
        Button cancelButton = robot.lookup(CANCEL_BUTTON_TEXT).queryAs(Button.class);

        assertDoesNotThrow(() -> robot.clickOn(cancelButton));
    }

    @Test
    @Tag("gui")
    void testFormFilling(FxRobot robot) {
        TextField usernameField = robot.lookup(ID_USERNAME_FIELD).queryAs(TextField.class);
        TextField firstnameField = robot.lookup(ID_FIRSTNAME_FIELD).queryAs(TextField.class);
        TextField surnameField = robot.lookup(ID_SURNAME_FIELD).queryAs(TextField.class);
        PasswordField passwordField = robot.lookup(ID_PASSWORD_FIELD).queryAs(PasswordField.class);
        PasswordField repeatPasswordField = robot.lookup(ID_REPEAT_PASSWORD_FIELD).queryAs(PasswordField.class);
        DatePicker birthdayPicker = robot.lookup(ID_BIRTHDAY_PICKER).queryAs(DatePicker.class);

        robot.interact(() -> {
            usernameField.setText("testuser");
            firstnameField.setText("Test");
            surnameField.setText("User");
            passwordField.setText("password123");
            repeatPasswordField.setText("password123");
            birthdayPicker.setValue(LocalDate.of(1990, 1, 1));
        });

        assertEquals("testuser", usernameField.getText());
        assertEquals("Test", firstnameField.getText());
        assertEquals("User", surnameField.getText());
        assertEquals("password123", passwordField.getText());
        assertEquals("password123", repeatPasswordField.getText());
        assertEquals(LocalDate.of(1990, 1, 1), birthdayPicker.getValue());
    }

    @Test
    @Tag("gui")
    void testUserRightsComboBox(FxRobot robot) {
        @SuppressWarnings("unchecked")
        ComboBox<String> userRightsCombo = robot.lookup(ID_USER_RIGHTS_COMBO).queryAs(ComboBox.class);

        assertNotNull(userRightsCombo.getItems(), "User rights combo sollte Items haben");

        if (!userRightsCombo.getItems().isEmpty()) {
            String firstOption = userRightsCombo.getItems().get(0);
            robot.interact(() -> userRightsCombo.getSelectionModel().select(firstOption));
            assertEquals(firstOption, userRightsCombo.getValue());
        }
    }

    @Test
    @Tag("gui")
    void testDatePicker(FxRobot robot) {
        DatePicker birthdayPicker = robot.lookup(ID_BIRTHDAY_PICKER).queryAs(DatePicker.class);

        LocalDate testDate = LocalDate.of(1985, 5, 15);
        robot.interact(() -> birthdayPicker.setValue(testDate));
        assertEquals(testDate, birthdayPicker.getValue());
    }
}