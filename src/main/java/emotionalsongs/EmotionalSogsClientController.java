package emotionalsongs;

import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/*
TO DO : risolvere problema resize della finestra
 */

public class EmotionalSogsClientController implements Initializable {

    protected static final int HEIGHT = 750;
    protected static final int WIDTH = 1200;

    private static double xOffset = 0;
    private static double yOffset = 0;

    private Stage exitStage;

    @FXML
    private BorderPane pane;
    @FXML
    private BorderPane dynamicPane;
    @FXML
    private Button closeBtn;
    @FXML
    private Button maximizedStageBtn;
    @FXML
    private Button minimizeStageBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private Button playlistBtn;
    @FXML
    private Button userBtn;
    @FXML
    private Button exitBtn;
    @FXML
    //private Tooltip exitTooltip;

    private double X;
    private double Y;

    private boolean isMaximized;


    /**
     * TODO document
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set Tooltip delay
        //exitTooltip.setShowDelay(new Duration(1));

        /*
         set the boolean variable isMaximized, which tells me if the stage is maximzed or not, at firt the
         variable has value false because the stage isn't maximized
         */
        isMaximized = false;

        // at firt the dynamic pane contains the search pane
        setDynamicPane("search.fxml");
    }

    /**
     * TODO document
     * @param event
     */
    @FXML
    public void handleCloseButtonAction(ActionEvent event){
        /*Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();*/
        EmotionalSongs.getStage().close();
    }

    /**
     * TODO document
     */
    @FXML
    public void handleMaximizedStageButtonAction(){

        /*PROBLEMA : al posto di ingrandirsi, la finistra (stage) sparisce, non so il motivo

        Stage stage = EmotionalSongs.getStage();
        if(stage.isMaximized()){
            stage.setMaximized(false);
        }else{
            stage.setMaximized(true);
        }
        */

        // UN MODO PER RISOLVERE IL PROBLEMA è IL SEGUENTE:
        Stage stage = EmotionalSongs.getStage();

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
        EmotionalSongs.getStage().setIconified(true);
    }

    /**
     * TODO document
     */
    @FXML
    public void handleSearchButtonAction(){
        System.out.println("search button cliecked");
        /*
        TO DO : finire di implementare il search pane
         */
        setDynamicPane("search.fxml");
    }

    /**
     * TODO document
     */
    @FXML
    public void handlePlaylistButtonAction(){
        System.out.println("playlist button clicked");
         /*
        TO DO : finire di implementare il playlist pane
         */
        setDynamicPane("playlist.fxml");

    }

    /**
     * TODO document
     */
    @FXML
    public void handleUserButtonAction(){
        System.out.println("user button cliked");
        /*
        TO DO : finire di implementare il User pane
         */
        setDynamicPane("user.fxml");

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
        try {
            exitStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("exit.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            exitStage.setScene(scene);
            exitStage.initStyle(StageStyle.UNDECORATED);
            exitStage.setResizable(false);
            /*
            tramite initModality rendo il main pane non cliccabile, di fatto lo disattivo, grazie a ciò
            quando apro lo exit stage se premo al di fuori dello stage, esso non si chiude.
            SE NON SI CAPISCE COSA FA, SCRIVIMI.
             */
            exitStage.initModality(Modality.APPLICATION_MODAL);
            exitStage.show();

        }catch(IOException e){
            e.printStackTrace();
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

                xOffset = event.getScreenX() - EmotionalSongs.getStage().getX();
                yOffset = event.getScreenY() - EmotionalSongs.getStage().getY();

            }
        });

        this.pane.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event){

                if (xOffset == 0 && yOffset==0) {
                    // questa porzione di codice è necessaria per evitare che la finestra snappi alle coordinate 0,0 (cosa che avviene alla primo trascinamento della schermata)
                    calculateGap(event);
                }

                EmotionalSongs.getStage().setX(event.getScreenX() - xOffset);
                EmotionalSongs.getStage().setY(event.getScreenY() - yOffset);
            }
        });

    }


    /**
     * TODO document
     * @param event
     */
    private void calculateGap(MouseEvent event){
        xOffset = event.getScreenX() - EmotionalSongs.getStage().getX();
        yOffset = event.getScreenY() - EmotionalSongs.getStage().getY();
    }

    /**
     * TODO document
     * @param fxml
     */
    public void setDynamicPane(String fxml){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
            Node pane = fxmlLoader.load();

            dynamicPane.setCenter(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * TODO document
     * @param username
     */
    public void setUsername(String username){
        this.userBtn.setText("  " + username);
    }
}