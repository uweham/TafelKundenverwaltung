package kundenverwaltung.model;

import java.util.ArrayList;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NationEinstellungen
{




        // XmLElementWrapper generates a wrapper element around XML representation
        // XmlElement sets the name of the entities
        @XmlElement(name = "Nation")
        private ArrayList<Nation> nationList;


        /**
         *.
         */
    public String getNationalitaet()
    {
        return nationalitaet;
    }
    /**
     *.
     */
    public void setNationalitaet(String nationalitaet)
    {
        this.nationalitaet = nationalitaet;
    }

    private String nationalitaet;
    /**
     *.
     */
        public void setNationenList(ArrayList<Nation> nationList)
        {
            this.nationList = nationList;
        }
        /**
         *.
         */
    public void setNationList(ArrayList<Nation> nationList)
    {
        this.nationList = nationList;
    }
    /**
     *.
     */
    public ArrayList<Nation> getNationList()
    {
        return nationList;
    }
    /**
     *.
     */
        public ArrayList<Nation> geNationenList()
        {
            return nationList;
        }




}
