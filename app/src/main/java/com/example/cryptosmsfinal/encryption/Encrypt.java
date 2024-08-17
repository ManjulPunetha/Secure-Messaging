package com.example.cryptosmsfinal.encryption;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.example.cryptosmsfinal.encryption.KeyGenerator.generateKey;

/**
 * This class is to encrypt the message entered by the user using
 * the Key provided by the user. It uses AES algorithm.
 */
public class Encrypt {
    public static String encrypt(String fsMessage, String fsKey) throws Exception {
        // Derive key from string (insecure, for demonstration only)
        SecretKeySpec key = generateKey(fsKey);

        // Generate random IV
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Encrypt the message
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(fsMessage.getBytes());

        // Concatenate IV and encrypted message (for decryption)
        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

        // Encode to Base64 for easier handling
        return Base64.getEncoder().encodeToString(combined);
    }
}