package kundenverwaltung.logger.file;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class LogMessageCipherTest
{

    @Test
    @Tag("unit")
    public void encryptAndDecrypt_shouldReturnOriginalMessage()
    {
        String original = "Dies ist eine geheime Nachricht.";

        String encrypted = LogMessageCipher.encryptMessage(original);
        assertNotNull(encrypted, "Die verschlüsselte Nachricht darf nicht null sein");
        assertNotEquals(original, encrypted, "Die verschlüsselte Nachricht darf nicht mit dem Original übereinstimmen");

        String decrypted = LogMessageCipher.decryptMessage(encrypted, true);
        assertEquals(original, decrypted, "Die entschlüsselte Nachricht muss dem Original entsprechen");
    }

    @Test
    @Tag("unit")
    public void decrypt_withInvalidData_shouldThrow()
    {
        String invalid = "ungültigeBase64Daten";

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
        {
            LogMessageCipher.decryptMessage(invalid, true);
        });

        assertTrue(exception.getMessage().contains("Decryption failed"), "Die Fehlermeldung sollte auf einen Entschlüsselungsfehler hinweisen");
    }

    @Test
    @Tag("unit")
    public void setSecretKey_shouldChangeEncryptionKey()
    {
        String original = "Testnachricht";

        LogMessageCipher.setSecretKey("NEUER_GEHEIMSCHLUESSEL_TAFEL_VERWALTUNG");

        String encrypted = LogMessageCipher.encryptMessage(original);
        String decrypted = LogMessageCipher.decryptMessage(encrypted, false);

        assertEquals(original, decrypted, "Die entschlüsselte Nachricht muss nach Schlüsseländerung dem Original entsprechen");
    }
}
