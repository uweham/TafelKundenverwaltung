package kundenverwaltung.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderByTest {

    private OrderBy orderBy;

    @BeforeEach
    void setUp() {
        orderBy = new OrderBy("Kundenname", "nName");
    }

    @Test
    @Tag("unit")
    void constructor_setsAllFieldsCorrectly() {
        assertAll("Prüfen der durch den Konstruktor gesetzten Werte",
                () -> assertEquals("Kundenname", orderBy.getTerm()),
                () -> assertEquals("nName", orderBy.getDbColumn())
        );
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workAsExpected() {
        orderBy.setTerm("Kundennummer");
        assertEquals("Kundennummer", orderBy.getTerm());

        orderBy.setDbColumn("kundennummer");
        assertEquals("kundennummer", orderBy.getDbColumn());
    }

    @Test
    @Tag("unit")
    void testWithNullValues() {
        OrderBy orderByNull = new OrderBy(null, null);
        assertNull(orderByNull.getTerm());
        assertNull(orderByNull.getDbColumn());

        orderBy.setTerm(null);
        assertNull(orderBy.getTerm());

        orderBy.setDbColumn(null);
        assertNull(orderBy.getDbColumn());
    }
}