package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

    protected static List<String> playlist;

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

        /*
         inizializzo la playlist list, dato che essa è statica questa questa operazione
         la faccio una sola volta (ovvero la prima) ecco il motivo dell'if
         */
        if(playlist == null) {
            /*
             TODO invocare metodo server che interroga il DB per farsi restituire tutte le playlist
              create dall'utente, e inizializzare la lista playlist con la lista restituista dal metodo
             */
            playlist = new ArrayList<String>();
        }

        // display the user playlist
        viewAllPlaylist();
    }

    /**
     * TODO document
     */
    @FXML
    public void handleAddPlaylistButtonAction(){
        // TODO aprire il pane createPlaylist.fxml
        try{
            Stage createPlaylistStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("createPlaylist.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
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
    public static void addNewPlaylist(String playlistName){
        // TODO aggiungere la playlist al DB, invocando il metodo dal server
        playlist.add(playlistName);
        //viewAllPlaylist(); // TODO remove
    }

    /**
     * TODO document
     * @param playlistName
     * @return
     */
    public static boolean checkPlaylistName(String playlistName){
        return !playlist.contains(playlistName);
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
        if(playlist.isEmpty()){
            setAllPlaylistEmpty();
        }else {
            dynamicScrollPane.setContent(dynamicGridPane); // TODO mettere if che fa eseguire questo comando solo se primi la allPlaylist era vuota(Empty)
            // set addPlaylistButton visibile and make it active
            addPlaylistButton_.setVisible(true);
            addPlaylistButton_.setDisable(false);
            try {
                // add playlist into the dynamicGridPane
                for (int i = 0; i < playlist.size(); i++) {
                    FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongsClient.class.getResource("playlist.fxml"));
                    Node hBox = fxmlLoader.load();

                    PlaylistController playlistController = fxmlLoader.getController();
                    playlistController.setPlaylistName(playlist.get(i));

                    dynamicGridPane.add(hBox, 0, i);
                    GridPane.setMargin(hBox, new Insets(10));

                    System.out.println("visualizzo playlist : " + playlist.get(i));
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
            dynamicScrollPane.setContent(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
