package emotionalsongs;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Implementation of the {@link PingableClient} interface that extends {@link UnicastRemoteObject}.<br><br>
 *
 * This class represents a client capable of responding to pings from a remote server and
 * establishing secure communication using RSA encryption.<br><br>
 *
 * A pair of RSA keys is generated during construction, which are used for secure communication with the server.
 * It provides methods for retrieving the client's public and private keys, as well as responding to ping requests from the server.
 *
 */
class PingableClientClientImpl extends UnicastRemoteObject implements PingableClient {

    @Serial
    private static final long serialVersionUID = 1L;
    private static KeyPair pair;
    private static Calendar calendar;

    /**
     * Constructor method for the class {@code PingableClient}.<br><br>
     *
     * This constructor creates a new instance of the class {@code PingableClient} by generating a new pair of keys used
     * to establish a secure communication between the client and the server.
     * @throws RemoteException If a communication error occurs during the remote method invocation.
     */
    protected PingableClientClientImpl() throws RemoteException {
        super();
        calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        generateKeys();
    }

    /**
     * Retrieves the client's public key.<br><br>
     *
     * This method retrieves the client's public key used to transmit sensible data on the network. This key is used by
     * the server to encrypt the data using an RSA algorithm.
     *
     * @return The {@link PublicKey} used to encrypt the data to be sent
     * @throws RemoteException If a communication error occurs during the remote method invocation.
     */
    @Override
    public PublicKey getPublicKey() throws RemoteException {
        return pair.getPublic();
    }

    /**
     * Retrieves the client's private key.<br><br>
     *
     * This method retrieves the client's private key used to decrypt sensible data received from the network. This key is used by
     * the client to decrypt the RSA-encrypted data received from the server.
     *
     * @return The {@link PublicKey} used to decrypt the data received
     * @throws RemoteException If a communication error occurs during the remote method invocation.
     */
    protected PrivateKey getPrivateKey() throws RemoteException { // NOTE: this method is NOT invokable by the server.
        return pair.getPrivate();
    }

    /**
     * Pings the remote client to check its responsiveness.<br><br>
     *
     * This method is intended to be invoked by the server to determine if the client is responsive.
     * The responsiveness of the client is checked through {@code RemoteException}.<br><br>
     *
     * @throws RemoteException If a communication-related exception occurs during the ping process.
     */
    public void pingClient() throws RemoteException{
        calendar.setTime(new Date());
        System.out.println("["+ String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) )+ "] "+ "I've been pinged by the server!");
    }

    /**
     * Generates RSA key pairs if not already generated.
     */
    private static void generateKeys() {
        if (pair == null) {

            KeyPairGenerator generator;

            try {
                generator = KeyPairGenerator.getInstance("RSA");
                generator.initialize(2048);
                pair = generator.generateKeyPair();
            } catch (NoSuchAlgorithmException e) {
                //
            }
        }
    }

}
