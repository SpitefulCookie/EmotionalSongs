package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AllPlaylistEmptyController {

    @FXML
    private Button createPlaylistBtn;

    @FXML
    protected void handleCreatePlaylistButtonAction(){
        // TODO aprire il pane createPlaylist.fxml
        Stage createPlaylistStage = new Stage();

        createPlaylistStage.setScene(GUIUtilities.getInstance().getScene("createPlaylist.fxml"));
        CreatePlaylistController.clearPlaylistNameField(); // "pulisco" la textField
        createPlaylistStage.initStyle(StageStyle.UNDECORATED);
        createPlaylistStage.setResizable(false);
        createPlaylistStage.initModality(Modality.APPLICATION_MODAL);
        createPlaylistStage.show();

    }
}
