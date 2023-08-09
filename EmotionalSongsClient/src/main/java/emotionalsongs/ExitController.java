package emotionalsongs;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

// TODO rename this class to Log-out controller
public class ExitController {

    @FXML private Button exitBtn;
    @FXML private Button annullaBtn;

    /**
     * TODO document
     */
    @FXML
    public void handleExitButtonAction(){

        // when pressed the Exit button, the user is redirected to login page
        Scene scene = GUIUtilities.getInstance().getScene("login.fxml");
        LoginController.clearFields();

        EmotionalSongsClient.setStage(scene, LoginController.WIDTH, LoginController.HEIGHT, false);
        // close the exit stage
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();

        EmotionalSongsClient.unexportClient();

    }

    /**
     * TODO document
     */
    @FXML
    public void handleAnnullaButtonAction(){
        // get the exit stage and close it
        Stage stage = (Stage) annullaBtn.getScene().getWindow();
        stage.close();
    }
}
