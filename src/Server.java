import java.net.*;
import java.io.IOException;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
    private String clientIP = null;
    
    private static final Timer myTimer = new Timer();
    private List<String> DBMSs = new ArrayList<String>();
    
    private Server () throws SocketException {
     	System.out.println ("Replica waiting for DBMS request ");
    		
    	myTimer.scheduleAtFixedRate (
    		new TimerTask() {
    		@Override
    		public void run() {
    			Set<String> services = Utils.availableServices();
    			
    			synchronized(DBMSs) {
    				DBMSs.clear();
    				DBMSs.addAll(services);
    				//System.out.println("DB Hosts in the air: " + Arrays.toString(DBMSs.toArray()));
    			}
    		
    		}	
    	}, 0, Utils.DBMS_REFRESH);
    }
    
    private void processRequests() throws IOException, InterruptedException {
    	String qry = "ok";
    	DatagramSocket serverSocket = new DatagramSocket(Utils.REPLICAPORT);
    	DatagramSocket dbSocket = new DatagramSocket(Utils.DBPORT);
    	while (true) 
    	{
    		byte[] dataFromClient = new byte[64];
   		
    	    DatagramPacket receivePacket = new DatagramPacket(dataFromClient, dataFromClient.length);
    	    serverSocket.receive(receivePacket);
    	    String sentence = new String( receivePacket.getData());
    	    InetAddress IPAddress = receivePacket.getAddress();
    	    int clientport = receivePacket.getPort();
    	    clientIP = IPAddress.getHostName();
    	         
    	    System.out.println("RECEIVED FROM CLIENT " + sentence + " from " + clientIP );
  
    	    List<String> responses = new ArrayList<String>();
    	    
    	    synchronized(DBMSs) {
    	     for (String dBHost: DBMSs) 
    	     {
    	    	byte[] dataFromDB = new byte[128];
    	    	String dbResponse = "response-" + dBHost;
    	    	
    	    	System.out.printf(" ------> FORWARD %s TO %s\n", sentence,dBHost);
        	    
    	    	dbSocket.send(new DatagramPacket( sentence.getBytes(StandardCharsets.UTF_8), sentence.getBytes(StandardCharsets.UTF_8).length, InetAddress.getByName(dBHost), Utils.DBPORT)); 	    
        	    DatagramPacket dbRespPacket = new DatagramPacket(dataFromDB, dataFromDB.length);
        	    dbSocket.receive(dbRespPacket);
        	    dbResponse = new String( dbRespPacket.getData());
        	   
        	    System.out.printf(" <------ RECEIVED %s FROM %s\n", dbResponse, dBHost);
        	    responses.add(dbResponse);
    	     }
    	    
    	     Thread.currentThread().sleep(Utils.REPLICA_COLLECT_DELAY);
    	     String totalResponse = String.join("_", responses);
    	     byte [] data1 = totalResponse.getBytes(StandardCharsets.UTF_8);
    	     serverSocket.send(new DatagramPacket( data1, data1.length, InetAddress.getByName(clientIP), clientport)); 
    	 }   
    	}
    }
   
	public static void main(String[] args) throws IOException, InterruptedException {	
       new Server().processRequests();
   }
}
