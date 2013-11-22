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
 * This component evaluates the quality of a split with the Information gain
 * measure. This measure is based on entropy calculation of an input attribute
 * compared to the output attribute. It measures which input attribute describes
 * the output attribute best, and thus reduces entropy.
 * 
 * This split evaluation measure is used in ID3 algorithm Quinlan JR (1986)
 * Induction of decision trees, Machine Learning.
 * 
 * @author Milan Vukicevic
 * @componentName Information Gain
 */
public class InformationGain extends AbstractSplitEvaluation {

	/**
	 * Instantiates a information gain component for split evaluation.
	 * 
	 * @param parameters
	 *            - this method takes empty parameter list.
	 */
	public InformationGain(List<SubproblemParameter> parameters) {
		super(parameters);

	}

	/** The LOG factor used in the formulae */
	private static double LOG_FACTOR = 1d / Math.log(2);

	/** Calculates class frequencies for candidate splits. */
	private FrequencyCalculator calculator = new FrequencyCalculator();

	/**
	 * Evaluates possible splits with information gain.
	 * 
	 * @param exampleSet
	 *            - candidate split that will be evaluated.
	 * 
	 * @return double value - evaluation of candidate split.
	 */
	@Override
	public double evaluate(SplittedExampleSet exampleSet) {
		double[] totalWeights = calculator.getLabelWeights(exampleSet);
		double totalWeight = calculator.getTotalWeight(totalWeights);
		double totalEntropy = getEntropy(totalWeights, totalWeight);
		double gain = 0;
		for (int i = 0; i < exampleSet.getNumberOfSubsets(); i++) {
			exampleSet.selectSingleSubset(i);
			double[] partitionWeights = calculator.getLabelWeights(exampleSet);
			double partitionWeight = calculator
					.getTotalWeight(partitionWeights);
			gain += getEntropy(partitionWeights, partitionWeight)
					* partitionWeight / totalWeight;
		}
		exampleSet.selectAllSubsets();
		return totalEntropy - gain;
	}

	/**
	 * Calculates entropy as an impurity measure of a split.
	 * 
	 * @param labelWeights
	 *            the weights of each class label based on example count
	 * @param totalWeight
	 *            the total weight
	 * 
	 * @return the entropy for candidate split
	 */
	public double getEntropy(double[] labelWeights, double totalWeight) {
		double entropy = 0;
		for (int i = 0; i < labelWeights.length; i++) {
			if (labelWeights[i] > 0) {
				double proportion = labelWeights[i] / totalWeight;
				entropy -= (Math.log(proportion) * LOG_FACTOR) * proportion;
			}
		}
		return entropy;
	}

	/**
	 * Compares evaluation values of two candidate splits.
	 * 
	 * @param x
	 *            - first evaluation value
	 * @param y
	 *            - second evaluation value
	 * 
	 * @return true if x is better than y.
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
