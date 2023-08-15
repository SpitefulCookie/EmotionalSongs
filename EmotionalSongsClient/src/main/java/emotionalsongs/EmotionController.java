package emotionalsongs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class EmotionController implements Initializable{

    @FXML private VBox emotionPane;
    @FXML private Label emotionNameLabel;
    @FXML private Label descriptionLabel;
    @FXML private ProgressBar scoreProgressBar;
    @FXML private Button increaseScoreBtn;
    @FXML private Button decreaseScoreBtn;
    @FXML private Label scoreLabel;
    @FXML private TextArea notesArea;
    @FXML private Label numCharactersNotesLabel;

    private IntegerProperty score;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // create new Score
        score = new SimpleIntegerProperty();
        score.set(1);

        // aggiungo il limite di soli 256 caratteri scrivibili nelle note
        GUIUtilities.forceTextInput(notesArea, 256);

        scoreProgressBar.setProgress((double) score.get() / 5);

        // add listener to score
        score.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(score.get() <= 1){
                    decreaseScoreBtn.setDisable(true);
                }else{
                    decreaseScoreBtn.setDisable(false);
                }

                if(score.get() >= 5){
                    increaseScoreBtn.setDisable(true);
                }else{
                    increaseScoreBtn.setDisable(false);
                }
            }
        });

        // add listener to notesArea
        notesArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                numCharactersNotesLabel.setText(String.valueOf(notesArea.getText().length()));
            }
        });

    }

    /**
     * TODO document
     */
    @FXML
    public void handleDecreaseScoreButtonAction(){
        // update the score, the scoreLabel and the scoreProgressBar
        score.set(score.get() - 1);
        scoreLabel.setText(String.valueOf(score.get()));
        scoreProgressBar.setProgress((double) score.get() / 5);
    }

    /**
     * TODO document
     */
    @FXML
    public void handleIncreaseScoreButtonAction(){
        // update the score, the scoreLabel and the scoreProgressBar
        score.set(score.get() + 1);
        scoreLabel.setText(String.valueOf(score.get()));
        scoreProgressBar.setProgress((double) score.get() / 5);
    }

    /**
     * TODO document
     * @param emotion
     */
    public void setEmotion(EmozioneEnum emotion){
        // set emotion name and description
        emotionNameLabel.setText(emotion.toString());
        descriptionLabel.setText(emotion.description());
        // set style of emotionPane
        emotionPane.getStyleClass().add(emotion.toString() + "Pane");
        // set the bar color of scoreProgressBar
        scoreProgressBar.getStyleClass().add(emotion.toString() + "Bar");
    }

    /**
     * TODO document
     * @return
     */
    public int getScore(){
        return score.get();
    }

    /**
     * TODO document
     * @return
     */
    public String getNotes(){
        return notesArea.getText();
    }

}
