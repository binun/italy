import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Utils {
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
		
		System.out.println("       EXECUTE " + command);
		
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
