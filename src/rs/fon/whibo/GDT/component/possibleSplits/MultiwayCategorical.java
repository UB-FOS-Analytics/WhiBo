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
import rs.fon.whibo.GDT.tools.Tools;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.Partition;

/**
 * The component produces split candidates for categorical attributes that have
 * as many leaves as there are categories in an attribute. It represents a
 * computationally effective way of splitting categorical attributes.
 * 
 * This component can be found in decision trees ID3 Quinlan JR (1986) and C4.5
 * Quinlan JR (1993).
 * 
 * Quinlan JR (1986) Induction of decision trees, Machine Learning. Quinlan JR
 * (1993) C4.5 Programs for Machine Learning, Morgan Kaufmann.
 * 
 * @componentName Multiway Categorical
 */
public class MultiwayCategorical extends AbstractPossibleSplit {

	/** Current attribute for candidate split creation. */
	Attribute currentAttribute;

	/**
	 * Instantiates a multiway categorical component for candidate split
	 * creation.
	 * 
	 * @param parameters
	 *            - this method takes empty parameter list.
	 */
	public MultiwayCategorical(List<SubproblemParameter> parameters) {
		super(parameters);
	}

	/**
	 * Inits the component with example set in current node and the attributes
	 * that are available for creation of splitting candidates. When candidate
	 * split is created on the bassis of one attribute, that attribute is
	 * removed from a list. Also, some attributes could be eliminated with
	 * remove insignificant attributes component).
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
	}

	/**
	 * Checks if there are more categorical attributes for candidate split
	 * creation.
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
	 * Next split that will be evaluated in split evaluation subproblem
	 * 
	 * @return SplittedExampleSet - splitting candidate for evaluation
	 */
	@Override
	public SplittedExampleSet nextSplit() {

		if (!hasNextSplit())
			return null;

		currentAttribute = iteratorAtt.next();
		while (!currentAttribute.isNominal()) {
			currentAttribute = iteratorAtt.next();
		}
		Split(currentSplit, currentAttribute);
		return currentSplit;

	}

	/**
	 * Creates categorical multiway splitting candidate for evaluation .
	 * 
	 * @param currentSplit
	 *            - dataset in current node that will be splitted
	 * @param attribute
	 *            - the attribute for splitting
	 */
	private void Split(SplittedExampleSet currentSplit, Attribute attribute) {
		int[] elements = new int[currentSplit.size()];

		LinkedList<String> categories = getAllCategories(currentSplit,
				attribute);
		int numOfSplits = categories.size();

		Iterator<Example> reader = currentSplit.iterator();
		int i = 0;

		while (reader.hasNext()) {
			Example example = reader.next();
			String currentValue = example.getValueAsString(attribute);

			for (int k = 0; k < categories.size(); k++) {
				if (currentValue.equals(categories.get(k).toString()))
					elements[i] = k;

			}

			i++;

		}
		Partition partition = new Partition(elements, numOfSplits);

		currentSplit.setPartition(partition);
		currentSplit.setAttribute(attribute);
		currentSplit.setCategories(Tools.getAllCategories(currentSplit,
				attribute));

	}

	/**
	 * Gets all categories from categorical attribute.
	 * 
	 * @param exampleSet
	 *            - dataset in current node
	 * @param attribute
	 *            - the categorical attribute
	 * 
	 * @return the all categories
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
