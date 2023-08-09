package emotionalsongs;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;

public class RepositoryManagerImpl extends UnicastRemoteObject implements RepositoryManager{

    @Serial
    private static final long serialVersionUID = 1L;
    private static QueryHandler dbReference;

    protected RepositoryManagerImpl() throws RemoteException {
        super();
        dbReference = EmotionalSongsServer.getQueryHandlerInstance();
    }

    @Override
    public HashSet<Canzone> ricercaCanzone(String titolo) { // verificata
        ArrayList<String[]> dataRetrieved = dbReference.executeQuery(new String[]{titolo}, QueryHandler.QUERY_SEARCH_SONG_BY_TITLE);
        if (dataRetrieved.size() != 0) {
            HashSet<Canzone> resultsToBeReturned = new HashSet<>();
            for (var s : dataRetrieved) {
                // Struttura tabella db: SongUUID(idx = 0), titolo(1), autore(2), anno(3);
                // Ordine argomenti costruttore Canzone: titolo, autore, anno, uuid.
                resultsToBeReturned.add(new Canzone(s[1], s[2], Integer.parseInt(s[3]), s[0]));
            }
            return resultsToBeReturned;
        }

        return null;

    }

    @Override
    public HashSet<Canzone> ricercaCanzone(String autore, String anno) throws RemoteException { // verificata
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

        return null;
    }

    @Override
    public HashSet<String> getUserPlaylists(String user) throws RemoteException { // non verificata

        ArrayList<String[]> dataRetrieved = dbReference.executeQuery(new String[]{user}, QueryHandler.QUERY_USER_PLAYLISTS);
        if (dataRetrieved.size() != 0) {
            HashSet<String> resultsToBeReturned = new HashSet<>();
            for (var s : dataRetrieved) {
                resultsToBeReturned.add(s[0]); // l'indice sarà sempre zero perché viene effettuato il SELECT di una sola colonna (nome).
            }
            return resultsToBeReturned;
        }

        return null;
    }

    @Override
    public HashSet<Canzone> getSongsInPlaylist(String playlistName) throws RemoteException { // non verificata

        ArrayList<String[]> dataRetrieved = dbReference.executeQuery(new String[]{playlistName}, QueryHandler.QUERY_SONGS_IN_PLAYLIST);
        if (dataRetrieved.size() != 0) {
            HashSet<Canzone> resultsToBeReturned = new HashSet<>();
            for (var s : dataRetrieved) {
                resultsToBeReturned.add(new Canzone(s[0], s[1], Integer.parseInt(s[2]), s[3]));
            }
            return resultsToBeReturned;
        }

        return null;

    }

    @Override
    public void registerPlaylist(String username, String playlistName, String loggedUser) throws RemoteException { // non verificata
        dbReference.executeUpdate(new String[]{username, playlistName, loggedUser}, QueryHandler.QUERY_REGISTER_PLAYLIST );
    }

    @Override
    public void addSongToPlaylist(String username, String nomePlaylist) throws RemoteException { // non verificata
        dbReference.executeUpdate(new String[]{username, nomePlaylist}, QueryHandler.QUERY_REGISTER_SONG_IN_PLAYLIST );
    }

    @Override
    public void registerEmotions(ArrayList<Emozione> emozioniProvate, String songUUID, String userId) throws RemoteException { // verificata

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

    // TODO Implement
    @Override
    public ArrayList<Emozione> getSongEmotions(String songUUID, String userid) throws RemoteException { // verificata
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

        return null;
    }

    // TODO remove before turning in the project
    public void test(){

        /* -------- TEST registra emozioni -------- /

        ArrayList<Emozione> testEmozioni = new ArrayList<>();
        testEmozioni.add(new Amazement(1,"a"));
        testEmozioni.add(new Solemnity(2,"b"));
        testEmozioni.add(new Tenderness(3,"c"));
        testEmozioni.add(new Nostalgia(4,"d"));
        testEmozioni.add(new Calmness(5,"e"));
        testEmozioni.add(new Power(1,"f"));
        testEmozioni.add(new Joy(2,"g"));
        testEmozioni.add(new Tension(3,"h"));
        testEmozioni.add(new Sadness(4,"i"));

        try {
            registerEmotions(testEmozioni,"TEST_UUID", "test");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // -------- Fine TEST registra emozioni -------- */


        /* -------- TEST getSongEmotions: -------- /

        try {
            ArrayList<Emozione> emozioni = getSongEmotions("TEST_UUID", "test");
            for (Emozione a:emozioni) {
                System.out.println("Emozione: "+a.getClass().getName() + "\nPunteggio: " + a.getPunteggio() +"\nCommento: " + a.getCommento());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // -------- Fine TEST getSongEmotions -------- */


         /* -------- TEST Ricerca canzone per autore e anno: -------- /
        try {
            HashSet<Canzone> canzoni = ricercaCanzone("Queen", "1980");
            for (Canzone a:canzoni) {
                System.out.println(a.toString());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // -------- Fine TEST Ricerca canzone per autore e anno -------- */


    }

}
