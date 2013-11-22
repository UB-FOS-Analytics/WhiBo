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

import java.util.Date;

public class TreeAnalysisAverages extends TreeAnalysis {

	public int countOfAnalysis;

	public TreeAnalysisAverages() {
		this.runDescription = "Average";
		this.algorithmDescription = "";
		this.datasetDescription = "";
		this.maxTreeDepth = 0;
		this.numberOfLeaves = 0;
		this.numberOfNodes = 0;
		this.performance = 0;
		this.countOfAnalysis = 0;
		this.elapsedTime = null;
	}

	public void addAnalysis(TreeAnalysis analysis) {

		if (this.runDescription.isEmpty())
			this.runDescription = analysis.getRunDescription();
		if (this.algorithmDescription.isEmpty())
			this.algorithmDescription = analysis.getAlgorithmDescription();
		if (this.datasetDescription.isEmpty())
			this.datasetDescription = analysis.getDatasetDescription();

		this.maxTreeDepth += analysis.getMaxTreeDepth();
		this.weightedAverageTreeDepth += analysis.getWeightedAverageTreeDepth();
		this.numberOfLeaves += analysis.getNumberOfLeaves();
		this.numberOfNodes += analysis.getNumberOfNodes();
		this.performance += analysis.getPerfomance();
		this.countOfAnalysis += 1;

		if (elapsedTime != null)
			this.elapsedTime = new Date(elapsedTime.getTime()
					+ analysis.getElapsedTime().getTime());
		else
			this.elapsedTime = analysis.getElapsedTime();
	}

	public double getMaxTreeDepth() {
		return maxTreeDepth / countOfAnalysis;
	}

	public double getWeightedAverageTreeDepth() {
		return weightedAverageTreeDepth / countOfAnalysis;
	}

	public double getNumberOfNodes() {
		return numberOfNodes / countOfAnalysis;
	}

	public double getNumberOfLeaves() {
		return numberOfLeaves / countOfAnalysis;
	}

	public double getPerfomance() {
		return performance / countOfAnalysis;
	}

}
