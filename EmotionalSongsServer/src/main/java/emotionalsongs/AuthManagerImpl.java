package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Implementation of the {@link AuthManager} interface for user authentication and management.
 *
 * <p>The {@code AuthManagerImpl} class provides the concrete implementation of the {@link AuthManager}
 * interface, allowin remote method invocation for user authentication and management through Java RMI.<br>
 * It includes methods for user registration, login, client registration, and more.
 *
 *  @author <a href="https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
 */
public class AuthManagerImpl extends UnicastRemoteObject implements AuthManager{

    @Serial
    private static final long serialVersionUID = 1L;
    private final QueryHandler dbReference;

    private static KeyPair pair = null;


    /**
     * Constructs a new instance of {@link AuthManagerImpl}.
     *
     * <p>This constructor initializes a new {@code AuthManagerImpl} object. It generates RSA key pairs,
     * initializes the database reference, and prepares the client list.
     * The purpose of the latter is to allow the server to keep track of the currently active clients.
     *
     * @throws RemoteException If a communication error occurs during remote object creation.
     */
    protected AuthManagerImpl() throws RemoteException {
        super();
        generateKeys();
        //clientList = new HashSet<>();
        dbReference = EmotionalSongsServer.getQueryHandlerInstance();
    }


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
    public synchronized boolean usernameExists(String username) throws RemoteException, UsernameNotVerifiedException {
        return dbReference.usernameExists(username);
    }

    /**
     * Checks if the provided fiscal code exists in the database.<br>
     *
     * <p>This method queries the database to verify the existence of the specified fiscal code.
     *
     * @param cf The fiscal code to check encoded using the RSA algorithm.
     * @return {@code true} if the fiscal code exists, otherwise {@code false}.
     * @throws RemoteException If a communication error occurs during remote method invocation.
     */
    public synchronized boolean cfExists(byte[] cf) throws RemoteException {
        if(cf!=null){
            String decodedCF = AuthManager.decryptRSA(cf, pair.getPrivate());
            String queryResult = dbReference.executeQuery(new String[]{decodedCF}, QueryHandler.QUERY_CF_EXISTS).get(0)[0];
            return Integer.parseInt(queryResult) == 1;
        }
        return false;
    }


    /**
     * Registers a new user with the provided data.<br><br>
     *
     * <p>This method decrypts the provided user data and registers the user in the database.
     *
     * @param userData The RSA encrypted user data to be registered.
     * @throws RemoteException If a communication error occurs during remote method invocation.
     */
    @Override
    public synchronized void registrazione(byte[] userData) throws RemoteException {

        String[] data = AuthManager.decryptRSA(userData, pair.getPrivate()).split("&SEP&");

        dbReference.registerUser(data);

    }

    /**
     * Retrieves the RSA public key of the server.<br><br>
     *
     * <p>This method returns the public key that clients can use for encryption.
     *
     * @return The RSA public key of the server.
     * @throws RemoteException If a communication error occurs during remote method invocation.
     */
    public synchronized PublicKey getPublicKey() throws RemoteException{
        return pair.getPublic();
    }

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
    @Override
    public boolean userLogin(String username, byte[] pwd) throws RemoteException {

        // La password ricevuta da remoto sarà incapsulata in una codifica RSA, evitando così la trasmissione in chiaro di quest'ultima lungo la rete
        String decryptedPwd;

        decryptedPwd = AuthManager.decryptRSA(pwd, pair.getPrivate());

        /* Mentre la password salvata all'interno del db sarà codificata tramite algoritmo BCrypt in questo modo,
         * anche se il db venga attaccato e la tabella contenente i dati relativi agli utenti cada in mano dell'attaccante,
         * questi si ritroveranno con una tabella contenente gli hash delle password che necessiteranno di essere decriptati
         * per garantire l'accesso alla piattaforma.
         */
        String retrievedPwd = dbReference.queryUserPassword(username);

        // Nel caso di una SQLException o di un Result set vuoto queryUserPassword() restituirà null quindi
        // sarà necessario effettuare una verifica prima d'invocare il metodo di verifica della password.
        return retrievedPwd != null && BCryptVerifyPassword(decryptedPwd, retrievedPwd);

    }

    /**
     * Generates a BCrypt hash of the provided password.<br><br>
     *
     * <p>This method hashes the password using the BCrypt algorithm for secure storage.
     *
     * @param password The password to be hashed.
     * @return The BCrypt hash of the password.
     */
    protected static String BCryptHashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt()); // questa verrà salvata nel db come password utente
    }

    /**
     * Verifies a BCrypt hashed password against a stored hash.<br><br>
     *
     * <p>This method checks if the provided password matches the stored hash.
     *
     * @param password The password to verify.
     * @param dbPassword The stored BCrypt hashed password.
     * @return {@code true} if the password matches the hash, otherwise {@code false}.
     */
    private static boolean BCryptVerifyPassword(String password, String dbPassword){
        return BCrypt.checkpw(password, dbPassword);
    }

    /**
     * Generates RSA key pairs if not already generated.
     */
    private void generateKeys(){

        if (pair == null) {

            KeyPairGenerator generator;

            try {
                generator = KeyPairGenerator.getInstance("RSA");
                generator.initialize(2048);
                pair = generator.generateKeyPair();

            } catch (NoSuchAlgorithmException e) {
                EmotionalSongsServer.mainView.logError("NoSuchAlgorithmException thrown while attempting to get KeyPairGenerator instance");
            }
        }

    }


    /**
     * Retrieves user data from the remote database for a given user ID.
     *
     * @param userId The unique identifier of the user for whom data is requested.
     * @return A string representation of the user data retrieved from the database.
     * @throws RemoteException If a remote communication error occurs during data retrieval.
     */
    @Override
    public String getUserData(String userId) throws RemoteException{
        return
            Arrays.toString(
                dbReference.executeQuery(new String[]{userId}, QueryHandler.QUERY_GET_USER_DATA).get(0)
            );

    }

}
