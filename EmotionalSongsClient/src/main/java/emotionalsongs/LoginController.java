package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The `LoginController` class implements the controller logic for the login user interface.
 * This class handles user interactions and manages the UI components associated with user login and authentication.
 * Additionally, it allows the user to toggle password visibility, drag the application window, and control various UI elements.
 *
 * @author <a href="https://github.com/SpitefulCookie"> Della Chiesa Mattia</a>
 */
public class LoginController implements Initializable {

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
     * Initializes the controller when the corresponding FXML is loaded.
     * This method sets up various UI elements and interactions, including starting a dedicated thread to initialize the server connection.
     * It handles user interface actions such as clicking the settings button, toggling password visibility, and handling login attempts.
     *
     * @param url The URL to the FXML document (unused).
     * @param resourceBundle The resources used for localization (unused).
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
            Scene dialogScene = GUIUtilities.getInstance().getScene("clientLoginSettings.fxml");

            dialog.setScene(dialogScene);
            dialog.showAndWait();
            updateConnectionGraphics();

        });

        pwdField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                handleLoginButtonAction();
            }
        });

        overlappingTextField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                handleLoginButtonAction();
            }
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

        // System.out.println("Tempo esecuzione: " + (System.currentTimeMillis()-startTime) + "ms");

    }

    /**
     * Updates the graphics and status labels to reflect the current state of the connection.
     * If the connection to the server is not initialized this method displays a failed connection status, a successful connection status otherwise.
     */
    protected static void updateConnectionGraphics(){

        if(!EmotionalSongsClient.isConnectionInitialized){

            _connectionStatusIcon.setImage(guiUtilities.getImage("badConnection"));
            _connectionStatusLabel.setText("Connessione fallita");
            Tooltip tip = new Tooltip("Impossibile contattare il server, verifica le tue impostazioni");
            tip.setShowDelay(new Duration(0.3));
            tip.setHideDelay(new Duration(0.3));
            _connectionStatusLabel.setTooltip(tip);

        } else{
            _connectionStatusIcon.setImage(guiUtilities.getImage("goodConnection"));
            _connectionStatusLabel.setText("Connessione stabilita");
        }

    }

    /**
     * Removes any text present from the form fields
     */
    protected static void clearFields(){
        _usernameField.setText("");
        _overlappingPwdField.setText("");
        _pwdField.setText("");
    }

    /**
     * Handles the event when the "Continue as Guest" button is clicked.
     * Initializes the server connection, registers the client, and sets up the user interface
     * for the guest user.
     */
    @FXML protected void handleContinueAsGuest(){

        EmotionalSongsClient.initializeServerConnection(false);

        if (EmotionalSongsClient.isConnectionInitialized) {

            //EmotionalSongs.registerClient();

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

        }

    }

    /**
     * Handles the event when the "Register" button is clicked.
     * Initializes the server connection, registers the client, and displays the user registration screen.
     */
    @FXML protected void handleRegisterButton() {

            EmotionalSongsClient.initializeServerConnection(false);

            if (EmotionalSongsClient.isConnectionInitialized) {
                //EmotionalSongs.registerClient();
                EmotionalSongsClient.setStage(GUIUtilities.getInstance().getScene("UserRegistration.fxml"), UserRegistrationController.WIDTH, UserRegistrationController.HEIGHT, true);
                EmotionalSongsClient.getStage().show();
            }
    }

    /**
     * Handles the event when the "Close" button is clicked.
     * Closes the current stage, disconnects the client from the server, and exits the application.
     */
    @FXML protected void handleCloseButtonAction(ActionEvent event){
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();
        //EmotionalSongs.disconnectClient();
        Platform.exit();
        System.exit(0);
    }

    /**
     * Applies the default style to the username field.
     */
    @FXML protected void handleUserFieldHighlight(){
        // questa funzione non viene direttamente utilizzata nel codice ma viene invocata mediante fxml;
        // Il suo scopo consiste nel resettare lo stile del Textfield una volta che l'utente digita un carattere,
        // eventualmente rimuovendo lo stile di "errore" dovuto alla non valorizzazione del campo
        usernameField.setStyle(null);
    }

    /**
     * Applies the default style to the overlappingTextField or pwdField, depending on which one it is currently displayed.
     */
    @FXML protected void handlePwdFieldHighlight(){

        // Come per la funzione precedente, questa non viene direttamente utilizzata nel codice ma viene invocata mediante fxml;
        // Il suo scopo consiste nel resettare lo stile del TextField una volta che l'utente digita un carattere,
        // eventualmente rimuovendo lo stile di "errore" dovuto alla non valorizzazione del campo
        if(isDisplayed){overlappingTextField.setStyle(null);}
        else{pwdField.setStyle(null);}

    }

    /**
     * Handles the event when the "Login" button is clicked.
     * Initializes the server connection, processes the user's login attempt, and sets up the main client screen upon successful login.
     */
    @FXML protected void handleLoginButtonAction(){
                
        EmotionalSongsClient.initializeServerConnection(false);

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

        if ( pwd != null && username != null) {

            try {
                // This is the actual login
                if (EmotionalSongsClient.auth.userLogin(username, AuthManager.RSA_Encrypt(pwd, EmotionalSongsClient.auth.getPublicKey()))) {

                    //EmotionalSongs.registerClient();

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("emotionalSongsClient.fxml"));

                    try {

                        Scene scene = new Scene(fxmlLoader.load());

                        EmotionalSongsClientController client = fxmlLoader.getController();
                        client.setUser(usernameField.getText(), false);

                        EmotionalSongsClient.setStage(scene, EmotionalSongsClientController.WIDTH, EmotionalSongsClientController.HEIGHT, true);

                    }catch (IOException e1){
                        // Something went wrong, the client will disconnect from the server
                         //EmotionalSongs.disconnectClient();
                    }

                } else { // Le credenziali sono errate

                    loginFailedLabel.setText("Invalid username or password");
                    loginFailedLabel.setStyle(errorMessage);
                    loginFailedLabel.setVisible(true);

                }

            } catch (Exception e) { // Covers also the case where EmotionalSongsClient.auth is null

                Stage connectionFailedStage = new Stage();

                connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
                connectionFailedStage.initStyle(StageStyle.UNDECORATED);
                connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
                connectionFailedStage.setResizable(false);
                connectionFailedStage.show();

            }

        }

    }

    /**
     * Handles the click event when the password visibility toggle button is clicked.
     * This method toggles the visibility of the password input field, allowing the user to view the password in plain text or as masked characters.
     * It also updates the icon on the toggle button to reflect the current state.
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
     * Sets the JavaFX stage for the application window.
     *
     * @param s The JavaFX `Stage` object representing the application window.
     */
    public static void setStage(Stage s){stage = s;}

    /**
     * Calculates the gap between the mouse cursor position and the top-left corner of the application window.
     *
     * This method is used to calculate the gap between the current mouse cursor position and the top-left corner of
     * the application window.
     * The values updated within this method are used to move the application window.
     *
     * @param event The MouseEvent representing the mouse event that triggered the calculation.
     */
    private void calculateGap(MouseEvent event){
        xOffset = event.getScreenX() - stage.getX();
        yOffset = event.getScreenY() - stage.getY();
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
