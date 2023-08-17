package emotionalsongs;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ConnectionVerify extends Thread{

    private static Thread _instance;
    private static long pingDelay = 180*1000; // 3min / 180s

    public ConnectionVerify(){
        _instance = this;
        this.setDaemon(true);
        start();
    }

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
                    }

                }

                sleep(pingDelay);

            } catch(InterruptedException interrupt){
                // Normal behaviour, this will interrupt the thread's sleep.
                awoken = true;
            }
        }
    }

    protected static void setPingDelay(long d){
        pingDelay = d;
    }

    protected int getClientsConnected(){
        this.interrupt();
        return AuthManagerImpl.getClientList().size();
    }

    protected static Thread getInstance(){
        return _instance;
    }

    protected long getCurrentDelay(){
        return pingDelay;
    }

}
