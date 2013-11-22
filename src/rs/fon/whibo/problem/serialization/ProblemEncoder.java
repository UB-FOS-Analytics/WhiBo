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
package rs.fon.whibo.problem.serialization;

import java.io.FileOutputStream;

import rs.fon.whibo.problem.Problem;

import com.thoughtworks.xstream.XStream;

/**
 * Utill class that encode from Problem to .wba file.
 * 
 * @author Nikola Nikolic
 */
public class ProblemEncoder {

	/**
	 * 
	 * Method that encode from Problem to .wba file.
	 * 
	 * @param xmlFile
	 *            path to .wba file
	 * @param problem
	 *            {@link Problem}
	 * @return {@link String} simply returns string path to .wba file that was
	 *         just encoded to.
	 */
	// public static String encodeFormProcesToXML(Problem problem, String
	// xmlFile) {
	// try {
	// //this line points to Problem class loader - s.velickovic
	// Thread.currentThread().setContextClassLoader(Problem.class.getClassLoader());
	//
	// FileOutputStream out = new FileOutputStream(xmlFile);
	//
	// XMLEncoder xml = new XMLEncoder(out);
	// xml.writeObject(problem);
	// xml.close();
	//
	// return xmlFile;
	//
	// } catch (Exception exc) {
	// exc.printStackTrace();
	// }
	//
	// return null;
	// }
	//
	public static String encodeFormProcesToXML(Problem problem, String xmlFile) {
		try {

			Thread.currentThread().setContextClassLoader(
					Problem.class.getClassLoader());

			FileOutputStream out = new FileOutputStream(xmlFile);
			XStream xs = new XStream();
			xs.toXML(problem, out);

		} catch (Exception exc) {
			exc.printStackTrace();
		}

		return null;
	}

}
