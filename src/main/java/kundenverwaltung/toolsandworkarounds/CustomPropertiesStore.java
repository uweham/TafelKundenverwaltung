package kundenverwaltung.toolsandworkarounds;

import static kundenverwaltung.toolsandworkarounds.PropertiesFileController.PROPERTIES_LOCATION_DATABASE_INFO;
import static kundenverwaltung.toolsandworkarounds.PropertiesFileController.PROPERTIES_LOCATION_TAFEL_INFO;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

public class CustomPropertiesStore extends Properties
{
    private static final long serialVersionUID = 1L;


    /**
     *.
     */
    @Override public void store(OutputStream out, String comments) throws IOException
    {
        customStore0(new BufferedWriter(new OutputStreamWriter(out, "8859_1")), comments, true);
    }


    //Override to stop '/' or ':' chars from being replaced by not called
    //saveConvert(key, true, escUnicode)
    private void customStore0(BufferedWriter bw, String comments, boolean escUnicode)
            throws IOException
    {
        bw.write("#" + comments);
        bw.newLine();
        bw.write("#" + new Date().toString());
        bw.newLine();
        synchronized (this)
        {
            for (@SuppressWarnings("rawtypes")
			Enumeration e = keys(); e.hasMoreElements(); )
            {
                String key = (String) e.nextElement();
                String val = (String) get(key);
                // Commented out to stop '/' or ':' chars being replaced
                //key = saveConvert(key, true, escUnicode);
                //val = saveConvert(val, false, escUnicode);
                bw.write(key + "=" + val);
                bw.newLine();
            }
        }
        bw.flush();
    }

    public static CustomPropertiesStore loadDatabasePropertiesFileCustomStore() throws Exception
    {
        CustomPropertiesStore prop = new CustomPropertiesStore();
        InputStream in = new FileInputStream(PROPERTIES_LOCATION_DATABASE_INFO);
        prop.load(in);
        in.close();
        return prop;
    }

    public static CustomPropertiesStore loadTafelInfoPropertiesFileCustomStore() throws Exception
    {
        CustomPropertiesStore prop = new CustomPropertiesStore();
        InputStream in = new FileInputStream(PROPERTIES_LOCATION_TAFEL_INFO);
        prop.load(in);
        in.close();
        return prop;
    }


}
