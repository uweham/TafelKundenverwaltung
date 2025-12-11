package kundenverwaltung.model;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public final class Anrede
{
    private int anredeId;
    private String anrede;
    private String herr = "Herr";
    private String frau = "Frau";
    private String divers = "Divers";


    public Anrede(int anredeId)
    {
        this.anredeId = anredeId;
        this.anrede = getAnrede(anredeId);
    }

    private String getAnrede(int anredeId)
    {
        switch (anredeId)
        {
            case 31: return herr;
            case 32: return frau;
            case 33: return divers;
          default:
            break;
        }
        return null;
    }

    public int getAnredeId()
    {
        return anredeId;
    }

    public String getAnrede()
    {
        return anrede;
    }
}
