package kundenverwaltung.toolsandworkarounds;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * BlobToTemplate.java is a tool converting templates from db to functional templates that you can.
 * print out
 *
 * @version 1.0
 * @Author Adam Starobrzanski
 * @Date 14.09.2018
 */

public class BlobToTemplate
{
    @SuppressWarnings("unused")
	private Integer bytebuffer = 262144;
    private String blobString;
    private File blobTempFileOut;
    /**
     *.
     */
    public File getBlobTempFileOut()
    {
        return blobTempFileOut;
    }
    /**
     *.
     */
    public void setBlobTempFileOut(File blobTempFileOut)
    {
        this.blobTempFileOut = blobTempFileOut;
    }



    /**
     * This function converts Blob to *.html (temp file).
     *
     * @param blobFile blob Template from database
     * @return returns temporary html file with placeholders that can be converted into ready filled
     * in template form
     */

    public File convertBlobToTemplate(Blob blobFile) throws IOException
    {
        try
        {
            String blob;
            StringBuffer stringOut = new StringBuffer();
            BufferedReader blobReader =
                    new BufferedReader(new InputStreamReader(blobFile.getBinaryStream()));
            while ((blob = blobReader.readLine()) != null)
            {
                stringOut.append(blob);
            }
            blobReader.close();
            blobString = stringOut.toString();
            System.out.println("stringo: " + blobString);
            File temp = File.createTempFile("tempTemplate", ".html");
            File blobTempFile = new File(temp.getAbsolutePath());
            BufferedWriter blobWriter = new BufferedWriter(new FileWriter(blobTempFile));
            try
            {
                blobWriter.write(blobString);

            } catch (IOException i)
            {
                i.printStackTrace();
            }
            blobWriter.close();
            blobTempFileOut = blobTempFile;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        blobTempFileOut.deleteOnExit();
        return blobTempFileOut;
    }

   /* public File convertBlobToTemplate(Blob blobFile)
    {
        try
        {
            InputStream input = blobFile.getBinaryStream();
            File temp = File.createTempFile("tempTemplate", ".html");
            File blobTempFile = new File(temp.getAbsolutePath());
            BufferedWriter blobWriter = new BufferedWriter(new FileWriter(blobTempFile));
            byte[] buffer = new byte[BYTE_BUFFER];
            while(input.read(buffer) > 0)
            {
                blobWriter.write(buffer);
            }
            BufferedReader blobReader = new BufferedReader(new FileReader());


            *//*FileOutputStream fosHTML = new FileOutputStream(blobTempFile);*//*

            //buffer needs to be this big otherwise it takes way too long
      *//*      InputStream input = blobFile.getBinaryStream();
            byte[] buffer = new byte[BYTE_BUFFER];
            while (input.read(buffer) > 0)
            {
                fosHtml.write(buffer);
            }
            blobTempFileOut = blobTempFile;*//*
 *//*fosHTML.close();*//*
 *//*  input.close();*//*

            temp.deleteOnExit();
            blobReader.close();
            blobWriter.close();

        } catch (IOException e)
        {
            e.printStackTrace();
      *//*  } catch (SQLException e)
        {
            e.printStackTrace();*//*
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return blobTempFileOut;
    }
*/
}


/*
  try
        {

            File temp = File.createTempFile("tempTemplate", ".html");
            File blobTempFile = new File(temp.getAbsolutePath());
            try (BufferedWriter blobWriter = new BufferedWriter(new FileWriter(blobTempFile)))
            {
                blobWriter.write(BYTE_BUFFER);
            }
            /*FileOutputStream fosHTML = new FileOutputStream(blobTempFile);*/

//buffer needs to be this big otherwise it takes way too long
         /*   InputStream input = blobFile.getBinaryStream();
            byte[] buffer = new byte[BYTE_BUFFER];
            while (input.read(buffer) > 0)
            {
                fosHTML.write(buffer);
            }
*/
/*
            blobTempFileOut = blobTempFile;
                    */
/*fosHTML.close();*//*

 */
/*  input.close();*//*


                    temp.deleteOnExit();

                    } catch (IOException e)
                    {
                    e.printStackTrace();
       */
/* } catch (SQLException e)
        {
            e.printStackTrace();*//*

                    }

                    return blobTempFileOut;
                    }
*/

