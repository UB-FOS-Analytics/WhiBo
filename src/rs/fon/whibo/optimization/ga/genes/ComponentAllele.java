package rs.fon.whibo.optimization.ga.genes;

import java.util.LinkedList;

import rs.fon.whibo.optimization.ga.genes.ComponentAlleleParameter.ComponentType;
import rs.fon.whibo.problem.Subproblem;

public class ComponentAllele {

	private String implementationClassName;
	private ComponentAlleleParameter[] parameters;
	private boolean isNull;

	public ComponentAllele(String implementationClassName,
			ComponentAlleleParameter... parameters) {
		if (implementationClassName == null)
			isNull = true;
		else
			isNull = false;
		this.setImplementationClassName(implementationClassName);
		this.setParameters(parameters);
	}

	public Subproblem createSubproblem() throws Exception {
		throw new Exception();
	}

	public void setImplementationClassName(String implementationClassName) {
		this.implementationClassName = implementationClassName;
	}

	public String getImplementationClassName() {
		return implementationClassName;
	}

	public void setParameters(ComponentAlleleParameter[] parameters) {
		this.parameters = parameters;
	}

	public ComponentAlleleParameter[] getParameters() {
		return parameters;
	}

	public String[] getParameterValues() {
		LinkedList<String> paramValues = new LinkedList<String>();

		for (ComponentAlleleParameter p : parameters) {
			if (p.type == ComponentType.Double)
				paramValues.add(String.valueOf(p.value));
			if (p.type == ComponentType.Integer)
				paramValues.add(String.valueOf((int) p.value));
		}

		return (String[]) paramValues.toArray(new String[paramValues.size()]);

	}

	public String toString() {
		if (isNull())
			return "null";
		String result = "";
		String componentName = implementationClassName.split("\\.")[implementationClassName
				.split("\\.").length - 1];
		result = result + componentName;
		if (parameters.length > 0) {
			result = result + "(";
			for (ComponentAlleleParameter p : parameters) {
				if (p.type == ComponentType.Double)
					result = result + String.valueOf(p.value);
				if (p.type == ComponentType.Integer)
					result = result + String.valueOf((int) p.value);
				result = result + "; ";
			}
			if (result.contains(";"))
				result = result.substring(0, result.length() - 2);
			result = result + ")";
		}
		return result;
	}

	public boolean isNull() {
		return isNull;
	}
}
