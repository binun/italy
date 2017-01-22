import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MariaDBProxy extends DBProxy {
  
    private Connection connection = null;
    
	public MariaDBProxy() {
		port=3306;
		username = "root";
		password = "root";
		startDB = "information_schema";
		//connect("localhost");
	}
	@Override
	public boolean connect(String hostName) {
		//String hostName = DBUtils.execCommand("./docker-ip.sh " + replicaName)[0]; 
		System.out.println("MySQL JDBC Connection");

		  try {
			  Class.forName("com.mysql.jdbc.Driver").newInstance();
		  } catch (ClassNotFoundException e) {
			  System.out.println("Where is your MySQL JDBC Driver?");
			  e.printStackTrace();
			  return false;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}

		System.out.println("MySQL JDBC Driver Registered!");
		String connStr = String.format("jdbc:mysql://%s:%d/%s", hostName,port,startDB); 
		
		try {
			connection = DriverManager.getConnection(connStr,username, password);

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return false;
		}
		return true;
		
	}

	@Override
	public Object createDB(String dbName) {
		Statement st;
		int result;
		try {
			st = (Statement) connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		}
		try {
			result=st.executeUpdate("CREATE DATABASE " + dbName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		}
		lastDB = (Object)dbName;
		return lastDB;
	}

	@Override
	public Object createTable(String tbName, String columns) {
		Statement st;
		int result;
		try {
			st = (Statement) connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		}
		try {
			result=st.executeUpdate(String.format("CREATE TABLE %s.%s(%s)", (String)lastDB,tbName,columns));
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		}
		lastTable = (Object)tbName;
		return lastTable;
	}

	@Override
	public void addTuple(String[] values) {
		System.out.println("Inserting records into the table...");
		Statement st = null;
		try {
			st = (Statement) connection.createStatement();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
	      
		String joined = Utils.join(",", values);
	    String sql = String.format("INSERT INTO %s.%s VALUES (%s)", (String)lastDB,(String)lastTable,joined);
	                  
	    try {
			st.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}
	
	}

	@Override
	public void rmTuple(String filter) {
		System.out.println("Removing records from the table...");
		Statement st = null;
		try {
			st = (Statement) connection.createStatement();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
	      
		
	    String sql = String.format("DELETE FROM %s.%s WHERE %s", this.lastDB,this.lastTable,filter);
	                  
	    try {
			st.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}
	}
	@Override
	public Object createTable(String tbName) {
		String columnDef = "";
		
		for (int i=0; i < columns.length; i++) {
			columnDef = columnDef + columns[i] + " int";
			if (i!=columns.length-1)
				columnDef = columnDef + ",";
		}
	   		
		return this.createTable(tbName, columnDef);
	}
	
	@Override
	public Object createTable(String dbName, String tbName, String columns) {
		lastDB = dbName;
		return this.createTable(tbName, columns);
	}
	
	@Override
	public String getContent(String dbName, String tbName) {
		String query = String.format("select %s from %s.%s;", Utils.join(",",this.columns), dbName,tbName);
		Statement st = null;
		String result = "";
		try {
		     st = (Statement) connection.createStatement();
		     ResultSet rs = st.executeQuery(query);
		     while (rs.next())
		     {
		      for (String col: this.columns)
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

}
