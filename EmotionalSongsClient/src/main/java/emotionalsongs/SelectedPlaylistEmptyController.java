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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * The controller class to display the {@link javafx.scene.Node} informing the user that the open playlist
 * has no songs inserted.
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class SelectedPlaylistEmptyController {

    @FXML
    private Button addSongsBtn;

    private String playlistName;

    /**
     * Method that handles the behaviour of the {@link SelectedPlaylistEmptyController#addSongsBtn}.
     * <p>
     *     When {@link SelectedPlaylistEmptyController#addSongsBtn} is clicked, the {@link Stage} (window) controlled
     *     by the class {@link AddSongsToPlaylistController} opens, allowing the user to add songs to the open playlist.
     * </p>
     */
    @FXML
    public void handleAddSongsButtonAction() {

        try{
            Stage addSongsStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addSongsToPlaylist.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            AddSongsToPlaylistController addSongsToPlaylistController = fxmlLoader.getController();
            addSongsToPlaylistController.setPlaylist(playlistName);

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
     * Method that sets the name of the open song.
     *
     * @param playlistName Represents the name of the open song.
     */
    public void setPlaylistName(String playlistName){
        this.playlistName = playlistName;
    }
}
