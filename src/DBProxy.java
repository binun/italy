
public abstract class DBProxy {
 
  protected int port;
  protected String username = "";
  protected String password = "";
  protected String driver;
  
  protected String host;
  protected String startDB;
  protected String columns;
  protected boolean connected = true;
  protected boolean res = false;
 
  protected DBProxy(int port, String startDB) {
      this.port = port;
      this.connected = false;
      this.startDB = startDB;
      this.connected = false;
      this.res = false;
   }
  
  public int getPort() { return port; }
  
  protected Object runCommand(String query) {
	String [] parsed = query.split(" ");
	String dbname = parsed[1];
	
	if (parsed[0].equals("createDB")) {
		return new Boolean(createDB(dbname));
	}
	
    if (parsed[0].equals("createTable")) {
		String tbname = parsed[2];
		return new Boolean(createTable(dbname,tbname));
	}
    
    if (parsed[0].equals("addTuple")) {
    	String tbname = parsed[2];
    	String [] args = new String[2];
    	for (int i = 3; i < parsed.length; i++)
    		args[i-3] = parsed[i];
    	
    	return new Boolean(addTuple(dbname,tbname,args));
	}
    
    if (parsed[0].equals("rmTuple")) {
    	String tbname = parsed[2];
    	String arg = parsed[3];
    	
    	return new Boolean(rmTuple(dbname,tbname,arg));
	}
    
    if (parsed[0].equals("deleteTable")) {
    	String tbname = parsed[2];
		return new Boolean(deleteTable(dbname,tbname));
	}
    
    if (parsed[0].equals("fetch")) {
    	String tbname = parsed[2];
		return (Object)fetch(dbname,tbname);
	}
	
	return query;
	  
  }
  
  public int runScenario(String host, String [] commands) {
	    int i = 0;
	    if (connect(host)==false)
	    	return 0;
	    
		for (i = 0; i < commands.length; i++) {
			Object o = this.runCommand(commands[i]);
			
			if (o instanceof Boolean) {
				if (((Boolean)o).booleanValue()==false)
					break;
			}
			
			if (o instanceof String) {
				if (((String)o).length()<2)
					break;
			}
		}
		
		disconnect();
		return 0;
		
	}
  
  public abstract boolean connect(String host);
  public abstract boolean disconnect(); 
  public abstract boolean createDB(String dbName);  
  public abstract boolean createTable(String dbName, String tbName);
  public abstract boolean addTuple(String dbName, String tbName,String [] values);
  public abstract boolean rmTuple(String dbName, String tbName,String filter);
  public abstract String fetch(String dbName, String tbName);
  public abstract boolean deleteTable(String dbname, String tbname);
  
}
