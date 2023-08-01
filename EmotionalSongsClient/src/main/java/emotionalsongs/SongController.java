package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/*
TODO :
    1- Implementare il comportamente (cosa deve succedere) quando i bottoni AddToPlaylist e viewEmotions vengono
       premuti
    2- modificare immagine pulsante addToPlaylistBtn ?
 */
public class SongController implements Initializable {

    private static final int BUTTON_MAX_WIDTH = 200;
    private static final int BUTTON_MIN_WIDTH = 50;

    @FXML
    private Label songNameLabel;
    @FXML
    private Label authorNameLabel;
    @FXML
    private Button addToPlaylistBtn;
    @FXML
    private Button viewEmotionsBtn;

    /**
     * TODO document
     * @param url
     * @param resourceBundle
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
     * TODO document
     */
    @FXML
    public void handleAddToPlaylistButtonAction(){
        /*
        implementare cosa deve accadare quando quando viene premuto questo pulsante
         */
    }

    /**
     * TODO document
     */
    @FXML
    public void handleViewEmotionsButtonAction(){
        /*
        implementare cosa deve accadare quando quando viene premuto questo pulsante
         */
    }

    /**
     * TODO document
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
     * TODO docunent
     */
    @FXML
    public void handleAddToPlaylistButtonMouseExitedAction(){
        /*
        quando il mouse è sopra al pulsante addPlaylistBtn ed 'esce' da esso (quindi il cursore non sarà più
        sul pulsante) il testo del pulsante viene resettato e viene resettata anche la sua lunghezza,
        questo sempre per creare una specie di animazione.
         */
        addToPlaylistBtn.setText("");
        addToPlaylistBtn.setMaxWidth(BUTTON_MIN_WIDTH);
    }

    /**
     * TODO docunent
     */
    @FXML
    public void handleViewEmotionsButtonMouseMovedAction(){
        /*
         quando il mouse passa sopra al viewEmotionsBtn viene settato il testo del pulsante e viene reimpostata
         la sua lunghezza, così da creare una specie di animazione
         */
        viewEmotionsBtn.setText("Visualizza emozioni");
        viewEmotionsBtn.setMaxWidth(BUTTON_MAX_WIDTH);
    }

    /**
     * TODO docunent
     */
    @FXML
    public void handleViewEmotionsButtonMouseExitedAction(){
        /*
        quando il mouse è sopra al pulsante viewEmotionsBtn ed 'esce' da esso (quindi il cursore non sarà più
        sul pulsante) il testo del pulsante viene resettato e viene resettata anche la sua lunghezza,
        questo sempre per creare una specie di animazione.
         */
        viewEmotionsBtn.setText("");
        viewEmotionsBtn.setMaxWidth(BUTTON_MIN_WIDTH);

    }

    /**
     *
     * @param songName
     * @param authorName
     */
    public void setData(String songName, String authorName){
        setSongNameLabel(songName);
        setAuthorNameLabel(authorName);
    }

    /**
     *
     * @param songName
     */
    public void setSongNameLabel(String songName){
        this.songNameLabel.setText(songName);
    }

    /**
     *
     * @param authorName
     */
    public void setAuthorNameLabel(String authorName){
        this.authorNameLabel.setText(authorName);
    }

}
