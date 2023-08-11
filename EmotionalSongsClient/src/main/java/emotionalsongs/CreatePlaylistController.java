package emotionalsongs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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
        AllPlaylistController.addNewPlaylist(playlistNameField.getText());

        // display the playlist that i have just created
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("selectedPlaylist.fxml"));
            Node playlist = fxmlLoader.load();

            SelectedPlaylistController selectedPlaylistController = fxmlLoader.getController();
            selectedPlaylistController.setPlaylist(playlistNameField.getText());

            EmotionalSongsClientController.setDynamicPane(playlist);

        }catch(IOException e){
            e.printStackTrace();
        }

        // close the stage
        closeCreatePlaylistStage(createPlaylistBtn);
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
