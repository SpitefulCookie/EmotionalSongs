package emotionalsongs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * TODO Document
 */
public class EmotionalSongsServer extends Application {

    private static Stage esStage;
    private static FXMLLoader loader;
    private static ConnectionVerify connectionVerify;
    protected static ServerMainViewController mainView;

    protected static QueryHandler qh;
    protected static AuthManagerImpl auth;

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
        stage.getIcons().add(new Image(new FileInputStream("EmotionalSongsServer/src/main/resources/Images/blueFire.png")));
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
        connectionVerify.setPingDelay(delay);
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

}

