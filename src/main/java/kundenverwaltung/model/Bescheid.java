package kundenverwaltung.model;

import java.time.LocalDate;
import kundenverwaltung.dao.BescheidDAO;
import kundenverwaltung.dao.BescheidDAOimpl;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class Bescheid
{
    private BescheidDAO bescheidDAO = new BescheidDAOimpl();
    private int bescheidId;

    private IntegerProperty anzahlPersonen = new SimpleIntegerProperty(); // Anzahl als IntegerProperty

    private Familienmitglied person;
    private Bescheidart bescheidart;
    private LocalDate gueltigAb;
    private LocalDate gueltigBis;

    private ChangeDateFormat changeDateFormat = new ChangeDateFormat();

    // Konstruktor mit allen Parametern
    public Bescheid(int bescheidId, Familienmitglied person, int anzahlPersonen, Bescheidart bescheidart, LocalDate gueltigAb, LocalDate gueltigBis)
    {
        this.bescheidId = bescheidId;
        this.person = person;
        this.anzahlPersonen.set(anzahlPersonen); // Setzen der Anzahl
        this.bescheidart = bescheidart;
        this.gueltigAb = gueltigAb;
        this.gueltigBis = gueltigBis;
    }

    // Konstruktor ohne bescheidId
    public Bescheid(Familienmitglied person, Bescheidart bescheidart, LocalDate gueltigAb, LocalDate gueltigBis, int anzahlPersonen)
    {
        this.person = person;
        this.bescheidart = bescheidart;
        this.gueltigAb = gueltigAb;
        this.gueltigBis = gueltigBis;
        this.anzahlPersonen.set(anzahlPersonen); // Setzen der Anzahl
        // bescheidDAO.create(this); // Aktivieren, wenn Sie speichern möchten
    }

    // Weitere Konstruktoren
    public Bescheid(int bescheidId, Familienmitglied familienmitglied, Bescheidart bescheidart, LocalDate gueltigAb, LocalDate gueltigBis)
    {
        this.bescheidId = bescheidId;
        this.person = familienmitglied;
        this.bescheidart = bescheidart;
        this.gueltigAb = gueltigAb;
        this.gueltigBis = gueltigBis;
    }

    public Bescheid(Familienmitglied familienmitglied, Bescheidart bescheidArt, LocalDate gueltigAb, LocalDate gueltigBis)
    {
        this.person = familienmitglied;
        this.bescheidart = bescheidArt;
        this.gueltigAb = gueltigAb;
        this.gueltigBis = gueltigBis;
    }

    public Bescheid(int bescheidId, Object o, Bescheidart bescheidart)
    {
    }
    /**
     */
    public BescheidDAO getBescheidDAO()
    {
        return bescheidDAO;
    }
    /**
     */
    public boolean isGueltig()
    {
        return gueltigAb.minusDays(1).isBefore(LocalDate.now()) && LocalDate.now().isBefore(gueltigBis.plusDays(1));
    }
    /**
     */
    public int getAnzahlPersonen()
    {
        return anzahlPersonen.get(); // Rückgabe der Anzahl
    }
    /**
     */
    public void setAnzahlPersonen(int value)
    {
        this.anzahlPersonen.set(value); // Setzen der Anzahl
    }
    /**
     */
    public IntegerProperty anzahlPersonenProperty()
    {
        return anzahlPersonen; // Rückgabe der IntegerProperty
    }
    /**
     */
    public void setBescheidId(int bescheidId)
    {
        this.bescheidId = bescheidId;
    }
    /**
     */
    public int getBescheidId()
    {
        return bescheidId;
    }
    /**
     */
    public Familienmitglied getPerson()
    {
        return person;
    }
    /**
     */
    public LocalDate getGueltigAb()
    {
        return gueltigAb;
    }
    /**
     */
    public LocalDate getGueltigBis()
    {
        return gueltigBis;
    }
    /**
     */
    public void setPerson(Familienmitglied familienmitglied)
    {
        this.person = familienmitglied;
    }
    /**
     */
    public void setBescheidart(Bescheidart bescheidart)
    {
        this.bescheidart = bescheidart;
    }
    /**
     */
    public void setGueltigAb(LocalDate gueltigAb)
    {
        this.gueltigAb = gueltigAb;
    }
    /**
     */
    public void setGueltigBis(LocalDate gueltigBis)
    {
        this.gueltigBis = gueltigBis;
    }
    /**
     */
    // Für Tableviews
    public String getName()
    {
        return person.getName(); // Überprüfe, ob die Methode getName() in der Klasse Familienmitglied existiert
    }
    /**
     */
    public Bescheidart getBescheidart()
    {
        return bescheidart; // Rückgabe der Bescheidart
    }
    /**
     */
    public String getBescheidName()
    {
        return bescheidart.getName(); // Überprüfe, ob die Methode getName() in der Klasse Bescheidart existiert
    }
    /**
     */
    public String getValidFrom()
    {
        return changeDateFormat.changeDateToDefaultString(this.gueltigAb); // Überprüfe, ob die Methode korrekt funktioniert
    }
    /**
     */
    public String getDateOfExpiry()
    {
        return changeDateFormat.changeDateToDefaultString(this.gueltigBis); // Überprüfe, ob die Methode korrekt funktioniert
    }

}
