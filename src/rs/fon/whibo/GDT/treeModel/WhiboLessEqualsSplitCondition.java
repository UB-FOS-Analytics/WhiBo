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

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.tools.Tools;

/**
 * Implements the Less or Equals than condition used by the TreeModel.
 */
public class WhiboLessEqualsSplitCondition extends WhiboAbstractSplitCondition {

	/** The value where the split occurs. */
	private double value;

	/**
	 * Instantiates a new whibo less equals split condition.
	 * 
	 * @param attribute
	 *            the attribute
	 * @param value
	 *            the split value
	 */
	public WhiboLessEqualsSplitCondition(Attribute attribute, double value) {
		super(attribute.getName());
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.whibo.integration.interfaces.ISplitCondition#getRelation()
	 */
	@Override
	public String getRelation() {
		return "<=";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rs.fon.whibo.integration.interfaces.ISplitCondition#getValueString()
	 */
	@Override
	public String getValueString() {
		return Tools.formatIntegerIfPossible(this.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.whibo.integration.interfaces.ISplitCondition#test(com.rapidminer
	 * .example.Example)
	 */
	@Override
	public boolean test(Example example) {
		return example
				.getValue(example.getAttributes().get(getAttributeName())) <= value;
	}

}
