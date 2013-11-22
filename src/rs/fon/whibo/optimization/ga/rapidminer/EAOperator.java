package rs.fon.whibo.optimization.ga.rapidminer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rs.fon.whibo.GDT.problem.GenericTreeProblemBuilder;
import rs.fon.whibo.integration.adapters.parameter.ParameterTypeProblemFileEA;
import rs.fon.whibo.integration.adapters.parameter.ProblemFileValueEditorEA;
import rs.fon.whibo.optimization.ga.WhiBoTreeGaOpt;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.serialization.ProblemDecoder;
import rs.fon.whibo.problem.serialization.ProblemEncoder;

import com.rapidminer.example.AttributeWeights;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.SplittedExampleSet;
import com.rapidminer.gui.properties.PropertyPanel;
import com.rapidminer.operator.OperatorCapability;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.learner.AbstractLearner;
import com.rapidminer.operator.learner.CapabilityCheck;
import com.rapidminer.operator.learner.Learner;
import com.rapidminer.operator.learner.tree.TreeModel;
import com.rapidminer.operator.performance.PerformanceVector;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.LearnerPrecondition;
import com.rapidminer.operator.ports.metadata.PassThroughRule;
import com.rapidminer.operator.ports.metadata.SubprocessTransformRule;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeFile;
import com.rapidminer.parameter.ParameterTypeList;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.tools.Tools;

public class EAOperator extends OperatorChain implements Learner {

	public static final String PARAMETER_WBA_FILE_PATH_MACRO = "wba_file_path_macro_name";
	public static final String PARAMETER_LOG_FILE_PATH = "log_file_path";
	private static final String PARAMETER_PARAMETERS = "evolutionary_parameters";
	private static final String PARAMETER_VALUES = "values";
	private static final String PARAMETER_PARAMETER = "parameter_name";

	public String wbaFile;
	public ExampleSet fullExampleSet;
	public ExampleSet surrogateExampleSet;
	public double samplePercentage;
	public static String logFilePath;

	private final InputPort exampleSetInput = getInputPorts().createPort(
			"training set");
	private final OutputPort modelOutput = getOutputPorts().createPort("model");
	private final OutputPort exampleSetOutput = getOutputPorts().createPort(
			"exampleSet");
	private final OutputPort performanceOutput = getOutputPorts().createPort(
			"performance");
	private final OutputPort innerExampleSource = getSubprocess(0)
			.getInnerSources().createPort("training set");
	private final InputPort innerPerformanceSink = getSubprocess(0)
			.getInnerSinks().createPort("performance");
	private final InputPort innerModelSink = getSubprocess(0).getInnerSinks()
			.createPort("model");

	WhiBoTreeGaOpt eaAlgorithm;

	public EAOperator(OperatorDescription description) {
		super(description, "Evolutionary search on WhiBo");
		PropertyPanel.registerPropertyValueCellEditor(
				ParameterTypeProblemFileEA.class,
				ProblemFileValueEditorEA.class);
		exampleSetInput.addPrecondition(new LearnerPrecondition(this,
				exampleSetInput));
		getTransformer().addRule(
				new PassThroughRule(exampleSetInput, innerExampleSource, true));
		getTransformer().addRule(new SubprocessTransformRule(getSubprocess(0)));
		getTransformer().addRule(
				new PassThroughRule(innerModelSink, modelOutput, true));
	}

	@Override
	public TreeModel learn(ExampleSet exampleSet) throws OperatorException {
		try {
			eaAlgorithm = new WhiBoTreeGaOpt(this);
			eaAlgorithm.initEA();

			TreeModel bestTreeModel;
			Problem bestWhiboAlgorithm;

			bestWhiboAlgorithm = eaAlgorithm.createOptimalWBAlgorithmTweak();
			ProblemEncoder.encodeFormProcesToXML(bestWhiboAlgorithm,
					this.wbaFile);

			bestTreeModel = produceModel();

			return bestTreeModel;

		} catch (Exception e) {
			throw new OperatorException(e.getMessage());
		}
	}

	public double evaluate(ExampleSet exampleSet) {
		try {
			innerExampleSource.deliver(exampleSet);
			getSubprocess(0).execute();
			PerformanceVector result = (PerformanceVector) innerPerformanceSink
					.getData();
			return result.getMainCriterion().getAverage();
		} catch (Exception e) {
			System.out.println("Failed to evaluate fitness function");
			return 0;
		}
	}

	public TreeModel produceModel() throws OperatorException {
		try {
			innerExampleSource.deliver(fullExampleSet);
			getSubprocess(0).execute();
			TreeModel resultModel = innerModelSink.getData();
			return resultModel;
		} catch (Exception e) {
			throw new OperatorException(
					"Error in training process! .wba file is corrupted or does not exist");
		}
	}

	public void setSamplePercentage(double percentage) throws OperatorException {

		SplittedExampleSet newExampleSet = new SplittedExampleSet(
				fullExampleSet, percentage,
				SplittedExampleSet.STRATIFIED_SAMPLING, false, -1);
		newExampleSet.selectSingleSubset(0);
		this.surrogateExampleSet = newExampleSet;
		this.surrogateExampleSet.setSource(fullExampleSet.getSource());

	}

	@Override
	public void doWork() throws OperatorException {
		try {
			try {
				File file = getParameterAsFile("EA subspace file");
				Problem process = ProblemDecoder.decodeFromXMLToProces(file
						.getAbsolutePath());
				ProblemHolder.getInstance().setProblem(process);
				if (!file.getAbsolutePath().contains(".ass")) {
					throw new Exception(
							"Algorithm space search (.ass) file is not defined");
				}
			} catch (Exception e) {
				throw new OperatorException(
						"Algorithm space search (.ass) file is not defined");
			}

			logFilePath = getParameterAsString(PARAMETER_LOG_FILE_PATH);

			this.wbaFile = "./WhiboEvolutedAlgorithm.wba";
			getProcess().getMacroHandler().addMacro(
					getParameterAsString(PARAMETER_WBA_FILE_PATH_MACRO),
					this.wbaFile);

			this.fullExampleSet = exampleSetInput.getData();
			setSamplePercentage(1);

			// some checks
			if (fullExampleSet.getAttributes().getLabel() == null) {
				throw new UserError(this, 105, new Object[0]);
			}
			if (fullExampleSet.getAttributes().size() == 0) {
				throw new UserError(this, 106, new Object[0]);
			}
			if (fullExampleSet.size() == 0) {
				throw new UserError(this, 117);
			}

			// check capabilities and produce errors if they are not fulfilled
			CapabilityCheck check = new CapabilityCheck(
					this,
					Tools.booleanValue(
							System.getProperty(AbstractLearner.PROPERTY_RAPIDMINER_GENERAL_CAPABILITIES_WARN),
							true));
			check.checkLearnerCapabilities(this, fullExampleSet);

			TreeModel bestTreeModel = learn(fullExampleSet);

			(new File(this.wbaFile)).delete();
			modelOutput.deliver(bestTreeModel);
			exampleSetOutput.deliver(fullExampleSet);
			performanceOutput.deliver(getEstimatedPerformance());
		} catch (Exception e) {
			throw new OperatorException(e.getMessage());
		}
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
	public boolean shouldEstimatePerformance() {
		return false;
	}

	/** The default implementation throws an exception. */
	public PerformanceVector getEstimatedPerformance() throws OperatorException {
		return (PerformanceVector) getSubprocess(0).getInnerSinks()
				.getPortByName("performance").getData();
		// throw new UserError(this, 912, getName(),
		// "estimation of performance not supported.");
	}

	@Override
	public boolean shouldCalculateWeights() {
		return false;
	}

	public AttributeWeights getWeights(ExampleSet exampleSet)
			throws OperatorException {
		throw new UserError(this, 916, getName(),
				"calculation of weights not supported.");
	}

	public List<ParameterType> getParameterTypes() {
		List<ParameterType> types = super.getParameterTypes();
		ParameterType type;

		type = new ParameterTypeProblemFileEA("EA subspace file",
				"Algorithm search space (.ass)", "ass", false,
				GenericTreeProblemBuilder.class);
		type.setExpert(false);
		types.add(type);

		List<String[]> defaultParameters = new ArrayList<String[]>();
		defaultParameters.add(new String[] { "MAX_ALLOWED_EVOLUTIONS", "50" });
		defaultParameters.add(new String[] { "POPULATION_SIZE", "30" });
		defaultParameters.add(new String[] { "MUTATION_RATE", "6" });
		defaultParameters.add(new String[] { "CROSSOVER_RATE", "0.35" });
		defaultParameters.add(new String[] {
				"SWITCH_FROM_SURROGATE_PERCENTAGE_EVOLUTIONS", "0.4" });
		defaultParameters.add(new String[] { "SURROGATE_PERCENTAGE", "0.3" });
		defaultParameters.add(new String[] { "mutateComponents", "true" });
		defaultParameters.add(new String[] { "mutateParameters", "false" });
		defaultParameters.add(new String[] { "componentMutationRate", "1" });
		defaultParameters.add(new String[] { "parametersMutationRate", "1" });

		type = new ParameterTypeList(PARAMETER_PARAMETERS, "The parameters.",
				new ParameterTypeString(PARAMETER_PARAMETER, "The parameter."),
				new ParameterTypeString(PARAMETER_VALUES,
						"The value specifications for the parameters."),
				defaultParameters);
		type.setExpert(false);
		types.add(type);

		type = new ParameterTypeString(
				PARAMETER_WBA_FILE_PATH_MACRO,
				"A macro containing the path to a wba file (WhiBo Algorithm definition file) where the algorithms are being created",
				"wbaFilePath");
		type.setExpert(false);
		types.add(type);

		type = new ParameterTypeFile(PARAMETER_LOG_FILE_PATH,
				"Path to a log file made by EA", "csv", "D:\\logEA.csv");
		type.setExpert(false);
		types.add(type);

		return types;

	}

	public List<String[]> getEAParameters() throws OperatorException {
		// check parameter values
		List<String[]> parametersStrings = getParameterList(PARAMETER_PARAMETERS);

		// initialize data structures
		// Map<String,String> parametersOutput = new HashMap<String, String>();

		// get parameter values and fill data structures
		/*
		 * for (Iterator<ParameterValues> iterator =
		 * parameterValuesList.iterator(); iterator.hasNext(); ) {
		 * ParameterValues parameterValues = iterator.next();
		 * parametersOutput.put(parameterValues.getParameterType().getKey(),
		 * parameterValues.getValuesString()); }
		 */
		return parametersStrings;
	}

}
