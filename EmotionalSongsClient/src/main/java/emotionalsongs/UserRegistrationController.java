package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The controller class for the user registration view.
 *
 * This class implements the necessary functionality to handle user registration in the application.
 * It initializes GUI components, sets up event listeners, and handles user input validation.
 * The class also provides methods to calculate the strength of passwords, check username availability,
 * and handle registration confirmation actions.
 *
 */
public class UserRegistrationController implements Initializable {

    // Constants for GUI dimensions
    protected static final int HEIGHT = 750;
    protected static final int WIDTH = 800;

    // Variables to store the initial mouse cursor offset for window dragging
    private static double xOffset = 0;
    private static double yOffset = 0;

    private GUIUtilities guiUtilities;

    // CSS styles for various message types
    private static final String errorMessage = "-fx-text-fill: #FF5959;";
    private static final String warningMessage = "-fx-text-fill: #FF7600;";
    private static final String successMessage = "-fx-text-fill: #1FB57B;";

    // FXML fields representing various input components in the registration form
    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private TextField codFiscField;
    @FXML private TextField viaField;
    @FXML private TextField emailField;
    @FXML private TextField numberField;
    @FXML private TextField comuneField;
    @FXML private TextField provField;
    @FXML private TextField capField;
    @FXML private TextField usernameField;
    @FXML private TextField pwdField;
    @FXML private ImageView checkUsernameResultImg;
    @FXML private Label checkUsernameResultLbl;
    @FXML private ProgressBar pwdQualityIndicator;
    @FXML private Label pwdQualityLabel;
    @FXML private Button eyeBtn;
    @FXML private TextField overlappingPasswordTF;
    @FXML private Button closeBtn;
    @FXML private Pane pane;

    // Images used for UI elements
    private Image failureIcon;
    private Image successIcon;
    private Image eye;
    private Image eyeCrossed;
    private Boolean isPwdDisplayed = false;

    /**
     * Initializes the GUI components and sets up the initial state of the application window.<br><br>
     *
     * This method is called automatically when the FXML file associated with this controller is loaded. It serves as the
     * initialization method for the GUI components and the application window's initial state.<br><br>
     * The method performs the following tasks:
     * <ul>
     * <li> Initializes `guiUtilities` with the instance of GUIUtilities and Loads the images for the various UI elements.
     * <li> Configures the close button (`closeBtn`) with the close icon or the "X" character, depending on the availability of the image.
     * <li> Applies several input constraints to the text input fields.
     * </ul>
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        guiUtilities = GUIUtilities.getInstance();

        eye =  guiUtilities.getImage("eye");
        eyeCrossed = guiUtilities.getImage("eyeCrossed");
        successIcon = guiUtilities.getImage("success");
        failureIcon = guiUtilities.getImage("failure");
        Image cross = guiUtilities.getImage("close");
        eyeBtn.setText("");

        eyeBtn.setGraphic(new ImageView(eye));
        eyeBtn.setFocusTraversable(false);


        ImageView closeImageView = new ImageView(cross);
        closeImageView.setFitWidth(20);
        closeImageView.setFitHeight(20);

        if(cross != null) {
            closeBtn.setGraphic(closeImageView);
        }else{
            closeBtn.setText("X");
        }

        checkUsernameResultImg.setVisible(false);

        GUIUtilities.forceTextInput(provField, 2);
        GUIUtilities.forceNumericInput(capField, 5);

        addPwdChangeListener(pwdField, pwdQualityLabel, pwdQualityIndicator);
        addPwdChangeListener(overlappingPasswordTF, pwdQualityLabel, pwdQualityIndicator);

    }

    /**
     * Adds a password change listener to the specified TextField and updates associated visual indicators.<br><br>
     *
     * This method adds a password change listener to the provided TextField (`field`). When the text of the TextField changes
     * (e.g., when the user types or deletes characters), the password strength is evaluated using the `checkPasswordStrength` method.
     * Based on the password strength, the method also updates the appearance of the UI elements meant to visually indicate the password's strength.<br><br>
     *
     * Additionally, the method checks for certain forbidden characters (e.g., double quotes, single quotes, less than, and greater than symbols)
     * in the password and, if detected, they are removed from the password text.<br><br>
     *
     * @param field               The TextField to which the password change listener will be added.
     * @param pwdQualityLabel     The Label that will display the password strength quality (e.g., "Poor", "Medium", "Excellent").
     * @param pwdQualityIndicator The ProgressBar that visually represents the password strength level.
     */
    private static void addPwdChangeListener(TextField field, Label pwdQualityLabel, ProgressBar pwdQualityIndicator){

        field.textProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.contains("\"")|| newValue.contains("'") || newValue.contains("<")|| newValue.contains(">")) {
                String s = field.getText().substring(0, field.getText().length()-1);
                field.setText(s);
            } else {

                int strength = checkPasswordStrength(field.getText());

                switch (strength) {
                    case 0 -> {
                        pwdQualityIndicator.setStyle(null);
                        pwdQualityLabel.setVisible(false);
                    }
                    case 1 -> {
                        pwdQualityLabel.setText("Poor");
                        pwdQualityLabel.setStyle(errorMessage);
                        pwdQualityLabel.setVisible(true);
                    }
                    case 2 -> {
                        pwdQualityLabel.setText("Medium");
                        pwdQualityLabel.setStyle(warningMessage);
                        pwdQualityLabel.setVisible(true);
                    }
                    case 3 -> {
                        pwdQualityLabel.setText("Excellent");
                        pwdQualityLabel.setStyle(successMessage);
                        pwdQualityLabel.setVisible(true);
                    }
                }

                pwdQualityIndicator.setProgress((double) strength / 3);
            }
        });

    }

    /**
     * Determines if the provided password contains at least one special character.<br><br>
     *
     * This method checks if the provided password contains at least one special character. Special characters
     * are any characters that are not letters (both uppercase and lowercase) or digits. The method uses a regular
     * expression to match any character that is not in the set of letters (both uppercase and lowercase) and digits.
     * If at least one special character is found in the password, the method returns true; otherwise, it returns false.<br><br>
     *
     * @param pwd The password to check for the presence of special characters.
     * @return true if the password contains at least one special character, false otherwise.
     */
    private static boolean containsSpecialCharacter(String pwd) {return pwd.matches(".*[^a-zA-Z0-9].*");}

    /**
     * Checks the strength of the provided password.<br><br>
     *
     * This method evaluates the strength of the provided password based on several factors, including password length,
     * the presence of uppercase letters, lowercase letters, digits, and special characters. The password strength is
     * assessed by awarding complexity points for each of these components. A password with a higher complexity score
     * is considered stronger. The method returns an integer representing the password's strength level:
     * <ul>
     * <li> <strong>Excellent password (3)</strong>- Contains a mix of uppercase letters, lowercase letters, digits, and special characters,
     *                       with a length of at least 10 characters.
     * <li><strong>Medium password (2)</strong> - Contains a mix of uppercase letters, lowercase letters, digits, and special characters,
     *                       but either has a length less than 10 characters or lacks one of the components.
     * <li><strong>Weak password (1)</strong>- Contains at least one component (uppercase letters, lowercase letters, digits, or special
     *                      characters) but lacks others, making it relatively easy to guess.
     * <li><strong>Empty password (0)</strong>- The password is empty.
     * </ul>
     * @param pwd The password to evaluate for its strength.
     * @return An integer representing the password strength level (0 to 3).
     */
    private static int checkPasswordStrength(String pwd) {
        int len = pwd.length();
        int complexity = 0;

        if (len >= 10) complexity++; // Password length is an important factor in strength.

        if (containsSpecialCharacter(pwd)) complexity++; // Check for the presence of at least one special character.

        // Check for the presence of uppercase letters, lowercase letters, and digits.
        if (pwd.matches(".*[A-Z].*")) complexity++;
        if (pwd.matches(".*[a-z].*")) complexity++;
        if (pwd.matches(".*\\d.*")) complexity++;

        // Assess password strength based on complexity.
        if (complexity >= 4) {
            return 3; // Strong password.
        } else if (complexity >= 2) {
            return 2; // Medium password.
        } else if (complexity >= 1) {
            return 1; // Weak password.
        } else {
            return 0; // Empty or very weak password (no characters).
        }
    }

    /**
     * Toggles the visibility of the entered password between hidden and displayed.
     */
    @FXML private void viewPwd(){

        if(!isPwdDisplayed) {
            eyeBtn.setGraphic(new ImageView(eyeCrossed));
            overlappingPasswordTF.setText(pwdField.getText());
            pwdField.setVisible(false);
            overlappingPasswordTF.setVisible(true);
            isPwdDisplayed = true;
        } else{
            eyeBtn.setGraphic(new ImageView(eye));
            pwdField.setText(overlappingPasswordTF.getText());
            overlappingPasswordTF.setVisible(false);
            pwdField.setVisible(true);
            isPwdDisplayed = false;
        }

    }

    /**
     * Handles the action when the close button is clicked.<br><br>
     *
     * This method is triggered when the close button is clicked on the JavaFX stage (window). It performs
     * the necessary actions to close the stage. Firstly, it retrieves the current stage using the event
     * object and the source of the event, which is assumed to be the close button. Then, it closes the
     * stage, effectively closing the associated window. Additionally, the method calls the
     * `EmotionalSongsClient.unexportClient()` method to un-export the RMI client.
     *
     * @param event The ActionEvent triggered when the close button is clicked.
     */
    @FXML protected void handleCloseButtonAction(ActionEvent event){
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();

        //EmotionalSongs.disconnectClient();
    }

    /**
     * Handles the action when the cancel button is clicked.<br><br>
     *
     * This method is triggered when the cancel button is clicked in the user registration form- thus
     * annulling the user registration process.<br><br>
     *
     * Upon clicking on the cancel button, the stage will be changed to the login screen.
     * Additionally, the method calls the `EmotionalSongsClient.unexportClient()` method to un-export
     * the RMI client.
     *
     */
    @FXML protected void handleCancelButton(){
        try {
            EmotionalSongs.setStage(new Scene(FXMLLoader.load(Objects.requireNonNull(EmotionalSongs.class.getResource("login.fxml")))), LoginController.WIDTH, LoginController.HEIGHT, true);
            EmotionalSongs.getStage().show();
            //EmotionalSongs.disconnectClient();
        } catch (IOException e){
            //
        }
    }

    /**
     * Verifies the user input in the registration form and checks for validity.<br><br>
     *
     * This method performs various checks on the input fields in the registration form,
     * including name, surname, CF, email, address details, username and password.
     * It sets the appropriate error styles for invalid fields and updates prompt texts if necessary.
     *
     * @return true if user input is valid, false if there are errors in any input field.
     */
    private boolean verifyUserInput(){

        boolean isInputValid = true;

        // =============== N O M I N A T I V O  ===============

        if(nomeField.getText().isBlank()){
            GUIUtilities.setErrorStyle(nomeField);
            isInputValid = false;
        } else{GUIUtilities.setDefaultStyle(nomeField);}

        if (cognomeField.getText().isBlank()){
            GUIUtilities.setErrorStyle(cognomeField);
            isInputValid = false;
        } else{GUIUtilities.setDefaultStyle(cognomeField);}

        // =============== C O D I C E   F I S C A L E  ===============

        try {
            boolean isCfTaken = EmotionalSongs.auth.cfExists(AuthManager.RSA_Encrypt(codFiscField.getText(), EmotionalSongs.auth.getPublicKey()));
            if (!isValidCF(codFiscField.getText()) || isCfTaken) {
                GUIUtilities.setErrorStyle(codFiscField);

                if (isCfTaken) {
                    codFiscField.setPromptText("Codice fiscale già registrato");
                    Tooltip tp = new Tooltip("Codice fiscale già registrato");
                    tp.setShowDelay(new Duration(0.5));
                    tp.setHideDelay(new Duration(0.6));
                    codFiscField.setTooltip(tp);
                } else if (!codFiscField.getText().isBlank()) {
                    codFiscField.setPromptText("Codice fiscale invalido");
                    Tooltip tp = new Tooltip("Codice fiscale invalido");
                    tp.setShowDelay(new Duration(0.5));
                    tp.setHideDelay(new Duration(0.6));
                    codFiscField.setTooltip(tp);
                }

                isInputValid = false;

            } else {
                GUIUtilities.setDefaultStyle(codFiscField);
                codFiscField.setTooltip(null);
            }
        } catch (RemoteException e){isInputValid=false;}

        // =============== E M A I L  ===============

        if (emailField.getText().isBlank() | !emailField.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$") ){

            GUIUtilities.setErrorStyle(emailField);
            if(!emailField.getText().isBlank())
                emailField.setPromptText("Invalid email");
            isInputValid = false;

        } else{GUIUtilities.setDefaultStyle(emailField);}

        // =============== I N D I R I Z Z O   T O P O N O M A S T I C O  ===============

        if (viaField.getText().isBlank()){
            GUIUtilities.setErrorStyle(viaField);
            isInputValid = false;
        } else{GUIUtilities.setDefaultStyle(viaField);}

        if (numberField.getText().isBlank() | !numberField.getText().matches("\\d.*") | numberField.getText().contains("-")){
            GUIUtilities.setErrorStyle(numberField);
            if(!numberField.getText().isBlank() && !numberField.getText().matches("\\d.*"))
                numberField.setPromptText("Invalid");
            isInputValid = false;
        } else{GUIUtilities.setDefaultStyle(numberField);}

        if (comuneField.getText().isBlank()){
            GUIUtilities.setErrorStyle(comuneField);
            isInputValid = false;
        } else{GUIUtilities.setDefaultStyle(comuneField);}

        if (provField.getText().isBlank()){
            GUIUtilities.setErrorStyle(provField);
            isInputValid = false;
        } else{GUIUtilities.setDefaultStyle(provField);}

        if (capField.getText().isBlank()){
            GUIUtilities.setErrorStyle(capField);
            isInputValid = false;
        } else{GUIUtilities.setDefaultStyle(capField);}

        // =============== C R E D E N Z I A L I   U T E N T E  ===============

        if (usernameField.getText().isBlank() | !checkUsernameAvailability()) {
            GUIUtilities.setErrorStyle(usernameField);
            isInputValid = false;
        } else {GUIUtilities.setDefaultStyle(usernameField);}

        if(!isPwdDisplayed){
            if (pwdField.getText().isBlank()){
                GUIUtilities.setErrorStyle(pwdField);
                isInputValid = false;
            } else{GUIUtilities.setDefaultStyle(pwdField);}

        } else{
            if (overlappingPasswordTF.getText().isBlank()){
                GUIUtilities.setErrorStyle(overlappingPasswordTF);
                isInputValid = false;
            } else{GUIUtilities.setDefaultStyle(overlappingPasswordTF);}
        }

        return isInputValid;

    }

    /**
     * Handles the confirmation button action in the registration form.<br><br>
     *
     * This method is invoked when the user clicks on the confirmation button in the registration form.
     * It first calls the `verifyUserInput` method to validate the user input in the form. If the input is valid,
     * the method retrieves the user's details and constructs a string containing the user's data.
     * It then encrypts the user data using RSA encryption and sends it to the server to complete the user registration.
     *
     * If the user input is not valid, the method does not proceed with the registration and does nothing.
     */
    @FXML protected void handleConfirmButton(){

        if(verifyUserInput()){

            String userData =
                cognomeField.getText() +" "+ nomeField.getText() +"&SEP&"+
                codFiscField.getText() +"&SEP&"+
                viaField.getText() +"&SEP&"+
                numberField.getText() +"&SEP&"+
                capField.getText() +"&SEP&"+
                comuneField.getText() +"&SEP&"+
                provField.getText() +"&SEP&"+
                emailField.getText() +"&SEP&"+
                usernameField.getText() +"&SEP&"+
                pwdField.getText();

            try {
                EmotionalSongs.auth.registrazione(AuthManager.RSA_Encrypt(userData, EmotionalSongs.auth.getPublicKey()));

            } catch (RemoteException e){

                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Errore");

                ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();

                dialogStage.getIcons().add(guiUtilities.getImage("failure"));

                dialog.setContentText("È stato riscontrato un errore. Ritentare e, se l'errore persiste, riavviare l'applicazione.");

                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.showAndWait();

            }

            try{

                // opening the login page
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                EmotionalSongs.setStage(scene, LoginController.WIDTH, LoginController.HEIGHT, true);

            }catch(IOException e1){
                e1.printStackTrace();
            }

        }

    }

    /**
     * Calculates the gap between the mouse cursor position and the top-left corner of the application window.<br><br>
     *
     * This method is used to calculate the gap between the current mouse cursor position and the top-left corner of
     * the application window.<br><br>
     * The values updated within this method are used to move the application window.
     *
     * @param event The MouseEvent representing the mouse event that triggered the calculation.
     */
    private void calculateGap(MouseEvent event){
        xOffset = event.getScreenX() - EmotionalSongs.getStage().getX();
        yOffset = event.getScreenY() - EmotionalSongs.getStage().getY();
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

        this.pane.setOnMousePressed(event -> {

            xOffset = event.getScreenX() - EmotionalSongs.getStage().getX();
            yOffset = event.getScreenY() - EmotionalSongs.getStage().getY();

        });

        this.pane.setOnMouseDragged(event -> {

            if (xOffset == 0 && yOffset==0) {
                // evita che la finestra snappi alle coordinate 0,0 (cosa che avviene alla primo trascinamento della schermata)
                calculateGap(event);
            }

            EmotionalSongs.getStage().setX(event.getScreenX() - xOffset);
            EmotionalSongs.getStage().setY(event.getScreenY() - yOffset);
        });

    }


    /**
     * Checks the availability of the entered username.<br><br>
     *
     * This method checks the availability of the entered username.
     * If the username is not blank, and it is not taken yet, the method displays a success icon and
     * message indicating the availability.<br>
     * If the username is already taken or invalid, the method displays a failure icon and message, and
     * changes the appearance of the username field to reflect the username's unavailability.<br>
     * Similarly, if an error occurs while verifying the username's availability, a {@link UsernameNotVerifiedException}
     * will be thrown, signaling that the server was unable to determine if the username was already in use. <br><br>
     *
     * If an exception occurs during the process a dialog box is shown with an error message indicating the issue.<br><br>
     *
     * @return {@code true} if the username is available, {@code false} if it is already taken or an exception occurs.
     */
    @FXML protected boolean checkUsernameAvailability() {

        try {

            if(!usernameField.getText().isBlank()) {

                try {

                    checkUsernameResultLbl.setVisible(true);
                    checkUsernameResultImg.setVisible(true);

                    boolean exists = EmotionalSongs.auth.usernameExists(usernameField.getText());

                    if (!exists) {

                        checkUsernameResultImg.setImage(successIcon);
                        checkUsernameResultLbl.setText("Username disponibile");
                        GUIUtilities.setDefaultStyle(usernameField);

                        return true;

                    } else {

                        checkUsernameResultImg.setImage(failureIcon);
                        checkUsernameResultLbl.setText("Username non disponibile");
                        GUIUtilities.setErrorStyle(usernameField);
                        usernameField.setPromptText("");
                        return false;

                    }

                } catch (UsernameNotVerifiedException u){

                    checkUsernameResultImg.setImage(failureIcon);
                    checkUsernameResultLbl.setText("Couldn't verify username availability");
                    GUIUtilities.setErrorStyle(usernameField);
                    usernameField.setPromptText("");
                    return false;

                }


            } else{ GUIUtilities.setErrorStyle(usernameField); }

        } catch (Exception e) {

            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();

        }

        return false;

    }

    /**
     * Validates a regular CF.
     * Source: <a href = "http://www.icosaedro.it/cf-pi/vedi-codice.cgi?f=cf-java.txt"> www.icosaedro.it </a>
     *
     * @param cf Normalized, 16 characters CF.
     * @return {@code true} if valid, or {@code false} if the CF must be rejected.
     * @author Umberto Salsi salsi@icosaedro.it
     *
     */
    public static boolean isValidCF(String cf){

        if(!cf.matches("^[0-9A-Z]{16}$")){return false;}

        int somma = 0;
        String evenMap = "BAFHJNPRTVCESULDGIMOQKWZYX";

        for(int i = 0; i < 15; i++){

            int charIntValue = cf.charAt(i);
            int idxEvenMap;

            if('0' <= charIntValue && charIntValue <= '9' ){idxEvenMap = charIntValue - '0';}
            else{ idxEvenMap = charIntValue - 'A';}

            if( (i & 1) == 0 ){idxEvenMap = evenMap.charAt(idxEvenMap) - 'A';}

            somma += idxEvenMap;

        }

        return somma % 26 + 'A' == cf.charAt(15);

    }
}
