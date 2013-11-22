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
package rs.fon.whibo.GC.component.StopCriteria;

import java.util.List;

import rs.fon.whibo.GC.algorithm.ClusterState;
import rs.fon.whibo.problem.Parameter;
import rs.fon.whibo.problem.SubproblemParameter;

public class MaxOptimizationSteps extends AbstractStopCriteria {

	/** Max runs parameter defines number algorithm's executions. */
	@Parameter(defaultValue = "3", minValue = "1", maxValue = "100000")
	private int Max_Optimization_Steps;

	public MaxOptimizationSteps(List<SubproblemParameter> parameters) {
		super(parameters);
		Max_Optimization_Steps = Integer.parseInt(parameters.get(0)
				.getXenteredValue());
	}

	public boolean shouldStop(ClusterState clusterState) {
		if (clusterState.getOptimizationStepNumber() >= Max_Optimization_Steps)
			return true;
		else
			return false;
	}

	/**
	 * Gets collection of class names of components that are not compatible with
	 * this component
	 * 
	 * @return collection of class names of not compatible components
	 */
	public String[] getNotCompatibleClassNames() {
		return null;
	}

	/**
	 * Gets collection of class names of components which are exclusive to this
	 * component
	 * 
	 * @return collection of class names of exclusive components
	 */
	public String[] getExclusiveClassNames() {
		return null;
	}
}
