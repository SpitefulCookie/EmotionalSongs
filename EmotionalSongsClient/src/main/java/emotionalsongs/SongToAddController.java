package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/*
TODO:
    1- forse far si che quando l'utente vuole vedere le canzoni aggiunte alla lista delle canzoni da aggiungere
    alla playlist, al posto di visualizzare queste canzoni nel girdPane, aprire un altro stage e visualizzarle
    li, questo perchè attualmente quando l'utente visualizza queste canzoni poi perde la ricerca effettuata.
    ----- FORSE FARLO ----
 */
public class SongToAddController implements Initializable{

    @FXML private HBox songToAddPane;
    @FXML private Label songNameLabel;
    @FXML private Label infoLabel;
    @FXML private Label existingSongLabel; // TODO vedere dove settarla
    @FXML private ImageView checkMarkImg;

    private GUIUtilities guiUtilities;

    private Canzone song;
    private boolean isAdded;
    private boolean alreadyExistInPlaylist; // mi indica se la canzone già esiste nella playlist


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guiUtilities = GUIUtilities.getInstance();
    }

    /**
     * TODO document
     */
    @FXML
    public void handleAddSongToPlaylistAction(){
        /*
         l'utente potrà aggiungere la canzone alla lista delle canzoni da aggungere solo se essa non è
         già contenuta nella playlist
         */
        if(!alreadyExistInPlaylist) {

            /*
            verifico se quando premo sulla canzone essa è già stata aggiunta alle canzoni da aggiungere alla
            playlist, se non è stata aggiunta allora la aggiungo e rendo visibile l'immagine checkMarkImg, altrimenti
            vado a rimuoverla dalla canzoni da aggiungere alla playlist e vado a rendere non visibile
            l'immagine checkMarkImg
            */
            if (!isAdded) {
                // add the song to the temporary list: songsToAdd
                AddSongsToPlaylistController.addSong(song);

                // make the checkMarkImg visible
                checkMarkImg.setVisible(true);

                // set the songToAddPane style
                guiUtilities.setNodeStyle(songToAddPane, "songToAddPane", "songToAddPaneClicked");

                // now the song is added
                isAdded = true;
            } else {
                // remove the song to the temporary list: songsToAdd
                AddSongsToPlaylistController.removeSong(song);

                // make the checkMarkImg not visible
                checkMarkImg.setVisible(false);

                // set the songToAddPane style
                guiUtilities.setNodeStyle(songToAddPane, "songToAddPaneClicked", "songToAddPane");

                // now the song is not added
                isAdded = false;
            }
        }
    }

    /**
     * TODO document
     * @param song
     * @param isAdded
     */
    public void setSong(Canzone song, String playlistName, boolean isAdded){
        /*
        isAdded mi indica se la canzone è già stata aggiunta alla lista delle canzoni
        da aggiungere alla playlist (lista songToAdd della classe addSongToPlaylistController)
         */

        songNameLabel.setText(song.getTitolo());
        infoLabel.setText(song.getAutore() + " - " + " (" + song.getAnno() + ")");
        this.song = song;

        // recupero se la canzone già esiste nella playlist
        alreadyExistInPlaylist = AllPlaylistController.songAlreadyExist(playlistName, song);

        // set the style of the song box according to the value of alreadyExistsInPlaylist
        if (alreadyExistInPlaylist){

            /*
             se la canzone è già presente nella playlist, imposto lo style e visualizzo la label che informa
             l'utente di ciò.
             */
            guiUtilities.setNodeStyle(songToAddPane, "songToAddPane", "songToAddPaneClicked");
            existingSongLabel.setVisible(true);

        }else { // altrimenti se non è già presente nella playlist

            /*
            se la canzone è già aggiunta alla lista delle canzoni da aggiungere allora visualizzo il
            check mark e setto lo stile del pane della canzone
            */
            if (isAdded) {
                this.isAdded = true;
                checkMarkImg.setVisible(true);
                // set the songToAddPane style on songToAddPaneClieked
                guiUtilities.setNodeStyle(songToAddPane, "songToAddPane", "songToAddPaneClicked");
            } else {
                this.isAdded = false;
            }
        }
    }
}
