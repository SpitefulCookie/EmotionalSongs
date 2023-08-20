package emotionalsongs;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Represents a thread that verifies the connection status of the clients currently connected to the server.
 *
 * <p>The {@code ConnectionVerify} class implements a thread responsible for verifying the connection status of clients.
 * It periodically sends ping requests to connected clients and removes any disconnected or inactive clients.
 *
 * <p><strong>Implementation note:</strong> This class, and subsequent connection management approach, has been implemented
 * in order to contrast an undesired RMI client thread behaviour that would sometimes occur when the client's execution
 * had been abruptly interrupted (e.g. application crash, loss of network connection,
 */
public class ConnectionVerify extends Thread{

    private static Thread _instance;
    private static long pingDelay = 180*1000; // 3min / 180s

    /**
     * Constructs a new instance of {@code ConnectionVerify}.
     *
     * <p>This constructor initializes the thread and starts the connection verification process.
     * Due to the low priority nature of the task, the thread responsible for pinging the clients has been marked as a daemon thread.
     */
    public ConnectionVerify(){
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

        while(true){

            try{

                ArrayList<PingClient> disconnected = new ArrayList<>();

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
     * Retrieves the number of clients that are currently connected to the server.
     *
     * <p>This method interrupts the thread, allowing it to update the clients count and returns it to the calling method.
     *
     * @return The count of connected clients.
     */
    protected int getClientsConnected(){
        this.interrupt();
        return AuthManagerImpl.getClientList().size();
    }

    /**
     * Retrieves the instance of the {@code ConnectionVerify} thread.<br><br>
     *
     * @return The instance of the connection verification thread.
     */
    protected static Thread getInstance(){
        return _instance;
    }

    /**
     * Retrieves the current ping delay for connection verification.<br><br>
     *
     * @return The current ping delay in milliseconds.
     */
    protected long getCurrentDelay(){
        return pingDelay;
    }

}
