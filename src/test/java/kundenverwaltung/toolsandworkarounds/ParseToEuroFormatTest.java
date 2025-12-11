package kundenverwaltung.toolsandworkarounds;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ParseToEuroFormatTest {

    private final ParseToEuroFormat parser = new ParseToEuroFormat();

    @Test
    @Tag("unit")
    void parseFloatToEuroString_float_withEuroSymbol() {
        assertEquals("12,34 €", parser.parseFloatToEuroString(12.34f, true));
        assertEquals("12,30 €", parser.parseFloatToEuroString(12.3f, true));  // prüft Auffüllen auf 2 Dezimalstellen
        assertEquals("0,00 €", parser.parseFloatToEuroString(0f, true));
        assertEquals("12,35 €", parser.parseFloatToEuroString(12.345f, true)); // prüft Runden
    }

    @Test
    @Tag("unit")
    void parseFloatToEuroString_float_withoutEuroSymbol() {
        assertEquals("12,34", parser.parseFloatToEuroString(12.34f, false));
        assertEquals("12,30", parser.parseFloatToEuroString(12.3f, false));
        assertEquals("0,00", parser.parseFloatToEuroString(0f, false));
    }

    @Test
    @Tag("unit")
    void parseFloatToEuroString_double_withEuroSymbol() {
        assertEquals("12,34 €", parser.parseFloatToEuroString(12.34, true));
        assertEquals("12,30 €", parser.parseFloatToEuroString(12.3, true));
        assertEquals("0,00 €", parser.parseFloatToEuroString(0.0, true));
        assertEquals("12,35 €", parser.parseFloatToEuroString(12.345, true));
    }

    @Test
    @Tag("unit")
    void parseFloatToEuroString_double_withoutEuroSymbol() {
        assertEquals("12,34", parser.parseFloatToEuroString(12.34, false));
        assertEquals("12,30", parser.parseFloatToEuroString(12.3, false));
        assertEquals("0,00", parser.parseFloatToEuroString(0.0, false));
    }
}
