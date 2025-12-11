package kundenverwaltung.controller.admintool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdmintoolControllerTest {

    private AdmintoolController controller;

    @BeforeEach
    void setUp() {
        controller = new AdmintoolController();
    }

    @Test
    @Tag("unit")
    void testStaticLastSelectedAusgabegruppeId() {
        AdmintoolController.setLastSelectedAusgabegruppeId(42);
        assertEquals(42, AdmintoolController.getLastSelectedAusgabegruppeId());

        AdmintoolController.setLastSelectedAusgabegruppeId(100);
        assertEquals(100, AdmintoolController.getLastSelectedAusgabegruppeId());
    }

    @Test
    @Tag("unit")
    void testSingletonInstance() {
        AdmintoolController instance1 = AdmintoolController.getInstance();
        AdmintoolController instance2 = AdmintoolController.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    @Tag("unit")
    void testBenutzerverwaltungControllerSetterGetter() {
        BenutzerverwaltungController mockController = new BenutzerverwaltungController();
        AdmintoolController.setBenutzerverwaltungController(mockController);
        assertEquals(mockController, AdmintoolController.getBenutzerverwaltungController());
    }
}
