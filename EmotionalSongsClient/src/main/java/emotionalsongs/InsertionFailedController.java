package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InsertionFailedController implements Initializable {

    @FXML private Button okBtn;
    @FXML private Label errorLabel;

    private static Label errorLabel_;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel_ = errorLabel;
    }

    /**
     * TODO document
     */
    @FXML
    public void handleOkButtonAction(){
        closeStage(okBtn);
    }

    /**
     * TODO document
     * @param button
     */
    private void closeStage(Button button){
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    /**
     * TODO document
     * @param errorMessage
     */
    protected static void setErrorLabel(String errorMessage){
        /*
        metodo che setta il messaggio di errore
         */
        errorLabel_.setText(errorMessage);
    }
}
