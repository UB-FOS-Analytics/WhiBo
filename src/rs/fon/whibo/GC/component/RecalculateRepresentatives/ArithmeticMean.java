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
package rs.fon.whibo.GC.component.RecalculateRepresentatives;

import java.util.ArrayList;
import java.util.List;

import rs.fon.whibo.GC.clusterModel.WhiBoCentroid;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;

import com.rapidminer.example.ExampleSet;

/**
 * The Class ArithmeticMean recalculates representatives as an arithmetic mean
 * of all data points assigned to current cluster.
 * 
 * J. Hartigan and M. Wong, Algorithm AS136: A k-means clustering algorithm,
 * Applied Statistics 28 (1979), pp. 100–108
 */
public class ArithmeticMean extends AbstractRecalculateRepresentatives {

	/** The init cluster. */
	WhiBoCentroidClusterModel initCluster;

	/**
	 * Instantiates an arithmetic mean component for recalculating cluster
	 * representatives.
	 * 
	 * @param parameters
	 *            - this method does't take any parameter.
	 */
	public ArithmeticMean(
			List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.whibo.GC.component.RecalculateRepresentatives.
	 * AbstractRecalculateRepresentatives
	 * #Recalculate(rs.fon.whibo.GC.clusterModel.CentroidClusterModel,
	 * com.rapidminer.example.ExampleSet,
	 * rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure)
	 */
	public boolean Recalculate(WhiBoCentroidClusterModel model,
			ExampleSet exampleSet, DistanceMeasure distanceMeasure) {

		int n = model.getNumberOfClusters();
		int numOfDimensions = exampleSet.getAttributes().size();
		ArrayList<WhiBoCentroid> oldCentroids = new ArrayList<WhiBoCentroid>();
		ArrayList<WhiBoCentroid> newCentroids = new ArrayList<WhiBoCentroid>();
		boolean stable = true;

		// Takes list of representatives from previous optimization step
		// and calculates new representatives
		for (int j = 0; j < n; j++) {

			oldCentroids.add(model.getCentroid(j));
			WhiBoCentroid newCentroid = new WhiBoCentroid(numOfDimensions);
			newCentroid.setCentroidSum(model.getCentroid(j).getCentroidSum());
			newCentroid
					.setCentroid(newCentroidCoordinates(oldCentroids.get(j)));

			newCentroids.add(newCentroid);
		}

		for (int l = 0; l < n; l++) {
			if (!equalCoordinates(oldCentroids.get(l), newCentroids.get(l))) {
				stable = false;
			}
		}

		if (!stable) {

			for (int k = 0; k < n; k++) {
				newCentroids.get(k).setNumberOfAssigned(0);
				newCentroids.get(k).restartCentroidSum();

			}

			model.setCentroids(newCentroids);
		}

		return stable;

	}

	/**
	 * New centroid coordinates.
	 * 
	 * @param centroid
	 *            the centroid
	 * 
	 * @return the double[]
	 */
	private double[] newCentroidCoordinates(WhiBoCentroid centroid) {

		double[] centroidSum = centroid.getCentroidSum();
		int l = centroidSum.length;
		double[] newCentroidCoordinates = new double[l];
		int numberOfAssigned = centroid.getNumberOfAssigned();

		for (int i = 0; i < l; i++) {
			if (numberOfAssigned == 0)
				newCentroidCoordinates[i] = centroidSum[i];
			else
				newCentroidCoordinates[i] = centroidSum[i] / numberOfAssigned;
		}

		return newCentroidCoordinates;
	}

	/**
	 * Equal coordinates.
	 * 
	 * @param c1
	 *            the c1
	 * @param c2
	 *            the c2
	 * 
	 * @return true, if successful
	 */
	private boolean equalCoordinates(WhiBoCentroid c1, WhiBoCentroid c2) {
		boolean equal = true;
		double[] c1Coordinates = c1.getCentroid();
		double[] c2Coordinates = c2.getCentroid();
		int l = c1Coordinates.length;
		for (int i = 0; i < l; i++) {
			if (!(c1Coordinates[i] == c2Coordinates[i]))
				equal = false;
		}

		return equal;
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
