package emotionalsongs;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

/**
 * TODO document
 */
public class LoginController implements Initializable {

    /* Old article about RMI optimization TODO remove before turning in the project
     * http://www.javaperformancetuning.com/tips/j2ee_rmi.shtml
     */

    private static final String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    private static final String errorMessage = "-fx-text-fill: RED;";

    protected static final int WIDTH = 650;
    protected static final int HEIGHT = 420;

    private static Stage stage;
    private static double xOffset = 0;
    private static double yOffset = 0;
    private static TextField _usernameField;
    private static TextField _overlappingPwdField;
    private static PasswordField _pwdField;
    private static Label _connectionStatusLabel;
    private static ImageView _connectionStatusIcon;

    private static GUIUtilities guiUtilities;
    private boolean isDisplayed = false;
    private Image eye;
    private Image eyeCrossed;
    //private Tooltip connectionStatusTip; // TODO verify

    @FXML private Pane anchorPane;
    @FXML private Label loginFailedLabel;
    @FXML private TextField usernameField;
    @FXML private TextField overlappingTextField;
    @FXML private PasswordField pwdField;
    @FXML private Button closeBtn;
    @FXML private Button showPasswordInput;
    @FXML private Button settingsButton;
    @FXML private ImageView connectionStatusIcon;
    @FXML private Label connectionStatusLabel;

    /**
     * TODO document
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        long startTime = System.currentTimeMillis();

        /*
         * In questo punto, viene avviato un nuovo thread dedicato a inizializzare la connessione con il server.
         * Questa operazione è finalizzata a garantire la fluidità dell'applicazione. Questo perché, a seguito dell'invocazione
         * del metodo initializeServerConnection(), qualora il server non risulti essere raggiungibile,
         * l'applicazione sospenderà temporaneamente la sua esecuzione in attesa di una risposta dal server - che inevitabilmente
         * si concluderà con un timeout.
         * Poiché in questo punto l'invocazione del metodo initializeServerConnection() è fatta puramente per fornire un
         * feedback immediato all'utente sullo stato della connessione al server, di seguito verrà effettuato un join
         * con timeout sul thread appena creato. Questo garantirà che i tempi di attesa per la risposta
         * del server non eccederanno mai i 700ms, permettendo così all'applicazione di continuare a eseguire senza interruzioni significative.
         */

        Thread t = new Thread(() -> EmotionalSongsClient.initializeServerConnection(true));

        t.start();

        Image closeIcon;
        guiUtilities = GUIUtilities.getInstance();

        settingsButton.setGraphic(new ImageView(guiUtilities.getImage("wrench")));
        settingsButton.setFocusTraversable(false);

        settingsButton.setOnAction( event -> {

            Stage dialog = new Stage();
            ClientSettingController.setStage(dialog);

            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.initOwner(EmotionalSongsClient.getStage());
            Scene dialogScene = GUIUtilities.getInstance().getScene("clientLoginSettings.fxml");;

            dialog.setScene(dialogScene);
            dialog.showAndWait();
            updateConnectionGraphics();

        });

        closeIcon = guiUtilities.getImage("close");
        eye = guiUtilities.getImage("eye");
        eyeCrossed = guiUtilities.getImage("eyeCrossed");

        ImageView closeImageView = new ImageView(closeIcon);
        closeImageView.setFitWidth(20);
        closeImageView.setFitHeight(20);

        if (closeIcon != null) {closeBtn.setGraphic(closeImageView);}
        else{closeBtn.setText("X");}

        showPasswordInput.setGraphic(new ImageView(eye));
        showPasswordInput.setFocusTraversable(false);
        _usernameField = usernameField;
        _overlappingPwdField = overlappingTextField;
        _pwdField = pwdField;
        _connectionStatusIcon = connectionStatusIcon;
        _connectionStatusLabel = connectionStatusLabel;

        try {
            t.join(700); // Max timeout: 700ms
        } catch (InterruptedException e) {
            // Exception not thrown
        }

        updateConnectionGraphics(); // This method is responsible for updating the look of the "monitor icon", meant to indicate the state of the connection with the server

        System.out.println("Tempo esecuzione: " + (System.currentTimeMillis()-startTime) + "ms");

    }

    protected static void updateConnectionGraphics(){
        if(!EmotionalSongsClient.isConnectionInitialized){
            _connectionStatusIcon.setImage(guiUtilities.getImage("badConnection"));
            _connectionStatusLabel.setText("Connection failed");
            Tooltip tip = new Tooltip("Unable to contact the server, please check your settings");
            tip.setShowDelay(new Duration(0.3));
            tip.setHideDelay(new Duration(0.3));
            _connectionStatusLabel.setTooltip(tip);
        } else{
            _connectionStatusIcon.setImage(guiUtilities.getImage("goodConnection"));
            _connectionStatusLabel.setText("Connection established");
        }
    }

    protected static void clearFields(){
        _usernameField.setText("");
        _overlappingPwdField.setText("");
        _pwdField.setText("");
    }
    /**
     * TODO document
     */
    @FXML protected void handleContinueAsGuest(){

        System.out.println("Continue as guest clicked!"); // TODO togliere tutti questi print

        if (EmotionalSongsClient.isConnectionInitialized) {

            EmotionalSongsClient.registerClient();

            // La connessione al server è necessaria anche se l'utente non è registrato, pertanto non dev'essere
            // in grado di visualizzare la schermata principale senza che il client si sia connesso al server.

            try {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("emotionalSongsClient.fxml"));

                Scene scene = new Scene(fxmlLoader.load());

                EmotionalSongsClientController client = fxmlLoader.getController();

                client.setUser("Guest", true);

                EmotionalSongsClient.setStage(scene, EmotionalSongsClientController.WIDTH, EmotionalSongsClientController.HEIGHT, true);
                EmotionalSongsClient.getStage().show();

            } catch (IOException e) {
                //
            }

        } else {
            EmotionalSongsClient.initializeServerConnection(false);
        }

    }

    /**
     * TODO document
     */
    @FXML protected void handleRegisterButton() {
        if(EmotionalSongsClient.isConnectionInitialized) {
            EmotionalSongsClient.registerClient();
            EmotionalSongsClient.setStage(GUIUtilities.getInstance().getScene("UserRegistration.fxml"), UserRegistrationController.WIDTH, UserRegistrationController.HEIGHT, true);
            EmotionalSongsClient.getStage().show();
        } else{

            EmotionalSongsClient.initializeServerConnection(false);

        }

    }

    /**
     * TODO document
     * @param event
     */
    @FXML protected void handleCloseButtonAction(ActionEvent event){
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();
        EmotionalSongsClient.disconnectClient();
        // TODO added the following lines as an attempt to fix the client remaining open in the background after exiting the application, remove if it doesn't work
        Platform.exit();
        System.exit(0);
    }

    /**
     * TODO document
     */
    @FXML protected void handleUserFieldHighlight(){
        // questa funzione non viene direttamente utilizzata nel codice ma viene invocata mediante fxml;
        // Il suo scopo consiste nel resettare lo stile del Textfield una volta che l'utente digita un carattere,
        // eventualmente rimuovendo lo stile di "errore" dovuto alla non valorizzazione del campo
        usernameField.setStyle(null);
    }

    /**
     * TODO document
     */
    @FXML protected void handlePwdFieldHighlight(){

        // Come per la funzione precedente, questa non viene direttamente utilizzata nel codice ma viene invocata mediante fxml;
        // Il suo scopo consiste nel resettare lo stile del TextField una volta che l'utente digita un carattere,
        // eventualmente rimuovendo lo stile di "errore" dovuto alla non valorizzazione del campo
        if(isDisplayed){overlappingTextField.setStyle(null);}
        else{pwdField.setStyle(null);}

    }

    /**
     * TODO document
     */
    @FXML protected void handleLoginButtonAction(){

        if (EmotionalSongsClient.isConnectionInitialized) {

            String pwd = null;
            String username = null;

            if (isDisplayed) { // Se la password è visualizzata in chiaro (textbox visibile, password field nascosto)

                if (overlappingTextField.getText().isBlank()) {

                    overlappingTextField.setStyle(errorStyle);
                    overlappingTextField.setPromptText("Mandatory field");

                } else {
                    pwd = overlappingTextField.getText();
                }

            } else { // Se la password NON è visualizzata in chiaro (textbok nascosto, password field visibile)

                if (pwdField.getText().isBlank()) {

                    pwdField.setStyle(errorStyle);
                    pwdField.setPromptText("Mandatory field");

                } else {
                    pwd = pwdField.getText();
                }

            }

            if (usernameField.getText().isBlank()) {

                usernameField.setStyle(errorStyle);
                usernameField.setPromptText("Mandatory field");

            } else {
                username = usernameField.getText();
            }

            if (pwd != null && username != null) {

                try {
                    // This is the actual login
                    if (EmotionalSongsClient.auth.userLogin(username, AuthManager.RSA_Encrypt(pwd, EmotionalSongsClient.auth.getPublicKey()))) {

                        EmotionalSongsClient.registerClient();

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("emotionalSongsClient.fxml"));

                        try {

                            Scene scene = new Scene(fxmlLoader.load());

                            EmotionalSongsClientController client = fxmlLoader.getController();
                            client.setUser(usernameField.getText(), false);

                            EmotionalSongsClient.setStage(scene, EmotionalSongsClientController.WIDTH, EmotionalSongsClientController.HEIGHT, true);

                        }catch (IOException e1){
                            // Something went wrong, the client will disconnect from the server
                             EmotionalSongsClient.disconnectClient();
                        }

                    } else { // Le credenziali sono errate

                        loginFailedLabel.setText("Invalid username or password");
                        loginFailedLabel.setStyle(errorMessage);
                        loginFailedLabel.setVisible(true);

                    }

                } catch (RemoteException e) {

                    Stage connectionFailedStage = new Stage();

                    connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
                    connectionFailedStage.initStyle(StageStyle.UNDECORATED);
                    connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
                    connectionFailedStage.setResizable(false);
                    connectionFailedStage.show();

                }

            }

        } else{
                EmotionalSongsClient.initializeServerConnection(false);
        }

    }

    /**
     * TODO Focument
     */
    @FXML protected void onClickEvent(){

        if(!isDisplayed){

            overlappingTextField.setText(pwdField.getText());
            overlappingTextField.setVisible(true);
            pwdField.setVisible(false);
            showPasswordInput.setGraphic(new ImageView(this.eyeCrossed));

            isDisplayed = true;
        } else{

            overlappingTextField.setVisible(false);
            pwdField.setVisible(true);
            pwdField.setText(overlappingTextField.getText());
            showPasswordInput.setGraphic(new ImageView(this.eye));

            isDisplayed = false;
        }
    }

    /**
     * TODO Document
     * @param s
     */
    public static void setStage(Stage s){stage = s;}

    /**
     * TODO document
     * @param event
     */
    private void calculateGap(MouseEvent event){
        xOffset = event.getScreenX() - stage.getX();
        yOffset = event.getScreenY() - stage.getY();
    }

    /**
     * TODO document
     */
    @FXML protected void moveWindow() {

        this.anchorPane.setOnMousePressed(event -> {
            xOffset = event.getScreenX() - stage.getX();
            yOffset = event.getScreenY() - stage.getY();
        });

        this.anchorPane.setOnMouseDragged(event -> {
            if (xOffset == 0 && yOffset==0) { calculateGap(event); }
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

    }

}
