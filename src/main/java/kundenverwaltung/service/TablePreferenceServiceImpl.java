package kundenverwaltung.service;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import kundenverwaltung.controller.MainController;
import kundenverwaltung.dao.SQLConnection;
import kundenverwaltung.model.ColumnPreference;
import kundenverwaltung.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TablePreferenceServiceImpl implements TablePreferenceService
{
    private static final TablePreferenceServiceImpl tablePreferenceService = new TablePreferenceServiceImpl();
    private final DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @SuppressWarnings("unchecked")
    private void applyFormattedAndCenteredCellFactory(TableView<?> tableView)
    {
        for (TableColumn<?, ?> col : tableView.getColumns())
        {
            TableColumn<Object, Object> typedCol = (TableColumn<Object, Object>) col;

            typedCol.setCellFactory(column -> new TableCell<Object, Object>()
            {
                @Override
                protected void updateItem(Object item, boolean empty)
                {
                    super.updateItem(item, empty);

                    if (empty || item == null)
                    {
                        setText(null);
                        setGraphic(null);
                    }
                    else
                    {
                        String text;

                        if (item instanceof LocalDateTime dateTime)
                        {
                            text = defaultDateTimeFormatter.format(dateTime);
                        }
                        else
                        {
                            text = item.toString();
                        }

                        Label label = new Label(text);
                        label.setMaxWidth(Double.MAX_VALUE);
                        label.setAlignment(Pos.CENTER);
                        setGraphic(label);
                        setText(null);
                    }
                }
            });
        }
    }

    /**
     * Saves the current state of a TableView's columns (width, order, visibility, sort type) to the database.
     *
     * @param userId    The ID of the user for whom to save the preferences.
     * @param tableName A unique name for the table being saved.
     * @param tableView The TableView whose column preferences are to be saved.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void savePreferences(int userId, String tableName, TableView<?> tableView) throws SQLException
    {
        String deleteSql = "DELETE FROM table_column_preferences WHERE userId = ? AND table_name = ?";

        try (Connection conn = SQLConnection.getCon())
        {
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setInt(1, userId);
            deleteStmt.setString(2, tableName);
            deleteStmt.executeUpdate();
        }

        String insertSql = """
                INSERT INTO table_column_preferences
                (userId, table_name, column_id, width, order_index, visible, sort_type)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = SQLConnection.getCon())
        {
            PreparedStatement stmt = conn.prepareStatement(insertSql);
            ObservableList<? extends TableColumn<?, ?>> columns = tableView.getColumns();

            for (int i = 0; i < columns.size(); i++)
            {
                TableColumn<?, ?> col = columns.get(i);

                if (col.getId() == null)
                {
                    continue;
                }

                stmt.setInt(1, userId);
                stmt.setString(2, tableName);
                stmt.setString(3, col.getId());
                stmt.setDouble(4, col.getWidth());
                stmt.setInt(5, i);
                stmt.setBoolean(6, col.isVisible());
                stmt.setString(7, col.getSortType() != null ? col.getSortType().toString() : null);
                stmt.addBatch();
            }

            stmt.executeBatch();
        }
    }

    /**
     * Loads and applies column preferences (width, order, visibility, sort type) from the database to a TableView.
     *
     * @param userId    The ID of the user whose preferences are to be loaded.
     * @param tableName A unique name for the table being configured.
     * @param tableView The TableView to which the loaded preferences will be applied.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void loadPreferences(int userId, String tableName, TableView<?> tableView) throws SQLException
    {
        String selectSql = """
                SELECT column_id, width, order_index, visible, sort_type
                FROM table_column_preferences
                WHERE userId = ? AND table_name = ?
                """;

        Map<String, ColumnPreference> prefMap = new HashMap<>();

        try (Connection conn = SQLConnection.getCon())
        {
            PreparedStatement stmt = conn.prepareStatement(selectSql);
            stmt.setInt(1, userId);
            stmt.setString(2, tableName);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    String columnId = rs.getString("column_id");

                    ColumnPreference pref = new ColumnPreference(
                        userId,
                        tableName,
                        columnId,
                        rs.getDouble("width"),
                        rs.getInt("order_index"),
                        rs.getBoolean("visible"),
                        rs.getString("sort_type")
                    );

                    prefMap.put(columnId, pref);
                }
            }
        }

        List<TableColumn<?, ?>> currentColumns = new ArrayList<>(tableView.getColumns());

        currentColumns.sort(Comparator.comparingInt(c ->
        {
            ColumnPreference pref = prefMap.get(c.getId());
            return pref != null ? pref.getOrderIndex() : Integer.MAX_VALUE;
        }));

        for (TableColumn<?, ?> col : currentColumns)
        {
            ColumnPreference pref = prefMap.get(col.getId());

            if (pref != null)
            {
                col.setPrefWidth(pref.getWidth());
                col.setVisible(pref.isVisible());

                if (pref.getSortType() != null)
                {
                    col.setSortType(TableColumn.SortType.valueOf(pref.getSortType()));

                }
                else
                {
                    col.setSortType(null);
                }
            }
        }

        tableView.getColumns().setAll(currentColumns.toArray(new TableColumn[0]));
    }

    /**
     * Deletes all saved table layout preferences for a specific user from the database.
     *
     * @param userId The ID of the user whose preferences are to be deleted.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteAllTableLayouts(int userId) throws SQLException
    {
        String deleteSql = "DELETE FROM table_column_preferences WHERE userId = ?";

        try (Connection conn = SQLConnection.getCon())
        {
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setInt(1, userId);
            deleteStmt.executeUpdate();
        }
    }

    public void setupPersistence(TableView<?> tableView, String tableName)
    {
        setupPersistence(tableView, tableName, false, true);
    }

    public void setupPersistence(TableView<?> tableView, String tableName, boolean autoColumnCenterAlignment)
    {
        setupPersistence(tableView, tableName, autoColumnCenterAlignment, true);
    }

    /**
     * Sets up persistence for a TableView. It loads the preferences when the view is created
     * and saves them when the window is closed.
     *
     * @param tableView The TableView for which to manage persistence.
     * @param tableName A unique name for the table.
     */
    public void setupPersistence(TableView<?> tableView, String tableName, boolean autoColumnCenterAlignment, boolean formatLocalDateTime)
    {
        User user = MainController.getInstance().getUser();

        if (user == null)
        {
            return;
        }

        int userId = user.getUserId();

        try
        {
            loadPreferences(userId, tableName, tableView);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        Platform.runLater(() ->
        {
            if (tableView != null && tableView.getScene() != null)
            {
                Stage stage = (Stage) tableView.getScene().getWindow();

                stage.setOnCloseRequest(event ->
                {
                    try
                    {
                        savePreferences(userId, tableName, tableView);
                    }
                    catch (SQLException e)
                    {
                        throw new RuntimeException(e);
                    }
                });
            }
        });

        applyFormattedAndCenteredCellFactory(tableView);
    }

    /**
     * Returns the singleton instance of the TablePreferenceServiceImpl.
     *
     * @return The TablePreferenceServiceImpl instance.
     */
    public static TablePreferenceServiceImpl getInstance()
    {
        return tablePreferenceService;
    }
}