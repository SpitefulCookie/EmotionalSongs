package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
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
 * @author <a href="https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
 */
public class AuthManagerImpl extends UnicastRemoteObject implements AuthManager {

    private static final long serialVersionUID = 1L;
    private final QueryHandler dbReference;

    private static KeyPair pair = null;
    private static HashSet<PingableClient> clientList = new HashSet<>();

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
        clientList = new HashSet<>();
        dbReference = EmotionalSongsServer.getQueryHandlerInstance();
    }

    /**
     * Removes the provided clients from the list of currently active clients.<br><br>
     *
     * @param disconnected An ArrayList of PingClient instances representing the clients to be removed.
     */
    public static void removeClients(ArrayList<PingableClient> disconnected) {
        disconnected.forEach(clientList::remove);
    }

    /**
     * Disconnects the specified client from the server.
     *
     * @param client The client to be disconnected from the server.
     * @return {@code true} if the client was successfully disconnected, {@code false} otherwise.
     * @throws RemoteException If a communication error occurs while invoking or executing the remote method.
     */
    @Override
    public boolean disconnect(PingableClient client) throws RemoteException {
        return clientList.remove(client);
    }

    /**
     * Checks if the specified username is already present in the database.<br><br>
     * This method is called from within the {@code UserRegistrationController} class, and it's used to
     * determine whether the desired username is already in use by querying the database.<br>
     * As a fail-safe mechanism, in case the request could not be satisfied by the server
     * a {@code UsernameNotVerifiedException} will be thrown signaling that the username might be already in use.
     *
     * @param username The desired username
     * @return {@code true} if the provided username is already in use, {@code false} if the username is available
     * @throws RemoteException              If a communication error occurs while invoking or executing the remote method.
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
        if (cf != null) {
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
    public synchronized PublicKey getPublicKey() throws RemoteException {
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
     * @param pwd      The byte array containing the password - encrypted with the RSA algorithm - of the user attempting to log in.
     * @return {@code true} if the user's login attempt is successful, {@code false} otherwise.
     * @throws RemoteException If a communication error occurs while invoking or executing the remote method.
     */
    @Override
    public boolean userLogin(String username, byte[] pwd) throws RemoteException, NoSuchAlgorithmException, InvalidKeySpecException {

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
        return retrievedPwd != null && PBKDF2VerifyPassword(decryptedPwd, retrievedPwd);

    }

    protected static byte[] getSalt() {
        byte[] salt = new byte[16];
        return salt;
    }

    protected static String PBKDF2HashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    /**
     * Verifies a PBKDF2 hashed password against a stored hash.
     *
     * @param password       The password to verify.
     * @param storedPassword The stored PBKDF2 hashed password.
     * @return {@code true} if the password matches the hash, otherwise {@code false}.
     */
    private static boolean PBKDF2VerifyPassword(String password, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }


    /**
     * Generates RSA key pairs if not already generated.
     */
    private void generateKeys() {

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
     * Registers the provided client on the server.
     *
     * <p>This method is used to register a client on the server, allowing the latter to keep track of the number of
     * active clients. The provided `PingClient` object implements methods that allow the server to ping the client
     * associated to the instance of the remote object.
     *
     * <p>Registration is synchronized to ensure thread-safe access when multiple clients are
     * being registered concurrently.
     *
     * @param client The client to be registered on the server.
     * @throws RemoteException If a communication error occurs during the remote method invocation.
     */
    public synchronized void registerClient(PingableClient client) throws RemoteException {
        System.out.println("Adding client: " + client.getPublicKey() + "\nAdded? " + clientList.add(client));
    }

    /**
     * Retrieves the list of registered clients.<br><br>
     *
     * <p>This method returns the list of active clients that are currently registered on the server.
     * These clients will be periodically pinged by the server to determine their status.
     *
     * @return A {@link HashSet} representing the list of registered clients.
     */
    protected static HashSet<PingableClient> getClientList() {
        return clientList;
    }

    /**
     * Retrieves the provided user's data from the database.<br><br>
     * <p>
     * This method retrieves from the database the data associated with the provided userId. The retrieved data is then encrypted using an
     * RSA algorithm and the provided {@link PublicKey}.<br><br>
     *
     * <strong>Implementation note:</strong> This feature wasn't initially planned in the application and, as such, due
     * to some architectural limitations with the communication protocol between the client and the server, the public key necessary
     * to encrypt the data to be sent to the client is passed as one of the method's arguments. This is a conceptually incorrect approach
     * and would require a refactoring of both the {@link AuthManagerImpl} class and the implementation of the {@link PingableClient} interface
     * to allow an encrypted two-way communication between the client and the server. However, since this feature is already out of the project's
     * requirement's scope, we have decided to leave things as they are and acknowledge this as point for future rework.
     *
     * @param userId The userId of the desired user for which to retrieve the information from the database.
     * @param pk     The {@code PublicKey} used to encrypt the data to be sent to the client.
     * @return An array of bytes containing the desired user's data.
     * @throws RemoteException If a communication error occurs during the remote method invocation.
     */
    @Override
    public byte[] getUserData(String userId, PublicKey pk) throws RemoteException {
        return AuthManager.RSA_Encrypt(
                Arrays.toString(
                        dbReference.executeQuery(new String[]{userId}, QueryHandler.QUERY_GET_USER_DATA).get(0)
                ),
                pk);
    }

}
