package kundenverwaltung.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import kundenverwaltung.model.Vorlage;
import kundenverwaltung.model.Vorlagearten;
import kundenverwaltung.toolsandworkarounds.GetTemplateType;

public class VorlageDAOimpl implements VorlageDAO
{
    private static final GetTemplateType GET_TEMPLATE_TYPE = new GetTemplateType();
    /**
     *.
     */
    @Override
    public boolean create(Vorlage vorlage)
    {
        String idAbfrage = "Select AUTO_INCREMENT "
    +
                "FROM INFORMATION_SCHEMA.TABLES "
    +
                "WHERE TABLE_SCHEMA = 'lingener-tafel' "
    +
                "AND TABLE_NAME = 'vorlage'";


        String sql = "INSERT INTO vorlage("
               /*+ "vorlageID,"*/
                + "templateType,"
                + "name,"
                + "autor,"
                + "fileVersion,"
                + "fileTypes,"
                + "defaultText,"
                + "passwort,"
                + "aktiv,"
                + "daten)"
                + "VALUES(?,?,?,?,?,?,?,?,?)";

        try
        {
            Connection con =  SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int vorlageID = count.getInt(1) + 1;
            vorlage.setVorlageId(vorlageID);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, GET_TEMPLATE_TYPE.getTemplateString(vorlage.getTemplateType())); //String.valueOf(vorlage.getTemplateType()));
            smt.setString(2, vorlage.getName());
            smt.setString(3, vorlage.getAutor());
            smt.setString(4, vorlage.getFileVersion());
            smt.setString(5, vorlage.getFileTypes());
            smt.setString(6, vorlage.getDefaultText());
            smt.setInt(7, vorlage.getPasswort());
            smt.setBoolean(8, vorlage.isAktiv());
            smt.setBlob(9, vorlage.getDaten());

            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Vorlage Einfügen Klappt nicht");
        }
        return false;
    }
    /**
     *.
     */
    @Override
    public boolean update(Vorlage vorlage)
    {
        String sql = "UPDATE vorlage "
    +
                //"SET templateType = ?, " +
                "SET name = ?, "
                +
                "autor = ?, "
                +
                "fileVersion = ?, "
                +
                "fileTypes = ?, "
                +
                "defaultText = ?, "
                +
                "passwort = ?, "
                +
                "aktiv = ?, "
                +
                "daten = ? "
                +
                "WHERE vorlageID = ?";

        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            //smt.setString(1, String.valueOf(vorlage.getTemplateType()));
            smt.setString(1, vorlage.getName());
            smt.setString(2, vorlage.getAutor());
            smt.setString(3, vorlage.getFileVersion());
            smt.setString(4, vorlage.getFileTypes());
            smt.setString(5, vorlage.getDefaultText());
            smt.setInt(6, vorlage.getPasswort());
            smt.setBoolean(7, vorlage.isAktiv());
            smt.setBlob(8, vorlage.getDaten());
            smt.setInt(9, vorlage.getVorlageId());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Vorlage Update Klappt nicht");
        }
        return false;
    }
    /**
     *.
     */
    @Override
    public boolean delete(Vorlage vorlage)
    {
        String sql = "Delete From vorlage Where vorlageID= ?";
        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, vorlage.getVorlageId());
            smt.executeUpdate();
            smt.close();

            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Vorlage Loeschen Klappt nicht");
        }
        return false;
    }
    /**
     *.
     */
    @Override
    public Vorlage read(int vorlageID)
    {
        String sql = "SELECT * FROM vorlage WHERE vorlageID = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, vorlageID);
            ResultSet vorlageResult = smt.executeQuery();
            vorlageResult.next();
            Vorlagearten templateType = Vorlagearten.valueOf(vorlageResult.getString("templateType"));
            String name = vorlageResult.getString("name");
            String autor = vorlageResult.getString("autor");
            String fileVersion = vorlageResult.getString("fileVersion");
            String fileType = vorlageResult.getString("fileType");
            String defaultText = vorlageResult.getString("defaultText");
            int passwort = vorlageResult.getInt("passwort");
            Boolean aktiv = vorlageResult.getBoolean("aktiv");
            Blob daten = vorlageResult.getBlob("daten");
            smt.close();

            Vorlage vorlage = new Vorlage(vorlageID, templateType, name, autor, fileVersion, fileType, defaultText, passwort, aktiv, daten);

            return vorlage;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Recht lesen klappt nicht");
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
    public ArrayList<Vorlage> getAllTemplate()
    {
        String sql = "SELECT * FROM vorlage";
        try
        {
            ArrayList<Vorlage> resultTemplates = new ArrayList<>();
            new SQLConnection();
			Connection connection = SQLConnection.getCon();
            Statement statement = connection.createStatement();
            ResultSet resultSetTemplates = statement.executeQuery(sql);

            while (resultSetTemplates.next())
            {
                int templateId = resultSetTemplates.getInt("vorlageId");
                String templateType = resultSetTemplates.getString("templateType");
                String name = resultSetTemplates.getString("name");
                String author = resultSetTemplates.getString("autor");
                String fileVersion = resultSetTemplates.getString("fileVersion");
                String fileTypes = resultSetTemplates.getString("fileTypes");
                String defaultText = resultSetTemplates.getString("defaultText");
                int password = resultSetTemplates.getInt("passwort");
                Boolean aktiv = resultSetTemplates.getBoolean("aktiv");
                Blob files = resultSetTemplates.getBlob("daten");

                Vorlage template = new Vorlage(templateId, GET_TEMPLATE_TYPE.getTemplateType(templateType), name, author, fileVersion, fileTypes,
                        defaultText, password, aktiv, files);

                resultTemplates.add(template);
            }
            return resultTemplates;

        } catch (SQLException e)
        {
            System.out.println("Vorlagen konnten nicht gelesen werden.");
            e.printStackTrace();
        }

        return null;
    }
    /**
     *.
     */
    @Override
    public boolean migrate(Connection conOldDb, Connection conNewdDb)
    {
        String sqlRead = "SELECT * FROM lita_vorlagen";
        String sql = "INSERT INTO vorlage("
                + "vorlageID,"
                + "templateType,"
                + "name,"
                + "autor,"
                + "fileVersion,"
                + "fileTypes,"
                + "defaultText,"
                + "passwort,"
                + "aktiv,"
                + "daten)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?)";

        try
        {
            //Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
            //Connection con =  SQLConnection.getCon();               //neue DB

            PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
            ResultSet vorlageResult = smt.executeQuery();

            while (vorlageResult.next())
            {
                int vorlageId = vorlageResult.getInt("id");
                String templatetype = vorlageResult.getString("templatetype");
                String name = vorlageResult.getString("name");
                String autor = vorlageResult.getString("autor");
                String fileversion = vorlageResult.getString("fileversion");
                String filetypes = vorlageResult.getString("filetypes");
                String defaultText = vorlageResult.getString("defaultext");     //In der alten DB auch falsch geschrieben
                Boolean passwort = vorlageResult.getBoolean("passwort");
                Boolean aktiv = vorlageResult.getBoolean("aktiv");
                Blob daten = vorlageResult.getBlob("daten");

                try
                {
                    PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
                    smt2.setInt(1, vorlageId);
                    smt2.setString(2, templatetype);
                    smt2.setString(3, name);
                    smt2.setString(4, autor);
                    smt2.setString(5, fileversion);
                    smt2.setString(6, filetypes);
                    smt2.setString(7, defaultText);
                    smt2.setBoolean(8, passwort);
                    smt2.setBoolean(9, aktiv);
                    smt2.setBlob(10, daten);
                    smt2.executeUpdate();
                    smt2.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                    System.out.println("Vorlage Migrieren klappt nicht");
                }
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    /**
     *.
     */
    @Override public ArrayList<Vorlage> getTemplates(Vorlagearten template)
    {
        String sql = "SELECT * FROM vorlage WHERE aktiv = TRUE AND templateType = ?";

        ArrayList<Vorlage> resultTemplateList = new ArrayList<>();

        try
        {
            new SQLConnection();
			Connection connection = SQLConnection.getCon();
            PreparedStatement smt = connection.prepareStatement(sql);
            smt.setString(1, GET_TEMPLATE_TYPE.getTemplateString(template));
            ResultSet templateResult = smt.executeQuery();

            while (templateResult.next())
            {
                int templateId = templateResult.getInt("vorlageId");
                Vorlagearten templateType = GET_TEMPLATE_TYPE.getTemplateType(templateResult.getString("templateType"));
                String name = templateResult.getString("name");
                String author = templateResult.getString("autor");
                String fileVersion = templateResult.getString("fileVersion");
                String fileType = templateResult.getString("fileTypes");
                String defaultText = templateResult.getString("defaultText");
                int password = templateResult.getInt("passwort");
                boolean isActive = templateResult.getBoolean("aktiv");
                Blob blob = templateResult.getBlob("daten");

                Vorlage tempTemplate =
                        new Vorlage(templateId, templateType, name, author, fileVersion, fileType,
                                defaultText, password, isActive, blob);

                resultTemplateList.add(tempTemplate);


            }
            return resultTemplateList;



        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
