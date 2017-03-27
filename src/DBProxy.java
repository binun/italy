import java.io.File;
import java.util.HashMap;
import java.util.Map;


public abstract class DBProxy {
 
  protected int port;
  protected String username = "";
  protected String password = "";
  protected String driver;
  
  protected String host;
  protected String startDB;
  protected String [] colIDs = new String[2];
  protected boolean connected = false;
  protected boolean res = false;

  Map<String,String> posTraits;
  Map<String,String> negTraits;
 
  protected DBProxy(int port, String startDB) {
      this.port = port;
      this.connected = false;
      this.startDB = startDB;
      this.connected = false;
      this.res = false;
      this.colIDs[0] = "id";
      this.colIDs[1] = "name";
      
      new File("log.txt").delete();
	  new File("reslog.txt").delete();
   }
  
  public int getPort() { return port; }
  public boolean isConnected() { return connected; }
  
  public String runCommand(String query) {
	  
	String result = Utils.FAIL;
	if (!connected)
		return result;
	//System.out.println("Executes " + query); 
	
	String [] parsed = query.split(" ");
	String dbname = parsed[1];	
	
	if (parsed[0].equals("createDB")) {
		result = createDB(dbname);
	}
	
	if (parsed[0].equals("deleteDB")) {
		result = deleteDB(dbname);
	}	
	
    if (parsed[0].equals("createTable")) {
    	String tbname = parsed[2];
		result = createTable(dbname,tbname);
	}
    
    if (parsed[0].equals("deleteTable")) {
    	String tbname = parsed[2];
		result = deleteTable(dbname,tbname);
	}
       
    if (parsed[0].equals("fetch")) {
    	String tbname = parsed[2];
		result = fetch(dbname,tbname);
	}
    
    String [] args = new String[2];
    
    if (parsed[0].equals("addTuple")) {
    	String tbname = parsed[2];
    	for (int i = 3; i < parsed.length; i++)
    		args[i-3] = parsed[i];
    	
    	result = addTuple(dbname,tbname,args);
	}
    
    if (parsed[0].equals("updateTuple")) {
    	String tbname = parsed[2];
    	for (int i = 3; i < parsed.length; i++)
    		args[i-3] = parsed[i];
    	
    	result = updateTuple(dbname,tbname,args);
	}
    
    if (parsed[0].equals("rmTuple")) {
    	String tbname = parsed[2];
    	String arg = parsed[3];
    	
    	result = rmTuple(dbname,tbname,arg);
    }
    
    /*if (parsed[0].equals("fetch")==false) {
    	
    	String [] ptraits = posTraits.get(parsed[0]).split(" ");
    	String [] ntraits = negTraits.get(parsed[0]).split(" ");
    	
    	boolean posPresent = false, negPresent=false;
    	for (String p: ptraits)
    		if (result.contains(p))
    			posPresent = true;
    	for (String n: ntraits)
    		if (result.contains(n))
    			negPresent = true;
    	
    	if (posPresent && !negPresent)
    		result= Utils.OK;
    	else
    		result= Utils.FAIL;
    }*/
    
	return result;
  }
  
  public int runScenario(String host, String [] commands) {
	    int i = 0;
	    if (connect(host)==false)
	    	return 0;
	    
		for (i = 0; i < commands.length; i++) {
			Object o = this.runCommand(commands[i]);
			
			/*if (o==null)
				break;
			
			if (o instanceof Boolean) {
				if (((Boolean)o).booleanValue()==false)
					break;
			}
			
			if (o instanceof String) {
				if (((String)o).length()<2)
					break;
				else
					System.out.println((String)o);
			}*/
		}
		
		disconnect();
		return 0;
		
	}
  
  public abstract boolean connect(String host);
  public abstract boolean disconnect(); 
  public abstract String createDB(String dbName);  
  public abstract String createTable(String dbName, String tbName);
  public abstract String addTuple(String dbName, String tbName,String [] values);
  public abstract String rmTuple(String dbName, String tbName,String filter);
  public abstract String updateTuple(String dbName, String tbName,String [] values);
  public abstract String fetch(String dbName, String tbName);
  public abstract String deleteTable(String dbname, String tbname);
  public abstract String deleteDB(String dbName);
  public boolean online() {return connected; }
  
}
