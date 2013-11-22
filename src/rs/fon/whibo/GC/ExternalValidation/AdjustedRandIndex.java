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
package rs.fon.whibo.GC.ExternalValidation;

import java.util.Iterator;
import java.util.List;

import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;
import rs.fon.whibo.GC.component.Evaluation.AbstractEvaluation;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.operator.clustering.ClusterModel;
import com.rapidminer.tools.Ontology;

public class AdjustedRandIndex extends AbstractEvaluation {

	public AdjustedRandIndex(List<SubproblemParameter> parameters) {
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
	@Override
	public double Evaluate(DistanceMeasure measure, ClusterModel model,
			ExampleSet exampleSet) {

		ExampleSet results = addClusterAttribute(measure, model, exampleSet);
		Attribute classAttribute = results.getAttributes().getLabel();
		Attribute clusterAttribute = results.getAttributes().getCluster();
		int numberOfClasses = classAttribute.getMapping().size();
		int numberOfClusters = clusterAttribute.getMapping().size();
		int[][] exampleAlocations = new int[numberOfClusters][numberOfClasses];
		int[] totalsPerCluster = new int[numberOfClusters];
		int[] totalsPerClass = new int[numberOfClasses];
		int totalExamples = results.size();

		// calculate distributions of examples in (cluster,class) cells
		Iterator<Example> it = results.iterator();
		Example e;
		while (it.hasNext()) {
			e = it.next();
			int clusterNo = (int) e.getValue(clusterAttribute);
			int classNo = (int) e.getValue(classAttribute);
			exampleAlocations[clusterNo][classNo] += 1;
			totalsPerCluster[clusterNo] += 1;
			totalsPerClass[classNo] += 1;
		}

		// calculate correspondance matrix
		int sumSquareAll = 0;
		int sumSquareClusterTotals = 0;
		int sumSquareClassTotals = 0;

		for (int k = 0; k < numberOfClasses; k++)
			for (int c = 0; c < numberOfClusters; c++)
				sumSquareAll += Math.pow(exampleAlocations[c][k], 2);

		for (int k = 0; k < numberOfClasses; k++)
			sumSquareClassTotals += Math.pow(totalsPerClass[k], 2);

		for (int c = 0; c < numberOfClusters; c++)
			sumSquareClusterTotals += Math.pow(totalsPerCluster[c], 2);

		double a = (sumSquareAll - totalExamples) / 2;
		double b = (sumSquareClassTotals - sumSquareAll) / 2;
		double c = (sumSquareClusterTotals - sumSquareAll) / 2;
		double d = (sumSquareAll + totalExamples ^ 2 - sumSquareClassTotals
				- sumSquareClusterTotals) / 2;

		double Nover2 = totalExamples * (totalExamples - 1) / 2;

		double numerator = (Nover2 * (a + d) - ((a + b) * (a + c) - (c + d)
				* (b + d)));
		double nominator = (Math.pow(Nover2, 2) - ((a + b) * (a + c) - (c + d)
				* (b + d)));

		double adjustedRandIndex = numerator / nominator;
		return adjustedRandIndex;
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

	@Override
	public double getWorstValue() {
		return Double.NEGATIVE_INFINITY;
	}

	private ExampleSet addClusterAttribute(DistanceMeasure measure,
			ClusterModel model, ExampleSet exampleSet) {
		ExampleSet result = (ExampleSet) exampleSet.clone();
		int[] clusterAssignments = model.getClusterAssignments(exampleSet);

		Attribute cluster = AttributeFactory.createAttribute("cluster",
				Ontology.NOMINAL);
		result.getExampleTable().addAttribute(cluster);
		result.getAttributes().setCluster(cluster);
		for (int i = 0; i < result.size(); i++) {
			result.getExample(i).setValue(cluster,
					"cluster_" + clusterAssignments[i]);
		}

		return result;
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
