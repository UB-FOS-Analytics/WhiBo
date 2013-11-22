package rs.fon.whibo.GDT.algorithm.blackbox;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import rs.fon.whibo.GDT.algorithm.GDTAlgorithm;
import rs.fon.whibo.problem.Problem;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.Model;
import com.rapidminer.operator.OperatorCapability;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.AbstractLearner;
import com.rapidminer.operator.learner.Learner;
import com.rapidminer.operator.learner.tree.Tree;
import com.rapidminer.operator.learner.tree.TreeModel;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeDouble;
import com.rapidminer.parameter.ParameterTypeInt;

public class CHAID extends AbstractLearner implements Learner {

	protected static String TREE_DEPTH = "Maximal tree depth";
	protected static String MIN_NODE_SIZE = "Minimal size of each node";
	protected static String LEAF_LABEL_CONFIDENCE = "Minimal confidence of each decision label in a leaf";
	protected static String MERGE_CONF = "Confidence for merging values of an attribute in each node";
	protected static String SPLIT_CONF = "Confidence for splitting values of an attribute in each node";

	public CHAID(OperatorDescription description) {
		super(description);
	}

	@Override
	public List<ParameterType> getParameterTypes() {

		List<ParameterType> types = super.getParameterTypes();

		ParameterType type = new ParameterTypeInt(TREE_DEPTH,
				"Maximal tree depth", 1, 100, 10);
		type.setExpert(false);
		types.add(type);

		type = new ParameterTypeInt(MIN_NODE_SIZE, "Minimal size of each node",
				1, 1000, 30);
		type.setExpert(false);
		types.add(type);

		type = new ParameterTypeDouble(LEAF_LABEL_CONFIDENCE,
				"Minimal confidence of each decision label in a leaf", 0, 1,
				0.95);
		type.setExpert(false);
		types.add(type);

		type = new ParameterTypeDouble(MERGE_CONF,
				"Confidence for merging values of an attribute in each node",
				0, 1, 0.05);
		type.setExpert(false);
		types.add(type);

		type = new ParameterTypeDouble(SPLIT_CONF,
				"Confidence for splitting values of an attribute in each node",
				0, 1, 0.049);
		type.setExpert(false);
		types.add(type);

		return types;
	}

	private Problem deSerializeProblem() {
		Problem result = null;
		try {
			// Serialize to a file
			java.io.ObjectInput in = new ObjectInputStream(new FileInputStream(
					"C:\\HCI\\Plugin\\CHAID.dat"));
			result = (Problem) in.readObject();
			in.close();
		} catch (Exception e) {
		}
		return result;
	}

	@Override
	public void doWork() throws OperatorException {

		ExampleSet exampleSet = getExampleSetInputPort().getData();
		Problem problem = deSerializeProblem();

		int treeDepth = getParameterAsInt(TREE_DEPTH);
		int minNodeSize = getParameterAsInt(MIN_NODE_SIZE);
		double leafConf = getParameterAsDouble(LEAF_LABEL_CONFIDENCE);
		double merge = getParameterAsDouble(MERGE_CONF);
		double split = getParameterAsDouble(SPLIT_CONF);

		problem.getSubproblems().get(2).getMultipleStepData().get(2)
				.getListOfParameters().get(0)
				.setXenteredValue(String.valueOf(treeDepth));
		problem.getSubproblems().get(2).getMultipleStepData().get(1)
				.getListOfParameters().get(0)
				.setXenteredValue(String.valueOf(minNodeSize));
		problem.getSubproblems().get(2).getMultipleStepData().get(0)
				.getListOfParameters().get(0)
				.setXenteredValue(String.valueOf(leafConf));
		problem.getSubproblems().get(0).getMultipleStepData().get(1)
				.getListOfParameters().get(0)
				.setXenteredValue(String.valueOf(merge));
		problem.getSubproblems().get(0).getMultipleStepData().get(1)
				.getListOfParameters().get(1)
				.setXenteredValue(String.valueOf(split));

		TreeModel model;

		try {
			GDTAlgorithm builder = new GDTAlgorithm(problem);

			// learn tree
			Tree root = builder.learnTree(exampleSet);

			// create and return model
			model = new TreeModel(exampleSet, root);

			getOutputPorts().getPortByName("model").deliver(model);

		} catch (Exception e) {
			throw new OperatorException(e.getMessage(), e);
		}
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
