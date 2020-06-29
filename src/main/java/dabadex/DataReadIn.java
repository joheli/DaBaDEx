package dabadex;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

public class DataReadIn extends ArrayList<String[]> {

	private Logger lg;
	Iterator<String[]> i = null;
	String[] line = null;
	private String[] fieldNames = null;

	public DataReadIn(File dataFile) throws IOException {
		this(dataFile, true, ";");
	}

	public DataReadIn(File dataFile, boolean hasHeader, String separator)
			throws IOException {
		this.lg = Start.LOGGER;
		lg.log(Level.FINE, "Reading dataFile " + dataFile.getAbsolutePath()
				+ " ...");
		List<String> lines = FileUtils.readLines(dataFile);
		Iterator<String> lineIt = lines.iterator();
		int i = 0; // count rows
		while (lineIt.hasNext()) {
			String[] fields = lineIt.next().split(separator);
			// check if size of fields is equal to that of first row (regarded
			// as reference)
			// if not --> attach empty Strings ("")
			if (i > 0) {
				if ((fields.length < this.get(0).length)) {
					fields = extendStringArray(fields, this.get(0).length
							- fields.length, "");
				}
			}
			this.add(fields);
			i++;
		}
		if (hasHeader) {
			this.fieldNames = this.get(0);
			this.remove(0);
		}
	}

	public DataReadIn(ResultSet rs) throws SQLException {
		this.lg = Start.LOGGER;
		int nCol = rs.getMetaData().getColumnCount();
		int nRow = 0;
		ArrayList<String> fn = new ArrayList<String>();
		for (int i = 1; i <= nCol; i++) {
			fn.add(rs.getMetaData().getColumnName(i));
		}
		this.fieldNames = fn.toArray(new String[fn.size()]);

		while (rs.next()) {
			String[] z = new String[nCol];
			for (int j = 1; j <= nCol; j++) {
				z[j - 1] = rs.getString(j);
			}
			this.add(z);
			nRow++;
		}
		lg.log(Level.FINE, nRow + " rows and " + nCol + " columns read in.");
	}

	public boolean empty() {
		return this.size() == 0;
	}

	public void first() {
		i = null;
		i = this.iterator();
		if (i.hasNext()) {
			line = this.get(0);
		} else {
			line = null;
		}
	}

	public boolean next() {
		if (i.hasNext()) {
			line = i.next();
			return true;
		} else {
			return false;
		}
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public void writeOutCSV(File outFile) throws IOException {
		writeOutCSV(outFile, ";");
	}

	public void writeOutCSV(File outFile, String separator) throws IOException {
		ArrayList<String> l = new ArrayList<String>();
		String fieldNameL = "";
		int j = 1;
		for (String fp : fieldNames) {
			fieldNameL = fieldNameL.concat(fp);
			if (j < fieldNames.length)
				fieldNameL = fieldNameL.concat(separator);
			j++;
		}
		l.add(fieldNameL);
		for (String[] z : this) {
			String zz = "";
			int i = 1;
			for (String zp : z) {
				if (zp == null)
					zp = "";
				zz = zz.concat(zp.trim());
				if (i < z.length)
					zz = zz.concat(separator);
				i++;
			}
			l.add(zz);
		}
		FileUtils.writeLines(outFile, l);
	}

	private String[] generateStringArray(int size, String prefill) {
		String[] s = new String[size];
		for (int i = 0; i < size; i++) {
			s[i] = prefill;
		}
		return s;
	}

	private String[] extendStringArray(String[] s, int add, String prefill) {
		String[] a = generateStringArray(s.length + add, prefill);
		for (int i = 0; i < s.length; i++) {
			a[i] = s[i];
		}
		return a;
	}
}
