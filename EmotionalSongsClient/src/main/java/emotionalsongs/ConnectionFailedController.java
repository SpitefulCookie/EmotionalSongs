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
        GUIUtilities.closeStage(tryAgainButton);
        EmotionalSongsClient.initializeServerConnection(false);
    }


    /**
     * TODO document
     */
    @FXML
    public void handleExitButtonAction(){
        // close the connection failed stage
        GUIUtilities.closeStage(exitButton);
    }

}
