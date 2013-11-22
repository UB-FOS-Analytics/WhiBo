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

import java.util.Collection;

import com.rapidminer.tools.Tools;

/**
 * This class represents a single centroid used for centroid based clustering.
 * It also provides methods for centroid calculation of a number of examples.
 * 
 * @author Sebastian Land
 * @version $Id: Centroid.java,v 1.2.2.1 2009/03/14 08:13:37 ingomierswa Exp $
 */
public class WhiBoCentroid {

	private double[] centroid;

	private double[] centroidSum;
	private int numberOfAssigned = 0;

	public WhiBoCentroid(int numberOfDimensions) {
		// super(numberOfDimensions);
		centroid = new double[numberOfDimensions];
		centroidSum = new double[numberOfDimensions];
	}

	public double[] getCentroid() {
		return centroid;
	}

	public void setCentroid(double[] coordinates) {
		this.centroid = coordinates;
	}

	public void setNumberOfAssigned(int numberOfAssigned) {
		this.numberOfAssigned = numberOfAssigned;
	}

	public void assignExample(double[] exampleValues) {
		numberOfAssigned++;
		for (int i = 0; i < exampleValues.length; i++) {
			centroidSum[i] += exampleValues[i];
		}
	}

	public void restartCentroidSum() {
		this.centroidSum = new double[centroidSum.length];
	}

	public void setCentroidSum(double[] centroidSum) {
		this.centroidSum = centroidSum;

	}

	public boolean finishAssign() {
		double[] newCentroid = new double[centroid.length];
		boolean stable = true;
		for (int i = 0; i < centroid.length; i++) {
			newCentroid[i] = centroidSum[i] / numberOfAssigned;
			stable &= Double.compare(newCentroid[i], centroid[i]) == 0;
		}
		centroid = newCentroid;
		centroidSum = new double[centroidSum.length];
		numberOfAssigned = 0;
		return stable;
	}

	public double[] getCentroidSum() {
		return this.centroidSum;
	}

	public int getNumberOfAssigned() {
		return this.numberOfAssigned;
	}

	public String toString(Collection<String> dimensionNames) {
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		for (String dimName : dimensionNames) {
			buffer.append(dimName + ":\t");
			buffer.append(Tools.formatNumber(centroid[i])
					+ Tools.getLineSeparator());
			i++;
		}
		return buffer.toString();
	}

}