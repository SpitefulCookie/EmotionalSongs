package emotionalsongs;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;


public interface RepositoryManager extends Remote {

    // TODO Ho optato per una hashSet invece che una ArrayList per migliori prestazioni (la H.S non garantisce l'ordine degli elementi però non penso che ce ne freghi.
    HashSet<Canzone> ricercaCanzone(String titolo) throws RemoteException; // todo rename method
    HashSet<Canzone> ricercaCanzone(String autore, String anno) throws RemoteException;
    HashSet<String> getUserPlaylists(String user) throws RemoteException;
    HashSet<Canzone> getSongsInPlaylist(String playlistName) throws RemoteException;
    void registerPlaylist (String username, String playlistName, String loggedUser) throws RemoteException;
    void addSongToPlaylist( String username, String nomePlaylist) throws RemoteException;
    void registerEmotions(ArrayList<Emozione> emozioniProvate, String songUUID, String userId) throws RemoteException;
    ArrayList<Emozione> getSongEmotions(String songUUID, String userid) throws RemoteException;

    //int[] getPlaylistSongs(String playlistName) throws RemoteException; // TODO (int[]? che canna avevamo fumato??)

}
