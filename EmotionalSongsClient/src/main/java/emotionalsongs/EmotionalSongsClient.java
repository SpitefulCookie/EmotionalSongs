package emotionalsongs;

/* TODO Add this to all java classes
 * Progetto svolto da:
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * TODO Document
 */
/*
TODO: aggiungere a tutte le classi i metodi richiesti dal progetto
*/
public class EmotionalSongsClient extends Application {

    private static Stage esStage;

    private static PingClientClientImpl ping;

    private GUIUtilities guiUtilities;

    protected static AuthManager auth;
    protected static RepositoryManager repo;

    protected static boolean isConnectionInitialized = false;

    /**
     * TODO Document
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage){

        guiUtilities = GUIUtilities.getInstance();
        guiUtilities.initScenes();

        Scene scene = guiUtilities.getScene("login.fxml");

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setWidth(LoginController.WIDTH);
        stage.setHeight(LoginController.HEIGHT);
        stage.setTitle("Emotional Songs");
        stage.getIcons().add(guiUtilities.getImage("fire"));
        stage.setResizable(false);
        LoginController.setStage(stage);
        stage.setScene(scene);
        esStage = stage;
        stage.show();

    }

    protected static void initializeServerConnection(boolean suppressConnectionWarning){

        isConnectionInitialized = false;

        try {

            Registry reg = LocateRegistry.getRegistry(ClientSettingController.getServerAddress(), ClientSettingController.getServerPort());
            auth = (AuthManager) reg.lookup("AuthManager");
            repo = (RepositoryManager) reg.lookup("RepoManager");
            ping = new PingClientClientImpl();

            isConnectionInitialized = true;

        } catch (RemoteException | NotBoundException e){
            if(!suppressConnectionWarning) {
                Stage connectionFailedStage = new Stage();

                connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
                connectionFailedStage.initStyle(StageStyle.UNDECORATED);
                connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
                connectionFailedStage.setResizable(false);
                connectionFailedStage.show();
            }
        }
    }



    /**
     * TODO Document
     * @return
     */
    protected static Stage getStage(){
        return esStage;
    }

    /**
     * TODO Document
     * @param scene
     * @param width
     * @param height
     * @param isWindowResizable
     */
    protected static void setStage(Scene scene, int width, int height, boolean isWindowResizable){
        esStage.close();
        esStage.setWidth(width);
        esStage.setHeight(height);
        esStage.setResizable(isWindowResizable);
        esStage.setScene(scene);
        esStage.show();
    }

    /**
     * TODO Document?
     * @param args
     */
    public static void main(String[] args) {new EmotionalSongsClient().launch();}

    public static void disconnectClient(){
        try {
            auth.disconnect(ping);
            UnicastRemoteObject.unexportObject(ping, true);
        } catch (NoSuchObjectException e) {
            // quest'eccezione viene lanciata qualora non sia stato effettuato l'export dell'oggetto PingClient,
            // questo tipicamente avviene solo quando il server non è raggiungibile.
        } catch (RemoteException e) {
            //
        }
    }

    public static void registerClient(){
        try {
            if(auth!= null)
                auth.registerClient(ping);
        } catch (RemoteException e){
            //
        }
    }
}

