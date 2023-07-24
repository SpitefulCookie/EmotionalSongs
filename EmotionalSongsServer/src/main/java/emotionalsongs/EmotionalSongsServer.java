package emotionalsongs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * TODO Document
 */
public class EmotionalSongsServer extends Application {

    private static Stage esStage;
    private static FXMLLoader loader;

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
        esStage = stage;
        stage.show();

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

}

