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

    }

    /**
     * TODO document
     */
    @FXML
    public void handleRegistrationButtonAction(){

        // TODO anche in questo caso verificare se è coretto mantenere questo if, ----> chiedere a mattia

        // open the registration stage
        if(EmotionalSongsClient.isConnectionInitialized) {
            EmotionalSongsClient.registerClient();
            EmotionalSongsClient.setStage(GUIUtilities.getInstance().getScene("UserRegistration.fxml"), UserRegistrationController.WIDTH, UserRegistrationController.HEIGHT, true);
            EmotionalSongsClient.getStage().show();
        } else{

            EmotionalSongsClient.initializeServerConnection(false);

        }

    }

}
