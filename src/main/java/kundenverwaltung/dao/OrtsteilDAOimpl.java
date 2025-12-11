package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import kundenverwaltung.model.Ortsteil;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

public class OrtsteilDAOimpl implements OrtsteilDAO
{

    private String databaseName = PropertiesFileController.getDbName();

    /**
     * Creates a new Ortsteil in the database.
     *
     * @param ortsteil the Ortsteil object to be created
     * @return true if the creation was successful, false otherwise
     */
    @Override
    public boolean create(Ortsteil ortsteil)
    {
        String idAbfrage = "Select AUTO_INCREMENT "
                + "FROM INFORMATION_SCHEMA.TABLES "
                + "WHERE TABLE_SCHEMA = '" + databaseName + "' "
                + "AND TABLE_NAME = 'ortsteil'";

        String sql = "INSERT INTO ortsteil("
                + "name,"
                + "plz) "
                + "VALUES(?,?)";
        try
        {
            Connection con = SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int ortsteilId = count.getInt(1);
            ortsteil.setOrtsteilId(ortsteilId);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, ortsteil.getName());
            smt.setInt(2, ortsteil.getPlz());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Ortsteil Einfügen Klappt nicht");
        }

        return false;
    }

    /**
     * Updates an existing Ortsteil in the database.
     *
     * @param ortsteil the Ortsteil object containing the updated values
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean update(Ortsteil ortsteil)
    {
        String sql = "UPDATE ortsteil "
                + "SET name = ? ,"
                + "plz = ? "
                + "WHERE ortsteilId = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, ortsteil.getName());
            smt.setInt(2, ortsteil.getPlz());
            smt.setInt(3, ortsteil.getOrtsteilId()); // <-- FIX: fehlte vorher
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Ortsteil Update Klappt nicht");
        }
        return false;
    }

    /**
     * Deletes an Ortsteil from the database.
     *
     * @param ortsteil the Ortsteil object to be deleted
     * @return true if the deletion was successful, false otherwise
     */
    @Override
    public boolean delete(Ortsteil ortsteil)
    {
        String sql = "Delete From ortsteil Where ortsteilId= ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, ortsteil.getOrtsteilId());
            smt.executeUpdate();
            smt.close();

            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Ortsteil Loeschen Klappt nicht");
        }
        return false;
    }

    /**
     * Reads an Ortsteil from the database.
     *
     * @param ortsteilId the ID of the Ortsteil to be read
     * @return the Ortsteil object, or null if not found
     */
    @Override
    public Ortsteil read(int ortsteilId)
    {
        String sql = "SELECT * FROM ortsteil WHERE ortsteilId = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, ortsteilId);
            ResultSet ortsteilResult = smt.executeQuery();
            ortsteilResult.next();
            String name = ortsteilResult.getString("name");
            int plz = ortsteilResult.getInt("plz");
            smt.close();

            Ortsteil ortsteil = new Ortsteil(ortsteilId, name, plz);

            return ortsteil;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Ortsteil lesen klappt nicht");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }

    /**
     * Reads all Ortsteil objects for a given PLZ.
     *
     * @param plzId the ID of the PLZ to read Ortsteile for (FK auf plz.plzId)
     * @return an ArrayList of Ortsteil objects, or null if an error occurs
     */
    @Override
    public ArrayList<Ortsteil> readAll(int plzId)
    {
        ArrayList<Ortsteil> alleOrtsteile = new ArrayList<>();

        String sql = "SELECT * FROM ortsteil WHERE plz = ?";

        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, plzId);
            ResultSet plzResult = smt.executeQuery();
            while (plzResult.next())
            {
                int ortsteilId = plzResult.getInt("ortsteilId");
                String name = plzResult.getString("name");
                int plz = plzResult.getInt("plz");
                Ortsteil ortsteil = new Ortsteil(ortsteilId, name, plz);
                alleOrtsteile.add(ortsteil);
            }
            smt.close();
            return alleOrtsteile;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
}
