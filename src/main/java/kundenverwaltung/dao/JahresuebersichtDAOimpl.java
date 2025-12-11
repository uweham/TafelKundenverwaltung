package kundenverwaltung.dao;

import kundenverwaltung.model.statistiktool.Jahresuebersicht;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JahresuebersichtDAOimpl extends JahresuebersichtDAO
{
  /**
   */
	public ObservableList<Jahresuebersicht> getJahresuebersichtData()
	{
		List<Jahresuebersicht> personenListe = getPersonenDaten(); // Diese Methode sollte deine Personendaten zurückgeben

		Map<String, Integer> neuzugaengeProMonat = new HashMap<>();
		Map<String, Double> gesamtUmsatzHaushaltProMonat = new HashMap<>();
		Map<String, Double> gesamtUmsatzEinkaufProMonat = new HashMap<>();

		int gesamtanzahlPersonen = 0;

		for (Jahresuebersicht jahresuebersicht : personenListe)
		{
			String monat = jahresuebersicht.getMonat().toString();

			gesamtanzahlPersonen++;

			neuzugaengeProMonat.merge(monat, 1, Integer::sum);

			gesamtUmsatzHaushaltProMonat.merge(monat, jahresuebersicht.getGesamtUmsatzHaushalt(), Double::sum);
			gesamtUmsatzEinkaufProMonat.merge(monat, jahresuebersicht.getGesamtUmsatzEinkauf(), Double::sum);
		}

		ObservableList<Jahresuebersicht> jahresuebersichtData = FXCollections.observableArrayList();
		for (Map.Entry<String, Integer> entry : neuzugaengeProMonat.entrySet())
		{
			String monat = entry.getKey();
			int neuzugaenge = entry.getValue();
			double gesamtUmsatzHaushalt = gesamtUmsatzHaushaltProMonat.getOrDefault(monat, 0.0);
			double gesamtUmsatzEinkauf = gesamtUmsatzEinkaufProMonat.getOrDefault(monat, 0.0);

			jahresuebersichtData.add(new Jahresuebersicht(monat, neuzugaenge, gesamtanzahlPersonen, gesamtUmsatzHaushalt, gesamtUmsatzEinkauf));
		}

		return jahresuebersichtData;
	}
	/**
     */
	@Override
	public ObservableList<Jahresuebersicht> getJahresuebersichtByYear(int year)
	{
		ObservableList<Jahresuebersicht> filteredData = FXCollections.observableArrayList();

		String query = "SELECT "
		+
				"    MONTHNAME(h.kundeSeit) AS monat, "
		+
				"    COUNT(DISTINCT f.personId) AS anzahlPersonen, "
		+
				"    SUM(COALESCE(h.saldo, 0)) AS gesamtUmsatzHaushalt, "
		+
				"    SUM(COALESCE(e.warentyp, 0)) AS gesamtUmsatzEinkauf, "
		+
				"    COUNT(DISTINCT f2.personId) AS neuzugaenge "
		+
				"FROM "
		+
				"    haushalt h "
		+
				"LEFT JOIN "
		+
				"    familienmitglied f ON h.kundennummer = f.haushaltId AND f.gDatum IS NOT NULL "
		+
				"LEFT JOIN "
				+
				"    einkauf e ON e.kunde = h.kundennummer "
				+
				"LEFT JOIN "
				+
				"    familienmitglied f2 ON MONTH(h.kundeSeit) = MONTH(COALESCE(f.gDatum, h.kundeSeit)) "
				+
				"WHERE "
			+
				"    (YEAR(h.kundeSeit) = ?) "
			+
				"GROUP BY "
			+
				"    MONTH(h.kundeSeit);";

		try (Connection con = SQLConnection.getCon();
			 PreparedStatement stmt = con.prepareStatement(query))
		{

			stmt.setInt(1, year);

			try (ResultSet rs = stmt.executeQuery())
			{
				while (rs.next())
				{
					String monat = rs.getString("monat");
					int anzahlPersonen = rs.getInt("anzahlPersonen");
					double gesamtUmsatzHaushalt = rs.getDouble("gesamtUmsatzHaushalt");
					double gesamtUmsatzEinkauf = rs.getDouble("gesamtUmsatzEinkauf");
					int neuzugaenge = rs.getInt("neuzugaenge");

					// Hinzufügen der Daten zur Liste
					filteredData.add(new Jahresuebersicht(monat, anzahlPersonen, neuzugaenge, gesamtUmsatzHaushalt, gesamtUmsatzEinkauf));
				}
			}
		} catch (SQLException e)
		{
			System.err.println("Fehler bei der SQL-Abfrage: " + e.getMessage());
			e.printStackTrace();
		}

		return filteredData;
	}
	/**
     */
	@Override
	public void saveJahresuebersicht(Jahresuebersicht jahresuebersicht)
	{
		if (!exists(jahresuebersicht))
		{
			String query = "INSERT INTO jahresUebersichtStatistik (monat, anzahlPersonen, neuzugaenge, gesamtUmsatzHaushalt, gesamtUmsatzEinkauf) VALUES (?, ?, ?, ?, ?)";

			try (Connection con = SQLConnection.getCon();
				 PreparedStatement stmt = con.prepareStatement(query))
			{

				stmt.setString(1, jahresuebersicht.getMonat());
				stmt.setInt(2, jahresuebersicht.getAnzahlPersonen());
				stmt.setInt(3, jahresuebersicht.getNeuzugaenge());
				stmt.setDouble(4, jahresuebersicht.getGesamtUmsatzHaushalt());
				stmt.setDouble(5, jahresuebersicht.getGesamtUmsatzEinkauf());

				stmt.executeUpdate();
				System.out.println("Daten erfolgreich gespeichert.");
			} catch (SQLException e)
			{
				System.err.println("Fehler beim Speichern der Daten: " + e.getMessage());
				e.printStackTrace();
			}
		} else
		{
			System.out.println("Eintrag existiert bereits.");
		}
	}

	private boolean exists(Jahresuebersicht jahresuebersicht)
	{
		String query = "SELECT COUNT(*) FROM jahresUebersichtStatistik WHERE monat = ? AND anzahlPersonen = ? AND neuzugaenge = ? AND gesamtUmsatzHaushalt = ? AND gesamtUmsatzEinkauf = ?";

		try (Connection con = SQLConnection.getCon();
			 PreparedStatement stmt = con.prepareStatement(query))
		{

			stmt.setString(1, jahresuebersicht.getMonat());
			stmt.setInt(2, jahresuebersicht.getAnzahlPersonen());
			stmt.setInt(3, jahresuebersicht.getNeuzugaenge());
			stmt.setDouble(4, jahresuebersicht.getGesamtUmsatzHaushalt());
			stmt.setDouble(5, jahresuebersicht.getGesamtUmsatzEinkauf());

			try (ResultSet rs = stmt.executeQuery())
			{
				if (rs.next())
				{
					return rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e)
		{
			System.err.println("Fehler bei der Überprüfung der Existenz: " + e.getMessage());
			e.printStackTrace();
		}

		return false;
	}
}

