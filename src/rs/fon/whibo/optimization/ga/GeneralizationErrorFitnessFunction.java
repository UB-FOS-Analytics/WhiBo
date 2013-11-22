package rs.fon.whibo.optimization.ga;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

import rs.fon.whibo.GDT.component.possibleSplits.BinaryCategorical;
import rs.fon.whibo.GDT.component.possibleSplits.BinaryNumerical;
import rs.fon.whibo.GDT.component.possibleSplits.MultiwayCategorical;
import rs.fon.whibo.GDT.component.possibleSplits.SignificantCategorical;
import rs.fon.whibo.GDT.component.removeInsignificantAttributes.ChiSquareTestCategorical;
import rs.fon.whibo.GDT.component.removeInsignificantAttributes.FTestNumerical;
import rs.fon.whibo.GDT.problem.GenericTreeProblemBuilder;
import rs.fon.whibo.optimization.ga.genes.CreateSplitGene;
import rs.fon.whibo.optimization.ga.genes.EvaluateSplitGene;
import rs.fon.whibo.optimization.ga.genes.PruneGene;
import rs.fon.whibo.optimization.ga.genes.RIAGene;
import rs.fon.whibo.optimization.ga.genes.StoppingGene;
import rs.fon.whibo.optimization.ga.rapidminer.EAOperator;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.problem.SubproblemParameter;
import rs.fon.whibo.problem.SubproblemParameterReader;
import rs.fon.whibo.problem.serialization.ProblemEncoder;

import com.rapidminer.example.ExampleSet;

public class GeneralizationErrorFitnessFunction extends FitnessFunction {

	private EAOperator eaOperator;

	private HashMap<String, Double> cache;

	private ExampleSet exampleSet;

	public GeneralizationErrorFitnessFunction(EAOperator eaOperator) {
		cache = new HashMap<String, Double>();
		this.eaOperator = eaOperator;
		this.switchToFullFitnessFunction();
	}

	@Override
	protected double evaluate(IChromosome chromosome) {

		// check cached function values
		String chromosomeDescription = Tools.chromosomeToString(chromosome);

		// System.out.println(chromosomeDescription);

		if (cache.containsKey(chromosomeDescription)) {
			LogProgress.getInstance().numReturnedFromCache++;
			return cache.get(chromosomeDescription).doubleValue();
		}
		LogProgress.getInstance().numEvaluations++;

		Problem problem = createProblem(chromosome);
		// ako nije izabrao nijedan CreateSplit... ubija jedinku
		// if (problem.getSubproblems().get(1).getMultipleStepData()==null)
		// return 0;

		ProblemEncoder.encodeFormProcesToXML(problem, eaOperator.wbaFile);

		double result = eaOperator.evaluate(exampleSet);

		// log cromosome under test
		LogProgress.getInstance().log(chromosome.getGenes(), result);

		// cache the calculated function value
		cache.put(chromosomeDescription, new Double(result));

		return result;
	}

	public Problem createProblem(IChromosome chromosome) {
		Problem gdt = null;
		try {
			// GET GENES----------------------------------------------------

			Gene[] genes = chromosome.getGenes();
			RIAGene riaGene = null;
			CreateSplitGene createSplitGene = null;
			EvaluateSplitGene evaluateSplitGene = null;
			StoppingGene stoppingGene = null;
			PruneGene pruneGene = null;

			for (Gene g : genes) {
				if (g.getClass().equals(RIAGene.class))
					riaGene = (RIAGene) g;
				if (g.getClass().equals(CreateSplitGene.class))
					createSplitGene = (CreateSplitGene) g;
				if (g.getClass().equals(EvaluateSplitGene.class))
					evaluateSplitGene = (EvaluateSplitGene) g;
				if (g.getClass().equals(StoppingGene.class))
					stoppingGene = (StoppingGene) g;
				if (g.getClass().equals(PruneGene.class))
					pruneGene = (PruneGene) g;
			}

			// GET SUBPROBLEMS----------------------------------------------

			GenericTreeProblemBuilder gdtBuilder = new GenericTreeProblemBuilder();
			gdt = gdtBuilder.buildProblem();
			Subproblem ria = gdt.getSubproblems().get(0);
			Subproblem possibleSplit = gdt.getSubproblems().get(1);
			Subproblem evaluateSplit = gdt.getSubproblems().get(2);
			Subproblem stoppingCriteria = gdt.getSubproblems().get(3);
			Subproblem prunning = gdt.getSubproblems().get(4);

			// SETUP SUBPROBLEMS--------------------------------------------

			if (riaGene == null)
				addSubproblem(ria, riaGene.getStaticAllele()
						.getImplementationClassName(), riaGene
						.getStaticAllele().getParameterValues());
			else if (!riaGene.getComponentAllele().isNull()) {
				if (riaGene.getComponentAllele().getImplementationClassName()
						.equals("RIA"))
					addRIA(ria);
				else
					addSubproblem(ria, riaGene.getComponentAllele()
							.getImplementationClassName(), riaGene
							.getComponentAllele().getParameterValues());
			}

			// TODO: ZBUDZ
			addSubproblem(possibleSplit, BinaryNumerical.class.getName(),
					new String[] {});

			if (createSplitGene == null)
				addSubproblem(possibleSplit, createSplitGene.getStaticAllele()
						.getImplementationClassName(), createSplitGene
						.getStaticAllele().getParameterValues());
			else if (!createSplitGene.getComponentAllele().isNull()) {
				if (createSplitGene.getComponentAllele()
						.getImplementationClassName().equals("All"))
					addAllCreateSplit(possibleSplit);
				else
					addSubproblem(possibleSplit, createSplitGene
							.getComponentAllele().getImplementationClassName(),
							createSplitGene.getComponentAllele()
									.getParameterValues());
			}
			if (evaluateSplitGene == null)
				addSubproblem(evaluateSplit, evaluateSplitGene
						.getStaticAllele().getImplementationClassName(),
						evaluateSplitGene.getStaticAllele()
								.getParameterValues());
			else if (!evaluateSplitGene.getComponentAllele().isNull())
				addSubproblem(evaluateSplit, evaluateSplitGene
						.getComponentAllele().getImplementationClassName(),
						evaluateSplitGene.getComponentAllele()
								.getParameterValues());

			if (stoppingGene == null)
				addSubproblem(stoppingCriteria, stoppingGene.getStaticAllele()
						.getImplementationClassName(), stoppingGene
						.getStaticAllele().getParameterValues());
			else if (!stoppingGene.getComponentAllele().isNull())
				addSubproblem(stoppingCriteria, stoppingGene
						.getComponentAllele().getImplementationClassName(),
						stoppingGene.getComponentAllele().getParameterValues());

			if (pruneGene == null)
				addSubproblem(prunning, pruneGene.getStaticAllele()
						.getImplementationClassName(), pruneGene
						.getStaticAllele().getParameterValues());
			else if (!pruneGene.getComponentAllele().isNull())
				addSubproblem(prunning, pruneGene.getComponentAllele()
						.getImplementationClassName(), pruneGene
						.getComponentAllele().getParameterValues());

			// -------------------------------------------------------------

		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return gdt;
	}

	private void addSubproblem(Subproblem sp, String implementationClassName,
			String[] parametersValues) throws ClassNotFoundException {

		SubproblemData subproblemData = new SubproblemData();
		subproblemData.setNameOfImplementationClass(implementationClassName);
		List<SubproblemParameter> params = new LinkedList<SubproblemParameter>();
		params.addAll(SubproblemParameterReader.readParameters(Class
				.forName(implementationClassName)));

		for (int i = 0; i < parametersValues.length; i++) {
			params.get(i).setXenteredValue(parametersValues[i]);
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

	private void addRIA(Subproblem ria) throws Exception {

		addSubproblem(ria, ChiSquareTestCategorical.class.getName(),
				new String[] { "0.05", "0", "0" });
		addSubproblem(ria, FTestNumerical.class.getName(), new String[] {
				"0.05", "0", "0" });

	}

	private void addAllCreateSplit(Subproblem possibleSplit) throws Exception {

		addSubproblem(possibleSplit, BinaryCategorical.class.getName(),
				new String[] {});
		addSubproblem(possibleSplit, MultiwayCategorical.class.getName(),
				new String[] {});
		addSubproblem(possibleSplit, SignificantCategorical.class.getName(),
				new String[] { "0.05", "0.049" });

	}

	public void clearCache() {
		cache.clear();
	}

	public void switchToSurrogateFunction() {

		this.exampleSet = eaOperator.surrogateExampleSet;

	}

	public void switchToFullFitnessFunction() {

		this.exampleSet = eaOperator.fullExampleSet;

	}
}
