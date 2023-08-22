package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/*
TODO:
   1- implemnetare cosa deve accadare quando viene premuto il pulsante di viewEmotions
   2- implemnetare cosa deve accadare quando viene premuto il pulsante di addEmotions
   3- forse aggiungere il pulsante di remove canzone --> esso andrà a rimuovere la canzone dalla playlist
 */
public class SongPlaylistController implements Initializable {

    private static final int BUTTON_MAX_WIDTH = 200;
    private static final int BUTTON_MIN_WIDTH = 45;

    @FXML private Button viewEmotionsSummaryBtn;
    @FXML private Button multipleBtn;
    @FXML private Label songNameLabel;
    @FXML private Label infoLabel;
    @FXML private ImageView multipleBtnImg;

    private GUIUtilities guiUtilities;

    private Canzone song;
    private int posInGridPane;
    private boolean emotionsAdded;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guiUtilities = GUIUtilities.getInstance();
    }

    @FXML
    public void handleMultipleButtonAction(){
        // TODO imolementare comportamento in base a se l'utente ha inseire le emozioni o no
        if(!emotionsAdded) { // se l'utente non ha inserito le emozioni

            try {
                Stage addEmotionsStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addEmotions.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                AddEmotionsController addEmotionsController = fxmlLoader.getController();
                addEmotionsController.setSong(song, posInGridPane);

                addEmotionsStage.setScene(scene);
                addEmotionsStage.initStyle(StageStyle.UNDECORATED);
                addEmotionsStage.setResizable(false);
                addEmotionsStage.initModality(Modality.APPLICATION_MODAL);
                addEmotionsStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{ // altrimenti se ha inserito le emozioni

            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewEmotions.fxml"));
                Node emotions_pane = fxmlLoader.load();

                ViewEmotionsController viewEmotionsController = fxmlLoader.getController();
                viewEmotionsController.setEmotions(song, SelectedPlaylistController.getSongEmotions(song.getSongUUID()));

                EmotionalSongsClientController.setDynamicPane(emotions_pane);

            }catch (IOException e){
                e.printStackTrace();
            }

            // DEBUG TODO remove
            System.out.println("Pulsante visualizza emozioni per la canzone: " + song + " premuto");

        }
    }

    /**
     * TODO document
     */
    @FXML
    public void handleViewEmotionsSummaryButtonAction(){
        // TODO implementare cosa deve accadare quando viene premuto questo pulsante

        // DEBUG TODO remove
        System.out.println("Pulsante visualizza report emozioni per la canzone: " + song + " premuto");

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewReportEmotions.fxml"));
            Node emotions_pane = fxmlLoader.load();

            ViewReportEmotionsController viewReportEmotionsController = fxmlLoader.getController();
            viewReportEmotionsController.setEmotions(song, false);

            EmotionalSongsClientController.setDynamicPane(emotions_pane);

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * TODO document
     */
    @FXML
    public void handleMultipleButtonMouseMovedAction(){
        /*
         quando il mouse passa sopra al addEmotionsBtn viene settato il testo del pulsante e viene reimpostata
         la sua lunghezza, così da creare una specie di animazione
         */

        // verifico se l'utente ha inserito delle emozioni alla canzone
        if(!emotionsAdded) {
            multipleBtn.setText("Aggiungi emozioni");
            multipleBtn.setMaxWidth(BUTTON_MAX_WIDTH);
        }else{
            multipleBtn.setText("Visualizza le tue emozioni");
            multipleBtn.setMaxWidth(BUTTON_MAX_WIDTH);
        }
    }

    /**
     * TODO docunent
     */
    @FXML
    public void handleViewEmotionsSummaryButtonMouseMovedAction(){
        /*
         quando il mouse passa sopra al viewEmotionsBtn viene settato il testo del pulsante e viene reimpostata
         la sua lunghezza, così da creare una specie di animazione
         */
        viewEmotionsSummaryBtn.setText("Visualizza report emozioni");
        viewEmotionsSummaryBtn.setMaxWidth(BUTTON_MAX_WIDTH);
    }

    /**
     * TODO document
     */
    @FXML
    public void handleMouseExitedAction(){
        /*
        gestisce l'animazione di quando il mouse finisce di essere sopra a uno dei due bottoni
         */
        multipleBtn.setText("");
        viewEmotionsSummaryBtn.setText("");
        multipleBtn.setMaxWidth(BUTTON_MIN_WIDTH);
        viewEmotionsSummaryBtn.setMaxWidth(BUTTON_MIN_WIDTH);
    }

    /**
     * TODO document
     * @param song
     */
    public void setSong(Canzone song, int posInGridPane){ // pos in gridPane mi indica la posizione della canzone nel gridPane
        // set songNameLabel and authorNameLabel
        songNameLabel.setText(song.getTitolo());
        infoLabel.setText(song.getAutore() + " - " + " (" + song.getAnno() + ")");

        this.song = song;
        this.posInGridPane = posInGridPane;
        // initialize the emotions of the song
        initEmotions(song);
    }


    /**
     * TODO document
     * @param song
     */
    protected void initEmotions(Canzone song){
        /*
        metodo che invoca l'apposito metodo del server che va a interrogare il DB per farsi restituire le
        emozioni presenti nella canzone specifica.
        se la lista restituita dal metodo ha dimensione = 0 allora l'utente non ha inserito emozioni alla
        canzone specifica, se invece la dimesione della lista è != 0 allora l'utente ha inserito le
        emozioni alla canzone specifica
         */

        try{

            /*
             controllo se non già caricato le emozioni per questa canzone (questo controllo avviene
             andando a verificare se esiste una chiave nella hashMap emotionsSongs uguale allo uuid della
             canzone song) , se non le ho ancora caricate le vado a caricare interrogando prima il db
             tramite l'apposito metodo del server e poi aggiungendole alla hashMap contenuta
             nella classe SelectedPlaylist
             */
            if(!SelectedPlaylistController.songEmotionsAlreadyExist(song.getSongUUID())) {

                ArrayList<Emozione> emotions = EmotionalSongsClient.repo.getSongEmotions(song.getSongUUID(), EmotionalSongsClientController.getUsername());

                /*
                aggiungo la canzone e le emozioni nella hashMap emotionsSongs contenuta nella classe
                selectedPlaylist
                */
                SelectedPlaylistController.addEmotionsSong(song.getSongUUID(), emotions);
            }else{
                // DEBUG TODO remove else
                System.out.println("Emozioni già caricate per la canzone: " + song.getTitolo() + " con uuid: " + song.getSongUUID());
            }

            // DEBUG todo remove
            System.out.println("numero di emozioni : " + SelectedPlaylistController.getSongEmotions(song.getSongUUID()).size());

            if(SelectedPlaylistController.getSongEmotions(song.getSongUUID()).isEmpty()){ // todo fare il controllo acccedendo alle emozioni nella hashMap
                // set the emotionsAdded on false
                emotionsAdded = false;
            }else{
                // set the emotionsAdded on true
                emotionsAdded = true;
                // change the img of multipleBtn
                multipleBtnImg.setImage(guiUtilities.getImage("viewEmotions"));

            }

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
     */
    protected void newEmotionsAdded(){
        /*
        metodo che viene incocato quando vengono aggiunte le emozioni alla canzone.
         */

        // now the song has the emotions
        emotionsAdded = true;

        // change the text of multipleBtn
        multipleBtn.setText("Visualizza emozioni");
        // change the Img of multipleBtn
        multipleBtnImg.setImage(guiUtilities.getImage("viewEmotions"));

        //DEBUG TODO remove
        System.out.println("cambio il bottone della canzone in pos: " + posInGridPane);
    }
}
