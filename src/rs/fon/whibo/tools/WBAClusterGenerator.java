package rs.fon.whibo.tools;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import rs.fon.whibo.GC.ExternalValidation.AdjustedRandIndex;
import rs.fon.whibo.GC.ExternalValidation.FowlkesMallowsIndex;
import rs.fon.whibo.GC.ExternalValidation.JaccardIndex;
import rs.fon.whibo.GC.ExternalValidation.RandIndex;
import rs.fon.whibo.GC.component.DistanceMeasure.Chebychev;
import rs.fon.whibo.GC.component.DistanceMeasure.CityBlok;
import rs.fon.whibo.GC.component.DistanceMeasure.CosineSimilarity;
import rs.fon.whibo.GC.component.DistanceMeasure.DiceNumerical;
import rs.fon.whibo.GC.component.DistanceMeasure.Euclidian;
import rs.fon.whibo.GC.component.DistanceMeasure.InnerProduct;
import rs.fon.whibo.GC.component.DistanceMeasure.JaccardNumerical;
import rs.fon.whibo.GC.component.DistanceMeasure.MaxProduct;
import rs.fon.whibo.GC.component.Evaluation.GlobalSilhouetteIndex;
import rs.fon.whibo.GC.component.Evaluation.IntraClusterDistance;
import rs.fon.whibo.GC.component.Evaluation.MInMaxCut;
import rs.fon.whibo.GC.component.Evaluation.RCsInDevelopement.BIC;
import rs.fon.whibo.GC.component.Evaluation.RCsInDevelopement.Symmetry;
import rs.fon.whibo.GC.component.Initialization.KMeansPlusPlus;
import rs.fon.whibo.GC.component.RecalculateRepresentatives.ArithmeticMean;
import rs.fon.whibo.GC.component.RecalculateRepresentatives.Medoid;
import rs.fon.whibo.GC.component.RecalculateRepresentatives.Online;
import rs.fon.whibo.GC.component.SplitClusters.XMeans;
import rs.fon.whibo.GC.component.StopCriteria.MaxOptimizationSteps;
import rs.fon.whibo.GC.problem.GenericClustererProblemBuilder;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.problem.SubproblemParameter;
import rs.fon.whibo.problem.SubproblemParameterReader;
import rs.fon.whibo.problem.serialization.ProblemEncoder;

public class WBAClusterGenerator {

	private static String[] initializeCentroids = { Random.class.getName(),
			KMeansPlusPlus.class.getName() };
	private static String[] measureDistance = { Chebychev.class.getName(),
			CityBlok.class.getName(), CosineSimilarity.class.getName(),
			Euclidian.class.getName(), JaccardNumerical.class.getName(),
			MaxProduct.class.getName(), InnerProduct.class.getName(),
			DiceNumerical.class.getName() };
	private static String[] calucateRepresentatives = {
			ArithmeticMean.class.getName(), Medoid.class.getName(),
			Online.class.getName() };
	private static String[] stoppingCriteria = { null,
			MaxOptimizationSteps.class.getName() };
	private static String[] splitClusters = { null, XMeans.class.getName() };
	private static String[] evaluation = { RandIndex.class.getName(),
			AdjustedRandIndex.class.getName(), JaccardIndex.class.getName(),
			FowlkesMallowsIndex.class.getName(), BIC.class.getName(),
			GlobalSilhouetteIndex.class.getName(),
			IntraClusterDistance.class.getName(), MInMaxCut.class.getName(),
			Symmetry.class.getName() };

	public static void main(String[] args) {

		int i = 1;

		for (String init : initializeCentroids)
			for (String distance : measureDistance)
				for (String representatives : calucateRepresentatives)
					for (String stop : stoppingCriteria)
						for (String split : splitClusters)
							for (String evaluate : evaluation) {

								Problem problem = createTreeProblem(init,
										distance, representatives, stop, split,
										evaluate);
								saveToWBAfile(problem, "algorithm" + i++);
							}

	}

	public static Problem createTreeProblem(String init, String distance,
			String representatives, String stop, String split, String evaluate) {
		Problem gc = null;
		try {

			// -------------------------------------------------------------
			GenericClustererProblemBuilder gcBuilder = new GenericClustererProblemBuilder();
			gc = gcBuilder.buildProblem();
			Subproblem initSP = gc.getSubproblems().get(0);
			Subproblem distanceSP = gc.getSubproblems().get(1);
			Subproblem representativesSP = gc.getSubproblems().get(2);
			Subproblem stopSP = gc.getSubproblems().get(3);
			Subproblem splitSP = gc.getSubproblems().get(4);
			Subproblem evaluateSP = gc.getSubproblems().get(5);

			// -------------------------------------------------------------
			// Initialize clusters
			{
				if (init != null) {
					SubproblemData sd = new SubproblemData();
					sd.setNameOfImplementationClass(init);
					List<SubproblemParameter> params = new LinkedList<SubproblemParameter>();
					params.addAll(SubproblemParameterReader
							.readParameters(Class.forName(init)));
					for (SubproblemParameter par : params) {
						par.setXenteredValue(par.getDefaultValue());
					}
					sd.setListOfParameters(params);
					List<SubproblemData> subproblemsData = new LinkedList<SubproblemData>();
					subproblemsData.add(sd);
					initSP.setMultipleSubproblemData(subproblemsData);
				}
			}

			// -------------------------------------------------------------
			// Measure distance
			{
				if (distance != null) {
					SubproblemData sd = new SubproblemData();
					sd.setNameOfImplementationClass(distance);
					List<SubproblemParameter> params = new LinkedList<SubproblemParameter>();
					params.addAll(SubproblemParameterReader
							.readParameters(Class.forName(distance)));
					for (SubproblemParameter par : params) {
						par.setXenteredValue(par.getDefaultValue());
					}
					sd.setListOfParameters(params);
					List<SubproblemData> subproblemsData = new LinkedList<SubproblemData>();
					subproblemsData.add(sd);
					distanceSP.setMultipleSubproblemData(subproblemsData);
				}
			}

			// -------------------------------------------------------------
			// Calculate representatives
			{
				if (representatives != null) {
					SubproblemData sd = new SubproblemData();
					sd.setNameOfImplementationClass(representatives);
					List<SubproblemParameter> params = new LinkedList<SubproblemParameter>();
					params.addAll(SubproblemParameterReader
							.readParameters(Class.forName(representatives)));
					for (SubproblemParameter par : params) {
						par.setXenteredValue(par.getDefaultValue());
					}
					sd.setListOfParameters(params);
					List<SubproblemData> subproblemsData = new LinkedList<SubproblemData>();
					subproblemsData.add(sd);
					representativesSP
							.setMultipleSubproblemData(subproblemsData);
				}
			}

			// -------------------------------------------------------------
			// Stopping criteria
			{
				if (stop != null) {
					SubproblemData sd = new SubproblemData();
					sd.setNameOfImplementationClass(stop);
					List<SubproblemParameter> params = new LinkedList<SubproblemParameter>();
					params.addAll(SubproblemParameterReader
							.readParameters(Class.forName(stop)));
					for (SubproblemParameter par : params) {
						par.setXenteredValue(par.getDefaultValue());
					}
					sd.setListOfParameters(params);
					List<SubproblemData> subproblemsData = new LinkedList<SubproblemData>();
					subproblemsData.add(sd);
					stopSP.setMultipleSubproblemData(subproblemsData);
				}
			}

			// -------------------------------------------------------------
			// Split clusters
			{
				if (split != null) {
					SubproblemData sd = new SubproblemData();
					sd.setNameOfImplementationClass(split);
					List<SubproblemParameter> params = new LinkedList<SubproblemParameter>();
					params.addAll(SubproblemParameterReader
							.readParameters(Class.forName(split)));
					for (SubproblemParameter par : params) {
						par.setXenteredValue(par.getDefaultValue());
					}
					sd.setListOfParameters(params);
					List<SubproblemData> subproblemsData = new LinkedList<SubproblemData>();
					subproblemsData.add(sd);
					splitSP.setMultipleSubproblemData(subproblemsData);
				}
			}

			// -------------------------------------------------------------
			// Evaluate clusters
			{
				if (evaluate != null) {
					SubproblemData sd = new SubproblemData();
					sd.setNameOfImplementationClass(evaluate);
					List<SubproblemParameter> params = new LinkedList<SubproblemParameter>();
					params.addAll(SubproblemParameterReader
							.readParameters(Class.forName(evaluate)));
					for (SubproblemParameter par : params) {
						par.setXenteredValue(par.getDefaultValue());
					}
					sd.setListOfParameters(params);
					List<SubproblemData> subproblemsData = new LinkedList<SubproblemData>();
					subproblemsData.add(sd);
					evaluateSP.setMultipleSubproblemData(subproblemsData);
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			// TODO: handle
		}

		return gc;
	}

	public static File saveToWBAfile(Problem problem, String fileName) {

		String filePath = "D:\\" + fileName + ".wba";
		ProblemEncoder.encodeFormProcesToXML(problem, filePath);
		File wbaFile = new File(filePath);
		if (wbaFile.exists())
			return wbaFile;
		else
			return null;

	}

}
