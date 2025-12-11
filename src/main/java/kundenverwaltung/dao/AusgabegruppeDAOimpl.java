package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.scene.paint.Color;
import kundenverwaltung.model.AusgabeTagZeit;
import kundenverwaltung.model.Ausgabegruppe;

public class AusgabegruppeDAOimpl implements AusgabegruppeDAO
{
  /**
  *
  */
    @Override
    public boolean create(Ausgabegruppe g)
    {
        final String sqlIns  = "INSERT INTO ausgabegruppe (Name, Aktiv, AnzeigeFarbe) VALUES (?,?,?)";
        final String sqlJoin = "INSERT INTO ausgabegruppe_ausgabetagzeit (ausgabeGruppeId, ausgabeTagZeitId) VALUES (?, ?)";

        try (Connection con = SQLConnection.getCon();
             PreparedStatement ps = con.prepareStatement(sqlIns, Statement.RETURN_GENERATED_KEYS))
        {

            ps.setString(1, g.getName());
            ps.setBoolean(2, g.isAktiv());
            ps.setString(3, colorToHexString(g.getAnzeigeFarbe()));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys())
            {
                if (keys.next()) g.setAusgabegruppeId(keys.getInt(1));
            }

            if (g.getAusgabeTagZeiten() != null)
            {
                for (AusgabeTagZeit z : g.getAusgabeTagZeiten())
                {
                    try (Connection conLinks = SQLConnection.getCon();
                         PreparedStatement pj = conLinks.prepareStatement(sqlJoin))
                    {
                        pj.setInt(1, g.getAusgabegruppeId());
                        pj.setInt(2, z.getAusgabezeitId());
                        pj.executeUpdate();
                    }
                }
            }
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Ausgabegruppe Einfügen klappt nicht");
            return false;
        }
    }
    /**
    *
    */
    @Override
    public boolean update(Ausgabegruppe g)
    {
        final String sql = "UPDATE ausgabegruppe SET Name=?, Aktiv=?, AnzeigeFarbe=? WHERE ausgabegruppeId=?";
        try (Connection con = SQLConnection.getCon();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1, g.getName());
            ps.setBoolean(2, g.isAktiv());
            ps.setString(3, colorToHexString(g.getAnzeigeFarbe()));
            ps.setInt(4, g.getAusgabegruppeId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Ausgabegruppe Update klappt nicht");
            return false;
        }
    }
    /**
    *
    */
    @Override
    public boolean delete(Ausgabegruppe g)
    {
        final String sql = "DELETE FROM ausgabegruppe WHERE ausgabegruppeId = ?";
        try (Connection con = SQLConnection.getCon();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, g.getAusgabegruppeId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Ausgabegruppe Löschen klappt nicht");
            return false;
        }
    }
    /**
    *
    */
    @Override
    public Ausgabegruppe read(int id)
    {
        final String sqlGrp  = "SELECT * FROM ausgabegruppe WHERE ausgabegruppeId = ?";
        final String sqlLink = "SELECT * FROM ausgabegruppe_ausgabetagzeit WHERE ausgabegruppeId = ?";

        try (Connection con = SQLConnection.getCon();
             PreparedStatement pg = con.prepareStatement(sqlGrp))
        {

            pg.setInt(1, id);
            try (ResultSet rs = pg.executeQuery())
            {
                if (!rs.next()) return null;

                String name = rs.getString("name");
                boolean aktiv = rs.getBoolean("aktiv");
                String colorStr = rs.getString("anzeigeFarbe");
                Color color;
                try
                {
                  color = (colorStr != null && !colorStr.trim().isEmpty()) ? Color.web(colorStr) : Color.WHITE; }
                catch (Exception ex)
                { color = Color.WHITE;
                }

                ArrayList<AusgabeTagZeit> zeiten = new ArrayList<>();
                // separater Connection-Handle für die Verknüpfungen
                try (Connection conLinks = SQLConnection.getCon();
                     PreparedStatement pl = conLinks.prepareStatement(sqlLink))
                {
                    pl.setInt(1, id);
                    try (ResultSet rl = pl.executeQuery())
                    {
                        AusgabeTagZeitDAO zeitDAO = new AusgabeTagZeitDAOimpl();
                        while (rl.next())
                        {
                            zeiten.add(zeitDAO.read(rl.getInt("ausgabeTagZeitId")));
                        }
                    }
                }
                return new Ausgabegruppe(id, name, aktiv, color, zeiten);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Ausgabegruppe lesen klappt nicht");
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
    /**
    *
    */
    @Override
    public ArrayList<Ausgabegruppe> readAll()
    {
        final ArrayList<Ausgabegruppe> list = new ArrayList<>();
        final String sqlGrp  = "SELECT * FROM ausgabegruppe";
        final String sqlLink = "SELECT * FROM ausgabegruppe_ausgabetagzeit WHERE ausgabegruppeId = ?";

        try (Connection con = SQLConnection.getCon();
             PreparedStatement pg = con.prepareStatement(sqlGrp);
             ResultSet rs = pg.executeQuery())
        {

            while (rs.next())
            {
                int id = rs.getInt("ausgabegruppeId");
                String name = rs.getString("Name");
                boolean aktiv = rs.getBoolean("aktiv");
                String colorStr = rs.getString("anzeigeFarbe");
                Color color;
                try
                { color = (colorStr != null && !colorStr.trim().isEmpty()) ? Color.web(colorStr) : Color.WHITE;
                }
                catch (Exception ex)
                { color = Color.WHITE; }

                ArrayList<AusgabeTagZeit> zeiten = new ArrayList<>();
                // separater Connection-Handle: verhindert „Connection is closed“
                try (Connection conLinks = SQLConnection.getCon();
                     PreparedStatement pl = conLinks.prepareStatement(sqlLink))
                {
                    pl.setInt(1, id);
                    try (ResultSet rl = pl.executeQuery())
                    {
                        AusgabeTagZeitDAO zeitDAO = new AusgabeTagZeitDAOimpl();
                        while (rl.next())
                        {
                            zeiten.add(zeitDAO.read(rl.getInt("ausgabeTagZeitId")));
                        }
                    }
                }

                list.add(new Ausgabegruppe(id, name, aktiv, color, zeiten));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Ausgabegruppen lesen klappt nicht");
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return list;
    }
    /**
    *
    */
    @Override
    public boolean migrate(Connection conOldDb, Connection conNewdDb)
    {
      return false; }

    private String colorToHexString(Color c)
    {
        if (c == null) return "#FFFFFF";
        return String.format("#%02X%02X%02X",
            (int) Math.round(c.getRed() * 255),
            (int) Math.round(c.getGreen() * 255),
            (int) Math.round(c.getBlue() * 255));
    }
}
