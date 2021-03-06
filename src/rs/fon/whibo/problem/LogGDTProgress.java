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

/**
 * Logger for GDT alghoritm and components
 * 
 * @author Nenad Zdravkovic
 * 
 */
public class LogGDTProgress {

	/** Variable which specifies if logging is turned on or off */
	private static boolean logIsEnabled = false;

	private static LogProgress gdtInstance;

	private static LogProgress CreateInstance() {
		if (logIsEnabled)
			return new LogProgress("GDTExperimentLog.csv");
		else
			return new LogProgress();
	}

	/**
	 * Get instance of GDT logger
	 * 
	 * @return GDT logger
	 */
	public static LogProgress getLog() {
		if (gdtInstance == null)
			gdtInstance = CreateInstance();
		return gdtInstance;
	}
}
