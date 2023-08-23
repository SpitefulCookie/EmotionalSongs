package emotionalsongs;

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

/*
TODO :
    1- aggiungere pulsante che permette l'inserimento della canzoni durante la creazione della playlist (questo
    perchè abbiamo imposto come vincolo che le playlist non possono essere vuote (anche se io lo rivedrei questo
    vicolo))
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

    private static TextField playlistNameField_;

    private boolean fromAddToPlaylist;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        playlistNameField_ = playlistNameField;

        /*
         verifico in "tempo reale" se esiste già una playlist con nome uguale a quello inserito nella textField.
         Controlli effettuati:
         1- verifico se la textField è vuota, se così fosse, disabilito il pulsante di crea playlist in quanto
            non è possibile creare una playlist senza nome
         2- se esiste già una playlist con nome uguale a quello inserito nella textField, visualizzo un messaggio
         di errore e disabilito il pulsante di crea playlist in quanto non è possibile creare più playist con lo
         stesso nome.
         */
        playlistNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
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
            }
        });
    }

    /**
     * TODO document
     */
    @FXML
    public void handleAnnullaButtonAction(){
        // close the stage
        closeCreatePlaylistStage(annullaBtn);
    }

    /**
     * TODO document
     */
    @FXML
    public void handleCreatePlaylistButtonAction(){
        // add playlist
        boolean playlistCreationCheck = AllPlaylistController.addNewPlaylist(playlistNameField.getText());

        // verifico se la creazione della playlist è andata a buon fine
        if (playlistCreationCheck) {

            /*
            verifico se la creazione della playlist è stata chiamata dalla finistra di addToPlaylist, se
            non è stata chiamata da quella finista, visualizzo la playlista appena creata, altrimenti
            aggiungo la playlist alla lista selectedPlaylist della classe addToPlaylist e vado a rivisualizzare
            le playlist sul gridPane della finistra addToPlaylist.
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

                // rivisualizzo le playlist sul gridPane della pane addToPlaylist
                AddToPlaylistController.viewPlaylist();

            /*
             imposto la playlist come aperta perchè tanto essa è appena stata creata e quindi è vuota o
             le sue canzoni contenute sono gia nella lista della hashMap con chiave playlistNameField.getText(),
             questo mi evita anche un inutile interrogazione al db, in quanto se so che è vuota o comuqnue
             le sue canzoni sono già contenute nella lista della hashMap con chiave playlistNameField.getText() è
             inutile interrogarlo.
             */
                AllPlaylistController.setOpenPlaylist(playlistNameField.getText());
            }

            // close the stage
            closeCreatePlaylistStage(createPlaylistBtn);
        }
    }

    /**
     *
     * @param fromAddToPlaylist
     */
    public void setFromAddToPlaylist(boolean fromAddToPlaylist){
        this.fromAddToPlaylist = fromAddToPlaylist;
    }


    /**
     * TODO document
     */
    public static void clearPlaylistNameField(){
        playlistNameField_.setText("");
    }

    /**
     * TODO document
     * @param button
     */
    private void closeCreatePlaylistStage(Button button){
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

}
