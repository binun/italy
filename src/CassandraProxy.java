

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
    
    public CassandraProxy() {
		super(7000, "system");
		columns = "id int PRIMARY KEY, name text";
	}
    
	@Override
	public boolean connect(String host) {
		if (connected)
			return true;
		
		cluster = Cluster.builder().addContactPoints(host).build();
		session = cluster.connect(this.startDB);
		connected=true;
		return (session!=null);
	}

	@Override
	public boolean createDB(String dbName) {
		Session session = cluster.connect(this.startDB);
		res = false;
		String query = "CREATE KEYSPACE IF NOT EXISTS " +  dbName + " WITH replication " + "= {'class':'SimpleStrategy', 'replication_factor':1};";	
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
		String query= String.format("CREATE TABLE IF NOT EXISTS %s.%s(%s);", dbname, tbName, columns);
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
		Session session = cluster.connect(dbname);
	    
		//String query = String.format("INSERT INTO %s.%s(id,name) VALUES(%s,\'%s\');", dbname,tbname,values[0],values[1]); 
		Statement query= QueryBuilder.insertInto(dbname,tbname).value("id",Integer.valueOf(values[0])).value("name",values[1]).ifNotExists();
	
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
	
	@Override
	public boolean updateTuple(String dbName, String tbName, String id, String name) {
		System.out.println("Updating records into the table...");
		Session session = cluster.connect(dbName);
	    
		//String query = String.format("INSERT INTO %s.%s(id,name) VALUES(%s,\'%s\');", dbname,tbname,values[0],values[1]); 
		Statement query = QueryBuilder.update(dbName,tbName).with(QueryBuilder.set("name", name)).where(QueryBuilder.eq("id",Integer.valueOf(id)));
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


	@Override
	public boolean rmTuple(String dbname, String tbname, String filter) {
		System.out.println("Removing records from the table...");
		Session session = cluster.connect(dbname);
	
		String query = String.format("DELETE FROM %s.%s WHERE id=%s;", dbname,tbname,filter); 
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

	@Override
	public String fetch(String dbname, String tbname) {
		//String query = String.format("select * from %s.%s;",dbname,tbname);
		Session session = cluster.connect(dbname);
		String encoded = "";
        //List<String> res = new ArrayList<String>();
		//String cqlStatement = String.format("SELECT * FROM %s.%s", db,table);
        Statement stmt = QueryBuilder.select().all().from(tbname);
		ResultSet rs = session.execute(stmt);
		//Row row = rs.one();
		List<Row> rows = rs.all();
		
		List<Definition> cd = rows.get(0).getColumnDefinitions().asList();
		for (Row r: rows) {
		  String rowres = r.toString();
		  
	      encoded = encoded + rowres.substring(3, rowres.length()) + ";";
	      //res.add(rowres.substring(4, rowres.length()-1));
	      //System.out.println("");
	      
		}
		
		session.close();
		
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

	@Override
	public boolean deleteDB(String dbName) {
		Session session = cluster.connect(this.startDB);
		res = false;
		String query = "DROP KEYSPACE " +  dbName;	
		try {
			session.execute(query);
			System.out.println("DB deleted " + dbName);
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
