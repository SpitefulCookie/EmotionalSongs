package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConnectionFailedController {

    @FXML
    private Button tryAgainButton;
    @FXML
    private Button exitButton;

    /**
     * TODO document
     */
    @FXML
    public void handleTryAgainButtonAction(){
        // TODO riprovare connessione con server
        try{
            // try again to connect to the server
            Registry reg = LocateRegistry.getRegistry(EmotionalSongsClient.getServerAddress(), EmotionalSongsClient.getPort());
            EmotionalSongsClient.auth = (AuthManager) reg.lookup("AuthManager");
            // close the connection failed stage
            closeConnectionFailedStage(tryAgainButton);
        }catch (RemoteException | NotBoundException e){
            // close the connection failed stage
            closeConnectionFailedStage(tryAgainButton);
            // display the connection failed stage
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
    }

    /**
     * TODO document
     */
    @FXML
    public void handleExitButtonAction(){
        // close the connection failed stage
        closeConnectionFailedStage(exitButton);
        // close the main stage
        //EmotionalSongsClient.getStage().close(); // TODO togliere dal commento
    }

    /**
     * TODO document
     * @param button
     */
    public void closeConnectionFailedStage(Button button){
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
