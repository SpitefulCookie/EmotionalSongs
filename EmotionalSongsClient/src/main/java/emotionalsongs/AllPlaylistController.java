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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;


/**
 * The controller class for displaying the user's playlists.
 * <p>
 *     this class implements the functionality necessary to display the user's playlists.
 *     The functionalities handled by this class are:
 *     <ul>
 *         <li>
 *             Display the user's playlists, using the appropriate method {@link AllPlaylistController#viewAllPlaylist()}.
 *         </li>
 *         <li>
 *             Add a new playlist, via the {@link AllPlaylistController#addPlaylistBtn}.
 *         </li>
 *     </ul>
 *
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class AllPlaylistController implements Initializable {

    private static final int BUTTON_MAX_WIDTH = 185;
    private static final int BUTTON_MIN_WIDTH = 50;

    @FXML
    private VBox pane;
    @FXML
    private Button addPlaylistBtn;
    @FXML
    private HBox dynamicPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;
    private static ScrollPane dynamicScrollPane;
    private static GridPane dynamicGridPane;
    private static Button addPlaylistButton_;

    protected static HashMap<String, ArrayList<Canzone>> playlists;
    protected static HashMap<String, Boolean> openPlaylists; // mi indica se la playlist specifica è stata aperta o meno

    /**
     * Initialises the GUI components and sets the initial state for displaying user playlists.<br><br>
     *
     * This method is called automatically when the FXML file associated with this controller is loaded.
     * When called automatically, it initialises the GUI components and displays the user's playlists via
     * {@link AllPlaylistController#viewAllPlaylist()}.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        il motivo per il quale ho due scrollPane(uguali) e due gridPane (uguali) è il seguente:
        lo scrollPane e il gridPane devono essere statici per vari motivi dovuti alla dinamicità di essi
        (se hai qualche dubbio contattami che te li spiego a voce), ma i componenti fxml non possono essere
        dichiarati statici, questo problema l'ho risolvo (anche se in maniera non del tutto bella)
        creato un altro scrollPane questa volta statico e un altro gridPane anch'esso statico, e assegnando
        a loro le componenti equivalenti fxml. Di fatto scrollPane e dynamicScrollPane sono la stessa cosa
        idem gridPane con dynamicGridPane.
        Il tutto vale anche per addPlaylistButton.
        (SE HAI QUALCHE DUBBIO SCRIVIMI)
         */
        dynamicScrollPane = scrollPane;
        dynamicGridPane = gridPane;
        addPlaylistButton_ = addPlaylistBtn;

        // set auto-resize of component in scrollPane
        dynamicScrollPane.setFitToHeight(true);
        dynamicScrollPane.setFitToWidth(true);

        // display the user playlist
        viewAllPlaylist();
    }

    /**
     * Method that handles the behaviour of the {@link AllPlaylistController#addPlaylistBtn}.
     * <p>
     *     When {@link AllPlaylistController#addPlaylistBtn} is clicked, the {@link Stage} (window) controlled
     *     by the class {@link CreatePlaylistController} opens, allowing the creation of a new playlist.
     * </p>
     */
    @FXML
    public void handleAddPlaylistButtonAction(){

        try{
            Stage createPlaylistStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("createPlaylist.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            CreatePlaylistController createPlaylist = fxmlLoader.getController();
            createPlaylist.setFromAddToPlaylist(false);

            createPlaylistStage.setScene(scene);
            createPlaylistStage.initStyle(StageStyle.UNDECORATED);
            createPlaylistStage.setResizable(false);
            createPlaylistStage.initModality(Modality.APPLICATION_MODAL);
            createPlaylistStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Method that manages the display of the {@link AllPlaylistController#addPlaylistBtn} when the mouse moved over it.
     * <p>
     *     When the mouse is moved over the {@link AllPlaylistController#addPlaylistBtn}, the button text
     *     is set and its width is changed.
     * </p>
     */
    @FXML
    public void handleMouseMovedAction(){
        /*
         quando il mouse passa sopra al addPlaylistButton viene settato il testo del pulsante e viene reimpostata
         la sua lunghezza, così da creare una specie di animazione.
         */
        addPlaylistButton_.setText("Nuova Playlist");
        addPlaylistButton_.setMaxWidth(BUTTON_MAX_WIDTH);
    }

    /**
     * Method that manages the display of the {@link AllPlaylistController#addPlaylistBtn} when the mouse is exited.
     * <p>
     *     When the mouse leaves the {@link AllPlaylistController#addPlaylistBtn}, it is set to its initial state.
     * </p>
     */
    @FXML
    public void handleMouseExitedAction(){
        /*
        quando il mouse è sopra al pulsante addPlaylistButton ed 'esce' da esso (quindi il cursore non sarà più
        sul pulsante) il testo del pulsante viene resettato e viene resettata anche la sua lunghezza,
        questo sempre per creare una specie di animazione
         */
        addPlaylistButton_.setText("");
        addPlaylistButton_.setMaxWidth(BUTTON_MIN_WIDTH);
    }

    /**
     * Method that adds the playlist passed as a parameter to the database via the appropriate method {@link RepositoryManager#registerPlaylist(String playlistName, String username)}
     * and to the lists {@link AllPlaylistController#playlists}, {@link AllPlaylistController#openPlaylists}.
     * <p>
     *     This method is invoked in the {@link CreatePlaylistController#registraPlaylist()} method, when a new playlist is created.
     * </p>
     * @param playlistName represents the name of the playlist to be added.
     * @return {@code true} if the insertion of the playlist in the database was successful, {@code false} otherwise.
     */
    public static boolean addNewPlaylist(String playlistName) {

        boolean playlistInsertionCheck = false;

        try {
            // add the playlist to the DB
            playlistInsertionCheck = EmotionalSongs.repo.registerPlaylist(playlistName, EmotionalSongsClientController.getUsername());

            // add the playlist in the hashMap if the insertion of it in the db was successful
            if(playlistInsertionCheck) {
                // add the playlist to the hashMap
                if (!playlists.containsKey(playlistName)) {
                    playlists.put(playlistName, new ArrayList<>());
                    openPlaylists.put(playlistName, false); // inizialmente il valore è settato su false perchè la playlit non è stata mai aperta
                }

            }else{

                try {
                    Stage insertionFailedStage = new Stage();

                    FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongs.class.getResource("insertionFailed.fxml"));

                    insertionFailedStage.setScene(new Scene(fxmlLoader.load()));

                    InsertionFailedController insertionFailedController = fxmlLoader.getController();
                    insertionFailedController.setErrorLabel("Impossibile creare la playlist");

                    insertionFailedStage.initStyle(StageStyle.UNDECORATED);
                    insertionFailedStage.initModality(Modality.APPLICATION_MODAL);
                    insertionFailedStage.setResizable(false);
                    insertionFailedStage.show();
                }catch (IOException e){
                    //
                }

            }

        }catch(RemoteException e){

            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();

            return playlistInsertionCheck;

        }

        return playlistInsertionCheck;
    }

    /**
     * Method that adds the playlist passed as a parameter to the lists {@link AllPlaylistController#playlists}, {@link AllPlaylistController#openPlaylists}.
     * <p>
     *     This method is invoked in the {@link AllPlaylistController#loadPlaylist()} method, when the
     *     user's playlists are loaded from the database.
     * </p>
     * @param playlistName represents the name of the playlist to be added.
     */
    public static void addReturnedPlaylist(String playlistName){
        /*
        metodo che svolge praticamente la stessa funzione del metodo addNewPlaylist, l'unica differenza
        è che questo viene invocato solo quando viene interrogato il DB per farsi restituire le playlist
        dell'utente loggato, quindi nel metodo uploadPlaylists, di conseguenza la playlist passata
        non viene inserita nel DB, cosa che invece avviene nel metodo addNewPlaylist
         */
        if (!playlists.containsKey(playlistName)){
            playlists.put(playlistName, new ArrayList<>());
            openPlaylists.put(playlistName, false); // inizialmente il valore è settato su false perchè la playlit non è stata mai aperta
        }
    }

    /**
     * Method that checks if the playlist is contained or not in the {@link AllPlaylistController#playlists}.
     *
     * @param playlistName represents the playlist to be checked.
     * @return {@code true} if the playlist is <strong>not</strong> contained in the {@link AllPlaylistController#playlists}, {@code false} otherwise.
     */
    public static boolean checkPlaylistName(String playlistName){
        return !playlists.containsKey(playlistName);
    }

    /**
     * Method that displays the user's playlists, adding them to the {@link AllPlaylistController#dynamicGridPane},
     * if the user has no playlists, the pane controlled by the {@link AllPlaylistEmptyController} is displayed.
     */
    public static void viewAllPlaylist(){
        /*
        verifico se l'utente ha creato delle playlist, se non le ha create, visualizzo il pane (vBox) che contiene
        il messaggio che informa l'utente che non ha creato playlist e un bottone che permette la creazione di
        playlist, altrimenti se ha creato delle playlist le visualizzo.
        */
        if(playlists.isEmpty()){
            setAllPlaylistEmpty();
        }else {
            dynamicScrollPane.setContent(dynamicGridPane);
            // set addPlaylistButton visibile and make it active
            addPlaylistButton_.setVisible(true);
            addPlaylistButton_.setDisable(false);
            try {
                // add playlist into the dynamicGridPane
                int row = 0;
                for (String playlist : playlists.keySet()) { // tramite il metodo keySet ottengo tutte le chiavi della hashMap che appunto solo le mie playlist

                    FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongs.class.getResource("playlist.fxml"));
                    Node hBox = fxmlLoader.load();

                    PlaylistController playlistController = fxmlLoader.getController();
                    playlistController.setPlaylistName(playlist);

                    dynamicGridPane.add(hBox, 0, row);
                    GridPane.setMargin(hBox, new Insets(10));

                    row ++;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method that loads and adds to the {@link AllPlaylistController#dynamicScrollPane}, the pane controlled
     * by the {@link AllPlaylistEmptyController} class, informing the user that he has no playlists created.
     * <p>
     *     This method is called by the {@link AllPlaylistController#viewAllPlaylist()}, if the user has no playlists created.
     * </p>
     */
    public static void setAllPlaylistEmpty(){
        try{
            // diable addPlaylitButton and make it not visibile
            addPlaylistButton_.setVisible(false);
            addPlaylistButton_.setDisable(true);

            // load mainViewPlaylistEmpty pane and add it to scrollPane
            FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongs.class.getResource("allPlaylistEmpty.fxml"));
            Node vBox = fxmlLoader.load();

            AllPlaylistEmptyController allPlaylistEmptyController = fxmlLoader.getController();
            allPlaylistEmptyController.setFromAddToPlaylist(false);

            dynamicScrollPane.setContent(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that adds the songs contained in the list passed as a parameter to the playlist.
     *
     * @param playlistName represents the playlist into which the songs.
     * @param songs represents the songs to be added to the playlist passed as a parameter.
     */
    public static void addSongs(String playlistName, List<Canzone> songs){
        /*
        metodo che va ad aggiungere alla playlist: playlistName le canzoni contenute nella lista songs
         */
        playlists.get(playlistName).addAll(songs);
    }

    /**
     * Method that adds the song passed as a parameter to the playlist.
     *
     * @param playlistName represents the playlist into which the song.
     * @param songs represents the song to be added to the playlist passed as a parameter.
     */
    public static void addSongs(String playlistName, Canzone songs) {
        /*
        metodo che va ad aggiungere alla playlist: playlistName la canzone song, prima di fare ciò verifico
        se essa non è già contenuta
         */
        if (!songAlreadyExist(playlistName, songs)) {
            playlists.get(playlistName).add(songs);
        }

    }


    /**
     * Method that returns the songs in the playlist passed as a parameter.
     *
     * @param playlistName represents the playlist whose songs are to be returned.
     * @return A {@link List} containing the songs contained in the playlist passed as parameter.
     */
    public static List<Canzone> getSongs(String playlistName){
        /*
        metodo che restituisce le canzoni della playlist: playlistName
         */
        return playlists.get(playlistName);
    }

    /**
     * Method which checks if the song passed as a parameter is present in the playlist passed as a parameter.
     *
     * @param playlistName represents the playlist in which to check the song.
     * @param song represents the song to be controlled.
     * @return {@code true} if the song passed as a parameter is present in the playlist passed as a parameter,
     *         {@code false} otherwise.
     */
    public static boolean songAlreadyExist(String playlistName, Canzone song){
        /*
        metodo che verifica se la canzone: song è contenuta nella playlist: playlistName
        la verifica avviene per SongUUID
         */

        List<Canzone> songPlaylist = playlists.get(playlistName);
        for (Canzone canzone : songPlaylist) {
            if (canzone.getSongUUID().equals(song.getSongUUID())) {
                return true; // la canzone è già contenuta nella playlist
            }
        }
        return false; // la canzone non è contenuta nella playlist
    }

    /**
     * Method which returns if the playlist passed as a parameter has already been opened.
     *
     * @param playlistName represents the playlist to be checked.
     * @return {@code true} if the playlist passed as a parameter has already been opened, {@code false} otherwise.
     */
    public static boolean getPlaylistWasOpened(String playlistName){
        /*
        tale metodo ritorna il valore associato alla playlist: PlaylistName, tale valore mi indica se la
        playlist è stata aperta o meno.
        return true --> la playlist è stata aperta
        return false --> la playlist non è stata aperta
         */
        return openPlaylists.get(playlistName);
    }

    /**
     * Method which sets the playlist passed as a parameter as open.
     *
     * @param playlistName represents the playlist to be set as open.
     */
    public static void setOpenPlaylist(String playlistName){
        /*
        setta il valore associato alla chiave playlistName su true, esso infatti indicherà che la playlist
        è stata aperta
         */
        openPlaylists.put(playlistName, true);
    }

    /**
     * method which returns all the keys of the {@link AllPlaylistController#playlists}, each key representing a playlist created by the user.
     * @return A {@link Set} containing the keys of the {@link AllPlaylistController#playlists}.
     */
    public static Set<String> returnAllPlaylist(){
        return playlists.keySet();
    }

    /**
     * Method that loads all the user's playlists from the database using the appropriate method {@link RepositoryManager#getUserPlaylists(String user)}.
     */
    public static void loadPlaylist(){
        /*
        questo metodo va a caricare tutte le playlist dell'utente loggato, questo metodo viene
        invocato nel metodo setUser() della classe EmotionalSongsClientController solo se l'utente
        non è guest.
         */

        if(playlists == null) {

            try {
                // interrogo il db per farmi restituire tutte le playlist dell'utente
                Set<String> returned_playlists = EmotionalSongs.repo.getUserPlaylists(EmotionalSongsClientController.getUsername());

                playlists = new HashMap<>();
                openPlaylists = new HashMap<>();

                // add the returnedPlaylists into playlists hashMap
                for(String playlist : returned_playlists){
                    addReturnedPlaylist(playlist);
                }

            }catch (RemoteException e){

                e.printStackTrace();

                Stage connectionFailedStage = new Stage();

                connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
                connectionFailedStage.initStyle(StageStyle.UNDECORATED);
                connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
                connectionFailedStage.setResizable(false);
                connectionFailedStage.show();
            }
        }
    }
}
