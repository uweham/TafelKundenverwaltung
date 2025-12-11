package kundenverwaltung.model;

public class StatistiktoolModel
{
	private int gesamtsumme;
	private int[] gruppen;
	private int[] altersgruppen;  // Neue Variable für Altersgruppen

	public StatistiktoolModel(int gesamtsumme, int[] gruppen)
	{
		this.gesamtsumme = gesamtsumme;
		this.gruppen = gruppen;
		this.altersgruppen = altersgruppen;  // Initialisierung der Altersgruppen
	}

	 /**
	   *.
	   */
	public int getGesamtsumme()
	{
		return gesamtsumme;
	}
	 /**
	   *.
	   */
	public void setGesamtsumme(int gesamtsumme)
	{
		this.gesamtsumme = gesamtsumme;
	}

	 /**
	   *.
	   */
	public int[] getGruppen()
	{
		return gruppen;
	}
	 /**
	   *.
	   */
	public void setGruppen(int[] gruppen)
	{
		this.gruppen = gruppen;
	}

	 /**
	   *.
	   */
	public int[] getAltersgruppen()
	{
		return altersgruppen;
	}
	 /**
	   *.
	   */
	public void setAltersgruppen(int[] altersgruppen)
	{
		this.altersgruppen = altersgruppen;
	}
}
