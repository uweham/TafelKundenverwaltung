package kundenverwaltung.model;

import kundenverwaltung.dao.VerteilstelleDAO;
import kundenverwaltung.dao.VerteilstelleDAOimpl;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class Verteilstelle
{
    private VerteilstelleDAO verteilstelleDAO = new VerteilstelleDAOimpl();
    private int verteilstellenId;
    private String bezeichnung;
    private String adresse;
    private int listennummer;

    public Verteilstelle(String bezeichnung, String adresse, int listennummer)
    {
        this.bezeichnung = bezeichnung;
        this.adresse = adresse;
        this.listennummer = listennummer;
        verteilstelleDAO.create(this);
    }
    /**
     *.
     */
    @Override
    public String toString()
    {
        return bezeichnung;
    }

    public Verteilstelle(int verteilstellenId, String bezeichnung, String adresse, int listennummer)
    {
        this.verteilstellenId = verteilstellenId;
        this.bezeichnung = bezeichnung;
        this.adresse = adresse;
        this.listennummer = listennummer;
    }


    /**
     *.
     */
    public VerteilstelleDAO getVerteilstelleDAO()
    {
        return verteilstelleDAO;
    }
    /**
     *.
     */
    public int getVerteilstellenId()
    {
        return verteilstellenId;
    }
    /**
     *.
     */
    public void setVerteilstellenId(int verteilstellenId)
    {
        this.verteilstellenId = verteilstellenId;
    }
    /**
     *.
     */
    public String getBezeichnung()
    {
        return bezeichnung;
    }
    /**
     *.
     */
    public void setBezeichnung(String bezeichnung)
    {
        this.bezeichnung = bezeichnung;
    }
    /**
     *.
     */
    public String getAdresse()
    {
        return adresse;
    }
    /**
     *.
     */
    public void setAdresse(String adresse)
    {
        this.adresse = adresse;
    }
    /**
     *.
     */
    public int getListennummer()
    {
        return listennummer;
    }
    /**
     *.
     */
    public void setListennummer(int listennummer)
    {
        this.listennummer = listennummer;
    }
    /**
     */
    public int getId()
    {
      return verteilstellenId;
  }
    /**
     */
	 public String getName()
	 {
      return bezeichnung;
  }

}


