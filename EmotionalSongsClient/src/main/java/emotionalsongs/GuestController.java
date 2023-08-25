package emotionalsongs;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GuestController {

    @FXML private Button loginBtn;
    @FXML private Button registrationBtn;

    /**
     * TODO document
     */
    @FXML
    public void handleLoginButtonAction(){

        // open the login stage
        Scene scene = GUIUtilities.getInstance().getScene("login.fxml");
        LoginController.clearFields();

        EmotionalSongsClient.setStage(scene, LoginController.WIDTH, LoginController.HEIGHT, false);

        EmotionalSongsClient.disconnectClient(); // TODO verificare se è corretto mettere questa istruzione ---> chiedere a mattia
                                                 // Risposta: Si, il client quando si trova nella schermata di login non ha a tutti gli effetti stabilito
                                                 // una connessione con il server (invocazione del metodo registerClient() ) questo avverrà solamente dopo
                                                 // il login oppure dopo aver pigiato il tasto di continue as guest/registrazione.
    }

    /**
     * TODO document
     */
    @FXML
    public void handleRegistrationButtonAction(){

        // TODO anche in questo caso verificare se è coretto mantenere questo if, ----> chiedere a mattia
        // Risposta: No, non serve. La connessione con il server è già stata instaurata in questo punto ed il client
        // è già registrato con il server

        // open the registration stage
        EmotionalSongsClient.setStage(GUIUtilities.getInstance().getScene("UserRegistration.fxml"), UserRegistrationController.WIDTH, UserRegistrationController.HEIGHT, true);
        EmotionalSongsClient.getStage().show();

    }

}
