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
 * The Class SplitEvaluation.
 * 
 * @author Nikola Nikolic
 */
public class SplitEvaluation implements Subproblem {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -158567947783080631L;

	/** The name. */
	private String name = "Evaluate split";;

	/** The description. */
	private String description = "Evaluate split";

	/**
	 * The available implementation class names define concrete components that
	 * could be used for solving subproblem.
	 */
	private String[] availableImplementationClassNames = ReflectionTools
			.getComponentClassNamesForSubproblem(rs.fon.whibo.GDT.component.splitEvaluation.SplitEvaluation.class);

	/**
	 * The multiple property defines if more than one component can be selected
	 * through GUI for solving subproblem.
	 */
	private boolean multiple = false;

	/**
	 * The step data keeps concrete component that is selected through GUI in
	 * runtime.
	 */
	private SubproblemData stepData;

	/** The list of step data. */
	private List<SubproblemData> listOfStepData;

	/**
	 * Sets the available implementation class names.
	 * 
	 * @param availableImplementationClassNames
	 *            the new available implementation class names
	 */
	public void setAvailableImplementationClassNames(
			String[] availableImplementationClassNames) {
		this.availableImplementationClassNames = availableImplementationClassNames;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.whibo.problem.Subproblem#getAvailableImplementationClassNames()
	 */
	public String[] getAvailableImplementationClassNames() {
		return availableImplementationClassNames.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.whibo.problem.Subproblem#getName()
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.whibo.problem.Subproblem#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.whibo.problem.Subproblem#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.whibo.problem.Subproblem#setDescription(java.lang.String)
	 */
	public void setDescription(String decription) {
		this.description = decription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.rapidminer.process.Step#getStepData()
	 */

	public SubproblemData getSubproblemData() {
		return this.stepData;
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
		return this.listOfStepData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.rapidminer.process.Step#setMultipleStepData(java.util.List)
	 */

	public void setMultipleStepData(List<SubproblemData> listOfStepData) {
		this.listOfStepData = listOfStepData;

	}

	public void setMultipleSubproblemData(List<SubproblemData> listOfStepData) {
		this.listOfStepData = listOfStepData;

	}

}
