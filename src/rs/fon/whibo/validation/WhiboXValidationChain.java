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
package rs.fon.whibo.validation;

import java.util.List;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.ExecutionUnit;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.InputDescription;
import com.rapidminer.operator.Model;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.SimpleResultObject;
import com.rapidminer.operator.learner.PredictionModel;
import com.rapidminer.operator.performance.PerformanceVector;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;
import com.rapidminer.operator.ports.metadata.SubprocessTransformRule;
import com.rapidminer.parameter.ParameterType;

public abstract class WhiboXValidationChain extends OperatorChain {

	// private IOContainer learnResult;
	private Model learnResult;

	protected InputPort exampleSetInput = getInputPorts().createPort(
			"example set in");
	private final OutputPort innerTrainSetSource = getSubprocess(0)
			.getInnerSources().createPort("train set source");
	private final InputPort innerModelSink = getSubprocess(0).getInnerSinks()
			.createPort("model sink", Model.class);
	private final OutputPort innerTestSetSource = getSubprocess(1)
			.getInnerSources().createPort("test set source");
	private final OutputPort innerModelSource = getSubprocess(1)
			.getInnerSources().createPort("model source");
	private final InputPort innerPerformanceSink = getSubprocess(1)
			.getInnerSinks().createPort("performance vector sink",
					PerformanceVector.class);
	protected OutputPort performanceOutput = getOutputPorts().createPort(
			"performance and significance");
	private final OutputPort modelOutput = getOutputPorts().createPort("model");
	private final OutputPort exampleSetOutput = getOutputPorts().createPort(
			"exampleSet");

	public WhiboXValidationChain(OperatorDescription description) {
		super(description, "Training", "Testing");
		getTransformer().addPassThroughRule(exampleSetInput,
				innerTrainSetSource);
		getTransformer().addRule(new SubprocessTransformRule(getSubprocess(0)));
		getTransformer()
				.addPassThroughRule(exampleSetInput, innerTestSetSource);
		getTransformer().addPassThroughRule(innerModelSink, innerModelSource);
		getTransformer().addRule(new SubprocessTransformRule(getSubprocess(1)));
		getTransformer().addPassThroughRule(innerPerformanceSink,
				performanceOutput);
		getTransformer().addRule(
				new PassThroughRule(innerModelSink, modelOutput, false));

	}

	/** Returns the maximum number of innner operators. */
	public int getMaxNumberOfInnerOperators() {
		return 3;
	}

	/** Returns the minimum number of innner operators. */
	public int getMinNumberOfInnerOperators() {
		return 3;
	}

	public InputDescription getInputDescription(Class cls) {
		if (ExampleSet.class.isAssignableFrom(cls)) {
			return new InputDescription(cls, false, true);
		} else {
			return super.getInputDescription(cls);
		}
	}

	/** Returns the the classes this operator provides as output. */
	public Class<?>[] getInputClasses() {
		return new Class[] { ExampleSet.class };
	}

	/** Returns the the classes this operator expects as input. */
	public Class<?>[] getOutputClasses() {
		return new Class[] {};
	}

	/**
	 * Returns the first encapsulated inner operator (or operator chain), i.e.
	 * the learning operator (chain).
	 */
	protected ExecutionUnit getLearner() {
		return getSubprocess(0);
	}

	@Override
	public void doWork() throws OperatorException {
		// ExampleSet eSet = getInput(ExampleSet.class);
		ExampleSet eSet = exampleSetInput.getData();
		estimatePerformance(eSet);

		// Generate complete model, if desired
		if (modelOutput.isConnected()) {
			learn(eSet);
			modelOutput.deliver(innerModelSink.getData());
		}

		IOObject result = new SimpleResultObject("Results",
				"Results are written to log files");
		exampleSetOutput.deliver(eSet);
	}

	/**
	 * Applies the applier and evaluator (= second encapsulated inner operator).
	 * In order to reuse possibly created predicted label attributes, we do the
	 * following: We compare the predicted label of <code>testSet</code> before
	 * and after applying the inner operator. If it changed, the predicted label
	 * is removed again. No outer operator could ever see it. The same applies
	 * for the confidence attributes in case of classification learning.
	 */
	public PerformanceVector evaluate(ExampleSet testSet, Model learnResult)
			throws OperatorException {
		if (learnResult == null) {
			throw new RuntimeException(
					"Wrong use of ValidationChain.evaluate(ExampleSet): "
							+ "No preceding invocation of learn(ExampleSet)!");
		}

		Attribute predictedBefore = testSet.getAttributes().getPredictedLabel();
		innerTestSetSource.deliver(testSet);
		executeEvaluator();

		Attribute predictedAfter = testSet.getAttributes().getPredictedLabel();

		// remove predicted label and confidence attributes if there is a new
		// prediction which is not equal to an old one
		if ((predictedAfter != null)
				&& ((predictedBefore == null) || (predictedBefore
						.getTableIndex() != predictedAfter.getTableIndex()))) {
			PredictionModel.removePredictedLabel(testSet);
		}
		// return result;
		performanceOutput.deliver(innerPerformanceSink.getData());
		return innerPerformanceSink.getData();

	}

	/**
	 * Applies the applier and evaluator (= second encapsulated inner operator).
	 * In order to reuse possibly created predicted label attributes, we do the
	 * following: We compare the predicted label of <code>testSet</code> before
	 * and after applying the inner operator. If it changed, the predicted label
	 * is removed again. No outer operator could ever see it. The same applies
	 * for the confidence attributes in case of classification learning.
	 */
	protected PerformanceVector evaluate(ExampleSet testSet)
			throws OperatorException {
		learnResult = innerModelSource.getData();
		if (learnResult == null) {
			throw new RuntimeException(
					"Wrong use of ValidationChain.evaluate(ExampleSet): "
							+ "No preceding invocation of learn(ExampleSet)!");
		}
		PerformanceVector result = evaluate(testSet, learnResult);
		learnResult = null;
		return result;
	}

	public List<ParameterType> getParameterTypes() {
		List<ParameterType> types = super.getParameterTypes();
		return types;
	}

	/** Applies the learner. */

	Model learn(ExampleSet trainingSet) throws OperatorException {
		innerTrainSetSource.deliver(trainingSet);
		getSubprocess(0).execute();
		innerModelSource.deliver(innerModelSink.getData());
		return innerModelSink.getData();
	}

	/**
	 * This is the main method of the validation chain and must be implemented
	 * to estimate a performance of inner operators on the given example set.
	 * The implementation can make use of the provided helper methods in this
	 * class.
	 */
	public abstract void estimatePerformance(ExampleSet inputSet)
			throws OperatorException;

	/**
	 * Returns the first subprocess (or operator chain), i.e. the learning
	 * operator (chain).
	 * 
	 * @throws OperatorException
	 */
	protected void executeLearner() throws OperatorException {
		getSubprocess(0).execute();
	}

	/**
	 * Returns the second encapsulated inner operator (or operator chain), i.e.
	 * the application and evaluation operator (chain)
	 * 
	 * @throws OperatorException
	 */
	protected void executeEvaluator() throws OperatorException {
		getSubprocess(1).execute();
	}

}
