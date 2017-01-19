import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Utils {
	
	public static final int DBMS_REFRESH = 5000; //milliseconds
	
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
