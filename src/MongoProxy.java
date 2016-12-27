import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

//http://pingax.com/mongodb-basics-with-java/
public class MongoProxy extends DBProxy {
   
	private MongoClient connection = null;
	private Map<String,String[]> columnsForDBs = new HashMap<String,String[]>();
	
    public MongoProxy() {
		port=27017;
		connect("localhost");
	}
	@Override
	public boolean connect(String hostName) {
		//String hostName = DBUtils.execCommand("./docker-ip.sh " + replicaName)[0]; 
		System.out.println("Mongo DB Connection");
		try {
			connection = new MongoClient(hostName, port);
		} catch (UnknownHostException e) {
			return false;
		}
		 
		return true;
		
	}

	@Override
	public Object createDB(String dbName) {
		
		DB db = connection.getDB(dbName);
		lastDB = (Object)db;
				
		return lastDB;
	}

	@Override
	public Object createTable(String tbName, String cols) {
		lastTable = ((DB)lastDB).getCollection(tbName);
		
		if (columnsForDBs.containsKey(tbName)==false)
			columnsForDBs.put(tbName, cols.split(","));
	
		return (Object)lastTable;
	}

	@Override
	public void addTuple(String[] values) {
		String lastTableName = ((DBCollection)lastTable).getName();
		String [] columnsNames = columnsForDBs.get(lastTableName);
		if (columnsNames==null) 
			return;
		
		BasicDBObject document = new BasicDBObject();
		
		for (int i=0; i < columnsNames.length; i++) 
		    document.put(columnsNames[i], values[i]);
		
		((DBCollection)lastTable).insert(document);
		
	}

	@Override
	public void rmTuple(String filter) {
		BasicDBObject  removeObject = new BasicDBObject();
        removeObject.append("author","vishal");

        //remove document in  collection
        ((DBCollection)lastTable).remove(removeObject);
 
       
	}
	@Override
	public Object createTable(String tbName) {
       lastTable = ((DB)lastDB).getCollection(tbName);
		
		if (columnsForDBs.containsKey(tbName)==false)
			columnsForDBs.put(tbName, this.columns);
	
		return (Object)lastTable;
	}
	@Override
	public Object createTable(String dbName, String tbName, String columns) {
		lastDB = connection.getDB(dbName);
		return this.createTable(tbName, columns);
	}
	@Override
	public String getContent(String dbName, String tbName) {
		String result = "";
		BasicDBObject searchQuery = new BasicDBObject();
		//searchQuery.put("name", "mkyong");

		DBCursor cursor = ((DBCollection)lastTable).find(searchQuery);

		while (cursor.hasNext()) {
			result = result + " " + cursor.next();
			//System.out.println(cursor.next());
		}
	 return result;
	}
}	

