package rs.fon.whibo.optimization.ga.genes;

import java.util.ArrayList;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;

import rs.fon.whibo.GDT.problem.subproblem.RemoveInsignificantAtributes;
import rs.fon.whibo.optimization.ga.genes.ComponentAlleleParameter.ComponentType;
import rs.fon.whibo.optimization.ga.rapidminer.ProblemHolder;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.problem.SubproblemParameter;

public class RIAGene extends SubproblemGene {

	private static final long serialVersionUID = -719327453192666159L;

	public RIAGene(Configuration a_configuration)
			throws InvalidConfigurationException {
		super(a_configuration);

	}

	@Override
	protected Gene newGeneInternal() {
		try {
			return new RIAGene(getConfiguration());
		} catch (InvalidConfigurationException iex) {
			throw new IllegalStateException(iex.getMessage());
		}
	}

	@Override
	public void setupAvailableAlleles() {
		ArrayList<ComponentAllele> components = new ArrayList<ComponentAllele>();
		Problem problem = ProblemHolder.getInstance().getProblem();
		for (Subproblem sb : problem.getSubproblems()) {
			if (sb.getClass().equals(RemoveInsignificantAtributes.class)) {
				if (sb.getMultipleStepData() != null) {
					for (SubproblemData spd : sb.getMultipleStepData()) {
						ComponentAllele ca = new ComponentAllele(
								spd.getNameOfImplementationClass());
						ComponentAlleleParameter[] capArray = new ComponentAlleleParameter[1];
						double min;
						double max;
						double def;
						for (SubproblemParameter sbpp : spd
								.getListOfParameters()) {
							min = Double.parseDouble(sbpp.getMinValue());
							max = Double.parseDouble(sbpp.getMaxValue());
							def = Double.parseDouble(sbpp.getDefaultValue());
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
		// components.add(new ComponentAllele(FTestNumerical.class.getName(),
		// new ComponentAlleleParameter(0, 0.5, 0.05, ComponentType.Double), new
		// ComponentAlleleParameter(0, 1, 0, ComponentType.Integer), new
		// ComponentAlleleParameter(0, 1, 0.3, ComponentType.Double)));
		// components.add(new
		// ComponentAllele(ChiSquareTestCategorical.class.getName(), new
		// ComponentAlleleParameter(0, 0.5, 0.05, ComponentType.Double), new
		// ComponentAlleleParameter(0, 1, 0,ComponentType.Integer), new
		// ComponentAlleleParameter(0, 1, 0.3, ComponentType.Double)));
		// components.add(new ComponentAllele("RIA"));
		components.add(new ComponentAllele(null));
		components.add(new ComponentAllele(null));

		setAvailableAlleles(components);
	}

}
