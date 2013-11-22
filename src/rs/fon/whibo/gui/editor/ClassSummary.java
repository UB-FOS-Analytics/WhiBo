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
package rs.fon.whibo.gui.editor;

/**
 * Defines structure of class context information
 * 
 * @author Nenad Zdravkovic
 */

public class ClassSummary {
	private String componentName = "";
	private String comment = "";
	private String author = "";
	private String date = "";

	public void setComponentName(String componentNameToSet) {
		componentName = componentNameToSet;
	}

	public void setComment(String commentToSet) {
		comment = commentToSet;
	}

	public void setAuthor(String authorToSet) {
		author = authorToSet;
	}

	public void setDate(String dateToSet) {
		date = dateToSet;
	}

	public String getComponentName() {
		return componentName;
	}

	public String getComment() {
		return comment;
	}

	public String getAuthor() {
		return author;
	}

	public String getDate() {
		return date;
	}
}