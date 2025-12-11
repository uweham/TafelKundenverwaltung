package kundenverwaltung.model;

import kundenverwaltung.dao.BescheidartDAO;
import kundenverwaltung.dao.BescheidartDAOimpl;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class Bescheidart
{ // in Tabelle auslagern
    private BescheidartDAO bescheidartDAO = new BescheidartDAOimpl();
    private int bescheidartId;
    private String name;
    private boolean aktiv = true;
    /**
     *.
     */
    @Override
    public String toString()
    {
        return name;
    }

    public Bescheidart(String name, boolean aktiv)
    {
        this.name = name;
        this.aktiv = aktiv;
        // WICHTIG: create() hier entfernt, das macht jetzt der Controller!
    }

    public Bescheidart(int bescheidartId, String name, boolean aktiv)
    {
        this.bescheidartId = bescheidartId;
        this.name = name;
        this.aktiv = aktiv;
    }


    /**
     *.
     */
    public BescheidartDAO getBescheidartDAO()
    {
        return bescheidartDAO;
    }
    /**
     *.
     */
    public int getBescheidartId()
    {
        return bescheidartId;
    }
    /**
     *.
     */
    public void setBescheidartId(int bescheidartId)
    {
        this.bescheidartId = bescheidartId;
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
}