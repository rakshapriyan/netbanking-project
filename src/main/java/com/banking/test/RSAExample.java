//$Id$
package com.banking.test;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;

public class RSAExample {

    public static void main(String[] args) throws Exception {

        // Step 1: Generate RSA key pair (public and private keys)
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // 2048-bit key size
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Step 2: Convert text to bytes
        String text = "Hi Hello How are you";
        byte[] textBytes = text.getBytes();

        // Step 3: Encrypt the text using the public key
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(textBytes);

        // Step 4: Print the encrypted bytes as a sequence of numbers
        System.out.println("Encrypted Text: ");
        for (byte b : encryptedBytes) {
            System.out.print((int) b + " ");
        }
        System.out.println();

        // Step 5: Decrypt the encrypted text using the private key
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Step 6: Convert decrypted bytes back to string
        String decryptedText = new String(decryptedBytes);
        System.out.println("Decrypted Text: " + decryptedText);
    }
}

