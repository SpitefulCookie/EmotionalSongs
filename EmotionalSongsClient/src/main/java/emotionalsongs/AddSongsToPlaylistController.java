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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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

/**
 * TODO:
 *  1- mettere il vincolo alla yearLabel che accetta solo l'inserimento di numeri, usando l'apposito metodo della
 *     classe GUIUtilites
 *  2- quando viene visualizzata una canzone, verificare se essa è già presente nella lista songsToAdd, e se
 *     così fosse impostare isAdded su true nel meotodo newSongsFound, così facendo l'utente sà già che quella
 *     canzone è già nella lista delle canzoni da aggiungere. NOTA: quando avremo l'interazione con il db
 *     questo controllo deve avvenire sullo songUUID (questo perchè si possono avere canzoni con lo stesso nome)
 */
public class AddSongsToPlaylistController implements Initializable {

    @FXML private Button removeSearchBtn;
    @FXML private Button addSongsToPlaylistBtn;
    @FXML private Button advancedSearchBtn;
    @FXML private Button annullaBtn;
    @FXML private Button titleSearchBtn;
    @FXML private Button authorAndYearSearchBtn;
    @FXML private Button viewSongsAddedBtn;
    @FXML private TextField searchField;
    @FXML private TextField yearField;
    @FXML private Label infoLabel;
    @FXML private ImageView removeSearchImg;
    @FXML private ScrollPane scrollPane;
    @FXML private GridPane gridPane;
    @FXML private Label numSongsAddedLabel;
    @FXML private HBox advancedSearchBox;


    private static Label numSongsAddedLabel_;

    private static String playlistName;
    private static IntegerProperty numSongAdded;

    private GUIUtilities guiUtilities;
    private String filteredSearch;

    private boolean advancedSearchActivated;
    private static List<Canzone> songsToAdd;
    private List<Node> songsPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // setto l'auto resize dei componenti/elementi che si trovano nello scroll pane
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        numSongsAddedLabel_ = numSongsAddedLabel;

        numSongAdded = new SimpleIntegerProperty();

        // initially the advanced search is not activated
        advancedSearchActivated = false;

        // create a new guiUtilities object with pattern singleton
        guiUtilities = GUIUtilities.getInstance();

        // initialize the songsPane list, which contains the song that will be added to the playlist
        songsToAdd = new ArrayList<Canzone>();

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

        /*
        add listener to numSongAdded, this listener serves for make viewSongsAddedBtn disable or not and this
        update must become in "real time" that's why need of this listener
         */
        numSongAdded.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(numSongAdded.get() > 0){
                    viewSongsAddedBtn.setDisable(false);
                    addSongsToPlaylistBtn.setDisable(false);
                }else{
                    viewSongsAddedBtn.setDisable(true);
                    addSongsToPlaylistBtn.setDisable(true);
                }
            }
        });
    }

    /**
     * TODO document
     */
    @FXML
    public void handleSearchButtonAction(KeyEvent key) throws RemoteException{ // TODO rimuovere eccezzione

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

                    // set the content of scroll pane
                    scrollPane.setContent(gridPane);

                    // viuslizzo le canzoni restituite dal db
                    int row = 0;
                    for (Canzone song : songs) {
                        setNewSongFound(song, songIsAlreadyAdded(song), row);
                        row ++;
                    }
                } else {
                    // DEBUG TODO remove
                    System.out.println("la ricerca non ha protato a nessun risultato");

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
            // in caso di connessione persa con il server, visualizzo l'apposita finestra
            e.printStackTrace();

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
     */
    @FXML
    public void handleViewSongsAddedButtonAction(){
        // TODO implementare cosa deve accadere quando viene premuto questo pulsante
        System.out.println("bottone visualizza canzoni aggiunte schiaccato");

        // remove the search
        handleRemoveSearchButtonAction();

        // add to gridPane the songs added
        for(int i = 0; i < songsToAdd.size(); i++){
            setNewSongFound(songsToAdd.get(i), true, i);
        }

    }

    /**
     * TODO document
     */
    @FXML
    public void handleAddSongsToPlaylistButtonAction(){
        // debug
        System.out.println("add song to playlist button cliked for playlist: " + playlistName);

        // add the songs to the playlist
        SelectedPlaylistController.addNewSongs(songsToAdd);

        // close the stage
        GUIUtilities.closeStage(addSongsToPlaylistBtn);
    }

    /**
     * TODO document
     */
    @FXML
    public void handleAnnullaButtonAction(){
        GUIUtilities.closeStage(annullaBtn);
    }

    /**
     * TODO document
     */
    public void handleViewSongsAddedButtonMouseMovedAction(){
        /*
        metodo che va a modificare la lunghezza e il testo del viewSongsAddedButton quando il mouse ci passa sopra
        questo per andare a cerare una specie di 'animazione'
         */
        viewSongsAddedBtn.setPrefWidth(275);
        viewSongsAddedBtn.setText("Visualizza le canzoni aggiunte");
    }

    /**
     * TODO document
     */
    public void handleViewSongsAddedButtonMouseExitedAction(){
        /*
        metodo che va a ripristinare la lunghezza e il testo del viewSongsAddedButton quando il mouse si 'toglie'
        dal pulsante questo per andare a cerare una specie di 'animazione'
         */
        viewSongsAddedBtn.setPrefWidth(40);
        viewSongsAddedBtn.setText("");
    }

    /**
     * TODO document
     * @param song
     * @param isAdded
     * @param row
     */
    private void setNewSongFound(Canzone song, boolean isAdded, int row){
        /*
         isAdded mi indica se la canzone è già stata aggiunta alla lista delle canzoni da aggiungere (songsToAdd),
         mentre row mi indica la riga del gridPane in cui andare a inserire la canzone
         */
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("songToAdd.fxml"));
            Node song_pane = fxmlLoader.load();

            // get song controller, this it serves for set song name and author name of the song
            SongToAddController songToAddController = fxmlLoader.getController();
            songToAddController.setSong(song, playlistName, isAdded);

            // add the song pane laoded to songsPane list
            songsPane.add(song_pane);

            // add the fxml song view in the gridPane
            gridPane.add(song_pane, 0, row);
            // set the margin between songs (the space between songs)
            GridPane.setMargin(song_pane, new Insets(10));

        }catch (IOException e){
            System.out.println("Songs view PROBLEM !");
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
     * @param song
     */
    public static void addSong(Canzone song){
        /*
         metodo che aggiunge la canzone passata come argomento alla lista songsToAdd, tale metodo viene
         invocato dal metodo handleAddSongToPlaylistAction della classe SongsToAddController
         */
        System.out.println("Aggiungo la canzone: " + song.getTitolo() + "alla playlist " + playlistName);

        // add song to list
        songsToAdd.add(song);

        // update the numSongsAddedLabel
        numSongAdded.set(numSongAdded.get() + 1);
        if(numSongAdded.get() == 1){
            numSongsAddedLabel_.setText(numSongAdded.get() + " canzone aggiunta");
        }else {
            numSongsAddedLabel_.setText(numSongAdded.get() + " canzoni aggiunte");
        }
    }

    /**
     * TODO document
     * @param song
     */
    public static void removeSong(Canzone song){
        /*
         metodo che rimuove la canzone passata come argomento dalla lista songsToAdd, tale metodo viene
         invocato dal metodo handleAddSongToPlaylistAction della classe SongsToAddController
         */
        System.out.println("Rimuovo la canzone: " + song.getTitolo() + "dalla playlist " + playlistName);

        /*
         remove song to list, la rimuozione della canzone non avviene semplicemente facendo
         songToAdd.remove(Canzone song) questo perchè la rimozione deve avvenire in base allo songUUID
         */
        for(int i = 0; i < songsToAdd.size(); i++){
            if(songsToAdd.get(i).getSongUUID().equals(song.getSongUUID())){
                songsToAdd.remove(songsToAdd.get(i));
            }
        }

        System.out.println("size lista songToAdd: " + songsToAdd.size()); // TODO remove

        // update the numSongsAddedLabel
        numSongAdded.set(numSongAdded.get() - 1);
        if(numSongAdded.get() == 1){
            numSongsAddedLabel_.setText(numSongAdded.get() + " canzone aggiunta");
        }else {
            numSongsAddedLabel_.setText(numSongAdded.get() + " canzoni aggiunte");
        }
    }

    /**
     * TODO document
     * @param playlist_name
     */
    public void setPlaylist(String playlist_name){
        playlistName = playlist_name;
    }

    /**
     * TODO document
     * @param song
     */
    public boolean songIsAlreadyAdded(Canzone song){
        /*
        metodo che verifica se la canzone passata come argomento song è già stata aggiunta alla lista
        songsToAdd
         */
        for(int i = 0; i < songsToAdd.size(); i++){
            if(songsToAdd.get(i).getSongUUID().equals(song.getSongUUID())){
                return true;
            }
        }
        return false;
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
