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
package rs.fon.whibo.GC.component.Evaluation;

import java.util.List;

import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;
import rs.fon.whibo.problem.AbstractComponent;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.clustering.ClusterModel;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractEvaluation.
 */
public abstract class AbstractEvaluation extends AbstractComponent implements
		Evaluation {

	/** The parameters. */
	List<SubproblemParameter> parameters;

	/**
	 * Instantiates a new abstract initialization.
	 * 
	 * @param parameters
	 *            the parameters
	 */
	public AbstractEvaluation(List<SubproblemParameter> parameters) {
		this.parameters = parameters;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.whibo.GC.component.Evaluation.Evaluation#Evaluate(rs.fon.whibo
	 * .GC.component.DistanceMeasure.DistanceMeasure,
	 * rs.fon.whibo.GC.clusterModel.CentroidClusterModel,
	 * com.rapidminer.example.ExampleSet)
	 */
	// public double Evaluate(DistanceMeasure measure, WhiBoCentroidClusterModel
	// model, ExampleSet exampleSet)
	// {
	// return 0;
	// }
	//
	// public double Evaluate(DistanceMeasure measure, CentroidClusterModel
	// model, ExampleSet exampleSet)
	// {
	// return 0;
	// }

	public double Evaluate(DistanceMeasure measure, ClusterModel model,
			ExampleSet exampleSet) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.whibo.GC.component.Evaluation.Evaluation#isBetter(double,
	 * double)
	 */
	public boolean isBetter(double eval1, double eval2) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.whibo.GC.component.Evaluation.Evaluation#getWorstValue()
	 */
	public double getWorstValue() {
		return Double.MIN_VALUE;
	}

}