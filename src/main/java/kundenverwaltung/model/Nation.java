package kundenverwaltung.model;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;



/**
 * Created by Florian-PC on 02.11.2017.
 */
@XmlRootElement(name = "Nation")
public class Nation
{ //in XML-Datei auslagern
    private int nationId;
    private String name;
    private String nationalitaet;
    private Boolean aktiv;
    private final IntegerProperty anzahl = new SimpleIntegerProperty();

    public Nation(String name, String nationalitaet, boolean aktiv)
    {
        this.name = name;
        this.nationalitaet = nationalitaet;
        this.aktiv = aktiv;
    }
    /**
     */
    public Boolean getAktiv()
    {
        return aktiv;
    }
    /**
     */
    public void setAktiv(Boolean aktiv)
    {
        this.aktiv = aktiv;
    }

    public Nation(int nationId, String name, String nationalitaet)
    {
        this.nationId = nationId;
        this.name = name;
        this.nationalitaet = nationalitaet;
    }
    public Nation(int nationId, String name, String nationalitaet, Boolean aktiv)
    {
        this.nationId = nationId;
        this.name = name;
        this.nationalitaet = nationalitaet;
        this.aktiv = aktiv;
    }

    public Nation(String name, String nationalitaet)
    {
        this.name = name;
        this.nationalitaet = nationalitaet;
    }


    /**
    @Override
    public String toString() {
        return "Nation{" +
                "nationId=" + nationId +
                ", name='" + name + '\'' +
                ", nationalitaet='" + nationalitaet + '\'' +
                '}';
    }
    **/

    @Override
    public String toString()
    {
        return name;
    }
    /**
     */
    @XmlElement
    public int getNationId()
    {
        return nationId;
    }
    /**
     */
    public void setNationId(int nationId)
    {
        this.nationId = nationId;
    }

    /**
     */
    public String getName()
    {
        return name;
    }
    /**
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     */
    public String getNationalitaet()
    {
        return nationalitaet;
    }
    /**
     */
    public void setNationalitaet(String nationalitaet)
    {
        this.nationalitaet = nationalitaet;
    }
    /**
     */
    public void setAnzahl(int anzahl)
    {
        this.anzahl.set(anzahl);
    }
    /**
     */
    public int getAnzahl()
    {
        return anzahl.get();
    }
    /**
     */
    public IntegerProperty anzahlProperty()
    {
        return anzahl;
    }
}
