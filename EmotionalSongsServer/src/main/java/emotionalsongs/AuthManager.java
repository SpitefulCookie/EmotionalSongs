package emotionalsongs;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

public interface AuthManager extends Remote {

    boolean usernameExists(String username) throws RemoteException;
    void registrazione(byte[] user) throws RemoteException;
    boolean userLogin(String username, byte[] pwd) throws RemoteException;
    PublicKey getPublicKey() throws RemoteException;
    void registerClient(PingClient client) throws RemoteException;
    boolean cfExists(String cf) throws RemoteException;

}
