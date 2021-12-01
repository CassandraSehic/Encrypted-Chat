package net.sehic.cassandra.chat;

import java.io.IOException;
import java.net.Socket;

public class Client extends ChatApplication {

    protected Socket getSocket() throws IOException {
        Socket s = new Socket(Custom.HOST, Custom.PORT); // Connect to server on Custom.HOST and Custom.PORT
        messages.appendText("Connected to server.\n"); // Let user know that connection to server was made.
        return s;
    }

    protected String getTitle() {
        return "Client";
    }

    protected String prefixMessage(String message) {
        return "Client: " + message;
    }
}
