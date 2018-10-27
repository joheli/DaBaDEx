package org.elias.dabadex;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.elias.utils.Utils;

public class ParametersP extends Parameters implements DaBaDExParam {
	File dataFile;
	private ArrayList<String> fields = new ArrayList<String>();
	private ArrayList<Boolean> numeric = new ArrayList<Boolean>();
	private ArrayList<Integer> key = new ArrayList<Integer>();
	String table;
	String action;
	private DBData dBData;
	private String cronSchedule;
	private Integer minTextRows = null;
	private String followUp = null;

	public ParametersP(File parameterFile) throws IOException,
			ParameterException, ClassNotFoundException, SQLException {
		super(parameterFile, new String[] { "dataFile", "fields", "dburl",
				"table", "user", "pass", "driver" });
		attachParameters();
		lg.log(Level.INFO, "Parameter file ok.");
	}

	public String getTable() {
		return table;
	}

	public File getDataFile() {
		return dataFile;
	}

	public ArrayList<String> getFields() {
		return fields;
	}

	public ArrayList<Boolean> getNumeric() {
		return numeric;
	}

	public ArrayList<Integer> getKey() {
		return key;
	}

	public String getAction() {
		return action;
	}

	public DBData getdBData() {
		return dBData;
	}

	public String getCronSchedule() {
		return cronSchedule;
	}

	public String getFollowUp() {
		return followUp;
	}

	private void attachParameters() throws ClassNotFoundException, SQLException {
		attachDataFile();
		if (parameters.get("key") == null) {
			key = null;
		} else {
			for (String s : parameters.get("key").split(",")) {
				this.key.add(Integer.valueOf(s));
			}
		}
		this.action = (parameters.get("action") == null) ? "update"
				: parameters.get("action");
		this.table = parameters.get("table");
		fieldsAndNumeric(parameters.get("fields"));
		attachDBData();
		this.cronSchedule = parameters.get("cronSchedule");
		this.minTextRows = Integer.valueOf(parameters.get("minTextRows"));
		this.followUp = parameters.get("followUp");
	}

	private void attachDataFile() {
		File f = new File(parameters.get("dataFile"));
		if (f.isDirectory()) {
			this.dataFile = Utils.getLatestFile(FileUtils.listFiles(f, null,
					false));
			lg.log(Level.FINE,
					"Parameter 'dataFile' corresponds to a directory: the newest file ("
							+ f.getAbsolutePath() + ") is chosen.");
		} else {
			this.dataFile = f;
			lg.log(Level.FINE, "File " + f.getAbsolutePath()
					+ " is chosen for upload.");
		}
	}

	private void attachDBData() throws ClassNotFoundException, SQLException {
		this.dBData = new DBData(parameters.get("dburl"),
				parameters.get("table"), parameters.get("user"),
				parameters.get("pass"), parameters.get("driver"));
	}

	private void fieldsAndNumeric(String fieldStr) {
		String[] s = fieldStr.split(",");
		for (String f : s) {
			if (f.contains("'")) {
				this.numeric.add(Boolean.TRUE);
				this.fields.add(f.split("'")[0]);
			} else {
				this.numeric.add(Boolean.FALSE);
				this.fields.add(f);
			}
		}
	}
	
	public Integer getMinTextRows() {
		return minTextRows;
	}
	
	@Override
	public void reAttachParameters() throws ClassNotFoundException,
			SQLException {
		attachParameters();
	}
}
