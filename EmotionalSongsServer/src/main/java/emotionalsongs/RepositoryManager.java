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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The {@code RepositoryManager} interface defines the remote methods for managing song repositories and playlists
 * that can be invoked on the remote object using Java RMI.<br><br>
 *
 * <p>This interface includes methods for searching songs by title and author, retrieving user playlists,
 * managing songs in playlists, registering new playlists, adding songs to playlists, registering user emotions
 * for songs, etc...
 *
 * @author <a href="https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
 */

public interface RepositoryManager extends Remote {

    /**
     * Searches for songs by title and returns a set of matching songs.
     *
     * @param titolo The title of the song to search for.
     * @return A HashSet of Canzone objects representing matching songs.
     */
    HashSet<Canzone> ricercaCanzone(String titolo) throws RemoteException; // todo rename method

    /**
     * Searches for songs by author and year and returns a set of matching songs.
     *
     * @param autore The author of the song to search for.
     * @param anno The year of the song to search for.
     * @return A HashSet of Canzone objects representing matching songs.
     * @throws RemoteException If a remote communication error occurs.
     */
    HashSet<Canzone> ricercaCanzone(String autore, String anno) throws RemoteException;

    /**
     * Retrieves user playlists for the given user.
     *
     * @param user The username for which to retrieve playlists.
     * @return A HashSet of playlist names belonging to the user.
     * @throws RemoteException If a remote communication error occurs.
     */
    HashSet<String> getUserPlaylists(String user) throws RemoteException;
    /**
     * Retrieves a set of songs present in the specified playlist.
     *
     * @param playlistName The name of the playlist for which to retrieve songs.
     * @param username The username of the user registering the playlist.
     * @return A HashSet of Canzone objects representing songs in the playlist.
     * @throws RemoteException If a remote communication error occurs.
     */
    HashSet<Canzone> getSongsInPlaylist(String playlistName, String username) throws RemoteException;

    /**
     * Registers a new playlist for the specified user.
     *
     * @param playlistName The name of the playlist to register.
     * @param username The username of the user registering the playlist.
     * @throws RemoteException If a remote communication error occurs.
     * @return {@code true} if the playlist registration ended with a success, {@code false} otherwise
     */
    boolean registerPlaylist (String playlistName, String username) throws RemoteException, SQLException;

    /**
     * Adds a song to the specified playlist for the given user.
     *
     * @param nomePlaylist The name of the playlist to which the song will be added.
     * @param userID The ID of the user adding the song to the playlist.
     * @param songUUID The UUID of the song to be added.
     * @throws RemoteException If a remote communication error occurs.
     * @throws SQLException If an SQL error occurs.
     */
    void addSongToPlaylist(String nomePlaylist, String userID, String songUUID) throws RemoteException, SQLException;

    /**
     * Registers user emotions for a song.
     *
     * @param emozioniProvate The list of emotions experienced by the user. The order of the emotion must be as defined in the EmozioneEnum class
     * @param songUUID The UUID of the song.
     * @param userId The ID of the user.
     * @throws RemoteException If a remote communication error occurs.
     * @throws SQLException If an SQL error occurs.
     */
    void registerEmotions(ArrayList<Emozione> emozioniProvate, String songUUID, String userId) throws RemoteException, SQLException;

    /**
     * Retrieves emotions associated with a song for a specific user.
     *
     * @param songUUID The UUID of the desired song.
     * @param userid The ID of the user (username).
     * @return An ArrayList of Emozione objects representing emotions associated with the song.
     * @throws RemoteException If a remote communication error occurs.
     */
    ArrayList<Emozione> getSongEmotions(String songUUID, String userid) throws RemoteException;

    /**
     * Retrieves the average emotion scores for a specified song.<br><br>
     *
     * <p>The {@code getSongAverageEmotions} method fetches the average emotion scores associated with a
     * specific song. The method queries the database to retrieve the average emotion scores and returns an array of
     * double values representing the average scores for each emotion.
     *
     * @param songUUID The UUID of the song desired.
     * @return An array of double values representing the average emotion scores for the specified song.
     *         The array follows the order of emotions defined by the {@link EmozioneEnum} class.
     *         If no scores are available, an array with negative values will be returned.
     * @throws RemoteException If a communication error occurs during the remote method invocation.
     */
    double[] getSongAverageEmotions(String songUUID) throws RemoteException;

    /**
     * Retrieves the total number of user feedbacks (registered emotions) for the song provided.
     *
     * @param songUUID The UUID of the song for which to retrieve the total feedback count.
     * @return The total number of user feedbacks for the specified song.
     */
    int getTotalUserFeedbacksForSong (String songUUID) throws RemoteException;

}
