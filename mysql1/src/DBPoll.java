import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBPoll {
	
	//private static String[] containerNames = DBUtils.execCommand("docker ps -a | awk '{if(NR>1) print $NF}'");
	//private static String username = "debian-sys-maint";
	//private static String password = "";
	
	private static String username = "root";
	private static String password = "root";
	private static String dbName = "information_schema";
	private static String host = "172.17.0.2";
	private static int port = 3306;
	
	public static void main (String [] args) {
	  System.out.println("MySQL JDBC Connection Testing");

	  try {
		  Class.forName("com.mysql.jdbc.Driver").newInstance();
	  } catch (ClassNotFoundException e) {
		  System.out.println("Where is your MySQL JDBC Driver?");
		  e.printStackTrace();
		  return;
	} catch (InstantiationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	System.out.println("MySQL JDBC Driver Registered!");
	Connection connection = null;
	String connStr = String.format("jdbc:mysql://%s:%d/%s", host,port,dbName); 

	try {
		connection = DriverManager.getConnection(connStr,username, password);

	} catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
		return;
	}

	if (connection != null) {
		System.out.println("You made it, take control your database now!");
	} else {
		System.out.println("Failed to make connection!");
		return;
	}
	
	Statement st = null;
	int result;
	
	/*try {
		st = (Statement) connection.createStatement();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	
	}
	try {
		result=st.executeUpdate("CREATE DATABASE mydata;");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		
	}*/
	
	try {
		st = (Statement) connection.createStatement();
	} catch (SQLException e) {
	
	}
	try {
		result=st.executeUpdate("CREATE DATABASE mydata.mytabl(id int, surid int);");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		
	}
	
	// Get rows from the table userpasses
	String query = "select id,surid from mydb.mytable;";
	
    
    try {
     st = (Statement) connection.createStatement();
     ResultSet rs = st.executeQuery(query);
     while (rs.next())
     {
      String uid= rs.getString("id");
      String pass = rs.getString("surid");
      
      
      // print the results
      System.out.format("%s, %s\n", uid, pass);
    }
    st.close();
  }
  catch (Exception e)
  {
    System.err.println("Got an exception! ");
    System.err.println(e.getMessage());
  }
    
  // Add rows to the table userpasses
 try {
	Statement createStatement = (Statement) connection.createStatement();
	String sql = "INSERT INTO userpasses VALUES ('user1', 'pass1')";
	// same for delete
    createStatement.executeUpdate(sql);
 } catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 
   try {
	connection.close();
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
    
  }
}