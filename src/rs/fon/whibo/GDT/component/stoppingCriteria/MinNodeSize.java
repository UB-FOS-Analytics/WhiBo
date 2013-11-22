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
 * This component stops growth of decision tree on branches when there are not
 * enough cases for a node. This criteria should used when it is important to
 * build trees that should have nodes with a minimum number of cases. In some
 * cases, this can prevent overfitting This stop criterion can be used in all
 * decision tree classifiers.
 * 
 * @author Milos Jovanovic, Milan Vukicevic
 * @componentName Minimum node size
 */
public class MinNodeSize extends AbstractStopingCriteria {

	/** User parameter that defines the minimal node size. */
	@Parameter(defaultValue = "30", minValue = "1", maxValue = "1000")
	private Integer minSize;

	/**
	 * Instantiates a new min node size component.
	 * 
	 * @param parameters
	 *            the user supplied parameters
	 */
	public MinNodeSize(List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
		minSize = Integer.parseInt(parameters.get(0).getXenteredValue());
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
		boolean result = (exampleSet.size() < minSize);

		return result;
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
