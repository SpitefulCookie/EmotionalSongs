package emotionalsongs;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Implementation of the RepositoryManager interface that provides methods to interact with the song repository.
 * This class extends UnicastRemoteObject to facilitate remote method invocation.
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
     * @return A HashSet of Canzone objects representing songs in the playlist.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public HashSet<Canzone> getSongsInPlaylist(String playlistName) throws RemoteException {

        ArrayList<String[]> dataRetrieved = dbReference.executeQuery(new String[]{playlistName}, QueryHandler.QUERY_SONGS_IN_PLAYLIST);
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
     */
    @Override // Verificata
    public void registerPlaylist(String playlistName, String username) throws RemoteException {
        dbReference.executeUpdate(new String[]{playlistName, username}, QueryHandler.QUERY_REGISTER_PLAYLIST );
    }

    /**
     * Adds a song to the specified playlist for the given user.
     *
     * @param nomePlaylist The name of the playlist to which the song will be added.
     * @param userID The ID of the user adding the song to the playlist.
     * @param songUUID The UUID of the song to be added.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override // Verificata
    public void addSongToPlaylist(String nomePlaylist, String userID, String songUUID) throws RemoteException {
        dbReference.executeUpdate(new String[]{nomePlaylist, userID, songUUID}, QueryHandler.QUERY_REGISTER_SONG_IN_PLAYLIST );
    }

    /**
     * Registers user emotions for a song.
     *
     * @param emozioniProvate The list of emotions experienced by the user. The order of the emotion must be as defined in the EmozioneEnum class
     * @param songUUID The UUID of the song.
     * @param userId The ID of the user.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override // Verificata
    public void registerEmotions(ArrayList<Emozione> emozioniProvate, String songUUID, String userId) throws RemoteException {

        int i = 0;
        for (var emozione : emozioniProvate){

            dbReference.executeUpdate(
                    new String[]{
                        EmozioneEnum.values()[i++].toString(),
                        userId,
                        songUUID,
                        String.valueOf(emozione.getPunteggio()),
                        emozione.getCommento()
                    },
                    QueryHandler.QUERY_REGISTER_SONG_EMOTION
            );

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
    @Override // Verificata
    public ArrayList<Emozione> getSongEmotions(String songUUID, String userid) throws RemoteException {
        // Vedi commento in QueryHandler.executeQuery sul motivo del passaggio di un array di stringhe vuoto.
        ArrayList<String[]> dataRetrieved = dbReference.executeQuery(new String[]{}, QueryHandler.QUERY_GET_SONG_EMOTIONS.replace("uId", userid).replace("sId", songUUID));

        if (dataRetrieved.size() != 0) { // Se sono stati ottenuti dei risultati
            ArrayList<Emozione> resultsToBeReturned = new ArrayList<>();

            for (int i = 0; i< dataRetrieved.size(); i++){
                Emozione e = EmozioneEnum.getEmotionInstanceFromEnumValue(i);
                e.setPunteggio(Integer.parseInt(dataRetrieved.get(i)[0]));
                e.setCommento(dataRetrieved.get(i)[1]);
                resultsToBeReturned.add(e);
            }

            return resultsToBeReturned;
        }

        return new ArrayList<>();

    }

}
