package kundenverwaltung.toolsandworkarounds;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Sets the paths to the required .properties files.<br>
 *
 * @author Robin Becker
 * @version 1.0
 * @Date 13.08.2018
 */
public class PropertiesFileController
{
    public static final String HOME_DIR = System.getProperty("user.home");
    public static final String PROPERTIES_LOCATION_DATABASE_INFO = HOME_DIR + "/Tafel Kundenverwaltung/DatabaseInfo.properties";
    public static final String PROPERTIES_LOCATION_TAFEL_INFO = HOME_DIR + "/Tafel Kundenverwaltung/TafelInfo.properties";

    /**
     * Reads the DatabaseInfo.properties file with FileInputstream and loads them in a Properties.
     *
     * @return Properties (DatabaseInfo.properties)
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Properties loadDbInfoPropertiesFile()
    {
        Properties prop = new Properties();
        try (InputStream in = new FileInputStream(PROPERTIES_LOCATION_DATABASE_INFO))
        {
            prop.load(in);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return prop;
    }

    /**
     * Reads the TafelInfo.properties file with FileInputstream and loads them in a Properties.
     *
     * @return Properties (TafelInfo.properties)
     * @throws FileNotFoundException
     * @throws IOException
     */

    public static Properties loadTafelInfoPropertiesFile()
    {
        Properties prop = new Properties();
        try (InputStream in = new FileInputStream(PROPERTIES_LOCATION_TAFEL_INFO))
        {
            prop.load(in);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return prop;
    }

    /**
     * Returns the value of the key "jdbc.dbName" from the DatabaseInfo.properties file.
     *
     * @return Database name as a String
     * @throws FileNotFoundException
     * @throws IOException
     */

    public static String getDbName()
    {
        Properties prop = loadDbInfoPropertiesFile();
        return prop.getProperty("jdbc.dbName");
    }

    /**
     * Returns the value of the key "tafel.name" from the TafelInfo.properties file.
     *
     * @return Tafel name as a String
     * @throws FileNotFoundException
     * @throws IOException
     */

    public static String getTafelName()
    {
        Properties prop = loadTafelInfoPropertiesFile();
        return prop.getProperty("tafel.name");
    }

    /**
     * Returns the value of the key "tafel.location" from the TafelInfo.properties file.
     *
     * @return Tafel location as a String
     */
    public static String getTafelLocation()
    {
        Properties prop = loadTafelInfoPropertiesFile();
        return prop.getProperty("tafel.location");
    }
}
