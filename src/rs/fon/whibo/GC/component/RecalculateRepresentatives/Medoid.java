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

import java.util.List;

import rs.fon.whibo.GC.Tools.WhiBoTools;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroid;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;

import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;

/**
 * The Class Medoid recalculates representatives as in K-medoids algorithm. For
 * every instance of a cluster it calculates intra cluster distance. Instance
 * with the minimum intra cluster distance becomes new medoid.
 * 
 * L. Kaufman and P.J. Rousseeuw, Clustering by Means of Medoids. Statistical
 * Data Analysis based on the L1 Norm, Elsevier, Berlin (1987) pp. 405–416.
 */
public class Medoid extends AbstractRecalculateRepresentatives {

	/** The init cluster. */
	WhiBoCentroidClusterModel initCluster;

	WhiBoTools tools = new WhiBoTools();

	/**
	 * Instantiates a medoid component for recalculating representatives.
	 * 
	 * @param parameters
	 *            - this method has no parameters.
	 */
	public Medoid(List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
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
	public boolean Recalculate(int[] centroidAssignmens,
			WhiBoCentroidClusterModel model, ExampleSet exampleSet,
			DistanceMeasure distanceMeasure) {
		int n = model.getNumberOfClusters();

		// boolean stable = false;
		boolean stable = true;

		// Takes list of representatives from previous optimization step
		// and calculates new representatives. If there were not any changes in
		// cluster representatives coordinates sets stable property
		// to true (signal for algorithm stop)
		for (int j = 0; j < n; j++) {
			model.setCentroid(
					j,
					newCentroidCoordinates(j, centroidAssignmens,
							model.getCentroid(j), exampleSet, distanceMeasure));

			// if (!stable)
			// {
			// for (int k=0; k<n; k++)
			// {
			// newCentroids.get(k).setNumberOfAssigned(0);
			// newCentroids.get(k).restartCentroidSum();
			// model.setCentroids(newCentroids);
			// }
			// }
			//
		}
		return false;
	}

	/**
	 * New centroid coordinates.
	 * 
	 * @param clusterNumber
	 *            the cluster number
	 * @param centroid
	 *            the centroid
	 * @param exampleSet
	 *            the example set
	 * @param distanceMeasure
	 *            the distance measure
	 * 
	 * @return the double[]
	 */
	private double[] newCentroidCoordinates(int clusterNumber,
			int[] centroidAssignments, WhiBoCentroid centroid,
			ExampleSet exampleSet, DistanceMeasure distanceMeasure) {

		double[] bestMedoidValues = new double[exampleSet.getAttributes()
				.size()];
		double bestDistanceSum = Double.POSITIVE_INFINITY;
		for (Example medoid : exampleSet) {
			// calculates intra cluster distance if this example is used as
			// medoid
			// example with minimal intra cluster distance becomes medoid
			double distanceSum = 0;
			double[] medoidValues = tools.getAsDoubleArray(medoid,
					exampleSet.getAttributes());
			int k = 0;

			for (Example example : exampleSet) {
				if (centroidAssignments[k] == clusterNumber) // only instances
																// inside one
																// cluster
					distanceSum += distanceMeasure.calculateDistance(
							tools.getAsDoubleArray(example,
									exampleSet.getAttributes()), medoidValues);
				k++;

			}
			if (distanceSum < bestDistanceSum) {
				bestDistanceSum = distanceSum;
				bestMedoidValues = medoidValues;
			}
		}

		return bestMedoidValues;
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

	// public boolean Recalculate(WhiBoCentroidClusterModel model, ExampleSet
	// exampleSet, DistanceMeasure distanceMeasure)
	// {
	// int n = model.getNumberOfClusters();
	// ArrayList<WhiBoCentroid> oldCentroids = new ArrayList<WhiBoCentroid>();
	// ArrayList<WhiBoCentroid> newCentroids = new ArrayList<WhiBoCentroid>();
	// // boolean stable = false;
	// boolean stable = true;
	//
	// // Takes list of representatives from previous optimization step
	// // and calculates new representatives. If there were not any changes in
	// // cluster representatives coordinates sets stable property
	// //to true (signal for algorithm stop)
	// for (int j =0; j<n; j++)
	// {
	// oldCentroids.add(model.getCentroid(j));
	// newCentroids.add(model.getCentroid(j));
	// newCentroids.get(j).setCentroid(newCentroidCoordinates(j,
	// oldCentroids.get(j), exampleSet, distanceMeasure));
	// if (!tools.equalCoordinates(oldCentroids.get(j), newCentroids.get(j)))
	// {
	// stable=false;
	// }
	// }
	//
	// if (!stable)
	// {
	// for (int k=0; k<n; k++)
	// {
	// newCentroids.get(k).setNumberOfAssigned(0);
	// newCentroids.get(k).restartCentroidSum();
	// model.setCentroids(newCentroids);
	// }
	// }
	//
	// return stable;
	// }

}
