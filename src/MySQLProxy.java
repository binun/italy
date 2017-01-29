import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySQLProxy extends DBProxy {
  
    private Connection connection = null;
    
	// MySQLProxy("debian-sys-maint", "","com.mysql.jdbc.Driver")
	// MySQLProxy("root", "root","org.mariadb.jdbc.Driver")
	public MySQLProxy(String username, String password, String driver) {
		
		super(3306, "information_schema");
		this.username = username;
		this.password = password;
		this.driver = driver;
		columns = "id int not null, name varchar(20) not null,primary key(id)";
	}
	@Override
	public boolean connect(String hostName)  {
		//String hostName = DBUtils.execCommand("./docker-ip.sh " + replicaName)[0]; 
		boolean res = false;
		if (connected)
			return true;
		
		System.out.println("MySQL JDBC Connection");
        try {
			Class.forName(driver).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		
        
		System.out.println("MySQL JDBC Driver Registered!");
		//String connStr = String.format("jdbc:mysql://%s:%d/%s", hostName,port,startDB); 
		String driverKind = driver.split(".")[1];
		String connStr = String.format("jdbc:%s://%s:%d/%s", driverKind, hostName,port,startDB); 
		try {
			connection = DriverManager.getConnection(connStr,username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		
		connected = true;
	    return true;
		
	}

	@Override
	public boolean createDB(String dbName) {
		Statement st;
		int result;
		
		try {
			st = (Statement) connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		try {
			result=st.executeUpdate("CREATE DATABASE " + dbName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean createTable(String dbname,String tbName) {
		Statement st;
		try {
			st = (Statement) connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		try {
			st.executeUpdate(String.format("CREATE TABLE %s.%s(%s)", dbname,tbName,columns));		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean addTuple(String dbname,String tbName,String[] values) {
		System.out.println("Inserting records into the table...");
		Statement st = null;
		try {
			st = (Statement) connection.createStatement();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
	      
		String joined = Utils.join(",", values);
	    String sql = String.format("INSERT INTO %s.%s VALUES (%s)", dbname,tbName,joined);
	                  
	    try {
			st.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
	    return true;
	}

	@Override
	public boolean rmTuple(String dbname,String tbName, String filter) {
		System.out.println("Removing records from the table...");
		Statement st = null;
		try {
			st = (Statement) connection.createStatement();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
	      
		
	    String sql = String.format("DELETE FROM %s.%s WHERE id=%s", dbname,tbName,filter);
	                  
	    try {
			st.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	    return true;
	}
	
	@Override
	public String fetch(String dbName, String tbName) {
		String query = String.format("select * from %s.%s;", dbName,tbName);
		Statement st = null;
		String result = "";
		String [] cols = {"id","name"};
		try {
		     st = (Statement) connection.createStatement();
		     ResultSet rs = st.executeQuery(query);
		     while (rs.next())
		     {
		    	 
		      for (String col: cols)
		    	 result = result + " " + rs.getString(col);
		     }  
		     st.close();
		  }
		  catch (Exception e)
		  {
		    System.err.println("Got an exception! ");
		    System.err.println(e.getMessage());
		  }
		 return result;
	}
	@Override
	public boolean disconnect() {
		if (connection != null) {
	        try {
	            connection.close();
	        } catch (Exception e) { /* ignored */}
	    }
		return connected;
		
	}
	@Override
	public boolean deleteTable(String dbName,String tbName) {
		  System.out.println("Deleting table in given database...");
		  Statement stmt = null;
	      try {
			stmt = connection.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
	      
	      String sql = "DROP TABLE " + dbName + "." + tbName;
	 
	      try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	      System.out.println("Table  deleted in given database...");
		return true;
	}

}
