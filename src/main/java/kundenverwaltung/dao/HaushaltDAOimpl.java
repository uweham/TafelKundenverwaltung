package kundenverwaltung.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kundenverwaltung.model.Ausgabegruppe;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.OrderBy;
import kundenverwaltung.model.PLZ;
import kundenverwaltung.model.statistiktool.ArchivierteKunden;
import kundenverwaltung.model.statistiktool.Herkunft;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.model.Warentyp;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class HaushaltDAOimpl implements HaushaltDAO
{
    private static final int ILLEGAL_PRODUCT_TYPE_ID = -1;
    private String databaseName = PropertiesFileController.getDbName();
    /**
     */
    @Override
    public boolean create(Haushalt haushalt)
    {
        String idAbfrage = "Select AUTO_INCREMENT "
    +
                "FROM INFORMATION_SCHEMA.TABLES "
    +
                "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
    +
                "AND TABLE_NAME = 'haushalt'";

        String sql = "INSERT INTO haushalt("
                + "strasse,"
                + "hausnummer,"
                + "plz,"
                + "telefonnummer,"
                + "mobilnummer,"
                + "bemerkung,"
                + "kundeSeit,"
                + "saldo,"
                + "verteilstellenId,"
                + "istArchiviert,"
                + "istGesperrt,"
                + "ausgabegruppeId,"
                + "belieferung,"
                + "datenschutzerklaerung)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try
        {
            Connection con = SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int kundennummer = count.getInt(1);
            haushalt.setKundennummer(kundennummer);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, haushalt.getStrasse());
            smt.setString(2, haushalt.getHausnummer());
            smt.setInt(3, haushalt.getPlz().getPlzId());
            smt.setString(4, haushalt.getTelefonnummer());
            smt.setString(5, haushalt.getMobilnummer());
            smt.setString(6, haushalt.getBemerkungen());
            smt.setDate(7, Date.valueOf(haushalt.getKundeSeit()));
            smt.setFloat(8, haushalt.getSaldo());
            smt.setInt(9, haushalt.getVerteilstelle().getVerteilstellenId());
            smt.setBoolean(10, haushalt.getIstArchiviert());
            smt.setBoolean(11, haushalt.getIstGesperrt());
            smt.setInt(12, haushalt.getAusgabegruppe().getAusgabegruppeId());
            smt.setBoolean(13, haushalt.isBelieferung());
            smt.setBoolean(14, haushalt.isDatenschutzerklaerung());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Haushalt Einfügen Klappt nicht");
        }
        return false;
    }
    /**
     */
    public Integer createHaushaltAndGetId(Haushalt haushalt)
    {
        String idAbfrage = "Select AUTO_INCREMENT "
    +
                "FROM INFORMATION_SCHEMA.TABLES "
    +
                "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
    +
                "AND TABLE_NAME = 'haushalt'";

        String sql = "INSERT INTO haushalt("
                + "strasse,"
                + "hausnummer,"
                + "plz,"
                + "telefonnummer,"
                + "mobilnummer,"
                + "bemerkung,"
                + "kundeSeit,"
                + "saldo,"
                + "verteilstellenId,"
                + "istArchiviert,"
                + "istGesperrt,"
                + "ausgabegruppeId,"
                + "belieferung,"
                + "datenschutzerklaerung)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try
        {
            Connection con = SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int kundennummer = count.getInt(1);
            haushalt.setKundennummer(kundennummer);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, haushalt.getStrasse());
            smt.setString(2, haushalt.getHausnummer());
            smt.setInt(3, haushalt.getPlz().getPlzId());
            smt.setString(4, haushalt.getTelefonnummer());
            smt.setString(5, haushalt.getMobilnummer());
            smt.setString(6, haushalt.getBemerkungen());
            smt.setDate(7, Date.valueOf(haushalt.getKundeSeit()));
            smt.setFloat(8, haushalt.getSaldo());
            smt.setInt(9, haushalt.getVerteilstelle().getVerteilstellenId());
            smt.setBoolean(10, haushalt.getIstArchiviert());
            smt.setBoolean(11, haushalt.getIstGesperrt());
            smt.setInt(12, haushalt.getAusgabegruppe().getAusgabegruppeId());
            smt.setBoolean(13, haushalt.isBelieferung());
            smt.setBoolean(14, haushalt.isDatenschutzerklaerung());
            smt.executeUpdate();
            smt.close();
            return kundennummer;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Haushalt Einfügen Klappt nicht");
        }
        return -1;
    }
    /**
     */
    @Override
    public boolean update(Haushalt haushalt)
    {
        String sql = "UPDATE haushalt "
    +
                "SET strasse = ?, "
    +
                "hausnummer = ?, "
    +
                "plz = ?, "
    +
                "telefonnummer = ?, "
    +
                "mobilnummer = ?, "
    +
                "bemerkung = ?, "
    +
                "kundeSeit = ?, "
    +
                "saldo = ?, "
    +
                "verteilstellenId = ?, "
    +
                "istArchiviert = ?, "
    +
                "istGesperrt = ?, "
    +
                "ausgabegruppeId = ?, "
    +
                "belieferung = ?, "
    +
                "datenschutzerklaerung = ? "
    +
                "WHERE kundennummer = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setString(1, haushalt.getStrasse());
            smt.setString(2, haushalt.getHausnummer());
            smt.setInt(3, haushalt.getPlz().getPlzId());
            smt.setString(4, haushalt.getTelefonnummer());
            smt.setString(5, haushalt.getMobilnummer());
            smt.setString(6, haushalt.getBemerkungen());
            smt.setDate(7, Date.valueOf(haushalt.getKundeSeit()));
            smt.setFloat(8, haushalt.getSaldo());
            smt.setInt(9, haushalt.getVerteilstelle().getVerteilstellenId());
            smt.setBoolean(10, haushalt.getIstArchiviert());
            smt.setBoolean(11, haushalt.getIstGesperrt());
            smt.setInt(12, haushalt.getAusgabegruppe().getAusgabegruppeId());
            smt.setBoolean(13, haushalt.isBelieferung());
            smt.setBoolean(14, haushalt.isDatenschutzerklaerung());
            smt.setInt(15, haushalt.getKundennummer());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Haushalt Update Klappt nicht");
        }
        return false;
    }
    /**
     */
    @Override
    public boolean delete(Haushalt haushalt)
    {
        String sql = "Delete From haushalt Where kundennummer= ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, haushalt.getKundennummer());
            smt.executeUpdate();
            smt.close();

            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Haushalt Loeschen Klappt nicht");
        }
        return false;
    }
    /**
     */
    @Override
    public Haushalt read(int kundennummer)
    {
        String sql = "SELECT * FROM haushalt WHERE kundennummer = ?";
        try
        {
            PLZDAO plzdao = new PLZDaoImpl();
            VerteilstelleDAO verteilstelleDAO = new VerteilstelleDAOimpl();
            AusgabegruppeDAO ausgabegruppeDAO = new AusgabegruppeDAOimpl();

            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, kundennummer);
            ResultSet haushaltResult = smt.executeQuery();
            haushaltResult.next();
            String strasse = haushaltResult.getString("strasse");
            String hausnummer = haushaltResult.getString("hausnummer");
            PLZ plz = plzdao.read(haushaltResult.getInt("plz"));
            // Korrektur: Spaltenname "telefonnummer" statt "hausnummer"
            String telefonnummer = haushaltResult.getString("telefonnummer");
            String mobilnummer = haushaltResult.getString("mobilnummer");
            String bemerkungen = haushaltResult.getString("bemerkung");
            Date kundeSeitSQLDate = haushaltResult.getDate("kundeSeit");
            LocalDate kundeSeit = null;
            if (kundeSeitSQLDate != null)
            {
                kundeSeit = kundeSeitSQLDate.toLocalDate();
            }
            float saldo = haushaltResult.getFloat("saldo");
            Verteilstelle verteilstelle = verteilstelleDAO.read(haushaltResult.getInt("verteilstellenId"));
            boolean istArchiviert = haushaltResult.getBoolean("istArchiviert");
            boolean istGesperrt = haushaltResult.getBoolean("istGesperrt");
            Ausgabegruppe ausgabegruppe = ausgabegruppeDAO.read(haushaltResult.getInt("ausgabegruppeId"));
            boolean belieferung = haushaltResult.getBoolean("belieferung");
            boolean datenschutzerklaerung = haushaltResult.getBoolean("datenschutzerklaerung");
            smt.close();

            Haushalt haushalt = new Haushalt(kundennummer, strasse, hausnummer, plz, telefonnummer, mobilnummer, bemerkungen, kundeSeit, saldo, verteilstelle, istArchiviert, istGesperrt, ausgabegruppe, belieferung, datenschutzerklaerung);

            return haushalt;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Haushalt lesen klappt nicht");
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }

    /**
     * get all households with distribution point id xy.
     *
     * @param distributionPointId
     * @return
     * @Author Richard Kromm
     */
    @Override
    public ArrayList<Haushalt> getHousehold(int distributionPointId)
    {
        ArrayList<Haushalt> resultHouseholdArrayList = new ArrayList<>();
        String sql = "SELECT * FROM haushalt WHERE verteilstellenid = ?";

        try
        {
            new SQLConnection();
            Connection connection = SQLConnection.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, distributionPointId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                int customerId = resultSet.getInt("kundennummer");
                String street = resultSet.getString("strasse");
                String houseNumber = resultSet.getString("hausnummer");
                PLZ plz = new PLZDaoImpl().read(resultSet.getInt("plz"));
                // Korrektur: Spaltenname "telefonnummer" statt "hausnummer"
                String phoneNumber = resultSet.getString("telefonnummer");
                String mobileNumber = resultSet.getString("mobilnummer");
                String comment = resultSet.getString("bemerkung");
                Date customerSinceSQLDate = resultSet.getDate("kundeSeit");
                LocalDate customerSince = null;
                if (customerSinceSQLDate != null)
                {
                    customerSince = customerSinceSQLDate.toLocalDate();
                }
                float saldo = resultSet.getFloat("saldo");
                Verteilstelle distributionPoint = new VerteilstelleDAOimpl().read(resultSet.getInt("verteilstellenId"));
                boolean isArchieved = resultSet.getBoolean("istArchiviert");
                boolean isBlocked = resultSet.getBoolean("istGesperrt");
                Ausgabegruppe outputGroupe = new AusgabegruppeDAOimpl().read(resultSet.getInt("ausgabegruppeId"));
                boolean supply = resultSet.getBoolean("belieferung");
                boolean gdpr = resultSet.getBoolean("datenschutzerklaerung");

                Haushalt household = new Haushalt(customerId, street, houseNumber, plz, phoneNumber, mobileNumber,
                        comment, customerSince, saldo, distributionPoint, isArchieved, isBlocked, outputGroupe, supply, gdpr);

                resultHouseholdArrayList.add(household);
            }
            preparedStatement.close();
            return resultHouseholdArrayList;

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Create customer list.
     *
     * @return
     * @Author Richard Kromm
     */
    @Override
    public ArrayList<Haushalt> createCustomerList(Verteilstelle distributionPoint, Warentyp productType, OrderBy orderBy, Boolean ascending,
                                                  Boolean showArchivedCustomer, Boolean showBlockedCustomer)
    {
        ArrayList<Haushalt> resultArrayList = new ArrayList<>();

        String sql = "SELECT kundennummer, nName, vName, strasse, hausnummer, haushalt.plz, plz.plz AS plzTemp, plz.ort "
                + "telefonnummer, mobilnummer, haushalt.bemerkung, kundeSeit, saldo, haushalt.verteilstellenId, "
                + "verteilstelle.bezeichnung, istArchiviert, istGesperrt, haushalt.ausgabeGruppeId, ausgabegruppe.name, "
                + "belieferung, datenschutzerklaerung "
                + "FROM haushalt, familienmitglied, plz, verteilstelle, ausgabegruppe "
                + "WHERE kundennummer = haushaltId AND haushaltsVorstand = true AND haushalt.plz = plz.plzId "
                + "AND haushalt.verteilstellenId = verteilstelle.verteilstellenId "
                + "AND haushalt.ausgabeGruppeId = ausgabegruppe.ausgabegruppeId "
                + "AND haushalt.verteilstellenId = ? ";

        sql += (!showArchivedCustomer) ? "AND istArchiviert = false " : "";
        sql += (!showBlockedCustomer) ? "AND istGesperrt = false " : "";
        sql += "ORDER BY " + orderBy.getDbColumn() + (ascending ? " ASC" : " DESC");

        try
        {
            Connection connection = SQLConnection.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, distributionPoint.getVerteilstellenId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                int customerId = resultSet.getInt("kundennummer");
                if (productType.getWarentypId() == ILLEGAL_PRODUCT_TYPE_ID
                        | new EinkaufDAOimpl().boughtHouseholdThisProductType(customerId, productType))
                {
                    String street = resultSet.getString("strasse");
                    String houseNumber = resultSet.getString("hausnummer");
                    PLZ plz = new PLZDaoImpl().read(resultSet.getInt("plz"));
                    // Korrektur: Spaltenname "telefonnummer" statt "hausnummer"
                    String phoneNumber = resultSet.getString("telefonnummer");
                    String mobileNumber = resultSet.getString("mobilnummer");
                    String comment = resultSet.getString("bemerkung");
                    Date customerSinceSQLDate = resultSet.getDate("kundeSeit");
                    LocalDate customerSince = null;
                    if (customerSinceSQLDate != null)
                    {
                        customerSince = customerSinceSQLDate.toLocalDate();
                    }
                    float saldo = resultSet.getFloat("saldo");
                    Verteilstelle distributionPointResult = new VerteilstelleDAOimpl().read(resultSet.getInt("verteilstellenId"));
                    boolean isArchieved = resultSet.getBoolean("istArchiviert");
                    boolean isBlocked = resultSet.getBoolean("istGesperrt");
                    Ausgabegruppe outputGroupe = new AusgabegruppeDAOimpl().read(resultSet.getInt("ausgabegruppeId"));
                    boolean supply = resultSet.getBoolean("belieferung");
                    boolean gdpr = resultSet.getBoolean("datenschutzerklaerung");

                    resultArrayList.add(new Haushalt(customerId, street, houseNumber, plz, phoneNumber, mobileNumber,
                            comment, customerSince, saldo, distributionPointResult, isArchieved, isBlocked, outputGroupe, supply, gdpr));
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return resultArrayList;
    }
    /**
     */
 
    /**
     * Create customer list for the last kundeSeit Date.
     *
     * @return
     * @Author Richard Kromm modified U.P.
     */
    @Override
    public ArrayList<Haushalt> createCustomerLastList(Verteilstelle distributionPoint, Warentyp productType, OrderBy orderBy, Boolean ascending,
                                                  Boolean showArchivedCustomer, Boolean showBlockedCustomer)
    {
        ArrayList<Haushalt> resultArrayList = new ArrayList<>();

        String sql = "SELECT kundennummer, nName, vName, strasse, hausnummer, haushalt.plz, plz.plz AS plzTemp, plz.ort "
                + "telefonnummer, mobilnummer, haushalt.bemerkung, kundeSeit, saldo, haushalt.verteilstellenId, "
                + "verteilstelle.bezeichnung, istArchiviert, istGesperrt, haushalt.ausgabeGruppeId, ausgabegruppe.name, "
                + "belieferung, datenschutzerklaerung "
                + "FROM haushalt, familienmitglied, plz, verteilstelle, ausgabegruppe "
                + "WHERE kundennummer = haushaltId AND haushaltsVorstand = true AND haushalt.plz = plz.plzId "
                + "AND haushalt.verteilstellenId = verteilstelle.verteilstellenId "
                + "AND haushalt.ausgabeGruppeId = ausgabegruppe.ausgabegruppeId "
                + "AND haushalt.verteilstellenId = ? ";

        sql += (!showArchivedCustomer) ? "AND istArchiviert = false " : "";
        sql += (!showBlockedCustomer) ? "AND istGesperrt = false " : "";
        sql += "AND kundeSeit in (SELECT max(kundeSeit) as datumlast from haushalt)";
        sql += "ORDER BY " + orderBy.getDbColumn() + (ascending ? " ASC" : " DESC");

        try
        {
            Connection connection = SQLConnection.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, distributionPoint.getVerteilstellenId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                int customerId = resultSet.getInt("kundennummer");
                if (productType.getWarentypId() == ILLEGAL_PRODUCT_TYPE_ID
                        | new EinkaufDAOimpl().boughtHouseholdThisProductType(customerId, productType))
                {
                    String street = resultSet.getString("strasse");
                    String houseNumber = resultSet.getString("hausnummer");
                    PLZ plz = new PLZDaoImpl().read(resultSet.getInt("plz"));
                    // Korrektur: Spaltenname "telefonnummer" statt "hausnummer"
                    String phoneNumber = resultSet.getString("telefonnummer");
                    String mobileNumber = resultSet.getString("mobilnummer");
                    String comment = resultSet.getString("bemerkung");
                    Date customerSinceSQLDate = resultSet.getDate("kundeSeit");
                    LocalDate customerSince = null;
                    if (customerSinceSQLDate != null)
                    {
                        customerSince = customerSinceSQLDate.toLocalDate();
                    }
                    float saldo = resultSet.getFloat("saldo");
                    Verteilstelle distributionPointResult = new VerteilstelleDAOimpl().read(resultSet.getInt("verteilstellenId"));
                    boolean isArchieved = resultSet.getBoolean("istArchiviert");
                    boolean isBlocked = resultSet.getBoolean("istGesperrt");
                    Ausgabegruppe outputGroupe = new AusgabegruppeDAOimpl().read(resultSet.getInt("ausgabegruppeId"));
                    boolean supply = resultSet.getBoolean("belieferung");
                    boolean gdpr = resultSet.getBoolean("datenschutzerklaerung");

                    resultArrayList.add(new Haushalt(customerId, street, houseNumber, plz, phoneNumber, mobileNumber,
                            comment, customerSince, saldo, distributionPointResult, isArchieved, isBlocked, outputGroupe, supply, gdpr));
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return resultArrayList;
    }
    /**
     */    
    
    @Override
    public boolean migrate(Connection conOldDb, Connection conNewdDb)
    {
        String sqlRead = "SELECT * FROM lita_kundenstamm";

        String sql = "INSERT INTO haushalt("
                + "kundennummer,"
                + "strasse,"
                + "hausnummer,"
                + "plz,"
                + "telefonnummer,"
                + "mobilnummer,"
                + "bemerkung,"
                + "kundeSeit,"
                + "saldo,"
                + "verteilstellenId,"
                + "istArchiviert,"
                + "istGesperrt,"
                + "ausgabegruppeId,"
                + "belieferung,"
                + "datenschutzerklaerung)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try
        {
            //Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
            //Connection con =  SQLConnection.getCon();               //neue DB

            PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
            ResultSet haushaltResult = smt.executeQuery();

            while (haushaltResult.next())
            {
                int kundennummer = haushaltResult.getInt("kundennummer");
                String strasse = haushaltResult.getString("Strasse");
                String hausnummer = haushaltResult.getString("hausnummer");
                int plz = haushaltResult.getInt("postleitzahl_id");
                String telefon = haushaltResult.getString("telefon_nr");
                String mobil = haushaltResult.getString("mobil_nr");
                String bemerkung = haushaltResult.getString("bemerkungen");
                Date kundeseit = haushaltResult.getDate("kunde_seit");
                Float saldo = haushaltResult.getFloat("kundenkonto_saldo");
                int verteilstelle = haushaltResult.getInt("verteilstellen_nr");
                Boolean archiviert = haushaltResult.getBoolean("kunde_ist_archiviert");
                Boolean gesperrt = haushaltResult.getBoolean("kunde_ist_gesperrt");
                int ausgabegruppe = haushaltResult.getInt("ausgabeGruppe");
                boolean belieferung = haushaltResult.getBoolean("belieferung");
                boolean datenschutzerklaerung = haushaltResult.getBoolean("datenschutzerklaerung");

                try
                {
                    PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
                    smt2.setInt(1, kundennummer);
                    smt2.setString(2, strasse);
                    smt2.setString(3, hausnummer);
                    smt2.setInt(4, plz);
                    smt2.setString(5, telefon);
                    smt2.setString(6, mobil);
                    smt2.setString(7, bemerkung);
                    smt2.setDate(8, kundeseit);
                    smt2.setFloat(9, saldo);
                    smt2.setInt(10, verteilstelle);
                    smt2.setBoolean(11, archiviert);
                    smt2.setBoolean(12, gesperrt);
                    smt2.setInt(13, ausgabegruppe);
                    smt2.setBoolean(14, belieferung);
                    smt2.setBoolean(15, datenschutzerklaerung);

                    smt2.executeUpdate();
                    smt2.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                    System.out.println("Haushalt Migrieren klappt nicht");
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
    // Methode zur Ausführung der SQL-Abfrage und Rückgabe der Statistikdaten
    // Implementierung der Methode getHouseholdsWithLocation
    public List<Herkunft> getHouseholdsWithLocation(String status, boolean isPerson, LocalDate startDatum, LocalDate endDatum)
    {
        List<Herkunft> statistikDaten = new ArrayList<>();

        String sql = "SELECT h.strasse, h.hausnummer, p.plz, COUNT(*) AS anzahlHaushalte "
        +
                "FROM haushalt h "
            +
                "LEFT JOIN plz p ON h.plz = p.plzid "
            +
                "WHERE 1=1 ";

        switch (status)
        {
            case "Aktive Kunden":
                sql += "AND h.istArchiviert = false AND h.istGesperrt = false ";
                break;
            case "Archivierte Kunden":
                sql += "AND h.istArchiviert = true ";
                break;
            case "Alle Kunden":
                break;
            default:
                throw new IllegalArgumentException("Unbekannter Status: " + status);
        }

        if (isPerson)
        {
            sql += "AND EXISTS (SELECT 1 FROM familienmitglied fm WHERE fm.haushaltId = h.kundennummer) ";
        }

        // Fügen Sie die Bedingungen für das Datum hinzu
        sql += "AND h.kundeSeit BETWEEN ? AND ? ";

        sql += "GROUP BY h.strasse, h.hausnummer, p.plz";

        System.out.println("SQL Query: " + sql);

        try (Connection connection = SQLConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql))
        {

            // Setzen der Parameter für die Datumsabfrage
            stmt.setDate(1, java.sql.Date.valueOf(startDatum));
            stmt.setDate(2, java.sql.Date.valueOf(endDatum));

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    String strasse = rs.getString("strasse");
                    String hausnummer = rs.getString("hausnummer");
                    String plz = rs.getString("plz");
                    int anzahlHaushalte = rs.getInt("anzahlHaushalte");
                    statistikDaten.add(new Herkunft(strasse, hausnummer, plz, anzahlHaushalte));
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return statistikDaten;
    }
    /**
     */
    public void saveHerkunftStatistik(String ort, String hausnummer, String plz, int anzahlHaushalte)
    {
        // Überprüfen, ob der Datensatz bereits existiert
        String checkSql = "SELECT COUNT(*) FROM herkunftStatistik WHERE ort = ? AND hausnummer = ? AND plz = ?";

        try (Connection connection = SQLConnection.getCon();
             PreparedStatement checkStmt = connection.prepareStatement(checkSql))
        {

            checkStmt.setString(1, ort);
            checkStmt.setString(2, hausnummer);
            checkStmt.setString(3, plz);

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0)
            {
                System.out.println("Datensatz existiert bereits.");
                return; // Abbrechen, wenn der Datensatz bereits existiert
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        // Wenn der Datensatz nicht existiert, dann speichern
        String sql = "INSERT INTO herkunftStatistik (ort, hausnummer, plz, anzahlHaushalte) VALUES (?, ?, ?, ?)";

        try (Connection connection = SQLConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql))
        {

            stmt.setString(1, ort);
            stmt.setString(2, hausnummer);
            stmt.setString(3, plz);
            stmt.setInt(4, anzahlHaushalte);
            stmt.executeUpdate();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    /**
     */
    @Override
    public List<Herkunft> getHerkunftStatistik()
    {
        List<Herkunft> statistikList = new ArrayList<>();

        String sql = "SELECT ort, hausnummer, plz, anzahlHaushalte FROM herkunftStatistik";

        try (Connection connection = SQLConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery())
        {

            while (rs.next())
            {
                String ort = rs.getString("ort");
                String hausnummer = rs.getString("hausnummer");
                String plz = rs.getString("plz");
                int anzahlHaushalte = rs.getInt("anzahlHaushalte");

                statistikList.add(new Herkunft(ort, hausnummer, plz, anzahlHaushalte)); // Passen Sie den Konstruktor entsprechend an
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return statistikList;
    }
    /**
     */
    // Methode zum Laden archivierter Kunden
    @Override
    public ObservableList<ArchivierteKunden> getArchivierteKunden() throws SQLException
    {
        ObservableList<ArchivierteKunden> archivierteKunden = FXCollections.observableArrayList();

        String query = "SELECT h.kundennummer, h.strasse, h.hausnummer, p.plz, p.ort, "
        +
                "f.vName, f.nName "
            +
                "FROM haushalt h "
                +
                "JOIN familienmitglied f ON f.haushaltId = h.kundennummer "
                +
                "JOIN plz p ON h.plz = p.plzId "
                +
                // Join mit der plz-Tabelle
                "WHERE h.istArchiviert = 1";

        try (Connection connection = SQLConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery())
        {

            while (rs.next())
            {
                ArchivierteKunden kunde = new ArchivierteKunden(
                        rs.getString("kundennummer"),
                        rs.getString("vName") + " " + rs.getString("nName"),
                        rs.getString("strasse") + " " + rs.getString("hausnummer"),
                        rs.getString("plz"), // PLZ aus der plz-Tabelle
                        rs.getString("ort")  // Ort aus der plz-Tabelle
                );
                archivierteKunden.add(kunde);
            }
            System.out.println("Anzahl archivierter Kunden: " + archivierteKunden.size());
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return archivierteKunden;
    }
    /**
     */
    @Override
    public ObservableList<ArchivierteKunden> getGesperrteKunden() throws SQLException
    {
        ObservableList<ArchivierteKunden> gesperrteKunden = FXCollections.observableArrayList();

        String query = "SELECT h.kundennummer, h.strasse, h.hausnummer, p.plz, p.ort, "
        +
                "f.vName, f.nName "
            +
                "FROM haushalt h "
                +
                "JOIN familienmitglied f ON f.haushaltId = h.kundennummer "
                +
                "JOIN plz p ON h.plz = p.plzId "
                +
                // Join mit der plz-Tabelle
                "WHERE h.istGesperrt = 1";

        try (Connection connection = SQLConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery())
        {

            while (rs.next())
            {
                ArchivierteKunden kunde = new ArchivierteKunden(
                        rs.getString("kundennummer"),
                        rs.getString("vName") + " " + rs.getString("nName"),
                        rs.getString("strasse") + " " + rs.getString("hausnummer"),
                        rs.getString("plz"), // PLZ aus der plz-Tabelle
                        rs.getString("ort")  // Ort aus der plz-Tabelle
                );
                gesperrteKunden.add(kunde);
            }
            System.out.println("Anzahl gesperrte Kunden: " + gesperrteKunden.size());
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return gesperrteKunden;
    }
    /**
     */
    public void resetCustomerStatus(ArchivierteKunden customer) throws SQLException
    {
        String sql = "UPDATE haushalt SET istArchiviert = ?, istGesperrt = ? WHERE kundennummer = ?";

        try (Connection connection = SQLConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql))
        { // Klammer hier geschlossen
            stmt.setInt(1, 0); // Setzt istArchiviert auf 0
            stmt.setInt(2, 0); // Setzt istGesperrt auf 0
            stmt.setString(3, customer.getKdNr()); // Setzt die Kunden-Nummer

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0)
            {
                throw new SQLException("Kunde nicht gefunden: " + customer.getKdNr());
            }
        } // try-with-resources schließt die Ressourcen automatisch
    }
    /**
     */
    public void updateCustomer(ArchivierteKunden customer) throws SQLException
    {
        String updateKundenInfosSql = "UPDATE kundenInfos SET name = ?, strasse = ?, plz = ?, ort = ? WHERE kdNr = ?";

        try (Connection connection = SQLConnection.getCon())
        {
            // Transaktion starten
            connection.setAutoCommit(false);
            try (PreparedStatement stmtKundenInfos = connection.prepareStatement(updateKundenInfosSql);
            PreparedStatement stmtHaushalt = connection.prepareStatement(updateKundenInfosSql))
            {
                // Update in kundenInfos
                stmtKundenInfos.setString(1, customer.getName());
                stmtKundenInfos.setString(2, customer.getStrasse());
                stmtKundenInfos.setString(3, customer.getPlz());
                stmtKundenInfos.setString(4, customer.getOrt());
                stmtKundenInfos.setString(5, customer.getKdNr());
                stmtKundenInfos.executeUpdate();

                // Transaktion abschließen
                connection.commit();
            } catch (SQLException e)
            {
                connection.rollback();
                throw new SQLException("Fehler beim Aktualisieren des Kunden: " + e.getMessage());
            } finally
            {
                connection.setAutoCommit(true); // AutoCommit wieder aktivieren
            }
        }
    }
    /**
     */
    public void saveCustomer(ArchivierteKunden kunde) throws SQLException
    {
        String updateKundenInfos = "UPDATE kundenInfos SET name = ?, strasse = ?, plz = ?, ort = ? WHERE kdNr = ?";
        String updateHaushalt = "UPDATE haushalt SET plz = ?, ort = ? WHERE kundennummer = ?";

        try (Connection connection = SQLConnection.getCon())
        {
            // Transaktion starten
            connection.setAutoCommit(false);
            try (PreparedStatement stmtKundenInfos = connection.prepareStatement(updateKundenInfos);
                 PreparedStatement stmtHaushalt = connection.prepareStatement(updateHaushalt))
            {

                // Update in kundenInfos
                stmtKundenInfos.setString(1, kunde.getName());
                stmtKundenInfos.setString(2, kunde.getStrasse());
                stmtKundenInfos.setString(3, kunde.getPlz());
                stmtKundenInfos.setString(4, kunde.getOrt());
                stmtKundenInfos.setString(5, kunde.getKdNr());
                stmtKundenInfos.executeUpdate();

                // Hole die PLZ-ID
                int plzId = getPlzIdByDescription(kunde.getPlz());
                if (plzId == -1)
                {
                    throw new SQLException("PLZ nicht gefunden: " + kunde.getPlz());
                }

                // Update in haushalt
                stmtHaushalt.setInt(1, plzId); // Setze die PLZ-ID
                stmtHaushalt.setString(2, kunde.getOrt());
                stmtHaushalt.setString(3, kunde.getKdNr()); // Hier überprüfen, ob dies der richtige Schlüssel ist
                stmtHaushalt.executeUpdate();

                // Transaktion abschließen
                connection.commit();
            } catch (SQLException e)
            {
                connection.rollback();
                throw new SQLException("Fehler beim Aktualisieren des Kunden: " + e.getMessage());
            } finally
            {
                connection.setAutoCommit(true); // AutoCommit wieder aktivieren
            }
        }
    }
    /**
     */
    public boolean customerExists(String kdNr, String plz) throws SQLException
    {
        String customerSql = "SELECT COUNT(*) FROM kundenInfos WHERE kdNr = ?";
        String plzSql = "SELECT COUNT(*) FROM plz WHERE plzId = ?";

        try (Connection connection = SQLConnection.getCon();
             PreparedStatement customerStmt = connection.prepareStatement(customerSql);
             PreparedStatement plzStmt = connection.prepareStatement(plzSql))
        {

            // Überprüfung der Kundenexistenz
            customerStmt.setString(1, kdNr);
            ResultSet customerRs = customerStmt.executeQuery();
            customerRs.next();
            boolean customerExists = customerRs.getInt(1) > 0;

            // Überprüfung der PLZ-Existenz
            plzStmt.setString(1, plz);
            ResultSet plzRs = plzStmt.executeQuery();
            plzRs.next();
            boolean plzExists = plzRs.getInt(1) > 0;

            // Rückgabe, ob der Kunde existiert und die PLZ gültig ist
            return customerExists && plzExists;
        } catch (SQLException e)
        {
            throw new SQLException("Fehler beim Prüfen der Kunden- oder PLZ-Existenz: " + e.getMessage());
        }
    }
    /**
     */
    public int getPlzIdByDescription(String plzDescription) throws SQLException
    {
        String query = "SELECT id FROM plz WHERE beschreibung = ?";
        try (Connection connection = SQLConnection.getCon();
                PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, plzDescription);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                return rs.getInt("id");
            } else
            {
                return -1; // Wenn die PLZ nicht existiert
            }
        }
    }

}