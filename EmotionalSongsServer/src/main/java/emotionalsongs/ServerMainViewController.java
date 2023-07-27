package emotionalsongs;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.net.URL;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;

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

        commandField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    handleSendButton();
                }
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
    }

    /**
     * TODO Document
     * FIXME The function did not work - haven't looked into what seemed to be the issue.
     * @param text
     */
    public void logText(String text){
        this.textFlow.getChildren().add(new Text(text+"\n"));
    }

    // FIXME

    /**
     * TODO Document
     * @param text
     */
    public void logError(String text){
        Text t = new Text(text+"\n");
        t.setStyle("color: red");
        this.textFlow.getChildren().add(t);
    }

    /* function added for testing purposes
    // TODO remove before sending the project
    private void spam(int i){
        System.out.println("Spamming");
        for (var g = 0 ; g<i; g++){
            this.textFlow.getChildren().add(new Text(g+1+"\n"));
        }
    }
    */

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
        if(cmd.equalsIgnoreCase("shutdown")) {
            try {
                UnicastRemoteObject.unexportObject(EmotionalSongsServer.auth, true);
            } catch (NoSuchObjectException e) {
                e.printStackTrace();
            }
        }
    }
}
