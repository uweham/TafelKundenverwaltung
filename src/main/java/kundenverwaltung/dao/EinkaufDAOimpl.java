package kundenverwaltung.dao;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import kundenverwaltung.model.Einkauf;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.OrderBy;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.model.Warentyp;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;


/**
 * Created by Florian-PC on 03.11.2017.
 */
public class EinkaufDAOimpl implements EinkaufDAO
{
    private static final int ILLEGAL_PRODUCT_TYPE_ID = -1;
    private String databaseName = PropertiesFileController.getDbName();

    /**
     * Creates a new Einkauf in the database.
     *
     * @param einkauf the Einkauf object to be created
     * @return true if the creation was successful, false otherwise
     */
    @Override
    public boolean create(Einkauf einkauf)
    {
        String idAbfrage = "Select AUTO_INCREMENT "
                +
                "FROM INFORMATION_SCHEMA.TABLES "
                +
                "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
                +

                "AND TABLE_NAME = 'einkauf'";

        String sql = "INSERT INTO einkauf("
                + "warentyp,"
                + "storniertAm,"
                + "stornoText,"
                + "buchungstext,"
                + "kunde,"
                + "person,"
                + "erfassungsZeit,"
                + "summeEinkauf,"
                + "summeZahlung,"
                + "beiVerteilstelle,"
                + "anzahlKinder,"
                + "anzahlErwachsene)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try
        {
            Connection con = SQLConnection.getCon();
            Statement smtID = con.createStatement();
            ResultSet count = smtID.executeQuery(idAbfrage);
            count.next();
            int einkaufId = count.getInt(1);
            einkauf.setEinkaufId(einkaufId);
            smtID.close();

            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, einkauf.getWarentyp().getWarentypId());
            if (einkauf.getStorniertAm() == null)
            {
                smt.setNull(2, Types.TIMESTAMP);
            } else
            {
                smt.setTimestamp(2, Timestamp.valueOf(einkauf.getStorniertAm()));
            }
            smt.setString(3, einkauf.getStornoText());
            smt.setString(4, einkauf.getBuchungstext());
            smt.setInt(5, einkauf.getKunde().getKundennummer());
            smt.setInt(6, einkauf.getPerson().getPersonId());
            smt.setTimestamp(7, Timestamp.valueOf(einkauf.getErfassungsZeit()));
            smt.setDouble(8, einkauf.getSummeEinkauf());
            smt.setDouble(9, einkauf.getSummeZahlung());
            smt.setInt(10, einkauf.getBeiVerteilstelle().getVerteilstellenId());
            smt.setInt(11, einkauf.getAnzahlKinder());
            smt.setInt(12, einkauf.getAnzahlErwachsene());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Einkauf Einfügen Klappt nicht");
        }
        return false;
    }
    /**
     * Updates the person field in the Einkauf record when a person is deleted.
     *
     *
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean update(Einkauf einkauf)
    {
        String sql = "UPDATE einkauf "
                +
                "SET warentyp = ?, "
                +
                "storniertAm = ?, "
                +
                "stornoText = ?, "
                +
                "buchungsText = ?, "
                +
                "kunde = ?, "
                +
                "person = ?, "
                +
                "erfassungsZeit = ?, "
                +
                "summeEinkauf = ?, "
                +
                "summeZahlung = ?, "
                +
                "beiVerteilstelle = ?, "
                +
                "anzahlKinder = ?, "
                +
                "anzahlErwachsene = ? "
                +
                "WHERE einkaufId = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, einkauf.getWarentyp().getWarentypId());
            smt.setTimestamp(2, Timestamp.valueOf(einkauf.getStorniertAm()));
            smt.setString(3, einkauf.getStornoText());
            smt.setString(4, einkauf.getBuchungstext());
            smt.setInt(5, einkauf.getKunde().getKundennummer());
            smt.setInt(6, einkauf.getPerson().getPersonId());
            smt.setTimestamp(7, Timestamp.valueOf(einkauf.getErfassungsZeit()));
            smt.setDouble(8, einkauf.getSummeEinkauf());
            smt.setDouble(9, einkauf.getSummeZahlung());
            smt.setInt(10, einkauf.getBeiVerteilstelle().getVerteilstellenId());
            smt.setInt(11, einkauf.getAnzahlKinder());
            smt.setInt(12, einkauf.getAnzahlErwachsene());
            smt.setInt(13, einkauf.getEinkaufId());
            smt.executeUpdate();
            smt.close();
            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Einkauf Update Klappt nicht");
        }
        return false;
    }
    /**
     * Deletes an existing Einkauf from the database.
     *
     * @return true if the deletion was successful, false otherwise
     */
    public boolean updateOnDeletePerson(int householderDirectorId, int shoppingId)
    {
        String sql = "UPDATE einkauf SET person = ? WHERE einkaufId = ?";
        try
        {
            Connection connection = SQLConnection.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, householderDirectorId);
            preparedStatement.setInt(2, shoppingId);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Reads an Einkauf from the database by its ID.
     *
     * @return the Einkauf object, or null if not found
     */
    @Override
    public boolean delete(Einkauf einkauf)
    {
        String sql = "Delete From einkauf Where einkaufId= ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, einkauf.getEinkaufId());
            smt.executeUpdate();
            smt.close();

            return true;

        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Einkauf Loeschen Klappt nicht");
        }
        return false;
    }
    /**
     * Reads an Einkauf from the database by its ID.
     *
     * @param einkaufId the ID of the Einkauf to be read
     * @return the Einkauf object, or null if not found
     */
    @Override
    public Einkauf read(int einkaufId)
    {
        String sql = "SELECT * FROM einkauf WHERE einkaufId = ?";
        try
        {
            WarentypDAO warentypdao = new WarentypDAOimpl();
            FamilienmitgliedDAO familienmitgliedao = new FamilienmitgliedDAOimpl();
            HaushaltDAO haushaltao = new HaushaltDAOimpl();
            VerteilstelleDAO verteilstelledao = new VerteilstelleDAOimpl();

            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, einkaufId);
            ResultSet einkaufResult = smt.executeQuery();
            einkaufResult.next();
            Warentyp warentyp = warentypdao.read(einkaufResult.getInt("warentyp"));
            LocalDateTime storniertAm = einkaufResult.getTimestamp("storniertAm").toLocalDateTime();
            String stornoText = einkaufResult.getString("stornoText");
            String buchungsText = einkaufResult.getString("buchungsText");
            Haushalt kunde = haushaltao.read(einkaufResult.getInt("kunde"));
            Familienmitglied person = familienmitgliedao.read(einkaufResult.getInt("person"));
            LocalDateTime erfassungsZeit = einkaufResult.getTimestamp("erfassungsZeit").toLocalDateTime();
            Float summeEinkauf = einkaufResult.getFloat("summeEinkauf");
            Float summeZahlung = einkaufResult.getFloat("summeZahlung");
            Verteilstelle beiVerteilstelle = verteilstelledao.read(einkaufResult.getInt("beiVerteilstelle"));
            int anzahlKinder = einkaufResult.getInt("anzahlKinder");
            int anzahlErwachsene = einkaufResult.getInt("anzahlErwachsene");
            smt.close();

            Einkauf einkauf = new Einkauf(einkaufId, warentyp, storniertAm, stornoText, buchungsText, kunde, person, erfassungsZeit, summeEinkauf, summeZahlung, beiVerteilstelle, anzahlKinder, anzahlErwachsene);

            return einkauf;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Einkauf lesen klappt nicht");
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
    /**
     * Migrates Einkauf data from an old database to a new database.
     *
     * @param conOldDb the connection to the old database
     * @param conNewdDb the connection to the new database
     * @return true if the migration was successful, false otherwise
     */
    @Override
    public boolean migrate(Connection conOldDb, Connection conNewdDb)
    {
        String sqlRead = "SELECT * FROM lita_kunden_einkaeufe";
        String sql = "INSERT INTO einkauf("
                + "einkaufId,"
                + "warentyp,"
                + "storniertAm,"
                + "stornoText,"
                + "buchungstext,"
                + "kunde,"
                + "person,"
                + "erfassungsZeit,"
                + "summeEinkauf,"
                + "summeZahlung,"
                + "beiVerteilstelle,"
                + "anzahlKinder,"
                + "anzahlErwachsene)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try
        {
            //Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
            //Connection con = SQLConnection.getCon();               //neue DB

            PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
            ResultSet einkaufResult = smt.executeQuery();

            while (einkaufResult.next())
            {
                int einkaufId = einkaufResult.getInt("einkauf_nr");
                int warentypId = einkaufResult.getInt("warentyp");
                Timestamp storniertAm = einkaufResult.getTimestamp("storniert_Datum_und_Zeit");
                String stornoText = einkaufResult.getString("stornotext");
                String buchungstext = einkaufResult.getString("buchungstext");
                int kundennummer = einkaufResult.getInt("kundennummer");
                int personId = einkaufResult.getInt("person_nr");
                Timestamp erfassungszeit = einkaufResult.getTimestamp("erfassungs_datum_und_zeit");
                Float summeeinkauf = einkaufResult.getFloat("summe_einkauf");
                Float summezahlung = einkaufResult.getFloat("summe_zahlung");
                int beiVerteilstelle = einkaufResult.getInt("bei_verteilstelle");
                int kinder = einkaufResult.getInt("kinder");
                int erwachsene = einkaufResult.getInt("erwachsen");
                int householdDirectorId = new FamilienmitgliedDAOimpl().getHousholdDirector(kundennummer);

                try
                {
                    PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
                    smt2.setInt(1, einkaufId);
                    smt2.setInt(2, warentypId);
                    smt2.setTimestamp(3, storniertAm);
                    smt2.setString(4, stornoText);
                    smt2.setString(5, buchungstext);
                    smt2.setInt(6, kundennummer);

                    if (einkaufResult.getInt("person_nr") == 0)
                    {
                        smt2.setInt(7, householdDirectorId);
                    }
                    else
                    {
                        smt2.setInt(7, personId);
                    }

                    smt2.setTimestamp(8, erfassungszeit);
                    smt2.setFloat(9, summeeinkauf);
                    smt2.setFloat(10, summezahlung);
                    smt2.setInt(11, beiVerteilstelle);
                    smt2.setInt(12, kinder);
                    smt2.setInt(13, erwachsene);
                    smt2.executeUpdate();
                    smt2.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                    System.out.println("Einkauf Migrieren klappt nicht");
                }
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the last Einkauf date for a household.
     *
     * @return the date and time of the last Einkauf, or null if not found
     */
    @Override
    public boolean boughtHouseholdThisProductType(int householdId, Warentyp productType)
    {
        String sql = "SELECT warentyp, kunde FROM einkauf WHERE warentyp = ? AND kunde = ? LIMIT 1";
        try
        {
            Connection connection = SQLConnection.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, productType.getWarentypId());
            preparedStatement.setInt(2, householdId);
            ResultSet resultSet = preparedStatement.executeQuery();
            int countRows = 0;
            while (resultSet.next())
            {
                countRows++;
            }
            return countRows > 0;

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Gets all Einkäufe for a household.
     *
     * @param haushalt the Haushalt object
     * @return a list of Einkäufe for the household
     */
    @Override
    public LocalDateTime getLetzerEinkauf(Haushalt haushalt)
    {
        String sql = "Select MAX(erfassungsZeit) AS letzterEinkauf FROM einkauf WHERE kunde = ?";
        try
        {
            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, haushalt.getKundennummer());
            ResultSet einkaufResult = smt.executeQuery();
            einkaufResult.next();
            if (einkaufResult.getTimestamp("letzterEinkauf") == null)
            {
                return null; //Falls kein letzter Einkauf vorhanden
            }
            LocalDateTime erfassungszeit = einkaufResult.getTimestamp("letzterEinkauf").toLocalDateTime();
            smt.close();
            return erfassungszeit;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Letzter Einkauf wurde nicht gefunden");
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }
    /**
     */
    @Override
    public ArrayList<Einkauf> getAllEinkauefe(Haushalt haushalt)
    {
        String sql = "SELECT * FROM einkauf WHERE Kunde = ?";
        ArrayList<Einkauf> einkaufListe = new ArrayList<>();
        try
        {
            WarentypDAO warentypdao = new WarentypDAOimpl();
            FamilienmitgliedDAO familienmitgliedao = new FamilienmitgliedDAOimpl();
            HaushaltDAO haushaltao = new HaushaltDAOimpl();
            VerteilstelleDAO verteilstelledao = new VerteilstelleDAOimpl();

            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            smt.setInt(1, haushalt.getKundennummer());
            ResultSet einkaufResult = smt.executeQuery();
            while (einkaufResult.next())
            {
                Warentyp warentyp = warentypdao.read(einkaufResult.getInt("warentyp"));
                LocalDateTime storniertAm = null;
                if (einkaufResult.getTimestamp("storniertAm") != null)
                {
                    storniertAm = einkaufResult.getTimestamp("storniertAm").toLocalDateTime();
                }
                String stornoText = einkaufResult.getString("stornoText");
                String buchungsText = einkaufResult.getString("buchungsText");
                Haushalt kunde = haushaltao.read(einkaufResult.getInt("kunde"));
                Familienmitglied person = familienmitgliedao.read(einkaufResult.getInt("person"));
                LocalDateTime erfassungsZeit = einkaufResult.getTimestamp("erfassungsZeit").toLocalDateTime();
                Float summeEinkauf = einkaufResult.getFloat("summeEinkauf");
                Float summeZahlung = einkaufResult.getFloat("summeZahlung");
                Verteilstelle beiVerteilstelle = verteilstelledao.read(einkaufResult.getInt("beiVerteilstelle"));
                int anzahlKinder = einkaufResult.getInt("anzahlKinder");
                int anzahlErwachsene = einkaufResult.getInt("anzahlErwachsene");
                int einkaufId = einkaufResult.getInt("einkaufId");
                Einkauf einkauf = new Einkauf(einkaufId, warentyp, storniertAm, stornoText, buchungsText, kunde, person, erfassungsZeit, summeEinkauf, summeZahlung, beiVerteilstelle, anzahlKinder, anzahlErwachsene);
                einkaufListe.add(einkauf);
            }
            return einkaufListe;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Einkauf lesen klappt nicht");
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;

    }

    //Get fuer die Suche

    /**
     *
     * @Author Richard Kromm
     * @param periodeStart
     * @param periodeEnd
     * @param familyMember
     * @param distributionPoint
     * @param orderBy
     * @param ascending
     * @param productTyp
     * @param salesCanceledToday
     * @param salesEarlierCanceledToday
     * @return
     */
    @Override
    public ArrayList<Einkauf> getAllSalesToday(Timestamp periodeStart, Timestamp periodeEnd,
                                               Familienmitglied familyMember, Verteilstelle distributionPoint,
                                               OrderBy orderBy, Boolean ascending, Warentyp productTyp,
                                               Boolean salesCanceledToday, Boolean salesEarlierCanceledToday)
    {
        String sql = "SELECT einkaufId, warentyp, storniertAm, stornoText, buchungstext, kunde, person, vName, nName, "
                + "erfassungsZeit, summeEinkauf, summeZahlung, beiVerteilstelle, "
                + "bezeichnung, anzahlKinder, anzahlErwachsene "
                + "FROM einkauf, familienmitglied, verteilstelle "
                + "WHERE person = personId AND beiVerteilstelle = verteilstellenId ";

        sql += (familyMember != null) ? "AND kunde = ? " : "AND beiVerteilstelle = ? ";
        int customerIdDistributionPointId = (familyMember != null) ? familyMember.getKundennummer() : distributionPoint.getVerteilstellenId();
        sql += (productTyp.getWarentypId() > ILLEGAL_PRODUCT_TYPE_ID)  ? "AND warentyp = ? " : "AND warentyp > ? ";
        sql += (salesEarlierCanceledToday ? "AND erfassungsZeit < ? " : "AND erfassungsZeit BETWEEN ? AND ? ");
        sql += (salesCanceledToday || salesEarlierCanceledToday ? "AND storniertAm BETWEEN ? AND ? " : "");
        sql += "ORDER BY " + orderBy.getDbColumn() + (ascending ? " ASC" : " DESC");

        System.out.println("FERTIGER SQL: " + sql);

        ArrayList<Einkauf> resultArrayList = new ArrayList<>();
        try
        {
            WarentypDAO warentypdao = new WarentypDAOimpl();
            FamilienmitgliedDAO familienmitgliedao = new FamilienmitgliedDAOimpl();
            HaushaltDAO haushaltao = new HaushaltDAOimpl();
            VerteilstelleDAO verteilstelledao = new VerteilstelleDAOimpl();

            Connection connection = SQLConnection.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int parameterIndex = 1;
            preparedStatement.setInt(parameterIndex++, customerIdDistributionPointId);
            preparedStatement.setInt(parameterIndex++, productTyp.getWarentypId());
            if (!salesEarlierCanceledToday)
            {
                preparedStatement.setTimestamp(parameterIndex++, periodeStart);
                preparedStatement.setTimestamp(parameterIndex++, periodeEnd);
            } else
            {
                preparedStatement.setTimestamp(parameterIndex++, periodeStart);
            }
            if (salesCanceledToday || salesEarlierCanceledToday)
            {
                preparedStatement.setTimestamp(parameterIndex++, periodeStart);
                preparedStatement.setTimestamp(parameterIndex, periodeEnd);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                int salesId = resultSet.getInt("einkaufId");
                Warentyp productTypResult = warentypdao.read(resultSet.getInt("warentyp"));
                Timestamp canceledOn = resultSet.getTimestamp("storniertAm");
                String canceledText = resultSet.getString("stornoText");
                String text = resultSet.getString("buchungsText");
                Haushalt customer = haushaltao.read(resultSet.getInt("kunde"));
                Familienmitglied familyMemberResult = familienmitgliedao.read(resultSet.getInt("person"));
                LocalDateTime timestamp = resultSet.getTimestamp("erfassungsZeit").toLocalDateTime();
                Float totalSales = resultSet.getFloat("summeEinkauf");
                Float totalPayment = resultSet.getFloat("summeZahlung");
                Verteilstelle distributionPointResult = verteilstelledao.read(resultSet.getInt("beiVerteilstelle"));
                int numberChildren = resultSet.getInt("anzahlKinder");
                int numberAdults = resultSet.getInt("anzahlErwachsene");

                if (canceledOn != null && (salesCanceledToday || salesEarlierCanceledToday))
                {
                    resultArrayList.add(new Einkauf(salesId, productTypResult, canceledOn.toLocalDateTime(), canceledText, text, customer,
                            familyMemberResult, timestamp, totalSales, totalPayment, distributionPointResult,
                            numberChildren, numberAdults));
                } else
                {
                    if (canceledOn == null)
                    {
                        resultArrayList.add(new Einkauf(salesId, productTypResult, null, canceledText, text, customer,
                                familyMemberResult, timestamp, totalSales, totalPayment, distributionPointResult,
                                numberChildren, numberAdults));
                    }
                }
            }
            return resultArrayList;

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all Einkäufe for a household within a specified date range and other criteria.
     *
     * @param household the Haushalt object
     * @param warentyp the Warentyp object
     * @param verteilstelle the Verteilstelle object
     * @param storniert true to include canceled sales, false otherwise
     * @param startDatum the start date
     * @param endDatum the end date
     * @return a list of Einkäufe that match the criteria
     */
    @Override
    public ArrayList<Einkauf> getAllEinkauefe(Haushalt household, Warentyp warentyp, Verteilstelle verteilstelle, boolean storniert, LocalDate startDatum, LocalDate endDatum)
    {
        String sql = "SELECT * FROM einkauf WHERE kunde = " + household.getKundennummer();

        if (!storniert)
        {
            sql = sql + " AND storniertAm IS NULL";

            if (warentyp != null)
            {
                sql = sql + " AND warentyp = " + warentyp.getWarentypId();
            }

            if (verteilstelle != null)
            {
                sql = sql + " AND beiVerteilstelle = " + verteilstelle.getVerteilstellenId();
            }

            if (startDatum != null && endDatum != null)
            {
                sql = sql + " AND erfassungsZeit BETWEEN '" + startDatum + "' AND '" + endDatum + "'";
            }
            else
            {
                if (startDatum != null)
                {
                    sql = sql + " AND erfassungsZeit >= '" + startDatum + "'";
                }
                if (endDatum != null)
                {
                    sql = sql + " AND erfassungsZeit <= '" + endDatum + "'";
                }
            }
        }
        else
        {
            sql = sql + " AND einkaufID IS NOT NULL ";     //Dass sollte immer eintreten. Sonst gibt es probleme mit den AND der anderen Bedinbungen /ggf noch bessere Lösung finden
            if (warentyp != null)
            {
                sql = sql + " AND warentyp = " + warentyp.getWarentypId();
            }

            if (verteilstelle != null)
            {
                sql = sql + " AND beiVerteilstelle = " + verteilstelle.getVerteilstellenId();
            }

            if (startDatum != null && endDatum != null)
            {
                sql = sql + " AND erfassungsZeit BETWEEN '" + startDatum + "' AND '" + endDatum + "'";
            }
            else
            {
                if (startDatum != null)
                {
                    sql = sql + " AND erfassungsZeit >= '" + startDatum + "'";
                }
                if (endDatum != null)
                {
                    sql = sql + " AND erfassungsZeit <= '" + endDatum + "'";
                }
            }
        }
        System.out.println(sql);
        try
        {

            ArrayList<Einkauf> einkauefe = new ArrayList<>();
            WarentypDAO warentypdao = new WarentypDAOimpl();
            FamilienmitgliedDAO familienmitgliedao = new FamilienmitgliedDAOimpl();
            HaushaltDAO haushaltao = new HaushaltDAOimpl();
            VerteilstelleDAO verteilstelledao = new VerteilstelleDAOimpl();

            Connection con = SQLConnection.getCon();
            PreparedStatement smt = con.prepareStatement(sql);
            ResultSet einkaufResult = smt.executeQuery();
            while (einkaufResult.next())
            {
                Warentyp warentyp2 = warentypdao.read(einkaufResult.getInt("warentyp"));
                LocalDateTime storniertAm = null;
                if (einkaufResult.getTimestamp("storniertAm") != null)
                {
                    storniertAm = einkaufResult.getTimestamp("storniertAm").toLocalDateTime();
                }
                String stornoText = einkaufResult.getString("stornoText");
                String buchungsText = einkaufResult.getString("buchungsText");
                Haushalt kunde = haushaltao.read(einkaufResult.getInt("kunde"));
                Familienmitglied person = familienmitgliedao.read(einkaufResult.getInt("person"));
                LocalDateTime erfassungsZeit = einkaufResult.getTimestamp("erfassungsZeit").toLocalDateTime();
                Float summeEinkauf = einkaufResult.getFloat("summeEinkauf");
                Float summeZahlung = einkaufResult.getFloat("summeZahlung");
                Verteilstelle beiVerteilstelle = verteilstelledao.read(einkaufResult.getInt("beiVerteilstelle"));
                int anzahlKinder = einkaufResult.getInt("anzahlKinder");
                int anzahlErwachsene = einkaufResult.getInt("anzahlErwachsene");
                int einkaufId = einkaufResult.getInt("einkaufId");
                Einkauf einkauf = new Einkauf(einkaufId, warentyp2, storniertAm, stornoText, buchungsText, kunde, person, erfassungsZeit, summeEinkauf, summeZahlung, beiVerteilstelle, anzahlKinder, anzahlErwachsene);
                einkauefe.add(einkauf);
            }
            smt.close();
            return einkauefe;
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Einkauf lesen klappt nicht");
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Fehler");
        }
        return null;
    }

    /**
     * @param einkaeufe
     */
    @Override
    public void saveGuthabenStatistik(List<Einkauf> einkaeufe)
    {

    }

    /**
     * @param type
     * @param verteilstelle
     * @return
     */
    @Override
    public List<Einkauf> getEinkaeufe(String type, String verteilstelle)
    {
        return getEinkaeufeByQuery(type, verteilstelle);
    }
    /**
     */
    @Override
    public List<Einkauf> buildQuery(String type, String verteilstelle)
    {
        List<Einkauf> einkaeufe = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT e.einkaufId, ");

        query.append("f.vName, f.nName, "); // Vorname und Nachname
        query.append("w.name AS warentyp, ");
        query.append("e.summeEinkauf, e.summeZahlung, ");
        query.append("(e.summeEinkauf - e.summeZahlung) AS saldo, ");
        query.append("e.anzahlKinder, e.anzahlErwachsene, f.personId ");

        query.append("FROM einkauf e ");
        query.append("JOIN familienmitglied f ON e.kunde = f.personId ");
        query.append("JOIN warentyp w ON e.warentyp = w.warentypId ");
        query.append("JOIN verteilstelle v ON e.beiVerteilstelle  = v.verteilstellenId ");

        query.append("WHERE v.bezeichnung = ? ");

        if ("Offene Beträge".equals(type))
        {
            query.append("AND e.summeEinkauf > e.summeZahlung ");
        } else if ("Guthaben".equals(type))
        {
            query.append("AND e.summeEinkauf <= e.summeZahlung ");
        }

        try (Connection con = SQLConnection.getCon();
             PreparedStatement statement = con.prepareStatement(query.toString()))
        {
            statement.setString(1, verteilstelle);

            try (ResultSet resultSet = statement.executeQuery())
            {
                while (resultSet.next())
                {
                    Einkauf einkauf = new Einkauf();
                    einkauf.setEinkaufId(resultSet.getInt("einkaufId"));

                    // Erstellen des Familienmitglied-Objekts
                    Familienmitglied person = new Familienmitglied();
                    String vName = resultSet.getString("vName");
                    String nName = resultSet.getString("nName");

                    person.setvName(vName != null ? vName : "Unbekannt");
                    person.setnName(nName != null ? nName : "Unbekannt");

                    einkauf.setPerson(person);

                    einkauf.setWarentypName(resultSet.getString("warentyp"));
                    einkauf.setSummeEinkauf(resultSet.getFloat("summeEinkauf"));
                    einkauf.setSummeZahlung(resultSet.getFloat("summeZahlung"));
                    einkauf.setAnzahlKinder(resultSet.getInt("anzahlKinder"));
                    einkauf.setAnzahlErwachsene(resultSet.getInt("anzahlErwachsene"));
                    einkauf.setSaldo(resultSet.getDouble("saldo"));

                    einkaeufe.add(einkauf);
                }
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return einkaeufe;
    }
    /**
     */
    @Override
    public List<Einkauf> getEinkaeufeByQuery(String type, String verteilstelle)
    {
        List<Einkauf> einkaeufe = buildQuery(type, verteilstelle);

        if (einkaeufe == null || einkaeufe.isEmpty())
        {
            System.out.println("Keine Einkäufe gefunden für die Verteilstelle: " + verteilstelle);
            return new ArrayList<>();
        }

        System.out.println("Anzahl der gefundenen Einkäufe: " + einkaeufe.size());

        return einkaeufe;
    }

    // Methode zum Speichern in die Guthabenstatistik-Tabelle
    /**
     */
    public void saveToGuthabenStatistik(List<Einkauf> einkaeufe)
    {
        if (einkaeufe == null || einkaeufe.isEmpty())
        {
            System.out.println("Die Liste der Einkäufe ist leer oder null.");
            return;
        }

        String sqlInsert = "INSERT INTO guthabenStatistik (einkaufId, warentyp, kunde, saldo, summeEinkauf, summeZahlung, anzahlKinder, anzahlErwachsene) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlCheck = "SELECT COUNT(*) FROM guthabenStatistik WHERE einkaufId = ? AND warentyp = ? AND summeEinkauf = ? AND summeZahlung = ?";

        try (Connection con = SQLConnection.getCon();
             PreparedStatement stmtInsert = con.prepareStatement(sqlInsert);
             PreparedStatement stmtCheck = con.prepareStatement(sqlCheck))
        {

            for (Einkauf einkauf : einkaeufe)
            {
                String kundeName = einkauf.getKundeName();
                String warentyp = einkauf.getWarentypName();
                int einkaufId = einkauf.getEinkaufId();
                int kundennummer = einkauf.getKundennummer();

                // Debugging-Ausgaben
                System.out.println("Verarbeite Einkauf: " + einkaufId);
                System.out.println("Kunde: " + kundeName);
                System.out.println("Warentyp: " + warentyp);
                System.out.println("Kundennummer: " + kundennummer);

                double saldo = 0.0;
                if (kundennummer != -1)
                {
                    try
                    {
                        saldo = getSaldoFromHaushalt(con, kundennummer);
                    } catch (SQLException e)
                    {
                        System.out.println("Fehler beim Abrufen des Saldos für Kundennummer " + kundennummer + ": " + e.getMessage());
                        continue; // Überspringe diesen Einkauf, wenn der Saldo nicht abgerufen werden kann
                    }
                }

                // Überprüfen, ob der Eintrag bereits existiert
                stmtCheck.setInt(1, einkaufId);
                stmtCheck.setString(2, warentyp);
                stmtCheck.setDouble(3, einkauf.getSummeEinkauf());
                stmtCheck.setDouble(4, einkauf.getSummeZahlung());

                ResultSet rs = stmtCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0)
                {
                    System.out.println("Eintrag existiert bereits, überspringe.");
                    continue;
                }

                stmtInsert.setInt(1, einkaufId);
                stmtInsert.setString(2, warentyp);
                stmtInsert.setString(3, kundeName);
                stmtInsert.setDouble(4, saldo);
                stmtInsert.setDouble(5, einkauf.getSummeEinkauf());
                stmtInsert.setDouble(6, einkauf.getSummeZahlung());
                stmtInsert.setInt(7, einkauf.getAnzahlKinder());
                stmtInsert.setInt(8, einkauf.getAnzahlErwachsene());

                stmtInsert.addBatch();
            }

            stmtInsert.executeBatch();
            System.out.println("Daten erfolgreich in die Tabelle guthabenStatistik gespeichert.");

        } catch (SQLException e)
        {
            logError("Fehler beim Speichern in die Guthabenstatistik: ", e);
        }
    }
    /**
     */
    public String generateSQLQuery(String verteilstelle, String type)
    {
        StringBuilder query = new StringBuilder("SELECT e.einkaufId, ");

        // KundeName (Vorname und Nachname)
        query.append("CONCAT(IFNULL(f.vName, ''), ' ', IFNULL(f.nName, '')) AS kundeName, ");

        // Warentyp-Name aus warentyp
        query.append("w.name AS warentyp, ");

        // Andere benötigte Felder aus einkauf
        query.append("e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId ");

        // Tabellen verknüpfen
        query.append("FROM einkauf e ");
        query.append("JOIN familienmitglied f ON e.kunde = f.personId ");
        query.append("JOIN warentyp w ON e.warentyp = w.warentypId ");
        query.append("JOIN verteilstelle v ON e.beiVerteilstelle  = v.verteilstellenId ");

        // Dynamische Filterung der Verteilstelle
        query.append("WHERE v.bezeichnung = ? ");  // Platzhalter für die Verteilstelle

        // Optionaler Typfilter (Offene Beträge oder Guthaben)
        if ("Offene Beträge".equals(type))
        {
            query.append("AND e.summeEinkauf > e.summeZahlung ");
        } else if ("Guthaben".equals(type))
        {
            query.append("AND e.summeEinkauf <= e.summeZahlung ");
        }

        return query.toString();  // Gib die SQL-Abfrage als String zurück
    }
    /**
     */
    // Methode zum Abrufen des Saldos aus der Tabelle Haushalt
    public double getSaldoFromHaushalt(Connection con, int kundennummer) throws SQLException
    {
        if (kundennummer == -1)
        {
            throw new SQLException("Ungültige Kundennummer: -1");
        }

        String sql = "SELECT saldo FROM haushalt WHERE kundennummer = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setInt(1, kundennummer);
            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getDouble("saldo");
                } else
                {
                    throw new SQLException("Saldo für Haushalt mit kundennummer " + kundennummer + " nicht gefunden.");
                }
            }
        }
    }

    // Methode zur Fehlerprotokollierung
    private void logError(String message, SQLException e)
    {
        System.err.println(message + e.getMessage());
        e.printStackTrace();
    }

    }
