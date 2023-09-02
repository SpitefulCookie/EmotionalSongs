package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Implementation of the RepositoryManager interface that provides methods to interact with the song repository.
 * This class extends {@link UnicastRemoteObject} to facilitate remote method invocation.ù
 *
 * @author <a href="https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
 */
public class RepositoryManagerImpl extends UnicastRemoteObject implements RepositoryManager{

    @Serial private static final long serialVersionUID = 1L;
    private static QueryHandler dbReference;

    /**
     * Constructor to initialize the RepositoryManager implementation.
     * Retrieves the QueryHandler instance from EmotionalSongsServer.
     *
     * @throws RemoteException If a remote communication error occurs.
     */
    protected RepositoryManagerImpl() throws RemoteException {
        super();
        dbReference = EmotionalSongsServer.getQueryHandlerInstance();
    }

    /**
     * Searches for songs by title and returns a set of matching songs.
     *
     * @param titolo The title of the song to search for.
     * @return A HashSet of Canzone objects representing matching songs.
     */
    @Override // Verificata
    public HashSet<Canzone> ricercaCanzone(String titolo) {
        ArrayList<String[]> dataRetrieved = dbReference.executeQuery(new String[]{}, String.format(QueryHandler.QUERY_SEARCH_SONG_BY_TITLE, titolo).replace('#','%'));
        if (dataRetrieved.size() != 0) {
            HashSet<Canzone> resultsToBeReturned = new HashSet<>();
            for (var s : dataRetrieved) {
                // Struttura tabella db: SongUUID(idx = 0), titolo(1), autore(2), anno(3);
                // Ordine argomenti costruttore Canzone: titolo, autore, anno, uuid.
                resultsToBeReturned.add(new Canzone(s[1], s[2], Integer.parseInt(s[3]), s[0]));
            }
            return resultsToBeReturned;
        }
        return new HashSet<>();

    }

    /**
     * Searches for songs by author and year and returns a set of matching songs.
     *
     * @param autore The author of the song to search for.
     * @param anno The year of the song to search for.
     * @return A HashSet of Canzone objects representing matching songs.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override // Verificata
    public HashSet<Canzone> ricercaCanzone(String autore, String anno) throws RemoteException {
        ArrayList<String[]> dataRetrieved = dbReference.executeQuery(new String[]{autore, anno}, QueryHandler.QUERY_SEARCH_SONG_BY_AUTHOR_AND_YEAR);
        if (dataRetrieved.size() != 0) {
            HashSet<Canzone> resultsToBeReturned = new HashSet<>();
            for (var s : dataRetrieved) {
                // Struttura tabella db: SongUUID(idx = 0), titolo(1), autore(2), anno(3);
                // Ordine argomenti costruttore Canzone: titolo, autore, anno, uuid.
                resultsToBeReturned.add(new Canzone(s[1], s[2], Integer.parseInt(s[3]), s[0]));
            }
            return resultsToBeReturned;
        }

        return new HashSet<>();
    }

    /**
     * Retrieves user playlists for the given user.
     *
     * @param user The username for which to retrieve playlists.
     * @return A HashSet of playlist names belonging to the user.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override // Verificata
    public HashSet<String> getUserPlaylists(String user) throws RemoteException {

        ArrayList<String[]> dataRetrieved = dbReference.executeQuery(new String[]{user}, QueryHandler.QUERY_USER_PLAYLISTS);
        if (dataRetrieved.size() != 0) {
            HashSet<String> resultsToBeReturned = new HashSet<>();
            for (var s : dataRetrieved) {
                resultsToBeReturned.add(s[0]); // l'indice sarà sempre zero perché viene effettuato il SELECT di una sola colonna (nome).
            }
            return resultsToBeReturned;
        }

        return new HashSet<>();
    }

    /**
     * Retrieves a set of songs present in the specified playlist.
     *
     * @param playlistName The name of the playlist for which to retrieve songs.
     * @param username The username of the user registering the playlist.
     * @return A HashSet of Canzone objects representing songs in the playlist.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public HashSet<Canzone> getSongsInPlaylist(String playlistName, String username) throws RemoteException {

        ArrayList<String[]> dataRetrieved = dbReference.executeQuery(new String[]{playlistName, username}, QueryHandler.QUERY_SONGS_IN_PLAYLIST);
        if (dataRetrieved.size() != 0) {
            HashSet<Canzone> resultsToBeReturned = new HashSet<>();
            for (var s : dataRetrieved) {
                resultsToBeReturned.add(new Canzone(s[0], s[1], Integer.parseInt(s[2]), s[3]));
            }
            return resultsToBeReturned;
        }

        return new HashSet<>();

    }

    /**
     * Registers a new playlist for the specified user.
     *
     * @param playlistName The name of the playlist to register.
     * @param username The username of the user registering the playlist.
     * @throws RemoteException If a remote communication error occurs.
     * @return {@code true} if the playlist registration ended with a success, {@code false} otherwise
     */
    @Override
    public boolean registerPlaylist(String playlistName, String username) throws RemoteException {

        try {
            dbReference.executeUpdate(new String[]{playlistName, username}, QueryHandler.QUERY_REGISTER_PLAYLIST );
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Adds a song to the specified playlist for the given user.
     *
     * @param nomePlaylist The name of the playlist to which the song will be added.
     * @param userID The ID of the user adding the song to the playlist.
     * @param songUUID The UUID of the song to be added.
     * @throws RemoteException If a remote communication error occurs.
     * @return {@code true} if the playlist registration ended with a success, {@code false} otherwise
     */
    @Override
    public boolean addSongToPlaylist(String nomePlaylist, String userID, String songUUID) throws RemoteException {
        try {
            dbReference.executeUpdate(new String[]{nomePlaylist, userID, songUUID}, QueryHandler.QUERY_REGISTER_SONG_IN_PLAYLIST );
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Registers user emotions for a song.
     *
     * @param emozioniProvate The list of emotions experienced by the user. The order of the emotion must be as defined in the EmozioneEnum class
     * @param songUUID The UUID of the song.
     * @param userId The ID of the user.
     * @throws RemoteException If a remote communication error occurs.
     * @return {@code true} if the playlist registration ended with a success, {@code false} otherwise
     */
    @Override
    public boolean registerEmotions(ArrayList<Emozione> emozioniProvate, String songUUID, String userId) throws RemoteException {

        System.out.println("emozioniProvate length:" + emozioniProvate.size());

        ArrayList<String> data = new ArrayList<>();
        data.add(userId);
        data.add(songUUID);

        try {

            for (var emozione : emozioniProvate) {

                System.out.println("Emozione:" + emozione.getClass().getSimpleName()+ "\n\tPunteggio: "+emozione.getPunteggio()+"\n\tCommento: "+ emozione.getCommento());

                data.add(emozione.getPunteggio()+"");
                data.add(emozione.getCommento());

            }

            dbReference.executeUpdate(
                    data.toArray(new String[0]),
                    QueryHandler.QUERY_REGISTER_SONG_EMOTION
            );

            return true;

        } catch(SQLException e1){
            EmotionalSongsServer.mainView.logError("SQL exception thrown while executing a rollback or while attempting to enable auto-commit.\n" + e1.getMessage());
            return false;
        }

    }

    /**
     * Retrieves emotions associated with a song for a specific user.
     *
     * @param songUUID The UUID of the desired song.
     * @param userid The ID of the user (username).
     * @return An ArrayList of Emozione objects representing emotions associated with the song.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public ArrayList<Emozione> getSongEmotions(String songUUID, String userid) throws RemoteException {
        ArrayList<String[]> dataRetrieved = dbReference.executeQuery(new String[]{userid, songUUID}, QueryHandler.QUERY_GET_SONG_EMOTIONS);
        if (dataRetrieved.size() != 0) { // Se sono stati ottenuti dei risultati
            String[] splitData = dataRetrieved.get(0);
            ArrayList<Emozione> resultsToBeReturned = new ArrayList<>();

            for (int i = 0; i<splitData.length; i++){
                Emozione e = EmozioneEnum.getInstanceFromEnum(i/2);
                e.setPunteggio(Integer.parseInt(splitData[i]));
                e.setCommento(splitData[++i]);
                resultsToBeReturned.add(e);
            }

            return resultsToBeReturned;
        }

        return new ArrayList<>();

    }

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
    @Override
    public double[] getSongAverageEmotions(String songUUID) throws RemoteException {
        // Vedi commento in QueryHandler.executeQuery sul motivo del passaggio di un array di stringhe vuoto.
        ArrayList<String[]> dataRetrieved = dbReference.executeQuery(new String[]{songUUID}, QueryHandler.QUERY_GET_SONG_AVERAGE_SCORES);
        double[] dataToBeReturned = new double[9];

        if (dataRetrieved.get(0)[0]!= null) { // Se sono stati ottenuti dei risultati

            String[] splitData = dataRetrieved.get(0);

            for (int i = 0; i< splitData.length; i++){
                dataToBeReturned[i] = Double.parseDouble(splitData[i]);
            }

        } else{
            Arrays.fill(dataToBeReturned, -1);
        }
        return dataToBeReturned;

    }

    /**
     * Retrieves the total number of user feedbacks (registered emotions) for the song provided.
     *
     * @param songUUID The UUID of the song for which to retrieve the total feedback count.
     * @return The total number of user feedbacks for the specified song.
     */
    public int getTotalUserFeedbacksForSong(String songUUID){

        ArrayList<String[]> dataRetrieved = dbReference.executeQuery(new String[]{songUUID}, QueryHandler.QUERY_GET_NUMBER_OF_FEEDBACK);
        return Integer.parseInt(dataRetrieved.get(0)[0]);

    }

}
