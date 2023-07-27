package emotionalsongs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.IOException;

public class EmotionalSongs extends Application {

    private static Stage esStage;
    private static FXMLLoader loader;

    @Override
    public void start(Stage stage) throws IOException {

        loader = new FXMLLoader(EmotionalSongs.class.getResource("login.fxml"));
        Scene scene = new Scene(loader.load());

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setWidth(LoginController.WIDTH);
        stage.setHeight(LoginController.HEIGHT);
        stage.setTitle("Emotional Songs");
        stage.getIcons().add(new Image(new FileInputStream(".\\src\\main\\resources\\emotionalsongs\\Images\\fire.png")));
        stage.setResizable(false);
        LoginController.setStage(stage);
        stage.setScene(scene);
        esStage = stage;
        stage.show();

    }


    protected static FXMLLoader getLoader(){
        return loader;
    }

    protected static Stage getStage(){
        return esStage;
    }

    protected static void setStage(Scene scene, int width, int heigth, boolean isWindowResizable){
        esStage.close();
        esStage.setWidth(width);
        esStage.setHeight(heigth);
        esStage.setResizable(isWindowResizable);
        esStage.setScene(scene);
        esStage.show();
    }

    public static void main(String[] args) {
        new EmotionalSongs().launch();
    }
}

