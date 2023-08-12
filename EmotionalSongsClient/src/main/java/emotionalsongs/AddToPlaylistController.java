package emotionalsongs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class AddToPlaylistController implements Initializable {

    @FXML private ScrollPane scrollPane;
    @FXML private GridPane gridPane;
    @FXML private Button annullaBtn;
    @FXML private Button addToPlaylistBtn;

    private static GridPane dynamicGridPane;
    private static ScrollPane dynamicScrollPane;

    protected static Canzone songToAdd;
    private Canzone song;
    private static IntegerProperty numSelectedPlaylists;
    private static List<String> selectedPlaylists;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dynamicGridPane = gridPane;
        dynamicScrollPane = scrollPane;

        numSelectedPlaylists = new SimpleIntegerProperty();

        // set auto-resize of component in scrollPane
        dynamicScrollPane.setFitToHeight(true);
        dynamicScrollPane.setFitToWidth(true);

        selectedPlaylists = new ArrayList<>();

        System.out.println("****** canzone da aggiungere: " + songToAdd);

        // add listener to numSelectedPlaylists
        numSelectedPlaylists.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(numSelectedPlaylists.get() > 0){
                    addToPlaylistBtn.setDisable(false);
                }else{
                    addToPlaylistBtn.setDisable(true);
                }
            }
        });

        // view the playlist
        viewPlaylist();

    }


    /**
     * TODO document
     */
    public void handleAddToPlaylistButtonAction(){
        // ad ogni playlist contenuta nella selectedPlaylists aggiungo la canzone
        System.out.println("aggiungo la canzone " + songToAdd);
        try {
            for (String playlist : selectedPlaylists) {
                // add the songToAdd to the playlist in DB
                EmotionalSongsClient.repo.addSongToPlaylist(playlist, EmotionalSongsClientController.getUsername(), songToAdd.getSongUUID());
                // add the songToAdd to the playlist in hashMap
                AllPlaylistController.addSongs(playlist, songToAdd);
            }
        }catch (RemoteException e){

            e.printStackTrace();

            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();
        }

        // close the stage
        closeStage(addToPlaylistBtn);
    }

    /**
     * TODO document
     */
    public void handleAnnullaButtonAction(){
        // close the stage
        closeStage(annullaBtn);
    }

    /**
     * TODO document
     */
    public static void viewPlaylist(){
        /*
         TODO visualizzare tutte le playlist, per fare ciò aggiunere un metodo getAllPlaylist
            nella classe allPlaylistController
         */

        System.out.println("---- canzone da aggiungere: " + songToAdd);

        Set<String> allPlaylist = AllPlaylistController.returnAllPlaylist();

        try {
            // verifico se l'utente ha creato playlist o no
            if (allPlaylist.isEmpty()) {
                // se l'utente non ha creato playlist, allora visualizzo il relativo pane
                setAllPlaylistEmpty();
            } else {
                // change content of scrollPane
                dynamicScrollPane.setContent(dynamicGridPane);
                int row = 0;
                for (String playlist : allPlaylist) {

                    FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongsClient.class.getResource("playlist_addToPlaylist.fxml"));
                    Node playlist_pane = fxmlLoader.load();

                    Playlist_addToPlaylistController playlistController = fxmlLoader.getController();
                    playlistController.setData(playlist);

                    dynamicGridPane.add(playlist_pane, 0, row);
                    GridPane.setMargin(playlist_pane, new Insets(10));

                    row ++;

                    System.out.println("visualizzo la playlist: " + playlist);

                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * TODO document
     * @param playlistName
     */
    public static void addPlaylist(String playlistName){
        /*
        aggiungo la playlist alla lista selectedPlaylist
         */
        selectedPlaylists.add(playlistName);

        // update the numSelectedPlaylists
        numSelectedPlaylists.set(numSelectedPlaylists.get() + 1);
    }

    public static void removePlaylist(String playlistName){
        /*
        rimuovo la playlist dalla lista selectedPlaylist
         */
        selectedPlaylists.remove(playlistName);

        // update the numSelectedPlaylists
        numSelectedPlaylists.set(numSelectedPlaylists.get() - 1);
    }

    /**
     * TODO document
     * @param playlistName
     */
    public static boolean playlistAlreadySelected(String playlistName){
        /*
        restituisce se la playlist è già presente nella lista selectedPlaylist
         */
        return selectedPlaylists.contains(playlistName);
    }

    /**
     * TODO document
     */
    public static void setAllPlaylistEmpty(){
        try{

            // load mainViewPlaylistEmpty pane and add it to scrollPane
            FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongsClient.class.getResource("allPlaylistEmpty.fxml"));
            Node vBox = fxmlLoader.load();

            AllPlaylistEmptyController allPlaylistEmptyController = fxmlLoader.getController();
            allPlaylistEmptyController.setFromAddToPlaylist(true);

            dynamicScrollPane.setContent(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO document
     * @param song
     */
    public void setSongToAdd(Canzone song){
        songToAdd = song;
    }

    /**
     * TODO document
     * @param button
     */
    public void closeStage(Button button){
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
