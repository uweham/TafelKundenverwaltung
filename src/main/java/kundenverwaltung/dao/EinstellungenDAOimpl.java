package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kundenverwaltung.model.Einstellungen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EinstellungenDAOimpl implements EinstellungenDAO
{
    private final Logger LOGGER = LoggerFactory.getLogger(EinstellungenDAOimpl.class);

  /**
   * Updates the Einstellungen in the database.
   *
   * @param einstellungen the Einstellungen object containing the updated values
   * @return true if the update was successful, false otherwise
   */
    @Override
    public boolean update(Einstellungen einstellungen)
    {
        // KORRIGIERTES SQL STATEMENT
        String sql = "UPDATE einstellungen "
                + "SET kundenarchivieren = ? , "
                + "gebuehrErwachsener = ? , "
                + "alterBescheid = ? , "
                + "alterErwachsener = ? , "
                + "bescheidBenoetigt = ? , "
                + "datenschutzerklaerung = ? , "   // Komma war wichtig
                + "verteilstellenzugehoerigkeit = ? " // Hier KEIN Komma, da WHERE folgt
                + "WHERE initialId = 0";

        try
        {
            Connection con =  SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);

            smt.setInt(1, einstellungen.getKundenarchivieren());
            smt.setInt(2, einstellungen.getGebuehrErwachsener());
            smt.setInt(3, einstellungen.getAlterBescheid());
            smt.setInt(4, einstellungen.getAlterErwachsener());
            smt.setBoolean(5, einstellungen.isBescheidBenoetigt());
            smt.setBoolean(6, einstellungen.isDatenschutzerklaerung());
            smt.setBoolean(7, einstellungen.isVerteilstellenzugehoerigkeit());

            smt.executeUpdate();
            smt.close();
            return true;

        }
        catch (SQLException e)
        {
            e.printStackTrace(); // Damit du den Fehler in der Konsole siehst!
            LOGGER.error(e.getMessage());
        }

        return false;
    }
    
    // ... Der Rest der Klasse (read Methode) kann so bleiben wie er war ...
    @Override
    public Einstellungen read()
    {
        String sql = "SELECT * FROM einstellungen WHERE initialId = 0";

        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            ResultSet resultSet = smt.executeQuery();
            resultSet.next();

            int kundenarchivieren = resultSet.getInt("kundenarchivieren");
            int gebuehrErwachsener = resultSet.getInt("gebuehrErwachsener");
            int alterBescheid = resultSet.getInt("alterBescheid");
            int alterErwachsener = resultSet.getInt("alterErwachsener");

            boolean bescheidbenoetigt = resultSet.getBoolean("bescheidBenoetigt");
            boolean datenschutzerklaerung = resultSet.getBoolean("datenschutzerklaerung");
            boolean verteilstellenzugehoerigkeit = resultSet.getBoolean("verteilstellenzugehoerigkeit");

            String tafelServerHostAddress = resultSet.getString("tafel_server_host");

            smt.close();

            Einstellungen einstellungen = new Einstellungen(kundenarchivieren, gebuehrErwachsener, alterBescheid, alterErwachsener, bescheidbenoetigt, datenschutzerklaerung, verteilstellenzugehoerigkeit, tafelServerHostAddress);

            return einstellungen;
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }

        return null;
    }
}