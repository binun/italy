import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.CodecNotFoundException;
import com.datastax.driver.core.querybuilder.QueryBuilder;

// https://github.com/datastax/java-driver/tree/3.x/manual
// https://www.tutorialspoint.com/cassandra/cassandra_read_data.htm

public class CasMain {
	
	private static String serverIP = "172.17.0.2";
	private static String keyspace = "system";
	private static String table = "local";
	private static Cluster cluster = null;

	public static List<List<String>> fetch(String ip, String db, String table) {
		
		//Cluster cluster = Cluster.builder().addContactPoints(ip).build();
		Session session = cluster.connect(db);
        List<List<String>> res = new ArrayList<List<String>>();
		//String cqlStatement = String.format("SELECT * FROM %s.%s", db,table);
        Statement stmt = QueryBuilder.select().all().from(table);
		ResultSet rs = session.execute(stmt);
		//Row row = rs.one();
		List<Row> rows = rs.all();
		
		List<Definition> cd = rows.get(0).getColumnDefinitions().asList();
		for (Row r: rows) {
		  ArrayList<String> data = new ArrayList<String>();
	      for (Definition d : cd) 
            { 
	    	  String temp = "";
	    	  try {
	    		  temp = r.getString(d.getName());
	    	  }
	    	  catch (CodecNotFoundException e) {
	    		  continue;
	    	  }
	    	  data.add(temp);
	    	  //System.out.print(r.getString(d.getName()) + " "); 
	    	  
	    	}
	      res.add(data);
	      System.out.println("");
		}
		
		session.close();
		//cluster.close();
		
		return res;
	}
	
	public static boolean createDB(String ip, String dbname) {
		//Cluster cluster = Cluster.builder().addContactPoints(ip).build();
		Session session = cluster.connect("system");
		boolean res = false;
		
		String query = "CREATE KEYSPACE " +  dbname + " WITH replication " + "= {'class':'SimpleStrategy', 'replication_factor':1};";	
		try {
			session.execute(query);
			session.execute("USE " + dbname);
			System.out.println("DB created");
			res = true;
		}
		catch (Exception e) {
			res = false;
		}
		finally {
			session.close();
		    //cluster.close();
		}
		return res;
	}
	
	public static boolean createTable(String ip, String dbname, String tbname) {
		//Cluster cluster = Cluster.builder().addContactPoints(ip).build();
		Session session = cluster.connect(dbname);
		boolean res = false;
		
		String query= "CREATE TABLE " + tbname + "(id int PRIMARY KEY, name text);";
		try {
		   session.execute(query);
		   System.out.println("Table created");
		   res = true;
		}
		catch (Exception e) {
			res = false;
		}
		
		finally {
			session.close();
		    //cluster.close();
		}
		return res;	
	}
	
	public static boolean deleteTable(String ip, String dbname, String tbname) {
		//Cluster cluster = Cluster.builder().addContactPoints(ip).build();
		Session session = cluster.connect(dbname);
		boolean res = false;
		
		String query= "DROP TABLE " + tbname + ";";
		try {
		   session.execute(query);
		   System.out.println("Table dropped");
		   res = true;
		}
		catch (Exception e) {
			res = false;
		}
		
		finally {
			session.close();
		    //cluster.close();
		}
		return res;	
	}
	
	public static boolean addRow(String ip, String dbname, String tbname, String [] values) {
		//Cluster cluster = Cluster.builder().addContactPoints(ip).build();
		Session session = cluster.connect(dbname);
		boolean res = false;
	    
		//String query = String.format("INSERT INTO %s.%s(id,name) VALUES(%s,\'%s\');", dbname,tbname,values[0],values[1]); 
		Statement query= QueryBuilder.insertInto(dbname,tbname).value("id",Integer.valueOf(values[0])).value("name",values[1]).ifNotExists();
		session.execute(query);
		try {
		   session.execute(query);
		   System.out.println("Data added");
		   res = true;
		}
		catch (Exception e) {
			res = false;
		}
		
		finally {
			session.close();
		    //cluster.close();
		}
		return res;	
	}
	
	public static boolean updateRow(String ip, String dbname, String tbname, String [] values) {
		//Cluster cluster = Cluster.builder().addContactPoints(ip).build();
		Session session = cluster.connect(dbname);
		boolean res = false;
	
		//String query = String.format("UPDATE %s.%s SET id=%s,name=\'%s\');", dbname,tbname,values[0],values[1]); 
		
		Statement query = QueryBuilder.update(dbname,tbname).with(QueryBuilder.set("name", values[1])).where(QueryBuilder.eq("id", Integer.valueOf(values[0])));
		//session.execute(exampleQuery);
		try {
		   session.execute(query);
		   System.out.println("Data updated");
		   res = true;
		}
		catch (Exception e) {
			res = false;
		}
		
		finally {
			session.close();
		    //cluster.close();
		}
		return res;	
	}
	
	public static boolean deleteRow(String ip, String dbname, String tbname, String key) {
		//Cluster cluster = Cluster.builder().addContactPoints(ip).build();
		Session session = cluster.connect(dbname);
		boolean res = false;
	
		String query = String.format("DELETE FROM %s.%s WHERE id=%s;", dbname,tbname,key); 
		try {
		   session.execute(query);
		   System.out.println("Data deleted");
		   res = true;
		}
		catch (Exception e) {
			res = false;
		}
		
		finally {
			session.close();
		    //cluster.close();
		}
		return res;	
	}
	
	public static void clearAll() {
		String [] res = Utils.execCommand("/clearState.sh");
	}
	
	public static void main(String[] args) {
		
		cluster = Cluster.builder().addContactPoints(serverIP).build();
		List<List<String>> s = fetch(serverIP, keyspace,table);
		for (List<String> g : s)
			System.out.println(g);
		
		boolean b = createDB(serverIP,"MySpace");
		boolean b1 = createTable(serverIP, "MySpace","MyTable");
		String [] row = {"1","nc1"};
		boolean b2 = addRow(serverIP, "MySpace","MyTable", row);
		
		String [] row1 = {"1","nc2"};
		boolean b3 = updateRow(serverIP, "MySpace","MyTable", row1);
		
		boolean b4 = deleteRow(serverIP, "MySpace","MyTable", "2");
		
		boolean b5 = deleteTable(serverIP, "MySpace","MyTable");
		
		//Cluster cluster = Cluster.builder().addContactPoints(serverIP).build();

		//Session session = cluster.connect(keyspace);
		
		// cqlStatement = "SELECT * FROM local";
		// rs = session.execute(cqlStatement);
		//Row row = rs.one();
		//System.out.println(rs.all());
		
		//String query = "CREATE KEYSPACE MySpace WITH replication " + "= {'class':'SimpleStrategy', 'replication_factor':1};";	
		//session.execute(query);
		//session.execute("USE MySpace");
		//System.out.println("Keyspace created"); 
		
		//Session session1 = cluster.connect("MySpace");
		
		//String cmd= "CREATE TABLE MyTable(id int PRIMARY KEY, name text);";
		//session1.execute(cmd);
		//System.out.println("Table created");
		
		//String data1 = "INSERT INTO MySpace.MyTable(id,name) VALUES(1,'ram');";
        //session.execute(data1);	        
       // System.out.println("Data created");
        
        
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
