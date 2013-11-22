package rs.fon.whibo.optimization.ga.genes;

public class ComponentAlleleParameter {

	public double max;
	public double min;
	public double value;
	public ComponentType type;

	public enum ComponentType {
		Double, Integer
	}

	public ComponentAlleleParameter(double min, double max,
			double defaultValue, ComponentType type) {
		super();
		this.max = max;
		this.min = min;
		this.type = type;
		this.value = defaultValue;
	}

}
