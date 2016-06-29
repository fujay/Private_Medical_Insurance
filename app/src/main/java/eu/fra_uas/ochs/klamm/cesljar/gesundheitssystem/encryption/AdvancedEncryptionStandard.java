package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.encryption;

import android.util.Base64;
import android.util.Log;

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

    public static final String TAG = /*"TEST";*/AdvancedEncryptionStandard.class.getSimpleName();
    private static final String SECURE_HASH_ALGORITHM = "SHA1PRNG";
    private static final String SYMMETRIC_ALGORITHM = "AES";

    private String data;
    private SecretKeySpec secretKeySpec;
    private byte[] encodedBytes = null;
    private byte[] decodedBytes = null;

    public AdvancedEncryptionStandard() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_HASH_ALGORITHM);
            secureRandom.setSeed(TAG.getBytes());
            KeyGenerator keyGenerator = KeyGenerator.getInstance(SYMMETRIC_ALGORITHM);
            keyGenerator.init(128, secureRandom);
            secretKeySpec = new SecretKeySpec((keyGenerator.generateKey()).getEncoded(), SYMMETRIC_ALGORITHM);
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
    }

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
        this.data = text;
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

    public static String encrypt(String seed, String cleartext) {
        byte[] result = null;
        try {
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_HASH_ALGORITHM);
            secureRandom.setSeed(seed.getBytes());
            KeyGenerator keyGenerator = KeyGenerator.getInstance(SYMMETRIC_ALGORITHM);
            keyGenerator.init(128, secureRandom);
            SecretKeySpec secretKeySpec = new SecretKeySpec((keyGenerator.generateKey()).getEncoded(), SYMMETRIC_ALGORITHM);

            Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            result = cipher.doFinal(cleartext.getBytes());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(result, Base64.DEFAULT);
    }

    public static String decrypt(String seed, String encrypted) {
        byte[] result = null;
        byte[] enc = null;
        try {
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_HASH_ALGORITHM);
            secureRandom.setSeed(seed.getBytes());
            KeyGenerator keyGenerator = KeyGenerator.getInstance(SYMMETRIC_ALGORITHM);
            keyGenerator.init(128, secureRandom);
            SecretKeySpec secretKeySpec = new SecretKeySpec((keyGenerator.generateKey()).getEncoded(), SYMMETRIC_ALGORITHM);

            enc = encrypted.getBytes();
            Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            result = cipher.doFinal(enc);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new String(result);
        //return Base64.encodeToString(result, Base64.DEFAULT);
        //return result.toString();
    }

    public String encodeText(String text) {
        try {
            Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encodedBytes = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
    }

    public String decodeText(String text) {
        try {
            encodedBytes = text.getBytes();
            Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decodedBytes = cipher.doFinal(encodedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(decodedBytes);
    }

    /**
     * Encode the original data with AES
     * @return the encrypted text
     */
    public String encrypt() {
        try {
            Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encodedBytes = cipher.doFinal(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
    }

    /**
     * Decode the encoded data with AES
     * @return the decrypted text
     */
    public String decrypt() {
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
