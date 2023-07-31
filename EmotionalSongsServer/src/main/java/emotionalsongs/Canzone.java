package emotionalsongs;

/*
 * Progetto svolto da:
 * 
 * Della Chiesa Mattia 749904, Ateneo di Varese
 * 
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
public class Canzone extends UnicastRemoteObject {

    private String titolo;
    private String songUUID;
    private String autore;
    private int anno;

    /***
     * Costruisce un oggetto {@code Canzone} a partire dai singoli dati che la descrivono.
     * @param titolo un oggetto di tipo {@code String} che rappresenta il titolo della canzone.
     * @param autore un oggetto di tipo {@code String} che rappresenta l'autore della canzone.
     * @param anno un valore {@code int} che indica l'anno di pubblicazione della canzone.
     * @param uuid un oggetto di tipo {@code String} che rappresenta un UUID di tipo 3.
     * 
     * @see <a href = https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html#nameUUIDFromBytes(byte[])>UUID.nameUUIDFromBytes()</a>
     * 
     */
    public Canzone(String titolo, String autore, int anno, String uuid) throws RemoteException {

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
    public Canzone(String dati) throws RemoteException{

        super();

        String[] val;

        if(dati.contains("\"")){

            String result = dati;
            String value;

            int begin;
            int last=-1;

            do{

                begin = dati.indexOf("\"", last+1);

                if(begin!=-1){

                    last = dati.indexOf('\"', begin+1);

                    if(last!=-1){

                        value = dati.substring(begin+1, last).replace(',', '§');
                        
                        result = result.replace(dati.substring(begin, last+1), value);

                    }

                }

            } while (last != -1 && begin != -1);
                        
            val = result.split(",");

            for(int i=0; i<val.length; i++){val[i] = val[i].replace("§", ",");}


        } else{val = dati.split(",");}

        this.songUUID = val[0];
        this.titolo = val[1];
        this.autore = val[2];
        this.anno = Integer.parseInt(val[3]);

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