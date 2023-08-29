package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Utility class for managing GUI-related operations in the EmotionalSongs application.
 * This class provides methods for formatting text, handling images,
 * and enforcing text input constraints in the JavaFX text fields.
 *
 * @author <a href="https://github.com/SpitefulCookie"> Della Chiesa Mattia</a>
 * @author <a href="https://github.com/samuk52"> Samuele Corallo</a>
 */
public class GUIUtilities {

    private final HashMap<String, Scene> appScenes;
    private final HashMap<String, Node> appNode;
    private final HashMap<String, Image> images;
    private static GUIUtilities guiUtilities;

    private static final String[] guiControllers = {"clientLoginSettings.fxml", "connectionFailed.fxml", "exit.fxml", "login.fxml", "search.fxml", "user.fxml", "UserRegistration.fxml", "guest.fxml", "insertionFailed.fxml"};

    /**
     * Constructs a new instance of the GUIUtilities class.
     * Initializes the maps for storing scenes, nodes, and images used in the application.
     * Loads the predefined images into the image map through the {@code initImages} method.
     */
    GUIUtilities(){
        // creo la hashmap
        images = new HashMap<>();
        appScenes = new HashMap<>();
        appNode = new HashMap<>();

        // setto (aggiungo) le varie immagini nella hashmap
        initImages();

    }

    /**
     * Retrieves the singleton instance of the GUIUtilities class.
     *
     * @return The singleton instance of GUIUtilities.
     */
    public static GUIUtilities getInstance(){
        if(guiUtilities == null){guiUtilities = new GUIUtilities();}
        return guiUtilities;
    }

    /**
     * Initializes scene objects from FXML files and stores them in the appScenes map.
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
     * Initializes node objects from FXML files and stores them in the appNode map.
     */
    protected void initNode(){

        try {

            for (String controller : guiControllers) {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller));
                appNode.put(controller, fxmlLoader.load());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Enforces text input constraints for a {@link TextField} by disallowing numeric characters and limiting the input's length.
     *
     * @param tf The {@link TextField} to enforce constraints on.
     * @param maxLen The maximum length of the text.
     */
    public static void forceTextInput(final TextInputControl tf, final int maxLen) {

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
     * Limits the length of input text in a {@link TextInputControl} to the specified maximum length.
     *
     * @param tf     The {@link TextInputControl} to limit input length for.
     * @param maxLen The maximum length of the text.
     */
    public static void limitTextInputLength(final TextInputControl tf, final int maxLen) {

        tf.textProperty().addListener((observable, oldValue, newValue) -> {

           if (tf.getText().length() > maxLen) {
                String s = tf.getText().substring(0, maxLen);
                tf.setText(s);
           }

        });
    }

    /**
     * Enforces text input constraints for a {@link TextField} by allowing exclusively numeric characters.
     *
     * @param tf The {@link TextField} to enforce constraints on.
     */
    public static void forceNumericInput(final TextField tf) {

        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /*
     * Ti ho rimosso questo metodo perché faceva la stessa cosa dell'altro metodo forceTextInput e, poiché TextArea e
     * TextInputArea possiedono la stessa super classe è più corretto utilizzare un metodo unico e generalizzato per entrambe.
     *
     */

    /* *
     *
     * @param ta
     * @param maxLen
     * /
    public static void forceTextInput(final TextArea ta, final int maxLen) {

        ta.textProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.matches(".*\\d.*")) {
                ta.setText(newValue.replaceAll("\\d", ""));
            }else if (ta.getText().length() > maxLen) {
                String s = ta.getText().substring(0, maxLen);
                ta.setText(s);
            }
        });
    }
    */

    /**
     * Enforces text input constraints for a {@link TextField} by allowing exclusively numeric characters and
     * limiting the input's maximum length.
     *
     * @param tf The {@link TextField} to enforce constraints on.
     * @param maxLen The maximum length of the text.
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

    /**
     * Sets an error style for a given {@link TextField} or {@link TextArea} node,
     * indicating that the field is mandatory and/or the provided input is invalid.
     *
     * @param node The {@link TextField} or {@link TextArea} node to apply the error style to.
     */
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

    /**
     * Sets the default style for a given {@link TextField} or {@link TextArea} node,
     * removing any error styling.
     *
     * @param node The {@link TextField} or {@link TextArea} node to restore the default style for.
     */
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
     * Changes the style of a given node by removing a specified style class and adding another.
     *
     * @param node         The node to modify the style for.
     * @param styleToRemove The style class to remove.
     * @param styleToAdd    The style class to add.
     */
    protected void setNodeStyle(Node node, String styleToRemove, String styleToAdd){
        /*
        metodo che va a modificare lo style del bottone passato come parametro, andando a rimuovergli
        lo styleToRemove e andando ad aggiungerli lo StyleToAdd.
         */
        node.getStyleClass().remove(styleToRemove);
        node.getStyleClass().add(styleToAdd);
    }

    /**
     * Initializes images used in the application and stores them in the images map.
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
            addImages("openSideBar", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/openIcon.png")));
            addImages("closeSideBar", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/returnIcon.png")));
            addImages("search", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/searchIcon.png")));
            addImages("removeSearch", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/remove.png")));
            addImages("goodConnection", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/connectionSuccess.png")));
            addImages("badConnection", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/connectionFail.png")));
            addImages("wrench", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/wrench.png")));
            addImages("viewEmotions", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/viewEmotions.png")));
            addImages("add", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/addIcon.png")));

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * Retrieves the {@link Scene} associated with the given scene name from the appScenes map.
     *
     * @param sceneName The name of the scene to retrieve.
     * @return The retrieved {@link Scene} object, or null if not found.
     */
    protected Scene getScene(String sceneName){
       return appScenes.get(sceneName);
    }

    /**
     * Retrieves the {@link Node} associated with the given node name from the appNode map.
     *
     * @param nodeName The name of the node to retrieve.
     * @return The retrieved {@link Node} object, or null if not found.
     */
    protected Node getNode(String nodeName) { return appNode.get(nodeName); }

    /**
     * Adds a node to the appNode map with the given node name as the key.
     *
     * @param nodeName   The name to associate with the node.
     * @param nodeToAdd The {@link Node} object to add.
     */
    protected void addNode(String nodeName, Node nodeToAdd){
        appNode.put(nodeName, nodeToAdd);
    }


    /**
     * Adds an image to the images map with the given key as the identifier.
     *
     * @param key   The key associated with the image.
     * @param image The {@link Image} object to add.
     */
    protected void addImages(String key, Image image){
        // prima d'inserire l'immagine verifico se già esiste
        if(!images.containsKey(key)){
            images.put(key, image);
        }
    }

    /**
     * Retrieves the image associated with the given key from the images map.
     *
     * @param key The key associated with the desired image.
     * @return The {@link Image} associated with the provided key, or null if not found.
     */
    protected Image getImage(String key){
        // se la hash map contiene la key passata come parametro allora restituisci l'immagine associata alla chiave
        if(images.containsKey(key)) { return images.get(key);}
        else{ return null;}// altrimenti restituisci null
    }

    /**
     * Closes the stage associated with a given {@link Node}.
     *
     * @param node The {@link Node} whose stage is to be closed.
     */
    protected static void closeStage(Node node){
        /*
        metodo che va a chiudere lo stage tramite il nodo passato come argomento
         */
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

}
