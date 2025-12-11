package kundenverwaltung.model;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import kundenverwaltung.dao.AusgabegruppeDAO;
import kundenverwaltung.dao.AusgabegruppeDAOimpl;
import kundenverwaltung.dao.AusgabegruppeAusgabeTagZeitDAO;
import kundenverwaltung.dao.AusgabegruppeAusgabeTagZeitDAOimpl;

public class Ausgabegruppe
{

    private AusgabegruppeDAO ausgabegruppeDAO = new AusgabegruppeDAOimpl();
    private AusgabegruppeAusgabeTagZeitDAO ausgabegruppeausgabeTagZeitDAO = new AusgabegruppeAusgabeTagZeitDAOimpl();

    private int ausgabegruppeId;
    private String name;
    private boolean aktiv;
    private Color anzeigeFarbe;
    private ArrayList<AusgabeTagZeit> ausgabeTagZeiten;

    public Ausgabegruppe(int ausgabegruppeId, String name, boolean aktiv, Color anzeigeFarbe, ArrayList<AusgabeTagZeit> ausgabeTagZeiten)
    {
        this.ausgabegruppeId = ausgabegruppeId;
        this.name = name;
        this.aktiv = aktiv;
        this.anzeigeFarbe = anzeigeFarbe;
        this.ausgabeTagZeiten = ausgabeTagZeiten;
    }

    public Ausgabegruppe(String name, boolean aktiv, Color anzeigeFarbe, ArrayList<AusgabeTagZeit> ausgabeTagZeiten)
    {
        this.name = name;
        this.aktiv = aktiv;
        this.anzeigeFarbe = anzeigeFarbe;
        this.ausgabeTagZeiten = ausgabeTagZeiten;
        ausgabegruppeDAO.create(this);
        for (AusgabeTagZeit ausgabeTagZeit : ausgabeTagZeiten)
        {
            ausgabegruppeausgabeTagZeitDAO.ausgabeTagZeitHinzufuegen(this, ausgabeTagZeit);
        }
    }

    // Konstruktor für eine leere Ausgabegruppe im Admin-Tool
    public Ausgabegruppe(String name)
    {
        this.name = name;
        this.aktiv = false;
        this.anzeigeFarbe = Color.WHITE;
        this.ausgabeTagZeiten = new ArrayList<>();
        ausgabegruppeDAO.create(this);
    }
    /***/
    @Override
    public String toString()
    {
        return name;
    }
    /***/
    public int getAusgabegruppeId()
    {
        return ausgabegruppeId;
    }
    /***/
    public void setAusgabegruppeId(int ausgabegruppeId)
    {
        this.ausgabegruppeId = ausgabegruppeId;
    }
    /***/
    public String getName()
    {
        return name;
    }
    /***/
    public boolean isAktiv()
    {
        return aktiv;
    }
    /***/
    public Color getAnzeigeFarbe()
    {
        return anzeigeFarbe;
    }
    /***/
    public ArrayList<AusgabeTagZeit> getAusgabeTagZeiten()
    {
        return ausgabeTagZeiten;
    }
    /***/
    public void setName(String name)
    {
        this.name = name;
    }
    /***/
    public void setAktiv(boolean aktiv)
    {
        this.aktiv = aktiv;
    }
    /***/
    public void setAnzeigeFarbe(Color anzeigeFarbe)
    {
        this.anzeigeFarbe = anzeigeFarbe;
    }
    /***/
    public void setAusgabeTagZeiten(ArrayList<AusgabeTagZeit> ausgabeTagZeiten)
    {
        this.ausgabeTagZeiten = ausgabeTagZeiten;
    }
}
