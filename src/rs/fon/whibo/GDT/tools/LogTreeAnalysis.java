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
package rs.fon.whibo.GDT.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogTreeAnalysis {

	private File file;
	private BufferedWriter output;

	public LogTreeAnalysis(String filePath) throws Exception {
		try {
			file = new File(filePath);

			if (file.exists()) {
				output = new BufferedWriter(new FileWriter(file, true));
			} else {
				file.createNewFile();
				output = new BufferedWriter(new FileWriter(file));
				writeToFile(TreeAnalysis.getValuesDescription() + "\n");
			}

		} catch (IOException ioe) {
			System.out.print("Error in log writing: ");
			System.out.println(ioe.getMessage());
		}

	}

	public void logPerformance(TreeAnalysis treeAnalysis) {
		writeToFile(treeAnalysis.getValues() + "\n");
	}

	private void writeToFile(String entry) {
		try {
			output.write(entry);
			output.flush();

		} catch (IOException ioe) {
			System.out.print("Error in log writing: ");
			System.out.println(ioe.getMessage());
		}
	}

	public void closeLog() {
		try {
			output.close();

		} catch (IOException ioe) {
			System.out.print("Error in log writing: ");
			System.out.println(ioe.getMessage());
		}
	}
}
