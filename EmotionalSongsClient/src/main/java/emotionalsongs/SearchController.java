package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/*
TO DO :
1- adeguare cosa deve succedere quando viene premuto il pulsante di cerca canzone ovvero :
    1- viene effettuata la ricerca nel db della canzone cercata
    2- vengono visualizzate le canzoni eventualmente restituite dal db
2- forse mettere un multi button che mi indica la tipologia di ricerca da fare, ovvero se per titolo, autore
   o anno.
 */
public class SearchController implements Initializable {

    @FXML
    private VBox pane;
    @FXML
    private TextField songToSearchField;
    @FXML
    private Button searchBtn;
    @FXML
    private Button removeSearchBtn;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;

    private List<Node> songsPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // setto l'auto resize dei componenti/elementi che si trovano nello scroll pane
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        // initialize the songsPane list
        songsPane = new ArrayList<Node>();

    }

    /**
     * TODO document
     */
    @FXML
    public void handleSearchButtonAction(){
        /*
        implementare cosa deve accadare quando quando viene premuto questo pulsante
         */

        // TODO : ricerca nel db della canzone inserita nella text Field

        // remove the last search before the new search
        removeLastSearch();

        // TODO : visualizziare solo le canzoni restituite dalla chiamata effettuata al db

        if(!songToSearchField.getText().isEmpty()) { // verifico che il text filed non sia vuoto
            removeSearchBtn.setVisible(true); // visualizzo il bottone di elimina ricerca
            removeSearchBtn.setDisable(false); // rendo il bottone di elimina ricerca premibile
            for (int i = 0; i < 20; i++) { // riempo per prova il gridpane
                setNewSongFound("canzone " + i, "autore " + i, i);
            }
        }else{
            System.out.println("the Text field is empty");
        }
    }

    /**
     * TODO document
     */
    @FXML
    public void handleRemoveSearchButtonAction(){
        System.out.println("bottone elimina ricerca premuto");

        removeSearchBtn.setVisible(false); // rendo non visibile il bottone di elimina ricerca
        removeSearchBtn.setDisable(true); // rendo il bottone di elimina ricerca non premibile

        // reset the text Field
        songToSearchField.setText("");

        // remove last search (reset the grid pane and the text filed)
        removeLastSearch();

    }

    /**
     * TODO document
     * @param songName
     * @param authorName
     * @param row
     */
    public void setNewSongFound(String songName, String authorName, int row){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("song.fxml"));
            Node song = fxmlLoader.load();

            // get song controller, this it serves for set song name and author name of the song
            SongController songController = fxmlLoader.getController();
            songController.setData(songName, authorName);

            // add the song pane laoded to songsPane list
            songsPane.add(song);

            // add the fxml song view in the gridPane
            gridPane.add(song, 0, row);
            // set the margin between songs (the space between songs)
            GridPane.setMargin(song, new Insets(10));

        }catch (IOException e){
            System.out.println("Songs view PROBLEM !");
            e.printStackTrace();
        }
    }

    /**
     * TODO document
     */
    public void removeLastSearch(){
        /*
        metodo che "rimuove" l'ultima ricerca, ovvero va a resettare il gridpane
         */
        for(Node pane : songsPane){
            gridPane.getChildren().remove(pane);
        }

        /*
         clear list that contains panes of last search ("pulisco" la lista che contiene i
         panes dell'ultima ricerca, questo viene fatto per evitare che troppo stazio venga occupato)
         */
        songsPane.clear();
    }
}
