package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kundenverwaltung.model.Benutzer;
import kundenverwaltung.model.Recht;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public class BenutzerRechteDAOimpl implements BenutzerRechteDAO
{
  /**
   * Adds a Recht to a Benutzer.
   * @param benutzer The Benutzer object to which the Recht is to be added.
   * @param recht The Recht object to be added.
   * @return true if the Recht was successfully added, false otherwise.
   */
    @Override
    public boolean rechtHinzufuegen(Benutzer benutzer, Recht recht)
    {
        String sqlRechtZuweisung = "INSERT INTO benutzer_rechte("
                + "BenutzerId,"
                + "RechtId)"
                + "VALUES(?,?)";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smtZeiten = con.prepareStatement(sqlRechtZuweisung);
            smtZeiten.setInt(1, benutzer.getBenutzerId());
            smtZeiten.setInt(2, recht.getRechtId());
            smtZeiten.executeUpdate();
            smtZeiten.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Recht Einfügen Klappt nicht");
        }
        return false;
    }
    /**
     * Removes a Recht from a Benutzer.
     * @param benutzer The Benutzer object from which the Recht is to be removed.
     * @param recht The Recht object to be removed.
     * @return true if the Recht was successfully removed, false otherwise.
     */
    @Override
    public boolean rechtEntfernen(Benutzer benutzer, Recht recht)
    {
        String sql = "Delete From benutzer_rechte Where BenutzerId= ? AND RechtId = ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, benutzer.getBenutzerId());
            smt.setInt(2, recht.getRechtId());
            smt.executeUpdate();
            smt.close();

            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Recht Entfernen Klappt nicht");
        }
        return false;
    }
    /**
     * Migrates BenutzerRechte records from an old database to a new database.
     * @param conOldDb The connection to the old database.
     * @param conNewdDb The connection to the new database.
     * @return false (this method is not yet implemented).
     */
    @Override
    public boolean migrate(Connection conOldDb, Connection conNewdDb)
    {
        String sqlRead = "SELECT * FROM lita_benutzerrechte";

        String sql = "INSERT INTO benutzer_rechte("
                + "BenutzerId,"
                + "RechtId)"
                + "VALUES(?,?)";

        try
        {
            //Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
            // Connection con =  SQLConnection.getCon();               //neue DB

            PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
            ResultSet benutzerRechteResult = smt.executeQuery();

            while (benutzerRechteResult.next())
            {
                int rechtId = benutzerRechteResult.getInt("rechte_nr");
                int benutzerId = benutzerRechteResult.getInt("benutzer_nr");

                try
                {
                    PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
                    smt2.setInt(1, benutzerId);
                    smt2.setInt(2, rechtId);
                    smt2.executeUpdate();
                    smt2.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                    System.out.println("Benutzerrechte Migrieren klappt nicht");
                }

            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
