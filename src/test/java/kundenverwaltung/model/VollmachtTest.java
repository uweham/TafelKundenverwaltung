package kundenverwaltung.model;

import kundenverwaltung.dao.VollmachtDAO;
import kundenverwaltung.dao.VollmachtDAOimpl;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VollmachtTest {

    @Mock
    private Haushalt mockHaushalt;
    @Mock
    private Familienmitglied mockPerson;

    private Vollmacht vollmacht;
    private final LocalDate ausgestelltAmDatum = LocalDate.of(2023, 1, 1);
    private final LocalDate ablaufDatum = LocalDate.of(2025, 12, 31);

    @BeforeEach
    void setUp() {

        vollmacht = new Vollmacht(
                1,
                mockHaushalt,
                mockPerson,
                ausgestelltAmDatum,
                ablaufDatum
        );
    }

    @Test
    @Tag("unit")
    void constructor_withId_setsAllFieldsCorrectly() {
        assertAll("Prüfen der durch den Konstruktor gesetzten Werte",
                () -> assertEquals(1, vollmacht.getVollmachtId()),
                () -> assertEquals(mockHaushalt, vollmacht.getHaushalt()),
                () -> assertEquals(mockPerson, vollmacht.getBevollmaechtigtePerson()),
                () -> assertEquals(ausgestelltAmDatum, vollmacht.getAusgestelltAm()),
                () -> assertEquals(ablaufDatum, vollmacht.getAblaufDatum())
        );
    }

    @Test
    @Tag("unit")
    void constructor_withoutId_doesNotCallDAO() {

        try (MockedConstruction<VollmachtDAOimpl> daoMock = mockConstruction(VollmachtDAOimpl.class)) {
            Vollmacht neueVollmacht = new Vollmacht(
                    mockHaushalt,
                    mockPerson,
                    ausgestelltAmDatum,
                    ablaufDatum
            );

            if (!daoMock.constructed().isEmpty()) {
                VollmachtDAO mockDao = daoMock.constructed().get(0);
                verify(mockDao, times(0)).create(neueVollmacht);
            }

            assertEquals(mockPerson, neueVollmacht.getBevollmaechtigtePerson());
            assertEquals(mockHaushalt, neueVollmacht.getHaushalt());
        }
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workAsExpected() {
        vollmacht.setVollmachtId(99);
        assertEquals(99, vollmacht.getVollmachtId());

        Haushalt neuerHaushalt = mock(Haushalt.class);
        vollmacht.setHaushalt(neuerHaushalt);
        assertEquals(neuerHaushalt, vollmacht.getHaushalt());

        Familienmitglied neuePerson = mock(Familienmitglied.class);
        vollmacht.setBevollmaechtigtePerson(neuePerson);
        assertEquals(neuePerson, vollmacht.getBevollmaechtigtePerson());

        LocalDate neuesAusgestellt = LocalDate.of(2024, 6, 15);
        vollmacht.setAusgestelltAm(neuesAusgestellt);
        assertEquals(neuesAusgestellt, vollmacht.getAusgestelltAm());

        LocalDate neuesAblauf = LocalDate.of(2030, 1, 1);
        vollmacht.setAblaufDatum(neuesAblauf);
        assertEquals(neuesAblauf, vollmacht.getAblaufDatum());
    }

    @Test
    @Tag("unit")
    void getRecipient_returnsCorrectName() {
        when(mockPerson.getnName()).thenReturn("Musterfrau");
        when(mockPerson.getvName()).thenReturn("Erika");
        assertEquals("Musterfrau, Erika", vollmacht.getEmpfaenger());
    }

    @Test
    @Tag("unit")
    void getRecipientNumber_returnsCorrectId() {
        when(mockPerson.getPersonId()).thenReturn(123);
        assertEquals(123, vollmacht.getEmpfaengerNr());
    }

    @Test
    @Tag("unit")
    void getDateOf_returnsFormattedString() {
        try (MockedConstruction<ChangeDateFormat> cdfMocked = mockConstruction(ChangeDateFormat.class,
                (mock, context) -> {
                    when(mock.changeDateToDefaultString(ausgestelltAmDatum)).thenReturn("01.01.2023");
                    when(mock.changeDateToDefaultString(ablaufDatum)).thenReturn("31.12.2025");

                    when(mock.changeDateToDefaultString((LocalDate) null)).thenReturn("");
                })) {

            Vollmacht testVollmacht = new Vollmacht(
                    1,
                    mockHaushalt,
                    mockPerson,
                    ausgestelltAmDatum,
                    ablaufDatum
            );

            assertEquals("01.01.2023", testVollmacht.getDateOfIssue(), "Sollte das 'ausgestelltAm'-Datum korrekt formatieren.");
            assertEquals("31.12.2025", testVollmacht.getDateOfExpiry(), "Sollte das 'ablaufDatum' korrekt formatieren.");

            testVollmacht.setAusgestelltAm(null);
            testVollmacht.setAblaufDatum(null);

            assertEquals("", testVollmacht.getDateOfIssue(), "Sollte einen leeren String für ein null 'ausgestelltAm'-Datum zurückgeben.");
            assertEquals("", testVollmacht.getDateOfExpiry(), "Sollte einen leeren String für ein null 'ablaufDatum' zurückgeben.");
        }
    }
}