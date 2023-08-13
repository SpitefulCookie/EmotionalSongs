package emotionalsongs;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.stage.Stage;


public class ConnectionFailedController {

    @FXML private Button tryAgainButton;
    @FXML private Button exitButton;

    /**
     * TODO document
     */
    @FXML
    public void handleTryAgainButtonAction(){
        closeConnectionFailedStage(tryAgainButton);
        EmotionalSongsClient.initializeServerConnection(false);
    }


    /**
     * TODO document
     */
    @FXML
    public void handleExitButtonAction(){
        // close the connection failed stage
        closeConnectionFailedStage(exitButton);
        // close the main stage
        //EmotionalSongsClient.getStage().close(); // TODO togliere
    }

    /**
     * TODO document
     * @param button
     */
    public void closeConnectionFailedStage(Button button){
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
