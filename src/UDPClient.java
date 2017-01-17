import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;



public class UDPClient
{
   private final static int PACKETSIZE = 1024 ;
   private final static int PORT  = 5555;
   private final static int TIMEOUT = 1;
   private final static String hostsFile = "./hosts.config";
   
   private String [] hosts = null;
   private DatagramSocket socket = null;
   
   public void runScenario() {
	   if (hosts.length==0) {
		   System.out.println("No replicas created");
		   return;
	   }
	   
	   for (;;) {
		  this.broadcast("createDB myDB");
		  try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
   }

   private void broadcast (String message) {
	   synchronized(hosts) {
	       for (String host: hosts) 
	       {
	    	   System.out.println("Handling " + host);
	           
	    	   InetAddress ha = null;
			   try 
			   {
				    ha = InetAddress.getByName(host);
			   } 
			   catch (UnknownHostException e) 
			   {
				    continue;
			   }
	    	   byte [] data = message.getBytes() ;
	           DatagramPacket packet = new DatagramPacket( data, data.length, ha, PORT ) ;
	           System.out.println("  Sending " + message + " to " + host);
	           try 
	           {
				    socket.send(packet);
			   } 
	           catch (IOException e) 
	           {
				    continue;
			   }
	           System.out.println("      Sent to " + host);
	       }
	   }
   }
   
   private UDPClient() {
	   try 
	      {
			socket = new DatagramSocket() ;
		  } 
	      catch (SocketException e1)
	      {
			e1.printStackTrace();
		  }
	      
	      Runtime.getRuntime().addShutdownHook(new Thread() {
	          public void run() {
	            if( socket != null )
	               socket.close() ;
	          }
	        });
	      
	      hosts = Utils.getReplicaIPs(hostsFile);
	      
   }
   
   public static void main( String args[] )
   {
      new UDPClient().runScenario();
   }
}
