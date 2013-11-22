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
import rs.fon.whibo.problem.Parameter;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.clustering.ClusterModel;

// TODO: Auto-generated Javadoc
/**
 * The Class Connectivity.
 */
public class Connectivity extends AbstractEvaluation {

	/** Parameter No_of nearest defines number nearest for check. */
	@Parameter(defaultValue = "5", minValue = "1", maxValue = "1000")
	private int No_Of_Nearest;

	/**
	 * Instantiates a new connectivity.
	 * 
	 * @param parameters
	 *            the parameters
	 */
	public Connectivity(List<SubproblemParameter> parameters) {
		super(parameters);

		No_Of_Nearest = Integer.parseInt(parameters.get(0).getXenteredValue());

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
	// //calculates distanceSum for n nearest
	// // i counter for records in exampleSet
	// //j counter for n nearest
	// //if j is not from i's cluster we add to distance sum 1/j
	// double distanceSum=0;
	// WhiBoTools tools = new WhiBoTools();
	//
	// int n= No_Of_Nearest;
	// Object[][] array2d=tools.getDistanceTable(measure, exampleSet,n );
	//
	//
	// for(int i=0;i<array2d.length;i++)
	// {
	//
	// int counter=0;
	// double clid1=
	// model.getClusterIndexOfId(Double.parseDouble(array2d[i][0].toString()));
	//
	//
	//
	//
	//
	// for(int j=1;j<n*2+1;j=j+2)
	// {
	// double
	// clid2=model.getClusterIndexOfId(Double.parseDouble(array2d[i][j+1].toString()));
	// counter++;
	//
	// if(clid1!=clid2 )
	// {
	// distanceSum=distanceSum+1.0/counter;
	// }
	//
	// }
	//
	// }
	//
	// return distanceSum;
	// }
	@Override
	public double Evaluate(DistanceMeasure measure, ClusterModel model,
			ExampleSet exampleSet) {
		// calculates distanceSum for n nearest
		// i counter for records in exampleSet
		// j counter for n nearest
		// if j is not from i's cluster we add to distance sum 1/j
		double distanceSum = 0;
		WhiBoTools tools = new WhiBoTools();

		int n = No_Of_Nearest;
		Object[][] array2d = tools.getDistanceTable(measure, exampleSet, n);

		int[] assignments = new int[exampleSet.size()];
		assignments = model.getClusterAssignments(exampleSet);

		for (int i = 0; i < array2d.length; i++) {

			int counter = 0;
			// double clid1=
			// model.getClusterIndexOfId(Double.parseDouble(array2d[i][0].toString()));
			// String clid1 =
			// exampleSet.getExample(i).getNominalValue(exampleSet.getExample(i).getAttributes().get("cluster"));
			// int clid1 = model.getClusterIndexOfId(exampleSet.getExample(i));
			int clid1 = assignments[i];
			for (int j = 1; j < n * 2 + 1; j = j + 2) {
				// double
				// clid2=model.getClusterIndexOfId(Double.parseDouble(array2d[i][j+1].toString()));
				// String clid2 =
				// exampleSet.getExample(j).getNominalValue(exampleSet.getExample(j).getAttributes().get("cluster"));
				int clid2 = assignments[j];
				counter++;

				if (clid1 != clid2)
				// if(!clid1.equals(clid2))
				{
					distanceSum = distanceSum + 1.0 / counter;
				}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.whibo.GC.component.Evaluation.AbstractEvaluation#getWorstValue()
	 */
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
