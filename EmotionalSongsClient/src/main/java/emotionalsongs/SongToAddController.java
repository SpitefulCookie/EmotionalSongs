package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller class that manages the single song that is displayed in the {@link Node},
 * managed by the {@link AddSongsToPlaylistController} class.
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
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

    /**
     * Initializes the controller when the corresponding {@link FXML} is loaded.
     *
     * @param url The URL to the FXML document (unused).
     * @param resourceBundle The resources used for localization (unused).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guiUtilities = GUIUtilities.getInstance();
    }

    /**
     * Method that manages the behaviour of the {@link SongToAddController#songToAddPane} when it is clicked.
     * <p>
     *     When {@link SongToAddController#songToAddPane} is clicked its style is modified depending on whether
     *     or not the song is already present in the "songsToAdd" list of the class {@link AddSongsToPlaylistController}.
     * </p>
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
     * Method that sets the song and the various GUI components.
     *
     * @param song Represents the song to be set.
     * @param playlistName Represents the playlist in which to insert the song.
     * @param isAdded indicates whether or not the song is already present in the "songsToAdd" list
     *                of the class {@link AddSongsToPlaylistController}.
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
                // set the songToAddPane style on songToAddPaneClicked
                guiUtilities.setNodeStyle(songToAddPane, "songToAddPane", "songToAddPaneClicked");
            } else {
                this.isAdded = false;
            }
        }
    }
}
