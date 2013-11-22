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

import java.io.FileInputStream;

import rs.fon.whibo.problem.Problem;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 
 * Utility class that decode from .wba to Problem object.
 * 
 * @author Nikola Nikolic
 */
public class ProblemDecoder {

	/**
	 * 
	 * Method that decode from from .wba to Problem object.
	 * 
	 * @param xmlFile
	 *            path to .wba file
	 * @return {@link rs.fon.whibo.problem.Problem}
	 * @throws Exception
	 */
	public static <T> rs.fon.whibo.problem.Problem decodeFromXMLToProces(
			String xmlFile) {
		try {
			// this line points to Problem class loader - s.velickovic
			Thread.currentThread().setContextClassLoader(
					Problem.class.getClassLoader());

			FileInputStream in = new FileInputStream(xmlFile);

			XStream xs = new XStream(new DomDriver());

			rs.fon.whibo.problem.Problem process = (rs.fon.whibo.problem.Problem) xs
					.fromXML(in);

			return process;

		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return null;
	}

	// Old version of decode method

	// public static <T> rs.fon.whibo.problem.Problem decodeFromXMLToProces(
	// String xmlFile) {
	// try {
	// //this line points to Problem class loader - s.velickovic
	// Thread.currentThread().setContextClassLoader(Problem.class.getClassLoader());
	//
	//
	// FileInputStream in = new FileInputStream(xmlFile);
	//
	// XMLDecoder xml = new XMLDecoder(in);
	// rs.fon.whibo.problem.Problem process = (rs.fon.whibo.problem.Problem)
	// xml.readObject();
	//
	// return process;
	//
	// } catch (Exception exc) {
	// exc.printStackTrace();
	// }
	//
	// return null;
	//
	// }

}
