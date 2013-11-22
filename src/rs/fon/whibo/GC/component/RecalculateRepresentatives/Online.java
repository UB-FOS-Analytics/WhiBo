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
package rs.fon.whibo.GC.component.RecalculateRepresentatives;

import java.util.ArrayList;
import java.util.List;

import rs.fon.whibo.GC.clusterModel.WhiBoCentroid;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;

import com.rapidminer.example.ExampleSet;

//import com.rapidminer.operator.clustering.Centroid;

/**
 * The Class Online recalculates representatives by getting closer (by a step)
 * to each data point assigned to current cluster.
 * 
 * Also reffered in the literature as the "Stohastic" algorithm.
 */
public class Online extends AbstractRecalculateRepresentatives {

	/** The init cluster. */
	WhiBoCentroidClusterModel initCluster;

	public Online(List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.whibo.GC.component.RecalculateRepresentatives.
	 * AbstractRecalculateRepresentatives
	 * #Recalculate(rs.fon.whibo.GC.clusterModel.CentroidClusterModel,
	 * com.rapidminer.example.ExampleSet,
	 * rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure)
	 */
	public boolean Recalculate(WhiBoCentroidClusterModel model,
			ExampleSet exampleSet, DistanceMeasure distanceMeasure) {

		int n = model.getNumberOfClusters();
		int numOfDimensions = exampleSet.getAttributes().size();
		ArrayList<WhiBoCentroid> oldCentroids = new ArrayList<WhiBoCentroid>();
		ArrayList<WhiBoCentroid> newCentroids = new ArrayList<WhiBoCentroid>();
		boolean stable = false;

		return stable;

	}

	/**
	 * Gets collection of class names of components that are not compatible with
	 * this component
	 * 
	 * @return collection of class names of not compatible components
	 */
	public String[] getNotCompatibleClassNames() {
		return null;
	}

	/**
	 * Gets collection of class names of components which are exclusive to this
	 * component
	 * 
	 * @return collection of class names of exclusive components
	 */
	public String[] getExclusiveClassNames() {
		return null;
	}

}
