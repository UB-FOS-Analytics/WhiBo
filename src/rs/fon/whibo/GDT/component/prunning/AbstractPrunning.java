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
package rs.fon.whibo.GDT.component.prunning;

import java.util.List;

import rs.fon.whibo.problem.AbstractComponent;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.operator.learner.tree.DecisionTreeLeafCreator;

/**
 * This component some criterion criterion implemented in inherited classes to
 * decide which subtree to replace with a node.
 * 
 * As an input it takes Decision tree and list of parameters for evaluating
 * subtree quality . Can be used to reduce the tree in order to get more
 * accurate or more understandable trees.
 * 
 * @author Milos Jovanovic, Milan Vukicevic
 */
public abstract class AbstractPrunning extends AbstractComponent implements
		Prunning {

	/** The parameters for evaluation of subtree quality. */
	protected List<SubproblemParameter> parameters;

	/** The leaf creator. */
	protected DecisionTreeLeafCreator leafCreator;

	/**
	 * Instantiates a new abstract prunning.
	 * 
	 * @param parameters
	 *            the parameters
	 */
	public AbstractPrunning(List<SubproblemParameter> parameters) {
		this.parameters = parameters;
		leafCreator = new DecisionTreeLeafCreator();
	}

}
