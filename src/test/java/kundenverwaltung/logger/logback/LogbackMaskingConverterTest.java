package kundenverwaltung.logger.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class LogbackMaskingConverterTest {

    @Test
    @Tag("unit")
    void convert_shouldMaskSensitiveInputs() {
        String rawMessage = "Eingabe: passwort123, Auswahl: geheim, Datum: 2025-07-13";

        ILoggingEvent event = Mockito.mock(ILoggingEvent.class);
        Mockito.when(event.getFormattedMessage()).thenReturn(rawMessage);

        LogbackMaskingConverter converter = new LogbackMaskingConverter();
        String maskedMessage = converter.convert(event);

        System.out.println(maskedMessage);

        assertTrue(maskedMessage.contains("Eingabe: ***********"));
        assertTrue(maskedMessage.contains("Auswahl: ******"));
        assertTrue(maskedMessage.contains("Datum: **********"));
    }

    @Test
    @Tag("unit")
    void convert_shouldHandleBracketedSensitiveInput() {
        String rawMessage = "Parameter: [username, email, geheim1234]";

        ILoggingEvent event = Mockito.mock(ILoggingEvent.class);
        Mockito.when(event.getFormattedMessage()).thenReturn(rawMessage);

        LogbackMaskingConverter converter = new LogbackMaskingConverter();
        String maskedMessage = converter.convert(event);

        assertTrue(maskedMessage.contains("[username, email, **********]"));
    }

    @Test
    @Tag("unit")
    void convert_shouldNotAlterNonSensitiveInput() {
        String rawMessage = "Keine sensiblen Daten hier.";

        ILoggingEvent event = Mockito.mock(ILoggingEvent.class);
        Mockito.when(event.getFormattedMessage()).thenReturn(rawMessage);

        LogbackMaskingConverter converter = new LogbackMaskingConverter();
        String maskedMessage = converter.convert(event);

        assertEquals(rawMessage, maskedMessage);
    }

    @Test
    @Tag("unit")
    void convert_shouldMaskMultipleSensitiveInputs() {
        String rawMessage = "Eingabe: geheim123, Auswahl: topsecret, Datum: 2025-07-13, [login, password, secretValue]";

        ILoggingEvent event = Mockito.mock(ILoggingEvent.class);
        Mockito.when(event.getFormattedMessage()).thenReturn(rawMessage);

        LogbackMaskingConverter converter = new LogbackMaskingConverter();
        String maskedMessage = converter.convert(event);

        assertTrue(maskedMessage.contains("Eingabe: *********"));
        assertTrue(maskedMessage.contains("Auswahl: *********"));
        assertTrue(maskedMessage.contains("Datum: **********"));
        assertTrue(maskedMessage.contains("[login, password, ***********]"));
    }
}
