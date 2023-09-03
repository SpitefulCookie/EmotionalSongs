package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class controlling the {@link Stage} (window) for the creation of a new playlist.
 * <p>
 *     This class implements the JavaFX Initializable interface, which is automatically called when the associated FXML dialog
 *     is loaded. It is also responsible for initializing the UI components.
 * </p>
 *
 *  @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class CreatePlaylistController implements Initializable {

    @FXML
    private TextField playlistNameField;
    @FXML
    private Button annullaBtn;
    @FXML
    private Button createPlaylistBtn;
    @FXML
    private Label existingPlaylistLabel;

    private boolean fromAddToPlaylist;

    /**
     * Initializes the window for the creation of a new playlist.
     * <p>
     *     This method is called automatically when the JavaFX dialog associated with this controller is loaded.
     *     It initializes various UI components and adds a {@link ChangeListener} to the {@link CreatePlaylistController#playlistNameField}
     *     to check in 'real time' if a playlist with the same name as the one entered in the {@link CreatePlaylistController#playlistNameField}
     *     already exists; if it does, the {@link CreatePlaylistController#existingPlaylistLabel} is displayed
     *     and the {@link CreatePlaylistController#createPlaylistBtn} is disabled.
     * </p>
     *
     * @param url The URL of the FXML file.
     * @param resourceBundle The ResourceBundle associated with the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /*
         verifico in "tempo reale" se esiste già una playlist con nome uguale a quello inserito nella textField.
         Controlli effettuati:
         1- verifico se la textField è vuota, se così fosse, disabilito il pulsante di crea playlist in quanto
            non è possibile creare una playlist senza nome
         2- se esiste già una playlist con nome uguale a quello inserito nella textField, visualizzo un messaggio
         di errore e disabilito il pulsante di crea playlist in quanto non è possibile creare più playist con lo
         stesso nome.
         */
        playlistNameField.textProperty().addListener((observableValue, s, t1) -> {
            if(!playlistNameField.getText().isEmpty()){
                if (AllPlaylistController.checkPlaylistName(playlistNameField.getText())){
                    existingPlaylistLabel.setVisible(false);
                    createPlaylistBtn.setDisable(false);
                }else{
                    existingPlaylistLabel.setVisible(true);
                    createPlaylistBtn.setDisable(true);
                }
            }else{
                existingPlaylistLabel.setVisible(false);
                createPlaylistBtn.setDisable(true);
            }
        });
    }

    /**
     * Method that handles the behaviour of the {@link CreatePlaylistController#annullaBtn}.
     * <p>
     *     When the {@link CreatePlaylistController#annullaBtn} is clicked, the operation is cancelled and the method closes the window containing the button.
     *
     */
    @FXML
    public void handleAnnullaButtonAction(){
        // close the stage
        GUIUtilities.closeStage(annullaBtn);
    }

    /**
     * Method that handles the behaviour of the {@link CreatePlaylistController#createPlaylistBtn}.
     * <p>
     *     When the {@link CreatePlaylistController#createPlaylistBtn} is clicked, the playlist with the same
     *     name as the one entered in the {@link CreatePlaylistController#playlistNameField} is created.
     *
     * <p>
     *     Once the playlist has been created, the behaviour of the method varies according to the value
     *     of the {@link CreatePlaylistController#fromAddToPlaylist}:
     *     <ol>
     *         <li>
     *             if {@link CreatePlaylistController#fromAddToPlaylist} is {@code true}, the playlist is added
     *             and displayed in the window controlled by the {@link AddToPlaylistController} class.
     *         </li>
     *         <li>
     *             if {@link CreatePlaylistController#fromAddToPlaylist} is {@code false}, the playlist is added and displayed in the
     *             'dynamicPane' contained in the class {@link EmotionalSongsClientController}.
     *         </li>
     *     </ol>
     *
     */
    @FXML
    public void registraPlaylist(){
        // add playlist
        boolean playlistCreationCheck = AllPlaylistController.addNewPlaylist(playlistNameField.getText());

        // verifico se la creazione della playlist è andata a buon fine
        if (playlistCreationCheck) {

            /*
            verifico se la creazione della playlist è stata chiamata dalla finestra di addToPlaylist, se
            non è stata chiamata da quella finestra, visualizzo la playlist appena creata, altrimenti
            aggiungo la playlist alla lista selectedPlaylist della classe addToPlaylist e vado a ri-visualizzare
            le playlist sul gridPane della finestra addToPlaylist.
            */
            if (!fromAddToPlaylist) {
                // display the playlist that i have just created
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("selectedPlaylist.fxml"));
                    Node playlist_pane = fxmlLoader.load();

                    SelectedPlaylistController selectedPlaylistController = fxmlLoader.getController();
                    selectedPlaylistController.setPlaylist(playlistNameField.getText());

                    // set the playlist opened
                    SelectedPlaylistController.setPlaylistOpened(playlistNameField.getText());

                    // set playlist as opened
                    AllPlaylistController.setOpenPlaylist(playlistNameField.getText());

                    EmotionalSongsClientController.setDynamicPane(playlist_pane);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                // aggiungo la playlist alle playlist selezionate
                AddToPlaylistController.addPlaylist(playlistNameField.getText());

                // ri-visualizzo le playlist sul gridPane della pane addToPlaylist
                AddToPlaylistController.viewPlaylist();

                /*
                imposto la playlist come aperta perché tanto essa è appena stata creata e quindi è vuota o
                le sue canzoni contenute sono gia nella lista della hashMap con chiave playlistNameField.getText(),
                questo mi evita anche un inutile interrogazione al db, in quanto se so che è vuota o comunque
                le sue canzoni sono già contenute nella lista della hashMap con chiave playlistNameField.getText()
                è inutile interrogarlo.
                */
                AllPlaylistController.setOpenPlaylist(playlistNameField.getText());
            }

            // close the stage
            GUIUtilities.closeStage(createPlaylistBtn);
        }
    }

    /**
     * Method that sets {@link CreatePlaylistController#fromAddToPlaylist}.
     *
     * @param fromAddToPlaylist represents the {@code boolean} variable with which the {@link CreatePlaylistController#fromAddToPlaylist} will be set.
     */
    public void setFromAddToPlaylist(boolean fromAddToPlaylist){
        this.fromAddToPlaylist = fromAddToPlaylist;
    }

}
