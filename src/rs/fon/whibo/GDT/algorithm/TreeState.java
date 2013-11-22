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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.rapidminer.operator.learner.tree.Edge;
import com.rapidminer.operator.learner.tree.Tree;

/**
 * The Class TreeState is used to capture and hold the state of the tree as it
 * grows within the algorithm. It provides some basic metrics (numeric
 * description) of the current tree that is being built. State of the tree is
 * set using methods that represent events, and state information is extracted
 * using getter methods.
 */
public class TreeState {

	/** Current tree depth. */
	private int treeDepth;

	/** Total number of nodes. */
	private int numberOfNodes;

	/** Total number of leaves. */
	private int numberOfLeaves;

	/** Time when the algorithm was run. */
	private Date startTime;

	/** Time when the algorithm finished. */
	private Date endTime;

	/** Maximal tree depth within the current tree. */
	private int maxTreeDepth;

	/** Weighted average tree depth of the whole tree. */
	private double weightedAverageTreeDepth;

	/** Date formatting object. */
	private DateFormat df;

	/**
	 * The run number of the algorithm. Used when the algorithm is started many
	 * times.
	 */
	private static int runNumber = 0;

	// ----------------- SETTING STATE INFORMATION (Events that reflect to
	// TreeState)

	/**
	 * Instantiates a new tree state.
	 */
	public TreeState() {
		this.treeDepth = 1;
		this.maxTreeDepth = 1;
		this.weightedAverageTreeDepth = 0;
		this.numberOfNodes = 1;
		this.numberOfLeaves = 0;
		this.startTime = new Date();
		this.endTime = null;

		this.df = new SimpleDateFormat("mm:ss");
		TreeState.runNumber += 1;
	}

	/**
	 * Enter branch event. It changes the state of the tree caused when the
	 * algorithm enters into a branch to grow the subtree.
	 */
	public void enterBranch() {
		this.treeDepth += 1;
		this.numberOfNodes += 1;

		if (treeDepth > maxTreeDepth)
			this.maxTreeDepth = treeDepth;
	}

	/**
	 * Exit branch event. It changes the state of the tree caused when the
	 * algorithm returns from a branch to a parent node to continue with the
	 * tree building process.
	 * 
	 * @param tree
	 *            Current tree
	 */
	public void exitBranch(Tree tree) {
		this.treeDepth -= 1;
		if (tree.isLeaf())
			this.numberOfLeaves += 1;
	}

	/**
	 * Algorithm end.
	 */
	public void algorithmEnd() {
		this.endTime = new Date();
	}

	/**
	 * Analyze final tree. Calculates averages and sums for some metrics. Should
	 * be called when tree building process completes.
	 * 
	 * @param tree
	 *            the final tree
	 */
	public void analyseFinalTree(Tree tree) {
		long sizeOfDataset = tree.getTrainingSet().size();

		double weightedSum = calculateWeigtedAverageTreeDepth(tree, 1,
				sizeOfDataset);
		this.weightedAverageTreeDepth = weightedSum - 1;
	}

	/**
	 * Calculate weigted average tree depth.
	 * 
	 * @param node
	 *            the node
	 * @param depth
	 *            the depth
	 * @param sizeOfDataset
	 *            the size of dataset
	 * 
	 * @return the double
	 */
	private double calculateWeigtedAverageTreeDepth(Tree node, int depth,
			long sizeOfDataset) {

		if (node.isLeaf()) {
			long sizeOfLeaf = node.getFrequencySum();
			double weight = ((double) sizeOfLeaf) / sizeOfDataset;
			double leafWeightedDepth = weight * depth;
			return leafWeightedDepth;
		} else {
			double sumOfWeightedDepth = 0;

			Iterator<Edge> i = node.childIterator();
			while (i.hasNext()) {
				Tree nextNode = i.next().getChild();
				sumOfWeightedDepth += calculateWeigtedAverageTreeDepth(
						nextNode, depth + 1, sizeOfDataset);
			}

			return sumOfWeightedDepth;

		}
	}

	// ----------------- GETTING STATE INFORMATION

	/**
	 * Gets the start time.
	 * 
	 * @return the start time
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Gets the end time.
	 * 
	 * @return the end time
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Gets the tree depth.
	 * 
	 * @return the tree depth
	 */
	public int getTreeDepth() {
		return treeDepth;
	}

	/**
	 * Gets the max tree depth.
	 * 
	 * @return the max tree depth
	 */
	public int getMaxTreeDepth() {
		return maxTreeDepth;
	}

	/**
	 * Gets the weighted average tree depth.
	 * 
	 * @return the weighted average tree depth
	 */
	public double getWeightedAverageTreeDepth() {
		return weightedAverageTreeDepth;
	}

	/**
	 * Gets the number of nodes.
	 * 
	 * @return the number of nodes
	 */
	public int getNumberOfNodes() {
		return numberOfNodes;
	}

	/**
	 * Gets the number of leaves.
	 * 
	 * @return the number of leaves
	 */
	public int getNumberOfLeaves() {
		return numberOfLeaves;
	}

	/**
	 * Gets the elapsed time.
	 * 
	 * @return the elapsed time
	 */
	public Date getElapsedTime() {

		long startTime = System.currentTimeMillis();
		long currentTime = System.currentTimeMillis();
		long elapsed = currentTime - startTime;

		return new Date(elapsed);
	}

	/**
	 * Gets the memory usage by the java virtual machine (to compare the memory
	 * usage of an algorithm).
	 * 
	 * @return the memory
	 */
	public long getMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	/**
	 * Gets the values formated as a string, separated by commas.
	 * 
	 * @return the values in csv format
	 */
	public String getValues() {
		String rn = Integer.toString(TreeState.runNumber);
		String time = df.format(getElapsedTime());
		String depth = Integer.toString(getTreeDepth());
		String maxDepth = Integer.toString(getMaxTreeDepth());
		String wAvgTreeDepth = Double.toString(getWeightedAverageTreeDepth());
		String nodes = Integer.toString(getNumberOfNodes());
		String leaves = Integer.toString(getNumberOfLeaves());

		return rn + "," + time + "," + depth + "," + maxDepth + ","
				+ wAvgTreeDepth + "," + nodes + "," + leaves;
	}

	/**
	 * Gets the values description as a string, separated by commas. This
	 * actually represent the header row for the getValues() method.
	 * 
	 * @return the values description in csv format
	 */
	public static String getValuesDescription() {
		return "Run#, ElapsedTime, TreeDepth, MaxTreeDepth, WAvgTreeDepth, TotalNodes, TotalLeaves";
	}

}
