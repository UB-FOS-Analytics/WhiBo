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

import java.util.LinkedList;
import java.util.List;

import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import rs.fon.whibo.GDT.tools.ChiSquareTest;
import rs.fon.whibo.GDT.tools.Tools;

/**
 * This component evaluates the quality of a split with the chi-square test. It
 * checks whether the proposed split is statistically significant with respect
 * to output attribute.
 * 
 * This split evaluation measure is used in the CHAID algorithm Kass GV (1980)
 * An Exploratory Technique for Investigating Large Quantities of Categorical
 * Data, Journal of Applied Statistics.
 * 
 * @author Milan Vukicevic
 * @componentName Chi Square
 */

public class ChiSquare extends AbstractSplitEvaluation {

	/**
	 * Instantiates a chi square component for split evaluation.
	 * 
	 * @param parameters
	 *            - this method takes empty parameter list.
	 */
	public ChiSquare(List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
	}

	/**
	 * Evaluates possible splits with chi square test.
	 * 
	 * @param exampleSet
	 *            - possible split that will be evaluated.
	 * 
	 * @return double value - evaluation of possible split.
	 */
	@Override
	public double evaluate(SplittedExampleSet exampleSet) {

		ChiSquareTest chi = new ChiSquareTest(exampleSet);
		double chiSquareValue = chi.getChiSquare();
		return chiSquareValue;
	}

	/**
	 * Calculates bonferoni correction value.
	 * 
	 * @param categories
	 *            - list of categories, after concatenation
	 * 
	 * @return double - bonferrony value
	 */
	public double calculateBonferoni(LinkedList<String> categories) {
		double bonferoni = 1;
		int beforeReduction = 0;
		int afterReduction = categories.size();
		for (int i = 0; i < categories.size(); i++) {
			String[] splittedArray = categories.get(i).split(":");
			beforeReduction += splittedArray.length;
		}

		for (int i = 0; i <= afterReduction - 1; i++) {
			double a;
			double b;
			double c;
			a = Math.pow((afterReduction - i), beforeReduction);
			b = Tools.factorial(i) * Tools.factorial(afterReduction - i);
			c = Math.pow(-1, i);

			bonferoni += c * a / b;
		}

		return bonferoni;
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
