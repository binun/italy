import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;


public class MySQLProxy extends DBProxy {
  
    private Connection connection = null;
    private String driverKind = "";
    String [] cols = new String[2];
    
	// MySQLProxy("debian-sys-maint", "","com.mysql.jdbc.Driver")
	// MySQLProxy("root", "root","org.mariadb.jdbc.Driver")
	public MySQLProxy(String driver) {
		
		super(3306, "information_schema");
		columns = "id int, name varchar(20),primary key(id)";
		this.driver = driver;
		if (driver.contains("mariadb")) {
			driverKind="mariadb";
			this.username = "root";
			this.password = "root";
		}
		
        if (driver.contains("mysql")) {
        	driverKind="mysql";
        	this.username = "debian-sys-maint";
    		this.password = "";
		}	
        
       String[] r = columns.split(", ");
       String [] cd1 = r[0].split(" ");
       String [] cd2 = r[1].split(" ");
       cols[0] = cd1[0];
       cols[1] = cd2[0];
	}
	@Override
	public boolean connect(String hostName)  {
		//String hostName = DBUtils.execCommand("./docker-ip.sh " + replicaName)[0]; 

		if (connected)
			return true;
		
		System.out.println("MySQL JDBC Connection");
        try {
			Class.forName(driver).newInstance();
		} catch (Exception e) {
			
			return false;
		}
		
        
		System.out.println("MySQL JDBC Driver Registered!");
		//String connStr = String.format("jdbc:mysql://%s:%d/%s", hostName,port,startDB); 
	
		String connStr = String.format("jdbc:%s://%s:%d/%s", driverKind, hostName,port,startDB); 
		try {
			connection = DriverManager.getConnection(connStr,username, password);
		} catch (SQLException e) {
			
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
			result=st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		System.out.println("DB created...");
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
			st.executeUpdate(String.format("CREATE TABLE IF NOT EXISTS %s.%s(%s)", dbname,tbName,columns));		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		System.out.println("Table created ...");
		return true;
	}

	@Override
	public boolean addTuple(String dbname,String tbName,String[] values) {
		System.out.println("Inserting records into the table...");
		
		String sql = String.format("INSERT INTO %s.%s VALUES (?,?)", dbname,tbName);
	    try {
	       PreparedStatement preparedStmt = connection.prepareStatement(sql);
	       preparedStmt.setInt (1, Integer.valueOf(values[0]));
	       preparedStmt.setString (2, values[1]);
	       preparedStmt.execute();
	    }
	    catch (Exception e) {
	    	return false;
	    }
	    System.out.println("Tuple added");
	    return true;
	}
	
	@Override
	public boolean updateTuple(String dbName, String tbName, String id, String name) {
       System.out.println("Updating records in the table...");
		
		String sql = String.format("UPDATE %s.%s SET NAME=? WHERE ID=?", dbName,tbName);
	    try {
	       PreparedStatement preparedStmt = connection.prepareStatement(sql);
	       
	       preparedStmt.setString (1, name);
	       preparedStmt.setInt (2, Integer.valueOf(id));
	       preparedStmt.execute();
	    }
	    catch (Exception e) {
	    	return false;
	    }
	    System.out.println("Tuple modified");
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
			//e1.printStackTrace();
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
	    System.out.println("Tuple erased");
	    return true;
	}
	
	@Override
	public String fetch(String dbName, String tbName) {
		String query = String.format("select * from %s.%s;", dbName,tbName);
		Statement st = null;
		String result = "";
		//String [] cols = {"id","name"};
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
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			return false;
		}
	      
	      String sql = "DROP TABLE " + dbName + "." + tbName;
	 
	      try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
	    System.out.println("Table deleted "+ tbName);
		return true;
	}
	@Override
	public boolean deleteDB(String dbName) {
		Statement st;
		try {
			st = (Statement) connection.createStatement();
			st.executeUpdate("DROP DATABASE " + dbName);
			System.out.println("DB deleted "+ dbName);
		} catch (Exception e) {
			
			return false;
		}
		
		return true;
	}

}
