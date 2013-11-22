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
import rs.fon.whibo.problem.Parameter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Statistics;
import com.rapidminer.operator.learner.tree.Edge;
import com.rapidminer.operator.learner.tree.Tree;

/**
 * Stops growth of decision tree on branches when there are not enough cases for
 * a leaf. As an input it takes Decision tree in progress, minimum leaf size
 * parameter. Can be used to reduce the tree in order to get more accurate or
 * more understandable trees. This pruning criterion can be used in all decision
 * tree classifiers.
 * 
 * @author Milan Vukicevic, Milos Jovanovic
 * @componentName Minimum Leaf Size
 */
public class MinLeafSize extends AbstractPrunning {

	/** The Size of leaf parameter entered by the user. */
	@Parameter(defaultValue = "30", minValue = "1", maxValue = "1000")
	private Double Size_Of_Leaf;

	/** The minimal size of leaf. Private variable used in calculation */
	private double minSizeOfLeaf;

	/**
	 * Instantiates a new min leaf size component.
	 * 
	 * @param parameters
	 *            the parameters
	 */
	public MinLeafSize(List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
		Size_Of_Leaf = Double.parseDouble(parameters.get(0).getXenteredValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.whibo.GDT.component.prunning.Prunning#prune(com.rapidminer.operator
	 * .learner.tree.Tree)
	 */
	@Override
	public Tree prune(Tree root, ExampleSet pruneSet) {
		minSizeOfLeaf = Size_Of_Leaf;
		Tree tree = PruningTools.deepCopy(root);
		pruneChild(tree);
		return tree;
	}

	/**
	 * Prunes the child nodes of a given tree, if it meets the conditions.
	 * 
	 * @param currentNode
	 *            Node which child should be checked for prunning
	 */
	private void pruneChild(Tree currentNode) {
		// going down to fathers of leafs
		if (!currentNode.isLeaf()) {
			Iterator<Edge> childIterator = currentNode.childIterator();
			while (childIterator.hasNext()) {
				pruneChild(childIterator.next().getChild());
			}

			boolean pruneNode = false;

			// checks if any child leaf node nas less than required leafSize
			childIterator = currentNode.childIterator();
			while (childIterator.hasNext()) {
				Tree node = childIterator.next().getChild();
				int nodeSize = node.getSubtreeFrequencySum();

				if (nodeSize < minSizeOfLeaf)
					pruneNode = true;
			}

			// if any leaf is too small, prune father node
			if (pruneNode) {
				rs.fon.whibo.GDT.tools.Tools.treeToLeaf(currentNode);
			}
		}
	}

	/**
	 * Gets the class label to replace the prunned tree (actually MODE of the
	 * class label distribution).
	 * 
	 * @param exampleSet
	 *            the example set of the pruned subtree
	 * 
	 * @return the class label
	 */
	public double prunedLabel(ExampleSet exampleSet) {
		Attribute labelAttribute = exampleSet.getAttributes().getLabel();
		exampleSet.recalculateAttributeStatistics(labelAttribute);
		double test = exampleSet.getStatistics(labelAttribute, Statistics.MODE);
		return test;
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

	@Override
	public boolean usesPruneSet() {
		return false;
	}

}
