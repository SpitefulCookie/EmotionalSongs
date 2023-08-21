package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
     * TODO document
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // setto l'auto resize dei componenti/elementi che si trovano nello scroll pane
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        row = 0;

        // create the emozioniProvateList
        emozioniProvate = new ArrayList<Emozione>();

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
     * TODO document
     */
    public void handleAddEmotionsButtonAction(){
        // TODO implementare comportamento

        // DEBUG
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

        // TODO aggiungere le emozioni alla canzone
        try{
            // add each emotion to emozioniProvate list
            emozioniProvate.add(new Amazement(Amazement.getScore(), Amazement.getNotes()));
            emozioniProvate.add(new Calmness(Calmness.getScore(), Calmness.getNotes()));
            emozioniProvate.add(new Joy(Joy.getScore(), Joy.getNotes()));
            emozioniProvate.add(new Nostalgia(Nostalgia.getScore(), Nostalgia.getNotes()));
            emozioniProvate.add(new Power(Power.getScore(), Power.getNotes()));
            emozioniProvate.add(new Sadness(Sadness.getScore(), Sadness.getNotes()));
            emozioniProvate.add(new Solemnity(Solemnity.getScore(), Solemnity.getNotes()));
            emozioniProvate.add(new Tenderness(Tenderness.getScore(), Tenderness.getNotes()));
            emozioniProvate.add(new Tension(Tension.getScore(), Tension.getNotes()));

            // insert the emotions to the DB
            EmotionalSongsClient.repo.registerEmotions(emozioniProvate, song.getSongUUID(), EmotionalSongsClientController.getUsername());

            /*
            TODO aggiungere la canzone e le emozioni nella hashMap emotionsSongs contenuta nella classe
                SelectedPlaylist
             */

            // add emotions to the hashMap emotionsSongs contained in the SelectedPlaylist class
            SelectedPlaylistController.addEmotionsSong(song.getSongUUID(), emozioniProvate);

            // set that now the song has an emotions
            SelectedPlaylistController.getSongController(posInGridPane).newEmotionsAdded();

        }catch (RemoteException e){
            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // close the stage
        closeStage(addEmotionsBtn);
    }

    /**
     * TODO document
     */
    public void handleAnnullaButtonAction(){
        // close the stage
        closeStage(annullaBtn);
    }

    /**
     * TODO document
     * @param emotion
     * @return
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
     * TODO document
     * @param song
     */
    public void setSong(Canzone song, int posInGridPane){
        this.song = song;
        this.posInGridPane = posInGridPane;

        // set text of songNameLabel
        songNameLabel.setText(song.getTitolo());
    }

    /**
     * TODO document
     * @param button
     */
    private void closeStage(Button button){
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

}
