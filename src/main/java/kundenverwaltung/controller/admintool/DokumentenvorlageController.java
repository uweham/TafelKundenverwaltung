package kundenverwaltung.controller.admintool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Optional;
import javax.sql.rowset.serial.SerialBlob;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import kundenverwaltung.dao.VorlageDAOimpl;
import kundenverwaltung.model.Vorlage;
import kundenverwaltung.model.Vorlagearten;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.toolsandworkarounds.ExportTemplate;
import kundenverwaltung.toolsandworkarounds.GetTemplateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DokumentenvorlageController
{
    public static final Logger DOK_VORLAGE_CONTROLLER_LOGGER =
            LoggerFactory.getLogger(DokumentenvorlageController.class);

    @FXML private Button btnImport;
    @FXML private Button btnExport;
    @FXML private Button btnDelete;
    @FXML private Button btnActivate;

    @SuppressWarnings("rawtypes")
    @FXML private TableView filesTableView;

    @SuppressWarnings("rawtypes")
    @FXML private TableColumn templateName;

    @SuppressWarnings("rawtypes")
    @FXML private TableColumn author;

    @SuppressWarnings("rawtypes")
    @FXML private TableColumn version;

    @SuppressWarnings("rawtypes")
    @FXML private TableColumn status;

    private VorlageDAOimpl vorlageDAOimpl = new VorlageDAOimpl();
    private ArrayList<Vorlage> templatesArrayList = new ArrayList<>();
    private ObservableList<Vorlage> templateObservableList;
    private GetTemplateType getTemplateType = new GetTemplateType();
    private ExportTemplate exportTemplate = new ExportTemplate();

    // Konstanten für das Parsen
    private static final String SEARCH_NAME = "NAME=";
    private static final String SEARCH_VERSION = "VERSION=";
    private static final String SEARCH_TYPE = "TYPE=";
    private static final String SEARCH_FILETYPES = "FILETYPES=";
    private static final String SEARCH_PASSWORD = "PASSWORT=";
    private static final String SEARCH_DEFAULTEXT = "DEFAULTEXT=";
    private static final String SEARCH_AUTHOR = "AUTOR=";


    @SuppressWarnings("unchecked")
    public void initialize()
    {
        templatesArrayList = vorlageDAOimpl.getAllTemplate();
        templateObservableList = FXCollections.observableList(templatesArrayList);

        templateName.setCellValueFactory(new PropertyValueFactory<>("name"));
        templateName.setId("name");

        author.setCellValueFactory(new PropertyValueFactory<>("autor"));
        author.setId("autor");

        version.setCellValueFactory(new PropertyValueFactory<>("fileVersion"));
        version.setId("fileVersion");

        status.setCellValueFactory(new PropertyValueFactory<>("aktivString"));
        status.setId("aktivString");

        filesTableView.setItems(templateObservableList);
        TablePreferenceServiceImpl.getInstance().setupPersistence(filesTableView, "FilesTableViewDokumentVorlage");
    }

    @SuppressWarnings("unchecked")
    @FXML public void deleteTemplate()
    {
        Vorlage template = (Vorlage) filesTableView.getSelectionModel().getSelectedItem();
        if (template != null && vorlageDAOimpl.delete(template))
        {
            templateObservableList.remove(template);
            filesTableView.setItems(templateObservableList);
        }
    }

    @SuppressWarnings("unchecked")
    @FXML public void disabledTemplate()
    {
        Vorlage template = (Vorlage) filesTableView.getSelectionModel().getSelectedItem();
        if (template != null)
        {
            // Erstellt ein neues Objekt für das Update (ohne DB-Create Aufruf, da wir ID übergeben)
            Vorlage updatedTemplate = new Vorlage(template.getVorlageId(),
                    template.getTemplateType(),
                    template.getName(), template.getAutor(), template.getFileVersion(), template.getFileTypes(),
                    template.getDefaultText(), template.getPasswort(), !template.isAktiv(), template.getDaten());

            Boolean updateSuccessful = vorlageDAOimpl.update(updatedTemplate);

            if (updateSuccessful)
            {
                initialize(); // Reload list to reflect changes
            }
        }
    }

    @FXML public void exportTemplate()
    {
        int selectedTemplateIndex = filesTableView.getSelectionModel().getSelectedIndex();
        if (selectedTemplateIndex >= 0)
        {
            Vorlage selected = (Vorlage) filesTableView.getItems().get(selectedTemplateIndex);
            exportTemplate.exportTemplate(selected.getDaten());
        }
    }

    public static byte[] convertFileContentToBlob(String fPath) throws IOException
    {
        StringBuilder fileContentStr = new StringBuilder();
        // Nutze UTF-8 beim Einlesen, um Sonderzeichenprobleme zu vermeiden
        try (BufferedReader reader = new BufferedReader(new FileReader(fPath, StandardCharsets.UTF_8)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                fileContentStr.append(line).append("\n");
            }
            return fileContentStr.toString().trim().getBytes(StandardCharsets.UTF_8);
        }
    }

    /**
     * Importiert eine Datei in die Datenbank. Prüft auf Duplikate und führt ggf. ein Update durch.
     */
    @SuppressWarnings("unchecked")
    @FXML
    protected void importButtonAction()
    {
        String name = null;
        String templateTypeString = null;
        String author = null;
        String fileVersion = null;
        String fileTypes = null;
        String defaultText = null;
        String password = "0";
        Boolean aktiv = true;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Tafel-Vorlagen", "*.html"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null)
        {
            String fPath = selectedFile.getAbsolutePath();
            DOK_VORLAGE_CONTROLLER_LOGGER.info("Importing File: {}", fPath);

            // V3: Der robuste Parser
            // Wir nutzen UTF-8, um Probleme mit Sonderzeichen zu vermeiden
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile, StandardCharsets.UTF_8)))
            {
                String line;
                int linesRead = 0;

                // Wir lesen maximal 50 Zeilen
                while ((line = reader.readLine()) != null && linesRead < 50)
                {
                    linesRead++;
                    
                    // BOM entfernen (unsichtbares Zeichen am Dateianfang)
                    if (linesRead == 1 && line.startsWith("\uFEFF")) {
                        line = line.substring(1);
                    }

                    line = line.trim();
                    if (line.isEmpty()) continue;

                    // --- HIER WAR DER FEHLER KORRIGIERT ---
                    // Wir entfernen NUR echte HTML-Kommentar-Tags am Anfang oder Ende
                    if (line.endsWith("-->")) {
                      if(line.length() >= 3) line = line.substring(0, line.length() - 3).trim();
                  }
                    // --------------------------------------

                    // WICHTIG: Wir prüfen JEDE Zeile, die ein "=" enthält
                    if (line.contains("=")) {
                         String[] tempArray = line.split("=", 2);
                         if (tempArray.length == 2) {
                             String key = tempArray[0].trim().toUpperCase();
                             String value = tempArray[1].trim();

                             switch(key) {
                                 case "AUTOR": author = value; break;
                                 case "NAME": name = value; break;
                                 case "VERSION": fileVersion = value; break;
                                 case "TYPE": templateTypeString = value; break;
                                 case "PASSWORT": password = value; break;
                                 case "FILETYPES": fileTypes = value; break;
                                 case "DEFAULTEXT": defaultText = value; break;
                             }
                         }
                    }
                }

                if (name == null || templateTypeString == null) {
                    DOK_VORLAGE_CONTROLLER_LOGGER.error("Ungültiges Template-Format: Name oder Typ fehlt. (Gelesen bis Zeile " + linesRead + ")");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Import Fehler");
                    alert.setHeaderText("Ungültiges Format");
                    alert.setContentText("Die Datei enthält keine gültigen Metadaten (Name, Type) im Header.");
                    alert.showAndWait();
                    return;
                }

                byte[] blobConverter = convertFileContentToBlob(fPath);
                Blob blobFile = new SerialBlob(blobConverter);
                kundenverwaltung.model.Vorlagearten type = getTemplateType.getTemplateType(templateTypeString);

                // Update-Check
                Vorlage existingTemplate = null;
                for (Vorlage v : templatesArrayList) {
                    if (v.getName().equalsIgnoreCase(name) && v.getTemplateType() == type) {
                        existingTemplate = v;
                        break;
                    }
                }

                if (existingTemplate != null)
                {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Vorlage existiert bereits");
                    alert.setHeaderText("Eine Vorlage mit dem Namen '" + name + "' existiert bereits.");
                    alert.setContentText("Möchten Sie die vorhandene Vorlage aktualisieren (Überschreiben) oder eine neue Kopie anlegen?");
                    
                    ButtonType buttonUpdate = new ButtonType("Aktualisieren");
                    ButtonType buttonNew = new ButtonType("Als Neu anlegen");
                    ButtonType buttonCancel = new ButtonType("Abbrechen", ButtonType.CANCEL.getButtonData());
                    alert.getButtonTypes().setAll(buttonUpdate, buttonNew, buttonCancel);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == buttonUpdate)
                    {
                        Vorlage updateModel = new Vorlage(
                            existingTemplate.getVorlageId(),
                            type, name, author, fileVersion, fileTypes, defaultText,
                            Integer.parseInt(password), aktiv, blobFile
                        );
                        vorlageDAOimpl.update(updateModel);
                        DOK_VORLAGE_CONTROLLER_LOGGER.info("Template '{}' wurde aktualisiert.", name);
                    }
                    else if (result.isPresent() && result.get() == buttonNew)
                    {
                        new Vorlage(type, name + " (Kopie)", author, fileVersion, fileTypes, defaultText,
                                Integer.parseInt(password), aktiv, blobFile);
                        DOK_VORLAGE_CONTROLLER_LOGGER.info("Template als Kopie angelegt.");
                    }
                }
                else
                {
                    new Vorlage(type, name, author, fileVersion, fileTypes, defaultText,
                            Integer.parseInt(password), aktiv, blobFile);
                    DOK_VORLAGE_CONTROLLER_LOGGER.info("Neues Template '{}' angelegt.", name);
                }

                initialize(); // Tabelle neu laden

            } catch (Exception e)
            {
                e.printStackTrace();
                DOK_VORLAGE_CONTROLLER_LOGGER.error("Fehler beim Importieren: " + e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setHeaderText("Fehler beim Import");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }
}