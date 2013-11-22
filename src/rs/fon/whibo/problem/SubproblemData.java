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

import java.io.Serializable;
import java.util.List;

/**
 * This class holds chosen implementation class of components for the
 * subproblem, and their list of Parameters.
 * 
 * @author Nikola Nikolic
 * 
 */
public class SubproblemData implements Serializable {

	private static final long serialVersionUID = -8655770995584187886L;
	private List<SubproblemParameter> listOfParameters;
	private String nameOfImplementationClass;

	/**
	 * This method returns list of {@link SubproblemParameter} objects
	 */
	public List<SubproblemParameter> getListOfParameters() {
		return listOfParameters;
	}

	/**
	 * This method sets list of {@link SubproblemParameter} objects
	 */
	public void setListOfParameters(List<SubproblemParameter> listOfParameters) {
		this.listOfParameters = listOfParameters;
	}

	/**
	 * This method returns name of implementation class
	 */
	public String getNameOfImplementationClass() {
		return nameOfImplementationClass;
	}

	/**
	 * This method sets name of implementation class
	 */
	public void setNameOfImplementationClass(String nameOfImplementationClass) {
		this.nameOfImplementationClass = nameOfImplementationClass;
	}

}
