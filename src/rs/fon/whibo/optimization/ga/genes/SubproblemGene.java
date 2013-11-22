package rs.fon.whibo.optimization.ga.genes;

import java.util.List;

import org.jgap.BaseGene;
import org.jgap.Configuration;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.UnsupportedRepresentationException;

import rs.fon.whibo.optimization.ga.genes.ComponentAlleleParameter.ComponentType;

public abstract class SubproblemGene extends BaseGene {

	private static final long serialVersionUID = -650881297751843965L;

	private ComponentAllele allele;
	private static ComponentAllele staticAllele;
	private List<ComponentAllele> availableAlleles;
	private static boolean mutateComponents = true;
	private static boolean mutateParameters = false;
	private static double componentMutationRate = 1;
	private static double parametersMutationRate = 1;

	public static void shouldMutateComponents(boolean mutate) {
		mutateComponents = mutate;
	}

	public static void setComponentMutationRate(double rate) {
		componentMutationRate = rate;
	}

	public static void setParameterMutationRate(double rate) {
		parametersMutationRate = rate;
	}

	public static void shouldMutateParameters(boolean mutate) {
		if (mutate) {
			mutateParameters = true;
			componentMutationRate = 0.2;
		} else {
			mutateParameters = false;
			componentMutationRate = 1;
		}
	}

	public SubproblemGene(Configuration a_configuration)
			throws InvalidConfigurationException {
		super(a_configuration);
		setupAvailableAlleles();
	}

	@Override
	protected Object getInternalValue() {
		return allele;
	}

	@Override
	public void applyMutation(int index, double a_percentage) {

		RandomGenerator random = getConfiguration().getRandomGenerator();

		if (mutateComponents) {
			if (random.nextDouble() <= componentMutationRate) {
				int componentIndex = random.nextInt(availableAlleles.size());
				allele = availableAlleles.get(componentIndex);
			}
		}
		if (mutateParameters) {
			for (ComponentAlleleParameter par : allele.getParameters()) {
				if (random.nextDouble() <= parametersMutationRate) {
					if (par.type == ComponentType.Double)
						par.value = par.min + random.nextDouble()
								* (par.max - par.min);
					if (par.type == ComponentType.Integer)
						par.value = par.min
								+ random.nextInt((int) (par.max - par.min + 1));
				}
			}
		}

	}

	@Override
	public String getPersistentRepresentation()
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValueFromPersistentRepresentation(String a_representation)
			throws UnsupportedOperationException,
			UnsupportedRepresentationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAllele(Object value) {
		allele = (ComponentAllele) value;
	}

	public ComponentAllele getComponentAllele() {
		return allele;
	}

	@Override
	public void setToRandomValue(RandomGenerator generator) {
		int componentIndex = generator.nextInt(availableAlleles.size());
		allele = availableAlleles.get(componentIndex);

		if (mutateParameters) {
			for (ComponentAlleleParameter p : allele.getParameters()) {
				if (p.type == ComponentType.Double)
					p.value = p.min + generator.nextDouble() * (p.max - p.min);
				if (p.type == ComponentType.Integer)
					p.value = p.min
							+ generator.nextInt((int) (p.max - p.min + 1));
			}
		}
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static void setStaticAllele(ComponentAllele staticAllele) {
		SubproblemGene.staticAllele = staticAllele;
	}

	public static ComponentAllele getStaticAllele() {
		return staticAllele;
	}

	public boolean shouldMutateComponents() {
		return mutateComponents;
	}

	public boolean shouldMutateParameters() {
		return mutateParameters;
	}

	protected abstract void setupAvailableAlleles();

	public void setAvailableAlleles(List<ComponentAllele> availableAlleles) {
		this.availableAlleles = availableAlleles;
	}

	public List<ComponentAllele> getAvailableAlleles() {
		return availableAlleles;
	}

	public String toString() {
		return allele.toString();
	}

}
