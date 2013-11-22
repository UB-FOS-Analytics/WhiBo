package rs.fon.whibo.validation;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import rs.fon.whibo.GC.ExternalValidation.AdjustedRandIndex;
import rs.fon.whibo.GC.ExternalValidation.FowlkesMallowsIndex;
import rs.fon.whibo.GC.ExternalValidation.JaccardIndex;
import rs.fon.whibo.GC.ExternalValidation.RandIndex;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.Evaluation.Connectivity;
import rs.fon.whibo.GC.component.Evaluation.Evaluation;
import rs.fon.whibo.GC.component.Evaluation.GlobalSilhouetteIndex;
import rs.fon.whibo.GC.component.Evaluation.IntraClusterDistance;
import rs.fon.whibo.GC.component.Evaluation.MInMaxCut;
import rs.fon.whibo.GC.component.Evaluation.XBIndex;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.clustering.CentroidClusterModel;
import com.rapidminer.operator.clustering.ClusterModel;
import com.rapidminer.operator.performance.PerformanceCriterion;
import com.rapidminer.operator.performance.PerformanceVector;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.parameter.UndefinedParameterError;
import com.rapidminer.tools.Tools;
import com.rapidminer.tools.math.Averagable;

public class ClusterExternalValidation extends Operator {

	private final InputPort exampleSetInput = getInputPorts().createPort(
			"training set");
	private final InputPort clusterModelInput = getInputPorts().createPort(
			"cluster model");
	private final OutputPort validationOutput = getOutputPorts().createPort(
			"performances");
	// private final OutputPort exampleSetOutput =
	// getOutputPorts().createPort("exampleSet");

	public static final boolean Fowlkes_Mallows_Index = false;
	public static final boolean Jaccard_Index = false;
	public static final boolean Rand_Index = false;
	public static final boolean Adjusted_Rand_Index = false;

	public static final rs.fon.whibo.GC.problem.subproblem.DistanceMeasure distanceMeasure = new rs.fon.whibo.GC.problem.subproblem.DistanceMeasure();

	public ClusterExternalValidation(OperatorDescription description) {
		super(description);
	}

	public List<ParameterType> getParameterTypes() {
		List<ParameterType> types = super.getParameterTypes();
		ParameterType type;

		type = new ParameterTypeCategory("Distance_Measure",
				"Defines the distance measure used for cluster evaluation",
				distanceMeasure.getAvailableImplementationClassNames(), 0);
		type.setExpert(false);
		types.add(type);
		type = new ParameterTypeBoolean("Fowlkes_Mallows_Index",
				"Evaluates cluster model with BIC", false);
		type.setExpert(false);
		types.add(type);
		type = new ParameterTypeBoolean("Jaccard_Index",
				"Evaluates cluster model with BIC", false);
		type.setExpert(false);
		types.add(type);
		type = new ParameterTypeBoolean("Rand_Index",
				"Evaluates cluster model with BIC", false);
		type.setExpert(false);
		types.add(type);
		type = new ParameterTypeBoolean("Adjusted_Rand_Index",
				"Evaluates cluster model with BIC", false);
		type.setExpert(false);
		types.add(type);
		return types;
	}

	@Override
	public void doWork() throws OperatorException {

		// WhiBoCentroidClusterModel ccm = clusterModelInput.getData();
		ClusterModel ccm = clusterModelInput.getData();

		int size = 0;
		// ExampleSet exampleSet = getInput(ExampleSet.class);
		ExampleSet exampleSet = exampleSetInput.getData();
		// getValidations(ccm, exampleSet);

		validationOutput.deliver(getValidations(ccm, exampleSet));
	}

	/** Returns the the classes this operator provides as input. */
	@SuppressWarnings("unchecked")
	public Class[] getInputClasses() {
		return new Class[] {
				com.rapidminer.operator.clustering.ClusterModel.class,
				ExampleSet.class };
	}

	/** Returns the the classes this operator expects as output. */
	@SuppressWarnings("unchecked")
	public Class[] getOutputClasses() {
		return new Class[] { PerformanceVector.class };
	}

	// private IOObject getValidations( WhiBoCentroidClusterModel
	// centroidClusterModel, ExampleSet exampleSet) {
	private IOObject getValidations(ClusterModel centroidClusterModel,
			ExampleSet exampleSet) {

		CentroidClusterModel centroidClusterModelRapid = null;
		WhiBoCentroidClusterModel centroidClusterModelWhiBo = null;

		if (centroidClusterModel.getClass().equals(CentroidClusterModel.class)) {

			centroidClusterModelRapid = (CentroidClusterModel) centroidClusterModel;
		} else {

			centroidClusterModelWhiBo = (WhiBoCentroidClusterModel) centroidClusterModel;
		}

		// Gets distance measure from the component repository on the basis of
		// selected parameter
		String className = getParameterType("Distance_Measure").toString(0);
		rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure distance = null;
		Constructor c = null;
		try {
			c = Class.forName(className).getConstructor(
					new Class[] { List.class });

			distance = (rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure) c
					.newInstance(new Object[] { new LinkedList<SubproblemParameter>() });
		} catch (Exception e) {
		}

		Evaluation e = null;
		double evaluation = 0;
		MyPerformanceCriterion mpc = new MyPerformanceCriterion();

		LinkedList<SubproblemParameter> ll = new LinkedList<SubproblemParameter>();
		// Instantiates sub-problem parameter class for validation measures that
		// need parameters
		SubproblemParameter sp = new SubproblemParameter();
		sp.setParametertType(Integer.class);
		sp.setMinValue("1");
		sp.setMaxValue("1000");

		// Gets and executes evaluation components from the component repository
		// on the basis of selected parameter
		if (getParameterAsBoolean("Intra_Cluster_Distance")) {
			e = new IntraClusterDistance(ll);
			if (centroidClusterModel.getClass().equals(
					CentroidClusterModel.class))
				evaluation = e.Evaluate(distance, centroidClusterModelRapid,
						exampleSet);
			else
				evaluation = e.Evaluate(distance, centroidClusterModelWhiBo,
						exampleSet);

			mpc.addPerformance("Intra_Cluster_Distance", evaluation);
		}
		if (getParameterAsBoolean("Connectivity")) {

			try {
				sp.setXenteredValue(getParameter("NN_Connectivity").toString());
			} catch (UndefinedParameterError e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			ll.add(sp);
			e = new Connectivity(ll);
			if (centroidClusterModel.getClass().equals(
					CentroidClusterModel.class))
				evaluation = e.Evaluate(distance, centroidClusterModelRapid,
						exampleSet);
			else
				evaluation = e.Evaluate(distance, centroidClusterModelWhiBo,
						exampleSet);
			mpc.addPerformance("Connectivity", evaluation);
		}
		if (getParameterAsBoolean("Global_Silhouette_Index")) {
			e = new GlobalSilhouetteIndex(ll);
			if (centroidClusterModel.getClass().equals(
					CentroidClusterModel.class))
				evaluation = e.Evaluate(distance, centroidClusterModelRapid,
						exampleSet);
			else
				evaluation = e.Evaluate(distance, centroidClusterModelWhiBo,
						exampleSet);
			mpc.addPerformance("Global_Silhouette_Index", evaluation);
		}
		if (getParameterAsBoolean("XB_Index")) {
			e = new XBIndex(ll);
			if (centroidClusterModel.getClass().equals(
					CentroidClusterModel.class))
				evaluation = e.Evaluate(distance, centroidClusterModelRapid,
						exampleSet);
			else
				evaluation = e.Evaluate(distance, centroidClusterModelWhiBo,
						exampleSet);
			mpc.addPerformance("XB_Index", evaluation);
		}
		if (getParameterAsBoolean("Min_Max_Cut")) {
			e = new MInMaxCut(ll);
			if (centroidClusterModel.getClass().equals(
					CentroidClusterModel.class))
				evaluation = e.Evaluate(distance, centroidClusterModelRapid,
						exampleSet);
			else
				evaluation = e.Evaluate(distance, centroidClusterModelWhiBo,
						exampleSet);
			mpc.addPerformance("Min_Max_Cut", evaluation);
		}
		if (getParameterAsBoolean("Symmetry")) {
			try {
				sp.setXenteredValue(getParameter("NN_Symmetry").toString());
			} catch (UndefinedParameterError e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ll.removeFirstOccurrence(sp);
			ll.add(sp);
			// e=new Symmetry(ll);
			// if
			// (centroidClusterModel.getClass().equals(CentroidClusterModel.class))
			// evaluation = e.Evaluate(distance, centroidClusterModelRapid,
			// exampleSet);
			// else
			// evaluation = e.Evaluate(distance, centroidClusterModelWhiBo,
			// exampleSet);
			// mpc.addPerformance("Symmetry", evaluation);
			// }
			// if(getParameterAsBoolean("BIC")){
			// e=new BIC(ll);
			// if
			// (centroidClusterModel.getClass().equals(CentroidClusterModel.class))
			// evaluation = e.Evaluate(distance, centroidClusterModelRapid,
			// exampleSet);
			// else
			// evaluation = e.Evaluate(distance, centroidClusterModelWhiBo,
			// exampleSet);
			// mpc.addPerformance("BIC", evaluation);
		}

		if (getParameterAsBoolean("Fowlkes_Mallows_Index")) {
			e = new FowlkesMallowsIndex(ll);
			if (centroidClusterModel.getClass().equals(
					CentroidClusterModel.class))
				evaluation = e.Evaluate(distance, centroidClusterModelRapid,
						exampleSet);
			else
				evaluation = e.Evaluate(distance, centroidClusterModelWhiBo,
						exampleSet);

			mpc.addPerformance("Fowlkes_Mallows_Index", evaluation);
		}
		if (getParameterAsBoolean("Jaccard_Index")) {
			e = new JaccardIndex(ll);
			if (centroidClusterModel.getClass().equals(
					CentroidClusterModel.class))
				evaluation = e.Evaluate(distance, centroidClusterModelRapid,
						exampleSet);
			else
				evaluation = e.Evaluate(distance, centroidClusterModelWhiBo,
						exampleSet);

			mpc.addPerformance("Jaccard_Index", evaluation);
		}
		if (getParameterAsBoolean("Rand_Index")) {
			e = new RandIndex(ll);

			evaluation = e.Evaluate(distance, centroidClusterModel, exampleSet);

			mpc.addPerformance("Rand_Index", evaluation);
		}
		if (getParameterAsBoolean("Adjusted_Rand_Index")) {
			e = new AdjustedRandIndex(ll);
			if (centroidClusterModel.getClass().equals(
					CentroidClusterModel.class))
				evaluation = e.Evaluate(distance, centroidClusterModelRapid,
						exampleSet);
			else
				evaluation = e.Evaluate(distance, centroidClusterModelWhiBo,
						exampleSet);

			mpc.addPerformance("Adjusted_Rand_Index", evaluation);
		}

		return mpc;
	}

	private int getNumberOfSelectedEvaluations() {
		int number = 0;
		Iterator iter = this.getParameters().iterator();
		while (iter.hasNext()) {
			boolean icd = getParameterAsBoolean(iter.next().toString());
			if (icd)
				number++;
		}

		return number;
	}

	// //////////////////////////////////////////////////////////////
	// INNER CLASS FOR HOLDING THE RESULTS

	public static class MyPerformanceCriterion extends PerformanceCriterion {
		String name = "Cluster evaluation";
		LinkedList<String> names = new LinkedList<String>();
		LinkedList<Double> values = new LinkedList<Double>();

		public MyPerformanceCriterion() {
			super();
			// this.name=name;
			// this.value=value;
		}

		private void addPerformance(String name, double value) {
			names.add(name);
			values.add(value);
		}

		@Override
		public String toString() {
			String results = new String();
			for (int i = 0; i < names.size(); i++) {
				results += names.get(i).toString() + ": "
						+ Tools.formatNumber(values.get(i).doubleValue())
						+ "\n";
			}
			return results;
			// return name+" "+ Tools.formatNumber(value);
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public double getExampleCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double getFitness() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected void buildSingleAverage(Averagable averagable) {
			// TODO Auto-generated method stub

		}

		@Override
		public double getMikroAverage() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double getMikroVariance() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}

	}

	// private int getIDOfEvaluation(ParameterTypeBoolean bool){
	// int id=0;
	// if (bool.)
	// id=Integer.parseInt(bool.getKey());
	// this.Intra_Cluster_Distance=false;
	// return id;
	// }
	//
	// private Evaluation mapParameterToComponent(int paramIndex){
	// switch(paramIndex){
	// case 1: return new IntraClusterDistance(new
	// LinkedList<SubproblemParameter>());
	// case 2: return new Connectivity(new LinkedList<SubproblemParameter>());
	// case 3: return new GlobalSilhouetteIndex(new
	// LinkedList<SubproblemParameter>());
	// case 4: return new XBIndex(new LinkedList<SubproblemParameter>());
	// case 5: return new MInMaxCut(new LinkedList<SubproblemParameter>());
	// //case 6: return new Symmetry(new LinkedList<SubproblemParameter>());
	// case 7: return new BIC(new LinkedList<SubproblemParameter>());
	//
	// }
	// return null;

	// }
}
