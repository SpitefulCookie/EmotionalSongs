package emotionalsongs;


import javafx.application.Application;
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

    private GUIUtilities guiUtilities;
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

    /**
     * TODO document
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        EmotionalSongsClient.registerClient();

        Image closeIcon;
        guiUtilities = GUIUtilities.getInstance();

        settingsButton.setGraphic(new ImageView(guiUtilities.getImage("gear")));
        settingsButton.setFocusTraversable(false);

        settingsButton.setOnAction( event -> {

            Stage dialog = new Stage();
            ClientSettingController.setStage(dialog);

            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.initOwner(EmotionalSongsClient.getStage());
            Scene dialogScene = GUIUtilities.getInstance().getScene("clientLoginSettings.fxml");;

            dialog.setScene(dialogScene);
            dialog.show();

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

        if (EmotionalSongsClient.isConnectionInitialized){

            // La connessione al server è necessaria anche se l'utente non è registrato, pertanto non dev'essere
            // in grado di visualizzare la schermata principale senza che il client si sia connesso al server.

            try{

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("emotionalSongsClient.fxml"));

                Scene scene = new Scene(fxmlLoader.load());

                    EmotionalSongsClientController client = fxmlLoader.getController();

                    // TODO invece di utilizzare il controller per impostare l'utente,
                    //  visto che vi sarà una ed una sola istanza della classe non si può rendere setUser
                    //  statico così da effettuare il caricamento della schermata all'interno di GUI utilities?
                    //  In questo modo si toglierebbero non pochi try-catch inutili.

                    client.setUser("Guest", true);

                    EmotionalSongsClient.setStage(scene, EmotionalSongsClientController.WIDTH, EmotionalSongsClientController.HEIGHT, true);
                    EmotionalSongsClient.getStage().show();

            }catch(IOException e){
                //
            }

        } else{EmotionalSongsClient.initializeServerConnection();}

    }

    /**
     * TODO document
     */
    @FXML protected void handleRegisterButton() {

        EmotionalSongsClient.setStage(GUIUtilities.getInstance().getScene("UserRegistration.fxml"), UserRegistrationController.WIDTH, UserRegistrationController.HEIGHT, true);
        EmotionalSongsClient.getStage().show();

    }

    /**
     * TODO document
     * @param event
     */
    @FXML protected void handleCloseButtonAction(ActionEvent event){
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();
        EmotionalSongsClient.unexportClient();
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

                    if (EmotionalSongsClient.auth.userLogin(username, AuthManager.RSA_Encrypt(pwd))) {

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("emotionalSongsClient.fxml"));

                        try {
                            Scene scene = new Scene(fxmlLoader.load());

                            EmotionalSongsClientController client = fxmlLoader.getController();
                            client.setUser(usernameField.getText(), false);

                            EmotionalSongsClient.setStage(scene, EmotionalSongsClientController.WIDTH, EmotionalSongsClientController.HEIGHT, true);

                    }catch (IOException e1){
                         //
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

        } else{ EmotionalSongsClient.initializeServerConnection(); }

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
