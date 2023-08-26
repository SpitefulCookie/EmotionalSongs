package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlaylistController{

    @FXML
    HBox pane;
    @FXML
    private Label playlistNameLabel;

    /**
     * TODO document
     */
    @FXML
    protected void handleOpenPlaylistAction(MouseEvent event){
        // DEBUG TODO remove
        System.out.println("Apro la playlist : " + playlistNameLabel.getText());

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
     * TODO document
     * @param playlistName
     */
    protected void setPlaylistName(String playlistName){
        playlistNameLabel.setText(playlistName);
    }

}
