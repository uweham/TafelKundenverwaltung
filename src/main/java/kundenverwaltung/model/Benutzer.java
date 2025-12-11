package kundenverwaltung.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import kundenverwaltung.dao.BenutzerDAO;
import kundenverwaltung.dao.BenutzerDAOimpl;
import kundenverwaltung.dao.BenutzerRechteDAO;
import kundenverwaltung.dao.BenutzerRechteDAOimpl;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class Benutzer
{
    private BenutzerRechteDAO benutzerrechteDAO = new BenutzerRechteDAOimpl();
    private BenutzerDAO benutzerDAO = new BenutzerDAOimpl();
    private int benutzerId;
    private String name;
    private byte[] passwort;
    private String anzeigename;
    private boolean lokal;
    private ArrayList<Recht> rechte;

    public Benutzer(int benutzerId, String name, byte[] passwort, String anzeigename, boolean lokal, ArrayList<Recht> rechte)
    {
        this.benutzerId = benutzerId;
        this.name = name;
        this.passwort = passwort;
        this.anzeigename = anzeigename;
        this.lokal = lokal;
        this.rechte = rechte;
    }

    public Benutzer(String name, String passwort, String anzeigename, boolean lokal, ArrayList<Recht> rechte)
    {
        this.name = name;
        this.passwort = passwortHashen(passwort);
        this.anzeigename = anzeigename;
        this.lokal = lokal;
        this.rechte = rechte;
        benutzerDAO.create(this);
        if (rechte != null)
        {
            for (Recht recht : rechte)
            {
                benutzerrechteDAO.rechtHinzufuegen(this, recht);
            }
        }

    }

    private byte[] passwortHashen(String passwort)
    {
        byte[] hash;
        MessageDigest digest = null;
        try
        {
            digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(passwort.getBytes(StandardCharsets.UTF_8));
            return hash;
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *.
     */
    @Override
    public String toString()
    {
        return anzeigename;
    }
    /**
     *.
     */
    public void addRecht(Recht recht)
        {
        rechte.add(recht);
        benutzerrechteDAO.rechtHinzufuegen(this, recht);
    }
    /**
     *.
     */
    public void entferneRecht(Recht recht)
{
        rechte.remove(recht);
        benutzerrechteDAO.rechtEntfernen(this, recht);
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
    public void setPasswort(String passwort)
    {
        this.passwort = passwortHashen(passwort);
    }
    /**
     *.
     */
    public void setAnzeigename(String anzeigename)
    {
        this.anzeigename = anzeigename;
    }
    /**
     *.
     */
    public void setLokal(boolean lokal)
    {
        this.lokal = lokal;
    }
    /**
     *.
     */
    public void setBenutzerId(int benutzerId)
    {
        this.benutzerId = benutzerId;
    }
    /**
     *.
     */
    public BenutzerRechteDAO getBenutzerrechteDAO()
    {
        return benutzerrechteDAO;
    }
    /**
     *.
     */
    public BenutzerDAO getBenutzerDAO()
    {
        return benutzerDAO;
    }
    /**
     *.
     */
    public int getBenutzerId()
    {
        return benutzerId;
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
    public byte[] getPasswort()
    {
        return passwort;
    }
    /**
     *.
     */
    public String getAnzeigename()
    {
        return anzeigename;
    }
    /**
     *.
     */
    public boolean isLokal()
    {
        return lokal;
    }
    /**
     *.
     */
    public ArrayList<Recht> getRechte()
    {
        return rechte;
    }
}
