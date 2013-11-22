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

import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;

import com.rapidminer.example.ExampleSet;

// TODO: Auto-generated Javadoc
/**
 * The Interface DistanceMeasure.
 */
public interface DistanceMeasure {

	/**
	 * Assigns instances to centroids on the basis of concrete distance measure.
	 * 
	 * @param exampleSet
	 *            - the example set for clustering
	 * @param model
	 *            - initialized centroid cluster model
	 * 
	 * @return the centroid cluster model
	 */
	public int[] assignInstances(ExampleSet exampleSet,
			WhiBoCentroidClusterModel model);

	/**
	 * Calculates distance distance between two coordinate vectors of instances
	 * in example set .
	 * 
	 * @param coordinates1
	 *            - coordinates of the first vector
	 * @param coordinates2
	 *            - coordinates of the second vector
	 * 
	 * @return the double
	 */
	public double calculateDistance(double[] coordinates1, double[] coordinates2);
}
