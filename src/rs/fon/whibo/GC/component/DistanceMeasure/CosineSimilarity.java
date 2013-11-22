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
package rs.fon.whibo.GC.component.DistanceMeasure;

import java.util.List;

import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Tools;
import com.rapidminer.operator.OperatorException;

public class CosineSimilarity extends AbstractDistanceMeasure {
	/**
	 * Instantiates a new Cosine similarity measure.
	 * 
	 * @param parameters
	 *            the parameters
	 * */

	public CosineSimilarity(List<SubproblemParameter> parameters) {
		super(parameters);
	}

	public double calculateSimilarity(double[] value1, double[] value2) {
		double sum = 0.0;
		double sum1 = 0.0;
		double sum2 = 0.0;
		for (int i = 0; i < value1.length; i++) {
			double v1 = value1[i];
			double v2 = value2[i];
			if ((!Double.isNaN(v1)) && (!Double.isNaN(v2))) {
				sum += v2 * v1;
				sum1 += v1 * v1;
				sum2 += v2 * v2;
			}
		}
		if ((sum1 > 0) && (sum2 > 0))
			return sum / (Math.sqrt(sum1) * Math.sqrt(sum2));
		else
			return Double.NaN;
	}

	public double calculateDistance(double[] value1, double[] value2) {
		return Math.acos(calculateSimilarity(value1, value2));
	}

	// doing nothing
	public void init(ExampleSet exampleSet) throws OperatorException {
		Tools.onlyNumericalAttributes(exampleSet, "value based similarities");
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
