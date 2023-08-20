package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller class for the login screen of the server application.
 *
 * This class is responsible for managing the user interface and behavior of the server login screen.
 * It initializes UI components, handles user interactions, data validation and verifies the user's credentials.
 *
 * @author <a href="https://github.com/SpitefulCookie"> Della Chiesa Mattia</a>
 */
public class ServerLoginController implements Initializable {

    private static final String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    private static final String errorMessage = "-fx-text-fill: RED;";

    protected static final int WIDTH = 650;
    protected static final int HEIGHT = 420;

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
     * Changes the appearance of the username field once selected by the user (typically after it had been highlighted
     * as an error due to a missing or invalid input)
     */
    @FXML protected void handleUserFieldHighlight(){
        usernameField.setStyle(null);
    }

    /**
     * Changes the appearance of the password field once selected by the user (typically after it had been highlighted
     * as an error due to a missing or invalid input)
     */
    @FXML protected void handlePwdFieldHighlight(){

        if(isDisplayed){overlappingTextField.setStyle(null);}
        else{pwdField.setStyle(null);}

    }

    /**
     * Handles the action when the login button is clicked.
     * Validates the user input, performs login operation, and displays appropriate messages.
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

        } else{ // Se la password NON è visualizzata in chiaro (textbox nascosto, password field visibile)

            if(pwdField.getText().isBlank()){

                pwdField.setStyle(errorStyle);
                pwdField.setPromptText("Mandatory field");

            } else{
                pwd = pwdField.getText();
            }

        }

        if (usernameField.getText().isBlank()){

            usernameField.setStyle(errorStyle);
            usernameField.setPromptText("Mandatory field");

        } else{
            username = usernameField.getText();
        }

        if(pwd != null && username != null) {

            try{
                /*  Di seguito viene effettuato il login dell'admin, il costruttore di QueryHandler lancierà
                 *  una SQLException nel caso la coppia username e password non risulti essere corretta.
                 */

                EmotionalSongsServer.initializeQueryHandler(new QueryHandler(username, pwd ));
                EmotionalSongsServer.loggedUser = username;
                try {

                    EmotionalSongsServer.setStage(new Scene(FXMLLoader.load(Objects.requireNonNull(EmotionalSongsServer.class.getResource("serverMainView.fxml")))), ServerMainViewController.WIDTH, ServerMainViewController.HEIGHT, true);
                    EmotionalSongsServer.getStage().show();

                } catch (IOException e){
                    System.err.println("IOException thrown while attempting to load FXML file \"serverMainView.fxml\"\nReason: " + e.getMessage());
                }

            } catch (SQLException e){

                loginFailedLabel.setText("Invalid username or password");
                loginFailedLabel.setStyle(errorMessage);
                loginFailedLabel.setVisible(true);

            }

        }

    }

    /**
     * Handles the event when the button "show password" (the eye icon) is clicked.
     *
     * This method toggles the visibility of the password field and an overlapping text field.
     * When the button is clicked, it switches between displaying the password in clear text and hiding it.
     *
     * The method manages the state of the password visibility and updates the graphical representation accordingly.
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
     * Initializes the server login window.
     *
     * This method is automatically called when the JavaFX scene associated with this controller is loaded.
     * It sets up the initial state of UI components, such as buttons and text fields.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known. (unused within the code)
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized. (unused within the code)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.eye = GUIUtilities.getInstance().getImage("eye");
        this.eyeCrossed =  GUIUtilities.getInstance().getImage("eyeCrossed");

        settingsButton.setGraphic(new ImageView(GUIUtilities.getInstance().getImage("settings")));
        settingsButton.setFocusTraversable(false);
        Tooltip tip = new Tooltip("DB Settings");
        tip.setShowDelay(new Duration(0.3));
        tip.setHideDelay(new Duration(0.3));
        settingsButton.setTooltip(tip);

        settingsButton.setOnAction(
            event -> {

                FXMLLoader loader = new FXMLLoader(EmotionalSongsServer.class.getResource("serverLoginSettings.fxml"));

                try {

                    Scene scene = new Scene(loader.load());

                    final Stage dialog = new Stage();
                    dialog.getIcons().add(GUIUtilities.getInstance().getImage("blueFire"));
                    dialog.setTitle("Database settings");
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.initOwner(EmotionalSongsServer.getStage());

                    dialog.setScene(scene);
                    dialog.show();

                } catch (IOException e) {
                    System.err.println("IOException thrown while attempting to load FXML file \"serverLoginSettings.fxml\"\nReason: " + e.getMessage());
                }
            });

        showPasswordInput.setGraphic(new ImageView(eye));
        showPasswordInput.setFocusTraversable(false);

        // I seguenti blocchi di codice rilevano quando l'utente preme il tasto invio all'interno del campo login (ovvero ha terminato d'inserire le proprie credenziali)
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

    }
}
