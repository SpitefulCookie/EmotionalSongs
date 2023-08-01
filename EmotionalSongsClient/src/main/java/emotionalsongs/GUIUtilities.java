package emotionalsongs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class GUIUtilities {

    private HashMap<String, Image> images;
    private static GUIUtilities guiUtilities;

    GUIUtilities(){
        // creao la hashmap
        images = new HashMap<String, Image>();
        // setto (aggiungo) le varie immagini nella hashmap
        setImages();
    }

    // pattern singleton
    public static GUIUtilities getInstance(){
        if(guiUtilities == null){
            guiUtilities = new GUIUtilities();
            return guiUtilities;
        }else{
            return guiUtilities;
        }
    }

    /**
     * TODO document
     * @param tf
     * @param maxLen
     */
    public static void forceTextInput(final TextField tf, final int maxLen) {

        tf.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (newValue.matches(".*\\d.*")) {
                    tf.setText(newValue.replaceAll("\\d", ""));
                }else if (tf.getText().length() > maxLen) {
                    String s = tf.getText().substring(0, maxLen);
                    tf.setText(s);
                }
            }

        });
    }

        /**
         * TODO document
         * @param tf
         */
        public static void forceNumericInput(final TextField tf) {

            tf.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                                    String newValue) {
                    if (!newValue.matches("\\d*")) {
                        tf.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });

        }

        /**
         * TODO document
         * @param tf
         * @param maxLen
         */
        public static void forceNumericInput(final TextField tf, final int maxLen) {

            tf.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                                    String newValue) {
                    if (!newValue.matches("\\d*")) {
                        tf.setText(newValue.replaceAll("[^\\d]", ""));
                    }else if (tf.getText().length() > maxLen) {
                        String s = tf.getText().substring(0, maxLen);
                        tf.setText(s);
                    }
                }
            });

        }

    /**
     * TODO document
     * @param tf
     * @param maxLength
     */
    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

        /**
         * TODO document
         * @param tf
         */
        public static void forceTextInput(final TextField tf) {

            tf.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                                    String newValue) {
                    if (newValue.matches("\\d*")) {
                        tf.setText(newValue.replaceAll("[\\d]", ""));
                    }
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
     */
    protected void setImages(){
        // Se questo viene invocato solamente alla creazione di una nuova istanza di GUIUtilities, ha senso metterlo in un metodo a parte invece che nel costruttore?
        try{
            addImages("close", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/closeIcon.png")));
            addImages("eye", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/view.png")));
            addImages("eyeCrossed", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/hide.png")));
            addImages("success", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/correct15px.png")));
            addImages("failure", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/failure15px.png")));
            addImages("fire", new Image(new FileInputStream("EmotionalSongsClient/src/main/resources/emotionalsongs/Images/fire.png")));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * TODO document
     */
    protected void addImages(String key, Image image){
        // prima d'inserire l'immagine verifico se già esiste
        if(!images.containsKey(key)){
            images.put(key, image);
        }else{
            System.out.println("immagine già esistente");
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
