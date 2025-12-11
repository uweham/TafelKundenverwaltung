package kundenverwaltung.model.statistiktool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HerkunftTest {

    private Herkunft herkunft;
    private final String strasse = "Musterstraße";
    private final String hausnummer = "1a";
    private final String plz = "12345";
    private final int anzahlHaushalte = 5;

    @BeforeEach
    void setUp() {
        herkunft = new Herkunft(strasse, hausnummer, plz, anzahlHaushalte);
    }

    @Test
    @Tag("unit")
    void konstruktor_setztAlleFelderKorrekt() {
        assertAll("Prüfen der durch den Konstruktor gesetzten Werte",
                () -> assertEquals(strasse, herkunft.getStrasse()),
                () -> assertEquals(hausnummer, herkunft.getHausnummer()),
                () -> assertEquals(plz, herkunft.getPlz()),
                () -> assertEquals(anzahlHaushalte, herkunft.getAnzahlHaushalte())
        );
    }

    @Test
    @Tag("unit")
    void getterUndSetter_funktionierenWieErwartet() {
        String neueStrasse = "Testweg";
        String neueHausnummer = "99";
        String neuePlz = "54321";
        int neueAnzahl = 10;

        herkunft.setStrasse(neueStrasse);
        herkunft.setHausnummer(neueHausnummer);
        herkunft.setPlz(neuePlz);
        herkunft.setAnzahlHaushalte(neueAnzahl);

        assertAll("Prüfen der durch die Setter gesetzten Werte",
                () -> assertEquals(neueStrasse, herkunft.getStrasse()),
                () -> assertEquals(neueHausnummer, herkunft.getHausnummer()),
                () -> assertEquals(neuePlz, herkunft.getPlz()),
                () -> assertEquals(neueAnzahl, herkunft.getAnzahlHaushalte())
        );
    }

    @Test
    @Tag("unit")
    void testMitNullUndNegativWerten() {
        Herkunft testHerkunft = new Herkunft(null, null, null, -1);

        assertAll("Prüfen des Verhaltens mit Null- und Negativwerten",
                () -> assertNull(testHerkunft.getStrasse()),
                () -> assertNull(testHerkunft.getHausnummer()),
                () -> assertNull(testHerkunft.getPlz()),
                () -> assertEquals(-1, testHerkunft.getAnzahlHaushalte())
        );
    }
}