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

import java.util.List;

import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;
import rs.fon.whibo.problem.Parameter;
import MathlabGadget.Transformer;

import com.rapidminer.example.ExampleSet;

//import koancniProjekat.*;

//import com.mathworks.toolbox.javabuilder.*;

/**
 * The Class KMeansPlusPlus initializes centroid cluster model like in Kmeans ++
 * algorithm.
 * 
 * First centroid is randomly selected and other (K-1) centroids are initialized
 * as follows:. Distances from every centroid to every example is calculated and
 * an example with farthest distance from any centroid becomes new centroid.
 * 
 * David Arthur , Sergei Vassilvitskii, k-means++: the advantages of careful
 * seeding, Proceedings of the eighteenth annual ACM-SIAM symposium on Discrete
 * algorithms, p.1027-1035, January 07-09, 2007, New Orleans, Louisiana
 */
public class GMeans extends AbstractInitialization {

	/** Parameter No_Of_Restarts defines number of restarts of the algorithm. */
	@Parameter(defaultValue = "5", minValue = "1", maxValue = "1000")
	private int No_Of_Restarts;

	/** Cluster model that will be initialized with K centroids. */
	WhiBoCentroidClusterModel initCluster;

	/**
	 * Instantiates a significant categorical component for candidate split
	 * creation.
	 * 
	 * @param parameters
	 *            - this method has one parameter: Parameter_K.
	 */
	public GMeans(List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
		initCluster = null;
		No_Of_Restarts = Integer.parseInt(parameters.get(0).getXenteredValue());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.whibo.GC.component.Initialization.Initialization#InitializeCentroids
	 * (com.rapidminer.example.ExampleSet,
	 * rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure)
	 */
	public WhiBoCentroidClusterModel InitializeCentroids(ExampleSet exampleSet,
			DistanceMeasure measure) {
		Transformer t = new Transformer();
		double example[][] = t.prepakujExample(exampleSet);

		Object[] in = new Object[3];
		in[0] = example;
		in[1] = "euc";
		in[2] = "inline";

		Object[] out = new Object[1];
		WhiBoCentroidClusterModel wccm = null;
		try {
			// izvrsna iz=new izvrsna();

			// out=iz.diana1(1, in);

			wccm = t.prepakujCentre(exampleSet, (double[][]) out[0]);
		} catch (Exception ex) {

		}

		return wccm;
	}

	public int getNumberOfRestarts() {
		return No_Of_Restarts;
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