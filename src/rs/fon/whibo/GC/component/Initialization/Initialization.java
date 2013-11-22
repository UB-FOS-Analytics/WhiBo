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
package rs.fon.whibo.GC.component.Initialization;

import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;

import com.rapidminer.example.ExampleSet;

/**
 * 
 * Interface for solving initialization sub-problem in generic clusterer
 * algorithm
 * 
 * @author Milan Vukicevic
 * 
 */
public interface Initialization {

	/**
	 * Inits the initialization component. It generates centroid cluster model
	 * with initial centroids
	 * 
	 * @param exampleSet
	 *            - example set for clustering
	 * @param DistanceMeasure
	 *            - distance measure that is used by initialization component
	 */

	public WhiBoCentroidClusterModel InitializeCentroids(ExampleSet exampleSet,
			DistanceMeasure measure);

	public int getNumberOfRestarts();
}