package kundenverwaltung.model;

import kundenverwaltung.dao.VorlageDAO;
import kundenverwaltung.dao.VorlageDAOimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Blob;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VorlageTest {

    @Mock
    private Blob mockDaten;

    private Vorlage vorlage;

    private final Vorlagearten testTemplateType = Vorlagearten.Liste;

    @BeforeEach
    void setUp() {
        vorlage = new Vorlage(
                1,
                testTemplateType,
                "Testvorlage",
                "Max Mustermann",
                "1.0",
                ".docx",
                "Standardtext für die Vorlage",
                1234,
                true,
                mockDaten
        );
    }

    @Test
    @Tag("unit")
    void constructor_withId_setsAllFieldsCorrectly() {
        assertAll("Prüfen der durch den Konstruktor mit ID gesetzten Werte",
                () -> assertEquals(1, vorlage.getVorlageId()),
                () -> assertEquals(testTemplateType, vorlage.getTemplateType()),
                () -> assertEquals("Testvorlage", vorlage.getName()),
                () -> assertEquals("Max Mustermann", vorlage.getAutor()),
                () -> assertEquals("1.0", vorlage.getFileVersion()),
                () -> assertEquals(".docx", vorlage.getFileTypes()),
                () -> assertEquals("Standardtext für die Vorlage", vorlage.getDefaultText()),
                () -> assertEquals(1234, vorlage.getPasswort()),
                () -> assertTrue(vorlage.isAktiv()),
                () -> assertEquals(mockDaten, vorlage.getDaten())
        );
    }

    @Test
    @Tag("unit")
    void constructor_withoutId_callsDAOCreate() {
        try (MockedConstruction<VorlageDAOimpl> daoMock = mockConstruction(VorlageDAOimpl.class)) {
            Vorlage neueVorlage = new Vorlage(
                    testTemplateType,
                    "Neue Vorlage",
                    "Anna Anders",
                    "2.0",
                    ".pdf",
                    "Anderer Text",
                    5678,
                    false,
                    mockDaten
            );

            assertEquals(1, daoMock.constructed().size());
            VorlageDAO mockDao = daoMock.constructed().get(0);

            verify(mockDao, times(1)).create(neueVorlage);

            assertEquals("Neue Vorlage", neueVorlage.getName());
            assertFalse(neueVorlage.isAktiv());
        }
    }

    @Test
    @Tag("unit")
    void setTemplateId_setsIdCorrectly() {
        vorlage.setVorlageId(99);
        assertEquals(99, vorlage.getVorlageId());
    }

    @Test
    @Tag("unit")
    void isActiveString_returnsCorrectStatus() {
        assertEquals(Vorlage.ENABLED, vorlage.isAktivString());

        Vorlage inaktiveVorlage = new Vorlage(
                2,
                testTemplateType,
                "Inaktive Vorlage",
                "Autor", "1.0", ".txt", "text", 0,
                false, // explizit auf false gesetzt
                mockDaten
        );
        assertEquals(Vorlage.DISABLED, inaktiveVorlage.isAktivString());
    }
}