package net.sehic.cassandra.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends ChatApplication {

    ServerSocket ss;

    // Make sure the server socket gets closed.
    @Override
    public void stop() throws Exception {
        ss.close();
        super.stop();
    }
    protected Socket getSocket() throws IOException {
        ss = new ServerSocket(Custom.PORT); // Open server socket on port Custom.PORT
        Socket s = ss.accept(); // Listen for client connections.
        messages.appendText("Client connected.\n"); // Let user know that client connected.
        return s;
    }

    protected String getTitle() {
        return "Server";
    }

    protected String prefixMessage(String message) {
        return "Server: " + message;
    }
}

