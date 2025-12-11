package kundenverwaltung.model;

import java.sql.Blob;
import kundenverwaltung.dao.VorlageDAO;
import kundenverwaltung.dao.VorlageDAOimpl;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public class Vorlage
{
    public static final String ENABLED = "aktiv";
    public static final String DISABLED = "inaktiv";

    private VorlageDAO vorlageDAO = new VorlageDAOimpl();
    private int vorlageId;
    private Vorlagearten templateType; //hab aus String nen Enum gemacht, weil die möglichen Arten ja fest sind
    private String name;
    private String autor;
    private String fileVersion;
    private String fileTypes;
    private String defaultText;
    private int passwort;
    private boolean aktiv;
    private Blob daten; //Typ?

    public Vorlage(int vorlageId, Vorlagearten templateType, String name, String autor, String fileVersion, String fileTypes, String defaultText, int passwort, boolean aktiv, Blob daten)
    {
        this.vorlageId = vorlageId;
        this.templateType = templateType;
        this.name = name;
        this.autor = autor;
        this.fileVersion = fileVersion;
        this.fileTypes = fileTypes;
        this.defaultText = defaultText;
        this.passwort = passwort;
        this.aktiv = aktiv;
        this.daten = daten;
    }

    public Vorlage(Vorlagearten templateType, String name, String autor, String fileVersion, String fileTypes, String defaultText, int passwort, boolean aktiv, Blob daten)
    {
        this.templateType = templateType;
        this.name = name;
        this.autor = autor;
        this.fileVersion = fileVersion;
        this.fileTypes = fileTypes;
        this.defaultText = defaultText;
        this.passwort = passwort;
        this.aktiv = aktiv;
        this.daten = daten;
        vorlageDAO.create(this);
    }
    /**
     *.
     */
    public void setVorlageId(int vorlageId)
    {
        this.vorlageId = vorlageId;
    }
    /**
     *.
     */
    public int getVorlageId()
    {
        return vorlageId;
    }
    /**
     *.
     */
    public Vorlagearten getTemplateType()
    {
        return templateType;
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
    public String getAutor()
    {
        return autor;
    }
    /**
     *.
     */
    public String getFileVersion()
    {
        return fileVersion;
    }
    /**
     *.
     */
    public String getFileTypes()
    {
        return fileTypes;
    }
    /**
     *.
     */
    public String getDefaultText()
    {
        return defaultText;
    }
    /**
     *.
     */
    public int getPasswort()
    {
        return passwort;
    }
    /**
     *.
     */
    public boolean isAktiv()
    {
        return aktiv;
    }
    /**
     *.
     */
    public Blob getDaten()
    {
        return daten;
    }


    //TableView
    /**
     *.
     */
    public String isAktivString()
    {
        if (aktiv)
        {
            return ENABLED;
        }
        else
            {
                return DISABLED;
            }
    }
}
