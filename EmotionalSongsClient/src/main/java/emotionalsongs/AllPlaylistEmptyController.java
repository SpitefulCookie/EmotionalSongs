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
 * The controller class to display the {@link javafx.scene.Node} pane which informs the user
 * that it has no playlists created.
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class AllPlaylistEmptyController {

    @FXML
    private Button createPlaylistBtn;
    private boolean fromAddToPlaylist;

    /**
     * Method that handles the behaviour of the {@link AllPlaylistEmptyController#createPlaylistBtn}.
     * <p>
     *     When {@link AllPlaylistEmptyController#createPlaylistBtn} is clicked, the {@link Stage} (window) controlled
     *     by the class {@link CreatePlaylistController} opens, allowing the creation of a new playlist.
     * </p>
     */
    @FXML
    protected void handleCreatePlaylistButtonAction(){

        // open the createPlaylist stage.
        try{
            Stage createPlaylistStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("createPlaylist.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            CreatePlaylistController createPlaylist = fxmlLoader.getController();
            createPlaylist.setFromAddToPlaylist(this.fromAddToPlaylist);

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
     * Method that sets {@link AllPlaylistEmptyController#fromAddToPlaylist}.
     *
     * @param fromAddToPlaylist represents the {@code boolean} variable with which the {@link AllPlaylistEmptyController#fromAddToPlaylist} will be set.
     */
    public void setFromAddToPlaylist(boolean fromAddToPlaylist){
        this.fromAddToPlaylist = fromAddToPlaylist;
    }

}
