import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
    private String mongores = "mongoresult.txt";
    private String queryS = "/runMongo.sh ";
    
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
      StringBuffer sb = new StringBuffer();
      try {
		
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
		out.println(command);
		out.close();
		
		Utils.execCommand(command.replaceAll("\0", ""));
		
		File temp = new File(mongores);
		while (temp.exists()==false) {}
		
		BufferedReader br = new BufferedReader(new FileReader(temp));
		
	   
	    String line = br.readLine();

	    while (line != null) {
	        sb.append(line);
	        sb.append("\n");
	        line = br.readLine();
	    }
      }
      catch (Exception e) {
    	  e.printStackTrace();
      }	  
      return sb.toString();		
	}
    
	@Override
	public boolean connect(String hostName)  {
		connected=true;
		return connected;
	}

	@Override
	public String createDB(String dbName) {
		
		String query = String.format("./runMongo.sh createDB %s", dbName);	
		return shellExec(query);
		//String [] commands = new String[3];
		//commands[0] = this.queryS;
		//commands[1] = "createDB";
		//commands[2] = dbName;
        
		//return shellExec(commands);
        //String cmdres = Utils.execCommand(shellExec(query));
        //return cmdres;
        //String remain = cmdres.replaceAll("\\s+","");
        //return (remain.length()<2) ? Utils.OK : remain;
		//curDB = connection.getDB(dbName);

	}

	@Override
	public String createTable(String dbname,String tbName) {
		
		//String [] commands = new String[4];
		//commands[0] = this.queryS;
		//commands[1] = "createTable";
		//commands[2] = dbname;
		//commands[3] = tbName;
		//return shellExec(query);
		String query = String.format("./runMongo.sh createTable %s %s", dbname,tbName);	
		return shellExec(query);
		/*MessageFormat messageFormat = new MessageFormat("use {0}\ndb.createCollection(''{1}'')");
		Object[] args = {dbname, tbName};
		String query = messageFormat.format(args);
		
		String cmdres = Utils.execCommand(shellExec(query));
		
        String remain = cmdres.replaceAll("\\s+","");
        return (remain.length()<2) ? Utils.OK : remain;*/
		//curDB = connection.getDB(dbname);
		//curTable = curDB.createCollection(tbName,null);
		//return Utils.OK;

	}

	@Override
	public String addTuple(String dbname, String tbName, String[] values) {
		
		//String [] commands = new String[8];
		//commands[0] = this.queryS;
		//commands[1] = "addTuple";
		//commands[2] = dbname;
		//commands[3] = tbName;
		//commands[4] = this.colIDs[0];
		//commands[5] = values[0];
		//commands[6] = this.colIDs[1];
		//commands[7] = values[1];
		
		
		//return shellExec(commands);
		String query = String.format("./runMongo.sh addTuple %s %s %s %s %s %s", 
		  dbname,tbName,this.colIDs[0],values[0],this.colIDs[1],values[1]);
		return shellExec(query);
		
		///String cmdres = Utils.execCommand(shellExec(query));
		//return cmdres;
        //String remain = cmdres.replaceAll("\\s+","");
        //return (remain.length()<2) ? Utils.OK : remain;
		//DBCollection ctb = curDB.getCollection(tbName);
		//BasicDBObject doc = new BasicDBObject("name", "MongoDB").
           //     append("type", "database").
            //    append("count", 1).
          //      append("info", new BasicDBObject("x", 203).append("y", 102));

        //curTable.insert(doc);
		//for (int i=0; i < values.length;i++)
			//document.put(this.colIDs[i], values[i]);
	
		//return Utils.OK;
	}
	
	@Override
	public String updateTuple(String dbName, String tbName, String [] values) {
		String query = String.format("./runMongo.sh updateTuple %s %s %s %s %s %s", 
				dbName,tbName,this.colIDs[0],values[0],this.colIDs[1],values[1]);	
		//String [] commands = new String[8];
		//commands[0] = this.queryS;
		//commands[1] = "updateTuple";
		//commands[2] = dbName;
		//commands[3] = tbName;
		//commands[4] = this.colIDs[0];
		//commands[5] = values[0];
		//commands[6] = this.colIDs[1];
		//commands[7] = values[1];
		
		//return shellExec(commands);
		return shellExec(query);
	
		//String query = String.format("use %s\ndb.%s.update({%s:%s},{%s:%s,%s:%s} )", 
				//dbName, tbName,
				//this.colIDs[0],values[0],
				//this.colIDs[0],values[0],
				//this.colIDs[1],"\\" + '\"' +values[1] + "\\" + '\"');
		
		//String cmdres = Utils.execCommand(shellExec(query));
		//return cmdres;
        //String remain = cmdres.replaceAll("\\s+","");
        //return (remain.length()<2) ? Utils.OK : remain;
		//BasicDBObject newDocument = new BasicDBObject();
		//newDocument.append("$set", new BasicDBObject().append(this.colIDs[1], values[1]));
		
		//BasicDBObject searchQuery = new BasicDBObject().append(this.colIDs[0], values[0]);
		
		//curTable.update(searchQuery, newDocument);
		//return Utils.OK;
		
	}
	

	@Override
	public String rmTuple(String dbName, String tbName,String filter) {
		
		//String [] commands = new String[6];
		//commands[0] = this.queryS;
		//commands[1] = "rmTuple";
		//commands[2] = dbName;
		//commands[3] = tbName;
		//commands[4] = this.colIDs[0];
		//commands[5] = filter;
		//return shellExec(commands);
		
		String query = String.format("./runMongo.sh rmTuple %s %s %s %s", 
				dbName,tbName,this.colIDs[0],filter);	
		return shellExec(query);
		//String query = String.format("use %s\ndb.%s.deleteMany({%s:%s})", 
				//dbName, tbName,
				//this.colIDs[0],filter);
		
		//String cmdres = Utils.execCommand(shellExec(query));
		//return cmdres;
        //String remain = cmdres.replaceAll("\\s+","");
        //return (remain.length()<2) ? Utils.OK : remain;
		
		 //DBObject query = BasicDBObjectBuilder.start().add(this.colIDs[0], filter).get();
		 //curTable.remove(query);
		// return Utils.OK;
	}
	
	
	@Override
	public String fetch(String dbName, String tbName) {
		//String [] commands = new String[4];
		//commands[0] = this.queryS;
		//commands[1] = "fetch";
		//commands[2] = dbName;
		//commands[3] = tbName;
		//return shellExec(commands);
		
		String query = String.format("./runMongo.sh fetch %s %s", dbName,tbName);	
		return shellExec(query);
		///String query = String.format("use %s\ndb.%s.find()", 
				//dbName, tbName);
		
		//String cmdres = Utils.execCommand(shellExec(query));
       // String remain = cmdres.replaceAll("\\s+"," ");
        
		//return cmdres; 
		
		
		//StringBuffer result = new StringBuffer();
		//BasicDBObject searchQuery = new BasicDBObject();
		//DBCursor cursor = ctb.find(searchQuery);
		//DBObject doc = curTable.findOne();
		//result.append("{"+doc.toString()+"}");
		//while (cursor.hasNext()) {
            
		  //  result.append("{"+cursor.next()+"}");
		//}
	   //return result.toString();
	}
	
	@Override
	public boolean disconnect() {
		connected=false;
		return true;
		
	}
	@Override
	public String deleteTable(String dbname, String tbName) {
		//String [] commands = new String[4];
		//commands[0] = this.queryS;
		//commands[1] = "deleteTable";
		//commands[2] = dbname;
		//commands[3] = tbName;
		//return shellExec(commands);
		String query = String.format("./runMongo.sh deleteTable %s %s", dbname,tbName);
		return shellExec(query);
		//String cmdres = Utils.execCommand(shellExec(query));
		//return cmdres;
        //String remain = cmdres.replaceAll("\\s+","");
        //return (remain.length()<2) ? Utils.OK : remain;
		//curTable.drop();
		//return Utils.OK;
	}
	@Override
	public String deleteDB(String dbName) {
		//String [] commands = new String[3];
		//commands[0] = this.queryS;
		//commands[1] = "deleteDB";
		//commands[2] = dbName;
		
		//return shellExec(commands);
		String query = String.format("./runMongo.sh deleteDB %s", dbName);	
		return shellExec(query);
		//String query = String.format("use %s\ndb.dropDatabase()", 
				//dbName);
		
		//String cmdres = Utils.execCommand(shellExec(query));
		//return cmdres;
        //String remain = cmdres.replaceAll("\\s+","");
        //return (remain.length()<2) ? Utils.OK : remain;
		//connection.dropDatabase(dbName);
		//return Utils.OK;
	}
	
}	

