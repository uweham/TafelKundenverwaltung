package kundenverwaltung.model;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public final class Ausgabetag
{
    private int tagId;
    private String name;
    private String abkuerzung;

    public Ausgabetag(int tagId)
    {
        this.tagId = tagId;
        switch (tagId)
        {
            case 1: this.name = "Sonntag";
                    this.abkuerzung = "So.";
                break;
            case 2: this.name = "Montag";
                    this.abkuerzung = "Mo.";
                    break;
            case 3: this.name = "Dienstag";
                    this.abkuerzung = "Di.";
                    break;
            case 4: this.name = "Mittwoch";
                    this.abkuerzung = "Mi.";
                    break;
            case 5: this.name = "Donnerstag";
                    this.abkuerzung = "Do.";
                    break;
            case 6: this.name = "Freitag";
                    this.abkuerzung = "Fr.";
                    break;
            case 7: this.name = "Samstag";
                    this.abkuerzung = "Sa.";
                    break;
          default:
            break;
        }
    }

    public int getTagId()
    {
        return tagId;
    }

    public String getName()
    {
        return name;
    }

    public String getAbkuerzung()
    {
        return abkuerzung;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
