

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.CodecNotFoundException;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class CassandraProxy extends DBProxy {
	
	private Cluster cluster;
	private Session session;
	private boolean connected = false;
    
    public CassandraProxy() {
		super(7000, "system");
		columns = "id int PRIMARY KEY, name text";
	}
    
	@Override
	public boolean connect(String host) {
		if (connected)
			return true;
		
		session = cluster.connect();
		
		return (session!=null);
	}

	@Override
	public boolean createDB(String dbName) {
		Session session = cluster.connect(this.startDB);
		res = false;
		String query = "CREATE KEYSPACE " +  dbName + " WITH replication " + "= {'class':'SimpleStrategy', 'replication_factor':1};";	
		try {
			session.execute(query);
			session.execute("USE " + dbName);
			System.out.println("DB created");
			res = true;
		}
		catch (Exception e) {
	
		}
		finally {
			session.close();
		    //cluster.close();
		}

		return res;
	}

	@Override
	public boolean createTable(String dbname,String tbName) {
		Session session = cluster.connect(dbname);
		String query= String.format("CREATE TABLE %s(%s);", tbName, columns);
		res = false;
		try {
		   session.execute(query);
		   System.out.println("Table created");
		   res = true;
		}
		catch (Exception e) {

		}
		
		finally {
			session.close();
		    //cluster.close();
		}
		return res;
		
	}

	@Override
	public boolean addTuple(String dbname, String tbname, String [] values) {
		System.out.println("Inserting records into the table...");
		String joined = Utils.join(",", values);
	    String sql = String.format("INSERT INTO %s.%s VALUES (%s)", dbname,tbname,joined);
	    res = false;               
	    try {
			session.execute(sql);
			res = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		
		}
      return res;
	}

	@Override
	public boolean rmTuple(String dbname, String tbname, String filter) {
		System.out.println("Removing records from the table...");
		String sql = String.format("DELETE FROM %s.%s WHERE id=%s", dbname,tbname,filter);
	    res = false;             
	    try {
			session.execute(sql);
		    return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	   
	    return res;
	}

	@Override
	public String fetch(String dbname, String tbname) {
		//String query = String.format("select * from %s.%s;",dbname,tbname);
		Session session = cluster.connect(dbname);
        List<List<String>> res = new ArrayList<List<String>>();
		//String cqlStatement = String.format("SELECT * FROM %s.%s", db,table);
        Statement stmt = QueryBuilder.select().all().from(tbname);
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
		
		//return res;
		String encoded = "";
		for (List<String> row : res) {
			for (String value: row)
				encoded = encoded + value + " ";
			encoded = encoded + ";\n";
		}
		return encoded;	
	}

	@Override
	public boolean disconnect() {
		cluster.close();
		return true;
		
	}
	@Override
	public boolean deleteTable(String dbname,String tbname) {
		Session session = cluster.connect(dbname);
		String query= "DROP TABLE " + tbname + ";";
		res = false;
		try {
		   session.execute(query);
		   System.out.println("Table dropped");
		   res = true;
		}
		catch (Exception e) {

		}
		
		finally {
			session.close();
		    //cluster.close();
		}

		return res;	
	}

}
