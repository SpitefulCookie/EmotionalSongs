package emotionalsongs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * TODO document
 */
public class LoginController implements Initializable {

    /* Old article about RMI optimization
     * http://www.javaperformancetuning.com/tips/j2ee_rmi.shtml
     */

    private static final String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    private static final String errorMessage = "-fx-text-fill: RED;";

    protected static final int WIDTH = 650;
    protected static final int HEIGHT = 420;

    private static Stage stage;
    private static double xOffset = 0;
    private static double yOffset = 0;

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

        closeIcon = guiUtilities.getImage("close");
        eye = guiUtilities.getImage("eye");
        eyeCrossed = guiUtilities.getImage("eyeCrossed");

        ImageView closeImageView = new ImageView(closeIcon);
        closeImageView.setFitWidth(20);
        closeImageView.setFitHeight(20);

        if (closeIcon != null) {
            closeBtn.setGraphic(closeImageView);
        }else{
            closeBtn.setText("X");
        }

        showPasswordInput.setGraphic(new ImageView(eye));
        showPasswordInput.setFocusTraversable(false);
    }

    /**
     * TODO document
     */
    @FXML protected void handleContinueAsGuest(){
        System.out.println("Continue as guest clicked!");

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("emotionalSongsClient.fxml"));
            //Scene scene = new Scene(FXMLLoader.load(new File("emotionalSongsClient.fxml").toURI().toURL())); TODO remove
            Scene scene = new Scene(fxmlLoader.load());

            EmotionalSongsClientController client = fxmlLoader.getController();
            client.setUser("Guest", true);

            EmotionalSongsClient.setStage(scene, EmotionalSongsClientController.WIDTH, EmotionalSongsClientController.HEIGHT, true);
            EmotionalSongsClient.getStage().show();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * TODO document
     */
    @FXML protected void handleRegisterButton() {
        try {
            EmotionalSongsClient.setStage(new Scene(FXMLLoader.load(Objects.requireNonNull(EmotionalSongsClient.class.getResource("UserRegistration.fxml")))), UserRegistrationController.WIDTH, UserRegistrationController.HEIGHT, true);

            EmotionalSongsClient.getStage().show();
        } catch (IOException e){
            e.printStackTrace();
            // TODO Auto generated stub
        }
    }

    /**
     * TODO document
     * @param event
     */
    @FXML protected void handleCloseButtonAction(ActionEvent event){
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();
        EmotionalSongsClient.unexportClient();
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

        String pwd = null;
        String username = null;

        if(isDisplayed){ // Se la password è visualizzata in chiaro (textbox visibile, password field nascosto)

            if(overlappingTextField.getText().isBlank()){

                overlappingTextField.setStyle(errorStyle);
                overlappingTextField.setPromptText("Mandatory field");

            } else{
                pwd = overlappingTextField.getText();
            }

        } else{ // Se la password NON è visualizzata in chiaro (textbok nascosto, password field visibile)

            if(pwdField.getText().isBlank()){

                pwdField.setStyle(errorStyle);
                pwdField.setPromptText("Mandatory field");

            } else{ pwd = pwdField.getText();}

        }

        if (usernameField.getText().isBlank()){

            usernameField.setStyle(errorStyle);
            usernameField.setPromptText("Mandatory field");

        } else{
            username = usernameField.getText();
        }

        if(pwd != null && username != null) {

            try{

                if(EmotionalSongsClient.auth.userLogin(username, RSA_Encrypt(pwd))){

                    System.out.println("User logged in!"); // TODO Qui verrà visualizzata la schermata principale

                    try{
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("emotionalSongsClient.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());

                        EmotionalSongsClientController client = fxmlLoader.getController();
                        client.setUser(usernameField.getText(), false);

                        EmotionalSongsClient.setStage(scene, EmotionalSongsClientController.WIDTH, EmotionalSongsClientController.HEIGHT, true);

                    }catch (IOException e1){
                        // TODO forse rimuovere sollevamento eccezzione
                        e1.printStackTrace();
                    }

                } else{ // Le credenziali sono errate

                    loginFailedLabel.setText("Invalid username or password");
                    loginFailedLabel.setStyle(errorMessage);
                    loginFailedLabel.setVisible(true);

                }

            } catch (RemoteException e) {

                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Errore");

                ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);

                dialog.setContentText("Impossibile contattare il server. Riavviare l'applicazione.");

                // Tipicamente il client quando solleva una RemoteException in questo punto vuol dire che possiede un riferimento obsoleto alla classe AuthManager.
                // In altre parole il server è stato chiuso (e/o riavviato) DOPO che il client ha ottenuto il riferimento a quest'ultima.
                // Pertanto l'unica soluzione consiste nell'ottenere un nuovo riferimento all'oggetto remoto, questo si potrebbe effettuare all'interno del codice
                // però si tratta di una feature aggiuntiva che si può includere nel futuro. Onde evitare che questo commento venga perso lo marco con un TODO.

                Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();

                dialogStage.getIcons().add(guiUtilities.getImage("failure"));

                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.showAndWait();
            }

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

            if (xOffset == 0 && yOffset==0) {
                // questa porzione di codice è necessaria per evitare che la finestra snappi alle coordinate 0,0 (cosa che avviene alla primo trascinamento della schermata)
                calculateGap(event);
            }

            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

    }

    protected static byte[] RSA_Encrypt(String data){
        try {
            PublicKey pk = EmotionalSongsClient.auth.getPublicKey();
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, pk);
            return encryptCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e){
            return null;
        }
    }
}
