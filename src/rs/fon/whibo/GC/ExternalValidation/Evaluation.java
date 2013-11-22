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
package rs.fon.whibo.GC.ExternalValidation;

import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.clustering.CentroidClusterModel;

// TODO: Auto-generated Javadoc
/**
 * The Interface Evaluation.
 */
public interface Evaluation {

	/**
	 * Evaluates cluster model.
	 * 
	 * @param measure
	 *            the measure
	 * @param model
	 *            the model
	 * @param exampleSet
	 *            the example set
	 * 
	 * @return double, cluster model evaluation value
	 */
	/*
	 * public double Evaluate(DistanceMeasure measure, WhiBoCentroidClusterModel
	 * model, ExampleSet exampleSet);
	 * 
	 * // nesto od ova dva ne treba public double Evaluate(DistanceMeasure
	 * measure, CentroidClusterModel model, ExampleSet exampleSet);
	 */
	public double Evaluate(DistanceMeasure measure, CentroidClusterModel model,
			ExampleSet exampleSet);

	public double Evaluate(DistanceMeasure measure,
			WhiBoCentroidClusterModel model, ExampleSet exampleSet);

	/**
	 * Checks if is better.
	 * 
	 * @param eval1
	 *            the eval1
	 * @param eval2
	 *            the eval2
	 * 
	 * @return true, if is better compares eval1 and eval2 and define which is
	 *         better
	 */
	public boolean isBetter(double eval1, double eval2);

	/**
	 * Gets the worst value. return the worst possible value for concrete
	 * evaluation component
	 * 
	 * @return the worst value
	 */
	public double getWorstValue();

}
