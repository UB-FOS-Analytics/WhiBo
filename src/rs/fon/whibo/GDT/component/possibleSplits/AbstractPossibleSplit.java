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

package rs.fon.whibo.GDT.component.possibleSplits;

import java.util.List;
import java.util.ListIterator;

import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import rs.fon.whibo.problem.AbstractComponent;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.Partition;

/**
 * The Class AbstractPossibleSplit that is inherited by all Possible split
 * components. As an input this class takes example set in current node and list
 * of attributes that are available for possible split creation. As an output
 * this class provides splitted example set (possible split for branching a
 * tree) that is proceeded to Split Evaluation component.
 * 
 */
public abstract class AbstractPossibleSplit extends AbstractComponent implements
		PossibleSplit {

	/** List of parameters for Possible Split components. */
	protected List<SubproblemParameter> parameters;

	/** Current split for evaluation . */
	protected SplittedExampleSet currentSplit;

	/** List of attributes that are awailable for possible split creation. */
	protected List<Attribute> attributesForSplitting;

	/** The iterator att. */
	ListIterator<Attribute> iteratorAtt;

	/**
	 * Instantiates a new abstract possible split.
	 * 
	 * @param parameters
	 */
	public AbstractPossibleSplit(List<SubproblemParameter> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Inits the component data and attributes for splitting.
	 * 
	 * @param exampleSet
	 *            in current node
	 * @param attributesForSplitting
	 *            the attributes for splitting
	 */
	@Override
	public void init(ExampleSet exampleSet,
			List<Attribute> attributesForSplitting) {
		currentSplit = new SplittedExampleSet(exampleSet, new Partition(
				new int[exampleSet.size()], 2));
		this.attributesForSplitting = attributesForSplitting;
		iteratorAtt = attributesForSplitting.listIterator();
	}
}
