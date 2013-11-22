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
import rs.fon.whibo.problem.SubproblemParameter;

/**
 * This component evaluates returns a random value as a split evaluation
 * 
 * This split evaluation measure is used in the CART algorithm Breiman et al.
 * (1984) Classification and Regression Trees, CRC Press .
 * 
 * @author Milan Vukicevic
 * @componentName Random Evaluation
 */
public class RandomEval extends AbstractSplitEvaluation {

	/**
	 * Instantiates a Random Evaluation component for split evaluation.
	 * 
	 * @param parameters
	 *            - this method takes empty parameter list.
	 */
	public RandomEval(List<SubproblemParameter> parameters) {
		super(parameters);

	}

	/**
	 * Evaluates split by random value.
	 * 
	 * @param exampleSet
	 *            - candidate split that will be evaluated.
	 * 
	 * @return double value - random value
	 */
	@Override
	public double evaluate(SplittedExampleSet exampleSet) {

		return Math.random();

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
