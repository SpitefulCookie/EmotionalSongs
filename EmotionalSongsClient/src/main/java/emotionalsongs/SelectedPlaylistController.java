package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;

/**
 * The controller class for displaying the songs contained in the open playlist.
 * <p>
 *     This class implements the functionality necessary to display the songs contained in the open playlist.
 *     The functionalities handled by this class are:
 *     <ul>
 *         <li>
 *             Displays the songs contained in the open playlist, using the appropriate method {@link SelectedPlaylistController#viewSongs()}.
 *         </li>
 *         <li>
 *             Add a new song to the open playlist, via the {@link SelectedPlaylistController#addSongsBtn} button.
 *         </li>
 *     </ul>
 * </p>
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class SelectedPlaylistController implements Initializable {

    private static final int BUTTON_MAX_WIDTH = 180;
    private static final int BUTTON_MIN_WIDTH = 50;

    @FXML
    private Label playlistNameLabel;
    @FXML
    private Button addSongsBtn;
    @FXML
    private Button returnToAllPlaylistBtn;
    @FXML
    private HBox dynamicPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;
    private static ScrollPane dynamicScrollPane;
    private static GridPane dynamicGridPane;
    private static Button addSongsButton_;
    private static Label playlistNameLabel_;

    private static String playlistOpened;

    /*
    questa lista mi contiene i controller delle canzoni presenti sul gridPane, devo memorizzare i controller
    perchè poi mi serviranno nel momento in cui andrò ad aggiungere le emozioni alla canzone.
     */
    private static List<SongPlaylistController> songControllers;
    /*
     NOTA: lista che contiene le emozioni associate alla key canzone, la chiave che è di tipo String
     viene rappresentata dal songUUID dell canzone.
     */
    private static HashMap<String, List<Emozione>> emotionsSongs = new HashMap<String, List<Emozione>>();

    /**
     * It initialises the GUI components and sets the initial state for displaying the songs in the open playlist.<br><br>
     *
     * This method is called automatically when the FXML file associated with this controller is loaded.
     * When called automatically, it initialises the GUI components and displays the songs contained in the open playlist via
     * {@link SelectedPlaylistController#viewSongs()}.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // il motivo per il quale faccio ciò è lo stesso spiegato nella classe AllPlaylistController.
        dynamicScrollPane = scrollPane;
        dynamicGridPane = gridPane;
        addSongsButton_ = addSongsBtn;
        playlistNameLabel_ = playlistNameLabel;

        // set auto-resize of component in scrollPane
        dynamicScrollPane.setFitToHeight(true);
        dynamicScrollPane.setFitToWidth(true);

    }

    /**
     * Method that handles the behaviour of the {@link SelectedPlaylistController#addSongsBtn}.
     * <p>
     *     When {@link SelectedPlaylistController#addSongsBtn} is clicked, the {@link Stage} (window) controlled
     *     by the class {@link AddSongsToPlaylistController} opens, allowing the user to add songs to the open playlist.
     * </p>
     */
    @FXML
    public void handleAddSongsButtonAction() {

        try{
            Stage addSongsStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addSongsToPlaylist.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            AddSongsToPlaylistController addSongsToPlaylistController = fxmlLoader.getController();
            addSongsToPlaylistController.setPlaylist(playlistNameLabel.getText());

            addSongsStage.initStyle(StageStyle.UNDECORATED);
            addSongsStage.setScene(scene);
            addSongsStage.setResizable(false);
            addSongsStage.initModality(Modality.APPLICATION_MODAL);
            addSongsStage.show();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method that handles the behaviour of the {@link SelectedPlaylistController#returnToAllPlaylistBtn}.
     * <p>
     *     When {@link SelectedPlaylistController#returnToAllPlaylistBtn} is clicked, the {@link Node} controlled
     *     by the {@link AllPlaylistController} class opens, for the display of all playlists created by the user.
     * </p>
     */
    @FXML
    public void handleReturnToAllPlaylistButtonAction(){
        // return to allPlaylist pane
        EmotionalSongsClientController.setDynamicPane("allPlaylist.fxml");
    }

    /**
     * Method that manages the display of the {@link SelectedPlaylistController#addSongsBtn} when the mouse moved over it.
     * <p>
     *     When the mouse is moved over the {@link SelectedPlaylistController#addSongsBtn}, the button text
     *     is set and its width is changed.
     * </p>
     */
    @FXML
    public void handleMouseMovedAction(){
        /*
         quando il mouse passa sopra al addPlaylistButton viene settato il testo del pulsante e viene reimpostata
         la sua lunghezza, così da creare una specie di animazione
         */
        addSongsButton_.setText("Aggiungi Canzoni");
        addSongsButton_.setMaxWidth(BUTTON_MAX_WIDTH);
    }

    /**
     * Method that manages the display of the {@link SelectedPlaylistController#addSongsBtn} when the mouse is exited.
     * <p>
     *     When the mouse leaves the {@link SelectedPlaylistController#addSongsBtn}, it is set to its initial state.
     * </p>
     */
    @FXML
    public void handleMouseExitedAction(){
        /*
        quando il mouse è sopra al pulsante addPlaylistButton ed 'esce' da esso (quindi il cursore non sarà più
        sul pulsante) il testo del pulsante viene resettato e viene resettata anche la sua lunghezza,
        questo sempre per creare una specie di animazione
         */
        addSongsButton_.setText("");
        addSongsButton_.setMaxWidth(BUTTON_MIN_WIDTH);
    }

    /**
     * Method that adds each song in the list passed as a parameter to the playlist and the database
     * via the appropriate method {@link RepositoryManager#addSongToPlaylist(String playlistName, String userId, String songUUID)}.
     *
     * @param songsToAdd Represents the songs to be added to the playlist.
     */
    public static void addNewSongs(List<Canzone> songsToAdd){
        /*
        metodo che aggiunge le canzoni contenute nella lista songsToAdd nella playlist specifica, successivamente
        va a rivisualizzare la playlist invocando l'opportuno metodo viewSongs
         */

        try {

            // add the song to the DB
            for(Canzone song : songsToAdd) {
                EmotionalSongsClient.repo.addSongToPlaylist(playlistNameLabel_.getText(), EmotionalSongsClientController.getUsername(), song.getSongUUID());
            }

            // add the song to the playlist
            AllPlaylistController.addSongs(playlistNameLabel_.getText(), songsToAdd); // aggiungo le canzoni nella playlist

            // display the playlist songs
            viewSongs();
        }catch (RemoteException e){

            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();

        } catch (SQLException f){

            Stage insertionFailedStage = new Stage();

            insertionFailedStage.setScene(GUIUtilities.getInstance().getScene("insertionFailed.fxml"));
            InsertionFailedController.setErrorLabel("Impossibile aggiungere una o più canzoni alla playlist");
            insertionFailedStage.initStyle(StageStyle.UNDECORATED);
            insertionFailedStage.initModality(Modality.APPLICATION_MODAL);
            insertionFailedStage.setResizable(false);
            insertionFailedStage.show();

        }

    }

    /**
     * Method that displays the songs contained in the open playlist, adding them to the {@link SelectedPlaylistController#dynamicGridPane},
     * if the playlist has no songs added, the {@link Node} controlled by {@link SelectedPlaylistEmptyController} is displayed.
     */
    public static void viewSongs(){

        // recupero le canzoni della playlist, invocando l'apposito metodo della classe allPlaylist
        List<Canzone> songs = AllPlaylistController.getSongs(playlistNameLabel_.getText());

        // ogni volta che visualizzo le canzoni vado a creare una nuova lista songController
        songControllers = new ArrayList<SongPlaylistController>();

        /*
        metodo che visualizza le canzoni contenute nella playlist aperta, se non c'e ne sono, viene visualizzato
        il pane di playlist empty.
         */
        if(songs.isEmpty()){ // verifico se la lista è vuota
            setPlaylistEmpty();
        }else{ // altrimenti se non è vuota vado a visualizzare tutte le canzone contenute al suo interno
            try {
                // change content of scrollPane
                dynamicScrollPane.setContent(dynamicGridPane);
                // make addSongsButton visible and not disable
                addSongsButton_.setVisible(true);
                addSongsButton_.setDisable(false);
                // add song contained in the list to the gridPane
                for (int i = 0; i < songs.size(); i++) {
                    // load the songPlaylist pane
                    FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongsClient.class.getResource("songPlaylist.fxml"));
                    Node song_pane = fxmlLoader.load();

                    // get the song-specific songPlaylistController from fxml
                    SongPlaylistController songPlaylistController = fxmlLoader.getController();
                    songPlaylistController.setSong(songs.get(i), i);

                    // add the songPlaylistController to songController list
                    songControllers.add(songPlaylistController);

                    // add song pane in to the gridPane
                    dynamicGridPane.add(song_pane, 0, i);
                    GridPane.setMargin(song_pane, new Insets(10));

                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Method responsible for opening the playlist passed as a parameter.
     * <p>
     *     This method sets the name of the playlist and displays the songs in the playlist by loading them
     *     from the database if they have never been loaded.
     * </p>
     * @param playlistName Represents the playlist to be opened.
     * @return {@code true} if the loading of songs was successful, {@code false} otherwise.
     */
    public boolean openPlaylist(String playlistName){
        /*
        questo metodo viene eseguito nel metodo handleOpenPlaylistAction della
        classe PlaylistController
         */

        boolean songsLoaded = true;

        // set the playlist name
        this.playlistNameLabel.setText(playlistName);

        // set playlistOpened
        playlistOpened = playlistName;

        // se la playlist non è mai stata aperta fino ad ora
        if(!AllPlaylistController.getPlaylistWasOpened(playlistName)){

            /*
             carico le canzoni contenute nella playlist: playlistName
             NOTA: è in questo metodo che avviene l'interazione con il db.
             */
            songsLoaded = loadSongs(playlistName);

            // DEGUB TODO remove DEBUG
            System.out.println("apro per la prima voltra la playlist: " + playlistName); // TODO remove


        }else{
            // TODO remove
            System.out.println("---------------------- apro la playlist " + playlistName + " non per la prima volta");
        }

        // view the playlist songs
        viewSongs();

        return songsLoaded;
    }

    /**
     * Method that sets the playlist to open.
     *
     * @param playlistName Represents the name of the playlist.
     */
    public void setPlaylist(String playlistName){
        /*
        metodo che viene invocato nel metodo handleCreatePlaylistButtonAction della classe
        CreatePlaylistController, questo metodo va a settare il nome della playlist e a chiamare il
        metodo setPlaylistEmpty() questo perchè la playlist appena creata è vuota
        NOTA: potevo anche lasicare solamente il metodo openPlaylist() che sotto a un certo punto di vista
        fa la stessa cosa di questo metodo, ma andavo a effettuare un interrogazione inutile al db, in quanto
        so già a priori che la playlist è vuota.
         */

        // set the playlist name
        this.playlistNameLabel.setText(playlistName);

        // display the PlaylistEmptyPane
        setPlaylistEmpty();
    }

    /**
     * Method responsible for loading the songs in the playlist passed as a parameter from the database,
     * via the appropriate method {@link RepositoryManager#getSongsInPlaylist(String playlistName, String username)}.
     *
     * @param playlistName Playlist for loading songs.
     * @return {@code true} if the loading of songs was successful, {@code false} otherwise.
     */
    public static boolean loadSongs(String playlistName){
        /*
        questo metodo interroga il db per farsi restituire tutte le canzoni contenute nella
        playlist: playlistName
         */

        try {
            // interrogo il DB per farmi restituire tutte le canzoni contenute nella playlist: playlistName
            Set<Canzone> songs = EmotionalSongsClient.repo.getSongsInPlaylist(playlistName, EmotionalSongsClientController.getUsername());

            /*
             add the songs to the playlist, NOTA : passo new Arraylist<>(songs) perchè il DB mi ritorna un
             hashSet mentre il metodo richiede una arraylist, quindi per convertire un hashSet in arrayList si
             fa così.
             */
            AllPlaylistController.addSongs(playlistName, new ArrayList<>(songs));

            // now the playlist has been opened
            AllPlaylistController.setOpenPlaylist(playlistName);

            return true;

        }catch (RemoteException e){

            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();

            return false;

        }
    }

    /**
     * Method responsible for adding the emotions passed as a parameter to the specific song.
     *
     * @param songUUID Represents the UUID of the song in which to insert emotions.
     * @param emotions Representing emotions to be added.
     */
    public static void addEmotionsSong(String songUUID, List<Emozione> emotions){
        emotionsSongs.put(songUUID, emotions);
    }

    /**
     * Method which returns the emotions of songs with UUID equal to the one passed as parameter.
     *
     * @param songUUID It represents the UUID of the song.
     * @return A {@link List} of {@link Emozione} if the song has inserted emotions, {@code null} otherwise.
     */
    public static List<Emozione> getSongEmotions(String songUUID){
        /* metodo che ritorna le emozioni della canzone ha ha con uuid : songUUID se essa è presente
        nella hashMap, se la canzone non è presente nella hashMap ritorna null.
         */
        if (emotionsSongs.containsKey(songUUID)){
            return emotionsSongs.get(songUUID);
        }
        return null;
    }

    /**
     * Method which checks if the song with UUID equal to the one passed as parameter, has already loaded emotions.
     *
     * @param songUUID Represents the UUID of the song to be controlled.
     * @return {@code true} if the song has already loaded the emotions, {@code false} otherwise.
     */
    public static boolean songEmotionsAlreadyExist(String songUUID){
        /*
        metodo che mi restituisce un valore booleano in base a se la hashMap contiene già una chiave uguale
        a songUUID o no
        true --> la hashMap contiene già una chiave uguale a songUUID -> questo significa che ho già caricato
                 le emotiozni per questa canzone.
        false --> la hashMap non contiene una chiave uguale a songUUID -> questo significa che non ho ancora
                 caricato le canzoni per questa canzone.
         */
        return emotionsSongs.containsKey(songUUID);
    }

    /**
     * Method which returns the {@link SongPlaylistController} present in the {@link SelectedPlaylistController#songControllers} list at the position passed as parameter.
     *
     * @param pos The position in the {@link SelectedPlaylistController#songControllers} of the {@link SongPlaylistController} to be returned.
     * @return The required {@link SongPlaylistController}.
     */
    protected static SongPlaylistController getSongController(int pos){
        /*
        metodo che mi ritorna il songPlaylistController che si trova in posizione pos nella lista: songControllers
         */
        return songControllers.get(pos);
    }

    /**
     * Method that sets the name of the playlist that is currently open.
     *
     * @param playlistName The name of the playlist to be set.
     */
    public static void setPlaylistOpened(String playlistName){
        /*
        metodo che setta la playlist aperta
         */
        playlistOpened = playlistName;
    }

    /**
     * Method that returns the name of the currently open playlist.
     *
     * @return {@link SelectedPlaylistController#playlistOpened}.
     */
    protected static String getPlaylistOpened(){
        /*
        metodo che ritorna la playlist aperta
         */
        return playlistOpened;
    }

    /**
     * Method that loads and adds to the {@link SelectedPlaylistController#dynamicScrollPane}, the pane controlled
     * by the {@link SelectedPlaylistEmptyController} class, which informs the user that the open playlist does not contain any songs.
     * <p>
     *     This method is called by the {@link SelectedPlaylistController#viewSongs()}, if the open playlist contains no songs.
     * </p>
     */
    public static void setPlaylistEmpty(){
        /*
         metodo che visualizza il pane di playlist empty, esso viene invocato quando l'utente non ha nessuna
         canzone nella playlist aperta.
         */
        try{
            // diable addSongsButton and make it not visibile
            addSongsButton_.setVisible(false);
            addSongsButton_.setDisable(true);

            // load playlistEmpty pane
            FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongsClient.class.getResource("selectedPlaylistEmpty.fxml"));
            Node playlistEmpty = fxmlLoader.load();

            // set the playlist name
            SelectedPlaylistEmptyController selectedPlaylistEmptyController = fxmlLoader.getController();
            selectedPlaylistEmptyController.setPlaylistName(playlistNameLabel_.getText());

            // add playlistEmpty pane into scrollPane
            dynamicScrollPane.setContent(playlistEmpty);

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
