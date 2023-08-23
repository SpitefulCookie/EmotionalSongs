package emotionalsongs;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;


// TODO rename this class to Log-out controller
public class ExitController {

    @FXML private Button exitBtn;
    @FXML private Button annullaBtn;
    /**
     * TODO document
     */
    @FXML
    public void handleExitButtonAction(){

        // When pressed the Exit button, the user is redirected to login page
        Scene scene = GUIUtilities.getInstance().getScene("login.fxml");
        LoginController.clearFields();

        EmotionalSongsClient.setStage(scene, LoginController.WIDTH, LoginController.HEIGHT, false);
        // close the exit stage
        GUIUtilities.closeStage(exitBtn);

        EmotionalSongsClient.disconnectClient();

    }

    /**
     * TODO document
     */
    @FXML
    public void handleAnnullaButtonAction(){
        // get the exit stage and close it
        GUIUtilities.closeStage(exitBtn);
    }
}
