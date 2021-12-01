package net.sehic.cassandra.chat;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class RSAEncryption {
    // https://www.geeksforgeeks.org/asymmetric-encryption-cryptography-in-java/

    private static final String ALGORITHM = "RSA";
    private static final KeyPair keyPair = generateRSAKeyPair();
    private static byte[] publicKey;

    private static KeyPair generateRSAKeyPair() {
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(1);
        }
        keyPairGenerator.initialize(2048, secureRandom);
        return keyPairGenerator.generateKeyPair();
    }

    static void setHandshakeValue(String value) {
        publicKey = Base64.getDecoder().decode(value);
    }

    static String getHandshakeValue() {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    static String encrypt(String message) {
        try {
            PublicKey key = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(publicKey));
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes()));
        } catch (Exception e) {
            System.out.println("Error while encrypting:\n" + e);
            e.printStackTrace();
        }
        return null;
    }

    static String decrypt(String message) {
        try {
            PrivateKey key = KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded()));
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(message.getBytes())));
        } catch (Exception e) {
            System.out.println("Error while decrypting:\n" + e);
            e.printStackTrace();
        }
        return null;
    }
}
