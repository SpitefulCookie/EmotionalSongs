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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The controller class that manages the single song that is displayed in the {@link Node},
 * managed by the {@link SelectedPlaylistController} class.
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
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

    private static Stage connectionFailedStage;

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
     * Method that handles the behaviour of the {@link SongPlaylistController#multipleBtn}.
     *
     *     When {@link SongPlaylistController#multipleBtn} is clicked, its behavior varies according to:
     *     <ol>
     *         <li>
     *             The user has not inserted emotions to this song, in this case when the {@link SongPlaylistController#multipleBtn} is clicked,
     *             it opens the {@link Stage} controlled by the class {@link AddEmotionsController}, which
     *             allows the user to insert emotions to the song.
     *         </li>
     *         <li>
     *             The user has inserted emotions to this song, in this case when the {@link SongPlaylistController#multipleBtn} is clicked,
     *             the {@link Node} controlled by the {@link ViewEmotionsController} class opens, which allows
     *             the user to view the inserted emotions.
     *         </li>
     *     </ol>
     *
     */
    @FXML
    public void handleMultipleButtonAction(){

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

        }
    }

    /**
     * Method that handles the behaviour of the {@link SongPlaylistController#viewEmotionsSummaryBtn}.
     * <p>
     *     When {@link SongPlaylistController#viewEmotionsSummaryBtn} is clicked, the {@link Node} controlled
     *     by the class {@link ViewReportEmotionsController} opens, allowing to view a summary of the emotions
     *     associated with the specific song.
     * </p>
     */
    @FXML
    public void visualizzaEmozioneBrano(){

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewReportEmotions.fxml"));
            Node emotions_pane = fxmlLoader.load();

            ViewReportEmotionsController viewReportEmotionsController = fxmlLoader.getController();
            boolean checkEmotionsAverageRetrieved = viewReportEmotionsController.setEmotions(song, false);

            if(checkEmotionsAverageRetrieved) {
                EmotionalSongsClientController.setDynamicPane(emotions_pane);
            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Method that manages the display of the {@link SongPlaylistController#multipleBtn} when the mouse moved over it.
     * <p>
     *     When the mouse is moved over the {@link SongPlaylistController#multipleBtn}, the button text
     *     is set and its width is changed.
     * </p>
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
     * Method that manages the display of the {@link SongPlaylistController#viewEmotionsSummaryBtn} when the mouse moved over it.
     * <p>
     *     When the mouse is moved over the {@link SongPlaylistController#viewEmotionsSummaryBtn}, the button text
     *     is set and its width is changed.
     * </p>
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
     * Method that manages the display of the buttons: {@link SongPlaylistController#multipleBtn} and {@link SongPlaylistController#viewEmotionsSummaryBtn}
     * when the mouse is exited from them.
     * <p>
     *     When the mouse leaves the buttons: {@link SongPlaylistController#multipleBtn} and {@link SongPlaylistController#viewEmotionsSummaryBtn},
     *     they are set to their initial state.
     * </p>
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
     * Method that sets the song.
     *
     * @param song Represents the song to be set.
     * @param posInGridPane indicates the position of the song in the GridPane contained in the {@link SelectedPlaylistController},
     *                      this information will be used to update the {@link SongPlaylistController#multipleBtn},
     *                      when emotions are added to the song.
     */
    public void setSong(Canzone song, int posInGridPane){ // pos in gridPane mi indica la posizione della canzone nel gridPane
        // set songNameLabel and authorNameLabel
        songNameLabel.setText(song.getTitolo());
        infoLabel.setText(song.getAutore() + " - " + " (" + song.getAnno() + ")");

        this.song = song;
        this.posInGridPane = posInGridPane;

        if(SelectedPlaylistController.getSongEmotions(song.getSongUUID()) != null) {
            // check if the song has any added emotion
            if (Objects.requireNonNull(SelectedPlaylistController.getSongEmotions(song.getSongUUID())).isEmpty()) {
                // set the emotionsAdded on false
                emotionsAdded = false;
            } else {
                // set the emotionsAdded on true
                emotionsAdded = true;
                // change the img of multipleBtn
                multipleBtnImg.setImage(guiUtilities.getImage("viewEmotions"));
            }
        }
    }


    /**
     * Method that performs a series of operations when emotions are added to the song.
     */
    protected void newEmotionsAdded(){

        // now the song has the emotions
        emotionsAdded = true;
        
        // change the Img of multipleBtn
        multipleBtnImg.setImage(guiUtilities.getImage("viewEmotions"));

    }
}
