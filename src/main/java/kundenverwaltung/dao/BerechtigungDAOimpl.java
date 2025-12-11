package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import kundenverwaltung.model.Berechtigung;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

public class BerechtigungDAOimpl implements BerechtigungDAO
{

    private String databaseName = PropertiesFileController.getDbName();
    /**
     * Creates a new Berechtigung record in the database.
     * @param berechtigung The Berechtigung object to be created.
     * @return true if the record was successfully created, false otherwise.
     */
    @Override
    public boolean create(Berechtigung berechtigung)
    {
        String idAbfrage = "Select AUTO_INCREMENT "
    +
                "FROM INFORMATION_SCHEMA.TABLES "
    +
                "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
    +
                "AND TABLE_NAME = 'Berechtigung'";



        String sql = "INSERT INTO berechtigung("
                + "name)"
                + "VALUES(?)";
        try
        {
            Connection con =  SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int berechtigungID = count.getInt(1);
            berechtigung.setBerechtigungId(berechtigungID);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, berechtigung.getName());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Berechtigung Einfügen Klappt nicht");
        }
        return false;
    }
    /**
     * Updates an existing Berechtigung record in the database.
     * @param berechtigung The Berechtigung object with updated information.
     * @return true if the record was successfully updated, false otherwise.
     */
    @Override
    public boolean update(Berechtigung berechtigung)
    {
        String sql = "UPDATE berechtigung "
    +
                "SET Name = ? "
    +
                "WHERE BerechtigungId = ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, berechtigung.getName());
            smt.setInt(2, berechtigung.getBerechtigungId());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Berechtigung Update Klappt nicht");
        }
        return false;
    }
    /**
     * Deletes a Berechtigung record from the database.
     * @param berechtigung The Berechtigung object to be deleted.
     * @return true if the record was successfully deleted, false otherwise.
     */
    @Override
    public boolean delete(Berechtigung berechtigung)
    {
        String sql = "Delete From berechtigung Where berechtigungId= ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, berechtigung.getBerechtigungId());
            smt.executeUpdate();
            smt.close();

            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Berechtigung Loeschen Klappt nicht");
        }
        return false;
    }
    /**
     * Reads a Berechtigung record from the database by its ID.
     * @param berechtigungID The ID of the Berechtigung to be read.
     * @return The Berechtigung object if found, null otherwise.
     */
    @Override
    public Berechtigung read(int berechtigungID)
    {
        String sql = "SELECT * FROM berechtigung WHERE BerechtigungID = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, berechtigungID);
            ResultSet berechtigungResult = smt.executeQuery();
            berechtigungResult.next();
            String name = berechtigungResult.getString("name");
            smt.close();

            Berechtigung berechtigung = new Berechtigung(berechtigungID, name);

            return berechtigung;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Berechtigung lesen klappt nicht");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
    /**
     * Migrates Berechtigung records from an old database to a new database.
     * @param conOldDb The connection to the old database.
     * @param conNewdDb The connection to the new database.
     * @return false (this method is not yet implemented).
     */
    @Override
	public boolean migrate(Connection conOldDb, Connection conNewdDb)
    {
        String sqlRead = "SELECT * FROM lita_listen WHERE liste LIKE 'berechtigung' OR liste LIKE 'default'";

        String sql = "INSERT INTO berechtigung("
                + "berechtigungId,"
                + "name)"
                + "VALUES(?,?)";

        try
        {
           // Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
           // Connection con =  SQLConnection.getCon();               //neue DB

            PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
            ResultSet berechtigungResult = smt.executeQuery();

            while (berechtigungResult.next())
            {
                int berechtigungid = berechtigungResult.getInt("nummer");
                String name = berechtigungResult.getString("name");

                try
                {
                    PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
                    smt2.setInt(1, berechtigungid);
                    smt2.setString(2, name);
                    smt2.executeUpdate();
                    smt2.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                    System.out.println("Berechtigung Migrieren klappt nicht");
                }
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }
    /**
     * Retrieves all Berechtigung records from the database.
     * @return An ArrayList of Berechtigung objects.
     */
    @Override
	public ArrayList<Berechtigung> getAllBerechtigungen()
    {
        String sql = "SELECT * FROM berechtigung";
        try
        {
            Connection con = SQLConnection.getCon();
            ArrayList<Berechtigung> berechtigungsliste = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next())
            {
                int berechtigungsId = resultSet.getInt("berechtigungId");
                String name = resultSet.getString("name");

                Berechtigung berechtigungobjekt = new Berechtigung(berechtigungsId, name);

                berechtigungsliste.add(berechtigungobjekt);
            }
            return berechtigungsliste;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Berechtigung lesen klappt nicht");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }

}
