package kundenverwaltung.model;

import kundenverwaltung.dao.BescheidDAOimpl;
import kundenverwaltung.dao.EinkaufDAOimpl;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.service.Booking_err_warn_list;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HaushaltTest {

    @Mock
    private PLZ mockPlz;
    @Mock
    private Verteilstelle mockVerteilstelle;
    @Mock
    private Ausgabegruppe mockAusgabegruppe;
    @Mock
    private Warentyp mockWarentyp;

    private Haushalt haushalt;

    @BeforeEach
    void setUp() {
        haushalt = new Haushalt(
                4711,
                "Teststraße",
                "1a",
                mockPlz,
                "012345",
                "67890",
                "Keine Bemerkungen",
                LocalDate.of(2020, 1, 1),
                10.0f,
                mockVerteilstelle,
                false,
                false,
                mockAusgabegruppe,
                false,
                true
        );
    }

    @Test
    @Tag("unit")
    void constructor_setsAllFieldsCorrectly() {
        assertAll("Prüfen der durch den Konstruktor gesetzten Werte",
                () -> assertEquals(4711, haushalt.getKundennummer()),
                () -> assertEquals("Teststraße", haushalt.getStrasse()),
                () -> assertEquals(mockPlz, haushalt.getPlz()),
                () -> assertEquals(10.0f, haushalt.getSaldo()),
                () -> assertFalse(haushalt.getIstGesperrt()),
                () -> assertTrue(haushalt.isDatenschutzerklaerung())
        );
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workAsExpected() {
        haushalt.setStrasse("Musterweg");
        assertEquals("Musterweg", haushalt.getStrasse());

        haushalt.setIstGesperrt(true);
        assertTrue(haushalt.getIstGesperrt());

        haushalt.setSaldo(50.5f);
        assertEquals(50.5f, haushalt.getSaldo());
    }

    @Test
    @Tag("unit")
    void getAmountToPay_calculatesSumForAdultsAndChildrenCorrectly() {
        Familienmitglied erwachsener = mock(Familienmitglied.class);
        when(erwachsener.getgDatum()).thenReturn(LocalDate.now().minusYears(25));

        Familienmitglied kind = mock(Familienmitglied.class);
        when(kind.getgDatum()).thenReturn(LocalDate.now().minusYears(10));

        ArrayList<Familienmitglied> mitglieder = new ArrayList<>(List.of(erwachsener, kind));

        when(mockWarentyp.getPreisErwachsene()).thenReturn(5.0f);
        when(mockWarentyp.getPreisKinder()).thenReturn(2.0f);

        try (MockedConstruction<FamilienmitgliedDAOimpl> daoMock = mockConstruction(FamilienmitgliedDAOimpl.class,
                (mock, context) -> when(mock.getAllFamilienmitglieder(haushalt.getKundennummer())).thenReturn(mitglieder))) {

            float zuZahlen = haushalt.getZuZahlen(mockWarentyp);

            assertEquals(7.0f, zuZahlen);
        }
    }

    @Test
    @Tag("unit")
    void getNumberOfChildrenAndAdults_countsCorrectly() {
        Familienmitglied erwachsener1 = mock(Familienmitglied.class);
        when(erwachsener1.getgDatum()).thenReturn(LocalDate.now().minusYears(30));
        Familienmitglied erwachsener2 = mock(Familienmitglied.class);
        when(erwachsener2.getgDatum()).thenReturn(LocalDate.now().minusYears(18));
        Familienmitglied kind = mock(Familienmitglied.class);
        when(kind.getgDatum()).thenReturn(LocalDate.now().minusYears(12));

        ArrayList<Familienmitglied> mitglieder = new ArrayList<>(List.of(erwachsener1, erwachsener2, kind));

        try (MockedConstruction<FamilienmitgliedDAOimpl> daoMock = mockConstruction(FamilienmitgliedDAOimpl.class,
                (mock, context) -> when(mock.getAllFamilienmitglieder(haushalt.getKundennummer())).thenReturn(mitglieder))) {

            assertEquals(1, haushalt.getanzahlErwachsene());
            assertEquals(1, haushalt.getanzahlKinder());
        }
    }
/*
    @Test
    @Tag("unit")
    void getBookingWarnings_returnsWarningForNegativeBalance() {
        haushalt.setSaldo(-5.0f);

        try (
                MockedConstruction<EinkaufDAOimpl> einkaufDaoMock = mockConstruction(EinkaufDAOimpl.class, (mock, context) -> when(mock.getLetzerEinkauf(any())).thenReturn(null));
                MockedConstruction<FamilienmitgliedDAOimpl> fmDaoMock = mockConstruction(FamilienmitgliedDAOimpl.class, (mock, context) -> when(mock.getAllFamilienmitglieder(anyInt())).thenReturn(new ArrayList<>()))
        ) {
            ArrayList<Booking_err_warn_list> warnungen = haushalt.getBuchungswarnungen(mockWarentyp);

            assertTrue(warnungen.getFirst().getList_message().contains("Sollsaldo"));
        }
    }

    @Test
    @Tag("unit")
    void getBookingWarnings_returnsWarningForMissingNotice() {
        Familienmitglied mitgliedOhneBescheid = mock(Familienmitglied.class);
        when(mitgliedOhneBescheid.getvName()).thenReturn("Max");
        when(mitgliedOhneBescheid.getnName()).thenReturn("Mustermann");
        ArrayList<Familienmitglied> mitglieder = new ArrayList<>(List.of(mitgliedOhneBescheid));

        try (
                MockedConstruction<EinkaufDAOimpl> einkaufDaoMock = mockConstruction(EinkaufDAOimpl.class, (mock, context) -> when(mock.getLetzerEinkauf(any())).thenReturn(null));
                MockedConstruction<FamilienmitgliedDAOimpl> fmDaoMock = mockConstruction(FamilienmitgliedDAOimpl.class, (mock, context) -> when(mock.getAllFamilienmitglieder(anyInt())).thenReturn(mitglieder));
                MockedConstruction<BescheidDAOimpl> bescheidDaoMock = mockConstruction(BescheidDAOimpl.class, (mock, context) -> when(mock.readAllGueltige(any())).thenReturn(new ArrayList<>()))
        ) {
            ArrayList<String> warnungen = haushalt.getBuchungswarnungen(mockWarentyp);

            assertTrue(warnungen.stream().anyMatch(s -> s.contains("keine gültigen Bescheide")));
        }
    }
*/
    
    @Test
    @Tag("unit")
    void getHouseholdHead_findsCorrectPerson() {
        Familienmitglied mitglied1 = mock(Familienmitglied.class);
        when(mitglied1.isHaushaltsVorstand()).thenReturn(false);

        Familienmitglied vorstand = mock(Familienmitglied.class);
        when(vorstand.isHaushaltsVorstand()).thenReturn(true);

        ArrayList<Familienmitglied> mitglieder = new ArrayList<>(List.of(mitglied1, vorstand));

        Familienmitglied gefundenerVorstand = haushalt.getHaushaltsvorstand(mitglieder);

        assertEquals(vorstand, gefundenerVorstand);
    }

    @Test
    @Tag("unit")
    void getHouseholdHead_returnsNullIfNoneFound() {
        Familienmitglied mitglied1 = mock(Familienmitglied.class);
        when(mitglied1.isHaushaltsVorstand()).thenReturn(false);
        Familienmitglied mitglied2 = mock(Familienmitglied.class);
        when(mitglied2.isHaushaltsVorstand()).thenReturn(false);

        ArrayList<Familienmitglied> mitglieder = new ArrayList<>(List.of(mitglied1, mitglied2));

        Familienmitglied gefundenerVorstand = haushalt.getHaushaltsvorstand(mitglieder);

        assertNull(gefundenerVorstand);
    }
}
