package emotionalsongs;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

class PingableClientClientImpl extends UnicastRemoteObject implements PingableClient {

    @Serial
    private static final long serialVersionUID = 1L;
    private static KeyPair pair;
    private static Calendar calendar;

    protected PingableClientClientImpl() throws RemoteException {
        super();
        calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        generateKeys();
    }

    @Override
    public PublicKey getPublicKey() throws RemoteException {
        return pair.getPublic();
    }

    protected PrivateKey getPrivateKey() throws RemoteException {
        return pair.getPrivate();
    }

    public void pingClient() throws RemoteException{
        calendar.setTime(new Date());
        System.out.println("["+ String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) )+ "] "+ "I've been pinged by the server!");
    }

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
