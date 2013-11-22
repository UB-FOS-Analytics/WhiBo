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
package rs.fon.whibo.GDT.treeModel;

import java.util.LinkedList;

import rs.fon.whibo.integration.interfaces.ISplitCondition;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.operator.learner.tree.AbstractSplitCondition;
import com.rapidminer.tools.Tools;

/**
 * A split condition for nominal values (equals).
 * 
 * @author Ingo Mierswa
 * @version $Id: NominalSplitCondition.java,v 1.6 2007/11/30 16:57:03
 *          ingomierswa Exp $
 */
public class NominalSplitCondition extends AbstractSplitCondition implements
		ISplitCondition {

	private static final long serialVersionUID = 3883155435836330171L;

	private int[] valuesList;
	private LinkedList<String> valuesStringList;

	public NominalSplitCondition(Attribute attribute, String valueString) {
		super(attribute.getName());
		attribute.getMapping().getIndex(valueString);
	}

	public NominalSplitCondition(Attribute attribute,
			LinkedList<String> allCategoryList) {
		super(attribute.getName()); // nepotrebno, ziveo zbudz

		this.valuesStringList = allCategoryList;
		this.valuesList = new int[valuesStringList.size()];
		for (int i = 0; i < allCategoryList.size(); i++) {
			int map = attribute.getMapping().getIndex(valuesStringList.get(i));

			this.valuesList[i] = map;
		}

	}

	public String getRelation() {
		return "=";
	}

	public String getValueString() {
		// return this.valueString;
		String value = new String();
		for (int i = 0; i < valuesStringList.size(); i++) {
			value += valuesStringList.get(i) + " ";
		}
		return value;
	}

	public boolean test(Example example) {

		double currentValue;
		boolean equal = false;

		for (int i = 0; i < valuesList.length; i++) {
			currentValue = example.getValue(example.getAttributes().get(
					getAttributeName()));
			if (Tools.isEqual(currentValue, valuesList[i]))
				equal = true;

		}

		return equal;
	}

}
