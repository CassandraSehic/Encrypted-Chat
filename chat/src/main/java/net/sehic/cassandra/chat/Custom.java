package net.sehic.cassandra.chat;

class Custom {
    static final String HOST = "127.0.0.1"; // Only the client uses the HOST.
    static final int PORT = 1234; // Port on which server opens socket and client connects.

    static final boolean HANDSHAKE = true;

    static void setHandshakeValue(String value) {
        RSAEncryption.setHandshakeValue(value);
    }

    static String getHandshakeValue() {
        return RSAEncryption.getHandshakeValue();
    }

    static String encrypt(String message) {
        return RSAEncryption.encrypt(message);
    }

    static String decrypt(String message) {
        return RSAEncryption.decrypt(message);
    }
}
