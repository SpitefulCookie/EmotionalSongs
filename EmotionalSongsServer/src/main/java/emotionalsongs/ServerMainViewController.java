package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Controller class for the main view of the server application.
 *
 * This class is responsible for managing the user interface and behavior of the main server view.
 * It initializes UI components, handles user interactions, logs messages, processes commands, and manages server shutdown.
 *
 *  @author <a href="https://github.com/SpitefulCookie"> Della Chiesa Mattia</a>
 */
public class ServerMainViewController implements Initializable {

    protected static final int HEIGHT = 500;
    protected static final int WIDTH = 800;

    private static TextField _commandField;
    private static Button _sendBtn;
    private static final Calendar calendar = Calendar.getInstance();

    @FXML private TextField commandField;
    @FXML private TextFlow textFlow;
    @FXML private ScrollPane scrollPane;
    @FXML private Button sendBtn;

    /**
     * Initializes the server main view.
     *
     * This method is automatically called when the JavaFX scene associated with this controller is loaded.
     * It sets up the initial state of UI components, such as the scroll pane and text field.
     * Additionally, it configures event handlers for user interactions, such as pressing the Enter key to send commands.
     * The method also initializes the authentication manager, creates an RMI registry for remote authentication,
     * and establishes necessary server components for handling client interactions.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known. (unused within the code)
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized. (unused within the code)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        _commandField = this.commandField;
        _sendBtn = this.sendBtn;

        // Configure scroll pane to fit width and height
        scrollPane.fitToWidthProperty().set(true);
        scrollPane.fitToHeightProperty().set(true);

        // Prevent focus traversal for the TextFlow and ScrollPane
        textFlow.setFocusTraversable(false);
        scrollPane.setFocusTraversable(false);

        // Initialize the vertical scrollbar position to the bottom
        scrollPane.setVvalue(1);
        
        commandField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                // Set event handler for pressing the Enter key in the command field
                // (thus allowing the user to send a command just by pressing enter)
                handleSendButton();
            }else if (ke.getCode().equals(KeyCode.UP)) {
                // Set event handler for pressing the Up arrow key in the command field 
                // (thus allowing the user to recall the last command used)
                commandField.setText(lastCommandSent);
                commandField.end();
            }
        });


        try {
            // Initialize the authentication and repository managers before binding them to the RMI registry
            EmotionalSongsServer.initializeAuthManager(new AuthManagerImpl());
            Registry reg = LocateRegistry.createRegistry(EmotionalSongsServer.getServerPort());
            reg.rebind("AuthManager", EmotionalSongsServer.getAuthManagerInstance());
            reg.rebind("RepoManager", EmotionalSongsServer.getRepositoryManagerInstance());
            logText("Server is running, use command **showIp** to view the current LAN ip address for the server", true);

        } catch (RemoteException e) {
            logError("A RemoteException has occurred while initializing the server.\n" + e.getMessage());
        }

        EmotionalSongsServer.setMainViewController(this);

        // Starts the connection verify service, a daemon thread meant to ping the clients at regular intervals and
        // removing any inactive ones.

    }

    /**
     * Logs the provided text to the text flow in the server main view.
     *
     * This method logs the specified text in the server's text flow. It allows adding a timestamp
     * to the logged message and takes care of proper text alignment when multiple lines are logged.
     *
     * @param text The text to be logged.
     * @param timestamp A flag indicating whether to include a timestamp in the log message.
     */
    protected void logText(final String text, boolean timestamp){

        // Updates the UI elements from a non-JavaFX thread
        Platform.runLater(() -> {
            String buffer;
            Text textObj;

            ArrayList<Text> msg = new ArrayList<>();

            if(timestamp){ // If a timestamp is required
                // Update the current time
                calendar.setTime(new Date());
                // Create a new text object containing the timestamp and change its font
                textObj = new Text("[" + String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)) + "] ");
                textObj.setFont(Font.font("SANS_SERIF"));
                // Adds the newly generated object to the message that will be displayed
                msg.add(textObj);
            }

            // If the message contains a timestamp and also a new line character (\n)
            if(text.contains("\n") && timestamp){
                // in order to align the text with the timestamp a padding will be required
                buffer = text.replace("\n", "\n\t    ");
            } else{ // otherwise, there's nothing to be done
                buffer = text;
            }

            // If the message contains characters that mark a section in bold or in italic
            if (buffer.contains("**")||buffer.contains("__") ){
                // The message will be parsed and formatted accordingly by the function formatText()
                // As a limitation, the function is unable to format text that contains nested formatting
                // (e.g. a portion of bold text within an italic paragraph). This will require a more complex function
                // or recursive method callings.
                Collection<? extends Text> t =  GUIUtilities.formatText(buffer);
                msg.addAll(t);
            } else{ // Otherwise, the message will be converted to text object, and appended to the msg list.
                textObj = new Text(buffer);
                textObj.setFont(Font.font("SANS_SERIF"));
                msg.add(textObj);
            }

            TextFlow tf = this.textFlow;
            msg.add(new Text("\n"));

            // Adds the processed message to the TextFlow object...
            tf.getChildren().addAll(msg);

            // ... and updates the scrollbar position. As a bug/limitation the scrollbar position doesn't appear to
            // properly update after long messages such as the help command or after a thrown exception.
            // Sadly, I haven't been able to track down the source of the bug and as such the issue remains unsolved.
            updateScrollbarPosition();

        });

    }

    /**
     * Logs an error message to the text flow in the server main view.
     *
     * This method logs the provided error message in red color to the server's text flow. It also
     * includes a timestamp for the logged error message.
     *
     * @param text The error message to be logged.
     */
    public void logError(String text){

        // If the text contains newline escape codes, add padding to align the logged message with the timestamp.
        if(text.contains("\n")){
            text = text.replace("\n", "\n\t    ");
        }
        // Update the timestamp
        calendar.setTime(new Date());

        // Create a new Text object containing the timestamp and error message, with custom font and color
        Text timestampText = new Text("[" + String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)) + "] ");
        timestampText.setFont(Font.font("SANS_SERIF"));
        timestampText.setFill(Color.rgb(201, 28, 28));

        Text errorMessageText = new Text(text + "\n");
        errorMessageText.setFill(Color.rgb(201, 28, 28));

        // Update the UI elements from the JavaFX Application
        Platform.runLater(() -> {
            // Adds the processed message to the TextFlow object...
            textFlow.getChildren().addAll(timestampText, errorMessageText);

            // ... and updates the scrollbar position. As a bug/limitation the scrollbar position doesn't appear to
            // properly update after long messages such as the help command or after a thrown exception.
            // Sadly, I haven't been able to track down the source of the bug and as such the issue remains unsolved.
            updateScrollbarPosition();
        });

    }

    /**
     * Updates the vertical scrollbar position of the text flow displayed on the main screen.
     *
     * This method sets the vertical scrollbar position of the text flow displayed on the main screen to its maximum value,
     * effectively scrolling the view to display the most recent log entries.
     * This function is called after adding new log entries to ensure that the user always sees the latest log messages.
     */
    private void updateScrollbarPosition(){
        scrollPane.setVvalue(scrollPane.getVmax());
    }

    private String lastCommandSent = "";
    /**
     * Handles the action when the "Send" button is pressed.
     *
     * This method is triggered when the user presses the "Send" button to submit a command. It performs the following steps:
     *
     * <ol>
     * <li> Checks if the command input field is blank, and if so, returns without further action.
     * <li> Creates a new text node representing the sent command with a specific formatting (bold and blue color).
     * <li> Adds the text node to the text flow to display the command as an echo.
     * <li> Parses the entered command using the {@link #parseCommand(String)} method.
     * <li> Clears the command input field.
     * <li> Updates the vertical scrollbar position to ensure the new content is visible.
     * </ol>
     */
    @FXML private void handleSendButton(){

        if(commandField.getText().isBlank()) return;

        ArrayList<Text> a = GUIUtilities.formatText("[**"+EmotionalSongsServer.loggedUser+"**] > __"+commandField.getText()+"__\n");

        for(Text t : a){
            t.setFill(Color.rgb(10,91,220));
        }

        textFlow.getChildren().addAll(a); // ECHO del comando
        parseCommand(commandField.getText());
        lastCommandSent = commandField.getText();
        commandField.setText("");
        scrollPane.setVvalue(scrollPane.getVmax());

    }

    /**
     *
     * This method is triggered when the user selects the "Close" menu item or when the command shutdown is inputted in the console. <br>
     * The method calls the {@link #shutdownServer(boolean)} method to initiate the shutdown process for the server.
     */
    @FXML private void handleCloseMenuItemAction(){
        shutdownServer(true);
    }

    /**
     * Parses and processes various commands received by the server application.
     * This method interprets the input command string and performs corresponding actions.
     * Supported commands include '{@code shutdown}', '{@code showIP}', '{@code quit}', and '{@code help}'.
     *
     * @param cmd The command string to be parsed and executed.
     * @see ServerMainViewController#shutdownServer(boolean)
     * @see EmotionalSongsServer#getLanIpAddress()
     */
    private static void parseCommand(String cmd) {
        cmd = cmd.trim().toLowerCase(Locale.ROOT);

        if (cmd.isEmpty()) {return;}

        if (cmd.equals("shutdown")) {
            shutdownServer(false);
        }else if (cmd.equals("showip")){
            EmotionalSongsServer.mainView.logText("Current LAN ip address is: **" + EmotionalSongsServer.getLanIpAddress() + "**: "+EmotionalSongsServer.getServerPort(), true);
        }else if (cmd.equals("quit")) {
            shutdownServer(true);
        } else if (cmd.equals("help")) {
            EmotionalSongsServer.mainView.logText("""
                    List of available commands:
                    - **shutdown**
                    - **showip**
                    - **quit**
                    - **help**""", true);
            EmotionalSongsServer.mainView.logText("Type '**help**' followed by a command to display a brief description.", false);
        } else if (cmd.startsWith("help ")) {
            String[] parts = cmd.split("\\s+", 2);
            if (parts.length == 2) {
                String subCommand = parts[1];
                switch (subCommand) {
                    case "shutdown" -> EmotionalSongsServer.mainView.logText("**shutdown**: Shuts down the server.", true);
                    case "showip" -> EmotionalSongsServer.mainView.logText("**showip**: Shows the current LAN ip of the server.", true);
                    case "quit" -> EmotionalSongsServer.mainView.logText("**quit**: Quits the application.", true);
                    default -> EmotionalSongsServer.mainView.logError("Command \"" + cmd + "\" not recognized.");
                }
            }
        } else {
            EmotionalSongsServer.mainView.logError("Command \"" + cmd + "\" not recognized.");
        }

    }

    /**
     * Initiates the shutdown process for the server.<br><br>
     *
     * This method is responsible for shutting down the server components and optionally exiting the application.
     * It performs the following steps:<br>
     * <ol>
     * <li> Unexports the AuthManager instance using {@link UnicastRemoteObject#unexportObject(java.rmi.Remote, boolean)}.
     *    This detaches the server object from the RMI runtime, making it unavailable for remote calls.
     *    If any issues occur during the unexport process, an error message is logged.
     *
     * <li> If the 'exit' parameter is set to true, it calls {@link Platform#exit()} to gracefully close JavaFX.
     *    Then, it terminates the application using {@link System#exit(int)} with a status code of 0.
     *    If the 'exit' parameter is set to false, the window displaying the console will remain open and a message
     *    indicating that the server has been shut down will be displayed on it.
     *</ol>
     * @param exit Specifies whether the application should immediately exit ({@code true}) or not ({@code false}) after server shutdown.
     */
    protected static void shutdownServer(boolean exit){

        EmotionalSongsServer.unexportResources(exit);

        if(exit){
            Platform.exit();
            System.exit(0);
        } else{
            if(EmotionalSongsServer.mainView!=null) {
                _commandField.setDisable(true);
                _sendBtn.setDisable(true);
                EmotionalSongsServer.mainView.logText("The server has been correctly shut down. You may now close the application.", true);
            }
        }
    }
}
