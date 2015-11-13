package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractDBConnector {
	
	Connection con;
	private final static String dbUrl = Constants.DB_URL;
	private final static String dbClass = Constants.JDBC_DRIVER;
	private final static String userName = Constants.DBUSER;
	private final static String password = Constants.DBPWD;
	
	
	public boolean connectDB() {
		try {
			Class.forName(dbClass);	
			con = DriverManager.getConnection (dbUrl, userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean closeDB() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
}
