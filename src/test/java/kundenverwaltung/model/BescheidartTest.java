package kundenverwaltung.model;

import kundenverwaltung.dao.BescheidartDAOimpl;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BescheidartTest {

    @Test
    @Tag("unit")
    void constructorWithNameAndActive_callsDaoCreate() {
        try (MockedConstruction<BescheidartDAOimpl> mocked =
                     mockConstruction(BescheidartDAOimpl.class, (mockDao, ctx) -> {
                     })) {

            Bescheidart b = new Bescheidart("Testname", true);

            assertEquals("Testname", b.getName());
            assertTrue(b.isAktiv());
            assertNotNull(b.getBescheidartDAO());

            BescheidartDAOimpl daoMock = mocked.constructed().get(0);
            verify(daoMock).create(b);
        }
    }

    @Test
    @Tag("unit")
    void constructorWithIdNameActive_setsAllFields() {
        Bescheidart b = new Bescheidart(42, "Art42", false);

        assertEquals(42, b.getBescheidartId());
        assertEquals("Art42", b.getName());
        assertFalse(b.isAktiv());
    }

    @Test
    @Tag("unit")
    void toString_returnsName() {
        Bescheidart b = new Bescheidart(1, "MeinBescheid", true);
        assertEquals("MeinBescheid", b.toString());
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workCorrectly() {
        Bescheidart b = new Bescheidart(1, "Name1", true);

        b.setBescheidartId(99);
        b.setName("Neu");
        b.setAktiv(false);

        assertEquals(99, b.getBescheidartId());
        assertEquals("Neu", b.getName());
        assertFalse(b.isAktiv());
    }
}
