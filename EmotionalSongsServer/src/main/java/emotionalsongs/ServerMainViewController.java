package emotionalsongs;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * TODO Document
 */
public class ServerMainViewController implements Initializable {

    protected static final int HEIGHT = 500;
    protected static final int WIDTH = 800;
    private static final int PORT = 6789;

    @FXML private TextField commandField; // Add autocomplete function? https://stackoverflow.com/questions/36861056/javafx-textfield-auto-suggestions
    @FXML private TextFlow textFlow;
    @FXML private ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
            EmotionalSongsServer.auth = new AuthManagerImpl();
            Registry reg = LocateRegistry.createRegistry(PORT);
            reg.rebind("AuthManager", EmotionalSongsServer.auth);
            logText("Server running");

            // TODO terminare il maniera "elegante" il server mediante comando quit/shutdown

        } catch (RemoteException e) {
            e.printStackTrace();
            logError("Server creation failed");
        }

        EmotionalSongsServer.initializeConnectionVerify();
        EmotionalSongsServer.setMainViewController(this);
    }

    /**
     * TODO Document
     * @param text
     */
    protected void logText(String text){
        TextFlow tf = this.textFlow;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
               tf.getChildren().add(new Text(text+"\n"));
                scrollPane.setVvalue(1);
            }

        });

    }

    /**
     * TODO Document
     * @param text
     */
    public void logError(String text){

        TextFlow tf = this.textFlow;

        Text t = new Text(text+"\n");
        t.setStyle("color: red");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tf.getChildren().add(t);
                scrollPane.setVvalue(1);
            }
        });

    }

    /**
     * TODO Document
     */
    @FXML private void handleSendButton(){
        parseCommand(commandField.getText());

        Text t = new Text("> " + commandField.getText()+"\n");
        t.setFont(Font.font(null, FontWeight.BOLD, 12));
        t.setFill(Color.BLUE);
        textFlow.getChildren().add(t); // ECHO del comando

        commandField.setText("");
        scrollPane.setVvalue(1);
        // TODO add parse command

    }

    /**
     * TODO Document
     * @param cmd
     */
    private static void parseCommand(String cmd){
        // TODO Implement
        StringTokenizer tokens = new StringTokenizer(cmd);
        if(tokens.nextToken().equalsIgnoreCase("shutdown")) {
            if(tokens.hasMoreTokens() && tokens.nextToken().equals("true")) {
                shutdownServer(true);
            } else{
                shutdownServer(false);
            }
        } else{
            if(tokens.nextToken().equalsIgnoreCase("help")){

                // TODO print the application's commands

            }
        }
    }

    protected static void shutdownServer(boolean exit){
        try {
            UnicastRemoteObject.unexportObject(EmotionalSongsServer.auth, true);
            if(exit){
                Platform.exit();
                System.exit(0);
            }
        } catch (NoSuchObjectException e) {
            //
        }

    }

}
