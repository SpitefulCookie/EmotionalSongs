package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * The controller class that handles the {@link Stage} that is displayed when the 'exitBtn' button is clicked
 * on the main view (handled by the {@link EmotionalSongsClientController}).
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class ExitController {

    @FXML private Button exitBtn;
    @FXML private Button annullaBtn;

    /**
     * Method that handles the behaviour of the {@link ExitController#exitBtn}.
     * <p>
     *     When {@link ExitController#exitBtn} is clicked, application terminates.
     * </p>
     */
    @FXML
    public void handleExitButtonAction(){

        // close the exitStage
        GUIUtilities.closeStage(exitBtn);

        // close the main stage
        EmotionalSongsClient.getStage().close();

    }

    /**
     * Method that handles the behaviour of the {@link ExitController#annullaBtn}.
     * <p>
     *     When the {@link ExitController#annullaBtn} is clicked, the method closes the window containing the button.
     * </p>
     */
    @FXML
    public void handleAnnullaButtonAction(){
        // get the exit stage and close it
        GUIUtilities.closeStage(exitBtn);
    }
}
