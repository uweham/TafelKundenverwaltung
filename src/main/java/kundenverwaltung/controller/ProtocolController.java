package kundenverwaltung.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.logger.file.LogFileService;
import kundenverwaltung.logger.file.LogReader;
import kundenverwaltung.logger.model.LogEntry;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.toolsandworkarounds.CustomPropertiesStore;

import static kundenverwaltung.toolsandworkarounds.PropertiesFileController.PROPERTIES_LOCATION_TAFEL_INFO;

public class ProtocolController
{
    @FXML private TextField tfStoragePath;
    @FXML private Button btnBrowseStorage;
    @FXML private Spinner<Integer> loeschInterval;
    @FXML private ComboBox<String> cbSelectedFile;
    @FXML private TextField filterField;
    @FXML private TableView<LogEntry> tableViewLogs;
    @FXML private TableColumn<LogEntry, String> colTimestamp;
    @FXML private TableColumn<LogEntry, String> colLevel;
    @FXML private TableColumn<LogEntry, String> colSource;
    @FXML private TableColumn<LogEntry, String> colMessage;
    @FXML private TextArea textAreaDetail;

    private final ObservableList<LogEntry> masterData = FXCollections.observableArrayList();
    private static final String NOTIFICATION_TITLE_CHANGES_SAVED = "Gespeichert";
    private static final String NOTIFICATION_TEXT_CHANGES_SAVED = "Protokoll Änderungen wurden gespeichert!";

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded. It sets up the UI components,
     * table columns, filtering, and loads the initial log data.
     */
    @FXML
    public void initialize()
    {
        initSettingsUI();

        initLogFileComboBox();

        colTimestamp.setCellValueFactory(data -> data.getValue().timestampProperty());
        colTimestamp.setId("timestamp");

        colLevel.setCellValueFactory(data -> data.getValue().levelProperty());
        colLevel.setId("level");

        colSource.setCellValueFactory(data -> data.getValue().sourceProperty());
        colSource.setId("source");

        colMessage.setCellValueFactory(data -> data.getValue().messageProperty());
        colMessage.setId("message");

        FilteredList<LogEntry> filteredData = new FilteredList<>(masterData, p -> true);

        filterField.textProperty().addListener((obs, oldVal, newVal) ->
        {
            String filter = newVal == null ? "" : newVal.toLowerCase();
            filteredData.setPredicate(entry ->
            {
                if (filter.isEmpty()) return true;

                return entry.timestampProperty().get().toLowerCase().contains(filter)
                        || entry.levelProperty().get().toLowerCase().contains(filter)
                        || entry.sourceProperty().get().toLowerCase().contains(filter)
                        || entry.messageProperty().get().toLowerCase().contains(filter);
            });
        });

        SortedList<LogEntry> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableViewLogs.comparatorProperty());
        tableViewLogs.setItems(sortedData);

        tableViewLogs.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) ->
        {
            textAreaDetail.setText(selected != null ? selected.getFullMessage() : "");
        });

        TablePreferenceServiceImpl.getInstance().setupPersistence(tableViewLogs, "TableViewLogsProtokoll");

        loadLogs();
    }

    /**
     * Initializes the settings part of the UI with current values from the configuration.
     */
    private void initSettingsUI()
    {
        loeschInterval.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365)
        );

        String logDirectoryPath = LogFileService.getLogDirectoryPath();
        tfStoragePath.setText(logDirectoryPath);

        int interval = LogFileService.getDeleteIntervalInDays();
        loeschInterval.getValueFactory().setValue(interval);
    }

    /**
     * Initializes the ComboBox for log file selection with available log files.
     */
    private void initLogFileComboBox()
    {
        ObservableList<String> logFiles = LogFileService.getAvailableLogFiles();

        cbSelectedFile.setItems(logFiles);

        cbSelectedFile.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
        {
            if (newVal != null)
            {
                loadSelectedLogFile(newVal);
            }
        });
    }

    /**
     * Loads the content of a selected log file into the TableView.
     *
     * @param fileName The name of the log file to load.
     */
    private void loadSelectedLogFile(String fileName)
    {
        String logDirectory = LogFileService.getLogDirectoryPath();
        String fullPath = Paths.get(logDirectory, fileName).toString();

        try
        {
            List<LogEntry> logs = LogReader.readLogsFromFile(fullPath);

            logs.sort(Comparator.comparing(LogEntry::getTimestamp).reversed());

            masterData.clear();
            masterData.addAll(logs);
        }
        catch (IOException e)
        {
            showErrorDialog("Fehler beim Laden!", e.getMessage());
        }
    }

    /**
     * Handles the action of the "Browse" button to select a new log storage directory.
     */
    @FXML
    private void onBrowseStorage()
    {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Log-Verzeichnis wählen");
        File last = new File(tfStoragePath.getText());

        if (last.exists())
        {
            chooser.setInitialDirectory(last);
        }

        File dir = chooser.showDialog(btnBrowseStorage.getScene().getWindow());

        if (dir != null)
        {
            tfStoragePath.setText(dir.getAbsolutePath());
        }
    }

    /**
     * Handles the action of saving the protocol settings, including the deletion interval and storage path.
     *
     * @throws Exception if an error occurs while saving the settings.
     */
    @FXML
    private void onSaveSettings() throws Exception
    {
        int interval = loeschInterval.getValue();

        try
        {
            setDeleteInterval(interval);
        }
        catch (Exception e)
        {
            showErrorDialog("Fehler beim Lösch-Interval!", "Die TafelInfo.properties im Verzeichnis " + Paths.get(PROPERTIES_LOCATION_TAFEL_INFO).toString() + " ist nicht vorhanden!");
            return;
        }

        try
        {
            LogFileService.updateDirectoryLogPath(tfStoragePath.getText());
        }
        catch (Exception e)
        {
            showErrorDialog("Fehler beim Log-Verzeichnis!", e.getMessage());
            return;
        }

        loadLogs();

        initLogFileComboBox();

        Benachrichtigung.infoBenachrichtigung(NOTIFICATION_TITLE_CHANGES_SAVED, NOTIFICATION_TEXT_CHANGES_SAVED);
    }

    /**
     * Handles the action of the "Refresh" button to reload the log data.
     */
    @FXML
    private void onRefresh()
    {
        loadLogs();
    }

    /**
     * Handles the action of deleting the currently selected log file.
     */
    @FXML
    private void onDeleteSelected()
    {
        String selectedFile = cbSelectedFile.getSelectionModel().getSelectedItem();

        if (selectedFile == null || selectedFile.isEmpty())
        {
            showErrorDialog("Keine Datei ausgewählt!", "Bitte wählen Sie eine Log-Datei zum Löschen aus.");
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Wirklich die Log-Datei '" + selectedFile + "' löschen?",
                ButtonType.YES, ButtonType.NO
        );

        confirm.initOwner(tableViewLogs.getScene().getWindow());

        confirm.showAndWait().ifPresent(button ->
        {
            if (button == ButtonType.YES)
            {
                try
                {
                    String logDirectory = LogFileService.getLogDirectoryPath();
                    Path fileToDelete = Paths.get(logDirectory, selectedFile);

                    Files.delete(fileToDelete);

                    Benachrichtigung.infoBenachrichtigung(
                            "Datei gelöscht",
                            "Die Log-Datei wurde erfolgreich gelöscht"
                    );

                    loadLogs();
                    initLogFileComboBox();
                }
                catch (IOException e)
                {
                    showErrorDialog(
                            "Fehler beim Löschen!",
                            "Die Datei konnte nicht gelöscht werden: " + e.getMessage()
                    );
                }
            }
        });
    }

    /**
     * Loads the first available log file into the TableView.
     */
    private void loadLogs()
    {
        ObservableList<String> entries = cbSelectedFile.getItems();

        if (entries != null && !entries.isEmpty())
        {
            cbSelectedFile.getSelectionModel().selectFirst();
            String entry = cbSelectedFile.getSelectionModel().getSelectedItem();

            if (entry != null)
            {
                loadSelectedLogFile(entry);
            }
        }
    }

    /**
     * Sets the log file deletion interval in the application's properties file.
     *
     * @param deleteInterval The number of days after which log files should be deleted.
     * @throws Exception if an error occurs while writing to the properties file.
     */
    public void setDeleteInterval(int deleteInterval) throws Exception
    {
        if (!Files.exists(Paths.get(PROPERTIES_LOCATION_TAFEL_INFO)))
        {
            return;
        }

        CustomPropertiesStore prop = CustomPropertiesStore.loadTafelInfoPropertiesFileCustomStore();
        prop.setProperty("tafel.protokoll.loeschinterval", String.valueOf(deleteInterval));
        FileOutputStream output = new FileOutputStream(PROPERTIES_LOCATION_TAFEL_INFO);
        prop.store(output, "Tafel Informationen");
        output.close();
    }

    /**
     * Displays an error dialog with a specific title and message.
     *
     * @param title   The title of the error dialog.
     * @param message The message to be displayed in the dialog.
     */
    private void showErrorDialog(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}