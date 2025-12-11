package kundenverwaltung.model;

import java.time.LocalDate;
import kundenverwaltung.dao.VollmachtDAO;
import kundenverwaltung.dao.VollmachtDAOimpl;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class Vollmacht
{
    private VollmachtDAO vollmachtDAO = new VollmachtDAOimpl();
    private int vollmachtId;
    private Haushalt haushalt;
    private Familienmitglied bevollmaechtigtePerson;
    private LocalDate ausgestelltAm;
    private LocalDate ablaufDatum;

    private ChangeDateFormat changeDateFormat = new ChangeDateFormat();

    public Vollmacht(int vollmachtId, Haushalt haushalt, Familienmitglied bevollmaechtigtePerson, LocalDate ausgestelltAm, LocalDate ablaufDatum)
    {
        this.vollmachtId = vollmachtId;
        this.haushalt = haushalt;
        this.bevollmaechtigtePerson = bevollmaechtigtePerson;
        this.ausgestelltAm = ausgestelltAm;
        this.ablaufDatum = ablaufDatum;
    }

    public Vollmacht(Haushalt haushalt, Familienmitglied bevollmaechtigtePerson, LocalDate ausgestelltAm, LocalDate ablaufDatum)
    {
        this.haushalt = haushalt;
        this.bevollmaechtigtePerson = bevollmaechtigtePerson;
        this.ausgestelltAm = ausgestelltAm;
        this.ablaufDatum = ablaufDatum;
        //vollmachtDAO.create(this);
    }
    /**
     *.
     */
    public VollmachtDAO getVollmachtDAO()
    {
        return vollmachtDAO;
    }
    /**
     *.
     */
    public void setVollmachtDAO(VollmachtDAO vollmachtDAO)
    {
        this.vollmachtDAO = vollmachtDAO;
    }
    /**
     *.
     */
    public int getVollmachtId()
    {
        return vollmachtId;
    }
    /**
     *.
     */
    public void setVollmachtId(int vollmachtId)
    {
        this.vollmachtId = vollmachtId;
    }
    /**
     *.
     */
    public Haushalt getHaushalt()
    {
        return haushalt;
    }
    /**
     *.
     */
    public void setHaushalt(Haushalt haushalt)
    {
        this.haushalt = haushalt;
    }
    /**
     *.
     */
    public Familienmitglied getBevollmaechtigtePerson()
    {
        return bevollmaechtigtePerson;
    }
    /**
     *.
     */
    public void setBevollmaechtigtePerson(Familienmitglied bevollmaechtigtePerson)
    {
        this.bevollmaechtigtePerson = bevollmaechtigtePerson;
    }
    /**
     *.
     */
    public LocalDate getAusgestelltAm()
    {
        return ausgestelltAm;
    }
    /**
     *.
     */
    public void setAusgestelltAm(LocalDate ausgestelltAm)
    {
        this.ausgestelltAm = ausgestelltAm;
    }
    /**
     *.
     */
    public LocalDate getAblaufDatum()
    {
        return ablaufDatum;
    }
    /**
     *.
     */
    public void setAblaufDatum(LocalDate ablaufDatum)
    {
        this.ablaufDatum = ablaufDatum;
    }
    /**
     *.
     */
    public String getEmpfaenger()
    {
        return bevollmaechtigtePerson.getnName() + ", " + bevollmaechtigtePerson.getvName();
    }
    /**
     *.
     */
    public Integer getEmpfaengerNr()
    {
        return bevollmaechtigtePerson.getPersonId();
    }


    // for tableview
    /**
     *.
     */
    public String getDateOfIssue()
    {
        return changeDateFormat.changeDateToDefaultString(ausgestelltAm);
    }
    /**
     *.
     */
    public String getDateOfExpiry()
    {
        return changeDateFormat.changeDateToDefaultString(ablaufDatum);
    }

}

