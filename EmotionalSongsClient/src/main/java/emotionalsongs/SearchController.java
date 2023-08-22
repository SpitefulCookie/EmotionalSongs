package emotionalsongs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

/*
TODO:
    1- ricerca nel db della canzone cercata
    2- rendere questa ricerca dinamica con un listener?
    3- aggiungere una combo box per filtrare la ricerca, quindi per scegliere se cercare per titolo autore o anno
    4- il key event handleSearchButtonAction forse metterlo a livello di pane e non ha livello di searchBox(come lo è adesso)
 */
public class SearchController implements Initializable {

    @FXML private VBox pane;
    @FXML private TextField searchField;
    @FXML private TextField yearField;
    @FXML private Button searchBtn;
    @FXML private Button removeSearchBtn;
    @FXML private Button advancedSearchBtn;
    @FXML private Button titleSearchBtn;
    @FXML private Button authorAndYearSearchBtn;
    @FXML private ScrollPane scrollPane;
    @FXML private GridPane gridPane;
    @FXML private ImageView removeSearchImg;
    @FXML private Label infoLabel;
    @FXML private HBox advancedSearchBox;

    private GUIUtilities guiUtilities;
    private boolean advancedSearchActivated;
    private String filteredSearch;
    private static List<Node> songsPane; // TODO forse rimetterlo non statico

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // setto l'auto resize dei componenti/elementi che si trovano nello scroll pane
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        // create a new guiUtilities object with pattern singleton
        guiUtilities = GUIUtilities.getInstance();

        // initially the advanced search is not activated
        advancedSearchActivated = false;

        // initialize the songsPane list
        songsPane = new ArrayList<Node>();

        // initially the search is set to titleSearch
        handleTitleSearchButtonAction();

        // aggiungo come vincolo alla yearField l'inserimento di soli numeri e di massimo 4 numeri
        GUIUtilities.forceNumericInput(yearField, 4);

        // add a listener to searchField, which displays removeSearchBtn.
        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                // se la searchField non è vuota, visualizzo e rendo premibile il pulsante di elimina ricerca
                if(filteredSearch.equalsIgnoreCase("title")) {
                    if (!searchField.getText().isEmpty()) {
                        showRemoveSearchBtn();
                    } else {
                        /*
                        altrimenti se la text field è vuota rendo non visibile e non premibile il pulsante
                        di elimina ricerca
                        */
                        hideRemoveSearchBtn();
                    }
                }else{
                    // add a listener to yearField
                    yearField.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                            /*
                            se la searchFiled e la yearField non sono vuote, visualizzo e rendo premibile
                            il pulsante di elimina ricerca
                            */
                            if (!searchField.getText().isEmpty() && !yearField.getText().isEmpty()) {
                                showRemoveSearchBtn();
                            } else {
                                /*
                                altrimenti se solo una della due textField è vuota rendo non visibile e non premibile
                                il di elimina ricerca
                                 */
                                hideRemoveSearchBtn();
                            }
                        }
                    });
                    /*
                     se la searchFiled e la yearField non sono vuote, visualizzo e rendo premibile
                     il pulsante di elimina ricerca
                     */
                    if (!searchField.getText().isEmpty() && !yearField.getText().isEmpty()) {
                        showRemoveSearchBtn();
                    } else {
                        /*
                        altrimenti se solo una della due textField è vuota rendo non visibile e non premibile
                        il di elimina ricerca
                       */
                        hideRemoveSearchBtn();
                    }
                }
            }
        });
    }

    /**
     * TODO document
     */
    @FXML
    public void handleSearchButtonAction(KeyEvent key){

        try {

            // la ricerca viene effettuata quando viene premuto il pulsante di INVIO
            if (key.getCode() == KeyCode.ENTER) {

                System.out.println("hai premuto il pulsante enter");

                // remove the last search before the new search
                removeLastSearch();

                Set<Canzone> songs = null;

                if (filteredSearch.equalsIgnoreCase("title")) {

                    if(searchField.getText().isEmpty()){
                        System.out.println("ricerca per titolo vuota");
                        return;
                    }

                    // DEBUG TODO remove
                    System.out.println("ricerco la canzone: " + searchField.getText());
                    System.out.println("ricerca per titolo");

                    // interrogo il db per farmi restituire le canzoni carecate
                    songs = EmotionalSongsClient.repo.ricercaCanzone(searchField.getText());

                } else {

                    if(searchField.getText().isEmpty() || yearField.getText().isEmpty()){
                        System.out.println("ricerca per autore e anno vuota");
                        return;
                    }

                    // DEBUG TODO remove
                    System.out.println("ricerco la canzone: " + searchField.getText() + " con anno " + yearField.getText());
                    System.out.println("ricerca per autore e anno");

                    // interrogo il db per farmi restituire le canzoni carecate
                    songs = EmotionalSongsClient.repo.ricercaCanzone(searchField.getText(), yearField.getText());

                }

                // verifico se se le canzoni restituite non sono vuote
                if (!songs.isEmpty()) {
                    // visualizzo le canzoni restituite dal db

                    // set the content of scroll pane
                    scrollPane.setContent(gridPane);

                    // viuslizzo le canzoni restituite dal db
                    int row = 0;
                    for (Canzone song : songs) {
                        setNewSongFound(song, row);
                        row ++;
                    }
                } else {
                    // DEBUG TODO remove
                    System.out.println("la ricerca non ha portato a nessun risultato");

                    try{
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("noSearchResult.fxml"));
                        Node noSearchResult_pane = fxmlLoader.load();

                        // add the noSearchResult_pane to scroll pane.
                        scrollPane.setContent(noSearchResult_pane);

                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }

        }catch (RemoteException e){

            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();
        }
    }

    /**
     * TODO document
     */
    @FXML
    public void handleRemoveSearchButtonAction(){
        System.out.println("bottone elimina ricerca premuto");

        // nascondo il bottone di rimuovi ricerca
        hideRemoveSearchBtn();

        // set the content of scroll pane
        scrollPane.setContent(gridPane);

        // controllo il filtro della ricerca
        if(filteredSearch.equalsIgnoreCase("title")) {
            // reset the searchField
            searchField.setText("");
        }else{
            // reset the searchField and the yearField
            searchField.setText("");
            yearField.setText("");
        }

        // remove last search (reset the grid pane and the text filed)
        removeLastSearch();
    }

    /**
     * TODO document
     */
    public void handleAdvancedSearchButtonAction(){
        // verifico se la ricerca avanzata è attivata o meno
        if(!advancedSearchActivated){ // se non è attivata
            advancedSearchBox.setVisible(true);
            advancedSearchActivated = true;
        }else{ // se è attivata
            advancedSearchBox.setVisible(false);
            advancedSearchActivated = false;
        }
    }

    /**
     * TODO document
     */
    @FXML
    public void handleTitleSearchButtonAction(){
        // set the searchField prompt text
        searchField.setPromptText("Inserisci il titolo della canzone");

        // make the yearField not visibile and disable
        yearField.setDisable(true);
        yearField.setVisible(false);

        // change style of searchFilterButtons
        guiUtilities.setNodeStyle(titleSearchBtn, "searchFilterButton", "searchFilterButtonClicked");
        guiUtilities.setNodeStyle(authorAndYearSearchBtn, "searchFilterButtonClicked", "searchFilterButton");

        // set the filtered
        filteredSearch = "title";

        // remove the last search
        handleRemoveSearchButtonAction();
    }

    /**
     * TODO document
     */
    public void handleAuthorAndYearSearchButtonAction(){
        // TODO implementare cosa deve accadere quando viene premuto questo pulsante
        // set the searchField prompt text
        searchField.setPromptText("Inserisci l'autore della canzone");

        // make the yearField not disable and visible
        yearField.setDisable(false);
        yearField.setVisible(true);

        // change style of searchFilterButtons
        guiUtilities.setNodeStyle(authorAndYearSearchBtn, "searchFilterButton", "searchFilterButtonClicked");
        guiUtilities.setNodeStyle(titleSearchBtn, "searchFilterButtonClicked", "searchFilterButton");

        // set the filtered
        filteredSearch = "authorAndYear";

        // remove the last search
        handleRemoveSearchButtonAction();
    }

    /**
     * TODO document
     * @param song
     * @param row
     */
    private void setNewSongFound(Canzone song, int row){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("song.fxml"));
            Node song_pane = fxmlLoader.load();

            // get song controller, this it serves for set song name and author name of the song
            SongController songController = fxmlLoader.getController();
            songController.setData(song);

            // add the song pane loded to songsPane list
            songsPane.add(song_pane);

            // add the fxml song view in the gridPane
            gridPane.add(song_pane, 0, row);
            // set the margin between songs (the space between songs)
            GridPane.setMargin(song_pane, new Insets(10));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO document
     */
    private void removeLastSearch(){
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

    /**
     * TODO document
     */
    public void showRemoveSearchBtn(){
        removeSearchBtn.setDisable(false);
        removeSearchImg.setImage(guiUtilities.getImage("removeSearch"));
        infoLabel.setVisible(true);
    }

    /**
     * todo document
     */
    public void hideRemoveSearchBtn(){
        removeSearchBtn.setDisable(true);
        removeSearchImg.setImage(guiUtilities.getImage("search"));
        infoLabel.setVisible(false);
    }
}
