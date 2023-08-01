package emotionalsongs;

/* TODO Add this to all java classes
 * Progetto svolto da:
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
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
public class EmotionalSongsClient extends Application {

    private static final int PORT = 6789;
    private static final String SERVER_ADDRESS = "localhost";

    private static Stage esStage;
    private static FXMLLoader loader;

    private GUIUtilities guiUtilities;
    private static PingClientClientImpl ping;

    protected static AuthManager auth;
    //protected static RepositoryManager repo; // TODO implement

    /**
     * TODO Document
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {

        //Scene scene = new Scene(FXMLLoader.load(new File("C:\\Users\\orobi\\Documents\\EmotionalSongs\\EmotionalSongsClient\\src\\main\\resources\\emotionalsongs\\login.fxml").toURI().toURL()));

        loader = new FXMLLoader(EmotionalSongsClient.class.getResource("login.fxml"));
        Scene scene = new Scene(loader.load());
        guiUtilities = GUIUtilities.getInstance(); // creo l'oggetto in questo modo per via del pattern singleton

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setWidth(LoginController.WIDTH);
        stage.setHeight(LoginController.HEIGHT);
        stage.setTitle("Emotional Songs");
        //stage.getIcons().add(new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/fire.png")));
        stage.getIcons().add(guiUtilities.getImage("fire"));
        stage.setResizable(false);
        LoginController.setStage(stage);
        stage.setScene(scene);
        esStage = stage;
        stage.show();

        try {

            Registry reg = LocateRegistry.getRegistry(SERVER_ADDRESS, PORT);
            auth = (AuthManager) reg.lookup("AuthManager");
            //repo = (RepositoryManager) reg.lookup("RepoManager"); // TODO add once implemented
            ping = new PingClientClientImpl();


        } catch (RemoteException | NotBoundException e) {

            /*Dialog<String> dialog = new Dialog<String>();
            dialog.setTitle("Errore");

            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);

            dialog.setContentText("Impossibile contattare il server."); // TODO modify the appearance and text content?
            Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();

            dialogStage.getIcons().add(guiUtilities.getImage("failure"));
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
            System.err.println("Server not found on address: " + SERVER_ADDRESS +":"+ PORT); // TODO remove before turning in the project*/

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
     * TODO Document
     * @return
     */
    protected static FXMLLoader getLoader(){
        return loader;
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
     * @param heigth
     * @param isWindowResizable
     */
    protected static void setStage(Scene scene, int width, int heigth, boolean isWindowResizable){
        esStage.close();
        esStage.setWidth(width);
        esStage.setHeight(heigth);
        esStage.setResizable(isWindowResizable);
        esStage.setScene(scene);
        esStage.show();
    }

    /**
     * TODO document
     * @return
     */
    public static String getServerAddress(){
        return SERVER_ADDRESS;
    }

    /**
     * TODO document
     * @return
     */
    public static int getPort(){
        return PORT;
    }

    /**
     * TODO Document?
     * @param args
     */
    public static void main(String[] args) {new EmotionalSongsClient().launch();}

    public static void unexportClient(){
        try {
            UnicastRemoteObject.unexportObject(ping, true);
        } catch (NoSuchObjectException e) {
            // quest'eccezione viene lanciata qualora non sia stato effettuato l'export dell'oggetto PingClient,
            // questo avviene solo quando il server non è raggiungibile.
        }
    }

    public static void registerClient(){
        try {
            if(auth!= null)
                auth.registerClient(ping);
        } catch (RemoteException e){}
    }
}

