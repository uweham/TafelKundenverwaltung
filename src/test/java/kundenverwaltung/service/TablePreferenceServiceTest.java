package kundenverwaltung.service;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class TablePreferenceServiceTest
{

    private TablePreferenceServiceImpl service;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException
    {
        service = TablePreferenceServiceImpl.getInstance();

        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }


    @Test
    void getInstance_ShouldReturnSingleton()
    {
        TablePreferenceServiceImpl instance1 = TablePreferenceServiceImpl.getInstance();
        TablePreferenceServiceImpl instance2 = TablePreferenceServiceImpl.getInstance();

        assertNotNull(instance1, "getInstance should not return null");
        assertSame(instance1, instance2, "getInstance should return the same instance (singleton pattern)");
    }

    @Test
    void savePreferences_ShouldNotThrowWithValidData()
    {
        int userId = 1;
        String tableName = "testTable";
        TableView<Object> tableView = createTestTableView();

        assertDoesNotThrow(() -> service.savePreferences(userId, tableName, tableView),
                "savePreferences should not throw an exception with valid data");
    }

    @Test
    void savePreferences_ShouldThrowBatchUpdateExceptionWithNonExistentUserId()
    {
        int nonExistentUserId = 99999;
        String tableName = "testTable";
        TableView<Object> tableView = createTestTableView();

        Exception exception = assertThrows(Exception.class,
                () -> service.savePreferences(nonExistentUserId, tableName, tableView),
                "savePreferences should throw BatchUpdateException with non-existent userId");

        assertTrue(exception.getMessage().contains("foreign key constraint") ||
                        exception instanceof java.sql.BatchUpdateException,
                "Exception should be related to foreign key constraint violation");
    }

    @Test
    void savePreferences_ShouldThrowWithNullTableName()
    {
        int userId = 1;
        String tableName = null;
        TableView<Object> tableView = createTestTableView();

        assertThrows(Exception.class,
                () -> service.savePreferences(userId, tableName, tableView),
                "savePreferences should throw an exception with null tableName");
    }

    @Test
    void loadPreferences_ShouldNotThrowWithValidData()
    {
        int userId = 1;
        String tableName = "testTable";
        TableView<Object> tableView = createTestTableView();

        assertDoesNotThrow(() -> service.loadPreferences(userId, tableName, tableView),
                "loadPreferences should not throw an exception with valid data");
    }

    @Test
    void deleteAllTableLayouts_ShouldNotThrowWithValidUserId()
    {
        int userId = 1;

        assertDoesNotThrow(() -> service.deleteAllTableLayouts(userId),
                "deleteAllTableLayouts should not throw an exception with valid userId");
    }

    private TableView<Object> createTestTableView()
    {
        TableView<Object> tableView = new TableView<>();

        TableColumn<Object, String> column1 = new TableColumn<>("Column 1");
        column1.setId("col1");
        column1.setPrefWidth(100);
        column1.setVisible(true);
        column1.setSortType(TableColumn.SortType.ASCENDING);

        TableColumn<Object, String> column2 = new TableColumn<>("Column 2");
        column2.setId("col2");
        column2.setPrefWidth(150);
        column2.setVisible(false);
        column2.setSortType(TableColumn.SortType.DESCENDING);

        tableView.getColumns().addAll(column1, column2);
        return tableView;
    }

}