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

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Statistics;

/**
 * 
 * 
 * @author Milos Jovanovic
 * @componentName Leaf Label Confidence
 */
public class LeafLabelConfidence extends AbstractStopingCriteria {

	/** User parameter that defines the minimal node size. */
	@Parameter(defaultValue = "0.95", minValue = "0", maxValue = "1")
	private Double labelConfidence;

	/**
	 * Instantiates a new min node size component.
	 * 
	 * @param parameters
	 *            the user supplied parameters
	 */
	public LeafLabelConfidence(
			List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
		labelConfidence = Double.parseDouble(parameters.get(0)
				.getXenteredValue());
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
		String labelValue = majorityClassName(exampleSet);

		double totalSize = exampleSet.size();
		double majorityClassSize = exampleSet.getStatistics(exampleSet
				.getAttributes().getLabel(), Statistics.COUNT, labelValue);

		double confidence = majorityClassSize / totalSize;

		if (confidence >= labelConfidence)
			return true;
		else
			return false;
	}

	private String majorityClassName(ExampleSet exampleSet) {

		Attribute labelAttribute = exampleSet.getAttributes().getLabel();

		int maxCount = 0;
		String maxCountLabel = null;

		for (String value : labelAttribute.getMapping().getValues()) {

			int count = (int) exampleSet.getStatistics(labelAttribute,
					Statistics.COUNT, value);
			if (count > maxCount) {
				maxCount = count;
				maxCountLabel = value;
			}
		}

		return maxCountLabel;
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
