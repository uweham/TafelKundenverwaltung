package kundenverwaltung.model;

import java.time.LocalTime;
import kundenverwaltung.dao.AusgabeTagZeitDAO;
import kundenverwaltung.dao.AusgabeTagZeitDAOimpl;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public class AusgabeTagZeit
{
    private AusgabeTagZeitDAO ausgabeTagZeitDAO = new AusgabeTagZeitDAOimpl();
    private int ausgabezeitId;
    private Ausgabetag ausgabetag;
    private LocalTime startzeit;
    private LocalTime endzeit;
    /**
     *.
     */
    @Override
    public String toString()
    {
        return ausgabetag
            +
                " (" + startzeit
                +
                " - " + endzeit
                +
                '}';
    }

    public AusgabeTagZeit(int ausgabezeitId, int ausgabetagId, LocalTime startzeit, LocalTime endzeit)
    {
        this.ausgabezeitId = ausgabezeitId;
        this.ausgabetag = new Ausgabetag(ausgabetagId);
        this.startzeit = startzeit;
        this.endzeit = endzeit;
    }

    public AusgabeTagZeit(Ausgabetag ausgabetag, LocalTime startzeit, LocalTime endzeit)
    {
        this.ausgabetag = ausgabetag;
        this.startzeit = startzeit;
        this.endzeit = endzeit;
        ausgabeTagZeitDAO.create(this);
    }
    /**
     *.
     */
    public AusgabeTagZeitDAO getAusgabeTagZeitDAO()

    {
        return ausgabeTagZeitDAO;
    }
    /**
     *.
     */
    public int getAusgabezeitId()
    {
        return ausgabezeitId;
    }
    /**
     *.
     */
    public String getAusgabetagName()
    {
        return ausgabetag.getName();
    }
    /**
     *.
     */
    public void setAusgabezeitId(int ausgabezeitId)
    {
        this.ausgabezeitId = ausgabezeitId;
    }
    /**
     *.
     */
    public Ausgabetag getAusgabetag()
    {
        return ausgabetag;
    }
    /**
     *.
     */
    public LocalTime getStartzeit()
    {
        return startzeit;
    }
    /**
     *.
     */
    public LocalTime getEndzeit()
    {
        return endzeit;
    }
}
