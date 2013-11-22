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
package rs.fon.whibo.GC.component.Initialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;
import rs.fon.whibo.problem.Parameter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.tools.RandomGenerator;

/**
 * The Class KMeansPlusPlus initializes centroid cluster model like in Kmeans ++
 * algorithm.
 * 
 * First centroid is randomly selected and other (K-1) centroids are initialized
 * as follows:. Distances from every centroid to every example is calculated and
 * an example with farthest distance from any centroid becomes new centroid.
 * 
 * David Arthur , Sergei Vassilvitskii, k-means++: the advantages of careful
 * seeding, Proceedings of the eighteenth annual ACM-SIAM symposium on Discrete
 * algorithms, p.1027-1035, January 07-09, 2007, New Orleans, Louisiana
 */
public class KMeansPlusPlus extends AbstractInitialization {

	/** K parameter defines number of clusters. */
	@Parameter(defaultValue = "3", minValue = "2", maxValue = "100000")
	private int Parameter_K;

	/** Parameter No_Of_Restarts defines number of restarts of the algorithm. */
	@Parameter(defaultValue = "1", minValue = "1", maxValue = "1000")
	private int No_Of_Restarts;

	/** Cluster model that will be initialized with K centroids. */
	WhiBoCentroidClusterModel initCluster;

	/**
	 * Instantiates a significant categorical component for candidate split
	 * creation.
	 * 
	 * @param parameters
	 *            - this method has one parameter: Parameter_K.
	 */
	public KMeansPlusPlus(
			List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
		initCluster = null;
		Parameter_K = Integer.parseInt(parameters.get(0).getXenteredValue());
		No_Of_Restarts = Integer.parseInt(parameters.get(1).getXenteredValue());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.whibo.GC.component.Initialization.Initialization#InitializeCentroids
	 * (com.rapidminer.example.ExampleSet,
	 * rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure)
	 */
	public WhiBoCentroidClusterModel InitializeCentroids(ExampleSet exampleSet,
			DistanceMeasure distanceMeasure) {
		// Opet zbudz sa example setom
		initCluster = new WhiBoCentroidClusterModel(exampleSet, Parameter_K,
				exampleSet.getAttributes());
		ArrayList<Example> tempCentroids = new ArrayList<Example>();

		// Inits first centroid at random
		RandomGenerator generator = RandomGenerator.getGlobalRandomGenerator();
		int randomIndex = generator.nextIntInRange(0, exampleSet.size() - 1);
		tempCentroids.add(exampleSet.getExample(randomIndex));

		// Calculates other (K-1) centroids. Distances from every centroid to
		// every example
		// is calculated and centroid is picked from a sample distribution
		// proportional to those distances.
		double[] exampleProbability = new double[exampleSet.size()];
		for (int i = 1; i < Parameter_K; i++) {
			exampleProbability = getExampleProbability(exampleSet,
					tempCentroids, distanceMeasure);
			Example newCentroid = pickNewCenter(exampleSet, exampleProbability,
					tempCentroids);
			if ((newCentroid != null)) {
				tempCentroids.add(newCentroid);
			}
		}

		for (int i = 0; i < Parameter_K; i++) {
			initCluster
					.assignExample(i, getAsDoubleArray(tempCentroids.get(i)));
		}
		initCluster.finishAssign();
		return initCluster;
	}

	private double[] getExampleProbability(ExampleSet exampleSet,
			ArrayList<Example> centroids, DistanceMeasure distanceMeasure) {
		double[] exampleProbability = new double[exampleSet.size()];

		// Calculate distance from nearest cluster
		for (int ex = 0; ex < exampleSet.size(); ex++) {
			double minDistance = Double.MAX_VALUE;
			for (int cluster = 0; cluster < centroids.size(); cluster++) {
				double distance = distanceMeasure.calculateDistance(
						getAsDoubleArray(exampleSet.getExample(ex)),
						getAsDoubleArray(centroids.get(cluster)));
				if (distance < minDistance)
					minDistance = distance;
			}
			exampleProbability[ex] = minDistance;
		}

		// Transform distance to probabilities

		// first square the values
		double sumOfSquaredDistances = 0;
		for (int i = 0; i < exampleSet.size(); i++) {
			exampleProbability[i] = Math.pow(exampleProbability[i], 2);
			sumOfSquaredDistances += exampleProbability[i];
		}
		// then normalize the values
		for (int i = 0; i < exampleSet.size(); i++)
			exampleProbability[i] /= sumOfSquaredDistances;

		return exampleProbability;
	}

	private Example pickNewCenter(ExampleSet exampleSet,
			double[] exampleProbability, ArrayList<Example> centroids) {

		double randomNumber = RandomGenerator.getGlobalRandomGenerator()
				.nextDouble();

		double sumOfProbabilities = 0;
		int currentExample = -1;
		while (sumOfProbabilities <= randomNumber
				&& currentExample + 1 < exampleProbability.length) {
			currentExample += 1;
			sumOfProbabilities += exampleProbability[currentExample];
		}

		Example newCentroid = exampleSet.getExample(currentExample);

		// IF this centroid already exist, repeat sampling (with recursion)
		if (centroids.contains(newCentroid))
			return pickNewCenter(exampleSet, exampleProbability, centroids);
		else
			return newCentroid;
	}

	// Izvuci u neke Toolsove
	/**
	 * Gets the as double array.
	 * 
	 * @param example
	 *            the example
	 * @param attributes
	 *            the attributes
	 * 
	 * @return the as double array
	 */
	private double[] getAsDoubleArray(Example example) {
		double[] values = new double[example.getAttributes().size()];
		int i = 0;
		for (Attribute attribute : example.getAttributes()) {
			values[i] = example.getValue(attribute);
			i++;
		}
		return values;
	}

	private boolean containsCentroid(WhiBoCentroidClusterModel clusters,
			double[] newClusterCoordinates) {

		boolean isContained = false;
		double[] oldClusterCoordinates;
		for (int i = 0; i < initCluster.getNumberOfClusters(); i++) {
			oldClusterCoordinates = initCluster.getCentroidCoordinates(i);
			if (Arrays.equals(oldClusterCoordinates, newClusterCoordinates)) {
				isContained = true;
				break;
			}
		}

		return isContained;
	}

	public int getNumberOfRestarts() {
		return No_Of_Restarts;
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