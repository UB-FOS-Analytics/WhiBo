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
 * Step interface defines methods that describe steps in process.
 * 
 * @author Nikola Nikolic
 * 
 */
public interface Subproblem extends Serializable {

	/**
	 * Gets name of the subproblem;
	 * 
	 * @return String name of subproblem;
	 */
	public abstract String getName();

	/**
	 * Sets name of the subproblem;
	 * 
	 * @param name
	 *            Name for the subproblem
	 */
	public abstract void setName(String name);

	/**
	 * Determine does this subproblem has many or just one implementation class
	 * to choose. For some subproblems require more then one implementation
	 * classes to do the job like Stopping Criteria can have many stopping
	 * criteria not just one.
	 * 
	 */
	public abstract boolean isMultiple();

	public abstract void setMultiple(boolean multiple);

	/**
	 * Gets implementation classes names to be shown on gui in order to be
	 * chosen
	 * 
	 * @return array of names of implementation classes;
	 */
	public String[] getAvailableImplementationClassNames();

	/**
	 * Sets subproblem data that describes the subproblem definition. Used to
	 * query the user for preferences.
	 */
	public void setSubproblemData(SubproblemData subproblemData);

	/**
	 * Gets subproblem data that describes the subproblem definition. Used to
	 * query the user for preferences.
	 * 
	 */
	public SubproblemData getSubproblemData();

	/**
	 * Sets the multiple subproblem data, used for the subproblems that have
	 * multiple components chosen to solve it.
	 * 
	 * @param listOfSubproblemData
	 *            {@link SubproblemData}
	 * 
	 */
	public void setMultipleSubproblemData(
			List<SubproblemData> listOfSubproblemData);

	/**
	 * Gets the multiple subproblem data, used for the subproblems that have
	 * multiple components chosen to solve it.
	 * 
	 * @return {@link SubproblemData}
	 */

	/**
	 * 
	 * gets Subproblem description
	 */
	public List<SubproblemData> getMultipleStepData();

	/**
	 * 
	 * gets Subproblem description
	 */
	public String getDescription();

	/**
	 * sets Subproblem description
	 */
	public void setDescription(String decription);

}
