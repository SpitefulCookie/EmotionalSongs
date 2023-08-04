package emotionalsongs;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Manages database queries and connections for the EmotionalSong application.
 *
 * The QueryHandler class facilitates database operations for the EmotionalSong application.
 * It utilizes a PostgreSQL database and manages a single database connection throughout the
 * application's lifecycle, ensuring the Singleton Design Pattern.
 * The class provides methods to interact with the database, such as checking if a username exists,
 * retrieving user passwords, and registering new users.
 *
 * The class also holds essential information for establishing a database connection, including the
 * database name, address (host) and port number. Additionally, it contains constant SQL query strings
 * for specific database operations, such as querying user passwords and checking for existing usernames.
 *
 * The methods in this class are designed to handle potential SQLExceptions and log error messages to
 * the EmotionalSongsServer's main view when database operations encounter exceptions. The class is thread-safe.
 */
public class QueryHandler {

    private static Connection dbConnection;
    private static Statement stmt;

    private static String DB_Name = "EmotionalSong";
    private static String DB_Address = "localhost";
    private static String DB_Port = "5432";

    protected static final String QUERY_USER_PWD = "SELECT Password FROM UtentiRegistrati WHERE Userid = '%s'";
    protected static final String QUERY_USERNAME_EXISTS = "SELECT COUNT(*) FROM utentiregistrati WHERE userid = '%s'";
    protected static final String QUERY_REGISTER_USER = "INSERT INTO UtentiRegistrati (nome, codicefiscale, via, numerocivico, cap, comune, provincia, email, userid, password) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')";
    protected static final String QUERY_SEARCH_SONG_BY_TITLE = "SELECT * FROM Canzoni WHERE Titolo LIKE '#%s#'";
    protected static final String QUERY_USER_PLAYLISTS = "SELECT Nome FROM Playlist WHERE UserId = '%s'";
    protected static final String QUERY_SONGS_IN_PLAYLIST = "SELECT Titolo, Autore, anno, songUUID FROM Canzoni NATURAL JOIN Contiene WHERE Nome = '%s'";
    protected static final String QUERY_REGISTER_PLAYLIST = "INSERT INTO Playlist (Nome, UserId) VALUES ('%s', '%s')";
    protected static final String QUERY_REGISTER_SONG_IN_PLAYLIST = "INSERT INTO Contiene (Nome, UserId, SongUUID) VALUES ('%s', '%s', '%s')";
    protected static final String QUERY_REGISTER_SONG_EMOTION = "INSERT INTO %s (UserId, SongUUID, Punteggio, Note) VALUES ('%s', '%s', '%s', '%s')";

    /**
     * Constructs a new QueryHandler object and establishes a database connection using the provided
     * username and password.
     *
     * This constructor initializes a new instance of the QueryHandler class and establishes a database
     * connection using the provided username and password. The constructor uses the Singleton Design Pattern
     * to ensure that only one instance of the database connection is created throughout the application's
     * lifecycle. If a database connection has not been established yet, the constructor creates a new
     * connection using the PostgreSQL driver and the provided database address, port, and name. It then
     * creates a statement object for executing queries on the database.
     *
     * @param username The username to connect to the database.
     * @param password The password associated with the provided username.
     * @throws SQLException if there is an error during the database connection setup or query statement creation.
     */
    public QueryHandler(String username, String password) throws SQLException{

        // Singleton Design Pattern
        if(dbConnection == null){
            dbConnection = DriverManager.getConnection("jdbc:postgresql://"+ DB_Address+":"+DB_Port+"/"+DB_Name, username, password);
            stmt = dbConnection.createStatement();

        }

    }

    /**
     * Checks if the provided username exists in the database.
     *
     * This method queries the database to determine if the given username exists. It performs a
     * database query using the provided username and checks the result to see if any matching records
     * are found. If a record with the provided username exists, the method returns true; otherwise,
     * it returns false. If there is an error during database query execution, the method returns true
     * to indicate that the username might exist (due to uncertainty) but an error occurred.
     *
     * @param username The username to check for existence in the database.
     * @return true if the username exists in the database or if there has been an error during database query execution, false if it does not exist.
     */
    protected boolean usernameExists(String username) {

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

            e.printStackTrace(); // TODO verify behaviour

            return true;
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
     * @return The password associated with the provided username, or null if the username is not found
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

            e.printStackTrace(); // TODO verify behaviour

            return null;
        }

        return pwdToBeReturned;

    }


    /**
     * Retrieves the name of the PostgreSQL database being used for the connection.
     *
     * This method returns the name of the PostgreSQL database being used for the current connection.
     * The database name is an essential parameter required to establish a connection to the database.
     *
     * @return The name of the PostgreSQL database being used for the connection.
     */
    protected static String getDB_Name(){return DB_Name;}

    /**
     * Retrieves the address (host) of the machine where the PostgreSQL database is hosted.
     *
     * This method returns the address (host) of the machine where the PostgreSQL database is hosted.
     * The database address is an essential parameter required to establish a connection to the database.
     *
     * @return The address (host) of the machine where the PostgreSQL database is hosted.
     */
    protected static String getDB_Address(){return DB_Address;}

    /**
     * Retrieves the port number on which the PostgreSQL database is listening for connections.
     *
     * This method returns the port number on which the PostgreSQL database is listening for connections.
     * The database port number is an essential parameter required to establish a connection to the database.
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

        //System.out.println("db connection set to:" + "jdbc:postgresql://"+ DB_Address+":"+DB_Port+"/"+DB_Name);

    }

    /**
     * Registers a new user in the database with the provided user data.
     *
     * This method allows registering a new user in the database by inserting the provided user data
     * into the appropriate database table. The user data should be provided as an array of strings, where
     * each element contains specific information about the user. The order of the elements in the array
     * should match the expected order for registration (e.g., username, email, full name, etc.).
     * Additionally, the method generates a BCrypt hash of the user's password before storing it in the database.
     *
     * @param data An array of strings containing the user data to be registered. The order of the elements
     *             should be as follows:
     *
     *             data[0] - Nome
     *             data[1] - Codice fiscale
     *             data[2] - Via
     *             data[3] - Numero civico
     *             data[4] - CAP
     *             data[5] - Comune
     *             data[6] - Provincia
     *             data[7] - Email
     *             data[8] - Userid
     *             data[9] - Plain text password to be hashed and stored securely in the database
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

            EmotionalSongsServer.mainView.logError("SQLException thrown while executing function registerUser in QueryHandler.java");

        }

    }

    /*  "HashSet gives you an O(1) contains() method but doesn't preserve order." -https://www.baeldung.com/java-hashset-vs-treeset
        "ArrayList contains() is O(n) but you can control the order of the entries. -https://stackoverflow.com/questions/18706870/java-hashset-vs-array-performance
        Array if you need to insert anything in between, worst case can be O(n), since you will have to move the data down and make room for the insertion.
        In Set, you can directly use SortedSet which too has O(n) too but with flexible operations.
        HashSet provides constant-time performance for most operations like add(), remove() and contains(), versus the log(n) time offered by the TreeSet. "
     */

    /**
     * Executes an SQL query with the provided arguments.<br><br>
     *
     * This method executes an SQL query using the provided query command and arguments. The query command is a
     * parameterized SQL query string that includes placeholders for the arguments. The method replaces the placeholders
     * in the query command with the provided arguments.
     * This method differs from the method executeUpdate for the type of operations it covers.<br>
     * This method is meant to be used to retrieve data from the database.
     *
     * @param args The arguments to be inserted into the parameterized query command.
     * @param queryCommand The parameterized SQL query command to execute.
     */
    public ArrayList<String[]> executeQuery(final String[] args, final String queryCommand){

        ArrayList<String[]> results = new ArrayList<>();

        try {

            ResultSet set = stmt.executeQuery(String.format(queryCommand, (Object[]) args));

            int numCols = set.getMetaData().getColumnCount();
            String[] row = new String[numCols];

            while (set.next()) { // Finché vi sono righe...

                for (var i = 1; i< numCols; i++){ // per ciascuna colonna presente nella tabella,
                    row[i-1] = set.getString(i); // aggiungo la stringa ottenuta al mio array
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
     * This method is meant to be used to insert or update data in the database.
     *
     * @param args The arguments to be inserted into the parameterized query command.
     * @param queryCommand The parameterized SQL query command to execute.
     */
    public void executeUpdate(final String[] args,  final String queryCommand) {

        try {
            stmt.executeUpdate(String.format(queryCommand, (Object[]) args));
        } catch (SQLException e) {

            EmotionalSongsServer.mainView.logError("SQLException thrown while executing update:\n" +String.format(queryCommand, (Object[]) args) + "\nReason: " + e.getMessage());
        }

    }
}
