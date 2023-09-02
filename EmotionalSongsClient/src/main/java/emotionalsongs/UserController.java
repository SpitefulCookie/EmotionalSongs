package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * Controller class for managing user-related information in the user interface.
 * This class handles the display and retrieval of user data in the user profile section of the application.
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
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
            String data= EmotionalSongs.auth.getUserData(user); //AuthManager.decryptRSA(EmotionalSongs.auth.getUserData(user, EmotionalSongs.ping.getPublicKey()), EmotionalSongs.ping.getPrivateKey());
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
