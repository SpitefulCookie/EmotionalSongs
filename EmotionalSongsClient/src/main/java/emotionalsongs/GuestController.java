package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * The controller class that manages the {@link javafx.scene.Node} that is displayed when the user, logged in as a <Strong>guest</Strong>,
 * clicks the "userBtn" button in the main view (managed by the {@link EmotionalSongsClientController}).
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class GuestController {

    @FXML private Button loginBtn;
    @FXML private Button registrationBtn;

    /**
     * Method that handles the behaviour of the {@link GuestController#loginBtn}.
     * <p>
     *     When {@link GuestController#loginBtn} is clicked, the user is redirected to the login window.
     * </p>
     */
    @FXML
    public void handleLoginButtonAction(){

        // open the login stage
        Scene scene = GUIUtilities.getInstance().getScene("login.fxml");
        LoginController.clearFields();

        EmotionalSongs.setStage(scene, LoginController.WIDTH, LoginController.HEIGHT, false);

        //EmotionalSongs.disconnectClient();
    }

    /**
     * Method that handles the behaviour of the {@link GuestController#registrationBtn}.
     * <p>
     *     When {@link GuestController#registrationBtn} is clicked, the user is redirected to the registration window.
     * </p>
     */
    @FXML
    public void handleRegistrationButtonAction(){

        // open the registration stage
        EmotionalSongs.setStage(GUIUtilities.getInstance().getScene("UserRegistration.fxml"), UserRegistrationController.WIDTH, UserRegistrationController.HEIGHT, true);
        EmotionalSongs.getStage().show();

    }

}
