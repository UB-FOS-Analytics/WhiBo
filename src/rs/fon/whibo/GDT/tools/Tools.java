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
package rs.fon.whibo.GDT.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.learner.tree.Edge;
import com.rapidminer.operator.learner.tree.Tree;

/**
 * Tools for data and model manipulation.
 * 
 * @author Milan Vukicevic, Milos Jovanovic
 */
public class Tools {

	/**
	 * Gets the all different categories from example set for current attribute.
	 * 
	 * @param exampleSet
	 *            the example set
	 * @param attribute
	 *            the attribute
	 * 
	 * @return all different categories
	 */
	public static LinkedList<String> getAllCategories(ExampleSet exampleSet,
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
	 * Calculates factorial of a number.
	 * 
	 * @param n
	 * 
	 * @return long
	 */
	public static long factorial(long n) {
		if (n < 0)
			throw new RuntimeException("Underflow error in factorial");
		else if (n > 20)
			throw new RuntimeException("Overflow error in factorial");
		else if (n == 0)
			return 1;
		else
			return n * factorial(n - 1);
	}

	/**
	 * Changes tree node to leaf.
	 * 
	 * @param node
	 *            for creating leaf
	 */
	public static void treeToLeaf(Tree node) {

		if (!node.isLeaf()) {
			Map<String, Integer> labelFrequencies = getLabelFrequencies(node);

			// detect best label value
			String bestLabel = null;
			int bestLabelFrequency = 0;
			for (String s : labelFrequencies.keySet()) {
				if (labelFrequencies.get(s) >= bestLabelFrequency) {
					bestLabel = s;
					bestLabelFrequency = labelFrequencies.get(s);
				}
			}

			// convert tree node to leaf
			node.removeChildren();
			node.setLeaf(bestLabel);
			for (String s : labelFrequencies.keySet()) {
				node.addCount(s, labelFrequencies.get(s));
			}
		}
	}

	/**
	 * Calculates frequencies of label categories.
	 * 
	 * @param node
	 *            for calculating frequecies of label categories
	 * 
	 * @return frequencies of label categories
	 */
	private static Map<String, Integer> getLabelFrequencies(Tree node) {

		if (node.isLeaf()) {
			return node.getCounterMap();
		} else {
			Map<String, Integer> labelFrequencies = new HashMap<String, Integer>();
			Iterator<Edge> childIterator = node.childIterator();
			while (childIterator.hasNext()) {
				Tree childNode = childIterator.next().getChild();
				Map<String, Integer> childLabelFrequencies = getLabelFrequencies(childNode);

				// add childLabelFreq into labelFreq
				for (String s : childLabelFrequencies.keySet()) {
					if (labelFrequencies.containsKey(s)) {
						int oldValue = labelFrequencies.get(s);
						labelFrequencies.remove(s);
						labelFrequencies.put(s, oldValue
								+ childLabelFrequencies.get(s));
					} else
						labelFrequencies.put(s, childLabelFrequencies.get(s));
				}
			}
			return labelFrequencies;
		}
	}

	/**
	 * Clone serializable object.
	 * 
	 * @param object
	 *            for cloning
	 * 
	 * @return cloned object
	 */
	public static Object cloneSerializable(Serializable obj) {
		ObjectOutputStream out = null;
		ObjectInputStream in = null;

		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bout);

			out.writeObject(obj);
			out.close();

			ByteArrayInputStream bin = new ByteArrayInputStream(
					bout.toByteArray());
			in = new ObjectInputStream(bin);
			Object copy = in.readObject();

			in.close();

			return copy;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}

				if (in != null) {
					in.close();
				}
			} catch (IOException ignore) {
			}
		}

		return null;
	}
}
