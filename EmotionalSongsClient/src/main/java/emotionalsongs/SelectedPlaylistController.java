package emotionalsongs;

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

        /*
         TODO: per l'interrogazione del db, l'idea è quella di creare un metodo che vada a inerrogare il db
               facendosi restituire le canzoni contenute nella playlist, sempre in questo metodo le canzoni
               restituite verranno aggiunte nella lista con chiave playlistName nella hashMap contenuta
               in allPlaylist, --> successivamete la visualizzazione delle canzoni avverrà chiamando un metodo
               getSongs(ancora da implementare) della classe AllPlaylist che restituirà la lista di canzoni
               contenute nella playlist, quindi la lista songs di questa classe non servirà più ---------
               ---> di conseguenza il metodo viewSongs con ciclerà più sulla lista songs ma sulla lista
               restituita dal metodo getSogs della classe allPlaylist
         */

        // la visualizzazione iniziale delle canzoni avviene nel metodo setPlaylist
    }

    /**
     * TODO document
     */
    @FXML
    public void handleAddSongsButtonAction() {
        // TODO aprire stage che permette l'inserimento delle canzoni nella playlist
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
     * TODO document
     */
    @FXML
    public void handleReturnToAllPlaylistButtonAction(){
        // return to allPlaylist pane
        EmotionalSongsClientController.setDynamicPane("allPlaylist.fxml");
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
        addSongsButton_.setText("Aggiungi Canzoni");
        addSongsButton_.setMaxWidth(BUTTON_MAX_WIDTH);
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
        addSongsButton_.setText("");
        addSongsButton_.setMaxWidth(BUTTON_MIN_WIDTH);
    }

    /**
     * TODO document
     * @param songsToAdd
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
     * TODO document
     */
    public static void viewSongs(){

        // recupero le canzoni della playlist, invocando l'apposito metodo della classe allPlaylist
        List<Canzone> songs = AllPlaylistController.getSongs(playlistNameLabel_.getText());

        /*
         ogni volta che visualizzo le canzoni vado a creare una nuova lista songController
         TODO vedere se creare la lista una sola volta (ovvero la prima volta) e quindi in questo metodo
            invocare il clear della lista (in quanto per il corretto funzionamento la lista prima di ogni
            visualizzazione deve essere vuota), oppure lasciare così.
         */
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
     * TODO document
     * @param playlistName
     */
    public void openPlaylist(String playlistName){
        /*
        questo metodo viene eseguito nel metodo handleOpenPlaylistAction della
        classe PlaylistController
         */

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
            loadSongs(playlistName);

            // DEGUB TODO remove DEBUG
            System.out.println("apro per la prima voltra la playlist: " + playlistName); // TODO remove


        }else{
            // TODO remove
            System.out.println("---------------------- apro la playlist " + playlistName + " non per la prima volta");
        }

        // view the playlist songs
        viewSongs();
    }

    /**
     * TODO document
     * @param playlistName
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
     * TODO document
     * @param playlistName
     */
    public static void loadSongs(String playlistName){
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
        }catch (RemoteException e){
            e.printStackTrace(); // TODO remove

            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();
        }
    }

    /**
     * TODO document
     * @param songUUID
     * @param emotions
     */
    public static void addEmotionsSong(String songUUID, List<Emozione> emotions){
        /*
         prima di inserire la canzone e le emozioni nella hashMap verifico se non esiste già una chiave
         con nome songUUID, se non esiste aggiungo la canzone e le relative emozioni alla hashMap
         */
        emotionsSongs.put(songUUID, emotions);
    }

    /**
     * TODO document
     * @param songUUID
     * @return
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
     * TODO document
     * @param songUUID
     * @return
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
     * TODO document
     * @param pos
     * @return
     */
    protected static SongPlaylistController getSongController(int pos){
        /*
        metodo che mi ritorna il songPlaylistController che si trova in posizione pos nella lista: songControllers
         */
        return songControllers.get(pos);
    }

    /**
     * TODO document
     * @param playlistName
     */
    public static void setPlaylistOpened(String playlistName){
        /*
        metodo che setta la playlist aperta
         */
        playlistOpened = playlistName;
    }

    /**
     * TODO document
     * @return
     */
    protected static String getPlaylistOpened(){
        /*
        metodo che ritorna la playlist aperta
         */
        return playlistOpened;
    }

    /**
     * TODO document
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
