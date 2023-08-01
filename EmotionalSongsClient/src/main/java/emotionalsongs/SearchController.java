package emotionalsongs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
TODO:
    1- ricareca nel db della canzone cercata
    2- rendere questa ricerca dinamica con un listener?
    3- aggiungere una combo box per filtrare la ricerca, quindi per scegliere se cercare per titolo autore o anno
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

        // add a listener to SongToSearchField, which displays removeSearchBtn if the songToSearchField is not Empty
        songToSearchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                // se la test field non è vuota, visualizzo e rendo premibile il pulsante di elimina ricerca
                if(!songToSearchField.getText().isEmpty()){
                    removeSearchBtn.setVisible(true);
                    removeSearchBtn.setDisable(false);
                }else{
                    /*
                    altrimenti se la text field è vuota rendo non visibile e non premibile il pulsante
                    di elimina ricerca
                     */
                    removeSearchBtn.setVisible(false);
                    removeSearchBtn.setDisable(true);
                }
            }
        });

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
