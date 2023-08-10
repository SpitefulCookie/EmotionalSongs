package emotionalsongs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class GUIUtilities {

    private final HashMap<String, Image> images;
    private static GUIUtilities guiUtilities;

    GUIUtilities(){

        this.images = new HashMap<>();

        try{

            images.put("eye", new Image(new FileInputStream("EmotionalSongsServer/src/main/resources/Images/view.png")));
            images.put("eyeCrossed", new Image(new FileInputStream("EmotionalSongsServer/src/main/resources/Images/hide.png")));
            images.put("blueFire", new Image(new FileInputStream("EmotionalSongsServer/src/main/resources/Images/blueFire.png")));
            images.put("gear", new Image( new FileInputStream("EmotionalSongsServer/src/main/resources/Images/gear2.png")));

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    // pattern singleton
    public static GUIUtilities getInstance(){
        if(guiUtilities == null){guiUtilities = new GUIUtilities();}
        return guiUtilities;
    }

    protected Image getImage(String key){

        // se la hash map contiene la key passata come parametro allora restituisci l'immagine associata alla chiave
        if(images.containsKey(key)) { return images.get(key);}
        else{ return null;}// altrimenti restituisci null
    }


    /**
     * TODO document
     * @param tf
     * @param maxLen
     */
    protected static void forceTextInput(final TextField tf, final int maxLen) {

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
    protected static void forceNumericInput(final TextField tf) {

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
    protected static void forceNumericInput(final TextField tf, final int maxLen) {

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
    protected static void addTextLimiter(final TextField tf, final int maxLength) {
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
    protected static void forceTextInput(final TextField tf) {

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
}
