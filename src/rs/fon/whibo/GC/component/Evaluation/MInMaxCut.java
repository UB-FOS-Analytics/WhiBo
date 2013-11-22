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
 * The Class MInMaxCut.
 */
public class MInMaxCut extends AbstractEvaluation {

	/**
	 * Instantiates a new m in max cut.
	 * 
	 * @param parameters
	 *            the parameters
	 */
	public MInMaxCut(List<SubproblemParameter> parameters) {
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
	// //minMaxCut is ratio between cutValue and eValue
	// // cutValue is sum of distance between example and examples from other
	// clusters
	// //eValue is distance sum of distance between example and examples from
	// same cluster
	// WhiBoTools tools = new WhiBoTools();
	//
	// double minMaxCut=0;
	// double cutValue=0;
	// double eValue=0;
	//
	// for(Cluster c:model.getClusters())
	// {
	//
	// for(Example ex1: exampleSet)
	// {
	//
	// double ex1Code=ex1.getId();
	// if(c.getClusterId()==model.getClusterIndexOfId(ex1Code))
	// {
	// for(Example ex2:exampleSet)
	// {
	// double ex2Code=ex2.getId();
	//
	// if(model.getClusterIndexOfId(ex1Code)!=model.getClusterIndexOfId(ex2Code))
	// {
	// cutValue=cutValue+measure.calculateDistance(tools.getAsDoubleArray(ex1,
	// exampleSet.getAttributes()),tools.getAsDoubleArray(ex2,
	// exampleSet.getAttributes()));
	// }
	// else
	// {
	// eValue=eValue+measure.calculateDistance(tools.getAsDoubleArray(ex1,
	// exampleSet.getAttributes()),tools.getAsDoubleArray(ex2,
	// exampleSet.getAttributes()));
	// }
	// }
	// }
	//
	//
	// }
	//
	// minMaxCut=minMaxCut+cutValue/eValue*1/2;
	// }
	//
	// return minMaxCut;
	// }

	@Override
	public double Evaluate(DistanceMeasure measure, ClusterModel model,
			ExampleSet exampleSet) {
		// minMaxCut is ratio between cutValue and eValue
		// cutValue is sum of distance between example and examples from other
		// clusters
		// eValue is distance sum of distance between example and examples from
		// same cluster
		WhiBoTools tools = new WhiBoTools();

		double minMaxCut = 0;
		double cutValue = 0;
		double eValue = 0;

		int[] assignments = new int[exampleSet.size()];
		assignments = model.getClusterAssignments(exampleSet);

		for (int i = 0; i < model.getClusters().size(); i++) {
			int l = 0;
			int k = 0;
			for (Example ex1 : exampleSet) {

				// double ex1Code=ex1.getId();
				// String clustId = "cluster_"+c.getClusterId();
				// String ex1ID =
				// ex1.getNominalValue(ex1.getAttributes().get("cluster"));
				if (i == assignments[l]) {
					for (Example ex2 : exampleSet) {
						// String ex2ID =
						// ex2.getNominalValue(ex1.getAttributes().get("cluster"));

						// if(!ex1ID.equals(ex2ID))
						if (assignments[l] != assignments[k]) {
							cutValue = cutValue
									+ measure
											.calculateDistance(
													tools.getAsDoubleArray(
															ex1,
															exampleSet
																	.getAttributes()),
													tools.getAsDoubleArray(
															ex2,
															exampleSet
																	.getAttributes()));
						} else {
							eValue = eValue
									+ measure
											.calculateDistance(
													tools.getAsDoubleArray(
															ex1,
															exampleSet
																	.getAttributes()),
													tools.getAsDoubleArray(
															ex2,
															exampleSet
																	.getAttributes()));
						}
						k++;
					}
					l++;
					k = 0;
				} else
					l++;

			}

			minMaxCut = minMaxCut + cutValue / eValue * 1 / 2;
		}

		return minMaxCut;
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
		if (eval1 > eval2)
			return true;
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.whibo.GC.component.Evaluation.AbstractEvaluation#getWorstValue()
	 */
	@Override
	public double getWorstValue() {
		return Double.MIN_VALUE;
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
