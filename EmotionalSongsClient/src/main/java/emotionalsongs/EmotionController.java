package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller class that manages the single emotion that will be displayed in the {@link Stage}
 * managed by the class {@link AddEmotionsController}.
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
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

    /**
     * Initialises the GUI components and sets the initial state of the emotion.<br><br>
     *
     * This method is called automatically when the FXML file associated with this controller is loaded.
     * This method when called:
     * <ol>
     *     <li>
     *         creates a new {@link IntegerProperty} representing the emotion score, to which is subsequently
     *         added an {@link javafx.beans.value.ChangeListener} that takes care of disabling or enabling
     *         the buttons: {@link EmotionController#decreaseScoreBtn}, {@link EmotionController#increaseScoreBtn}.
     *     </li>
     *     <li>
     *         Initialises the {@link EmotionController#scoreProgressBar}.
     *     </li>
     *     <li>
     *         Adds a text limiter to the {@link EmotionController#notesArea}.
     *     </li>
     * </ol>
     *
     */
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
     * Method that handles the behaviour of the {@link EmotionController#decreaseScoreBtn}.
     * <p>
     *     When {@link EmotionController#decreaseScoreBtn} is clicked, the {@link EmotionController#score} is decreased by 1
     *     and the {@link EmotionController#scoreProgressBar} is updated to the score value.
     * </p>
     */
    @FXML
    public void handleDecreaseScoreButtonAction(){
        // update the score, the scoreLabel and the scoreProgressBar
        score.set(score.get() - 1);
        scoreLabel.setText(String.valueOf(score.get()));
        scoreProgressBar.setProgress((double) score.get() / 5);
    }

    /**
     * Method that handles the behaviour of the {@link EmotionController#increaseScoreBtn}.
     * <p>
     *     When {@link EmotionController#increaseScoreBtn} is clicked, the {@link EmotionController#score} is incremented by 1
     *     and the {@link EmotionController#scoreProgressBar} is updated to the score value.
     * </p>
     */
    @FXML
    public void handleIncreaseScoreButtonAction(){
        // update the score, the scoreLabel and the scoreProgressBar
        score.set(score.get() + 1);
        scoreLabel.setText(String.valueOf(score.get()));
        scoreProgressBar.setProgress((double) score.get() / 5);
    }

    /**
     * Method that sets emotion.
     * <p>
     *     This method takes care of setting the name of the emotion, its description and changing the colour
     *     of the border of the {@link EmotionController#emotionPane} and the colour of the {@link EmotionController#scoreProgressBar}
     *     with the specific colour of the given emotion.
     * </p>
     *
     * @param emotion represents the emotion to be set.
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
     * Method that returns the emotion score.
     *
     * @return The {@link EmotionController#score} value.
     */
    public int getScore(){
        return score.get();
    }

    /**
     * Method that returns the notes of emotion.
     *
     * @return The text contained in the {@link EmotionController#notesArea}.
     */
    public String getNotes(){
        return notesArea.getText();
    }

}
