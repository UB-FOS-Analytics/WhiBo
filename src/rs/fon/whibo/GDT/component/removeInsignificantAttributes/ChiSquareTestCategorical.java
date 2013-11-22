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

import org.apache.commons.math.MathException;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.inference.ChiSquareTestImpl;

import rs.fon.whibo.GDT.tools.Tools;
import rs.fon.whibo.problem.Parameter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;

/**
 * This component removes "insignificant" categorical attributes by measuring if
 * there are significant differences in values of a categorical attribute with
 * respect to label attribute (class). As an input it takes Numerical
 * attributes, label attribute and significance threshold as parameter for
 * evaluation of attribute significance.
 * 
 * @componentName Chi Square Test Categorical
 */
public class ChiSquareTestCategorical extends
		AbstractRemoveInsignificantAtributes {

	/**
	 * Instantiates a new chi square test categorical.
	 * 
	 * @param parameters
	 *            the user passed parameters
	 */
	public ChiSquareTestCategorical(
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
	@Override
	public LinkedList<Attribute> removeAttributes(ExampleSet exampleSet,
			LinkedList<Attribute> attributesForSplitting) {

		// checks if the example set is pure, and if it is, it exits the method
		Attribute label = exampleSet.getAttributes().getLabel();
		if (Tools.getAllCategories(exampleSet, label).size() < 2)
			return attributesForSplitting;

		// selects the attributes to be evaluated for removal (by calculating
		// chi-square probability for each attribute)
		ArrayList<Attribute> attributesToRemove = new ArrayList<Attribute>();
		ArrayList<Double> attributeProbabilities = new ArrayList<Double>();
		for (Attribute attr : attributesForSplitting)
			if (attr.isNominal()) {
				// calculate chi-square probability of the attribute
				double probability = 0;
				try {
					long[][] matrixForAttribute = getContigencyTable(
							exampleSet, attr);
					ChiSquareTestImpl chiTest = new ChiSquareTestImpl();
					probability = chiTest.chiSquareTest(matrixForAttribute);
				} catch (MathException me) {
					// System.out.println("Error in calculating math formula (chiTest)");
				}
				// add the attribute to the list
				attributesToRemove.add(attr);
				attributeProbabilities.add(new Double(probability));
			}

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
	 * Gets the contigency table of the (attribute, label) pair.
	 * 
	 * @param exampleSet
	 *            the dataset
	 * @param attribute
	 *            the attribute
	 * 
	 * @return the contigency table
	 */
	private long[][] getContigencyTable(ExampleSet exampleSet,
			Attribute attribute) {
		Attribute label = exampleSet.getAttributes().getLabel();
		List<String> labelCategories = null;
		List<String> attributeCategories = null;

		labelCategories = Tools.getAllCategories(exampleSet, label);
		attributeCategories = Tools.getAllCategories(exampleSet, attribute);

		int numberOfLabelCategories = labelCategories.size();
		int numberOfAttributeCategories = attributeCategories.size();

		long[][] matrix = new long[numberOfAttributeCategories][numberOfLabelCategories];

		for (int i = 0; i < numberOfAttributeCategories; i++)
			for (int j = 0; j < numberOfLabelCategories; j++) {
				matrix[i][j] = 0;

				for (Example example : exampleSet) {
					if (labelCategories.get(j).equals(
							example.getNominalValue(label))
							&& attributeCategories.get(i).equals(
									example.getNominalValue(attribute)))
						matrix[i][j] += 1;
				}
			}

		return matrix;
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
