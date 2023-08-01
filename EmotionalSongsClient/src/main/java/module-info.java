module emotionalsongs.emotionalsongsclient{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;


    opens emotionalsongs to javafx.fxml;
    exports emotionalsongs;
}