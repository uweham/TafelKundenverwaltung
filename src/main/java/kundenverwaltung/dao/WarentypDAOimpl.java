package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import kundenverwaltung.model.Warentyp;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

public class WarentypDAOimpl implements WarentypDAO
{

    private String databaseName = PropertiesFileController.getDbName();
    /**
     *.
     */
    @Override
    public boolean create(Warentyp warentyp)
    {
        String idAbfrage = "Select AUTO_INCREMENT "
    +
                "FROM INFORMATION_SCHEMA.TABLES "
    +
                "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
    +
                "AND TABLE_NAME = 'Warentyp'";


        String sql = "INSERT INTO warentyp("
                + "name,"
                + "preisErwachsener,"
                + "preisKinder,"
                + "aktiv)"
                + "VALUES(?,?,?,?)";
        try
        {
            Connection con =  SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int warentypId = count.getInt(1);
            warentyp.setWarentypId(warentypId);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, warentyp.getName());
            smt.setFloat(2, warentyp.getPreisErwachsene());
            smt.setFloat(3, warentyp.getPreisKinder());
            smt.setBoolean(4, warentyp.isAktiv());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Warentyp Einfügen Klappt nicht");
        }
        return false;
    }
    /**
     *.
     */
    @Override
    public boolean update(Warentyp warentyp)
    {
        String sql = "UPDATE warentyp "
                +
                "SET Name = ?, "
                +
                "preisErwachsener = ?, "
                +
                "preisKinder = ?, "
                +
                "naechsterwarentypid = ?, "
                +
                "zuordnungperson = ?, "
                +
                "zuordnungbuchungstext = ?, "
                +
                "warentyplimitanzahl = ?, "
                +
                "warentyplimitart = ?, "
                +
                "warentyplimitabstand = ?, "
                +
                "warentyplimitabstandart = ?, "
                +
                "haushaltspauschale = ?, "
                +
                "deckelbetrag = ?, "
                +
                "minbetrag = ?, "
                +
                "aktiv = ? ,"
                +
                "manuelleberechnung = ? "
                +
                "WHERE warentypId = ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, warentyp.getName());
            smt.setFloat(2, warentyp.getPreisErwachsene());
            smt.setFloat(3, warentyp.getPreisKinder());
            smt.setInt(4, warentyp.getNaechsterWarentypid());
            smt.setInt(5, warentyp.getZuordnungPerson());
            smt.setInt(6, warentyp.getZuordnungBuchungstext());
            smt.setInt(7, warentyp.getWarentyplimitanzahl());
            smt.setInt(8, warentyp.getWarentyplimitart());
            smt.setInt(9, warentyp.getWarentyplimitabstand());
            smt.setInt(10, warentyp.getWarentyplimitabstandart());
            smt.setFloat(11, warentyp.getHaushaltspauschale());
            smt.setFloat(12, warentyp.getDeckelbetrag());
            smt.setFloat(13, warentyp.getMinbetrag());

            smt.setBoolean(14, warentyp.isAktiv());
            smt.setBoolean(15, warentyp.isManuelleBerechnung());
            smt.setInt(16, warentyp.getWarentypId());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Warentyp Update Klappt nicht");
        }
        return false;
    }
    /**
     *.
     */
    @Override
    public boolean delete(Warentyp warentyp)
    {
        String sql = "Delete From warentyp Where warentypId= ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, warentyp.getWarentypId());
            smt.executeUpdate();
            smt.close();

            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Warentyp Loeschen Klappt nicht");
        }
        return false;
    }
    /**
     *.
     */
    @Override
    public Warentyp read(int warentypId)
    {
        String sql = "SELECT * FROM warentyp WHERE WarentypId = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, warentypId);
            ResultSet warentypResult = smt.executeQuery();
            warentypResult.next();
            String name = warentypResult.getString("Name");
            float preisErwachsene = warentypResult.getFloat("preisErwachsener");
            float preisKinder = warentypResult.getFloat("preisKinder");
            Boolean aktiv = warentypResult.getBoolean("aktiv");

            //neu
            float haushaltspauschale = warentypResult.getFloat("haushaltspauschale");
            float deckelbetrag = warentypResult.getFloat("deckelbetrag");
            float minbetrag = warentypResult.getFloat("minbetrag");
            Boolean manuelleberechnung = warentypResult.getBoolean("manuelleberechnung");
            int zuordnungperson = warentypResult.getInt("zuordnungperson");
            int zuordnungbuchungstext = warentypResult.getInt("zuordnungbuchungstext");
            int warentyplimitanzahl = warentypResult.getInt("warentyplimitanzahl");
            int warentyplimitart = warentypResult.getInt("warentyplimitart");
            int warentyplimitabstand = warentypResult.getInt("warentyplimitabstand");
            int warentyplimitabstandart = warentypResult.getInt("warentyplimitabstandart");
            int naechsterwarentypid = warentypResult.getInt("naechsterwarentypid");

            smt.close();

            Warentyp warentyp = new Warentyp(warentypId, name, preisErwachsene, preisKinder, aktiv, haushaltspauschale, deckelbetrag, minbetrag, manuelleberechnung, zuordnungperson, zuordnungbuchungstext, warentyplimitanzahl, warentyplimitart, warentyplimitabstand, warentyplimitabstandart, naechsterwarentypid);


            return warentyp;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Warentyp lesen klappt nicht");
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
    public ArrayList<Warentyp> readAllAktiv()
    {
        ArrayList<Warentyp> alleWarentypen = new ArrayList<>();
        String sql = "SELECT * FROM warentyp WHERE aktiv = TRUE";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            ResultSet warentypResult = smt.executeQuery();
            while (warentypResult.next())
            {
                int warentypId = warentypResult.getInt("warentypId");
                String name = warentypResult.getString("Name");
                float preisErwachsene = warentypResult.getFloat("preisErwachsener");
                float preisKinder = warentypResult.getFloat("preisKinder");
                Boolean aktiv = warentypResult.getBoolean("aktiv");

                //neu
                float haushaltspauschale = warentypResult.getFloat("haushaltspauschale");
                float deckelbetrag = warentypResult.getFloat("deckelbetrag");
                float minbetrag = warentypResult.getFloat("minbetrag");
                Boolean manuelleberechnung = warentypResult.getBoolean("manuelleberechnung");
                int zuordnungperson = warentypResult.getInt("zuordnungperson");
                int zuordnungbuchungstext = warentypResult.getInt("zuordnungbuchungstext");
                int warentyplimitanzahl = warentypResult.getInt("warentyplimitanzahl");
                int warentyplimitart = warentypResult.getInt("warentyplimitart");
                int warentyplimitabstand = warentypResult.getInt("warentyplimitabstand");
                int warentyplimitabstandart = warentypResult.getInt("warentyplimitabstandart");
                int naechsterwarentypid = warentypResult.getInt("naechsterwarentypid");



                Warentyp warentyp = new Warentyp(warentypId, name, preisErwachsene, preisKinder, aktiv, haushaltspauschale, deckelbetrag, minbetrag, manuelleberechnung, zuordnungperson, zuordnungbuchungstext, warentyplimitanzahl, warentyplimitart, warentyplimitabstand, warentyplimitabstandart, naechsterwarentypid);
                alleWarentypen.add(warentyp);

            }
            smt.close();



            return alleWarentypen;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Warentypen lesen klappt nicht");
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
    public ArrayList<Warentyp> readAll()
    {
        ArrayList<Warentyp> alleWarentypen = new ArrayList<>();
        String sql = "SELECT * FROM warentyp";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            ResultSet warentypResult = smt.executeQuery();
            while (warentypResult.next())
            {
                int warentypId = warentypResult.getInt("warentypId");
                String name = warentypResult.getString("Name");
                float preisErwachsene = warentypResult.getFloat("preisErwachsener");
                float preisKinder = warentypResult.getFloat("preisKinder");
                Boolean aktiv = warentypResult.getBoolean("aktiv");

                //neu
                float haushaltspauschale = warentypResult.getFloat("haushaltspauschale");
                float deckelbetrag = warentypResult.getFloat("deckelbetrag");
                float minbetrag = warentypResult.getFloat("minbetrag");
                Boolean manuelleberechnung = warentypResult.getBoolean("manuelleberechnung");
                int zuordnungperson = warentypResult.getInt("zuordnungperson");
                int zuordnungbuchungstext = warentypResult.getInt("zuordnungbuchungstext");
                int warentyplimitanzahl = warentypResult.getInt("warentyplimitanzahl");
                int warentyplimitart = warentypResult.getInt("warentyplimitart");
                int warentyplimitabstand = warentypResult.getInt("warentyplimitabstand");
                int warentyplimitabstandart = warentypResult.getInt("warentyplimitabstandart");
                int naechsterwarentypid = warentypResult.getInt("naechsterwarentypid");



                Warentyp warentyp = new Warentyp(warentypId, name, preisErwachsene, preisKinder, aktiv, haushaltspauschale, deckelbetrag, minbetrag, manuelleberechnung, zuordnungperson, zuordnungbuchungstext, warentyplimitanzahl, warentyplimitart, warentyplimitabstand, warentyplimitabstandart, naechsterwarentypid);
                alleWarentypen.add(warentyp);

            }
            smt.close();



            return alleWarentypen;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Warentypen lesen klappt nicht");
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
        String sqlReadWarentypen = " SELECT * FROM lita_listen WHERE liste = 'gebuehr'";
        String sqlReadGebuehren = "SELECT * FROM lita_einstellungen WHERE KEY1 = ?";

        String sql = "INSERT INTO warentyp("
                + "warentypId,"
                + "name,"
                + "preisErwachsener,"
                + "preisKinder,"
                + "aktiv)"
                + "VALUES(?,?,?,?,?)";

        try
        {
            //Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
            //Connection con =  SQLConnection.getCon();               //neue DB

            PreparedStatement smt = conOldDb.prepareStatement(sqlReadWarentypen);
            ResultSet warentypResult = smt.executeQuery();

            while (warentypResult.next())
            {
                PreparedStatement smtGebuehr = conOldDb.prepareStatement(sqlReadGebuehren);
                smtGebuehr.setString(1, warentypResult.getString("wert"));
                ResultSet gebuehrenResult = smtGebuehr.executeQuery();
                while (gebuehrenResult.next())
                {
                    int warentypId = warentypResult.getInt("nummer");
                    String name = warentypResult.getString("name");
                    String preisErwachsene = gebuehrenResult.getString("value1");
                    String preisKinder = gebuehrenResult.getString("value2");
                    boolean aktiv = warentypResult.getBoolean("aktiv");

                    try
                    {
                        PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
                        smt2.setInt(1, warentypId);
                        smt2.setString(2, name);
                        System.out.println(preisErwachsene + "_________________" + preisKinder);
                        if (!preisErwachsene.equals("x"))
                        {
                            smt2.setFloat(3, Float.parseFloat(preisErwachsene));
                        }
                        else
                        {
                            smt2.setNull(3, Types.FLOAT);
                        }
                        if (!preisKinder.equals("x"))
                        {
                            smt2.setFloat(4, Float.parseFloat(preisKinder));
                        }
                        else
                        {
                            smt2.setNull(4, Types.FLOAT);
                        }
                        smt2.setBoolean(5, aktiv);
                        smt2.executeUpdate();
                        smt2.close();
                    } catch (SQLException e)
                    {
                        e.printStackTrace();
                        System.out.println("Warentyp Migrieren klappt nicht");
                    }
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
