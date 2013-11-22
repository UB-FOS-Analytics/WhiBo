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
package rs.fon.whibo.GDT.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.rapidminer.operator.learner.tree.Edge;
import com.rapidminer.operator.learner.tree.Tree;

public class TreeAnalysis {

	protected Tree tree;
	protected double numberOfNodes;
	protected double numberOfLeaves;
	protected double maxTreeDepth;
	protected double weightedAverageTreeDepth;
	protected double performance;
	protected String runDescription;
	protected String algorithmDescription;
	protected String datasetDescription;
	protected Date startTime;
	protected Date elapsedTime;

	public TreeAnalysis() {
		this.runDescription = "";
		this.algorithmDescription = "";
		this.datasetDescription = "";
		this.maxTreeDepth = 0;
		this.numberOfLeaves = 0;
		this.numberOfNodes = 0;
		this.performance = 0;
		this.tree = null;
		this.startTime = new Date();
		this.elapsedTime = new Date();
	}

	public void analyseTree(Tree tree, double performance) {
		this.tree = tree;
		this.performance = performance;

		long sizeOfDataset = tree.getFrequencySum();

		double weightedSum = calculateWeigtedAverageTreeDepth(tree, 1,
				sizeOfDataset);
		this.weightedAverageTreeDepth = weightedSum - 1;
	}

	private double calculateWeigtedAverageTreeDepth(Tree node, int depth,
			long sizeOfDataset) {

		// update properties
		if (depth > maxTreeDepth)
			maxTreeDepth = depth;
		numberOfNodes += 1;
		if (node.isLeaf())
			numberOfLeaves += 1;

		// do the recursion for calculating weighted average tree depth

		// trivial case
		if (node.isLeaf()) {
			long sizeOfLeaf = node.getFrequencySum();
			double weight = ((double) sizeOfLeaf) / sizeOfDataset;
			double leafWeightedDepth = weight * depth;
			return leafWeightedDepth;
		}
		// non-trivial case: calculate branches (recursion)
		else {
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

	public void setRunDescription(String runDescription) {
		this.runDescription = runDescription;
	}

	public void setAlgorithmDescription(String algorithmDescription) {
		this.algorithmDescription = algorithmDescription;
	}

	public void setDatasetDescription(String datasetDescription) {
		this.datasetDescription = datasetDescription;
	}

	public void measureTime() {
		long start = startTime.getTime();
		long current = System.currentTimeMillis();
		long elapsed = current - start;

		elapsedTime = new Date(elapsed);
	}

	// ----------------- GETTING STATE INFORMATION

	public Date getElapsedTime() {
		return elapsedTime;
	}

	public String getRunDescription() {
		return runDescription;
	}

	public String getAlgorithmDescription() {
		return algorithmDescription;
	}

	public String getDatasetDescription() {
		return datasetDescription;
	}

	public double getMaxTreeDepth() {
		return maxTreeDepth;
	}

	public double getWeightedAverageTreeDepth() {
		return weightedAverageTreeDepth;
	}

	public double getNumberOfNodes() {
		return numberOfNodes;
	}

	public double getNumberOfLeaves() {
		return numberOfLeaves;
	}

	public double getPerfomance() {
		return performance;
	}

	public String getValues() {
		String perf = Double.toString(getPerfomance());
		String maxDepth = Double.toString(getMaxTreeDepth());
		String wAvgTreeDepth = Double.toString(getWeightedAverageTreeDepth());
		String nodes = Double.toString(getNumberOfNodes());
		String leaves = Double.toString(getNumberOfLeaves());

		DateFormat df = new SimpleDateFormat("mm:ss.S");
		String time = df.format(getElapsedTime());

		return algorithmDescription + "," + datasetDescription + ","
				+ runDescription + "," + perf + "," + maxDepth + ","
				+ wAvgTreeDepth + "," + nodes + "," + leaves + "," + time;
	}

	public static String getValuesDescription() {
		return "AlgorithmDescription, DatasetDescription, RunDescription, Perfomance, MaxTreeDepth, WAvgTreeDepth, TotalNodes, TotalLeaves, ElapsedTime";
	}

}
