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

public class Server {
    private static DBProxy actingProxy = null;
    private static final int myPort = 5555;
    private static final Timer myTimer = new Timer();
    private static List<String> currentDBs = new ArrayList<String>();
    
    private static List<DBProxy> proxies = new ArrayList<DBProxy>() {
        {
        	add(new MySQLProxy("debian-sys-maint", "","com.mysql.jdbc.Driver"));
        	add(new MySQLProxy("root", "root","org.mariadb.jdbc.Driver"));
        	add(new MongoProxy("root", "root","mongo"));
        }
    };
    
    
    private static void chooseProxy() {
    	actingProxy = null;
    	int i = 0;
    	synchronized(currentDBs)
    	 {
    		 for (String candidate : currentDBs) {
    		    String host = candidate.split(":")[0];
    		    Integer port = Integer.valueOf(candidate.split(":")[1]);
    		 
    		    for (DBProxy proxy : proxies) { 
    			   if (proxy.getPort() == port ) {
    				  actingProxy = proxy;
    				  try
    				  {
						actingProxy.connect(host);
					  } 
    				  catch (Exception e) {
    					actingProxy.disconnect();
    					actingProxy = null;
    				    continue; 
    				  }		     
						
    				  break;
    			   }
    		     }
    		 }
    	 }
    	
    	
    }
    
	public static void main(String[] args) throws IOException {
			
    DatagramSocket serverSocket = new DatagramSocket(myPort); 
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];
	       
	System.out.println ("Proxy waiting for DBMS request ");
	String response = "ok";
		
	myTimer.scheduleAtFixedRate (
		new TimerTask() {
		@Override
		public void run() {
			List<String> services = Utils.availableServices();
			
			synchronized(currentDBs) {
				currentDBs.clear();
				currentDBs.addAll(services);
			}
				
			System.out.println(Arrays.toString(currentDBs.toArray()));		
			//chooseProxy();
			
		}	
	}, 0, Utils.DBMS_REFRESH);
		
	while (true) 
	{
	    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	    serverSocket.receive(receivePacket);
	    String sentence = new String( receivePacket.getData());
	    InetAddress IPAddress = receivePacket.getAddress();
	    int port = receivePacket.getPort();
	           
	    System.out.println("RECEIVED: " + sentence + " from " + IPAddress.getHostName());
	    String [] parts = sentence.split(" ");
	           
	          
	    if (parts[0].equals("createDB") ) {
	       String dbname = parts[1];
	       //proxy.createDB(dbname);   
	       response = "createDB " + dbname;
	       continue;     	   
	     }
	           
	     if (parts[0].equals("createTable") ) {
	       String dbname = parts[1];
	       String tbname = parts[2];
	       String cols = parts[3];
	       //proxy.createTable(dbname, tbname,cols);
	       response = "createTable " + dbname+ "."+tbname;
	       continue;     	   
	     }
	           
	     if (parts[0].equals("addTuple") ) {
	       String [] values = parts[1].split(" ");
	        	   
	       //proxy.addTuple(values);
	       response = "addTuple " + parts[1];
	       continue;     	   
	     }
	           
	     if (parts[0].equals("rmTuple") ) {
	       String filter = parts[1];
	        	   
	       //proxy.rmTuple(filter);
	        	   
	       response = "rmTuple " + filter;
	       continue;     	   
	      }
	           
	     if (parts[0].equals("getContent") ) {
	       String dbname = parts[1];
	       String tbname = parts[2];
	        	   
	       //response = proxy.getContent(dbname, tbname);
	        response = " getContent " + dbname + " " + tbname;
	        continue;     	   
	     }
	           
	     System.out.println("Responded with " + response);
	              
	     //sendData = response.getBytes();          
	     //DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	     //serverSocket.send(sendPacket); 
	 }   
   }
}
