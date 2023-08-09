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
import java.util.*;

/**
 * TODO Document
 */
public class ServerMainViewController implements Initializable {

    protected static final int HEIGHT = 500;
    protected static final int WIDTH = 800;


    private static Calendar calendar = Calendar.getInstance();

    @FXML private TextField commandField;
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

            EmotionalSongsServer.initializeAuthManager(new AuthManagerImpl());
            Registry reg = LocateRegistry.createRegistry(EmotionalSongsServer.getServerPort());
            reg.rebind("AuthManager", EmotionalSongsServer.getAuthManagerInstance());
            logText("Server running", true);

        } catch (RemoteException e) {
            logError("A remote exception has occurred while initializing the server.\n" + e.getMessage());
        }

        EmotionalSongsServer.setMainViewController(this);

        EmotionalSongsServer.initializeConnectionVerify();
        EmotionalSongsServer.getRepositoryManagerInstance();

        // ------------------TESTING--------------------
        // TODO remove this block before turning in the project
            EmotionalSongsServer.getRepositoryManagerInstance().test();

        // ---------------------------------------------
    }

    /**
     * TODO Document
     * @param text
     */
    protected void logText(final String text, boolean timestamp){

        TextFlow tf = this.textFlow;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Il seguente blocco di codice si occupa di allineare il testo sotto al timestamp con l'inizio del messaggio loggato
                String msg;
                if(text.contains("\n") && timestamp){
                    msg = text.replace("\n", "\n\t    ");
                } else{
                    msg = text;
                }

                if(timestamp){
                    calendar.setTime(new Date());
                    tf.getChildren().add(new Text("["+ String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) )+ "] "+ msg+"\n"));
                } else{
                    tf.getChildren().add(new Text("\t    "+msg+"\n"));
                }


            }

        });

        updateScrollbarPosition();

    }

    /**
     * TODO Document
     * @param text
     */
    public void logError(String text){

        TextFlow tf = this.textFlow;

        // Il seguente blocco di codice si occupa di allineare il testo sotto al timestamp con l'inizio del messaggio loggato
        if(text.contains("\n")){
            text = text.replace("\n", "\n\t    ");
        }

        calendar.setTime(new Date());
        Text t = new Text("["+ String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) )+ "] "+ text+"\n");
        t.setFill(Color.RED);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                tf.getChildren().add(t);
                //scrollPane.setVvalue(scrollPane.getVmax());

                updateScrollbarPosition();
            }
        });

    }

    private void updateScrollbarPosition(){
        scrollPane.setVvalue(scrollPane.getVmax());
    }

    /**
     * TODO Document
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

    @FXML private void handleCloseMenuItemAction(){
        shutdownServer(true);
    }

    /**
     * TODO Document
     * @param cmd
     */
    private static void parseCommand(String cmd){ // TODO add command to change connection verify delay
        String token = "";
        System.out.println("cmd: " + cmd);
        if(cmd!="") {
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

                        switch(token) {
                            case "shutdown":
                                EmotionalSongsServer.mainView.logText("Shuts down the server.", true);
                                break;
                            case "quit":
                                EmotionalSongsServer.mainView.logText("Quits the application.", true);
                                break;
                            default:
                                EmotionalSongsServer.mainView.logError("Command \""+ cmd +"\" not recognized.");
                                break;
                        }
                    }
                    break;
                default:
                    EmotionalSongsServer.mainView.logError("Command \""+cmd+"\" not recognized.");
                break;
            }
        }
    }

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
            if(EmotionalSongsServer.mainView!=null)
                EmotionalSongsServer.mainView.logText("The server has been correctly shut down. You may now close the application.", true);
        }

    }

}
