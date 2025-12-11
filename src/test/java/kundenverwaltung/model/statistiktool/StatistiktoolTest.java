package kundenverwaltung.model.statistiktool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatistiktoolTest {

    private Statistiktool statistiktool;
    private final int gesamtsumme = 100;
    private final int[] gruppen = {10, 20, 70};
    private final int jahresergebnis = 50;
    private final String name = "Deutsche";

    @BeforeEach
    void setUp() {
        statistiktool = new Statistiktool(gesamtsumme, gruppen, jahresergebnis);
    }

    @Test
    @Tag("unit")
    void hauptkonstruktor_setztWerteKorrekt() {
        assertAll("Prüfen der durch den Hauptkonstruktor gesetzten Werte",
                () -> assertEquals(gesamtsumme, statistiktool.getGesamtsumme()),
                () -> assertArrayEquals(gruppen, statistiktool.getGruppen()),
                () -> assertEquals(jahresergebnis, statistiktool.getJahresergebnis()),
                () -> assertNull(statistiktool.getName(), "Name sollte null sein, da er nicht im Konstruktor gesetzt wird.")
        );
    }

    @Test
    @Tag("unit")
    void ueberladenerKonstruktor_setztNurGruppen() {
        Statistiktool nurGruppenTool = new Statistiktool(gruppen);

        assertAll("Prüfen der durch den überladenen Konstruktor gesetzten Werte",
                () -> assertArrayEquals(gruppen, nurGruppenTool.getGruppen()),
                () -> assertEquals(0, nurGruppenTool.getGesamtsumme(), "Gesamtsumme sollte standardmäßig 0 sein."),
                () -> assertEquals(0, nurGruppenTool.getJahresergebnis(), "Jahresergebnis sollte standardmäßig 0 sein.")
        );
    }

    @Test
    @Tag("unit")
    void standardKonstruktor_initialisiertWerteMitStandardwerten() {
        Statistiktool defaultTool = new Statistiktool();

        assertAll("Prüfen der durch den Standard-Konstruktor gesetzten Werte",
                () -> assertNull(defaultTool.getGruppen(), "Gruppen-Array sollte null sein."),
                () -> assertEquals(0, defaultTool.getGesamtsumme()),
                () -> assertEquals(0, defaultTool.getJahresergebnis()),
                () -> assertNull(defaultTool.getName())
        );
    }

    @Test
    @Tag("unit")
    void getterUndSetter_funktionierenWieErwartet() {
        Statistiktool testTool = new Statistiktool();

        testTool.setGesamtsumme(250);
        assertEquals(250, testTool.getGesamtsumme());

        int[] neueGruppen = {5, 15, 25};
        testTool.setGruppen(neueGruppen);
        assertArrayEquals(neueGruppen, testTool.getGruppen());

        testTool.setJahresergebnis(99);
        assertEquals(99, testTool.getJahresergebnis());

        testTool.setName(name);
        assertEquals(name, testTool.getName());
    }

    @Test
    @Tag("unit")
    void getErgebnisForGroup_gibtEingabewertUnveraendertZurueck() {
        int groupNumber = 5;
        int result = statistiktool.getErgebnisForGroup(groupNumber);

        assertEquals(groupNumber, result, "Die Methode sollte den Eingabewert direkt zurückgeben.");
        assertNotEquals(gruppen[0], result, "Das Ergebnis sollte nicht dem Wert aus dem Array entsprechen (es sei denn zufällig).");

        assertEquals(0, statistiktool.getErgebnisForGroup(0));
        assertEquals(-10, statistiktool.getErgebnisForGroup(-10));
    }
}