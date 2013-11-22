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
public class DaviesBouldinIndex extends AbstractEvaluation {

	public DaviesBouldinIndex(List<SubproblemParameter> parameters) {
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

	// public double Evaluate(DistanceMeasure measure, CentroidClusterModel
	// model, ExampleSet exampleSet)
	// {
	// //compactness calculates distance between all examples(records) and their
	// cluster center (centroid)
	// WhiBoTools tools = new WhiBoTools();
	// double DaviesBouldin = 0;
	// double [][] InterClusterDistances = InterClusterDistances(model,
	// measure);
	// double [] IntraClusterDistances = IntraClusterDistances(model, measure,
	// exampleSet);
	// double DBEveryCluster [] = DBEveryCluster(InterClusterDistances,
	// IntraClusterDistances);
	// int k = model.getNumberOfClusters();
	// for (int i =0; i<k;i++)
	// DaviesBouldin+=DBEveryCluster[i];
	//
	//
	// return DaviesBouldin/k;
	// }

	public double Evaluate(DistanceMeasure measure, ClusterModel model,
			ExampleSet exampleSet) {
		// compactness calculates distance between all examples(records) and
		// their cluster center (centroid)
		WhiBoTools tools = new WhiBoTools();
		double DaviesBouldin = 0;
		double[][] InterClusterDistances = InterClusterDistances(exampleSet,
				model, measure);
		double[] IntraClusterDistances = IntraClusterDistances(model, measure,
				exampleSet);
		double DBEveryCluster[] = DBEveryCluster(InterClusterDistances,
				IntraClusterDistances);
		int k = model.getNumberOfClusters();
		for (int i = 0; i < k; i++)
			DaviesBouldin += DBEveryCluster[i];

		return DaviesBouldin / k;
	}

	public double[] DBEveryCluster(double[][] InterClusterDistances,
			double[] IntraClusterDistances) {

		int k = IntraClusterDistances.length;
		double[] maxDist = new double[k];
		double currentDist = 0;
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < k; j++) {
				if (i > j || i < j) {
					currentDist = (IntraClusterDistances[i] + IntraClusterDistances[j])
							/ InterClusterDistances[i][j];

					if (maxDist[i] < currentDist)
						maxDist[i] = currentDist;
				}
			}

		}

		return maxDist;
	}

	double[][] InterClusterDistances(ExampleSet exampleSet, ClusterModel model,
			DistanceMeasure measure) {
		int k = model.getNumberOfClusters();
		double[][] centroids = RecalculateCentroids(exampleSet, model, measure);
		double[][] distanceMatrix = new double[k][k];
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < k; j++) {

				// double dist =
				// measure.calculateDistance(model.getCentroidCoordinates(i),
				// model.getCentroidCoordinates(j));
				double dist = measure.calculateDistance(centroids[i],
						centroids[j]);
				distanceMatrix[i][j] = dist;

			}
		}

		return distanceMatrix;
	}

	double[] IntraClusterDistances(ClusterModel model, DistanceMeasure measure,
			ExampleSet exampleSet) {

		double[][] centroids = RecalculateCentroids(exampleSet, model, measure);
		int[] assignments = new int[exampleSet.size()];
		assignments = model.getClusterAssignments(exampleSet);
		WhiBoTools tools = new WhiBoTools();
		int k = model.getNumberOfClusters();
		double[] distanceArray = new double[k];
		for (Example example : exampleSet) {
			// for(Cluster c:model.getClusters())
			int h = 0;
			for (int i = 0; i < model.getClusters().size(); i++) {
				// checking is Example example from Cluster c
				// String clustId = "cluster_"+c.getClusterId();

				// if(clustId.equals(example.getNominalValue(example.getAttributes().get("cluster"))))
				// if(example.getId()==model.getClusterIndexOfId(example.getId()))
				if (i == assignments[h]) {
					// double
					// distance=measure.calculateDistance(model.getCentroidCoordinates(c.getClusterId()),
					// tools.getAsDoubleArray(example,
					// exampleSet.getAttributes()));
					double distance = measure.calculateDistance(
							centroids[i],
							tools.getAsDoubleArray(example,
									exampleSet.getAttributes()));
					distanceArray[i] = distanceArray[i] + distance;
					distanceArray[i] = distanceArray[i]
							/ model.getCluster(i).getExampleIds().size();
				}
				h++;
			}
		}

		return distanceArray;
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
}
