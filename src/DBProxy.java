
public abstract class DBProxy {
 
  protected int port;
  protected String username = "";
  protected String password = "";
  protected String driver;
  
  protected String host;
  protected String startDB;
  protected String columns;
  protected boolean connected = true;
 
  protected DBProxy(int port, String startDB) {
      this.port = port;
      this.connected = false;
      this.startDB = startDB;
   }
  
  public int getPort() { return port; }
  
  public abstract boolean connect(String host);
  public abstract boolean disconnect(); 
  public abstract boolean createDB(String dbName);  
  public abstract boolean createTable(String dbName, String tbName);
  public abstract boolean addTuple(String dbName, String tbName,String [] values);
  public abstract boolean rmTuple(String dbName, String tbName,String filter);
  public abstract String fetch(String dbName, String tbName);
  public abstract boolean deleteTable(String dbname, String tbname);
  
}
