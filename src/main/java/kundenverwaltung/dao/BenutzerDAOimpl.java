package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import kundenverwaltung.model.Benutzer;
import kundenverwaltung.model.Recht;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public class BenutzerDAOimpl implements BenutzerDAO
{

    private String databaseName = PropertiesFileController.getDbName();
    /**
     * Creates a new Benutzer record in the database.
     * @param benutzer The Benutzer object to create.
     * @return true if the creation was successful, false otherwise.
     */
    @Override
    public boolean create(Benutzer benutzer)
    {
        String idAbfrage = "Select AUTO_INCREMENT "
    +
                "FROM INFORMATION_SCHEMA.TABLES "
    +
                "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
    +
                "AND TABLE_NAME = 'benutzer'";


        String sql = "INSERT INTO benutzer("
                + "Name,"
                + "Passwort,"
                + "Anzeigename,"
                + "lokal) "
                + "VALUES(?,?,?,?)";

            String sqlRechtZuweisung = "INSERT INTO benutzer_Rechte("
                    + "BenutzerId,"
                    + "RechtId)"
                    + "VALUES(?,?)";
        try
        {
            Connection con =  SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int benutzerId = count.getInt(1);
            benutzer.setBenutzerId(benutzerId);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, benutzer.getName());
            smt.setBytes(2, benutzer.getPasswort());
            smt.setString(3, benutzer.getAnzeigename());
            smt.setBoolean(4, benutzer.isLokal());
            smt.executeUpdate();
            smt.close();

            if (benutzer.getRechte() != null)
            {
                for (Recht element : benutzer.getRechte())
                {
                    PreparedStatement smtZeiten = con.prepareStatement(sqlRechtZuweisung);
                    smtZeiten.setInt(1, benutzer.getBenutzerId());
                    smtZeiten.setInt(2, element.getRechtId());
                    smtZeiten.executeUpdate();
                    smtZeiten.close();
                }
            }
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Benutzer Einfügen Klappt nicht");
        }
        return false;
    }
    /**
     * Updates an existing Benutzer record in the database.
     * @param benutzer The Benutzer object to update.
     * @return true if the update was successful, false otherwise.
     */
    @Override
    public boolean update(Benutzer benutzer)
    {
        String sql = "UPDATE benutzer "
    +
                "SET Name = ?, "
    +
                "Passwort = ?, "
    +
                "Anzeigename = ?, "
    +
                "Lokal = ? "
    +
                "WHERE benutzerId = ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, benutzer.getName());
            smt.setBytes(2, benutzer.getPasswort());
            smt.setString(3, benutzer.getAnzeigename());
            smt.setBoolean(4, benutzer.isLokal());
            smt.setInt(5, benutzer.getBenutzerId());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Benutzer Update Klappt nicht");
        }
        return false;
    }
    /**
     * Deletes an existing Benutzer record from the database.
     * @param benutzer The Benutzer object to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    @Override
    public boolean delete(Benutzer benutzer)
    {
        String sql = "Delete From benutzer Where benutzerId= ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, benutzer.getBenutzerId());
            smt.executeUpdate();
            smt.close();

            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Benutzer Loeschen Klappt nicht");
        }
        return false;
    }
    /**
     * Checks if a Benutzer has a specific Recht.
     * @param benutzer The Benutzer object.
     * @param rechtid The ID of the Recht to check.
     * @return true if the Benutzer has the Recht, false otherwise.
     */
    @Override
    public boolean readRecht(Benutzer benutzer, int rechtid)
    {
        String sql = "Select * from benutzer_rechte Where benutzerID = ? AND rechtid = ?";
        boolean ergebnis = false;
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, benutzer.getBenutzerId());
            smt.setInt(2, rechtid);
            ResultSet benutzerrechteresultset = smt.executeQuery();
            if (!benutzerrechteresultset.next())
            {
                ergebnis = false;
            }
            else
            {
                ergebnis = true;
            }

              smt.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Benutzerrechte lesen klappt nicht");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return ergebnis;
    }
    /**
     * Reads a Benutzer record from the database by its ID.
     * @param benutzerId The ID of the Benutzer to read.
     * @return The Benutzer object, or null if not found.
     */
    @Override
    public Benutzer read(int benutzerId)
    {
        String sql = "SELECT * FROM benutzer WHERE benutzerId = ?";
        String sqlZwei = "SELECT * FROM benutzer_Rechte WHERE benutzerId = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, benutzerId);
            ResultSet ausgabegruppeResult = smt.executeQuery();
            ausgabegruppeResult.next();
            String name = ausgabegruppeResult.getString("Name");
            byte[] passwort = ausgabegruppeResult.getBytes("Passwort");
            String anzeigeName = ausgabegruppeResult.getString("Anzeigename");
            boolean lokal = ausgabegruppeResult.getBoolean("lokal");
            smt.close();

            ArrayList<Recht> rechte = new ArrayList<>();
            PreparedStatement smtZwei = con.prepareStatement(sqlZwei);
            smt.setInt(1, benutzerId);
            ResultSet benutzerRechteResult = smt.executeQuery();
            RechteDAO rechteDAO = new RechteDAOimpl();
            while (benutzerRechteResult.next())
            {
                Recht recht = rechteDAO.read(benutzerRechteResult.getInt("RechtId"));
                rechte.add(recht);
            }
            smtZwei.close();
            Benutzer benutzer = new Benutzer(benutzerId, name, passwort, anzeigeName, lokal, rechte);

            return benutzer;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Benutzer lesen klappt nicht");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
    /**
     * Reads all Benutzer records from the database.
     * @return An ArrayList of all Benutzer objects.
     */
    @Override
    public ArrayList<Benutzer> readAll()
    {
        ArrayList<Benutzer> alleBenutzer = new ArrayList<>();

        String sql = "SELECT * FROM benutzer";
        String sqlZwei = "SELECT * FROM benutzer_rechte WHERE benutzerId = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            ResultSet benutzerResult = smt.executeQuery();
            while (benutzerResult.next())
            {
                int id = benutzerResult.getInt("benutzerId");
                String name = benutzerResult.getString("Name");
                byte[] passwort = benutzerResult.getBytes("Passwort");
                String anzeigeName = benutzerResult.getString("Anzeigename");
                boolean lokal = benutzerResult.getBoolean("lokal");


                ArrayList<Recht> rechte = new ArrayList<>();
                PreparedStatement smtZwei = con.prepareStatement(sqlZwei);
                smtZwei.setInt(1, id);
                ResultSet benutzerRechteResult = smtZwei.executeQuery();
                RechteDAO rechteDAO = new RechteDAOimpl();
                while (benutzerRechteResult.next())
                {
                    Recht recht = rechteDAO.read(benutzerRechteResult.getInt("rechtId"));
                    rechte.add(recht);
                }
                smtZwei.close();
                Benutzer benutzer = new Benutzer(id, name, passwort, anzeigeName, lokal, rechte);
                alleBenutzer.add(benutzer);
            }
            smt.close();
            return alleBenutzer;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
    /**
     * Migrates Benutzer records from an old database to a new database.
     * @param conOldDb The connection to the old database.
     * @param conNewdDb The connection to the new database.
     * @return false (this method is not yet implemented).
     */
    @Override
    public boolean migrate(Connection conOldDb, Connection conNewdDb)
    {
        String sqlRead = "SELECT * FROM lita_Benutzer";

        String sql = "INSERT INTO benutzer("
                + "benutzerId,"
                + "Name,"
                + "Passwort,"
                + "Anzeigename,"
                + "lokal) "
                + "VALUES(?,?,?,?,?)";

        try
        {
           // Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
           // Connection con =  SQLConnection.getCon();               //neue DB

        PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
        ResultSet benutzerResult = smt.executeQuery();

        while (benutzerResult.next())
        {
            int benutzernr = benutzerResult.getInt("benutzer_Nr");
            String benutzername = benutzerResult.getString("benutzername");
            byte[] benutzerpasswort = benutzerResult.getBytes("benutzerpasswort");
            String anzeigename = benutzerResult.getString("anzeigename");
            Boolean lokal = benutzerResult.getBoolean("profil");

            try
            {
                PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
                smt2.setInt(1, benutzernr);
                smt2.setString(2, benutzername);
                smt2.setBytes(3, benutzerpasswort);
                smt2.setString(4, anzeigename);
                smt2.setBoolean(5, lokal);
                smt2.executeUpdate();
                smt2.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
                System.out.println("Benutzer Migrieren klappt nicht");
            }
        }

    } catch (SQLException e)
        {
        e.printStackTrace();
    }
        return false;
    }

}
