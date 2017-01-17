import java.net.*;
import java.io.IOException;
import java.net.DatagramSocket;

public class Server {
    private static DBProxy proxy = null;
    private static final int myPort = 5555;
	public static void main(String[] args) throws IOException {
		
		
		DatagramSocket serverSocket = new DatagramSocket(myPort); 
		byte[] receiveData = new byte[1024];
	    byte[] sendData = new byte[1024];
	       
		System.out.println ("Proxy waiting for DBMS request ");
		String response = "ok";
		while (true)
		   {
			   DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	           serverSocket.receive(receivePacket);
	           String sentence = new String( receivePacket.getData());
	           InetAddress IPAddress = receivePacket.getAddress();
	           int port = receivePacket.getPort();
	           
	           System.out.println("RECEIVED: " + sentence + " from " + IPAddress.getHostName());
	           String [] parts = sentence.split(" ");
	           
	           /*if (parts[0].equals("setupProxy") && proxy==null) 
	           { 	   
	          	   String type = parts[1];
	        	   if (type.equalsIgnoreCase("mongo")) {
	        		   //proxy = new MongoProxy(); 
	        		   response = "new mongo";
	        	   }
	        	   
	        	   else if (type.equalsIgnoreCase("mysql")) {
	        		   //proxy = new MySQLProxy();
	        		   response = "new mysql";
	        	   }
	        	   
	        	   else if (type.equalsIgnoreCase("mariadb")) {
	        		   //proxy = new MariaDBProxy();
	        		   response = "new mariadb";
	        	   }
	        	   
	        	   else if (type.equalsIgnoreCase("cassandra")) {
	        		   //proxy = new CassandraProxy();
	        		   response = "new cassandra";
	        	   }
	        	   proxy = new MongoProxy("test");
	        	   continue;     	   
	           }
	           
	           // now commands requiring a proxy to be set
	           if (proxy==null) continue;
	           */
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
