package dabadex;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Query {
	private Connection conn;
	private Statement st;
	private Logger lg;
	private ParametersG p;
	private ResultSet rs;
	private DataReadIn dt;
	
	public Query(ParametersG p) throws SQLException {
		this.p = p;
		this.conn = p.getdBData().getConn();
		this.st = conn.createStatement();
		this.lg = Start.LOGGER;
		getData();
	}
	
	private void getData() throws SQLException {
	  String sql = p.getQuery();
		lg.log(Level.FINE,"Sending query: " + sql);
		if (!p.getStoredProcedure()) {
		  // The query can be an insert statement without data to be uploaded (e.g. insert data from existing table to other).
		  // These queries do not return a resultset and therefore generate an error.
		  // To avoid that queries that contain "insert" are processed using executeUpdate.
		  
		  // check if sql contains pattern 'insert'
		  if (sql.toLowerCase().contains("insert")) {
		    // if yes: process with executeUpdate
		    st.executeUpdate(sql);
		  } else {
		    // if no: process with executeQuery
			  rs = st.executeQuery(sql);
		  }
		} else {
			rs = conn.prepareCall(sql).executeQuery();
		}
		dt = new DataReadIn(rs);
		//st.close();
		//conn.close();
	}
	
	public void exportToCSV() throws IOException {
		dt.writeOutCSV(p.getExportFile());
	}
	
	
	

}
