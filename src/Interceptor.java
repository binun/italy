import java.net.*;
import java.io.IOException;
import java.net.DatagramSocket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Interceptor {
    private DBProxy actingDB = new MySQLProxy("com.mysql.jdbc.Driver");
    private long phase = System.currentTimeMillis();
    private Map<Integer,String> responses = new HashMap<Integer,String>();
    
    private Interceptor (String db) throws SocketException {
    	
    	if (db.contains("cassandra"))
    		actingDB = new CassandraProxy();
    	if (db.contains("mongo"))
    		actingDB = new MongoProxy();
    	if (db.contains("mysql"))
    		actingDB = new MySQLProxy("com.mysql.jdbc.Driver");
    	if (db.contains("maria"))
    		actingDB = new MySQLProxy("org.mariadb.jdbc.Driver");
    	
    	if (actingDB.online()==false) {
			  actingDB.connect("localhost");
			  actingDB.deleteDB("mydb");
    	}
    }
    
    private void processRequests() throws IOException {
    	DatagramSocket socket = new DatagramSocket(Utils.DBPORT);
        
 	    System.out.println ("Interceptor waits on " + actingDB.getClass());
 	    String resp = "ok";
 	
 	    while (true) {
 		  byte[] receiveData = new byte[128];
 		
 	      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
 	      socket.receive(receivePacket);
 	      String request = new String( receivePacket.getData());  
 	      String [] parts = request.split("@");
 	      Integer timestamp = Integer.valueOf(parts[0]);
 	      String query = parts[1];
 	      
 	      String response = responses.get(timestamp);
 	      if (response==null) {
 	    	  System.out.println("MUST EXECUTE: " + request);
 	    	  response = actingDB.runCommand(query);
        	  
        	  responses.put(timestamp, response);
        	  
 	      }
 	      
 	      System.out.println("Back: " + response);
 	      byte [] data = response.getBytes();
          socket.send(new DatagramPacket( data, data.length, receivePacket.getAddress(), Utils.DBPORT)); 
                    
        }
      }
    
	public static void main(String[] args) throws IOException {
		new Interceptor(args[0]).processRequests();
	 }   
}
