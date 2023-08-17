package emotionalsongs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMISocketFactory;
import java.util.Enumeration;

/**
 * TODO Document
 */
public class EmotionalSongsServer extends Application {

    protected static String loggedUser = "";
    private static Stage esStage;
    private static FXMLLoader loader;
    private static ConnectionVerify connectionVerify;
    protected static ServerMainViewController mainView;

    private static final int PORT = 6789;

    private static QueryHandler qh = null;
    private static AuthManagerImpl auth = null;
    private static RepositoryManagerImpl repo = null;

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

    public static QueryHandler getQueryHandlerInstance() {
        return EmotionalSongsServer.qh;
    }

    public static void initializeQueryHandler(QueryHandler queryHandler) {
        qh = queryHandler;
    }

    public static void initializeAuthManager(AuthManagerImpl authManager) {
        auth = authManager;
    }

    public static AuthManagerImpl getAuthManagerInstance() {
        return auth;
    }

    /**
     * TODO Document?
     * @param stage
     */
    @Override
    public void start(Stage stage) throws IOException {

        loader = new FXMLLoader(EmotionalSongsServer.class.getResource("serverLogin.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setWidth(ServerLoginController.WIDTH);
        stage.setHeight(ServerLoginController.HEIGHT);
        stage.setTitle("Emotional Songs Server");
        stage.getIcons().add(GUIUtilities.getInstance().getImage("blueFire"));
        stage.setResizable(false);
        ServerLoginController.setStage(stage);
        stage.setScene(scene);

        // Invoca il metodo shutdownServer() (un-export dell'oggetto remoto)
        // quando l'applicazione viene terminata mediante la barra del titolo

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                ServerMainViewController.shutdownServer(false);
                System.err.println("Shutting down server...");
                Platform.exit();
                System.exit(0);
            }
        });

        esStage = stage;
        stage.show();

    }

    protected static void initializeConnectionVerify(){
        if(connectionVerify == null){
            connectionVerify = new ConnectionVerify(); // 30s delay between pings
        }
    }

    protected static void setDelay(long delay){
        initializeConnectionVerify();
        ConnectionVerify.setPingDelay(delay);
    }

    /**
     * TODO document
     *
     */
    protected static FXMLLoader getLoader(){
        return loader;
    }

    /**
     * TODO document
     *
     */
    protected static Stage getStage(){
        return esStage;
    }

    /**
     * TODO document
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

    public static void main(String[] args) {
        new EmotionalSongsServer().launch();
    }

    protected static void setMainViewController(ServerMainViewController server){
        mainView = server;
    }

    protected static int getServerPort(){return PORT;}

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

