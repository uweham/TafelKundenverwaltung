package kundenverwaltung.service;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationExtension;
import kundenverwaltung.dao.SQLConnection;
import kundenverwaltung.controller.MainController;
import kundenverwaltung.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class TablePreferenceServiceImplTest
{

    private TablePreferenceServiceImpl service;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private MockedStatic<SQLConnection> mockedSQLConnection;
    private MockedStatic<MainController> mockedMainController;

    @BeforeEach
    void setUp() throws SQLException
    {
        service = TablePreferenceServiceImpl.getInstance();

        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        mockedSQLConnection = Mockito.mockStatic(SQLConnection.class);
        mockedSQLConnection.when(SQLConnection::getCon).thenReturn(mockConnection);

        MainController mockMainController = mock(MainController.class);
        User mockUser = mock(User.class);
        when(mockUser.getUserId()).thenReturn(1);
        when(mockMainController.getUser()).thenReturn(mockUser);

        mockedMainController = Mockito.mockStatic(MainController.class);
        mockedMainController.when(MainController::getInstance).thenReturn(mockMainController);
    }

    @AfterEach
    void tearDown()
    {
        if (mockedSQLConnection != null)
        {
            mockedSQLConnection.close();
        }
        if (mockedMainController != null)
        {
            mockedMainController.close();
        }
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
    void savePreferences_ShouldExecuteCorrectSQLStatements() throws SQLException
    {
        int userId = 1;
        String tableName = "testTable";
        TableView<Object> tableView = createTestTableView();

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);


        service.savePreferences(userId, tableName, tableView);

        verify(mockConnection, times(2)).prepareStatement(anyString());
        verify(mockPreparedStatement, atLeast(1)).executeUpdate();
        verify(mockPreparedStatement, times(1)).executeBatch();

        verify(mockPreparedStatement, atLeast(2)).setInt(eq(1), eq(userId));
        verify(mockPreparedStatement, atLeast(1)).setString(eq(2), eq(tableName));
    }

    @Test
    void loadPreferences_ShouldQueryDatabaseCorrectly() throws SQLException
    {
        int userId = 1;
        String tableName = "testTable";
        TableView<Object> tableView = createTestTableView();

        when(mockResultSet.next()).thenReturn(false);

        service.loadPreferences(userId, tableName, tableView);

        verify(mockConnection).prepareStatement(contains("SELECT"));
        verify(mockPreparedStatement).setInt(1, userId);
        verify(mockPreparedStatement).setString(2, tableName);
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void loadPreferences_ShouldApplyPreferencesWhenFound() throws SQLException
    {
        int userId = 1;
        String tableName = "testTable";
        TableView<Object> tableView = createTestTableView();

        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("column_id")).thenReturn("col1", "col2");
        when(mockResultSet.getDouble("width")).thenReturn(150.0, 200.0);
        when(mockResultSet.getInt("order_index")).thenReturn(0, 1);
        when(mockResultSet.getBoolean("visible")).thenReturn(true, false);
        when(mockResultSet.getString("sort_type")).thenReturn("ASCENDING", "DESCENDING");

        service.loadPreferences(userId, tableName, tableView);

        verify(mockResultSet, atLeast(2)).getString("column_id");
        verify(mockResultSet, atLeast(2)).getDouble("width");
        verify(mockResultSet, atLeast(2)).getInt("order_index");
        verify(mockResultSet, atLeast(2)).getBoolean("visible");
        verify(mockResultSet, atLeast(2)).getString("sort_type");

        assertEquals(2, tableView.getColumns().size());
    }

    @Test
    void deleteAllTableLayouts_ShouldExecuteDeleteStatement() throws SQLException
    {
        int userId = 1;
        when(mockPreparedStatement.executeUpdate()).thenReturn(5);

        service.deleteAllTableLayouts(userId);

        verify(mockConnection).prepareStatement(contains("DELETE"));
        verify(mockPreparedStatement).setInt(1, userId);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void setupPersistence_ShouldReturnEarlyWhenNoUser()
    {
        MainController mockMainController = mock(MainController.class);
        when(mockMainController.getUser()).thenReturn(null);

        mockedMainController.when(MainController::getInstance).thenReturn(mockMainController);

        TableView<Object> tableView = createTestTableView();
        String tableName = "testTable";

        assertDoesNotThrow(() -> service.setupPersistence(tableView, tableName),
                "setupPersistence should handle null user gracefully");
    }

    @Test
    void setupPersistence_WithTwoParameters_ShouldCallMainMethod()
    {
        TableView<Object> tableView = createTestTableView();
        String tableName = "testTable";

        assertDoesNotThrow(() -> service.setupPersistence(tableView, tableName),
                "setupPersistence with 2 parameters should not throw");
    }

    @Test
    void setupPersistence_WithThreeParameters_ShouldCallMainMethod()
    {
        TableView<Object> tableView = createTestTableView();
        String tableName = "testTable";
        boolean autoColumnCenterAlignment = true;

        assertDoesNotThrow(() -> service.setupPersistence(tableView, tableName, autoColumnCenterAlignment),
                "setupPersistence with 3 parameters should not throw");
    }

    @Test
    void savePreferences_ShouldHandleSQLException()
    {
        int userId = 1;
        String tableName = "testTable";
        TableView<Object> tableView = createTestTableView();

        try {
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

            assertDoesNotThrow(() -> service.savePreferences(userId, tableName, tableView),
                    "savePreferences should handle SQLException gracefully");

        } catch (SQLException e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }

    @Test
    void loadPreferences_ShouldHandleSQLException()
    {
        int userId = 1;
        String tableName = "testTable";
        TableView<Object> tableView = createTestTableView();

        try {
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

            assertDoesNotThrow(() -> service.loadPreferences(userId, tableName, tableView),
                    "loadPreferences should handle SQLException gracefully");

        } catch (SQLException e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }

    @Test
    void savePreferences_ShouldSkipColumnsWithoutId() throws SQLException
    {
        int userId = 1;
        String tableName = "testTable";
        TableView<Object> tableView = new TableView<>();

        TableColumn<Object, String> columnWithId = new TableColumn<>("Column 1");
        columnWithId.setId("col1");

        TableColumn<Object, String> columnWithoutId = new TableColumn<>("Column 2");

        tableView.getColumns().addAll(columnWithId, columnWithoutId);

        service.savePreferences(userId, tableName, tableView);

        verify(mockPreparedStatement, times(1)).addBatch();
    }

    @Test
    void deleteAllTableLayouts_ShouldHandleSQLException()
    {
        int userId = 1;

        try {
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

            assertDoesNotThrow(() -> service.deleteAllTableLayouts(userId),
                    "deleteAllTableLayouts should handle SQLException gracefully");

        } catch (SQLException e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }

    private TableView<Object> createTestTableView()
    {
        TableView<Object> tableView = new TableView<>();
        
        TableColumn<Object, String> col1 = new TableColumn<>("Column 1");
        col1.setId("col1");
        col1.setPrefWidth(100.0);
        
        TableColumn<Object, String> col2 = new TableColumn<>("Column 2");
        col2.setId("col2");
        col2.setPrefWidth(150.0);
        
        tableView.getColumns().addAll(col1, col2);
        return tableView;
    }
}