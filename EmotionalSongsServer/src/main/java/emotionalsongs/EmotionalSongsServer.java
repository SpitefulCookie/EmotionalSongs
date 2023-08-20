package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.*;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;

/**
 * The {@code EmotionalSongsServer} class represents the main entry point for the Emotional Songs server application.<br><br>
 *
 * <p>This class extends {@link javafx.application.Application}, initializing the graphical user interface and managing
 * server components. It handles the startup and initialization of the server interface, connections, and resource management.
 *
 * @author <a href="https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
 */
public class EmotionalSongsServer extends Application {

    protected static String loggedUser = "";
    private static Stage esStage;
    private static ConnectionVerify connectionVerify;
    protected static ServerMainViewController mainView;

    private static final int PORT = 6789;

    private static QueryHandler qh = null;
    private static AuthManagerImpl auth = null;
    private static RepositoryManagerImpl repo = null;

    /**
     * Retrieves the instance of the repository manager.<br><br>
     *
     * <p>This method returns the instance of the {@link RepositoryManagerImpl} used by the server application. If the
     * instance is not yet created, it attempts to initialize it.
     *
     * @return The instance of the repository manager.
     */
    public static RepositoryManagerImpl getRepositoryManagerInstance() {

        if(EmotionalSongsServer.repo == null){
            try {
                EmotionalSongsServer.repo = new RepositoryManagerImpl();
            } catch (RemoteException e) {
                // Metodo eseguito localmente, eccezione non lanciata
            }
        }

        return EmotionalSongsServer.repo;

    }

    /**
     * Retrieves the instance of the query handler.
     *
     * @return The instance of the {@link QueryHandler} used by the server application.
     */
    public static QueryHandler getQueryHandlerInstance() {
        return EmotionalSongsServer.qh;
    }

    /**
     * Initializes the query handler instance.<br><br>
     *
     * <p>This method sets the {@link QueryHandler} instance to be used by the server application.
     *
     * @param queryHandler The {@link QueryHandler} instance to be used for database queries.
     */
    public static void initializeQueryHandler(QueryHandler queryHandler) {
        qh = queryHandler;
    }

    /**
     * Initializes the authentication manager instance.<br><br>
     *
     * <p>This method sets the {@link AuthManagerImpl} instance to be used by the server application.
     *
     * @param authManager The {@link AuthManagerImpl} instance to be used for user authentication.
     */
    public static void initializeAuthManager(AuthManagerImpl authManager) {
        auth = authManager;
    }

    /**
     * Retrieves the instance of the authentication manager.<br><br>
     *
     * @return The instance of the {@link AuthManagerImpl} used for user authentication.
     */
    public static AuthManagerImpl getAuthManagerInstance() {
        return auth;
    }

    /**
     * Starts the server application and initializes its primary stage.<br><br>
     *
     * <p>This method is the entry point for the server application. It initializes the primary stage with the server
     * login interface. The method also registers a handler to invoke the {@link ServerMainViewController#shutdownServer(boolean)}
     * method when the application is closed.
     *
     * @param stage The primary {@link Stage} of the server application.
     * @throws IOException If an I/O exception occurs during the loading of the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(EmotionalSongsServer.class.getResource("serverLogin.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setWidth(ServerLoginController.WIDTH);
        stage.setHeight(ServerLoginController.HEIGHT);
        stage.setTitle("Emotional Songs Server");
        stage.getIcons().add(GUIUtilities.getInstance().getImage("blueFire"));
        stage.setResizable(false);
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> ServerMainViewController.shutdownServer(true));

        esStage = stage;
        stage.show();

    }

    /**
     * Unexports server resources.<br><br>
     *
     * <p>This method attempts to unexport the repository manager and authentication manager instances. It handles the case
     * where exceptions might occur due to objects not being initialized or already unexported.
     * This method is called once the server shuts down.
     *
     * @param force If true, forcefully unexports the objects.
     */
    protected static void unexportResources(boolean force){
        try {
            UnicastRemoteObject.unexportObject(repo, force);
            UnicastRemoteObject.unexportObject(auth, force);
        } catch (NoSuchObjectException f){
            if(EmotionalSongsServer.mainView!=null){
                // Se main view è null allora vuol dire che il server è stato chiuso prima che venga effettuato il login,
                // pertanto è normale che le risorse auth e repo siano a loro volta non inizializzate
                EmotionalSongsServer.mainView.logError("A NoSuchObjectException has occurred while attempting to shut down the server.\n");
            }
        }
    }

    /**
     * Initializes the connection verification service for tracking client connections.
     *
     * <p>This method creates a {@link ConnectionVerify} instance if one does not already exist. The service pings clients
     * at regular intervals to verify their connection status.
     */
    protected static void initializeConnectionVerify(){
        if(connectionVerify == null){
            connectionVerify = new ConnectionVerify(); // 30s delay between pings
        }
    }

    /**
     * Retrieves the primary stage of the server application.<br><br>
     *
     * @return The primary {@link Stage} instance of the server application.
     */
    protected static Stage getStage(){
        return esStage;
    }

    /**
     * Sets the properties of the primary stage to display a new scene.<br><br>
     *
     * <p>This method allows changing the contents of the application window by setting a new scene with specified properties.
     *
     * @param scene The new scene to be displayed.
     * @param width The new width of the stage.
     * @param height The new height of the stage.
     * @param isWindowResizable If {@code true}, the window is resizable; otherwise, it is not.
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
     * The main method of the Emotional Songs server application.<br><br>
     *
     * <p>This method is the entry point for starting the server. It launches the JavaFX application using the JavaFX platform
     * initialization mechanism.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Sets the reference to the main view controller of the server.
     *
     * @param server The {@link ServerMainViewController} instance to set as the main view controller.
     */
    protected static void setMainViewController(ServerMainViewController server){
        mainView = server;
    }

    /**
     * Retrieves the server's listening port number.
     *
     * @return The port number the server is listening on.
     */
    protected static int getServerPort(){return PORT;}

    /**
     * Retrieves the LAN IP address of the server.
     *
     * <p>This method iterates through the available network interfaces and their addresses to find a site-local address
     * that is typically associated with the LAN. The method returns the IP address as a string.
     *
     * @return The LAN IP address of the server, or an empty string if no suitable address is found.
     */
    protected static String getLanIpAddress(){

        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while( networkInterfaceEnumeration.hasMoreElements()){
                for ( InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses())
                    if (interfaceAddress.getAddress().isSiteLocalAddress())
                        return interfaceAddress.getAddress().getHostAddress();
            }
        } catch (SocketException e) {
            //
        }
        return "";
    }
}

