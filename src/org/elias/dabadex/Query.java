package org.elias.dabadex;

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
		lg.log(Level.FINE,"Sending query: " + p.getQuery());
		if (!p.getStoredProcedure()) {
			rs = st.executeQuery(p.getQuery());
		} else {
			rs = conn.prepareCall(p.getQuery()).executeQuery();
		}
		dt = new DataReadIn(rs);
		//st.close();
		//conn.close();
	}
	
	public void exportToCSV() throws IOException {
		dt.writeOutCSV(p.getExportFile());
	}
	
	
	

}
