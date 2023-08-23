package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML private Label nameLabel;
    @FXML private Label surnameLabel;
    @FXML private Label emailLabel;
    @FXML private Label addressLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // TODO interrogare il db per farsi restituire le informazioni dell'utente

        // TODO settare le varie label

        // DEBUG TODO remove
        System.out.println("initialize user effettuato");

    }
}
