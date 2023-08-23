package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.rmi.RemoteException;
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

    public static String retrieveUserData() { // TODO static aggiunto solo per effettuare dei test, vedi tu la segnatura del metodo
        try {
            String data= AuthManager.decryptRSA(EmotionalSongsClient.auth.getUserData("test", EmotionalSongsClient.ping.getPublicKey()),EmotionalSongsClient.ping.getPrivateKey()); // TODO rimpiazzare 'test' con l'userId dell'utente loggato
            return data.substring(1,data.length()-1);
        } catch (RemoteException e) {
            e.printStackTrace(); // TODO decidi tu come gestire l'eccezione
            return "";
        }
    }
}
