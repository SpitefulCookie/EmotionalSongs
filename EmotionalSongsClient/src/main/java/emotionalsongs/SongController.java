package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/*
TO DO :
1- Implementare il comportamente (cosa deve succedere) quando i bottoni AddToPlaylist e viewEmotions vengono
   premuti
 */
public class SongController implements Initializable {

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
