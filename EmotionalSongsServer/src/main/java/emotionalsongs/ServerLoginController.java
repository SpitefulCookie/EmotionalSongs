package emotionalsongs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * TODO document
 */
public class ServerLoginController implements Initializable {

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

    @FXML private Label loginFailedLabel;
    @FXML private TextField usernameField;
    @FXML private TextField overlappingTextField;
    @FXML private PasswordField pwdField;
    @FXML private Button showPasswordInput;
    @FXML private Button settingsButton;

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

        //char[] pwd = null;
        String pwd = null;
        String username = null;

        if(isDisplayed){ // Se la password è visualizzata in chiaro (textbox visibile, password field nascosto)

            if(overlappingTextField.getText().isBlank()){

                overlappingTextField.setStyle(errorStyle);
                overlappingTextField.setPromptText("Mandatory field");
                isUserMandatoryHL = true;

            } else{
                //pwd = overlappingTextField.getText().toCharArray();
                pwd = overlappingTextField.getText();
            }

        } else{ // Se la password NON è visualizzata in chiaro (textbok nascosto, password field visibile)

            if(pwdField.getText().isBlank()){

                pwdField.setStyle(errorStyle);
                pwdField.setPromptText("Mandatory field");
                isUserMandatoryHL = true;

            } else{
                //pwd = pwdField.getText().toCharArray();
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

            try{
                /*  Di seguito viene effettuato il login dell'admin, il costruttore di QueryHandler lancierà
                 *  una SQLException nel caso la coppia username e password non risulti essere corretta.
                 */

               EmotionalSongsServer.qh = new QueryHandler(username, pwd );

                try {

                    EmotionalSongsServer.setStage(new Scene(EmotionalSongsServer.getLoader().load(EmotionalSongsServer.class.getResource("serverMainView.fxml") )), ServerMainViewController.WIDTH, ServerMainViewController.HEIGHT, true);

                    EmotionalSongsServer.getStage().show();

                } catch (IOException e){}

            } catch (SQLException e){
                e.printStackTrace();

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

            overlappingTextField.requestFocus();

            pwdField.setVisible(false);
            showPasswordInput.setGraphic(new ImageView(this.eyeCrossed));

            isDisplayed = true;
        } else{

            overlappingTextField.setVisible(false);
            pwdField.setVisible(true);
            pwdField.setText(overlappingTextField.getText());
            pwdField.requestFocus();

            showPasswordInput.setGraphic(new ImageView(this.eye));

            isDisplayed = false;
        }
    }

    /**
     * TODO document
     */
    public static void setStage(Stage s){
        stage = s;
    }

    /**
     * TODO document
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try{

            eye =  new Image(new FileInputStream("EmotionalSongsServer/src/main/resources/Images/view.png"));
            eyeCrossed =  new Image(new FileInputStream("EmotionalSongsServer/src/main/resources/Images/hide.png"));

            settingsButton.setGraphic(new ImageView(new Image( new FileInputStream("EmotionalSongsServer/src/main/resources/Images/gear2.png"))));
            settingsButton.setFocusTraversable(false);

            settingsButton.setOnAction(
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {

                            FXMLLoader loader = new FXMLLoader(EmotionalSongsServer.class.getResource("serverLoginSettings.fxml"));

                            try {

                                Scene scene = new Scene(loader.load());

                                final Stage dialog = new Stage();
                                dialog.initModality(Modality.APPLICATION_MODAL);
                                dialog.initOwner(EmotionalSongsServer.getStage());
                                Scene dialogScene = scene;

                                dialog.setScene(dialogScene);
                                dialog.show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });


            showPasswordInput.setGraphic(new ImageView(eye));
            showPasswordInput.setFocusTraversable(false);

        } catch (FileNotFoundException e) {}

    }
}
