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
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller class controlling the window for adding emotions to the song.
 * <p>
 *     This class implements the JavaFX Initializable interface, which is automatically called when the associated FXML dialog
 *     is loaded. It Initialises the UI components.
 * </p>
 * <p>
 *     The class handles the configuration of the behaviour of the 'annullaBtn' and 'addEmotionsBtn' buttons to cancel or add emotions to the song, respectively.
 * </p>
 * @author <a href="https://github.com/samuk52">Corallo Samuele </a>
 */
public class AddEmotionsController implements Initializable {

    @FXML private Button addEmotionsBtn;
    @FXML private Button annullaBtn;
    @FXML private GridPane gridPane;
    @FXML private ScrollPane scrollPane;
    @FXML private Label songNameLabel;

    private EmotionController Amazement;
    private EmotionController Calmness;
    private EmotionController Joy;
    private EmotionController Nostalgia;
    private EmotionController Power;
    private EmotionController Sadness;
    private EmotionController Solemnity;
    private EmotionController Tenderness;
    private EmotionController Tension;

    private Canzone song;
    private ArrayList<Emozione> emozioniProvate;

    private int posInGridPane;
    private int row;



    /**
     * Initializes the window for adding emotions.
     * <p>
     *     This method is called automatically when the JavaFX dialog associated with this controller is loaded.
     *     It initializes various UI components and sets up their behavior.
     * </p>
     * @param url The URL of the FXML file.
     * @param resourceBundle The ResourceBundle associated with the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // setto l'auto resize dei componenti/elementi che si trovano nello scroll pane
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        row = 0;

        // create the emozioniProvateList
        emozioniProvate = new ArrayList<>();

        // add each emotion to gridPane
        Amazement = newEmotion(EmozioneEnum.AMAZEMENT);
        Calmness = newEmotion(EmozioneEnum.CALMNESS);
        Joy = newEmotion(EmozioneEnum.JOY);
        Nostalgia = newEmotion(EmozioneEnum.NOSTALGIA);
        Power = newEmotion(EmozioneEnum.POWER);
        Sadness = newEmotion(EmozioneEnum.SADNESS);
        Solemnity = newEmotion(EmozioneEnum.SOLEMNITY);
        Tenderness = newEmotion(EmozioneEnum.TENDERNESS);
        Tension = newEmotion(EmozioneEnum.TENSION);

    }

    /**
     * This method handles the behaviour of the {@link AddEmotionsController#addEmotionsBtn}.
     * <p>
     *     When the {@link AddEmotionsController#addEmotionsBtn} is clicked, each {@link Emozione} is added to the specific song.
     * </p>
     */
    public void inserisciEmozioniBrano(){

        // DEBUG TODO remove
        System.out.println("aggiungo alla canzone " + song + "\n" +
                "LE EMOZIONI: " + "\n" +
                "Amazement: " + Amazement.getScore() + ", " + Amazement.getNotes() + "\n" +
                "Calmness: " + Calmness.getScore() + ", " + Calmness.getNotes() + "\n" +
                "Joy: " + Joy.getScore() + ", " + Joy.getNotes() + "\n" +
                "Nostalgia: " + Nostalgia.getScore() + ", " + Nostalgia.getNotes() + "\n" +
                "Power: " + Power.getScore() + ", " + Power.getNotes() + "\n" +
                "Sadness: " + Sadness.getScore() + ", " + Sadness.getNotes() + "\n" +
                "Solemnity: " + Solemnity.getScore() + ", " + Solemnity.getNotes() + "\n" +
                "Tenderness: " + Tenderness.getScore() + ", " + Tenderness.getNotes() + "\n" +
                "Tension: " + Tension.getScore() + ", " + Tension.getNotes());
        // FINE DEBUG

        try{
            // add each emotion to the list emozioniProvate
            emozioniProvate.add(new Amazement(Amazement.getScore(), Amazement.getNotes()));
            emozioniProvate.add(new Solemnity(Solemnity.getScore(), Solemnity.getNotes()));
            emozioniProvate.add(new Tenderness(Tenderness.getScore(), Tenderness.getNotes()));
            emozioniProvate.add(new Nostalgia(Nostalgia.getScore(), Nostalgia.getNotes()));
            emozioniProvate.add(new Calmness(Calmness.getScore(), Calmness.getNotes()));
            emozioniProvate.add(new Power(Power.getScore(), Power.getNotes()));
            emozioniProvate.add(new Joy(Joy.getScore(), Joy.getNotes()));
            emozioniProvate.add(new Tension(Tension.getScore(), Tension.getNotes()));
            emozioniProvate.add(new Sadness(Sadness.getScore(), Sadness.getNotes()));

            // insert the emotions to the DB
            boolean emotionRegistrationCheck = EmotionalSongs.repo.registerEmotions(emozioniProvate, song.getSongUUID(), EmotionalSongsClientController.getUsername());

            if(emotionRegistrationCheck) {
                // add emotions to the hashMap emotionsSongs contained in the SelectedPlaylist class
                SelectedPlaylistController.addEmotionsSong(song.getSongUUID(), emozioniProvate);

                // set that now the song has an emotions
                SelectedPlaylistController.getSongController(posInGridPane).newEmotionsAdded();

            }else{

                try {
                    // opened the insertionFailedStage informing the user that the insertion of emotions into the song had failed.
                    Stage insertionFailedStage = new Stage();

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("insertionFailed.fxml"));

                    insertionFailedStage.setScene(new Scene(fxmlLoader.load()));

                    InsertionFailedController insertionFailedController = fxmlLoader.getController();
                    insertionFailedController.setErrorLabel("Impossibile aggiungere le emozioni alla canzone");

                    insertionFailedStage.initStyle(StageStyle.UNDECORATED);
                    insertionFailedStage.initModality(Modality.APPLICATION_MODAL);
                    insertionFailedStage.setResizable(false);
                    insertionFailedStage.show();
                }catch (IOException e){
                    //
                }
            }

        }catch (RemoteException e){

            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();

        }

        // close the stage
        GUIUtilities.closeStage(addEmotionsBtn);
    }

    /**
     * Method that handles the behaviour of the {@link AddEmotionsController#annullaBtn}.
     * <p>
     *     When the {@link AddEmotionsController#annullaBtn} is clicked, the operation is cancelled and the method closes the window containing the button.
     * </p>
     */
    public void handleAnnullaButtonAction(){
        // close the stage
        GUIUtilities.closeStage(annullaBtn);
    }

    /**
     * Method which loads and adds the specific {@link Emozione} passed as a parameter to the Add Emotion window.
     *
     * @param emotion representing the emotion to be loaded and added to the Add Emotions window.
     * @return {@link EmotionController} of the loaded emotion, which will be used to get the score and notes of the emotion.
     */
    private EmotionController newEmotion(EmozioneEnum emotion){

        EmotionController emotionController = null;

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("emotion.fxml"));
            Node emotion_pane = fxmlLoader.load();

            emotionController = fxmlLoader.getController();
            emotionController.setEmotion(emotion);

            gridPane.add(emotion_pane, 0, row);
            GridPane.setMargin(emotion_pane, new Insets(10));

            row ++;

        }catch(Exception e){
            e.printStackTrace();
        }

        return emotionController;

    }

    /**
     * Method that sets the song to which emotions will be added.
     *
     * @param song the song to which emotions will be added.
     * @param posInGridPane indicates the position of the song in the GridPane contained in the {@link SelectedPlaylistController},
     *                      this information will be used to update the song's 'multipleBtn' button, contained in the {@link SongPlaylistController},
     *                      when emotions are added to the song.
     */
    public void setSong(Canzone song, int posInGridPane){
        this.song = song;
        this.posInGridPane = posInGridPane;

        // set text of songNameLabel
        songNameLabel.setText(song.getTitolo()  + " (" + song.getAutore() + " - " + song.getAnno() + ")");
    }
}
