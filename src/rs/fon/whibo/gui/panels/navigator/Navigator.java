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

package rs.fon.whibo.gui.panels.navigator;

import rs.fon.whibo.gui.MainPanel;
import rs.fon.whibo.problem.Subproblem;

/**
 * Class for controlling left pane navigation
 * 
 * @author Sasa Mrkela
 */
public class Navigator extends NavigatorPanel {

	public Navigator() {
		super();
	}

	/**
	 * Performs actions when buttons pressed
	 * 
	 */

	@Override
	protected void performAction(Subproblem step) {
		MainPanel.refreshStep(step);
		//
	}

}
