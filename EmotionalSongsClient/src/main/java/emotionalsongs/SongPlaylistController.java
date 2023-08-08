package emotionalsongs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/*
TODO:
   1- implemnetare cosa deve accadare quando viene premuto il pulsante di viewEmotions
   2- forse aggiungere il pulsante di remove canzone --> esso andrà a rimuovere la canzone dalla playlist
 */
public class SongPlaylistController {

    private static final int BUTTON_MAX_WIDTH = 200;
    private static final int BUTTON_MIN_WIDTH = 50;

    @FXML
    private Button viewEmotionsBtn;
    @FXML
    private Label songNameLabel;
    @FXML
    private Label authorNameLabel;

    private Canzone song;

    /**
     * TODO document
     */
    @FXML
    public void handleViewEmotionsButtonAction(){
        // TODO implementare cosa deve accadare quando viene premuto questo pulsante

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
     * TODO document
     * @param song
     */
    public void setSong(Canzone song){
        // set songNameLabel and authorNameLabel
        songNameLabel.setText(song.getTitolo());
        authorNameLabel.setText(song.getAutore());

        this.song = song;
    }
}
