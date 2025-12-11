package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import kundenverwaltung.model.Anrede;
import kundenverwaltung.model.Berechtigung;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Gender;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Nation;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class FamilienmitgliedDAOimpl implements FamilienmitgliedDAO
{
	private static final int ILLEGAL_PERSON_ID = -1;
	@SuppressWarnings("unused")
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	private String databaseName = PropertiesFileController.getDbName();
    private final NationDAO nationDAO = new NationDAOimpl();

	/**
	 * Creates a new Familienmitglied in the database.
	 *
	 * @param familienmitglied the Familienmitglied object to be created
	 * @return true if the creation was successful, false otherwise
	 */
	@Override
	public boolean create(Familienmitglied familienmitglied)
	{

		//String Server
		String idAbfrage = "Select AUTO_INCREMENT "
				+ "FROM INFORMATION_SCHEMA.TABLES "
				+ "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
				+ "AND TABLE_NAME = 'familienmitglied'";

		String sql = "INSERT INTO familienmitglied("
				+ "haushaltId,"
				+ "anredeId,"
				+ "genderId,"
				+ "vName,"
				+ "nName,"
				+ "gDatum,"
				+ "bemerkung,"
				+ "haushaltsVorstand,"
				+ "einkaufsBerechtigt,"
				+ "gebuehrenBefreiung,"
				+ "nation,"
				+ "berechtigungId,"
				+ "aufAusweis,"
				+ "dseSubmitted,"
				+ "hinzugefuegtAm,"
				+ "geaendertAm)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try
		{
			Connection con = SQLConnection.getCon();
			Statement smtID = con.createStatement();
			ResultSet count = smtID.executeQuery(idAbfrage);
			count.next();
			int personId = count.getInt(1);
			familienmitglied.setPersonId(personId);
			smtID.close();

			PreparedStatement smt = con.prepareStatement(sql);
			smt.setInt(1, familienmitglied.getHaushalt().getKundennummer());

			if (familienmitglied.getAnrede() == null)
			{
				smt.setObject(2, null);
			}
			else
			{
				smt.setInt(2, familienmitglied.getAnrede().getAnredeId());
			}

			smt.setInt(3, familienmitglied.getGender().getGenderId());
			smt.setString(4, familienmitglied.getvName());
			smt.setString(5, familienmitglied.getnName());
			smt.setDate(6, Date.valueOf(familienmitglied.getgDatum()));
			smt.setString(7, familienmitglied.getBemerkung());
			smt.setBoolean(8, familienmitglied.isHaushaltsVorstand());
			smt.setBoolean(9, familienmitglied.isEinkaufsBerechtigt());
			smt.setBoolean(10, familienmitglied.isGebuehrenBefreiung());
			smt.setInt(11, familienmitglied.getNation().getNationId());
			smt.setInt(12, familienmitglied.getBerechtigung().getBerechtigungId());
			smt.setBoolean(13, familienmitglied.isAufAusweis());
			smt.setBoolean(14, familienmitglied.dseSubmitted());

			if (familienmitglied.getHinzugefuegtAm() != null)
			{
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				smt.setString(15, familienmitglied.getHinzugefuegtAm().format(dtf));
				System.out.println(java.sql.Timestamp.valueOf(familienmitglied.getHinzugefuegtAm().format(dtf)));
				System.out.println(familienmitglied.getHinzugefuegtAm().format(dtf));
				System.out.println(familienmitglied.getHinzugefuegtAm());
				System.out.println(Timestamp.valueOf(familienmitglied.getHinzugefuegtAm()));
			} else
			{
				smt.setObject(15, null);
			}
			if (familienmitglied.getGeaendertAm() != null)
			{
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				smt.setTimestamp(16, Timestamp.valueOf(familienmitglied.getGeaendertAm().format(dtf)));
			} else
			{
				smt.setObject(16, null);
			}
			System.out.println(smt);
			smt.executeUpdate();
			smt.close();
			return true;

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Familienmitglied einfügen klappt nicht");
		}
		return false;
	}


	/**
	 * Updates an existing Familienmitglied in the database.
	 *
	 * @param familienmitglied the Familienmitglied object containing the updated values
	 * @return true if the update was successful, false otherwise
	 */
	@Override
	public boolean update(Familienmitglied familienmitglied)
	{
		String sql = "UPDATE familienmitglied " + "SET haushaltId = ?, " + "anredeId = ?, " + "genderId = ?, " + "vName = ?, " + "nName = ?, " + "gDatum = ?, " + "bemerkung = ?, " + "haushaltsvorstand = ?, " + "einkaufsBerechtigt = ?, " + "gebuehrenBefreiung = ?, " + "nation = ?, " + "berechtigungId = ?, " + "aufAusweis = ?, " + "dseSubmitted = ?, " + "hinzugefuegtAm = ?, " + "geaendertAm = ? " + "WHERE personId = ?";

		try
		{
			Connection con = SQLConnection.getCon();
			PreparedStatement smt = con.prepareStatement(sql);
			smt.setInt(1, familienmitglied.getHaushalt().getKundennummer());

			if (familienmitglied.getAnrede() == null)
			{
				smt.setObject(2, null);
			}
			else
			{
				smt.setInt(2, familienmitglied.getAnrede().getAnredeId());
			}

			smt.setInt(3, familienmitglied.getGender().getGenderId());
			smt.setString(4, familienmitglied.getvName());
			smt.setString(5, familienmitglied.getnName());
			smt.setDate(6, Date.valueOf(familienmitglied.getgDatum()));
			smt.setString(7, familienmitglied.getBemerkung());
			smt.setBoolean(8, familienmitglied.isHaushaltsVorstand());
			smt.setBoolean(9, familienmitglied.isEinkaufsBerechtigt());
			smt.setBoolean(10, familienmitglied.isGebuehrenBefreiung());
			smt.setInt(11, familienmitglied.getNation().getNationId());
			smt.setInt(12, familienmitglied.getBerechtigung().getBerechtigungId());
			smt.setBoolean(13, familienmitglied.isAufAusweis());
			smt.setBoolean(14, familienmitglied.dseSubmitted());
			if (familienmitglied.getHinzugefuegtAm() != null)
			{
				smt.setTimestamp(15, Timestamp.valueOf(familienmitglied.getHinzugefuegtAm()));
			} else
			{
				smt.setObject(15, null);
			}
			if (familienmitglied.getGeaendertAm() != null)
			{
				smt.setTimestamp(16, Timestamp.valueOf(familienmitglied.getGeaendertAm()));
			} else
			{
				smt.setObject(16, null);
			}
			smt.setInt(17, familienmitglied.getPersonId());
			smt.executeUpdate();
			smt.close();
			return true;

		} catch (SQLException e)
			{
				e.printStackTrace();
				System.out.println("Familienmitglied Update klappt nicht");
			}

		return false;
	}
	/**
     */
	// Neue Methode: Liest Familienmitglieder, die zu einer bestimmten Verteilstelle gehören
    @Override
    public List<Familienmitglied> readByVerteilstelle(Verteilstelle verteilstelle)
    {
        List<Familienmitglied> familienMitglieder = new ArrayList<>();
        String sql = "SELECT * FROM familienmitglied WHERE verteilstellenId = ?"; // Passen Sie den Spaltennamen ggf. an
        try (Connection con = SQLConnection.getCon();
             PreparedStatement pstmt = con.prepareStatement(sql))
        {
            pstmt.setInt(1, verteilstelle.getVerteilstellenId());
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    Familienmitglied fm = createFamilienmitgliedFromResultSet(rs);
                    familienMitglieder.add(fm);
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return familienMitglieder;
    }

    // Hilfsmethode, um ein Familienmitglied-Objekt aus dem ResultSet zu erstellen
    private Familienmitglied createFamilienmitgliedFromResultSet(ResultSet rs) throws SQLException
    {
        HaushaltDAO haushaltDAO = new HaushaltDAOimpl();
        BerechtigungDAO berechtigungDAO = new BerechtigungDAOimpl();
        int personId = rs.getInt("personId");
        Haushalt haushalt = haushaltDAO.read(rs.getInt("haushaltId"));
        int anredeID = rs.getInt("anredeId");
        Anrede anrede = new Anrede(anredeID);
        int genderID = rs.getInt("genderId");
        Gender gender = new Gender(genderID);
        String vName = rs.getString("vName");
        String nName = rs.getString("nName");
        LocalDate gDatum = null;
        Date gDatumSQLDate = rs.getDate("gDatum");
        if (gDatumSQLDate != null)
        {
            gDatum = gDatumSQLDate.toLocalDate();
        }
        String bemerkung = rs.getString("bemerkung");
        boolean haushaltsVorstand = rs.getBoolean("haushaltsVorstand");
        boolean einkaufsberechtigt = rs.getBoolean("einkaufsberechtigt");
        boolean gebuehrenBefreiung = rs.getBoolean("gebuehrenBefreiung");
        Berechtigung berechtigung = berechtigungDAO.read(rs.getInt("berechtigungId"));
        Nation nation = nationDAO.read(rs.getInt("nation"));
        boolean aufAusweis = rs.getBoolean("aufAusweis");
        boolean dseSubmitted = rs.getBoolean("dseSubmitted");
        LocalDateTime hinzugefuegtAm = null;
        Timestamp hinzugefuegtAmTS = rs.getTimestamp("hinzugefuegtAm");
        if (hinzugefuegtAmTS != null)
        {
            hinzugefuegtAm = hinzugefuegtAmTS.toLocalDateTime();
        }
        LocalDateTime geaendertAm = null;
        Timestamp geaendertAmTS = rs.getTimestamp("geaendertAm");
        if (geaendertAmTS != null)
        {
            geaendertAm = geaendertAmTS.toLocalDateTime();
        }
        return new Familienmitglied(personId, haushalt, anrede, gender, vName, nName, gDatum, bemerkung,
                haushaltsVorstand, einkaufsberechtigt, gebuehrenBefreiung, nation, berechtigung, aufAusweis,
                dseSubmitted, hinzugefuegtAm, geaendertAm);
    }

	/**
	 * Deletes a Familienmitglied from the database.
	 *
	 * @param familienmitglied the Familienmitglied object to be deleted
	 * @return true if the deletion was successful, false otherwise
	 */
	@Override
	public boolean delete(Familienmitglied familienmitglied)
	{
		String sqlDeletePerson = "Delete From familienmitglied Where personId= ?";

		try
		{
			Connection con = SQLConnection.getCon();
			PreparedStatement smtDelete = con.prepareStatement(sqlDeletePerson);
			smtDelete.setInt(1, familienmitglied.getPersonId());
			smtDelete.executeUpdate();
			smtDelete.close();

			return true;

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Familienmitglied Loeschen klappt nicht");
		}

		return false;
	}

	/**
	 * Inserts a deleted Familienmitglied into the deleted_MemberOfTheFamily table.
	 *
	 * @param memberOfTheFamily the Familienmitglied object to be inserted
	 * @param reasonDelete the reason for deletion
	 * @return true if the insertion was successful, false otherwise
	 */
	@Override
	public boolean insertDeletedMemberOfTheFamiliy(Familienmitglied memberOfTheFamily, String reasonDelete)
	{
		String sqlInsertDeletedMemberOfTheFamiliy = "INSERT INTO deleted_MemberOfTheFamily "
										+ "(householdId, firstName, surname, birthday, reasonDelete, deletedOn) "
										+ "VALUES (?,?,?,?,?, curdate())";
		try
		{
			Connection con = SQLConnection.getCon();

			PreparedStatement statement = con.prepareStatement(sqlInsertDeletedMemberOfTheFamiliy);
			statement.setInt(1, memberOfTheFamily.getHaushalt().getKundennummer());
			statement.setString(2, memberOfTheFamily.getvName());
			statement.setString(3, memberOfTheFamily.getnName());
			statement.setDate(4, memberOfTheFamily.getGeburtsdatum());
			statement.setString(5, reasonDelete);
			statement.executeUpdate();
			statement.close();
			con.close();

			Boolean resultDelete = delete(memberOfTheFamily);

			return resultDelete;
		} catch (SQLException e)
			{
				e.printStackTrace();
				System.out.println("Familienmitglied konnte nicht den gelöschten Familienmitglieder hinzugefügt werden.");

				return false;
			}
	}

	/**
	 * Gets the household director (head) for a given household.
	 *
	 * @param householdId the ID of the household
	 * @return the person ID of the household director, or ILLEGAL_PERSON_ID if not found
	 */
	@Override
	public int getHousholdDirector(int householdId)
	{
		String sql = "SELECT * FROM familienmitglied WHERE haushaltsVorstand = 1 AND haushaltId = ?";
		try
		{
			Connection con = SQLConnection.getCon();
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, householdId);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();

			return resultSet.getInt("personId");

		} catch (SQLException e)
			{
				e.printStackTrace();
			}
		return ILLEGAL_PERSON_ID;
	}
	/**
	 * Gets the maximum person ID from the familienmitglied table.
	 *
	 * @return the maximum person ID, or ILLEGAL_PERSON_ID if not found
	 */
	@Override
	public int getMaxPersonId()
	{
		String sql = "SELECT MAX(personId) FROM familienmitglied";

		try
		{
			new SQLConnection();
			Connection connection = SQLConnection.getCon();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			resultSet.next();
			int result = resultSet.getInt("MAX(personId)");
			System.out.println("Letzter PersonId: " + result);

			return resultSet.getInt("MAX(personId)");
		} catch (SQLException e)
			{
				e.printStackTrace();
			}
		return ILLEGAL_PERSON_ID;
	}

	/**
	 * Reads a Familienmitglied from the database.
	 *
	 * @param personId the ID of the Familienmitglied to be read
	 * @return the Familienmitglied object, or null if not found
	 */
	@Override
	public Familienmitglied read(int personId)
	{
		String sql = "SELECT * FROM familienmitglied WHERE personId = ?";
		try
		{
			HaushaltDAO haushaltdao = new HaushaltDAOimpl();
			BerechtigungDAO berechtigungDao = new BerechtigungDAOimpl();
			@SuppressWarnings("unused")
      NationDAO nationDao = new NationDAOimpl();

			Connection con = SQLConnection.getCon();
			PreparedStatement smt = con.prepareStatement(sql);
			smt.setInt(1, personId);
			ResultSet familienmitgliedResult = smt.executeQuery();
			familienmitgliedResult.next();
			Haushalt haushalt = haushaltdao.read(familienmitgliedResult.getInt("haushaltId"));
			int anredeID = familienmitgliedResult.getInt("anredeId");
			Anrede anrede = new Anrede(anredeID);
			int genderID = familienmitgliedResult.getInt("genderId");
			Gender gender = new Gender(genderID);
			String vName = familienmitgliedResult.getString("vName");
			String nName = familienmitgliedResult.getString("nName");
			LocalDate gDatum = familienmitgliedResult.getDate("gDatum").toLocalDate();
			String bemerkung = familienmitgliedResult.getString("bemerkung");
			Boolean haushaltsVorstand = familienmitgliedResult.getBoolean("haushaltsVorstand");
			Boolean einkaufsberechtigt = familienmitgliedResult.getBoolean("einkaufsberechtigt");
			Boolean gebuehrenBefreiung = familienmitgliedResult.getBoolean("gebuehrenBefreiung");
			Nation nation = nationDAO.read(familienmitgliedResult.getInt("nation"));
			Berechtigung berechtigung = berechtigungDao.read(familienmitgliedResult.getInt("berechtigungId"));
			Boolean aufAusweis = familienmitgliedResult.getBoolean("aufAusweis");
			Boolean dseSubmitted = familienmitgliedResult.getBoolean("dseSubmitted");
			LocalDateTime hinzugefuegtAm = familienmitgliedResult.getTimestamp("hinzugefuegtAm").toLocalDateTime();
			LocalDateTime geandertAm = familienmitgliedResult.getTimestamp("hinzugefuegtAm").toLocalDateTime();
			Familienmitglied familienMitglied = new Familienmitglied(personId, haushalt, anrede, gender, vName, nName, gDatum, bemerkung, haushaltsVorstand, einkaufsberechtigt, gebuehrenBefreiung, nation, berechtigung, aufAusweis, dseSubmitted, hinzugefuegtAm, geandertAm);

			return familienMitglied;
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Familienmitglied Lesen klappt nicht");
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Fehler");
		}

		return null;
	}
	/**
	 * Gets all Familienmitglieder for a given household.
	 *
	 * @param haushaltsid the ID of the household
	 * @return an ArrayList of Familienmitglied objects, or null if not found
	 */
	@Override
	public ArrayList<Familienmitglied> getAllFamilienmitglieder(int haushaltsid)
	{

		String sql = "SELECT * FROM familienmitglied WHERE haushaltId = ?";
		try
		{
			ArrayList<Familienmitglied> familienmitgliedListe = new ArrayList<>();
			HaushaltDAO haushaltdao = new HaushaltDAOimpl();
			BerechtigungDAO berechtigungDAO = new BerechtigungDAOimpl();
			@SuppressWarnings("unused")
      NationDAO nationDAO = new NationDAOimpl();

			Connection con = SQLConnection.getCon();
			PreparedStatement smt = con.prepareStatement(sql);
			smt.setInt(1, haushaltsid);
			ResultSet familienmitgliedResult = smt.executeQuery();
			Haushalt haushalt = haushaltdao.read(haushaltsid);

			while (familienmitgliedResult.next())
			{
				int anredeID = familienmitgliedResult.getInt("anredeId");
				int genderID = familienmitgliedResult.getInt("genderId");
				int personId = familienmitgliedResult.getInt("personId");
				Anrede anrede = new Anrede(anredeID);
				Gender gender = new Gender(genderID);
				String vName = familienmitgliedResult.getString("vName");
				String nName = familienmitgliedResult.getString("nName");
				Date gDatumSQLDate = familienmitgliedResult.getDate("gDatum");
				LocalDate gDatum = null;
				if (gDatumSQLDate != null)
				{
					gDatum = gDatumSQLDate.toLocalDate();
				}
				String bemerkung = familienmitgliedResult.getString("bemerkung");
				Boolean haushaltsVorstand = familienmitgliedResult.getBoolean("haushaltsVorstand");
				Boolean einkaufsberechtigt = familienmitgliedResult.getBoolean("einkaufsberechtigt");
				Boolean gebuehrenBefreiung = familienmitgliedResult.getBoolean("gebuehrenBefreiung");
				//Nation muss hier hin
				Berechtigung berechtigung = berechtigungDAO.read(familienmitgliedResult.getInt("berechtigungId"));
				Nation nation = nationDAO.read(familienmitgliedResult.getInt("nation"));
				Boolean aufAusweis = familienmitgliedResult.getBoolean("aufAusweis");
				Boolean dseSubmitted = familienmitgliedResult.getBoolean("dseSubmitted");
				Timestamp hinzugefuegtAmSQLTimestamp = familienmitgliedResult.getTimestamp("hinzugefuegtAm");
				LocalDateTime hinzugefuegtAm = null;
				if (hinzugefuegtAmSQLTimestamp != null)
				{
					hinzugefuegtAm = hinzugefuegtAmSQLTimestamp.toLocalDateTime();
				}
				Timestamp geandertAmSQLTimestamp = familienmitgliedResult.getTimestamp("geaendertAm");
				LocalDateTime geandertAm = null;
				if (hinzugefuegtAmSQLTimestamp != null)
				{
					geandertAm = geandertAmSQLTimestamp.toLocalDateTime();
				}
				Familienmitglied familienMitglied = new Familienmitglied(personId, haushalt, anrede, gender, vName, nName, gDatum, bemerkung, haushaltsVorstand, einkaufsberechtigt, gebuehrenBefreiung, nation, berechtigung, aufAusweis, dseSubmitted, hinzugefuegtAm, geandertAm);
				familienmitgliedListe.add(familienMitglied);
			}

			return familienmitgliedListe;
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Familienmitglieder lesen klappt nicht");
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Fehler");
		}

		return null;
	}

	/**
	 * Gets all Familienmitglieder from the database.
	 *
	 * @return an ArrayList of Familienmitglied objects, or null if not found
	 */
	public ArrayList<Familienmitglied> getAllFamilienmitglieder()
	{

		String sql = "SELECT * FROM familienmitglied WHERE einkaufsBerechtigt = true";
		try
		{
			ArrayList<Familienmitglied> familienmitgliedListe = new ArrayList<>();
			HaushaltDAO haushaltdao = new HaushaltDAOimpl();
			BerechtigungDAO berechtigungDAO = new BerechtigungDAOimpl();
			@SuppressWarnings("unused")
      NationDAO nationDAO = new NationDAOimpl();

			Connection con = SQLConnection.getCon();
			PreparedStatement smt = con.prepareStatement(sql);
			// smt.setInt(1, haushaltsid);
			ResultSet familienmitgliedResult = smt.executeQuery();


			while (familienmitgliedResult.next())
			{
				Haushalt haushalt = haushaltdao.read(familienmitgliedResult.getInt("haushaltId"));
				int anredeID = familienmitgliedResult.getInt("anredeId");
				int genderID = familienmitgliedResult.getInt("genderId");
				int personId = familienmitgliedResult.getInt("personId");
				Anrede anrede = new Anrede(anredeID);
				Gender gender = new Gender(genderID);
				String vName = familienmitgliedResult.getString("vName");
				String nName = familienmitgliedResult.getString("nName");
				LocalDate gDatum = familienmitgliedResult.getDate("gDatum").toLocalDate();

				String bemerkung = familienmitgliedResult.getString("bemerkung");
				Boolean haushaltsVorstand = familienmitgliedResult.getBoolean("haushaltsVorstand");
				Boolean einkaufsberechtigt = familienmitgliedResult.getBoolean("einkaufsberechtigt");
				Boolean gebuehrenBefreiung = familienmitgliedResult.getBoolean("gebuehrenBefreiung");
				//Nation muss hier hin
				Berechtigung berechtigung = berechtigungDAO.read(familienmitgliedResult.getInt("berechtigungId"));
				Nation nation = nationDAO.read(familienmitgliedResult.getInt("nation"));
				Boolean aufAusweis = familienmitgliedResult.getBoolean("aufAusweis");
				Boolean dseSubmitted = familienmitgliedResult.getBoolean("dseSubmitted");
				LocalDateTime hinzugefuegtAm = familienmitgliedResult.getTimestamp("hinzugefuegtAm").toLocalDateTime();
				LocalDateTime geandertAm = familienmitgliedResult.getTimestamp("hinzugefuegtAm").toLocalDateTime();
				Familienmitglied familienMitglied = new Familienmitglied(personId, haushalt, anrede, gender, vName, nName, gDatum, bemerkung, haushaltsVorstand, einkaufsberechtigt, gebuehrenBefreiung, nation, berechtigung, aufAusweis, dseSubmitted, hinzugefuegtAm, geandertAm);
				familienmitgliedListe.add(familienMitglied);
			}

			return familienmitgliedListe;
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Familienmitglieder lesen klappt nicht");
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Fehler");
		}

		return null;
	}

	/**
	 * Gets all Familienmitglieder with a given last name.
	 *
	 * @param name the last name to search for
	 * @return an ArrayList of Familienmitglied objects, or null if not found
	 */
	@Override
	public ArrayList<Familienmitglied> getAllFamilienmitglieder(String name)
	{

		String sql = "SELECT * FROM familienmitglied WHERE nName = ?";
		try
		{
			ArrayList<Familienmitglied> familienmitgliedListe = new ArrayList<>();
			HaushaltDAO haushaltdao = new HaushaltDAOimpl();
			BerechtigungDAO berechtigungDAO = new BerechtigungDAOimpl();
			@SuppressWarnings("unused")
      NationDAO nationDAO = new NationDAOimpl();

			Connection con = SQLConnection.getCon();
			PreparedStatement smt = con.prepareStatement(sql);
			smt.setString(1, name);
			ResultSet familienmitgliedResult = smt.executeQuery();


			while (familienmitgliedResult.next())
			{
				Haushalt haushalt = haushaltdao.read(familienmitgliedResult.getInt("haushaltId"));
				int anredeID = familienmitgliedResult.getInt("anredeId");
				int genderID = familienmitgliedResult.getInt("genderId");
				int personId = familienmitgliedResult.getInt("personId");
				Anrede anrede = new Anrede(anredeID);
				Gender gender = new Gender(genderID);
				String vName = familienmitgliedResult.getString("vName");
				String nName = familienmitgliedResult.getString("nName");
				LocalDate gDatum = familienmitgliedResult.getDate("gDatum").toLocalDate();

				String bemerkung = familienmitgliedResult.getString("bemerkung");
				Boolean haushaltsVorstand = familienmitgliedResult.getBoolean("haushaltsVorstand");
				Boolean einkaufsberechtigt = familienmitgliedResult.getBoolean("einkaufsberechtigt");
				Boolean gebuehrenBefreiung = familienmitgliedResult.getBoolean("gebuehrenBefreiung");
				Berechtigung berechtigung = berechtigungDAO.read(familienmitgliedResult.getInt("berechtigungId"));
				Nation nation = nationDAO.read(familienmitgliedResult.getInt("nation"));
				Boolean aufAusweis = familienmitgliedResult.getBoolean("aufAusweis");
				Boolean dseSubmitted = familienmitgliedResult.getBoolean("dseSubmitted");
				LocalDateTime hinzugefuegtAm = familienmitgliedResult.getTimestamp("hinzugefuegtAm").toLocalDateTime();
				LocalDateTime geandertAm = familienmitgliedResult.getTimestamp("hinzugefuegtAm").toLocalDateTime();
				Familienmitglied familienMitglied = new Familienmitglied(personId, haushalt, anrede, gender, vName, nName, gDatum, bemerkung, haushaltsVorstand, einkaufsberechtigt, gebuehrenBefreiung, nation, berechtigung, aufAusweis, dseSubmitted, hinzugefuegtAm, geandertAm);
				familienmitgliedListe.add(familienMitglied);
			}

			return familienmitgliedListe;
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Familienmitglieder lesen klappt nicht");
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Fehler");
		}

		return null;
	}

	/**
	 * Gets all Familienmitglieder based on search criteria.
	 *
	 * @param suche the search string
	 * @param filter the search filter
	 * @param genaueSuche whether to perform an exact search
	 * @return an ArrayList of Familienmitglied objects, or null if not found
	 */
	//Kann man wahrscheinlich löschen, ist noch von der alten Suchfunktion...
	@Override
	public ArrayList<Familienmitglied> getAllFamilienmitglieder(String suche, int filter, boolean genaueSuche)
	{
		String sql = "SELECT * FROM familienmitglied";
		if (genaueSuche)
		{
			switch (filter)
			{
				case 0:
					sql = "SELECT * FROM familienmitglied WHERE haushaltId = ? and not haushaltId = 1";
					break;
				case 1:
					sql = "SELECT * FROM familienmitglied WHERE nName = ? and not haushaltId = 1";
					break;
				case 2:
					sql = "SELECT * FROM familienmitglied WHERE vName = ? and not haushaltId = 1";
					break;
				case 3:
					sql = "Select * from familienmitglied inner join haushalt on haushaltId=kundennummer Where Strasse = ? and not haushaltId = 1";
					break;
				case 4:
					sql = "Select * from familienmitglied inner join haushalt on haushaltId=kundennummer inner join plz on haushalt.plz = plz.plzId Where (plz.plz = ? OR ort = ?) and not haushaltId = 1";
					break;
				case 5:
					sql = "Select * from familienmitglied inner join haushalt on haushaltId=kundennummer inner join verteilstelle on haushalt.verteilstellenId = verteilstelle.verteilstellenId Where verteilstelle.bezeichnung = ? and not haushaltId = 1";
					break;
				case 6:
					sql = "Select * from familienmitglied inner join haushalt on haushaltId=kundennummer inner join ausgabegruppe on haushalt.ausgabeGruppeId=ausgabegruppe.ausgabegruppeId Where ausgabegruppe.name = ? and not haushaltId = 1";
        default:
          break;
			}
		} else
		{
			switch (filter)
			{
				case 0:
					sql = "SELECT * FROM familienmitglied WHERE haushaltId LIKE ? and not haushaltId = 1";
					break;
				case 1:
					sql = "SELECT * FROM familienmitglied WHERE nName LIKE ? and not haushaltId = 1";
					break;
				case 2:
					sql = "SELECT * FROM familienmitglied WHERE vName LIKE ? and not haushaltId = 1";
					break;
				case 3:
					sql = "Select * from familienmitglied inner join haushalt on haushaltId=kundennummer Where Strasse LIKE ? and not haushaltId = 1";
					break;
				case 4:
					sql = "Select * from familienmitglied inner join haushalt on haushaltId=kundennummer inner join plz on haushalt.plz = plz.plzId Where (plz.plz LIKE ? OR ort LIKE ?) and not haushaltId = 1";
					break;
				case 5:
					sql = "Select * from familienmitglied inner join haushalt on haushaltId=kundennummer inner join verteilstelle on haushalt.verteilstellenId = verteilstelle.verteilstellenId Where verteilstelle.bezeichnung LIKE ? and not haushaltId = 1";
					break;
				case 6:
					sql = "Select * from familienmitglied inner join haushalt on haushaltId=kundennummer inner join ausgabegruppe on haushalt.ausgabeGruppeId=ausgabegruppe.ausgabegruppeId Where ausgabegruppe.name LIKE ? and not haushaltId = 1";
        default:
          break;
			}
		}

		try
		{
			ArrayList<Familienmitglied> familienmitgliedListe = new ArrayList<>();
			HaushaltDAO haushaltdao = new HaushaltDAOimpl();
			BerechtigungDAO berechtigungDAO = new BerechtigungDAOimpl();
			@SuppressWarnings("unused")
      NationDAO nationDAO = new NationDAOimpl();

			Connection con = SQLConnection.getCon();
			PreparedStatement smt = con.prepareStatement(sql);
			if (genaueSuche)
			{
				smt.setString(1, suche);
				if (filter == 4)
				{
					smt.setString(2, suche);
				}
			} else
			{
				smt.setString(1, "%" + suche + "%");
				if (filter == 4)
				{
					smt.setString(2, "%" + suche + "%");
				}
			}
			ResultSet familienmitgliedResult = smt.executeQuery();


			while (familienmitgliedResult.next())
			{
				Haushalt haushalt = haushaltdao.read(familienmitgliedResult.getInt("haushaltId"));
				int anredeID = familienmitgliedResult.getInt("anredeId");
				int genderID = familienmitgliedResult.getInt("genderId");
				int personId = familienmitgliedResult.getInt("personId");
				Anrede anrede = new Anrede(anredeID);
				Gender gender = new Gender(genderID);
				String vName = familienmitgliedResult.getString("vName");
				String nName = familienmitgliedResult.getString("nName");
				Date gDatumSQLDate = familienmitgliedResult.getDate("gDatum");
				LocalDate gDatum = null;
				if (gDatumSQLDate != null)
				{
					gDatum = gDatumSQLDate.toLocalDate();

				}

				String bemerkung = familienmitgliedResult.getString("bemerkung");
				Boolean haushaltsVorstand = familienmitgliedResult.getBoolean("haushaltsVorstand");
				Boolean einkaufsberechtigt = familienmitgliedResult.getBoolean("einkaufsberechtigt");
				Boolean gebuehrenBefreiung = familienmitgliedResult.getBoolean("gebuehrenBefreiung");
				//Nation muss hier hin
				Berechtigung berechtigung = berechtigungDAO.read(familienmitgliedResult.getInt("berechtigungId"));
				Nation nation = nationDAO.read(familienmitgliedResult.getInt("nation"));
				Boolean aufAusweis = familienmitgliedResult.getBoolean("aufAusweis");
				Boolean dseSubmitted = familienmitgliedResult.getBoolean("dseSubmitted");
				LocalDateTime hinzugefuegtAm = familienmitgliedResult.getTimestamp("hinzugefuegtAm").toLocalDateTime();
				LocalDateTime geandertAm = familienmitgliedResult.getTimestamp("hinzugefuegtAm").toLocalDateTime();
				Familienmitglied familienMitglied = new Familienmitglied(personId, haushalt, anrede, gender, vName, nName, gDatum, bemerkung, haushaltsVorstand, einkaufsberechtigt, gebuehrenBefreiung, nation, berechtigung, aufAusweis, dseSubmitted, hinzugefuegtAm, geandertAm);
				familienmitgliedListe.add(familienMitglied);
			}

			return familienmitgliedListe;
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Familienmitglieder lesen klappt nicht");
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Fehler");
		}

		return null;
	}
	/**
	 * Migrates Familienmitglied data from an old database to a new database.
	 *
	 * @param conOldDb the connection to the old database
	 * @param conNewdDb the connection to the new database
	 * @return true if the migration was successful, false otherwise
	 */
	@Override
	public boolean migrate(Connection conOldDb, Connection conNewdDb)
	{
		String sqlRead = "SELECT * FROM lita_familienmitglieder";
		String sql = "INSERT INTO Familienmitglied(" + "personId," + "haushaltId," + "anredeId," + "genderId," + "vName," + "nName," + "gDatum," + "bemerkung," + "haushaltsVorstand," + "einkaufsBerechtigt," + "gebuehrenBefreiung," + "nation," + "berechtigungId," + "aufAusweis," + "dseSubmitted," + "hinzugefuegtAm," + "geaendertAm)" + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try
		{
			//Connection alteDbCon = AlteDbSQLConnection.getCon();    //zu migrierende DB
			//Connection con = SQLConnection.getCon();               //neue DB

			PreparedStatement smt = conOldDb.prepareStatement(sqlRead);
			ResultSet personResult = smt.executeQuery();

			while (personResult.next())
			{
				int personId = personResult.getInt("person_Nr");
				int haushaltId = personResult.getInt("kundennummer");
				int anredeId = personResult.getInt("anrede_id");
				int genderId = 74;
				String vName = personResult.getString("Vorname");
				String nName = personResult.getString("Nachname");
				Date gDatum = personResult.getDate("Geburtsdatum");
				String bermerkung = personResult.getString("bemerkung");
				Boolean haushaltsvorstand = personResult.getBoolean("haushaltsvorstand");
				Boolean einkaufsberechtigt = personResult.getBoolean("einkaufberechtigt");
				Boolean gebuehrenbefreiung = personResult.getBoolean("gebuehrenbefreiung");
				int nation = personResult.getInt("nation");
				int berechtigungId = personResult.getInt("berechtigung");
				Boolean aufAusweis = personResult.getBoolean("aufAusweis");
				Boolean dseSubmitted = false;
				Timestamp hinzugefuegt = personResult.getTimestamp("hinzugefuegt");
				Timestamp geaendert = personResult.getTimestamp("geandert");        //In der alten DB auch falsch geschrieben


				try
				{
					PreparedStatement smt2 = conNewdDb.prepareStatement(sql);
					smt2.setInt(1, personId);
					smt2.setInt(2, haushaltId);
					smt2.setInt(3, anredeId);
					smt2.setInt(4, genderId);
					smt2.setString(5, vName);
					smt2.setString(6, nName);
					smt2.setDate(7, gDatum);
					smt2.setString(8, bermerkung);
					smt2.setBoolean(9, haushaltsvorstand);
					smt2.setBoolean(10, einkaufsberechtigt);
					smt2.setBoolean(11, gebuehrenbefreiung);
					smt2.setInt(12, nation);
					smt2.setInt(13, berechtigungId);
					smt2.setBoolean(14, aufAusweis);
					smt2.setBoolean(15, dseSubmitted);
					smt2.setTimestamp(16, hinzugefuegt);
					smt2.setTimestamp(17, geaendert);
					smt2.executeUpdate();
					smt2.close();

				} catch (SQLException e)
				{
					e.printStackTrace();
					System.out.println("Familienmitglied Migrieren klappt nicht");
				}
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return false;

	}


}
