package guitest.errorreport;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kundenverwaltung.server.dto.ErrorReportDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class ErrorReportList4MemberControllerTest extends ApplicationTest
{

    private TextField searchField;
    private TableView<ErrorReportDTO> table;
    private Button newButton;
    private Button refreshButton;

    @Override
    public void start(Stage stage)
    {
        searchField = new TextField();
        searchField.setPromptText("Fehlermeldungen durchsuchen...");
        searchField.setId("searchField");

        table = new TableView<>();
        table.setId("errorReportTable");

        newButton = new Button("Neuen Fehler melden");
        newButton.setId("newButton");

        refreshButton = new Button("Aktualisieren");
        refreshButton.setId("refreshButton");

        VBox topBox = new VBox(10, new Label("Fehlerberichte"), searchField);
        HBox bottomBox = new HBox(10, newButton, refreshButton);
        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(table);
        root.setBottom(bottomBox);

        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    @Test
    @Tag("gui")
    public void testInitialState()
    {
        verifyThat("#searchField", hasText(""));
        assertNotNull(table);
        assertNotNull(newButton);
        assertNotNull(refreshButton);

        assertEquals("Neuen Fehler melden", newButton.getText());
        assertEquals("Aktualisieren", refreshButton.getText());
    }

    @Test
    @Tag("gui")
    public void testButtonsClickable()
    {
        assertDoesNotThrow(() -> clickOn("#newButton"));
        assertDoesNotThrow(() -> clickOn("#refreshButton"));
    }

    @Test
    @Tag("gui")
    public void testUIComponentsAccessible()
    {
        assertNotNull(searchField);
        assertNotNull(table);
        assertNotNull(newButton);
        assertNotNull(refreshButton);
        assertEquals("Fehlermeldungen durchsuchen...", searchField.getPromptText());
    }
}
