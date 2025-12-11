package kundenverwaltung.controller.admintool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

import javax.sql.rowset.serial.SerialException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import kundenverwaltung.dao.VorlageDAOimpl;
import kundenverwaltung.model.Vorlage;
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

    /**
     * Initializes the controller by loading all templates from the database and setting them in the TableView.
     */
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

    /**
     * This function delete selected template in the database.
     *
     * @Author Richard Kromm
     * @Date 31.08.2018
     */
    @SuppressWarnings("unchecked")
	@FXML public void deleteTemplate()
    {
        @SuppressWarnings("unused")
		Boolean deleteSuccessful;
        Vorlage template = (Vorlage) filesTableView.getSelectionModel().getSelectedItem();
        if (template != null & vorlageDAOimpl.delete(template))
        {
                templateObservableList.remove(template);
                filesTableView.setItems(templateObservableList);
        }
    }

    /**
     * This function disabled/enabled selected template in the database.
     *
     * @Author Richard Kromm
     * @Date 31.08.2018
     */
    @SuppressWarnings("unchecked")
	@FXML public void disabledTemplate()
    {
        Vorlage template = (Vorlage) filesTableView.getSelectionModel().getSelectedItem();
        if (template != null)
        {
            Boolean updateSuccessful = vorlageDAOimpl.update(new Vorlage(template.getVorlageId(),
                    getTemplateType.getTemplateType(getTemplateType.getTemplateString(template.getTemplateType())),
                    template.getName(), template.getAutor(), template.getFileVersion(), template.getFileTypes(),
                    template.getDefaultText(), template.getPasswort(), !template.isAktiv(), template.getDaten()));

            if (updateSuccessful)
            {
                templatesArrayList = vorlageDAOimpl.getAllTemplate();
                templateObservableList = FXCollections.observableArrayList(templatesArrayList);
                filesTableView.setItems(templateObservableList);
            }
        }
    }

    /**
     * This function export a template.
     */
    @FXML public void exportTemplate()
    {
        int selectedTemplateIndex = filesTableView.getSelectionModel().getSelectedIndex();
        if (selectedTemplateIndex >= 0)
        {
            exportTemplate.exportTemplate(templatesArrayList.get(selectedTemplateIndex).getDaten());

        }

    }

    /**
     * FileChooser, enables you to choose a template (html extension File) and should also read the
     * template File and write the name, author and version info into the Table.
     */

    private static final String SEARCH_NAME = "NAME=";
    private static final String SEARCH_VERSION = "VERSION=";
    private static final String SEARCH_TYPE = "TYPE=";
    private static final String SEARCH_FILETYPES = "FILETYPES=";
    private static final String SEARCH_PASSWORD = "PASSWORT=";
    private static final String SEARCH_DEFAULTEXT = "DEFAULTEXT=";
    private static final String SEARCH_AUTHOR = "AUTOR=";


    /**
     * This function converts the template File to a Blob.
     * @param fPath path to file
     * @return Blobfile
     * @throws IOException
     */

    public static byte[] convertFileContentToBlob(String fPath) throws IOException
    {
        byte[] fileContent = null;
        // initialize string buffer to hold contents of file
        StringBuffer fileContentStr = new StringBuffer("");
        BufferedReader reader = null;
        try
        {
            // initialize buffered reader
            reader = new BufferedReader(new FileReader(fPath));
            String line = null;
            // read lines of file
            while ((line = reader.readLine()) != null)
            {
                //append line to string buffer
                fileContentStr.append(line).append("\n");
            }
            // convert string to byte array
            fileContent = fileContentStr.toString().trim().getBytes();
        } catch (IOException e)
        {
            throw new IOException("Unable to convert file to byte array. " + e.getMessage());
        } finally
        {
            if (reader != null)
            {
                reader.close();
            }
        }
        return fileContent;
    }

    /**
     * this function imports a File to Databank.
     */

    @SuppressWarnings("unchecked")
	@FXML protected void importButtonAction()
    {
        String name = null;
        String templateType = null;
        String author = null;
        String fileVersion = null;
        String fileTypes = null;
        String defaultText = null;
        String password = null;
        Boolean aktiv = true;
        @SuppressWarnings("unused")
		Blob daten;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Tafel-Vorlagen", "*.html"));
        String fName = null;
        String fPath = null;

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null)
        {

            filesTableView.getItems().add(selectedFile.getName());
            filesTableView.getItems().add(selectedFile.getAbsolutePath());

            fName = selectedFile.getName();

            fPath = selectedFile.getAbsolutePath();

            DOK_VORLAGE_CONTROLLER_LOGGER.info("File Name: {}\nFile Path: {}", fName, fPath);

            try
            {
                @SuppressWarnings("resource")
				Scanner scanner = new Scanner(selectedFile);
                int lineNumber = 0;
                System.out.println("try startet");

                while (scanner.hasNext() && lineNumber <= 12)
                {

                    System.out.println("while läuft");

                    String line = scanner.nextLine();
                    String[] tempArray = line.split("=");
                    lineNumber++;
                    System.out.println("Number: " + lineNumber);

                    if (line.toUpperCase().indexOf(SEARCH_AUTHOR) > -1)
                    {
                        author = tempArray[1];
                    } else if (line.toUpperCase().indexOf(SEARCH_NAME) > -1)
                    {
                        name = tempArray[1];
                    } else if (line.toUpperCase().indexOf(SEARCH_VERSION) > -1)
                    {
                        fileVersion = tempArray[1];
                    } else if (line.toUpperCase().indexOf(SEARCH_TYPE) > -1)
                    {
                        templateType = tempArray[1];
                    } else if (line.toUpperCase().indexOf(SEARCH_PASSWORD) > -1)
                    {
                        password = tempArray[1];
                    } else if (line.toUpperCase().indexOf(SEARCH_FILETYPES) > -1)
                    {
                        fileTypes = tempArray[1];
                    } else if (line.toUpperCase().indexOf(SEARCH_DEFAULTEXT) > -1)
                    {
                        defaultText = tempArray[1];
                    }

                }

                byte[] blobConverter = convertFileContentToBlob(fPath);

                Blob blobFile = new javax.sql.rowset.serial.SerialBlob(blobConverter);


                @SuppressWarnings("unused")
				Vorlage vorlage =
                        new Vorlage(getTemplateType.getTemplateType(templateType), name, author,
                                fileVersion, fileTypes, defaultText, Integer.parseInt(password),
                                aktiv, blobFile);

                initialize();

            } catch (FileNotFoundException e)
            {
                System.out.println("catch file");
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (SerialException e)
            {
                e.printStackTrace();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        else
        {
            DOK_VORLAGE_CONTROLLER_LOGGER.info("The given file is not valid!");
        }

    }

}
