package emotionalsongs;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RepositoryManager extends Remote {
    Canzone[] ricercaCanzone(String titolo) throws RemoteException;
}
