package emotionalsongs;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ConnectionVerify extends Thread{

    private static long pingDelay = 180*1000; // 3min / 180s

    public ConnectionVerify(){
        this.setDaemon(true);
        start();
    }

    @Override
    public void run(){

        EmotionalSongsServer.mainView.logText("Connection verification service initialized with a "+pingDelay/1000+"sec ping delay.", true);

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

                    String msg = "[Connection Verifier] Detected "+ (clientsConnected-disconnected.size()) + " active clients out of " + clientsConnected + ".";

                    if(!disconnected.isEmpty()) {

                        msg += " Removing inactive clients.";
                        AuthManagerImpl.removeClients(disconnected);
                    }

                    EmotionalSongsServer.mainView.logText(msg, true);

                }

                sleep(pingDelay);

            } catch(InterruptedException interrupt){
                break;
            }
        }
    }

    protected synchronized void setPingDelay(long d){
        pingDelay = d;
    } // TODO È necessario usare synchronized se viene eseguita una singola istanza di questo thread?

    protected int getClientsConnected(){
        this.interrupt();
        return AuthManagerImpl.getClientList().size();
    }

    protected long getCurrentDelay(){
        return pingDelay;
    }

}
