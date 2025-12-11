package kundenverwaltung.model;

import kundenverwaltung.dao.EinstellungenDAO;
import kundenverwaltung.dao.EinstellungenDAOimpl;

public class Einstellungen
{
    private EinstellungenDAO einstellungenDAO = new EinstellungenDAOimpl();
    private int kundenarchivieren;
    private int gebuehrErwachsener;
    private int alterBescheid;
    private boolean bescheidBenoetigt;
    private boolean datenschutzerklaerung;
    private boolean verteilstellenzugehoerigkeit;
    private int alterErwachsener;
    private String tafelServerHostAddress;

    public Einstellungen(int kundenarchivieren, int gebuehrErwachsener, int alterBescheid, int alterErwachsener, boolean bescheidBenoetigt, boolean datenschutzerklaerung, boolean verteilstellenzugehoerigkeit, String tafelServerHostAddress)
    {
        this.kundenarchivieren = kundenarchivieren;
        this.gebuehrErwachsener = gebuehrErwachsener;
        this.alterBescheid = alterBescheid;
        this.alterErwachsener = alterErwachsener;
        this.bescheidBenoetigt = bescheidBenoetigt;
        this.datenschutzerklaerung = datenschutzerklaerung;
        this.verteilstellenzugehoerigkeit = verteilstellenzugehoerigkeit;
        this.tafelServerHostAddress = tafelServerHostAddress;
    }
    /**
     *.
     */
    public EinstellungenDAO getEinstellungenDAO()
    {
        return einstellungenDAO;
    }
    /**
     *.
     */
    public int getKundenarchivieren()
    {
        return kundenarchivieren;
    }
    /**
     *.
     */
    public void setKundenarchivieren(int kundenarchivieren)
    {
        this.kundenarchivieren = kundenarchivieren;
    }
    /**
     *.
     */
    public int getGebuehrErwachsener()
    {
        return gebuehrErwachsener;
    }
    /**
     *.
     */
    public void setGebuehrErwachsener(int gebuehrErwachsener)
    {
        this.gebuehrErwachsener = gebuehrErwachsener;
    }
    /**
     *.
     */
    public int getAlterBescheid()
    {
        return alterBescheid;
    }
    /**
     *.
     */
    public void setAlterBescheid(int alterBescheid)
    {
        this.alterBescheid = alterBescheid;
    }
    /**
     *.
     */
    public boolean isBescheidBenoetigt()
    {
        return bescheidBenoetigt;
    }
    /**
     *.
     */
    public void setBescheidBenoetigt(boolean bescheidBenoetigt)
    {
        this.bescheidBenoetigt = bescheidBenoetigt;
    }
    /**
     *.
     */
    public boolean isDatenschutzerklaerung()
    {
        return datenschutzerklaerung;
    }
    /**
     *.
     */
    public void setDatenschutzerklaerung(boolean datenschutzerklaerung)
    {
        this.datenschutzerklaerung = datenschutzerklaerung;
    }
    /**
     *.
     */
    public boolean isVerteilstellenzugehoerigkeit()
    {
        return verteilstellenzugehoerigkeit;
    }
    /**
     *.
     */
    public void setVerteilstellenzugehoerigkeit(boolean verteilstellenzugehoerigkeit)
    {
        this.verteilstellenzugehoerigkeit = verteilstellenzugehoerigkeit;
    }
    /**
     *.
     */
    public int getAlterErwachsener()
    {
        return alterErwachsener;
    }
    /**
     *.
     */
    public void setAlterErwachsener(int alterErwachsener)
    {
        this.alterErwachsener = alterErwachsener;
    }

    public String getTafelServerHostAddress()
    {
        return tafelServerHostAddress;
    }
}
