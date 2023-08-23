package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * This interface defines the remote methods for user authentication and management that can
 * be invoked on the remote object using Java RMI.
 *
 * <p>This interface includes methods to check the existence of a specific username or a fiscal code,
 * register new users, perform user login, retrieve the server's RSA public key, manage client
 * registration, and more.
 *
 * @author <a href="https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
 *
 */
public interface AuthManager extends Remote {

    /**
     * Disconnects the specified client from the server.
     * @param client The client to be disconnected from the server.
     * @return {@code true} if the client was successfully disconnected, {@code false} otherwise.
     * @throws RemoteException If a communication error occurs while invoking or executing the remote method.
     */
    boolean disconnect(PingableClient client) throws RemoteException;

    /**
     * Checks if the specified username is already present in the database.<br><br>
     * This method is called from within the {@code UserRegistrationController} class, and it's used to
     * determine whether the desired username is already in use by querying the database.<br>
     * As a fail-safe mechanism, in case the request could not be satisfied by the server
     * a {@code UsernameNotVerifiedException} will be thrown signaling that the username might be already in use.
     * @param username The desired username
     * @return {@code true} if the provided username is already in use, {@code false} if the username is available
     * @throws RemoteException If a communication error occurs while invoking or executing the remote method.
     * @throws UsernameNotVerifiedException If an error has occurred while processing the request.
     */
    boolean usernameExists(String username) throws RemoteException, UsernameNotVerifiedException;

    /**
     * Registers a new user with the provided user data.<br><br>
     *
     * <p>This method is used to register a new user on the platform. The user data is encrypted using an asymmetric
     * cryptography algorithm (RSA) and passed to the server as an array of bytes.
     * <p> This function is called after the user registration process has been completed and the provided data has
     * passed all the integrity checks implemented within the {@code UserRegistrationController} class.
     * @param user An array of bytes containing all the user data encoded with the RSA algorithm.
     * @throws RemoteException If a communication error occurs while invoking or executing the remote method.
     */
    void registrazione(byte[] user) throws RemoteException;

    /**
     * Attempts to log in a user with the provided username and password.<br><br>
     *
     * <p>This method is used to authenticate a user by comparing the provided username and password
     * against the credentials stored on the database. If the credentials match, the user is considered logged
     * in and authentication succeeds.
     *
     * @param username The username of the user attempting to log in.
     * @param pwd The byte array containing the password - encrypted with the RSA algorithm - of the user attempting to log in.
     * @return {@code true} if the user's login attempt is successful, {@code false} otherwise.
     * @throws RemoteException If a communication error occurs while invoking or executing the remote method.
     */
    boolean userLogin(String username, byte[] pwd) throws RemoteException;

    /**
     * Retrieves from the server the public key used to establish a secure communication with the clients. <br><br>
     * This method is invoked by the clients, allowing them to retrieve the server public key used to encrypt sensitive
     * data through the RSA algorithm before transmitting it to the server.
     * @return The public key of the server for secure communication.
     * @throws RemoteException If a communication error occurs while invoking or executing the remote method.
     */
    PublicKey getPublicKey() throws RemoteException;

    /**
     * Registers the provided client on the server.<br><br>
     *
     * <p>This method is used to register a client on the server, allowing the latter to keep track of the number of
     * active clients. The provided `PingClient` object implements methods that allow the server to ping the client
     * associated to the instance of the remote object.
     *
     * <p>Registration is synchronized to ensure thread-safe access when multiple clients are
     * being registered concurrently.
     *
     * @param client The client to be registered on the server.
     * @throws RemoteException If a communication error occurs while invoking or executing the remote method.
     */
    void registerClient(PingableClient client) throws RemoteException;

    /**
     * Checks if the specified fiscal code is already present in the database.<br><br>
     * This method is called from within the {@code UserRegistrationController} class, and it's used to
     * determine whether the provided fiscal code is already in use by querying the database.<br>
     * @param cf The provided fiscal code encoded using the RSA algorithm.
     * @return {@code true} if the provided fiscal code is already present in the database, {@code false} otherwise.
     * @throws RemoteException If a communication error occurs while invoking or executing the remote method.
     */
    boolean cfExists(byte[] cf) throws RemoteException;


    /**
     * Encrypts the provided dats using the RSA algorithm.
     * @param data A {@code String} containing the data to encrypt.
     * @param publicKey The public key used to encrypt the data.
     * @return A byte array containing the encrypted data.
     */
    static byte[] RSA_Encrypt(String data, PublicKey publicKey) throws RemoteException{
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return encryptCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Decrypts the provided data using the RSA algorithm.<br><br>
     *
     * <p>This method decrypts the provided data using the server's private key.
     *
     * @param data The RSA encrypted data to decrypt.
     * @return A string containing the decrypted data.
     */
    static String decryptRSA(byte[] data, PrivateKey privateKey) throws RemoteException{
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(decryptCipher.doFinal(data), StandardCharsets.UTF_8);
        }catch (Exception e){
            //
            return "";
        }
    }

    byte[] getUserData(String userId, PublicKey pk) throws RemoteException;

}
