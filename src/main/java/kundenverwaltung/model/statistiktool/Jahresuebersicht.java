package kundenverwaltung.model.statistiktool;

public class Jahresuebersicht
{

	private String monat;
	private int neuzugaenge;
	private int anzahlPersonen;
	private double gesamtUmsatzHaushalt;
	private double gesamtUmsatzEinkauf;

	// Hauptkonstruktor
	public Jahresuebersicht(String monat, int neuzugaenge, int anzahlPersonen, double gesamtUmsatzHaushalt, double gesamtUmsatzEinkauf)
	{
		this.monat = monat;
		this.neuzugaenge = neuzugaenge;
		this.anzahlPersonen = anzahlPersonen;
		this.gesamtUmsatzHaushalt = gesamtUmsatzHaushalt;
		this.gesamtUmsatzEinkauf = gesamtUmsatzEinkauf;

}

	// Überladener Konstruktor für flexiblere Initialisierung (z.B. ohne Umsatz)
	public Jahresuebersicht(String monat, int neuzugaenge, int anzahlPersonen)
	{
		this(monat, neuzugaenge, anzahlPersonen, 0.0, 0.0);
	}
	/**
     */
	// Getter und Setter
	public String getMonat()
	{
		return monat;
	}
	/**
     */
	public void setMonat(String monat)
	{
		this.monat = monat;
	}
	/**
     */
	public int getNeuzugaenge()
	{
		return neuzugaenge;
	}
	/**
     */
	public void setNeuzugaenge(int neuzugaenge)
	{
		this.neuzugaenge = neuzugaenge;
	}
	/**
     */
	public int getAnzahlPersonen()
	{
		return anzahlPersonen;
	}
	/**
     */
	public void setAnzahlPersonen(int anzahlPersonen)
	{
		this.anzahlPersonen = anzahlPersonen;
	}
	/**
     */
	public double getGesamtUmsatzHaushalt()
	{
		return gesamtUmsatzHaushalt;
	}
	/**
     */
	public void setGesamtUmsatzHaushalt(double gesamtUmsatzHaushalt)
	{
		this.gesamtUmsatzHaushalt = gesamtUmsatzHaushalt;
	}
	/**
     */
	public double getGesamtUmsatzEinkauf()
	{
		return gesamtUmsatzEinkauf;
	}
	/**
     */
	public void setGesamtUmsatzEinkauf(double gesamtUmsatzEinkauf)
	{
		this.gesamtUmsatzEinkauf = gesamtUmsatzEinkauf;
	}
	/**
     */
	// toString-Methode für einfache Textdarstellung der Objektinformationen
	@Override
	public String toString()
	{
		return String.format("Monat: %s, Neuzugänge: %d, Anzahl Personen: %d, Gesamtumsatz Haushalt: %.2f, Gesamtumsatz Einkauf: %.2f",
				monat, neuzugaenge, anzahlPersonen, gesamtUmsatzHaushalt, gesamtUmsatzEinkauf);
	}
	/**
     */
	// equals und hashCode Methoden falls Vergleich und Nutzung in Collections erforderlich sind
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		Jahresuebersicht that = (Jahresuebersicht) obj;

		if (neuzugaenge != that.neuzugaenge) return false;
		if (anzahlPersonen != that.anzahlPersonen) return false;
		if (Double.compare(that.gesamtUmsatzHaushalt, gesamtUmsatzHaushalt) != 0) return false;
		if (Double.compare(that.gesamtUmsatzEinkauf, gesamtUmsatzEinkauf) != 0) return false;
		return monat != null ? monat.equals(that.monat) : that.monat == null;
	}
	/**
     */
	@Override
	public int hashCode()
	{
		int result;
		long temp;
		result = monat != null ? monat.hashCode() : 0;
		result = 31 * result + neuzugaenge;
		result = 31 * result + anzahlPersonen;
		temp = Double.doubleToLongBits(gesamtUmsatzHaushalt);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(gesamtUmsatzEinkauf);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}
