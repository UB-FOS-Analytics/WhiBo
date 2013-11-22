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

import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.problem.AbstractComponent;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;

public abstract class AbstractDistanceMeasure extends AbstractComponent
		implements DistanceMeasure {

	/** The parameters. */
	List<SubproblemParameter> parameters;

	/**
	 * Instantiates a new abstract distance measure.
	 * 
	 * @param parameters
	 *            the parameters
	 */
	public AbstractDistanceMeasure(List<SubproblemParameter> parameters) {
		this.parameters = parameters;
	}

	public int[] assignInstances(ExampleSet exampleSet,
			WhiBoCentroidClusterModel model) {
		int[] centroidAssignments = new int[exampleSet.size()];

		int i = 0;
		for (Example example : exampleSet) {
			double[] exampleValues = getAsDoubleArray(example,
					exampleSet.getAttributes());
			double nearestDistance = calculateDistance(
					model.getCentroidCoordinates(0), exampleValues);
			int nearestIndex = 0;
			for (int centroidIndex = 1; centroidIndex < model
					.getNumberOfClusters(); centroidIndex++) {
				double distance = calculateDistance(
						model.getCentroidCoordinates(centroidIndex),
						exampleValues);
				if (distance < nearestDistance) {
					nearestDistance = distance;
					nearestIndex = centroidIndex;
				}
			}

			centroidAssignments[i] = nearestIndex;
			// model.getCentroid(nearestIndex).assignExample(exampleValues);
			// model.assignExample(nearestIndex, exampleValues);
			i++;

		}

		return centroidAssignments;
	}

	private double[] getAsDoubleArray(Example example, Attributes attributes) {
		double[] values = new double[attributes.size()];
		int i = 0;
		for (Attribute attribute : attributes) {
			values[i] = example.getValue(attribute);
			i++;
		}
		return values;
	}

}
