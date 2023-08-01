package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

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
        // TODO implementare apertura playlist selezionata
        System.out.println("Apro la playlist : " + playlistNameLabel.getText());
    }

    /**
     * TODO document
     * @param playlistName
     */
    protected void setPlaylistName(String playlistName){
        playlistNameLabel.setText(playlistName);
    }

}
