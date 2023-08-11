package emotionalsongs;

/*
 * Progetto svolto da:
 * 
 * Della Chiesa Mattia 749904, Ateneo di Varese
 * 
 */

import java.io.Serial;
import java.io.Serializable;

/**
 * La classe {@code Canzone} rappresenta le informazioni relative a una canzone presente all'interno della repository.
 *  <p>Ogni canzone viene identificato dai seguenti dati:
 * 
 *  <ul>
 *  <li> UUID
 *  <li> Titolo della canzone
 *  <li> Nome dell'autore
 *  <li> Anno di pubblicazione
 *  </ul>
 *
 * 
 *  @author <a href="https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
 *  
 */
public class Canzone implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    private final String titolo;
    private final String songUUID;
    private final String autore;
    private final int anno;

    /***
     * Costruisce un oggetto {@code Canzone} a partire dai singoli dati che la descrivono.
     * @param titolo un oggetto di tipo {@code String} che rappresenta il titolo della canzone.
     * @param autore un oggetto di tipo {@code String} che rappresenta l'autore della canzone.
     * @param anno un valore {@code int} che indica l'anno di pubblicazione della canzone.
     * @param uuid un oggetto di tipo {@code String} che rappresenta un UUID di tipo 3.
     * 
     */
    public Canzone(String titolo, String autore, int anno, String uuid) {

        super();
        this.songUUID = uuid;
        this.titolo = titolo;
        this.autore = autore;
        this.anno = anno;

    }

    /***
     * Costruisce un oggetto {@code Canzone} a partire da un'unica stringa contenente i dati che la descrivono.
     * @param dati un oggetto di tipo {@code String}, formattato secondo lo standard CSV, contenente le informazioni relative ad una canzone.
     *
     */
    public Canzone(String[] dati){

        this.songUUID = dati[0];
        this.titolo = dati[1];
        this.autore = dati[2];
        this.anno = Integer.parseInt(dati[3]);

    }

    /***
     * Restituisce l'UUID type 3 associato alla canzone.
     * @return un oggetto di tipo {@code String} contenente l'UUID della canzone.
     */
    public String getSongUUID(){return this.songUUID;}
    
     /***
     * Restituisce l'autore della canzone.
     * @return un oggetto di tipo {@code String} contenente l'autore della canzone.
     */
    public String getAutore(){return this.autore;}

    /***
     * Restituisce il titolo della canzone.
     * @return un oggetto di tipo {@code String} contenente il titolo della canzone.
     */
    public String getTitolo(){return this.titolo;}

    /***
     * Restituisce l'anno di pubblicazione della canzone.
     * @return un valore {@code int} che rappresenta l'anno di pubblicazione della canzone.
     */
    public int getAnno(){return this.anno;}

    /***
     * Restituisce una stringa contenente le informazioni relative alla canzone nel formato:<p>
     * Titolo - Autore (Anno)
     * @return un oggetto di tipo {@code String} che contiene le informazioni sulla canzone.
     */
    @Override
    public String toString(){
        return this.titolo + " - " + this.autore + " (" + this.anno + ")";
    }

}