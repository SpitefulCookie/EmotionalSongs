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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

    private static List<Canzone> songs;


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
         TODO invocare metodo server, che invoca il db per farsi restituire le canzoni contenute
           nella playlist aperta, ---- forse questa invoazione farla nel metodo setPlaylist ----
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

    public static void addNewSongs(List<Canzone> songsToAdd){
        /*
        TODO aggiungere le canzoni contenute nella lista passata come argomento (songsToAdd) nel db, quindi invocare
         l'apposito metodo del server.
         */
        songs.addAll(songsToAdd); // aggiunto tutte le canzoni contenute in songsToAdd nella lista songs TODO forse rimuovere

        // display the playlist songs
        viewSongs();

    }

    /**
     * TODO document
     */
    public static void viewSongs(){
        /*
        TODO forse interrogare in questo metodo il db per farsi restituire le canzoni contenute nella
           playlist apera, invocando l'apposito metodo del server.  --- quindi questa operazione non avverà ne
           nel metodo Initizialize  ne nel metodo setPlaylist --- Consultarsi con Mattia per vedere qual'è la
           soluzione migliore
         */

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
                    songPlaylistController.setSong(songs.get(i));

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
    public void setPlaylist(String playlistName){
        // set the playlist name
        this.playlistNameLabel.setText(playlistName);

        /*
         TODO interroga il db per farsi restituire le canzoni contenute nella playlist apera, e inizilizzare
          la lista songs con la lista restituita dal metodo invocato --- forse effettuare questa operazione
          nel metodo viewSongs ---
         */

        /*
         create the songs list TODO: forse rimuovere e far si che la lista venga assegata a quella restituita
                                    dal metodo del server
         */
        songs = new ArrayList<Canzone>();

        // view the playlist songs
        viewSongs();
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
