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

import rs.fon.whibo.GC.Tools.WhiBoTools;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;
import rs.fon.whibo.problem.Parameter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;

/*
 * DIANA divisive hierachical clustering approach to initialize centroids
 * first builds two clusters with DIANA method
 * then calculates mean values for each cluster that define the 2 centroids
 * works only for k=2
 */

public class Diana extends AbstractInitialization {

	/** Parameter No_Of_Restarts defines number of restarts of the algorithm. */
	@Parameter(defaultValue = "5", minValue = "1", maxValue = "1000")
	private int No_Of_Restarts;

	WhiBoCentroidClusterModel initCluster;

	/**
	 * Instantiates a significant categorical component for candidate split
	 * creation.
	 * 
	 * @param parameters
	 *            - this method two parameters: The Merge_ alpha_ value and The
	 *            Split_ alpha_ value.
	 */
	public Diana(List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
		initCluster = null;
		No_Of_Restarts = Integer.parseInt(parameters.get(0).getXenteredValue());

	}

	public WhiBoCentroidClusterModel InitializeCentroids(ExampleSet exampleSet,
			DistanceMeasure measure) {

		Attributes attributes = exampleSet.getAttributes();
		// Opet zbudz sa example setom
		initCluster = new WhiBoCentroidClusterModel(exampleSet, 2, attributes);
		// find object with the highest average dissimilarity to all other
		// objects
		int maxAvgDistanceIndex = getMaxAverageDistanceIndex(measure,
				initCluster, exampleSet);
		// assign element with highest dissimilarity to first cluster
		// this is the splinter group
		initCluster.assignExample(
				0,
				getAsDoubleArray(exampleSet.getExample(maxAvgDistanceIndex),
						attributes));
		// divide data into 2 sets
		int numObjects = exampleSet.size();
		double Distance = 0;
		for (int i = 0; i <= numObjects; i++) {
			Distance = getDistance(i, measure, initCluster, exampleSet);
			if (Distance >= 0) {
				// if distance is positive, then i belongs to splinter group
				initCluster.assignExample(0,
						getAsDoubleArray(exampleSet.getExample(i), attributes));
			} else if (Distance < 0) {
				// if Distance is negative, the object is not close to splinter
				// group
				initCluster.assignExample(1,
						getAsDoubleArray(exampleSet.getExample(i), attributes));
			}
		}
		initCluster.finishAssign();
		// initCluster.getCluster(0).
		return initCluster;
	}

	private int getMaxAverageDistanceIndex(DistanceMeasure measure,
			WhiBoCentroidClusterModel model, ExampleSet exampleSet) { // find
																		// the
																		// object
																		// with
																		// highest
																		// dissimilarity
																		// to
																		// all
																		// other
																		// objects
		double maxAvgDistance = 0;
		double currentAvgDistance = 0;
		int exampleIndex = 0;
		int maxAvgDistanceIndex = 0;
		double sumDistance = 0;
		int numObjects = exampleSet.size();
		// int k = model.getNumberOfClusters();

		for (int l = 0; l <= numObjects; l++) {
			exampleIndex = 0;
			// calculate sum of distance from l to all other objects in the
			// example set
			for (Example example : exampleSet) {
				sumDistance = sumDistance
						+ measure.calculateDistance(
								model.getCentroidCoordinates(l),
								getAsDoubleArray(example,
										exampleSet.getAttributes()));
				exampleIndex++;
			}
			currentAvgDistance = sumDistance / (numObjects - 1);
			if (currentAvgDistance > maxAvgDistance) {
				maxAvgDistance = currentAvgDistance;
				maxAvgDistanceIndex = l;
			}
		}

		return maxAvgDistanceIndex;
	}

	private double getDistance(int element_id, DistanceMeasure measure,
			WhiBoCentroidClusterModel model, ExampleSet exampleSet) {
		double Dist = 0;
		double Splinter_Dist = 0;
		double sumDistance = 0;
		double NonSplinter_Dist = 0;
		double SumDistance = 0;
		int exampleIndex = 0;
		int numObjects = exampleSet.size();
		;
		WhiBoTools tools = new WhiBoTools();
		// calculate sum of distance from example_id object to all other objects
		// in the splinter set
		for (Example example : exampleSet) { // consider only objects in
												// splinter group
			if (tools.getClusterNumber(example) == 0) {
				sumDistance = sumDistance
						+ measure.calculateDistance(
								model.getCentroidCoordinates(element_id),
								getAsDoubleArray(example,
										exampleSet.getAttributes()));
				exampleIndex++;
			}
		}
		Splinter_Dist = sumDistance
				/ (model.getCluster(0).getNumberOfExamples());

		exampleIndex = 0;
		sumDistance = 0;
		// calculate sum of distance from example_id object to all other objects
		// not in the splinter set
		for (Example example : exampleSet) { // consider only objects in
												// splinter group
			if (tools.getClusterNumber(example) != 0) {
				sumDistance = sumDistance
						+ measure.calculateDistance(
								model.getCentroidCoordinates(element_id),
								getAsDoubleArray(example,
										exampleSet.getAttributes()));
				exampleIndex++;
			}
		}
		NonSplinter_Dist = sumDistance
				/ (Math.abs(exampleSet.size()
						- model.getCluster(0).getNumberOfExamples()));

		Dist = NonSplinter_Dist - Splinter_Dist;

		return Dist;
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
