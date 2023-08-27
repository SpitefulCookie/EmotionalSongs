module emotionalsongs.emotionalsongsserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.rmi;

    opens emotionalsongs to javafx.fxml;
    exports emotionalsongs;
}