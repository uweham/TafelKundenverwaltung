package kundenverwaltung.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColumnPreferenceTest {

    @Test
    @Tag("unit")
    void constructorAndGetters_workCorrectly() {
        ColumnPreference pref = new ColumnPreference(
                101,
                "kunden_tabelle",
                "spalte_id",
                150.5,
                3,
                true,
                "ASC"
        );

        assertEquals(101, pref.getUserId());
        assertEquals("kunden_tabelle", pref.getTableName());
        assertEquals("spalte_id", pref.getColumnId());
        assertEquals(150.5, pref.getWidth());
        assertEquals(3, pref.getOrderIndex());
        assertTrue(pref.isVisible());
        assertEquals("ASC", pref.getSortType());
    }
}
