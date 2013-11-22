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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Log persistence manager
 * 
 * @author Nenad Zdravkovic
 * 
 */
public class LogManager {

	/**
	 * Saves log
	 * 
	 * @param log
	 *            Log which should be saved
	 */
	public static void saveLog(Log log) {
		LinkedList<String> records = log.getOutputs();
		if (records.size() > 0) {
			try {
				FileLogHandler.writeAsTSV(records);
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				String wslFileName = FileLogHandler.serialize(log);
				System.out.println(wslFileName);
				// for testing purposes, reload log and write it again to file
				// reloadSerializedLog(wslFileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method for testing purposes Load saved log, and save it again
	 * 
	 * @param wslFileName
	 *            Serialized log file name
	 */
	private static void reloadSerializedLog(String wslFileName) {
		try {
			Log deserialzedLog = loadLog(wslFileName);
			FileLogHandler.writeAsTSV(deserialzedLog.getOutputs());
			FileLogHandler.serialize(deserialzedLog);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load log from file
	 * 
	 * @param fileName
	 *            Full file name
	 * @return Log instance
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Log loadLog(String fileName) throws IOException,
			ClassNotFoundException {
		Log deserialzedLog = FileLogHandler.deserialze(fileName);
		return deserialzedLog;
	}
}
