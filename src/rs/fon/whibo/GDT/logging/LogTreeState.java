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

package rs.fon.whibo.GDT.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import rs.fon.whibo.GDT.algorithm.TreeState;

/**
 * Tree state log class
 * 
 * @author Nenad Zdravkovic
 * 
 */
public class LogTreeState extends Log {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3309452724510193213L;

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

	private LogTreeState() {
		super();
	}

	public static Log log(TreeState treeState) {
		LogTreeState log = new LogTreeState();
		log.treeDepth = treeState.getTreeDepth();
		log.numberOfNodes = treeState.getNumberOfNodes();
		log.numberOfLeaves = treeState.getNumberOfLeaves();
		log.startTime = treeState.getStartTime();
		log.endTime = treeState.getEndTime();
		log.maxTreeDepth = treeState.getTreeDepth();
		log.weightedAverageTreeDepth = treeState.getTreeDepth();
		log.df = new SimpleDateFormat("mm:ss");
		return log;
	}

	@Override
	protected String getOutput() {
		String output = getOutputHeader() + "Run number: " + runNumber
				+ DELIMITER + "Tree depth: " + treeDepth + DELIMITER
				+ "Max tree depth: " + maxTreeDepth + DELIMITER
				+ "Weighted averate tree depth: " + weightedAverageTreeDepth
				+ DELIMITER + "Number of nodes: " + numberOfNodes + DELIMITER
				+ "Number of leaves: " + numberOfLeaves + DELIMITER
				+ "Start time: " + getTimeString(startTime) + DELIMITER
				+ "End time: " + getTimeString(endTime) + DELIMITER;
		return output;
	}

	private String getTimeString(Date time) {
		if (time == null)
			return "";
		else
			return df.format(time);
	}
}
