import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

//http://pingax.com/mongodb-basics-with-java/
//http://stackoverflow.com/questions/8857276/how-do-i-drop-a-mongodb-database-from-the-command-line

// printf "use mydb" > test.js
// printf "use mydb\ndb.createCollection('mytb')" > test.js
// printf "use mydb\ndb.mydb.insert({id:1,name:'myname'})" > test.js
// printf "use mydb\ndb.mydb.update({id:1},{id:1,name:'myname2'})" > test.js
// printf "use mydb\ndb.mydb.find()"
// printf "use mydb\ndb.mydb.drop()" > test.js 
// printf "use mydb\ndb.dropDatabase()" > test.js
// db.mydb.remove({'title':'MongoDB Overview'})

public class MongoProxy extends DBProxy {
   
	private MongoClient connection = null;
    private DB curDB;
    private DBCollection curTable;
    private String [] colNames;
    
    public MongoProxy() {
    	
    	super(27017, "test");
    	this.username = "";
		this.password = "";
		this.driver = "";
		
		posTraits = new HashMap<String, String>()
		{
		  {
			        put("createDB", "ok");
			        put("deleteDB", "ok");
			        put("createTable", "ok");
			        put("deleteTable", "ok");
			        put("fetch", "ok");
			        put("addTuple", "ok");
			        put("updateTuple", "ok");
			        put("rmTuple", "ok");
		  }
	    };
	    
	    negTraits = new HashMap<String, String>()
	    {
	      {
	    			put("createDB", "ok");
	    			put("deleteDB", "ok");
	    			put("createTable", "ok");
	    			put("deleteTable", "ok");
	    			put("fetch", "ok");
	    			put("addTuple", "ok");
	    			put("updateTuple", "ok");
	    			put("rmTuple", "ok");
	      }
	    };

    }
    
    public String toString() {
		return "MongoProxy "; 
	}
    
    private String shellExec(String command) {
		String res = "printf " + '"'+ command + '"' + " > test.js && cat test.js | mongo";
		System.out.println(res);
		return res;
	}
    
	@Override
	public boolean connect(String hostName)  {
		connected=true;
		return connected;
	}

	@Override
	public String createDB(String dbName) {
		
        String query = "use " + dbName;	
		
        String cmdres = Utils.execCommand(shellExec(query));
        return cmdres;
        //String remain = cmdres.replaceAll("\\s+"," ");
		//return (remain.length()<2);

	}

	@Override
	public String createTable(String dbname,String tbName) {
		// 
		MessageFormat messageFormat = new MessageFormat("use {0}\ndb.createCollection(''{1}'')");
		Object[] args = {dbname, tbName};
		String query = messageFormat.format(args);
		
		String cmdres = Utils.execCommand(shellExec(query));
		return cmdres;
        //String remain = cmdres.replaceAll("\\s+"," ");
		//return (remain.length()<2);

	}

	@Override
	public String addTuple(String dbname, String tbName, String[] values) {
		
		String query = String.format("use %s\ndb.%s.insert({%s:%s,%s:%s})", 
				dbname, tbName,this.colIDs[0],values[0],this.colIDs[1],'"'+values[1]+'"');
		
		
		String cmdres = Utils.execCommand(shellExec(query));
		return cmdres;
        //String remain = cmdres.replaceAll("\\s+"," ");
		//return (remain.length()<2);
		
	}
	
	@Override
	public String updateTuple(String dbName, String tbName, String [] values) {
		
		String query = String.format("use %s\ndb.%s.update({%s:%s},{%s:%s,%s:%s})", 
				dbName, tbName,
				this.colIDs[0],values[0],
				this.colIDs[0],values[0],
				this.colIDs[1],'"'+values[1]+'"');
		
		String cmdres = Utils.execCommand(shellExec(query));
		return cmdres;
        //String remain = cmdres.replaceAll("\\s+"," ");
		//return (remain.length()<2);
	}
	

	@Override
	public String rmTuple(String dbName, String tbName,String filter) {
		
		String query = String.format("use %s\ndb.%s.remove({%s:%s})", 
				dbName, tbName,
				this.colIDs[0],filter);
		
		String cmdres = Utils.execCommand(shellExec(query));
		return cmdres;
        //String remain = cmdres.replaceAll("\\s+"," ");
		//return (remain.length()<2);
	}
	
	
	@Override
	public String fetch(String dbName, String tbName) {
		MessageFormat messageFormat = new MessageFormat("use {0}\ndb.{1}.drop()");
		String[] args = {dbName,tbName};
		String query = messageFormat.format(args);
		
		String cmdres = Utils.execCommand(shellExec(query));
        //String remain = cmdres.replaceAll("\\s+","");
        
		return cmdres; 
	}
	
	@Override
	public boolean disconnect() {
		connected=false;
		return true;
		
	}
	@Override
	public String deleteTable(String dbname, String tbName) {
		MessageFormat messageFormat = new MessageFormat("use {0}\ndb.{1}.drop()");
		String[] args = {dbname,tbName};
		String query = messageFormat.format(args);
		
		String cmdres = Utils.execCommand(shellExec(query));
		return cmdres;
        //String remain = cmdres.replaceAll("\\s+"," ");
		//return (remain.length()<2);
	}
	@Override
	public String deleteDB(String dbName) {
		MessageFormat messageFormat = new MessageFormat("use {0}\ndb.dropDatabase()");
		Object[] args = {dbName};
		String query = messageFormat.format(args);
		
		String cmdres = Utils.execCommand(shellExec(query));
		return cmdres;
        //String remain = cmdres.replaceAll("\\s+"," ");
		//return (remain.length()<2);
	}
	
}	

