package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import kundenverwaltung.controller.MainController;
import kundenverwaltung.toolsandworkarounds.BlowfishEncryption;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

public final class SQLConnection
{

    private static Connection con;

    SQLConnection()
    {
        // Prevent instantiation
    }

    private static boolean sQLConnection()
    {
        try
        {
            String jdbcDriver = "org.mariadb.jdbc.Driver";

            Properties prop = PropertiesFileController.loadDbInfoPropertiesFile();
            String dbName = prop.getProperty("jdbc.dbName");
            String dbPort = prop.getProperty("jdbc.port");
            String dbHostname = prop.getProperty("jdbc.hostname");
            String dbUsername = prop.getProperty("jdbc.username");
            String dbPassword = BlowfishEncryption.decrypt(prop.getProperty("jdbc.password"));

            Class.forName(jdbcDriver);

            con = DriverManager
                    .getConnection("jdbc:mariadb://" + dbHostname + ":" + dbPort + "/" + dbName,
                            dbUsername, dbPassword);

        } catch (SQLException e)
        {
            e.printStackTrace();
            MainController.getInstance().openDbConnectionError();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public static Connection getCon() throws SQLException
    {
        if (con == null || con.isClosed())
        {
            sQLConnection();
        }
        return con;
    }
}
