package emotionalsongs;

/**
 * La classe {@code Utente} rappresenta le informazioni relative ad un utente della piattaforma.
 * <p>Ogni utente viene identificato da i seguenti dati:
 *  <ul>
 *  <li> Nominativo
 *  <li> Codice fiscale
 *  <li> Indirizzo
 *  <li> Email
 *  <li> UserId
 *  <li> Password
 *  </ul>
 * 
 *  @see Indirizzo
 *  @author <a href="https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
 *  
 */
public class Utente {

    private String nominativo;
    private String codiceFiscale;
    private Indirizzo indirizzo;
    private String email;
    private String userId; 
    private String password;


    /***
     * Costruisce un oggetto {@code Utente} a partire dai parametri forniti.
     * @param nominativo un oggetto di tipo {@code String} dove è contenuto il nome e cognome dell'utente.
     * @param codFiscale un oggetto di tipo {@code String} che rappresenta il codice fiscale dell'utente.
     * @param indirizzo un'array bidimensionale di tipo {@code String} contenente l'indirizzo toponomastico dell'utente.
     * @param email un oggetto di tipo {@code String} che rappresenta l'indirizzo email dell'utente.
     * @param userId un oggetto di tipo {@code String} che rappresenta l'identificativo univoco dell'utente.
     * @param password un oggetto di tipo {@code String} che rappresenta la password dell'utente.
     */
    public Utente(String nominativo, String codFiscale, String[] indirizzo, String email, String userId, String password) {
        
        this.nominativo = nominativo;
        this.codiceFiscale = codFiscale;
        this.indirizzo = new Indirizzo(indirizzo);
        this.email = email;
        this.userId = userId;
        this.password = password;

    }

    /**
     * Crea un nuovo oggetto di tipo {@code Utente} in cui tutti i campi sono vuoti. // TODO verificare che serva a qualcosa nel lab B
     */
    public Utente(){

        this.nominativo = null;
        this.codiceFiscale = null;
        this.indirizzo = null;
        this.email = null;
        this.userId = null;
        this.password = null;

    }

    /**
     * Crea un nuovo oggetto di tipo {@code Utente} a partire dai dati forniti in input.
     * @param nominativo un oggetto di tipo {@code String} dove è contenuto il nome e cognome dell'utente.
     * @param codFiscale un oggetto di tipo {@code String} che rappresenta il codice fiscale dell'utente.
     * @param indirizzo un'array bidimensionale di tipo {@code String} contenente l'indirizzo toponomastico dell'utente.
     * @param email un oggetto di tipo {@code String} che rappresenta l'indirizzo email dell'utente.
     * @param userId un oggetto di tipo {@code String} che rappresenta l'identificativo univoco dell'utente.
     * @param password un oggetto di tipo {@code String} che rappresenta la password dell'utente.
     */
    public Utente(String nominativo, String codFiscale, Indirizzo indirizzo, String email, String userId, String password){
        
        this.nominativo = nominativo;
        this.codiceFiscale = codFiscale;
        this.indirizzo = indirizzo;
        this.email = email;
        this.userId = userId;
        this.password = password;

    }

    /***
     * Restituisce una stringa contenente il nominativo dell'utente
     * @return un oggetto di tipo {@code String} contenente il nome dell'utente
     */

    public String getNominativo(){return this.nominativo;}

    /***
     * Restituisce una stringa contenente il codice fiscale dell'utente
     * @return un oggetto di tipo {@code String} contenente il codice ficale dell'utente
     */
    public String getCodiceFiscale(){return this.codiceFiscale;}

    /***
     * Restituisce l'indirizzo toponomastico dell'utente
     * @return un oggetto di tipo {@code Indirizzo} contenente l'indirizzo toponomastico dell'utente suddiviso nelle sue componenti atomiche
     */
    public Indirizzo getIndirizzo(){return this.indirizzo;}

    /***
     * Restituisce l'indirizzo di posta elettronica dell'utente
     * @return un oggetto di tipo {@code String} contenente l'email dell'utente
     */
    public String getEmail(){return this.email;}

    /***
     * Restituisce l'userID dell'utente
     * @return un oggetto di tipo {@code String} contenente l'id utente
     */
    public String getUserId(){return this.userId;}

    /***
     * Restituisce la password dell'utente
     * @return un oggetto di tipo {@code String} contenente la password dell'utente
     */
    public String getPassword(){return this.password;}

    /***
     * Setta il nominativo dell'utente
     * @param nominativo un oggetto di tipo {@code String} contenente il nome e cognome dell'utente
     */
    public void setNominativo(String nominativo){this.nominativo = nominativo;}

    /***
     * Setta il codice fiscale dell'utente
     * @param codiceFiscale un oggetto di tipo {@code String} contenente il codice fiscale dell'utente
     */
    public void setCodiceFiscale(String codiceFiscale){this.codiceFiscale = codiceFiscale;}

    /***
     * Setta l'indirizzo toponomastico dell'utente
     * @param indirizzo un oggetto di tipo {@code Indirizzo} contenente l'indirizzo toponomastico dell'utente
     */
    public void setIndirizzo(Indirizzo indirizzo){this.indirizzo = indirizzo;}

    /***
     * Setta l'indirizzo di posta elettronica dell'utente
     * @param email un oggetto di tipo {@code String} contenente l'indirizzo email dell'utente
     */
    public void setEmail(String email){this.email = email;}

    /***
     * Setta l'userID dell'utente
     * @param userId un oggetto di tipo {@code String} contenente l'id utente
     */
    public void setUserId(String userId){this.userId = userId;}

    /***
     * Setta la password dell'utente
     * @param password un oggetto di tipo {@code String} contenente la password dell'utente
     */  
    public void setPassword(String password){this.password = password;}

    /***
     * Restituisce i dati dell'utente
     * @return un oggetto {@code String} contenente tutti i dati dell'utente
     */ 


    /**
     * Restituisce un oggetto di tipo {@code String} contenente tutti i dati dell'utente in formato CSV
     * @return un oggetto di tipo {@code String} contenente tutti i dati dell'utente in formato CSV
     */
    public String toCSV() {
        return this.nominativo + "," 
        + this.codiceFiscale + ","
        + this.indirizzo.formatForCSV() + "," 
        + this.email + "," 
        + this.userId + "," 
        + this.password;
    }

}

/**
     * La classe {@code Indirizzo} rappresenta un indirizzo toponomastico suddiviso nelle sue componenti atomiche quali:<p>
     *<ul>
    * <li> Via
    * <li> Numero
    * <li> CAP
    * <li> Città
    * <li> Provincia

    * </ul>
    * <p>
    * Autore: @author  <a href=https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
    */

class Indirizzo{

    private String via;
    private String numeroCivico;
    private String cap;
    private String citta;
    private String provincia;

    /***
     * Costruisce un oggetto {@code Indirizzo} a partire da un array di tipo {@code String}.
     * @param indirizzo un array di tipo {@code String} che rappresenta un indirizzo toponomastico
     */
    public Indirizzo(String[] indirizzo) {

        this.via = indirizzo[0];
        this.numeroCivico = indirizzo[1];
        this.cap = indirizzo[2];
        this.citta = indirizzo[3];
        this.provincia = indirizzo[4];
        
    }

    /***
     * Restituisce il campo via dell'indirizzo
     * @return un oggetto di tipo {@code String} contenente la via
     */
    public String getVia() {return this.via;}

    /***
     * Restituisce il campo CAP dell'indirizzo
     * @return un oggetto di tipo {@code String} contenente il CAP
     */
    public String getCap() {return this.cap;}

    /***
     * Restituisce il campo Città dell'indirizzo
     * @return un oggetto di tipo {@code String} contenente La città
     */
    public String getCitta(){return this.citta;}

    /***
     * Restituisce il campo provincia dell'indirizzo
     * @return un oggetto di tipo {@code String} contenente la provincia
     */
    public String getProvincia(){return this.provincia;}

    /***
     * Restituisce il campo numero civico dell'indirizzo
     * @return un oggetto di tipo {@code String} rappresentante il numero civico
     */
    public String getNumeroCivico(){return this.numeroCivico;}

    /***
     * Restituisce le informazioni sull'indirizzo
     * @return un oggetto {@code String} contenente tutti i dati relativi all'indirizzo toponomastico.
     */
    @Override
    public String toString(){
        return this.via + " " + this.numeroCivico + ", " + this.cap + " " + this.citta + " (" + this.provincia + ")";
    }

    /***
     * Formatta le informazioni relative all'utente in un formato conforme allo standard CSV.
     * @return un oggetto {@code String} racchiuso tra doppi apici e contenente tutti i dati relativi all'utente separati da una virgola.
     */
    public String formatForCSV(){
        return "\""+this.via + "," + this.numeroCivico + "," + this.cap + "," + this.citta + "," + this.provincia+"\"";
    }

}

