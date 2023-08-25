package emotionalsongs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

        /*
         * Ti ho modificato questa linea di codice aggiungendoti il metodo limitTextInputLength perché forceTextInput era
         * stato ideato per aggiungere un constraint che impediva all'utente d'immettere caratteri numerici.
         * In questo contesto l'utente deve essere in grado d'inserire anche caratteri numerici all'interno dei
         * commenti (e.g. "Questa canzone mi ricorda l'estate del 2010...")
         */
        GUIUtilities.limitTextInputLength(notesArea, 256);

        scoreProgressBar.setProgress((double) score.get() / 5);

        // add listener to score
        score.addListener((observableValue, number, t1) -> {
            decreaseScoreBtn.setDisable(score.get() <= 1);
            increaseScoreBtn.setDisable(score.get() >= 5);
        });

        // add listener to notesArea
        notesArea.textProperty().addListener((observableValue, s, t1) -> numCharactersNotesLabel.setText(String.valueOf(notesArea.getText().length())));

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
        emotionPane.getStyleClass().add(emotion + "Pane");
        // set the bar color of scoreProgressBar
        scoreProgressBar.getStyleClass().add(emotion + "Bar");
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
