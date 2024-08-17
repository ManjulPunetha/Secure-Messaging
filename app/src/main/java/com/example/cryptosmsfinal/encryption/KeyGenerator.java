package com.example.cryptosmsfinal.encryption;

import java.security.MessageDigest;

import javax.crypto.spec.SecretKeySpec;

/**
 * This class provides a method for generating an AES key from a string.
 */
public class KeyGenerator {

    /**
     * Derives an AES key from a given string.
     *
     * @param keyString - The string to derive the key from.
     * @return A SecretKeySpec representing the derived AES key.
     * @throws Exception- If an error occurs during key generation.
     */
    public static SecretKeySpec generateKey(String keyString) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(keyString.getBytes());
        return new SecretKeySpec(keyBytes, "AES");
    }
}
