package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.encryption;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * Symmetric Encryption Example - AES
 * Uses secret key for 128-bit AES encryption and decryption
 */
public class AdvancedEncryptionStandard {

    private static final String TAG = AdvancedEncryptionStandard.class.getSimpleName();
    private static final String SECURE_HASH_ALGORITHM = "SHA1PRNG";
    private static final String SYMMETRIC_ALGORITHM = "AES";

    private String text;
    private SecretKeySpec secretKeySpec = null;
    private byte[] encodedBytes = null;
    private byte[] decodedBytes = null;

    /**
     * Default contructor, calls extended Contructor with class name as parameter
     */
    public AdvancedEncryptionStandard(String text) {
        this(TAG, text);
    }

    /**
     * * // Set up secret key spec for 128-bit AES encryption and decryption
     * @param seed data used as random seed
     * @param text text which should be en/decrepted
     */
    public AdvancedEncryptionStandard(String seed, String text) {
        this.text = text;
        try {
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_HASH_ALGORITHM);
            secureRandom.setSeed(seed.getBytes());
            KeyGenerator keyGenerator = KeyGenerator.getInstance(SYMMETRIC_ALGORITHM);
            keyGenerator.init(128, secureRandom);
            secretKeySpec = new SecretKeySpec((keyGenerator.generateKey()).getEncoded(), SYMMETRIC_ALGORITHM);
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
    }

    /**
     * Encode the original data with AES
     * @return the encrypted text
     */
    public String encode() {
        try {
            Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encodedBytes = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
    }

    /**
     * Decode the encoded data with AES
     * @return the decrypted text
     */
    public String decode() {
        try {
            Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decodedBytes = cipher.doFinal(encodedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(decodedBytes);
        //return decodedBytes.toString();
    }
}
