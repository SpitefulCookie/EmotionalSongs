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

/**
 *
 * This interface defines a method to be implemented by remote ping clients.
 * The purpose of the method is to allow the server to ping the client to check their responsiveness.
 *
 * @author <a href="https://github.com/SpitefulCookie"> Della Chiesa Mattia</a>
 */
public interface PingClient extends Remote {

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
