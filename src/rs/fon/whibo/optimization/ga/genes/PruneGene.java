package rs.fon.whibo.optimization.ga.genes;

import java.util.ArrayList;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;

import rs.fon.whibo.GDT.component.prunning.CostComplexity;
import rs.fon.whibo.GDT.component.prunning.ReducedError;
import rs.fon.whibo.GDT.problem.subproblem.Prunning;
import rs.fon.whibo.optimization.ga.genes.ComponentAlleleParameter.ComponentType;
import rs.fon.whibo.optimization.ga.rapidminer.ProblemHolder;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.problem.SubproblemParameter;

public class PruneGene extends SubproblemGene {

	private static final long serialVersionUID = -719327453192666159L;

	public PruneGene(Configuration a_configuration)
			throws InvalidConfigurationException {
		super(a_configuration);

	}

	@Override
	protected Gene newGeneInternal() {
		try {
			return new PruneGene(getConfiguration());
		} catch (InvalidConfigurationException iex) {
			throw new IllegalStateException(iex.getMessage());
		}
	}

	@Override
	public void setupAvailableAlleles() {
		ArrayList<ComponentAllele> components = new ArrayList<ComponentAllele>();
		// components.add(new ComponentAllele(PessimisticError.class.getName(),
		// new ComponentAlleleParameter(0.0001, 0.5, 0.25,
		// ComponentType.Double)));
		// components
		// .add(new ComponentAllele(MinLeafSize.class.getName(),
		// new ComponentAlleleParameter(1, 1000, 30,
		// ComponentType.Integer)));
		// components.add(new ComponentAllele(CostComplexity.class.getName(),
		// new ComponentAlleleParameter(0, 1, 0, ComponentType.Integer)));
		// components.add(new ComponentAllele(ReducedError.class.getName()));
		// components
		// .add(new ComponentAllele(MinimalError.class.getName(),
		// new ComponentAlleleParameter(0, 1000, 2,
		// ComponentType.Double)));

		Problem problem = ProblemHolder.getInstance().getProblem();
		for (Subproblem sb : problem.getSubproblems()) {
			if (sb.getClass().equals(Prunning.class)) {
				if (sb.getMultipleStepData() != null) {
					for (SubproblemData spd : sb.getMultipleStepData()) {
						if (spd.getNameOfImplementationClass()
								.equalsIgnoreCase(
										ReducedError.class.getCanonicalName())) {
							ComponentAllele ca = new ComponentAllele(
									spd.getNameOfImplementationClass());
							components.add(ca);
						} else {
							for (SubproblemParameter sbpp : spd
									.getListOfParameters()) {
								ComponentAllele ca = new ComponentAllele(
										spd.getNameOfImplementationClass());
								ComponentAlleleParameter[] capArray = new ComponentAlleleParameter[1];
								if (spd.getNameOfImplementationClass()
										.equalsIgnoreCase(
												CostComplexity.class
														.getCanonicalName())) {
									int min = Integer.parseInt(sbpp
											.getMinValue());
									int max = Integer.parseInt(sbpp
											.getMaxValue());
									int def = Integer.parseInt(sbpp
											.getDefaultValue());
									if (min > def) {
										def = min;
									}
									if (max < def) {
										def = max;
									}
									ComponentAlleleParameter cap = new ComponentAlleleParameter(
											min, max, def,
											ComponentType.Integer);
									capArray[0] = cap;
								} else {
									double min = Double.parseDouble(sbpp
											.getMinValue());
									double max = Double.parseDouble(sbpp
											.getMaxValue());
									double def = Double.parseDouble(sbpp
											.getDefaultValue());
									if (min > def) {
										def = min;
									}
									if (max < def) {
										def = max;
									}
									ComponentAlleleParameter cap = new ComponentAlleleParameter(
											min, max, def, ComponentType.Double);
									capArray[0] = cap;
								}
								ca.setParameters(capArray);
								components.add(ca);
							}
						}
					}
				}
			}
		}

		components.add(new ComponentAllele(null));
		components.add(new ComponentAllele("null"));

		setAvailableAlleles(components);
	}

}
