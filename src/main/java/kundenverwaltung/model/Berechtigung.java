package kundenverwaltung.model;

import kundenverwaltung.dao.BerechtigungDAO;
import kundenverwaltung.dao.BerechtigungDAOimpl;

/**
 * Created by Florian-PC on 02.11.2017.
 */


//Hier könnte man vielleicht auch besser nen Enum raus machen?

public class Berechtigung
{ //in Tabelle auslagern
    private BerechtigungDAO berechtigungDAO = new BerechtigungDAOimpl();
    private int berechtigungId;
    private String name;

    public Berechtigung(int berechtigungId, String name)
    {
        this.berechtigungId = berechtigungId;
        this.name = name;
    }

    public Berechtigung(String name)
    {
        this.name = name;
        berechtigungDAO.create(this);
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
    public BerechtigungDAO getBerechtigungDAO()
    {
        return berechtigungDAO;
    }
    /**
     *.
     */
    public int getBerechtigungId()
    {
        return berechtigungId;
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
    public void setBerechtigungId(int berechtigungId)
    {
        this.berechtigungId = berechtigungId;
    }
}
