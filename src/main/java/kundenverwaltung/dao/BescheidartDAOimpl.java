package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import kundenverwaltung.model.Bescheidart;

/**
 * This class save, delete, update, read and migrate an assessmant in the database.
 *
 * @author Florian Bruns
 * @Date 03.11.2018
 * @author Richard Kromm
 * @Date 20.08.2018
 * @Version 1.1
 */
public class BescheidartDAOimpl implements BescheidartDAO
{
    /**
     * This function save a new assessment in the database.
     *
     * @param bescheidart
     * @return true: insert successful; false: insert failed
     */
    @Override
    public boolean create(Bescheidart bescheidart)
    {
        // FIX: Wir nutzen RETURN_GENERATED_KEYS statt INFORMATION_SCHEMA, 
        // damit es unabhängig vom Datenbanknamen funktioniert.
        String sql = "INSERT INTO bescheidart(Name, aktiv) VALUES(?,?)";

        try
        {
            Connection con = SQLConnection.getCon();
            // Wichtig: Statement.RETURN_GENERATED_KEYS anfordern
            PreparedStatement smt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            smt.setString(1, bescheidart.getName());
            smt.setBoolean(2, bescheidart.isAktiv());
            
            int affectedRows = smt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            // Die generierte ID von der Datenbank abholen und ins Objekt setzen
            try (ResultSet generatedKeys = smt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bescheidart.setBescheidartId(generatedKeys.getInt(1));
                } else {
                    return false;
                }
            }
            
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Bescheidart Einfügen Klappt nicht");
        }
        return false;
    }

    /**
     * This function update a existing assessment in the database.
     *
     * @param bescheidart
     * @return true: update successful; false: update failed
     */
    @Override
    public boolean update(Bescheidart bescheidart)
    {
        String sql = "UPDATE bescheidart "
                + "SET Name = ?, "
                + "aktiv = ? "
                + "WHERE BescheidartId = ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, bescheidart.getName());
            smt.setBoolean(2, bescheidart.isAktiv());
            smt.setInt(3, bescheidart.getBescheidartId());
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
     * This function delete a existing assessment in the database.
     *
     * @param bescheidart
     * @return true: delete successful; false: delete failed
     */
    @Override
    public boolean delete(Bescheidart bescheidart)
    {
        String sql = "Delete From bescheidart Where bescheidartId= ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, bescheidart.getBescheidartId());
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
     * This function read the assessmant with the selected id.
     *
     * @param bescheidArtId
     * @return an assessment
     */
    @Override
    public Bescheidart read(int bescheidArtId)
    {
        String sql = "SELECT * FROM bescheidart WHERE bescheidartId = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, bescheidArtId);
            ResultSet rechteResult = smt.executeQuery();
            rechteResult.next();
            String name = rechteResult.getString("Name");
            boolean aktiv = rechteResult.getBoolean("aktiv");
            smt.close();

            Bescheidart bescheidart = new Bescheidart(bescheidArtId, name, aktiv);

            return bescheidart;
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
     * This function read all assessments from database.
     *
     * @return arraylist with all assessments
     */
    @Override
    public ArrayList<Bescheidart> readAll()
    {
        ArrayList<Bescheidart> alleBescheidarten = new ArrayList<>();
        String sql = "SELECT * FROM bescheidart";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            ResultSet bescheidArtResult = smt.executeQuery();
            while (bescheidArtResult.next())
            {
                int bescheidArtId = bescheidArtResult.getInt("bescheidArtId");
                String name = bescheidArtResult.getString("Name");
                boolean aktiv = bescheidArtResult.getBoolean("aktiv");

                Bescheidart bescheidart = new Bescheidart(bescheidArtId, name, aktiv);
                alleBescheidarten.add(bescheidart);
            }
            smt.close();

            return alleBescheidarten;
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
     * This function read all enabled/disabled assessments from database.
     *
     * @param enabled true: all enabled assessments; false: all disabled assessments
     * @return arraylist with all assessments
     */
    @Override
    public ArrayList<Bescheidart> readAssessments(Boolean enabled)
    {
        ArrayList<Bescheidart> resultAssessmentsArrayList = new ArrayList<>();
        String sql = "SELECT * FROM bescheidart WHERE aktiv = ? ORDER BY name";

        try
        {
            Connection connection = SQLConnection.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, enabled);
            ResultSet resultSetAssessmentsArrayList = preparedStatement.executeQuery();
            while (resultSetAssessmentsArrayList.next())
            {
                int assessmentId = resultSetAssessmentsArrayList.getInt("bescheidArtId");
                String name = resultSetAssessmentsArrayList.getString("Name");
                boolean enabledAssessment = resultSetAssessmentsArrayList.getBoolean("aktiv");

                Bescheidart assessment = new Bescheidart(assessmentId, name, enabledAssessment);
                resultAssessmentsArrayList.add(assessment);
            }
            preparedStatement.close();

            return resultAssessmentsArrayList;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Bescheid lesen klappt nicht");
        }
        return null;
    }

    /**
     * Migrates Berechtigung records from an old database to a new database.
     * @param conOldDb The connection to the old database.
     * @param conNewdDb The connection to the new database.
     * @return false (this method is not yet implemented).
     */
    @Override
    public boolean migrate(Connection conOldDb, Connection conNewdDb)
    {
        String sqlRead = "SELECT * FROM lita_listen WHERE liste LIKE 'bescheid'";

        String sql = "INSERT INTO bescheidart("
                + "bescheidartId,"
                + "Name,"
                + "aktiv) "
                + "VALUES(?,?,?)";

        try
        {
            PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
            ResultSet bescheidArtResult = smt.executeQuery();

            while (bescheidArtResult.next())
            {
                int bescheidId = bescheidArtResult.getInt("nummer");
                String name = bescheidArtResult.getString("name");
                Boolean aktiv = bescheidArtResult.getBoolean("aktiv");

                try
                {
                    PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
                    smt2.setInt(1, bescheidId);
                    smt2.setString(2, name);
                    smt2.setBoolean(3, aktiv);
                    smt2.executeUpdate();
                    smt2.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                    System.out.println("Bescheidart Migrieren klappt nicht");
                }
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}