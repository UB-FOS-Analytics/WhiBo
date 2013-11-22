package rs.fon.whibo.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import rs.fon.whibo.GDT.component.possibleSplits.BinaryCategorical;
import rs.fon.whibo.GDT.component.possibleSplits.BinaryNumerical;
import rs.fon.whibo.GDT.component.possibleSplits.MultiwayCategorical;
import rs.fon.whibo.GDT.component.possibleSplits.SignificantCategorical;
import rs.fon.whibo.GDT.component.prunning.CostComplexity;
import rs.fon.whibo.GDT.component.prunning.MinLeafSize;
import rs.fon.whibo.GDT.component.prunning.MinimalError;
import rs.fon.whibo.GDT.component.prunning.PessimisticError;
import rs.fon.whibo.GDT.component.prunning.ReducedError;
import rs.fon.whibo.GDT.component.removeInsignificantAttributes.ChiSquareTestCategorical;
import rs.fon.whibo.GDT.component.removeInsignificantAttributes.FTestNumerical;
import rs.fon.whibo.GDT.component.splitEvaluation.ChiSquare;
import rs.fon.whibo.GDT.component.splitEvaluation.DistanceMeasure;
import rs.fon.whibo.GDT.component.splitEvaluation.GainRatio;
import rs.fon.whibo.GDT.component.splitEvaluation.GiniIndex;
import rs.fon.whibo.GDT.component.splitEvaluation.InformationGain;
import rs.fon.whibo.GDT.component.stoppingCriteria.LeafLabelConfidence;
import rs.fon.whibo.GDT.component.stoppingCriteria.MinNodeSize;
import rs.fon.whibo.GDT.component.stoppingCriteria.TreeDepth;
import rs.fon.whibo.GDT.problem.GenericTreeProblemBuilder;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.problem.SubproblemParameter;
import rs.fon.whibo.problem.SubproblemParameterReader;
import rs.fon.whibo.problem.serialization.ProblemEncoder;

public class WBATreeGenerator {

	private static String[] removeInsignificantAttributes = { null,
			// ChiSquareTestCategorical.class.getName(),
			// FTestNumerical.class.getName(),
			"RIA" };
	private static String[] createSplit = { BinaryCategorical.class.getName(),
			MultiwayCategorical.class.getName(),
			SignificantCategorical.class.getName(), "All" };
	private static String[] evaluateSplit = { GainRatio.class.getName(),
			GiniIndex.class.getName(), InformationGain.class.getName(),
			DistanceMeasure.class.getName(), ChiSquare.class.getName() };
	private static String[] stoppingCriteria = { null,
			TreeDepth.class.getName(), MinNodeSize.class.getName(),
			LeafLabelConfidence.class.getName() };
	private static String[] prunning = { null, ReducedError.class.getName(),
			CostComplexity.class.getName(), PessimisticError.class.getName(),
			MinimalError.class.getName(), MinLeafSize.class.getName() };

	public static void main(String[] args) {
		NumberFormat myFormat = NumberFormat.getInstance();
		myFormat.setGroupingUsed(false);
		myFormat.setMinimumIntegerDigits(4);
		File csvAlgorithmFile = new File("D:\\algorithms.csv");
		Writer csvAlgorithmFileOutput = null;
		try {
			csvAlgorithmFileOutput = new OutputStreamWriter(
					new FileOutputStream(csvAlgorithmFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int i = 1;

		for (String ria : removeInsignificantAttributes)
			for (String create : createSplit)
				for (String evaluate : evaluateSplit)
					for (String stop : stoppingCriteria)
						for (String prune : prunning) {

							Problem problem = createTreeProblem(ria, create,
									evaluate, stop, prune);
							String algorithmNumber = myFormat.format(i++);
							saveToWBAfile(problem, "algorithm"
									+ algorithmNumber);
							createAlgorithmDescriptionRecord(
									csvAlgorithmFileOutput, algorithmNumber,
									ria, create, evaluate, stop, prune);
						}

	}

	private static void createAlgorithmDescriptionRecord(Writer fileOutput,
			String algorithmNumber, String ria, String create, String evaluate,
			String stop, String prune) {
		try {

			if (ria == null)
				ria = "null";
			if (create == null)
				create = "null";
			if (evaluate == null)
				evaluate = "null";
			if (stop == null)
				stop = "null";
			if (prune == null)
				prune = "null";

			if (ria.lastIndexOf(".") != -1)
				ria = ria.substring(ria.lastIndexOf(".") + 1);
			if (create.lastIndexOf(".") != -1)
				create = create.substring(create.lastIndexOf(".") + 1);
			if (evaluate.lastIndexOf(".") != -1)
				evaluate = evaluate.substring(evaluate.lastIndexOf(".") + 1);
			if (stop.lastIndexOf(".") != -1)
				stop = stop.substring(stop.lastIndexOf(".") + 1);
			if (prune.lastIndexOf(".") != -1)
				prune = prune.substring(prune.lastIndexOf(".") + 1);

			fileOutput.write(algorithmNumber + "," + ria + "," + create + ","
					+ evaluate + "," + stop + "," + prune + "\n");
			fileOutput.flush();

		} catch (Exception e) {

		}

	}

	public static Problem createTreeProblem(String ria, String create,
			String eval, String stop, String prun) {
		Problem gdt = null;
		try {

			// -------------------------------------------------------------
			GenericTreeProblemBuilder gdtBuilder = new GenericTreeProblemBuilder();
			gdt = gdtBuilder.buildProblem();
			Subproblem riaSP = gdt.getSubproblems().get(0);
			Subproblem possibleSplitSP = gdt.getSubproblems().get(1);
			Subproblem evaluateSplitSP = gdt.getSubproblems().get(2);
			Subproblem stoppingCriteriaSP = gdt.getSubproblems().get(3);
			Subproblem prunningSP = gdt.getSubproblems().get(4);

			// -------------------------------------------------------------
			//
			if (ria != null) {
				if (ria.equals("RIA"))
					addRIA(riaSP);
				else
					addSubproblem(riaSP, ria);
			}

			if (create != null) {
				if (create.equals("All"))
					addAllCreateSplit(possibleSplitSP);
				else
					addSubproblem(possibleSplitSP, create);
			}

			addSubproblem(possibleSplitSP, BinaryNumerical.class.getName());

			if (eval != null) {
				addSubproblem(evaluateSplitSP, eval);
			}

			if (stop != null) {
				addSubproblem(stoppingCriteriaSP, stop);
			}

			if (prun != null) {
				addSubproblem(prunningSP, prun);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			// TODO: handle
		}

		return gdt;
	}

	public static File saveToWBAfile(Problem problem, String fileName) {

		String filePath = "D:\\b\\" + fileName + ".wba";
		ProblemEncoder.encodeFormProcesToXML(problem, filePath);
		File wbaFile = new File(filePath);
		if (wbaFile.exists())
			return wbaFile;
		else
			return null;

	}

	private static void addSubproblem(Subproblem sp,
			String implementationClassName) throws ClassNotFoundException {

		SubproblemData subproblemData = new SubproblemData();
		subproblemData.setNameOfImplementationClass(implementationClassName);
		List<SubproblemParameter> params = new LinkedList<SubproblemParameter>();
		params.addAll(SubproblemParameterReader.readParameters(Class
				.forName(implementationClassName)));

		for (int i = 0; i < params.size(); i++) {
			params.get(i).setXenteredValue(params.get(i).getDefaultValue());
		}
		subproblemData.setListOfParameters(params);

		if (sp.isMultiple()) {
			List<SubproblemData> subproblemsData = sp.getMultipleStepData();
			if (subproblemsData == null)
				subproblemsData = new LinkedList<SubproblemData>();
			subproblemsData.add(subproblemData);
			sp.setMultipleSubproblemData(subproblemsData);
		} else {
			sp.setSubproblemData(subproblemData);
		}
	}

	private static void addRIA(Subproblem ria) throws Exception {

		addSubproblem(ria, ChiSquareTestCategorical.class.getName());
		addSubproblem(ria, FTestNumerical.class.getName());

	}

	private static void addAllCreateSplit(Subproblem possibleSplit)
			throws Exception {

		addSubproblem(possibleSplit, BinaryCategorical.class.getName());
		addSubproblem(possibleSplit, MultiwayCategorical.class.getName());
		addSubproblem(possibleSplit, SignificantCategorical.class.getName());

	}

}
