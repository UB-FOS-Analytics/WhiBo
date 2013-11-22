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
import rs.fon.whibo.GDT.tools.CombinationGenerator;
import rs.fon.whibo.GDT.tools.Tools;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.Partition;

/**
 * 
 * This component groups categories of a categorical attribute in two parts. All
 * possible combinations of rearranging categories in two parts can be produced
 * by this component. It represents a computationally demanding way to split
 * categorical attributes. This component, however, can influence producing more
 * accurate splits.
 * 
 * This component can be found in decision tree CART (Breiman 1984)
 * 
 * Breiman L, Friedman J, Olshen R (1984) Classification and Regression Trees,
 * Chapman & Hall publisher.
 * 
 * @author Milos Jovanovic, Milan Vukicevic
 * @componentName Binary Categorical
 */
public class BinaryCategorical extends AbstractPossibleSplit {

	/** Current attribute for candidate split creation. */
	Attribute currentAttribute;

	/**
	 * The combination generator that keeps state of combinations in a current
	 * attribute.
	 */
	CombinationGenerator combinationGenerator;

	/** Current size of combination */
	int sizeOfCombination;

	/** The max size of combination for partition creation. */
	int maxSizeOfCombination;

	/**
	 * Instantiates a binary numerical component for candidate split creation.
	 * 
	 * @param parameters
	 *            - this method takes empty parameter list.
	 */
	public BinaryCategorical(List<SubproblemParameter> parameters) {
		super(parameters);
	}

	/**
	 * Inits the component with example set in current node and the attributes
	 * that are available for creation of splitting candidates. (some attributes
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
		sizeOfCombination = 0;
		maxSizeOfCombination = -1;
		combinationGenerator = new CombinationGenerator();
	}

	/**
	 * Checks if there are more splits in list of partitions for attribute, or
	 * if there are more categorical attributes.
	 * 
	 * @return true, if successful
	 */
	@Override
	public boolean hasNextSplit() {

		if (combinationGenerator.hasMore())
			return true;
		else if (sizeOfCombination < maxSizeOfCombination)
			return true;

		// if there are more more categorical attributes
		ListIterator<Attribute> i = attributesForSplitting
				.listIterator(iteratorAtt.nextIndex());

		while (i.hasNext()) {
			if (i.next().isNominal())
				return true;
		}

		return false;

	}

	/**
	 * Next split that will be evaluated in split evaluation subproblem
	 * 
	 * @return SplittedExampleSet - splitting candidate for evaluation
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public SplittedExampleSet nextSplit() throws Exception {

		LinkedList<String> combination = getCombination();
		Partition partition = getPartition(combination);
		SplittedExampleSet split = Split(partition);
		return split;
	}

	/**
	 * Creates binary categorical splitting candidate for evaluation.
	 * 
	 * @param currentSplit
	 *            - dataset in current node that will be splitted
	 * @param attribute
	 *            - attribute for splitting
	 */
	private SplittedExampleSet Split(Partition partition) {
		currentSplit.setPartition(partition);
		currentSplit.setAttribute(currentAttribute);
		currentSplit.setCategories(Tools.getAllCategories(currentSplit,
				currentAttribute));
		return currentSplit;
	}

	/**
	 * Gets the combination.
	 * 
	 * @return the combination
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private LinkedList<String> getCombination() throws Exception {
		LinkedList<String> categories;

		// checks if there is more combinations in Combination Generator
		if (combinationGenerator.hasMore()) {
			categories = getAllCategories(currentSplit, currentAttribute);
			LinkedList<String> combination = new LinkedList<String>();
			int[] indices;
			// Creates all possible combinations of categories
			indices = combinationGenerator.getNext();
			for (int i = 0; i < indices.length; i++) {
				int index = indices[i];
				combination.add(categories.get(index));
			}
			return combination;
		}
		// if all sizes of combinations are not created
		else if (sizeOfCombination < maxSizeOfCombination) {
			categories = getAllCategories(currentSplit, currentAttribute);
			sizeOfCombination++;
			combinationGenerator = new CombinationGenerator(categories.size(),
					sizeOfCombination);
			return getCombination();
		}
		// if there is more attributes for combination generation
		else if (hasNextSplit()) {
			currentAttribute = iteratorAtt.next();
			while (!currentAttribute.isNominal()) {
				currentAttribute = iteratorAtt.next();
			}
			categories = getAllCategories(currentSplit, currentAttribute);
			maxSizeOfCombination = (int) Math.floor((categories.size() / 2)); // izbegava
																				// ponavaljanje
																				// kombinacija
																				// kategorija
			sizeOfCombination = 0;
			return getCombination();
		}
		// if there is no more attributes for split creation and procedure is
		// called
		else
			throw new Exception("no more available splits");

	}

	/**
	 * Gets the partition generated from combination.
	 * 
	 * @param combination
	 *            of categories
	 * 
	 * @return the partition
	 */
	private Partition getPartition(LinkedList<String> combination) {

		int[] elements;

		Iterator<Example> reader = currentSplit.iterator();
		elements = new int[currentSplit.size()];
		int i = 0;
		while (reader.hasNext()) {
			Example example = reader.next();
			String currentValue = example.getValueAsString(currentAttribute);
			if (combination.contains(currentValue))
				elements[i] = 1;
			i++;

		}

		Partition partition = new Partition(elements, 2);
		return partition;

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

		while (reader.hasNext()) {
			Example example = reader.next();
			String currentValue = example.getValueAsString(attribute);
			if (!allCategoryList.contains(currentValue))
				allCategoryList.add(currentValue);
		}

		return allCategoryList;
	}

	/**
	 * Checks if split is categorical.
	 * 
	 * @return true, if split is categorical.
	 */
	@Override
	public boolean isCategoricalSplit() {
		return true;
	}

	/**
	 * Checks if is numerical split.
	 * 
	 * @return true, split is numerical
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
