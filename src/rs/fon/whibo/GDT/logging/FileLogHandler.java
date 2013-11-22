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

package rs.fon.whibo.GDT.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 * Class that handles writing and reading logs from file
 * 
 * @author Nenad Zdravkovic
 * 
 */
public class FileLogHandler {

	/**
	 * New line string
	 */
	private static final String NEW_LINE = "\n";

	/**
	 * Formating info for date-time part of filename
	 */
	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd_HH-mm-ss-SSS";

	/**
	 * Physical path to directory which will contain logs
	 */
	private static final String LOG_LOCATION = ""; // e.g. "C:\\WhiBo_Logs\\";

	/**
	 * Write log as tab separated values file
	 * 
	 * @param records
	 *            Lines of text to save
	 * @return Filename of saved file
	 * @throws IOException
	 */
	public static String writeAsTSV(LinkedList<String> records)
			throws IOException {
		String fileName = LOG_LOCATION + "GDTLog_" + getDateTimeNowFormated()
				+ ".tsv";
		try {
			File file = new File(fileName);
			BufferedWriter output;
			if (file.exists()) {
				throw new IOException(
						"Log file with specified name already exist.");
			} else {
				file.createNewFile();
				output = new BufferedWriter(new FileWriter(file));
			}
			for (String string : records) {
				output.write(string);
				output.write(NEW_LINE);
			}
			output.flush();
			output.close();
			return fileName;
		} catch (IOException e) {
			System.out.print("Error in log progress writing: ");
			System.out.println(e.getMessage());
			throw e;
		}
	}

	/**
	 * Serializes log instance to file
	 * 
	 * @param log
	 *            Log which should be serialized
	 * @return File name of saved file
	 * @throws IOException
	 */
	public static String serialize(Log log) throws IOException {
		String fileName = LOG_LOCATION + "GDTlog_" + getDateTimeNowFormated()
				+ ".wsl";
		try {
			ObjectOutput out = new ObjectOutputStream(new FileOutputStream(
					fileName));
			out.writeObject(log);
			out.close();
			return fileName;
		} catch (IOException e) {
			System.out.print("Error during serialzation of logs: ");
			System.out.println(e.getMessage());
			throw e;
		}
	}

	/**
	 * Deserializes log from file
	 * 
	 * @param serializedFileName
	 *            File name which contain serialized log
	 * @return Deserialized log instance
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Log deserialze(String serializedFileName) throws IOException,
			ClassNotFoundException {
		File file = new File(serializedFileName);
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					file));
			Log log = (Log) in.readObject();
			in.close();
			return log;
		} catch (IOException e) {
			System.out.println("Error during reading or deserializing file: "
					+ serializedFileName);
			System.out.println(e.getMessage());
			throw e;
		} catch (ClassNotFoundException e) {
			System.out.println("Error during reading or deserializing file: "
					+ serializedFileName);
			System.out.println(e.getMessage());
			throw e;
		}
	}

	/**
	 * Get current moment in specific format
	 * 
	 * @return formatted date and time
	 */
	private static String getDateTimeNowFormated() {
		Date dateTime = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(dateTime);
	}
}
