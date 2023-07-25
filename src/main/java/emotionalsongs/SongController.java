package emotionalsongs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/*
TO DO :
1- Implementare il comportamente (cosa deve succedere) quando i bottoni AddToPlaylist e viewEmotions vengono
   premuti
 */
public class SongController {

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
