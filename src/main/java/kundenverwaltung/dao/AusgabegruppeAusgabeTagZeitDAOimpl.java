package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import kundenverwaltung.model.AusgabeTagZeit;
import kundenverwaltung.model.Ausgabegruppe;

public class AusgabegruppeAusgabeTagZeitDAOimpl implements AusgabegruppeAusgabeTagZeitDAO
{
  /**
  *
  */
    @Override
    public boolean ausgabeTagZeitHinzufuegen(Ausgabegruppe g, AusgabeTagZeit z)
    {
        // verhindert Doppelzeilen in der Link-Tabelle (setzt UNIQUE(ausgabeGruppeId, ausgabeTagZeitId) voraus)
        final String sql =
            "INSERT INTO ausgabegruppe_ausgabetagzeit (ausgabeGruppeId, ausgabeTagZeitId) "
        +
            "VALUES (?, ?) "
        +
            "ON DUPLICATE KEY UPDATE ausgabeTagZeitId = VALUES(ausgabeTagZeitId)";
        try (Connection con = SQLConnection.getCon();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, g.getAusgabegruppeId());
            ps.setInt(2, z.getAusgabezeitId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("AusgabeTagZeit-Verknüpfung anlegen fehlgeschlagen");
            return false;
        }
    }
    /**
    *
    */
    @Override
    public boolean ausgabeTagZeitEntfernen(Ausgabegruppe g, AusgabeTagZeit z)
    {
        final String sql = "DELETE FROM ausgabegruppe_ausgabetagzeit WHERE ausgabeGruppeId = ? AND ausgabeTagZeitId = ?";
        try (Connection con = SQLConnection.getCon();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, g.getAusgabegruppeId());
            ps.setInt(2, z.getAusgabezeitId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("AusgabeTagZeit-Verknüpfung entfernen fehlgeschlagen");
            return false;
        }
    }
}
