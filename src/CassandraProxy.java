
import java.sql.Statement;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AuthenticationException;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class CassandraProxy extends DBProxy {
	
	private Cluster cluster;
	private Session session;
	String hostname = "127.0.0.1";
	
	public CassandraProxy() {
		try {
			Class.forName("org.apache.cassandra.cql.jdbc.CassandraDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		cluster = Cluster.builder().addContactPoint(hostname).build();
	}

	@Override
	public boolean connect(String replicaName) {
		try {
		     session = cluster.connect();
		}
		
		catch (NoHostAvailableException e) {
			return false;
		}
		
		catch (AuthenticationException e) {
			return false;
		}
		
		catch (IllegalStateException e) {
			return false;
		}
		
		return true;
	}

	@Override
	public Object createDB(String dbName) {
		String query = "CREATE KEYSPACE IF NOT EXISTS \""+ dbName +"\"" +
				"WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};";
        return session.execute(query);
	}

	@Override
	public Object createTable(String dbName, String tbName, String columns) {
		
		lastDB = dbName;
		return this.createTable(tbName, columns);
	}

	@Override
	public Object createTable(String tbName, String columns) {
		String query = String.format("CREATE TABLE %s.%s(%s)", (String)lastDB,tbName,columns);
		ResultSet rs = session.execute(query);
		lastTable = (Object)tbName;
		return lastTable;
		
	}

	@Override
	public Object createTable(String tbName) {
		String columnDef = "";
		
		for (int i=0; i < columns.length; i++) {
			columnDef = columnDef + columns[i] + " int";
			if (i!=columns.length-1)
				columnDef = columnDef + ",";
		}
	   		
		return this.createTable(tbName, columnDef);
	}

	@Override
	public void addTuple(String[] values) {
		System.out.println("Inserting records into the table...");
		Statement st = null;
		
		String joined = Utils.join(",", values);
	    String sql = String.format("INSERT INTO %s.%s VALUES (%s)", (String)lastDB,(String)lastTable,joined);
	                  
	    try {
			session.execute(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

	}

	@Override
	public void rmTuple(String filter) {
		System.out.println("Removing records from the table...");
		Statement st = null;
		
		
	    String sql = String.format("DELETE FROM %s.%s WHERE %s", this.lastDB,this.lastTable,filter);
	                  
	    try {
			session.execute(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}
	}

	@Override
	public String getContent(String dbName, String tbName) {
		String query = String.format("select %s from %s.%s;", Utils.join(",",this.columns), dbName,tbName);
		
		String result = "";
		try {
		     
		     ResultSet rs = session.execute(query);
		     List<Row> al = rs.all();
		     for (Row row: al)
		     {
		      int rowSize = row.getColumnDefinitions().size();
		      for (int i=0; i < rowSize; i++)
		         result = result + " " + row.getString(i);
		     }  
		    
		  }
		  catch (Exception e)
		  {
		    System.err.println("Got an exception! ");
		    System.err.println(e.getMessage());
		  }
		 return result;
	}

}
