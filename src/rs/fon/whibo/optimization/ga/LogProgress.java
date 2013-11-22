package rs.fon.whibo.optimization.ga;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jgap.Gene;

import rs.fon.whibo.optimization.ga.rapidminer.EAOperator;

public class LogProgress {

	private static LogProgress instance;
	private File file;
	private BufferedWriter output;

	public int numReturnedFromCache = 0;
	public int numEvaluations = 0;
	private Date startTime;
	private Date endTime;

	public static LogProgress getInstance() {
		if (instance == null) {
			instance = new LogProgress();
		}
		return instance;
	}

	public void reset() {
		instance = new LogProgress();
	}

	public void log(String message) {

		try {
			output.write(message + "\n");
			output.flush();

		} catch (IOException ioe) {
			System.out.print("Error in log writing: ");
			System.out.println(ioe.getMessage());
		}

	}

	public void log(Gene[] genes) {
		String output = "";
		for (Gene gene : genes) {
			if (!(output == ""))
				output = output + ", ";
			output = output + gene.getAllele().toString();
		}
		log(output);
	}

	public void log(Gene[] genes, double performance) {
		String output = "";
		for (Gene gene : genes) {
			if (!(output == ""))
				output = output + ", ";
			output = output + gene.getAllele().toString();
		}
		log(output + "," + Double.valueOf(performance));
	}

	private LogProgress() {
		try {

			file = new File(EAOperator.logFilePath);

			if (file.exists()) {
				output = new BufferedWriter(new FileWriter(file, true));
				log("===================================");
				log("===================================");
			} else {
				file.createNewFile();
				output = new BufferedWriter(new FileWriter(file));
				log("RIA, CreateSplit, EvaluateSplit, StoppingCriteria, Prune, Performance");
			}

		} catch (IOException ioe) {
			System.out.print("Error in log writing: ");
			System.out.println(ioe.getMessage());
		}
	}

	/*
	 * public void closeLog(){ try { output.close();
	 * 
	 * }catch(IOException ioe){ System.out.print("Error in log writing: ");
	 * System.out.println(ioe.getMessage()); } }
	 */

	public void recordStatistics() {

		log("Number of values returned from cache: " + numReturnedFromCache);
		log("Number of evaluations of fitness function: " + numEvaluations);

	}

	public void clearStatistic() {
		numReturnedFromCache = 0;
		numEvaluations = 0;
	}

	public void startStopWatch() {
		startTime = new Date();
	}

	public void splitTimeStopWatch() {
		endTime = new Date();
	}

	public String elapsedTime() {
		DateFormat df = new SimpleDateFormat("mm:ss");
		Date diff = new Date(endTime.getTime() - startTime.getTime());
		return df.format(diff);
	}

	public void closeLog() {
		try {
			output.close();
		} catch (IOException ioe) {

		}
	}

}
