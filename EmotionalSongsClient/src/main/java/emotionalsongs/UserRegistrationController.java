package emotionalsongs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * TODO document, add minimize window button
 */
public class UserRegistrationController implements Initializable {

    protected static final int HEIGHT = 750;
    protected static final int WIDTH = 800;

    private static double xOffset = 0;
    private static double yOffset = 0;

    private GUIUtilities guiUtilities;

    private static String errorMessage = "-fx-text-fill: #FF5959;";
    private static String warningMessage = "-fx-text-fill: #FF7600;";

    String successMessage = "-fx-text-fill: #1FB57B;";

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

    private Image failureIcon;
    private Image successIcon;
    private Image cross;
    private Image eye;
    private Image eyeCrossed;
    private Boolean isPwdDisplayed = false;

    /**
     * TODO document
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        guiUtilities = GUIUtilities.getInstance();

        eye =  guiUtilities.getImage("eye");
        eyeCrossed = guiUtilities.getImage("eyeCrossed");
        successIcon = guiUtilities.getImage("success");
        failureIcon = guiUtilities.getImage("failure");
        cross = guiUtilities.getImage("close");
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

        pwdField.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (newValue.contains("\"")|| newValue.contains("\'") || newValue.contains("<")|| newValue.contains(">")) {
                    String s = pwdField.getText().substring(0, pwdField.getText().length()-1);
                    pwdField.setText(s);
                } else {

                    int strength = checkPasswordStrength(pwdField.getText());

                    switch (strength) {
                        case 0:
                            pwdQualityIndicator.setStyle(null);
                            pwdQualityLabel.setVisible(false);
                            break;

                        case 1:
                            pwdQualityLabel.setText("Poor");
                            pwdQualityLabel.setStyle(errorMessage);
                            pwdQualityLabel.setVisible(true);
                            break;

                        case 2:
                            pwdQualityLabel.setText("Medium");
                            pwdQualityLabel.setStyle(warningMessage);
                            pwdQualityLabel.setVisible(true);
                            break;

                        case 3:
                            pwdQualityLabel.setText("Excellent");
                            pwdQualityLabel.setStyle(successMessage);
                            pwdQualityLabel.setVisible(true);
                            break;

                    }

                    pwdQualityIndicator.setProgress((double) strength / 3);
                }
            }

        });

    }

    /**
     * TODO document
     * @param pwd
     * @return
     */
    private static int checkPasswordStrength(String pwd){

            int len = pwd.length();
            int upChars=0, lowChars=0;
            int special=0, digits=0;

            for(int i=0; i<len; i++){

                if(Character.isUpperCase(pwd.charAt(i)))
                    upChars++;
                else if(Character.isLowerCase(pwd.charAt(i)))
                    lowChars++;
                else if(Character.isDigit(pwd.charAt(i)))
                    digits++;
                else{
                    if(pwd.charAt(i)=='<' || pwd.charAt(i)=='>'){
                        System.out.println("\nThe Password is Malicious!");
                        return -1;
                    } else {
                        special++;
                    }
                }
            }

        if(upChars!=0 && lowChars!=0 && digits!=0 && special!=0) {
            if(len>=10 && (special+digits)>= len/5) {
                /*
                 * Se il numero di caratteri numerici e speciali è almeno 1/5 della password, questa sarà ritenuta
                 * altamente sicura
                 */
                return 3; //strong
            } else {
                return 2; //med strength
            }
        } else if (lowChars!=0 && (upChars!=0 || digits==0 || special==0)){
            if(len>12){
                return 2;
            } else
                return 1;
        } else if (len == 0) { // password vuota
            return 0;
        } else{ // tutti gli altri casi
            return 1;
        }

    }

    /**
     * TODO document
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

    @FXML protected void handleCloseButtonAction(ActionEvent event){
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML protected void handleCancelButton(){
        try {
            EmotionalSongsClient.setStage(new Scene(EmotionalSongsClient.getLoader().load(EmotionalSongsClient.class.getResource("login.fxml"))), LoginController.WIDTH, LoginController.HEIGHT, true);
            EmotionalSongsClient.getStage().show();
        } catch (IOException e){
            e.printStackTrace();
            // TODO Auto generated stub
        }
    }


    @FXML protected void handleConfirmButton(){
        boolean isInputValid = true;

        if(nomeField.getText().isBlank()){
            GUIUtilities.setErrorStyle(nomeField);
            isInputValid = false;
        } else{GUIUtilities.setDefaultStyle(nomeField);}

        if (cognomeField.getText().isBlank()){
            GUIUtilities.setErrorStyle(cognomeField);
            isInputValid = false;
        } else{GUIUtilities.setDefaultStyle(cognomeField);}

        if (!isValidCF(codFiscField.getText())){
            GUIUtilities.setErrorStyle(codFiscField);
            if(!codFiscField.getText().isBlank())
                codFiscField.setPromptText("Invalid CF"); // questa istruzione effettuerà la sovrascrittura del prompt text aggiunto in setErrorStyle
            isInputValid = false;

        } else{ GUIUtilities.setDefaultStyle(codFiscField);}

        if (emailField.getText().isBlank() | !emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") ){

            GUIUtilities.setErrorStyle(emailField);
            if(!emailField.getText().isBlank())
                emailField.setPromptText("Invalid email");
            isInputValid = false;

        } else{GUIUtilities.setDefaultStyle(emailField);}

        if (viaField.getText().isBlank()){
            GUIUtilities.setErrorStyle(viaField);
            isInputValid = false;
        } else{GUIUtilities.setDefaultStyle(emailField);}

        if (numberField.getText().isBlank() | !numberField.getText().matches("\\d.*") ){
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

        if (usernameField.getText().isBlank() | checkUsernameAvailability()) { // TODO aggiungere controllo sulla disponibilità dell'username (l'utente potrebbe non aver cliccato "controlla username" oppure l'username scelto potrebbe non essere disponibile)
            GUIUtilities.setErrorStyle(usernameField);
            isInputValid = false;
        } else {GUIUtilities.setDefaultStyle(usernameField);}

        if(!isPwdDisplayed){
            if (pwdField.getText().isBlank()){
                GUIUtilities.setErrorStyle(pwdField);
                isInputValid = false;
            } else{GUIUtilities.setDefaultStyle(pwdField);} // TODO verify behaviour with hidden field

        } else{
            if (overlappingPasswordTF.getText().isBlank()){
                GUIUtilities.setErrorStyle(overlappingPasswordTF);
                isInputValid = false;
            } else{GUIUtilities.setDefaultStyle(overlappingPasswordTF);}
        }

        if(isInputValid){ // TODO inserire l'utente nel db - Come trasferire i dati? RSAEncription?

            System.out.println(
                    "Nome: " + nomeField.getText()
                    + "\nCognome: " + cognomeField.getText()
                    + "\nCF: " + codFiscField.getText()
                    + "\nEmail: " + emailField.getText()
                    + "\nIndirizzo: " + viaField.getText() + " " + numberField.getText() + ", " + comuneField.getText() + " ("+provField.getText().toUpperCase(Locale.ROOT)+ ") " + capField.getText()
                    +"\nUsername: " + usernameField.getText()
                    + "\nPassword: " + pwdField.getText()
                    +"\n"
            );

        }

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
     * TODO document put in its own class
     */
    @FXML protected void moveWindow() {

        this.pane.setOnMousePressed(new EventHandler<MouseEvent>() {

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
                    // evita che la finestra snappi alle coordinate 0,0 (cosa che avviene alla primo trascinamento della schermata)
                    calculateGap(event);
                }

                EmotionalSongsClient.getStage().setX(event.getScreenX() - xOffset);
                EmotionalSongsClient.getStage().setY(event.getScreenY() - yOffset);
            }
        });

    }

    /**
     * TODO document and implement checks on the db side - the outcome is currently random
     */
    @FXML protected boolean checkUsernameAvailability() {

        try {

            boolean exists = EmotionalSongsClient.auth.usernameExists(usernameField.getText());

            checkUsernameResultLbl.setVisible(true);
            checkUsernameResultImg.setVisible(true);

            if (!exists){

                checkUsernameResultImg.setImage(successIcon);
                checkUsernameResultLbl.setText("Username disponibile");
                GUIUtilities.setDefaultStyle(usernameField);

                return true;

            } else{

                checkUsernameResultImg.setImage(failureIcon);
                checkUsernameResultLbl.setText("Username non disponibile");
                GUIUtilities.setErrorStyle(usernameField);
                usernameField.setPromptText("");
                return false;

            }

        } catch (Exception e) {

            /*Dialog<String> dialog = new Dialog<String>();
            dialog.setTitle("Errore");

            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();

            dialogStage.getIcons().add(guiUtilities.getImage("failure"));

            dialog.setContentText("Impossibile contattare il server."); // TODO modify?

            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();*/

            try{
                Stage connectionFailedStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("connectionFailed.fxml"));
                Scene connectionFailedScene = new Scene(fxmlLoader.load());
                connectionFailedStage.setScene(connectionFailedScene);
                connectionFailedStage.initStyle(StageStyle.UNDECORATED);
                connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
                connectionFailedStage.setResizable(false);
                connectionFailedStage.show();
            }catch(IOException e1){
                e1.printStackTrace();
            }

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

        if(somma%26 + 'A' != cf.charAt(15)){return false;}

        return true;

    }
}
