package kundenverwaltung.model.statistiktool;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ArchivierteKunden
{
	private final StringProperty kdNr;
	private final StringProperty name;
	private final StringProperty strasse;
	private final StringProperty plz;
	private final StringProperty ort;
	private int status;

	public ArchivierteKunden(String kdNr, String name, String strasse, String plz, String ort)
	{
		this.kdNr = new SimpleStringProperty(kdNr);
		this.name = new SimpleStringProperty(name);
		this.strasse = new SimpleStringProperty(strasse);
		this.plz = new SimpleStringProperty(plz);
		this.ort = new SimpleStringProperty(ort);
	}

	// Getter für die Properties (Für direkte Verwendung in TableView)
	/**
     */
	public StringProperty kdNrProperty()
	{
		return kdNr;
	}
	/**
     */
	public StringProperty nameProperty()
	{
		return name;
	}
	/**
     */
	public StringProperty strasseProperty()
	{
		return strasse;
	}
	/**
     */
	public StringProperty plzProperty()
	{
		return plz;
	}
	/**
     */
	public StringProperty ortProperty()
	{
		return ort;
	}


	/**
     */
	public String getKdNr()
	{
		return kdNr.get();
	}
	/**
     */
	public String getName()
	{
		return name.get();
	}
	/**
     */
	public String getStrasse()
	{
		return strasse.get();
	}
	/**
     */
	public String getPlz()
	{
		return plz.get();
	}
	/**
     */
	public String getOrt()
	{
		return ort.get();
	}
	/**
     */
	// Optionale Setter
	public void setKdNr(String kdNr)
	{
		this.kdNr.set(kdNr);
	}
	/**
     */
	public void setName(String name)
	{
		this.name.set(name);
	}
	/**
     */
	public void setStrasse(String strasse)
	{
		this.strasse.set(strasse);
	}
	/**
     */
	public void setPlz(String plz)
	{
		this.plz.set(plz);
	}
	/**
     */
	public void setOrt(String ort)
	{
		this.ort.set(ort);
	}
	/**
     */
	public void setStatus(int status)
	{
		this.status = status;
	}
	/**
     */
	public int getStatus()
	{
		return status;
	}
	/**
     */
	public String getHausnummer()
	{
		return getHausnummer();
	}
}
