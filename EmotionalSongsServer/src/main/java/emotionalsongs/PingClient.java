package emotionalsongs;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PingClient extends Remote {
    public void pingClient() throws RemoteException;
}
