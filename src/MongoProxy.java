import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

//http://pingax.com/mongodb-basics-with-java/
//http://stackoverflow.com/questions/8857276/how-do-i-drop-a-mongodb-database-from-the-command-line
public class MongoProxy extends DBProxy {
   
	private MongoClient connection = null;
    private DB curDB;
    private DBCollection curTable;
    private String [] colNames;
    
    public MongoProxy() {
    	
    	super(27017, "information_schema");
    	this.username = "";
		this.password = "";
		this.driver = "";
		this.columns = "id name";
		colNames = columns.split(" ");
    }
    
    public String toString() {
		return "MongoProxy "; 
	}
	@Override
	public boolean connect(String hostName)  {
		//String hostName = DBUtils.execCommand("./docker-ip.sh " + replicaName)[0]; 
		res = false;
		if (connected)
			return true;
		
		System.out.println("Mongo DB Connected");
		try {
			connection = new MongoClient(hostName, port);
			connected = true;
			res = true;
		} catch (Exception e) {
			res=false;
			System.out.println("Mongo DB NOCONNECT " + e.getMessage());
		}
		 
	   return res;	
	}

	@Override
	public boolean createDB(String dbName) {
		
		try {
		  curDB = connection.getDB(dbName);	
	      System.out.println("Mongo DB created " + dbName);
		  return true;
		}
		catch (Exception e) {
			System.out.println("Mongo DB FAILCREATE " + dbName + " " + e.getMessage());
			return false;
		}

	}

	@Override
	public boolean createTable(String dbname,String tbName) {
		
		if (!connected)
		  return false;
		
		try {
		  curDB = connection.getDB(dbname);
		  curTable = curDB.getCollection(tbName);
		  System.out.println("Mongo table created " + tbName);
		  return true;
		} 
		
		catch (Exception e) {
			System.out.println("Mongo table FAILCREATE " + tbName+" " + e.getMessage());
			return false;
		}

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
		
		try {
		   curTable.insert(document);
		   System.out.println("Mongo record added " + tbName);
		   return true;
		}
		catch (Exception e) {
			System.out.println("Mongo record FAILADD " + tbName + " " + e.getMessage());
			return false;
		}
		
		
	}
	
	@Override
	public boolean updateTuple(String dbName, String tbName, String id, String name) {
		
		if (curDB.getName().equals(dbName)==false)
			curDB = connection.getDB(dbName);
		
		if (curTable.getName().equals(tbName)==false)
			curTable = curDB.getCollection(tbName);
		
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", new BasicDBObject().append("name", name));

		BasicDBObject searchQuery = new BasicDBObject().append("id", id);
		

		
		try {
		    curTable.update(searchQuery, newDocument);
		    System.out.println("Mongo record updated " + tbName);
		    return true;
		}
		catch (Exception e) {
			System.out.println("Mongo record FAILUPDATE " + tbName + " " + e.getMessage());
		    return false;
		}
	}
	

	@Override
	public boolean rmTuple(String dbName, String tbName,String filter) {
	       DBObject query = BasicDBObjectBuilder.start().add("id", filter).get();
           
           try {
       		curTable.remove(query);
   		    System.out.println("Mongo record removed " + tbName);
   		    return true;
   		   }
   		   catch (Exception e) {
   			System.out.println("Mongo record FAILREMOVE " + tbName + " " + e.getMessage());
   		    return false;
   		   }
	}
	
	
	@Override
	public String fetch(String dbName, String tbName) {
		String result = "";
		BasicDBObject searchQuery = new BasicDBObject();
		//searchQuery.put("name", "mkyong");
		DBCursor cursor = null;

		try {
			cursor = curTable.find(searchQuery);
	
            while (cursor.hasNext()) {
			   DBObject dbo = cursor.next();
			   String tuple="";
			   for (int i=0; i < colNames.length; i++) {
				  tuple=tuple+dbo.get(colNames[i]);
			      if (i<colNames.length-1)
			         tuple=tuple+" ";
			    	
			  }
			  result = result + "{"+tuple+"}";
			  //System.out.println(cursor.next());
		    }
            System.out.println("Mongo record retrieved " + tbName);
       	    return result;  
		}
     catch (Exception e) {
    	 System.out.println("Mongo record FAILRETRIEVE " + tbName+ " " + e.getMessage());
    	 return "empty"; 
     }
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
		try {
		   curTable.drop();
		   System.out.println("Mongo Dropped table");
	       return true;
		}
		catch (Exception e) {
			 
			System.out.println("Mongo DROPTABLEFAIL " + " " + e.getMessage());
		    return false;
		}
		
	}
	@Override
	public boolean deleteDB(String dbName) {
		try {
		  connection.dropDatabase(dbName);
		  System.out.println("Mongo Dropped DB ");
		}
		
		catch (Exception e) {
			System.out.println("Mongo DELDBFAIL " + " " + e.getMessage());
			return false;
		}
		return true;
	}
	
}	

