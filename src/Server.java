import java.net.*;
import java.io.IOException;
import java.net.DatagramSocket;

public class Server {
    private static DBProxy proxy = null;
	public static void main(String[] args) throws IOException {
		String request;
		int myPort = 5555;
		boolean proxySet=false;
		// set up connection socket
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
	           if (parts[0].equals("setupProxy") && !proxySet) {
	        	   String type = parts[1];
	        	   if (type.equalsIgnoreCase("mongo")) 
	        		   proxy = new MongoProxy();        		
	        	   
	        	   else if (type.equalsIgnoreCase("mysql")) 
	        		   proxy = new MySQLProxy();
	        	   
	        	   else if (type.equalsIgnoreCase("mariadb")) 
	        		   proxy = new MariaDBProxy();
	        	   
	        	   proxySet=true;
	        	   response = "Proxy-OK";
	        	   continue;     	   
	           }
	           
	           if (parts[0].equals("createDB") && proxySet) {
	        	   String dbname = parts[1];
	        	   proxy.createDB(dbname);   
	        	   response = dbname;
	        	   continue;     	   
	           }
	           
	           if (parts[0].equals("createTable") && proxySet) {
	        	   String dbname = parts[1];
	        	   String tbname = parts[2];
	        	   String cols = parts[3];
	        	   proxy.createTable(dbname, tbname,cols);
	        	   response = dbname+"."+tbname;
	        	   continue;     	   
	           }
	           
	           if (parts[0].equals("addTuple") && proxySet) {
	        	   String [] values = parts[1].split(" ");
	        	   
	        	   proxy.addTuple(values);
	        	   response = parts[1];
	        	   continue;     	   
	           }
	           
	           if (parts[0].equals("rmTuple") && proxySet) {
	        	   String filter = parts[1];
	        	   
	        	   proxy.rmTuple(filter);
	        	   response = filter;
	        	   continue;     	   
	           }
	           
	           if (parts[0].equals("getContent") && proxySet) {
	        	   String dbname = parts[1];
	        	   String tbname = parts[2];
	        	   
	        	   response = proxy.getContent(dbname, tbname);
	        	   
	        	   continue;     	   
	           }
	           
	           //String response = sentence.toUpperCase();
	           
	           
	           sendData = response.getBytes();          
	           DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	           serverSocket.send(sendPacket); 
		   }
		   
   }

}
