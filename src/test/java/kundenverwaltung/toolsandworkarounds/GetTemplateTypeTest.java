package kundenverwaltung.toolsandworkarounds;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import kundenverwaltung.model.Vorlagearten;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests für GetTemplateType")
class GetTemplateTypeTest {

    private GetTemplateType getTemplateType;

    @BeforeEach
    void setUp() {
        getTemplateType = new GetTemplateType();
    }

    @Nested
    class GetTemplateTypeFromStringTests {

        static Stream<Arguments> provideValidStringsAndExpectedEnums() {
            return Stream.of(
                    Arguments.of("kvpKassenabrechnung", Vorlagearten.Kassenabrechnung),
                    Arguments.of("kvpAusweis", Vorlagearten.Ausweis),
                    Arguments.of("kvpListe", Vorlagearten.Liste),
                    Arguments.of("kvpDSGVO", Vorlagearten.DSGVO),
                    Arguments.of("kvpSonstiges", Vorlagearten.Sonstiges)
            );
        }

        @ParameterizedTest
        @MethodSource("provideValidStringsAndExpectedEnums")
        void whenGivenValidString_shouldReturnType(String input, Vorlagearten expected) {
            Vorlagearten actual = getTemplateType.getTemplateType(input);
            assertEquals(expected, actual);
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "unbekannt", "kvp"})
        void whenGivenInvalidOrEmptyString_shouldReturnNull(String invalidInput) {
            assertNull(getTemplateType.getTemplateType(invalidInput));
        }

        @Test
        @Tag("unit")
        void whenGivenNull_shouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, () -> {
                getTemplateType.getTemplateType(null);
            });
        }
    }

    @Nested
    class GetTemplateStringFromEnumTests {

        static Stream<Arguments> provideEnumsAndExpectedStrings() {
            return Stream.of(
                    Arguments.of(Vorlagearten.Kassenabrechnung, "kvpKassenabrechnung"),
                    Arguments.of(Vorlagearten.Ausweis, "kvpAusweis"),
                    Arguments.of(Vorlagearten.Liste, "kvpListe"),
                    Arguments.of(Vorlagearten.DSGVO, "kvpDSGVO"),
                    Arguments.of(Vorlagearten.Sonstiges, "kvpSonstiges")
            );
        }

        @ParameterizedTest
        @MethodSource("provideEnumsAndExpectedStrings")
        void whenGivenValidEnum_shouldReturnString(Vorlagearten input, String expected) {
            String actual = getTemplateType.getTemplateString(input);
            assertEquals(expected, actual);
        }

        @Test
        @Tag("unit")
        void whenGivenNull_shouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, () -> {
                getTemplateType.getTemplateString(null);
            });
        }
    }
}