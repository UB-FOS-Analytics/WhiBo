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
 * This component evaluates the quality of a split with the Distance measure.
 * This measure is based on a distance between partitions such that the selected
 * attribute in a node induces the partition which is closest to the correct
 * partition of the subset of training examples corresponding to this node. It
 * is also formally proved that our distance is not biased towards attributes
 * with large numbers of values. This measure is an improvement of Information
 * gain measure.
 * 
 * R.L. De Mantaras (1991) A Distance-Based Attribute Selection Measure for
 * Decision Tree Induction, Machine Learning
 * 
 * @author Nemanja Stevic
 * @componentName Distance Measure
 */
public class DistanceMeasure extends AbstractSplitEvaluation {

	/**
	 * Instantiates a distance measure component for split evaluation.
	 * 
	 * @param parameters
	 *            - this method takes empty parameter list.
	 */
	public DistanceMeasure(List<SubproblemParameter> parameters) {
		super(parameters);

	}

	/** The LOG factor used in the distance formulae */
	private static double LOG_FACTOR = 1d / Math.log(2);

	/** Calculates class frequencies for candidate splits. */
	private FrequencyCalculator calculator = new FrequencyCalculator();

	/**
	 * Evaluates possible splits with distance measure.
	 * 
	 * @param exampleSet
	 *            - possible split that will be evaluated.
	 * 
	 * @return double value - evaluation of possible split.
	 */
	@Override
	public double evaluate(SplittedExampleSet exampleSet) {
		double[] totalWeights = calculator.getLabelWeights(exampleSet);
		double totalWeight = calculator.getTotalWeight(totalWeights);
		double[] partitionWeights_ = calculator.getPartitionWeights(exampleSet);
		double I_Pb = getEntropy(totalWeights, totalWeight);
		double I_Pa = getEntropy(partitionWeights_, totalWeight);
		double I_Pa_presek_Pb = 0;

		for (int i = 0; i < exampleSet.getNumberOfSubsets(); i++) {
			exampleSet.selectSingleSubset(i);
			double[] partitionWeights = calculator.getLabelWeights(exampleSet);
			I_Pa_presek_Pb += getEntropy(partitionWeights, totalWeight);
		}

		double I_Pb_kroz_Pa = I_Pa_presek_Pb - I_Pa;
		double I_Pa_kroz_Pb = I_Pa_presek_Pb - I_Pb;
		double d_od_Pa_Pb = I_Pb_kroz_Pa + I_Pa_kroz_Pb;
		double dn_od_Pa_Pb = d_od_Pa_Pb / I_Pa_presek_Pb;
		exampleSet.selectAllSubsets();
		return dn_od_Pa_Pb;
	}

	/**
	 * Calculates entropy as an impurity measure of a split.
	 * 
	 * @param labelWeights
	 *            the label weights
	 * @param totalWeight
	 *            the total weight
	 * 
	 * @return the entropy value
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
	 * @return true if x is better than, else false.
	 */
	@Override
	public boolean betterThan(double x, double y) {
		if (x < y)
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
		return Double.POSITIVE_INFINITY;
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
