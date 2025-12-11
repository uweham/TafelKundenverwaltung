package kundenverwaltung.model;

import kundenverwaltung.dao.VerteilstelleDAO;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerteilstelleTest {

    private Verteilstelle verteilstelle;

    @BeforeEach
    void setUp() {
        verteilstelle = new Verteilstelle(
                1,
                "Hauptstelle",
                "Musterstraße 1, 49074 Osnabrück",
                10
        );
    }

    @Test
    @Tag("unit")
    void constructor_withId_setsAllFieldsCorrectly() {
        assertAll("Prüfen der durch den Konstruktor mit ID gesetzten Werte",
                () -> assertEquals(1, verteilstelle.getVerteilstellenId()),
                () -> assertEquals("Hauptstelle", verteilstelle.getBezeichnung()),
                () -> assertEquals("Musterstraße 1, 49074 Osnabrück", verteilstelle.getAdresse()),
                () -> assertEquals(10, verteilstelle.getListennummer())
        );
    }

    @Test
    @Tag("unit")
    void constructor_withoutId_callsDAOCreate() {
        try (MockedConstruction<VerteilstelleDAOimpl> daoMock = mockConstruction(VerteilstelleDAOimpl.class)) {
            Verteilstelle neueStelle = new Verteilstelle(
                    "Zweigstelle",
                    "Nebenweg 2, 49076 Osnabrück",
                    20
            );

            assertEquals(1, daoMock.constructed().size());
            VerteilstelleDAO mockDao = daoMock.constructed().get(0);

            verify(mockDao, times(1)).create(neueStelle);

            assertEquals("Zweigstelle", neueStelle.getBezeichnung());
            assertEquals(20, neueStelle.getListennummer());
        }
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workAsExpected() {
        verteilstelle.setVerteilstellenId(99);
        assertEquals(99, verteilstelle.getVerteilstellenId());

        verteilstelle.setBezeichnung("Lager");
        assertEquals("Lager", verteilstelle.getBezeichnung());

        verteilstelle.setAdresse("Industrieweg 100, 49082 Osnabrück");
        assertEquals("Industrieweg 100, 49082 Osnabrück", verteilstelle.getAdresse());

        verteilstelle.setListennummer(55);
        assertEquals(55, verteilstelle.getListennummer());
    }

    @Test
    @Tag("unit")
    void toString_returnsLabel() {
        assertEquals("Hauptstelle", verteilstelle.toString());

        verteilstelle.setBezeichnung("Neuer Name");
        assertEquals("Neuer Name", verteilstelle.toString());
    }

    @Test
    @Tag("unit")
    void getId_returnsCorrectId() {
        assertEquals(1, verteilstelle.getId());
    }

    @Test
    @Tag("unit")
    void getName_returnsCorrectLabel() {
        assertEquals("Hauptstelle", verteilstelle.getName());
    }
}