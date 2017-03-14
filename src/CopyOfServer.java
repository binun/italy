import java.net.*;
import java.io.IOException;
import java.net.DatagramSocket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

public class CopyOfServer {
    private DBProxy actingProxy = null;
    private String actingDBHost = null;
    private DatagramSocket serverSocket = null;
    private static final int myPort = 5555;
    private static final Timer myTimer = new Timer();
    private List<String> dbHosts = new ArrayList<String>();
    
    private static List<DBProxy> proxies = new ArrayList<DBProxy>() {
        {
        	add(new MySQLProxy("com.mysql.jdbc.Driver"));
        	add(new MySQLProxy("org.mariadb.jdbc.Driver"));
        	add(new MongoProxy());
        	add(new CassandraProxy());
        }
    };
    
    private CopyOfServer () throws SocketException {
    	serverSocket = new DatagramSocket(myPort); 
	       
    	System.out.println ("Proxy waiting for DBMS request ");
    		
    	myTimer.scheduleAtFixedRate (
    		new TimerTask() {
    		@Override
    		public void run() {
    			List<String> services = Utils.availableServices();
    			
    			synchronized(dbHosts) {
    				dbHosts.clear();
    				dbHosts.addAll(services);
    			}
    				
    			System.out.print("DBMSs running: "); 
    			System.out.println(Arrays.toString(dbHosts.toArray()));		
    			chooseProxy();
    			
    		}	
    	}, 0, Utils.DBMS_REFRESH);
    }
    
    private void processRequests() throws IOException {
    	String qry = "ok";
    	while (true) 
    	{
    		byte[] receiveData = new byte[128];
    		byte[] sendData = new byte[128];
    		
    	    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    	    serverSocket.receive(receivePacket);
    	    String sentence = new String( receivePacket.getData());
    	    InetAddress IPAddress = receivePacket.getAddress();
    	    int port = receivePacket.getPort();
    	           
    	    System.out.println("RECEIVED: " + sentence + " from " + IPAddress.getHostName());
    	    
    	    /*String [] parts = sentence.split(" ");
    	    if (parts[0].equals("createDB") ) {
    	       String dbname = parts[1];  
    	       qry = "createDB " + dbname;   	   
    	     }
    	           
    	     if (parts[0].equals("createTable") ) {
    	       String dbname = parts[1];
    	       String tbname = parts[2];
    	      
    	       qry = "createTable " + dbname+ " "+tbname;    	   
    	     }
    	           
    	     if (parts[0].equals("addTuple") ) {
    	       String dbname = parts[1];
    		   String tbname = parts[2];
    	       String val1 = parts[3];
    	       String val2 = parts[4]; 	   
    	      
    	       qry = String.format("addTuple %s %s %s %s", dbname, tbname, val1, val2);   
    	     }
    	           
    	     if (parts[0].equals("rmTuple") ) {
    	       String dbname = parts[1];
    		   String tbname = parts[2];
    		   String key = parts[3];
    		      
    		   qry = String.format("rmTuple %s %s %s", dbname, tbname, key);   	   
    	      }
    	           
    	     if (parts[0].equals("fetch") ) {
    	       String dbname = parts[1];
    	       String tbname = parts[2];
    	        	   
    	       qry = "fetch " + dbname + " " + tbname;	   
    	     }
    	     
    	     if (parts[0].equals("deleteTable") ) {
    		    String dbname = parts[1];
    		    String tbname = parts[2];
    		        	   		     
    		    qry = "deleteTable " + dbname + " " + tbname;
    		    //continue;     	   
    		 }
    	     
    	     if (parts[0].equals("deleteDB") ) {
    		       String dbname = parts[1];		    
    		       qry = "deleteDB " + dbname;    	   
    		 }*/
    	     
    	     actingProxy.runCommand(qry);
    	     //System.out.println("Responded with " + response);
    	              
    	     //sendData = response.getBytes();          
    	     //DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
    	     //serverSocket.send(sendPacket); 
    	 }   
    }
    private void chooseProxy() {
    	actingProxy = null;
    	int i = 0;
    	synchronized(dbHosts)
    	 {
    		 for (String candidate : dbHosts) {
    		    String host = candidate.split(":")[0];
    		    Integer port = Integer.valueOf(candidate.split(":")[1]);
    		 
    		    for (DBProxy proxy : proxies) { 
    			   if (proxy.getPort() == port ) {
    				   
    				  if (proxy!=actingProxy && actingProxy!=null && actingProxy.isConnected()) {
    					actingProxy.disconnect();
      					actingProxy = null;
    				  }
    				  
    				  actingProxy = proxy;
    				  System.out.println("        DB Proxy to run: " + actingProxy.toString());
    				  try
    				  {
						actingProxy.connect(host);
					  } 
    				  catch (Exception e) 
    				  {
    					actingProxy.disconnect();
    					actingProxy = null;
    				    continue; 
    				  }	  
						
    				  return;
    			   }
    		     }
    		 }
    	 }
    	
    	
    }
    
	public static void main(String[] args) throws IOException {	
       new CopyOfServer().processRequests();
   }
}
