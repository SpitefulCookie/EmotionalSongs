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

/**
 * Controller class for managing user-related information in the user interface.
 * This class handles the display and retrieval of user data in the user profile section of the application.
 */
public class UserController {

    @FXML private Label usernameLabel;
    @FXML private Label nameAndLastNameLabel;
    @FXML private Label codiceFiscaleLabel;
    @FXML private Label emailLabel;
    @FXML private Label addressLabel;

    /**
     * Sets and displays the user information in the user profile section.<br><br>
     *
     * This method retrieves user data associated with the provided username and populates the user profile section of the UI with the relevant information.
     *
     * @param user The username of the user for whom to retrieve and display information.
     * @return True if user data retrieval and display were successful, otherwise false.
     */
    protected boolean setUser(String user) {

        // retrive the user data
        String[] userData = retrieveUserData(user).split(",");

        if (userData.length != 1) {

            // set the label
            usernameLabel.setText(user);
            nameAndLastNameLabel.setText(" " + userData[0]);
            codiceFiscaleLabel.setText(userData[1]);
            emailLabel.setText(userData[2]);
            addressLabel.setText(userData[3] + " n." + userData[4] + "   " + userData[5] + " -" + userData[7] + " (" + userData[6].toUpperCase() + " )");

            // now the user data were retrieved
            EmotionalSongsClientController.setUserDataRetrieved(true);

            // DEBUG TODO remove
            System.out.println("Dati dell'utente: " + EmotionalSongsClientController.getUsername() + " -> " + Arrays.toString(userData));

            return true;

        }else{

            // the user data were not retrieved
            EmotionalSongsClientController.setUserDataRetrieved(false);

            return false;

        }

    }

    /**
     * Retrieves user data from the server.<br><br>
     *
     * This method communicates with the server to retrieve encrypted user data associated with the provided username.
     * The retrieved data is then decrypted using the RSA algorithm and returned as a string.
     *
     * @param user The username of the user for whom to retrieve data.
     * @return The decrypted user data as a string.
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
