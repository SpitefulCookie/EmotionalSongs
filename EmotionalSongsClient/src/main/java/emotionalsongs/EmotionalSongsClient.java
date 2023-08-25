package emotionalsongs;

/*
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

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/*
TODO: aggiungere a tutte le classi i metodi richiesti dal progetto
*/

/**
 * The main class for the EmotionalSongs client application. Manages the user interface, server connection,
 * and various utility methods related to client functionality.
 * <p>
 * This class extends {@link Application} and serves as the entry point for the client application. It handles
 * initializing the graphical user interface, managing the server connection, and providing various utility methods
 * for client operations.
 * <p>
 * The client application uses JavaFX for its graphical interface and relies on RMI to communicate with the server
 * for authentication and data retrieval.
 *
 */
public class EmotionalSongsClient extends Application {

    private static Stage esStage;

    protected static PingableClientClientImpl ping;

    protected static AuthManager auth;
    protected static RepositoryManager repo;

    protected static boolean isConnectionInitialized = false;

    /**
     * Starts the client application and initializes its primary stage.<br><br>
     *
     * <p>This method is the entry point for the client application.
     * It invokes the methods {@link GUIUtilities#initNode()} and {@link GUIUtilities#initScenes()} from the class
     * {@code GUIUtilities} which are responsible for loading and initializing the majority of the application's graphical interfaces;
     * It then initializes and displays the login screen.
     *
     * @param stage The primary {@link Stage} of the client application.
     */
    @Override
    public void start(Stage stage){

        GUIUtilities guiUtilities = GUIUtilities.getInstance();
        guiUtilities.initScenes();
        guiUtilities.initNode();

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

    /**
     * Initializes the server connection by looking up remote objects from the RMI registry.<br><br>
     * This method is responsible for initializing the connection with the server by looking up the remote objects from
     * the RMI registry; If the connection attempt fails, this method will display a connection warning.
     *
     * @param suppressConnectionWarning If true, suppresses the connection warning dialog.
     */
    protected static void initializeServerConnection(boolean suppressConnectionWarning){

        isConnectionInitialized = false;

        try {

            if(ping != null){
                disconnectClient();
            }

            Registry reg = LocateRegistry.getRegistry(ClientSettingController.getServerAddress(), ClientSettingController.getServerPort());
            auth = (AuthManager) reg.lookup("AuthManager");
            repo = (RepositoryManager) reg.lookup("RepoManager");
            ping = new PingableClientClientImpl();

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
     * Retrieves the primary stage of the application.
     *
     * @return The primary {@link Stage} of the application.
     */
    protected static Stage getStage(){
        return esStage;
    }

    /**
     * Sets the stage with a new scene and specified dimensions.
     *
     * @param scene The new {@link Scene} to be set.
     * @param width The width of the stage.
     * @param height The height of the stage.
     * @param isWindowResizable Determines if the window is resizable.
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
     * Entry point for launching the client application.
     *
     * @param args The command-line arguments. (Unused)
     */
    public static void main(String[] args) {launch();}

    /**
     * Disconnects the client from the server, unregistering it and unexporting the remote object.
     */
    public static void disconnectClient(){
        try {
            if(auth!=null)
                auth.disconnect(ping);
            UnicastRemoteObject.unexportObject(ping, true);
        } catch (NoSuchObjectException e) {
            // quest'eccezione viene lanciata qualora non sia stato effettuato l'export dell'oggetto PingClient,
            // questo tipicamente avviene solo quando il server non è raggiungibile.
        } catch (RemoteException e) {
            System.err.println("Remote exception thrown while attempting to disconnect the client. Reason:\n" + e.getMessage());
        }
    }

    /**
     * Registers the client with the server.<br>
     * The {@link PingableClientClientImpl} reference provided will be used as a mean to remotely ping the client from the server.
     */
    public static void registerClient(){
        try {
            if(auth!= null)
                auth.registerClient(ping);
        } catch (RemoteException e){
            System.err.println("Remote exception thrown while attempting to register the client. Reason:\n" + e.getMessage());
        }
    }
}

