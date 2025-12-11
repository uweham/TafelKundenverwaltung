package kundenverwaltung.dao;

import kundenverwaltung.model.statistiktool.Statistiktool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementierung der StatistikAnsehenDAO-Schnittstelle.
 * Diese Klasse enthält die Methoden zum Speichern und Laden von Statistikdaten in und aus CSV-Dateien.
 * @author Fadumo Noor
 */
public class StatistikAnsehenDAOimpl implements StatistikAnsehenDAO
{

	/**
	 * Speichert die Statistikdaten in einer CSV-Datei.
	 * @param statistikData Die zu speichernden Statistikdaten, als Liste von String-Arrays.
	 * @param filePath Der Pfad zur Datei, in die die Daten gespeichert werden sollen.
	 * @throws IOException Wenn ein Fehler beim Schreiben in die Datei auftritt.
	 */
	@Override
	public void saveStatistik(List<String[]> statistikData, String filePath) throws IOException
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)))
		{
			for (String[] row : statistikData)
			{
				writer.write(String.join(",", row));
				writer.newLine();
			}
		}
	}

	/**
	 * Lädt die Statistikdaten aus einer CSV-Datei.
	 * @param filePath Der Pfad zur Datei, aus der die Daten geladen werden sollen.
	 * @return Eine Liste von String-Arrays, die die geladenen Statistikdaten enthalten.
	 * @throws IOException Wenn ein Fehler beim Lesen der Datei auftritt.
	 */
	@Override
	public List<String[]> loadStatistik(String filePath) throws IOException
	{
		List<String[]> statistikData = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				String[] row = line.split(",");
				statistikData.add(row);
			}
		}
		return statistikData;
	}

	/**
	 * Diese Methode wird nicht implementiert.
	 * @param year Das Jahr, für das die Altersstatistik geladen werden soll.
	 * @param altersgruppen Eine Liste von Altersgruppen, die in der Statistik berücksichtigt werden sollen.
	 * @return Immer null, da die Methode nicht implementiert ist.
	 */
	@Override
	public Optional<Statistiktool> loadAltersstatistik(int year, List<int[]> altersgruppen)
	{
		return null;
	}
}
