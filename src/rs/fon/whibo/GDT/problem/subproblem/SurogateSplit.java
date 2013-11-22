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
package rs.fon.whibo.GDT.problem.subproblem;

import java.util.List;

import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.tools.ReflectionTools;

/**
 * 
 * This subproblem defines GUI properties for surrogate split components
 * 
 * @author Nikola Nikolic
 * 
 */
public class SurogateSplit implements Subproblem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1346534853363302815L;

	private String name = "Create surogate split";;

	private String description = "Create surogate split";

	/**
	 * The available implementation class names define concrete components that
	 * could be used for solving subproblem.
	 */
	private String[] availableImplementationClassNames = ReflectionTools
			.getComponentClassNamesForSubproblem(rs.fon.whibo.GDT.component.surrogateSplit.SurogateSplit.class);

	/** The multiple property defines if more than one . */
	private boolean multiple = false;

	/**
	 * The multiple property defines if more than one component can be selected
	 * through GUI for solving subproblem.
	 */
	private SubproblemData stepData;

	private List<SubproblemData> listOfStepData;

	public void setAvailableImplementationClassNames(
			String[] availableImplementationClassNames) {
		this.availableImplementationClassNames = availableImplementationClassNames;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public String[] getAvailableImplementationClassNames() {
		return availableImplementationClassNames.clone();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String decription) {
		this.description = decription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.rapidminer.process.Step#getStepData()
	 */

	public SubproblemData getSubproblemData() {
		return stepData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.rapidminer.process.Step#isMultiple()
	 */

	public boolean isMultiple() {
		return multiple;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.rapidminer.process.Step#setStepData(rs.fon.rapidminer.process.
	 * StepData)
	 */

	public void setSubproblemData(SubproblemData stepData) {
		this.stepData = stepData;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.rapidminer.process.Step#getMultipleStepData()
	 */

	public List<SubproblemData> getMultipleStepData() {
		return listOfStepData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.rapidminer.process.Step#setMultipleStepData(java.util.List)
	 */

	public void setMultipleSubproblemData(List<SubproblemData> listOfStepData) {
		this.listOfStepData = listOfStepData;

	}

	public void setMultipleStepData(List<SubproblemData> listOfStepData) {
		this.listOfStepData = listOfStepData;

	}

}
