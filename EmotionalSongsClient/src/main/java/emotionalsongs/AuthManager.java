package emotionalsongs;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

/**
 * TODO Document??
 */
public interface AuthManager extends Remote {

    boolean usernameExists(String username) throws RemoteException;
    void registrazione(byte[] user) throws RemoteException;
    boolean userLogin(String username, byte[] pwd) throws RemoteException;
    PublicKey getPublicKey() throws RemoteException;
    void registerClient(PingClient client) throws RemoteException;
    boolean cfExists(String cf) throws RemoteException;

    static byte[] RSA_Encrypt(String data){
        try {
            PublicKey pk = EmotionalSongsClient.auth.getPublicKey();
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, pk);
            return encryptCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e){
            return null;
        }
    }
}
