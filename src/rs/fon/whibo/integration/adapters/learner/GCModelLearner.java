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
package rs.fon.whibo.integration.adapters.learner;

import java.io.File;
import java.util.List;

import rs.fon.whibo.GC.algorithm.GCAlgorithm;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.problem.GenericClustererProblemBuilder;
import rs.fon.whibo.integration.adapters.parameter.ParameterTypeProblemFile;
import rs.fon.whibo.integration.adapters.parameter.ProblemFileValueEditor;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.serialization.ProblemDecoder;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.gui.properties.PropertyPanel;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.clustering.ClusterModel;
import com.rapidminer.operator.clustering.clusterer.AbstractClusterer;
import com.rapidminer.parameter.ParameterType;

public class GCModelLearner extends AbstractClusterer {

	protected static String GC_FILE = "Generic clusterer file";

	public GCModelLearner(OperatorDescription description) {
		super(description);

		// Integration with .parameter package
		// PropertyTable.registerPropertyValueCellEditor(ParameterTypeProblemFile.class,
		// ClusterProblemFileValueEditor.class);
		PropertyPanel.registerPropertyValueCellEditor(
				ParameterTypeProblemFile.class, ProblemFileValueEditor.class);
		// PropertyTable.registerPropertyValueCellEditor(ParameterTypeProblemFile.class,
		// ProblemFileValueEditor.class);
	}

	@Override
	public List<ParameterType> getParameterTypes() {

		List<ParameterType> types = super.getParameterTypes();

		ParameterType type = new ParameterTypeProblemFile(getProcessFile(),
				"Process file from which to load the generic clusterer", "wba",
				false, GenericClustererProblemBuilder.class);
		type.setExpert(false);
		types.add(type);
		return types;
	}

	@Override
	public void doWork() throws OperatorException {
		String fileLocation = getParameterAsString(getProcessFile());
		File file = new File(fileLocation);
		Problem process = ProblemDecoder.decodeFromXMLToProces(file
				.getAbsolutePath());
		WhiBoCentroidClusterModel model;
		ExampleSet exampleSet = getExampleSetInputPort().getData();
		// try {
		// com.rapidminer.example.Tools.checkAndCreateIds(exampleSet);
		// } catch (OperatorException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		try {
			GCAlgorithm builder = new GCAlgorithm(process);

			// learn tree
			model = builder.learnClusterModel(exampleSet);

			getOutputPorts().getPortByName("cluster model").deliver(model);
			getOutputPorts().getPortByName("clustered set").deliver(exampleSet);

			// return model;

		} catch (Exception e) {
			throw new OperatorException(e.getMessage(), e);
		}
	}

	public String getProcessFile() {
		return GC_FILE;
	}

	public void setProcessFile(String pf) {
		GC_FILE = pf;
	}

	public Class<?>[] getOutputClasses() {
		return new Class[] { WhiBoCentroidClusterModel.class };
	}

	// @Override
	// public IOObject[] apply() throws OperatorException {
	// ExampleSet exampleSet = getInput(ExampleSet.class);
	//
	// // some checks
	// // if (exampleSet.getAttributes().getLabel() == null) {
	// // throw new UserError(this, 105);
	// // }
	// if (exampleSet.getAttributes().size() == 0) {
	// throw new UserError(this, 106);
	// }
	// if (exampleSet.size() == 0) {
	// throw new UserError(this, 117);
	// }
	//
	// // check capabilities and produce errors if they are not fulfilled
	// // CapabilityCheck check = new CapabilityCheck(this,
	// Tools.booleanValue(System.getProperty(PROPERTY_RAPIDMINER_GENERAL_CAPABILITIES_WARN),
	// true) || onlyWarnForNonSufficientCapabilities());
	// // check.checkLearnerCapabilities(this, exampleSet);
	//
	// List<IOObject> results = new LinkedList<IOObject>();
	// Model model = learn(exampleSet);
	// results.add(model);
	//
	// // weights must be calculated _after_ learning
	// if (shouldCalculateWeights()) {
	// AttributeWeights weights = getWeights(exampleSet);
	// if (weights != null)
	// results.add(weights);
	// }
	//
	// PerformanceVector perfVector = null;
	// if (shouldEstimatePerformance()) {
	// perfVector = getEstimatedPerformance();
	// }
	//
	// if (shouldDeliverOptimizationPerformance()) {
	// PerformanceVector optimizationPerformance = getOptimizationPerformance();
	// if (optimizationPerformance != null) {
	// if (perfVector != null) {
	//
	// } else {
	// perfVector = optimizationPerformance;
	// }
	// }
	// }
	// if (perfVector != null)
	// results.add(perfVector);
	//
	// IOObject[] resultArray = new IOObject[results.size()];
	// results.toArray(resultArray);
	// return resultArray;
	// }

	// @Override
	// public boolean supportsCapability(OperatorCapability capability) {
	// if (capability ==
	// com.rapidminer.operator.OperatorCapability.BINOMINAL_ATTRIBUTES)
	// return true;
	// if (capability ==
	// com.rapidminer.operator.OperatorCapability.POLYNOMINAL_ATTRIBUTES)
	// return true;
	// if (capability ==
	// com.rapidminer.operator.OperatorCapability.NUMERICAL_ATTRIBUTES)
	// return true;
	// if (capability ==
	// com.rapidminer.operator.OperatorCapability.POLYNOMINAL_LABEL)
	// return true;
	// if (capability ==
	// com.rapidminer.operator.OperatorCapability.BINOMINAL_LABEL)
	// return true;
	// if (capability ==
	// com.rapidminer.operator.OperatorCapability.WEIGHTED_EXAMPLES)
	// return true;
	// if (capability ==
	// com.rapidminer.operator.OperatorCapability.NUMERICAL_LABEL)
	// return true;
	//
	// return false;
	// }

	// @Override
	// public Model learn(ExampleSet exampleSet) throws OperatorException {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public ClusterModel generateClusterModel(ExampleSet exampleSet)
			throws OperatorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean addsClusterAttribute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean addsIdAttribute() {
		// TODO Auto-generated method stub
		return false;
	}

}
