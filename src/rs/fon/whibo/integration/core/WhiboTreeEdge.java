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

import rs.fon.whibo.integration.interfaces.ISplitCondition;
import rs.fon.whibo.integration.interfaces.ITreeEdge;

@SuppressWarnings("serial")
public class WhiboTreeEdge implements ITreeEdge, Serializable,
		Comparable<WhiboTreeEdge> {

	private ISplitCondition condition;
	private WhiboTree child;

	public WhiboTreeEdge(WhiboTree child, ISplitCondition condition) {
		this.child = child;
		this.condition = condition;
	}

	@Override
	public int compareTo(WhiboTreeEdge o) {
		return (this.condition.getRelation() + this.condition.getValueString())
				.compareTo(o.condition.getRelation()
						+ o.condition.getValueString());
	}

	@Override
	public WhiboTree getChild() {
		return this.child;
	}

	@Override
	public ISplitCondition getCondition() {
		return this.condition;
	}

}
