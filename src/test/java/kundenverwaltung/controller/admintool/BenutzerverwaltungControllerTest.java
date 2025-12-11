package kundenverwaltung.controller.admintool;

import static org.junit.Assert.*;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import kundenverwaltung.model.Benutzer;
import kundenverwaltung.model.Recht;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Tag;

public class BenutzerverwaltungControllerTest {

    private BenutzerverwaltungController controller;
    private Benutzer testBenutzer;
    private Recht testRecht1;
    private Recht testRecht2;

    @Before
    public void setUp() {
        controller = new BenutzerverwaltungController();

        testRecht1 = new Recht(1, "Test Recht 1", "Beschreibung 1");
        testRecht2 = new Recht(2, "Test Recht 2", "Beschreibung 2");

        ArrayList<Recht> rechte = new ArrayList<>();
        rechte.add(testRecht1);

        testBenutzer = new Benutzer(1, "testuser", "password".getBytes(), "Test User", true, rechte);

        ArrayList<Benutzer> benutzerListe = new ArrayList<>();
        benutzerListe.add(testBenutzer);

        BenutzerverwaltungController.setBenutzerliste(benutzerListe);
    }

    @Test
    @Tag("unit")
    public void testGetBenutzerliste() {
        ArrayList<Benutzer> liste = BenutzerverwaltungController.getBenutzerliste();
        assertNotNull(liste);
        assertFalse(liste.isEmpty());
        assertEquals("Test User", liste.get(0).getAnzeigename());
    }

    @Test
    @Tag("unit")
    public void testSetBenutzerliste() {
        ArrayList<Benutzer> neueListe = new ArrayList<>();
        neueListe.add(new Benutzer(2, "newuser", "pass".getBytes(), "New User", false, new ArrayList<>()));

        BenutzerverwaltungController.setBenutzerliste(neueListe);

        ArrayList<Benutzer> aktuelleListe = BenutzerverwaltungController.getBenutzerliste();
        assertEquals(1, aktuelleListe.size());
        assertEquals("New User", aktuelleListe.get(0).getAnzeigename());
    }

    @Test
    @Tag("unit")
    public void testDatenLaden() {
        ObservableList<Benutzer> obsListe = getObsbenutzerliste();
        assertNotNull(obsListe);
        assertFalse(obsListe.isEmpty());
    }

    @Test
    @Tag("unit")
    public void testRechteLaden() {
        ArrayList<Recht> rechte = testBenutzer.getRechte();
        assertNotNull(rechte);
        assertFalse(rechte.isEmpty());
        assertEquals(1, rechte.get(0).getRechtId());
    }

    @SuppressWarnings("unchecked")
    public ObservableList<Benutzer> getObsbenutzerliste() {
        try {
            java.lang.reflect.Field field = BenutzerverwaltungController.class.getDeclaredField("obsbenutzerliste");
            field.setAccessible(true);
            return (ObservableList<Benutzer>) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}