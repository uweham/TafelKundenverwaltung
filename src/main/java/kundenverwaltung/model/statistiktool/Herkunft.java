package kundenverwaltung.model.statistiktool;

public class Herkunft
{
	private String strasse;
	private String hausnummer;
	private String plz;
	private int anzahlHaushalte;

	// Konstruktor, der alle Felder akzeptiert
	public Herkunft(String strasse, String hausnummer, String plz, int anzahlHaushalte)
	{
		this.strasse = strasse;
		this.hausnummer = hausnummer;
		this.plz = plz;
		this.anzahlHaushalte = anzahlHaushalte;
	}
	/**
     */
	// Getter und Setter
	public String getStrasse()
	{
		return strasse;
	}
	/**
     */
	public void setStrasse(String strasse)
	{
		this.strasse = strasse;
	}
	/**
     */
	public String getHausnummer()
	{
		return hausnummer;
	}
	/**
     */
	public void setHausnummer(String hausnummer)
	{
		this.hausnummer = hausnummer;
	}
	/**
     */
	public String getPlz()
	{
		return plz;
	}
	/**
     */
	public void setPlz(String plz)
	{
		this.plz = plz;
	}
	/**
     */
	public int getAnzahlHaushalte()
	{
		return anzahlHaushalte;
	}
	/**
     */
	public void setAnzahlHaushalte(int anzahlHaushalte)
	{
		this.anzahlHaushalte = anzahlHaushalte;
	}
}
