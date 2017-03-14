public class DBMSTest {

	private static String [] scenario = {
			"createDB myDB",
			"createTable myDB myTable",
			"addTuple myDB myTable 1 name1",
			"addTuple myDB myTable 2 name2",
			"fetch myDB myTable",
			"rmTuple myDB myTable 1",
			"fetch myDB myTable",
			"updateTuple myDB myTable 2 name3",
			"fetch myDB myTable",
			"deleteTable myDB myTable",
			"deleteDB myDB"
	};
	
	
	public static void main(String args[]) throws Exception
	   {
		DBProxy dbp = new MySQLProxy("com.mysql.jdbc.Driver");
    	//DBProxy dbp = new MySQLProxy("org.mariadb.jdbc.Driver");
    	//DBProxy dbp = new MongoProxy();
    	//DBProxy dbp = new CassandraProxy();
		
		dbp.runScenario("172.17.0.2", scenario);
	   }

}
