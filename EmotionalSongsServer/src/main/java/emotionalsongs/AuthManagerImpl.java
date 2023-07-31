package emotionalsongs;


import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.ArrayList;

/**
 * TODO Document
 */
public class AuthManagerImpl extends UnicastRemoteObject implements AuthManager{

    private static final long serialVersionUID = 1L;
    private QueryHandler dbReference;

    private static boolean acceptsConnections = true;
    private static KeyPair pair = null;
    private static ArrayList<PingClient> clientList = new ArrayList<>();

    /**
     * TODO Document
     * @throws RemoteException
     */
    protected AuthManagerImpl() throws RemoteException {
        super();
        generateKeys();
        clientList = new ArrayList<>();
        dbReference = EmotionalSongsServer.qh;

        // TODO auto-generated stub
    }

    public static void removeClients(ArrayList<PingClient> disconnected) {
        clientList.removeAll(disconnected);
    }

    /**
     * TODO Document and Implement
     * @param username
     * @return
     * @throws RemoteException
     */

    public boolean usernameExists(String username) throws RemoteException {
        System.out.println("[SERVER] Ricevuto username: " + username);
        if(username!=null && dbReference.usernameExists(username)){
            return true;
        }
        return false;
    }

    /**
     * TODO Document and Implement
     * @param userData
     * @return
     * @throws RemoteException
     */
    @Override
    public void registrazione(byte[] userData) throws RemoteException {

        String[] data = decryptRSA(userData).split("&SEP&");

        dbReference.registerUser(data);

    }

    public PublicKey getPublicKey() throws RemoteException{
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

            KeyPairGenerator generator = null;

            try {
                generator = KeyPairGenerator.getInstance("RSA");
                generator.initialize(2048);
                pair = generator.generateKeyPair();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

    }

    private String decryptRSA(byte[] data){
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
            return new String(decryptCipher.doFinal(data), StandardCharsets.UTF_8);
        }catch (Exception e){
            return "";
        }
    }

    public void registerClient(PingClient client) throws RemoteException{
        if (acceptsConnections && !clientList.contains(client)){ clientList.add(client);}
        else{throw new RemoteException();}
    }

    protected static ArrayList<PingClient> getClientList(){
        return clientList;
    }


}
