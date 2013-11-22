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
package rs.fon.whibo.GC.clusterModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents an individual cluster, storing all examples by remembering their
 * ids
 * 
 * @author Sebastian Land
 * @version $Id: Cluster.java,v 1.2.2.1 2009/03/14 08:13:37 ingomierswa Exp $
 */
public class WhiBoCluster {

	private static final long serialVersionUID = -7437913251661093493L;
	private ArrayList<Object> exampleIds;
	private int clusterId;

	public WhiBoCluster(int clusterId) {

		this.clusterId = clusterId;
		this.exampleIds = new ArrayList<Object>();
	}

	/**
	 * Get all ids of the examples associated with this cluster.
	 * 
	 * @return Iterator of String
	 */
	public Collection<Object> getExampleIds() {
		return exampleIds;
	}

	public boolean containsExampleId(Object id) {
		return getExampleIds().contains(id);
	}

	/**
	 * Get the id of the cluster.
	 * 
	 * @return Object
	 */
	public int getClusterId() {
		return clusterId;
	}

	/**
	 * Returns the number of examples in this cluster
	 * 
	 * @return number of examples
	 */
	public int getNumberOfExamples() {
		return exampleIds.size();
	}

	public void assignExample(Object exampleId) {
		exampleIds.add(exampleId);
	}

	// removes examples because of more iterations
	public void removeExamples() {
		getExampleIds().clear();
	}

	// public String toString() {
	// return "cluster_" + clusterId;
	// }
}
