module com.example.emotionalsongstest {
    requires javafx.controls;
    requires javafx.fxml;


    opens emotionalsongs to javafx.fxml;
    exports emotionalsongs;
}