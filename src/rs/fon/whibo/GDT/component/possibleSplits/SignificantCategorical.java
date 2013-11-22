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
package rs.fon.whibo.GDT.component.possibleSplits;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import rs.fon.whibo.GDT.tools.ChiSquareTest;
import rs.fon.whibo.GDT.tools.CombinationGenerator;
import rs.fon.whibo.problem.Parameter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.Partition;

/**
 * This component groups similar categories into mergers that can produce
 * significant splits. Number of branches produced by this component can be
 * between two (like with binary categorical posible splits) and the number of
 * categories in attribute (like in multiway categorical possibel split).
 * 
 * This split evaluation measure is used in the CHAID algorithm. Kass GV (1980)
 * An Exploratory Technique for Investigating Large Quantities of Categorical
 * Data, Applied Statistics.
 * 
 * @author Milan Vukicevic, Milos Jovanovic
 * @componentName Significant Categorical
 */
public class SignificantCategorical extends AbstractPossibleSplit {

	/**
	 * The Merge_ alpha_ value for testing the differences between simple
	 * categories.
	 */
	@Parameter(defaultValue = "0.050", minValue = "0", maxValue = "1")
	private Double Merge_Alpha_Value;

	/** The Split_ alpha_ value for testing the similarity in merged categories. */
	@Parameter(defaultValue = "0.049", minValue = "0", maxValue = "1")
	private Double Split_Alpha_Value;

	/** Current attribute for candidate split creation. */
	Attribute currentAttribute;

	/** Current partition for attribute. */
	Partition currentPartitionForAttribute;

	/** List of final categories after merging and splitting. */
	LinkedList<String> finalCategories;

	/**
	 * Instantiates a significant categorical component for candidate split
	 * creation.
	 * 
	 * @param parameters
	 *            - this method two parametars: The Merge_ alpha_ value and The
	 *            Split_ alpha_ value.
	 */
	public SignificantCategorical(
			List<rs.fon.whibo.problem.SubproblemParameter> parameters) {
		super(parameters);
		Merge_Alpha_Value = Double.parseDouble(parameters.get(0)
				.getXenteredValue());
		Split_Alpha_Value = Double.parseDouble(parameters.get(1)
				.getXenteredValue());
	}

	/**
	 * Inits the component with example set in current node and the attributes
	 * that are available for creation of splitting candidates. Some attributes
	 * could be eliminated with remove insignificant attributes component).
	 * 
	 * @param exampleSet
	 *            - the dataset in current node that will be splitted
	 * @param attributesForSplitting
	 *            - list of attributes that are awailable for creating splitting
	 *            candidate
	 */
	@Override
	public void init(ExampleSet exampleSet,
			List<Attribute> attributesForSplitting) {
		super.init(exampleSet, attributesForSplitting);
		currentAttribute = null;
		finalCategories = null;

	}

	/**
	 * Checks if there are more splits in generated partitions for attribute, or
	 * if there are more numerical attributes.
	 * 
	 * @return true, if successful
	 */
	@Override
	public boolean hasNextSplit() {

		ListIterator<Attribute> i = attributesForSplitting
				.listIterator(iteratorAtt.nextIndex());

		while (i.hasNext()) {
			if (i.next().isNominal())
				return true;
		}

		return false;
	}

	/**
	 * Checks if there are more categorical attributes for candidate split
	 * creation.
	 * 
	 * @return the splitted example set
	 */
	@Override
	public SplittedExampleSet nextSplit() {

		if (!hasNextSplit()) {
			return null;
		} else {
			currentAttribute = iteratorAtt.next();
			while (!currentAttribute.isNominal()) {
				currentAttribute = iteratorAtt.next();
			}

			getOptimalPartition(currentSplit, currentAttribute,
					Merge_Alpha_Value, Split_Alpha_Value);
			Split(currentSplit, currentAttribute, finalCategories);
			return currentSplit;
		}

	}

	/**
	 * Creates categorical significant splitting candidate for evaluation .
	 * 
	 * @param currentSplit
	 *            - dataset in current node that will be splitted
	 * @param attribute
	 *            - the attribute for splitting
	 * @param finalCategories
	 *            - list of simple and merged categories for splitting
	 */
	private void Split(SplittedExampleSet currentSplit, Attribute attribute,
			LinkedList<String> finalCategories) {
		currentSplit.setPartition(currentPartitionForAttribute);
		currentSplit.setAttribute(attribute);
		currentSplit.setCategories(finalCategories);
	}

	/**
	 * Creates combinations of specific size of categories for current
	 * attribute.
	 * 
	 * @param categories
	 *            - list of categories of current attribute
	 * @param combinationSize
	 *            - number of categories for generating combinations
	 * 
	 * @return allCombinations - all combinations of categories of current
	 *         attribute
	 */
	public LinkedList<LinkedList<String>> getAllCombinations(
			LinkedList<String> categories, int combinationSize) {

		LinkedList<LinkedList<String>> allCombinations = new LinkedList<LinkedList<String>>();
		int index;
		int[] indices;
		CombinationGenerator x = new CombinationGenerator(categories.size(),
				combinationSize);

		while (x.hasMore()) {
			LinkedList<String> combination = new LinkedList<String>();
			indices = x.getNext();
			for (int i = 0; i < indices.length; i++) {
				index = indices[i];
				combination.add(categories.get(index));
			}
			allCombinations.add(combination);
		}
		return allCombinations;
	}

	/**
	 * Creates all possible combinations of categories for current attribute.
	 * 
	 * @param categories
	 *            - list of categories of current attribute
	 * 
	 * @return allCombinations - all combinations of categories of current
	 *         attribute
	 */
	public LinkedList<LinkedList<String>> getAllCombinations(
			LinkedList<String> categories) {

		LinkedList<LinkedList<String>> allCombinations = new LinkedList<LinkedList<String>>();
		int index;
		int[] indices;
		int maxSizeOfCombination = (int) Math.floor((categories.size() / 2));
		for (int k = 1; k <= maxSizeOfCombination; k++) {
			CombinationGenerator x = new CombinationGenerator(
					categories.size(), k);

			while (x.hasMore()) {
				LinkedList<String> combination = new LinkedList<String>();
				indices = x.getNext();
				for (int i = 0; i < indices.length; i++) {
					index = indices[i];
					combination.add(categories.get(index));
				}
				allCombinations.add(combination);
			}
		}
		return allCombinations;
	}

	/**
	 * Gets p values for every combination of attributes.
	 * 
	 * @param allCombinations
	 *            - all combinations of categories of current attribute
	 * @param categories
	 *            - list of categories for current attribute
	 * @param exampleSet
	 *            - the dataset in current node that will be splitted
	 * @param attribute
	 *            - current attribute for candidate split creation
	 * 
	 * @return pValues - an array of p values for every generated combination of
	 *         categories
	 */
	public LinkedList<Double> getAllPValues(
			LinkedList<LinkedList<String>> allCombinations,
			LinkedList<String> categories, SplittedExampleSet exampleSet,
			Attribute attribute) {

		double pValue = 0;
		LinkedList<Double> pValues = new LinkedList<Double>();

		for (int i = 0; i < allCombinations.size(); i++) {

			ChiSquareTest chi = new ChiSquareTest(exampleSet,
					allCombinations.get(i), attribute);
			pValue = chi.getProbability();
			pValues.add(new Double(pValue));

		}

		return pValues;
	}

	/**
	 * Gets significant partition for current node.
	 * 
	 * @param exampleSet
	 *            - the example set of current node
	 * @param attribute
	 *            - current categorical attribute for candidate split creation
	 * @param insignificanceParameter
	 *            - bound for merging categories
	 * @param significanceParameter
	 *            - bound for splitting categories
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void getOptimalPartition(SplittedExampleSet exampleSet,
			Attribute attribute, double insignificanceParameter,
			double significanceParameter) {

		int[] elements = new int[exampleSet.size()];

		String newCategory = null;
		LinkedList<String> categories = getAllCategories(exampleSet, attribute);

		// keeps all combinations of two categories
		LinkedList<LinkedList<String>> allCombinations = new LinkedList();
		// keeps p values for every combination of categories with respect to
		// label attribute
		LinkedList<Double> pValues = new LinkedList<Double>();

		// indication of merging or splitting. If both of these variables are
		// set to false in one iteration, component finishes search for
		// significant categories
		boolean concatenation;
		boolean division;

		do {
			LinkedList<String> concatCategories = new LinkedList<String>();

			// this loop searches for unsignificant combinations of categories
			// and merges them if p value is higher than significance parameter

			do {
				newCategory = null;
				pValues = new LinkedList<Double>();
				allCombinations = new LinkedList<LinkedList<String>>();

				if (categories.size() > 2) {
					allCombinations = getAllCombinations(categories, 2);
					pValues = getAllPValues(allCombinations, categories,
							exampleSet, attribute);
					newCategory = concatInsignificantCategories(pValues,
							allCombinations, insignificanceParameter);

					concatenation = false;

					if (newCategory != null) {

						concatCategories = sinchronizeCategories(newCategory,
								concatCategories);
						concatenation = true;

					} else
						concatCategories = null;

					if (concatenation) {

						categories = sinchronizeCategories(newCategory,
								categories);

					}
				} else
					concatenation = false;

			} while (concatenation);

			division = false;

			// if there are some complex categories that are merged from 3 or
			// more simple categories
			// algorithm tries to divide these categories and checks if that
			// division is
			// statistically significant with respect to class attribute.
			if (concatCategories != null) {
				for (int g = 0; g < concatCategories.size(); g++) {
					if (getSizeOfComplexCategory(concatCategories.get(g)) >= 3) {
						LinkedList<String> newCategories = divideComplexCategory(concatCategories
								.get(g));
						LinkedList<LinkedList<String>> simpleCombinations = getAllCombinations(newCategories);
						LinkedList<LinkedList<String>> realCombinations = getRealCombinations(
								concatCategories.get(g), simpleCombinations);

						LinkedList<Double> pValues1 = getAllPValues(
								realCombinations, newCategories, exampleSet,
								attribute);

						LinkedList<String> splittedCategories = concatSignificantCategories(
								concatCategories.get(g), pValues1,
								realCombinations, categories,
								significanceParameter);
						if (splittedCategories != null) {
							categories = splittedCategories;
							division = true;
						}

					}
				}

			}

		}

		while (division);

		// Sets partition for creation of branch when there is no more merging
		// or splitting

		Iterator<Example> reader = exampleSet.iterator();
		int i = 0;
		while (reader.hasNext()) {
			Example example = reader.next();
			String currentValue = example.getValueAsString(attribute);
			elements[i] = getIndexOfStringValue(currentValue, categories);
			i++;
		}

		Partition partition = new Partition(elements, categories.size());

		if (!concatenation)
			finalCategories = categories;
		currentPartitionForAttribute = partition;

	}

	/**
	 * Merges categories that have no significant differnces with a respect to
	 * output attribute.
	 * 
	 * @param pValues
	 *            - list of p values for every pair of categories
	 * @param allCombinations
	 *            - all possible combinations (two by two categories)
	 * @param insignificanceParameter
	 *            - parameter for merging categories
	 * 
	 * @return the string
	 */
	public String concatInsignificantCategories(LinkedList<Double> pValues,
			LinkedList<LinkedList<String>> allCombinations,
			double insignificanceParameter) {
		double maxValue = getMaxValue(pValues);

		String multiCategory = new String();
		if (maxValue > insignificanceParameter) {
			int index = getIndexOfValue(maxValue, pValues);
			LinkedList<String> listOfCategories = allCombinations.get(index);

			for (int k = 0; k < listOfCategories.size(); k++) {
				multiCategory += listOfCategories.get(k) + ":";
			}
			multiCategory = multiCategory.substring(0,
					multiCategory.lastIndexOf(':'));
			return multiCategory;
		} else

			return null;
	}

	/**
	 * Splits complex categorie, if some combination of sub-categories are
	 * statisticaly significant with respect to label attribute.
	 * 
	 * @param multiCategory
	 *            - complex category for splitting
	 * @param pValues
	 *            - an array of p values for every combination of two categories
	 * @param allCombinations
	 *            - list of all combinations of simple categories. Every
	 *            combination is represented as an array
	 * @param allCategories
	 *            - list of current categories
	 * @param significanceParameter
	 *            - parameter for splitting
	 * 
	 * @return list of new categories
	 */
	public LinkedList<String> concatSignificantCategories(String multiCategory,
			LinkedList<Double> pValues,
			LinkedList<LinkedList<String>> allCombinations,
			LinkedList<String> allCategories, double significanceParameter) {
		double minValue = getMinValue(pValues);
		String mergeCombination = new String();

		if (minValue < significanceParameter) {
			int index = getIndexOfValue(minValue, pValues);
			LinkedList<String> listOfCategories = allCombinations.get(index);

			for (int k = 0; k < listOfCategories.size(); k++) {
				mergeCombination += listOfCategories.get(k) + ":";
			}
			mergeCombination = mergeCombination.substring(0,
					mergeCombination.lastIndexOf(':'));

			allCategories.remove(multiCategory);

			allCategories.addAll(listOfCategories);

			return allCategories;
		} else

			return null;
	}

	/**
	 * Gets the real combinations.
	 * 
	 * @param complexCategory
	 *            the complex category
	 * @param simpleCombinations
	 *            the simple combinations
	 * 
	 * @return the real combinations
	 */
	private LinkedList<LinkedList<String>> getRealCombinations(
			String complexCategory,
			LinkedList<LinkedList<String>> simpleCombinations) {
		LinkedList<LinkedList<String>> realCombinations = new LinkedList<LinkedList<String>>();

		String combinationPart;
		String otherPart;

		for (int i = 0; i < simpleCombinations.size(); i++) {
			LinkedList<String> newCombination = new LinkedList<String>();
			combinationPart = concatCategories(simpleCombinations.get(i));
			otherPart = getDifferenceBetweenComplexCategories(complexCategory,
					combinationPart);
			newCombination.add(combinationPart);
			newCombination.add(otherPart);
			realCombinations.add(newCombination);

		}

		return realCombinations;

	}

	/**
	 * Merges more simple categories into one complex categorie. Merged simple
	 * categories are separated with caracter ':'
	 * 
	 * @param categories
	 *            - categories for merging
	 * 
	 * @return String - complex categorie
	 */
	public String concatCategories(LinkedList<String> categories) {
		String mergeCategory = new String();
		for (int k = 0; k < categories.size(); k++) {
			mergeCategory += categories.get(k) + ":";
		}
		mergeCategory = mergeCategory.substring(0,
				mergeCategory.lastIndexOf(':'));

		return mergeCategory;
	}

	/**
	 * Gets the difference between two complex categories.
	 * 
	 * @param cat1
	 *            - first complex categorie
	 * @param cat2
	 *            - second complex categorie
	 * 
	 * @return String - the difference between complex categories
	 */
	public String getDifferenceBetweenComplexCategories(String cat1, String cat2) {
		// divides complex categories into an arrays of simple categories
		LinkedList<String> categories1 = divideComplexCategory(cat1);
		LinkedList<String> categories2 = divideComplexCategory(cat2);

		String difference = new String();

		// removes simple categories that are contained in both arrays of
		// categories
		if (categories1.size() > categories2.size()) {
			categories1.removeAll(categories2);
			for (int k = 0; k < categories1.size(); k++) {
				difference += categories1.get(k) + ":";
			}
			difference = difference.substring(0, difference.lastIndexOf(':'));
		} else {
			categories2.removeAll(categories1);
			for (int k = 0; k < categories2.size(); k++) {
				difference += categories2.get(k) + ":";
			}
			difference = difference.substring(0, difference.lastIndexOf(':'));

		}

		return difference;
	}

	/**
	 * Splits complex category into an array of simple categories.
	 * 
	 * @param complexCategory
	 *            - string created by merging of simple categories
	 * 
	 * @return list of simple categories
	 */
	public LinkedList<String> divideComplexCategory(String complexCategory) {
		String[] categories = complexCategory.split(":");
		LinkedList<String> categoryList = new LinkedList<String>();
		for (int i = 0; i < categories.length; i++)
			categoryList.add(categories[i]);
		return categoryList;

	}

	/**
	 * Removes categories from a list of categories if they are included in
	 * complex category.
	 * 
	 * @param complexCategory
	 *            - new complex category
	 * @param categories
	 *            - current categories
	 * 
	 * @return sinchronized list of categories
	 */
	private LinkedList<String> sinchronizeCategories(String complexCategory,
			LinkedList<String> categories) {
		LinkedList<String> returnList = new LinkedList<String>();
		// list of simple categories contained in complex category
		LinkedList<String> dividedComplexCategory = divideComplexCategory(complexCategory);

		for (int i = 0; i < categories.size(); i++) {
			String cat1 = categories.get(i);
			if (cat1.lastIndexOf(":") != -1) {
				LinkedList<String> helpList = divideComplexCategory(cat1);
				if (!dividedComplexCategory.containsAll(helpList))

					returnList.add(cat1);
			} else {
				if (!dividedComplexCategory.contains(cat1))
					returnList.add(cat1);
			}
		}
		returnList.add(complexCategory);
		return returnList;

	}

	/**
	 * Gets the size of complex category.
	 * 
	 * @param complexCategory
	 *            - string created by merging of simple categories
	 * 
	 * @return size - number of simple categories contained in complex category
	 */
	public int getSizeOfComplexCategory(String complexCategory) {

		String[] categories = complexCategory.split(":");

		return categories.length;
	}

	/**
	 * Gets the all categories from categorical attribute.
	 * 
	 * @param exampleSet
	 *            - the example set in current node
	 * @param attribute
	 *            - current attribute
	 * 
	 * @return list of all different categories
	 */
	public LinkedList<String> getAllCategories(ExampleSet exampleSet,
			Attribute attribute) {
		LinkedList<String> allCategoryList = new LinkedList<String>();
		Iterator<Example> reader = exampleSet.iterator();
		// goes through example set and gets different categories of an
		// attribute
		while (reader.hasNext()) {
			Example example = reader.next();
			String currentValue = example.getValueAsString(attribute);
			if (!inList(currentValue, allCategoryList))
				allCategoryList.add(currentValue);
		}

		return allCategoryList;
	}

	/**
	 * Checks if category is contained in list of current categories (both
	 * complex and simple).
	 * 
	 * @param category
	 * @param allCategoryList
	 *            - list of current categories
	 * 
	 * @return true, if successful
	 */
	private boolean inList(String category, List<String> allCategoryList) {

		LinkedList<String> helpList = new LinkedList<String>();
		helpList.addAll(allCategoryList);
		LinkedList<String> dividedCategories = new LinkedList<String>();
		// divides all complex categories into an array of simple categories
		for (int i = 0; i < helpList.size(); i++) {

			if (allCategoryList.get(i).lastIndexOf(":") != -1) {
				dividedCategories.addAll(divideComplexCategory(allCategoryList
						.get(i)));

			}

		}
		helpList.addAll(dividedCategories);

		// checks if category is contained in the list of categories
		for (int j = 0; j < helpList.size(); j++) {
			if (helpList.get(j).equals(category))

				return true;
		}

		return false;
	}

	/**
	 * Gets the max value from the list of p values.
	 * 
	 * @param pValues
	 *            - current list of p values
	 * 
	 * @return max value
	 */
	private double getMaxValue(LinkedList<Double> pValues) {
		double maxValue = pValues.get(0).doubleValue();

		for (int i = 0; i < pValues.size(); i++) {
			if (maxValue < pValues.get(i).doubleValue())
				maxValue = pValues.get(i).doubleValue();
		}

		return maxValue;
	}

	/**
	 * Gets the min value from the list of p values.
	 * 
	 * @param Values
	 *            - current list of p values
	 * 
	 * @return min value
	 */
	private double getMinValue(LinkedList<Double> pValues) {
		double minValue = pValues.get(0).doubleValue();

		for (int i = 0; i < pValues.size(); i++) {
			if (minValue > pValues.get(i).doubleValue())
				minValue = pValues.get(i).doubleValue();
		}

		return minValue;
	}

	/**
	 * Gets the index of value.
	 * 
	 * @param value
	 * @param pValues
	 *            - current list of p values
	 * 
	 * @return the index of value
	 */
	private int getIndexOfValue(double value, LinkedList<Double> pValues) {
		int returnIndex = 0;

		for (int j = 0; j < pValues.size(); j++) {
			if (value == pValues.get(j).doubleValue())
				returnIndex = j;
		}
		return returnIndex;
	}

	/**
	 * Gets the index of string value.
	 * 
	 * @param value
	 * @param categories
	 *            - list of current categories
	 * 
	 * @return the index of string value
	 */
	private int getIndexOfStringValue(String value,
			LinkedList<String> categories) {

		int returnIndex = 0;

		for (int j = 0; j < categories.size(); j++) {

			if (categories.get(j).lastIndexOf(":") != -1) {
				LinkedList<String> complexList = divideComplexCategory(categories
						.get(j));

				for (int k = 0; k < complexList.size(); k++) {
					if (complexList.get(k).equals(value))
						returnIndex = j;
				}

			}

			else {
				if (categories.get(j).equals(value))
					returnIndex = j;
			}
		}
		return returnIndex;
	}

	/**
	 * Checks if split is categorical.
	 * 
	 * @return true, if is categorical split
	 */
	@Override
	public boolean isCategoricalSplit() {
		return true;
	}

	/**
	 * Checks if split is numerical.
	 * 
	 * @return true, if is numerical split
	 */
	@Override
	public boolean isNumericalSplit() {
		return false;
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
