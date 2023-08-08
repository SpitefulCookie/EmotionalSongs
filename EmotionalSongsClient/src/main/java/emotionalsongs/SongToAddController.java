package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/*
TODO: inserire una label che indica che se la canzone è già presente nella playlist in cui voglio andare ad
      aggiungere le canzoni. se così fosse questa label viene resa visibile e il pane della canzone non cliccabile
 */
public class SongToAddController{

    @FXML private Label songNameLabel;
    @FXML private Label authorNameLabel;
    @FXML private ImageView checkMarkImg;

    private Canzone song;
    private boolean isAdded;

    /**
     * TODO document
     */
    @FXML
    public void handleAddSongToPlaylistAction(){
        /*
        verifico se quando premo sulla canzone essa è già stata aggiunta alle canzoni da aggiungere alla
        playlist, se non è stata aggiunta allaora la aggiungo e rendo visibile l'immagine, altrimenti
        vado a rimuoverla dalla canzoni da aggiungere alla playlist e vado a rendere non visibile
        l'immagine
         */
        if(!isAdded) {
            // add the song to the temporary list: songsToAdd
            AddSongsToPlaylistController.addSong(song);

            // make visibile the checkMarkImg
            checkMarkImg.setVisible(true);

            // now the song is added
            isAdded = true;
        }else{
            // remove the song to the temporary list: songsToAdd
            AddSongsToPlaylistController.removeSong(song);

            checkMarkImg.setVisible(false);

            // now the song is not added
            isAdded = false;
        }
    }

    /**
     * TODO document
     * @param song
     * @param isAdded
     */
    public void setSong(Canzone song, boolean isAdded){
        // isAdded mi indica se la canzone è già stata aggiunta alla lista delle canzoni da aggiungere alla playlist

        songNameLabel.setText(song.getTitolo());
        authorNameLabel.setText(song.getAutore());
        this.song = song;

        if(isAdded){
            this.isAdded = true;
            checkMarkImg.setVisible(true);
        }else{
            this.isAdded = false;
        }
    }
}
