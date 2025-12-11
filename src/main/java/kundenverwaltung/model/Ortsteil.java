package kundenverwaltung.model;

import kundenverwaltung.dao.OrtsteilDAO;
import kundenverwaltung.dao.OrtsteilDAOimpl;

public class Ortsteil
{
    private OrtsteilDAO ortsteildao = new OrtsteilDAOimpl();
    private int ortsteilId;
    private String name;
    private int plz;

    public Ortsteil(int ortsteilId, String name, int plz)
    {
        this.ortsteilId = ortsteilId;
        this.name = name;
        this.plz = plz;
    }

    public Ortsteil(String name, int plz)
    {
        this.name = name;
        this.plz = plz;
    }
    /**
     *.
     */
    public OrtsteilDAO getOrtsteildao()
    {
        return ortsteildao;
    }
    /**
     *.
     */
    public void setOrtsteildao(OrtsteilDAO ortsteildao)
    {
        this.ortsteildao = ortsteildao;
    }
    /**
     *.
     */
    public int getOrtsteilId()
    {
        return ortsteilId;
    }
    /**
     *.
     */
    public void setOrtsteilId(int ortsteilId)
    {
        this.ortsteilId = ortsteilId;
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
    public int getPlz()
    {
        return plz;
    }
    /**
     *.
     */
    public void setPlz(int plz)
    {
        this.plz = plz;
    }
}
