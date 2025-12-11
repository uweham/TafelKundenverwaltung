package kundenverwaltung.toolsandworkarounds;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;



import javafx.stage.FileChooser;

@SuppressWarnings("serial")
public class ExportTemplate extends Component
{
    @SuppressWarnings("unused")
	private static final String DIALOG_TITEL_SAVE_AS = "Speichern unter...";

    @SuppressWarnings("unused")
	private static final int FIRST_POSITION = 0;
    private static final int SECOND_POSITION = 1;
    private static final int BYTE_BUFFER = 262144;



    /**
     * This function exports a Blob File from database and converts it to Html File.
     * @param blobTemplate selected Template File from Database
     */

    public void exportTemplate(Blob blobTemplate)
    {
        try
        {
            Blob blobFile = blobTemplate;
            @SuppressWarnings("unused")
			byte[] blobData = blobFile.getBytes(SECOND_POSITION, (int) blobFile.length());

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("html Datei", "*.html"));

            File blobinFile = new File(fileChooser.showSaveDialog(null).getAbsolutePath());

            FileOutputStream fosHtml = new FileOutputStream(blobinFile);

            InputStream input = blobFile.getBinaryStream();
            byte[] buffer = new byte[BYTE_BUFFER];
            while (input.read(buffer) > 0)
            {
                fosHtml.write(buffer);
            }

            input.close();
            fosHtml.close();


        } catch (IOException ioException)
        {
            ioException.printStackTrace();
        } catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }


    }

}

