package kundenverwaltung.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;
import kundenverwaltung.dao.BescheidDAOimpl;
import kundenverwaltung.dao.EinkaufDAOimpl;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.dao.HaushaltDAO;
import kundenverwaltung.dao.HaushaltDAOimpl;
import kundenverwaltung.dao.VollmachtDAOimpl;
import kundenverwaltung.dao.EinstellungenDAOimpl;
import kundenverwaltung.model.Einstellungen;
/**
 * Created by Florian-PC on 02.11.2017.
 * <p>
 * Last Change:
 *
 * @Author Adam Starobrzanski Date: 27.07.2018
 */
public class Haushalt
{
    private HaushaltDAO haushaltDAO = new HaushaltDAOimpl();
    private int kundennummer;

    //Attribut für örtliche Kundennummer fehlt noch
    //Attribut für Belieferung und Datenschutz fehlt noch
    private String strasse;
    private String hausnummer;
    private PLZ plz;
    private String telefonnummer;
    private String mobilnummer;
    private String bemerkungen;
    private LocalDate kundeSeit;
    private float saldo;
    private Verteilstelle verteilstelle;
    private boolean istArchiviert;
    private boolean istGesperrt;
    private Ausgabegruppe ausgabegruppe;
    private boolean belieferung;
    private boolean datenschutzerklaerung;

    private boolean einkaufsberechtigt;
    private ArrayList<Haushaltsinformationen> haushaltsinformationen = new ArrayList<>();



    public Haushalt(int kundennummer, String strasse, String hausnummer, PLZ plz,
                    String telefonnummer, String mobilnummer, String bemerkungen,
                    LocalDate kundeSeit, float saldo, Verteilstelle verteilstelle,
                    boolean istArchiviert, boolean istGesperrt, Ausgabegruppe ausgabegruppe,
                    boolean belieferung, boolean datenschutzerklaerung)
    {
        this.kundennummer = kundennummer;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.telefonnummer = telefonnummer;
        this.mobilnummer = mobilnummer;
        this.bemerkungen = bemerkungen;
        this.kundeSeit = kundeSeit;
        this.saldo = saldo;
        this.verteilstelle = verteilstelle;
        this.istArchiviert = istArchiviert;
        this.istGesperrt = istGesperrt;
        this.ausgabegruppe = ausgabegruppe;
        this.belieferung = belieferung;
        this.datenschutzerklaerung = datenschutzerklaerung;
    }


    /**
     *.
     */
    public boolean isBelieferung()
    {
        return belieferung;
    }


    /**
     *.
     */
    public void setBelieferung(boolean belieferung)
    {
        this.belieferung = belieferung;
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



    public Haushalt(String strasse, String hausnummer, PLZ plz, String telefonnummer,
                    String mobilnummer, String bemerkungen, LocalDate kundeSeit, float saldo,
                    Verteilstelle verteilstelle, Ausgabegruppe ausgabegruppe, boolean belieferung,
                    boolean datenschutzerklaerung)
    {
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.telefonnummer = telefonnummer;
        this.mobilnummer = mobilnummer;
        this.bemerkungen = bemerkungen;
        this.kundeSeit = kundeSeit;
        this.saldo = saldo;
        this.verteilstelle = verteilstelle;
        this.ausgabegruppe = ausgabegruppe;
        this.belieferung = belieferung;
        this.datenschutzerklaerung = datenschutzerklaerung;
//        haushaltDAO.create(this);
    }


    /**
     *.
     */
    public float getZuZahlen(Warentyp wt)
    {
      float gebuehr;
      // 1) Pauschale hat Vorrang
      if (wt.getHaushaltspauschale() > 0)
      {
          gebuehr = wt.getHaushaltspauschale();
      } else
      {
          // 2) Sonst pro Person: zählt intern Erwachsenen/Kinder über Dein Alter-Kriterium
          int erw = getanzahlErwachsene();
          int kin = getanzahlKinder();
          gebuehr = erw * wt.getPreisErwachsene()
                 + kin * wt.getPreisKinder();
      }
      // 3) Deckel anwenden
      if (wt.getDeckelbetrag() > 0 && gebuehr > wt.getDeckelbetrag())
      {
          gebuehr = wt.getDeckelbetrag();
      }
      // 4) Nie negativ
      return Math.max(0, gebuehr);
  }



    /**
     *.
     */
    public int getanzahlKinder()
    {
        // NEU: Einstellungen aus der Datenbank laden
        Einstellungen einstellungen = new EinstellungenDAOimpl().read();
        // Fallback, falls in der DB nichts steht (0), nehmen wir 18 als Sicherheit
        int grenzAlter = (einstellungen.getAlterErwachsener() > 0) ? einstellungen.getAlterErwachsener() : 18;

        ArrayList<Familienmitglied> familienmitgliedListe =
                new FamilienmitgliedDAOimpl().getAllFamilienmitglieder(this.getKundennummer());
        int anzahlKinder = 0;
        for (Familienmitglied element : familienmitgliedListe)
        {
            LocalDate now = LocalDate.now();
            LocalDate gDatum = element.getgDatum();
            long alter = ChronoUnit.YEARS.between(gDatum, now);

            // HIER GEÄNDERT: Variable statt starrer Zahl
            if (alter < grenzAlter)
            {
                anzahlKinder++;
            }
        }
        return anzahlKinder;
    }

    /**
     *.
     */
    public int getanzahlErwachsene()
    {
        // NEU: Einstellungen laden
        Einstellungen einstellungen = new EinstellungenDAOimpl().read();
        // Fallback auf 18, falls DB leer/0 ist
        int grenzAlter = (einstellungen.getAlterErwachsener() > 0) ? einstellungen.getAlterErwachsener() : 18;

        ArrayList<Familienmitglied> familienmitgliedListe =
                new FamilienmitgliedDAOimpl().getAllFamilienmitglieder(this.getKundennummer());
        int anzahlErwachsene = 0;
        for (Familienmitglied element : familienmitgliedListe)
        {
            LocalDate now = LocalDate.now();
            LocalDate gDatum = element.getgDatum();
            long alter = ChronoUnit.YEARS.between(gDatum, now);

            // HIER GEÄNDERT: Variable statt starrer Zahl
            // WICHTIG: >= verwenden, damit das Grenzalter selbst schon als Erwachsen zählt
            if (alter >= grenzAlter)
            {
                anzahlErwachsene++;
            }
        }
        return anzahlErwachsene;
    }



    /**
     * @param warentyp productype
     * @return returns a warning text for the pop-up Window
     * @Author Gruppe_1
     * @Author Adam Starobrzanski
     * @Author Robin Becker, Philipp Wilm Date 15.08.2018
     */

    public ArrayList<String> getBuchungswarnungen(Warentyp warentyp)
    {
        
        ArrayList<String> warnungen = new ArrayList<>();
        if (this.saldo < 0.0)
        {
            warnungen.add("Das Kundenkonto hat derzeit einen Sollsaldo von " + this.saldo + "EUR.");
        }
        LocalDateTime letzerEinkauf = new EinkaufDAOimpl().getLetzerEinkauf(this);
        long datediff=0L;
        if (letzerEinkauf != null)
        {
          datediff = ChronoUnit.DAYS.between(letzerEinkauf.toLocalDate(), LocalDate.now());
        }
        if (letzerEinkauf != null)
        {
            // bugfix datecompare
            if (datediff
                <=
                    (long) warentyp.getWarentyplimitabstand() && warentyp.getWarentyplimitabstand() != 0)
            {
                warnungen.add("Der letzte Einkauf (" + letzerEinkauf
                    +
                        ") liegt noch nicht lange genug zurück. Erforderlich "
                    +
                        warentyp.getWarentyplimitabstand() + " Tage");
            }
        }

        if (letzerEinkauf != null && datediff == 0L && warentyp.getWarentypId() != 3)
        {
            warnungen.add("Der Kunde hat heute bereits eingekauft!");
        }

        ArrayList<Familienmitglied> familienmitglieder =
                new FamilienmitgliedDAOimpl().getAllFamilienmitglieder(this.getKundennummer());
        for (int i = 0; i < familienmitglieder.size(); i++)
        {
            ArrayList<Bescheid> bescheid =
                    new BescheidDAOimpl().readAllGueltige(familienmitglieder.get(i));
            if (bescheid.size() == 0)
            {
                warnungen.add("Für " + familienmitglieder.get(i).getvName() + " "
            +
                        familienmitglieder.get(i).getnName()
                        +
                        " liegen keine gültigen Bescheide vor.");
            }
            if (familienmitglieder.get(i).getGeburtsdatum() == null)
            {
                warnungen.add(familienmitglieder.get(i).getvName() + " "
            +
                        familienmitglieder.get(i).getnName()
                        +
                        " hat kein gültiges Geburtsdatum. Die Betrags-Berechnung könnte somit "
                        +
                        "fehlerhaft sein. Bitte prüfen Sie die Daten.");
            }
        }

        return warnungen;
    }

    /**
     *.
     */
    public ArrayList<Haushaltsinformationen> getHaushaltsinformationen(
            ArrayList<Familienmitglied> alleFamilienmitglieder)
    {
        // --- NEU: Einstellungen laden (Optimierung: Nur 1x vor der Schleife) ---
        Einstellungen einstellungen = new EinstellungenDAOimpl().read();
        
        // Fallback auf 18, falls die Datenbank 0 liefert oder das Objekt null ist.
        // Das ist sicherer als 14, da Kinder unter 18 meist keinen eigenen Bescheid haben.
        int alterBescheidPflicht = (einstellungen != null && einstellungen.getAlterBescheid() > 0) 
                                   ? einstellungen.getAlterBescheid() 
                                   : 18; 
        // -----------------------------------------------------------------------

        int anzahlungueltigeBescheideHaushalt = 0;
        einkaufsberechtigt = true;

        if (istGesperrt)
        {
            haushaltsinformationen.add(new Haushaltsinformationen(null, "Haushalt ist gesperrt",
                    Informationstypen.Gesperrt));
            einkaufsberechtigt = false;
        }
        if (belieferung)
        {
            haushaltsinformationen.add(new Haushaltsinformationen(null, "Haushalt wird beliefert",
                    Informationstypen.Belieferung));
        }
        if (saldo < 0.00)
        {
            haushaltsinformationen
                    .add(new Haushaltsinformationen(null, "Das Kundenkonto ist negativ",
                            Informationstypen.Kontensaldo));
        }
        if (istArchiviert)
        {
            haushaltsinformationen
                    .add(new Haushaltsinformationen(null, "Der Kunde befindet sich im Archiv",
                            Informationstypen.Archiv));
        }

        for (int i = 0; i < alleFamilienmitglieder.size(); i++)
        {
            Familienmitglied familienmitgliedAkt = alleFamilienmitglieder.get(i);

            ArrayList<Bescheid> bescheide =
                    new BescheidDAOimpl().readAllGueltige(familienmitgliedAkt);

            // --- HIER WURDE GEÄNDERT: Variable statt fester Zahl (Bug 5 Fix) ---
            if ((familienmitgliedAkt.getGeburtsdatum().toLocalDate()
                    .isBefore(LocalDate.now().minusYears(alterBescheidPflicht)))
                &&
                    (bescheide == null || bescheide.size() == 0))
            {
                haushaltsinformationen.add(new Haushaltsinformationen(familienmitgliedAkt,
                        familienmitgliedAkt.getName() + ": hat keinen gültigen Bescheid",
                        Informationstypen.BescheideUngueltig));
                anzahlungueltigeBescheideHaushalt++;
                einkaufsberechtigt = false;
            }
            // -------------------------------------------------------------------

            if (familienmitgliedAkt.getBerechtigung().getBerechtigungId() != 49)
            {
                haushaltsinformationen.add(new Haushaltsinformationen(familienmitgliedAkt,
                        "Bes. Berechtigung für " + familienmitgliedAkt.getName() + ": "
                +
                                familienmitgliedAkt.getBerechtigung().getName(),
                        Informationstypen.Berechtigung));
            }
            if (familienmitgliedAkt.isGebuehrenBefreiung())
            {
                haushaltsinformationen.add(new Haushaltsinformationen(familienmitgliedAkt,
                        familienmitgliedAkt.getName() + ": "
                +
                                "ist von der Gebührenzahlung befreit",
                        Informationstypen.Gebuehrenbefreiung));
            }
            if (!Objects.equals(familienmitgliedAkt.getBemerkung(), "")
                &&
                    familienmitgliedAkt.getBemerkung() != null)
            {
                haushaltsinformationen.add(new Haushaltsinformationen(familienmitgliedAkt,
                        "Bemerkung zu " + familienmitgliedAkt + ": "
                +
                                familienmitgliedAkt.getBemerkung(), Informationstypen.Bemerkung));
            }
        }

        if (anzahlungueltigeBescheideHaushalt == 0)
        {
            haushaltsinformationen
                    .add(new Haushaltsinformationen(null, "Alle Bescheide sind gültig",
                            Informationstypen.BescheideGueltig));
            // haushaltsinformationen.add(new Haushaltsinformationen(familienmitgliedAkt,
            // familienmitgliedAkt + ": Bescheid ist gültig.", Informationstypen.BescheideGueltig));
        }

        ArrayList<Vollmacht> vollmachten = new VollmachtDAOimpl().readAllGueltige(kundennummer);

        if (vollmachten != null && vollmachten.size() != 0)
        {

            for (int i = 0; i < vollmachten.size(); i++)
            {
                haushaltsinformationen.add(new Haushaltsinformationen(null,
                        "Vollmacht besteht für "
                +
                                vollmachten.get(i).getBevollmaechtigtePerson().getName()
                                +
                                "(Kd.-Nr. "
                                +
                                vollmachten.get(i).getBevollmaechtigtePerson().getHaushalt()
                                        .getKundennummer() + ")", Informationstypen.Vollmacht));
            }

        }

        return haushaltsinformationen;
    }


    /**
     *.
     */
    public Boolean getEinkaufsberechtigt()
    {
        return einkaufsberechtigt;
    }


    /**
     *.
     */
    public Familienmitglied getHaushaltsvorstand(ArrayList<Familienmitglied> alleFamilienmitglieder)
    {

        Familienmitglied haushaltsvorstand = null;

        for (Familienmitglied element : alleFamilienmitglieder)
        {
            if (element.isHaushaltsVorstand())
            {
                haushaltsvorstand = element;
                break;
            }
        }
        return haushaltsvorstand;
    }


    /**
     *.
     */
    public HaushaltDAO getHaushaltDAO()
    {
        return haushaltDAO;
    }


    /**
     *.
     */
    public void setKundennummer(int kundennummer)
    {
        this.kundennummer = kundennummer;
    }


    /**
     *.
     */
    public int getKundennummer()
    {
        return kundennummer;
    }


    /**
     *.
     */
    public String getStrasse()
    {
        return strasse;
    }

    /**
     *.
     */

    public String getHausnummer()
    {
        return hausnummer;
    }

    /**
     *.
     */

    public PLZ getPlz()
    {
        return plz;
    }

    /**
     *.
     */

    public String getTelefonnummer()
    {
        return telefonnummer;
    }


    /**
     *.
     */
    public String getMobilnummer()
    {
        return mobilnummer;
    }


    /**
     *.
     */
    public String getBemerkungen()
    {
        return bemerkungen;
    }


    /**
     *.
     */
    public LocalDate getKundeSeit()
    {
        return kundeSeit;
    }


    /**
     *.
     */
    public float getSaldo()
    {
        return saldo;
    }

    /**
     *.
     */

    public Verteilstelle getVerteilstelle()
    {
        return verteilstelle;
    }

    /**
     *.
     */

    public boolean getIstArchiviert()
    {
        return istArchiviert;
    }

    /**
     *.
     */

    public boolean getIstGesperrt()
    {
        return istGesperrt;
    }

    /**
     *.
     */

    public Ausgabegruppe getAusgabegruppe()
    {
        return ausgabegruppe;
    }

    /**
     *.
     */

    public void setStrasse(String strasse)
    {
        this.strasse = strasse;
    }

    /**
     *.
     */

    public void setHausnummer(String hausnummer)
    {
        this.hausnummer = hausnummer;
    }

    /**
     *.
     */

    public void setPlz(PLZ plz)
    {
        this.plz = plz;
    }

    /**
     *.
     */

    public void setTelefonnummer(String telefonnummer)
    {
        this.telefonnummer = telefonnummer;
    }

    /**
     *.
     */

    public void setMobilnummer(String mobilnummer)
    {
        this.mobilnummer = mobilnummer;
    }


    /**
     *.
     */
    public void setBemerkungen(String bemerkungen)
    {
        this.bemerkungen = bemerkungen;
    }

    /**
     *.
     */

    public void setKundeSeit(LocalDate kundeSeit)
    {
        this.kundeSeit = kundeSeit;
    }


    /**
     *.
     */
    public void setSaldo(float saldo)
    {
        this.saldo = saldo;
    }


    /**
     *.
     */
    public void setVerteilstelle(Verteilstelle verteilstelle)
    {
        this.verteilstelle = verteilstelle;
    }

    /**
     *.
     */

    public void setIstArchiviert(boolean istArchiviert)
    {
        this.istArchiviert = istArchiviert;
    }


    /**
     *.
     */
    public void setIstGesperrt(boolean istGesperrt)
    {
        this.istGesperrt = istGesperrt;
    }


    /**
     *.
     */
    public void setAusgabegruppe(Ausgabegruppe ausgabegruppe)
    {
        this.ausgabegruppe = ausgabegruppe;
    }

}
