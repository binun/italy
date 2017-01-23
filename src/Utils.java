import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Utils {
	
	public static final int DBMS_REFRESH = 5000; //milliseconds
	//public static Integer [] ports = {3306,27017,7199,7000,7001,9160,9042};
	public static Integer [] ports = {3306,27017};
    public static String prefixIP = "172.17.0.";
    public static int startReplica = 4;
	
	public static boolean portIsOpen(String ip, int port) {
		int timeout = 200;
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);
            socket.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
	
	public static List<String> availableServices() {
		ArrayList<String> services = new ArrayList<String>();
		for (Integer port : ports) {
			for (int r = 0; r < 10; r++) {
				String ip = prefixIP + ((Integer)(startReplica+r)).toString();
				if (Utils.portIsOpen(ip, port)) {
					services.add(ip+":"+port);
					System.out.print(ip + ":" + port + " responds");
				}
			}
			System.out.println("");
		}
		return services;
	}
	
	public static String [] getReplicaIPs(String hostsFile) {
		String strLine;
	    String [] hosts;
		ArrayList<String> hostlist = new ArrayList<String>();
	
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(hostsFile)));
  
			while ((strLine = br.readLine()) != null)   {
	            //System.out.println(strLine);
	            
				String [] components = strLine.split(" ");
				hostlist.add(components[1]); 
				//System.out.println(components[1]);
			}
			br.close();
		}
        
	   catch (Exception e) {
		   
	   }
	   String[] replicaIPs = (String[]) hostlist.toArray(new String[0]);
	   return replicaIPs;
	}
	
	public static String join(String separator, String [] values) {
		String result = "";
		
		for (int i=0; i<values.length;i++) {
			result=result+values[i];
			if (i<values.length-1)
				result=result+separator;
		}
		    
		return result;
		
	}
	
	public static String [] execCommand(String command) {
		String line="";
		ArrayList<String> result = new ArrayList<String>();
	    //String result="";
		
		//System.out.println("       EXECUTE " + command);
		
		try 
		{	       
		     Process p = Runtime.getRuntime().exec(command);
		     p.waitFor();
		     BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		     while ((line = in.readLine()) != null) {
		        //System.out.println(line); 
		    	result.add(line);
		     }
		       
	        p.destroy();
		    in.close();
		} 
		catch (Exception ex) 
		{
		    ex.printStackTrace();
		}
		//System.out.println("       RESPONSE " + command);
		return result.toArray(new String[0]);
	  }
}
