package kundenverwaltung.toolsandworkarounds;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class PropertiesFileControllerTest {

    @Test
    @Tag("unit")
    void testLoadDbInfoPropertiesFile_notNull() {
        Properties props = PropertiesFileController.loadDbInfoPropertiesFile();
        assertNotNull(props, "Properties-Objekt darf nicht null sein");
        assertFalse(props.isEmpty(), "Properties sollte nicht leer sein");
    }

    @Test
    @Tag("unit")
    void testLoadTafelInfoPropertiesFile_notNull() {
        Properties props = PropertiesFileController.loadTafelInfoPropertiesFile();
        assertNotNull(props, "Properties-Objekt darf nicht null sein");
        assertFalse(props.isEmpty(), "Properties sollte nicht leer sein");
    }

    @Test
    @Tag("unit")
    void testGetDbName_returnsValue() {
        String dbName = PropertiesFileController.getDbName();
        assertNotNull(dbName, "Datenbankname darf nicht null sein");
        assertFalse(dbName.isEmpty(), "Datenbankname darf nicht leer sein");
    }

    @Test
    @Tag("unit")
    void testGetTafelName_returnsValue() {
        String tafelName = PropertiesFileController.getTafelName();
        assertNotNull(tafelName, "Tafelname darf nicht null sein");
        assertFalse(tafelName.isEmpty(), "Tafelname darf nicht leer sein");
    }
}
