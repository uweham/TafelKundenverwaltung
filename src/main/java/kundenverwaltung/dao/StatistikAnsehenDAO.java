package kundenverwaltung.dao;

import kundenverwaltung.model.statistiktool.Statistiktool;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Schnittstelle für das Datenzugriffsobjekt (DAO) zur Ansicht von Statistiken.
 * Diese Schnittstelle definiert Methoden zum Speichern und Laden von Statistikdaten,
 * sowie eine Methode zum Laden von Altersstatistiken.
 */
public interface StatistikAnsehenDAO
{

	/**
	 * Speichert die Statistikdaten in einer Datei.
	 *
	 * @param statistikData Die Liste von Statistikdaten, die gespeichert werden sollen.
	 *                      Jede Statistik wird als Array von Strings dargestellt.
	 * @param filePath Der Dateipfad, in den die Statistikdaten gespeichert werden sollen.
	 * @throws IOException Wenn ein Fehler beim Schreiben der Datei auftritt.
	 */
	void saveStatistik(List<String[]> statistikData, String filePath) throws IOException;

	/**
	 * Lädt die Statistikdaten aus einer Datei.
	 *
	 * @param filePath Der Dateipfad, aus dem die Statistikdaten geladen werden sollen.
	 * @return Eine Liste von Statistikdaten, wobei jede Statistik als Array von Strings dargestellt wird.
	 * @throws IOException Wenn ein Fehler beim Lesen der Datei auftritt.
	 */
	List<String[]> loadStatistik(String filePath) throws IOException;

	/**
	 * Lädt die Altersstatistik für ein bestimmtes Jahr und eine Liste von Altersgruppen.
	 * Diese Methode wird möglicherweise nicht implementiert.
	 *
	 * @param year Das Jahr, für das die Altersstatistik geladen werden soll.
	 * @param altersgruppen Eine Liste von Altersgruppen, die in der Statistik berücksichtigt werden sollen.
	 * @return Ein Optional, das das Statistiktool-Objekt mit den geladenen Daten enthält,
	 *         oder ein leeres Optional, wenn keine Daten gefunden wurden.
	 */
	Optional<Statistiktool> loadAltersstatistik(int year, List<int[]> altersgruppen);
}
