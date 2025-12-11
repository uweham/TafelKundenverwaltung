package kundenverwaltung.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PLZTest {

    private PLZ plz;

    @BeforeEach
    void setUp() {
        plz = new PLZ(1, "49074", "Osnabrück");
    }

    @Test
    @Tag("unit")
    void constructor_setsAllFieldsCorrectly() {
        assertAll("Prüfen der durch den Konstruktor gesetzten Werte",
                () -> assertEquals(1, plz.getPlzId()),
                () -> assertEquals("49074", plz.getPlz()),
                () -> assertEquals("Osnabrück", plz.getOrt())
        );
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workAsExpected() {
        PLZ testPlz = new PLZ(0, "", "");

        testPlz.setPlzId(99);
        assertEquals(99, testPlz.getPlzId());

        testPlz.setPlz("10115");
        assertEquals("10115", testPlz.getPlz());

        testPlz.setOrt("Berlin");
        assertEquals("Berlin", testPlz.getOrt());
    }

    @Test
    @Tag("unit")
    void toString_returnsCorrectString() {
        String expected = "Osnabrück";
        assertEquals(expected, plz.toString());

        PLZ anderePlz = new PLZ(2, "20095", "Hamburg");
        String andererExpected = "Hamburg";
        assertEquals(andererExpected, anderePlz.toString());
    }
}