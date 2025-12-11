package kundenverwaltung.toolsandworkarounds;

import kundenverwaltung.model.Vorlagearten;

public class GetTemplateType
{

    private static final String CASH_SETTLEMENT = "kvpKassenabrechnung";
    private static final String ID_CARD = "kvpAusweis";
    private static final String LIST = "kvpListe";
    private static final String GDPR = "kvpDSGVO";
    private static final String OTHER = "kvpSonstiges";


    /**
     *.
     */
    public Vorlagearten getTemplateType(String templateTypeString)
    {
        switch (templateTypeString)
        {
            case CASH_SETTLEMENT:
                return Vorlagearten.Kassenabrechnung;
            case ID_CARD:
                return Vorlagearten.Ausweis;
            case LIST:
                return Vorlagearten.Liste;
            case GDPR:
                return Vorlagearten.DSGVO;
            case OTHER:
                return Vorlagearten.Sonstiges;
          default:
            break;

        }
        return null;
    }


    /**
     *.
     */
    public String getTemplateString(Vorlagearten templateType)
    {
        switch (templateType)
        {
            case Kassenabrechnung:
                return CASH_SETTLEMENT;
            case Ausweis:
                return ID_CARD;
            case Liste:
                return LIST;
            case DSGVO:
                return GDPR;
            case Sonstiges:
                return OTHER;
          default:
            break;
        }

        return null;
    }



}
