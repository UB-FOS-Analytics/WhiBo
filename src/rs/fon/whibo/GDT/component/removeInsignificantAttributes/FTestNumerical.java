/*
 *  WhiBo
 *
 *  Copyright (C) 2010- by WhiBo development team and the contributors
 *
 *  Complete list of developers available at our web site:
 *
 *       http://www.whibo.fon.bg.ac.rs
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package rs.fon.whibo.GDT.component.removeInsignificantAttributes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.inference.OneWayAnova;
import org.apache.commons.math.stat.inference.OneWayAnovaImpl;

import rs.fon.whibo.GDT.tools.Tools;
import rs.fon.whibo.problem.Parameter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;

/**
 * This component removes "insignificant" numerical attributes by measuring if
 * there are significant differences in values of a numerical attribute with
 * respect to label attribute (class). As an input it takes Numerical
 * attributes, label attribute and significance threshold as parameter for
 * evaluation of attribute significance.
 * 
 * @author Milan Vukicevic, Milos Jovanovic
 * @componentName F-Test Numerical
 */
public class FTestNumerical extends AbstractRemoveInsignificantAtributes {

	/**
	 * Instantiates a new f test numerical.
	 * 
	 * @param parameters
	 *            the parameters
	 */
	public FTestNumerical(
			List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
		this.Alpha_Value = Double.parseDouble(parameters.get(0)
				.getXenteredValue());
		this.Use_Percentage_Instead = Integer.parseInt((parameters.get(1)
				.getXenteredValue()));
		this.Percentage_Remove = Double.parseDouble(parameters.get(2)
				.getXenteredValue());

	}

	/**
	 * The Alpha significance value for chi-square test used to decide on the
	 * removal of attributes.
	 */
	@Parameter(defaultValue = "0.05", maxValue = "0.5", minValue = "0.0")
	private Double Alpha_Value;

	/**
	 * Flag that indicates that the percentage parameter should be used instead
	 * of the alpha significance parameter
	 */
	@Parameter(defaultValue = "0", maxValue = "1", minValue = "0")
	private int Use_Percentage_Instead;

	/** Percentage of the attributes that should be removed by this component */
	@Parameter(defaultValue = "0.4", maxValue = "1.0", minValue = "0.0")
	private Double Percentage_Remove;

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.whibo.GDT.component.removeInsignificantAttributes.
	 * RemoveInsignificantAtributes#DoWork(com.rapidminer.example.ExampleSet,
	 * int, java.util.LinkedList)
	 */
	public LinkedList<Attribute> removeAttributes(ExampleSet exampleSet,
			LinkedList<Attribute> attributesForSplitting) {
		// checks if the example set is pure, and if it is, it exits the method
		Attribute label = exampleSet.getAttributes().getLabel();
		if (Tools.getAllCategories(exampleSet, label).size() < 2)
			return attributesForSplitting;

		// selects the attributes to be evaluated for removal (by calculating
		// F-test probability for each attribute)
		ArrayList<Attribute> attributesToRemove = new ArrayList<Attribute>();
		ArrayList<Double> attributeProbabilities = new ArrayList<Double>();
		for (Attribute attr : attributesForSplitting)
			if (attr.isNumerical()) {
				// calculate F-test probability of the attribute
				double probability = 0;
				try {

					OneWayAnova fTest = new OneWayAnovaImpl();
					List<double[]> paramForFTest = getArraysByLabel(exampleSet,
							attr);

					// tests if no arrays for f-test has fewer that 2 elements
					boolean fTestImpossible = false;
					for (double[] i : paramForFTest)
						if (i.length < 2)
							fTestImpossible = true;

					// calculates ftest probability
					if (!fTestImpossible)
						probability = fTest.anovaPValue(paramForFTest);

				} catch (Exception e) {
					// System.out.println("Error in calculating math formula (FTest)");
				}
				// add the attribute to the list
				attributesToRemove.add(attr);
				attributeProbabilities.add(new Double(probability));
			}

		if (attributesToRemove.size() == 0)
			return attributesForSplitting;

		// calculates the percentile of the required percentage. Percentile
		// variable in code represents the percentage of attributes to be kept
		// (not removed)
		double percentile;
		DescriptiveStatistics stat = new DescriptiveStatistics();
		for (Double d : attributeProbabilities)
			stat.addValue(d.doubleValue());
		percentile = stat.getPercentile((1 - Percentage_Remove) * 100);

		// evaluates attributes and chooses the ones for removal (actually saves
		// the ones not for removal)
		Iterator<Attribute> iattr = attributesToRemove.iterator();
		Iterator<Double> iprob = attributeProbabilities.iterator();
		while (iattr.hasNext()) {
			iattr.next();
			Double prob = iprob.next();
			if (Use_Percentage_Instead == 0) {
				if (prob <= Alpha_Value) {
					iattr.remove();
					iprob.remove();
				}
			} else {
				if (prob <= percentile) {
					iattr.remove();
					iprob.remove();
				}
			}
		}

		// removes the attributes
		for (Attribute attr : attributesToRemove)
			attributesForSplitting.remove(attr);
		return attributesForSplitting;

	}

	/**
	 * Gets the arrays of numerical attribute values for each class label
	 * separately.
	 * 
	 * @param exampleSet
	 *            the example set
	 * @param attribute
	 *            the numerical attribute
	 * 
	 * @return the arrays of values for each label
	 */
	private List<double[]> getArraysByLabel(ExampleSet exampleSet,
			Attribute attribute) {

		Attribute label = exampleSet.getAttributes().getLabel();
		LinkedList<double[]> arrays = new LinkedList<double[]>();
		LinkedList<String> labelList = Tools
				.getAllCategories(exampleSet, label);

		int size = labelList.size();

		for (int i = 0; i < size; i++) {
			String nLabel = labelList.get(i);
			double[] arrayForF = new double[getClassLabelCount(exampleSet,
					nLabel)];
			Iterator<Example> reader = exampleSet.iterator();
			int k = 0;
			while (reader.hasNext()) {
				Example example = reader.next();
				String currentValue = example.getValueAsString(label);
				if (currentValue.equals(nLabel)) {
					Double value = Double.parseDouble(example
							.getValueAsString(attribute));
					arrayForF[k] = value.doubleValue();
					k++;
				}

			}

			arrays.add(arrayForF);

		}
		return arrays;

	}

	/**
	 * Gets the number of examples that have the supplied class label value.
	 * 
	 * @param exampleSet
	 *            the example set
	 * @param label
	 *            the label value to match
	 * 
	 * @return the example count
	 */
	private int getClassLabelCount(ExampleSet exampleSet, String label) {
		Iterator<Example> reader = exampleSet.iterator();
		int attributeCount = 0;
		while (reader.hasNext()) {
			Example example = reader.next();
			String currentValue = example.getValueAsString(exampleSet
					.getAttributes().getLabel());
			if (currentValue.equals(label))

				attributeCount++;

		}
		return attributeCount;
	}

	/**
	 * Gets collection of class names of components that are not compatible with
	 * this component
	 * 
	 * @return collection of class names of not compatible components
	 */
	public String[] getNotCompatibleClassNames() {
		return null;
	}

	/**
	 * Gets collection of class names of components which are exclusive to this
	 * component
	 * 
	 * @return collection of class names of exclusive components
	 */
	public String[] getExclusiveClassNames() {
		return null;
	}

}
