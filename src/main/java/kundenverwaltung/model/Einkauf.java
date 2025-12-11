package kundenverwaltung.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import kundenverwaltung.dao.EinkaufDAO;
import kundenverwaltung.dao.EinkaufDAOimpl;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;

/**
 * Created by Florian-PC on 02.11.2017.
 */
//gehoert_zu_datum aus alter Datenbank entfernt (synchron mit erfassungsZeit)
public class Einkauf
{
    private ChangeDateFormat changeDateFormat = new ChangeDateFormat();
    private EinkaufDAO einkaufDAO = (EinkaufDAO) new EinkaufDAOimpl();

    private int einkaufId;
    private Warentyp warentyp;
    private LocalDateTime storniertAm; // nicht Storniert, wenn null
    private String stornoText;
    private String buchungstext;
    private Haushalt kunde;
    private Familienmitglied person;
    private LocalDateTime erfassungsZeit;
    private float summeEinkauf;
    private float summeZahlung;
    private Verteilstelle beiVerteilstelle;
    private int anzahlKinder;
    private int anzahlErwachsene;

    private double saldo; // Neues Feld für Saldo

    private String kundenName; // Jetzt private!
    /**
     */
    // Getter und Setter für kundenName
    public String getKundenName()
    {
        return kundenName;
    }
    /**
     */
    public void setKundenName(String kundenName)
    {
        this.kundenName = kundenName;
    }

    // Vorhandener Konstruktor ohne Saldo
    public Einkauf(int einkaufId, Warentyp warentyp, LocalDateTime storniertAm, String stornoText,
                   String buchungstext, Haushalt kunde, Familienmitglied person,
                   LocalDateTime erfassungsZeit, float summeEinkauf, float summeZahlung,
                   Verteilstelle beiVerteilstelle, int anzahlKinder, int anzahlErwachsene)
    {
        this.einkaufId = einkaufId;
        this.warentyp = warentyp;
        this.storniertAm = storniertAm;
        this.stornoText = stornoText;
        this.buchungstext = buchungstext;
        this.kunde = kunde;
        this.person = person;
        this.erfassungsZeit = erfassungsZeit;
        this.summeEinkauf = summeEinkauf;
        this.summeZahlung = summeZahlung;
        this.beiVerteilstelle = beiVerteilstelle;
        this.anzahlKinder = anzahlKinder;
        this.anzahlErwachsene = anzahlErwachsene;
        this.saldo = 0.0; // Standardwert für Saldo
    }

    // Vorhandener Konstruktor ohne einkaufId, jetzt ohne Saldo
    public Einkauf(Warentyp warentyp, LocalDateTime storniertAm, String stornoText,
                   String buchungstext, Haushalt kunde, Familienmitglied person,
                   LocalDateTime erfassungsZeit, float summeEinkauf, float summeZahlung,
                   Verteilstelle beiVerteilstelle, int anzahlKinder, int anzahlErwachsene)
    {
        this.warentyp = warentyp;
        this.storniertAm = storniertAm;
        this.stornoText = stornoText;
        this.buchungstext = buchungstext;
        this.kunde = kunde;
        this.person = person;
        this.erfassungsZeit = erfassungsZeit;
        this.summeEinkauf = summeEinkauf;
        this.summeZahlung = summeZahlung;
        this.beiVerteilstelle = beiVerteilstelle;
        this.anzahlKinder = anzahlKinder;
        this.anzahlErwachsene = anzahlErwachsene;
        this.saldo = 0.0; // Standardwert für Saldo
        einkaufDAO.create(this);
    }

    // Neuer Konstruktor mit Saldo
    public Einkauf(int einkaufId, Warentyp warentyp, LocalDateTime storniertAm, String stornoText,
                   String buchungstext, Haushalt kunde, Familienmitglied person,
                   LocalDateTime erfassungsZeit, float summeEinkauf, float summeZahlung,
                   Verteilstelle beiVerteilstelle, int anzahlKinder, int anzahlErwachsene,
                   double saldo)
    {
        this.einkaufId = einkaufId;
        this.warentyp = warentyp;
        this.storniertAm = storniertAm;
        this.stornoText = stornoText;
        this.buchungstext = buchungstext;
        this.kunde = kunde;
        this.person = person;
        this.erfassungsZeit = erfassungsZeit;
        this.summeEinkauf = summeEinkauf;
        this.summeZahlung = summeZahlung;
        this.beiVerteilstelle = beiVerteilstelle;
        this.anzahlKinder = anzahlKinder;
        this.anzahlErwachsene = anzahlErwachsene;
        this.saldo = saldo; // Initialisierung des Saldos
    }

    public Einkauf()
    {

    }
    /**
     */
    public double getSaldo()
    {
        return saldo;
    }
    /**
     */
    public void setSaldo(double saldo)
    {
        this.saldo = saldo;
    }

    /**
     *.
     */
    public EinkaufDAO getEinkaufDAO()
    {
        return einkaufDAO;
    }
    /**
     *.
     */
    public void setEinkaufDAO(EinkaufDAO einkaufDAO)
    {
        this.einkaufDAO = einkaufDAO;
    }
    /**
     *.
     */
    public void setWarentyp(Warentyp warentyp)
    {
        this.warentyp = warentyp;

    }
    /**
     *.
     */
    public void setStorniertAm(LocalDateTime storniertAm)
    {
        this.storniertAm = storniertAm;
    }
    /**
     *.
     */
    public void setStornoText(String stornoText)
    {
        this.stornoText = stornoText;
    }
    /**
     *.
     */
    public void setBuchungstext(String buchungstext)
    {
        this.buchungstext = buchungstext;
    }
    /**
     *.
     */
    public void setKunde(Haushalt kunde)
    {
        this.kunde = kunde;
    }
    /**
     *.
     */
    public void setPerson(Familienmitglied person)
    {
        this.person = person;
    }

    /**
     *.
     */
    public void setErfassungsZeit(LocalDateTime erfassungsZeit)
    {
        this.erfassungsZeit = erfassungsZeit;
    }
    /**
     *.
     */
    public void setSummeEinkauf(float summeEinkauf)
    {
        this.summeEinkauf = summeEinkauf;
    }
    /**
     *.
     */
    public void setSummeZahlung(float summeZahlung)
    {
        this.summeZahlung = summeZahlung;
    }
    /**
     *.
     */
    public void setBeiVerteilstelle(Verteilstelle beiVerteilstelle)
    {
        this.beiVerteilstelle = beiVerteilstelle;
    }
    /**
     *.
     */
    public void setAnzahlKinder(int anzahlKinder)
    {
        this.anzahlKinder = anzahlKinder;
    }
    /**
     *.
     */
    public void setAnzahlErwachsene(int anzahlErwachsene)
    {
        this.anzahlErwachsene = anzahlErwachsene;
    }
    /**
     *.
     */
    public void setEinkaufId(int einkaufId)
    {
        this.einkaufId = einkaufId;
    }
    /**
     *.
     */
    public int getEinkaufId()
    {
        return einkaufId;
    }
    /**
     *.
     */
    public Warentyp getWarentyp()
    {
        return warentyp;
    }
    /**
     *.
     */
    public LocalDateTime getStorniertAm()
    {
        return storniertAm;
    }
    /**
     *.
     */
    public String getStornoText()
    {
        return stornoText;
    }
    /**
     *.
     */
    public String getBuchungstext()
    {
        return buchungstext;
    }
    /**
     *.
     */
    public Haushalt getKunde()
    {
        return kunde;
    }
    /**
     *.
     */
    public Familienmitglied getPerson()
    {
        return person;
    }
    /**
     */
    // Methode, um den vollständigen Namen des Kunden zurückzugeben
    public String getKundeName()
    {
        if (person != null)
        {
            return person.getvName() + " " + person.getnName(); // Vorname und Nachname
        }
        return "Unbekannt"; // Fallback, wenn person null ist
    }
    /**
     *.
     */
    public LocalDateTime getErfassungsZeit()
    {
        return erfassungsZeit;
    }
    /**
     *.
     */
    public float getSummeEinkauf()
    {
        return summeEinkauf;
    }
    /**
     *.
     */
    public float getSummeZahlung()
    {
        return summeZahlung;
    }
    /**
     *.
     */
    public Verteilstelle getBeiVerteilstelle()
    {
        return beiVerteilstelle;
    }
    /**
     *.
     */
    public int getAnzahlKinder()
    {
        return anzahlKinder;
    }
    /**
     *.
     */
    public int getAnzahlErwachsene()
    {
        return anzahlErwachsene;
    }

    //Tableview getter
    /**
     *.
     */
    public String getName()
    {
        return this.person.getvName() + this.person.getnName();
    }
    /**
     *.
     */
    public String getBuchungText()
    {
        return this.buchungstext;
    }
    /**
     *.
     */
    public String getEingekauftAm()
    {
        return this.erfassungsZeit.toString();
    }
    /**
     *.
     */
    public String getErfasstAm()
    {
        return "";
    }
    /**
     *.
     */
    public String getWarentypName()
    {
        return this.warentyp.getName();
    }
    /**
     */
    public void setWarentypName(String warentypName)
    {
        if (this.warentyp == null)
        {
            this.warentyp = new Warentyp(); // Initialisierung des Warentyps
        }
        this.warentyp.setName(warentypName); // Setzen des Namens
    }
    /**
     *.
     */
    public String getUmsatz()
    {
        return Float.toString(this.summeEinkauf);
    }
    /**
     *.
     */
    public String getZahlung()
    {
        return Float.toString(this.summeZahlung);
    }
    /**
     *.
     */
    public String getVerteilstelleName()
    {
        return this.beiVerteilstelle.getBezeichnung();
    }
    /**
     *.
     */
    public String getStorniert()
    {
        if (this.storniertAm != null)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = storniertAm.format(formatter);
            return formatDateTime;
        }
        else
        {
            return "";
        }
    }

    /**
     *.
     */
    public String getShoppingOn()
    {
        return changeDateFormat.changeDateTimeToDefaultString(erfassungsZeit);
    }
    /**
     *.
     */
    public String getCanceledOn()
    {
        if (this.storniertAm != null)
        {
            return changeDateFormat.changeDateTimeToDefaultString(storniertAm);
        }
        return "";
    }
    /**
     */
    public int getKundennummer()
    {
        return this.kunde != null ? this.kunde.getKundennummer() : -1; // oder ein anderer Standardwert
    }
}
