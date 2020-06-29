package dabadex;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;

public class ParametersG extends Parameters implements DaBaDExParam {

	private DBData dBData;
	private File exportFile;
	private String query;
	private String cronSchedule;
	private Boolean storedProcedure;
	private String followUp = null;

	public ParametersG(File parameterFile)
			throws IOException, ParameterException, ClassNotFoundException,
			SQLException {
		super(parameterFile, new String[] { "dburl", "user", "pass", "driver",
				"query", "exportFile","storedProcedure" });
		attachParameters();
	}

	public DBData getdBData() {
		return dBData;
	}

	public File getExportFile() {
		return exportFile;
	}

	public String getQuery() {
		return query;
	}

	public String getCronSchedule() {
		return cronSchedule;
	}

	public Boolean getStoredProcedure() {
		return storedProcedure;
	}
	
	public String getFollowUp() {
		return followUp;
	}

	private void attachParameters() throws ClassNotFoundException,
			SQLException, IOException {
		this.exportFile = new File(parameters.get("exportFile"));
		attachDBData();
		attachExportFile();
		attachSql();
		this.cronSchedule = parameters.get("cronSchedule");
		this.storedProcedure = Boolean.valueOf(parameters.get("storedProcedure"));
		this.followUp = parameters.get("followUp");
	}

	private void attachSql() throws IOException {
		String q = parameters.get("query");
		if (new File(q).isFile()) {
			this.query = FileUtils.readFileToString(new File(q));
		} else {
			this.query = q;
		}
	}

	private void attachExportFile() {
		File ef = new File(parameters.get("exportFile"));
		if (ef.isDirectory()) {
			ef = new File(ef.getParentFile().getAbsoluteFile() + File.pathSeparator + "temp.csv");
			lg.log(Level.FINE,
					"Since 'exportFile' is a directory, a temporary file is generated.");
		}
		this.exportFile = ef;
	}

	private void attachDBData() throws ClassNotFoundException, SQLException {
		this.dBData = new DBData(parameters.get("dburl"), null,
				parameters.get("user"), parameters.get("pass"),
				parameters.get("driver"));
	}

	@Override
	public void reAttachParameters() throws ClassNotFoundException,
			SQLException, IOException {
		attachParameters();
		
	}
}
