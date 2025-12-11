package kundenverwaltung.model;

import kundenverwaltung.dao.BerechtigungDAO;
import kundenverwaltung.dao.BerechtigungDAOimpl;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BerechtigungTest {

    @Test
    @Tag("unit")
    void constructor_withId_setsFields() {
        Berechtigung b = new Berechtigung(5, "Lesen");

        assertEquals(5, b.getBerechtigungId());
        assertEquals("Lesen", b.getName());
        assertEquals("Lesen", b.toString());
    }

    @Test
    @Tag("unit")
    void constructor_withoutId_callsDAO() {
        try (MockedConstruction<BerechtigungDAOimpl> daoMock = mockConstruction(BerechtigungDAOimpl.class)) {
            Berechtigung b = new Berechtigung("Schreiben");

            BerechtigungDAO dao = daoMock.constructed().get(0);
            verify(dao, times(1)).create(b);

            assertEquals("Schreiben", b.getName());
        }
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workCorrectly() {
        Berechtigung b = new Berechtigung(1, "Start");

        b.setBerechtigungId(42);
        b.setName("Neu");

        assertEquals(42, b.getBerechtigungId());
        assertEquals("Neu", b.getName());
    }

    @Test
    @Tag("unit")
    void toString_returnsName() {
        Berechtigung b = new Berechtigung(10, "Admin");
        assertEquals("Admin", b.toString());
    }
}
