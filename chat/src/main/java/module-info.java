module net.sehic.cassandra.chat {
    requires javafx.controls;
    requires javafx.fxml;


    opens net.sehic.cassandra.chat to javafx.fxml;
    exports net.sehic.cassandra.chat;
}