import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class UDPClient
{
   private final static int PACKETSIZE = 1024 ;
   private final static int PORT  = 5555;
   private final static int TIMEOUT = 1;
   
   private List<String> hosts = new ArrayList<String>();\
   private static DatagramSocket socket = null;

   private void broadcast (String message) {
	   synchronized(hosts) {
	       for (String host: hosts) {
	    	   InetAddress ha = InetAddress.getByName(host);
	    	   byte [] data = message.getBytes() ;
	           DatagramPacket packet = new DatagramPacket( data, data.length, host, PORT ) ;
	       }
	   }
   }
   public static void main( String args[] )
   {
      socket = new DatagramSocket() ;
      while (true) {
    	  
      }

      try
      {
         // Convert the arguments first, to ensure that they are valid
         InetAddress host = InetAddress.getByName( args[0] ) ;
         int port         = Integer.parseInt( args[1] ) ;

         // Construct the socket
         

         // Construct the datagram packet
         byte [] data = "Hello Server".getBytes() ;
         DatagramPacket packet = new DatagramPacket( data, data.length, host, port ) ;

         // Send it
         socket.send( packet ) ;

         // Set a receive timeout, 2000 milliseconds
         socket.setSoTimeout( 2000 ) ;

         // Prepare the packet for receive
         packet.setData( new byte[PACKETSIZE] ) ;

         // Wait for a response from the server
         socket.receive( packet ) ;

         // Print the response
         System.out.println( new String(packet.getData()) ) ;

      }
      catch( Exception e )
      {
         System.out.println( e ) ;
      }
      finally
      {
         if( socket != null )
            socket.close() ;
      }
   }
}
