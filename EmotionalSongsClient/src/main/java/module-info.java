module emotionalsongs.emotionalsongsclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.sql;


    opens emotionalsongs to javafx.fxml;
    exports emotionalsongs;
}