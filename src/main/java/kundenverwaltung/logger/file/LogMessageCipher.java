package kundenverwaltung.logger.file;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.*;

public class LogMessageCipher
{
    private static final String DEFAULT_SECRET_KEY = "2025_DEFAULT_SECRET_KEY_TAFEL_VERWALTUNG";
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;
    private static String currentSecretKey = DEFAULT_SECRET_KEY;

    /**
     * Prepares an AES secret key from a given string.
     * The key is padded or truncated to a valid AES key size (16, 24, or 32 bytes).
     *
     * @param keyString The string to be used as the basis for the secret key.
     * @return A SecretKey object suitable for AES encryption.
     */
    private static SecretKey prepareAESKey(String keyString)
    {
        byte[] keyBytes = keyString.getBytes();
        int aesKeyLength = keyBytes.length < 16 ? 16
                : keyBytes.length < 24 ? 24 : 32;
        return new SecretKeySpec(Arrays.copyOf(keyBytes, aesKeyLength), "AES");
    }

    /**
     * Encrypts a given message using AES/GCM algorithm.
     *
     * @param message The plain text message to be encrypted.
     * @return A Base64 encoded string representing the encrypted message.
     * @throws RuntimeException if the encryption process fails.
     */
    public static String encryptMessage(String message)
    {
        try
        {
            SecretKey key = prepareAESKey(getCurrentKey());
            byte[] iv = new SecureRandom().generateSeed(IV_LENGTH);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH, iv));
            byte[] encrypted = cipher.doFinal(message.getBytes());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(iv);
            outputStream.write(encrypted);

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        }
        catch (Exception e)
        {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    /**
     * Decrypts a given Base64 encoded message using AES/GCM algorithm.
     *
     * @param encryptedBase64       The Base64 encoded string to be decrypted.
     * @param useDefaultEncryptionKey If true, the default secret key is used for decryption. Otherwise, the user-specific key is used.
     * @return The decrypted, plain text message.
     * @throws RuntimeException if the decryption process fails.
     */
    public static String decryptMessage(String encryptedBase64, boolean useDefaultEncryptionKey)
    {
        try
        {
            byte[] decoded = Base64.getDecoder().decode(encryptedBase64);
            byte[] iv = Arrays.copyOfRange(decoded, 0, IV_LENGTH);
            byte[] encrypted = Arrays.copyOfRange(decoded, IV_LENGTH, decoded.length);

            SecretKey key = prepareAESKey(useDefaultEncryptionKey ? DEFAULT_SECRET_KEY : getCurrentKey());
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH, iv));

            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Decryption failed", e);
        }
    }
    
    /**
     * Sets the secret key for the current user session.
     * The provided key is combined with a default salt to create the final secret key.
     *
     * @param key The user-specific part of the secret key.
     */
    public static void setSecretKey(String key)
    {
        currentSecretKey = DEFAULT_SECRET_KEY.replace("TAFEL_VERWALTUNG", key);
    }

    /**
     * Returns the current secret key.
     *
     * @return The current secret key.
     */
    private static String getCurrentKey()
    {
        return currentSecretKey;
    }
}