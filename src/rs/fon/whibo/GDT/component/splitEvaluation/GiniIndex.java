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
package rs.fon.whibo.GDT.component.splitEvaluation;

import java.util.List;

import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import rs.fon.whibo.GDT.tools.FrequencyCalculator;
import rs.fon.whibo.problem.SubproblemParameter;

/**
 * This component evaluates the quality of a split with the Gini ratio measure.
 * It is based on probability calculation of an input attribute compared to the
 * output attribute. It measures which input attribute describes the output
 * attribute best, and thus reduces impurity of a node. The purest node is
 * chosen as the best split.
 * 
 * This split evaluation measure is used in the CART algorithm Breiman et al.
 * (1984) Classification and Regression Trees, CRC Press .
 * 
 * @author Milan Vukicevic
 * @componentName Gini Index
 */
public class GiniIndex extends AbstractSplitEvaluation {

	/**
	 * Instantiates a Gini index component for split evaluation.
	 * 
	 * @param parameters
	 *            - this method takes empty parameter list.
	 */
	public GiniIndex(List<SubproblemParameter> parameters) {
		super(parameters);

	}

	/** The best benefit. */
	double bestBenefit = -1;

	/** Calculates class frequencies for candidate splits. */
	private FrequencyCalculator calculator = new FrequencyCalculator();

	/**
	 * Evaluates possible splits with gini index.
	 * 
	 * @param exampleSet
	 *            - candidate split that will be evaluated.
	 * 
	 * @return double value - evaluation of candidate split using gini index.
	 */
	@Override
	public double evaluate(SplittedExampleSet exampleSet) {

		double[] totalWeights = calculator.getLabelWeights(exampleSet);
		double totalWeight = calculator.getTotalWeight(totalWeights);
		double totalEntropy = getGiniIndex(totalWeights, totalWeight);
		double gain = 0;
		for (int j = 0; j < exampleSet.getNumberOfSubsets(); j++) {
			exampleSet.selectSingleSubset(j);
			double[] partitionWeights = calculator.getLabelWeights(exampleSet);
			double partitionWeight = calculator
					.getTotalWeight(partitionWeights);
			gain += getGiniIndex(partitionWeights, partitionWeight)
					* partitionWeight / totalWeight;
		}
		exampleSet.selectAllSubsets();
		return totalEntropy - gain;

	}

	/**
	 * Calculates gini index as an impurity measure of a split.
	 * 
	 * @param labelWeights
	 *            the weights of each class label based on example count
	 * @param totalWeight
	 *            the total weight
	 * 
	 * @return the gini index for candidate split
	 */
	private double getGiniIndex(double[] labelWeights, double totalWeight) {
		double sum = 0.0d;
		for (int i = 0; i < labelWeights.length; i++) {
			double frequency = labelWeights[i] / totalWeight;
			sum += frequency * frequency;
		}
		return 1.0d - sum;
	}

	/**
	 * Compares evaluation values of two candidate splits.
	 * 
	 * @param x
	 *            - first evaluation value
	 * @param y
	 *            - second evaluation value
	 * 
	 * @return true if x is better than, else false.
	 */
	@Override
	public boolean betterThan(double x, double y) {
		if (x >= y)
			return true;
		else
			return false;
	}

	/**
	 * Worst possible evaluation value for candidate split.
	 * 
	 * @return double - worst evaluation value.
	 */
	@Override
	public double worstValue() {
		return 0;
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
