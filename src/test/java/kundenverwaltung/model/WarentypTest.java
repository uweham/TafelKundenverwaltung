package kundenverwaltung.model;

import kundenverwaltung.dao.WarentypDAO;
import kundenverwaltung.dao.WarentypDAOimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WarentypTest {

    private Warentyp warentyp;

    @BeforeEach
    void setUp() {
        warentyp = new Warentyp(
                1,
                "Standardeinkauf",
                2.50f,
                1.00f,
                true,
                5.00f,
                20.00f,
                0.50f,
                false,
                1,
                2,
                1,
                3,
                7,
                1,
                0
        );
    }

    @Test
    @Tag("unit")
    void constructor_withId_setsAllFieldsCorrectly() {
        assertAll("Prüfen der durch den Konstruktor mit ID gesetzten Werte",
                () -> assertEquals(1, warentyp.getWarentypId()),
                () -> assertEquals("Standardeinkauf", warentyp.getName()),
                () -> assertEquals(2.50f, warentyp.getPreisErwachsene()),
                () -> assertEquals(1.00f, warentyp.getPreisKinder()),
                () -> assertTrue(warentyp.isAktiv()),
                () -> assertEquals(5.00f, warentyp.getHaushaltspauschale()),
                () -> assertEquals(20.00f, warentyp.getDeckelbetrag()),
                () -> assertEquals(0.50f, warentyp.getMinbetrag()),
                () -> assertFalse(warentyp.isManuelleBerechnung()),
                () -> assertEquals(1, warentyp.getZuordnungPerson()),
                () -> assertEquals(2, warentyp.getZuordnungBuchungstext()),
                () -> assertEquals(1, warentyp.getWarentyplimitanzahl()),
                () -> assertEquals(3, warentyp.getWarentyplimitart()),
                () -> assertEquals(7, warentyp.getWarentyplimitabstand()),
                () -> assertEquals(1, warentyp.getWarentyplimitabstandart()),
                () -> assertEquals(0, warentyp.getNaechsterWarentypid())
        );
    }

    @Test
    @Tag("unit")
    void constructor_withoutId_callsDAOCreate() {
        try (MockedConstruction<WarentypDAOimpl> daoMock = mockConstruction(WarentypDAOimpl.class)) {
            Warentyp neuerWarentyp = new Warentyp(
                    "Sonderposten",
                    0.0f,
                    0.0f,
                    true
            );

            assertEquals(1, daoMock.constructed().size());
            WarentypDAO mockDao = daoMock.constructed().get(0);

            verify(mockDao, times(1)).create(neuerWarentyp);

            assertEquals("Sonderposten", neuerWarentyp.getName());
            assertTrue(neuerWarentyp.isAktiv());
        }
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workAsExpected() {
        Warentyp testWarentyp = new Warentyp();

        testWarentyp.setWarentypId(99);
        assertEquals(99, testWarentyp.getWarentypId());

        testWarentyp.setName("Neuer Name");
        assertEquals("Neuer Name", testWarentyp.getName());

        testWarentyp.setPreisErwachsene(3.0f);
        assertEquals(3.0f, testWarentyp.getPreisErwachsene());

        testWarentyp.setPreisKinder(1.5f);
        assertEquals(1.5f, testWarentyp.getPreisKinder());

        testWarentyp.setAktiv(false);
        assertFalse(testWarentyp.isAktiv());

        testWarentyp.setHaushaltspauschale(10.0f);
        assertEquals(10.0f, testWarentyp.getHaushaltspauschale());

        testWarentyp.setDeckelbetrag(50.0f);
        assertEquals(50.0f, testWarentyp.getDeckelbetrag());

        testWarentyp.setMinbetrag(1.0f);
        assertEquals(1.0f, testWarentyp.getMinbetrag());

        testWarentyp.setManuelleBerechnung(true);
        assertTrue(testWarentyp.isManuelleBerechnung());

        testWarentyp.setNaechsterWarentypid(5);
        assertEquals(5, testWarentyp.getNaechsterWarentypid());
    }

    @Test
    @Tag("unit")
    void toString_returnsCorrectName() {
        assertEquals("Standardeinkauf", warentyp.toString());

        warentyp.setName("Geänderter Einkauf");
        assertEquals("Geänderter Einkauf", warentyp.toString());
    }
}