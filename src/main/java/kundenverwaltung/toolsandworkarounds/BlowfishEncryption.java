package kundenverwaltung.toolsandworkarounds;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Blowfish encryption algorithm<br>
 * Symmetric-key cryptography: encryption of plaintext (String inputString) and decryption of
 * ciphertext with the same cryptographic key (String KEY_STRING).
 *
 * @author Robin Becker
 * @version 1.0
 * @Date 31.07.2018
 */
public class BlowfishEncryption
{
    private static final String KEY_STRING = "qx9#HYbWH%4h@IzU";

    /**
     * Returns an encrypted string with Blowfish encryption algorithm and cryptographic key
     * (String strkey).
     *
     * @param inputString a plaintext string to be encrypted
     * @return the encrypted string with Blowfish algorithm
     */
    public static String encrypt(String inputString)
    {
        if (inputString == null || inputString.isEmpty())
        {
            return ""; // Wenn das Passwort leer ist, gibt es keine Verschlüsselung
        }

        String strData = null;

        try
        {
            SecretKeySpec key = new SecretKeySpec(KEY_STRING.getBytes("UTF8"), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            strData = Base64.getEncoder()
                    .encodeToString(cipher.doFinal(inputString.getBytes("UTF8")));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return strData;
    }

    /**
     * Returns a decrypted string with Blowfish encryption algorithm and cryptographic key
     * (String strkey).
     *
     * @param encryptedInputString an encrypted string to be decrypted
     * @return the decrypted string with Blowfish algorithm
     */
    public static String decrypt(String encryptedInputString)
    {
        if (encryptedInputString == null || encryptedInputString.isEmpty())
        {
            return ""; // Wenn das verschlüsselte Passwort leer ist, gibt es nichts zu entschlüsseln
        }

        String strData = null;

        try
        {
            byte[] encryptedData = Base64.getDecoder().decode(encryptedInputString);

            // Überprüfe, ob die Länge des verschlüsselten Textes korrekt ist
            if (encryptedData.length % 8 != 0)
            {
                throw new IllegalArgumentException("Die Länge des verschlüsselten Textes ist ungültig.");
            }

            SecretKeySpec key = new SecretKeySpec(KEY_STRING.getBytes("UTF8"), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(encryptedData);
            strData = new String(decrypted);

        } catch (IllegalArgumentException e)
        {
            System.err.println("Fehler beim Entschlüsseln: " + e.getMessage());
            return null; // Rückgabe von null bei Fehler
        } catch (Exception e)
        {
            e.printStackTrace();
            return null; // Rückgabe von null bei Fehler
        }
        return strData;
    }

}