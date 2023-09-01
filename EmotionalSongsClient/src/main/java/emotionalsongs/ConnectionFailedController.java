package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.stage.Stage;


/**
 * The controller class that handles the display of the {@link Stage} (window) informing the user that the
 * connection with the server has failed, this window is displayed when a {@link java.rmi.RemoteException} is generated.
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 * @author <a href="https://github.com/SpitefulCookie"> Della Chiesa Mattia</a>
 */
public class ConnectionFailedController {

    @FXML private Button tryAgainButton;
    @FXML private Button exitButton;

    /**
     * Method that handles the behaviour of the {@link ConnectionFailedController#tryAgainButton}.
     * <p>
     *     When the {@link ConnectionFailedController#tryAgainButton} is clicked, an attempt is made to
     *     reconnect with the server.
     * </p>
     */
    @FXML
    public void handleTryAgainButtonAction(){
        GUIUtilities.closeStage(tryAgainButton);
        EmotionalSongs.initializeServerConnection(false);
    }


    /**
     * Method that handles the behaviour of the {@link ConnectionFailedController#exitButton}.
     * <p>
     *     When the {@link ConnectionFailedController#exitButton} is clicked, the method closes the window containing the button.
     * </p>
     */
    @FXML
    public void handleExitButtonAction(){
        // close the connection failed stage
        GUIUtilities.closeStage(exitButton);
    }

}
