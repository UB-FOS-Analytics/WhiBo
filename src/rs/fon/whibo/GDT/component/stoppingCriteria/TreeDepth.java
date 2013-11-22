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
package rs.fon.whibo.GDT.component.stoppingCriteria;

import java.util.Date;
import java.util.List;

import rs.fon.whibo.problem.Parameter;

import com.rapidminer.example.ExampleSet;

/**
 * This component stops growth of decision tree when the maximum tree depth has
 * been reached. Decision tree in progress, maximum tree depth. Built decision
 * tree. This criteria should used when it is important to build trees that
 * shouldn’t have more than a specified depth. In some cases, this approach can
 * prevent overfitting and result with better classification accuracy. This
 * stopping criterion is used in almost implementations of all decision tree
 * classifiers.
 * 
 * @author Milan Vukicevic, Milos Jovanovic
 * @componentName Tree Depth
 */
public class TreeDepth extends AbstractStopingCriteria {

	/** User parameter that defines the maximal tree depth. */
	@Parameter(defaultValue = "10", minValue = "1", maxValue = "100")
	private Integer Tree_Depth;

	/**
	 * Instantiates a new tree depth component.
	 * 
	 * @param parameters
	 *            the user supplied parameters
	 */
	public TreeDepth(List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
		Tree_Depth = Integer.parseInt(parameters.get(0).getXenteredValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.whibo.GDT.component.stoppingCriteria.StoppingCriteria#
	 * evaluateStoppingCriteria(com.rapidminer.example.ExampleSet, int,
	 * java.util.Date)
	 */
	@Override
	public boolean evaluateStoppingCriteria(ExampleSet exampleSet, int depth,
			Date startDate) {
		return depth >= Tree_Depth;
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
