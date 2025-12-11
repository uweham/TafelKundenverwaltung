package kundenverwaltung.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import kundenverwaltung.dao.OrtsteilDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Testklasse für {@link Ortsteil}.
 */
class OrtsteilTest
{

    /**
     * Mock-Objekt für OrtsteilDAO.
     */
    private OrtsteilDAO mockOrtsteilDAO;

    /**
     * Das zu testende {@link Ortsteil}-Objekt.
     */
    private Ortsteil ortsteil;

    /**
     * Wird vor jedem Test ausgeführt und legt das Testobjekt an.
     */
    @BeforeEach
    void setUp()
    {
        mockOrtsteilDAO = mock(OrtsteilDAO.class);
        ortsteil = new Ortsteil(1, "Zentrum", 49074);
        ortsteil.setOrtsteildao(mockOrtsteilDAO);
    }

    /**
     * Testet die Erstellung eines {@link Ortsteil}-Objekts mit dem Konstruktor.
     */
    @Test
    @Tag("unit")
    void testOrtsteilCreation()
    {
        assertEquals(1, ortsteil.getOrtsteilId());
        assertEquals("Zentrum", ortsteil.getName());
        assertEquals(49074, ortsteil.getPlz());
    }

    /**
     * Testet das Setzen und Auslesen der Eigenschaften von {@link Ortsteil}.
     */
    @Test
    @Tag("unit")
    void testOrtsteilSettersAndGetters()
    {
        ortsteil.setOrtsteilId(2);
        ortsteil.setName("Süd");
        ortsteil.setPlz(49080);

        assertEquals(2, ortsteil.getOrtsteilId());
        assertEquals("Süd", ortsteil.getName());
        assertEquals(49080, ortsteil.getPlz());
    }
}
