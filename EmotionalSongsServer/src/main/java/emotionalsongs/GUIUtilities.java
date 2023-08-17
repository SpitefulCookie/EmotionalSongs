package emotionalsongs;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUIUtilities {

    private final HashMap<String, Image> images;
    private static GUIUtilities guiUtilities;

    GUIUtilities(){

        this.images = new HashMap<>();

        try{

            images.put("eye", new Image(new FileInputStream("EmotionalSongsServer/src/main/resources/Images/view.png")));
            images.put("eyeCrossed", new Image(new FileInputStream("EmotionalSongsServer/src/main/resources/Images/hide.png")));
            images.put("blueFire", new Image(new FileInputStream("EmotionalSongsServer/src/main/resources/Images/blueFire.png")));
            images.put("settings", new Image( new FileInputStream("EmotionalSongsServer/src/main/resources/Images/gear.png")));

        }catch (IOException e){
            System.err.println("IOException thrown while attempting to load FXML scene files. \nReason: " + e.getMessage());
        }

    }

    // pattern singleton
    public static GUIUtilities getInstance(){
        if(guiUtilities == null){guiUtilities = new GUIUtilities();}
        return guiUtilities;
    }

    public static ArrayList<Text> formatText(String input) {

        ArrayList<Text> formattedTextList = new ArrayList<>();

        boolean textContainedBold = false;

        // Define regex patterns to match bold and italic formatting
        Pattern boldPattern = Pattern.compile("\\*\\*(.*?)\\*\\*");
        Pattern italicPattern = Pattern.compile("__([^_]+)__");

        // Get the default font and create fonts for bold and italic styles
        Font defaultFont = Font.getDefault();
        Font boldFont = Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize());
        Font italicFont = Font.font(defaultFont.getFamily(), FontPosture.ITALIC, defaultFont.getSize());

        // Create matchers to find bold and italic patterns in the input text
        Matcher boldMatcher = boldPattern.matcher(input);
        Matcher italicMatcher = italicPattern.matcher(input);

        // Initialize the current index for tracking progress through the input text
        int currentIndex = 0;

        // Process bold patterns in the input text
        while (boldMatcher.find()) {
            // Add normal text before the bold pattern (if any)
            if (boldMatcher.start() > currentIndex) {
                formattedTextList.add(new Text(input.substring(currentIndex, boldMatcher.start())));
            }

            // Create a Text node for the bold pattern and set its font to bold
            Text boldText = new Text(boldMatcher.group(1));
            boldText.setFont(boldFont);
            formattedTextList.add(boldText);
            textContainedBold = true;

            // Update the current index
            currentIndex = boldMatcher.end();
        }

        // Process italic patterns in the input text
        while (italicMatcher.find()) {


            // Add normal text before the italic pattern (if any)
            if (italicMatcher.start() > currentIndex) {
                formattedTextList.add(new Text(input.substring(currentIndex, italicMatcher.start())));
            }

            // Create a Text node for the italic pattern and set its font to italic
            Text italicText = new Text(italicMatcher.group(1));
            italicText.setFont(italicFont);
            formattedTextList.add(italicText);

            // Update the current index
            currentIndex = italicMatcher.end();
        }

        // Add any remaining normal text at the end of the input
        if (currentIndex < input.length()) {
            formattedTextList.add(new Text(input.substring(currentIndex)));
        }

        // Return the list of formatted Text nodes
        return formattedTextList;
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
    protected static void forceNumericInput(final TextField tf) {

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
    protected static void forceNumericInput(final TextField tf, final int maxLen) {

        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("[^\\d]", ""));
            }else if (tf.getText().length() > maxLen) {
                String s = tf.getText().substring(0, maxLen);
                tf.setText(s);
            }
        });

    }

    /**
     * TODO document
     * @param tf
     * @param maxLength
     */
    protected static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }

    /**
     * TODO document
     * @param tf
     */
    protected static void forceTextInput(final TextField tf) {

        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("[\\d]", ""));
            }
        });

    }
}
