package dabadex;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import utils.Utils;

public class Start implements Runnable {
	public final static String version = "0.0.9";
	public final static Logger LOGGER = Logger.getLogger("DaBaDExLog");
	public static int cycle = 1;
	private DaBaDExParam p;
	private ParametersP pP;
	private ParametersG pG;
	private char mode;
	private File parameterFile;
	private String cronSchedule;

	public Start() throws SecurityException, IOException {
		FileHandler fH = new FileHandler("DaBaDEx.log", true);
		fH.setFormatter(new SimpleFormatter());
		LOGGER.addHandler(fH);
		LOGGER.setLevel(levelFromEnvironment()); 
	}
	
	private Level levelFromEnvironment() {
	  Level lvl = Level.INFO; // this is the default value.
	  String lvl_e = System.getenv("DBDX_LOGLEVEL"); // if not set it will be null
	  if (lvl_e != null) {
	    try {
	      lvl = Level.parse(lvl_e);
	      LOGGER.log(Level.INFO, "'DBDX_LOGLEVEL' was set to " + lvl_e + ".");
	    } catch (Exception e) {
	      LOGGER.log(Level.INFO, "Content of 'DBDX_LOGLEVEL' (" + lvl_e + ") could not be successfully translated to valid log level.");
	    } // if it doesn't work don't bother further.
	  } else {
	    LOGGER.log(Level.INFO, "Environment variable 'DBDX_LOGLEVEL' was not set. Using default level.");
	  }
	  return lvl;
	}

	public static void main(String[] args) {
		Start.LOGGER.log(Level.INFO, "This is DaBaDEx " + Start.version
				+ " by J. Elias");
		if (args.length > 1) {
			try {
				Start s = new Start();
				s.mode = args[0].charAt(1);
				s.parameterFile = new File(args[1]);
				s.attachParameters();
				s.extractCronSchedule();
				if (s.cronSchedule != null) {
					// Schedule
					Scheduler scheduler = new Scheduler();
					scheduler.schedule(s.cronSchedule, s);
					scheduler.start();
				} else {
					s.run();
				}
			} catch (Exception e) {
				Start.LOGGER.log(Level.SEVERE, e.getMessage());
				e.printStackTrace();
			}
		} else {
			Start.LOGGER
					.log(Level.SEVERE,
							"\nPlease provide arguments specifying mode and parameter file!");
			Start.help();
			System.exit(1);
		}
	}

	public void run() {
		try {
			if (cycle > 1)
				p.reAttachParameters();
			switch (mode) {
			case 'p':
				if (checkMinTextRows()) {
					DataReadIn dt = new DataReadIn(pP.getDataFile());
					for (String[] z : dt) {
						Update u = new Update(z, pP);
						u.relayCommand();
					}
					closeJob();
				} else {
					Start.LOGGER.log(
							Level.INFO,
							"Process aborted, as number of rows in "
									+ pP.getDataFile().getAbsolutePath()
									+ " is lower than minTextRows ("
									+ pP.getMinTextRows() + ")");
				}
				break;
			case 'g':
				Query qu = new Query(pG);
				qu.exportToCSV();
				closeJob();
				break;
			default:
				help();
				break;
			}
			
			if (p.getFollowUp() !=  null) {
				Utils.exec(p.getFollowUp());
			}
			
			cycle++;
		} catch (Exception e) {
			Start.LOGGER.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
	}

	private void attachParameters() throws IOException, ParameterException,
			ClassNotFoundException, SQLException {
		switch (mode) {
		case 'p':
			this.pP = new ParametersP(parameterFile);
			this.p = this.pP;
			break;
		case 'g':
			this.pG = new ParametersG(parameterFile);
			this.p = this.pG;
			break;
		default:
			help();
			break;
		}
	}

	private void closeJob() throws SQLException {
		p.getdBData().getConn().close();
		finalMessage();
	}

	private void extractCronSchedule() {
		this.cronSchedule = p.getCronSchedule();
	}

	private boolean checkMinTextRows() throws IOException {
		boolean pass = true;
		if (pP.getMinTextRows() != null) {
			pass = (Utils.textRowNum(pP.dataFile) >= pP.getMinTextRows());
		}
		return pass;
	}

	public static void help() {
		System.out.println("\nUse:");
		System.out.println("java -jar DaBaDEx.jar [-mode] [ParameterFile]");
		System.out.println("\t[-mode] can be '-p' (put) or '-g' (get)");
		System.out
				.println("\t[ParameterFile] is a textfile containing further arguments.\n");
	}

	private void finalMessage() {
		/*
		 * if (mode == 'p') { Start.LOGGER.log(Level.INFO,
		 * "Transfer completed (Mode 'p').\n\tNumber of inserts: " +
		 * Update.inserts + "\n\tNumber of updates: " + Update.updates +
		 * "\n\tNumber of unchanged records: " + Update.keeps +
		 * "\n\tNumber of errors: " + Update.errors); } else {
		 * Start.LOGGER.log(Level.INFO, "Transfer completed (Mode 'g')."); }
		 */
		LOGGER.log(Level.INFO,
				"Transfer completed (Mode '" + String.valueOf(mode) + "').");
	}

}
