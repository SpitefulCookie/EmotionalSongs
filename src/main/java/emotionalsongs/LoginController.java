package emotionalsongs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * TODO document
 */
public class LoginController implements Initializable {

    private static final String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    private static final String errorMessage = "-fx-text-fill: RED;";

    protected static final int WIDTH = 650;
    protected static final int HEIGHT = 420;

    private static Stage stage;
    private static double xOffset = 0;
    private static double yOffset = 0;

    private boolean isUserMandatoryHL = false;
    private boolean isPwdMandatoryHL = false;
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
     */
    @FXML protected void handleContinueAsGuest(){
        System.out.println("Continue as guest clicked!");
    }

    /**
     * TODO document
     */
    @FXML protected void handleRegisterButton() {
        try {
            EmotionalSongs.setStage(new Scene(EmotionalSongs.getLoader().load(EmotionalSongs.class.getResource("UserRegistration.fxml") )), UserRegistrationController.WIDTH, UserRegistrationController.HEIGHT, true);

            EmotionalSongs.getStage().show();
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
    }

    /**
     * TODO document
     */
    @FXML protected void handleUserFieldHighlight(){
        usernameField.setStyle(null);
    }

    /**
     * TODO document
     */
    @FXML protected void handlePwdFieldHighlight(){

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
                isUserMandatoryHL = true;

            } else{
                pwd = overlappingTextField.getText();
            }

        } else{ // Se la password NON è visualizzata in chiaro (textbok nascosto, password field visibile)

            if(pwdField.getText().isBlank()){

                pwdField.setStyle(errorStyle);
                pwdField.setPromptText("Mandatory field");
                isUserMandatoryHL = true;

            } else{
                pwd = pwdField.getText();
            }

        }

        if (usernameField.getText().isBlank()){

            usernameField.setStyle(errorStyle);
            usernameField.setPromptText("Mandatory field");
            isUserMandatoryHL = true;

        } else{
            username = usernameField.getText();
        }

        if(pwd != null && username != null) {

            // TODO qui verrà effettuato il login

            System.out.println("Username: " + username + "\nPassword: " + pwd);

            if(username.equals("err")){
                loginFailedLabel.setText("Invalid username or password");
                loginFailedLabel.setStyle(errorMessage);
                loginFailedLabel.setVisible(true);
            }

        }

    }

    /**
     * TODO document
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

    public static void setStage(Stage s){
        stage = s;
    }

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

        this.anchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            //xOffset = stage.getX() - event.getX() ;
            //yOffset = stage.getY() - event.getY() ;

            @Override
            public void handle(MouseEvent event) {

                xOffset = event.getScreenX() - stage.getX();
                yOffset = event.getScreenY() - stage.getY();

            }
        });

        this.anchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event){

                if (xOffset == 0 && yOffset==0) {
                    // questa porzione di codice è necessaria per evitare che la finestra snappi alle coordinate 0,0 (cosa che avviene alla primo trascinamento della schermata)
                    calculateGap(event);
                }

                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

    }

    /**
     * TODO document
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image closeIcon = null;

        try{
            closeIcon =  new Image(new FileInputStream(".\\src\\main\\resources\\emotionalsongs\\Images\\closeIcon.png"));
            eye =  new Image(new FileInputStream(".\\src\\main\\resources\\emotionalsongs\\Images\\view.png"));
            eyeCrossed =  new Image(new FileInputStream(".\\src\\main\\resources\\emotionalsongs\\Images\\hide.png"));
            closeBtn.setGraphic(new ImageView(closeIcon));
            showPasswordInput.setGraphic(new ImageView(eye));
            showPasswordInput.setFocusTraversable(false);

        } catch (FileNotFoundException e) {

            if (closeIcon == null) {
                closeBtn.setText("X");
            } else {
                e.printStackTrace();
            }

        }
    }
}
