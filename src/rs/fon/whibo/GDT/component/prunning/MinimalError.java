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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rs.fon.whibo.GDT.tools.PruningTools;
import rs.fon.whibo.problem.Parameter;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.tree.Edge;
import com.rapidminer.operator.learner.tree.Tree;

/**
 * This component implements a Minimal Error Pruning procedure, which prunes a
 * subtree to a leaf only if a leaf produces smaller error then the subtree. It
 * does not utilize a separate prune set for estimating errors, but instead uses
 * a m-estimate statistical bayesian method.
 * 
 * Introduces by: Niblett & Bratko 1986, Cestnik & Bratko 1991
 * 
 * @author Milos Jovanovic
 * @componentName Minimal Error Pruning
 */
public class MinimalError extends AbstractPrunning {

	@Parameter(defaultValue = "2", minValue = "0", maxValue = "1000")
	private Double mParameter;

	private Map<String, Double> priorProbabilities;

	/**
	 * Instantiates a new minimal error component.
	 * 
	 * @param parameters
	 *            the parameters passed by the user
	 */
	public MinimalError(
			List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
		mParameter = Double.parseDouble(parameters.get(0).getXenteredValue());
		priorProbabilities = new LinkedHashMap<String, Double>();
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
			PruningTools.recalculateTreeNodesStatistics(tree, pruneSet);
			estimatePriorClassProbabilities(tree);
			pruneChild(tree);
			return tree;
		} catch (Exception e) {
			// TODO: implementiraj TRY CATCH
			return null;
		}
	}

	private void estimatePriorClassProbabilities(Tree root) {
		priorProbabilities.clear();
		double totalExamples = root.getFrequencySum();
		for (String s : root.getCounterMap().keySet()) {
			priorProbabilities
					.put(s, (double) root.getCount(s) / totalExamples);
		}

	}

	private double pruneChild(Tree currentNode) throws OperatorException {

		// Calculate leaf error
		double totalLeafError = mEstimateForError(currentNode);

		if (currentNode.isLeaf())
			return totalLeafError;

		else {

			// Calculate subTree error
			double totalSubTreeError = 0;
			Iterator<Edge> childs = currentNode.childIterator();
			while (childs.hasNext()) {
				Tree subTree = childs.next().getChild();
				double subTreeWeight = (double) subTree.getFrequencySum()
						/ (double) currentNode.getFrequencySum();
				totalSubTreeError += pruneChild(subTree) * subTreeWeight;
			}

			if (totalSubTreeError < totalLeafError) // No need for pruning
													// subtree
				return totalSubTreeError;
			else { // Prune subTree to Leaf
				currentNode.setLeaf(PruningTools.predictedLabel(currentNode));
				currentNode.removeChildren();
				return totalLeafError;
			}
		}
	}

	private double mEstimateForError(Tree treeNode) {

		double totalExamples = treeNode.getFrequencySum();
		String majorityClass = PruningTools.predictedLabel(treeNode);
		if (majorityClass == null)
			return 0;
		double numberOfMajorityClassExamples = treeNode.getCount(majorityClass);
		double majorityClassAprioriProbability = priorProbabilities
				.get(majorityClass);

		double mEstimate = (majorityClassAprioriProbability * mParameter + numberOfMajorityClassExamples)
				/ (totalExamples + mParameter);

		return 1 - mEstimate;
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
		return false;
	}

}
