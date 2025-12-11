package kundenverwaltung.model;

public class Haushaltsinformationen
{

    @SuppressWarnings("unused")
	private Familienmitglied familienmitglied;
    private String beschreibung;
    private Informationstypen typ;

    public Haushaltsinformationen(Familienmitglied familienmitglied, String beschreibung, Informationstypen typ)
    {

        this.familienmitglied = familienmitglied;
        this.beschreibung = beschreibung;
        this.typ = typ;

    }
    /**
     *.
     */
    @Override
    public String toString()
    {
        return beschreibung;
    }
    /**
     *.
     */
    public Informationstypen getTyp()
    {
        return typ;
    }
}
