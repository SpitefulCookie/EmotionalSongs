package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

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

/**
 * Utility class for managing GUI-related operations in the EmotionalSongs application.
 * This class provides methods for formatting text, handling images,
 * and enforcing text input constraints in the JavaFX text fields.
 *
 * @author <a href="https://github.com/SpitefulCookie"> Della Chiesa Mattia</a>
 */
public class GUIUtilities {

    private final HashMap<String, Image> images;
    private static GUIUtilities guiUtilities;

    /**
     * Constructs a new instance of the GUIUtilities class.
     * Initializes a map for storing images used in the application.
     * Loads predefined images into the map.
     */
    GUIUtilities(){

        this.images = new HashMap<>();

        images.put("eye", new Image(EmotionalSongsServer.class.getResourceAsStream("/Images/view.png")));
        images.put("eyeCrossed", new Image(EmotionalSongsServer.class.getResourceAsStream("/Images/hide.png")));
        images.put("blueFire", new Image(EmotionalSongsServer.class.getResourceAsStream("/Images/blueFire.png")));
        images.put("settings", new Image(EmotionalSongsServer.class.getResourceAsStream("/Images/gear.png")));

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
     * Formats a given input text by identifying and applying bold and italic styles through regex patterns.<br><br>
     * <strong>Limitation:</strong> The method is incapable of processing nested text formatting.
     * Using nested formatting may result in unpredictable behaviour.
     * @param input The input text to be formatted.
     * @return A list of formatted {@link Text} nodes representing the input text with applied styles.
     */
    public static ArrayList<Text> formatText(String input) {

        ArrayList<Text> formattedTextList = new ArrayList<>();

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

    /**
     * Retrieves the image associated with the given key from hash map containing the application's images.
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
     * Enforces text input constraints for a {@link TextField} by allowing exclusively numeric characters.
     *
     * @param tf The {@link TextField} to enforce constraints on.
     */
    protected static void forceNumericInput(final TextField tf) {

        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }

}
