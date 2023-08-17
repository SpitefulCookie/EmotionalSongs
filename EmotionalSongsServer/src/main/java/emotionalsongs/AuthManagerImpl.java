package emotionalsongs;

import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.crypto.Cipher;
import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.security.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * TODO Document
 */
public class AuthManagerImpl extends UnicastRemoteObject implements AuthManager{

    @Serial
    private static final long serialVersionUID = 1L;
    private final QueryHandler dbReference;

    private static KeyPair pair = null;
    private static HashSet<PingClient> clientList = new HashSet<>();

    /**
     * TODO Document
     * @throws RemoteException
     */
    protected AuthManagerImpl() throws RemoteException {
        super();
        generateKeys();
        clientList = new HashSet<>();
        dbReference = EmotionalSongsServer.getQueryHandlerInstance();

        // TODO auto-generated stub
    }

    public static void removeClients(ArrayList<PingClient> disconnected) {
        disconnected.forEach(clientList::remove);
    }

    @Override
    public boolean disconnect(PingClient client) throws RemoteException {
        return clientList.remove(client);
    }

    /**
     * TODO Document and Implement
     * @param username
     * @return
     * @throws RemoteException
     */
    public synchronized boolean usernameExists(String username) throws RemoteException, UsernameNotVerifiedException {

        return dbReference.usernameExists(username);

    }

    /**
     * TODO Document and Implement
     * @param cf
     * @return
     * @throws RemoteException
     */
    public synchronized boolean cfExists(String cf) throws RemoteException {
        String queryResult = dbReference.executeQuery(new String[]{cf}, QueryHandler.QUERY_CF_EXISTS).get(0)[0];
        if (cf != null && Integer.parseInt(queryResult) == 1) {return true;}
        return false;
    }


    /**
     * TODO Document and Implement
     * @param userData
     * @return
     * @throws RemoteException
     */
    @Override
    public synchronized void registrazione(byte[] userData) throws RemoteException {

        String[] data = decryptRSA(userData).split("&SEP&");

        dbReference.registerUser(data);

    }

    public synchronized PublicKey getPublicKey() throws RemoteException{
        return pair.getPublic();
    }

    /**
     * TODO document
     * @param username
     * @param pwd
     * @return
     */
    @Override
    public boolean userLogin(String username, byte[] pwd) {

        // La password ricevuta da remoto sarà incapsulata in una codifica RSA, evitando così la trasmissione in chiaro di quest'ultima lungo la rete
        String decryptedPwd = decryptRSA(pwd);

        /* Mentre la password salvata all'interno del db sarà codificata tramite algoritmo BCrypt in questo modo,
         * anche se il db venga attaccato e la tabella contenente i dati relativi agli utenti cada in mano dell'attaccante,
         * questi si ritroveranno con una tabella contenente gli hash delle password che necessiteranno di essere decriptati
         * per garantire l'accesso alla piattaforma.
         */
        String retrievedPwd = dbReference.queryUserPassword(username);

        // Nel caso di una SQLException o di un Result set vuoto queryUserPassword() restituirà null quindi
        // sarà necessario effettuare una verifica prima d'invocare il metodo di verifica della password.
        if(retrievedPwd!=null && BCryptVerifyPassword(decryptedPwd, retrievedPwd)){return true;}
        else{return false;}

    }

    /**
     * TODO Document
     * @param password
     * @return
     */
    protected static String BCryptHashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt()); // questa verrà salvata nel db come password utente
    }

    /**
     * TODO Document
     * @param password
     * @param dbPassword
     * @return
     */
    private boolean BCryptVerifyPassword(String password, String dbPassword){
        return BCrypt.checkpw(password, dbPassword);
    }

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

    private String decryptRSA(byte[] data){
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
            return new String(decryptCipher.doFinal(data), StandardCharsets.UTF_8);
        }catch (Exception e){
            EmotionalSongsServer.mainView.logError("Exception thrown while attempting to decrypt RSA data");
            return "";
        }
    }

    public synchronized void registerClient(PingClient client) throws RemoteException{
        if (!clientList.contains(client)){
            System.out.println("Client registered");
            clientList.add(client);}
    }

    protected static HashSet<PingClient> getClientList(){
        return clientList;
    }

}
