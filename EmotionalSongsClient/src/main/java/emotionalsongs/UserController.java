package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class UserController {

    @FXML private Label usernameLabel;
    @FXML private Label nameAndLastNameLabel;
    @FXML private Label codiceFiscaleLabel;
    @FXML private Label emailLabel;
    @FXML private Label addressLabel;

    /**
     * TODO document
     * @param user
     */
    protected void setUser(String user){

        // retrive the user data
        String[] userData = retrieveUserData(user).split(",");

        // set the label
        usernameLabel.setText(user);
        nameAndLastNameLabel.setText(" " + userData[0]);
        codiceFiscaleLabel.setText(userData[1]);
        emailLabel.setText(userData[2]);
        addressLabel.setText(userData[3] + " n." + userData[4] + "   " + userData[5] + " -" + userData[7] + " (" + userData[6].toUpperCase() + " )");

        // DEBUG TODO remove
        System.out.println("Dati dell'utente: " + EmotionalSongsClientController.getUsername() + " -> " + Arrays.toString(userData));

    }

    /**
     * TODO document
     * @return
     */
    protected String retrieveUserData(String user) {
        try {
            String data= AuthManager.decryptRSA(EmotionalSongsClient.auth.getUserData(user, EmotionalSongsClient.ping.getPublicKey()),EmotionalSongsClient.ping.getPrivateKey()); // TODO rimpiazzare 'test' con l'userId dell'utente loggato
            return data.substring(1,data.length()-1);
        } catch (RemoteException e) {

            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();

            return "";
        }
    }
}
