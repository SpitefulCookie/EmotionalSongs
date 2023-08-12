package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/*
TODO: vedere come mai il controllo che verifica se la canzone è già contenuta nella playlist funziona
    male, ovvero sembra che i songUUID delle canzoni sono sballati.
 */
public class Playlist_addToPlaylistController implements Initializable {

    @FXML private HBox playlistPane;
    @FXML private Label playlistNameLabel;
    @FXML private Label existingSongLabel;
    @FXML private ImageView checkMarkImg;

    private GUIUtilities guiUtilities;
    //private static Canzone songToAdd; // TODO forse remove

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guiUtilities = GUIUtilities.getInstance();
    }

    public void handleAddToPlaylistAction(){

        /*
         verifico se la canzone è già contenuta nella playlist, però per fare ciò devo
         andare a verificare se la playlist è mai stata aperta o meno, questo lo verifico
         invocando il metodo getPlaylistWasOpened() della classe allPlaylistController, se questo
         metodo restituisce false allora invoco il metodo di uploadSongs della
         classe selectedPlaylistController che va a interrogare il db per farsi restituire le canzoni
         contenute nella playlist, altrimenti non faccio niente
         */
        if(!AllPlaylistController.getPlaylistWasOpened(playlistNameLabel.getText())){
            // DEBUG TODO remove
            System.out.println("la playlist: " + playlistNameLabel.getText() + " non è mai stata aperta, la apro ora");

            // carico le canzoni
            SelectedPlaylistController.uploadSongs(playlistNameLabel.getText());
        }

        // verifico se la canzone non è già contenuta nella playlist
        if(!AllPlaylistController.songAlreadyExist(playlistNameLabel.getText(), AddToPlaylistController.songToAdd)){
            // verifico se la playlist non è già stata aggiunta alla lista selectedPlaylists
            if(!AddToPlaylistController.playlistAlreadyAdded(playlistNameLabel.getText())){

                // add the playlist into temp list
                AddToPlaylistController.addPlaylist(playlistNameLabel.getText());
                // make visible the checkMarkImg
                checkMarkImg.setVisible(true);
                // change the playlistPane style
                guiUtilities.setNodeStyle(playlistPane, "playlistInToAddPane", "playlistInToAddPaneClicked");

            }else{
                /*
                 altrimenti se è già stata aggiunta, quando premo sulla playlist la devo andare a
                 rimuovere dalla lista selectedPlaylists
                 */

                // remove the playlist from temp list
                AddToPlaylistController.removePlaylist(playlistNameLabel.getText());
                // make not visible the checkMarkImg
                checkMarkImg.setVisible(false);
                // change the playlistPane style
                guiUtilities.setNodeStyle(playlistPane, "playlistInToAddPaneClicked", "playlistInToAddPane");

            }
        }else{ // altrimenti se la canzone esiste già nella playlist

            // display the existingSong message
            existingSongLabel.setVisible(true);
            // change the playlistPane style
            guiUtilities.setNodeStyle(playlistPane, "playlistInToAddPane", "playlistInToAddPaneClicked");

        }
    }

    /**
     * TODO document
     * @param playlistName
     */
    protected void setData(String playlistName){
        playlistNameLabel.setText(playlistName);
    }
}
