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
package rs.fon.whibo.GDT.algorithm;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import rs.fon.whibo.GDT.component.possibleSplits.PossibleSplit;
import rs.fon.whibo.GDT.component.prunning.Prunning;
import rs.fon.whibo.GDT.component.removeInsignificantAttributes.RemoveInsignificantAtributes;
import rs.fon.whibo.GDT.component.splitEvaluation.SplitEvaluation;
import rs.fon.whibo.GDT.component.stoppingCriteria.StoppingCriteria;
import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import rs.fon.whibo.GDT.logging.Log;
import rs.fon.whibo.GDT.logging.LogAlgorithm;
import rs.fon.whibo.GDT.logging.LogPossibleSplit;
import rs.fon.whibo.GDT.logging.LogRemoveInsignificantAttributes;
import rs.fon.whibo.GDT.logging.LogStoppingCriteria;
import rs.fon.whibo.GDT.logging.LogTreeState;
import rs.fon.whibo.GDT.tools.PruningTools;
import rs.fon.whibo.GDT.tools.Tools;
import rs.fon.whibo.GDT.treeModel.NominalSplitCondition;
import rs.fon.whibo.integration.adapters.example.ExampleSetAdapter;
import rs.fon.whibo.integration.adapters.interfaces.IGDTAlgorithm;
import rs.fon.whibo.integration.core.WhiboTree;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.learner.tree.AbstractSplitCondition;
import com.rapidminer.operator.learner.tree.DecisionTreeLeafCreator;
import com.rapidminer.operator.learner.tree.GreaterSplitCondition;
import com.rapidminer.operator.learner.tree.LessEqualsSplitCondition;
import com.rapidminer.operator.learner.tree.Tree;

/**
 * The Generic Decision Tree (GDT) algorithm shell. This Class is responsible of
 * running and controlling the execution of the decision tree induction
 * components. It relies on the subproblem interfaces and through them calls the
 * components to execute their part of the job.
 */
public class GDTAlgorithm implements IGDTAlgorithm {

	/**
	 * The tree state member that calculates the tree metrics (size, depth,
	 * etc.) throughout the tree building process.
	 */
	private TreeState treeState;
	private LogAlgorithm logAlgorithm;

	// COMPONENTS
	/**
	 * List of components selected to solve the RemoveInsignificantAttributes
	 * subproblem.
	 */
	private List<RemoveInsignificantAtributes> componentsRemove;

	/** List of components selected to solve the PossibleSplits subproblem. */
	private List<PossibleSplit> componentsSplit;

	/** List of components selected to solve the EvaluateSplit subproblem. */
	private SplitEvaluation componentEval;

	/** List of components selected to solve the StopCriteria subproblem. */
	private List<StoppingCriteria> componentsStop;

	/** List of components selected to solve the PruneTree subproblem. */
	private List<Prunning> componentsPrune;

	/**
	 * Instantiates a new GDT algorithm.
	 * 
	 * @param problem
	 *            file that holds all of the user defined information on
	 *            algorithm design.
	 * @throws Exception
	 *             with the description if something goes wrong.
	 */
	public GDTAlgorithm(Problem problem) throws Exception {
		initializeComponents(problem);
		// log = new LogState("C:\\WhiboTesting\\ExperimentLog.csv");
	}

	/**
	 * Initializes the components selected by the user for execution. The
	 * implementation objects of the components are passed to the subproblem
	 * interfaces that are used by the generic algorithm.
	 * 
	 * @param problem
	 *            file that holds all of the user defined information on
	 *            algorithm design.
	 * @throws Exception
	 *             with the description if something goes wrong.
	 */
	@SuppressWarnings("unchecked")
	private void initializeComponents(Problem problem) throws Exception {

		List<Subproblem> steps = problem.getSubproblems();
		Subproblem step;

		// REMOVE INSIGNIFICANT ATTRIBUTES
		step = steps.get(0);
		componentsRemove = new LinkedList<RemoveInsignificantAtributes>();

		if (step.getMultipleStepData() != null) {
			for (SubproblemData stepData : step.getMultipleStepData()) {
				Constructor c = Class.forName(
						stepData.getNameOfImplementationClass())
						.getConstructor(new Class[] { List.class });
				RemoveInsignificantAtributes rr = (RemoveInsignificantAtributes) c
						.newInstance(new Object[] { stepData
								.getListOfParameters() });
				componentsRemove.add((RemoveInsignificantAtributes) c
						.newInstance(new Object[] { stepData
								.getListOfParameters() }));
			}
		}

		// POSSIBLE SPLIT
		step = steps.get(1);
		componentsSplit = new LinkedList<PossibleSplit>();

		for (SubproblemData stepData : step.getMultipleStepData()) {
			Constructor c = Class.forName(
					stepData.getNameOfImplementationClass()).getConstructor(
					new Class[] { List.class });
			componentsSplit
					.add((PossibleSplit) c.newInstance(new Object[] { stepData
							.getListOfParameters() }));
		}

		if (componentsSplit.size() == 0) {
			throw new Exception("Please select at least one split component");
		}

		// EVAL SPLIT
		step = steps.get(2);
		if (step.getSubproblemData() == null) {
			throw new Exception("Please select split evaluation component");
		}
		{
			Constructor c = Class.forName(
					step.getSubproblemData().getNameOfImplementationClass())
					.getConstructor(new Class[] { List.class });
			componentEval = ((SplitEvaluation) c
					.newInstance(new Object[] { step.getSubproblemData()
							.getListOfParameters() }));
		}

		// STOPING CRITERIA
		step = steps.get(3);
		componentsStop = new LinkedList<StoppingCriteria>();

		if (step.getMultipleStepData() != null) {
			for (SubproblemData stepData : step.getMultipleStepData()) {
				Constructor c = Class.forName(
						stepData.getNameOfImplementationClass())
						.getConstructor(new Class[] { List.class });
				componentsStop.add((StoppingCriteria) c
						.newInstance(new Object[] { stepData
								.getListOfParameters() }));
			}
		}

		// PRUNING
		step = steps.get(4);
		componentsPrune = new LinkedList<Prunning>();

		if (step.getMultipleStepData() != null) {
			for (SubproblemData stepData : step.getMultipleStepData()) {
				Constructor c = Class.forName(
						stepData.getNameOfImplementationClass())
						.getConstructor(new Class[] { List.class });
				componentsPrune.add((Prunning) c
						.newInstance(new Object[] { stepData
								.getListOfParameters() }));
			}
		}

	}

	/**
	 * The main method where the execution of the generic algorithm starts.
	 * 
	 * @param exampleSet
	 *            the input dataset
	 * @return the resulting tree model
	 * @throws Exception
	 *             with the description if something goes wrong.
	 */
	public Tree learnTree(ExampleSet exampleSet) throws Exception {

		treeState = new TreeState();
		Tree root = new Tree(exampleSet); // .clone()); //OK/

		logAlgorithm = LogAlgorithm.log();

		// checks if there is need for splitting training set to: growing and
		// pruning set
		boolean needsSeparatePruneSet = false;
		for (Prunning p : componentsPrune)
			if (p.usesPruneSet())
				needsSeparatePruneSet = true;

		if (needsSeparatePruneSet) {
			SplittedExampleSet splittedSet = new SplittedExampleSet(exampleSet,
					0.7, SplittedExampleSet.STRATIFIED_SAMPLING, 2);
			splittedSet.selectSingleSubset(0); // select GrowingSet
			buildSubTree(root, splittedSet, logAlgorithm); // recursion

			splittedSet.selectSingleSubset(1); // select PruningSet
			root = pruneTree(root, splittedSet);
			PruningTools.recalculateTreeLeavesStatistics(root, exampleSet); // output
																			// tree
																			// with
																			// statistics
																			// using
																			// whole
																			// dataset
			PruningTools.setAllLeafLabels(root);
		} else {
			buildSubTree(root, exampleSet, logAlgorithm); // recursion
			root = pruneTree(root, exampleSet);
			PruningTools.setAllLeafLabels(root);
		}

		// treeState.analyseFinalTree(root);
		// LogManager.saveLog(logAlgorithm); //uncomment to enable logging
		return root;
	}

	/**
	 * Builds the sub tree for the sub sample passed through the parameter. The
	 * tree is build from the root node also passed through the parameter.
	 * 
	 * @param subTree
	 *            the sub tree to be built
	 * @param exampleSet
	 *            the dataset for the tree construction.
	 * @throws Exception
	 *             with the description if something goes wrong.
	 */
	private void buildSubTree(Tree subTree, ExampleSet exampleSet,
			Log subTreeLog) throws Exception {

		// =================================================================
		// -------------- ALGORITHM SETTINGS -----------------------------
		// =================================================================

		DecisionTreeLeafCreator leafCreator = new DecisionTreeLeafCreator();
		LinkedList<Attribute> attributesForSplit = new LinkedList<Attribute>();
		Iterator<Attribute> i = exampleSet.getAttributes().allAttributes();
		while (i.hasNext())
			attributesForSplit.add(i.next());

		SplittedExampleSet possibleSplit = null;
		SplittedExampleSet bestPossibleSplit = null;
		double splitEvaluation = 0;
		double bestSplitEvaluation = componentEval.worstValue();

		// =================================================================
		// -------------- ALGORITHM START --------------------------------
		// =================================================================

		// =================================================================
		// -------------- REMOVE ATTRIBUTES ------------------------------
		// =================================================================

		// log.LogPerformance(treeState);
		subTreeLog.add(LogTreeState.log(treeState));
		attributesForSplit = removeInsignificantAttributes(exampleSet,
				attributesForSplit, subTreeLog);
		// @log

		// =================================================================
		// -------------- STOPPING CRITERIA ------------------------------
		// =================================================================
		boolean splitNode = true;

		splitNode = checkStoppingCriteria(exampleSet, attributesForSplit,
				splitNode, subTreeLog);

		// =================================================================
		// -------------- SPLIT SUBTREE OR CREATE LEAF --------------------
		// =================================================================

		if (splitNode) {

			// SELECT BEST SPLIT

			for (PossibleSplit ps : componentsSplit) {
				ps.init(exampleSet, attributesForSplit);
				while (ps.hasNextSplit()) {
					possibleSplit = ps.nextSplit();
					splitEvaluation = componentEval.evaluate(possibleSplit);

					boolean splitIsBetter = componentEval.betterThan(
							splitEvaluation, bestSplitEvaluation);
					subTreeLog.add(LogPossibleSplit
							.logEvaluation(splitEvaluation,
									bestSplitEvaluation, splitIsBetter));
					if (splitIsBetter) {
						bestPossibleSplit = (SplittedExampleSet) possibleSplit
								.clone(); // OK/ //TODO: READ PROOF! da li uzima
											// memoriju
						bestSplitEvaluation = splitEvaluation;
					}
				}
			}

			// SPLIT SUBTREE AND ENTER RECURSION

			for (int l = 0; l < bestPossibleSplit.getNumberOfSubsets(); l++) {

				// select branch
				bestPossibleSplit.selectSingleSubset(l);

				Attribute bestAttribute = bestPossibleSplit.getAttribute();

				if (bestPossibleSplit.size() > 0) {
					Tree child = new Tree(null);

					AbstractSplitCondition condition = null;
					if (bestAttribute.isNominal()) {
						LinkedList<String> ll = bestPossibleSplit
								.getAllCategories(bestAttribute);
						condition = new NominalSplitCondition(bestAttribute, ll);
					} else {
						if (l == 0) {
							condition = new LessEqualsSplitCondition(
									bestAttribute,
									bestPossibleSplit.getSplitValue());
						} else {
							condition = new GreaterSplitCondition(
									bestAttribute,
									bestPossibleSplit.getSplitValue());
						}
					}
					subTree.addChild(child, condition);

					treeState.enterBranch();
					LogPossibleSplit logPossibleSplit = LogPossibleSplit
							.logBestPossibleSplit(l, bestPossibleSplit.size(),
									bestAttribute, condition);
					subTreeLog.add(logPossibleSplit);
					buildSubTree(child, bestPossibleSplit, logPossibleSplit);
					treeState.exitBranch(child);
				}
			}

		} else {
			// Create Leaf
			leafCreator.changeTreeToLeaf(subTree, exampleSet);
		}
	}

	/**
	 * Removes the insignificant attributes. This method removes the attributes
	 * useless for the tree building process at this node. After some default
	 * removal (e.g. label attributes), it calls the
	 * RemoveInsignificantAttributes component to do the user defined actions.
	 * 
	 * @param exampleSet
	 *            the dataset used to evaluate the usefulness of the attributes.
	 * @param attributesForSplit
	 *            starting collection of attributes
	 * @return resulting collection of attributes after removal
	 */
	private LinkedList<Attribute> removeInsignificantAttributes(
			ExampleSet exampleSet, LinkedList<Attribute> attributesForSplit,
			Log parentLog) {

		// removes label attribute
		Attribute attribute = exampleSet.getAttributes().getLabel();
		parentLog.add(LogRemoveInsignificantAttributes
				.logRemoveLabelAttribute(attribute));

		attributesForSplit.remove(attribute);

		// removes attributes with only single category
		Iterator<Attribute> iter = attributesForSplit.iterator();
		while (iter.hasNext()) {
			Attribute a = iter.next();
			if (Tools.getAllCategories(exampleSet, a).size() < 2) {
				parentLog.add(LogRemoveInsignificantAttributes
						.logRemoveOnlySingleCategoryAttribute(a));
				iter.remove();
			}
		}

		// izbacuje numericke/kategoricke atribute ako nija izabrana Split
		// strategija za njih
		boolean hasNumericalSplit = false;
		boolean hasCategoricalSplit = false;
		for (PossibleSplit ps : componentsSplit) {
			if (ps.isNumericalSplit())
				hasNumericalSplit = true;
			if (ps.isCategoricalSplit())
				hasCategoricalSplit = true;
		}
		Iterator<Attribute> it = attributesForSplit.iterator();
		Attribute a;
		while (it.hasNext()) {
			a = it.next();
			if (!hasNumericalSplit && a.isNumerical()) {
				parentLog.add(LogRemoveInsignificantAttributes
						.logNumercialCategorical(a));
				it.remove();
			}
			if (!hasCategoricalSplit && a.isNominal()) {
				parentLog.add(LogRemoveInsignificantAttributes
						.logNumercialCategorical(a));
				it.remove();
			}
		}

		// COMPONENT Remove Insignificant Attributes
		if (componentsRemove.size() > 0) {
			parentLog.add(LogRemoveInsignificantAttributes
					.logAttributesBeforeRemoval(attributesForSplit));
			for (RemoveInsignificantAtributes removeAttr : componentsRemove) {
				attributesForSplit = removeAttr.removeAttributes(exampleSet,
						attributesForSplit);
				parentLog.add(LogRemoveInsignificantAttributes
						.logAttributesAfterRemoval(attributesForSplit));
			}
		}

		return attributesForSplit;
	}

	/**
	 * Implements the complex criteria for stopping the tree building process.
	 * First checks for the trivial conditions (e.g. pure node). Lastly calls
	 * for the StoppingCriteria component to evaluate the user defined criteria
	 * for stopping the tree building process.
	 * 
	 * @param exampleSet
	 *            the dataset used to evaluate the stopping criteria
	 * @param attributesForSplit
	 *            collection of attributes to be used for tree building process.
	 * @param splitNode
	 *            boolean value that holds the evaluation results
	 * @return false, if the tree building process should end at this node
	 */
	private boolean checkStoppingCriteria(ExampleSet exampleSet,
			LinkedList<Attribute> attributesForSplit, boolean splitNode,
			Log parentLog) {
		// If the node is "pure"
		if ((Tools.getAllCategories(exampleSet, exampleSet.getAttributes()
				.getLabel()).size()) == 1)
			splitNode = false;

		parentLog.add(LogStoppingCriteria.logPureNode(splitNode));

		// No more attributes for splitting
		if (attributesForSplit.isEmpty())
			splitNode = false;

		parentLog.add(LogStoppingCriteria.logNoMoreAttributes(splitNode));

		// No more examples in the node
		if (exampleSet.size() == 0)
			splitNode = false;

		parentLog.add(LogStoppingCriteria.logNoMoreExamples(splitNode));

		// COMPONENT Stopping criteria
		if (componentsStop.size() > 0)
			for (StoppingCriteria crit : componentsStop) {
				boolean result = crit.evaluateStoppingCriteria(exampleSet,
						treeState.getTreeDepth(), treeState.getStartTime());
				if (result)
					splitNode = false;

				parentLog.add(LogStoppingCriteria.logEvaluation(crit,
						treeState, result, splitNode));
			}
		return splitNode;
	}

	/**
	 * Calls all of the PruneTree components selected by the user for execution.
	 * 
	 * @param root
	 *            of the whole tree to be pruned
	 */
	private Tree pruneTree(Tree root, ExampleSet exampleSet) {

		Tree tree = root;
		if (componentsPrune.size() > 0) {
			for (Prunning pruneTree : componentsPrune)
				tree = pruneTree.prune(root, exampleSet);
		}
		return tree;

	}

	// SOME WORK IN PROGRESS ON PREPARING THE WHIBO FOR INTEGRATION WITH OTHER
	// DATA MINING ENVIROMENTS
	// NOT YET FINISHED
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.whibo.integration.adapters.interfaces.IGDTAlgorithm#learnTree(
	 * rs.fon.whibo.integration.adapters.example.ExampleSetAdapter)
	 */
	public WhiboTree learnTree(ExampleSetAdapter exampleSet) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.whibo.integration.adapters.interfaces.IGDTAlgorithm#buildTree(
	 * rs.fon.whibo.integration.core.WhiboTree,
	 * rs.fon.whibo.integration.adapters.example.ExampleSetAdapter, int)
	 */
	public void buildTree(WhiboTree current, ExampleSetAdapter exampleSet,
			int depth) {

	}

}
