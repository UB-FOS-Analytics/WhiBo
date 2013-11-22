package rs.fon.whibo.GDT.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import rs.fon.whibo.GDT.dataset.SplittedExampleSet;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Statistics;
import com.rapidminer.example.set.Partition;
import com.rapidminer.operator.learner.tree.Edge;
import com.rapidminer.operator.learner.tree.SplitCondition;
import com.rapidminer.operator.learner.tree.Tree;

public class PruningTools {

	public static void recalculateTreeNodesStatistics(Tree tree,
			ExampleSet pruneSet) {

		// count statistics for this node
		tree.getCounterMap().clear();
		Attribute labelAttribute = pruneSet.getAttributes().getLabel();
		pruneSet.recalculateAttributeStatistics(labelAttribute);
		for (String labelValue : labelAttribute.getMapping().getValues()) {
			int count = (int) pruneSet.getStatistics(pruneSet.getAttributes()
					.getLabel(), Statistics.COUNT, labelValue);
			tree.addCount(labelValue, count);
		}

		// count statistics recursively for child nodes
		if (!tree.isLeaf()) {

			// split dataset into subTree branches
			int[] branching = new int[pruneSet.size()];
			Iterator<Edge> branchIterator = tree.childIterator();
			int branchIndex = -1;

			while (branchIterator.hasNext()) {
				branchIndex++;
				SplitCondition branchCondition = branchIterator.next()
						.getCondition();

				for (int exampleIndex = 0; exampleIndex < pruneSet.size(); exampleIndex++) {

					// TODO: hardcoded representation of missing values!
					// Refactor this!

					// checks if it it a missing value
					if (Double
							.isNaN(pruneSet
									.getExample(exampleIndex)
									.getValue(
											pruneSet.getAttributes()
													.get(branchCondition
															.getAttributeName())))) {
						if (pruneSet.getAttributes()
								.get(branchCondition.getAttributeName())
								.isNumerical())
							branching[exampleIndex] = 0;
						else
							branching[exampleIndex] = 0;

					} else if (branchCondition.test(pruneSet
							.getExample(exampleIndex)))
						branching[exampleIndex] = branchIndex;
				}
			}

			Partition partition = new Partition(branching, branchIndex + 1);
			SplittedExampleSet splittedES = new SplittedExampleSet(pruneSet,
					partition);

			// for each branch, recursively call statistics calculation
			branchIterator = tree.childIterator();
			branchIndex = -1;
			while (branchIterator.hasNext()) {
				Tree subTree = branchIterator.next().getChild();
				branchIndex++;
				splittedES.selectSingleSubset(branchIndex);
				recalculateTreeNodesStatistics(subTree, splittedES);
			}
		}
	}

	public static void recalculateTreeLeavesStatistics(Tree tree,
			ExampleSet pruneSet) {

		// count statistics for this leaf
		if (tree.isLeaf()) {
			tree.getCounterMap().clear();
			Attribute labelAttribute = pruneSet.getAttributes().getLabel();
			pruneSet.recalculateAttributeStatistics(labelAttribute);
			for (String labelValue : labelAttribute.getMapping().getValues()) {
				int count = (int) pruneSet.getStatistics(pruneSet
						.getAttributes().getLabel(), Statistics.COUNT,
						labelValue);
				tree.addCount(labelValue, count);
			}
		}

		// count statistics recursively for child nodes
		if (!tree.isLeaf()) {

			// split dataset into subTree branches
			int[] branching = new int[pruneSet.size()];
			Iterator<Edge> branchIterator = tree.childIterator();
			int branchIndex = -1;

			while (branchIterator.hasNext()) {
				branchIndex++;
				SplitCondition branchCondition = branchIterator.next()
						.getCondition();

				for (int exampleIndex = 0; exampleIndex < pruneSet.size(); exampleIndex++) {

					// TODO: hardcoded representation of missing values!
					// Refactor this!

					// checks if it it a missing value
					if (Double
							.isNaN(pruneSet
									.getExample(exampleIndex)
									.getValue(
											pruneSet.getAttributes()
													.get(branchCondition
															.getAttributeName())))) {
						if (pruneSet.getAttributes()
								.get(branchCondition.getAttributeName())
								.isNumerical())
							branching[exampleIndex] = 0;
						else
							branching[exampleIndex] = branchIndex;
					}

					else if (branchCondition.test(pruneSet
							.getExample(exampleIndex)))
						branching[exampleIndex] = branchIndex;
				}
			}

			Partition partition = new Partition(branching, branchIndex + 1);
			SplittedExampleSet splittedES = new SplittedExampleSet(pruneSet,
					partition);

			// for each branch, recursively call statistics calculation
			branchIterator = tree.childIterator();
			branchIndex = -1;
			while (branchIterator.hasNext()) {
				Tree subTree = branchIterator.next().getChild();
				branchIndex++;
				splittedES.selectSingleSubset(branchIndex);
				recalculateTreeLeavesStatistics(subTree, splittedES);
			}
		}
	}

	public static void setAllNodeLabels(Tree tree) {

		String label = predictedLabel(tree);
		tree.setLeaf(label);

		if (!tree.isLeaf()) {

			Iterator<Edge> i = tree.childIterator();
			while (i.hasNext()) {
				Tree child = i.next().getChild();
				setAllNodeLabels(child);
			}
		}
	}

	public static void setAllLeafLabels(Tree tree) {

		if (tree.isLeaf()) {
			String label = predictedLabel(tree);
			tree.setLeaf(label);
		}
		if (!tree.isLeaf()) {

			Iterator<Edge> i = tree.childIterator();
			while (i.hasNext()) {
				Tree child = i.next().getChild();
				setAllLeafLabels(child);
			}
		}
	}

	public static String predictedLabel(Tree currentNode) {
		String result = null;
		int maxFrequency = 0;
		Map<String, Integer> counterMap = currentNode.getCounterMap();
		for (String s : counterMap.keySet()) {
			if (counterMap.get(s) > maxFrequency) {
				maxFrequency = counterMap.get(s);
				result = s;
			}
		}
		if (result == null)
			return currentNode.getLabel();
		else
			return result;
	}

	/**
	 * Deep-copy of Serializable objects
	 */
	public static <T extends Serializable> T deepCopy(T original) {
		T clone = null;
		try {
			// Increased buffer size to speed up writing
			ByteArrayOutputStream bos = new ByteArrayOutputStream(5120);
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(original);
			out.flush();
			out.close();

			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
			clone = (T) in.readObject();

			in.close();
			bos.close();

			return clone;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}

		return null;
	}
}
