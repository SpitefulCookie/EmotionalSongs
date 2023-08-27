package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import java.io.Serializable;

/**
 * The class {@code Canzone} represents the information related to a song present within the database.
 * <p>Each song id identified by the following data:
 *
 *  <ul>
 *  <li> Song UUID, a unique identifier
 *  <li> Song title
 *  <li> Author
 *  <li> Release year
 *  </ul>
 *
 * <p>This class implements the {@link Serializable} interface to support object serialization.
 * <p>
 *  @author <a href="https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
 */
public class Canzone implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String titolo;
    private final String songUUID;
    private final String autore;
    private final int anno;

    /**
     * Constructs a new instance of {@link Canzone} with the provided data.<br><br>
     *
     * <p>This constructor initializes a new {@code Canzone} object with the specified title,
     * author, year of release, and UUID. The UUID is used to uniquely identify the song.
     *
     * @param titolo The title of the song.
     * @param autore The author of the song.
     * @param anno   The release of release.
     * @param uuid   The unique identifier associated with the song.
     */
    public Canzone(String titolo, String autore, int anno, String uuid) {

        this.songUUID = uuid;
        this.titolo = titolo;
        this.autore = autore;
        this.anno = anno;

    }

    /**
     * Retrieves the UUID associated with the song.<br><br>
     *
     * <p>This method returns the UUID associated with the song. The UUID is a unique identifier
     * that distinguishes this song from others.
     *
     * @return The UUID of the song.
     */
    public String getSongUUID() {
        return this.songUUID;
    }

    /**
     * Retrieves the author associated with the song.<br><br>
     * <p>This method returns a {@code String} representing the Author of the song. To avoid any issues with the return of
     * null values, if no author is specified, the method will return an empty {@code String}.
     *
     * @return A {@code String} representing the author of the song; An empty string if none were specified.
     */
    public String getAutore() {
        if (this.autore != null)
            return this.autore;
        return "";
    }

    /**
     * Retrieves the title of the song.<br><br>
     * <p>This method returns a {@code String} representing the title of the song.
     * To avoid any issues with the return of null values, if no author is specified, the method will return
     * an empty {@code String}.
     *
     * @return A {@code String} representing the title of the song; An empty string if none were specified.
     */
    public String getTitolo() {
        if (this.titolo != null)
            return this.titolo;
        return "";
    }

    /**
     * Retrieves the year of release of the song.<br><br>
     *
     * <p>This method returns the year in which the song was released.
     *
     * @return The year of release of the song.
     */
    public int getAnno() {
        return this.anno;
    }

    /**
     * Returns a string containing information about the song in the format:<br><br>
     * "Title - Author (Year)"<br><br>
     *
     * <p>This method generates a formatted string that includes the song's title,
     * author, and year of release.
     *
     * @return A {@code String} object containing information about the song.
     */
    @Override
    public String toString() {
        return this.titolo + " - " + this.autore + " (" + this.anno + ")";
    }

}