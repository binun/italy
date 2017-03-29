import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Utils {
	
	public static final int DBMS_REFRESH = 5000; //milliseconds
	//public static Integer [] ports = {3306,27017,7199,7000,7001,9160,9042};
	//public static Integer [] ports = {3306,7000,7001,7199,9042,9160,27017};
	public static Integer [] ports = {7000,7001,7199,9042,9160};
    public static String prefixIP = "172.17.0.";
    public static int startReplica = 1;
    public static int DBPORT = 6666;
    public static int REPLICAPORT = 5555;
    public static final String OK = "OK";
    public static final String FAIL = "FAIL";
    
    public static final int CLIENT_COLLECT_DELAY = 1000;
    public static final int REPLICA_COLLECT_DELAY = 200;
    public static final int DB_IS_RESPONDING = 500;
	
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
	
	public static Set<String> availableServices() {
		Set<String> hostSet = new HashSet<String>();
		for (Integer port : ports) {
			for (int r = 1; r < 10; r++) {
				String ip = prefixIP + ((Integer)(startReplica+r)).toString();
				if (Utils.portIsOpen(ip, port)) {
					
					hostSet.add(ip);
					//System.out.println(ip + ":" + port + " responds");		
				}
				//else System.out.print(ip + ":" + port + " FAILS");
			}
			//System.out.println("");
		}
		return hostSet;
	}
	
	public static String [] getReplicaIPs(String hostsFile) {
		String strLine;
	    String [] hosts;
		ArrayList<String> hostlist = new ArrayList<String>();
	
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(hostsFile)));
  
			while ((strLine = br.readLine()) != null)   {
	            //System.out.println(strLine);
	            
				//String [] components = strLine.split(" ");
				hostlist.add(strLine); 
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
	
	public static String [] execCommandArr(String command) {
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
	
	public static void execCommand(String command) throws IOException {
		StringBuffer sb = new StringBuffer();
		
		try 
		{	       
		     Process p = Runtime.getRuntime().exec(command);
		     p.waitFor();
		     //BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		     //String line = "";
		     //while ((line = in.readLine()) != null) {
		        //System.out.println(line); 
		    	//output.append(line);
		     //}
		       
	        p.destroy();
		    //in.close();
		} 
		catch (Exception ex) 
		{
		    ex.printStackTrace();
		}
		
		//return Utils.OK;
		
	  }
	
	  private String mostFrequent(String [] arr, float fraction) 
	  {
	    Map<String, Integer> m = new HashMap<String, Integer>();

	    for (String a : arr) {
	        Integer freq = m.get(a);
	        m.put(a, (freq == null) ? 1 : freq + 1);
	    }

	    int max = -1;
	    String mostFrequent = "";

	    for (Map.Entry<String, Integer> e : m.entrySet()) {
	        if (e.getValue() > max) {
	            mostFrequent = e.getKey();
	            max = e.getValue();
	        }
	    }
	    
	    if ((float)(max/arr.length) >=fraction)
	       return mostFrequent;
	    else
	       return "";
	}
	  
    public String unify(String [] arr, float fraction) {
    	if (fraction==0) {
    		String total = "";
    		for (String s : arr)
    			total=total+s+" ";
    		return total;
    	}
    	else
    		return mostFrequent(arr,fraction);
    		
    }


}
