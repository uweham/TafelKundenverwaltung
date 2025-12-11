package kundenverwaltung.dao;

import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.statistiktool.Jahresuebersicht;
import javafx.collections.ObservableList;

import java.util.List;

public class JahresuebersichtDAO
{
  /**
   */
	public ObservableList<Jahresuebersicht> getJahresuebersichtData()
	{
		return new kundenverwaltung.dao.JahresuebersichtDAOimpl().getJahresuebersichtData();
	}
	/**
     */
	// Diese Methode muss auch implementiert werden, um Personendaten abzurufen
	List<Jahresuebersicht> getPersonenDaten()
	{
		return null;
	}
	/**
     */
	public ObservableList<Jahresuebersicht> getJahresuebersichtByYear(int year)
	{
		return null;
	}

	public void saveJahresuebersicht(Jahresuebersicht jahresuebersicht)
	{

	}
}



