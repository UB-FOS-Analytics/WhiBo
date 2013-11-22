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
package rs.fon.whibo.GC.algorithm;

import java.util.Date;

import rs.fon.whibo.GC.Tools.CalculateStatistics;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;

import com.rapidminer.example.ExampleSet;

public class ClusterState {

	private int optimizationStepNumber;
	private int runNumber;
	private double intraClusterDistance;
	/** Time when the algorithm was run. */
	private Date startTime;

	/** Time when the algorithm finished. */
	private Date endTime;

	public ClusterState() {
		this.optimizationStepNumber = 1;
		this.runNumber = 1;
		this.startTime = new Date();
		this.endTime = null;
	}

	public void nextRun() {
		runNumber++;
	}

	public void restartOptimizationStepNumber() {
		optimizationStepNumber = 0;
	}

	public void nextOptimizationStep() {
		optimizationStepNumber++;
	}

	public void algorithmEnd() {
		this.endTime = new Date();
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

	public double getIntraClusterDistance() {
		return intraClusterDistance;
	}

	// Implementirati
	public void setIntraClusterDistance(DistanceMeasure measure,
			WhiBoCentroidClusterModel model, ExampleSet exampleSet) {

		CalculateStatistics cc = new CalculateStatistics();
		cc.getIntraClusterDistance(measure, model, exampleSet);
		intraClusterDistance = 0;
	}

	/**
	 * Gets the tree depth.
	 * 
	 * @return the tree depth
	 */
	public int getOptimizationStepNumber() {
		return optimizationStepNumber;
	}

	/**
	 * Gets the max tree depth.
	 * 
	 * @return the max tree depth
	 */
	public int getRunNumber() {
		return runNumber;
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

}
