package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import kundenverwaltung.model.Nation;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

public class NationDAOimpl implements  NationDAO
{

    private String databaseName = PropertiesFileController.getDbName();

    private static final int ILLEGAL_NATION_ID = -1;
    private static final Nation ILLEGAL_NATION_ORDER_BY_FREQUNCY = new Nation(ILLEGAL_NATION_ID, "------ HÄUFIGKEIT ------", "", false);
    private static final Nation ILLEGAL_NATION_ORDER_BY_ALPHABETICAL = new Nation(ILLEGAL_NATION_ID, "------ ALPHABETISCH ------", "", false);
    private static final String COLUMN_LABEL_NATION_ID = "nationId";
    private static final String COLUMN_LABEL_NATION_NAME = "name";
    private static final String COLUMN_LABEL_NATION_NATIONALITY = "nationalitaet";
    private static final String COLUMN_LABEL_NATION_ENABLED = "aktiv";

    /**
     * Creates a new Nation in the database.
     *
     * @param nation the Nation object to be created
     * @return true if the creation was successful, false otherwise
     */
    @Override
    public boolean create(Nation nation)
    {
        String idAbfrage = "Select AUTO_INCREMENT "
    +
                "FROM INFORMATION_SCHEMA.TABLES "
    +
                "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
    +
                "AND TABLE_NAME = 'Nation'";


        String sql = "INSERT INTO nation("
                + "Name,"
                + "aktiv,"
                + "nationalitaet) "
                + "VALUES(?,?,?)";

        try
        {
            Connection con =  SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int nationId = count.getInt(1);
            nation.setNationId(nationId);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, nation.getName());
            smt.setBoolean(2, nation.getAktiv());
            smt.setString(3, nation.getNationalitaet());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Nation Einfügen Klappt nicht");
        }
        return false;
    }

    /**
     * Updates an existing Nation in the database.
     *
     * @param nation the Nation object containing the updated values
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean update(Nation nation)
    {
        String sql = "UPDATE nation "
    +
                "SET Name = ?, "
    +
                "aktiv = ?, "
    +
                "nationalitaet = ? "
    +
                "WHERE nationId = ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, nation.getName());
            smt.setBoolean(2, nation.getAktiv());
            smt.setString(3, nation.getNationalitaet());
            smt.setInt(4, nation.getNationId());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Nation Update Klappt nicht");
        }
        return false;
    }
    /**
     * Deletes a Nation from the database.
     *
     * @param nation the Nation object to be deleted
     * @return true if the deletion was successful, false otherwise
     */
    @Override
    public boolean delete(Nation nation)
    {
        String sql = "Delete From nation Where nationId= ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, nation.getNationId());
            smt.executeUpdate();
            smt.close();

            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Nation Loeschen Klappt nicht");
        }
        return false;
    }
    /**
     * Reads a Nation from the database.
     *
     * @return the Nation object, or null if not found
     */

    public Nation read(int nation)
    {
        String sql = "SELECT * FROM nation WHERE nationId = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, nation);
            ResultSet nationResult = smt.executeQuery();
            Nation nationObjekt = null;
            if (nationResult.next())
            {
                String name = nationResult.getString("name");
                String nationalitaet = nationResult.getString("nationalitaet");
                boolean aktiv = nationResult.getBoolean("aktiv");
                nationObjekt = new Nation(nation, name, nationalitaet, aktiv);
            }
            smt.close();

            return nationObjekt;
        } catch (SQLException e)
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
     * Migrates Nation data from an old database to a new database.
     *
     * @param conOldDb the connection to the old database
     * @param conNewdDb the connection to the new database
     * @return true if the migration was successful, false otherwise
     */
    @Override
    public boolean migrate(Connection conOldDb, Connection conNewdDb)
    {
        String sqlRead = "SELECT * FROM lita_listen WHERE liste LIKE 'nation' OR liste LIKE 'default'";
        String sql = "INSERT INTO nation("
                + "nationId,"
                + "Name,"
                + "nationalitaet) "
                + "VALUES(?,?,?)";


        try
        {
            //Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
            //Connection con =  SQLConnection.getCon();               //neue DB

            PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
            ResultSet nationResult = smt.executeQuery();

            while (nationResult.next())
            {
                int nationId = nationResult.getInt("nummer");
                String name  = nationResult.getString("name");
                String nationalitaet = nationResult.getString("wert");

                try
                {
                    PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
                    smt2.setInt(1, nationId);
                    smt2.setString(2, name);
                    smt2.setString(3, nationalitaet);
                    smt2.executeUpdate();
                    smt2.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                    System.out.println("Nation Migrieren klappt nicht");
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * This function get all nation.
     *
     * @return arraylist with all nation.
     */
    @Override
    public ArrayList<Nation> getAllNationen()
    {
        String sql = "SELECT * FROM nation ORDER BY name";
        ArrayList<Nation> resultNationArrayList = new ArrayList<>();

        try
        {
            new SQLConnection();
            Connection connection = SQLConnection.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                resultNationArrayList.add(createNation(resultSet));
            }
            preparedStatement.close();
            return resultNationArrayList;

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * This function get the five most nationality and then all enabled natonality alphabetical.
     *
     * @Author Richard Kromm
     * @return arraylist
     */
    @Override
    public ArrayList<Nation> getAllEnabledNationen()
    {
        String sqlMostNation = "(SELECT nation.nationId, nation.name, nation.nationalitaet, nation.aktiv "
                                    + "FROM nation "
                                    + "WHERE name = 'Deutschland') "
                                + "UNION "
                                + "(SELECT nation.nationId, nation.name, nation.nationalitaet, nation.aktiv "
                                    + "FROM familienmitglied, nation "
                                    + "WHERE nation = nationId "
                                    + "GROUP BY nationId ORDER BY count(*) DESC LIMIT 5) "
                                + "UNION "
                                + "(SELECT nation.nationId, nation.name, nation.nationalitaet, nation.aktiv "
                                        + "FROM nation "
                                        + "WHERE name = 'Angabe Verweigert')";

        String sqlAllNation = "SELECT * FROM nation WHERE aktiv = 1 ORDER BY name COLLATE latin1_german1_ci";
        try
        {
            Connection con = SQLConnection.getCon();
            ArrayList<Nation> nationListe = new ArrayList<>();
            Statement stmt = con.createStatement();

            nationListe.add(ILLEGAL_NATION_ORDER_BY_FREQUNCY); // used as a separator
            ResultSet resultSetSqlMostNation = stmt.executeQuery(sqlMostNation);
            while (resultSetSqlMostNation.next())
            {
                nationListe.add(createNation(resultSetSqlMostNation));
            }

            nationListe.add(ILLEGAL_NATION_ORDER_BY_ALPHABETICAL); // used as a separator
            ResultSet resultSetSqlAllNation = stmt.executeQuery(sqlAllNation);
            while (resultSetSqlAllNation.next())
            {
                nationListe.add(createNation(resultSetSqlAllNation));
            }

            return nationListe;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * This function create a nation from resultset.
     *
     * @param resultSet
     * @return nation
     */
    public Nation createNation(ResultSet resultSet)
    {
        try
        {
            int nationId = resultSet.getInt(COLUMN_LABEL_NATION_ID);
            String name = resultSet.getString(COLUMN_LABEL_NATION_NAME);
            String nation = resultSet.getString(COLUMN_LABEL_NATION_NATIONALITY);
            Boolean enabled = resultSet.getBoolean(COLUMN_LABEL_NATION_ENABLED);

            return new Nation(nationId, name, nation, enabled);

        } catch (SQLException e)
            {
                e.printStackTrace();
                return null;
            }
    }
    /**
     */
    @Override
    public ArrayList<Nation> getAllNationenMitAnzahl()
    {
        String sql = "SELECT n.nationId, n.name, COUNT(f.nation) AS anzahl "
    +
                "FROM nation n INNER JOIN familienmitglied f ON n.nationId = f.nation "
            +
                "GROUP BY n.nationId, n.name "
                +
                "ORDER BY n.name";

        ArrayList<Nation> resultNationArrayList = new ArrayList<>();

        try (Connection connection = SQLConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery())
        {

            while (resultSet.next())
            {
                int nationId = resultSet.getInt("nationId");
                String name = resultSet.getString("name");
                int anzahl = resultSet.getInt("anzahl"); // Anzahl der Mitglieder

                // Nation-Objekt erstellen
                Nation nation = new Nation(nationId, name, null, true); // Placeholder für nationalitaet
                nation.setAnzahl(anzahl); // Anzahl setzen

                resultNationArrayList.add(nation); // Nation zur Liste hinzufügen
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultNationArrayList;
    }
    /**
     */
    public void saveNationStatistics(ArrayList<Nation> nations)
    {
        String checkSql = "SELECT COUNT(*) FROM nationStatistik WHERE nationalität = ? AND anzahlPersonen = ?";
        String insertSql = "INSERT INTO nationStatistik (nationalität, anzahlPersonen) VALUES (?, ?)";

        try (Connection connection = SQLConnection.getCon())
        {

            for (Nation nation : nations)
            {
                // Überprüfen, ob der Eintrag existiert
                try (PreparedStatement checkStatement = connection.prepareStatement(checkSql))
                {
                    checkStatement.setString(1, nation.getName());
                    checkStatement.setInt(2, nation.getAnzahl());

                    try (ResultSet resultSet = checkStatement.executeQuery())
                    {
                        if (resultSet.next() && resultSet.getInt(1) == 0)
                        { // Nur wenn der Eintrag nicht existiert

                            // Einfügen des neuen Eintrags
                            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql))
                            {
                                insertStatement.setString(1, nation.getName());
                                insertStatement.setInt(2, nation.getAnzahl());
                                insertStatement.executeUpdate();
                            }
                        }
                    }
                }
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
