public class DBMSTest {

	private static String [] scenario = {
			"createDB myDB",
			"createTable myDB myTable",
			"addTuple myDB myTable 1 name1",
			"addTuple myDB myTable 2 name2",
			"fetch myDB myTable",
			"rmTuple myDB myTable 1",
			"fetch myDB myTable",
			"deleteTable myDB myTable"
	};
	
	
	public static void main(String args[]) throws Exception
	   {
		DBProxy dbp1 = new MySQLProxy("com.mysql.jdbc.Driver");
    	//DBProxy dbp2 = new MySQLProxy("org.mariadb.jdbc.Driver");
    	//DBProxy dbp3 = new MongoProxy();
    	//DBProxy dbp4 = new CassandraProxy();
		
		dbp1.runScenario("172.17.0.2", scenario);
	   }

}
