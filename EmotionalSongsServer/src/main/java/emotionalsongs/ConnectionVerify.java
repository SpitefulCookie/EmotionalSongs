package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Thread that verifies the connection status of the clients currently connected to the server.<br><br>
 *
 * <p>The {@code ConnectionVerify} class implements a thread responsible for verifying the connection status of clients.
 * It periodically sends ping requests to connected clients and removes any disconnected or inactive clients.<br><br>
 *
 * <p><strong>Implementation note:</strong> <i>This class, and subsequent connection management approach, had been initially
 * implemented to allow the "soft termination" of the server. This meant that, if the server administrator chose to,
 * they could shut down the server by simply refusing any further incoming request and allow all the remaining active clients
 * continue interacting with the back-end infrastructure until the client application would disconnect.
 * However, due to some problems regarding the server's code infrastructure requiring some refactoring, this feature ended
 * up being pushed down the schedule until I opted to exclude it from the final product and leave this class exclusively as
 * a tool meant to provide information about the server's status.</i>
 *
 *  @author <a href="https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
 */
public class ConnectionVerify extends Thread{

    private static Thread _instance;
    private static long pingDelay = 180*1000; // 3min / 180s

    protected static boolean keepAlive = true;

    /**
     * Constructs a new instance of {@code ConnectionVerify}.
     *
     * <p>This constructor initializes the thread and starts the connection verification process.
     * Due to the low priority nature of the task, the thread responsible for pinging the clients has been marked as a daemon thread.
     */
    public ConnectionVerify(){
        keepAlive = true;
        _instance = this;
        this.setDaemon(true);
        start();
    }

    /**
     * Runs the connection verification process.
     *
     * <p>This method periodically pings connected clients and removes any disconnected or inactive clients.
     */
    @Override
    public void run(){

        EmotionalSongsServer.mainView.logText("Connection verification service initialized with a "+pingDelay/1000+"sec ping delay.", true);
        String msg;
        boolean awoken = false;

        while(keepAlive){

            try{

                ArrayList<PingableClient> disconnected = new ArrayList<>();

                // La classe AuthManagerImpl contiene la lista dei client connessi al server
                if(!AuthManagerImpl.getClientList().isEmpty()){

                    int clientsConnected = AuthManagerImpl.getClientList().size();

                    for(var i : AuthManagerImpl.getClientList()){
                        try{
                            i.pingClient();
                        } catch (RemoteException e){
                            disconnected.add(i);
                        }
                    }

                    msg = "[**Connection Verifier**] Detected "+ (clientsConnected-disconnected.size()) + " active clients out of " + clientsConnected + ".";

                    if(!disconnected.isEmpty()) {

                        msg += " Removing inactive clients.";
                        AuthManagerImpl.removeClients(disconnected);
                    }

                    EmotionalSongsServer.mainView.logText(msg, true);

                } else{

                    if(awoken) {
                        msg = "[**Connection Verifier**] No clients connected to the server.";
                        EmotionalSongsServer.mainView.logText(msg, true);
                        awoken = false;
                    }

                }

                sleep(pingDelay);

            } catch(InterruptedException interrupt){
                // Normal behaviour, this will interrupt the thread's sleep.
                awoken = true;
            }
        }
    }

    /**
     * Sets the ping delay for connection verification.<br><br>
     *
     * <p>This method allows adjusting the delay between ping checks.
     *
     * @param d The new ping delay in milliseconds.
     */
    protected static void setPingDelay(long d){
        pingDelay = d;
    }

    /**
     * Retrieves the instance of the {@code ConnectionVerify} thread.<br><br>
     *
     * @return The instance of the connection verification thread.
     */
    protected static Thread getInstance(){
        return _instance;
    }

}
