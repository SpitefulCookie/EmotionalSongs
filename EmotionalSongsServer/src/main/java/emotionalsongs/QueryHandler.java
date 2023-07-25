package emotionalsongs;

import java.sql.*;

/**
 * TODO Document
 */
public class QueryHandler {

    private static Connection dbConnection;
    private static Statement stmt;

    private static String DB_Name = "EmotionalSong";
    private static String DB_Address = "localhost";
    private static String DB_Port = "5432";

    /**
     * TODO Document
     * @param username
     * @param password
     * @throws SQLException
     */
    public QueryHandler(String username, String password) throws SQLException{

        // Singleton Design Pattern
        if(dbConnection == null){
            dbConnection = DriverManager.getConnection("jdbc:postgresql://"+ DB_Address+":"+DB_Port+"/"+DB_Name,username,password);
            stmt = dbConnection.createStatement();
        }
    }

    /**
     * TODO Document
     * @return
     */
    protected static String getDB_Name(){return DB_Name;}

    /**
     * TODO Document
     * @return
     */
    protected static String getDB_Address(){return DB_Address;}

    /**
     * TODO Document
     * @return
     */
    protected static String getDB_Port(){return DB_Port;}

    /**
     * TODO Document
     * @param dbName
     * @param dbAddress
     * @param dbPort
     */
    protected static void setDBConnection(String dbName, String dbAddress, String dbPort){

        DB_Name = dbName;
        DB_Address = dbAddress;
        DB_Port = dbPort;

        System.out.println("db connection set to:" + "jdbc:postgresql://"+ DB_Address+":"+DB_Port+"/"+DB_Name);

    }

    // TODO remove me
    public static void testDb(){

        String sql = "SELECT * FROM canzoni where songuuid = 'TRMYDFV128F42511FC'";
        System.out.println("Esecuzione della query: " + sql);

        try {
            ResultSet rs = stmt.executeQuery(sql);

            ResultSetMetaData rsMd = rs.getMetaData(); // Restituisci i metadati del ResultSet (ad es. nome e numero delle colonne etc...)

            System.out.print("\n| ");
            for(var i = 0; i<rsMd.getColumnCount(); i++){
                System.out.print(rsMd.getColumnName(i+1)+ " | ");
            }

            while(rs.next()){ // Viene effettuata un'elaborazione dei dati per ogni riga della relazione ottenuta

                System.out.print("\n| ");

                for(var j = 0; j<rsMd.getColumnCount(); j++){

                    // Computazione del risultato (in questo caso un banale print a schermo)
                    System.out.print(rs.getString(j+1)+ " | ");

                }

            }

            System.out.println();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
