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
package rs.fon.whibo.GC.problem.subproblem;

import java.util.List;

import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.tools.ReflectionTools;

/**
 * This subproblem defines GUI properties for possible split components
 * 
 * @author Milan Vukicevic
 */
public class DistanceMeasure implements Subproblem {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1608912330876289331L;

	/** The name. */
	private String name = "Distance measure";;

	/** The description. */
	private String description = "Distance measure";

	/**
	 * The available implementation class names define concrete components that
	 * could be used for solving subproblem.
	 */
	private String[] availableImplementationClassNames = ReflectionTools
			.getComponentClassNamesForSubproblem(rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure.class);

	/*
	 * = { Euclidian.class.getName(), CityBlok.class.getName(),
	 * Chebychev.class.getName(), Camberra.class.getName(),
	 * CosineSimilarity.class.getName(), JaccardNumerical.class.getName(),
	 * CorrelationNumerical.class.getName(), DiceNumerical.class.getName(),
	 * DTWNumerical.class.getName(), InnerProduct.class.getName(),
	 * MaxProduct.class.getName(), OverlapNumerical.class.getName(), };
	 */

	/**
	 * The multiple property defines f more than one component can be selected
	 * through GUI for solving subproblem.
	 */
	private boolean multiple = false;

	/**
	 * The step data keeps concrete component that is selected through GUI in
	 * runtime.
	 */
	private SubproblemData stepData;

	/**
	 * The list of step data keeps concrete components that are selected through
	 * GUI in runtime if subproblem is multiple.
	 */
	private List<SubproblemData> listOfStepData;

	/**
	 * Sets the available implementation class names that will be available
	 * through GUI.
	 * 
	 * @param availableImplementationClassNames
	 *            that are tested and ready to use
	 */
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

	public SubproblemData getSubproblemData() {
		return stepData;
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

	public void setSubproblemData(SubproblemData stepData) {
		this.stepData = stepData;

	}

	public void setMultipleSubproblemData(List<SubproblemData> listOfStepData) {
		this.listOfStepData = listOfStepData;

	}

	public SubproblemData getStepData() {
		return stepData;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setStepData(SubproblemData stepData) {
		this.stepData = stepData;

	}

	public List<SubproblemData> getMultipleStepData() {
		return listOfStepData;
	}

	public List<SubproblemData> getMultipleSubproblemData() {
		return listOfStepData;

	}

	public void setMultipleStepData(List<SubproblemData> listOfStepData) {
		this.listOfStepData = listOfStepData;

	}

}
