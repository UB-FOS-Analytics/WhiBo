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
package rs.fon.whibo.GDT.dataset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.IndexBasedExampleSetReader;
import com.rapidminer.example.set.Partition;
import com.rapidminer.example.set.PartitionBuilder;
import com.rapidminer.example.set.ShuffledPartitionBuilder;
import com.rapidminer.example.set.SimplePartitionBuilder;
import com.rapidminer.example.set.StratifiedPartitionBuilder;
import com.rapidminer.example.table.ExampleTable;

public class SplittedExampleSet extends
		com.rapidminer.example.set.AbstractExampleSet {

	private static final long serialVersionUID = 4573262969007377183L;

	private Attribute attribute;
	private double splitValue;
	private LinkedList<String> categories = new LinkedList<String>();

	/** Indicates a non-shuffled sampling for partition building. */
	public static final String[] SAMPLING_NAMES = { "linear sampling",
			"shuffled sampling", "stratified sampling" };

	/** Indicates a non-shuffled sampling for partition building. */
	public static final int LINEAR_SAMPLING = 0;

	/** Indicates a shuffled sampling for partition building. */
	public static final int SHUFFLED_SAMPLING = 1;

	/** Indicates a stratified shuffled sampling for partition building. */
	public static final int STRATIFIED_SAMPLING = 2;

	/** The partition. */
	private Partition partition;

	/** The parent example set. */
	private ExampleSet parent;

	/** Constructs a SplittedExampleSet with the given partition. */
	public SplittedExampleSet(ExampleSet exampleSet, Partition partition) {
		this.parent = (ExampleSet) exampleSet; // .clone(); //OK/
		this.partition = partition;

	}

	/**
	 * Creates an example set that is splitted into two subsets using the given
	 * sampling type.
	 */
	public SplittedExampleSet(ExampleSet exampleSet, double splitRatio,
			int samplingType, int seed) {
		this(exampleSet, new double[] { splitRatio, 1 - splitRatio },
				samplingType, seed);
	}

	/**
	 * Creates an example set that is splitted into n subsets with the given
	 * sampling type.
	 */
	public SplittedExampleSet(ExampleSet exampleSet, double[] splitRatios,
			int samplingType, int seed) {
		this(exampleSet, new Partition(splitRatios, exampleSet.size(),
				createPartitionBuilder(exampleSet, samplingType, seed)));
	}

	/**
	 * Creates an example set that is splitted into <i>numberOfSubsets</i> parts
	 * with the given sampling type.
	 */
	public SplittedExampleSet(ExampleSet exampleSet, int numberOfSubsets,
			int samplingType, int seed) {
		this(exampleSet, new Partition(numberOfSubsets, exampleSet.size(),
				createPartitionBuilder(exampleSet, samplingType, seed)));
	}

	/** Clone constructor. */
	@SuppressWarnings("unchecked")
	public SplittedExampleSet(SplittedExampleSet exampleSet) {
		this.parent = (ExampleSet) exampleSet.parent.clone();
		this.partition = (Partition) exampleSet.partition.clone();
		this.attribute = (Attribute) exampleSet.attribute;
		this.splitValue = exampleSet.splitValue;
		this.categories = (LinkedList<String>) exampleSet.categories.clone();

	}

	public boolean equals(Object o) {
		if (!super.equals(o))
			return false;
		if (!(o instanceof SplittedExampleSet))
			return false;
		return this.partition.equals(((SplittedExampleSet) o).partition);
	}

	public int hashCode() {
		return super.hashCode() ^ partition.hashCode();
	}

	/** Creates the partition builder for the given sampling type. */
	private static PartitionBuilder createPartitionBuilder(
			ExampleSet exampleSet, int samplingType, int seed) {
		PartitionBuilder builder = null;
		switch (samplingType) {
		case LINEAR_SAMPLING:
			builder = new SimplePartitionBuilder();
			break;
		case SHUFFLED_SAMPLING:
			builder = new ShuffledPartitionBuilder(true, seed);
			break;
		case STRATIFIED_SAMPLING:
		default:
			Attribute label = exampleSet.getAttributes().getLabel();
			if ((label != null) && (label.isNominal()))
				builder = new StratifiedPartitionBuilder(exampleSet, true, seed);
			else {
				exampleSet
						.getLog()
						.logNote(
								"Example set has no nominal label: using shuffled partition instead of stratified partition!");
				builder = new ShuffledPartitionBuilder(true, seed);
			}
			break;
		}
		return builder;
	}

	/** Adds the given subset. */
	public void selectAdditionalSubset(int index) {
		partition.selectSubset(index);
	}

	/** Selects exactly one subset. */
	public void selectSingleSubset(int index) {
		partition.clearSelection();
		partition.selectSubset(index);
	}

	/** Selects all but one subset. */
	public void selectAllSubsetsBut(int index) {
		partition.clearSelection();
		for (int i = 0; i < partition.getNumberOfSubsets(); i++) {
			if (i != index)
				partition.selectSubset(i);
		}
	}

	/** Selects all subsets. */
	public void selectAllSubsets() {
		partition.clearSelection();
		for (int i = 0; i < partition.getNumberOfSubsets(); i++) {
			partition.selectSubset(i);
		}
	}

	/** Inverts the current selection */
	public void invertSelection() {
		partition.invertSelection();
	}

	/** Clears the current selection */
	public void clearSelection() {
		partition.clearSelection();
	}

	/** Returns the number of subsets. */
	public int getNumberOfSubsets() {
		return partition.getNumberOfSubsets();
	}

	/**
	 * Returns an example reader that splits all examples that are not selected.
	 */
	public Iterator<Example> iterator() {
		return new IndexBasedExampleSetReader(this);
	}

	public int size() {
		return partition.getSelectionSize();
	}

	/**
	 * Searches i-th example in the currently selected partition. This is done
	 * in constant time.
	 */
	public Example getExample(int index) {
		int actualIndex = partition.mapIndex(index);
		return this.parent.getExample(actualIndex);
	}

	/** Returns the index of the example in the parent example set. */
	public int getActualParentIndex(int index) {
		return partition.mapIndex(index);
	}

	public ExampleTable getExampleTable() {
		return parent.getExampleTable();
	}

	public Attributes getAttributes() {
		return this.parent.getAttributes();
	}

	// -------------------- Factory methods --------------------

	/**
	 * Works only for nominal and integer attributes. If <i>k</i> is the number
	 * of different values, this method splits the example set into <i>k</i>
	 * subsets according to the value of the given attribute.
	 */

	/**
	 * Works only for real-value attributes. Returns an example set splitted
	 * into two parts containing all examples providing a greater (smaller)
	 * value for the given attribute than the given value. The first partition
	 * contains all examples providing a smaller or the same value than the
	 * given one.
	 */

	public double getSplitValue() {
		return splitValue;
	}

	public void setSplitValue(double splitValue) {
		this.splitValue = splitValue;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public LinkedList<String> getAllCategories(Attribute attribute) {
		LinkedList<String> allCategoryList = new LinkedList<String>();

		Iterator<Example> reader = this.iterator();

		while (reader.hasNext()) {
			Example example = reader.next();
			String currentValue = example.getValueAsString(attribute);
			if (!inList(currentValue, allCategoryList))
				allCategoryList.add(currentValue);
		}

		// return new SplittedExampleSet(exampleSet, partition);
		return allCategoryList;
	}

	public boolean inList(String category, List<String> allCategoryList) {

		for (int i = 0; i < allCategoryList.size(); i++) {
			if (allCategoryList.get(i).equals(category))
				return true;

		}

		return false;
	}

	public static SplittedExampleSet splitByAttribute(ExampleSet exampleSet,
			Attribute attribute) {
		int[] elements = new int[exampleSet.size()];
		int i = 0;
		Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
		AtomicInteger currentIndex = new AtomicInteger(0);
		for (Example example : exampleSet) {
			int value = (int) example.getValue(attribute);
			Integer indexObject = indexMap.get(value);
			if (indexObject == null) {
				indexMap.put(value, currentIndex.getAndIncrement());
			}
			int intValue = indexMap.get(value).intValue();
			elements[i++] = intValue;
		}

		int maxNumber = indexMap.size();
		indexMap.clear();
		Partition partition = new Partition(elements, maxNumber);
		return new SplittedExampleSet(exampleSet, partition);
	}

	public static SplittedExampleSet splitByAttribute(ExampleSet exampleSet,
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
		Partition partition = new Partition(elements, 2);
		return new SplittedExampleSet(exampleSet, partition);
	}

	public void setPartition(Partition p) {
		this.partition = p;
	}

	public void setCategories(LinkedList<String> cat) {
		this.categories = cat;
	}

	public LinkedList<String> getCategories() {
		return this.categories;
	}

}
