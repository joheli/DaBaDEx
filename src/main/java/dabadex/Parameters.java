package dabadex;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

public class Parameters {

	protected File parameterFile;
	protected String comment = "#";
	protected String[] requiredParameters = null;
	protected HashMap<String, String> parameters;
	protected Logger lg;

	public Parameters(File parameterFile, String[] requiredParameters)
			throws IOException, ParameterException, ClassNotFoundException,
			SQLException {
		this.parameterFile = parameterFile;
		this.lg = Start.LOGGER;
		this.requiredParameters = requiredParameters;
		lg.log(Level.INFO, "Reading parameters...");
		readParameters();
	}

	protected void readParameters() throws IOException, ParameterException,
			ClassNotFoundException, SQLException {
		this.parameters = readMap(parameterFile, "=", comment);
		checkParameters();
	}

	private void checkParameters() throws ParameterException {
		if (this.parameters == null || parameters.isEmpty()) {
			throw new ParameterException("No parameters available!");
		} else {
			boolean allOk = true;
			if (requiredParameters != null) {
				for (String p : requiredParameters) {
					if (!this.parameters.containsKey(p))
						allOk = false;
				}
			}
			if (!allOk)
				throw new ParameterException(
						"Not all required parameters were specified!");
		}
	}

	private HashMap<String, String> readMap(File f, String sep, String comm)
			throws IOException {
		HashMap<String, String> h = new HashMap<String, String>();
		List<String> lines = FileUtils.readLines(f);
		Iterator<String> lineIt = lines.iterator();
		while (lineIt.hasNext()) {
			String line = lineIt.next().trim();
			if (!line.isEmpty() && !line.startsWith(comm)) {
				StringTokenizer sT = new StringTokenizer(line, sep);
				if (sT.countTokens() > 1) {
					h.put(sT.nextToken().trim(), sT.nextToken().trim());
				}
			}
		}
		return h;
	}

}