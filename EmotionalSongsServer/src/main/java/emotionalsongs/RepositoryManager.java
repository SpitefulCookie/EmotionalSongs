package emotionalsongs;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RepositoryManager extends Remote {
    Canzone[] ricercaCanzone(String titolo) throws RemoteException; // todo rename method
    // getUserPlaylist(String user) : ArrayList<String>
    // getPlaylistSongs(String playlistname): ArrayList<Canzone> // TODO make canzone serializable
    // getSongEmotions(String SongUUID, String userid) : ArrayList<Emozione> // TODO make Emozione serializable
    // getSongCollectiveEmotions(String UUID) : int[9]
    // registerPlaylist(String username, String nomeplaylist)
    // addSongToPlaylist(String username, Stringa nomePlaylist, ArrayList<String> soongUUIDS)
    // registerEmotions(ArrayList<Emozione>, String songUUID)
    //
}
