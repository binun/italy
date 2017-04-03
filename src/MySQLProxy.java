import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.HashMap;


public class MySQLProxy extends DBProxy {
  
    private Connection connection = null;
    private String driverKind = "";
    private String columnsDef;
    //String [] cols = new String[2];
    
	// MySQLProxy("debian-sys-maint", "","com.mysql.jdbc.Driver")
	// MySQLProxy("root", "root","org.mariadb.jdbc.Driver")
	public MySQLProxy(String driver) {
		
		super(3306, "information_schema");
		columnsDef = this.colIDs[0] + " int," + this.colIDs[1] + " varchar(20),primary key("+this.colIDs[0] + ")";
		
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
        
        posTraits = new HashMap<String, String>()
        {
          {
            put("createDB", "ok");
        	put("deleteDB", "ok");
            put("createTable", "ok");
        	put("deleteTable", "ok");
            put("fetch", "ok");
            put("addTuple", "ok");
        	put("updateTuple", "ok");
            put("rmTuple", "ok");
          }
        };
        	    
        negTraits = new HashMap<String, String>()
        {
          {
        	put("createDB", "ok");
        	put("deleteDB", "ok");
        	put("createTable", "ok");
        	put("deleteTable", "ok");
        	put("fetch", "ok");
        	put("addTuple", "ok");
        	put("updateTuple", "ok");
            put("rmTuple", "ok");
          }
       };
	}
	
	public String toString() {
		return "MySQLProxy " + driver; 
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
	public String createDB(String dbName) {
		Statement st;
	
		
		try {
			st = (Statement) connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return Utils.FAIL;
		}
		try {
			int result=st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
			System.out.println("MYSQL DB created... " + dbName);
			return Utils.OK;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("MYSQL DB CREATFAILURE... " + dbName);
			return Utils.FAIL;
		}
		
	}

	@Override
	public String createTable(String dbname,String tbName) {
		Statement st;
		try {
			st = (Statement) connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return Utils.FAIL;
		}
		try {
			st.executeUpdate(String.format("CREATE TABLE IF NOT EXISTS %s.%s(%s)", dbname,tbName,columnsDef));		
			System.out.println("MYSQL Table created... " + tbName);
			return Utils.OK;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("MYSQL Table CREATFAILURE... " + tbName);
			return Utils.FAIL;
		}
	}

	@Override
	public String addTuple(String dbname,String tbName,String[] values) {
		System.out.println("Inserting records into the table...");
		
		String sql = String.format("LOCK TABLES %s.%s WRITE; INSERT INTO %s.%s VALUES (?,?);UNLOCK TABLES;", dbname,tbName,dbname,tbName);
	    try {
	       PreparedStatement preparedStmt = connection.prepareStatement(sql);
	       preparedStmt.setInt (1, Integer.valueOf(values[0]));
	       preparedStmt.setString (2, values[1]);
	       preparedStmt.execute();
	       System.out.println("MYSQL Tuple added");
	       return Utils.OK;
	    }
	    catch (Exception e) {
	    	System.out.println("MYSQL Tuple ADDFAILURE");
	    	return Utils.FAIL;
	    }
	    
	}
	
	@Override
	public String updateTuple(String dbName, String tbName, String [] values) {
       System.out.println("Updating records in the table...");
		
		String sql = String.format("LOCK TABLES %s.%s WRITE;UPDATE %s.%s SET NAME=? WHERE ID=?;UNLOCK TABLES;", dbName,tbName,dbName,tbName);
	    try {
	       PreparedStatement preparedStmt = connection.prepareStatement(sql);
	       
	       preparedStmt.setString (1, values[1]);
	       preparedStmt.setInt (2, Integer.valueOf(values[0]));
	       preparedStmt.execute();
	       System.out.println("MYSQL Tuple modified");
		   return Utils.OK;
	    }
	    catch (Exception e) {
	    	System.out.println("MYSQL Tuple MODFAILURE");
	    	return Utils.FAIL;
	    }
	}

	@Override
	public String rmTuple(String dbname,String tbName, String filter) {
		System.out.println("Removing records from the table...");
		Statement st = null;
		try 
		{
			st = (Statement) connection.createStatement();
		} 
		catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			return Utils.FAIL;
		}
	      
		
	    String sql = String.format("LOCK TABLES %s.%s WRITE;DELETE FROM %s.%s WHERE id=%s;UNLOCK TABLES;", dbname,tbName,dbname,tbName,filter);
	                  
	    try 
	    {
			st.executeUpdate(sql);
			System.out.println("MYSQL Tuple removed");
			return Utils.OK;
		} 
	    catch (Exception e) 
	    {
			// TODO Auto-generated catch block
			//e.printStackTrace();
	    	System.out.println("MYSQL Tuple REMFAILURE");
			return Utils.FAIL;
		}
	}
	
	@Override
	public String fetch(String dbName, String tbName) {
		String query = String.format("LOCK TABLES %s.%s WRITE;SELECT * FROM %s.%s;UNLOCK TABLES;", dbName,tbName,dbName, tbName);
		Statement st = null;
		String result = "";
		//String [] cols = {"id","name"};
		try {
		     st = (Statement) connection.createStatement();
		     ResultSet rs = st.executeQuery(query);
		     while (rs.next())
		     {
		    	 
		      for (String col: this.colIDs)
		    	 result = result + " " + rs.getString(col);
		     }  
		     st.close();
		  }
		  catch (Exception e)
		  {
		    System.err.println("Got an exception! " + e.getMessage());
		    return Utils.FAIL;
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
	public String deleteTable(String dbName,String tbName) {
		  System.out.println("Deleting table in given database...");
		  Statement stmt = null;
		  
	      try {
			stmt = connection.createStatement();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			return Utils.FAIL;
		}
	      
	      String sql = "DROP TABLE IF EXISTS " + dbName + "." + tbName;
	 
	      try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return Utils.FAIL;
		}
	    System.out.println("Table deleted "+ tbName);
		return Utils.OK;
	}
	@Override
	public String deleteDB(String dbName) {
		Statement st;
		try {
			st = (Statement) connection.createStatement();
			st.executeUpdate("DROP DATABASE IF EXISTS " + dbName);
			System.out.println("DB deleted "+ dbName);
		} catch (Exception e) {
			
			return Utils.FAIL;
		}
		
		return Utils.OK;
	}

	@Override
	protected String filterFetch(String result) {
		// TODO Auto-generated method stub
		return result;
	}

}
