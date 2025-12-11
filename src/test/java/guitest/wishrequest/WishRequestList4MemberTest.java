package guitest.wishrequest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kundenverwaltung.controller.wishrequest.WishRequestList4MemberController;
import kundenverwaltung.model.User;
import kundenverwaltung.server.dto.WishRequestDTO;
import kundenverwaltung.server.service.UserEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class WishRequestList4MemberTest extends ApplicationTest {

    private WishRequestList4MemberController controller;
    private TableView<WishRequestDTO> table;
    private TextField searchField;
    private Button newBtn, deleteBtn, refreshBtn;

    private ObservableList<WishRequestDTO> testData;

    @Override
    public void start(Stage stage) throws Exception
    {
        User testUser = new User("", "", "", LocalDate.now(), "", "");
        testUser.setUserId(1);
        testUser.setUserName("testuser");

        UserEntityService.getInstance().setUser(testUser);

        var loader = new javafx.fxml.FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/wishrequest/WishRequestList4Member.fxml"));
        var root = loader.load();
        controller = loader.getController();
        var scene = new javafx.scene.Scene((Parent) root);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() throws Exception
    {
        WaitForAsyncUtils.waitForFxEvents();

        table = lookup("#wishRequestTable").queryAs(TableView.class);
        searchField = lookup("#searchTextField").queryAs(TextField.class);
        newBtn = lookup("#newButton").queryAs(Button.class);
        deleteBtn = lookup("#deleteButton").queryAs(Button.class);
        refreshBtn = lookup("#refreshButton").queryAs(Button.class);

        injectTestData();
    }

    private void injectTestData() throws Exception {
        Field masterDataField = controller.getClass().getDeclaredField("masterData");
        masterDataField.setAccessible(true);
        ObservableList<WishRequestDTO> masterData = (ObservableList<WishRequestDTO>) masterDataField.get(controller);

        testData = FXCollections.observableArrayList(
                WishRequestDTO.builder()
                        .setUuid(UUID.randomUUID())
                        .setTitle("Mehr Kaffee")
                        .setDescription("Wir brauchen mehr Kaffee")
                        .setStatus(WishRequestDTO.Status.Offen)
                        .build(),
                WishRequestDTO.builder()
                        .setUuid(UUID.randomUUID())
                        .setTitle("Neue Stühle")
                        .setDescription("Stühle sind alt")
                        .setStatus(WishRequestDTO.Status.In_Bearbeitung)
                        .build(),
                WishRequestDTO.builder()
                        .setUuid(UUID.randomUUID())
                        .setTitle("Tafel sauber machen")
                        .setDescription("Bitte reinigen")
                        .setStatus(WishRequestDTO.Status.Abgeschlossen)
                        .build()
        );

        interact(() -> masterData.setAll(testData));
    }

    @Test
    public void testUIComponentsExist() {
        assertAll(
                () -> assertNotNull(table),
                () -> assertNotNull(searchField),
                () -> assertNotNull(newBtn),
                () -> assertNotNull(deleteBtn),
                () -> assertNotNull(refreshBtn)
        );
    }

    @Test
    public void testTableDataCorrectlyLoaded() {
        assertEquals(3, table.getItems().size());
        assertEquals("Mehr Kaffee", table.getItems().get(0).getTitle());
    }

    @Test
    public void testSearchFiltersTitle() {
        interact(() -> searchField.setText("Kaffee"));
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(1, table.getItems().size());
        assertEquals("Mehr Kaffee", table.getItems().get(0).getTitle());
    }

    @Test
    public void testSearchFiltersDescription() {
        interact(() -> searchField.setText("reinigen"));
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(1, table.getItems().size());
        assertEquals("Tafel sauber machen", table.getItems().get(0).getTitle());
    }

    @Test
    public void testSearchIsCaseInsensitive() {
        interact(() -> searchField.setText("STÜHLE"));
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(1, table.getItems().size());
        assertEquals("Neue Stühle", table.getItems().get(0).getTitle());
    }

    @Test
    public void testSearchNoMatch() {
        interact(() -> searchField.setText("xyz123"));
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(0, table.getItems().size());
    }

    @Test
    public void testRefreshButtonAction() {
        assertDoesNotThrow(() -> {
            interact(() -> refreshBtn.fire());
            WaitForAsyncUtils.waitForFxEvents();
        });
    }

    @Test
    public void testNewButtonExistsAndHasAction() {
        assertAll(
                () -> assertNotNull(newBtn.getOnAction()),
                () -> assertEquals("Neue Wunschanfrage einreichen", newBtn.getText())
        );
    }

    @Test
    public void testDeleteWithoutSelection() {
        // Ensure nothing is selected
        interact(() -> table.getSelectionModel().clearSelection());

        interact(() -> deleteBtn.fire());
        // Expected: warning dialog (would show real dialog, here we just ensure it doesn’t crash)
        WaitForAsyncUtils.waitForFxEvents();

        assertNull(table.getSelectionModel().getSelectedItem());
    }

    @Test
    public void testTableSelectionAndDeleteActionTrigger() {
        interact(() -> {
            table.getSelectionModel().select(0);
            deleteBtn.fire();
        });

        // Actual deletion confirmation dialog can't be auto-confirmed here,
        // but test ensures no exception is thrown.
        WaitForAsyncUtils.waitForFxEvents();
        assertNotNull(table.getSelectionModel().getSelectedItem());
    }

    @Test
    public void testPromptText() {
        assertEquals("Wunschanfragen durchsuchen...", searchField.getPromptText());
    }

    @Test
    public void testTableIsVisibleAndEnabled() {
        assertTrue(table.isVisible());
        assertFalse(table.isDisabled());
    }

    @Test
    public void testSearchFieldEditable() {
        assertTrue(searchField.isEditable());
        assertFalse(searchField.isDisabled());
    }

    @Test
    public void testColumnResizePolicy() {
        assertEquals(TableView.CONSTRAINED_RESIZE_POLICY, table.getColumnResizePolicy());
    }
}
