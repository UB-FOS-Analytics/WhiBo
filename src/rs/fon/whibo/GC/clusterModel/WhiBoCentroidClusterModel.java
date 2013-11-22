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
import java.util.LinkedList;
import java.util.List;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.clustering.ClusterModel;
import com.rapidminer.tools.Tools;

/**
 * This is the superclass for all centroid based cluster models and supports
 * assigning unseen examples to the nearest centroid.
 * 
 * @author Sebastian Land
 * @version $Id: CentroidClusterModel.java,v 1.2.2.2 2009/03/14 08:13:37
 *          ingomierswa Exp $
 */
public class WhiBoCentroidClusterModel extends ClusterModel {

	private static final long serialVersionUID = 3780908886210272852L;
	private Collection<String> dimensionNames;
	private ArrayList<WhiBoCentroid> centroids;

	public WhiBoCentroidClusterModel(ExampleSet exampleSet, int k,
			Attributes attributes) {
		super(exampleSet, k, true, true);
		List<String> dimensionNames = new LinkedList<String>();
		for (Attribute attribute : attributes)
			dimensionNames.add(attribute.getName());
		this.dimensionNames = dimensionNames;
		centroids = new ArrayList<WhiBoCentroid>(k);
		for (int i = 0; i < k; i++) {
			centroids.add(new WhiBoCentroid(dimensionNames.size()));
		}

	}

	public void setCentroid(int i, double[] coordinates) {
		centroids.get(i).setCentroid(coordinates);

	}

	// public int[] getClusterAssignments(ExampleSet exampleSet) {
	// int[] clusterAssignments = new int[exampleSet.size()];
	// Attribute[] attributes = new Attribute[dimensionNames.size()];
	// int i = 0;
	// for (String attributeName: dimensionNames) {
	// attributes[i] = exampleSet.getAttributes().get(attributeName);
	// i++;
	// }
	//
	// double[] exampleValues = new double[attributes.length];
	// int exampleIndex = 0;
	// for (Example example: exampleSet) {
	// // copying examplevalues into double array
	// for (i = 0; i < attributes.length; i++)
	// exampleValues[i] = example.getValue(attributes[i]);
	// // searching for nearest centroid
	// int centroidIndex = 0;
	// int bestIndex = 0;
	// double minimalDistance = Double.POSITIVE_INFINITY;
	// for (WhiBoCentroid centroid: centroids) {
	// double distance = distanceMeasure.calculateDistance(exampleValues,
	// centroid.getCentroid());
	// if (distance < minimalDistance) {
	// bestIndex = centroidIndex;
	// minimalDistance = distance;
	// }
	// centroidIndex++;
	// }
	// clusterAssignments[exampleIndex] = bestIndex;
	// exampleIndex++;
	// }
	// return clusterAssignments;
	// }

	/* This model does not need ids */
	public void checkCapabilities(ExampleSet exampleSet)
			throws OperatorException {

	}

	public void setCentroids(ArrayList<WhiBoCentroid> centroids) {
		this.centroids = centroids;

	}

	public String[] getAttributeNames() {
		return dimensionNames.toArray(new String[0]);
	}

	public double[] getCentroidCoordinates(int i) {
		return centroids.get(i).getCentroid();
	}

	public WhiBoCentroid getCentroid(int i) {
		return centroids.get(i);
	}

	// public void removeExamples()
	// {
	// for (int i=0; i<centroids.size();i++)
	// super.getCluster(i).
	// }

	public void assignExample(int i, double[] asDoubleArray) {
		centroids.get(i).assignExample(asDoubleArray);

	}

	public boolean finishAssign() {
		boolean stable = true;
		for (WhiBoCentroid centroid : centroids)
			stable &= centroid.finishAssign();
		return stable;
	}

	public String getExtension() {
		return "ccm";
	}

	public String getFileDescription() {
		return "Centroid based cluster model";
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		buffer.append(Tools.getLineSeparators(2));
		for (int i = 0; i < getNumberOfClusters(); i++) {
			buffer.append("Cluster " + i + Tools.getLineSeparator());
			buffer.append(getCentroid(i).toString(dimensionNames));
			buffer.append(Tools.getLineSeparator());
		}
		return buffer.toString();
	}

	@Override
	public boolean isUpdatable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setParameter(String key, Object value) throws OperatorException {
		throw new OperatorException("Method not implemented");
		// TODO Auto-generated method stub

	}

	@Override
	public void updateModel(ExampleSet updateExampleSet)
			throws OperatorException {
		throw new OperatorException("Method not implemented");
		// TODO Auto-generated method stub

	}

}
