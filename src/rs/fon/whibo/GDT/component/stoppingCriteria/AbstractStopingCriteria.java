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

package rs.fon.whibo.GDT.component.stoppingCriteria;

import java.util.List;

import rs.fon.whibo.problem.AbstractComponent;
import rs.fon.whibo.problem.SubproblemParameter;

/**
 * The Class AbstractStopingCrateria that is inherited by all stop criteria
 * components. As an input this class takes Decision tree and state of a tree in
 * progress. As an output this class provides built decision tree that is
 * proceeded to prune tree component.
 * 
 * @author Milan Vukicevic, Milos Jovanovic
 */
public abstract class AbstractStopingCriteria extends AbstractComponent
		implements StoppingCriteria {

	/** The user supplied parameters. */
	List<SubproblemParameter> parameters;

	/**
	 * Instantiates a new abstract stoping criteria.
	 * 
	 * @param parameters
	 *            user supplied parameters
	 */
	public AbstractStopingCriteria(List<SubproblemParameter> parameters) {
		this.parameters = parameters;
	}

}
