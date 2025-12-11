package kundenverwaltung.service;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnonymizeServiceTest
{


    @Test
    void anonymizeAllOpenWindows()
    {
        AnonymizeService service = AnonymizeService.getInstance();

        assertDoesNotThrow(() -> service.anonymizeAllOpenWindows(true),
                "anonymizeAllOpenWindows(true) should not throw an exception");

        assertDoesNotThrow(() -> service.anonymizeAllOpenWindows(false),
                "anonymizeAllOpenWindows(false) should not throw an exception");
    }

    @Test
    void getInstance()
    {
        AnonymizeService instance1 = AnonymizeService.getInstance();
        AnonymizeService instance2 = AnonymizeService.getInstance();

        assertNotNull(instance1, "getInstance should not return null");
        assertSame(instance1, instance2, "getInstance should return the same instance (singleton pattern)");
    }
}