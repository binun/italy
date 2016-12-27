import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;


public class CasMain {
	public static void main(String[] args) {
		String serverIP = "127.0.0.1";
		String keyspace = "system";

		Cluster cluster = Cluster.builder()
		  .addContactPoints(serverIP)
		  .build();

		Session session = cluster.connect(keyspace);
		
		String cqlStatement = "SELECT * FROM local";
		ResultSet x = session.execute(cqlStatement);
		
		
		String cqlStatementC = "INSERT INTO exampkeyspace.users (username, password) " + 
                "VALUES ('Serenity', 'fa3dfQefx')";

        String cqlStatementU = "UPDATE exampkeyspace.users" +
                "SET password = 'zzaEcvAf32hla'," +
                "WHERE username = 'Serenity';";

        String cqlStatementD = "DELETE FROM exampkeyspace.users " + 
                "WHERE username = 'Serenity';";

        session.execute(cqlStatementC); // interchangeable, put any of the statements u wish.
        
        String cqlStatement1 = "CREATE KEYSPACE myfirstcassandradb WITH " + 
        		  "replication = {'class':'SimpleStrategy','replication_factor':1}";
        session.execute(cqlStatement1);
        
     // based on the above keyspace, we would change the cluster and session as follows:
        
        //Session session = cluster.connect("myfirstcassandradb");

        String cqlStatement11 = "CREATE TABLE users (" + 
                              " user_name varchar PRIMARY KEY," + 
                              " password varchar " + 
                              ");";

        session.execute(cqlStatement11);
	}
}
