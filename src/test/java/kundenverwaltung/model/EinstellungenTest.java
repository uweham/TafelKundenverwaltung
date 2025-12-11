package kundenverwaltung.model;

import kundenverwaltung.dao.EinstellungenDAO;
import kundenverwaltung.dao.EinstellungenDAOimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EinstellungenTest {

    private Einstellungen einstellungen;

    @BeforeEach
    void setUp() {
        einstellungen = new Einstellungen(
                90,
                5,
                180,
                18,
                true,
                true,
                false,
                "tafelTest"
        );
    }

    @Test
    @Tag("unit")
    void testConstructorInitialization() {
        assertAll("Prüfen der durch den Konstruktor gesetzten Werte",
                () -> assertEquals(90, einstellungen.getKundenarchivieren()),
                () -> assertEquals(5, einstellungen.getGebuehrErwachsener()),
                () -> assertEquals(180, einstellungen.getAlterBescheid()),
                () -> assertEquals(18, einstellungen.getAlterErwachsener()),
                () -> assertTrue(einstellungen.isBescheidBenoetigt(), "bescheidBenoetigt sollte true sein"),
                () -> assertTrue(einstellungen.isDatenschutzerklaerung(), "datenschutzerklaerung sollte true sein"),
                () -> assertFalse(einstellungen.isVerteilstellenzugehoerigkeit(), "verteilstellenzugehoerigkeit sollte false sein")
        );
    }

    @Test
    @Tag("unit")
    void testGetAndSetKundenarchivieren() {
        einstellungen.setKundenarchivieren(120);
        assertEquals(120, einstellungen.getKundenarchivieren());
    }

    @Test
    @Tag("unit")
    void testGetAndSetGebuehrErwachsener() {
        einstellungen.setGebuehrErwachsener(10);
        assertEquals(10, einstellungen.getGebuehrErwachsener());
    }

    @Test
    @Tag("unit")
    void testGetAndSetAlterBescheid() {
        einstellungen.setAlterBescheid(365);
        assertEquals(365, einstellungen.getAlterBescheid());
    }

    @Test
    @Tag("unit")
    void testGetAndSetAlterErwachsener() {
        einstellungen.setAlterErwachsener(21);
        assertEquals(21, einstellungen.getAlterErwachsener());
    }

    @Test
    @Tag("unit")
    void testIsAndSetBescheidBenoetigt() {
        einstellungen.setBescheidBenoetigt(false);
        assertFalse(einstellungen.isBescheidBenoetigt());
    }

    @Test
    @Tag("unit")
    void testIsAndSetDatenschutzerklaerung() {
        einstellungen.setDatenschutzerklaerung(false);
        assertFalse(einstellungen.isDatenschutzerklaerung());
    }

    @Test
    @Tag("unit")
    void testIsAndSetVerteilstellenzugehoerigkeit() {
        einstellungen.setVerteilstellenzugehoerigkeit(true);
        assertTrue(einstellungen.isVerteilstellenzugehoerigkeit());
    }

    @Test
    @Tag("unit")
    void testGetEinstellungenDAO() {
        EinstellungenDAO dao = einstellungen.getEinstellungenDAO();
        assertNotNull(dao, "Das DAO-Objekt sollte nicht null sein.");
        assertInstanceOf(EinstellungenDAOimpl.class, dao, "Das Objekt sollte eine Instanz von EinstellungenDAOimpl sein.");
    }
}
