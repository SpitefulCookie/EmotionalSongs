package emotionalsongs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class GUIUtilities {

    private final HashMap<String, Scene> appScenes;
    private final HashMap<String, Image> images;
    private static GUIUtilities guiUtilities;

    private static final String[] guiControllers = {"clientLoginSettings.fxml", "connectionFailed.fxml", "exit.fxml", "login.fxml", "search.fxml", "user.fxml", "UserRegistration.fxml"};

    GUIUtilities(){
        // creo la hashmap
        images = new HashMap<>();
        appScenes = new HashMap<>();

        // setto (aggiungo) le varie immagini nella hashmap
        initImages();

    }

    // pattern singleton
    public static GUIUtilities getInstance(){
        if(guiUtilities == null){guiUtilities = new GUIUtilities();}
        return guiUtilities;
    }

    /**
     * TODO document
     */
    protected void initScenes(){

        try {

            for (String controller : guiControllers) {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller));
                appScenes.put(controller, new Scene(fxmlLoader.load()));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * TODO document
     * @param tf
     * @param maxLen
     */
    public static void forceTextInput(final TextField tf, final int maxLen) {

        tf.textProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.matches(".*\\d.*")) {
                tf.setText(newValue.replaceAll("\\d", ""));
            }else if (tf.getText().length() > maxLen) {
                String s = tf.getText().substring(0, maxLen);
                tf.setText(s);
            }
        });
    }

    /**
     * TODO document
     * @param tf
     */
    public static void forceNumericInput(final TextField tf) {

        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * TODO document
     * @param tf
     * @param maxLen
     */
    public static void forceNumericInput(final TextField tf, final int maxLen) {

        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (tf.getText().length() > maxLen) {
                String s = tf.getText().substring(0, maxLen);
                tf.setText(s);
            }
        });

    }


    protected static <T> void setErrorStyle(T node){

        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
        String mandatoryFieldMessage = "Required";

        if(node instanceof TextField) {
            ((TextField)node).setStyle(errorStyle);
            ((TextField)node).setPromptText(mandatoryFieldMessage);
        } if (node instanceof TextArea){
            ((TextArea)node).setStyle(errorStyle);
            ((TextArea)node).setPromptText(mandatoryFieldMessage);
        }

    }

    protected static <T> void setDefaultStyle(T node){
        if(node instanceof TextField) {
            ((TextField)node).setStyle(null);
            ((TextField)node).setPromptText(null);
        } if (node instanceof TextArea){
            ((TextArea)node).setStyle(null);
            ((TextArea)node).setPromptText(null);
        }
    }

    /**
     * TODO document
     * @param node
     */
    protected void setNodeStyle(Node node, String styleToRemove, String styleToAdd){
        /*
        metodo che va a modificare lo style del bottone passato come paramentro, andando a rimuovergli
        lo styleToRemove e andando ad aggiungerli lo StyleToAdd.
         */
        node.getStyleClass().remove(styleToRemove);
        node.getStyleClass().add(styleToAdd);
    }

    /**
     * TODO document
     */
    protected void initImages(){
        // TODO Se questo viene invocato solamente alla creazione di una nuova istanza di GUIUtilities, ha senso metterlo in un metodo a parte invece che nel costruttore?
        try{
            addImages("close", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/closeWindows.png")));
            addImages("eye", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/view.png")));
            addImages("eyeCrossed", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/hide.png")));
            addImages("success", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/correct15px.png")));
            addImages("failure", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/failure15px.png")));
            addImages("fire", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/fire.png")));
            //addImages("gear", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/gear2.png"))); // non usata
            addImages("openSideBar", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/openIcon.png")));
            addImages("closeSideBar", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/returnIcon.png")));
            addImages("search", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/searchIcon.png")));
            addImages("removeSearch", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/remove.png")));
            addImages("goodConnection", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/connectionSuccess.png")));
            addImages("badConnection", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/connectionFail.png")));
            addImages("wrench", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/wrench.png")));

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * TODO document
     */
    protected Scene getScene(String sceneName){
       return appScenes.get(sceneName);
    }


    /**
     * TODO document
     */
    protected void addImages(String key, Image image){
        // prima d'inserire l'immagine verifico se già esiste
        if(!images.containsKey(key)){
            images.put(key, image);
        }
    }

    /**
     * TODO document
     */
    protected Image getImage(String key){
        // se la hash map contiene la key passata come parametro allora restituisci l'immagine associata alla chiave
        if(images.containsKey(key)) { return images.get(key);}
        else{ return null;}// altrimenti restituisci null
    }

}
