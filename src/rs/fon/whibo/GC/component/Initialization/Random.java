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

import java.util.List;

import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;
import rs.fon.whibo.problem.Parameter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.tools.RandomGenerator;

/**
 * The Class Random initializes centroid cluster model with representatives like
 * in Kmeans algorithm. It initializes centroid cluster model with K randomly
 * selected instances.
 * 
 * J. Hartigan and M. Wong, Algorithm AS136: A k-means clustering algorithm,
 * Applied Statistics 28 (1979), pp. 100–108
 */
public class Random extends AbstractInitialization {

	/** K parameter defines number of clusters. */
	@Parameter(defaultValue = "3", minValue = "2", maxValue = "100000")
	private int Parameter_K;
	/** Parameter No_Of_Restarts defines number of restarts of the algorithm. */
	@Parameter(defaultValue = "5", minValue = "1", maxValue = "1000")
	private int No_Of_Restarts;

	/** Cluster with initial centroids. */
	WhiBoCentroidClusterModel initCluster;

	/**
	 * Instantiates a component for random initialization of centroids.
	 * 
	 * @param parameters
	 *            - this method one parametar: Parameter_K that defines number
	 *            of clusters in the model
	 */
	public Random(List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
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
		Attributes attributes = (Attributes) exampleSet.getAttributes();

		initCluster = new WhiBoCentroidClusterModel(exampleSet, Parameter_K,
				attributes);
		RandomGenerator generator = RandomGenerator
				.getRandomGenerator(true, -1);
		;
		int i = 0;
		for (Integer index : generator.nextIntSetWithRange(0,
				exampleSet.size() - 1, Parameter_K)) {
			initCluster.getCentroid(i).setCentroid(
					getAsDoubleArray(exampleSet.getExample(index), attributes));
			initCluster.assignExample(i,
					getAsDoubleArray(exampleSet.getExample(index), attributes));
			i++;
		}
		initCluster.finishAssign();
		return initCluster;
	}

	/**
	 * Gets the example values as double array.
	 * 
	 * @param example
	 *            the example
	 * @param attributes
	 *            the attributes
	 * 
	 * @return double [] - coordinate vector of instance
	 */
	private double[] getAsDoubleArray(Example example, Attributes attributes) {
		double[] values = new double[attributes.size()];
		int i = 0;
		for (Attribute attribute : attributes) {
			values[i] = example.getValue(attribute);
			i++;
		}
		return values;
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
