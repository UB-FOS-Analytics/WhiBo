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

package rs.fon.whibo.gui.util;

import java.util.List;

/**
 * Class that provides some neat tools.
 * 
 * @author Sasa Mrkela
 */
public class Helper {

	/**
	 * Turns an array of objects to an array of classes. It is assumed that
	 * objects represent string for which the classes can be created using the
	 * <code>Class.forName(String class)</code> method.
	 * 
	 * @param objects
	 *            objects to convert
	 * @return an array of classes
	 * @throws java.lang.ClassNotFoundException
	 *             if class cannot be created for a certain string, that is
	 *             object
	 */

	@SuppressWarnings("unchecked")
	public static Class[] objectsToClasses(Object[] objects)
			throws ClassNotFoundException {
		Class[] classes = new Class[objects.length];
		for (int i = 0; i < objects.length; i++) {
			classes[i] = Class.forName((String) objects[i]);
		}
		return classes;
	}

	/**
	 * Empties a list of elements
	 * 
	 * @param list
	 * @return empty list
	 */
	public static List<?> cleanList(List<?> list) {
		for (int i = 0; i < list.size(); i++) {
			list.remove(i);
		}
		return list;
	}

	/**
	 * Get the short name of the specified class by striping off the package
	 * name.
	 * 
	 * @param classname
	 *            Class name.
	 * @return Short class name.
	 */
	public static String stripPackageName(final String classname) {
		int idx = classname.lastIndexOf(".");
		if (idx != -1) {
			return classname.substring(idx + 1, classname.length());
		}
		return classname;
	}

}
