module emotionalsongs.emotionalsongsserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.rmi;
    requires spring.security.crypto;


    opens emotionalsongs to javafx.fxml;
    exports emotionalsongs;
}