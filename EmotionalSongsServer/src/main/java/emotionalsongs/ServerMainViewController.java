package emotionalsongs;

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
import java.rmi.NoSuchObjectException;
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
     * The method also initializes the authentication manager, creates a RMI registry for remote authentication,
     * and establishes necessary server components for handling client interactions.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known. (unused within the code)
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized. (unused within the code)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        _commandField = this.commandField;
        _sendBtn = this.sendBtn;
        scrollPane.fitToWidthProperty().set(true);
        scrollPane.fitToHeightProperty().set(true);

        textFlow.setFocusTraversable(false);
        scrollPane.setFocusTraversable(false);
        scrollPane.setVvalue(1);

        commandField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                handleSendButton();
            }
        });

        try {

            EmotionalSongsServer.initializeAuthManager(new AuthManagerImpl());
            Registry reg = LocateRegistry.createRegistry(EmotionalSongsServer.getServerPort());
            reg.rebind("AuthManager", EmotionalSongsServer.getAuthManagerInstance());
            reg.rebind("RepoManager", EmotionalSongsServer.getRepositoryManagerInstance());
            logText("Server running", true);

        } catch (RemoteException e) {
            logError("A remote exception has occurred while initializing the server.\n" + e.getMessage());
        }

        EmotionalSongsServer.setMainViewController(this);

        EmotionalSongsServer.initializeConnectionVerify();
        EmotionalSongsServer.getRepositoryManagerInstance();

        // ------------------TESTING--------------------
        // TODO remove this block before turning in the project
        // EmotionalSongsServer.getRepositoryManagerInstance().test();

        // ---------------------------------------------
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

        TextFlow tf = this.textFlow;

        Platform.runLater(() -> {
            // Il seguente blocco di codice si occupa di allineare il testo sotto al timestamp con l'inizio del messaggio loggato
            String msg;
            if(text.contains("\n") && timestamp){
                msg = text.replace("\n", "\n\t    ");
            } else{
                msg = text;
            }

            Text textObj = new Text();
            textObj.setFont(Font.font("SANS_SERIF"));

            if(timestamp){
                calendar.setTime(new Date());
                textObj.setText("["+ String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) )+ "] "+ msg+"\n");
            } else{
                textObj.setText("\t    "+msg+"\n");
            }

            tf.getChildren().add(textObj);

        });

        updateScrollbarPosition();

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

        TextFlow tf = this.textFlow;

        // Il seguente blocco di codice si occupa di allineare il testo sotto al timestamp con l'inizio del messaggio loggato
        if(text.contains("\n")){
            text = text.replace("\n", "\n\t    ");
        }

        calendar.setTime(new Date());
        Text t = new Text("["+ String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) )+ "] "+ text+"\n");
        t.setFont(Font.font("SANS_SERIF"));
        t.setFill(Color.rgb(201,28,28));

        Platform.runLater(() -> {

            tf.getChildren().add(t);

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

        Text t = new Text("> " + commandField.getText()+"\n");
        t.setFont(Font.font(null, FontWeight.BOLD, 12));
        t.setFill(Color.BLUE);
        textFlow.getChildren().add(t); // ECHO del comando
        parseCommand(commandField.getText());

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
     * Parses and processes a command entered by the user.
     *
     * This method receives a command string and interprets its content to perform specific actions or provide information.
     * It tokenizes the command to extract the primary command keyword and then executes corresponding actions.
     * If the primary keyword is not recognized, an error message is logged.
     *
     * @param cmd The command string entered by the user.
     */
    private static void parseCommand(String cmd){ // TODO add command to change connection verify delay and to disable new connections without shutting down the server
        String token;
        System.out.println("cmd: " + cmd);
        if(!cmd.equals("")) {
            StringTokenizer tokens = new StringTokenizer(cmd);
            token = tokens.nextToken();
            switch(token) {
                case "shutdown":
                    shutdownServer(false);
                    break;
                case "quit":
                    shutdownServer(true);
                    break;
                case "help":
                    if (!tokens.hasMoreTokens()) {
                        EmotionalSongsServer.mainView.logText("List of available commands:", true);
                        EmotionalSongsServer.mainView.logText("- shutdown", false);
                        EmotionalSongsServer.mainView.logText("- quit", false);
                        EmotionalSongsServer.mainView.logText("- help", false);
                        EmotionalSongsServer.mainView.logText("Type 'help' followed by a command to display a brief description.", false);
                    }else{
                        token = tokens.nextToken();

                        switch (token) {
                            case "shutdown" -> EmotionalSongsServer.mainView.logText("Shuts down the server.", true);
                            case "quit" -> EmotionalSongsServer.mainView.logText("Quits the application.", true);
                            default -> EmotionalSongsServer.mainView.logError("Command \"" + cmd + "\" not recognized.");
                        }
                    }
                    break;
                default:
                    EmotionalSongsServer.mainView.logError("Command \""+cmd+"\" not recognized.");
                break;
            }
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
        try {
            UnicastRemoteObject.unexportObject(EmotionalSongsServer.getAuthManagerInstance(), true);
        } catch (NoSuchObjectException e) {
            if(EmotionalSongsServer.mainView!=null)
                EmotionalSongsServer.mainView.logError("A NoSuchObjectException has occurred while attempting to shut down the server.\n" + e.getMessage());
        }

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
