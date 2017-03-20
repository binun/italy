

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolVersion;
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
	private String columnDef;
	
	private String shellExec(String command) {
		
		return "cqlsh -e " + '"'+ command + '"';
	}
    
    public CassandraProxy() {
		super(7000, "system");
		columnDef = this.colIDs[0] + " int PRIMARY KEY," + this.colIDs[1] + " text";
	}
    
    public String toString() {
		return "CassandraProxy "; 
	}
    
	@Override
	public boolean connect(String host) {	
    	connected=true;
		return connected;
	}

	@Override
	public boolean createDB(String dbName) {
		
		String query = "CREATE KEYSPACE IF NOT EXISTS " +  dbName + " WITH replication " + "= {'class':'SimpleStrategy', 'replication_factor':1};";	
		
        String cmdres = Utils.execCommand(shellExec(query));
        String remain = cmdres.replaceAll("\\s+","");
        
		return (remain.length()<2);
	}

	@Override
	public boolean createTable(String dbname,String tbName) {
		
		String query= String.format("CREATE TABLE IF NOT EXISTS %s.%s(%s);", dbname, tbName, columnDef);
		
		String cmdres = Utils.execCommand(shellExec(query));
        String remain = cmdres.replaceAll("\\s+","");
        
		return (remain.length()<2);
	}

	@Override
	public boolean addTuple(String dbname, String tbname, String [] values) {
		System.out.println("Inserting records into the table...");
		String query= String.format("INSERT INTO %s.%s(%s,%s) VALUES(%s,%s)", 
				   dbname, tbname, 
				   colIDs[0],colIDs[1],
				   values[0], "'"+values[1]+"'");
		
		String cmdres = Utils.execCommand(shellExec(query));
        String remain = cmdres.replaceAll("\\s+","");
        
		return (remain.length()<2);
	}
	
	@Override
	public boolean updateTuple(String dbname, String tbname, String [] values) {
		System.out.println("Updating records into the table...");
		String query= String.format("UPDATE %s.%s SET name=\'%s\' WHERE id=%s;", 
				   dbname, tbname, values[0],values[1]);
		
		String cmdres = Utils.execCommand(shellExec(query));
        String remain = cmdres.replaceAll("\\s+","");
        
		return (remain.length()<2);
	}


	@Override
	public boolean rmTuple(String dbname, String tbname, String id) {
		System.out.println("Deleting records in the table...");
		String query= String.format("DELETE FROM %s.%s WHERE id=%s;", 
				   dbname, tbname, id);
		
		String cmdres = Utils.execCommand(shellExec(query));
        String remain = cmdres.replaceAll("\\s+","");
        
		return (remain.length()<2);
	}

	@Override
	public String fetch(String dbname, String tbname) {
		System.out.println("Retreving records from the table...");
		String query= String.format("SELECT * FROM %s.%s;", 
				   dbname, tbname);
		
		String cmdres = Utils.execCommand(shellExec(query));
        
		return cmdres;
	}

	@Override
	public boolean disconnect() {
		//cluster.close();
		return true;
		
	}
	@Override
	public boolean deleteTable(String dbname,String tbname) {

		String query= "DROP TABLE " + dbname + "." + tbname + ";";
		String cmdres = Utils.execCommand(shellExec(query));
        String remain = cmdres.replaceAll("\\s+","");
        
		return (remain.length()<2);
	}

	@Override
	public boolean deleteDB(String dbname) {
		String query= "DROP KEYSPACE " + dbname + ";";
		String cmdres = Utils.execCommand(shellExec(query));
        String remain = cmdres.replaceAll("\\s+","");
        
		return (remain.length()<2);
	}
}
