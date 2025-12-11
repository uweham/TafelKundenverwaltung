package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class VerteilstelleDAOimpl implements VerteilstelleDAO
{
  private String databaseName = PropertiesFileController.getDbName();
  /**
   *.
   */
    @Override
    public boolean create(Verteilstelle verteilstelle)
    {
        String idAbfrage = "Select AUTO_INCREMENT "
    +
                "FROM INFORMATION_SCHEMA.TABLES "
    +
    "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
    +
                "AND TABLE_NAME = 'verteilstelle'";

        String sql = "INSERT INTO verteilstelle("
                + "Bezeichnung,"
                + "Adresse,"
                + "Listennummer) "
                + "VALUES(?,?,?)";
        try
        {
            Connection con =  SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int verteilstelleId = count.getInt(1);
            verteilstelle.setVerteilstellenId(verteilstelleId);
            smtID.close();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, verteilstelle.getBezeichnung());
            smt.setString(2, verteilstelle.getAdresse());
            smt.setInt(3, verteilstelle.getListennummer());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Verteilstelle Einfügen Klappt nicht");
        }
        return false;
    }
    /**
     *.
     */
    @Override
    public boolean update(Verteilstelle verteilstelle)
    {
        String sql = "UPDATE verteilstelle "
    +
                "SET bezeichnung = ?, "
    +
                "adresse = ?, "
    +
                "listennummer = ? "
                +
                "WHERE VerteilstellenId = ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, verteilstelle.getBezeichnung());
            smt.setString(2, verteilstelle.getAdresse());
            smt.setInt(3, verteilstelle.getListennummer());
            smt.setInt(4, verteilstelle.getVerteilstellenId());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Verteilstelle Update Klappt nicht");
        }
        return false;
    }
    /**
     *.
     */
    @Override
    public boolean delete(Verteilstelle verteilstelle)
    {
        String sql = "Delete From verteilstelle Where verteilstellenId= ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, verteilstelle.getVerteilstellenId());
            smt.executeUpdate();
            smt.close();

            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Verteilstelle Loeschen Klappt nicht");
        }
        return false;
    }
    /**
     *.
     */
    @Override
    public Verteilstelle read(int verteilstellenId)
    {
        String sql = "SELECT * FROM verteilstelle WHERE verteilstellenId = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, verteilstellenId);
            ResultSet verteilStellenResult = smt.executeQuery();
            verteilStellenResult.next();
            String bezeichnung = verteilStellenResult.getString("Bezeichnung");
            String adresse = verteilStellenResult.getString("Adresse");
            int listenNummer = verteilStellenResult.getInt("Listennummer");
            smt.close();

            Verteilstelle verteilstelle = new Verteilstelle(verteilstellenId, bezeichnung, adresse, listenNummer);

            return verteilstelle;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Verteilstelle lesen klappt nicht");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
    /**
     *.
     */
    @Override
    public ArrayList<Verteilstelle> readAll()
    {
        ArrayList<Verteilstelle> alleVerteilstellen = new ArrayList<>();
        String sql = "SELECT * FROM verteilstelle";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            ResultSet verteilStellenResult = smt.executeQuery();
            while (verteilStellenResult.next())
            {
                int id = verteilStellenResult.getInt("verteilstellenId");
                String bezeichnung = verteilStellenResult.getString("Bezeichnung");
                String adresse = verteilStellenResult.getString("Adresse");
                int listenNummer = verteilStellenResult.getInt("Listennummer");


                Verteilstelle verteilstelle = new Verteilstelle(id, bezeichnung, adresse, listenNummer);
                alleVerteilstellen.add(verteilstelle);
            }
            smt.close();
            return alleVerteilstellen;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Verteilstelle lesen klappt nicht");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
    /**
     *.
     */
    @Override
    public boolean migrate(Connection conOldDb, Connection conNewdDb)
    {
        String sqlRead = "SELECT * FROM lita_verteilstellen";

        String sql = "INSERT INTO Verteilstelle("
                + "verteilstellenId,"
                + "Bezeichnung,"
                + "Adresse,"
                + "Listennummer) "
                + "VALUES(?,?,?,?)";

        try
        {
            //Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
            //Connection con =  SQLConnection.getCon();               //neue DB

            PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
            ResultSet verteilstellenResult = smt.executeQuery();

            while (verteilstellenResult.next())
            {
                int verteilstellenId = verteilstellenResult.getInt("verteilstellen_Nr");
                String bezeichnung = verteilstellenResult.getString("bezeichnung");
                String adresse = verteilstellenResult.getString("adresse");
                int listennummer = verteilstellenResult.getInt("listennummer");

                try
                {
                    PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
                    smt2.setInt(1, verteilstellenId);
                    smt2.setString(2, bezeichnung);
                    smt2.setString(3, adresse);
                    smt2.setInt(4, listennummer);
                    smt2.executeUpdate();
                    smt2.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                    System.out.println("Verteilstelle Migrieren klappt nicht");
                }
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    /**
     */
    public List<Verteilstelle> getAllVerteilstelle()

    {
        return null;
    }
    /**
     */
    public Verteilstelle readByName(String name)
    {
      Verteilstelle verteilstelle = null;
      String sql = "SELECT * FROM verteilstelle "
                 + "WHERE TRIM(bezeichnung) = TRIM(?) "
                 + "LIMIT 1";
      try (Connection con = SQLConnection.getCon();
           PreparedStatement smt = con.prepareStatement(sql))
      {

          smt.setString(1, name.trim());
          try (ResultSet rs = smt.executeQuery())
          {
              if (rs.next())
              {
                  int id = rs.getInt("verteilstellenId");
                  String bezeichnung = rs.getString("Bezeichnung");
                  String adresse = rs.getString("Adresse");
                  int listennummer = rs.getInt("Listennummer");

                  // Oder du nimmst den 2-Parameter-Konstruktor, wenn du nur Id + bezeichnung brauchst
                  verteilstelle = new Verteilstelle(id, bezeichnung, adresse, listennummer);
              } else
              {
                  System.out.println("Keine Verteilstelle gefunden mit dem Namen: " + name);
              }
          }
      } catch (SQLException e)
      {
          e.printStackTrace();
          System.err.println("SQL Fehler bei readByName: " + e.getMessage());
      }
      return verteilstelle;
  }
}
