package emotionalsongs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

public class UserRegistrationController implements Initializable {

    protected static final int HEIGHT = 750;
    protected static final int WIDTH = 800;

    private static double xOffset = 0;
    private static double yOffset = 0;

    private static String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
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
    @FXML private TextField overlappingPasswordLabel;
    @FXML private Button closeBtn;
    @FXML private GridPane tst;
    @FXML private Pane pane;

    private Image failureIcon;
    private Image successIcon;
    private Image cross;
    private Image eye;
    private Image eyeCrossed;
    private Boolean ispwdDisplyed = false;

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

        if(!ispwdDisplyed) {
            eyeBtn.setGraphic(new ImageView(eyeCrossed));
            overlappingPasswordLabel.setText(pwdField.getText());
            pwdField.setVisible(false);
            overlappingPasswordLabel.setVisible(true);
            ispwdDisplyed = true;
        } else{
            eyeBtn.setGraphic(new ImageView(eye));
            pwdField.setText(overlappingPasswordLabel.getText());
            overlappingPasswordLabel.setVisible(false);
            pwdField.setVisible(true);
            ispwdDisplyed = false;
        }

    }

    @FXML protected void handleCloseButtonAction(ActionEvent event){
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        //stage.setIconified(true);
        stage.close();
    }

    @FXML protected void handleCancelButton(){
        try {
            EmotionalSongs.setStage(new Scene(EmotionalSongs.getLoader().load(EmotionalSongs.class.getResource("login.fxml") )), LoginController.WIDTH, LoginController.HEIGHT, true);

            EmotionalSongs.getStage().show();
        } catch (IOException e){
            e.printStackTrace();
            // TODO Auto generated stub
        }
    }

    @FXML protected void handleConfirmButton(){

        // In teoria andrebbe aggiunto un ramo else che resetta lo stile una volta che il campo è stato valorizzato
        String message = "Mandatory field";
        int valuesInserted = 11;

        if(nomeField.getText().isBlank()){
            nomeField.setStyle(errorStyle);
            nomeField.setPromptText(message);
            valuesInserted--;
        } else{
            nomeField.setStyle(null);
            nomeField.setPromptText(null);
        }

        if (cognomeField.getText().isBlank()){
            cognomeField.setStyle(errorStyle);
            cognomeField.setPromptText(message);
            valuesInserted--;
        } else{
            cognomeField.setStyle(null);
            cognomeField.setPromptText(null);
        }

        if (!isValidCF(codFiscField.getText())){
            if(codFiscField.getText().isBlank())
                codFiscField.setPromptText(message);
            codFiscField.setPromptText("Invalid CF");
            codFiscField.setStyle(errorStyle);
            valuesInserted--;
        } else{
            codFiscField.setStyle(null);
            codFiscField.setPromptText(null);

        }

        if (emailField.getText().isBlank() || !emailField.getText().contains("@")){

            // !emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") ){ // TODO add better email checks with regex

            if(!emailField.getText().isBlank()){
                emailField.setPromptText("Invalid email");
            } else{
                emailField.setPromptText(message);
            }
            emailField.setStyle(errorStyle);
            valuesInserted--;

        } else{
            emailField.setStyle(null);
            emailField.setPromptText(null);
        }

        if (viaField.getText().isBlank()){
            viaField.setStyle(errorStyle);
            viaField.setPromptText(message);
            valuesInserted--;
        } else{
            viaField.setStyle(null);
            viaField.setPromptText(null);
        }
        if (numberField.getText().isBlank()){ // TODO aggiungere controlli che assicurino la presenza di almeno un numero all'interno del valore (15A, 23B, etc..)
            numberField.setStyle(errorStyle);
            numberField.setPromptText(message);
            valuesInserted--;
        } else{
            numberField.setStyle(null);
            numberField.setPromptText(null);
        }
        if (comuneField.getText().isBlank()){
            comuneField.setStyle(errorStyle);
            comuneField.setPromptText(message);
            valuesInserted--;
        } else{
            comuneField.setStyle(null);
            comuneField.setPromptText(null);
        }
        if (provField.getText().isBlank()){
            provField.setStyle(errorStyle);
            provField.setPromptText(message);
            valuesInserted--;
        } else{
            provField.setStyle(null);
            provField.setPromptText(null);
        }
        if (capField.getText().isBlank()){
            capField.setStyle(errorStyle);
            capField.setPromptText(message);
            valuesInserted--;
        } else{
            capField.setStyle(null);
            capField.setPromptText(null);
        }
        if (usernameField.getText().isBlank()){ // TODO aggiungere controllo sulla disponibilità dell'username (l'untente potrebbe non aver cliccato "controlla username" oppure l'username scelto potrebbe non essere disponibile)
            usernameField.setStyle(errorStyle);
            usernameField.setPromptText(message);
            valuesInserted--;
        } else{
            usernameField.setStyle(null);
            usernameField.setPromptText(null);
        }
        if(!ispwdDisplyed){
            if (pwdField.getText().isBlank()){
                pwdField.setStyle(errorStyle);
                pwdField.setPromptText(message);
                valuesInserted--;
            } else{
                pwdField.setStyle(null);
                pwdField.setPromptText(null);
            }
        } else{
            if (overlappingPasswordLabel.getText().isBlank()){
                overlappingPasswordLabel.setStyle(errorStyle);
                overlappingPasswordLabel.setPromptText(message);
                valuesInserted--;
            } else{
                overlappingPasswordLabel.setStyle(null);
                overlappingPasswordLabel.setPromptText(null);
            }
        }

        if(valuesInserted == 11){ // TODO inserire l'utente nel db

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
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            eye =  new Image(new FileInputStream(".\\src\\main\\resources\\emotionalsongs\\Images\\view.png"));
            eyeCrossed =  new Image(new FileInputStream(".\\src\\main\\resources\\emotionalsongs\\Images\\hide.png"));
            successIcon =  new Image(new FileInputStream(".\\src\\main\\resources\\emotionalsongs\\Images\\correct15px.png"));
            failureIcon =  new Image(new FileInputStream(".\\src\\main\\resources\\emotionalsongs\\Images\\failure15px.png"));
            cross =  new Image(new FileInputStream(".\\src\\main\\resources\\emotionalsongs\\Images\\closeIcon10px.png"));
            eyeBtn.setText("");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // TODO auto-generated stub
        }

        eyeBtn.setGraphic(new ImageView(eye));
        eyeBtn.setFocusTraversable(false);

        closeBtn.setLayoutX(WIDTH-30);
        closeBtn.setLayoutY(4);
        closeBtn.setText(null);

        closeBtn.setGraphic(new ImageView(cross));

        checkUsernameResultImg.setVisible(false);

        forceTextInput(provField, 2);
        forceNumericInput(capField, 5);

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
     * @param event
     */
    private void calculateGap(MouseEvent event){
        xOffset = event.getScreenX() - EmotionalSongs.getStage().getX();
        yOffset = event.getScreenY() - EmotionalSongs.getStage().getY();
    }

    /**
     * TODO document put in its own class
     */
    @FXML protected void moveWindow() {

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
     * TODO document and implement checks with db
     */
    @FXML protected void checkUsernameAvailability(){

        Random random = new Random();

        if (random.nextInt()<0.5){
            checkUsernameResultImg.setImage(successIcon);
            checkUsernameResultLbl.setText("Username disponibile");
        } else{
            checkUsernameResultImg.setImage(failureIcon);
            checkUsernameResultLbl.setText("Username non disponibile");
        }
        checkUsernameResultLbl.setVisible(true);
        checkUsernameResultImg.setVisible(true);

    }

    /**
     * TODO document
     * @param tf
     * @param maxLength
     */
    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

    /**
     * TODO document
     * @param tf
     */
    public static void forceTextInput(final TextField tf) {

        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (newValue.matches("\\d*")) {
                    tf.setText(newValue.replaceAll("[\\d]", ""));
                }
            }
        });

    }

    /**
     * TODO document
     * @param tf
     * @param maxLen
     */
    public static void forceTextInput(final TextField tf, final int maxLen) {

        tf.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (newValue.matches(".*\\d.*")) {
                    tf.setText(newValue.replaceAll("\\d", ""));
                }else if (tf.getText().length() > maxLen) {
                    String s = tf.getText().substring(0, maxLen);
                    tf.setText(s);
                }
            }

        });

    }

    /**
     * TODO document
     * @param tf
     */
    public static void forceNumericInput(final TextField tf) {

        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

    }

    /**
     * TODO document
     * @param tf
     * @param maxLen
     */
    public static void forceNumericInput(final TextField tf, final int maxLen) {

        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf.setText(newValue.replaceAll("[^\\d]", ""));
                }else if (tf.getText().length() > maxLen) {
                    String s = tf.getText().substring(0, maxLen);
                    tf.setText(s);
                }
            }
        });

    }

    public static boolean isValidCF(String cf){

        if(!cf.matches("^[0-9A-Z]{16}$")){
            return false;
        }

        int somma = 0;
        String evenMap = "BAFHJNPRTVCESULDGIMOQKWZYX";

        for(int i = 0; i < 15; i++){

            int charIntValue = cf.charAt(i);
            int idxEvenMap;

            if('0' <= charIntValue && charIntValue <= '9' ){
                idxEvenMap = charIntValue - '0';
            }else{
                idxEvenMap = charIntValue - 'A';
            }

            if( (i & 1) == 0 ){
                idxEvenMap = evenMap.charAt(idxEvenMap) - 'A';
            }

            somma += idxEvenMap;

        }

        if(somma%26 + 'A' != cf.charAt(15)){return false;}

        return true;

    }
}
