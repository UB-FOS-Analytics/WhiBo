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
package rs.fon.whibo.GC.problem;

import java.util.LinkedList;
import java.util.List;

import rs.fon.whibo.GC.problem.subproblem.DistanceMeasure;
import rs.fon.whibo.GC.problem.subproblem.Evaluation;
import rs.fon.whibo.GC.problem.subproblem.Initialization;
import rs.fon.whibo.GC.problem.subproblem.RecalculateRepresentatives;
import rs.fon.whibo.GC.problem.subproblem.SplitClusters;
import rs.fon.whibo.GC.problem.subproblem.StopCriteria;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.ProblemBuilder;
import rs.fon.whibo.problem.Subproblem;

/**
 * Builds generic clusterer problem with an array of subproblems. This class
 * defines ordered sequence of subproblems that will automatically generate
 * subproblems on GUI.
 * 
 * 
 * @author Milan Vukicevic
 */
public class GenericClustererProblemBuilder extends ProblemBuilder {

	/**
	 * This method builds GenericClusterer problem .
	 * 
	 * @return problem for GUI update
	 */
	@Override
	public Problem buildProblem() {

		Subproblem s1 = new Initialization();
		Subproblem s2 = new DistanceMeasure();
		Subproblem s3 = new RecalculateRepresentatives();
		Subproblem s4 = new StopCriteria();
		Subproblem s5 = new SplitClusters();
		Subproblem s6 = new Evaluation();

		// defines order of subproblems on GUI
		List<Subproblem> steps = new LinkedList<Subproblem>();
		steps.add(s1);
		steps.add(s2);
		steps.add(s3);
		steps.add(s4);
		steps.add(s5);
		steps.add(s6);

		Problem process = new GenericClustererProblem();
		process.setSubproblems(steps);

		return process;
	}
}
