package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class provides a connection to an old SQL database.
 *
 * @Author Florian-PC
 * @Date 03.11.2017
 */
public final class AlteDbSQLConnection
{
    private static Connection conDbOld;

    // Private constructor to prevent instantiation
    private AlteDbSQLConnection()
    {
        // Prevent instantiation
    }

    /**
     * This method returns a connection to the old SQL database.
     *
     * @param dbAddress the address of the database server
     * @param dbPort the port of the database server
     * @param dbPass the password for the database user
     * @param dbUser the username for the database
     * @param dbName the name of the database
     * @return a Connection object to the old SQL database
     */
    public static Connection getOldSQLConnection(String dbAddress, String dbPort, String dbPass,
                                                  String dbUser, String dbName)
    {
        try
        {
            if (conDbOld == null || conDbOld.isClosed())
            {
                Class.forName("org.mariadb.jdbc.Driver");
                conDbOld = DriverManager
                        .getConnection("jdbc:mariadb://" + dbAddress + ":" + dbPort + "/" + dbName,
                                dbUser, dbPass);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Verbindung nicht moglich");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (ClassNotFoundException e)
        {
            System.out.println("Treiber nicht gefunden: " + e);

        }
        return conDbOld;
    }
}
