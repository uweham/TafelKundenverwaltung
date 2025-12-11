package kundenverwaltung.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import kundenverwaltung.dao.FamilienmitgliedDAO;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class Familienmitglied
{
    private static final ChangeDateFormat CHANGE_DATE_FORMAT = new ChangeDateFormat();
    private static final int OF_AGE = 18;

    private FamilienmitgliedDAO familienmitgliedDAO = new FamilienmitgliedDAOimpl();
    private int personId;
    private Haushalt haushalt;
    private Anrede anrede;
    private Gender gender;
    private String vName;
    private String nName;
    private LocalDate gDatum;
    private String bemerkung;
    private boolean haushaltsVorstand;
    private boolean einkaufsBerechtigt;
    private boolean gebuehrenBefreiung;
    private Nation nation;
    private Berechtigung berechtigung;
    private boolean aufAusweis;
    private boolean dseSubmitted;
    private LocalDateTime hinzugefuegtAm;
    private LocalDateTime geaendertAm;
    private String mitgliedId;
    private String relation;





    public Familienmitglied(int personId, Haushalt haushalt, Anrede anrede, Gender gender, String vName, String nName, LocalDate gDatum, String bemerkung, boolean haushaltsVorstand, boolean einkaufsBerechtigt, boolean gebuehrenBefreiung, Nation nation, Berechtigung berechtigung, boolean aufAusweis, boolean dseSubmitted, LocalDateTime hinzugefuegtAm, LocalDateTime geaendertAm)
    {
        this.personId = personId;
        this.haushalt = haushalt;
        this.anrede = anrede;
        this.gender = gender;
        this.vName = vName;
        this.nName = nName;
        this.gDatum = gDatum;
        this.bemerkung = bemerkung;
        this.haushaltsVorstand = haushaltsVorstand;
        this.einkaufsBerechtigt = einkaufsBerechtigt;
        this.gebuehrenBefreiung = gebuehrenBefreiung;
        this.nation = nation;
        this.berechtigung = berechtigung;
        this.aufAusweis = aufAusweis;
        this.dseSubmitted = dseSubmitted;
        this.hinzugefuegtAm = hinzugefuegtAm;
        this.geaendertAm = geaendertAm;
        this.mitgliedId = mitgliedId;
        this.relation = relation;

    }


    public Familienmitglied(Haushalt haushalt, Anrede anrede, Gender gender, String vName, String nName, LocalDate gDatum, String bemerkung, boolean haushaltsVorstand, boolean einkaufsBerechtigt, boolean gebuehrenBefreiung, Nation nation, Berechtigung berechtigung, boolean aufAusweis, boolean dseSubmitted, LocalDateTime hinzugefuegtAm, LocalDateTime geaendertAm)
    {
        this.haushalt = haushalt;
        this.anrede = anrede;
        this.gender = gender;
        this.vName = vName;
        this.nName = nName;
        this.gDatum = gDatum;
        this.bemerkung = bemerkung;
        this.haushaltsVorstand = haushaltsVorstand;
        this.einkaufsBerechtigt = einkaufsBerechtigt;
        this.gebuehrenBefreiung = gebuehrenBefreiung;
        this.nation = nation;
        this.berechtigung = berechtigung;
        this.aufAusweis = aufAusweis;
        this.dseSubmitted = dseSubmitted;
        this.hinzugefuegtAm = hinzugefuegtAm;
        this.geaendertAm = geaendertAm;
        //familienmitgliedDAO.create(this);
    }

    public Familienmitglied(String bescheidart, String name, String geburtsdatum, String relation)
    {
    }

	public Familienmitglied(int familienmitgliedId, String name)
	{
	}

    public Familienmitglied(int personId, int haushaltId)
    {
    }

	public Familienmitglied()
	{

	}

    public Familienmitglied(int personId, String vName, String nName, java.sql.Date gDatum, String strasse)
    {
    }

    /**
     *.
     */
    public FamilienmitgliedDAO getFamilienmitgliedDAO()
    {
        return familienmitgliedDAO;
    }
    /**
     *.
     */
    @Override
    public String toString()
    {
        return vName + " " + nName;
    }
    /**
     *.
     */
    public void setPersonId(int personId)
    {
        this.personId = personId;
    }
    /**
     *.
     */
    public int getPersonId()
    {
        return personId;
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
    public Anrede getAnrede()
    {
        return anrede;
    }
    /**
     *.
     */
    public Gender getGender()
    { return gender; }
    /**
     *.
     */
    public String getvName()
    {
        return vName;
    }
    /**
     *.
     */
    public String getnName()
    {
        return nName;
    }
    /**
     *.
     */
    public LocalDate getgDatum()
    {
        return gDatum;
    }
    /**
     *.
     */
    public String getBemerkung()
    {
        return bemerkung;
    }
    /**
     *.
     */
    public boolean isHaushaltsVorstand()
    {
        return haushaltsVorstand;
    }
    /**
     *.
     */
    public boolean isEinkaufsBerechtigt()
    {
        return einkaufsBerechtigt;
    }
    /**
     *.
     */
    public boolean isGebuehrenBefreiung()
    {
        return gebuehrenBefreiung;
    }
    /**
     *.
     */
    public Nation getNation()
    {
        return nation;
    }
    /**
     *.
     */
    public Berechtigung getBerechtigung()
    {
        return berechtigung;
    }
    /**
     *.
     */
    public boolean isAufAusweis()
    {
        return aufAusweis;
    }
    /**
     *.
     */
    public boolean dseSubmitted()
    {
        return dseSubmitted;
    }
    /**
     *.
     */
    public LocalDateTime getHinzugefuegtAm()
    {
        return hinzugefuegtAm;
    }
    /**
     *.
     */
    public LocalDateTime getGeaendertAm()
    {
        return geaendertAm;
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
    public void setAnrede(Anrede anrede)
    {
        this.anrede = anrede;
    }
    /**
     *.
     */
    public void setGender(Gender gender)
    {
        this.gender = gender;
    }
    /**
     *.
     */
    public void setvName(String vName)
    {
        this.vName = vName;
    }
    /**
     *.
     */
    public void setnName(String nName)
    {
        this.nName = nName;
    }
    /**
     *.
     */
    public void setgDatum(LocalDate gDatum)
    {
        this.gDatum = gDatum;
    }
    /**
     *.
     */
    public void setBemerkung(String bemerkung)
    {
        this.bemerkung = bemerkung;
    }
    /**
     *.
     */
    public void setHaushaltsVorstand(boolean haushaltsVorstand)
    {
        this.haushaltsVorstand = haushaltsVorstand;
    }
    /**
     *.
     */
    public void setEinkaufsBerechtigt(boolean einkaufsBerechtigt)
    {
        this.einkaufsBerechtigt = einkaufsBerechtigt;
    }
    /**
     *.
     */
    public void setGebuehrenBefreiung(boolean gebuehrenBefreiung)
    {
        this.gebuehrenBefreiung = gebuehrenBefreiung;
    }
    /**
     *.
     */
    public void setNation(Nation nation)
    {
        this.nation = nation;
    }
    /**
     *.
     */
    public void setBerechtigung(Berechtigung berechtigung)
    {
        this.berechtigung = berechtigung;
    }
    /**
     *.
     */
    public void setAufAusweis(boolean aufAusweis)
    {
        this.aufAusweis = aufAusweis;
    }
    /**
     *.
     */
    public void setDseSubmitted(boolean dseSubmitted)
    {
        this.dseSubmitted = dseSubmitted;
    }
    /**
     *.
     */
    public void setHinzugefuegtAm(LocalDateTime hinzugefuegtAm)
    {
        this.hinzugefuegtAm = hinzugefuegtAm;
    }
    /**
     *.
     */
    public void setGeaendertAm(LocalDateTime geaendertAm)
    {
        this.geaendertAm = geaendertAm;
    }
    /**
     *.
     */
    public Boolean isAdult()
    {
        Period period = Period.between(gDatum, LocalDate.now());
        int age = period.getYears();

        return age >= OF_AGE;

    }


    //Für TableViews
    /**
     *.
     */
    public Integer getKundennummer()
    {
        return haushalt.getKundennummer();
    }
    /**
     *.
     */
    public String getAdresse()
    {
        return haushalt.getStrasse() + " " + haushalt.getHausnummer();
    }
    /**
     *.
     */
    public String getPlz()
    {
        return haushalt.getPlz().getPlz();
    }
    /**
     *.
     */
    public String getWohnort()
    {
        return haushalt.getPlz().getOrt();
    }
    /**
     *.
     */
    public String getName()
    {
        return vName + " " + nName;
    }
    /**
     *.
     */
    public String getVorname()
    {
        return vName;
    }
    /**
     *.
     */
    public String getNachname()
    {
        return nName;
    }
    /**
     *.
     */
    public String getNationString()
    {
        if (nation != null)
        {
            return nation.getName();
        }
        else
        {
            return "";
        }
    }
    /**
     *.
     */
    public String getAnredeString()
    {

        return anrede.getAnrede();
    }
    /**
     *.
     */
    public String getGenderString()
    {

        return gender.getGender();
    }
    /**
     *.
     */
    public String getBerechtigungString()
    {
        return berechtigung.getName();
    }
    /**
     *.
     */
    public String getTypString()
    {
        if (haushaltsVorstand)
        {
            return "Haushaltsvorstand";
        } else if (einkaufsBerechtigt)
        {
            return "einkaufsberechtigt";
        } else
        {
            return "nicht einkaufsberechtigt";
        }
    }
    /**
     *.
     */
    public String getGebuehren()
    {
        if (gebuehrenBefreiung)
        {
            return "normal";
        } else
        {
            return "befreit";
        }
    }
    /**
     *.
     */
    public String getAusweis()
    {
        if (aufAusweis)
        {
            return "eingetragen";
        } else
        {
            return "nicht eingetragen";
        }
    }
    /**
     *.
     */
    public Date getGeburtsdatum()
    {
        if (gDatum != null)
        {
            return Date.valueOf(gDatum);
        }
        else
        {
            return null;
        }
    }
    /**
     *.
     */
    public String getBirthdayString()
    {
        if (gDatum != null)
        {
            return CHANGE_DATE_FORMAT.changeDateToDefaultString(gDatum);
        }
        else
        {
            return null;
        }
    }
    /**
     *.
     */
    public Date getKuSeit()
    {
        if (haushalt != null && haushalt.getKundeSeit() != null)
        {
            return Date.valueOf(haushalt.getKundeSeit());
        }
        else
        {
            return null;
        }
    }
    /**
     *.
     */
    public String getCustomerSince()
    {
        if (haushalt != null && haushalt.getKundeSeit() != null)
        {
            return CHANGE_DATE_FORMAT.changeDateToDefaultString(haushalt.getKundeSeit());
        }
        else
        {
            return null;
        }
    }
    /**
     *.
     */
    public String getAusgabegruppe()
    {
        if (haushalt != null && haushalt.getAusgabegruppe() != null)
        {
            return haushalt.getAusgabegruppe().getName();
        }
        else
        {
            return null;
        }

    }
    /**
     *.
     */
    public String getVerteilstelle()
    {
        return haushalt.getVerteilstelle().getBezeichnung();
    }
    /**
     *.
     */
    public String getBelieferungString()
    {

        if (haushalt.isBelieferung())
        {
            return "Ja";
         } else
         {
            return "Nein";
        }
    }


}
