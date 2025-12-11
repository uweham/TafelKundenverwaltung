package kundenverwaltung.model;

import javafx.beans.property.IntegerProperty;

import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BescheidTest {

    private Familienmitglied mockFamilienmitglied;
    private Bescheidart mockBescheidart;
    private ChangeDateFormat mockDateFormat;

    @BeforeEach
    void setUp() {
        mockFamilienmitglied = mock(Familienmitglied.class);
        mockBescheidart = mock(Bescheidart.class);
        mockDateFormat = mock(ChangeDateFormat.class);
    }

    @Test
    @Tag("unit")
    void constructor_withIdAndCount_setsAllFields() {
        Bescheid b = new Bescheid(1, mockFamilienmitglied, 3, mockBescheidart,
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));

        assertEquals(1, b.getBescheidId());
        assertEquals(mockFamilienmitglied, b.getPerson());
        assertEquals(3, b.getAnzahlPersonen());
        assertEquals(mockBescheidart, b.getBescheidart());
        assertEquals(LocalDate.of(2025, 1, 1), b.getGueltigAb());
        assertEquals(LocalDate.of(2025, 12, 31), b.getGueltigBis());
    }

    @Test
    @Tag("unit")
    void constructor_withoutId_setsAllFields() {
        Bescheid b = new Bescheid(mockFamilienmitglied, mockBescheidart,
                LocalDate.of(2025, 2, 1), LocalDate.of(2025, 8, 1), 5);

        assertEquals(mockFamilienmitglied, b.getPerson());
        assertEquals(5, b.getAnzahlPersonen());
        assertEquals(mockBescheidart, b.getBescheidart());
        assertEquals(LocalDate.of(2025, 2, 1), b.getGueltigAb());
        assertEquals(LocalDate.of(2025, 8, 1), b.getGueltigBis());
    }

    @Test
    @Tag("unit")
    void isValid_returnsTrue_whenTodayWithinRange() {
        LocalDate heute = LocalDate.now();
        Bescheid b = new Bescheid(1, mockFamilienmitglied, 1, mockBescheidart,
                heute.minusDays(5), heute.plusDays(5));

        assertTrue(b.isGueltig());
    }

    @Test
    @Tag("unit")
    void isValid_returnsFalse_whenTodayAfterEndDate() {
        LocalDate heute = LocalDate.now();
        Bescheid b = new Bescheid(1, mockFamilienmitglied, 1, mockBescheidart,
                heute.minusDays(10), heute.minusDays(1));

        assertFalse(b.isGueltig());
    }

    @Test
    @Tag("unit")
    void numberOfPersons_setterAndGetter_workCorrectly() {
        Bescheid b = new Bescheid(1, mockFamilienmitglied, 0, mockBescheidart,
                LocalDate.now(), LocalDate.now());

        b.setAnzahlPersonen(7);
        assertEquals(7, b.getAnzahlPersonen());

        IntegerProperty prop = b.anzahlPersonenProperty();
        prop.set(9);
        assertEquals(9, b.getAnzahlPersonen());
    }

    @Test
    @Tag("unit")
    void delegationMethods_callDependentObjects() {
        when(mockFamilienmitglied.getName()).thenReturn("Max Mustermann");
        when(mockBescheidart.getName()).thenReturn("Testart");

        Bescheid b = new Bescheid(1, mockFamilienmitglied, 1, mockBescheidart,
                LocalDate.now(), LocalDate.now());

        assertEquals("Max Mustermann", b.getName());
        assertEquals("Testart", b.getBescheidName());
    }

    @Test
    @Tag("unit")
    void getValidFrom_and_getDateOfExpiry_useChangeDateFormat() {
        try (MockedConstruction<ChangeDateFormat> mockedFormat =
                     mockConstruction(ChangeDateFormat.class, (mock, ctx) -> {
                         when(mock.changeDateToDefaultString(any(LocalDate.class)))
                                 .thenReturn("formatted");
                     })) {

            Bescheid b = new Bescheid(1, mockFamilienmitglied, 1, mockBescheidart,
                    LocalDate.of(2025, 3, 1), LocalDate.of(2025, 4, 1));

            assertEquals("formatted", b.getValidFrom());
            assertEquals("formatted", b.getDateOfExpiry());
        }
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workCorrectly() {
        Bescheid b = new Bescheid(1, mockFamilienmitglied, 1, mockBescheidart,
                LocalDate.now(), LocalDate.now());

        b.setBescheidId(99);
        b.setPerson(null);
        b.setBescheidart(null);
        b.setGueltigAb(LocalDate.of(2020, 1, 1));
        b.setGueltigBis(LocalDate.of(2020, 12, 31));

        assertEquals(99, b.getBescheidId());
        assertNull(b.getPerson());
        assertNull(b.getBescheidart());
        assertEquals(LocalDate.of(2020, 1, 1), b.getGueltigAb());
        assertEquals(LocalDate.of(2020, 12, 31), b.getGueltigBis());
    }
}
