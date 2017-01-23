import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

// https://github.com/datastax/java-driver/tree/3.x/manual
// https://www.tutorialspoint.com/cassandra/cassandra_read_data.htm

public class CasMain {
	public static void main(String[] args) {
		
		
		String serverIP = "172.17.0.2";
		String keyspace = "system";

		Cluster cluster = Cluster.builder().addContactPoints(serverIP).build();

		Session session = cluster.connect(keyspace);
		
		String cqlStatement = "SELECT * FROM local";
		ResultSet rs = session.execute(cqlStatement);
		//Row row = rs.one();
		System.out.println(rs.all());
		
		//String query = "CREATE KEYSPACE MySpace WITH replication " + "= {'class':'SimpleStrategy', 'replication_factor':1};";	
		//session.execute(query);
		//session.execute("USE MySpace");
		//System.out.println("Keyspace created"); 
		
		Session session1 = cluster.connect("MySpace");
		
		//String cmd= "CREATE TABLE MyTable(id int PRIMARY KEY, name text);";
		//session1.execute(cmd);
		//System.out.println("Table created");
		
		String data1 = "INSERT INTO MySpace.MyTable(id,name) VALUES(1,'ram');";
        session.execute(data1);	        
        System.out.println("Data created");
        
        
		/*
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
        */
	}
}
