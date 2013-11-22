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

import rs.fon.whibo.GDT.dataset.SplittedExampleSet;

/**
 * Interface for solving split evaluation subproblem
 * 
 * @author Milos Jovanovic
 */
public interface SplitEvaluation {

	/**
	 * Evaluate given split of an exampleSet.
	 * 
	 * @param exampleSet
	 *            split example set for evaluation
	 * 
	 * @return evaluation of the split "goodness"
	 */
	public double evaluate(SplittedExampleSet exampleSet);

	/**
	 * Checks if x operand is "better" than t operand, when compared
	 * interpreting evaluation measure.
	 * 
	 * @param x
	 *            first operand
	 * @param y
	 *            second operand
	 * 
	 * @return true, if x is better than y
	 */
	public boolean betterThan(double x, double y);

	/**
	 * Worst value on the evaluation scale.
	 * 
	 * @return the worst value
	 */
	public double worstValue();

}
