package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kundenverwaltung.model.Bescheid;
import kundenverwaltung.model.Bescheidart;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public class BescheidDAOimpl implements BescheidDAO
{

    private String databaseName = PropertiesFileController.getDbName();
    /**
     * Creates a new Bescheid record in the database.
     * @param bescheid The Bescheid object to be created.
     * @return true if the record was successfully created, false otherwise.
     */
    @Override
    public boolean create(Bescheid bescheid)
    {
        String idAbfrage = "Select AUTO_INCREMENT "
    +
                "FROM INFORMATION_SCHEMA.TABLES "
    +
                "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
    +
                "AND TABLE_NAME = 'bescheid'";


        String sql = "INSERT INTO bescheid("
                + "personId,"
                + "bescheidArtId,"
                + "gueltigAb,"
                + "gueltigBis)"
                + "VALUES(?,?,?,?)";
        try
        {
            Connection con =  SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int bescheidId = count.getInt(1);
            bescheid.setBescheidId(bescheidId);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, bescheid.getPerson().getPersonId());
            smt.setInt(2, bescheid.getBescheidart().getBescheidartId());
            smt.setDate(3, Date.valueOf(bescheid.getGueltigAb()));
            smt.setDate(4, Date.valueOf(bescheid.getGueltigBis()));
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Bescheid Einfügen Klappt nicht");
        }
        return false;
    }
    /**
     * Updates an existing Bescheid record in the database.
     * @param bescheid The Bescheid object with updated information.
     * @return true if the record was successfully updated, false otherwise.
     */
    @Override
    public boolean update(Bescheid bescheid)
    {
        String sql = "UPDATE bescheid "
    +
                "SET personId = ?, "
            +
                "bescheidArtId = ?, "
            +
                "gueltigAb = ?, "
            +
                "gueltigBis = ? "
            +
                "WHERE bescheidId = ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, bescheid.getPerson().getPersonId());
            smt.setInt(2, bescheid.getBescheidart().getBescheidartId());
            smt.setDate(3, Date.valueOf(bescheid.getGueltigAb()));
            smt.setDate(4, Date.valueOf(bescheid.getGueltigBis()));
            smt.setInt(5, bescheid.getBescheidId());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Bescheid Update Klappt nicht");
        }
        return false;
    }
    /**
     * Deletes a Bescheid record from the database.
     * @param bescheid The Bescheid object to be deleted.
     * @return true if the record was successfully deleted, false otherwise.
     */
    @Override
    public boolean delete(Bescheid bescheid)
    {
        String sql = "Delete From bescheid Where bescheidId= ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, bescheid.getBescheidId());
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
     * Reads a Bescheid record from the database by its ID.
     * @param bescheidId The ID of the Bescheid to be read.
     * @return The Bescheid object if found, null otherwise.
     */
    @Override
    public Bescheid read(int bescheidId)
    {
        String sql = "SELECT * FROM bescheid WHERE bescheidId = ?";
        try
        {
            FamilienmitgliedDAO familienmitglieddao = new FamilienmitgliedDAOimpl();
            BescheidartDAOimpl bescheidartdao = new BescheidartDAOimpl();

            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, bescheidId);
            ResultSet bescheidResult = smt.executeQuery();
            bescheidResult.next();
            Familienmitglied familienmitglied = familienmitglieddao.read(bescheidResult.getInt("personId"));
            Bescheidart bescheidart = bescheidartdao.read(bescheidResult.getInt("bescheidartId"));
            LocalDate gueltigAb = bescheidResult.getDate("gueltigAb").toLocalDate();
            LocalDate gueltigBis = bescheidResult.getDate("gueltigBis").toLocalDate();
            smt.close();

            Bescheid bescheid = new Bescheid(bescheidId, familienmitglied, bescheidart, gueltigAb, gueltigBis);

            return bescheid;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Bescheid lesen klappt nicht");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
    /**
     * Reads all Bescheid records for a given Familienmitglied.
     * @param familienmitglied The Familienmitglied whose Bescheide are to be read.
     * @return An ArrayList of Bescheid objects.
     */
    @Override
    public ArrayList<Bescheid> readAll(Familienmitglied familienmitglied)
    {

        BescheidartDAO bescheidartDAO = new BescheidartDAOimpl();
        ArrayList<Bescheid> alleBescheide = new ArrayList<>();
        String sql = "SELECT * FROM bescheid WHERE bescheid.personId = ?";
        try
        {

            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, familienmitglied.getPersonId());
            ResultSet bescheidResult = smt.executeQuery();

            while (bescheidResult.next())
            {
                int bescheidId = bescheidResult.getInt("bescheidId");
                Bescheidart bescheidart = bescheidartDAO.read(bescheidResult.getInt("bescheidartId"));
                LocalDate gueltigAb = bescheidResult.getDate("gueltigAb").toLocalDate();
                LocalDate gueltigBis = bescheidResult.getDate("gueltigBis").toLocalDate();


                Bescheid bescheid = new Bescheid(bescheidId, familienmitglied, bescheidart, gueltigAb, gueltigBis);
                alleBescheide.add(bescheid);
            }
            smt.close();


            return alleBescheide;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Bescheid lesen klappt nicht");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
    /**
     * Reads all valid Bescheid records for a given Familienmitglied.
     * @param familienmitglied The Familienmitglied whose valid Bescheide are to be read.
     * @return An ArrayList of valid Bescheid objects.
     */
    public ArrayList<Bescheid> readAllGueltige(Familienmitglied familienmitglied)
    {

        BescheidartDAO bescheidartDAO = new BescheidartDAOimpl();
        ArrayList<Bescheid> gueltigeBescheide = new ArrayList<>();
        String sql = "SELECT * FROM bescheid WHERE bescheid.personId = ? AND gueltigAb <= CURRENT_DATE AND gueltigBis >= CURRENT_DATE; ";
        try
        {

            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, familienmitglied.getPersonId());
            ResultSet bescheidResult = smt.executeQuery();

            while (bescheidResult.next())
            {
                int bescheidId = bescheidResult.getInt("bescheidId");
                Bescheidart bescheidart = bescheidartDAO.read(bescheidResult.getInt("bescheidartId"));
                LocalDate gueltigAb = bescheidResult.getDate("gueltigAb").toLocalDate();
                LocalDate gueltigBis = bescheidResult.getDate("gueltigBis").toLocalDate();
                Bescheid bescheid = new Bescheid(bescheidId, familienmitglied, bescheidart, gueltigAb, gueltigBis);
                gueltigeBescheide.add(bescheid);
            }
            smt.close();

            return gueltigeBescheide;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Bescheid lesen klappt nicht");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }


    /**
     * Migrates Bescheid records from an old database to a new database.
     * @param conOldDb The connection to the old database.
     * @param conNewdDb The connection to the new database.
     * @return false (this method is not yet implemented).
     */
    @Override
    public boolean migrate(Connection conOldDb, Connection conNewdDb)
    {
        String sqlRead = "SELECT * FROM lita_bescheide";
        String sql = "INSERT INTO bescheid("
                + "bescheidId,"
                + "personId,"
                + "bescheidArtId,"
                + "gueltigAb,"
                + "gueltigBis)"
                + "VALUES(?,?,?,?,?)";

        try
        {
            //Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
            //Connection con =  SQLConnection.getCon();               //neue DB

            PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
            ResultSet bescheidResult = smt.executeQuery();

            while (bescheidResult.next())
            {
                int bescheidId = bescheidResult.getInt("bescheid_Nr");
                int personId = bescheidResult.getInt("person_nr");
                int bescheidart = bescheidResult.getInt("bescheid");
                Date gueltigAb = bescheidResult.getDate("gueltig_ab");
                Date gueltigBis = bescheidResult.getDate("gueltig_bis");
                try
                {
                    PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
                    smt2.setInt(1, bescheidId);
                    smt2.setInt(2, personId);
                    smt2.setInt(3, bescheidart);
                    smt2.setDate(4, gueltigAb);
                    smt2.setDate(5, gueltigBis);
                    smt2.executeUpdate();
                    smt2.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                    System.out.println("Bescheid Migrieren klappt nicht");
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
    public List<Bescheid> readBescheid(Verteilstelle verteilstelle, String status)
    {
      List<Bescheid> bescheideList = new ArrayList<>();

      String sql = "SELECT ba.name AS bescheidartName, COUNT(DISTINCT f.personId) AS anzahlPersonen "
      +
              "FROM bescheid b "
      +
              "JOIN familienmitglied f ON b.personId = f.personId "
              +
              "JOIN haushalt h ON f.haushaltId = h.kundennummer "
              +
              "JOIN bescheidart ba ON b.bescheidartId = ba.bescheidartId "
              +
              "JOIN verteilstelle v ON h.verteilstellenId = v.verteilstellenId "
              +
              "WHERE v.bezeichnung = ? "
              +
              "AND ((? = 'Gültig' AND b.gueltigAb <= CURRENT_DATE AND b.gueltigBis >= CURRENT_DATE) "
              +
              "OR (? = 'Nicht gültig' AND (b.gueltigAb > CURRENT_DATE OR b.gueltigBis < CURRENT_DATE))) "
              +
              "GROUP BY ba.name";

      try (Connection con = SQLConnection.getCon();
           PreparedStatement smt = con.prepareStatement(sql))
      {

          smt.setString(1, verteilstelle.getName().trim());
          smt.setString(2, status);
          smt.setString(3, status);

          try (ResultSet resultSet = smt.executeQuery())
          {
              while (resultSet.next())
              {
                  String bescheidartName = resultSet.getString("bescheidartName");
                  int anzahlPersonen = resultSet.getInt("anzahlPersonen");

                  // Erstellen Sie die Bescheidart mit dem Namen
                  Bescheidart bescheidart = new Bescheidart(bescheidartName, false);

                  // Erstellen Sie das Bescheid-Objekt
                  Bescheid bescheid = new Bescheid(0, null, anzahlPersonen, bescheidart, null, null); // ID auf 0 setzen, da nicht benötigt
                  bescheideList.add(bescheid);
              }
          }
      } catch (SQLException e)
      {
          e.printStackTrace();
      }

      return bescheideList;
  }
    /**
     */
  public void saveBescheidStatistik(String bescheidartName, int anzahlPersonen)
  {
      String sqlInsert = "INSERT INTO bescheidStatistik (bescheidartName, anzahlPersonen) VALUES (?, ?) "
  +
              "ON DUPLICATE KEY UPDATE anzahlPersonen = anzahlPersonen + ?";

      try (Connection con = SQLConnection.getCon();
           PreparedStatement pstmt = con.prepareStatement(sqlInsert))
      {
          pstmt.setString(1, bescheidartName);
          pstmt.setInt(2, anzahlPersonen);
          pstmt.setInt(3, anzahlPersonen); // Erhöht die Anzahl, wenn die Bescheidart bereits existiert

          pstmt.executeUpdate();
      } catch (SQLException e)
      {
          e.printStackTrace();
          System.out.println("Fehler beim Speichern der Bescheidstatistik: " + e.getMessage());
      }
  }
}
