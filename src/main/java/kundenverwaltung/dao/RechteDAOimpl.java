package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import kundenverwaltung.model.Recht;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public class RechteDAOimpl implements RechteDAO
{

    private String databaseName = PropertiesFileController.getDbName();
    /**
     * Creates a new Recht entry in the database.
     *
     * @param recht the Recht object to be created
     * @return true if the creation was successful, false otherwise
     */
    @Override
    public boolean create(Recht recht)
    {
        String idAbfrage = "Select AUTO_INCREMENT "
    +
                "FROM INFORMATION_SCHEMA.TABLES "
    +
                "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
    +
                "AND TABLE_NAME = 'recht'";


        String sql = "INSERT INTO recht("
                + "Name,"
                + "Beschreibung) "
                + "VALUES(?,?)";
        try
        {
            Connection con =  SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            System.out.println(smtID.toString());
            count.next();
            int rechtId = count.getInt(1);
            recht.setRechtId(rechtId);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, recht.getName());
            smt.setString(2, recht.getBeschreibung());
            System.out.println(smt);
            smt.executeUpdate();

            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Recht Einfügen Klappt nicht");
        }
        return false;
    }
    /**
     * Updates an existing Recht entry in the database.
     *
     * @param recht the Recht object to be updated
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean update(Recht recht)
    {
        String sql = "UPDATE recht "
    +
                "SET Name = ?, "
    +
                "Beschreibung = ? "
    +
                "WHERE RechtId = ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, recht.getName());
            smt.setString(2, recht.getBeschreibung());
            smt.setInt(3, recht.getRechtId());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Recht Update Klappt nicht");
        }
        return false;
    }
    /**
     * Deletes an existing Recht entry from the database.
     *
     * @param recht the Recht object to be deleted
     * @return true if the deletion was successful, false otherwise
     */
    @Override
    public boolean delete(Recht recht)
    {
        String sql = "Delete From recht Where rechtId= ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, recht.getRechtId());
            smt.executeUpdate();
            smt.close();

            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Bescheid Loeschen Klappt nicht");
        }
        return false;
    }
    /**
     * Reads a Recht entry from the database.
     *
     * @param rechteId the ID of the Recht to be read
     * @return the Recht object, or null if the read operation failed
     */
    @Override
    public Recht read(int rechteId)
    {
        String sql = "SELECT * FROM recht WHERE RechtId = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, rechteId);
            ResultSet rechteResult = smt.executeQuery();
            rechteResult.next();
            String name = rechteResult.getString("Name");
            String beschreibung = rechteResult.getString("Beschreibung");
            smt.close();

            Recht recht = new Recht(rechteId, name, beschreibung);

            return recht;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Recht lesen klappt nicht");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
    /**
     * Migrates Recht entries from an old database to a new database.
     *
     * @param conOldDb the connection to the old database
     * @param conNewdDb the connection to the new database
     * @return false if the migration failed, true otherwise
     */
    @Override
    public boolean migrate(Connection conOldDb, Connection conNewdDb)
    {
        String sqlRead = "SELECT * FROM lita_rechte";
        String sql = "INSERT INTO recht("
                + "rechtId,"
                + "Name,"
                + "Beschreibung) "
                + "VALUES(?,?,?)";

        try
        {
            //Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
            //Connection con =  SQLConnection.getCon();               //neue DB

            PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
            ResultSet rechtResult = smt.executeQuery();

            while (rechtResult.next())
            {
                int rechtId = rechtResult.getInt("rechte_nr");
                String name = rechtResult.getString("name");
                String beschreibung = rechtResult.getString("beschreibung");

                try
                {
                    PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
                    smt2.setInt(1, rechtId);
                    smt2.setString(2, name);
                    smt2.setString(3, beschreibung);
                    smt2.executeUpdate();
                    smt2.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                    System.out.println("Recht Migrieren klappt nicht");
                }
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
