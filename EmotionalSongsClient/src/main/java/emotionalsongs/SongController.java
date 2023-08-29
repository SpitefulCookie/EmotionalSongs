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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller class that manages the single song that is displayed in the {@link Node},
 * managed by the {@link SearchController} class, when the user searches for a song.
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class SongController implements Initializable {

    private static final int BUTTON_MAX_WIDTH = 200;
    private static final int BUTTON_MIN_WIDTH = 45;

    @FXML
    private Label songNameLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Button addToPlaylistBtn;
    @FXML
    private Button viewEmotionsBtn;

    private Canzone song;

    /**
     * Initializes the controller when the corresponding {@link FXML} is loaded.
     * <p>
     *     When invoked, it disables and makes the {@link SongController#addToPlaylistBtn} not visible if
     *     the user is logged in as a <Strong>guest</Strong>.
     * </p>
     *
     * @param url The URL to the FXML document (unused).
     * @param resourceBundle The resources used for localization (unused).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        verifico se l'utente che è entrato nel client è un utente guest ovvero non regitrato, se così
        fosse vado a disabilitare e a rendere invisibile il addToPlaylistButton, in quanto, dato che
        esso non può creare playlist non potrà neanche aggiungere canzoni ad esse.
         */
        if(EmotionalSongsClientController.getIsGuest()){ // se è un utente guest
            addToPlaylistBtn.setDisable(true);
            addToPlaylistBtn.setVisible(false);
        }
    }

    /**
     * Method that handles the behaviour of the {@link SongController#addToPlaylistBtn}.
     * <p>
     *     When {@link SongController#addToPlaylistBtn} is clicked, the {@link Stage} (window) controlled
     *     by the class {@link AddToPlaylistController} opens, allowing the song to be added to playlists.
     * </p>
     */
    @FXML
    public void handleAddToPlaylistButtonAction(){

        try{

            Stage addToPlaylistStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addToPlaylist.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            AddToPlaylistController addToPlaylistController = fxmlLoader.getController();
            addToPlaylistController.setSongToAdd(song);

            addToPlaylistStage.setScene(scene);
            addToPlaylistStage.initStyle(StageStyle.UNDECORATED);
            addToPlaylistStage.initModality(Modality.APPLICATION_MODAL);
            addToPlaylistStage.setResizable(false);
            addToPlaylistStage.show();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method that handles the behaviour of the {@link SongController#viewEmotionsBtn}.
     * <p>
     *     When {@link SongController#viewEmotionsBtn} is clicked, the {@link Node} controlled
     *     by the class {@link ViewReportEmotionsController} opens, allowing to view a summary of the emotions
     *     associated with the specific song.
     * </p>
     */
    @FXML
    public void visualizzaEmozioneBrano(){

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewReportEmotions.fxml"));
            Node emotions_pane = fxmlLoader.load();

            ViewReportEmotionsController viewReportEmotionsController = fxmlLoader.getController();
            boolean checkEmotionsAverageRetrieved = viewReportEmotionsController.setEmotions(song, true);

            if(checkEmotionsAverageRetrieved) {
                EmotionalSongsClientController.setDynamicPane(emotions_pane);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method that manages the display of the {@link SongController#addToPlaylistBtn} when the mouse moved over it.
     * <p>
     *     When the mouse is moved over the {@link SongController#addToPlaylistBtn}, the button text
     *     is set and its width is changed.
     * </p>
     */
    @FXML
    public void handleAddToPlaylistButtonMouseMovedAction(){
        /*
         quando il mouse passa sopra al addPlaylistBtn viene settato il testo del pulsante e viene reimpostata
         la sua lunghezza, così da creare una specie di animazione
         */
        addToPlaylistBtn.setText("Aggiungi alla Playlist");
        addToPlaylistBtn.setMaxWidth(BUTTON_MAX_WIDTH);
    }

    /**
     * Method that manages the display of the {@link SongController#viewEmotionsBtn} when the mouse moved over it.
     * <p>
     *     When the mouse is moved over the {@link SongController#viewEmotionsBtn}, the button text
     *     is set and its width is changed.
     * </p>
     */
    @FXML
    public void handleViewEmotionsButtonMouseMovedAction(){
        /*
         quando il mouse passa sopra al viewEmotionsBtn viene settato il testo del pulsante e viene reimpostata
         la sua lunghezza, così da creare una specie di animazione
         */
        viewEmotionsBtn.setText("Visualizza report emozioni");
        viewEmotionsBtn.setMaxWidth(BUTTON_MAX_WIDTH);
    }

    /**
     * Method that manages the display of the buttons: {@link SongController#addToPlaylistBtn} and {@link SongController#viewEmotionsBtn}
     * when the mouse is exited from them.
     * <p>
     *     When the mouse leaves the buttons: {@link SongController#addToPlaylistBtn} and {@link SongController#viewEmotionsBtn},
     *     they are set to their initial state.
     * </p>
     */
    @FXML
    public void handleMouseExitedAction(){
        /*
        gestisce l'animazione di quando il mouse finisce di essere sopra a uno dei due bottoni
         */
        addToPlaylistBtn.setText("");
        viewEmotionsBtn.setText("");
        addToPlaylistBtn.setMaxWidth(BUTTON_MIN_WIDTH);
        viewEmotionsBtn.setMaxWidth(BUTTON_MIN_WIDTH);
    }

    /**
     * Method that sets the song.
     *
     * @param song Represents the song to be set.
     */
    public void setData(Canzone song){
        setSongNameLabel(song.getTitolo());
        setInfoLabel(song.getAutore() + " - " + " (" + song.getAnno() + ")");
        this.song = song;
    }

    /**
     * Method that sets the name of the song.
     *
     * @param songName Represents the name of the song.
     */
    public void setSongNameLabel(String songName){
        this.songNameLabel.setText(songName);
    }

    /**
     * Method that sets the information of the song, that is: the author of the song and the year it was produced.
     *
     * @param songInfo Represents song information.
     */
    public void setInfoLabel(String songInfo){
        this.infoLabel.setText(songInfo);
    }

}
