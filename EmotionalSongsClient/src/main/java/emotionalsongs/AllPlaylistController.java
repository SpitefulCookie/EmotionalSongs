package emotionalsongs;

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
import java.sql.SQLException;
import java.util.*;

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

    //protected static List<String> playlist;
    protected static HashMap<String, ArrayList<Canzone>> playlists;
    protected static HashMap<String, Boolean> openPlaylists; // mi indica se la playlist specifica è stata aperta o meno

    /**
     * TODO document
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        il motivo per il quale ho due scrollPane(uguali) e due gridPane (uguali) è il seguente:
        lo scrollPane e il gridPane devono essere statici per vari motivi dovuti alla dinamicità di essi
        (se hai qualche dubbio contattami che te li spiego a voce), ma i componenti fxml non possono essere
        dichiarati statici, questo probelma l'ho risolvo (anche se in maniera non del tutto bella)
        creato un altro scrollPane questa volta statico e un altro gridPane anch'esso statico, e assegnando
        a loro le componenti equivalenti fxml. di fatto scrollPane e dynamicScrollPane sono la stessa cosa
        idem gridPane con dynamicGridPane.
        il tutto vale anche per l'addPlaylistButton.
        (SE HAI QUALCHE DUBBIO SCRIVIMI)
         */
        dynamicScrollPane = scrollPane;
        dynamicGridPane = gridPane;
        addPlaylistButton_ = addPlaylistBtn;

        // set auto-resize of component in scrollPane
        dynamicScrollPane.setFitToHeight(true);
        dynamicScrollPane.setFitToWidth(true);

        //scrollPane_.setContent(dynamicGridPane_); // magari questo assegnamento serve di nuovo

        /* TODO remove all
         inizializzo la playlist list, dato che essa è statica questa questa operazione
         la faccio una sola volta (ovvero la prima) ecco il motivo dell'if

        if(playlist == null) {
            //playlist = new ArrayList<String>();
            playlist = new HashMap<>();
            openPlaylists = new HashMap<>();
        }*/

        // display the user playlist
        viewAllPlaylist();
    }

    /**
     * TODO document
     */
    @FXML
    public void handleAddPlaylistButtonAction(){
        // TODO aprire il pane createPlaylist.fxml

        /* TODO remove, perchè devo agire anche sul controller
        Stage createPlaylistStage = new Stage();

        createPlaylistStage.setScene(GUIUtilities.getInstance().getScene("createPlaylist.fxml"));
        CreatePlaylistController.clearPlaylistNameField(); // "pulisco" la textField
        createPlaylistStage.initStyle(StageStyle.UNDECORATED);
        createPlaylistStage.setResizable(false);
        createPlaylistStage.initModality(Modality.APPLICATION_MODAL);
        createPlaylistStage.show();
        */

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
     * TODO document
     */
    @FXML
    public void handleMouseMovedAction(){
        /*
         quando il mouse passa sopra al addPlaylistButton viene settato il testo del pulsante e viene reimpostata
         la sua lunghezza, così da creare una specie di animazione
         */
        addPlaylistButton_.setText("Nuova Playlist");
        addPlaylistButton_.setMaxWidth(BUTTON_MAX_WIDTH);
    }

    /**
     * TODO document
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
     * TODO document
     * @param playlistName
     * @return
     */
    public static boolean addNewPlaylist(String playlistName) {
        try {
            // add the playlist to the DB
            EmotionalSongsClient.repo.registerPlaylist(playlistName, EmotionalSongsClientController.getUsername());

            // add the playlist to the hashMap
            if (!playlists.containsKey(playlistName)) {
                playlists.put(playlistName, new ArrayList<Canzone>());
                openPlaylists.put(playlistName, false); // inizialmente il valore è settato su false perchè la playlit non è stata mai aperta
            }

        }catch(RemoteException e){

            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();

            return false;

        }catch (SQLException f){

            Stage insertionFailedStage = new Stage();

            insertionFailedStage.setScene(GUIUtilities.getInstance().getScene("insertionFailed.fxml"));
            InsertionFailedController.setErrorLabel("Impossibile creare la playlist");
            insertionFailedStage.initStyle(StageStyle.UNDECORATED);
            insertionFailedStage.initModality(Modality.APPLICATION_MODAL);
            insertionFailedStage.setResizable(false);
            insertionFailedStage.show();

            return false;

        }

        return true;
    }

    /**
     * TODO documnet
     * @param playlistName
     */
    public static void addReturnedPlaylist(String playlistName){
        /*
        metodo che svolge praticamente la stessa funzione del metodo addNewPlaylist, l'unica differenza
        è che questo viene invocato solo quando viene interrogato il DB per farsi restituire le playlist
        dell'utente loggato, quindi nel metodo uploadPlaylists, di conseguenza la playlist passata
        non viene inserita nel DB, cosa che invece avviene nel metodo addNewPlaylist
         */

        if (!playlists.containsKey(playlistName)){
            playlists.put(playlistName, new ArrayList<Canzone>());
            openPlaylists.put(playlistName, false); // inizialmente il valore è settato su false perchè la playlit non è stata mai aperta
        }
    }

    /**
     * TODO document
     * @param playlistName
     * @return
     */
    public static boolean checkPlaylistName(String playlistName){
        return !playlists.containsKey(playlistName);
    }

    /**
     * TODO document
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
            dynamicScrollPane.setContent(dynamicGridPane); // TODO mettere if che fa eseguire questo comando solo se primi la allPlaylist era vuota(Empty)
            // set addPlaylistButton visibile and make it active
            addPlaylistButton_.setVisible(true);
            addPlaylistButton_.setDisable(false);
            try {
                // add playlist into the dynamicGridPane
                int row = 0;
                for (String playlist : playlists.keySet()) { // tramite il metodo keySet ottengo tutte le chiavi della hashMap che appunto solo le mie playlist

                    FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongsClient.class.getResource("playlist.fxml"));
                    Node hBox = fxmlLoader.load();

                    PlaylistController playlistController = fxmlLoader.getController();
                    playlistController.setPlaylistName(playlist);

                    dynamicGridPane.add(hBox, 0, row);
                    GridPane.setMargin(hBox, new Insets(10));

                    row ++;

                    System.out.println("visualizzo playlist : " + playlist); // TODO togliere il print
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * TODO document
     */
    public static void setAllPlaylistEmpty(){
        try{
            // diable addPlaylitButton and make it not visibile
            addPlaylistButton_.setVisible(false);
            addPlaylistButton_.setDisable(true);

            // load mainViewPlaylistEmpty pane and add it to scrollPane
            FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongsClient.class.getResource("allPlaylistEmpty.fxml"));
            Node vBox = fxmlLoader.load();

            AllPlaylistEmptyController allPlaylistEmptyController = fxmlLoader.getController();
            allPlaylistEmptyController.setFromAddToPlaylist(false);

            dynamicScrollPane.setContent(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO document
     * @param playlistName
     * @param songs
     */
    public static void addSongs(String playlistName, List<Canzone> songs){
        /*
        metodo che va ad aggiungere alla playlist: playlistName le canzoni contenute nella lista songs
         */
        playlists.get(playlistName).addAll(songs);
    }

    /**
     * TODO document
     * @param playlistName
     * @param songs
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
     * TODO document
     * @param playlistName
     * @return
     */
    public static List<Canzone> getSongs(String playlistName){
        /*
        metodo che restituisce le canzoni della playlist: playlistName
         */
        return playlists.get(playlistName);
    }

    /**
     * TODO document
     * @param playlistName
     * @param song
     * @return
     */
    public static boolean songAlreadyExist(String playlistName, Canzone song){
        /*
        metodo che verifica se la canzone: song è contenuta nella playlist: playlistName
        la verifica avviene per SongUUID
         */

        System.out.println("controllo nella playlist " + playlistName + " la canzone con uuid " + song.getSongUUID());

        List<Canzone> songPlaylist = playlists.get(playlistName);
        for(int i = 0; i < songPlaylist.size(); i++){
            if(songPlaylist.get(i).getSongUUID().equals(song.getSongUUID())){
                return true; // la canzone è già contenuta nella playlist
            }
        }
        return false; // la canzone non è contenuta nella playlist
    }

    /**
     * TODO doccument
     * @param playlistName
     * @return
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
     * TODO document
     * @param playlistName
     */
    public static void setOpenPlaylist(String playlistName){
        /*
        setta il valore associato alla chiave playlistName su true, esso infatti indicherà che la playlist
        è stata aperta
         */
        openPlaylists.put(playlistName, true);
    }

    /**
     * TODO document
     * @return
     */
    public static Set<String> returnAllPlaylist(){
        return playlists.keySet();
    }

    /**
     * TODO document
     */
    public static void loadPlaylist(){
        /*
        questo metodo va a caricare tutte le playlist dell'utente loggato, questo metodo viene
        invocato nel metodo setUser() della classe EmotionalSongsClientController solo se l'utente
        non è guest.
         */

        System.out.println("metodo uploadPlaylist invocato"); // TODO remove

        if(playlists == null) {

            try {
                // interrogo il db per farmi restituire tutte le playlist dell'utente
                Set<String> returned_playlists = EmotionalSongsClient.repo.getUserPlaylists(EmotionalSongsClientController.getUsername());

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
