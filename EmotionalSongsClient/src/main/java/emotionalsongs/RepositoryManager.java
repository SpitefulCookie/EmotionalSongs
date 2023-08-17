package emotionalsongs;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;


public interface RepositoryManager extends Remote {

    HashSet<Canzone> ricercaCanzone(String titolo) throws RemoteException; // todo rename method
    HashSet<Canzone> ricercaCanzone(String autore, String anno) throws RemoteException;
    HashSet<String> getUserPlaylists(String user) throws RemoteException;
    HashSet<Canzone> getSongsInPlaylist(String playlistName, String username) throws RemoteException;
    void registerPlaylist (String playlistName, String username) throws RemoteException, SQLException;
    void addSongToPlaylist(String nomePlaylist, String userID, String songUUID) throws RemoteException, SQLException;
    void registerEmotions(ArrayList<Emozione> emozioniProvate, String songUUID, String userId) throws RemoteException, SQLException;
    ArrayList<Emozione> getSongEmotions(String songUUID, String userid) throws RemoteException;
    double[] getSongAverageEmotions(String songUUID) throws RemoteException;

}
