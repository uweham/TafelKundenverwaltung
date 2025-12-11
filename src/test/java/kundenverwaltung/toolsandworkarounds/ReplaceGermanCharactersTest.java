package kundenverwaltung.toolsandworkarounds;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ReplaceGermanCharactersTest {

    private ReplaceGermanCharacters replacer;

    @BeforeEach
    void setUp() {
        replacer = new ReplaceGermanCharacters();
    }

    @Test
    @Tag("unit")
    void testReplaceGermanUmlauts_nullInput_returnsNull() {
        assertNull(replacer.replaceGermanUmlauts(null));
    }

    @Test
    @Tag("unit")
    void testReplaceGermanUmlauts_noSpecialChars_returnsSameString() {
        String input = "Hallo Welt";
        String output = replacer.replaceGermanUmlauts(input);
        assertEquals(input, output);
    }

    @Test
    @Tag("unit")
    void testReplaceGermanUmlauts_replacesUmlautsCorrectly() {
        String input = "ÄäÖöÜüß€";
        String expected = "&Auml;&auml;&Ouml;&ouml;&Uuml;&uuml;&szlig;&euro;";
        String output = replacer.replaceGermanUmlauts(input);
        assertEquals(expected, output);
    }

    @Test
    @Tag("unit")
    void testReplaceGermanUmlauts_mixedText() {
        String input = "Grüße von der Küste: 50€";
        String expected = "Gr&uuml;&szlig;e von der K&uuml;ste: 50&euro;";
        String output = replacer.replaceGermanUmlauts(input);
        assertEquals(expected, output);
    }
}
