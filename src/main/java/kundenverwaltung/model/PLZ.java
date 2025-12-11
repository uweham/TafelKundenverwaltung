package kundenverwaltung.model;

import kundenverwaltung.dao.PLZDAO;
import kundenverwaltung.dao.PLZDaoImpl;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class PLZ
{
    private PLZDAO plzdao = new PLZDaoImpl();
    private int plzId;
    private String plz;
    private String ort;

    public PLZ(int plzId, String plz, String ort)
    {
        this.plzId = plzId;
        this.plz = plz;
        this.ort = ort;
    }

    public PLZ(String plz, String ort)
    {
        this.plz = plz;
        this.ort = ort;
        plzdao.create(this);
    }

    public PLZ(String plz, String ort, Boolean create)
    {
        this.plz = plz;
        this.ort = ort;
    }
    /**
     *.
     */
    @Override
    public String toString()
    {
        return ort;
    }
    /**
     *.
     */
    public PLZDAO getPlzdao()
    {
        return plzdao;
    }
    /**
     *.
     */
    public void setPlzdao(PLZDAO plzdao)
    {
        this.plzdao = plzdao;
    }
    /**
     *.
     */
    public int getPlzId()
    {
        return plzId;
    }
    /**
     *.
     */
    public void setPlzId(int plzId)
    {
        this.plzId = plzId;
    }
    /**
     *.
     */
    public String getPlz()
    {
        return plz;
    }
    /**
     *.
     */
    public void setPlz(String plz)
    {
        this.plz = plz;
    }
    /**
     *.
     */
    public String getOrt()
    {
        return ort;
    }
    /**
     *.
     */
    public void setOrt(String ort)
    {
        this.ort = ort;
    }
}
