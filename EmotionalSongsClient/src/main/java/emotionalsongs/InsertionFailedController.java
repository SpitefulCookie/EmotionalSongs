package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller class that handles the {@link Stage} (window) that is displayed when the database entry of: playlists,
 * songs in a playlist and emotions in a song, fails.
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class InsertionFailedController {

    @FXML private Button okBtn;
    @FXML private Label errorLabel;

    /**
     * Method that handles the behaviour of the {@link InsertionFailedController#okBtn}.
     * <p>
     *     When the {@link InsertionFailedController#okBtn} is clicked, the method closes the window containing the button.
     * </p>
     */
    @FXML
    public void handleOkButtonAction(){
        GUIUtilities.closeStage(okBtn);
    }


    /**
     * Method that sets the error message that will be displayed in the window.
     *
     * @param errorMessage Is the error message to be set.
     */
    protected void setErrorLabel(String errorMessage){
        /*
        metodo che setta il messaggio di errore
         */
        errorLabel.setText(errorMessage);
    }
}
