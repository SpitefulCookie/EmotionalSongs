package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
 * Controller class controlling the window for adding songs to a specific playlist.
 * <p>
 *     This class implements the JavaFX Initializable interface, which is automatically called when the associated FXML dialog
 *     is loaded. It Initialises the UI components.
 * </p>
 * <p>
 *     The class handles the configuration of the behaviour of the buttons: 'removeSearchBtn' to remove the search performed,
 *     'addSongsToPlaylistBtn' to add the selected songs to the specific playlist, 'advancedSearchBtn' to set the search type,
 *     'annullaBtn' to cancel the operation of adding songs, 'titleSearchBtn' to set the search to: search by title,
 *     'authorAndYearSearchBtn' to set the search to: search by author and year, 'viewSongsAddedBtn' to view the currently added songs.
 * </p>
 *
 *  @author <a href="https://github.com/samuk52">Corallo Samuele</a>
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

    /**
     * Initializes the window for adding songs to a specific playlist.
     * <p>
     *     This method is called automatically when the JavaFX dialog associated with this controller is loaded.
     *     It initializes various UI components and sets up their behavior.
     * </p>
     *
     * @param url The URL of the FXML file.
     * @param resourceBundle The ResourceBundle associated with the FXML file.
     */
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
        songsToAdd = new ArrayList<>();

        // initialize the songsPane list
        songsPane = new ArrayList<>();

        // initially the search is set to titleSearch
        handleTitleSearchButtonAction();

        // aggiungo come vincolo alla yearField l'inserimento di soli numeri e di massimo 4 numeri
        GUIUtilities.forceNumericInput(yearField, 4);

        // add a listener to searchField, which displays removeSearchBtn.
        searchField.textProperty().addListener((observableValue, s, t1) -> {
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
                yearField.textProperty().addListener((observableValue1, s1, t11) -> {
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
        });

        /*
        add listener to numSongAdded, this listener serves for make viewSongsAddedBtn disable or not and this
        update must become in "real time" that's why need of this listener
         */
        numSongAdded.addListener((observableValue, number, t1) -> {
            if(numSongAdded.get() > 0){
                viewSongsAddedBtn.setDisable(false);
                addSongsToPlaylistBtn.setDisable(false);
            }else{
                viewSongsAddedBtn.setDisable(true);
                addSongsToPlaylistBtn.setDisable(true);
            }
        });
    }


    /**
     * Method that manages the behaviour of the song search operation.
     * <p>
     *     When the enter key on the keyboard is clicked, depending on the type of search (by title or by author and year),
     *     the songs found are displayed; if no song is found, a message is displayed informing the user of this.
     * </p>
     * @param key The {@link KeyEvent} activated by pressing the enter key on the keyboard.
     */
    @FXML
    public void handleSearchButtonAction(KeyEvent key) {

        try {

            // la ricerca viene effettuata quando viene premuto il pulsante di INVIO
            if (key.getCode() == KeyCode.ENTER) {

                // remove the last search before the new search
                removeLastSearch();

                Set<Canzone> songs;

                if (filteredSearch.equalsIgnoreCase("title")) {

                    if(searchField.getText().isEmpty()){
                        return;
                    }


                    // interrogo il db per farmi restituire le canzoni carecate
                    songs = EmotionalSongsClient.repo.ricercaCanzone(searchField.getText());

                } else {

                    if(searchField.getText().isEmpty() || yearField.getText().isEmpty()){
                        return;
                    }

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
     * Method that handles the behaviour of the {@link AddSongsToPlaylistController#removeSearchBtn}.
     * <p>
     *     When the {@link AddSongsToPlaylistController#removeSearchBtn} is clicked, the search is reset.
     * </p>
     */
    @FXML
    public void handleRemoveSearchButtonAction(){
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
     * Method that handles the behaviour of the {@link AddSongsToPlaylistController#advancedSearchBtn}.
     * <p>
     *     When the {@link AddSongsToPlaylistController#advancedSearchBtn} is clicked, its behavior varies based on whether:
     *     <ol>
     *         <li>
     *             the {@link AddSongsToPlaylistController#advancedSearchBox} is not visible, in this case, it is made visible
     *             and with it the buttons are also made visible: 'titleSearchBtn' and 'authorAndYearSearchBtn'
     *             which will allow filtering the search.
     *         </li>
     *         <li>
     *             the {@link AddSongsToPlaylistController#advancedSearchBox} is visible, in this case when the 'AdvancedSearchBtn' button is clicked,
     *             the advanced search box is made invisible.
     *         </li>
     *     </ol>
     *
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
     * Method that handles the behaviour of the {@link AddSongsToPlaylistController#titleSearchBtn}.
     * <p>
     *     When the {@link AddSongsToPlaylistController#titleSearchBtn} is clicked, the search is set to: search by title.
     * </p>
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
     * Method that handles the behaviour of the {@link AddSongsToPlaylistController#authorAndYearSearchBtn}.
     * <p>
     *     When the {@link AddSongsToPlaylistController#authorAndYearSearchBtn} is clicked,
     *     the search is set to: search by author and year.
     * </p>
     */
    public void handleAuthorAndYearSearchButtonAction(){

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
     * Method that handles the behaviour of the {@link AddSongsToPlaylistController#viewSongsAddedBtn}.
     * <p>
     *     When the {@link AddSongsToPlaylistController#viewSongsAddedBtn} is clicked, the songs currently added
     *     to the specific playlist are displayed.
     * </p>
     */
    @FXML
    public void handleViewSongsAddedButtonAction(){

        // remove the search
        handleRemoveSearchButtonAction();

        // add to gridPane the songs added
        for(int i = 0; i < songsToAdd.size(); i++){
            setNewSongFound(songsToAdd.get(i), true, i);
        }

    }

    /**
     * Method that handles the behaviour of the {@link AddSongsToPlaylistController#addSongsToPlaylistBtn}.
     * <p>
     *     When the {@link AddSongsToPlaylistController#addSongsToPlaylistBtn} is clicked, the songs currently
     *     added to {@link AddSongsToPlaylistController#songsToAdd} are added to the playlist.
     * </p>
     */
    @FXML
    public void handleAddSongsToPlaylistButtonAction(){
        // add the songs to the playlist
        SelectedPlaylistController.addNewSongs(songsToAdd);

        // close the stage
        GUIUtilities.closeStage(addSongsToPlaylistBtn);
    }

    /**
     * Method that handles the behaviour of the {@link AddSongsToPlaylistController#annullaBtn}.
     * <p>
     *     When the {@link AddSongsToPlaylistController#annullaBtn} is clicked, the operation is cancelled and the method closes the window containing the button.
     * </p>
     */
    @FXML
    public void handleAnnullaButtonAction(){
        GUIUtilities.closeStage(annullaBtn);
    }

    /**
     * Method that manages the display of the {@link AddSongsToPlaylistController#viewSongsAddedBtn} when the mouse moved over it.
     * <p>
     *     When the mouse is moved over the {@link AddSongsToPlaylistController#viewSongsAddedBtn}, the button text is set and its width is changed.
     * </p>
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
     * Method that manages the display of the {@link AddSongsToPlaylistController#viewSongsAddedBtn} when the mouse is exited.
     * <p>
     *     When the mouse leaves the {@link AddSongsToPlaylistController#viewSongsAddedBtn}, it is set to its initial state.
     * </p>
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
     * Method that adds the song passed as a parameter to the {@link AddSongsToPlaylistController#gridPane}.
     *
     * @param song represents the song to add to the {@link AddSongsToPlaylistController#gridPane}.
     * @param isAdded indicates if the song has already been added to the {@link AddSongsToPlaylistController#songsToAdd}.
     * @param row indicates the row of the {@link AddSongsToPlaylistController#gridPane} in which to insert the song.
     */
    private void setNewSongFound(Canzone song, boolean isAdded, int row){
        /*
         isAdded mi indica se la canzone è già stata aggiunta alla lista delle canzoni da aggiungere (songsToAdd),
         mentre row mi indica la riga del gridPane in cui andare a inserire la canzone
         */
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("songToAdd.fxml"));
            Node song_pane = fxmlLoader.load();

            // get song controller, this it serves for set song name and info of the song
            SongToAddController songToAddController = fxmlLoader.getController();
            songToAddController.setSong(song, playlistName, isAdded);

            // add the song pane laoded to songsPane list
            songsPane.add(song_pane);

            // add the fxml song view in the gridPane
            gridPane.add(song_pane, 0, row);
            // set the margin between songs (the space between songs)
            GridPane.setMargin(song_pane, new Insets(10));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method that restores the {@link AddSongsToPlaylistController#gridPane}, removing from it the songs
     * found since the last search.
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
     * Method that adds the song passed as a parameter to the {@link AddSongsToPlaylistController#songsToAdd},
     * this method is invoked by the {@link SongToAddController#handleAddSongToPlaylistAction}.
     * @param song represents the song to be added to the {@link AddSongsToPlaylistController#songsToAdd}.
     */
    public static void addSong(Canzone song){
        /*
         metodo che aggiunge la canzone passata come argomento alla lista songsToAdd, tale metodo viene
         invocato dal metodo handleAddSongToPlaylistAction della classe SongsToAddController
         */

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
     * Method that removes the song passed as a parameter to the {@link AddSongsToPlaylistController#songsToAdd},
     * this method is invoked by {@link SongToAddController#handleAddSongToPlaylistAction}.
     * @param song represents the song to be removed from the {@link AddSongsToPlaylistController#songsToAdd}.
     */
    public static void removeSong(Canzone song){
        /*
         metodo che rimuove la canzone passata come argomento dalla lista songsToAdd, tale metodo viene
         invocato dal metodo handleAddSongToPlaylistAction della classe SongsToAddController
         */

        /*
         remove song to list, la rimuozione della canzone non avviene semplicemente facendo
         songToAdd.remove(Canzone song) questo perchè la rimozione deve avvenire in base allo songUUID
         */
        for(int i = 0; i < songsToAdd.size(); i++){
            if(songsToAdd.get(i).getSongUUID().equals(song.getSongUUID())){
                songsToAdd.remove(songsToAdd.get(i));
            }
        }

        // update the numSongsAddedLabel
        numSongAdded.set(numSongAdded.get() - 1);
        if(numSongAdded.get() == 1){
            numSongsAddedLabel_.setText(numSongAdded.get() + " canzone aggiunta");
        }else {
            numSongsAddedLabel_.setText(numSongAdded.get() + " canzoni aggiunte");
        }
    }

    /**
     * Method that sets the {@link AddSongsToPlaylistController#playlistName}.
     * @param playlist_name represents the {@code String} that is passed to {@link AddSongsToPlaylistController#playlistName}.
     */
    public void setPlaylist(String playlist_name){
        playlistName = playlist_name;
    }

    /**
     * Method that checks if the song passed as a parameter has already been added to the list
     * {@link AddSongsToPlaylistController#songsToAdd}.
     * @param song represents the song to be checked.
     * @return A {@code boolean} indicating if the song has already been added to the {@link AddSongsToPlaylistController#songsToAdd}.
     */
    public boolean songIsAlreadyAdded(Canzone song){
        /*
        metodo che verifica se la canzone passata come argomento song è già stata aggiunta alla lista
        songsToAdd
         */
        for (Canzone canzone : songsToAdd) {
            if (canzone.getSongUUID().equals(song.getSongUUID())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that changes the display of the {@link AddSongsToPlaylistController#removeSearchBtn}.
     * <p>
     *    When invoked, the {@link AddSongsToPlaylistController#removeSearchBtn}. is made active, visible and its image is changed.
     * </p>
     */
    private void showRemoveSearchBtn(){
        removeSearchBtn.setDisable(false);
        removeSearchImg.setImage(guiUtilities.getImage("removeSearch"));
        infoLabel.setVisible(true);
    }

    /**
     * Method that changes the display of the {@link AddSongsToPlaylistController#removeSearchBtn}.
     * <p>
     *     When invoked, the {@link AddSongsToPlaylistController#removeSearchBtn} is disabled, made not visible and its image is changed.
     * </p>
     */
    private void hideRemoveSearchBtn(){
        removeSearchBtn.setDisable(true);
        removeSearchImg.setImage(guiUtilities.getImage("search"));
        infoLabel.setVisible(false);
    }

}
