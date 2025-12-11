package kundenverwaltung.model;

import kundenverwaltung.dao.RechteDAO;
import kundenverwaltung.dao.RechteDAOimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RechtTest {

    private Recht recht;

    @BeforeEach
    void setUp() {
        recht = new Recht(1, "Administrator", "Volle Zugriffsrechte");
    }

    @Test
    @Tag("unit")
    void constructor_withId_setsAllFieldsCorrectly() {
        assertAll("Prüfen der durch den Konstruktor gesetzten Werte",
                () -> assertEquals(1, recht.getRechtId()),
                () -> assertEquals("Administrator", recht.getName()),
                () -> assertEquals("Volle Zugriffsrechte", recht.getBeschreibung())
        );
    }

    @Test
    @Tag("unit")
    void constructor_withoutId_callsDAOCreate() {
        try (MockedConstruction<RechteDAOimpl> daoMock = mockConstruction(RechteDAOimpl.class)) {
            Recht neuesRecht = new Recht("Benutzer", "Standardrechte für Benutzer");

            RechteDAO dao = daoMock.constructed().get(0);
            verify(dao, times(1)).create(neuesRecht);

            assertEquals("Benutzer", neuesRecht.getName());
            assertEquals("Standardrechte für Benutzer", neuesRecht.getBeschreibung());
        }
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workAsExpected() {
        Recht testRecht = new Recht(98, "Gast", "Nur Leserechte");

        testRecht.setRechtId(99);
        assertEquals(99, testRecht.getRechtId());

        testRecht.setName("Editor");
        assertEquals("Editor", testRecht.getName());

        testRecht.setBeschreibung("Kann Inhalte bearbeiten");
        assertEquals("Kann Inhalte bearbeiten", testRecht.getBeschreibung());
    }

    @Test
    @Tag("unit")
    void testWithNullValues() {
        Recht nullRecht = new Recht(0, null, null);
        assertNull(nullRecht.getName());
        assertNull(nullRecht.getBeschreibung());
    }
}