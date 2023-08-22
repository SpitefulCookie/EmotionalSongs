package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import java.sql.*;
import java.util.ArrayList;

/**
 * Manages database queries and connections for the EmotionalSong application.
 *
 * The QueryHandler class facilitates database operations for the EmotionalSong application.
 * It utilizes a PostgreSQL database and manages a single database connection throughout the
 * application's lifecycle, ensuring the Singleton Design Pattern.
 *
 * The class holds essential information for establishing a database connection, including the
 * database name, address (host) and port number. Additionally, it contains the SQL queries for specific database
 * operations.
 *
 * The methods in this class are designed to handle potential SQLExceptions and log error messages to
 * the EmotionalSongsServer's main view when database operations encounter exceptions. The class is thread-safe.
 *
 * @author <a href="https://github.com/SpitefulCookie"> Della Chiesa Mattia</a>
 */
public class QueryHandler {

    private static Connection dbConnection;
    private static Statement stmt;

    private static String DB_Name = "EmotionalSong";
    private static String DB_Address = "localhost";
    private static String DB_Port = "5432";

    protected static final String QUERY_USER_PWD = "SELECT Password FROM UtentiRegistrati WHERE Userid = '%s'";
    protected static final String QUERY_USERNAME_EXISTS = "SELECT COUNT(*) FROM utentiregistrati WHERE userid = '%s'";
    protected static final String QUERY_CF_EXISTS = "SELECT COUNT(*) FROM utentiregistrati WHERE codicefiscale = '%s'";
    protected static final String QUERY_REGISTER_USER = "INSERT INTO UtentiRegistrati (nome, codicefiscale, via, numerocivico, cap, comune, provincia, email, userid, password) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')";
    protected static final String QUERY_SEARCH_SONG_BY_TITLE = "SELECT * FROM Canzoni WHERE Titolo LIKE '#%s#' LIMIT 25";
    protected static final String QUERY_SEARCH_SONG_BY_AUTHOR_AND_YEAR = "SELECT * FROM Canzoni WHERE autore = '%s' AND anno = %s LIMIT 25";
    protected static final String QUERY_USER_PLAYLISTS = "SELECT Nome FROM Playlist WHERE UserId = '%s' LIMIT 50";
    protected static final String QUERY_SONGS_IN_PLAYLIST = "SELECT Titolo, Autore, anno, songUUID FROM Canzoni NATURAL JOIN Contiene WHERE Nome = '%s' AND UserId = '%s'";
    protected static final String QUERY_REGISTER_PLAYLIST = "INSERT INTO Playlist (Nome, UserId) VALUES ('%s', '%s')";
    protected static final String QUERY_REGISTER_SONG_IN_PLAYLIST = "INSERT INTO Contiene (Nome, UserId, SongUUID) VALUES ('%s', '%s', '%s')";
    protected static final String QUERY_REGISTER_SONG_EMOTION = "INSERT INTO %s (UserId, SongUUID, Punteggio, Note) VALUES ('%s', '%s', '%s', '%s')";
    protected static final String QUERY_GET_NUMBER_OF_FEEDBACK = "SELECT COUNT(*) FROM AMAZEMENT WHERE songuuid = '%s'";
    protected static final String QUERY_GET_SONG_EMOTIONS =
            "SELECT punteggio, note FROM amazement \n" +
                "WHERE userid = 'uId' AND songuuid = 'sId' UNION ALL(\n" +
            "SELECT punteggio, note FROM solemnity \n" +
                "WHERE userid = 'uId' AND songuuid = 'sId' UNION ALL(\n" +
            "SELECT punteggio, note FROM tenderness \n" +
                "WHERE userid = 'uId' AND songuuid = 'sId' UNION ALL(\n" +
            "SELECT punteggio, note FROM nostalgia \n" +
                "WHERE userid = 'uId' AND songuuid = 'sId' UNION ALL(\n" +
            "SELECT punteggio, note FROM calmness \n" +
                "WHERE userid = 'uId' AND songuuid = 'sId' UNION ALL(\n" +
            "SELECT punteggio, note FROM power \n" +
                "WHERE userid = 'uId' AND songuuid = 'sId' UNION ALL(\n" +
            "SELECT punteggio, note FROM joy \n" +
                "WHERE userid = 'uId' AND songuuid = 'sId' UNION ALL(\n" +
            "SELECT punteggio, note FROM tension \n" +
                "WHERE userid = 'uId' AND songuuid = 'sId' UNION ALL(\n" +
            "SELECT punteggio, note FROM sadness \n" +
                "WHERE userid = 'uId' AND songuuid = 'sId'\n" +
            "))))))));\n";

    protected static final String QUERY_GET_SONG_AVERAGE_SCORES =
        "SELECT AVG(punteggio) FROM amazement \n" +
                "WHERE songuuid = '%1$s' UNION ALL(\n" +
            "SELECT AVG(punteggio) FROM solemnity \n" +
                "WHERE songuuid = '%1$s' UNION ALL(\n" +
            "SELECT AVG(punteggio) FROM tenderness \n" +
                "WHERE songuuid = '%1$s' UNION ALL(\n" +
            "SELECT AVG(punteggio) FROM nostalgia \n" +
                "WHERE songuuid = '%1$s' UNION ALL(\n" +
            "SELECT AVG(punteggio) FROM calmness \n" +
                "WHERE songuuid = '%1$s' UNION ALL(\n" +
            "SELECT AVG(punteggio) FROM power \n" +
                "WHERE songuuid = '%1$s' UNION ALL(\n" +
            "SELECT AVG(punteggio) FROM joy \n" +
                "WHERE songuuid = '%1$s' UNION ALL(\n" +
            "SELECT AVG(punteggio) FROM tension \n" +
                "WHERE songuuid = '%1$s' UNION ALL(\n" +
            "SELECT AVG(punteggio) FROM sadness \n" +
                "WHERE songuuid = '%1$s'\n" +
            "))))))));\n";

    /**
     * Constructs a new QueryHandler object and establishes a database connection using the provided
     * username and password.
     *
     * This constructor initializes a new instance of the QueryHandler class and connects to the database
     * using the provided username and password. If a database connection has not been established
     * yet, the constructor creates a new connection using the PostgreSQL driver and the provided database address, port, and name.
     * The credentials used to authenticate the user are defined by PostgreSQL's roles.
     *
     * @param username The username to connect to the database.
     * @param password The password associated with the provided username.
     * @throws SQLException if there is an error during the database connection setup or query statement creation.
     */
    public QueryHandler(String username, String password) throws SQLException{

        // Singleton Design Pattern
        if (dbConnection == null) {
            dbConnection = DriverManager.getConnection("jdbc:postgresql://" + DB_Address + ":" + DB_Port + "/" + DB_Name, username, password);
            stmt = dbConnection.createStatement();

            }

    }

    /**
     * Checks if the provided username exists in the database.<br><br>
     *
     * This method queries the database to determine if the given username exists. It performs a
     * database query using the provided username and checks the result to see if any matching records
     * are found. If a record with the provided username exists, the method returns {@code true}; otherwise,
     * it returns {@code false}.<br><br>
     * As a fail-safe mechanism, if an error occurs while executing a query, the method throws a
     * {@link UsernameNotVerifiedException} to indicate that the server was unable to determine the provided username's availability. <br><br>
     *
     * @param username The username to check for existence in the database.
     * @return {@code true} if the username exists in the database {@code false} if it does not exist.
     * @throws UsernameNotVerifiedException if an error occurs while attempting to determine the username's availability.
     */
    protected boolean usernameExists(String username) throws UsernameNotVerifiedException {

        boolean usernameTaken = false;

        try {

            ResultSet set = stmt.executeQuery(String.format(QUERY_USERNAME_EXISTS, username));

            while (set.next()) {
                if (set.getString("count").equals("1")) {
                    usernameTaken = true;
                }
            }

            set.close();

            return usernameTaken;


        } catch (SQLException e) {

            EmotionalSongsServer.mainView.logError("SQLException thrown while trying to verify username availability.\nReason: " + e.getMessage());

            throw new UsernameNotVerifiedException();

        }

    }

    /**
     * Retrieves the password associated with the given username from the database.
     *
     * This method queries the database using the provided username to fetch the corresponding password.
     * If the username exists in the database, the method returns the associated password. If the username
     * does not exist or if there is an error while executing the database query, it returns null.
     *
     * @param username The username for which the password is to be retrieved.
     * @return The password associated with the provided username, or {@code null} if the username is not found
     *         or if there is an error during database query execution.
     */
    protected String queryUserPassword(String username) {

        String pwdToBeReturned = null;

        try {

            ResultSet set = stmt.executeQuery(String.format(QUERY_USER_PWD, username));

            while (set.next()) {
                pwdToBeReturned = set.getString(1);
            }

            set.close();

        } catch (SQLException e) {

            EmotionalSongsServer.mainView.logError("SQLException thrown while trying to query user password.\nReason: " + e.getMessage());

            return null;
        }

        return pwdToBeReturned;

    }


    /**
     * Retrieves the name of the PostgreSQL database being used for the connection.
     *
     * This method returns the name of the PostgreSQL database being used for the current connection.
     *
     * @return The name of the PostgreSQL database being used for the connection.
     */
    protected static String getDB_Name(){return DB_Name;}

    /**
     * Retrieves the address (host) of the machine where the PostgreSQL database is hosted.
     *
     * This method returns the address (host) of the machine where the PostgreSQL database is hosted.
     *
     * @return The address (host) of the machine where the PostgreSQL database is hosted.
     */
    protected static String getDB_Address(){return DB_Address;}

    /**
     * Retrieves the port number on which the PostgreSQL database is listening for connections.
     *
     * This method returns the port number on which the PostgreSQL database is listening for connections.
     *
     * @return The port number on which the PostgreSQL database is listening for connections.
     */
    protected static String getDB_Port(){return DB_Port;}

    /**
     * Sets the database connection parameters for establishing a connection to the PostgreSQL database.
     *
     * This method allows setting the database connection parameters, including the database name, database
     * address, and database port. These parameters are used to create the URL for connecting to the PostgreSQL
     * database.
     *
     * @param dbName    The name of the PostgreSQL database to connect to.
     * @param dbAddress The address of the machine where the PostgreSQL database is hosted.
     * @param dbPort    The port number on which the PostgreSQL database is listening for connections.
     */
    protected synchronized static void setDBConnection(String dbName, String dbAddress, String dbPort){

        DB_Name = dbName;
        DB_Address = dbAddress;
        DB_Port = dbPort;

    }

    /**
     * Registers a new user in the database with the provided user data.
     *
     * This method allows the registration of a new user in the database.
     * The user data should be provided as an array of strings, where each element contains specific information
     * about the user.
     * Additionally, the method generates a BCrypt hash of the user's password before storing it in the database.
     *
     * @param data An array of strings containing the user data to be registered. The order of the elements
     *             should be as follows:
     *
     *             data[0] - Nome<br>
     *             data[1] - Codice fiscale<br>
     *             data[2] - Via<br>
     *             data[3] - Numero civico<br>
     *             data[4] - CAP<br>
     *             data[5] - Comune<br>
     *             data[6] - Provincia<br>
     *             data[7] - Email<br>
     *             data[8] - Userid<br>
     *             data[9] - Plain text password to be hashed and stored securely in the database<br>
     *
     */
    public void registerUser(String[] data) {

        try {

            stmt.executeUpdate(
                    String.format(QUERY_REGISTER_USER,
                    data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],
                    AuthManagerImpl.BCryptHashPassword(data[9]))
            );

        } catch (SQLException e) {

            EmotionalSongsServer.mainView.logError("SQLException thrown while executing function registerUser in QueryHandler.java\nReason: " + e.getMessage());

        }

    }

    /**
     * Executes an SQL query with the provided arguments.<br><br>
     *
     * This method executes an SQL query using the provided query command and arguments. The query command is a
     * parameterized SQL query string that includes placeholders for the arguments. The method replaces the placeholders
     * in the query command with the provided arguments.
     * This method differs from the method executeUpdate for the type of operations it covers.<br>
     * This method is meant to be used exclusively to retrieve data from the database.
     *
     * @param args The arguments to be inserted into the parameterized query command.
     * @param queryCommand The parameterized SQL query command to execute.
     */
    public synchronized ArrayList<String[]> executeQuery(final String[] args, final String queryCommand){

        ArrayList<String[]> results = new ArrayList<>();

        try {
            ResultSet set;
            if(args.length !=0) {
                set = stmt.executeQuery(String.format(queryCommand, (Object[]) args));
            } else{
                // Ramo else aggiunto per semplificare le operazioni nella query QUERY_GET_SONG_EMOTIONS dove vi sono 2 parametri e 18 placeholders.
                // Nel caso di questa query la sostituzione dei placeholder con i parametri viene effettuata esternamente mediante il metodo replace() della classe String
                set = stmt.executeQuery(queryCommand);
            }

            int numCols = set.getMetaData().getColumnCount();

            while (set.next()) { // Finché vi sono righe...

                String[] row = new String[numCols];

                for (var i = 0; i<numCols; i++){ // per ciascuna colonna presente nella tabella,
                    row[i] = set.getString(i+1); // aggiungo la stringa ottenuta al mio array
                } // una volta ottenuto tutti i valori delle colonne

                results.add(row); // aggiungo la mia riga ai risultati ottenuti e...
            }

            set.close();

            return results; // restituisco i risultati

        } catch (SQLException e) {

            EmotionalSongsServer.mainView.logError("SQLException thrown while executing query:\n" +String.format(queryCommand, (Object[]) args) + "\nReason: " + e.getMessage());

            return null;

        }

    }

    /**
     * Executes an SQL update query with the provided arguments.<br><br>
     *
     * This method executes an SQL update query using the provided query command and arguments. The query command is a
     * parameterized SQL query string that includes placeholders for the arguments. The method replaces the placeholders
     * in the query command with the provided arguments.<br>
     * This method differs from the method executeQuery for the type of operations it covers.<br>
     * This method is meant to be used to exclusively insert or update data in the database.
     *
     * @param args The arguments to be inserted into the parameterized query command.
     * @param queryCommand The parameterized SQL query command to execute.
     */
    public synchronized void executeUpdate(final String[] args,  final String queryCommand) throws SQLException{
        try {
            stmt.executeUpdate(String.format(queryCommand, (Object[]) args));
        } catch (SQLException e) {
            EmotionalSongsServer.mainView.logError("SQLException thrown while executing update:\n" +String.format(queryCommand, (Object[]) args) + "\nReason: " + e.getMessage());
            throw e;
        }

    }

}
