package rs.fon.whibo.GDT.component.splitEvaluation;

import java.util.List;

import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import rs.fon.whibo.GDT.tools.FrequencyCalculator;
import rs.fon.whibo.problem.SubproblemParameter;

/**
 * This component evaluates the quality of a split with the Class Confidence
 * Proportion measure. This measure is presented on SIAM 2010 conference
 * 
 * 
 * @author Sandro Radovanovic
 * @componentName Class Confidence
 */
public class ClassConfidence extends AbstractSplitEvaluation {

	/**
	 * Instantiates a class confidence component for split evaluation.
	 * 
	 * @param parameters
	 *            - this method takes empty parameter list.
	 */
	public ClassConfidence(List<SubproblemParameter> parameters) {
		super(parameters);

	}

	/** The LOG factor used in the formulae */
	private static double LOG_FACTOR = 1d / Math.log(2);

	/** Calculates class frequencies for candidate splits. */
	private FrequencyCalculator calculator = new FrequencyCalculator();

	/**
	 * Evaluates possible splits with class confidence.
	 * 
	 * @param exampleSet
	 *            - candidate split that will be evaluated.
	 * 
	 * @return double value - evaluation of candidate split.
	 */
	@Override
	public double evaluate(SplittedExampleSet exampleSet) {
		try {
			double[] totalWeights = calculator.getLabelWeights(exampleSet);
			double totalWeight = calculator.getTotalWeight(totalWeights);
			double totalClassConfidence = getClassConfidence(totalWeights,
					totalWeight, totalWeights, totalWeight);
			double gain = 0;
			for (int i = 0; i < exampleSet.getNumberOfSubsets(); i++) {
				exampleSet.selectSingleSubset(i);
				double[] partitionWeights = calculator
						.getLabelWeights(exampleSet);
				double partitionWeight = calculator
						.getTotalWeight(partitionWeights);
				gain += getClassConfidence(partitionWeights, partitionWeight,
						totalWeights, totalWeight);
			}
			exampleSet.selectAllSubsets();
			return totalClassConfidence - gain;
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return 0;
	}

	/**
	 * Calculates class confidence as an impurity measure of a split.
	 * 
	 * @param labelWeights
	 *            the weights of each class label based on example count
	 * @param totalWeight
	 *            the total weight
	 * @param totalLabelWeights
	 *            the weights of whole example subset
	 * @param totalTotalWeight
	 *            the weight of total subset
	 * 
	 * @return the class confidence for candidate split
	 */
	public double getClassConfidence(double[] labelWeights, double totalWeight,
			double[] totalLabelWeights, double totalTotalWeight) {
		try {
			double classConfidence = 0;
			double y_part = 0;
			double not_y = 0;
			double not_y_part = 0;
			for (int i = 0; i < labelWeights.length; i++) {
				not_y = totalTotalWeight - totalLabelWeights[i];
				not_y_part = totalWeight - labelWeights[i];

				if (totalLabelWeights[i] != 0) {
					y_part = labelWeights[i] / totalLabelWeights[i];
				} else {
					y_part = 0;
				}

				if (not_y != 0) {
					not_y_part = not_y_part / not_y;
				} else {
					not_y_part = 0;
				}

				double proportion = y_part / (y_part + not_y_part);

				if (proportion > 0) {
					classConfidence -= (Math.log(proportion) * LOG_FACTOR)
							* labelWeights[i] / totalLabelWeights[i];
				}
			}
			return classConfidence;
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return 0;
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
