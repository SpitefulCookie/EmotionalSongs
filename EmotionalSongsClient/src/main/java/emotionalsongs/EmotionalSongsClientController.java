package emotionalsongs;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

/*
TODO:
   - vedere se si riesce a sistemare lo style dei pulsanti, ovvero sistemare quando il mouse passa sopra al
     pusante, se non si riesce amen.
   - implenatare stage per selezionare le playlist quando si preme sul pulsante aggiungi a una playlist.
   - implementare stage per visualizzazione emozioni.
   - sistemare ricerca impostando il filtro: ricerca per titolo o ricerca per autore e anno.
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
    // @FXML private Tooltip exitTooltip;

    private static BorderPane dynamicPane_;

    private GUIUtilities guiUtilities;

    private double X;
    private double Y;

    private boolean sideBarIsOpen;
    private boolean isMaximized;

    private static boolean isGuest;
    private static String username;

    private static Button[] buttonsSideBar;

    /**
     * TODO document
     * @param url
     * @param resourceBundle
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
     * TODO document
     * @param event
     */
    @FXML
    public void handleCloseButtonAction(ActionEvent event){
        EmotionalSongsClient.disconnectClient();
        EmotionalSongsClient.getStage().close();
        // TODO added the following lines as an attempt to fix the client remaining open in the background after
        //  exiting the application, remove if it doesn't work
        Platform.exit();
        System.exit(0);
    }

    /**
     * TODO document
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
     * TODO document
     */
    @FXML
    public void handleMinimizeStageButtonAction(){
        EmotionalSongsClient.getStage().setIconified(true);
    }

    /**
     * TODO document
     */
    @FXML
    public void handleSearchButtonAction(){
        // set button style
        setClickedStyle(searchBtn);

        // add search pane to dynamicPane
        setDynamicPane("search.fxml");
    }

    /**
     * TODO document
     */
    @FXML
    public void handlePlaylistButtonAction(){
        // set button style
        setClickedStyle(playlistBtn);

        // add playlist pane to dynamicPane
        setDynamicPane("allPlaylist.fxml");
    }

    /**
     * TODO document
     */
    @FXML
    public void handleUserButtonAction(){
        // set button style
        setClickedStyle(userBtn);

        if(isGuest){ // è guest
            // TODO creare il file fxml per l'utente guest e settarlo.
        }else { // non è guest --> è un utente registrato
            // TODO cambiare il nome del documento fxml in RegistredUser

            // add user pane to dynamicPane
            setDynamicPane("user.fxml");
            System.out.println("username: " + username);
        }
    }

    /**
     * TODO document
     */
    @FXML
    public void handleExitButtonAction(){
        //System.out.println("Exit button clicked");
        /*
        - implementare il frame che chiede all'utente se vuole realmente uscire dall'applicazione.
        */

            exitStage = new Stage();

            exitStage.setScene(GUIUtilities.getInstance().getScene("exit.fxml"));
            exitStage.initStyle(StageStyle.UNDECORATED);
            exitStage.setResizable(false);
            /*
            tramite initModality rendo il main pane non cliccabile, di fatto lo disattivo, grazie a ciò
            quando apro lo exit stage se premo al di fuori dello stage, esso non si chiude.
            SE NON SI CAPISCE COSA FA, SCRIVIMI.
             */
            exitStage.initModality(Modality.APPLICATION_MODAL);
            exitStage.show();

    }

    @FXML public void handleResizeSidebarButtonAction(){
        System.out.println("pulsante resize sidebar premuto");
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
     * TODO document put in its own class
     */
    @FXML protected void moveWindows() {

        this.pane.setOnMousePressed(new EventHandler<MouseEvent>() {
            //xOffset = stage.getX() - event.getX() ;
            //yOffset = stage.getY() - event.getY() ;

            @Override
            public void handle(MouseEvent event) {

                xOffset = event.getScreenX() - EmotionalSongsClient.getStage().getX();
                yOffset = event.getScreenY() - EmotionalSongsClient.getStage().getY();

            }
        });

        this.pane.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event){

                if (xOffset == 0 && yOffset==0) {
                    // questa porzione di codice è necessaria per evitare che la finestra snappi alle coordinate 0,0 (cosa che avviene alla primo trascinamento della schermata)
                    calculateGap(event);
                }

                EmotionalSongsClient.getStage().setX(event.getScreenX() - xOffset);
                EmotionalSongsClient.getStage().setY(event.getScreenY() - yOffset);
            }
        });

    }


    /**
     * TODO document
     * @param event
     */
    private void calculateGap(MouseEvent event){
        xOffset = event.getScreenX() - EmotionalSongsClient.getStage().getX();
        yOffset = event.getScreenY() - EmotionalSongsClient.getStage().getY();
    }

    /**
     * TODO document
     * @param fxml
     */
    public static void setDynamicPane(String fxml){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(EmotionalSongsClient.class.getResource(fxml));
            Node pane = fxmlLoader.load();

            dynamicPane_.setCenter(pane);
            dynamicPane_.getCenter().setStyle("-fx-backgroud-color: transparent;");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * TODO document
     * @param pane
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
     * TODO document
     * @param username
     * @param userIsGuest
     */
    public void setUser(String username, boolean userIsGuest){
        // set the username
        setUsername(username);

        // change the value of boolean variable isGuest
        setIsGuest(userIsGuest);

        System.out.println("Utente is guest ? " + isGuest);

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
     * TODO document
     * @param button
     */
    private void setClickedStyle(Button button){
        /*
        metodo che definisce lo style del pulsante quando esso viene premuto. esso controlla qual'è il
        button nella lista che è stato cliccato e gli va a impostare come style il setHoverStyle, agli
        altri button invece imposta come style il setBaseStyle
         */
        for (int i = 0; i < buttonsSideBar.length; i++){
            if(buttonsSideBar[i] == button){
                guiUtilities.setNodeStyle(buttonsSideBar[i], "buttonSideBar", "buttonSideBarClicked");
            }else{
                guiUtilities.setNodeStyle(buttonsSideBar[i], "buttonSideBarClicked", "buttonSideBar");
            }
        }
    }

    /**
     * TODO document
     * @param buttonsSideBar
     * @param pos
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
     * TODO document
     * @param user
     */
    public void setUsername(String user){
        username = user;
    }

    /**
     * TODO document
     * @return
     */
    public static String getUsername(){
        return username;
    }

    /**
     * TODO document
     * @param userIsGuest
     */
    public void setIsGuest(boolean userIsGuest){
        isGuest = userIsGuest;
    }

    /**
     * TODO document
     * @return isGuest
     */
    public static boolean getIsGuest(){
        return isGuest;
    }

}