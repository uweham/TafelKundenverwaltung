package kundenverwaltung.model;

import kundenverwaltung.dao.FamilienmitgliedDAO;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FamilienmitgliedTest {

    @Mock
    private Haushalt mockHaushalt;
    @Mock
    private Anrede mockAnrede;
    @Mock
    private Gender mockGender;
    @Mock
    private Nation mockNation;
    @Mock
    private Berechtigung mockBerechtigung;

    private Familienmitglied familienmitglied;
    private final LocalDate geburtsdatum = LocalDate.of(1990, 5, 15);
    private final LocalDateTime zeitstempel = LocalDateTime.of(2023, 1, 1, 12, 0);

    @BeforeEach
    void setUp() {
        familienmitglied = new Familienmitglied(
                101,
                mockHaushalt,
                mockAnrede,
                mockGender,
                "Max",
                "Mustermann",
                geburtsdatum,
                "Keine Bemerkung",
                true,
                true,
                false,
                mockNation,
                mockBerechtigung,
                true,
                true,
                zeitstempel,
                zeitstempel
        );
    }

    @Test
    @Tag("unit")
    void constructor_withId_setsAllFields() {
        assertAll("Prüfen der durch den Konstruktor gesetzten Werte",
                () -> assertEquals(101, familienmitglied.getPersonId()),
                () -> assertEquals(mockHaushalt, familienmitglied.getHaushalt()),
                () -> assertEquals(mockAnrede, familienmitglied.getAnrede()),
                () -> assertEquals(mockGender, familienmitglied.getGender()),
                () -> assertEquals("Max", familienmitglied.getvName()),
                () -> assertEquals("Mustermann", familienmitglied.getnName()),
                () -> assertEquals(geburtsdatum, familienmitglied.getgDatum()),
                () -> assertEquals("Keine Bemerkung", familienmitglied.getBemerkung()),
                () -> assertTrue(familienmitglied.isHaushaltsVorstand()),
                () -> assertTrue(familienmitglied.isEinkaufsBerechtigt()),
                () -> assertFalse(familienmitglied.isGebuehrenBefreiung()),
                () -> assertEquals(mockNation, familienmitglied.getNation()),
                () -> assertEquals(mockBerechtigung, familienmitglied.getBerechtigung()),
                () -> assertTrue(familienmitglied.isAufAusweis()),
                () -> assertTrue(familienmitglied.dseSubmitted()),
                () -> assertEquals(zeitstempel, familienmitglied.getHinzugefuegtAm()),
                () -> assertEquals(zeitstempel, familienmitglied.getGeaendertAm())
        );
    }

    @Test
    @Tag("unit")
    void defaultConstructor_createsEmptyObject() {
        Familienmitglied leeresMitglied = new Familienmitglied();
        assertNotNull(leeresMitglied);
        assertEquals(0, leeresMitglied.getPersonId());
        assertNull(leeresMitglied.getvName());
    }

    @Test
    @Tag("unit")
    void constructor_withoutId_doesNotCallDAO() {
        try (MockedConstruction<FamilienmitgliedDAOimpl> daoMock = mockConstruction(FamilienmitgliedDAOimpl.class)) {
            Familienmitglied neuesMitglied = new Familienmitglied(
                    mockHaushalt,
                    mockAnrede,
                    mockGender,
                    "Anna",
                    "Anders",
                    LocalDate.of(1995, 10, 20),
                    "Test",
                    false,
                    true,
                    false,
                    mockNation,
                    mockBerechtigung,
                    false,
                    true,
                    zeitstempel,
                    zeitstempel
            );

            assertEquals(1, daoMock.constructed().size());
            FamilienmitgliedDAO dao = daoMock.constructed().get(0);
            verify(dao, times(0)).create(neuesMitglied);

            assertEquals("Anna", neuesMitglied.getvName());
            assertFalse(neuesMitglied.isHaushaltsVorstand());
        }
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workAsExpected() {
        Familienmitglied mitglied = new Familienmitglied();

        mitglied.setPersonId(202);
        assertEquals(202, mitglied.getPersonId());

        mitglied.setvName("Erika");
        assertEquals("Erika", mitglied.getvName());

        mitglied.setnName("Musterfrau");
        assertEquals("Musterfrau", mitglied.getnName());

        LocalDate neuesDatum = LocalDate.of(2000, 1, 1);
        mitglied.setgDatum(neuesDatum);
        assertEquals(neuesDatum, mitglied.getgDatum());

        mitglied.setHaushaltsVorstand(false);
        assertFalse(mitglied.isHaushaltsVorstand());

        mitglied.setEinkaufsBerechtigt(false);
        assertFalse(mitglied.isEinkaufsBerechtigt());
    }

    @Test
    @Tag("unit")
    void toString_returnsFullName() {
        assertEquals("Max Mustermann", familienmitglied.toString());

        Familienmitglied ohneNachnamen = new Familienmitglied();
        ohneNachnamen.setvName("Erika");
        assertEquals("Erika null", ohneNachnamen.toString());
    }

    @Test
    @Tag("unit")
    void isAdult_calculatesAgeCorrectly() {
        Familienmitglied erwachsener = new Familienmitglied();
        erwachsener.setgDatum(LocalDate.now().minusYears(20));
        assertTrue(erwachsener.isAdult());

        Familienmitglied kind = new Familienmitglied();
        kind.setgDatum(LocalDate.now().minusYears(10));
        assertFalse(kind.isAdult());

        Familienmitglied gerade18 = new Familienmitglied();
        gerade18.setgDatum(LocalDate.now().minusYears(18));
        assertTrue(gerade18.isAdult());
    }

    @Test
    @Tag("unit")
    void tableViewGetters_returnCorrectValues() {
        when(mockHaushalt.getKundennummer()).thenReturn(12345);
        when(mockHaushalt.getStrasse()).thenReturn("Testweg");
        when(mockHaushalt.getHausnummer()).thenReturn("1a");
        assertEquals(12345, familienmitglied.getKundennummer());
        assertEquals("Testweg 1a", familienmitglied.getAdresse());
        assertEquals("Max Mustermann", familienmitglied.getName());
    }
}
