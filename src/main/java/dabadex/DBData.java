package dabadex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBData {
	private String dburl;
	private String table;
	private String user;
	private String pass;
	private String driver;
	private Logger lg;
	private Connection conn = null;

	public DBData(String dburl, String table, String user, String pass,
			String driver) throws ClassNotFoundException, SQLException {
		this.dburl = dburl;
		this.table = table;
		this.user = user;
		this.pass = pass;
		this.driver = driver;
		this.lg = Start.LOGGER;
		lg.log(Level.INFO,"Trying to connect to database...");
		connect();
		lg.log(Level.INFO,"Connection established to: " + dburl);
	}
	
	private void connect() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		this.conn = DriverManager.getConnection(dburl,user,pass);
	}

	public Connection getConn() {
		return conn;
	}

	public String getDburl() {
		return dburl;
	}

	public String getTable() {
		return table;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public String getDriver() {
		return driver;
	}
}
