/*
 *  WhiBo
 *
 *  Copyright (C) 2010- by WhiBo development team and the contributors
 *
 *  Complete list of developers available at our web site:
 *
 *       http://www.whibo.fon.bg.ac.rs
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package rs.fon.whibo.problem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Logger specialized for loging in csv format.
 * 
 * @author Nenad Zdravkovic
 * 
 */
public class LogProgress {

	private static final String NEW_LINE = "\n";
	private static final String SEPARATOR = ",";

	/** Log file. */
	private File file;

	/** Log output. */
	private BufferedWriter output;

	/** Variable used to determine if logging is turned on or off */
	private Boolean loggingIsEnabled = false;

	/**
	 * Instantiates a new log.
	 * 
	 * @param filePath
	 *            the file path. File path must be valid (directory location of
	 *            file must exist), otherwise loging will be disabled
	 */
	public LogProgress(String filePath) {
		try {
			file = new File(filePath);
			if (file.exists()) {
				output = new BufferedWriter(new FileWriter(file, true));
			} else {
				file.createNewFile();
				output = new BufferedWriter(new FileWriter(file));
			}
			loggingIsEnabled = true;
		} catch (Exception e) {
			System.out.print("Error in log progress writing: ");
			System.out.println(e.getMessage());
			System.out
					.println("Logging progress is being disabled due to an error.");
			loggingIsEnabled = false;
		}
	}

	/** Dummy constructor. Use if logging should be disabled */
	public LogProgress() {
		loggingIsEnabled = false;
	}

	/**
	 * Cleaning up.
	 */
	@Override
	protected void finalize() throws Throwable {
		try {
			closeFile();
		} finally {
			super.finalize();
		}
	}

	/**
	 * Close file.
	 */
	private void closeFile() {
		try {
			output.close();

		} catch (IOException ioe) {
			System.out.print("Error in log writing: ");
			System.out.println(ioe.getMessage());
		}
	}

	/**
	 * Actual writing to file.
	 * 
	 * @param entry
	 *            entry to write
	 */
	private void WriteToFile(String entry) {
		if (loggingIsEnabled) {
			try {
				output.write(entry);
				output.flush();

			} catch (IOException ioe) {
				System.out.print("Error in log writing: ");
				System.out.println(ioe.getMessage());
			}
		}
	}

	/**
	 * Write entry to log.
	 * 
	 * @param appendNewLineBefore
	 *            append line before entry
	 * @param entry
	 *            entry to write
	 * @param appendNewLineAfter
	 *            append line after entry
	 */
	public void log(boolean appendNewLineBefore, String entry,
			boolean appendNewLineAfter) {
		if (appendNewLineBefore)
			WriteToFile(NEW_LINE);

		WriteToFile(entry);

		if (appendNewLineAfter)
			WriteToFile(NEW_LINE);
	}

	/**
	 * Write entry to log.
	 * 
	 * @param appendNewLineBefore
	 *            append line before entry
	 * @param entry
	 *            entry to write
	 */
	public void log(boolean appendNewLineBefore, String entry) {
		log(appendNewLineBefore, entry, false);
	}

	/**
	 * Write entry to log.
	 * 
	 * @param entry
	 *            entry to write
	 * @param appendNewLineAfter
	 *            append line after entry
	 */
	public void log(String entry, boolean appendNewLineAfter) {
		log(false, entry, appendNewLineAfter);
	}

	/**
	 * Append new line to log.
	 */
	public void appendNewLine() {
		log(NEW_LINE);
	}

	/**
	 * Write entry to log.
	 * 
	 * @param entry
	 *            entry to write
	 */
	public void log(long entry) {
		log(String.valueOf(entry));
	}

	/**
	 * Write entry to log.
	 * 
	 * @param entry
	 *            entry to write
	 */
	public void log(Double entry) {
		log(String.valueOf(entry));
	}

	/**
	 * Write entry to log.
	 * 
	 * @param entry
	 *            entry to write
	 */
	public void log(String entry) {
		log(false, entry, false);
	}

	/**
	 * Write collection of entries to one line in log.
	 * 
	 * @param entries
	 *            collection of entries to write
	 */
	public void log(String... entries) {
		String stringToLog = new String();
		for (String entry : entries) {
			stringToLog += entry + SEPARATOR;
		}
		log(stringToLog);
	}

	/**
	 * Write collection of entries to one line in log.
	 * 
	 * @param entries
	 *            collection of entries to write
	 */
	public void log(ArrayList<?> entries) {
		String stringToLog = new String();
		for (Object entry : entries) {
			stringToLog += String.valueOf(entry.toString()) + SEPARATOR;
		}
		log(stringToLog);
	}
}