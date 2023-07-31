package emotionalsongs;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

class PingClientClientImpl extends UnicastRemoteObject implements PingClient {

    private static final long serialVersionUID = 1L;

    private static Calendar calendar;

    protected PingClientClientImpl() throws RemoteException {
        super();
        calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
    }

    public void pingClient() throws RemoteException{

        System.out.println("["+calendar.get(Calendar.HOUR_OF_DAY)+":"+ calendar.get(Calendar.MINUTE)  + "] I've been pinged by the server!");
    }


}
