package kundenverwaltung.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;

import kundenverwaltung.dao.BenutzerDAO;
import kundenverwaltung.dao.BenutzerDAOimpl;
import kundenverwaltung.dao.BenutzerRechteDAO;
import kundenverwaltung.dao.BenutzerRechteDAOimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

class BenutzerTest {

    private ArrayList<Recht> rechteListe;
    private Recht dummyRecht;

    @BeforeEach
    void setUp() {
        rechteListe = new ArrayList<>();
        dummyRecht = mock(Recht.class);
        rechteListe.add(dummyRecht);
    }

    @Test
    @Tag("unit")
    void constructor_withId_setsAllFields() {
        byte[] pw = "pass".getBytes(StandardCharsets.UTF_8);
        Benutzer b = new Benutzer(1, "user", pw, "Anzeige", true, rechteListe);

        assertEquals(1, b.getBenutzerId());
        assertEquals("user", b.getName());
        assertEquals(pw, b.getPasswort());
        assertEquals("Anzeige", b.getAnzeigename());
        assertTrue(b.isLokal());
        assertEquals(rechteListe, b.getRechte());
    }

    @Test
    @Tag("unit")
    void constructor_withoutId_callsDAOs_and_hashesPassword() throws Exception {
        try (
                MockedConstruction<BenutzerDAOimpl> daoMock = mockConstruction(BenutzerDAOimpl.class);
                MockedConstruction<BenutzerRechteDAOimpl> rechteDaoMock = mockConstruction(BenutzerRechteDAOimpl.class)
        ) {
            Benutzer b = new Benutzer("user", "secret", "Anzeigename", false, rechteListe);

            BenutzerDAO dao = daoMock.constructed().get(0);
            verify(dao, times(1)).create(b);

            BenutzerRechteDAO rdao = rechteDaoMock.constructed().get(0);
            verify(rdao, times(1)).rechtHinzufuegen(b, dummyRecht);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] expectedHash = digest.digest("secret".getBytes(StandardCharsets.UTF_8));
            assertArrayEquals(expectedHash, b.getPasswort());

            assertEquals("user", b.getName());
            assertEquals("Anzeigename", b.getAnzeigename());
            assertFalse(b.isLokal());
            assertEquals(rechteListe, b.getRechte());
        }
    }

    @Test
    @Tag("unit")
    void addRight_addsRightAndCallsDAO() {
        try (MockedConstruction<BenutzerRechteDAOimpl> rechteDaoMock = mockConstruction(BenutzerRechteDAOimpl.class)) {
            ArrayList<Recht> leerListe = new ArrayList<>();
            Benutzer b = new Benutzer(1, "n", "pw".getBytes(), "a", true, leerListe);

            Recht neuesRecht = mock(Recht.class);
            b.addRecht(neuesRecht);

            assertTrue(leerListe.contains(neuesRecht));

            BenutzerRechteDAO rdao = rechteDaoMock.constructed().get(0);
            verify(rdao, times(1)).rechtHinzufuegen(b, neuesRecht);
        }
    }

    @Test
    @Tag("unit")
    void removeRight_removesRightAndCallsDAO() {
        try (MockedConstruction<BenutzerRechteDAOimpl> rechteDaoMock = mockConstruction(BenutzerRechteDAOimpl.class)) {
            ArrayList<Recht> liste = new ArrayList<>();
            liste.add(dummyRecht);
            Benutzer b = new Benutzer(1, "n", "pw".getBytes(), "a", true, liste);

            b.entferneRecht(dummyRecht);

            assertFalse(liste.contains(dummyRecht));

            BenutzerRechteDAO rdao = rechteDaoMock.constructed().get(0);
            verify(rdao, times(1)).rechtEntfernen(b, dummyRecht);
        }
    }

    @Test
    @Tag("unit")
    void setPassword_hashesNewPassword() throws Exception {
        Benutzer b = new Benutzer(1, "n", "alt".getBytes(), "a", true, new ArrayList<>());
        b.setPasswort("neu");

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] expectedHash = digest.digest("neu".getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(expectedHash, b.getPasswort());
    }

    @Test
    @Tag("unit")
    void toString_returnsDisplayName() {
        Benutzer b = new Benutzer(1, "n", "pw".getBytes(), "DerName", true, new ArrayList<>());
        assertEquals("DerName", b.toString());
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workCorrectly() {
        Benutzer b = new Benutzer(1, "n", "pw".getBytes(), "a", true, new ArrayList<>());

        b.setName("x");
        b.setAnzeigename("y");
        b.setLokal(false);
        b.setBenutzerId(99);

        assertEquals("x", b.getName());
        assertEquals("y", b.getAnzeigename());
        assertFalse(b.isLokal());
        assertEquals(99, b.getBenutzerId());
    }
}
