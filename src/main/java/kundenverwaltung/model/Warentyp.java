package kundenverwaltung.model;

import kundenverwaltung.dao.WarentypDAO;
import kundenverwaltung.dao.WarentypDAOimpl;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class Warentyp
{
    // EIGENE TABELLE
    private WarentypDAO warentypDAO = new WarentypDAOimpl();
    private int warentypId;
    private String name;
    private float preisErwachsene;
    private float preisKinder;
    private float haushaltspauschale;
    private float deckelbetrag;
    private float minbetrag;
    private boolean aktiv;
    private boolean manuelleBerechnung;
    private int naechsterWarentypid;
    //"nicht möglich","erforferlich ","optinal / möglich"
    private int zuordnungPerson;
    //"nicht möglich","erforferlich ","optinal / möglich"
    private int zuordnungBuchungstext;
    private int warentyplimitanzahl;
    //"Kalenderwoche(SO-SA)","Arbeitswoche (Mo-FR) ","7 Tage","Monat", "Quartal", "Jahr"
    private int warentyplimitart;
    private int warentyplimitabstand;
    //"Tage", "Wochen", "Monate"
    private int warentyplimitabstandart;




    public Warentyp(int warentypId, String name, float preisErwachsene, float preisKinder, boolean aktiv)
    {
        this.warentypId = warentypId;
        this.name = name;
        this.preisErwachsene = preisErwachsene;
        this.preisKinder = preisKinder;
        this.aktiv = aktiv;
    }

    public Warentyp(String name, float preisErwachsene, float preisKinder, boolean aktiv)
    {
        this.name = name;
        this.preisErwachsene = preisErwachsene;
        this.preisKinder = preisKinder;
        this.aktiv = aktiv;
        warentypDAO.create(this);
    }

    public Warentyp(int warentypId, String name, float preisErwachsene, float preisKinder, Boolean aktiv, float haushaltspauschale, float deckelbetrag, float minbetrag, Boolean manuelleberechnung, int zuordnungperson, int zuordnungbuchungstext,
                    int warentyplimitanzahl, int warentyplimitart, int warentyplimitabstand, int warentyplimitabstandart, int naechsterwarentypid)
    {
        this.warentypId = warentypId;
        this.name = name;
        this.preisErwachsene = preisErwachsene;
        this.preisKinder = preisKinder;
        this.aktiv = aktiv;
        this.haushaltspauschale = haushaltspauschale;
        this.deckelbetrag = deckelbetrag;
        this.minbetrag = minbetrag;
        this.manuelleBerechnung = manuelleberechnung;
        this.naechsterWarentypid = naechsterwarentypid;
        this.zuordnungPerson = zuordnungperson;
        this.zuordnungBuchungstext = zuordnungbuchungstext;
        this.warentyplimitanzahl = warentyplimitanzahl;
        this.warentyplimitart = warentyplimitart;
        this.warentyplimitabstand = warentyplimitabstand;
        this.warentyplimitabstandart = warentyplimitabstandart;

    }

	public Warentyp()
	{

	}

	/**
     *.
     */
    public WarentypDAO getWarentypDAO()
    {
        return warentypDAO;
    }
    /**
     *.
     */
    @Override
    public String toString()
    {
        return name;
    }
    /**
     *.
     */
    public int getWarentypId()
    {
        return warentypId;
    }
    /**
     *.
     */
    public void setWarentypId(int warentypId)
    {
        this.warentypId = warentypId;
    }
    /**
     *.
     */
    public String getName()
    {
        return name;
    }
    /**
     *.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     *.
     */
    public float getPreisErwachsene()
    {
        return preisErwachsene;
    }
    /**
     *.
     */
    public void setPreisErwachsene(float preisErwachsene)
    {
        this.preisErwachsene = preisErwachsene;
    }
    /**
     *.
     */
    public float getPreisKinder()
    {
        return preisKinder;
    }
    /**
     *.
     */
    public void setPreisKinder(float preisKinder)
    {
        this.preisKinder = preisKinder;
    }
    /**
     *.
     */
    public boolean isAktiv()
    {
        return aktiv;
    }
    /**
     *.
     */
    public void setAktiv(boolean aktiv)
    {
        this.aktiv = aktiv;
    }
    /**
     *.
     */
    public int getNaechsterWarentypid()
    {
        return naechsterWarentypid;
    }
    /**
     *.
     */
    public void setNaechsterWarentypid(int naechsterWarentypid)
    {
        this.naechsterWarentypid = naechsterWarentypid;
    }
    /**
     *.
     */
    public int getZuordnungPerson()
    {
        return zuordnungPerson;
    }
    /**
     *.
     */
    public void setZuordnungPerson(int zuordnungPerson)
    {
        this.zuordnungPerson = zuordnungPerson;
    }
    /**
     *.
     */
    public int getZuordnungBuchungstext()
    {
        return zuordnungBuchungstext;
    }
    /**
     *.
     */
    public void setZuordnungBuchungstext(int zuordnungBuchungstext)
    {
        this.zuordnungBuchungstext = zuordnungBuchungstext;
    }
    /**
     *.
     */
    public int getWarentyplimitanzahl()
    {
        return warentyplimitanzahl;
    }
    /**
     *.
     */
    public void setWarentyplimitanzahl(int warentyplimitanzahl)
    {
        this.warentyplimitanzahl = warentyplimitanzahl;
    }
    /**
     *.
     */
    public int getWarentyplimitart()
    {
        return warentyplimitart;
    }
    /**
     *.
     */
    public void setWarentyplimitart(int warentyplimitart)
    {
        this.warentyplimitart = warentyplimitart;
    }
    /**
     *.
     */
    public int getWarentyplimitabstand()
    {
        return warentyplimitabstand;
    }
    /**
     *.
     */
    public void setWarentyplimitabstand(int warentyplimitabstand)
    {
        this.warentyplimitabstand = warentyplimitabstand;
    }
    /**
     *.
     */
    public int getWarentyplimitabstandart()
    {
        return warentyplimitabstandart;
    }
    /**
     *.
     */
    public void setWarentyplimitabstandart(int warentyplimitabstandart)
    {
        this.warentyplimitabstandart = warentyplimitabstandart;
    }
    /**
     *.
     */
    public float getHaushaltspauschale()
    {
        return haushaltspauschale;
    }
    /**
     *.
     */
    public void setHaushaltspauschale(float haushaltspauschale)
    {
        this.haushaltspauschale = haushaltspauschale;
    }
    /**
     *.
     */
    public float getDeckelbetrag()
    {
        return deckelbetrag;
    }
    /**
     *.
     */
    public void setDeckelbetrag(float deckelbetrag)
    {
        this.deckelbetrag = deckelbetrag;
    }
    /**
     *.
     */
    public float getMinbetrag()
    {
        return minbetrag;
    }
    /**
     *.
     */
    public void setMinbetrag(float minbetrag)
    {
        this.minbetrag = minbetrag;
    }
    /**
     *.
     */
    public boolean isManuelleBerechnung()
    {
        return manuelleBerechnung;
    }
    /**
     *.
     */
    public void setManuelleBerechnung(boolean manuelleBerechnung)
    {
        this.manuelleBerechnung = manuelleBerechnung;
    }

}
