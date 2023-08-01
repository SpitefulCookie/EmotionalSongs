package emotionalsongs;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

public interface AuthManager extends Remote {

    public boolean usernameExists(String username) throws RemoteException;
    public void registrazione(byte[] user) throws RemoteException; // temporarily set to String instead of Utente
    public boolean userLogin(String username, byte[] pwd) throws RemoteException;
    public PublicKey getPublicKey() throws RemoteException;
    public void registerClient(PingClient client) throws RemoteException;

}
