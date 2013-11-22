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

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.Model;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.performance.PerformanceVector;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.SubprocessTransformRule;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.tools.math.SignificanceTestResult;

/**
 * A ValidationChain for the 5x2cv F-test
 * 
 * @author Milos Jovanovic
 */

public abstract class Ftest5x2cvChain extends OperatorChain {

	protected InputPort exampleSetInput = getInputPorts().createPort(
			"example set in");
	private final OutputPort innerTrainSetSource = getSubprocess(0)
			.getInnerSources().createPort("train set source");
	private final InputPort innerModelSink = getSubprocess(0).getInnerSinks()
			.createPort("model sink", Model.class);
	private final OutputPort innerTrainSetSource1 = getSubprocess(1)
			.getInnerSources().createPort("train set source");
	private final InputPort innerModelSink1 = getSubprocess(1).getInnerSinks()
			.createPort("model sink 1", Model.class);
	private final OutputPort innerTestSetSource = getSubprocess(2)
			.getInnerSources().createPort("test set source");
	private final OutputPort innerModelSource = getSubprocess(2)
			.getInnerSources().createPort("model source");
	private final OutputPort innerModelSource1 = getSubprocess(2)
			.getInnerSources().createPort("model source 1");
	private final InputPort innerPerformanceSink = getSubprocess(2)
			.getInnerSinks().createPort("performance vector sink",
					PerformanceVector.class);
	protected OutputPort performanceOutput = getOutputPorts().createPort(
			"performance and significance");
	private final OutputPort exampleSetOutput = getOutputPorts().createPort(
			"exampleSet");

	public Ftest5x2cvChain(OperatorDescription description) {
		super(description, "Training 1", "Training 2", "Testing");
		getTransformer().addPassThroughRule(exampleSetInput,
				innerTrainSetSource);
		getTransformer().addRule(new SubprocessTransformRule(getSubprocess(0)));
		getTransformer().addPassThroughRule(exampleSetInput,
				innerTrainSetSource1);
		getTransformer().addRule(new SubprocessTransformRule(getSubprocess(1)));
		getTransformer()
				.addPassThroughRule(exampleSetInput, innerTestSetSource);
		getTransformer().addPassThroughRule(innerModelSink, innerModelSource);
		getTransformer().addRule(new SubprocessTransformRule(getSubprocess(2)));
		// getTransformer().addPassThroughRule(innerPerformanceSink,
		// performanceOutput);
	}

	public abstract SignificanceTestResult compareClassifiers(
			ExampleSet inputSet) throws OperatorException;

	public List<ParameterType> getParameterTypes() {
		List<ParameterType> types = super.getParameterTypes();
		return types;
	}

	/** Returns the maximum number of innner operators. */
	public int getMaxNumberOfInnerOperators() {
		return 3;
	}

	/** Returns the minimum number of innner operators. */
	public int getMinNumberOfInnerOperators() {
		return 3;
	}

	/** Returns the the classes this operator provides as input. */
	@SuppressWarnings("unchecked")
	public Class[] getInputClasses() {
		return new Class[] { ExampleSet.class };
	}

	/** Returns the the classes this operator expects as output. */
	@SuppressWarnings("unchecked")
	public Class[] getOutputClasses() {
		return new Class[] { SignificanceTestResult.class };
	}

	/** Applies the learner. */

	Model learn(int learnerNumber, ExampleSet trainingSet)
			throws OperatorException {
		// innerTrainSetSource.deliver(trainingSet);
		getSubprocess(learnerNumber - 1).execute();
		innerModelSource.deliver(innerModelSink.getData());
		return innerModelSink.getData();
	}

	Model learn1(int learnerNumber, ExampleSet trainingSet)
			throws OperatorException {
		// innerTrainSetSource1.deliver(trainingSet);
		getSubprocess(learnerNumber - 1).execute();
		innerModelSource1.deliver(innerModelSink1.getData());
		return innerModelSink1.getData();
	}

	protected PerformanceVector evaluate(Model m, ExampleSet testSet)
			throws OperatorException {

		// prepare input for evaluation
		// IOContainer evalInput = new IOContainer();
		// evalInput = evalInput.append(new IOObject[] {m});
		// evalInput = evalInput.append(new IOObject[] {testSet});
		innerTestSetSource.deliver(testSet);
		innerModelSource.deliver(m);
		executeEvaluator();

		// get results from Operator for evaluation
		// IOContainer result = getEvaluator().apply(evalInput);

		// return result
		// PerformanceVector pv =
		// (PerformanceVector)result.get(PerformanceVector.class);
		return innerPerformanceSink.getData();
		// return pv;
	}

	@Override
	public void doWork() throws OperatorException {
		// get input dataset
		ExampleSet eSet = exampleSetInput.getData();
		innerTrainSetSource.deliver(exampleSetInput.getData());
		innerTrainSetSource1.deliver(exampleSetInput.getData());
		// compare classifiers
		SignificanceTestResult comparation = compareClassifiers(eSet);

		// return results
		performanceOutput.deliver(comparation);
		exampleSetOutput.deliver(eSet);

		// IOObject[] results = new IOObject[1];
		// results[0] = comparation;
		// return results;

	}

	/**
	 * Returns the first subprocess (or operator chain), i.e. the learning
	 * operator (chain).
	 * 
	 * @throws OperatorException
	 */

	// verovatno ne treba
	protected void executeLearner() throws OperatorException {
		getSubprocess(0).execute();
	}

	protected void executeLearner1() throws OperatorException {
		getSubprocess(1).execute();
	}

	/**
	 * Returns the second encapsulated inner operator (or operator chain), i.e.
	 * the application and evaluation operator (chain)
	 * 
	 * @throws OperatorException
	 */
	protected void executeEvaluator() throws OperatorException {
		getSubprocess(2).execute();
	}

	// public PerformanceVector evaluate( Model learnResult, ExampleSet testSet)
	// throws OperatorException {
	// if (learnResult == null) {
	// throw new
	// RuntimeException("Wrong use of ValidationChain.evaluate(ExampleSet): " +
	// "No preceding invocation of learn(ExampleSet)!");
	// }
	//
	// Attribute predictedBefore = testSet.getAttributes().getPredictedLabel();
	// //IOContainer evalInput = learnResult.apply(testSet).append(new
	// IOObject[] { testSet });
	// //IOContainer result = getEvaluator().apply(evalInput);
	// innerTestSetSource.deliver(testSet);
	// executeEvaluator();
	//
	// Attribute predictedAfter = testSet.getAttributes().getPredictedLabel();
	//
	// // remove predicted label and confidence attributes if there is a new
	// prediction which is not equal to an old one
	// if ((predictedAfter != null) && ((predictedBefore == null) ||
	// (predictedBefore.getTableIndex() != predictedAfter.getTableIndex()))) {
	// PredictionModel.removePredictedLabel(testSet);
	// }
	// //return result;
	// //performanceOutput.deliver(innerPerformanceSink.getData());
	// return innerPerformanceSink.getData();
	//
	// }

	// public IOObject[] apply() throws OperatorException {
	// //get input dataset
	// ExampleSet eSet = getInput(ExampleSet.class);
	//
	// //compare classifiers
	// SignificanceTestResult comparation = compareClassifiers(eSet);
	//
	// //return results
	// IOObject[] results = new IOObject[1];
	// results[0] = comparation;
	// return results;
	// }

	// protected PerformanceVector evaluate(Model m, ExampleSet testSet) throws
	// OperatorException {
	//
	// //prepare input for evaluation
	// IOContainer evalInput = new IOContainer();
	// evalInput = evalInput.append(new IOObject[] {m});
	// evalInput = evalInput.append(new IOObject[] {testSet});
	//
	// //get results from Operator for evaluation
	// IOContainer result = getEvaluator().apply(evalInput);
	//
	// //return result
	// PerformanceVector pv =
	// (PerformanceVector)result.get(PerformanceVector.class);
	// return pv;
	// }

	// public InnerOperatorCondition getInnerOperatorCondition() {
	// CombinedInnerOperatorCondition condition = new
	// CombinedInnerOperatorCondition();
	// condition.addCondition(new SpecificInnerOperatorCondition("Training1", 0,
	// new Class[] { ExampleSet.class }, new Class[] { Model.class }));
	// condition.addCondition(new SpecificInnerOperatorCondition("Training2", 1,
	// new Class[] { ExampleSet.class }, new Class[] { Model.class }));
	// condition.addCondition(new SpecificInnerOperatorCondition("Testing", 2,
	// new Class[] { ExampleSet.class, Model.class }, new Class[] {
	// PerformanceVector.class }));
	// return condition;
	// }

	// protected Operator getLearner(int operatorNumber) {
	// return getOperator(operatorNumber-1);
	// }
	//
	// protected Operator getEvaluator() {
	// return getOperator(2);
	// }

	// protected Model learn(int learnerNumber, ExampleSet trainingSet) throws
	// OperatorException {
	// //learn model
	// IOContainer result = getLearner(learnerNumber).apply(new IOContainer(new
	// IOObject[] { trainingSet }));
	//
	// //return model
	// Model m = (Model)result.get(Model.class);
	// return m;
	// }

}
