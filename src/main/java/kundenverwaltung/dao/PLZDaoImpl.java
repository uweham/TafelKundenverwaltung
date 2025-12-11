package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import kundenverwaltung.model.PLZ;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class PLZDaoImpl implements PLZDAO
{

    private String databaseName = PropertiesFileController.getDbName();
    /**
     * Creates a new PLZ entry in the database.
     *
     * @param plz the PLZ object to be created
     * @return true if the creation was successful, false otherwise
     */
    @Override
    public boolean create(PLZ plz)
    {
        String idAbfrage = "Select AUTO_INCREMENT "
    +
                "FROM INFORMATION_SCHEMA.TABLES "
    +
                "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
    +
                "AND TABLE_NAME = 'plz'";


        String sql = "INSERT INTO plz("
                + "plz,"
                + "Ort) "
                + "VALUES(?,?)";
        try
        {
            Connection con =  SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int plzId = count.getInt(1);
            plz.setPlzId(plzId);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, plz.getPlz());
            smt.setString(2, plz.getOrt());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("PLZ Einfügen Klappt nicht");
        }

        return false;
    }
    /**
     * Creates a new PLZ entry in the database and returns its ID.
     *
     * @param plz the PLZ object to be created
     * @return the ID of the created PLZ, or -1 if creation failed
     */
    public int createAndGetId(PLZ plz)
    {



        String idAbfrage = "Select AUTO_INCREMENT "
        +
                "FROM INFORMATION_SCHEMA.TABLES "
        +
                "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
        +
                "AND TABLE_NAME = 'plz'";


        String sql = "INSERT INTO plz("
                + "plz,"
                + "Ort) "
                + "VALUES(?,?)";
        try
        {
            Connection con =  SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int plzId = count.getInt(1);
            plz.setPlzId(plzId);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, plz.getPlz());
            smt.setString(2, plz.getOrt());
            smt.executeUpdate();
            smt.close();
            return plzId;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("PLZ Einfügen Klappt nicht");
        }

        return -1;
    }
    /**
     * Updates an existing PLZ entry in the database.
     *
     * @param plz the PLZ object to be updated
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean update(PLZ plz)
    {
        String sql = "UPDATE plz "
    +

                "SET plz = ? ,"
                +
                "ort = ? "
                +
                "WHERE plzId = ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, plz.getPlz());
            smt.setString(2, plz.getOrt());
            smt.setInt(3, plz.getPlzId());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("PLZ Update Klappt nicht");
        }
        return false;
    }
    /**
     * Deletes an existing PLZ entry from the database.
     *
     * @param plz the PLZ object to be deleted
     * @return true if the deletion was successful, false otherwise
     */
    @Override
    public boolean delete(PLZ plz)
    {
        String sql = "Delete From plz Where plzId= ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, plz.getPlzId());
            smt.executeUpdate();
            smt.close();

            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("PLZ Loeschen Klappt nicht");
        }
        return false;
    }
    /**
     * Reads a PLZ entry from the database.
     *
     * @param plzId the ID of the PLZ to be read
     * @return the PLZ object, or null if the read operation failed
     */
    @Override
    public PLZ read(int plzId)
    {
        String sql = "SELECT * FROM plz WHERE plzId = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, plzId);
            ResultSet plzResult = smt.executeQuery();
            plzResult.next();
            String plz = plzResult.getString("plz");
            String ort = plzResult.getString("ort");
            smt.close();

            PLZ plzObjekt = new PLZ(plzId, plz, ort);

            return plzObjekt;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("PLZ lesen klappt nicht");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
    /**
     * Reads all PLZ entries from the database.
     *
     * @return an ArrayList of PLZ objects, or null if the read operation failed
     */
    @Override
    public ArrayList<PLZ> readAll()
    {
        ArrayList<PLZ> allePLZ = new ArrayList<>();

        String sql = "SELECT * FROM plz";

        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            ResultSet plzResult = smt.executeQuery();
            while (plzResult.next())
            {
                int plzId = plzResult.getInt("plzId");
                String plz = plzResult.getString("plz");
                String ort = plzResult.getString("ort");
                PLZ plzobjekt = new PLZ(plzId, plz, ort);
                allePLZ.add(plzobjekt);
            }
            smt.close();
            return allePLZ;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
    /**
     * Migrates PLZ entries from an old database to a new database.
     *
     * @param conOldDb the connection to the old database
     * @param conNewdDb the connection to the new database
     * @return false if the migration failed, true otherwise
     */
    @Override
    public boolean migrate(Connection conOldDb, Connection conNewdDb)
    {
        String sqlRead = "SELECT * FROM lita_postleitzahlen_zuordnung";
        String sql = "INSERT INTO plz("
                + "plzId,"
                + "plz,"
                + "Ort) "
                + "VALUES(?,?,?)";

        try
        {
            //Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
            //Connection con =  SQLConnection.getCon();               //neue DB

            PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
            ResultSet plzResult = smt.executeQuery();

            while (plzResult.next())
            {
                int plzId = plzResult.getInt("postleitzahl_Id");
                String plz = plzResult.getString("postleitzahl");
                String ort = plzResult.getString("ort");

                try
                {
                    PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
                    smt2.setInt(1, plzId);
                    smt2.setString(2, plz);
                    smt2.setString(3, ort);
                    smt2.executeUpdate();
                    smt2.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                    System.out.println("PLZ Migrieren klappt nicht");
                }
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
