package kundenverwaltung.model.statistiktool;

import java.util.List;

public class Statistiktool
{
	private int[] gruppen;
	private int gesamtsumme;
	private int jahresergebnis;  // Jahresergebnis
	private String name; // Name der Nationalität

	/**
	 * Konstruktor für Statistiktool mit Gesamtsumme, Gruppen und Jahresergebnis.
	 *
	 * @param gesamtsumme    Die Gesamtsumme der Ergebnisse.
	 * @param gruppen        Die Ergebnisse der einzelnen Gruppen.
	 * @param jahresergebnis Das Ergebnis des spezifischen Jahres.
	 */
	public Statistiktool(int gesamtsumme, int[] gruppen, int jahresergebnis)
	{
		this.gesamtsumme = gesamtsumme;
		this.gruppen = gruppen;
		this.jahresergebnis = jahresergebnis;
	}

	/**
	 * Konstruktor für Statistiktool mit nur Gruppen.
	 *
	 * @param gruppen Die Ergebnisse der einzelnen Gruppen.
	 */
	public Statistiktool(int[] gruppen)
	{
		this.gruppen = gruppen;
	}

	/**
	 * Standard-Konstruktor für Statistiktool.
	 */
	public Statistiktool()
	{
	}
	/**
     */
	// Getter-Methoden
	public int getErgebnisForGroup(int groupNumber)
	{
	         return groupNumber;
	}

	/**
	 * Gibt die Gesamtsumme der Ergebnisse zurück.
	 *
	 * @return Die Gesamtsumme der Ergebnisse.
	 */
	public int getGesamtsumme()
	{
		return gesamtsumme;
	}

	/**
	 * Setzt die Gesamtsumme der Ergebnisse.
	 *
	 * @param gesamtsumme Die Gesamtsumme der Ergebnisse.
	 */
	public void setGesamtsumme(int gesamtsumme)
	{
		this.gesamtsumme = gesamtsumme;
	}

	/**
	 * Gibt die Ergebnisse der einzelnen Gruppen zurück.
	 *
	 * @return Ein Array mit den Ergebnissen der einzelnen Gruppen.
	 */
	public int[] getGruppen()
	{
		return gruppen;
	}

	/**
	 * Setzt die Ergebnisse der einzelnen Gruppen.
	 *
	 * @param gruppen Ein Array mit den Ergebnissen der einzelnen Gruppen.
	 */
	public void setGruppen(int[] gruppen)
	{
		this.gruppen = gruppen;
	}

	/**
	 * Gibt das Jahresergebnis zurück.
	 *
	 * @return Das Jahresergebnis.
	 */
	public int getJahresergebnis()
	{
		return jahresergebnis;
	}

	/**
	 * Setzt das Jahresergebnis.
	 *
	 * @param jahresergebnis Das Jahresergebnis.
	 */
	public void setJahresergebnis(int jahresergebnis)
	{
		this.jahresergebnis = jahresergebnis;
	}

	/**
	 * Gibt den Namen der Nationalität zurück.
	 *
	 * @return Der Name der Nationalität.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Setzt den Namen der Nationalität.
	 *
	 * @param name Der Name der Nationalität.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

}
