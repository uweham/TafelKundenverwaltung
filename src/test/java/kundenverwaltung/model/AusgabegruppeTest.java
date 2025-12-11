package kundenverwaltung.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import kundenverwaltung.dao.AusgabegruppeAusgabeTagZeitDAOimpl;
import kundenverwaltung.dao.AusgabegruppeDAO;
import kundenverwaltung.dao.AusgabegruppeAusgabeTagZeitDAO;
import kundenverwaltung.dao.AusgabegruppeDAOimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

class AusgabegruppeTest {

    private ArrayList<AusgabeTagZeit> dummyZeiten;

    @BeforeEach
    void setUp() {
        dummyZeiten = new ArrayList<>();
        dummyZeiten.add(mock(AusgabeTagZeit.class));
    }

    @Test
    @Tag("unit")
    void constructor_withId_setsAllFieldsCorrectly() {
        Ausgabegruppe g = new Ausgabegruppe(1, "Test", true, Color.RED, dummyZeiten);

        assertEquals(1, g.getAusgabegruppeId());
        assertEquals("Test", g.getName());
        assertTrue(g.isAktiv());
        assertEquals(Color.RED, g.getAnzeigeFarbe());
        assertEquals(dummyZeiten, g.getAusgabeTagZeiten());
    }

    @Test
    @Tag("unit")
    void constructor_withoutId_callsDAOAndTimeDAO() {
        try (
                MockedConstruction<AusgabegruppeDAOimpl> daoMock = mockConstruction(AusgabegruppeDAOimpl.class);
                MockedConstruction<AusgabegruppeAusgabeTagZeitDAOimpl> zeitDaoMock = mockConstruction(AusgabegruppeAusgabeTagZeitDAOimpl.class)
        ) {
            Ausgabegruppe g = new Ausgabegruppe("Test", true, Color.BLUE, dummyZeiten);

            AusgabegruppeDAO dao = daoMock.constructed().get(0);
            verify(dao, times(1)).create(g);

            AusgabegruppeAusgabeTagZeitDAO zeitDAO = zeitDaoMock.constructed().get(0);
            for (AusgabeTagZeit zeit : dummyZeiten) {
                verify(zeitDAO, times(1)).ausgabeTagZeitHinzufuegen(g, zeit);
            }

            assertEquals("Test", g.getName());
            assertTrue(g.isAktiv());
            assertEquals(Color.BLUE, g.getAnzeigeFarbe());
            assertEquals(dummyZeiten, g.getAusgabeTagZeiten());
        }
    }

    @Test
    @Tag("unit")
    void constructor_withNameOnly_setsDefaultValuesAndCallsDAO() {
        try (MockedConstruction<AusgabegruppeDAOimpl> daoMock = mockConstruction(AusgabegruppeDAOimpl.class)) {
            Ausgabegruppe g = new Ausgabegruppe("NurName");

            AusgabegruppeDAO dao = daoMock.constructed().get(0);
            verify(dao, times(1)).create(g);

            assertEquals("NurName", g.getName());
            assertFalse(g.isAktiv());
            assertEquals(Color.WHITE, g.getAnzeigeFarbe());
            assertTrue(g.getAusgabeTagZeiten().isEmpty());
        }
    }

    @Test
    @Tag("unit")
    void toString_returnsName() {
        Ausgabegruppe g = new Ausgabegruppe(99, "Anzeigename", false, Color.GREEN, new ArrayList<>());
        assertEquals("Anzeigename", g.toString());
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workCorrectly() {
        Ausgabegruppe g = new Ausgabegruppe(1, "Test", true, Color.RED, dummyZeiten);

        g.setAusgabegruppeId(42);
        g.setName("Neu");
        g.setAktiv(false);
        g.setAnzeigeFarbe(Color.BLACK);
        ArrayList<AusgabeTagZeit> neueListe = new ArrayList<>();
        g.setAusgabeTagZeiten(neueListe);

        assertEquals(42, g.getAusgabegruppeId());
        assertEquals("Neu", g.getName());
        assertFalse(g.isAktiv());
        assertEquals(Color.BLACK, g.getAnzeigeFarbe());
        assertEquals(neueListe, g.getAusgabeTagZeiten());
    }
}
