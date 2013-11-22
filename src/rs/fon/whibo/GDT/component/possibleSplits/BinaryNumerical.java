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
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.Partition;
import com.rapidminer.example.set.SortedExampleSet;

/**
 * 
 * This component divides a numerical attribute in two parts, < and >= from a
 * specific value. The split produced by this component are therefore binary. It
 * represents the easiest way to split numerical attributes. For now only this
 * component is provided for splitting numerical attributes. This component can
 * be found in decision tree CART (Breiman 1984) C4.5 (Quinlan 1993).
 * 
 * Breiman L, Friedman J, Olshen R (1984) Classification and Regression Trees,
 * Chapman & Hall publisher. Quinlan JR (1986) Induction of decision trees,
 * Machine Learning. Quinlan JR (1993) C4.5 Programs for Machine Learning,
 * Morgan Kaufmann.
 * 
 * 
 * @author Nikola
 * @componentName Binary Numerical
 */
public class BinaryNumerical extends AbstractPossibleSplit {

	/**
	 * List of partitions that define all possible ways for splitting dataset in
	 * current node.
	 */
	LinkedList<Partition> allPartitionsForAttribute;

	/**
	 * List of values that defines all split points for splitting dataset in
	 * current node..
	 */
	LinkedList<Double> splitValueList;

	/** Current attribute for candidate split creation. */
	Attribute currentAttribute;

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
		allPartitionsForAttribute = new LinkedList<Partition>();
		splitValueList = new LinkedList<Double>();
		currentAttribute = null;
	}

	/**
	 * Instantiates a binary numerical component for candidate split creation.
	 * 
	 * @param parameters
	 *            - this method takes empty parameter list.
	 */
	public BinaryNumerical(List<SubproblemParameter> parameters) {
		super(parameters);
	}

	/**
	 * Checks if there are more splits in list of partitions for attribute, or
	 * if there are more numerical attributes.
	 * 
	 * @return true, if successful
	 * 
	 */
	@Override
	public boolean hasNextSplit() {

		// if there are more possible splits in generated partitions for an
		// attribute
		if (!allPartitionsForAttribute.isEmpty())
			return true;

		// if there are more numerical attributes
		ListIterator<Attribute> i = attributesForSplitting
				.listIterator(iteratorAtt.nextIndex());

		while (i.hasNext()) {
			if (i.next().isNumerical())
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

		if (!hasNextSplit()) {
			return null;
		}

		if (allPartitionsForAttribute.isEmpty()) {

			currentAttribute = iteratorAtt.next();
			while (!currentAttribute.isNumerical()) {
				currentAttribute = iteratorAtt.next();
			}
			getPartitionsForAttribute(currentSplit, currentAttribute);
			Split(currentSplit, currentAttribute);

		} else
			Split(currentSplit, currentAttribute);

		return currentSplit;
	}

	/**
	 * Creates binary numerical splitting candidate for evaluation.
	 * 
	 * @param currentSplit
	 *            - dataset in current node that will be splitted
	 * @param attribute
	 *            - attribute for splitting
	 */
	private void Split(SplittedExampleSet currentSplit, Attribute attribute) {
		currentSplit.setPartition(allPartitionsForAttribute.removeFirst());
		currentSplit.setAttribute(attribute);
		currentSplit.setSplitValue(splitValueList.removeFirst().doubleValue());
	}

	/**
	 * Creates all possible partitions for current numerical attribute
	 * 
	 * @param inputSet
	 *            - dataset in current node
	 * @param attribute
	 *            - the attribute for splitting
	 * 
	 */
	private void getPartitionsForAttribute(ExampleSet inputSet,
			Attribute attribute) {
		SortedExampleSet exampleSet = new SortedExampleSet(
				(ExampleSet) inputSet, attribute, SortedExampleSet.INCREASING);
		Attribute labelAttribute = exampleSet.getAttributes().getLabel();
		double oldLabel = Double.NaN;
		double lastValue = Double.NaN;
		;
		int counter = -1;

		for (Example e : exampleSet) {
			counter++;
			double currentValue = e.getValue(attribute);
			double label = e.getValue(labelAttribute);
			if (Double.isNaN(lastValue))
				lastValue = currentValue;
			if ((Double.isNaN(oldLabel))
					|| ((oldLabel != label) && (lastValue != currentValue))) {
				double splitValue = (lastValue + currentValue) / 2.0d;
				Partition partition = splitByAttribute(inputSet, attribute,
						splitValue);
				splitValueList.add(splitValue);
				allPartitionsForAttribute.add(partition);
			}
			lastValue = currentValue;
			oldLabel = label;
		}

	}

	/**
	 * Creates one partition for current attribute and split point.
	 * 
	 * @param exampleSet
	 *            - example set in current node
	 * @param attribute
	 *            - current numerical attribute for candidate split creation
	 * @param value
	 *            - split point value
	 * 
	 * @return Partition - one partition for attribute for a given split point
	 */
	private Partition splitByAttribute(ExampleSet exampleSet,
			Attribute attribute, double value) {
		int[] elements = new int[exampleSet.size()];
		Iterator<Example> reader = exampleSet.iterator();
		int i = 0;

		while (reader.hasNext()) {
			Example example = reader.next();
			double currentValue = example.getValue(attribute);
			if (currentValue <= value)
				elements[i++] = 0;
			else
				elements[i++] = 1;
		}
		return new Partition(elements, 2);

	}

	/**
	 * Checks if split is categorical.
	 * 
	 * @return true, if is categorical split
	 */
	@Override
	public boolean isCategoricalSplit() {
		return false;
	}

	/**
	 * Checks if split is numerical.
	 * 
	 * @return true, if is numerical split
	 */
	@Override
	public boolean isNumericalSplit() {
		return true;
	}

	/**
	 * Gets collection of class names of components that are not compatible with
	 * this component
	 * 
	 * @return collection of class names of not compatible components
	 */
	public String[] getNotCompatibleClassNames() {
		return new String[] {
		// MinLeafSize.class.getName(),
		// FTestNumerical.class.getName(),
		// GiniIndex.class.getName(),
		// TreeDepth.class.getName(),
		};
	}

	/**
	 * Gets collection of class names of components which are exclusive to this
	 * component
	 * 
	 * @return collection of class names of exclusive components
	 */
	public String[] getExclusiveClassNames() {
		return new String[] {
		// ChiSquareTestCategorical.class.getName(),
		// InformationGain.class.getName(),
		// DistanceMeasure.class.getName(),
		// TreeDepth.class.getName(),
		// PessimisticError.class.getName(),
		};
	}
}
