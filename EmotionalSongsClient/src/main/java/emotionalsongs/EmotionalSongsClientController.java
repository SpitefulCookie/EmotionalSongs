package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the main view of the client application.
 * <p>
 *     This class is responsible for managing the user interface and behavior of the main client view.
 * </p>
 *
 *  @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class EmotionalSongsClientController implements Initializable {

    protected static final int HEIGHT = 750;
    protected static final int WIDTH = 1200;

    private static final int MAX_WIDTH_SIDEBAR = 180;
    private static final int MIN_WIDTH_SIDEBAR = 100;

    private static double xOffset = 0;
    private static double yOffset = 0;

    private Stage exitStage;

    @FXML private BorderPane pane;
    @FXML private VBox sideBar;
    @FXML private BorderPane dynamicPane;
    @FXML private Button closeBtn;
    @FXML private Button maximizedStageBtn;
    @FXML private Button minimizeStageBtn;
    @FXML private Button searchBtn;
    @FXML private Button playlistBtn;
    @FXML private Button userBtn;
    @FXML private Button exitBtn;
    @FXML private Button resizeSidebarBtn;
    @FXML private ImageView resizeSidebarImg;
    @FXML private ImageView maximizedImg;

    private static BorderPane dynamicPane_;

    private GUIUtilities guiUtilities;

    private double X;
    private double Y;

    private boolean sideBarIsOpen;
    private boolean isMaximized;

    private static boolean isGuest;
    private static String username;

    private static boolean userDataRetrieved = false;

    private static Button[] buttonsSideBar;

    /**
     * Initializes the client main view.
     * <p>
     *     This method is automatically called when the JavaFX scene associated with this controller is loaded.
     *     It sets up the initial state of UI components.
     * </p>
     *
     * @param url The URL of the FXML file.
     * @param resourceBundle The ResourceBundle associated with the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dynamicPane_ = dynamicPane;

        if(buttonsSideBar == null){
            buttonsSideBar = new Button[4];
            buttonsSideBar[0] = searchBtn;
            buttonsSideBar[1] = playlistBtn;
            buttonsSideBar[2] = userBtn;
            buttonsSideBar[3] = exitBtn;
        }

        guiUtilities = GUIUtilities.getInstance();

        /*
        set the boolean variable sideBarIsOpen, which tells me if the sidebar is open, at the first sidebar is
        closed so the value of sideBarIsOpen is false.
         */
        sideBarIsOpen = false;

        /*
         set the boolean variable isMaximized, which tells me if the stage is maximzed or not, at firt the
         variable has value false because the stage isn't maximized
         */
        isMaximized = false;

        // at first the dynamic pane contains the search pane
        setDynamicPane("search.fxml");

        // set button style
        guiUtilities.setNodeStyle(searchBtn, "buttonSideBar", "buttonSideBarClicked");
    }

    /**
     * Method that handles the behaviour of the {@link EmotionalSongsClientController#closeBtn}.
     * <p>
     *     When the {@link EmotionalSongsClientController#closeBtn} is clicked, the client application is closed.
     * </p>
     */
    @FXML
    public void handleCloseButtonAction(){
        //EmotionalSongs.disconnectClient();
        EmotionalSongsClient.getStage().close();
        Platform.exit();
        System.exit(0);
    }

    /**
     * Method that handles the behaviour of the {@link EmotionalSongsClientController#maximizedStageBtn}.
     *
     *     When the {@link EmotionalSongsClientController#maximizedStageBtn} is clicked, depending on the value of
     *     the {@link EmotionalSongsClientController#isMaximized}, the client application is either maximised or
     *     reset to the default size:
     *     <ol>
     *         <li>
     *             If {@link EmotionalSongsClientController#isMaximized} is {@code false}, the client application is maximised.
     *         </li>
     *         <li>
     *             If {@link EmotionalSongsClientController#isMaximized} is {@code true}, the client application
     *             is returned to its default size.
     *         </li>
     *     </ol>
     *
     */
    @FXML
    public void handleMaximizedStageButtonAction(){
        Stage stage = EmotionalSongsClient.getStage();

        /*
        verifico se lo stage è già massimizzato, se si quando premerò il pulsante di "massimizzazione"
        lo stage ritornerà alle dimensioni di default, altrimenti se non è già massimizzato, viene
        massimizzato
        */

        if(!isMaximized){ // se non è massimizzato
            /*
            ricavo la X e la Y, mi serviranno quando andrò a "demassimizzare" lo stage, quindi quando
            lo riporto alle dimensioni di default.
            */

            X = stage.getX();
            Y = stage.getY();

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

            isMaximized = true;
        }else{ // altrimenti
            stage.setX(X);
            stage.setY(Y);
            stage.setWidth(WIDTH);
            stage.setHeight(HEIGHT);

            isMaximized = false;
        }
    }

    /**
     * Method that handles the behaviour of the {@link EmotionalSongsClientController#minimizeStageBtn}.
     * <p>
     *     When the {@link EmotionalSongsClientController#minimizeStageBtn} is clicked, the client application is minimize.
     * </p>
     */
    @FXML
    public void handleMinimizeStageButtonAction(){
        EmotionalSongsClient.getStage().setIconified(true);
    }

    /**
     * Method that handles the behaviour of the {@link EmotionalSongsClientController#searchBtn}.
     * <p>
     *     When the {@link EmotionalSongsClientController#searchBtn} is clicked, the {@link Node} controlled by the
     *     {@link SearchController} class for searching songs is loaded and added to the
     *     {@link EmotionalSongsClientController#dynamicPane}.
     * </p>
     */
    @FXML
    public void handleSearchButtonAction(){
        //set button style
        setClickedStyle(searchBtn);

        try{
            // load the search pane
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("search.fxml"));
            Node search_node = fxmlLoader.load();

            guiUtilities.addNode("search.fxml", search_node);

            // add search pane to dynamicPane
            setDynamicPane(search_node);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Method that handles the behaviour of the {@link EmotionalSongsClientController#playlistBtn}.
     * <p>
     *     When the {@link EmotionalSongsClientController#playlistBtn} is clicked, the {@link Node} controlled by the
     *     {@link AllPlaylistController} class to display the user's playlists is loaded and added to the
     *     {@link EmotionalSongsClientController#dynamicPane}.
     * </p>
     */
    @FXML
    public void handlePlaylistButtonAction(){
        // set button style
        setClickedStyle(playlistBtn);

        // add playlist pane to dynamicPane
        setDynamicPane("allPlaylist.fxml");
    }

    /**
     * Method that handles the behaviour of the {@link EmotionalSongsClientController#userBtn}.
     *
     *     When the {@link EmotionalSongsClientController#userBtn} is clicked:
     *     <ol>
     *         <li>
     *             If {@link EmotionalSongsClientController#isGuest} is {@code true}, the {@link Node} controlled by the
     *             {@link GuestController} class is loaded and added to the {@link EmotionalSongsClientController#dynamicPane}.
     *         </li>
     *         <li>
     *             If {@link EmotionalSongsClientController#isGuest} is {@code false}, the {@link Node} controlled by the
     *             {@link UserController} class to display user data is loaded and added to the
     *             {@link EmotionalSongsClientController#dynamicPane}.
     *         </li>
     *     </ol>
     *
     */
    @FXML
    public void handleUserButtonAction(){

        if(isGuest){ // è guest

            // add guest pane to dynamicPane
            setDynamicPane(guiUtilities.getNode("guest.fxml"));

        }else { // non è guest --> è un utente registrato

            // controllo se i dati dell'utente non sono già stati recuperati
            if(!userDataRetrieved) {

                try{
                    // load the user pane
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user.fxml"));
                    Node user_pane = fxmlLoader.load();

                    // set the user
                    UserController userController = fxmlLoader.getController();
                    boolean checkUserDataRetrieved = userController.setUser(EmotionalSongsClientController.getUsername());

                    /*
                     aggiorno il node nella hashMap appNode, setto il dynamicPane e cambio lo stile del userBtn
                     solo se i dati dell'utente sono stati recuperati
                     */
                    if(checkUserDataRetrieved) {
                        // update the node in the hashMap appNode contained in the guiUtilities class
                        guiUtilities.addNode("user.fxml", user_pane);

                        // set button style
                        setClickedStyle(userBtn);

                        // add the user pane to dynamicPane
                        setDynamicPane(user_pane);
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }

            }else{

                // set button style
                setClickedStyle(userBtn);

                // add user pane to dynamicPane
                setDynamicPane(guiUtilities.getNode("user.fxml"));
            }
        }
    }

    /**
     * Method that handles the behaviour of the {@link EmotionalSongsClientController#exitBtn}.
     * <p>
     *     When the {@link EmotionalSongsClientController#exitBtn} is clicked, the {@link Stage} (window) controlled
     *     by the {@link ExitController} class is displayed, allowing the user to logout.
     * </p>
     */
    @FXML
    public void handleExitButtonAction(){

        Stage exitStage = new Stage();

            exitStage.setScene(GUIUtilities.getInstance().getScene("exit.fxml"));
            exitStage.initStyle(StageStyle.UNDECORATED);
            exitStage.setResizable(false);
            exitStage.initModality(Modality.APPLICATION_MODAL);
            exitStage.show();

    }

    /**
     * Method that handles the behaviour of the {@link EmotionalSongsClientController#resizeSidebarBtn}.
     *
     *     When the {@link EmotionalSongsClientController#resizeSidebarBtn} is clicked:
     *     <ol>
     *         <li>
     *             If {@link EmotionalSongsClientController#sideBarIsOpen} is {@code false}, the sidebar is opened.
     *         </li>
     *         <li>
     *             If {@link EmotionalSongsClientController#sideBarIsOpen} is {@code true}, the sidebar is closed.
     *         </li>
     *     </ol>
     *
     */
    @FXML public void handleResizeSidebarButtonAction(){
        if(!sideBarIsOpen) { // se la sidebar NON è aperta, la apro.

            // Resize the width of the sidebar
            sideBar.setPrefWidth(MAX_WIDTH_SIDEBAR);
            sideBar.setMaxWidth(MAX_WIDTH_SIDEBAR);

            // change the image of the button resizeSidebar
            resizeSidebarImg.setImage(guiUtilities.getImage("closeSideBar"));

            // set the text of the sideBar buttons
            searchBtn.setText(" Cerca");
            playlistBtn.setText(" Playlist");
            userBtn.setText(" " + username);
            exitBtn.setText(" Esci");

            // set the alignment of sideBar buttons
            setButtonsSideBarAlignment(buttonsSideBar, Pos.BASELINE_LEFT);

            // now the sidebar is open
            sideBarIsOpen = true;

        }else{ // altrimenti se è aperta la chiudo

            // Resize the width of the sidebar
            sideBar.setPrefWidth(MIN_WIDTH_SIDEBAR);
            sideBar.setMaxWidth(MIN_WIDTH_SIDEBAR);

            // change the image of the button resizeSidebar
            resizeSidebarImg.setImage(guiUtilities.getImage("openSideBar"));

            // set the alignment of sideBar buttons
            setButtonsSideBarAlignment(buttonsSideBar, Pos.CENTER);

            // set the text of the sideBar button
            searchBtn.setText("");
            playlistBtn.setText("");
            userBtn.setText("");
            exitBtn.setText("");

            // now the sidebar is closed
            sideBarIsOpen = false;
        }
    }

    /**
     * Enables the dragging behavior of the application window using mouse events.
     *
     * This method sets up the dragging behavior of the application window using mouse events; When called, the method
     * creates the following event handlers for the application's UI components:
     * <ol>
     * <li> OnMousePressed: This event handler is triggered when the user presses the mouse button while the cursor is
     *    within the boundaries of the pane. It calculates the initial offset (`xOffset` and `yOffset`) between the
     *    mouse cursor position and the top-left corner of the application window.
     * <li> OnMouseDragged: This event handler is triggered when the user moves the mouse cursor after pressing the mouse
     *    button on the pane. It updates the position of the application window according to the cursor movement, thereby
     *    simulating the dragging behavior of the window.
     * </ol>
     *
     */
    @FXML protected void moveWindows() {

        this.pane.setOnMousePressed(event -> {

            xOffset = event.getScreenX() - EmotionalSongsClient.getStage().getX();
            yOffset = event.getScreenY() - EmotionalSongsClient.getStage().getY();

        });

        this.pane.setOnMouseDragged(event -> {

            if (xOffset == 0 && yOffset == 0) {
                // questa porzione di codice è necessaria per evitare che la finestra snappi alle coordinate 0,0 (cosa che avviene alla primo trascinamento della schermata)
                calculateGap(event);
            }

            EmotionalSongsClient.getStage().setX(event.getScreenX() - xOffset);
            EmotionalSongsClient.getStage().setY(event.getScreenY() - yOffset);
        });

    }


    /**
     * Calculates the gap between the mouse cursor position and the top-left corner of the application window.<br><br>
     *
     * This method is used to calculate the gap between the current mouse cursor position and the top-left corner of
     * the application window.
     * The values updated within this method are used to move the application window.
     *
     * @param event The MouseEvent representing the mouse event that triggered the calculation.
     */
    private void calculateGap(MouseEvent event){
        xOffset = event.getScreenX() - EmotionalSongsClient.getStage().getX();
        yOffset = event.getScreenY() - EmotionalSongsClient.getStage().getY();
    }

    /**
     * Method that loads the {@link FXML} file passed as a parameter and adds it to
     * the {@link EmotionalSongsClientController#dynamicPane}
     *
     * @param fxml represents the {@link FXML} file to be loaded.
     */
    public static void setDynamicPane(String fxml){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongsClient.class.getResource(fxml));
            Node pane = fxmlLoader.load();

            dynamicPane_.setCenter(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method which adds the {@link Node} passed as a parameter to {@link EmotionalSongsClientController#dynamicPane}.
     *
     * @param pane represents the {@link Node} to be added.
     */
    public static void setDynamicPane(Node pane){
        /*
        metodo che svolge una funzione uguale a quella svolta dal metodo setDybamicPane(String fxml), l'unica
        differenza è che questo viene invocato quando devo anche effettuare operazioni sul controller,
        di conseguenza il caricamento del file fxml deve avvenire esternamente dal metodo.
         */
        dynamicPane_.setCenter(pane);
    }

    /**
     * Method that sets the user.
     * <p>
     *     This method sets the {@link EmotionalSongsClientController#username} with the {@code String} passed as parameter and
     *     the {@link EmotionalSongsClientController#isGuest} with the {@code boolean} passed as parameter and loads the user's playlists
     *     via the appropriate method {@link AllPlaylistController#loadPlaylist()} if the user is not a guest.
     * </p>
     *
     * @param username represents the {@code String} with which to set the {@link EmotionalSongsClientController#username}.
     * @param userIsGuest represents the {@code boolean} with which to set the {@link EmotionalSongsClientController#isGuest}.
     */
    public void setUser(String username, boolean userIsGuest){
        // set the username
        setUsername(username);

        // change the value of boolean variable isGuest
        setIsGuest(userIsGuest);

        /*
        verifico se l'utente che è entrato nel client è un utente guest ovvero non regitrato, se così
        fosse vado a disabilitare e a rendere invisibile il playlistBtn inquando l'utente guest non può
        creare playlist ma solo cercare canzoni nel repository e vedere un prospetto riassuntivo delle emozioni
        inserite nelle canzoni cercate
         */
        if(userIsGuest){
            playlistBtn.setDisable(true);
            playlistBtn.setVisible(false);
        }else{
            // se l'utente non è guest carico le playlist di questo utente
            AllPlaylistController.loadPlaylist();
        }

    }

    /**
     * Method modifying the style of the button passed as a parameter.
     *
     * @param button represents the {@link Button} to which the style will be changed.
     */
    private void setClickedStyle(Button button){
        /*
        metodo che definisce lo style del pulsante quando esso viene premuto. esso controlla qual'è il
        button nella lista che è stato cliccato e gli va a impostare come style il setHoverStyle, agli
        altri button invece imposta come style il setBaseStyle.
         */
        for (Button value : buttonsSideBar) {
            if (value == button) {
                guiUtilities.setNodeStyle(value, "buttonSideBar", "buttonSideBarClicked");
            } else {
                guiUtilities.setNodeStyle(value, "buttonSideBarClicked", "buttonSideBar");
            }
        }
    }

    /**
     * Method that sets the alignment of the buttons contained in the array passed as a parameter to the
     * {@link Pos} passed as a parameter.
     *
     * @param buttonsSideBar represents the array containing the buttons of which to set the alignment.
     * @param pos represents the position where the alignment of each button will be set.
     */
    private void setButtonsSideBarAlignment(Button[] buttonsSideBar, Pos pos){
        /*
        metodo che va a settare l'allineamento dei pulsanti della sideBar
         */
        for(Button button : buttonsSideBar){
            button.setAlignment(pos);
        }
    }

    /**
     * Method that sets the {@link EmotionalSongsClientController#username}.
     *
     * @param user represents the {@code String} with which to set the {@link EmotionalSongsClientController#username}.
     */
    public void setUsername(String user){
        username = user;
    }

    /**
     * Method that return the {@link EmotionalSongsClientController#username}.
     *
     * @return The {@code String} contained in the {@link EmotionalSongsClientController#username}.
     */
    public static String getUsername(){
        return username;
    }

    /**
     * Method that sets {@link EmotionalSongsClientController#isGuest}.
     *
     * @param userIsGuest represents the {@code boolean} with which to set the {@link EmotionalSongsClientController#isGuest}.
     */
    public void setIsGuest(boolean userIsGuest){
        isGuest = userIsGuest;
    }

    /**
     * Method that return {@link EmotionalSongsClientController#isGuest}.
     *
     * @return The value of {@link EmotionalSongsClientController#isGuest}.
     */
    public static boolean getIsGuest(){
        return isGuest;
    }

    /**
     * Method that sets the {@link EmotionalSongsClientController#userDataRetrieved}.
     *
     * @param value represents the {@code boolean} with which to set the {@link EmotionalSongsClientController#userDataRetrieved}.
     */
    protected static void setUserDataRetrieved(boolean value){
        userDataRetrieved = value;
    }

}