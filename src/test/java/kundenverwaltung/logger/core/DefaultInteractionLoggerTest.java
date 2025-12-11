package kundenverwaltung.logger.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DefaultInteractionLoggerTest
{

    private DefaultInteractionLogger interactionLogger;
    private Logger mockLogger;

    @BeforeEach
    void setUp()
    {
        mockLogger = mock(Logger.class);

        // Overridden... because we cannot access private-static value...
        interactionLogger = new DefaultInteractionLogger()
        {
            @Override
            public void logInteraction(String message, String... args)
            {
                mockLogger.info(message, (Object[]) args);
            }
        };
    }

    @Test
    @Tag("unit")
    void testLogInteraction()
    {
        String message = "User interaction with ID: {}";
        String arg = "12345";
        interactionLogger.logInteraction(message, arg);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object[]> argsCaptor = ArgumentCaptor.forClass(Object[].class);

        verify(mockLogger).info(messageCaptor.capture(), argsCaptor.capture());

        assertEquals(message, messageCaptor.getValue());
        assertArrayEquals(new Object[]{arg}, argsCaptor.getValue());
    }
}
