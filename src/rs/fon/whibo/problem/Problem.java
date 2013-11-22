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
 * Every problem should implement this interface and define it's subproblems;
 * 
 * @author Nikola Nikolic
 * 
 */
public interface Problem extends Serializable {

	/**
	 * Gets the subproblems of a problem.
	 * 
	 * @return subproblems
	 */
	public List<Subproblem> getSubproblems();

	/**
	 * Sets subproblems
	 * 
	 * @param subproblems
	 */
	public void setSubproblems(List<Subproblem> subproblems);

	/**
	 * gets Problem description
	 */
	public String getDescription();

	/**
	 * sets Problem description
	 */
	public void setDescription(String decription);

	/**
	 * sets Problem name
	 */
	public void setName(String name);

	/**
	 * gets Problem name
	 */
	public String getName();
}
