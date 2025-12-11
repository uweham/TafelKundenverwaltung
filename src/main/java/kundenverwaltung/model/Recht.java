package kundenverwaltung.model;

import kundenverwaltung.dao.RechteDAO;
import kundenverwaltung.dao.RechteDAOimpl;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class Recht
{
    private RechteDAO rechteDAO = new RechteDAOimpl();
    private int rechtId;
    private String name;
    private String beschreibung;

    public Recht(String name, String beschreibung)
    {
        this.name = name;
        this.beschreibung = beschreibung;
        rechteDAO.create(this);
    }

    public Recht(int rechtId, String name, String beschreibung)
    {
        this.rechtId = rechtId;
        this.name = name;
        this.beschreibung = beschreibung;
    }
    /**
     *.
     */
    public RechteDAO getRechteDAO()
    {
        return rechteDAO;
    }
    /**
     *.
     */
    public void setRechteDAO(RechteDAO rechteDAO)
    {
        this.rechteDAO = rechteDAO;
    }
    /**
     *.
     */
    public int getRechtId()
    {
        return rechtId;
    }
    /**
     *.
     */
    public void setRechtId(int rechtId)
    {
        this.rechtId = rechtId;
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
    public String getBeschreibung()
    {
        return beschreibung;
    }
    /**
     *.
     */
    public void setBeschreibung(String beschreibung)
    {
        this.beschreibung = beschreibung;
    }
}
