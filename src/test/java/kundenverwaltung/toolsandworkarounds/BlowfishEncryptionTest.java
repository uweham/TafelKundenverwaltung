package kundenverwaltung.toolsandworkarounds;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BlowfishEncryptionTest {

    /** Test valid string encryption */
    @Test
    void encrypt_WithValidString_ShouldReturnEncryptedString()
    {
        String plaintext = "test123";

        String encrypted = BlowfishEncryption.encrypt(plaintext);

        assertNotNull(encrypted, "Encrypted string should not be null");
        assertFalse(encrypted.isEmpty(), "Encrypted string should not be empty");
        assertNotEquals(plaintext, encrypted, "Encrypted string should be different from plaintext");
        assertTrue(encrypted.length() > 0, "Encrypted string should have content");
    }

    /** Test null input encryption */
    @Test
    void encrypt_WithNullInput_ShouldReturnEmptyString()
    {
        String nullInput = null;

        String result = BlowfishEncryption.encrypt(nullInput);

        assertEquals("", result, "Encrypting null should return empty string");
    }

    /** Test empty string encryption */
    @Test
    void encrypt_WithEmptyString_ShouldReturnEmptyString()
    {
        String emptyInput = "";

        String result = BlowfishEncryption.encrypt(emptyInput);

        assertEquals("", result, "Encrypting empty string should return empty string");
    }

    /** Test valid decryption */
    @Test
    void decrypt_WithValidEncryptedString_ShouldReturnOriginalString()
    {
        String originalText = "password123";
        String encrypted = BlowfishEncryption.encrypt(originalText);

        String decrypted = BlowfishEncryption.decrypt(encrypted);

        assertEquals(originalText, decrypted, "Decrypted text should match original");
    }

    /** Test null input decryption */
    @Test
    void decrypt_WithNullInput_ShouldReturnEmptyString()
    {
        String nullInput = null;

        String result = BlowfishEncryption.decrypt(nullInput);

        assertEquals("", result, "Decrypting null should return empty string");
    }

    /** Test empty string decryption */
    @Test
    void decrypt_WithEmptyString_ShouldReturnEmptyString()
    {
        String emptyInput = "";

        String result = BlowfishEncryption.decrypt(emptyInput);

        assertEquals("", result, "Decrypting empty string should return empty string");
    }

    /** Test invalid Base64 decryption */
    @Test
    void decrypt_WithInvalidBase64_ShouldReturnNull()
    {
        String invalidBase64 = "this-is-not-valid-base64!@#";

        String result = BlowfishEncryption.decrypt(invalidBase64);

        assertNull(result, "Decrypting invalid Base64 should return null");
    }

    /** Test invalid block size decryption */
    @Test
    void decrypt_WithInvalidBlockSize_ShouldReturnNull()
    {
        String invalidBlockSize = "YWJj"; // "abc" in Base64 - only 3 bytes, not multiple of 8

        String result = BlowfishEncryption.decrypt(invalidBlockSize);

        assertNull(result, "Decrypting with invalid block size should return null");
    }

    /** Test encryption-decryption roundtrip */
    @Test
    void encryptDecrypt_Roundtrip_ShouldPreserveOriginalText()
    {
        String[] testStrings = {
                "Hello World",
                "password123",
                "üöäß", // German umlauts
                "!@#$%^&*()", // Special characters
                "1234567890",
                "Very long text that should still work correctly with Blowfish encryption algorithm"
        };

        for (String original : testStrings)
        {
            String encrypted = BlowfishEncryption.encrypt(original);
            String decrypted = BlowfishEncryption.decrypt(encrypted);

            assertEquals(original, decrypted,
                    "Round-trip encryption/decryption should preserve original text: " + original);
        }
    }

    /** Test consistent encryption output */
    @Test
    void encrypt_SameInputTwice_ShouldProduceSameOutput()
    {
        String input = "test123";

        String encrypted1 = BlowfishEncryption.encrypt(input);
        String encrypted2 = BlowfishEncryption.encrypt(input);

        assertEquals(encrypted1, encrypted2,
                "Same input should always produce same encrypted output");
    }

    /** Test different inputs produce different outputs */
    @Test
    void encrypt_DifferentInputs_ShouldProduceDifferentOutputs()
    {
        String input1 = "password1";
        String input2 = "password2";

        String encrypted1 = BlowfishEncryption.encrypt(input1);
        String encrypted2 = BlowfishEncryption.encrypt(input2);

        assertNotEquals(encrypted1, encrypted2,
                "Different inputs should produce different encrypted outputs");
    }

    /** Test encryption output is valid Base64 */
    @Test
    void encrypt_OutputShouldBeValidBase64()
    {
        String input = "test123";

        String encrypted = BlowfishEncryption.encrypt(input);

        assertDoesNotThrow(() -> {
            java.util.Base64.getDecoder().decode(encrypted);
        }, "Encrypted output should be valid Base64");
    }

    /** Test tampered data decryption */
    @Test
    void decrypt_WithTamperedData_ShouldReturnNull()
    {
        String original = "secret";
        String encrypted = BlowfishEncryption.encrypt(original);

        // Tamper with the encrypted data by changing last character
        String tamperedEncrypted = encrypted.substring(0, encrypted.length() - 1) + "X";

        String result = BlowfishEncryption.decrypt(tamperedEncrypted);

        assertNull(result, "Decrypting tampered data should return null");
    }

    /** Test methods are static */
    @Test
    void blowfishEncryption_MethodsShouldBeStatic() throws NoSuchMethodException
    {
        var encryptMethod = BlowfishEncryption.class.getDeclaredMethod("encrypt", String.class);
        assertTrue(java.lang.reflect.Modifier.isStatic(encryptMethod.getModifiers()),
                "encrypt method should be static");

        var decryptMethod = BlowfishEncryption.class.getDeclaredMethod("decrypt", String.class);
        assertTrue(java.lang.reflect.Modifier.isStatic(decryptMethod.getModifiers()),
                "decrypt method should be static");
    }

    /** Test class is public and in correct package */
    @Test
    void blowfishEncryption_ClassShouldBePublic()
    {
        assertTrue(java.lang.reflect.Modifier.isPublic(BlowfishEncryption.class.getModifiers()),
                "BlowfishEncryption class should be public");

        assertEquals("kundenverwaltung.toolsandworkarounds",
                BlowfishEncryption.class.getPackageName(),
                "BlowfishEncryption should be in correct package");
    }

    /** Test invalid block size error handling */
    @Test
    void decrypt_WithInvalidBlockSize_ShouldPrintErrorMessage()
    {
        String invalidBlockSize = "YWJj"; // 3 bytes, not multiple of 8

        // Capture System.err output
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        try
        {
            String result = BlowfishEncryption.decrypt(invalidBlockSize);

            assertNull(result, "Should return null for invalid block size");
            assertTrue(errContent.toString().contains("Fehler beim Entschlüsseln"),
                    "Should print error message to System.err");
        } finally
        {
            // Restore System.err
            System.setErr(originalErr);
        }
    }

    /** Test Unicode character handling */
    @Test
    void encryptDecrypt_WithUnicodeCharacters_ShouldWorkCorrectly()
    {
        String unicodeText = "Hello 🌍 Unicode: αβγδε 中文 العربية";

        String encrypted = BlowfishEncryption.encrypt(unicodeText);
        String decrypted = BlowfishEncryption.decrypt(encrypted);

        assertEquals(unicodeText, decrypted,
                "Should handle Unicode characters correctly");
    }
}