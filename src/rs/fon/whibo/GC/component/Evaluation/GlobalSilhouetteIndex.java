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
import com.rapidminer.operator.clustering.Cluster;
import com.rapidminer.operator.clustering.ClusterModel;

// TODO: Auto-generated Javadoc
/**
 * The Class GlobalSilhouetteIndex.
 */
public class GlobalSilhouetteIndex extends AbstractEvaluation {

	/**
	 * Instantiates a new global silhouette index.
	 * 
	 * @param parameters
	 *            the parameters
	 */
	public GlobalSilhouetteIndex(List<SubproblemParameter> parameters) {
		super(parameters);

	}

	@Override
	public double Evaluate(DistanceMeasure measure, ClusterModel model,
			ExampleSet exampleSet) {

		WhiBoTools tools = new WhiBoTools();
		int[] assignments = new int[exampleSet.size()];
		assignments = model.getClusterAssignments(exampleSet);

		double gsi = 0;

		double[][] array = new double[exampleSet.size()][4];
		// first column is list of examples id
		// second column is sum of distance between one example and all other
		// examples from same cluster divided with number of elements-1
		// third column is minimum of sum distances between example from one
		// cluster and examples from other clusters
		// forth column ratio between first-second column and their maximum for
		// that row

		// calculates ai (distances from every example to every other that
		// belogs to the same cluster)
		int counter = 0;
		int ex1Assignment = 0;
		int ex2Assignment = 0;
		for (Example ex1 : exampleSet) {
			// shows the number of examples cluster
			int counter1 = 0;
			double razdaljina = 0;
			for (Example ex2 : exampleSet) {
				if (assignments[ex1Assignment] == assignments[ex2Assignment]) {
					razdaljina += measure.calculateDistance(
							tools.getAsDoubleArray(ex1, ex1.getAttributes()),
							tools.getAsDoubleArray(ex2, ex2.getAttributes()));
					counter1++;
				}
				ex2Assignment++;
			}
			array[counter][0] = ex1.getId();
			array[counter][1] = razdaljina * 1 / (counter1 - 1);
			counter++;
			ex1Assignment++;
			ex2Assignment = 0;
		}

		// calculates bi (minimum average distance between example and examples
		// from other clusters)
		ex1Assignment = 0;
		ex2Assignment = 0;

		int counter2 = 0;
		for (Example ex1 : exampleSet) {
			double min = Double.MAX_VALUE;
			for (Cluster c1 : model.getClusters()) {
				double trenPreracunavanje = 0;
				for (Example ex2 : exampleSet) {
					if ((!(c1.getClusterId() == assignments[ex1Assignment]) && (c1
							.getClusterId() == assignments[ex2Assignment])))
						trenPreracunavanje = trenPreracunavanje
								+ measure.calculateDistance(
										tools.getAsDoubleArray(ex1,
												ex1.getAttributes()),
										tools.getAsDoubleArray(ex2,
												ex2.getAttributes()));
					ex2Assignment++;
				}

				if (min > trenPreracunavanje / c1.getNumberOfExamples()) {
					min = trenPreracunavanje / c1.getNumberOfExamples();
				}
				ex2Assignment = 0;
			}

			array[counter2][2] = min;
			counter2++;
			ex1Assignment++;
			ex2Assignment = 0;

		}

		ex1Assignment = 0;
		ex2Assignment = 0;
		double max = 0;
		for (int k = 0; k < array.length; k++) {
			double diff = Math.abs(array[k][2] - array[k][1]);

			if (diff > max)
				max = diff;
		}

		for (int l = 0; l < array.length; l++) {
			array[l][3] = (array[l][2] - array[l][1]) / max;

		}

		for (Cluster c1 : model.getClusters()) {
			double gsiPom = 0;
			int f = 0;
			for (Example example5 : exampleSet) {
				// if (c1.containsExampleId(example5.getId()))
				if (c1.getClusterId() == assignments[ex1Assignment])
					gsiPom = gsiPom + array[f][3];
				f++;
				ex1Assignment++; // ovo je valjda isto sto i f :)
			}
			gsi = gsi + gsiPom / c1.getNumberOfExamples();
			ex1Assignment = 0;
			f = 0;
		}

		return gsi / model.getNumberOfClusters();
	}

	// Ovo je bezveze obrisati posle

	// @Override
	// public double Evaluate(DistanceMeasure measure, ClusterModel model,
	// ExampleSet exampleSet)
	// {
	//
	// WhiBoTools tools = new WhiBoTools();
	// double gsi=0;
	//
	// double [][]array=new double[exampleSet.size()][4];
	// // first column is list of examples id
	// //second column is sum of distance between one example and all other
	// examples from same cluster divided with number of elements-1
	// // third column is minimum of sum distances between example from one
	// cluster and examples from other clusters
	// //forth column ratio between first-second column and their maximum for
	// that row
	//
	//
	//
	// int counter=0;
	// for(Example ex1:exampleSet)
	// {
	//
	// int counter1=0;
	// double razdaljina=0;
	// for(Example ex2:exampleSet)
	// {
	// if(model.getClusterIndexOfId(ex1.getId())!=model.getClusterIndexOfId(ex2.getId()))
	// {
	// razdaljina+=measure.calculateDistance(tools.getAsDoubleArray(ex1,ex1.getAttributes()),
	// tools.getAsDoubleArray(ex2,ex2.getAttributes()));
	// counter1++;
	// }
	// }
	// array[counter][0]=ex1.getId();
	// array[counter][1]=razdaljina*1/(counter1-1);
	// counter++;
	// }
	// int counter2=0;
	// for(Example ex1:exampleSet)
	// {
	//
	// double min=Double.MAX_VALUE;
	// for(Cluster c1:model.getClusters())
	// {
	//
	// if(c1.getClusterId()!=model.getClusterIndexOfId(ex1.getId()))
	// {
	// Collection<Object> coll1= c1.getExampleIds();
	// //Double [] niz1=coll1.toArray(new Double[0]);
	// //String [] niz1=coll1.toArray(new String[0]);
	//
	// double trenPreracunavanje=0;
	// for(int i=0;i<exampleSet.size();i++)
	// {
	// Example ex2=exampleSet.getExample(i);
	// trenPreracunavanje=trenPreracunavanje+measure.calculateDistance(tools.getAsDoubleArray(ex1,ex1.getAttributes()
	// ), tools.getAsDoubleArray(ex2, ex2.getAttributes()));
	// }
	// if(min>trenPreracunavanje/exampleSet.size())
	// {
	// min=trenPreracunavanje/exampleSet.size();
	// }
	// }
	// }
	//
	// array[counter2][2]=min;
	// if(array[counter2][2]>array[counter2][1])
	// {
	// array[counter2][3]=(array[counter2][2]-array[counter2][1])/array[counter2][2];
	// }
	// else
	// {
	// array[counter2][3]=(array[counter2][2]-array[counter2][1])/array[counter2][1];
	// }
	// counter2++;
	// }
	//
	// // for(int i=0;i<array.length;i++)
	// // {
	// // gsi=gsi+array[i][3];
	// // }
	// for(Cluster c1:model.getClusters()){
	// double gsiPom=0;
	// int f=0;
	// for(Example example5:exampleSet)
	// {
	// if (model.getClusterIndexOfId(example5)==c1.getClusterId())
	// gsiPom=gsiPom+array[f][3];
	// f++;
	// }
	// gsi = gsi/c1.getNumberOfExamples();
	// }
	//
	// return gsi/model.getNumberOfClusters();
	// }
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
