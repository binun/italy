import java.net.UnknownHostException;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

//http://pingax.com/mongodb-basics-with-java/
public class MongoProxy extends DBProxy {
   
	private MongoClient connection = null;
    private DB curDB;
    private DBCollection curTable;
    
    public MongoProxy() {
    	
    	super(27017, "information_schema");
    	this.username = "";
		this.password = "";
		this.driver = "";
		this.columns = "id name";
    }
	@Override
	public boolean connect(String hostName)  {
		//String hostName = DBUtils.execCommand("./docker-ip.sh " + replicaName)[0]; 
		res = false;
		if (connected)
			return true;
		
		System.out.println("Mongo DB Connection");
		try {
			connection = new MongoClient(hostName, port);
			connected = true;
			res = true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
		}
		 
	   return res;	
	}

	@Override
	public boolean createDB(String dbName) {
		
		curDB = connection.getDB(dbName);
		return (curDB!=null);
	}

	@Override
	public boolean createTable(String dbname,String tbName) {
		
		if (curDB==null)
		  return false;
		
		if (curDB.getName().contains(dbname)==false)
			curDB = connection.getDB(dbname);
		
		curTable = curDB.getCollection(tbName);
		return (curTable!=null);
	}

	@Override
	public boolean addTuple(String dbname, String tbName, String[] values) {
		
		if (curDB.getName().equals(dbname)==false)
			curDB = connection.getDB(dbname);
		
		if (curTable.getName().equals(tbName)==false)
			curTable = curDB.getCollection(tbName);
		
		BasicDBObject document = new BasicDBObject();
		
		String [] colNames = columns.split(" ");
				
		for (int i=0; i < colNames.length; i++) 
		    document.put(colNames[i], values[i]);
		
		curTable.insert(document);
		return true;
		
	}

	@Override
	public boolean rmTuple(String dbName, String tbName,String filter) {
	       DBObject query = BasicDBObjectBuilder.start().add("id", filter).get();
 
           curTable.remove(query);
           return true;
	}
	
	
	@Override
	public String fetch(String dbName, String tbName) {
		String result = "";
		BasicDBObject searchQuery = new BasicDBObject();
		//searchQuery.put("name", "mkyong");

		DBCursor cursor = curTable.find(searchQuery);

		while (cursor.hasNext()) {
			result = result + " " + cursor.next();
			//System.out.println(cursor.next());
		}
	 return result;
	}
	
	@Override
	public boolean disconnect() {
		if (connection != null) {
	        try {
	            connection.close();
	        } catch (Exception e) {
	        	return false;
	        	}
	    }
		return true;
		
	}
	@Override
	public boolean deleteTable(String dbname, String tbName) {
		curTable.drop();
	    return true;
	}
	
	
}	

