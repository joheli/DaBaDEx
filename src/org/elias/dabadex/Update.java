package org.elias.dabadex;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Update {

	private String[] dataRow;
	private DBData dbd;
	private ParametersP p;
	private Connection conn;
	private Statement st;
	private Logger lg;
	public static int errors = 0;
	public static int updates = 0;
	public static int inserts = 0;
	public static int keeps = 0;

	public Update(String[] dataRow, ParametersP p) throws SQLException {
		this.dataRow = dataRow;
		this.lg = Start.LOGGER;
		this.dbd = p.getdBData();
		this.conn = dbd.getConn();
		this.st = conn.createStatement();
		this.p = p;
	}

	public void relayCommand() {
		String sql = null;
		try {
			if (p.getKey() == null) {
				sql = generateInsertCommand();
				inserts++;
			} else {
				if (keyPresent()) {
					if (p.getAction().equals("update")) {
						sql = generateUpdateCommand();
						updates++;
					} else {
						// do nothing, keep old record
						keeps++;
					}
				} else {
					sql = generateInsertCommand();
					inserts++;
				}
			}
			if (sql == null) {
				lg.log(Level.FINE, "No SQL command to relay (option 'keep').");
			} else {
				lg.log(Level.FINE,
						"Relaying SQL command: " + conn.nativeSQL(sql));
			}
			if (sql != null)
				st.executeUpdate(sql);
		} catch (SQLException s) {
			errors++;
			if (sql != null) {
				if (sql.startsWith("I"))
					inserts--;
				if (sql.startsWith("U"))
					updates--;
			}
			lg.log(Level.SEVERE, s.getMessage());
		}
	}

	private boolean keyPresent() throws SQLException {
		boolean result = false;
		String sql = "SELECT COUNT(*) FROM " + dbd.getTable() + " WHERE "
				+ sqlKey();
		ResultSet r = st.executeQuery(sql);
		r.next();
		if (r.getInt(1) > 0)
			result = true;
		lg.log(Level.FINE, "Query '" + sql + "' retrieves result: " + result);
		return result;
	}

	private String generateUpdateCommand() {
		String sql = "UPDATE " + dbd.getTable() + " SET ";
		for (int i = 0; i < dataRow.length; i++) {
			sql = sql + p.getFields().get(i) + " = ";
			if (dataRow[i].trim().equals("")) {
				sql = sql + "NULL";
			} else {
				if (p.getNumeric().get(i)) {
					sql = sql + dataRow[i];
				} else {
					sql = sql + encapsulateString(dataRow[i]);
				}
			}
			if (i < (dataRow.length - 1))
				sql = sql + ",";
		}
		sql = sql + " WHERE " + sqlKey();
		return sql;
	}

	/* Generates sql string to insert after "WHERE" clause */
	private String sqlKey() {
		String sql = "";
		ArrayList<Integer> k = p.getKey();

		for (int i = 0; i < k.size(); i++) {

			int keyPosition = k.get(i) - 1;
			String fieldName = p.getFields().get(keyPosition);
			String fieldValue = dataRow[keyPosition];

			sql = sql + fieldName + " = ";

			if (p.getNumeric().get(keyPosition)) {
				sql = sql + fieldValue;
			} else {
				sql = sql + encapsulateString(fieldValue);
			}

			if (i < (k.size() - 1))
				sql = sql + " AND ";
		}
		// lg.log(Level.FINEST,sql);
		return sql;
	}

	private String generateInsertCommand() {
		String sql = "INSERT INTO " + dbd.getTable() + " (";
		for (String f : p.getFields()) {
			sql = sql + f;
			if (p.getFields().indexOf(f) < (p.getFields().size() - 1))
				sql = sql + ",";
		}
		sql = sql + ") VALUES (";
		for (int i = 0; i < dataRow.length; i++) {
			if (dataRow[i].trim().equals("")) {
				sql = sql + "NULL";
			} else {
				if (p.getNumeric().get(i)) {
					sql = sql + dataRow[i];
				} else {
					sql = sql + encapsulateString(dataRow[i]);
				}
			}
			if (i < (dataRow.length - 1))
				sql = sql + ",";
		}
		sql = sql + ")";
		return sql;
	}

	private String encapsulateString(String s) {
		return encapsulateString(s, "'");
	}

	private String encapsulateString(String s, String withWhat) {
		return (withWhat + s + withWhat);
	}

}
