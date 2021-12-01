package net.sehic.cassandra.chat;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

abstract class ChatApplication extends Application {
// Client and Server are similar. This class implements the things that is are the same between them.

// The following three abstract methods are the only things that are different between Client and Server.
    protected abstract Socket getSocket() throws IOException;
    protected abstract String getTitle();
    protected abstract String prefixMessage(String message);

    ConnectionThread connectionThread;
    DataInputStream dis;
    DataOutputStream dos;
    TextArea messages = new TextArea(); // All messages will be displayed here.

// The main method to start the application.
    public static void main(String[] args) {
        launch(args);
    }

// JavaFX lifecycle method that is called before the application is opened.
    @Override
    public void init() throws Exception {
        super.init(); // Do whatever JavaFX Application normally does when init() is called.
        connectionThread = new ConnectionThread(); // ConnectionThread defined below.
        connectionThread.start(); // Start the ConnectionThread to manage connection from Client to Server.
    }

// JavaFX lifecycle method that is called when the application is started.
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(getTitle()); // Set window title.
        primaryStage.setScene(new Scene(createContent())); // Create window content.
        primaryStage.show(); // Show window.
    }

// JavaFX lifecycle method that is called before the application exits.
    @Override
    public void stop() throws Exception {
        connectionThread.interrupt(); // Make sure the connection thread doesn't run forever.
        dos.close(); // Close output stream.
        dis.close(); // Close input stream.
        super.stop();
    }

    private Parent createContent() {
        messages.setPrefHeight(350);
        messages.setEditable(false); // Already sent or received messages cannot be edited.
        TextField input = new TextField(); // Input field for new messages.
        input.setOnAction(event -> { // When user hits ENTER.
            String message = input.getText();
            try {
                dos.writeUTF(Custom.encrypt(prefixMessage(message))); // Encrypt message and send it.
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            messages.appendText(message + "\n"); // Display the message that was sent.
            input.clear(); // Clear input field.
        });
        VBox root = new VBox(20, messages, input); // Simple layout.
        root.setPrefSize(500, 400);
        return root;
    }

    private class ConnectionThread extends Thread {
        @Override
        public void run() {
                try (Socket s = getSocket()) { // Server waits for client connection. Client connects to server. (Server must be running before client is started.)
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                    if (Custom.HANDSHAKE) { // If handshake needed for encryption, send handshake value before reading handshake value to avoid deadlock.
                        dos.writeUTF(Custom.getHandshakeValue());
                        Custom.setHandshakeValue(dis.readUTF());
                    }
                    while (!Thread.currentThread().isInterrupted()) { // Continue until window is closed. stop() is called.
                        String message = dis.readUTF(); // Read next message.
                        messages.appendText(Custom.decrypt(message) + "\n"); // Decrypt message and display it.
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
        }
    }
}
