package kundenverwaltung.model.statistiktool;

import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArchivierteKundenTest {

    private ArchivierteKunden kunde;

    @BeforeEach
    void setUp() {
        kunde = new ArchivierteKunden(
                "K-123",
                "Max Mustermann",
                "Musterstraße 1",
                "12345",
                "Musterstadt"
        );
    }

    @Test
    @Tag("unit")
    void konstruktor_setztWerteKorrekt() {
        assertAll("Prüfen der durch den Konstruktor gesetzten Werte",
                () -> assertEquals("K-123", kunde.getKdNr()),
                () -> assertEquals("Max Mustermann", kunde.getName()),
                () -> assertEquals("Musterstraße 1", kunde.getStrasse()),
                () -> assertEquals("12345", kunde.getPlz()),
                () -> assertEquals("Musterstadt", kunde.getOrt()),
                () -> assertEquals(0, kunde.getStatus(), "Status sollte standardmäßig 0 sein, da er nicht im Konstruktor gesetzt wird.")
        );
    }

    @Test
    @Tag("unit")
    void propertyGetter_gebenKorrektePropertiesZurueck() {
        assertAll("Prüfen der Property-Getter für die TableView-Anbindung",
                () -> {
                    StringProperty p = kunde.kdNrProperty();
                    assertNotNull(p, "kdNrProperty sollte nicht null sein.");
                    assertEquals("K-123", p.get());
                },
                () -> {
                    StringProperty p = kunde.nameProperty();
                    assertNotNull(p, "nameProperty sollte nicht null sein.");
                    assertEquals("Max Mustermann", p.get());
                },
                () -> {
                    StringProperty p = kunde.strasseProperty();
                    assertNotNull(p, "strasseProperty sollte nicht null sein.");
                    assertEquals("Musterstraße 1", p.get());
                },
                () -> {
                    StringProperty p = kunde.plzProperty();
                    assertNotNull(p, "plzProperty sollte nicht null sein.");
                    assertEquals("12345", p.get());
                },
                () -> {
                    StringProperty p = kunde.ortProperty();
                    assertNotNull(p, "ortProperty sollte nicht null sein.");
                    assertEquals("Musterstadt", p.get());
                }
        );
    }

    @Test
    @Tag("unit")
    void setter_aktualisierenWerteInPropertiesKorrekt() {
        kunde.setKdNr("K-999");
        assertEquals("K-999", kunde.getKdNr(), "Der Wert aus getKdNr() sollte aktualisiert sein.");
        assertEquals("K-999", kunde.kdNrProperty().get(), "Der Wert in der Property sollte ebenfalls aktualisiert sein.");

        kunde.setName("Erika Musterfrau");
        assertEquals("Erika Musterfrau", kunde.getName());

        kunde.setStrasse("Nebenweg 2");
        assertEquals("Nebenweg 2", kunde.getStrasse());

        kunde.setPlz("54321");
        assertEquals("54321", kunde.getPlz());

        kunde.setOrt("Andere-Stadt");
        assertEquals("Andere-Stadt", kunde.getOrt());
    }

    @Test
    @Tag("unit")
    void status_getterUndSetter_funktionierenWieErwartet() {
        assertEquals(0, kunde.getStatus(), "Der Standardwert für Status sollte 0 sein.");

        kunde.setStatus(1);
        assertEquals(1, kunde.getStatus());

        kunde.setStatus(-1);
        assertEquals(-1, kunde.getStatus());
    }

    @Test
    @Tag("unit")
    void getHausnummer_wirftStackOverflowErrorAufgrundVonEndlosrekursion() {
        assertThrows(StackOverflowError.class, () -> {
            kunde.getHausnummer();
        }, "Der Aufruf von getHausnummer() sollte einen StackOverflowError auslösen.");
    }
}