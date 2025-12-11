package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Vollmacht;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public class VollmachtDAOimpl implements VollmachtDAO
{

    private String databaseName = PropertiesFileController.getDbName();
    /**
     *.
     */
    @Override


    //Datensatz wird doppelt erstellt?!

    public boolean create(Vollmacht vollmacht)
    {


       //String Server

        String idAbfrage = "Select AUTO_INCREMENT "
        +
                "FROM INFORMATION_SCHEMA.TABLES "
        +
                "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
        +
                "AND TABLE_NAME = 'vollmacht'";


        String sql = "INSERT INTO vollmacht("
                + "HaushaltId,"
                + "bevollmaechtigtePersonId,"
                + "ausgestelltAm,"
                + "ablaufDatum) "
                + "VALUES(?,?,?,?)";

        try
        {
            Connection con =  SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int vollmachtId = count.getInt(1);
            vollmacht.setVollmachtId(vollmachtId);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, vollmacht.getHaushalt().getKundennummer());
            smt.setInt(2, vollmacht.getBevollmaechtigtePerson().getPersonId());
            smt.setDate(3, Date.valueOf(vollmacht.getAusgestelltAm()));
            smt.setDate(4, Date.valueOf(vollmacht.getAblaufDatum()));
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Vollmacht Einfügen Klappt nicht");
        }
        return false;
    }
    /**
     *.
     */
    @Override
    public boolean update(Vollmacht vollmacht)
    {
        String sql = "UPDATE vollmacht "
    +
                "SET haushaltId = ?, "
    +
                "bevollmaechtigtePersonId = ?, "
    +
                "ausgestelltAm = ?, "
    +
                "ablaufDatum = ? "
    +
                "WHERE vollmachtId = ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, vollmacht.getHaushalt().getKundennummer());
            smt.setInt(2, vollmacht.getBevollmaechtigtePerson().getPersonId());
            smt.setDate(3, Date.valueOf(vollmacht.getAusgestelltAm()));
            smt.setDate(4, Date.valueOf(vollmacht.getAblaufDatum()));
            smt.setInt(5, vollmacht.getVollmachtId());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Vollmacht Update Klappt nicht");
        }
        return false;
    }
    /**
     *.
     */
    @Override
    public boolean delete(Vollmacht vollmacht)
    {
        String sql = "Delete From vollmacht Where vollmachtId= ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, vollmacht.getVollmachtId());
            smt.executeUpdate();
            smt.close();

            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Vollmacht Loeschen Klappt nicht");
        }
        return false;
    }
    /**
     *.
     */
    @Override
    public Vollmacht read(int vollmachtId)
    {
        String sql = "SELECT * FROM Vollmacht WHERE vollmachId = ?";
        try
        {
            HaushaltDAO haushaltdao = new HaushaltDAOimpl();
            FamilienmitgliedDAO familienmitglieddao = new FamilienmitgliedDAOimpl();

            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, vollmachtId);
            ResultSet vollmachtResult = smt.executeQuery();
            vollmachtResult.next();
            Haushalt haushalt = haushaltdao.read(vollmachtResult.getInt("haushalt"));
            Familienmitglied familienmitglied = familienmitglieddao.read(vollmachtResult.getInt("familienmitgliedId"));
            LocalDate ausgestellAm = vollmachtResult.getDate("ausgestelltAm").toLocalDate();
            LocalDate ablaufDatum = vollmachtResult.getDate("ablaufDatum").toLocalDate();
            smt.close();

            Vollmacht vollmacht = new Vollmacht(vollmachtId, haushalt, familienmitglied, ausgestellAm, ablaufDatum);

            return vollmacht;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Vollmacht lesen klappt nicht");
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
    public ArrayList<Vollmacht> readAllGueltige(int haushaltsId)
    {

        String sql = "SELECT * FROM vollmacht WHERE haushaltId = " + haushaltsId + " AND vollmacht.ausgestelltAm <= CURRENT_DATE AND vollmacht.ablaufDatum >= CURRENT_DATE";
        try
        {
            Connection con = SQLConnection.getCon();
            ArrayList<Vollmacht> vollmachtenListe = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet vollmachtResult = stmt.executeQuery(sql);

            while (vollmachtResult.next())
            {
                int vollmachtid = vollmachtResult.getInt("vollmachtId");
                Haushalt haushalt = new HaushaltDAOimpl().read(haushaltsId);
                Familienmitglied bevollmaechtiger = new FamilienmitgliedDAOimpl().read(vollmachtResult.getInt("bevollmaechtigtePersonId"));
                LocalDate ausgestellAm = vollmachtResult.getDate("ausgestelltAm").toLocalDate();
                LocalDate ablaufDatum = vollmachtResult.getDate("ablaufDatum").toLocalDate();

                Vollmacht vollmachtobjekt = new Vollmacht(vollmachtid, haushalt, bevollmaechtiger, ausgestellAm, ablaufDatum);

                vollmachtenListe.add(vollmachtobjekt);
            }
            return vollmachtenListe;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Nation lesen klappt nicht");
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
	public ArrayList<Vollmacht> getAllVollmachtenById(int haushaltsId)
    {

        String sql = "SELECT * FROM vollmacht WHERE haushaltId = " + haushaltsId;
        try
        {
            Connection con = SQLConnection.getCon();
            ArrayList<Vollmacht> vollmachtenListe = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet vollmachtResult = stmt.executeQuery(sql);

            while (vollmachtResult.next())
            {
                int vollmachtid = vollmachtResult.getInt("vollmachtId");
                Haushalt haushalt = new HaushaltDAOimpl().read(haushaltsId);
                Familienmitglied bevollmaechtiger = new FamilienmitgliedDAOimpl().read(vollmachtResult.getInt("bevollmaechtigtePersonId"));
                LocalDate ausgestellAm = vollmachtResult.getDate("ausgestelltAm").toLocalDate();
                LocalDate ablaufDatum = vollmachtResult.getDate("ablaufDatum").toLocalDate();

                Vollmacht vollmachtobjekt = new Vollmacht(vollmachtid, haushalt, bevollmaechtiger, ausgestellAm, ablaufDatum);

                vollmachtenListe.add(vollmachtobjekt);
            }
            return vollmachtenListe;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Nation lesen klappt nicht");
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
        String sqlRead = "SELECT * FROM lita_vollmacht";
        String sql = "INSERT INTO Vollmacht("
                + "vollmachtId,"
                + "HaushaltId,"
                + "bevollmaechtigtePersonId,"
                + "ausgestelltAm,"
                + "ablaufDatum)"
                + "VALUES(?,?,?,?,?)";

        try
        {
            //Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
            //Connection con =  SQLConnection.getCon();               //neue DB

            PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
            ResultSet vollmachtResult = smt.executeQuery();

            while (vollmachtResult.next())
            {
                int vollmachtId = vollmachtResult.getInt("vollmacht_nr");
                int kundennummer = vollmachtResult.getInt("Kundennummer");
                int personId = vollmachtResult.getInt("person_nr");
                Date ausgestellt = vollmachtResult.getDate("ausgestellt");
                Date ablaufdatum = vollmachtResult.getDate("ablaufdatum");


                try
                {
                    PreparedStatement smt2 = conOldDb.prepareStatement(sql);
                    smt2.setInt(1, vollmachtId);
                    smt2.setInt(2, kundennummer);
                    smt2.setInt(3, personId);
                    smt2.setDate(4, ausgestellt);
                    smt2.setDate(5, ablaufdatum);
                    smt2.executeUpdate();
                    smt2.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                    System.out.println("Vollmacht Migrieren klappt nicht");
                }
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
