package kundenverwaltung.service;

import javafx.scene.control.TableView;

public interface TablePreferenceService
{
    /**
     * Saves the column visibility and order preferences for a given TableView.
     *
     * @param userId    The ID of the user whose preferences are being saved.
     * @param tableName The unique name identifying the table.
     * @param tableView The TableView instance whose preferences are to be saved.
     * @throws Exception if an error occurs while saving the preferences.
     */
    void savePreferences(int userId, String tableName, TableView<?> tableView) throws Exception;

    /**
     * Loads and applies the column visibility and order preferences for a given TableView.
     *
     * @param userId    The ID of the user whose preferences are being loaded.
     * @param tableName The unique name identifying the table.
     * @param tableView The TableView instance to which the preferences will be applied.
     * @throws Exception if an error occurs while loading the preferences.
     */
    void loadPreferences(int userId, String tableName, TableView<?> tableView) throws Exception;
}