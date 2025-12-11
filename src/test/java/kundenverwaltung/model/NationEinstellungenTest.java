package kundenverwaltung.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NationEinstellungenTest {

    private NationEinstellungen nationEinstellungen;

    @Mock
    private Nation mockNation1;

    @Mock
    private Nation mockNation2;

    @BeforeEach
    void setUp() {
        nationEinstellungen = new NationEinstellungen();
    }

    @Test
    @Tag("unit")
    void testSetAndGetNationList () {
        ArrayList<Nation> nationen = new ArrayList<>();
        nationen.add(mockNation1);
        nationen.add(mockNation2);

        nationEinstellungen.setNationList(nationen);

        assertEquals(nationen, nationEinstellungen.getNationList());
        assertEquals(2, nationEinstellungen.getNationList().size());
    }

    @Test
    @Tag("unit")
    void testSetAndGetNationality() {
        String testNationalitaet = "Deutsch";
        nationEinstellungen.setNationalitaet(testNationalitaet);

        assertEquals(testNationalitaet, nationEinstellungen.getNationalitaet());
    }

    @Test
    @Tag("unit")
    void testDuplicateAndTypoMethods() {
        ArrayList<Nation> nationen = new ArrayList<>();
        nationen.add(mockNation1);

        nationEinstellungen.setNationenList(nationen);
        assertEquals(nationen, nationEinstellungen.geNationenList());
        assertEquals(1, nationEinstellungen.geNationenList().size());
    }

    @Test
    @Tag("unit")
    void testInitialState() {
        assertNull(nationEinstellungen.getNationList());
        assertNull(nationEinstellungen.getNationalitaet());
    }
}