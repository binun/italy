import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;



public class UDPClient
{
   private final static int TIMEOUT = 500;
   private final static int INITDELAY = 5000;
   private final static String hostsFile = "./hosts.config";
 
   private static String [] scenario = {
	    //"deleteDB myDB",
		"createDB myDB",
		"createTable myDB myTable",
		"addTuple myDB myTable 1 name1",
		"addTuple myDB myTable 2 name2",
		"fetch myDB myTable",
		//"rmTuple myDB myTable 1",
		"fetch myDB myTable",
		//"updateTuple myDB myTable 2 name3",
		"fetch myDB myTable"
		//"deleteTable myDB myTable"
};
   
   private String [] hosts = null;
   private DatagramSocket replicaSocket = null;
   private int orderId = 0;
   public void runScenario() {
	   if (hosts.length==0) {
		   System.out.println("No replicas created");
		   return;
	   }
	   try 
	   {
		  Thread.sleep(INITDELAY);
		
		  for (int is=0; is < scenario.length; is++) {
			String msg = String.format("%d@%s", orderId,scenario[is]);
		    this.broadcast(msg);
		    orderId++;
		    Thread.sleep(TIMEOUT);
		  }
	   } 
	   catch (Exception e) 
	   {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
	   
   }

   private void broadcast (String message) throws InterruptedException {
	   synchronized(hosts) {
	       for (String host: hosts) 
	       {
	    	   //System.out.println("Handling " + host);
	           
	    	   InetAddress ha = null;
			   try 
			   {
				    ha = InetAddress.getByName(host);
			   } 
			   catch (Exception e) 
			   {
				   System.out.println(e.getMessage());    
				    continue;
			   }
	    	   byte [] data = message.getBytes(StandardCharsets.UTF_8) ;
	           DatagramPacket packet = new DatagramPacket( data, data.length, ha, Utils.REPLICAPORT) ;
	           System.out.println("  Sending " + message + " to " + host);
	           try 
	           {
				    replicaSocket.send(packet);
			   } 
	           catch (Exception e) 
	           {
	        	    System.out.println(e.getMessage());
				    continue;
			   }
	           
	           byte[] dataFromReplica = new byte[256];
	           DatagramPacket rpRespPacket = new DatagramPacket(dataFromReplica, dataFromReplica.length);
	           try 
	           {
				   replicaSocket.receive(rpRespPacket);
			   } 
	           catch (IOException e) 
	           {
	        	   System.out.println(e.getMessage());
	        	   continue;
			   }
	           
       	       String replicaResponse = new String( rpRespPacket.getData());
       	       System.out.println("  Obtaining " + replicaResponse + " from  " + host);
	       }
	       
	       Thread.currentThread().sleep(Utils.CLIENT_COLLECT_DELAY);
	       
	   }
   }
   
   private UDPClient() {
	   try 
	      {
			replicaSocket = new DatagramSocket(Utils.REPLICAPORT);
		  } 
	      catch (SocketException e1)
	      {
			e1.printStackTrace();
		  }
	      
	      Runtime.getRuntime().addShutdownHook(new Thread() {
	          public void run() {
	            if( replicaSocket != null )
	               replicaSocket.close() ;
	          }
	        });
	      
	      hosts = Utils.getReplicaIPs(hostsFile);
	      
   }
   
   public static void main( String args[] )
   {
      new UDPClient().runScenario();
   }
}
