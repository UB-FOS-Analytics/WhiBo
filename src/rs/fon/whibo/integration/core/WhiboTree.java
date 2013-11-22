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
package rs.fon.whibo.integration.core;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rs.fon.whibo.integration.adapters.example.ExampleSetAdapter;
import rs.fon.whibo.integration.interfaces.ISplitCondition;

public class WhiboTree implements Serializable {

	private static final long serialVersionUID = -194816772953290021L;

	private String label = null;

	private List<WhiboTreeEdge> children = new LinkedList<WhiboTreeEdge>();

	private Map<String, Integer> counterMap = new LinkedHashMap<String, Integer>();

	private transient ExampleSetAdapter trainingSet = null;

	public WhiboTree(ExampleSetAdapter trainingSet) {
		this.trainingSet = trainingSet;
	}

	public ExampleSetAdapter getTrainingSet() {
		return this.trainingSet;
	}

	public void addCount(String className, int count) {
		counterMap.put(className, count);
	}

	public int getCount(String className) {
		Integer count = counterMap.get(className);
		if (count == null)
			return 0;
		else
			return count;
	}

	public int getFrequencySum() {
		int sum = 0;
		for (Integer i : counterMap.values()) {
			sum += i;
		}
		return sum;
	}

	public int getSubtreeFrequencySum() {
		if (children.size() == 0) {
			return getFrequencySum();
		} else {
			int sum = 0;
			for (WhiboTreeEdge edge : children) {
				sum += edge.getChild().getSubtreeFrequencySum();
			}
			return sum;
		}
	}

	public Map<String, Integer> getCounterMap() {
		return counterMap;
	}

	public void setLeaf(String label) {
		this.label = label;
	}

	public void addChild(WhiboTree child, ISplitCondition condition) {
		this.children.add(new WhiboTreeEdge(child, condition));
		Collections.sort(this.children);
	}

	public void removeChildren() {
		this.children.clear();
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}

	public String getLabel() {
		return this.label;
	}

	public Iterator<WhiboTreeEdge> childIterator() {
		return children.iterator();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		toString("", this, "", buffer);
		return buffer.toString();
	}

	private void toString(String conditionString, WhiboTree tree,
			String indent, StringBuffer buffer) {
		if (conditionString.isEmpty()) {
			buffer.append(conditionString);
		}
		if (!tree.isLeaf()) {
			Iterator<WhiboTreeEdge> childIterator = tree.childIterator();
			while (childIterator.hasNext()) {
				buffer.append(System.getProperty("line.separator"));
				buffer.append(indent);
				WhiboTreeEdge edge = childIterator.next();
				toString(edge.getCondition().toString(), edge.getChild(),
						indent + "|   ", buffer);
			}
		} else {
			buffer.append(": ");
			buffer.append(tree.getLabel());
			buffer.append(" " + tree.counterMap.toString());
		}
	}

}
