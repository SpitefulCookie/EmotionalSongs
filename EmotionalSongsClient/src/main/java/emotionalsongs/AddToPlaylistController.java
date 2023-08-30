package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Controller class controlling the window for adding {@link AddToPlaylistController#songToAdd} to a specific playlist.
 * <p>
 *     This class implements the JavaFX Initializable interface, which is automatically called when the associated FXML dialog
 *     is loaded. It Initialises the UI components.
 * </p>
 * <p>
 *     The class handles the configuration of the behaviour of the buttons: 'annullaBtn' to cancel the operation of adding song,
 *     'addToPlaylistBtn' to add the {@link AddToPlaylistController#songToAdd} to the playlist in the {@link AddToPlaylistController#selectedPlaylists}
 * </p>
 *
 *  @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class AddToPlaylistController implements Initializable {

    @FXML private ScrollPane scrollPane;
    @FXML private GridPane gridPane;
    @FXML private Button annullaBtn;
    @FXML private Button addToPlaylistBtn;

    private static GridPane dynamicGridPane;
    private static ScrollPane dynamicScrollPane;

    protected static Canzone songToAdd;
    private Canzone song;
    private static IntegerProperty numSelectedPlaylists;
    private static List<String> selectedPlaylists;

    /**
     * Initializes the window for adding {@link AddToPlaylistController#songToAdd} to a specific playlist.
     * <p>
     *     This method is called automatically when the JavaFX dialog associated with this controller is loaded.
     *     It initializes various UI components and sets up their behavior.
     * </p>
     *
     * @param url The URL of the FXML file.
     * @param resourceBundle The ResourceBundle associated with the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dynamicGridPane = gridPane;
        dynamicScrollPane = scrollPane;

        numSelectedPlaylists = new SimpleIntegerProperty();

        // set auto-resize of component in scrollPane
        dynamicScrollPane.setFitToHeight(true);
        dynamicScrollPane.setFitToWidth(true);

        selectedPlaylists = new ArrayList<>();

        System.out.println("****** canzone da aggiungere: " + songToAdd);

        // add listener to numSelectedPlaylists
        numSelectedPlaylists.addListener((observableValue, number, t1) -> {
            addToPlaylistBtn.setDisable(numSelectedPlaylists.get() <= 0);
        });

        // view the playlist
        viewPlaylist();

    }


    /**
     * Method that handles the behaviour of the {@link AddToPlaylistController#addToPlaylistBtn}.
     * <p>
     *     When the {@link AddToPlaylistController#addToPlaylistBtn} is clicked, the {@link AddToPlaylistController#songToAdd}
     *     is added to each playlist contained in the {@link AddToPlaylistController#selectedPlaylists}.
     * </p>
     */
    public void handleAddToPlaylistButtonAction(){

        // DEBUG TODO remove
        System.out.println("aggiungo la canzone " + songToAdd);
        try {
            boolean songInsertionCheck = true;

            for (String playlist : selectedPlaylists) {
                // add the songToAdd to the playlist in DB and add the song to the hashMap if the insertion
                // of it into the db was successful.
                if(EmotionalSongsClient.repo.addSongToPlaylist(playlist, EmotionalSongsClientController.getUsername(), songToAdd.getSongUUID())) {
                    // add the songToAdd to the playlist in hashMap
                    AllPlaylistController.addSongs(playlist, songToAdd);
                }else{
                    // otherwise, if the entry of the song in the db was unsuccessful.
                    songInsertionCheck = false;
                }
            }

            if(!songInsertionCheck){

                try {
                    Stage insertionFailedStage = new Stage();

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("insertionFailed.fxml"));

                    insertionFailedStage.setScene(new Scene(fxmlLoader.load()));

                    InsertionFailedController insertionFailedController = fxmlLoader.getController();
                    insertionFailedController.setErrorLabel("Impossibile aggiungere la canzone a una o più playlist");

                    insertionFailedStage.initStyle(StageStyle.UNDECORATED);
                    insertionFailedStage.initModality(Modality.APPLICATION_MODAL);
                    insertionFailedStage.setResizable(false);
                    insertionFailedStage.show();
                }catch (IOException e){
                    //
                }

            }

        }catch (RemoteException e){

            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();

        }

        // close the stage
        GUIUtilities.closeStage(addToPlaylistBtn);
    }

    /**
     * Method that handles the behaviour of the {@link AddToPlaylistController#annullaBtn}.
     * <p>
     *     When the {@link AddToPlaylistController#annullaBtn} is clicked, the operation is cancelled and the method closes the window containing the button.
     * </p>
     */
    public void handleAnnullaButtonAction(){
        // close the stage
        GUIUtilities.closeStage(annullaBtn);
    }

    /**
     * Method that displays the user's playlists, adding them to the {@link AddToPlaylistController#gridPane},
     * if the user has no playlists, the pane controlled by the {@link AllPlaylistEmptyController} is displayed.
     */
    public static void viewPlaylist(){

        System.out.println("---- canzone da aggiungere: " + songToAdd);

        Set<String> allPlaylist = AllPlaylistController.returnAllPlaylist();

        try {
            // verifico se l'utente ha creato playlist o no
            if (allPlaylist.isEmpty()) {
                // se l'utente non ha creato playlist, allora visualizzo il relativo pane
                setAllPlaylistEmpty();
            } else {
                // change content of scrollPane
                dynamicScrollPane.setContent(dynamicGridPane);
                int row = 0;
                for (String playlist : allPlaylist) {

                    FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongsClient.class.getResource("playlist_addToPlaylist.fxml"));
                    Node playlist_pane = fxmlLoader.load();

                    Playlist_addToPlaylistController playlistController = fxmlLoader.getController();
                    playlistController.setData(playlist);

                    dynamicGridPane.add(playlist_pane, 0, row);
                    GridPane.setMargin(playlist_pane, new Insets(10));

                    row ++;

                    System.out.println("visualizzo la playlist: " + playlist);

                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method that adds the playlist passed as a parameter in the {@link AddToPlaylistController#selectedPlaylists}.
     * this method is invoked by the {@link CreatePlaylistController#registraPlaylist()} and by the {@link Playlist_addToPlaylistController#handleAddToPlaylistAction()}.
     * @param playlistName indicates the playlist to be added to the {@link AddToPlaylistController#selectedPlaylists}.
     */
    public static void addPlaylist(String playlistName){
        /*
        aggiungo la playlist alla lista selectedPlaylist
         */
        selectedPlaylists.add(playlistName);

        // update the numSelectedPlaylists
        numSelectedPlaylists.set(numSelectedPlaylists.get() + 1);
    }

    /**
     * Method that removes the playlist passed as a parameter from the {@link AddToPlaylistController#selectedPlaylists}.
     * this method is invoked by the {@link Playlist_addToPlaylistController#handleAddToPlaylistAction()}.
     * @param playlistName indicates the playlist to be removed from the {@link AddToPlaylistController#selectedPlaylists}.
     */
    public static void removePlaylist(String playlistName){
        /*
        rimuovo la playlist dalla lista selectedPlaylist
         */
        selectedPlaylists.remove(playlistName);

        // update the numSelectedPlaylists
        numSelectedPlaylists.set(numSelectedPlaylists.get() - 1);
    }

    /**
     * Method that checks if the playlist passed as a parameter is already contained in the {@link AddToPlaylistController#selectedPlaylists}.
     * @param playlistName represents the playlist to be checked.
     * @return {@code true} if the playlist has already been added to the {@link AddToPlaylistController#selectedPlaylists},
     *         {@code false} otherwise.
     */
    public static boolean playlistAlreadySelected(String playlistName){
        /*
        restituisce se la playlist è già presente nella lista selectedPlaylist
         */
        return selectedPlaylists.contains(playlistName);
    }

    /**
     * Method that loads and adds to the {@link AddToPlaylistController#dynamicScrollPane}, the pane controlled
     * by the {@link AllPlaylistEmptyController} class, informing the user that he has no playlists created.
     * <p>
     *     This method is called by the {@link AddToPlaylistController#viewPlaylist()}, if the user has no playlists created.
     * </p>
     */
    public static void setAllPlaylistEmpty(){
        try{

            // load mainViewPlaylistEmpty pane and add it to scrollPane
            FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongsClient.class.getResource("allPlaylistEmpty.fxml"));
            Node vBox = fxmlLoader.load();

            AllPlaylistEmptyController allPlaylistEmptyController = fxmlLoader.getController();
            allPlaylistEmptyController.setFromAddToPlaylist(true);

            dynamicScrollPane.setContent(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that sets the {@link AddToPlaylistController#songToAdd}.
     * @param song represents the {@link Canzone} that is passed to {@link AddToPlaylistController#songToAdd}.
     */
    public void setSongToAdd(Canzone song){
        songToAdd = song;
    }

}
