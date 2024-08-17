package com.example.cryptosmsfinal.encryption;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.example.cryptosmsfinal.encryption.KeyGenerator.generateKey;

public class Decrypt {
    public static String decrypt(String fsEncryptedMessage, String fsKey) throws Exception {
        // Decode from Base64
        byte[] combined = Base64.getDecoder().decode(fsEncryptedMessage);

        // Extract IV and encrypted message
        byte[] iv = new byte[16];
        byte[] encryptedBytes = new byte[combined.length - iv.length];
        System.arraycopy(combined, 0, iv, 0, iv.length);
        System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);

        // Derive key from string (insecure, for demonstration only)
        SecretKeySpec key = generateKey(fsKey);

        // Decrypt the message
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }
}
