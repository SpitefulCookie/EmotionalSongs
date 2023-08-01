package emotionalsongs;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RepositoryManagerImpl extends UnicastRemoteObject implements RepositoryManager{

    private static final long serialVersionUID = 1L;
    private static QueryHandler dbReference;

    protected RepositoryManagerImpl() throws RemoteException {
        super();
    }


    @Override
    public Canzone[] ricercaCanzone(String titolo) {
        return new Canzone[0];
    }
}
