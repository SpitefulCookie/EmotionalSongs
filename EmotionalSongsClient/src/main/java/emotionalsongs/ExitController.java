package emotionalsongs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

        try {
            // when pressed the Exit button, the user is redirected to login page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            EmotionalSongsClient.setStage(scene, LoginController.WIDTH, LoginController.HEIGHT, false);

            // close the exit stage
            Stage stage = (Stage) exitBtn.getScene().getWindow();
            stage.close();

            EmotionalSongsClient.unexportClient();

        }catch (IOException e){
            e.printStackTrace();
        }
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
