package rs.fon.whibo.optimization.ga.genes;

import java.util.ArrayList;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;

import rs.fon.whibo.GDT.problem.subproblem.SplitEvaluation;
import rs.fon.whibo.optimization.ga.rapidminer.ProblemHolder;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;

public class EvaluateSplitGene extends SubproblemGene {

	private static final long serialVersionUID = -719327453192666159L;

	public EvaluateSplitGene(Configuration a_configuration)
			throws InvalidConfigurationException {
		super(a_configuration);

	}

	@Override
	protected Gene newGeneInternal() {
		try {
			return new EvaluateSplitGene(getConfiguration());
		} catch (InvalidConfigurationException iex) {
			throw new IllegalStateException(iex.getMessage());
		}
	}

	@Override
	public void setupAvailableAlleles() {
		ArrayList<ComponentAllele> components = new ArrayList<ComponentAllele>();
		// components.add(new ComponentAllele(InformationGain.class.getName()));
		// components.add(new ComponentAllele(GainRatio.class.getName()));
		// components.add(new ComponentAllele(GiniIndex.class.getName()));
		// components.add(new ComponentAllele(ChiSquare.class.getName()));
		// components.add(new ComponentAllele(DistanceMeasure.class.getName()));

		Problem problem = ProblemHolder.getInstance().getProblem();
		for (Subproblem sb : problem.getSubproblems()) {
			if (sb.getClass().equals(SplitEvaluation.class)) {
				if (sb.getMultipleStepData() != null) {
					if (sb.getMultipleStepData().size() == 1) {
						for (SubproblemData spd : sb.getMultipleStepData()) {
							ComponentAllele ca = new ComponentAllele(
									spd.getNameOfImplementationClass());
							components.add(ca);
						}
					}
					for (SubproblemData spd : sb.getMultipleStepData()) {
						ComponentAllele ca = new ComponentAllele(
								spd.getNameOfImplementationClass());
						components.add(ca);
					}
				}
			}
		}
		// components.add(new ComponentAllele(null));

		setAvailableAlleles(components);
	}

}
