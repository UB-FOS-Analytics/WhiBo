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
package rs.fon.whibo.GC.component.Evaluation;

import java.util.List;

import rs.fon.whibo.GC.Tools.WhiBoTools;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.clustering.ClusterModel;

// TODO: Auto-generated Javadoc
/**
 * The Class IntraClusterDistance.
 */
public class IntraClusterDistance extends AbstractEvaluation {

	public IntraClusterDistance(List<SubproblemParameter> parameters) {
		super(parameters);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.whibo.GC.component.Evaluation.AbstractEvaluation#Evaluate(rs.fon
	 * .whibo.GC.component.DistanceMeasure.DistanceMeasure,
	 * rs.fon.whibo.GC.clusterModel.CentroidClusterModel,
	 * com.rapidminer.example.ExampleSet)
	 */
	// @Override
	// public double Evaluate(DistanceMeasure measure, WhiBoCentroidClusterModel
	// model, ExampleSet exampleSet)
	// {
	// //compactness calculates distance between all examples(records) and their
	// cluster center (centroid)
	//
	// WhiBoTools tools = new WhiBoTools();
	// double distanceSum = 0;
	// int [] assignments = new int[exampleSet.size()];
	// assignments=model.getClusterAssignments(exampleSet);
	//
	// double [][] centroids = RecalculateCentroids(exampleSet, model, measure);
	// //for(Cluster c:model.getClusters())
	// for(int i=0; i<model.getClusters().size(); i++)
	// {
	//
	// int h=0;
	// for (Example example: exampleSet) {
	//
	// //checking is Example example from Cluster c
	// //if(c.getClusterId()==model.getClusterIndexOfId(example.getId()))
	//
	// //if(c.getClusterId()==assignments[h]);
	// if(i==assignments[h])
	// {
	// //double
	// distance=measure.calculateDistance(model.getCentroidCoordinates(c.getClusterId()),
	// tools.getAsDoubleArray(example, exampleSet.getAttributes()));
	// double distance=measure.calculateDistance(centroids[i],
	// tools.getAsDoubleArray(example, exampleSet.getAttributes()));
	// distanceSum = distanceSum+ distance;
	// }
	// h++;
	// }
	//
	//
	// }
	//
	// return distanceSum;
	//
	// }

	@Override
	public double Evaluate(DistanceMeasure measure, ClusterModel model,
			ExampleSet exampleSet) {
		// //compactness calculates distance between all examples(records) and
		// their cluster center (centroid)
		// WhiBoTools tools = new WhiBoTools();
		// double distanceSum = 0;
		// for(Cluster c:model.getClusters())
		// {
		// for (Example example: exampleSet) {
		//
		// //checking is Example example from Cluster c
		// String clustId = "cluster_"+c.getClusterId();
		//
		// if(clustId.equals(example.getNominalValue(example.getAttributes().get("cluster"))))
		//
		//
		// //if(c.getClusterId()==model.getClusterIndexOfId(example.getId()))
		// {
		// double
		// distance=measure.calculateDistance(model.getCentroidCoordinates(c.getClusterId()),
		// tools.getAsDoubleArray(example, exampleSet.getAttributes()));
		// distanceSum = distanceSum+ distance;
		// }
		// }
		// }
		//
		// return distanceSum;
		WhiBoTools tools = new WhiBoTools();
		double distanceSum = 0;
		int[] assignments = new int[exampleSet.size()];
		assignments = model.getClusterAssignments(exampleSet);

		double[][] centroids = RecalculateCentroids(exampleSet, model, measure);
		// for(Cluster c:model.getClusters())
		for (int i = 0; i < model.getClusters().size(); i++) {

			int h = 0;
			for (Example example : exampleSet) {

				// checking is Example example from Cluster c
				// if(c.getClusterId()==model.getClusterIndexOfId(example.getId()))

				// if(c.getClusterId()==assignments[h]);
				if (i == assignments[h]) {
					// double
					// distance=measure.calculateDistance(model.getCentroidCoordinates(c.getClusterId()),
					// tools.getAsDoubleArray(example,
					// exampleSet.getAttributes()));
					double distance = measure.calculateDistance(
							centroids[i],
							tools.getAsDoubleArray(example,
									exampleSet.getAttributes()));
					distanceSum = distanceSum + distance;
				}
				h++;
			}

		}

		return distanceSum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.whibo.GC.component.Evaluation.AbstractEvaluation#isBetter(double,
	 * double)
	 */
	@Override
	public boolean isBetter(double eval1, double eval2) {
		if (eval1 < eval2)
			return true;
		else
			return false;
	}

	@Override
	public double getWorstValue() {
		return Double.MAX_VALUE;
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

	public double[][] RecalculateCentroids(ExampleSet exampleSet,
			ClusterModel model, DistanceMeasure measure) {
		WhiBoTools tools = new WhiBoTools();
		int k = model.getNumberOfClusters();
		int n = exampleSet.getAttributes().size();
		double[][] centroids = new double[k][n];
		for (int i = 0; i < model.getNumberOfClusters(); i++) {
			for (Example example : exampleSet) {
				if (model.getCluster(i).getExampleIds()
						.contains(example.getId())) {
					for (int j = 0; j < n; j++) {
						double[] exampleValues = tools.getAsDoubleArray(
								example, exampleSet.getAttributes());
						centroids[i][j] += exampleValues[j];

					}
				}
			}
		}

		return centroids;
	}
}
