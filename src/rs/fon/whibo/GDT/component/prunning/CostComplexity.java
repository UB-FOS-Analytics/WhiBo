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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rs.fon.whibo.GDT.tools.PruningTools;
import rs.fon.whibo.problem.Parameter;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.tree.Edge;
import com.rapidminer.operator.learner.tree.Tree;

/**
 * This component implements a Cost-Complexity Pruning procedure, which prunes a
 * subtree to a leaf only if complexity decrease of pruning the subtree does not
 * increase the expected error "much" (1 standard error).
 * 
 * Introduces by: Breiman, L., Friedman, J. H., Olshen, R. A. and Stone., C. J.
 * (1983). "Classification and Regression Trees." Wadsworth.
 * 
 * @author Milos Jovanovic
 * @componentName Cost Complexity Pruning
 */
public class CostComplexity extends AbstractPrunning {

	@Parameter(defaultValue = "0", minValue = "0", maxValue = "1")
	private Integer Trade_One_StandardError_For_Simplicity;

	private Tree bestKnownNodeForPruning;

	/**
	 * Instantiates a new reduced error component.
	 * 
	 * @param parameters
	 *            the parameters passed by the user
	 */
	public CostComplexity(
			List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
		Trade_One_StandardError_For_Simplicity = Integer.parseInt(parameters
				.get(0).getXenteredValue());
		;
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
			tree = pruneTree(tree, pruneSet);
			return tree;

		} catch (Exception e) {
			// TODO: implementiraj TRY CATCH
			return null;
		}
	}

	private Tree pruneTree(Tree root, ExampleSet pruneSet) {

		int totalNumberOfCases = root.getFrequencySum();

		// create pruned tree candidates Tn (1st phase)
		ArrayList<Tree> treeCandidates = new ArrayList<Tree>();
		Tree treeCandidate = PruningTools.deepCopy(root);
		treeCandidates.add(treeCandidate);

		Tree currentTree = root;
		while (!currentTree.isLeaf()) {
			findBestNodeToPrune(currentTree); // outputs to global field:
												// bestKnownNodeForPruning
			bestKnownNodeForPruning.setLeaf(PruningTools
					.predictedLabel(bestKnownNodeForPruning)); // prune node
			bestKnownNodeForPruning.removeChildren();
			treeCandidate = PruningTools.deepCopy(currentTree); // clone
			PruningTools
					.recalculateTreeNodesStatistics(treeCandidate, pruneSet); // USE
																				// PruneSet
																				// to
																				// calculate
																				// class
																				// distributions
																				// in
																				// all
																				// nodes
			treeCandidates.add(treeCandidate);
		}

		// choose best pruned tree candidate (2nd phase)

		// find minimum possible errors of any pruned tree
		int minErrorsPossible = Integer.MAX_VALUE;
		for (Tree tree : treeCandidates) {
			int treeError = calculateSubTreeErrors(tree);
			if (treeError < minErrorsPossible)
				minErrorsPossible = treeError;
		}
		double standardError = Math
				.sqrt((minErrorsPossible * (totalNumberOfCases - minErrorsPossible))
						/ totalNumberOfCases);
		double errorThreshold;
		if (Trade_One_StandardError_For_Simplicity == 1)
			errorThreshold = minErrorsPossible + standardError;
		else
			errorThreshold = minErrorsPossible;// + standardError;

		Tree bestTree = treeCandidates.get(0);

		for (int treeIndex = 1; treeIndex < treeCandidates.size(); treeIndex++) {
			if (calculateSubTreeErrors(treeCandidates.get(treeIndex)) <= errorThreshold) {
				bestTree = treeCandidates.get(treeIndex);
			}
		}

		return bestTree;
	}

	private double findBestNodeToPrune(Tree currentNode) {

		// calculate alpha for node
		double nodeAlpha = calculateAlpha(currentNode);
		double bestAlphaOfChildren = Double.MAX_VALUE;

		// calculate alpha for children
		Iterator<Edge> edges = currentNode.childIterator();
		while (edges.hasNext()) {
			double childrenAlpha;
			childrenAlpha = findBestNodeToPrune(edges.next().getChild());
			if (childrenAlpha < bestAlphaOfChildren)
				bestAlphaOfChildren = childrenAlpha;
		}

		// determine and return best alpha
		if (nodeAlpha <= bestAlphaOfChildren) {
			bestKnownNodeForPruning = currentNode;
			return nodeAlpha;
		} else
			return bestAlphaOfChildren;
	}

	private double calculateAlpha(Tree currentNode) {

		int errorAsSubTree = calculateSubTreeErrors(currentNode);
		int errorAsLeaf = currentNode.getFrequencySum()
				- currentNode
						.getCount(PruningTools.predictedLabel(currentNode));

		int numberOfLeavesAsSubTree = calculateNumberOfLeaves(currentNode);

		int errorsDifference = errorAsLeaf - errorAsSubTree;
		int leafsDifference = numberOfLeavesAsSubTree - 1;

		return ((double) errorsDifference) / ((double) leafsDifference);
	}

	public int calculateSubTreeErrors(Tree currentNode) {

		// Calculate subTree error
		if (currentNode.isLeaf())
			return currentNode.getFrequencySum()
					- currentNode.getCount(PruningTools
							.predictedLabel(currentNode));
		else {
			int totalSubTreeError = 0;
			Iterator<Edge> childs = currentNode.childIterator();
			while (childs.hasNext()) {
				Tree subTree = childs.next().getChild();
				totalSubTreeError += calculateSubTreeErrors(subTree);
			}
			return totalSubTreeError;
		}
	}

	public int calculateNumberOfLeaves(Tree currentNode) {
		if (currentNode.isLeaf())
			return 1;
		else {
			int totalLeaves = 0;
			Iterator<Edge> childs = currentNode.childIterator();
			while (childs.hasNext()) {
				Tree subTree = childs.next().getChild();
				totalLeaves += calculateNumberOfLeaves(subTree);
			}
			return totalLeaves;
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
