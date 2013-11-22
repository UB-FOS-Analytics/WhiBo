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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rs.fon.whibo.GDT.tools.PruningTools;
import rs.fon.whibo.GDT.tools.Tools;
import rs.fon.whibo.problem.Parameter;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.learner.tree.Edge;
import com.rapidminer.operator.learner.tree.Tree;
import com.rapidminer.tools.math.MathFunctions;

/**
 * This component uses a pessimistic criterion to decide which subtree to
 * replace with a node.
 * 
 * As an input it takes Decision tree in progress. As an output it takes
 * confidence level (0, 0.5] If this value is closer to 0.5 more sever pruning
 * is Can be used to reduce the tree in order to get more accurate or more
 * understandable trees. This pruning technique was first introduced in Quinlan
 * JR (1993).
 * 
 * Quinlan JR (1993) C4.5 Programs for Machine Learning, Morgan Kaufmann.
 * 
 * @author Nikola Nikolic
 * @componentName Pessimistic Error
 */
public class PessimisticError extends AbstractPrunning {

	/** The Confidence level parameter defined by the user. */
	@Parameter(defaultValue = "0.25", maxValue = "0.5", minValue = "0.0")
	private Double Confidence_Level;

	/**
	 * Defines the amount of preference that pruned tree needs to be better than
	 * non-pruned. If not met, prunning will not occur.
	 */
	/** Value is predefined and fixed */
	private static final double PRUNE_PREFERENCE = 0.001;

	/**
	 * Instantiates a new pessimistic error component.
	 * 
	 * @param parameters
	 *            the parameters passed by the user
	 */
	public PessimisticError(
			List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
		Confidence_Level = Double.parseDouble(parameters.get(0)
				.getXenteredValue());
	}

	/**
	 * Prunes the given tree.
	 * 
	 * @param root
	 *            - root node of decision tree that will be pruned
	 */
	@Override
	public Tree prune(Tree root, ExampleSet pruneSet) {
		Tree tree = PruningTools.deepCopy(root);
		pruneChild(tree, null);
		return tree;
	}

	/**
	 * Prunes the child node.
	 * 
	 * @param currentNode
	 *            - current node
	 * @param father
	 *            - father node
	 */
	private void pruneChild(Tree currentNode, Tree father) {

		// going down to fathers of leafs
		if (!currentNode.isLeaf()) {
			Iterator<Edge> childIterator = currentNode.childIterator();
			while (childIterator.hasNext()) {
				pruneChild(childIterator.next().getChild(), currentNode);
			}
			if (!childrenHaveChildren(currentNode)) {

				// calculating error estimate for leafs
				double leafsErrorEstimate = 0;
				childIterator = currentNode.childIterator();
				Set<String> classSet = new HashSet<String>();
				while (childIterator.hasNext()) {
					Tree leafNode = childIterator.next().getChild();
					classSet.add(leafNode.getLabel());
					int examples = leafNode.getFrequencySum();
					double currentErrorRate = (1 - (leafNode.getCounterMap()
							.get(leafNode.getLabel()) / (double) examples));
					leafsErrorEstimate += pessimisticErrors(examples,
							currentErrorRate, Confidence_Level)
							* (((double) examples) / (double) currentNode
									.getSubtreeFrequencySum());
				}

				// calculating error estimate for current node
				if (classSet.size() <= 1) {
					Tools.treeToLeaf(currentNode);
				} else {
					Tree prunnedCurrentNode = (Tree) Tools
							.cloneSerializable(currentNode);
					Tools.treeToLeaf(prunnedCurrentNode);
					int examples = prunnedCurrentNode.getFrequencySum();
					double currentErrorRate = (1 - (prunnedCurrentNode
							.getCounterMap().get(prunnedCurrentNode.getLabel()) / (double) examples));
					double nodeErrorEstimate = pessimisticErrors(examples,
							currentErrorRate, Confidence_Level);
					// if currentNode error level is less than children: prune

					if (nodeErrorEstimate - PRUNE_PREFERENCE <= leafsErrorEstimate) {
						Tools.treeToLeaf(currentNode);
					}
				}
			}
		}
	}

	/**
	 * Checks if the children of the given node have children.
	 * 
	 * @param node
	 *            given tree node
	 * 
	 * @return true, if children of father node have more children
	 */
	private boolean childrenHaveChildren(Tree node) {
		Iterator<Edge> iterator = node.childIterator();
		while (iterator.hasNext()) {
			if (!iterator.next().getChild().isLeaf())
				return true;
		}
		return false;
	}

	/**
	 * This method calculates the pessimistic number of errors, using some
	 * confidence level.
	 * 
	 * @param numberOfExamples
	 *            - the number of examples in current node
	 * @param errorRate
	 *            - the error rate for classification
	 * @param confidenceLevel
	 *            - confidence level for pruning
	 * 
	 * @return the estimated number of errors
	 */
	public double pessimisticErrors(double numberOfExamples, double errorRate,
			double confidenceLevel) {
		if (errorRate < 1E-6) {
			return errorRate
					+ numberOfExamples
					* (1.0 - Math.exp(Math.log(confidenceLevel)
							/ numberOfExamples));
		} else if (errorRate + 0.5 >= numberOfExamples) {
			return errorRate + 0.67 * (numberOfExamples - errorRate);
		} else {
			double coefficient = MathFunctions
					.normalInverse(1 - confidenceLevel);
			coefficient *= coefficient;
			double pessimisticRate = (errorRate + 0.5 + coefficient / 2.0d + Math
					.sqrt(coefficient
							* ((errorRate + 0.5)
									* (1 - (errorRate + 0.5) / numberOfExamples) + coefficient / 4.0d)))
					/ (numberOfExamples + coefficient);
			return (numberOfExamples * pessimisticRate);
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
		// MinNodeSize.class.getName(),
		// FTestNumerical.class.getName(),
		// GiniIndex.class.getName(),
		// TreeDepth.class.getName(),
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
		// ChiSquareTestCategorical.class.getName(),
		// InformationGain.class.getName(),
		// DistanceMeasure.class.getName(),
		// TreeDepth.class.getName(),
		// PessimisticError.class.getName(),
		};
	}

	@Override
	public boolean usesPruneSet() {
		return false;
	}

}
