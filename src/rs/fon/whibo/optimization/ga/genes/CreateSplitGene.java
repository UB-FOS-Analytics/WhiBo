package rs.fon.whibo.optimization.ga.genes;

import java.util.ArrayList;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;

import rs.fon.whibo.GDT.component.possibleSplits.SignificantCategorical;
import rs.fon.whibo.GDT.problem.subproblem.PossibleSplit;
import rs.fon.whibo.optimization.ga.genes.ComponentAlleleParameter.ComponentType;
import rs.fon.whibo.optimization.ga.rapidminer.ProblemHolder;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.problem.SubproblemParameter;

public class CreateSplitGene extends SubproblemGene {

	private static final long serialVersionUID = -719327453192666159L;

	public CreateSplitGene(Configuration a_configuration)
			throws InvalidConfigurationException {
		super(a_configuration);
	}

	@Override
	protected Gene newGeneInternal() {
		try {
			return new CreateSplitGene(getConfiguration());
		} catch (InvalidConfigurationException iex) {
			throw new IllegalStateException(iex.getMessage());
		}
	}

	@Override
	public void setupAvailableAlleles() {
		ArrayList<ComponentAllele> components = new ArrayList<ComponentAllele>();
		// components.add(new ComponentAllele(BinaryNumerical.class.getName()));
		// //Uvek je ukljuceno
		// components.add(new
		// ComponentAllele(BinaryCategorical.class.getName()));
		// components
		// .add(new ComponentAllele(MultiwayCategorical.class.getName()));
		// components.add(new ComponentAllele(SignificantCategorical.class
		// .getName(), new ComponentAlleleParameter(0.05, 0.5, 0.05,
		// ComponentType.Double), new ComponentAlleleParameter(0, 0.049,
		// 0.049, ComponentType.Double)));
		// components.add(new ComponentAllele("All"));

		Problem problem = ProblemHolder.getInstance().getProblem();
		for (Subproblem sb : problem.getSubproblems()) {
			if (sb.getClass().equals(PossibleSplit.class)) {
				if (sb.getMultipleStepData() != null) {
					if (sb.getMultipleStepData().size() == 1) {
						for (SubproblemData spd : sb.getMultipleStepData()) {
							if (spd.getNameOfImplementationClass()
									.equalsIgnoreCase(
											SignificantCategorical.class
													.getCanonicalName())) {
								ComponentAllele ca = new ComponentAllele(
										spd.getNameOfImplementationClass());
								ComponentAlleleParameter[] capArray = new ComponentAlleleParameter[1];
								for (SubproblemParameter sbpp : spd
										.getListOfParameters()) {
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
							} else {
								ComponentAllele ca = new ComponentAllele(
										spd.getNameOfImplementationClass());
								components.add(ca);
							}
						}
					}
					for (SubproblemData spd : sb.getMultipleStepData()) {
						if (spd.getNameOfImplementationClass()
								.equalsIgnoreCase(
										SignificantCategorical.class
												.getCanonicalName())) {
							ComponentAllele ca = new ComponentAllele(
									spd.getNameOfImplementationClass());
							ComponentAlleleParameter[] capArray = new ComponentAlleleParameter[1];
							for (SubproblemParameter sbpp : spd
									.getListOfParameters()) {
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
						} else {
							ComponentAllele ca = new ComponentAllele(
									spd.getNameOfImplementationClass());
							components.add(ca);
						}
					}
				}
			}
		}
		// components.add(new ComponentAllele(null));

		setAvailableAlleles(components);
	}

}
