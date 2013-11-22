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

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Abstract class which defines basic structure and behavior of log classes Logs
 * are defined as tree structure. Each log is leaf and can have child leafs.
 * 
 * @author Nenad Zdravkovic
 * 
 */
@SuppressWarnings("serial")
public abstract class Log implements Serializable {

	/**
	 * Child logs
	 */
	private LinkedList<Log> childLogs;

	/**
	 * Id of current node
	 */
	private String id;

	/**
	 * Delimiter for tab delimited formatting
	 */
	protected static final String DELIMITER = "\t";

	/**
	 * Leaf Constructor
	 * 
	 * @param typeOfLog
	 *            Type of log
	 */
	public Log() {
		childLogs = new LinkedList<Log>();
	}

	/**
	 * Constructor
	 * 
	 * @param typeOfLog
	 *            Type of log
	 * @param isRoot
	 *            Is log root log
	 */
	public Log(boolean isRoot) {
		childLogs = new LinkedList<Log>();
		if (isRoot) {
			id = "root";
		}
	}

	/**
	 * Add child log to current log
	 * 
	 * @param log
	 */
	public void add(Log log) {
		int size = childLogs.size() + 1;
		log.id = id + "_" + size;
		childLogs.add(log);
	}

	/**
	 * Get current leaf basic info (without child logs info)
	 * 
	 * @return Current leaf basic info
	 */
	protected abstract String getOutput();

	/**
	 * Get current leaf full string output
	 * 
	 * @return Current leaf full info (including children info)
	 */
	public LinkedList<String> getOutputs() {
		LinkedList<String> records = new LinkedList<String>();
		String logOutput = getOutput();
		records.add(logOutput);

		// log children
		for (Log log : childLogs) {
			LinkedList<String> logRecords = log.getOutputs();
			records.addAll(logRecords);
		}
		return records;
	}

	/**
	 * Get common output header
	 * 
	 * @return Common output header
	 */
	protected String getOutputHeader() {
		return getOutputHeader(null);
	}

	/**
	 * Get common output header
	 * 
	 * @param categoryName
	 *            Category name of specific component
	 * @return
	 */
	protected String getOutputHeader(String categoryName) {
		String output = id + DELIMITER + this.getClass().getSimpleName()
				+ DELIMITER;
		if (categoryName != null) {
			output += categoryName + DELIMITER;
		}
		return output;
	}

}
