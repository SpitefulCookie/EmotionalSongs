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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller class that manages the individual playlist that is displayed in the {@link Stage} (window)
 * managed by the {@link AddToPlaylistController} class.
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class Playlist_addToPlaylistController implements Initializable {

    @FXML private HBox playlistPane;
    @FXML private Label playlistNameLabel;
    @FXML private Label existingSongLabel;
    @FXML private ImageView checkMarkImg;

    private GUIUtilities guiUtilities;

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
     * Method that manages the behaviour of the {@link Playlist_addToPlaylistController#playlistPane} when it is clicked.
     *
     *     When the {@link Playlist_addToPlaylistController#playlistPane} is clicked:
     *     <ol>
     *         <li>
     *              It is checked if the songs of the playlist that the {@link Playlist_addToPlaylistController#playlistPane}
     *              "represents" have already been loaded from the database, if they have not already been loaded, they are loaded.
     *         </li>
     *         <li>
     *              The style of the {@link Playlist_addToPlaylistController#playlistPane} is modified and the
     *              playlist which the {@link Playlist_addToPlaylistController#playlistPane} "represents" is added to or removed
     *              from the list "selectedPlaylists" contained in the {@link AddToPlaylistController}, depending on if:
     *              <ol>
     *                  <li>
     *                      The song to be added to the playlist is already inserted in the playlist, in which
     *                      case an error message is displayed in the {@link Playlist_addToPlaylistController#playlistPane},
     *                      informing the user of this.
     *                  </li>
     *                  <li>
     *                      the {@link Playlist_addToPlaylistController#playlistPane} has already been clicked on, so the playlist that it "represents"
     *                      is already inserted in the list "selectedPlaylists" present in the {@link AddToPlaylistController},
     *                      consequently it is removed from the list.
     *                  </li>
     *                  <li>
     *                      The {@link Playlist_addToPlaylistController#playlistPane} has not yet been clicked on, so the playlist it "represents"
     *                      is not included in the "selectedPlaylists" list present in the {@link AddToPlaylistController} class,
     *                      consequently it is added to the list.
     *                  </li>
     *              </ol>
     *         </li>
     *     </ol>
     *
     */
    @FXML
    public void handleAddToPlaylistAction(){

        /*
        variabile booleana che controlla l'inserimento delle canzoni nelle playlist, inizialmente
        la variabile è inizializzata su true in quanto l'inserimento è possibile, il suo valore può essere
        modificato quando nel caricare le canzoni dal db tramite il metodo loadSongs, viene generata una
        remote exception, se così fosse il metodo loadSongs ritornerebbe false e questo implicherebbe che
        l'inserimento della canzone nella playlist non è possibile.
         */
        boolean insertionCheck = true;

        /*
         verifico se la canzone è già contenuta nella playlist, però per fare ciò devo
         andare a verificare se la playlist è mai stata aperta o meno, questo lo verifico
         invocando il metodo getPlaylistWasOpened() della classe allPlaylistController, se questo
         metodo restituisce false allora invoco il metodo di uploadSongs della
         classe selectedPlaylistController che va a interrogare il db per farsi restituire le canzoni
         contenute nella playlist, altrimenti non faccio niente
         */
        if(!AllPlaylistController.getPlaylistWasOpened(playlistNameLabel.getText())){

            // carico le canzoni
            insertionCheck = SelectedPlaylistController.loadSongs(playlistNameLabel.getText());
        }

        // verifico se l'inserimento è possibile
        if(insertionCheck) {
            // verifico se la canzone non è già contenuta nella playlist
            if (!AllPlaylistController.songAlreadyExist(playlistNameLabel.getText(), AddToPlaylistController.songToAdd)) {
                // verifico se la playlist non è già stata aggiunta alla lista selectedPlaylists
                if (!AddToPlaylistController.playlistAlreadySelected(playlistNameLabel.getText())) {

                    // add the playlist into selectedPlaylist list, contained in the addToPlaylistController class
                    AddToPlaylistController.addPlaylist(playlistNameLabel.getText());
                    // make visible the checkMarkImg
                    checkMarkImg.setVisible(true);
                    // change the playlistPane style
                    guiUtilities.setNodeStyle(playlistPane, "playlistInToAddPane", "playlistInToAddPaneClicked");

                } else {
                    /*
                    altrimenti se è già stata aggiunta, quando premo sulla playlist la devo andare a
                    rimuovere dalla lista selectedPlaylists, in quanto non deve essere aggiunta alla playlist
                    specifica
                    */

                    // remove the playlist from selectedPlaylist list, contained in the addToPlaylistController class
                    AddToPlaylistController.removePlaylist(playlistNameLabel.getText());
                    // make not visible the checkMarkImg
                    checkMarkImg.setVisible(false);
                    // change the playlistPane style
                    guiUtilities.setNodeStyle(playlistPane, "playlistInToAddPaneClicked", "playlistInToAddPane");

                }
            } else { // altrimenti se la canzone esiste già nella playlist

                // display the existingSong message
                existingSongLabel.setVisible(true);
                // change the playlistPane style
                guiUtilities.setNodeStyle(playlistPane, "playlistInToAddPane", "playlistInToAddPaneClicked");

            }
        }
    }

    /**
     * Method that sets the {@link Playlist_addToPlaylistController#playlistNameLabel} and checks if the playlist
     * passed as a parameter already exists in the list "selectedPlaylists" contained in the {@link AddToPlaylistController} class,
     * if so, the style of the {@link Playlist_addToPlaylistController#playlistPane} is changed.
     *
     * @param playlistName The playlist to be set.
     */
    protected void setData(String playlistName){
        playlistNameLabel.setText(playlistName);

        // verifico se la playlist è già stata selezionata, e se si gli scambio lo stile
        if(AddToPlaylistController.playlistAlreadySelected(playlistName)){
            // make visible the checkMarkImg
            checkMarkImg.setVisible(true);
            // change the playlistPane style
            guiUtilities.setNodeStyle(playlistPane, "playlistInToAddPane", "playlistInToAddPaneClicked");
        }
    }
}
