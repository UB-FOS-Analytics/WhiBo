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

import rs.fon.whibo.problem.AbstractComponent;
import rs.fon.whibo.problem.SubproblemParameter;

/**
 * The Split Evaluation abstract class. This component solves the Evaluate Split
 * subproblem. The goal is to calculate the measure that is used to compare
 * different split proposals of the selected dataset. The best split is chosen
 * as the most potential to split the tree node.
 * 
 * @author Nikola
 */
public abstract class AbstractSplitEvaluation extends AbstractComponent
		implements SplitEvaluation {

	/** The user supplied parameters. */
	List<SubproblemParameter> parameters;

	/**
	 * Instantiates a new abstract split evaluation.
	 * 
	 * @param The
	 *            user supplied parameters
	 */
	public AbstractSplitEvaluation(List<SubproblemParameter> parameters) {
		this.parameters = parameters;
	}
}
