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

import rs.fon.whibo.integration.interfaces.ISplitCondition;

/**
 * The Abstract Split Condition that is used by the TreeModel to define the
 * splits within the tree.
 */
public abstract class WhiboAbstractSplitCondition implements ISplitCondition {

	/** The attribute name. */
	private String attributeName;

	/**
	 * Instantiates a new whibo abstract split condition.
	 * 
	 * @param attributeName
	 *            the attribute name
	 */
	public WhiboAbstractSplitCondition(String attributeName) {
		this.attributeName = attributeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.fon.whibo.integration.interfaces.ISplitCondition#getAttributeName()
	 */
	@Override
	public String getAttributeName() {
		return attributeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return attributeName + " " + getRelation() + " " + getValueString();
	}
}
