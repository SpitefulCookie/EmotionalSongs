package emotionalsongs;


import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.Random;

/**
 * TODO Document
 */
public class AuthManagerImpl extends UnicastRemoteObject implements AuthManager{

    private static final long serialVersionUID = 1L;
    private static QueryHandler dbReference;

    private static KeyPair pair = null;

    /**
     * TODO Document
     * @throws RemoteException
     */
    protected AuthManagerImpl() throws RemoteException {
        super();
        generateKeys();
        dbReference = EmotionalSongsServer.qh;

        // TODO auto-generated stub
    }

    /**
     * TODO Document and Implement
     * @param username
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean usernameExists(String username) throws RemoteException {
        System.out.println("[SERVER] Ricevuto username: " + username);
        Random random = new Random();

        if (random.nextInt()<0.2){
           return false;
        } else{
            return true;
        }
    }

    /**
     * TODO Document and Implement
     * @param user
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean registrazione(String user) throws RemoteException { // I dati come verranno mandati? Sotto forma di stringa o oggetto utente? Codifica rsa?

        //
        return false;
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

        String encryptedPwd = decryptRSA(pwd);
        // TODO reperire la password dal db usando l'username fornito
        String testHashPwd = BCryptHashPassword("test");

        if( username.equals("test") && BCryptVerifyPassword(encryptedPwd, testHashPwd)){ return true;}
        else{return false;}

    }

    /**
     * TODO Document
     * @param password
     * @return
     */
    private String BCryptHashPassword(String password) {
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

}
