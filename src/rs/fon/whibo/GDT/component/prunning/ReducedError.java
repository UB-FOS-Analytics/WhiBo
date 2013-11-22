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

import java.util.Iterator;
import java.util.List;

import rs.fon.whibo.GDT.tools.PruningTools;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.tree.Edge;
import com.rapidminer.operator.learner.tree.Tree;

/**
 * This component implements a Reduced Error Pruning procedure, which prunes a
 * subtree to a leaf only if a leaf produces smaller error then the subtree.
 * 
 * Applying REP with a set of test examples (S) to a decision tree (T) produces
 * a pruning of T (Tprim), such that it is the smallest of those prunings of T
 * that have minimal error with respect to the test set.
 * 
 * Introduces by: Quinlan JR (1993) C4.5 Programs for Machine Learning, Morgan
 * Kaufmann. Implemented version desribed in: Elomaa T., Kaariainen M. (2001).
 * An analysis of reduced error pruning. Journal of Artificial Intelligence
 * Research, 15(1), 163–187.
 * 
 * @author Milos Jovanovic
 * @componentName Reduced Error Pruning
 */
public class ReducedError extends AbstractPrunning {

	/**
	 * Instantiates a new reduced error component.
	 * 
	 * @param parameters
	 *            the parameters passed by the user
	 */
	public ReducedError(
			List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
	}

	/**
	 * Prunes the given tree.
	 * 
	 * @param root
	 *            - root node of decision tree that will be pruned
	 * @throws OperatorException
	 */
	@Override
	public Tree prune(Tree root, ExampleSet pruneSet) {

		try {
			Tree tree = PruningTools.deepCopy(root);
			PruningTools.setAllNodeLabels(tree); // sets labels to all nodes
													// using Growing set
			PruningTools.recalculateTreeNodesStatistics(tree, pruneSet); // USE
																			// PruneSet
																			// to
																			// calculate
																			// class
																			// distribution
																			// in
																			// each
																			// node
			pruneChild(tree);
			return tree;
		} catch (Exception e) {
			// TODO: implementiraj TRY CATCH
			return null;
		}
	}

	/**
	 * Prunes the child node.
	 * 
	 * @param currentNode
	 *            - current node
	 * 
	 * @return the expected error based on the prune set
	 * 
	 * @throws OperatorException
	 *             the operator exception
	 */
	private int pruneChild(Tree currentNode) throws OperatorException {

		// Calculate leaf error
		String leafLabel = currentNode.getLabel();
		int totalLeafError = currentNode.getFrequencySum()
				- currentNode.getCount(leafLabel);

		if (currentNode.isLeaf())
			return totalLeafError;

		else {

			// Calculate subTree error
			int totalSubTreeError = 0;
			Iterator<Edge> childs = currentNode.childIterator();
			while (childs.hasNext()) {
				Tree subTree = childs.next().getChild();
				totalSubTreeError += pruneChild(subTree);
			}

			if (totalSubTreeError < totalLeafError) // No need for pruning
													// subtree
				return totalSubTreeError;
			else { // Prune subTree to Leaf
				currentNode.setLeaf(leafLabel);
				currentNode.removeChildren();
				return totalLeafError;
			}
		}
	}

	/**
	 * Gets collection of class names of components that are not compatible with
	 * this component
	 * 
	 * @return collection of class names of not compatible components
	 */
	public String[] getNotCompatibleClassNames() {
		return new String[] {

		};
	}

	/**
	 * Gets collection of class names of components which are exclusive to this
	 * component
	 * 
	 * @return collection of class names of exclusive components
	 */
	public String[] getExclusiveClassNames() {
		return new String[] {

		};
	}

	@Override
	public boolean usesPruneSet() {
		return true;
	}

}
