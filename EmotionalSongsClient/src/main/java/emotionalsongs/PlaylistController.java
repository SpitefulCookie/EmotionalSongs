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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller class that manages the single playlist that is displayed in the {@link Node}
 * managed by the {@link AllPlaylistController} class.
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class PlaylistController{

    @FXML private HBox playlistPane;
    @FXML private Label playlistNameLabel;

    /**
     * Method that manages the behaviour of the playlist when clicked on.
     * <p>
     *     When the playlist is clicked on, the {@link Node} managed by the {@link SelectedPlaylistController}
     *     class is displayed; it represents the open playlist.
     * </p>
     * @param event The ActionEvent triggered when the playlist is clicked (not used).
     */
    @FXML
    protected void handleOpenPlaylistAction(MouseEvent event){

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("selectedPlaylist.fxml"));
            Node playlist = fxmlLoader.load();

            SelectedPlaylistController selectedPlaylistController = fxmlLoader.getController();
            boolean checkOpenPlaylist = selectedPlaylistController.openPlaylist(playlistNameLabel.getText());

            /*
             aggiunto la playlist al dynamic pane solo se le canzoni della playlist sono state caricate
             correttamente, quindi solo se la playlist è stata aperta correttamente.
             */
            if(checkOpenPlaylist) {
                EmotionalSongsClientController.setDynamicPane(playlist);
            }

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Method that sets the name of the playlist.
     *
     * @param playlistName Represents the playlist name to be set.
     */
    protected void setPlaylistName(String playlistName){
        playlistNameLabel.setText(playlistName);
    }

}
