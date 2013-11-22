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

import rs.fon.whibo.GDT.algorithm.GDTAlgorithm;
import rs.fon.whibo.GDT.problem.GenericTreeProblemBuilder;
import rs.fon.whibo.integration.adapters.parameter.ParameterTypeProblemFile;
import rs.fon.whibo.integration.adapters.parameter.ProblemFileValueEditor;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.serialization.ProblemDecoder;

import com.rapidminer.example.AbstractAttributes;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.gui.properties.PropertyPanel;
import com.rapidminer.operator.Model;
import com.rapidminer.operator.OperatorCapability;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.AbstractLearner;
import com.rapidminer.operator.learner.Learner;
import com.rapidminer.operator.learner.tree.Tree;
import com.rapidminer.operator.learner.tree.TreeModel;
import com.rapidminer.parameter.ParameterType;

//import com.rapidminer.operator.learner.LearnerCapability;

/**
 * 
 */
public class GDTModelLearner extends AbstractLearner implements Learner {

	protected static String GDT_FILE = "Generic decision tree file";

	public GDTModelLearner(OperatorDescription description) {
		super(description);

		// Integration with .parameter package
		PropertyPanel.registerPropertyValueCellEditor(
				ParameterTypeProblemFile.class, ProblemFileValueEditor.class);
		// PropertyTable.registerPropertyValueCellEditor(ParameterTypeProblemFile.class,
		// ProblemFileValueEditor.class);

	}

	@Override
	public List<ParameterType> getParameterTypes() {

		List<ParameterType> types = super.getParameterTypes();

		ParameterType type = new ParameterTypeProblemFile(getProcessFile(),
				"Problem file from which to load the generic decision tree",
				"wba", false, GenericTreeProblemBuilder.class);
		type.setExpert(false);
		types.add(type);
		return types;
	}

	@Override
	public void doWork() throws OperatorException {

		ExampleSet exampleSet = getExampleSetInputPort().getData();
		getOutputPorts().getPortByName("exampleSet").deliver(exampleSet);

		AbstractAttributes attributes = (AbstractAttributes) exampleSet
				.getAttributes();
		if (attributes.getId() != null) {
			attributes.remove(attributes.getId());
		}

		String fileLocation = getParameterAsString(getProcessFile());
		File file = new File(fileLocation);
		Problem process = ProblemDecoder.decodeFromXMLToProces(file
				.getAbsolutePath());
		TreeModel model;

		try {
			GDTAlgorithm builder = new GDTAlgorithm(process);

			// learn tree
			Tree root = builder.learnTree(exampleSet);

			// create and return model
			model = new TreeModel(exampleSet, root);

			getOutputPorts().getPortByName("model").deliver(model);
		} catch (Exception e) {
			throw new OperatorException(
					"Please select create split and evaluate split component");
		}
	}

	public String getProcessFile() {
		return GDT_FILE;
	}

	public void setProcessFile(String pf) {
		GDT_FILE = pf;
	}

	public Class<?>[] getOutputClasses() {
		return new Class[] { TreeModel.class };
	}

	@Override
	public boolean supportsCapability(OperatorCapability capability) {
		if (capability == com.rapidminer.operator.OperatorCapability.BINOMINAL_ATTRIBUTES)
			return true;
		if (capability == com.rapidminer.operator.OperatorCapability.POLYNOMINAL_ATTRIBUTES)
			return true;
		if (capability == com.rapidminer.operator.OperatorCapability.NUMERICAL_ATTRIBUTES)
			return true;
		if (capability == com.rapidminer.operator.OperatorCapability.POLYNOMINAL_LABEL)
			return true;
		if (capability == com.rapidminer.operator.OperatorCapability.BINOMINAL_LABEL)
			return true;
		if (capability == com.rapidminer.operator.OperatorCapability.WEIGHTED_EXAMPLES)
			return true;
		if (capability == com.rapidminer.operator.OperatorCapability.NUMERICAL_LABEL)
			return true;

		return false;
	}

	@Override
	public Model learn(ExampleSet exampleSet) throws OperatorException {

		return null;

	}
}
