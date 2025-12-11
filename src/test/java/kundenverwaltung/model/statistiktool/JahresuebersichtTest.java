package kundenverwaltung.model.statistiktool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JahresuebersichtTest {

    private Jahresuebersicht jahresuebersicht;
    private final String monat = "Januar";
    private final int neuzugaenge = 10;
    private final int anzahlPersonen = 150;
    private final double umsatzHaushalt = 1200.50;
    private final double umsatzEinkauf = 3500.75;

    @BeforeEach
    void setUp() {
        jahresuebersicht = new Jahresuebersicht(monat, neuzugaenge, anzahlPersonen, umsatzHaushalt, umsatzEinkauf);
    }

    @Test
    @Tag("unit")
    void hauptkonstruktor_setztAlleFelderKorrekt() {
        assertAll("Prüfen der durch den Hauptkonstruktor gesetzten Werte",
                () -> assertEquals(monat, jahresuebersicht.getMonat()),
                () -> assertEquals(neuzugaenge, jahresuebersicht.getNeuzugaenge()),
                () -> assertEquals(anzahlPersonen, jahresuebersicht.getAnzahlPersonen()),
                () -> assertEquals(umsatzHaushalt, jahresuebersicht.getGesamtUmsatzHaushalt()),
                () -> assertEquals(umsatzEinkauf, jahresuebersicht.getGesamtUmsatzEinkauf())
        );
    }

    @Test
    @Tag("unit")
    void ueberladenerKonstruktor_setztUmsaetzeAufNull() {
        Jahresuebersicht uebersichtOhneUmsatz = new Jahresuebersicht(monat, neuzugaenge, anzahlPersonen);

        assertAll("Prüfen der durch den überladenen Konstruktor gesetzten Werte",
                () -> assertEquals(monat, uebersichtOhneUmsatz.getMonat()),
                () -> assertEquals(neuzugaenge, uebersichtOhneUmsatz.getNeuzugaenge()),
                () -> assertEquals(anzahlPersonen, uebersichtOhneUmsatz.getAnzahlPersonen()),
                () -> assertEquals(0.0, uebersichtOhneUmsatz.getGesamtUmsatzHaushalt(), "Umsatz Haushalt sollte 0.0 sein"),
                () -> assertEquals(0.0, uebersichtOhneUmsatz.getGesamtUmsatzEinkauf(), "Umsatz Einkauf sollte 0.0 sein")
        );
    }

    @Test
    @Tag("unit")
    void getterUndSetter_funktionierenWieErwartet() {
        String neuerMonat = "Februar";
        int neueZugaenge = 5;
        int neuePersonen = 155;
        double neuerUmsatzHaushalt = 1300.0;
        double neuerUmsatzEinkauf = 3800.0;

        jahresuebersicht.setMonat(neuerMonat);
        jahresuebersicht.setNeuzugaenge(neueZugaenge);
        jahresuebersicht.setAnzahlPersonen(neuePersonen);
        jahresuebersicht.setGesamtUmsatzHaushalt(neuerUmsatzHaushalt);
        jahresuebersicht.setGesamtUmsatzEinkauf(neuerUmsatzEinkauf);

        assertAll("Prüfen der durch die Setter gesetzten Werte",
                () -> assertEquals(neuerMonat, jahresuebersicht.getMonat()),
                () -> assertEquals(neueZugaenge, jahresuebersicht.getNeuzugaenge()),
                () -> assertEquals(neuePersonen, jahresuebersicht.getAnzahlPersonen()),
                () -> assertEquals(neuerUmsatzHaushalt, jahresuebersicht.getGesamtUmsatzHaushalt()),
                () -> assertEquals(neuerUmsatzEinkauf, jahresuebersicht.getGesamtUmsatzEinkauf())
        );
    }

    @Test
    @Tag("unit")
    void toString_gibtFormatiertenStringZurueck() {
        String expected = String.format("Monat: %s, Neuzugänge: %d, Anzahl Personen: %d, Gesamtumsatz Haushalt: %.2f, Gesamtumsatz Einkauf: %.2f",
                monat, neuzugaenge, anzahlPersonen, umsatzHaushalt, umsatzEinkauf);
        assertEquals(expected, jahresuebersicht.toString());
    }

    @Test
    @Tag("unit")
    void equalsUndHashCode_funktionierenKonsistent() {
        Jahresuebersicht gleicheUebersicht = new Jahresuebersicht(monat, neuzugaenge, anzahlPersonen, umsatzHaushalt, umsatzEinkauf);
        Jahresuebersicht andereUebersicht = new Jahresuebersicht("Februar", 5, 160, 100.0, 200.0);

        assertEquals(jahresuebersicht, jahresuebersicht);

        assertEquals(jahresuebersicht, gleicheUebersicht);
        assertEquals(gleicheUebersicht, jahresuebersicht);

        assertEquals(jahresuebersicht.hashCode(), gleicheUebersicht.hashCode());

        assertNotEquals(jahresuebersicht, andereUebersicht);
        assertNotEquals(jahresuebersicht.hashCode(), andereUebersicht.hashCode());

        assertNotEquals(null, jahresuebersicht);
        assertNotEquals(jahresuebersicht, new Object());
    }
}
