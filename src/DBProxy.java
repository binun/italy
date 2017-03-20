
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
 
  protected DBProxy(int port, String startDB) {
      this.port = port;
      this.connected = false;
      this.startDB = startDB;
      this.connected = false;
      this.res = false;
      this.colIDs[0] = "id";
      this.colIDs[1] = "name";
   }
  
  public int getPort() { return port; }
  public boolean isConnected() { return connected; }
  
  public Object runCommand(String query) {
	  
	if (!connected)
		return "offline";
	//System.out.println("Executes " + query); 
	
	String [] parsed = query.split(" ");
	String dbname = parsed[1];
	
	if (parsed[0].equals("createDB")) {
		return new Boolean(createDB(dbname));
	}
	
	if (parsed[0].equals("deleteDB")) {
		return new Boolean(deleteDB(dbname));
	}
	
	String tbname = parsed[2];
	
    if (parsed[0].equals("createTable")) {
		
		return new Boolean(createTable(dbname,tbname));
	}
    
    if (parsed[0].equals("deleteTable")) {
		return new Boolean(deleteTable(dbname,tbname));
	}
       
    if (parsed[0].equals("fetch")) {
		return (Object)fetch(dbname,tbname);
	}
    
    String [] args = new String[2];
    
    if (parsed[0].equals("addTuple")) {
    	
    	for (int i = 3; i < parsed.length; i++)
    		args[i-3] = parsed[i];
    	
    	return new Boolean(addTuple(dbname,tbname,args));
	}
    
    if (parsed[0].equals("updateTuple")) {

    	for (int i = 3; i < parsed.length; i++)
    		args[i-3] = parsed[i];
    	
    	return new Boolean(updateTuple(dbname,tbname,args));
	}
    
    if (parsed[0].equals("rmTuple")) {
    	String arg = parsed[3];
    	
    	return new Boolean(rmTuple(dbname,tbname,arg));
	}
	
	return query;
	  
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
  public abstract boolean createDB(String dbName);  
  public abstract boolean createTable(String dbName, String tbName);
  public abstract boolean addTuple(String dbName, String tbName,String [] values);
  public abstract boolean rmTuple(String dbName, String tbName,String filter);
  public abstract boolean updateTuple(String dbName, String tbName,String [] values);
  public abstract String fetch(String dbName, String tbName);
  public abstract boolean deleteTable(String dbname, String tbname);
  public abstract boolean deleteDB(String dbName);
  public boolean online() {return connected; }
  
}
