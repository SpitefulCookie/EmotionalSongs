package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

/**
 *
 * This interface defines a method to be implemented by remote ping clients.
 * The purpose of the method is to allow the server to ping the client to check their responsiveness.
 *
 * @author <a href="https://github.com/SpitefulCookie"> Della Chiesa Mattia</a>
 */
public interface PingableClient extends Remote {

    /**
     * Retrieves the client's public key.<br><br>
     *
     * This method retrieves the client's public key used to transmit sensible data on the network. This key is used by
     * the server to encrypt the data using an RSA algorithm.
     *
     * @return The {@link PublicKey} used to encrypt the data to be sent
     * @throws RemoteException If a communication error occurs during the remote method invocation.
     */
    PublicKey getPublicKey() throws RemoteException;

    /**
     * Pings the remote client to check its responsiveness.<br><br>
     *
     * This method is intended to be invoked by the server to determine if the client is responsive.
     * The responsiveness of the client is checked through {@code RemoteException}.<br><br>
     *
     * @throws RemoteException If a communication-related exception occurs during the ping process.
     */
    void pingClient() throws RemoteException;
}
