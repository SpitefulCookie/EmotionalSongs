package emotionalsongs;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

/**
 * TODO Document??
 */
public interface AuthManager extends Remote {

    public boolean usernameExists(String username) throws RemoteException;
    public boolean registrazione(String user) throws RemoteException; // temporarily set to String instead of Utente
    public boolean userLogin(String username, byte[] pwd) throws RemoteException;
    public PublicKey getPublicKey() throws RemoteException;

}
