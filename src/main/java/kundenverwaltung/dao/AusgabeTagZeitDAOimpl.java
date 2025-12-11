package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;

import kundenverwaltung.model.AusgabeTagZeit;

public class AusgabeTagZeitDAOimpl implements AusgabeTagZeitDAO
{
  /**
  *
  */
    @Override
    public boolean create(AusgabeTagZeit a)
    {
        final String sql = ""
            + "INSERT INTO ausgabetagzeit (ausgabeTag, startZeit, endZeit) "
            + "VALUES (?, ?, ?) "
            + "ON DUPLICATE KEY UPDATE "
            + "  ausgabeTagZeitId = LAST_INSERT_ID(ausgabeTagZeitId)";

        try (Connection con = SQLConnection.getCon();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {

            ps.setInt(1, a.getAusgabetag().getTagId());
            ps.setTime(2, Time.valueOf(a.getStartzeit()));
            ps.setTime(3, Time.valueOf(a.getEndzeit()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys())
            {
                if (rs.next())
                {
                    a.setAusgabezeitId(rs.getInt(1));
                } else
                {
                    // Fallback – sollte durch LAST_INSERT_ID eigentlich nicht nötig sein
                    try (PreparedStatement q = con.prepareStatement(
                        "SELECT ausgabeTagZeitId FROM ausgabetagzeit WHERE ausgabeTag=? AND startZeit=? AND endZeit=?"))
                    {
                        q.setInt(1, a.getAusgabetag().getTagId());
                        q.setTime(2, Time.valueOf(a.getStartzeit()));
                        q.setTime(3, Time.valueOf(a.getEndzeit()));
                        try (ResultSet r = q.executeQuery())
                        {
                            if (r.next()) a.setAusgabezeitId(r.getInt(1));
                        }
                    }
                }
            }
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("AusgabeTagZeit Einfügen klappt nicht");
            return false;
        }
    }
    /**
    *
    */
    @Override
    public boolean update(AusgabeTagZeit a)
    {
        final String sql = "UPDATE ausgabetagzeit SET ausgabeTag=?, startZeit=?, endZeit=? WHERE ausgabeTagZeitId=?";
        try (Connection con = SQLConnection.getCon();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, a.getAusgabetag().getTagId());
            ps.setTime(2, Time.valueOf(a.getStartzeit()));
            ps.setTime(3, Time.valueOf(a.getEndzeit()));
            ps.setInt(4, a.getAusgabezeitId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("AusgabeTagZeit Update klappt nicht");
            return false;
        }
    }
    /**
    *
    */
    @Override
    public boolean delete(AusgabeTagZeit a)
    {
        final String sql = "DELETE FROM ausgabetagzeit WHERE ausgabeTagZeitId = ?";
        try (Connection con = SQLConnection.getCon();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, a.getAusgabezeitId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("AusgabeTagZeit Löschen klappt nicht");
            return false;
        }
    }
    /**
    *
    */
    @Override
    public AusgabeTagZeit read(int id)
    {
        final String sql = "SELECT * FROM ausgabetagzeit WHERE ausgabeTagZeitId = ?";
        try (Connection con = SQLConnection.getCon();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    int tag = rs.getInt("ausgabeTag");
                    LocalTime start = rs.getTime("startZeit").toLocalTime();
                    LocalTime end   = rs.getTime("endZeit").toLocalTime();
                    return new AusgabeTagZeit(id, tag, start, end);
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("AusgabeTagZeit lesen klappt nicht");
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
    public ArrayList<AusgabeTagZeit> readAll()
    {
        final ArrayList<AusgabeTagZeit> list = new ArrayList<>();
        final String sql = "SELECT * FROM ausgabetagzeit";
        try (Connection con = SQLConnection.getCon();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {
            while (rs.next())
            {
                int id   = rs.getInt("ausgabeTagZeitId");
                int tag  = rs.getInt("ausgabeTag");
                LocalTime start = rs.getTime("startZeit").toLocalTime();
                LocalTime end   = rs.getTime("endZeit").toLocalTime();
                list.add(new AusgabeTagZeit(id, tag, start, end));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Ausgabetagzeiten lesen klappt nicht");
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
    { return false; }
}
