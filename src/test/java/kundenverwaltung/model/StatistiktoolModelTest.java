package kundenverwaltung.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatistiktoolModelTest {

    private StatistiktoolModel statistiktoolModel;
    private final int testGesamtsumme = 100;
    private final int[] testGruppen = {10, 20, 70};

    @BeforeEach
    void setUp() {
        statistiktoolModel = new StatistiktoolModel(testGesamtsumme, testGruppen);
    }

    @Test
    @Tag("unit")
    void constructor_setsValuesCorrectly() {
        assertAll("Prüfen der durch den Konstruktor gesetzten Werte",
                () -> assertEquals(testGesamtsumme, statistiktoolModel.getGesamtsumme()),
                () -> assertArrayEquals(testGruppen, statistiktoolModel.getGruppen()),
                () -> assertNull(statistiktoolModel.getAltersgruppen())
        );
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workAsExpected() {
        statistiktoolModel.setGesamtsumme(250);
        assertEquals(250, statistiktoolModel.getGesamtsumme());

        int[] neueGruppen = {50, 80, 120};
        statistiktoolModel.setGruppen(neueGruppen);
        assertArrayEquals(neueGruppen, statistiktoolModel.getGruppen());

        int[] altersgruppen = {5, 15, 25};
        statistiktoolModel.setAltersgruppen(altersgruppen);
        assertArrayEquals(altersgruppen, statistiktoolModel.getAltersgruppen());
    }
}